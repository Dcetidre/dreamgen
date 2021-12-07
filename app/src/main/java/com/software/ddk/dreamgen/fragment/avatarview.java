package com.software.ddk.dreamgen.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.software.ddk.dreamgen.activity.Act_Mis;
import com.software.ddk.dreamgen.utils.MapValues;
import com.software.ddk.dreamgen.R;
import com.software.ddk.dreamgen.activity.Act_Items;
import com.software.ddk.dreamgen.activity.Act_Main;
import com.software.ddk.dreamgen.activity.Act_Raw;
import com.software.ddk.dreamgen.activity.Act_Sets;
import com.software.ddk.dreamgen.utils.CustomAdapter;
import com.software.ddk.dreamgen.utils.CustomToast;
import com.software.ddk.dreamgen.utils.TouchImageView;
import com.software.ddk.dreamgen.utils.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;

public class avatarview extends Fragment {

    public TouchImageView iv_avatar;
    private ConstraintLayout cl_container, cl_sets, cl_base_container, cl_basebar, cl_avatarview_menu;
    private ImageView iv_loading, iv_face, iv_body, iv_front, iv_bkg, iv_bodyacc1, iv_bodyacc2, iv_backacc1, iv_backacc2, iv_faceacc1, iv_faceacc2, iv_hairacc1, iv_hairacc2, iv_hair1, iv_hair2;

    public static String[] items_searchstring = new String[9];
    public static boolean[] items_searchstate = {false, false, false, false, false, false, false, false, false};
    public static boolean[] sets_state_lock_list = {false, false, false, false, false, false, false, false, false, false, false, false, false};

    public static ImageButton bt_srv_select;
    private ImageButton ib_menu_removedor, ib_menu_intercamb, ib_menu_locker;
    private ImageButton bt_sharepic, bt_savepic, bt_randomset, bt_selectset, bt_lqlt_visible;

    private TextView tv_setinfo, tv_setstitle;

    //server
    public static String[] a_server = {"us-nizi2d-r53.amz-aws.jp", "app.nizikano-2d.jp"};
    public static String front2, hairacc3, bodyacc3, faceacc3, backacc3, outfit, bkg, hair, hair2, bang, face_eye1, face_eye2, hairacc1, hairacc2, bodyacc1, bodyacc2, faceacc1, faceacc2, backacc1, backacc2, front, face_exp, face_skin, face_tam, face_pup1, face_pup2, face_blush, face_cej;
    public static String setinfo = "Set no guardado.";
    public static String setname = "Default Set";

    public static int[] face_pos = {0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0};
    // notused 0 body 1 bodyacc 2 hairacc 3 faceacc 4 backacc 5 background 6 hair 7 front 8
    public static int[] items_pos = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    public static int server = 0;
    private int render_mode = 0;

    private Bitmap base;
    private MapValues mapas = new MapValues();
    private PopupWindow pop_apariencia, pop_random;
    private ImageView iv_avatar_initload;
    private ImageView iv_toolsmenu;
    private Boolean is_touched = false;
    private Boolean is_menu_open = false;
    private Boolean is_menu_tool_remover = false;
    private Boolean is_menu_tool_exchange = false;
    private Boolean is_menu_tool_locker = false;
    private Boolean is_random_relacional = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_avatarview, parent, false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            on_click_avatar();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        if (Act_Main.navigation != null) {
            Act_Main.navigation.setVisibility(View.GONE);
        }
        cl_container = view.findViewById(R.id.cl_container);
        cl_sets = view.findViewById(R.id.cl_sets);
        cl_base_container = view.findViewById(R.id.cl_base_container);
        cl_basebar = view.findViewById(R.id.cl_basebar);
        cl_avatarview_menu = view.findViewById(R.id.cl_avatarview_menu);
        iv_avatar = view.findViewById(R.id.iv_avatar);
        iv_loading = view.findViewById(R.id.iv_loading);
        iv_avatar_initload = view.findViewById(R.id.iv_avatar_initload);
        bt_sharepic = view.findViewById(R.id.bt_sharepic);
        bt_savepic = view.findViewById(R.id.bt_savepic);
        tv_setinfo = view.findViewById(R.id.tv_setinfo);
        tv_setstitle = view.findViewById(R.id.tv_setstitle);
        bt_lqlt_visible = view.findViewById(R.id.bt_lqlt_visible);
        bt_srv_select = view.findViewById(R.id.bt_srv_select);

        iv_toolsmenu = view.findViewById(R.id.iv_toolsmenu);
        ib_menu_intercamb = view.findViewById(R.id.ib_menu_intercamb);
        ib_menu_removedor = view.findViewById(R.id.ib_menu_removedor);
        ib_menu_locker = view.findViewById(R.id.ib_menu_locker);

        bt_randomset = view.findViewById(R.id.bt_randomset);
        bt_selectset = view.findViewById(R.id.bt_selectset);

        iv_face = view.findViewById(R.id.iv_face);
        iv_body = view.findViewById(R.id.iv_body);
        iv_front = view.findViewById(R.id.iv_front);
        iv_bkg = view.findViewById(R.id.iv_bkg);
        iv_bodyacc1 = view.findViewById(R.id.iv_bodyacc1);
        iv_bodyacc2 = view.findViewById(R.id.iv_bodyacc2);
        iv_backacc1 = view.findViewById(R.id.iv_backacc1);
        iv_backacc2 = view.findViewById(R.id.iv_backacc2);
        iv_faceacc1 = view.findViewById(R.id.iv_faceacc1);
        iv_faceacc2 = view.findViewById(R.id.iv_faceacc2);
        iv_hairacc1 = view.findViewById(R.id.iv_hairacc1);
        iv_hairacc2 = view.findViewById(R.id.iv_hairacc2);
        iv_hair1 = view.findViewById(R.id.iv_hair1);
        iv_hair2 = view.findViewById(R.id.iv_hair2);
        base = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.base), 640, 854, false);

        defaultset();
        initial_load_set(load_dataset2(), iv_avatar);
        load_seticons();

        iv_avatar.setZoom(0.99f);
        iv_avatar.setOnTouchImageViewListener(new TouchImageView.OnTouchImageViewListener() {
            @Override
            public void onMove() {
            }

            @Override
            public void onClick() {
                iv_avatar.setZoom(0.99f);
                on_click_avatar();
            }
        });

        cl_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                on_click_avatar();
                iv_avatar.setZoom(0.99f);
            }
        });

        //iniciar listeners de los botones de sets
        sets_listeners();
        //botones de guardado y share
        bt_savepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.check_storage_permission(getActivity())) {
                    Utils.save_bitmap(getContext(), iv_avatar, Utils.getCapturedFilename());
                }
            }
        });

        bt_sharepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.check_storage_permission(getActivity())) {
                    Utils.share_bitmap(getContext(), iv_avatar);
                }
            }
        });

        bt_lqlt_visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mostrar showpopup
                show_popup_showmode();
            }
        });

        //todo aca iria el serverselect
        bt_srv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mostrar serverpopup
                show_popup_serverselect();
            }
        });

        bt_randomset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), "Manten presionado para generar items.",Toast.LENGTH_SHORT).show();
                //new CustomToast(getContext(), "Manten presionado para generar items.", Toast.LENGTH_SHORT, Gravity.BOTTOM);
                //abrir popup.
                pop_random(view);


            }
        });
        bt_randomset.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                generate_randomset();
                return true;
            }
        });

        bt_selectset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getContext(), Act_Mis.class);
                startActivityForResult(in, 212);
            }
        });
        bt_selectset.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //long click para abrir el menu de herramientas de set.
                //disabled
                //show_menu_tools();
                return true;
            }
        });

        iv_toolsmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_menu_open) {
                    iv_toolsmenu.setColorFilter(Color.argb(255, 255, 255, 255));
                    cl_avatarview_menu.setVisibility(View.GONE);
                    is_menu_open = false;
                } else {
                    iv_toolsmenu.setColorFilter(Color.argb(255, 255, 0, 0));
                    show_menu_tools();
                }
            }
        });

    }

    private void pop_random(View view){
        View layout = LayoutInflater.from(getContext()).inflate(R.layout.layout_pop_query_random, null);
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 110, getResources().getDisplayMetrics()));

        pop_random = new PopupWindow(layout, width, height, true);
        pop_random.setContentView(layout);
        pop_random.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        pop_random.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        pop_random.getContentView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    pop_random.dismiss();
                    return true;
                }
                return false;
            }
        });

        final EditText et_randomword = layout.findViewById(R.id.et_randomword);
        ImageButton bt_start_random = layout.findViewById(R.id.bt_start_random);
        ImageButton bt_random_cancel = layout.findViewById(R.id.bt_random_cancel);
        final CheckBox cb_random_relacionar = layout.findViewById(R.id.cb_random_relacionar);

        is_random_relacional = cb_random_relacionar.isChecked();

        bt_start_random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                is_random_relacional = cb_random_relacionar.isChecked();
                find_random_ids(et_randomword.getText().toString());
                pop_random.dismiss();
            }
        });

        bt_random_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop_random.dismiss();
            }
        });

    }

    private void find_random_ids(String cadena){
        //backacc 1
        //bkg 2
        //bodyacc 3
        //faceacc 4
        //front 5
        //hairs 6
        //hairacc 7
        //outfit 8
        String[][] places = new String[][]{
                {"1", "1"}, {"1", "2"},
                {"2", "1"}, {"3", "1"},
                {"3", "2"}, {"4", "1"},
                {"4", "2"}, {"5", "1"},
                {"6", "1"}, {"6", "2"},
                {"7", "1"}, {"7", "2"},
                {"8", "1"}
        };

        for (String[] place : places) {
            new task_searchstring().execute(cadena, place[0], place[1]);
        }

        //new task_searchstring().execute(cadena, "1", "1");
        //        new task_searchstring().execute(cadena, "1", "2");
        //
        //        new task_searchstring().execute(cadena, "2", "1");
        //
        //        new task_searchstring().execute(cadena, "3", "1");
        //        new task_searchstring().execute(cadena, "3", "2");
        //
        //        new task_searchstring().execute(cadena, "4", "1");
        //        new task_searchstring().execute(cadena, "4", "2");
        //
        //        new task_searchstring().execute(cadena, "5", "1");
        //
        //        new task_searchstring().execute(cadena, "6", "1");
        //        new task_searchstring().execute(cadena, "6", "2");
        //
        //        new task_searchstring().execute(cadena, "7", "1");
        //        new task_searchstring().execute(cadena, "7", "2");
        //
        //        new task_searchstring().execute(cadena, "8", "1");

    }


    private void show_menu_tools() {
        //menu tools
        cl_avatarview_menu.setVisibility(View.VISIBLE);
        is_menu_open = true;

        //listeners
        ib_menu_removedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //removedor
                if (is_menu_tool_remover){
                    is_menu_tool_remover = false;
                    ib_menu_removedor.setColorFilter(Color.argb(255, 0, 0, 0));
                } else if (!is_menu_tool_exchange && !is_menu_tool_locker){
                    is_menu_tool_remover = true;
                    ib_menu_removedor.setColorFilter(Color.argb(255, 255, 0, 0));
                }
            }
        });

        ib_menu_intercamb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intercambiador
                if (is_menu_tool_exchange){
                    is_menu_tool_exchange = false;
                    ib_menu_intercamb.setColorFilter(Color.argb(255, 0,0,0));
                } else if (!is_menu_tool_remover && !is_menu_tool_locker){
                    is_menu_tool_exchange = true;
                    ib_menu_intercamb.setColorFilter(Color.argb(255, 255,0,0));
                }
            }
        });
        ib_menu_locker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //bloqueador
                if (is_menu_tool_locker){
                    is_menu_tool_locker = false;
                    ib_menu_locker.setColorFilter(Color.argb(255, 0, 0, 0));
                } else if (!is_menu_tool_exchange && !is_menu_tool_remover) {
                    is_menu_tool_locker = true;
                    ib_menu_locker.setColorFilter(Color.argb(255, 255, 0, 0));
                }
            }
        });

    }

    private void show_popup_serverselect() {
        PopupMenu popup = new PopupMenu(getContext(), bt_srv_select);
        popup.getMenuInflater().inflate(R.menu.server, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch ((String) menuItem.getTitle()) {
                    case "USA server":
                        server = 0;
                        bt_srv_select.setImageResource(R.drawable.usa_server);
                        break;
                    case "JP server":
                        server = 1;
                        bt_srv_select.setImageResource(R.drawable.jp_server);
                        break;
                }
                load_avatar_hd_set(iv_avatar);
                return true;
            }
        });
        popup.show();
    }

    private void show_popup_showmode() {
        PopupMenu popup = new PopupMenu(getContext(), bt_lqlt_visible);
        popup.getMenuInflater().inflate(R.menu.showmode, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch ((String) menuItem.getTitle()) {
                    case "Render Rápido":
                        render_mode = 0;
                        render_icon_mode(0);
                        break;
                    case "Render HD":
                        render_mode = 1;
                        render_icon_mode(1);
                        break;
                    case "Solo Fondo":
                        render_mode = 2;
                        render_icon_mode(2);
                        break;
                    case "Solo Avatar":
                        render_mode = 3;
                        render_icon_mode(3);
                        break;
                }
                load_avatar_hd_set(iv_avatar);
                return true;
            }
        });
        popup.show();
    }

    private void render_icon_mode(int render_mode){
        switch (render_mode) {
            case 0:
                bt_lqlt_visible.setImageResource(R.drawable.ic_layers_black_24dp);
                break;
            case 1:
                bt_lqlt_visible.setImageResource(R.drawable.ic_hd_black_24dp);
                break;
            case 2:
                bt_lqlt_visible.setImageResource(R.drawable.ic_layers_black_24dp);
                break;
            case 3:
                bt_lqlt_visible.setImageResource(R.drawable.ic_layers_black_24dp);
                break;
        }
    }

    private void defaultset() {
        server = 0;
        outfit = "0";
        bkg = "103";
        hair = "1308";
        hair2 = "0";
        bang = "1308";
        face_eye1 = "749"; //7
        face_eye2 = "749";
        hairacc1 = "0";
        hairacc2 = "0";
        bodyacc1 = "0";
        bodyacc2 = "0";
        faceacc1 = "0";
        faceacc2 = "0";
        backacc1 = "0";
        backacc2 = "0";
        front = "0";
        //extras
        front2 = "0";
        hairacc3 = "0";
        bodyacc3 = "0";
        faceacc3 = "0";
        backacc3 = "0";

        face_exp = "10"; //0
        face_skin = "11"; //1
        face_tam = "11"; //2
        face_pup1 = "0";
        face_pup2 = "0";
        face_blush = "01"; //3
        face_cej = "01"; //4

    }

    private void generate_randomset() {
        Random r = new Random();
        if (!sets_state_lock_list[0]){
            outfit = Act_Main.list_outfits[r.nextInt(Act_Main.list_outfits.length)];
        }
        if (!sets_state_lock_list[1]){
            bodyacc1 = Act_Main.list_bodyacc[r.nextInt(Act_Main.list_bodyacc.length)];
        }
        if (!sets_state_lock_list[2]){
            bodyacc2 = Act_Main.list_bodyacc[r.nextInt(Act_Main.list_bodyacc.length)];
        }
        if (!sets_state_lock_list[3]){
            hairacc1 = Act_Main.list_hats[r.nextInt(Act_Main.list_hats.length)];
        }
        if (!sets_state_lock_list[4]){
            hairacc2 = Act_Main.list_hats[r.nextInt(Act_Main.list_hats.length)];
        }
        if (!sets_state_lock_list[5]){
            faceacc1 = Act_Main.list_face_acc[r.nextInt(Act_Main.list_face_acc.length)];
        }
        if (!sets_state_lock_list[6]){
            faceacc2 = Act_Main.list_face_acc[r.nextInt(Act_Main.list_face_acc.length)];
        }
        if (!sets_state_lock_list[7]){
            backacc1 = Act_Main.list_back_acc[r.nextInt(Act_Main.list_back_acc.length)];
        }
        if (!sets_state_lock_list[8]){
            backacc2 = Act_Main.list_back_acc[r.nextInt(Act_Main.list_back_acc.length)];
        }
        if (!sets_state_lock_list[9]){
            bkg = Act_Main.list_bkg[r.nextInt(Act_Main.list_bkg.length)];
        }
        if (!sets_state_lock_list[10]){
            hair = Act_Main.list_hairs[r.nextInt(Act_Main.list_hairs.length)];
        }
        if (!sets_state_lock_list[11]){
            hair2 = Act_Main.list_hairs[r.nextInt(Act_Main.list_hairs.length)];
        }
        if (!sets_state_lock_list[12]){
            front = Act_Main.list_front[r.nextInt(Act_Main.list_front.length)];
        }

        load_seticons();
        load_avatar_hd_set(iv_avatar);

    }

    private void sets_listeners() {
        iv_face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //iniciaría el activity o fragment o popupwindow de los parametros del rostro.
                pop_apariencia(view);
            }
        });
        iv_body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_items_act(1, 1, outfit, false);
            }
        });
        iv_bodyacc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_items_act(2, 1, bodyacc1, false);
            }
        });
        iv_bodyacc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_items_act(2, 2, bodyacc2, false);
            }
        });
        iv_hairacc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_items_act(3, 1, hairacc1, false);
            }
        });
        iv_hairacc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_items_act(3, 2, hairacc2, false);
            }
        });
        iv_faceacc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_items_act(4, 1, faceacc1, false);
            }
        });
        iv_faceacc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_items_act(4, 2, faceacc2, false);
            }
        });
        iv_backacc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_items_act(5, 1, backacc1, false);
            }
        });
        iv_backacc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_items_act(5, 2, backacc2, false);
            }
        });
        iv_bkg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_items_act(6, 1, bkg, false);
            }
        });
        iv_hair1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_items_act(7, 1, hair, false);
            }
        });
        iv_hair2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_items_act(7, 2, hair2, false);
            }
        });
        iv_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_items_act(8, 1, front, false);
            }
        });
        sets_long_listeners();
    }

    private void sets_long_listeners() {
        iv_body.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                start_items_act(1, 1, outfit, true);
                return true;
            }
        });
        iv_bodyacc1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                start_items_act(2, 1, bodyacc1, true);
                return true;
            }
        });
        iv_bodyacc2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                start_items_act(2, 2, bodyacc2, true);
                return true;
            }
        });
        iv_hairacc1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                start_items_act(3, 1, hairacc1, true);
                return true;
            }
        });

        iv_hairacc2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                start_items_act(3, 2, hairacc2, true);
                return true;
            }
        });

        iv_faceacc1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                start_items_act(4, 1, faceacc1, true);
                return true;
            }
        });

        iv_faceacc2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                start_items_act(4, 2, faceacc2, true);
                return true;
            }
        });
        iv_backacc1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                start_items_act(5, 1, backacc1, true);
                return true;
            }
        });
        iv_backacc2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                start_items_act(5, 2, backacc2, true);
                return true;
            }
        });

        iv_bkg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                start_items_act(6, 1, bkg, true);
                return true;
            }
        });

        iv_hair1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                start_items_act(7, 1, hair, true);
                return true;
            }
        });
        iv_hair2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                start_items_act(7, 2, hair2, true);
                return true;
            }
        });
        iv_front.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                start_items_act(8, 1, front, true);
                return true;
            }
        });
    }

    private void start_items_act(int item_type, int item_kind, String selected_item, boolean positem) {
        if (is_menu_tool_remover && !is_menu_tool_exchange && !is_menu_tool_locker) {
            //si esta activo el removedor de items, agregar un item 0
            remove_item(item_type, item_kind);

        } else if (is_menu_tool_exchange && !is_menu_tool_remover && !is_menu_tool_locker) {
            //si esta activo intercambiar items de lugar.
            exchange_item(item_type, item_kind);

        } else if (is_menu_tool_locker && !is_menu_tool_exchange && !is_menu_tool_remover) {
            //si esta desbloqueado bloquear, y viceversa.
            if (is_locked(item_type, item_kind)){
                //desbloquear
                lock_item(item_type, item_kind, false);
            } else {
                //bloquear
                lock_item(item_type, item_kind, true);
            }

        } else {
            Intent in = new Intent(getContext(), Act_Items.class);
            in.putExtra("item_type", item_type);
            in.putExtra("item_kind", item_kind);
            in.putExtra("item_selected", selected_item);
            in.putExtra("item_positem", positem);
            startActivityForResult(in, 200);
        }
    }

    public static boolean is_locked(int item_type, int item_kind){
        switch (item_type){
            case 1:
                return sets_state_lock_list[0];
            case 2:
                if (item_kind == 1){
                    return sets_state_lock_list[1];
                } else {
                    return sets_state_lock_list[2];
                }
            case 3:
                if (item_kind == 1){
                    return sets_state_lock_list[3];
                } else {
                    return sets_state_lock_list[4];
                }
            case 4:
                if (item_kind == 1){
                    return sets_state_lock_list[5];
                } else {
                    return sets_state_lock_list[6];
                }
            case 5:
                if (item_kind == 1){
                    return sets_state_lock_list[7];
                } else {
                    return sets_state_lock_list[8];
                }
            case 6:
                return sets_state_lock_list[9];
            case 7:
                if (item_kind == 1){
                    return sets_state_lock_list[10];
                } else {
                    return sets_state_lock_list[11];
                }
            case 8:
                return sets_state_lock_list[12];
        }

        return false;
    }

    private void lock_item(int item_type, int item_kind, boolean lock){

        switch (item_type){
            case 1:
                sets_state_lock_list[0] = lock;
                break;
            case 2:
                if (item_kind == 1){
                    sets_state_lock_list[1] = lock;
                } else {
                    sets_state_lock_list[2] = lock;
                }
                break;
            case 3:
                if (item_kind == 1){
                    sets_state_lock_list[3] = lock;
                } else {
                    sets_state_lock_list[4] = lock;
                }
                break;
            case 4:
                if (item_kind == 1){
                    sets_state_lock_list[5] = lock;
                } else {
                    sets_state_lock_list[6] = lock;
                }
                break;
            case 5:
                if (item_kind == 1){
                    sets_state_lock_list[7] = lock;
                } else {
                    sets_state_lock_list[8] = lock;
                }
                break;
            case 6:
                sets_state_lock_list[9] = lock;
                break;
            case 7:
                if (item_kind == 1){
                    sets_state_lock_list[10] = lock;
                } else {
                    sets_state_lock_list[11] = lock;
                }
                break;
            case 8:
                sets_state_lock_list[12] = lock;
                break;
        }

        //actualizar iconos.
        load_seticons();

    }

    private void remove_item(int item_type, int item_kind){
        switch (item_type){
            case 1:
                outfit = "0";
                break;
            case 2:
                if (item_kind == 1){
                    bodyacc1 = "0";
                } else {
                    bodyacc2 = "0";
                }
                break;
            case 3:
                if (item_kind == 1){
                    hairacc1 = "0";
                } else {
                    hairacc2 = "0";
                }
                break;
            case 4:
                if (item_kind == 1){
                    faceacc1 = "0";
                } else {
                    faceacc2 = "0";
                }
                break;
            case 5:
                if (item_kind == 1){
                    backacc1 = "0";
                } else {
                    backacc2 = "0";
                }
                break;
            case 6:
                bkg = "0";
                break;
            case 7:
                if (item_kind == 1){
                    hair = "0";
                } else {
                    hair2 = "0";
                }
                break;
            case 8:
                front = "0";
                break;
        }

        //update all
        load_avatar_set(load_dataset2(), iv_avatar);
        load_seticons();
        Act_Main.UPDATE_DESIGN_VIEW = true;
    }

    private void exchange_item(int item_type, int item_kind){
        String[] item_dt = new String[]{"-","-"};
        switch (item_type){
            case 1:
                break;
            case 2:
                item_dt = new String[]{bodyacc1, bodyacc2};
                bodyacc1 = item_dt[1];
                bodyacc2 = item_dt[0];
                break;
            case 3:
                item_dt = new String[]{hairacc1, hairacc2};
                hairacc1 = item_dt[1];
                hairacc2 = item_dt[0];
                break;
            case 4:
                item_dt = new String[]{faceacc1, faceacc2};
                faceacc1 = item_dt[1];
                faceacc2 = item_dt[0];

                break;
            case 5:
                item_dt = new String[]{backacc1, backacc2};
                backacc1 = item_dt[1];
                backacc2 = item_dt[0];
                break;
            case 6:
                break;
            case 7:
                item_dt = new String[]{hair, hair2};
                hair = item_dt[1];
                hair2 = item_dt[0];
                break;
            case 8:
                break;
        }
        //update all
        load_avatar_set(load_dataset2(), iv_avatar);
        load_seticons();
        Act_Main.UPDATE_DESIGN_VIEW = true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 200) {
            if (resultCode == RESULT_OK) {
                //al seleccionar un item en la lista de items.
                setinfo = "Set no guardado.";
                load_avatar_hd_set(iv_avatar);
                load_seticons();
                Act_Main.UPDATE_DESIGN_VIEW = true;
            }
        }

        if (requestCode == 201) {
            if (resultCode == RESULT_OK) {
                //al aceptar cambios en el raw.
                setinfo = "Set no guardado.";
                //load_avatar_hd_set(iv_avatar);
                load_avatar_set(load_dataset2(),iv_avatar);
                load_seticons();
                Act_Main.UPDATE_DESIGN_VIEW = true;
            }
        }

        if (requestCode == 212){
            if (resultCode == RESULT_OK){
                //al seleccionar un conjunto en la lista de conjuntos.
                load_avatar_hd_set(iv_avatar);
                load_seticons();
                Act_Main.UPDATE_DESIGN_VIEW = true;
            }
            if (resultCode == 144){
                //al seleccionar un diseño intentar abrir el designerview.
                Act_Main.navigation.setSelectedItemId(R.id.navigation_live2d);
            }
        }

    }

    private void pop_apariencia(View v){
        //desactivar el detector de touch para que no resetee los valores del raw.
        is_touched = false;
        try {
            View layout = LayoutInflater.from(getContext()).inflate(R.layout.layout_pop_apariencia, null);
            int winHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
            pop_apariencia = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, winHeight, true);
            pop_apariencia.setContentView(layout);
            pop_apariencia.showAtLocation(v, Gravity.TOP, 0, 0);
            pop_apariencia.setOutsideTouchable(true);
            pop_apariencia.setFocusable(true);
            pop_apariencia.getContentView().setFocusableInTouchMode(true);
            pop_apariencia.getContentView().setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        pop_apariencia.dismiss();
                        return true;
                    }
                    return false;
                }
            });

            //cosas del popup
            final ImageView iv_item_preview = layout.findViewById(R.id.iv_item_preview);

            LinearLayout bt_random, bt_select, bt_rawdata;
            bt_random = layout.findViewById(R.id.bt_random);
            bt_select = layout.findViewById(R.id.bt_select);
            bt_rawdata = layout.findViewById(R.id.bt_rawdata);

            final EditText et_eye1, et_eye2, et_pup1, et_pup2;
            et_eye1 = layout.findViewById(R.id.et_eye1);
            et_eye2 = layout.findViewById(R.id.et_eye2);
            et_pup1 = layout.findViewById(R.id.et_pup1);
            et_pup2 = layout.findViewById(R.id.et_pup2);

            final Spinner sp_rostro, sp_color_ojos, sp_expresion, sp_blush, sp_tono_piel, sp_altura, sp_tono_bellos, sp_pup_tipo, sp_pup_color, sp_pup_tam, sp_pup_gradiente;
            sp_rostro = layout.findViewById(R.id.sp_rostro);
            sp_color_ojos = layout.findViewById(R.id.sp_color_ojos);
            sp_expresion = layout.findViewById(R.id.sp_expresion);
            sp_blush = layout.findViewById(R.id.sp_blush);
            sp_tono_piel = layout.findViewById(R.id.sp_tono_piel);
            sp_altura = layout.findViewById(R.id.sp_altura);
            sp_tono_bellos = layout.findViewById(R.id.sp_tono_bellos);
            sp_pup_tipo = layout.findViewById(R.id.sp_pup_tipo);
            sp_pup_color = layout.findViewById(R.id.sp_pup_color);
            sp_pup_tam = layout.findViewById(R.id.sp_pup_tam);
            sp_pup_gradiente = layout.findViewById(R.id.sp_pup_gradiente);

            //declarar adapters y spinners
            et_eye1.setText(face_eye1);
            et_eye2.setText(face_eye2);
            et_pup1.setText(face_pup1);
            et_pup2.setText(face_pup2);

            //carga de valores default.
            MapValues mapv = new MapValues();

            //carga de adapters custom
            sp_rostro.setAdapter(new CustomAdapter(getContext(), mapv.getArr_eface_drw(), mapv.getArr_eface(), 2));
            sp_pup_tipo.setAdapter(new CustomAdapter(getContext(), mapv.getPupils_drw(), mapv.getPupils(), 2));
            sp_pup_gradiente.setAdapter(new CustomAdapter(getContext(), mapv.getPupil_gradient_drw(), mapv.getPupil_gradient(), 2));

            //carga de datos iniciales
            sp_expresion.setSelection(face_pos[0]);
            sp_tono_piel.setSelection(face_pos[1]);
            sp_altura.setSelection(face_pos[2]);
            sp_blush.setSelection(face_pos[3]);
            sp_tono_bellos.setSelection(face_pos[4]);
            sp_rostro.setSelection(face_pos[5]);
            sp_color_ojos.setSelection(face_pos[6]);
            sp_pup_tipo.setSelection(face_pos[7]);
            sp_pup_color.setSelection(face_pos[8]);
            sp_pup_tam.setSelection(face_pos[9]);
            sp_pup_gradiente.setSelection(face_pos[10]);

            //carga de vista previa
            loadface_preview(iv_item_preview);

            //touch listener
            View.OnTouchListener touch_listener = new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    is_touched = true;
                    Act_Main.UPDATE_DESIGN_VIEW = true;
                    return false;
                }
            };

            sp_rostro.setOnTouchListener(touch_listener);
            sp_rostro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (is_touched){
                        //eyes
                        face_eye1 = mapas.getFacemap()[sp_rostro.getSelectedItemPosition()][sp_color_ojos.getSelectedItemPosition()];
                        face_eye2 = mapas.getFacemap()[sp_rostro.getSelectedItemPosition()][sp_color_ojos.getSelectedItemPosition()];
                        face_pos[5] = sp_rostro.getSelectedItemPosition();
                        face_pos[6] = sp_color_ojos.getSelectedItemPosition();

                        et_eye1.setText(face_eye1);
                        et_eye2.setText(face_eye2);
                        et_pup1.setText(face_pup1);
                        et_pup2.setText(face_pup2);

                        loadface_preview(iv_item_preview);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });

            sp_color_ojos.setOnTouchListener(touch_listener);
            sp_color_ojos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (is_touched){
                        face_eye1 = mapas.getFacemap()[sp_rostro.getSelectedItemPosition()][sp_color_ojos.getSelectedItemPosition()];
                        face_eye2 = mapas.getFacemap()[sp_rostro.getSelectedItemPosition()][sp_color_ojos.getSelectedItemPosition()];
                        face_pos[5] = sp_rostro.getSelectedItemPosition();
                        face_pos[6] = sp_color_ojos.getSelectedItemPosition();

                        et_eye1.setText(face_eye1);
                        et_eye2.setText(face_eye2);
                        et_pup1.setText(face_pup1);
                        et_pup2.setText(face_pup2);

                        loadface_preview(iv_item_preview);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });

            sp_expresion.setOnTouchListener(touch_listener);
            sp_expresion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (is_touched){
                        //expresiones
                        face_exp = mapas.getArr_expressions()[sp_expresion.getSelectedItemPosition()][1];
                        face_pos[0] = sp_expresion.getSelectedItemPosition();
                        loadface_preview(iv_item_preview);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });

            sp_blush.setOnTouchListener(touch_listener);
            sp_blush.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (is_touched){
                        //blush
                        face_blush = mapas.getArr_blush()[sp_blush.getSelectedItemPosition()][1];
                        face_pos[3] = sp_blush.getSelectedItemPosition();
                        loadface_preview(iv_item_preview);

                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });

            sp_tono_piel.setOnTouchListener(touch_listener);
            sp_tono_piel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (is_touched){
                        //piel
                        face_skin = mapas.getArr_skin()[sp_tono_piel.getSelectedItemPosition()][1];
                        face_pos[1] = sp_tono_piel.getSelectedItemPosition();
                        loadface_preview(iv_item_preview);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });

            sp_altura.setOnTouchListener(touch_listener);
            sp_altura.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (is_touched) {
                        //altura
                        face_tam = mapas.getArr_tam()[sp_altura.getSelectedItemPosition()][1];
                        face_pos[2] = sp_altura.getSelectedItemPosition();
                        loadface_preview(iv_item_preview);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            sp_tono_bellos.setOnTouchListener(touch_listener);
            sp_tono_bellos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (is_touched) {
                        //cej
                        face_cej = mapas.getCej_color()[sp_tono_bellos.getSelectedItemPosition()][1];
                        face_pos[4] = sp_tono_bellos.getSelectedItemPosition();
                        loadface_preview(iv_item_preview);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            sp_pup_tipo.setOnTouchListener(touch_listener);
            sp_pup_tipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (is_touched) {
                        //pupils
                        face_pup1 = mapas.getPupilmap()[sp_pup_tipo.getSelectedItemPosition()][sp_pup_color.getSelectedItemPosition()];
                        face_pup2 = mapas.getPupilmap()[sp_pup_tipo.getSelectedItemPosition()][sp_pup_color.getSelectedItemPosition()];
                        face_pos[7] = sp_pup_tipo.getSelectedItemPosition();
                        face_pos[8] = sp_pup_color.getSelectedItemPosition();
                        face_pos[9] = sp_pup_tam.getSelectedItemPosition();
                        face_pos[10] = sp_pup_gradiente.getSelectedItemPosition();
                        //pup_tamygrad
                        int rpup = update_r_pups(Integer.valueOf(face_pup1), sp_pup_tam.getSelectedItemPosition(), sp_pup_gradiente.getSelectedItemPosition());
                        face_pup1 = String.valueOf(rpup);
                        face_pup2 = String.valueOf(rpup);

                        et_eye1.setText(face_eye1);
                        et_eye2.setText(face_eye2);
                        et_pup1.setText(face_pup1);
                        et_pup2.setText(face_pup2);
                        loadface_preview(iv_item_preview);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            sp_pup_color.setOnTouchListener(touch_listener);
            sp_pup_color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (is_touched) {
                        //pupils
                        face_pup1 = mapas.getPupilmap()[sp_pup_tipo.getSelectedItemPosition()][sp_pup_color.getSelectedItemPosition()];
                        face_pup2 = mapas.getPupilmap()[sp_pup_tipo.getSelectedItemPosition()][sp_pup_color.getSelectedItemPosition()];
                        face_pos[7] = sp_pup_tipo.getSelectedItemPosition();
                        face_pos[8] = sp_pup_color.getSelectedItemPosition();
                        face_pos[9] = sp_pup_tam.getSelectedItemPosition();
                        face_pos[10] = sp_pup_gradiente.getSelectedItemPosition();
                        //pup_tamygrad
                        int rpup = update_r_pups(Integer.valueOf(face_pup1), sp_pup_tam.getSelectedItemPosition(), sp_pup_gradiente.getSelectedItemPosition());
                        face_pup1 = String.valueOf(rpup);
                        face_pup2 = String.valueOf(rpup);

                        et_eye1.setText(face_eye1);
                        et_eye2.setText(face_eye2);
                        et_pup1.setText(face_pup1);
                        et_pup2.setText(face_pup2);
                        loadface_preview(iv_item_preview);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            sp_pup_tam.setOnTouchListener(touch_listener);
            sp_pup_tam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (is_touched) {
                        //pupils
                        face_pup1 = mapas.getPupilmap()[sp_pup_tipo.getSelectedItemPosition()][sp_pup_color.getSelectedItemPosition()];
                        face_pup2 = mapas.getPupilmap()[sp_pup_tipo.getSelectedItemPosition()][sp_pup_color.getSelectedItemPosition()];
                        face_pos[7] = sp_pup_tipo.getSelectedItemPosition();
                        face_pos[8] = sp_pup_color.getSelectedItemPosition();
                        face_pos[9] = sp_pup_tam.getSelectedItemPosition();
                        face_pos[10] = sp_pup_gradiente.getSelectedItemPosition();
                        //pup_tamygrad
                        int rpup = update_r_pups(Integer.valueOf(face_pup1), sp_pup_tam.getSelectedItemPosition(), sp_pup_gradiente.getSelectedItemPosition());
                        face_pup1 = String.valueOf(rpup);
                        face_pup2 = String.valueOf(rpup);

                        et_eye1.setText(face_eye1);
                        et_eye2.setText(face_eye2);
                        et_pup1.setText(face_pup1);
                        et_pup2.setText(face_pup2);
                        loadface_preview(iv_item_preview);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            sp_pup_gradiente.setOnTouchListener(touch_listener);
            sp_pup_gradiente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (is_touched) {
                        //pupils
                        face_pup1 = mapas.getPupilmap()[sp_pup_tipo.getSelectedItemPosition()][sp_pup_color.getSelectedItemPosition()];
                        face_pup2 = mapas.getPupilmap()[sp_pup_tipo.getSelectedItemPosition()][sp_pup_color.getSelectedItemPosition()];
                        face_pos[7] = sp_pup_tipo.getSelectedItemPosition();
                        face_pos[8] = sp_pup_color.getSelectedItemPosition();
                        face_pos[9] = sp_pup_tam.getSelectedItemPosition();
                        face_pos[10] = sp_pup_gradiente.getSelectedItemPosition();
                        //pup_tamygrad
                        int rpup = update_r_pups(Integer.valueOf(face_pup1), sp_pup_tam.getSelectedItemPosition(), sp_pup_gradiente.getSelectedItemPosition());
                        face_pup1 = String.valueOf(rpup);
                        face_pup2 = String.valueOf(rpup);

                        et_eye1.setText(face_eye1);
                        et_eye2.setText(face_eye2);
                        et_pup1.setText(face_pup1);
                        et_pup2.setText(face_pup2);
                        loadface_preview(iv_item_preview);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            bt_random.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Act_Main.UPDATE_DESIGN_VIEW = true;

                    Random r = new Random();
                    MapValues val = new MapValues();
                    //int r_exp = r.nextInt(val.getArr_expressions().length);
                    //face_exp = val.getArr_expressions()[r_exp][1];
                    int r_skin = r.nextInt(val.getArr_skin().length);
                    face_skin = val.getArr_skin()[r_skin][1];
                    int r_tam = r.nextInt(val.getArr_tam().length);
                    face_tam = val.getArr_tam()[r_tam][1];
                    int r_blush = r.nextInt(val.getArr_blush().length);
                    face_blush = val.getArr_blush()[r_blush][1];
                    int r_cej = r.nextInt(val.getCej_color().length);
                    face_cej = val.getCej_color()[r_cej][1];

                    int r_ftype = r.nextInt(val.getArr_eface().length);
                    int r_fcolor = r.nextInt(val.getArr_ecolor().length);
                    face_eye1 = val.getFacemap()[r_ftype][r_fcolor];
                    face_eye2 = val.getFacemap()[r_ftype][r_fcolor];

                    int r_ptype = r.nextInt(val.getPupils().length);
                    int r_pcolor = r.nextInt(val.getPupil_colors().length);
                    int r_ptam = r.nextInt(val.getPupil_tam().length);
                    int r_pgrad = r.nextInt(val.getPupil_gradient().length);
                    int pup_id = Integer.valueOf(val.getPupilmap()[r_ptype][r_pcolor]);
                    face_pup1 = String.valueOf(update_r_pups(pup_id, r_ptam, r_pgrad));
                    face_pup2 = String.valueOf(update_r_pups(pup_id, r_ptam, r_pgrad));

                    et_eye1.setText(face_eye1);
                    et_eye2.setText(face_eye2);
                    et_pup1.setText(face_pup1);
                    et_pup2.setText(face_pup2);

                    //sp_expresion.setSelection(r_exp);
                    sp_tono_piel.setSelection(r_skin);
                    sp_altura.setSelection(r_tam);
                    sp_blush.setSelection(r_blush);
                    sp_tono_bellos.setSelection(r_cej);

                    sp_rostro.setSelection(r_ftype);
                    sp_color_ojos.setSelection(r_fcolor);
                    sp_pup_tipo.setSelection(r_ptype);
                    sp_pup_color.setSelection(r_pcolor);
                    sp_pup_tam.setSelection(r_ptam);
                    sp_pup_gradiente.setSelection(r_pgrad);

                    //face_pos[0] = sp_expresion.getSelectedItemPosition();
                    face_pos[1] = sp_tono_piel.getSelectedItemPosition();
                    face_pos[2] = sp_altura.getSelectedItemPosition();
                    face_pos[3] = sp_blush.getSelectedItemPosition();
                    face_pos[4] = sp_tono_bellos.getSelectedItemPosition();
                    face_pos[5] = sp_rostro.getSelectedItemPosition();
                    face_pos[6] = sp_color_ojos.getSelectedItemPosition();
                    face_pos[7] = sp_pup_tipo.getSelectedItemPosition();
                    face_pos[8] = sp_pup_color.getSelectedItemPosition();
                    face_pos[9] = sp_pup_tam.getSelectedItemPosition();
                    face_pos[10] = sp_pup_gradiente.getSelectedItemPosition();

                    loadface_preview(iv_item_preview);

                }
            });

            bt_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //face_exp = "10"; //0
                    //face_skin = "10"; //1
                    //face_tam = "10"; //2
                    //face_blush = "0"; //3
                    //face_cej = "0"; //4
                    //face_type //5
                    //face_eyecolor //6
                    //face_pup_type //7
                    //face_pup_color //8
                    //face_pup_tam //9
                    //face_pup_grad //10
                    pop_apariencia.dismiss();
                    load_avatar_hd_set(iv_avatar);
                    load_set("http://" + a_server[server] + "/img/avatar_thum/face/" + face_eye1 + ".png", iv_face);
                }
            });

            bt_rawdata.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(getContext(), Act_Raw.class);
                    startActivityForResult(in, 201);
                    pop_apariencia.dismiss();
                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private ArrayAdapter<String> create_adapter(String[] array){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private void loadface_preview(ImageView iv){
        String url_preview = "http://"+ a_server[server] +"/img/php/face_param.php?&id1=" + generate_ids(2,front) + ","+ generate_ids(1, outfit) + "," + generate_ids(1, bodyacc1)+ ","+generate_ids(1, bodyacc2)+","+generate_ids(1, hairacc1)+","+generate_ids(1, hairacc2)+","+generate_ids(1, faceacc1)+","+generate_ids(1, faceacc2)+","+generate_ids(1, backacc1)+","+generate_ids(1, backacc2)+","+generate_ids(1, bkg)+","+generate_ids(1, hair)+","+generate_ids(1, face_eye1)+","+generate_ids(1, face_eye2)+","+generate_ids(1, hair2)+"," + generate_ids(1, front2) + "," + generate_ids(1, bodyacc3) + "," + generate_ids(1, hairacc3) + "," + generate_ids(1, faceacc3) + "," + generate_ids(1, backacc3) + "," + generate_ids(1, face_pup1)+","+generate_ids(1, face_pup2)+",x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,"+generate_ids(4, face_cej)+"&id2="+generate_ids(3,face_skin)+"&id3="+generate_ids(3,face_tam)+"&id4="+generate_ids(3,face_exp)+"&id5="+generate_ids(3,face_blush)+"&id6=0x1&id7=0x1";
        Picasso.get()
                .load(url_preview)
                .noFade()
                .placeholder(R.drawable.progress_animation2)
                .into(iv);
    }

    private int update_r_pups(int pupil_id, int pupil_tm, int pupil_grad){

        if (pupil_id != 0){
            switch (pupil_tm){
                case 0:
                    break;
                case 1:
                    pupil_id = pupil_id + 1;
                    break;
                case 2:
                    pupil_id = pupil_id + 2;
                    break;
            }
            switch (pupil_grad){
                case 0:
                    break;
                case 1:
                    pupil_id = pupil_id + 100000;
                    break;
                case 2:
                    pupil_id = pupil_id + 200000;
                    break;
                case 3:
                    pupil_id = pupil_id + 300000;
                    break;
                case 4:
                    pupil_id = pupil_id + 400000;
                    break;
                case 5:
                    pupil_id = pupil_id + 500000;
                    break;
            }
        }

        return pupil_id;
    }

    private String load_dataset(){
        bang = hair;
        String url = "http://"+ a_server[server] +"/img/php/all_param2.php?&id1=" + generate_ids(2,front) + ","+ generate_ids(1, outfit) + "," + generate_ids(1, bodyacc1)+ ","+generate_ids(1, bodyacc2)+","+generate_ids(1, hairacc1)+","+generate_ids(1, hairacc2)+","+generate_ids(1, faceacc1)+","+generate_ids(1, faceacc2)+","+generate_ids(1, backacc1)+","+generate_ids(1, backacc2)+","+generate_ids(1, bkg)+","+generate_ids(1, hair)+","+generate_ids(1, face_eye1)+","+generate_ids(1, face_eye2)+","+generate_ids(1, hair2)+"," + generate_ids(1, front) + "," + generate_ids(1, bodyacc3) + "," + generate_ids(1, hairacc3) + "," + generate_ids(1, faceacc3) + "," + generate_ids(1, backacc3) + ","+generate_ids(1, face_pup1)+","+generate_ids(1, face_pup2)+",x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,"+generate_ids(4, face_cej)+"&id2="+generate_ids(3,face_skin)+"&id3="+generate_ids(3,face_tam)+"&id4="+generate_ids(3,face_exp)+"&id5="+generate_ids(3,face_blush)+"&id6=0x1&id7=0x1";
        return url;
    }

    public String generate_ids(int type, String id){
        switch (type){
            case 1:
                //format x1x2x3x4x
                char[] chars = id.toCharArray();
                String gen = "x";
                for (char aChar : chars) {
                    gen = gen + String.valueOf(aChar) + "x";
                }
                return gen;
            case 2:
                //format 1x2x3x4x
                char[] chars2 = id.toCharArray();
                String gen2 = "";
                for (char aChar : chars2) {
                    gen2 = gen2 + String.valueOf(aChar) + "x";
                }
                return gen2;
            case 3:
                //format 1x2x3x4
                char[] chars3 = id.toCharArray();
                String gen3 = "";
                for (char aChar : chars3) {
                    gen3 = gen3 + String.valueOf(aChar) + "x";
                }
                gen3 = gen3.substring(0,gen3.length() - 1);
                return gen3;
            case 4:
                //format x1x2x3x4
                char[] chars4 = id.toCharArray();
                String gen4 = "x";
                for (char aChar : chars4) {
                    gen4 = gen4 + String.valueOf(aChar) + "x";
                }
                gen4 = gen4.substring(0,gen4.length() - 1);
                return gen4;
            case 5:
                //format 1x2x3x4x1
                char[] chars5 = id.toCharArray();
                String gen5 = "";
                for (char aChar : chars5) {
                    gen5 = gen5 + String.valueOf(aChar) + "x";
                }
                return gen5.concat("1");
        }
        return null;
    }

    private String load_dataset2(){
        bang = hair;
        String url = "http://"+ a_server[server] +"/img/php/all_param2.php?&id1="+ front +","+ outfit +","+ bodyacc1 +"," + bodyacc2 + ","+ hairacc1 +","+ hairacc2 +","+ faceacc1 +","+ faceacc2 +","+ backacc1 +","+ backacc2 +","+ bkg +","+ hair +","+ face_eye1 +","+ face_eye2 +","+ hair2 + "," + front2 + "," + bodyacc3 + "," + hairacc3 + "," + faceacc3 + "," + backacc3 + "," + face_pup1 + "," + face_pup2 + ",0&id2="+ face_skin +"&id3=" + face_tam + "&id4="+ face_exp +"&id5="+ face_blush +"&id6=0&id7=0";
        return url;
    }

    private void load_avatar_hd_set(ImageView iview) {
        final String url_fondo = "http://" + a_server[server] + "/img/php/avatar.php?id1=" + generate_ids(5, bkg) + "&id2=1";
        final String url_capa = "http://" + a_server[server] + "/img/php/all_param2.php?&id1=" + generate_ids(2, front) + "," + generate_ids(1, outfit) + "," + generate_ids(1, bodyacc1) + "," + generate_ids(1, bodyacc2) + "," + generate_ids(1, hairacc1) + "," + generate_ids(1, hairacc2) + "," + generate_ids(1, faceacc1) + "," + generate_ids(1, faceacc2) + "," + generate_ids(1, backacc1) + "," + generate_ids(1, backacc2) + "," + generate_ids(1, bkg) + "," + generate_ids(1, hair) + "," + generate_ids(1, face_eye1) + "," + generate_ids(1, face_eye2) + "," + generate_ids(1, hair2) + "," + generate_ids(1, front2) + "," + generate_ids(1, bodyacc3) + "," + generate_ids(1, hairacc3) + "," + generate_ids(1, faceacc3) + "," + generate_ids(1, backacc3) + "," + generate_ids(1, face_pup1) + "," + generate_ids(1, face_pup2) + ",x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x," + generate_ids(4, face_cej) + "&id2=" + generate_ids(3, face_skin) + "&id3=" + generate_ids(3, face_tam) + "&id4=" + generate_ids(3, face_exp) + "&id5=" + generate_ids(3, face_blush) + "&id6=1x1&id7=0x1";

        final ImageView iv_fondo = new ImageView(getContext());
        final ImageView iv_capa = new ImageView(getContext());

        if (render_mode == 0) {
            //render rapido.
            load_avatar_set(load_dataset(), iv_avatar);
        }

        if (render_mode == 1) {
            //con imageviews completo
            iv_loading.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(url_fondo)
                    .into(iv_fondo, new Callback() {
                        @Override
                        public void onSuccess() {
                            Picasso.get()
                                    .load(url_capa)
                                    .into(iv_capa, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            Bitmap bm1 = ((BitmapDrawable) iv_fondo.getDrawable()).getBitmap();
                                            Bitmap bm2 = ((BitmapDrawable) iv_capa.getDrawable()).getBitmap();
                                            iv_avatar.setImageBitmap(join_bitmap(bm1, bm2));
                                            iv_loading.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onError(Exception e) {
                                            iv_loading.setVisibility(View.GONE);
                                            //intentar reconectarse y no quedarse con solo un intento.
                                            load_avatar_hd_set(iv_avatar);
                                        }
                                    });
                        }

                        @Override
                        public void onError(Exception e) {
                            //Toast.makeText(getContext(),"Error al conectar.", Toast.LENGTH_LONG).show();
                            iv_loading.setVisibility(View.GONE);
                            //intentar reconectarse y no quedarse con solo un intento.
                            load_avatar_hd_set(iv_avatar);
                        }
                    });
        }
        if (render_mode == 2) {
            //solo fondo
            iv_loading.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(url_fondo)
                    .into(iv_fondo, new Callback() {
                        @Override
                        public void onSuccess() {
                            iv_avatar.setImageBitmap(((BitmapDrawable) iv_fondo.getDrawable()).getBitmap());
                            iv_loading.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            iv_loading.setVisibility(View.GONE);
                            //intentar reconectarse y no quedarse con solo un intento.
                            load_avatar_hd_set(iv_avatar);
                        }
                    });
        }
        if (render_mode == 3) {
            //solo capa
            iv_loading.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(url_capa)
                    .into(iv_capa, new Callback() {
                        @Override
                        public void onSuccess() {
                            iv_avatar.setImageBitmap(((BitmapDrawable) iv_capa.getDrawable()).getBitmap());
                            iv_loading.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            iv_loading.setVisibility(View.GONE);
                            //intentar reconectarse y no quedarse con solo un intento.
                            load_avatar_hd_set(iv_avatar);
                        }
                    });
        }
        render_icon_mode(render_mode);
    }

    private Bitmap join_bitmap(Bitmap bm1, Bitmap bm2){
        int w = base.getWidth();
        int h = base.getHeight();
        Bitmap result = Bitmap.createBitmap(w, h, base.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(base, 0, 0, null);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawBitmap(bm1, 0, 0, paint);
        canvas.drawBitmap(bm2, 0, 0, paint);
        return result;
    }

    private void load_avatar_set(String url, ImageView iview){
        iv_loading.setVisibility(View.VISIBLE);
        Picasso.get()
                .load(url)
                .noFade()
                .noPlaceholder()
                .into(iview, new Callback() {
                    @Override
                    public void onSuccess() {
                        iv_loading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        //Toast.makeText(getContext(),"Error al conectar.", Toast.LENGTH_LONG).show();
                        new CustomToast(getContext(), "Error al conectar, reintente", Toast.LENGTH_SHORT, Gravity.BOTTOM);
                        iv_loading.setVisibility(View.GONE);
                    }
                });
    }

    private void load_set(String url, ImageView iview){
        Picasso.get()
                .load(url)
                .noFade()
                .noPlaceholder()
                .into(iview);
    }

    private void initial_load_set(String url, ImageView iview){
        Picasso.get()
                .load(url)
                .noFade()
                .noPlaceholder()
                .into(iview, new Callback() {
                    @Override
                    public void onSuccess() {
                        iv_avatar_initload.setVisibility(View.GONE);
                    }
                    @Override
                    public void onError(Exception e) {
                        iv_avatar_initload.setVisibility(View.GONE);
                    }
                });
    }

    private void load_seticons(){
        //load outfit set name.
        tv_setinfo.setText(setinfo);
        tv_setstitle.setText("\u3010" + setname + "\u3011");
        String thumb = "avatar_cate_thum";

        //face;
        load_set("http://" + a_server[server] + "/img/" + thumb + "/face/" + face_eye1 + ".png", iv_face);

        //outfit
        if (!outfit.equals("0")){
            load_set("http://" + a_server[server] + "/img/" + thumb + "/body1/" + outfit + ".png", iv_body);
        } else {
            iv_body.setImageResource(R.drawable.avatar_slot1);
        }
        if (sets_state_lock_list[0]){
            iv_body.setColorFilter(Color.argb(80, 255, 0, 0));
        } else {
            iv_body.setColorFilter(Color.argb(0, 0, 0, 0));
        }

        //front
        if (!front.equals("0")){
            load_set("http://" + a_server[server] + "/img/" + thumb + "/front/" + front + ".png", iv_front);
        } else {
            iv_front.setImageResource(R.drawable.avatar_slot0);
        }
        if (sets_state_lock_list[12]){
            iv_front.setColorFilter(Color.argb(80, 255, 0, 0));
        } else {
            iv_front.setColorFilter(Color.argb(0, 0, 0, 0));
        }

        //background
        if (!bkg.equals("0")){
            load_set("http://"+ a_server[server] +"/img/" + thumb + "/bg/" + bkg + ".png", iv_bkg);

        } else {
            iv_bkg.setImageResource(R.drawable.avatar_slot10);
        }
        if (sets_state_lock_list[9]){
            iv_bkg.setColorFilter(Color.argb(80, 255, 0, 0));
        } else {
            iv_bkg.setColorFilter(Color.argb(0, 0, 0, 0));
        }

        //bodyacc1
        if (!bodyacc1.equals("0")) {
            load_set("http://"+ a_server[server] +"/img/" + thumb + "/body_acc/" + bodyacc1 + ".png", iv_bodyacc1);
        } else {
            iv_bodyacc1.setImageResource(R.drawable.avatar_slot2);
        }
        if (sets_state_lock_list[1]){
            iv_bodyacc1.setColorFilter(Color.argb(80, 255, 0, 0));
        } else {
            iv_bodyacc1.setColorFilter(Color.argb(0, 0, 0, 0));
        }

        //bodyacc2
        if (!bodyacc2.equals("0")){
            load_set("http://"+ a_server[server] +"/img/" + thumb + "/body_acc/" + bodyacc2 + ".png", iv_bodyacc2);
        } else {
            iv_bodyacc2.setImageResource(R.drawable.avatar_slot3);
        }
        if (sets_state_lock_list[2]){
            iv_bodyacc2.setColorFilter(Color.argb(80, 255, 0, 0));
        } else {
            iv_bodyacc2.setColorFilter(Color.argb(0, 0, 0, 0));
        }

        //backacc1
        if (!backacc1.equals("0")){
            load_set("http://"+ a_server[server] +"/img/" + thumb + "/back_acc/" + backacc1 + ".png", iv_backacc1);
        } else {
            iv_backacc1.setImageResource(R.drawable.avatar_slot8);
        }
        if (sets_state_lock_list[7]){
            iv_backacc1.setColorFilter(Color.argb(80, 255, 0, 0));
        } else {
            iv_backacc1.setColorFilter(Color.argb(0, 0, 0, 0));
        }

        //backacc2
        if (!backacc2.equals("0")){
            load_set("http://"+ a_server[server] +"/img/" + thumb + "/back_acc/" + backacc2 + ".png", iv_backacc2);
        } else {
            iv_backacc2.setImageResource(R.drawable.avatar_slot9);
        }
        if (sets_state_lock_list[8]){
            iv_backacc2.setColorFilter(Color.argb(80, 255, 0, 0));
        } else {
            iv_backacc2.setColorFilter(Color.argb(0, 0, 0, 0));
        }

        //faceacc1
        if (!faceacc1.equals("0")){
            load_set("http://"+ a_server[server] +"/img/" + thumb + "/face_acc/" + faceacc1 + ".png", iv_faceacc1);
        } else {
            iv_faceacc1.setImageResource(R.drawable.avatar_slot6);
        }
        if (sets_state_lock_list[5]){
            iv_faceacc1.setColorFilter(Color.argb(80, 255, 0, 0));
        } else {
            iv_faceacc1.setColorFilter(Color.argb(0, 0, 0, 0));
        }

        //faceacc2
        if (!faceacc2.equals("0")){
            load_set("http://"+ a_server[server] +"/img/" + thumb + "/face_acc/" + faceacc2 + ".png", iv_faceacc2);
        } else {
            iv_faceacc2.setImageResource(R.drawable.avatar_slot7);
        }
        if (sets_state_lock_list[6]){
            iv_faceacc2.setColorFilter(Color.argb(80, 255, 0, 0));
        } else {
            iv_faceacc2.setColorFilter(Color.argb(0, 0, 0, 0));
        }

        //hairacc1
        if (!hairacc1.equals("0")){
            load_set("http://"+ a_server[server] +"/img/" + thumb + "/hair_acc/" + hairacc1 + ".png", iv_hairacc1);
        } else {
            iv_hairacc1.setImageResource(R.drawable.avatar_slot4);
        }
        if (sets_state_lock_list[3]){
            iv_hairacc1.setColorFilter(Color.argb(80, 255, 0, 0));
        } else {
            iv_hairacc1.setColorFilter(Color.argb(0, 0, 0, 0));
        }

        //hairacc2
        if (!hairacc2.equals("0")){
            load_set("http://"+ a_server[server] +"/img/" + thumb + "/hair_acc/" + hairacc2 + ".png", iv_hairacc2);
        } else {
            iv_hairacc2.setImageResource(R.drawable.avatar_slot5);
        }
        if (sets_state_lock_list[4]){
            iv_hairacc2.setColorFilter(Color.argb(80, 255, 0, 0));
        } else {
            iv_hairacc2.setColorFilter(Color.argb(0, 0, 0, 0));
        }

        //hair
        if (!hair.equals("0")){
            load_set("http://"+ a_server[server] +"/img/" + thumb + "/hair/" + hair + ".png", iv_hair1);
        } else {
            iv_hair1.setImageResource(R.drawable.avatar_slot11);
        }
        if (sets_state_lock_list[10]){
            iv_hair1.setColorFilter(Color.argb(80, 255, 0, 0));
        } else {
            iv_hair1.setColorFilter(Color.argb(0, 0, 0, 0));
        }

        //hair2
        if (!hair2.equals("0")){
            load_set("http://"+ a_server[server] +"/img/" + thumb + "/hair/" + hair2 + ".png", iv_hair2);
        } else {
            iv_hair2.setImageResource(R.drawable.avatar_slot14);
        }
        if (sets_state_lock_list[11]){
            iv_hair2.setColorFilter(Color.argb(80, 255, 0, 0));
        } else {
            iv_hair2.setColorFilter(Color.argb(0, 0, 0, 0));
        }

    }

    private void on_click_avatar(){
        if (cl_sets.getVisibility() == View.VISIBLE){
            //ocultar
            cl_sets.setVisibility(View.GONE);
            cl_basebar.setVisibility(View.VISIBLE);
            Act_Main.navigation.setVisibility(View.VISIBLE);
            //ocultar menu de herramientas si es que esta abierto.
            cl_avatarview_menu.setVisibility(View.GONE);
            //estirar la cl_container hasta el parent.
            ConstraintSet cset = new ConstraintSet();
            cset.clone(cl_base_container);
            //cset.connect(R.id.cl_container,ConstraintSet.BOTTOM,ConstraintSet.PARENT_ID,ConstraintSet.BOTTOM,0);
            cset.connect(R.id.cl_container, ConstraintSet.BOTTOM, R.id.cl_basebar, ConstraintSet.TOP, 0);
            cset.applyTo(cl_base_container);
        } else {
            //mostrar
            cl_sets.setVisibility(View.VISIBLE);
            cl_basebar.setVisibility(View.GONE);
            Act_Main.navigation.setVisibility(View.GONE);
            //mostrar menu si el flag esta activo
            if (is_menu_open){
                cl_avatarview_menu.setVisibility(View.VISIBLE);
            }
            //estirar la cl_container hasta la cl_sets.
            ConstraintSet cset = new ConstraintSet();
            cset.clone(cl_base_container);
            cset.connect(R.id.cl_container,ConstraintSet.BOTTOM,R.id.cl_sets,ConstraintSet.TOP,0);
            cset.applyTo(cl_base_container);
        }
    }


    private class task_searchstring extends AsyncTask<String, String, String> {
        ArrayList<String> result_ids = new ArrayList<>();
        String cadena;
        String item_type;
        String item_kind;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            cadena = strings[0];
            item_type = strings[1];
            item_kind = strings[2];

            String[] words = cadena.split("\\s+");

            //checkear si es relacional o no.
            if (!is_random_relacional){
                //busqueda no relacional.
                //este hace una busqueda de cada una de las palabras independientemente la una de la otra.
                //puede ser mas lenta que la busqueda relacional.
                for (int b = 0; b < words.length; b++){
                    words[b] = words[b].replaceAll("[^\\w]", "");
                    String regex_actual = "^(?=.*\\b"+ words[b] +"\\b)";
                    Pattern regex = Pattern.compile(regex_actual, Pattern.CASE_INSENSITIVE);
                    for (int i=0; i < Act_Main.item_names.length; i++){
                        Matcher match = regex.matcher(Act_Main.item_names[i]);
                        if (match.find()){
                            if (type_check(Integer.valueOf(item_type), i)){
                                result_ids.add(String.valueOf(i));
                            }
                        }
                    }
                }
            } else {
                //busqueda relacional.
                //el modo de busqueda tradicional, relaciona las palabras clave entre si, haciendo que el resultado tenga relacion
                //con ambas palabras, es mas rapida que la no relacional.
                String cadena_p = "^";
                for (int a = 0; a < words.length; a++) {
                    words[a] = words[a].replaceAll("[^\\w]", "");
                    cadena_p = cadena_p + "(?=.*\\b"+ words[a] +"\\b)";
                }
                Pattern regex = Pattern.compile(cadena_p, Pattern.CASE_INSENSITIVE);

                for (int i=0; i < Act_Main.item_names.length; i++){
                    Matcher match = regex.matcher(Act_Main.item_names[i]);
                    if (match.find()){
                        if (type_check(Integer.valueOf(item_type), i)){
                            result_ids.add(String.valueOf(i));
                        }
                    }
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Random r = new Random();
            String id_gen = "0";
            if (result_ids.size() > 0){
                id_gen = result_ids.get(r.nextInt(result_ids.size()));
            }
            //Log.d("dream_random", "generado id: " + id_gen + " - en tipo: " + item_type + " - " + is_random_relacional);
            if (!id_gen.equals("0")){
                type_set(Integer.valueOf(item_type), Integer.valueOf(item_kind), id_gen);
            }
        }
    }

    private void type_set(int item_type, int item_kind, String value){
        //backacc 1
        //bkg 2
        //bodyacc 3
        //faceacc 4
        //front 5
        //hairs 6
        //hairacc 7
        //outfit 8
        switch (item_type) {
            case 1:
                if (item_kind == 1) {
                    if (!sets_state_lock_list[7]){
                        backacc1 = value;
                    }
                } else {
                    if (!sets_state_lock_list[8]){
                        backacc2 = value;
                    }
                }
                break;
            case 2:
                if (!sets_state_lock_list[9]){
                    bkg = value;
                }
                break;
            case 3:
                if (item_kind == 1){
                    if (!sets_state_lock_list[1]){
                        bodyacc1 = value;
                    }
                } else {
                    if (!sets_state_lock_list[2]){
                        bodyacc2 = value;
                    }
                }
                break;
            case 4:
                if (item_kind == 1){
                    if (!sets_state_lock_list[5]){
                        faceacc1 = value;
                    }
                } else {
                    if (!sets_state_lock_list[6]){
                        faceacc2 = value;
                    }
                }
                break;
            case 5:
                if (!sets_state_lock_list[12]){
                    front = value;
                }
                break;
            case 6:
                if (item_kind == 1){
                    if (!sets_state_lock_list[10]){
                        hair = value;
                    }
                } else {
                    if (!sets_state_lock_list[11]){
                        hair2 = value;
                    }
                }
                break;
            case 7:
                if (item_kind == 1){
                    if (!sets_state_lock_list[3]){
                        hairacc1 = value;
                    }
                } else {
                    if (!sets_state_lock_list[4]){
                        hairacc2 = value;
                    }

                }
                break;
            case 8:
                if (!sets_state_lock_list[0]){
                    outfit = value;
                }
                break;
        }
        //todo si es posible, hacer que esto no se ejecute con cada item, sino solo con el del final
        load_avatar_set(load_dataset2(), iv_avatar);
        load_seticons();
    }

    private boolean type_check(int item_type, int item_id){

        //backacc 1
        //bkg 2
        //bodyacc 3
        //faceacc 4
        //front 5
        //hairs 6
        //hairacc 7
        //outfit 8

        String s_item_id = String.valueOf(item_id);

        switch (item_type){
            case 1:
                for (int i=0; i < Act_Main.list_back_acc.length; i++){
                    if (Act_Main.list_back_acc[i].equals(s_item_id)){
                        return true;
                    }
                }
                return false;
            case 2:
                for (int i=0; i < Act_Main.list_bkg.length; i++){
                    if (Act_Main.list_bkg[i].equals(s_item_id)){
                        return true;
                    }
                }
                return false;
            case 3:
                for (int i=0; i < Act_Main.list_bodyacc.length; i++){
                    if (Act_Main.list_bodyacc[i].equals(s_item_id)){
                        return true;
                    }
                }
                return false;
            case 4:
                for (int i=0; i < Act_Main.list_face_acc.length; i++){
                    if (Act_Main.list_face_acc[i].equals(s_item_id)){
                        return true;
                    }
                }
                return false;
            case 5:
                for (int i=0; i < Act_Main.list_front.length; i++){
                    if (Act_Main.list_front[i].equals(s_item_id)){
                        return true;
                    }
                }
                return false;
            case 6:
                for (int i=0; i < Act_Main.list_hairs.length; i++){
                    if (Act_Main.list_hairs[i].equals(s_item_id)){
                        return true;
                    }
                }
                return false;
            case 7:
                for (int i=0; i < Act_Main.list_hats.length; i++){
                    if (Act_Main.list_hats[i].equals(s_item_id)){
                        return true;
                    }
                }
                return false;
            case 8:
                for (int i=0; i < Act_Main.list_outfits.length; i++){
                    if (Act_Main.list_outfits[i].equals(s_item_id)){
                        return true;
                    }
                }
                return false;
        }
        return false;
    }


}