package com.software.ddk.dreamgen.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.software.ddk.dreamgen.R;
import com.software.ddk.dreamgen.activity.Act_Design;
import com.software.ddk.dreamgen.activity.Act_Main;
import com.software.ddk.dreamgen.utils.ColorFilterGenerator;
import com.software.ddk.dreamgen.utils.ColorSeekBar;
import com.software.ddk.dreamgen.utils.CustomToast;
import com.software.ddk.dreamgen.utils.LongTouchIntervalListener;
import com.software.ddk.dreamgen.utils.TouchImageView;
import com.software.ddk.dreamgen.utils.Utils;
import com.squareup.picasso.Picasso;
import com.warkiz.tickseekbar.OnSeekChangeListener;
import com.warkiz.tickseekbar.SeekParams;
import com.warkiz.tickseekbar.TickSeekBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class designerview extends Fragment {

    private String outfit, bkg, hair, hair2, bang, face_eye1, face_eye2, hairacc1, hairacc2, bodyacc1, bodyacc2, faceacc1, faceacc2, backacc1, backacc2, front, face_exp, face_skin, face_tam, face_pup1, face_pup2, face_blush, face_cej;
    public static final int PICK_IMAGE = 1;
    private int PICK_KIND = 1;
    private int last_kind = 1;
    private boolean tint_mode_cleanskin = false;
    private boolean configs_cb1 = false;
    private boolean configs_cb2 = false;
    private boolean cancel_loading = false;
    private EditText et_opt_2_hvalue;
    private String hvalue = "305";

    // configs posicion_x, posicion_y, w, h, visible, update
    private String[][] configs;

    public static String[][] pub_configs;
    public static Bitmap[] bmp_design;
    public static Bitmap pub_iv_render;
    public static String[] pub_render_configs;
    public static String design_name;

    //valores predeterminados de los layers.
    private int x = 0;
    private int y = 0;
    private int w = 640;
    private int h = 854;
    private int tmp_pos[] = {0, 0};

    private TouchImageView iv_render, iv_render_preview;
    private ConstraintLayout cl_design_loading, cl_design_w2x_loading;
    private Bitmap base, bm_layerbackground, bm_layerbackacc, bm_layerbackacc2, bm_layerback, bm_layerfront, bm_layerfrontall, bm_layercenter, bm_layereyes, bm_layerbangs, bm_layercheeks, bm_layercheeks2;
    private Bitmap bm_layerhair, bm_hairback, bm_cheeks, bm_hairaccs, bm_faceaccs, bm_head, bm_bangs, bm_layer_tint;
    private Bitmap bm_c_layerbackground, bm_c_layerbackacc2, bm_c_layerbackacc, bm_c_layerback, bm_c_layerhair, bm_c_boca, bm_c_layereyes, bm_c_layerfront, bm_c_layerfrontall, bm_c_layerhairback, bm_c_faceaccs, bm_c_hairaccs, bm_c_head, bm_c_bangs, bm_c_cheeks;
    private Bitmap bm_backup;
    private ConstraintLayout cl_design_base_container, cl_avarender, cl_comps, cl_des_basebar, cl_pop_layers;
    private ImageView iv_comp_faceaccs, iv_comp_layerbackground, iv_comp_backacc2, iv_comp_backacc, iv_comp_back, iv_comp_center, iv_comp_bangs, iv_comp_cheeks, iv_comp_eyes, iv_comp_front, iv_comp_frontall, iv_comp_layerhair, iv_comp_hairback, iv_comp_hairaccs;
    private ImageButton bt_design_savepic, bt_design_sharepic, bt_design_reloadset, bt_design_savew2x;
    private ImageButton bt_m_right, bt_m_left, bt_m_up, bt_m_down, bt_m_scale;
    private ImageButton bt_comp_list, bt_comp_config;
    private CheckBox cb_opt_1, cb_opt_2;

    private boolean bt_comp_list_active = true;
    private boolean bt_comp_config_active = true;
    private boolean all_buttons = true;
    private boolean transform_mode_scale = false;
    boolean[] temp_lock_lay;
    boolean is_lock_lay = false;
    private boolean[] locked_layers = new boolean[]{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};

    //global shared layers
    private ImageView iv_layer_preview;
    private Bitmap bm_fhair, bm_ojos, bm_backhair, bm_boca;
    private EditText et_pic_posx, et_pic_posy, et_pic_perc_w, et_pic_perc_h;
    private PopupWindow pop_layer, pop_tint, pop_transform, pop_config;
    private TickSeekBar sb_tint_saturacion, sb_tint_brightness;
    private ColorSeekBar sb_tint_hue;
    private TextView tv_tint_hue_value, tv_tint_saturacion_value, tv_tint_brightness_value, tv_conj_name;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_designerview, parent, false);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            //no oculto.
            on_click_avarender();
            if (Act_Main.LOAD_DESIGN_DATA){
                set_global_data();
                import_saved_maindata();
                //listeners();
            }
            if (Act_Main.UPDATE_DESIGN_VIEW){
                set_global_data();
                render_avatar();
                Act_Main.UPDATE_DESIGN_VIEW = false;
            }
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Act_Main.navigation.setVisibility(View.GONE);
        iv_render = view.findViewById(R.id.iv_render);
        iv_render_preview = view.findViewById(R.id.iv_render_preview);
        cl_design_loading = view.findViewById(R.id.cl_design_loading);
        cl_design_w2x_loading = view.findViewById(R.id.cl_design_w2xloading);
        cl_design_base_container = view.findViewById(R.id.cl_design_base_container);
        cl_avarender = view.findViewById(R.id.cl_avarender);
        cl_comps = view.findViewById(R.id.cl_comps);
        cl_des_basebar = view.findViewById(R.id.cl_des_basebar);
        cl_pop_layers = view.findViewById(R.id.cl_pop_layers);
        base = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.base), 640, 854, false);

        bt_design_reloadset = view.findViewById(R.id.bt_design_reloadset);
        bt_design_savepic = view.findViewById(R.id.bt_design_savepic);
        bt_design_sharepic = view.findViewById(R.id.bt_design_sharepic);
        bt_design_savew2x = view.findViewById(R.id.bt_design_savew2x);
        bt_comp_list = view.findViewById(R.id.bt_comp_list);
        bt_comp_config = view.findViewById(R.id.bt_comp_config);

        iv_comp_faceaccs = view.findViewById(R.id.iv_comp_faceaccs);
        iv_comp_layerbackground = view.findViewById(R.id.iv_comp_layerbackground);
        iv_comp_backacc2 = view.findViewById(R.id.iv_comp_backacc2);
        iv_comp_backacc = view.findViewById(R.id.iv_comp_backacc);
        iv_comp_back = view.findViewById(R.id.iv_comp_back);
        iv_comp_center = view.findViewById(R.id.iv_comp_center);
        iv_comp_bangs = view.findViewById(R.id.iv_comp_bangs);
        iv_comp_cheeks = view.findViewById(R.id.iv_comp_cheeks);
        iv_comp_eyes = view.findViewById(R.id.iv_comp_eyes);
        iv_comp_front = view.findViewById(R.id.iv_comp_front);
        iv_comp_frontall = view.findViewById(R.id.iv_comp_frontall);
        iv_comp_layerhair = view.findViewById(R.id.iv_comp_layerhair);
        iv_comp_hairback = view.findViewById(R.id.iv_comp_hairback);
        iv_comp_hairaccs = view.findViewById(R.id.iv_comp_hairaccs);
        tv_conj_name = view.findViewById(R.id.tv_conj_name);

        if (Act_Main.LOAD_DESIGN_DATA){
            //si hay un diseño queriendo cargarse, cargarlo.
            new CustomToast(getContext(), "Diseño Cargado.", Toast.LENGTH_SHORT, Gravity.CENTER);
            set_global_data();
            import_saved_maindata();
            listeners();
        } else {
            set_global_data();
            render_avatar();
            Act_Main.UPDATE_DESIGN_VIEW = false;
            listeners();
        }

        iv_render.setZoom(0.99f);
        iv_render_preview.setZoom(0.99f);
        iv_render.setOnTouchImageViewListener(new TouchImageView.OnTouchImageViewListener() {
            @Override
            public void onMove() {
                //hacer que el iv_render_preview se posicione de la misma manera que el iv_render
                if (iv_render.getDrawable() != null || iv_render_preview.getDrawable() != null){
                    PointF scroll = iv_render.getScrollPosition();
                    iv_render_preview.setZoom(iv_render.getCurrentZoom(), scroll.x, scroll.y);
                }
            }

            @Override
            public void onClick() {
                iv_render.setZoom(0.99f);
                on_click_avarender();
            }
        });

        cl_avarender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                on_click_avarender();
                iv_render.setZoom(0.99f);
            }
        });

        bt_design_savepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.check_storage_permission(getActivity())){
                    Utils.save_bitmap(getContext(), iv_render, Utils.getCapturedFilename());
                }
            }
        });

        bt_design_sharepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.check_storage_permission(getActivity())){
                    Utils.share_bitmap(getContext(), iv_render);
                }
            }
        });

        bt_design_reloadset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_global_data();
                render_avatar();
            }
        });

        bt_design_savew2x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_w2x();
            }
        });

        cl_design_loading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel_loading = true;
                //cl_design_loading.setVisibility(View.GONE);
                new CustomToast(getContext(), "Cancelando descarga...", Toast.LENGTH_SHORT, Gravity.BOTTOM);
            }
        });

    }

    private void save_w2x() {
        //guardado de la imagen con waifu2x
        new task_downloadW2X().execute();

    }

    private class task_downloadW2X extends AsyncTask<Object, String, String> {
        OkHttpClient client = new OkHttpClient();
        Bitmap data = ((BitmapDrawable) iv_render.getDrawable()).getBitmap();
        File file = new File(getContext().getCacheDir(), "tempw2x.png");
        String url = "https://api.deepai.org/api/waifu2x";
        RequestBody requestBody;
        Request request;
        Response response;
        String url_res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //show loading
            cl_design_w2x_loading.setVisibility(View.VISIBLE);

            //create file from bitmap
            try {
                file.createNewFile();
            } catch (IOException e){
                e.printStackTrace();
            }
            //convert bitmap and write
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            data.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] bitmapdata = bos.toByteArray();
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
            } catch (Exception e){
                e.printStackTrace();
            }

            requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", file.getName(), RequestBody.create(MediaType.parse("text/csv"), file))
                    .build();

            request = new Request.Builder()
                    .url(url)
                    .header("api-key", "64884968-143a-4c82-ad04-3a546bf8fa2e")
                    .post(requestBody)
                    .build();

        }

        @Override
        protected String doInBackground(Object... params) {
            try{
                response = client.newCall(request).execute();
            } catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            cl_design_w2x_loading.setVisibility(View.GONE);
            try {
                if (response != null && response.isSuccessful()) {
                    String jsonData = response.body().string();
                    JsonParser parser = new JsonParser();
                    JsonObject json = (JsonObject) parser.parse(jsonData);
                    url_res = json.get("output_url").getAsString();
                    //download image
                    new task_saveBitmapfromURL().execute(url_res);
                }
            } catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    private class task_saveBitmapfromURL extends AsyncTask<String, String, String> {
        Bitmap result_bmp;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cl_design_w2x_loading.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                result_bmp = Picasso.get().load(params[0]).get();
            } catch (IOException e){
                e.printStackTrace();
                new CustomToast(getContext(), "Error de Conexion", Toast.LENGTH_SHORT, Gravity.BOTTOM);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            cl_design_w2x_loading.setVisibility(View.GONE);
            Utils.save_bitmap_w2x_toPath(getContext(), result_bmp, Utils.getW2XFilename());

        }
    }



    private void import_saved_maindata(){
        //desactivar el flag de carga.
        Act_Main.LOAD_DESIGN_DATA = false;
        //leer datos guardados.
        String folder = Act_Main.SELECTED_DESIGN_DATA[1];
        bmp_design = new Bitmap[14];
        bmp_design[0] = Utils.open_bitmap(getContext(), folder, "layer_0.png");
        bmp_design[1] = Utils.open_bitmap(getContext(), folder, "layer_1.png");
        bmp_design[2] = Utils.open_bitmap(getContext(), folder, "layer_2.png");
        bmp_design[3] = Utils.open_bitmap(getContext(), folder, "layer_3.png");
        bmp_design[4] = Utils.open_bitmap(getContext(), folder, "layer_4.png");
        bmp_design[5] = Utils.open_bitmap(getContext(), folder, "layer_5.png");
        bmp_design[6] = Utils.open_bitmap(getContext(), folder, "layer_6.png");
        bmp_design[7] = Utils.open_bitmap(getContext(), folder, "layer_7.png");
        bmp_design[8] = Utils.open_bitmap(getContext(), folder, "layer_8.png");
        bmp_design[9] = Utils.open_bitmap(getContext(), folder, "layer_9.png");
        bmp_design[10] = Utils.open_bitmap(getContext(), folder, "layer_10.png");
        bmp_design[11] = Utils.open_bitmap(getContext(), folder, "layer_11.png");
        bmp_design[12] = Utils.open_bitmap(getContext(), folder, "layer_12.png");
        bmp_design[13] = Utils.open_bitmap(getContext(), folder, "layer_13.png");

        configs = restore_design_data(Act_Main.SELECTED_DESIGN_DATA);
        put_array_to_imageviews();
        face_tam = configs[0][2];
        face_skin = configs[0][3];
        face_exp = configs[0][4];
        configs_cb1 = Act_Main.SELECTED_DESIGN_DATA[3].equals("true");
        configs_cb2 = Act_Main.SELECTED_DESIGN_DATA[4].equals("true");
        hvalue = Act_Main.SELECTED_DESIGN_DATA[5];

        //cargar nombre del diseño.
        tv_conj_name.setText("Capas de: " +"\u3010" + Act_Main.SELECTED_DESIGN_DATA[0] + "\u3011");

        pre_calculo(true);
    }

    private String[][] restore_design_data(String[] design_data){
        String[][] res_configs = new String[configs.length][configs[0].length];
        ArrayList<String> des_data = new ArrayList<>(Arrays.asList(design_data));

        if (design_data.length < 99){
            //formato antiguo.
            des_data.remove(0); //des_name
            des_data.remove(0); //des_id
        } else if (des_data.size() < 103) {
            //formato viejo.
            des_data.remove(0); //des_name
            des_data.remove(0); //des_id
            des_data.remove(0); //des_desc
            des_data.remove(0); //cb1
            des_data.remove(0); //cb2
            des_data.remove(0); //hvalue
        } else {
            //formato nuevo.
            des_data.remove(0); //des_name
            des_data.remove(0); //des_id
            des_data.remove(0); //des_desc
            des_data.remove(0); //cb1
            des_data.remove(0); //cb2
            des_data.remove(0); //hvalue
            des_data.remove(0); //des_date
            des_data.remove(0); //fav flag.
            des_data.remove(0); //extra
        }
        for (int i=0; i < configs.length; i++){
            for (int j=0; j < configs[0].length; j++){
                res_configs[i][j] = des_data.get(i * configs[0].length + j);
            }
        }
        return res_configs;
    }

    private void create_imageviews_array(){
        bmp_design = new Bitmap[14];
        bmp_design[0] = bm_layerbackground;
        bmp_design[1] = bm_layerbackacc2;
        bmp_design[2] = bm_layerbackacc;
        bmp_design[3] = bm_layerback;
        bmp_design[4] = bm_layercenter;
        bmp_design[5] = bm_layerbangs;
        bmp_design[6] = bm_cheeks;
        bmp_design[7] = bm_layereyes;
        bmp_design[8] = bm_layerfront;
        bmp_design[9] = bm_layerfrontall;
        bmp_design[10] = bm_layerhair;
        bmp_design[11] = bm_hairback;
        bmp_design[12] = bm_faceaccs;
        bmp_design[13] = bm_hairaccs;
    }

    private void put_array_to_imageviews(){
        bm_layerbackground = bmp_design[0];
        bm_layerbackacc2 = bmp_design[1];
        bm_layerbackacc = bmp_design[2];
        bm_layerback = bmp_design[3];
        bm_layercenter = bmp_design[4];
        bm_layerbangs = bmp_design[5];
        bm_cheeks = bmp_design[6];
        bm_layereyes = bmp_design[7];
        bm_layerfront = bmp_design[8];
        bm_layerfrontall = bmp_design[9];
        bm_layerhair = bmp_design[10];
        bm_hairback = bmp_design[11];
        bm_faceaccs = bmp_design[12];
        bm_hairaccs = bmp_design[13];
    }


    private void listeners() {
        bt_comp_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bt_comp_list_active){
                    //crear el array.
                    create_imageviews_array();
                    if (iv_render.getDrawable() != null){
                        pub_configs = configs.clone();
                        pub_render_configs = new String[]{String.valueOf(configs_cb1),String.valueOf(configs_cb2), hvalue};
                        pub_iv_render = ((BitmapDrawable) iv_render.getDrawable()).getBitmap();
                    }
                    //cargar activity de la lista con un resultcode 332
                    Intent in = new Intent(getContext(), Act_Design.class);
                    startActivityForResult(in, 332);
                } else {
                    new CustomToast(getContext(), "Espere por favor.", Toast.LENGTH_SHORT, Gravity.BOTTOM);
                }
            }
        });
        bt_comp_config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bt_comp_config_active){
                    pop_config(last_kind);
                } else {
                    new CustomToast(getContext(), "Espere por favor.", Toast.LENGTH_SHORT, Gravity.BOTTOM);
                }
            }
        });

        iv_comp_layerbackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop_layers(1);
            }
        });
        iv_comp_backacc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop_layers(2);
            }
        });
        iv_comp_backacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop_layers(3);
            }
        });
        iv_comp_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop_layers(4);
            }
        });
        iv_comp_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop_layers(5);
            }
        });
        iv_comp_bangs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop_layers(6);
            }
        });
        iv_comp_cheeks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop_layers(7);
            }
        });
        iv_comp_layerhair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop_layers(8);
            }
        });
        iv_comp_eyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop_layers(9);
            }
        });
        iv_comp_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop_layers(10);
            }
        });
        iv_comp_frontall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop_layers(11);
            }
        });
        iv_comp_hairback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop_layers(12);
            }
        });
        iv_comp_faceaccs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop_layers(13);
            }
        });
        iv_comp_hairaccs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop_layers(14);
            }
        });


        //long click

        iv_comp_layerbackground.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                long_del(1);
                return true;
            }
        });
        iv_comp_backacc2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                long_del(2);
                return true;
            }
        });
        iv_comp_backacc.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                long_del(3);
                return true;
            }
        });
        iv_comp_back.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                long_del(4);
                return true;
            }
        });
        iv_comp_center.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                long_del(5);
                return true;
            }
        });
        iv_comp_bangs.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                long_del(6);
                return true;
            }
        });
        iv_comp_cheeks.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                long_del(7);
                return true;
            }
        });
        iv_comp_layerhair.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                long_del(8);
                return true;
            }
        });
        iv_comp_eyes.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                long_del(9);
                return true;
            }
        });
        iv_comp_front.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                long_del(10);
                return true;
            }
        });
        iv_comp_frontall.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                long_del(11);
                return true;
            }
        });
        iv_comp_hairback.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                long_del(12);
                return true;
            }
        });
        iv_comp_faceaccs.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                long_del(13);
                return true;
            }
        });
        iv_comp_hairaccs.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                long_del(14);
                return true;
            }
        });

    }

    private void pop_config(int kind){
        last_kind = kind;
        //pop_config
        View layout = LayoutInflater.from(getContext()).inflate(R.layout.layout_pop_config,  (ViewGroup) getView().findViewById(R.id.pop_config));
        int height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 230, getResources().getDisplayMetrics()));
        //int winHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        pop_config = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, height, true);
        pop_config.setContentView(layout);
        pop_config.showAtLocation(getView(), Gravity.BOTTOM, 0, 0);
        pop_config.setFocusable(true);
        pop_config.getContentView().setFocusableInTouchMode(true);
        pop_config.getContentView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });
        pop_config.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                configs[5][4] = "1";
                configs[9][4] = "1";
                configs[5][5] = "1";
                configs[9][5] = "1";
                pre_calculo(false);
                iv_render_preview.setVisibility(View.GONE);
            }
        });

        final TickSeekBar isb_ojos, isb_boca;
        isb_ojos = layout.findViewById(R.id.isb_ojos);
        isb_boca = layout.findViewById(R.id.isb_boca);
        ImageButton bt_conf_accept = layout.findViewById(R.id.bt_conf_accept);
        cb_opt_1 = layout.findViewById(R.id.cb_opt_1);
        cb_opt_2 = layout.findViewById(R.id.cb_opt_2);
        et_opt_2_hvalue = layout.findViewById(R.id.et_opt_2_hvalue);

        //cargar_posiciones
        isb_ojos.setProgress(tmp_pos[0]);
        isb_boca.setProgress(tmp_pos[1]);
        cb_opt_1.setChecked(configs_cb1);
        cb_opt_2.setChecked(configs_cb2);
        et_opt_2_hvalue.setText(hvalue);

        //actualizar render
        configs[5][5] = "1"; //orden de recargar capas de ojos y boca
        configs[9][5] = "1";
        configs[5][4] = "0"; //orden de ocultar ojos y boca
        configs[9][4] = "0";
        armar_todo(configs, false);
        config_update_face2(isb_ojos.getProgress(), isb_boca.getProgress());
        iv_render_preview.setVisibility(View.VISIBLE);


        //listener de seekbars.
        isb_ojos.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {

            }

            @Override
            public void onStartTrackingTouch(TickSeekBar tickSeekBar) {

            }

            @Override
            public void onStopTrackingTouch(TickSeekBar tickSeekBar) {
                tmp_pos[0] = tickSeekBar.getProgress();
                config_update_face2(tickSeekBar.getProgress(), isb_boca.getProgress());
            }
        });

        isb_boca.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {

            }

            @Override
            public void onStartTrackingTouch(TickSeekBar tickSeekBar) {

            }

            @Override
            public void onStopTrackingTouch(TickSeekBar tickSeekBar) {
                tmp_pos[1] = tickSeekBar.getProgress();
                config_update_face2(isb_ojos.getProgress(), tickSeekBar.getProgress());
            }
        });

        bt_conf_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configs_cb1 = cb_opt_1.isChecked();
                configs_cb2 = cb_opt_2.isChecked();
                hvalue = et_opt_2_hvalue.getText().toString();

                iv_render_preview.setVisibility(View.GONE);
                pop_config.dismiss();
            }
        });

    }

    private void config_update_face2(int pos_ojos, int pos_boca){
        //frames de ojo y boca.
        configs[0][0] = String.valueOf(pos_ojos);
        configs[0][1] = String.valueOf(pos_boca);

        iv_render_preview.setImageBitmap(get_faceframe());
    }

    private void pop_layers(int kind) {
        if (all_buttons){
            last_kind = kind;
            //pop_layers
            View layout = LayoutInflater.from(getContext()).inflate(R.layout.layout_pop_layer, null);
            int height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 230, getResources().getDisplayMetrics()));
            //int winHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
            pop_layer = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, height, true);
            pop_layer.setContentView(layout);
            pop_layer.showAtLocation(getView(), Gravity.BOTTOM, 0, 0);
            pop_layer.setOutsideTouchable(true);
            pop_layer.setFocusable(true);
            pop_layer.getContentView().setFocusableInTouchMode(true);
            pop_layer.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    iv_render_preview.setVisibility(View.GONE);
                    iv_render.setVisibility(View.VISIBLE);
                    pop_layer.dismiss();
                }
            });

            //declarar views
            iv_layer_preview = layout.findViewById(R.id.iv_layer_preview);
            final ImageView iv_layer_lock_indicator = layout.findViewById(R.id.iv_layer_lock_indicator);
            LinearLayout bt_layer_import = layout.findViewById(R.id.bt_layer_import);
            LinearLayout bt_layer_accept = layout.findViewById(R.id.bt_layer_accept);
            LinearLayout bt_layer_save = layout.findViewById(R.id.bt_layer_save);
            LinearLayout bt_layer_move = layout.findViewById(R.id.bt_layer_move);
            LinearLayout bt_layer_tint = layout.findViewById(R.id.bt_layer_tint);
            LinearLayout bt_layer_clean = layout.findViewById(R.id.bt_layer_clean);
            LinearLayout bt_layer_reload = layout.findViewById(R.id.bt_layer_reload);
            LinearLayout bt_layer_locker = layout.findViewById(R.id.bt_layer_locker);

            if (locked_layers[last_kind]){
                iv_layer_lock_indicator.setVisibility(View.VISIBLE);
            } else {
                iv_layer_lock_indicator.setVisibility(View.INVISIBLE);
            }

            iv_layer_preview.setImageBitmap(resize(get_topreview(kind, false), iv_layer_preview.getWidth(), iv_layer_preview.getHeight()));

            //listeners
            bt_layer_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iv_render_preview.setVisibility(View.GONE);
                    iv_render.setVisibility(View.VISIBLE);
                    pop_layer.dismiss();
                }
            });

            bt_layer_import.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //abrir importador
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    PICK_KIND = last_kind;
                    startActivityForResult(intent, PICK_IMAGE);
                }
            });

            bt_layer_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //guardar imagen de la capa.
                    if (Utils.check_storage_permission(getActivity())){
                        Utils.save_bitmap_toPath(getContext(), get_topreview(last_kind, false), Utils.getLayerFilename("layer"));
                    }
                }
            });

            bt_layer_move.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //activate move mode and close.
                    //on_render_preview(last_kind, true);
                    pop_layer.dismiss();
                    pop_transform(last_kind);
                    //cl_pop_layers.setVisibility(View.GONE);
                }
            });

            bt_layer_tint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pop_layer.dismiss();
                    pop_tints(last_kind);
                    //cl_pop_layers.setVisibility(View.GONE);
                }
            });

            bt_layer_clean.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //long_del(last_kind);
                    check_alert_clear(last_kind);
                    pop_layer.dismiss();
                }
            });

            bt_layer_reload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //todo - mover esto a un metodo
                    //guardar booleans temporalmente
                    is_lock_lay = true;
                    temp_lock_lay = locked_layers.clone();

                    //setear todos los valores de locked_layers a true
                    for (int i=0; i < locked_layers.length; i++){
                        locked_layers[i] = true;
                    }
                    //activar el layer actual.
                    locked_layers[last_kind] = false;
                    //renderizar.
                    render_avatar();

                }
            });

            bt_layer_locker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //todo - hacer que se cambie de color y demas.
                    if (!locked_layers[last_kind]){
                        locked_layers[last_kind] = true;
                        iv_layer_lock_indicator.setVisibility(View.VISIBLE);
                    } else {
                        locked_layers[last_kind] = false;
                        iv_layer_lock_indicator.setVisibility(View.INVISIBLE);
                    }
                }
            });

        } else {
            new CustomToast(getContext(), "Espere por Favor.", Toast.LENGTH_SHORT, Gravity.BOTTOM);
        }
    }

    private void check_alert_clear(final int kind){
        DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case DialogInterface.BUTTON_POSITIVE:
                        long_del(kind);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:

                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("La capa sera borrada, confirmar?")
                .setPositiveButton("Si", dialog)
                .setNegativeButton("No", dialog)
                .setIcon(R.drawable.ic_moon)
                .show();
    }

    private void pop_transform(final int kind) {
        last_kind = kind;
        //pop_transform view
        View layout = LayoutInflater.from(getContext()).inflate(R.layout.layout_pop_transform, null);
        int winHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        pop_transform = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, winHeight, true);
        pop_transform.setContentView(layout);
        pop_transform.showAtLocation(getView(), Gravity.TOP, 0, 0);
        pop_transform.setOutsideTouchable(true);
        pop_transform.setFocusable(true);
        pop_transform.getContentView().setFocusableInTouchMode(true);
        pop_transform.getContentView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    iv_render_preview.setVisibility(View.GONE);
                    pop_transform.dismiss();
                    return true;
                }
                return false;
            }
        });

        pop_transform.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //volver a renderizar set con la capa seleccionada
                configs[kind][4] = "1";
                configs[kind][5] = "1";
                iv_render_preview.setVisibility(View.GONE);
                armar_todo(configs, false);
            }
        });

        //declarar cosas del popup
        et_pic_posx = layout.findViewById(R.id.et_pic_posx);
        et_pic_posy = layout.findViewById(R.id.et_pic_posy);
        et_pic_perc_w = layout.findViewById(R.id.et_pic_perc_w);
        et_pic_perc_h = layout.findViewById(R.id.et_pic_perc_h);
        bt_m_up = layout.findViewById(R.id.bt_m_up);
        bt_m_down = layout.findViewById(R.id.bt_m_down);
        bt_m_left = layout.findViewById(R.id.bt_m_left);
        bt_m_right = layout.findViewById(R.id.bt_m_right);
        bt_m_scale = layout.findViewById(R.id.bt_m_scale);

        LinearLayout bt_m_accept = layout.findViewById(R.id.bt_m_accept);
        LinearLayout bt_m_creset = layout.findViewById(R.id.bt_m_creset);
        LinearLayout bt_m_cupdate = layout.findViewById(R.id.bt_m_cupdate);

        //renderizar set sin la capa seleccionada.
        configs[kind][4] = "0";
        configs[kind][5] = "1";
        armar_todo(configs, false);
        preview_render(kind);
        iv_render_preview.setVisibility(View.VISIBLE);

        //mostrar botones
        //bt_m_right.setVisibility(View.VISIBLE);
        //bt_m_left.setVisibility(View.VISIBLE);
        //bt_m_up.setVisibility(View.VISIBLE);
        //bt_m_down.setVisibility(View.VISIBLE);
        //bt_m_accept.setVisibility(View.VISIBLE);

        //setear a default las variables
        x = Integer.valueOf(configs[kind][0]);
        y = Integer.valueOf(configs[kind][1]);
        w = Integer.valueOf(configs[kind][2]);
        h = Integer.valueOf(configs[kind][3]);
        int repeat_interval = 10;

        et_pic_posx.setText("" + x);
        et_pic_posy.setText("" + y);
        et_pic_perc_w.setText("" + w);
        et_pic_perc_h.setText("" + h);

        //final Bitmap bm_render_preview = ((BitmapDrawable) iv_render_preview.getDrawable()).getBitmap();
        //final Bitmap bm_rp = bm_render_preview.copy(bm_render_preview.getConfig(), true);

        bt_m_scale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (transform_mode_scale){
                    transform_mode_scale = false;
                    bt_m_scale.setColorFilter(Color.argb(255, 255, 255, 255));
                } else {
                    transform_mode_scale = true;
                    bt_m_scale.setColorFilter(Color.argb(255, 255, 0, 0));
                }
            }
        });

        //listeners
        bt_m_right.setOnTouchListener(new LongTouchIntervalListener(repeat_interval) {
            @Override
            public void onTouchInterval() {
                if (transform_mode_scale){
                    w = w + 1;
                } else {
                    x = x + 1;
                }
                iv_render_preview.setImageBitmap(generar_capa(base, get_topreview(kind, true), x, y, w, h));
                update_pos_info();
            }
        });

        bt_m_left.setOnTouchListener(new LongTouchIntervalListener(repeat_interval) {
            @Override
            public void onTouchInterval() {
                if (transform_mode_scale){
                    w = w - 1;
                } else {
                    x = x - 1;
                }
                iv_render_preview.setImageBitmap(generar_capa(base, get_topreview(kind, true), x, y, w, h));
                update_pos_info();
            }
        });

        bt_m_up.setOnTouchListener(new LongTouchIntervalListener(repeat_interval) {
            @Override
            public void onTouchInterval() {
                if (transform_mode_scale){
                    h = h - 1;
                } else {
                    y = y - 1;
                }
                iv_render_preview.setImageBitmap(generar_capa(base, get_topreview(kind, true), x, y, w, h));
                update_pos_info();
            }
        });

        bt_m_down.setOnTouchListener(new LongTouchIntervalListener(repeat_interval) {
            @Override
            public void onTouchInterval() {
                if (transform_mode_scale){
                    h = h + 1;
                } else {
                    y = y + 1;
                }
                iv_render_preview.setImageBitmap(generar_capa(base, get_topreview(kind, true), x, y, w, h));
                update_pos_info();
            }
        });

        bt_m_creset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                x = 0;
                y = 0;
                w = 640;
                h = 854;
                iv_render_preview.setImageBitmap(generar_capa(base, get_topreview(kind, true), x, y, w, h));
                update_pos_info();

            }
        });

        bt_m_cupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                x = Integer.valueOf(et_pic_posx.getText().toString());
                y = Integer.valueOf(et_pic_posy.getText().toString());
                w = Integer.valueOf(et_pic_perc_w.getText().toString());
                h = Integer.valueOf(et_pic_perc_h.getText().toString());
                iv_render_preview.setImageBitmap(generar_capa(base, get_topreview(kind, true), x, y, w, h));
                update_pos_info();
            }
        });

        bt_m_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ocultar botones
                bt_m_right.setVisibility(View.GONE);
                bt_m_left.setVisibility(View.GONE);
                bt_m_up.setVisibility(View.GONE);
                bt_m_down.setVisibility(View.GONE);
                //bt_m_accept.setVisibility(View.GONE);

                //Log.d("kind", "kind: " + last_kind + " kind: " + kind);
                //put_tolayer(last_kind, ((BitmapDrawable) iv_render_preview.getDrawable()).getBitmap());

                configs[last_kind][0] = et_pic_posx.getText().toString();
                configs[last_kind][1] = et_pic_posy.getText().toString();
                configs[last_kind][2] = et_pic_perc_w.getText().toString();
                configs[last_kind][3] = et_pic_perc_h.getText().toString();
                configs[last_kind][4] = "1";
                configs[last_kind][5] = "1";
                pop_transform.dismiss();

            }
        });
    }

    private void update_pos_info() {
        //mostrar valores resultantes.
        et_pic_posx.setText("" + x);
        et_pic_posy.setText("" + y);
        et_pic_perc_w.setText("" + w);
        et_pic_perc_h.setText("" + h);
    }

    private void pop_tints(int kind){
        last_kind = kind;
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.layout_pop_tint, (ViewGroup) getActivity().findViewById(R.id.pop_tint));
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 230, getResources().getDisplayMetrics()));
        pop_tint = new PopupWindow(layout, width, height, true);
        pop_tint.setContentView(layout);
        pop_tint.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        pop_tint.showAtLocation(getView(), Gravity.BOTTOM, 0, 0);
        pop_tint.setOutsideTouchable(true);
        pop_tint.setFocusable(true);
        pop_tint.getContentView().setFocusableInTouchMode(true);
        pop_tint.getContentView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    //pop_tint.dismiss();
                    return true;
                }
                return false;
            }
        });
        pop_tint.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                configs[last_kind][5] = "1";
                pre_calculo(false);
                iv_render_preview.setVisibility(View.GONE);
            }
        });

        //colored bitmap
        //bm_layer_tint = get_topreview(kind, true);
        x = Integer.valueOf(configs[last_kind][0]);
        y = Integer.valueOf(configs[last_kind][1]);
        w = Integer.valueOf(configs[last_kind][2]);
        h = Integer.valueOf(configs[last_kind][3]);
        bm_layer_tint = generar_capa(base, get_topreview(kind, true), x, y, w, h);

        final Bitmap original_noraw_bitmap = bm_layer_tint.copy(Bitmap.Config.ARGB_8888, true);
        final Bitmap base_noraw_bitmap = Bitmap.createBitmap(original_noraw_bitmap.getWidth(), original_noraw_bitmap.getHeight(),original_noraw_bitmap.getConfig()).copy(Bitmap.Config.ARGB_8888, true);
        final Bitmap original_bitmap = get_topreview(last_kind, kind_framed_for_raw(last_kind)).copy(Bitmap.Config.ARGB_8888, true);
        final Bitmap base_bitmap = Bitmap.createBitmap(original_bitmap.getWidth(), original_bitmap.getHeight(),original_bitmap.getConfig()).copy(Bitmap.Config.ARGB_8888, true);

        sb_tint_hue = layout.findViewById(R.id.sb_tint_hue);
        sb_tint_saturacion = layout.findViewById(R.id.sb_tint_saturation);
        sb_tint_brightness = layout.findViewById(R.id.sb_tint_brightness);
        tv_tint_hue_value = layout.findViewById(R.id.tv_tint_hue_value);
        tv_tint_saturacion_value = layout.findViewById(R.id.tv_tint_saturation_value);
        tv_tint_brightness_value = layout.findViewById(R.id.tv_tint_brightness_value);

        LinearLayout bt_tint_accept = layout.findViewById(R.id.bt_tint_accept);
        LinearLayout bt_tint_reset = layout.findViewById(R.id.bt_tint_reset);
        LinearLayout bt_tint_cleanskin = layout.findViewById(R.id.bt_tint_cleanskin);

        //hue seeds
        float[] hsv = new float[3];
        int[] hue_array = new int[360];
        Color.colorToHSV(Color.RED, hsv);
        for (int i=0; i<360; i++){
            hsv[0] = i;
            hue_array[i] = Color.HSVToColor(hsv);
        }
        sb_tint_hue.setColorSeeds(hue_array);

        sb_tint_hue.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
                    @Override
                    public void onColorChangeListener(int colorBarPosition, int alphaBarPosition, int color) {
                        iv_render_preview.setVisibility(View.VISIBLE);

                        float[] col = new float[3];
                        Color.colorToHSV(color, col);

                        int saturation = sb_tint_saturacion.getProgress();
                        int brightness = sb_tint_brightness.getProgress();

                        update_tint(base_noraw_bitmap, original_noraw_bitmap, col[0], saturation, brightness);

                        update_hsv_value(String.valueOf(((int) col[0])), String.valueOf(saturation), String.valueOf(brightness));
                    }
                });

        sb_tint_saturacion.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                float[] col = new float[3];
                Color.colorToHSV(sb_tint_hue.getColor(), col);

                int saturation = sb_tint_saturacion.getProgress();
                int brightness = sb_tint_brightness.getProgress();

                update_tint(base_noraw_bitmap, original_noraw_bitmap, col[0], saturation, brightness);
                update_hsv_value(String.valueOf(((int) col[0])), String.valueOf(saturation), String.valueOf(brightness));
            }

            @Override
            public void onStartTrackingTouch(TickSeekBar tickSeekBar) {
                iv_render_preview.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStopTrackingTouch(TickSeekBar tickSeekBar) {

            }
        });

        sb_tint_brightness.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                float[] col = new float[3];
                Color.colorToHSV(sb_tint_hue.getColor(), col);

                int saturation = sb_tint_saturacion.getProgress();
                int brightness = sb_tint_brightness.getProgress();

                update_tint(base_noraw_bitmap, original_noraw_bitmap, col[0], saturation, brightness);
                update_hsv_value(String.valueOf(((int) col[0])), String.valueOf(saturation), String.valueOf(brightness));
            }

            @Override
            public void onStartTrackingTouch(TickSeekBar tickSeekBar) {
                iv_render_preview.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStopTrackingTouch(TickSeekBar tickSeekBar) {

            }
        });

        bt_tint_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //guardar en bm_backup la imagen original (o anterior)
                bm_backup = get_topreview(last_kind, false);

                //si el clean_skin ha sido ejecutado, evitar el re tintado de la imagen.
                if (!tint_mode_cleanskin){
                    tint_view(base_bitmap, original_bitmap);
                } else {
                    //todo aca aplicar el fix a la capa.
                    tint_mode_cleanskin = false;
                }

                pop_tint.dismiss();
            }
        });

        bt_tint_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sb_tint_hue.setColorBarPosition(0);
                sb_tint_brightness.setProgress(0);
                sb_tint_saturacion.setProgress(0);

                update_hsv_value("0", "0" , "0");

                if (kind_framed_for_raw(last_kind)){
                    iv_render_preview.setImageBitmap(original_bitmap);
                } else {
                    iv_render_preview.setImageBitmap(base);
                }
                put_tolayer(last_kind, original_bitmap);
            }
        });

        bt_tint_cleanskin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (kind_framed_for_raw(last_kind)){
                    new task_applyskinfix().execute(base_bitmap, original_bitmap);
                } else {
                    new CustomToast(getContext(), "No aplicable a esta capa.", Toast.LENGTH_SHORT, Gravity.BOTTOM);
                }
            }
        });
    }

    private class task_applyskinfix extends AsyncTask<Object, String, String> {
        Bitmap fixed_bitmap;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bm_backup = get_topreview(last_kind, false);
            cl_design_loading.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(Object... params) {
            Bitmap base_bitmap = (Bitmap) params[0];
            Bitmap original_bitmap = (Bitmap) params[1];
            fixed_bitmap = fix_skincolor(base_bitmap, original_bitmap);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            cl_design_loading.setVisibility(View.GONE);
            iv_render_preview.setImageBitmap(generar_capa(base, fixed_bitmap, x, y, w, h));
            //todo podria hacer que esto vaya en el accept para que la opcion se pueda cancelar incluso luego del skinfix.
            put_tolayer(last_kind, fixed_bitmap);
            tint_mode_cleanskin = true;

        }
    }

    private Bitmap fix_skincolor(Bitmap base_bitmap, Bitmap original_bitmap){
        //declarar float con valor regular.
        float[][] ranges = null;

        //fijar rango segun tono de piel
        switch (get_face_single(face_skin)){
            case 1:
                //regular
                ranges = new float[][]{
                        {0.0f, 0.09f, 0.55f}, //hsv_in
                        {40.0f, 0.69f, 1.1f}, //hsv_out  s: 0.80f
                        {93.0f, 0.71f, 0.97f}, //hsv_in_2 v: 0.79f
                        {360.0f, 0.71f, 1.1f} //hsv_out_2
                };
                break;
            case 2:
                //pale
                ranges = new float[][]{
                        {0.0f, 0.00f, 0.50f}, //hsv_in
                        {40.0f, 0.69f, 1.1f}, //hsv_out
                        {123.0f, 0.71f, 0.97f}, //hsv_in_2
                        {357.0f, 0.71f, 1.1f} //hsv_out_2
                };
                break;
            case 3:
                //tanned
                ranges = new float[][]{
                        {0.0f, 0.11f, 0.54f}, //hsv_in
                        {36.0f, 0.70f, 1.0f}, //hsv_out
                        {330.0f, 0.38f, 0.97f}, //hsv_in_2 v: 0.51f
                        {359.0f, 0.70f, 1.1f} //hsv_out_2
                };
                break;
        }

        //tintar el layer actual antes de corregir la piel.
        tint_view(base_bitmap, original_bitmap);

        //agregar esto por encima del layer actual
        Bitmap b_res = detectar_piel(bm_backup, ranges[0], ranges[1], ranges[2], ranges[3]);
        return armar_capa(get_topreview(last_kind, true), b_res, 0, 0);
    }

    private void tint_view(Bitmap base_bitmap, Bitmap original_bitmap){
        //aca se tienen que crear el filtro para la capa principal, y no para el preview.
        float[] col = new float[3];
        Color.colorToHSV(sb_tint_hue.getColor(), col);
        int saturation = sb_tint_saturacion.getProgress();
        int brightness = sb_tint_brightness.getProgress();
        put_tolayer(last_kind, aplicar_filtro(base_bitmap, original_bitmap, col[0], brightness, saturation));
    }

    public Bitmap detectar_piel(Bitmap mImage, float[] hsv_in, float[] hsv_out, float[] hsv_in_2, float[] hsv_out_2){
        int width = mImage.getWidth();
        int height = mImage.getHeight();
        int[] pixels = new int[width * height];
        mImage.getPixels(pixels, 0, width, 0, 0, width, height);
        int transparente = Color.argb(0,0,0,0);

        for(int x = 0; x < pixels.length; ++x) {
            //chequear si el pixel dado no es transparente.
            if (pixels[x] != 0){
            float[] hsv = new float[3];
            Color.colorToHSV(pixels[x], hsv);
            pixels[x] = (((hsv[0] >= hsv_in[0] && hsv[0] <= hsv_out[0]) && (hsv[1] >= hsv_in[1] && hsv[1] <= hsv_out[1]) && (hsv[2] >= hsv_in[2] && hsv[2] <= hsv_out[2])) || ((hsv[0] >= hsv_in_2[0] && hsv[0] <= hsv_out_2[0]) && (hsv[1] >= hsv_in_2[1] && hsv[1] <= hsv_out_2[1]) && (hsv[2] >= hsv_in_2[2] && hsv[2] <= hsv_out_2[2]))) ? pixels[x] : transparente;
            //pixels[x] = (pixels[x] >= ran_in && pixels[x] <= ran_out) ? pixels[x] : transparente;
            }
        }

        Bitmap newImage = Bitmap.createBitmap(width, height, mImage.getConfig());
        newImage.setPixels(pixels, 0, width, 0, 0, width, height);
        return newImage;
    }

    private void update_tint(Bitmap base_bitmap, Bitmap original_bitmap, float hue, int saturation, int brightness){
        bm_layer_tint = aplicar_filtro(base_bitmap, original_bitmap, hue, brightness, saturation);

        //x = Integer.valueOf(configs[last_kind][0]);
        //y = Integer.valueOf(configs[last_kind][1]);
        //w = Integer.valueOf(configs[last_kind][2]);
        //h = Integer.valueOf(configs[last_kind][3]);
        //bm_layer_tint = generar_capa(base, aplicar_filtro(base_bitmap, original_bitmap, hue, brightness, saturation), x, y, w, h);
        iv_render_preview.setImageBitmap(bm_layer_tint);
    }

    private void update_hsv_value(String hue, String saturation, String brightness){
        tv_tint_hue_value.setText(hue);
        tv_tint_saturacion_value.setText(saturation);
        tv_tint_brightness_value.setText(brightness);
    }

    private void preview_render(int kind) {
        Bitmap selected_bm = base;
        switch (kind) {
            case 1:
                selected_bm = generar_capa(base, bm_layerbackground, Integer.valueOf(configs[1][0]), Integer.valueOf(configs[1][1]), Integer.valueOf(configs[1][2]), Integer.valueOf(configs[1][3]));
                break;
            case 2:
                selected_bm = generar_capa(base, bm_layerbackacc2, Integer.valueOf(configs[2][0]), Integer.valueOf(configs[2][1]), Integer.valueOf(configs[2][2]), Integer.valueOf(configs[2][3]));
                break;
            case 3:
                selected_bm = generar_capa(base, bm_layerbackacc2, Integer.valueOf(configs[3][0]), Integer.valueOf(configs[3][1]), Integer.valueOf(configs[3][2]), Integer.valueOf(configs[3][3]));
                break;
            case 4:
                selected_bm = generar_capa(base, bm_layerback, Integer.valueOf(configs[4][0]), Integer.valueOf(configs[4][1]), Integer.valueOf(configs[4][2]), Integer.valueOf(configs[4][3]));
                break;
            case 5:
                selected_bm = generar_capa(base, bm_boca, Integer.valueOf(configs[5][0]), Integer.valueOf(configs[5][1]), Integer.valueOf(configs[5][2]), Integer.valueOf(configs[5][3]));
                break;
            // 6 bangs 7 cheeks
            case 6:
                selected_bm = generar_capa(base, bm_bangs, Integer.valueOf(configs[6][0]), Integer.valueOf(configs[6][1]), Integer.valueOf(configs[6][2]), Integer.valueOf(configs[6][3]));
                break;
            case 7:
                selected_bm = generar_capa(base, bm_cheeks, Integer.valueOf(configs[7][0]), Integer.valueOf(configs[7][1]), Integer.valueOf(configs[7][2]), Integer.valueOf(configs[7][3]));
                break;
            case 8:
                selected_bm = generar_capa(base, bm_fhair, Integer.valueOf(configs[8][0]), Integer.valueOf(configs[8][1]), Integer.valueOf(configs[8][2]), Integer.valueOf(configs[8][3]));
                break;
            case 9:
                selected_bm = generar_capa(base, bm_ojos, Integer.valueOf(configs[9][0]), Integer.valueOf(configs[9][1]), Integer.valueOf(configs[9][2]), Integer.valueOf(configs[9][3]));
                break;
            case 10:
                selected_bm = generar_capa(base, bm_layerfront, Integer.valueOf(configs[10][0]), Integer.valueOf(configs[10][1]), Integer.valueOf(configs[10][2]), Integer.valueOf(configs[10][3]));
                break;
            case 11:
                selected_bm = generar_capa(base, bm_layerfrontall, Integer.valueOf(configs[11][0]), Integer.valueOf(configs[11][1]), Integer.valueOf(configs[11][2]), Integer.valueOf(configs[11][3]));
                break;
            case 12:
                selected_bm = generar_capa(base, bm_backhair, Integer.valueOf(configs[12][0]), Integer.valueOf(configs[12][1]), Integer.valueOf(configs[12][2]), Integer.valueOf(configs[12][3]));
                break;
            case 13:
                selected_bm = generar_capa(base, bm_faceaccs, Integer.valueOf(configs[13][0]), Integer.valueOf(configs[13][1]), Integer.valueOf(configs[13][2]), Integer.valueOf(configs[13][3]));
                break;
            case 14:
                selected_bm = generar_capa(base, bm_hairaccs, Integer.valueOf(configs[14][0]), Integer.valueOf(configs[14][1]), Integer.valueOf(configs[14][2]), Integer.valueOf(configs[14][3]));
                break;
        }
        iv_render_preview.setImageBitmap(selected_bm);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                try {
                    InputStream is = getContext().getContentResolver().openInputStream(data.getData());
                    int kind = PICK_KIND;
                    load_imported(kind, is);
                    if (iv_layer_preview != null) {
                        iv_layer_preview.setImageBitmap(resize(get_topreview(kind, false), iv_layer_preview.getWidth(), iv_layer_preview.getHeight()));
                    }
                    mostrar_layers();
                    configs[kind][5] = "1";
                    pre_calculo(false);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        if (requestCode == 332 && resultCode == Activity.RESULT_OK){
            //aca se reciben los bitmaps guardados y se cargan.
            put_array_to_imageviews();
            configs = pub_configs.clone();

            face_tam = configs[0][2];
            face_skin = configs[0][3];
            face_exp = configs[0][4];
            configs_cb1 = pub_render_configs[0].equals("true");
            configs_cb2 = pub_render_configs[1].equals("true");
            hvalue = pub_render_configs[2];

            //cargar nombre del diseño.
            tv_conj_name.setText("Capas de: " +"\u3010" + design_name + "\u3011");

            pre_calculo(true);
        }

    }

    private void put_tolayer(int kind, Bitmap bitmap) {
        //Log.d("kind", "layerkind: " + kind);
        switch (kind) {
            case 1:
                bm_layerbackground = bitmap;
                break;
            case 2:
                bm_layerbackacc2 = bitmap;
                break;
            case 3:
                bm_layerbackacc = bitmap;
                break;
            case 4:
                bm_layerback = bitmap;
                break;
            case 5:
                bm_layercenter = bitmap;
                break;
            case 6:
                bm_layerbangs = bitmap;
                break;
            case 7:
                bm_cheeks = bitmap;
                break;
            case 8:
                bm_layerhair = bitmap;
                break;
            case 9:
                bm_layereyes = bitmap;
                break;
            case 10:
                bm_layerfront = bitmap;
                break;
            case 11:
                bm_layerfrontall = bitmap;
                break;
            case 12:
                bm_hairback = bitmap;
                break;
            case 13:
                bm_faceaccs = bitmap;
                break;
            case 14:
                bm_hairaccs = bitmap;
                break;
        }
    }

    private boolean kind_framed_for_raw(int kind) {
        switch (kind) {
            case 5:
                return false;
            case 6:
                return false;
            case 8:
                return false;
            case 9:
                return false;
            case 12:
                return false;
        }
        return true;
    }

    private Bitmap get_topreview(int kind, boolean noraw) {
        switch (kind) {
            case 1:
                return bm_layerbackground;
            case 2:
                return bm_layerbackacc2;
            case 3:
                return bm_layerbackacc;
            case 4:
                return bm_layerback;
            case 5:
                if (noraw) {
                    return bm_boca;
                } else {
                    return bm_layercenter;
                }
            case 6:
                if (noraw){
                    return bm_bangs;
                } else {
                    return bm_layerbangs;
                }
            case 7:
                //el check no necesita chequear el raw, solo tiene un frame.
                return bm_cheeks;
            case 8:
                if (noraw) {
                    return bm_fhair;
                } else {
                    return bm_layerhair;
                }
            case 9:
                if (noraw) {
                    return bm_ojos;
                } else {
                    return bm_layereyes;
                }
            case 10:
                return bm_layerfront;
            case 11:
                return bm_layerfrontall;
            case 12:
                if (noraw) {
                    return bm_backhair;
                } else {
                    return bm_hairback;
                }
            case 13:
                return bm_faceaccs;
            case 14:
                return bm_hairaccs;
        }
        return null;
    }

    private void load_imported(int kind, InputStream is) {
        switch (kind) {
            case 1:
                bm_layerbackground = BitmapFactory.decodeStream(is);
                break;
            case 2:
                bm_layerbackacc2 = BitmapFactory.decodeStream(is);
                break;
            case 3:
                bm_layerbackacc = BitmapFactory.decodeStream(is);
                break;
            case 4:
                bm_layerback = BitmapFactory.decodeStream(is);
                break;
            case 5:
                bm_layercenter = BitmapFactory.decodeStream(is);
                break;
            case 6:
                bm_layerbangs = BitmapFactory.decodeStream(is);
                break;
            case 7:
                bm_cheeks = BitmapFactory.decodeStream(is);
                break;
            case 8:
                bm_layerhair = BitmapFactory.decodeStream(is);
                break;
            case 9:
                bm_layereyes = BitmapFactory.decodeStream(is);
                break;
            case 10:
                bm_layerfront = BitmapFactory.decodeStream(is);
                break;
            case 11:
                bm_layerfrontall = BitmapFactory.decodeStream(is);
                break;
            case 12:
                bm_hairback = BitmapFactory.decodeStream(is);
                break;
            case 13:
                bm_faceaccs = BitmapFactory.decodeStream(is);
                break;
            case 14:
                bm_hairaccs = BitmapFactory.decodeStream(is);
                break;
        }
    }

    private void long_del(int kind) {
        if (all_buttons){
            switch (kind) {
                case 1:
                    bm_layerbackground = base;
                    break;
                case 2:
                    bm_layerbackacc2 = base;
                    break;
                case 3:
                    bm_layerbackacc = base;
                    break;
                case 4:
                    bm_layerback = base;
                    bm_head = base;
                    break;
                case 5:
                    bm_layercenter = base;
                    bm_boca = base;
                    break;
                case 6:
                    bm_layerbangs = base;
                    bm_bangs = base;
                    break;
                case 7:
                    bm_cheeks = base;
                    break;
                case 8:
                    bm_layerhair = base;
                    bm_fhair = base;
                    break;
                case 9:
                    bm_layereyes = base;
                    bm_ojos = base;
                    break;
                case 10:
                    bm_layerfront = base;
                    break;
                case 11:
                    bm_layerfrontall = base;
                    break;
                case 12:
                    bm_hairback = base;
                    bm_backhair = base;
                    break;
                case 13:
                    bm_faceaccs = base;
                    break;
                case 14:
                    bm_hairaccs = base;
                    break;
            }
            configs[kind][5] = "1";
            mostrar_layers();
            armar_todo(configs, false);
        } else {
            new CustomToast(getContext(), "Espere por favor.", Toast.LENGTH_SHORT, Gravity.BOTTOM);
        }
    }

    private void set_global_data() {
        outfit = avatarview.outfit;
        bkg = avatarview.bkg;
        bang = avatarview.bang;
        hair = avatarview.hair;
        hair2 = avatarview.hair2;
        face_eye1 = avatarview.face_eye1; //7
        face_eye2 = avatarview.face_eye2;
        hairacc1 = avatarview.hairacc1;
        hairacc2 = avatarview.hairacc2;
        bodyacc1 = avatarview.bodyacc1;
        bodyacc2 = avatarview.bodyacc2;
        faceacc1 = avatarview.faceacc1;
        faceacc2 = avatarview.faceacc2;
        backacc1 = avatarview.backacc1;
        backacc2 = avatarview.backacc2;
        front = avatarview.front;

        face_exp = avatarview.face_exp;
        face_skin = avatarview.face_skin;
        face_tam = avatarview.face_tam;
        face_pup1 = avatarview.face_pup1;
        face_pup2 = avatarview.face_pup2;
        face_blush = avatarview.face_blush;
        face_cej = avatarview.face_cej;

        tv_conj_name.setText("Capas de: " +"\u3010" + avatarview.setinfo + "\u3011");

        // configs posicion_x, posicion_y, w, h, visible, update
        configs = new String[][] {
                {"0", "0", face_tam, face_skin, face_exp, "0"}, //0 kind //0 ojo 1 boca 2 -height- 3 -skin- 4 -exp-
                {"0", "0", "640", "854", "1", "0"}, //1 layerbackground
                {"0", "0", "640", "854", "1", "0"}, //2 layerbackacc2
                {"0", "0", "640", "854", "1", "0"}, //3 layerbackacc
                {"0", "0", "640", "854", "1", "0"}, //4 layerback
                {"0", "0", "640", "854", "1", "0"}, //5 layercenter
                {"0", "0", "640", "854", "1", "0"}, //6 layerbangs
                {"0", "0", "640", "854", "1", "0"}, //7 layercheeks
                {"0", "0", "640", "854", "1", "0"}, //8 layerhair
                {"0", "0", "640", "854", "1", "0"}, //9 layereyes
                {"0", "0", "640", "854", "1", "0"}, //10 layerfront
                {"0", "0", "640", "854", "1", "0"}, //11 layerfrontall
                {"0", "0", "640", "854", "1", "0"}, //12 hairback
                {"0", "0", "640", "854", "1", "0"}, //13 faceaccs
                {"0", "0", "640", "854", "1", "0"}, //14 hairaccs
                {"0", "0", "640", "854", "1", "0"} //15 head
        };

    }

    private void on_click_avarender() {
        if (cl_comps.getVisibility() == View.VISIBLE) {
            //ocultar
            cl_comps.setVisibility(View.GONE);
            cl_des_basebar.setVisibility(View.VISIBLE);
            Act_Main.navigation.setVisibility(View.VISIBLE);
            //estirar la cl_container hasta el parent.
            ConstraintSet cset = new ConstraintSet();
            cset.clone(cl_design_base_container);
            cset.connect(R.id.cl_design_base, ConstraintSet.BOTTOM, R.id.cl_des_basebar, ConstraintSet.TOP, 0);
            cset.applyTo(cl_design_base_container);
        } else {
            //mostrar
            cl_comps.setVisibility(View.VISIBLE);
            cl_des_basebar.setVisibility(View.GONE);
            Act_Main.navigation.setVisibility(View.GONE);
            //estirar la cl_container hasta la cl_sets.
            ConstraintSet cset = new ConstraintSet();
            cset.clone(cl_design_base_container);
            cset.connect(R.id.cl_design_base, ConstraintSet.BOTTOM, R.id.cl_comps, ConstraintSet.TOP, 0);
            cset.applyTo(cl_design_base_container);
        }

    }

    public String generate_ids(int type, String id) {
        switch (type) {
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
                gen3 = gen3.substring(0, gen3.length() - 1);
                return gen3;
            case 4:
                //format x1x2x3x4
                char[] chars4 = id.toCharArray();
                String gen4 = "x";
                for (char aChar : chars4) {
                    gen4 = gen4 + String.valueOf(aChar) + "x";
                }
                gen4 = gen4.substring(0, gen4.length() - 1);
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

    private int get_face_single(String facepos) {
        if (facepos.equals("0") || facepos.equals("00") || facepos.equals("01")) {
            return 0;
        }
        if (facepos.equals("10") || facepos.equals("11")) {
            return 1;
        }
        if (facepos.equals("20") || facepos.equals("21")) {
            return 2;
        }
        if (facepos.equals("30") || facepos.equals("31")) {
            return 3;
        }
        if (facepos.equals("40") || facepos.equals("41")) {
            return 4;
        }
        if (facepos.equals("50") || facepos.equals("51")) {
            return 5;
        }
        if (facepos.equals("60") || facepos.equals("61")) {
            return 6;
        }
        if (facepos.equals("70") || facepos.equals("71")) {
            return 7;
        }
        if (facepos.equals("80") || facepos.equals("81")) {
            return 8;
        }
        if (facepos.equals("90") || facepos.equals("91")) {
            return 9;
        }

        return 1;
    }
    private String get_expressions_single(String face_exp) {
        String[] c_exp = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "011", "21", "22", "24", "25", "26", "27", "28", "29", "30", "32", "33", "34", "35", "012", "013", "014"};

        if (face_exp.equals("10")) {
            return c_exp[0];
        }
        if (face_exp.equals("20")) {
            return c_exp[1];
        }
        if (face_exp.equals("30")) {
            return c_exp[2];
        }
        if (face_exp.equals("40")) {
            return c_exp[3];
        }
        if (face_exp.equals("50")) {
            return c_exp[4];
        }
        if (face_exp.equals("60")) {
            return c_exp[5];
        }
        if (face_exp.equals("70")) {
            return c_exp[6];
        }
        if (face_exp.equals("80")) {
            return c_exp[7];
        }
        if (face_exp.equals("90")) {
            return c_exp[8];
        }
        if (face_exp.equals("111")) {
            return c_exp[9];
        }
        if (face_exp.equals("211")) {
            return c_exp[10];
        }
        if (face_exp.equals("221")) {
            return c_exp[11];
        }
        if (face_exp.equals("241")) {
            return c_exp[12];
        }
        if (face_exp.equals("251")) {
            return c_exp[13];
        }
        if (face_exp.equals("261")) {
            return c_exp[14];
        }
        if (face_exp.equals("271")) {
            return c_exp[15];
        }
        if (face_exp.equals("281")) {
            return c_exp[16];
        }
        if (face_exp.equals("291")) {
            return c_exp[17];
        }
        if (face_exp.equals("301")) {
            return c_exp[18];
        }
        if (face_exp.equals("321")) {
            return c_exp[19];
        }
        if (face_exp.equals("331")) {
            return c_exp[20];
        }
        if (face_exp.equals("341")) {
            return c_exp[21];
        }
        if (face_exp.equals("351")) {
            return c_exp[22];
        }
        if (face_exp.equals("120")) {
            return c_exp[23];
        }
        if (face_exp.equals("130")) {
            return c_exp[24];
        }
        if (face_exp.equals("140")) {
            return c_exp[25];
        }

        return "1";
    }

    private void render_avatar() {
        String server = avatarview.a_server[avatarview.server];
        String layer_background = "http://" + server + "/img/php/avatar.php?id1=" + generate_ids(5, bkg) + "&id2=1";
        String layer_backacc_url = "http://" + server + "/img/php/avatar.php?id1=" + generate_ids(5, backacc1) + "&id3=1x0x1&id4=11&id5=11&id2=014";
        String layer_backacc2_url = "http://" + server + "/img/php/avatar.php?id1=" + generate_ids(5, backacc2) + "&id3=1x0x1&id4=11&id5=11&id2=014";
        String layer_back_url = "http://" + server + "/img/php/blend_back_param.php?&id1=0x," + generate_ids(1, outfit) + "," + generate_ids(1, bodyacc1) + "," + generate_ids(1, bodyacc2) + "," + generate_ids(1, hairacc1) + "," + generate_ids(1, hairacc2) + "," + generate_ids(1, faceacc1) + "," + generate_ids(1, faceacc2) + "," + "0" + "," + "0" + "," + generate_ids(1, bkg) + "," + "x0x" + "," + generate_ids(1, face_eye1) + "," + generate_ids(1, face_eye2) + "," + "x0x" + ",0,0,0,0,0," + generate_ids(1, face_pup1) + "," + generate_ids(1, face_pup2) + ",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,x0x1&id2=" + face_skin + "&id3=" + face_tam + "&id4=01";
        String layer_front_url = "http://" + server + "/img/php/blend_front_param.php?&id1=0x," + generate_ids(1, outfit) + "," + generate_ids(1, bodyacc1) + "," + generate_ids(1, bodyacc2) + "," + "x0x" + "," + "x0x" + "," + "x0x" + "," + "x0x" + "," + generate_ids(1, backacc1) + "," + generate_ids(1, backacc2) + "," + generate_ids(1, bkg) + "," + "x0x" + "," + generate_ids(1, face_eye1) + "," + generate_ids(1, face_eye2) + "," + generate_ids(1, hair2) + ",0,0,0,0,0," + generate_ids(1, face_pup1) + "," + generate_ids(1, face_pup2) + ",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,x0x1&id2=" + face_skin + "&id3=" + face_tam + "&id4=01";
        String layer_bangs_url = "http://" + server + "/img/php/bangs.php?id1=" + bang + "&id2=" + get_face_single(face_skin);
        String layer_cheeks_url = "http://" + server + "/img/avatar/cheek/" + get_face_single(face_skin) + "/cheek01.png";
        String layer_cheeks2_url = "http://" + server + "/img/avatar/cheek/" + get_face_single(face_skin) + "/cheek02.png";
        String layer_ojos_url = "http://" + server + "/img/php/eye.php?id1=" + face_eye1 + "&id2=" + face_eye2 + "&id3=" + get_expressions_single(face_exp) + "&id4=0&eye_option_id=" + face_pup1 + "&eye_option_id2=" + face_pup2 + "&id5=" + get_face_single(face_cej);
        String layer_mouth_url = "http://" + server + "/img/php/mouth.php?id1=" + get_expressions_single(face_exp) + "&id2=" + front + "," + outfit + "," + bodyacc1 + "," + bodyacc2 + "," + hairacc1 + "," + hairacc2 + "," + faceacc1 + "," + faceacc2 + "," + backacc1 + "," + backacc2 + "," + bkg + "," + hair + "," + face_eye1 + "," + face_eye2 + "," + hair2 + ",0,0,0,0,0," + face_pup1 + "," + face_pup2 + ",0&skin_id=" + get_face_single(face_skin);
        String layer_frontall_url = "http://" + server + "/img/php/front.php?&id1=" + generate_ids(2, front) + "," + generate_ids(1, outfit) + "," + generate_ids(1, bodyacc1) + "," + generate_ids(1, bodyacc2) + "," + generate_ids(1, hairacc1) + "," + generate_ids(1, hairacc2) + "," + generate_ids(1, faceacc1) + "," + generate_ids(1, faceacc2) + "," + generate_ids(1, backacc1) + "," + generate_ids(1, backacc2) + "," + generate_ids(1, bkg) + "," + generate_ids(1, hair) + "," + generate_ids(1, face_eye1) + "," + generate_ids(1, face_eye2) + "," + generate_ids(1, face_eye2) + ",0,0,0,0,0," + generate_ids(1, face_pup1) + "," + generate_ids(1, face_pup1) + ",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,x0x1&id2=" + generate_ids(1, face_skin) + "&id3=" + generate_ids(1, face_tam) + "&id4=01";
        String layer_hair_url = "http://" + server + "/img/php/blend_front_param.php?&id1=0x,x5x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x," + generate_ids(1, hair) + ",x0x,x0x,x0x,0,0,0,0,0,x0x,x0x,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,x0x1&id2=21&id3=11&id4=01";
        String layer_hairback_url = "http://" + server + "/img/php/live2d/app_get_resouce2.php?key5=0,0,0,0,0,0,0,0,0,0,0,0," + hair + ",753,753," + hair2 + ",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0&type=11&key1=" + hair;
        String layer_faceaccs_url = "http://" + server + "/img/php/blend_front_param.php?&id1=0x," + "x5x" + "," + "x0x" + "," + "x0x" + "," + "x1x" + "," + "x1x" + "," + generate_ids(1, faceacc1) + "," + generate_ids(1, faceacc2) + "," + "x0x" + "," + "x0x" + "," + "x0x" + "," + "x0x" + "," + generate_ids(1, face_eye1) + "," + generate_ids(1, face_eye2) + "," + generate_ids(1, hair2) + ",0,0,0,0,0," + generate_ids(1, face_pup1) + "," + generate_ids(1, face_pup2) + ",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,x0x1&id2=" + face_skin + "&id3=" + face_tam + "&id4=01";
        String layer_hairaccs_url = "http://" + server + "/img/php/blend_front_param.php?&id1=0x," + "x5x" + "," + "x0x" + "," + "x0x" + "," + generate_ids(1, hairacc1) + "," + generate_ids(1, hairacc2) + "," + "x0x" + "," + "x0x" + "," + "x0x" + "," + "x0x" + "," + generate_ids(1, bkg) + "," + "x0x" + "," + generate_ids(1, face_eye1) + "," + generate_ids(1, face_eye2) + "," + generate_ids(1, hair2) + ",0,0,0,0,0," + generate_ids(1, face_pup1) + "," + generate_ids(1, face_pup2) + ",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,x0x1&id2=" + face_skin + "&id3=" + face_tam + "&id4=01";
        String layer_head_url = "http://" + server + "/img/php/blend_back_param.php?&id1=0x," + "x0x" + "," + "x0x" + "," + "x0x" + "," + "x0x" + "," + "x0x" + "," + "x0x" + "," + "x0x" + "," + "0" + "," + "0" + "," + generate_ids(1, bkg) + "," + "x0x" + "," + generate_ids(1, face_eye1) + "," + generate_ids(1, face_eye2) + "," + "x0x" + ",0,0,0,0,0," + generate_ids(1, face_pup1) + "," + generate_ids(1, face_pup2) + ",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,x0x1&id2=" + face_skin + "&id3=" + face_tam + "&id4=01";


        Log.d("dream_designer", "background: " + layer_background);
        Log.d("dream_designer", "backacc: " + layer_backacc_url);
        Log.d("dream_designer", "backacc2: " + layer_backacc2_url);
        Log.d("dream_designer", "back: " + layer_back_url);
        Log.d("dream_designer", "front: " + layer_front_url);
        Log.d("dream_designer", "bangs: " + layer_bangs_url);
        Log.d("dream_designer", "cheeks: " + layer_cheeks_url);
        Log.d("dream_designer", "cheeks2: " + layer_cheeks2_url);
        Log.d("dream_designer", "ojos: " + layer_ojos_url);
        Log.d("dream_designer", "mouth: " + layer_mouth_url);
        Log.d("dream_designer", "frontall: " + layer_frontall_url);
        Log.d("dream_designer", "hair: " + layer_hair_url);
        Log.d("dream_designer", "hairback: " + layer_hairback_url);
        Log.d("dream_designer", "faceaccs: " + layer_faceaccs_url);
        Log.d("dream_designer", "hairaccs: " + layer_hairaccs_url);
        Log.d("dream_designer", "head: " + layer_head_url);


        String[] urls = {
                layer_background, //0
                layer_backacc_url, //1
                layer_backacc2_url, //2
                layer_back_url, //3
                layer_front_url, //4
                layer_bangs_url, //5
                layer_cheeks_url, //6
                layer_cheeks2_url, //7
                layer_ojos_url, //8
                layer_mouth_url, //9
                layer_frontall_url, //10
                layer_hair_url, //11
                layer_hairback_url, //12
                layer_faceaccs_url, //13
                layer_hairaccs_url, //14
                layer_head_url //15
        };

        new task_getpictures().execute(urls);

    }

    private class task_getpictures extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cl_design_loading.setVisibility(View.VISIBLE);
            //deshabilitar los botones.
            bt_comp_config_active = false;
            bt_comp_list_active = false;
            all_buttons = false;

        }

        @Override
        protected String doInBackground(String... params) {

            //kind
            //background 1
            //backacc2 2
            //backacc 3
            //back 4
            //center 5
            //bangs 6
            //cheeks 7
            //hair 8
            //eyes 9
            //front 10
            //frontall 11
            //hairback 12
            //faceaccs 13
            //hairaccs 14

            if (!locked_layers[1]){
                if (!cancel_loading){
                    try {
                        bm_layerbackground = Picasso.get().load(params[0]).get();
                    } catch (IOException e) {
                        bm_layerbackground = Bitmap.createBitmap(base);
                    }
                } else {
                    bm_layerbackground = Bitmap.createBitmap(base);
                }
            }

            if (!locked_layers[2]){
                if (!cancel_loading){
                    try {
                        bm_layerbackacc2 = Picasso.get().load(params[2]).get();
                    } catch (IOException e) {
                        bm_layerbackacc2 = Bitmap.createBitmap(base);
                    }
                } else {
                    bm_layerbackacc2 = Bitmap.createBitmap(base);
                }
            }

            if (!locked_layers[3]){
                if (!cancel_loading){
                    try {
                        bm_layerbackacc = Picasso.get().load(params[1]).get();
                    } catch (IOException e) {
                        bm_layerbackacc = Bitmap.createBitmap(base);
                    }
                } else {
                    bm_layerbackacc = Bitmap.createBitmap(base);
                }
            }


            if (!locked_layers[4]){
                if (!cancel_loading){
                    try {
                        bm_layerback = Picasso.get().load(params[3]).get();
                    } catch (IOException e) {
                        bm_layerback = Bitmap.createBitmap(base);
                    }
                } else {
                    bm_layerback = Bitmap.createBitmap(base);
                }
            }

            if (!locked_layers[5]){
                if (!cancel_loading){
                    try {
                        bm_layercenter = Picasso.get().load(params[9]).get();
                    } catch (IOException e) {
                        bm_layercenter = Bitmap.createBitmap(base);
                    }
                } else {
                    bm_layercenter = Bitmap.createBitmap(base);
                }
            }

            if (!locked_layers[6]){
                if (!cancel_loading){
                    try {
                        bm_layerbangs = Picasso.get().load(params[5]).get();
                    } catch (IOException e) {
                        bm_layerbangs = Bitmap.createBitmap(base);
                    }
                } else {
                    bm_layerbangs = Bitmap.createBitmap(base);
                }
            }

            if (!locked_layers[7]){
                if (!cancel_loading){
                    try {
                        bm_layercheeks = Picasso.get().load(params[6]).get();
                    } catch (IOException e) {
                        bm_layercheeks = Bitmap.createBitmap(base);
                    }
                } else {
                    bm_layercheeks = Bitmap.createBitmap(base);
                }

                if (!cancel_loading){
                    try {
                        bm_layercheeks2 = Picasso.get().load(params[7]).get();
                    } catch (IOException e) {
                        bm_layercheeks2 = Bitmap.createBitmap(base);
                    }
                } else {
                    bm_layercheeks2 = Bitmap.createBitmap(base);
                }
            }

            if (!locked_layers[8]){
                if (!cancel_loading){
                    try {
                        bm_layerhair = Picasso.get().load(params[11]).get();
                    } catch (IOException e) {
                        bm_layerhair = Bitmap.createBitmap(base);
                    }
                } else {
                    bm_layerhair = Bitmap.createBitmap(base);
                }
            }

            if (!locked_layers[9]){
                if (!cancel_loading){
                    try {
                        bm_layereyes = Picasso.get().load(params[8]).get();
                    } catch (IOException e) {
                        bm_layereyes = Bitmap.createBitmap(base);
                    }
                } else {
                    bm_layereyes = Bitmap.createBitmap(base);
                }
            }

            if (!locked_layers[10]){
                if (!cancel_loading){
                    try {
                        bm_layerfront = Picasso.get().load(params[4]).get();
                    } catch (IOException e) {
                        bm_layerfront = Bitmap.createBitmap(base);
                    }
                } else {
                    bm_layerfront = Bitmap.createBitmap(base);
                }
            }

            if (!locked_layers[11]){
                if (!cancel_loading){
                    try {
                        bm_layerfrontall = Picasso.get().load(params[10]).get();
                    } catch (IOException e) {
                        bm_layerfrontall = Bitmap.createBitmap(base);
                    }
                } else {
                    bm_layerfrontall = Bitmap.createBitmap(base);
                }
            }


            if (!locked_layers[12]){
                if (!cancel_loading){
                    try {
                        bm_hairback = Picasso.get().load(params[12]).get();
                    } catch (IOException e) {
                        bm_hairback = Bitmap.createBitmap(base);
                    }
                } else {
                    bm_hairback = Bitmap.createBitmap(base);
                }
            }

            if (!locked_layers[13]){
                if (!cancel_loading){
                    try {
                        bm_faceaccs = Picasso.get().load(params[13]).get();
                    } catch (IOException e) {
                        bm_faceaccs = Bitmap.createBitmap(base);
                    }
                } else {
                    bm_faceaccs = Bitmap.createBitmap(base);
                }
            }

            if (!locked_layers[14]){
                if (!cancel_loading){
                    try {
                        bm_hairaccs = Picasso.get().load(params[14]).get();
                    } catch (IOException e) {
                        bm_hairaccs = Bitmap.createBitmap(base);
                    }
                } else {
                    bm_hairaccs = Bitmap.createBitmap(base);
                }
            }

            if (!locked_layers[4]){
                if (!cancel_loading){
                    try {
                        bm_head = Picasso.get().load(params[15]).get();
                    } catch (IOException e) {
                        bm_head = Bitmap.createBitmap(base);
                    }
                } else {
                    bm_head = Bitmap.createBitmap(base);
                }
            }

            cancel_loading = false;
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //activar los botones
            bt_comp_config_active = true;
            bt_comp_list_active = true;
            all_buttons = true;
            cl_design_loading.setVisibility(View.GONE);
            pre_calculo(true);

            if (is_lock_lay){
                locked_layers = temp_lock_lay.clone();
                is_lock_lay = false;
            }
        }
    }

    public static Bitmap aplicar_filtro(Bitmap basebitmap, Bitmap bitmap, float hue, int brigh, int saturation){
        Canvas canvas = new Canvas(basebitmap);
        Paint paint = new Paint();
        paint.setColorFilter(ColorFilterGenerator.adjustColor(brigh, 0, saturation, (int) hue));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return basebitmap;
    }

    private void pre_calculo(Boolean full_load) {
        define_face();
        define_hairs();
        if (full_load){
            define_selected_cheek();
        }
        //dibujar
        armar_todo(configs, full_load);
    }

    private Bitmap check_boca_single(int frame_boca){
        //012 013 014 mouth
        Bitmap boca;
        String exp_single = get_expressions_single(face_exp);
        if (exp_single.equals("012") || exp_single.equals("013") || exp_single.equals("014")) {
            boca = bm_layercenter;
        } else {
            boca = frames_bmp(bm_layercenter, 2)[frame_boca];
        }
        return boca;
    }

    private Bitmap[] define_faceframes(String[][] configs) {
        int frame_ojos = Integer.valueOf(configs[0][0]);
        int frame_boca = Integer.valueOf(configs[0][1]);
        Bitmap f_ojos, f_boca;
        if (is_framed_expression(get_expressions_single(face_exp))) {
            f_ojos = frames_bmp(bm_layereyes, 1)[frame_ojos];
            f_boca = check_boca_single(frame_boca);
        } else {
            f_ojos = bm_layereyes;
            f_boca = check_boca_single(frame_boca);
        }
        return new Bitmap[]{f_ojos, f_boca};
    }

    private void define_selected_cheek() {
        int[] dim_cheeks = calcular_cheeks(face_tam);
        //caching
        if (bm_layercheeks == null){
            bm_layercheeks = base;
        }
        if (bm_layercheeks2 == null){
            bm_layercheeks2 = base;
        }
        Bitmap bm_cheeks_blushing = armar_capa(base, Bitmap.createScaledBitmap(bm_layercheeks, dim_cheeks[0], dim_cheeks[1], false), dim_cheeks[2], dim_cheeks[3]);
        Bitmap bm_cheeks_crying = armar_capa(base, Bitmap.createScaledBitmap(bm_layercheeks2, dim_cheeks[0], dim_cheeks[1], false), dim_cheeks[2], dim_cheeks[3]);
        Bitmap bm_cheeks_blushcrying = armar_capa(bm_cheeks_blushing, bm_cheeks_crying, 0, 0);

        switch (get_face_single(face_blush)) {
            case 0:
                //none
                bm_cheeks = base;
                break;
            case 1:
                //blushing
                bm_cheeks = bm_cheeks_blushing;
                break;
            case 2:
                //crying
                bm_cheeks = bm_cheeks_crying;
                break;
            case 3:
                //blushcrying
                bm_cheeks = bm_cheeks_blushcrying;
                break;
        }
    }

    private Bitmap get_faceframe(){
        Bitmap[] fram = define_faceframes(configs);
        int[] dim_ojos = calcular_ojos(face_tam);
        int[] dim_boca = calcular_boca(face_tam);

        Bitmap bojos = armar_capa(base, Bitmap.createScaledBitmap(fram[0], dim_ojos[0], dim_ojos[1], false), dim_ojos[2], dim_ojos[3]);
        Bitmap bboca = armar_capa(base, Bitmap.createScaledBitmap(fram[1], dim_boca[0], dim_boca[1], false), dim_boca[2], dim_boca[3]);
        Bitmap layer_eye = generar_capa(base, bojos, Integer.valueOf(configs[9][0]), Integer.valueOf(configs[9][1]), Integer.valueOf(configs[9][2]), Integer.valueOf(configs[9][3]));
        Bitmap layer_mouth = generar_capa(base, bboca, Integer.valueOf(configs[5][0]), Integer.valueOf(configs[5][1]), Integer.valueOf(configs[5][2]), Integer.valueOf(configs[5][3]));

        Bitmap[] render = {
                layer_eye,
                layer_mouth
        };

        return unir_todo(render);
    }

    private void define_face() {
        //armar capa de ojos
        Bitmap[] fram = define_faceframes(configs);
        int[] dim_ojos = calcular_ojos(face_tam);
        bm_ojos = armar_capa(base, Bitmap.createScaledBitmap(fram[0], dim_ojos[0], dim_ojos[1], false), dim_ojos[2], dim_ojos[3]);
        int[] dim_boca = calcular_boca(face_tam);
        bm_boca = armar_capa(base, Bitmap.createScaledBitmap(fram[1], dim_boca[0], dim_boca[1], false), dim_boca[2], dim_boca[3]);
        int[] dim_bangs = calcular_bangs(face_tam);
        bm_bangs = armar_capa(base, Bitmap.createScaledBitmap(bm_layerbangs, dim_bangs[0], dim_bangs[1], false), dim_bangs[2], dim_bangs[3]);
        //calculo de cheeks.
        //int[] dim_cheeks = calcular_cheeks(face_tam);
        //bm_cheeks = armar_capa(base, Bitmap.createScaledBitmap(bm_cheeks, dim_cheeks[0], dim_cheeks[1], false), dim_cheeks[2], dim_cheeks[3]);
    }

    private void define_hairs() {
        int[] dim_hair = calcular_hair(face_tam);
        //backhair
        bm_backhair = armar_capa(base, Bitmap.createScaledBitmap(cortar_capa(bm_hairback), dim_hair[0], dim_hair[1], false), dim_hair[2], dim_hair[3]);
        //fronthair
        bm_fhair = armar_capa(base, Bitmap.createScaledBitmap(bm_layerhair, dim_hair[0], dim_hair[1], false), dim_hair[2], dim_hair[3]);
    }

    private void armar_todo(String[][] configs, Boolean full_load) {
        //posicionado de capas en base a las configs
        if (configs[1][4].equals("1")) {
            if (configs[1][5].equals("1") || full_load) {
                bm_c_layerbackground = generar_capa(base, bm_layerbackground, Integer.valueOf(configs[1][0]), Integer.valueOf(configs[1][1]), Integer.valueOf(configs[1][2]), Integer.valueOf(configs[1][3]));
                configs[1][5] = "0";
            }
        } else if (configs[1][4].equals("0")) {
            bm_c_layerbackground = base;
        }

        if (configs[2][4].equals("1")) {
            if (configs[2][5].equals("1") || full_load) {
                bm_c_layerbackacc2 = generar_capa(base, bm_layerbackacc2, Integer.valueOf(configs[2][0]), Integer.valueOf(configs[2][1]), Integer.valueOf(configs[2][2]), Integer.valueOf(configs[2][3]));
                configs[2][5] = "0";
            }
        } else if (configs[2][4].equals("0")) {
            bm_c_layerbackacc2 = base;
        }

        if (configs[3][4].equals("1")) {
            if (configs[3][5].equals("1") || full_load) {
                bm_c_layerbackacc = generar_capa(base, bm_layerbackacc, Integer.valueOf(configs[3][0]), Integer.valueOf(configs[3][1]), Integer.valueOf(configs[3][2]), Integer.valueOf(configs[3][3]));
                configs[3][5] = "0";
            }
        } else if (configs[3][4].equals("0")) {
            bm_c_layerbackacc = base;
        }

        if (configs[4][4].equals("1")) {
            if (configs[4][5].equals("1") || full_load) {
                bm_c_layerback = generar_capa(base, bm_layerback, Integer.valueOf(configs[4][0]), Integer.valueOf(configs[4][1]), Integer.valueOf(configs[4][2]), Integer.valueOf(configs[4][3]));
                if (bm_head == null){
                    bm_head = base;
                }
                bm_c_head = generar_capa(base, bm_head, Integer.valueOf(configs[4][0]), Integer.valueOf(configs[4][1]), Integer.valueOf(configs[4][2]), Integer.valueOf(configs[4][3]));
                configs[4][5] = "0";
            }
        } else if (configs[4][4].equals("0")) {
            bm_c_layerback = base;
            bm_c_head = base;
        }

        if (configs[5][4].equals("1")) {
            if (configs[5][5].equals("1") || full_load) {
                bm_c_boca = generar_capa(base, bm_boca, Integer.valueOf(configs[5][0]), Integer.valueOf(configs[5][1]), Integer.valueOf(configs[5][2]), Integer.valueOf(configs[5][3]));
                configs[5][5] = "0";
            }
        } else if (configs[5][4].equals("0")) {
            bm_c_boca = base;
        }

        //bangs
        if (configs[6][4].equals("1")) {
            if (configs[6][5].equals("1") || full_load) {
                bm_c_bangs = generar_capa(base, bm_bangs, Integer.valueOf(configs[6][0]), Integer.valueOf(configs[6][1]), Integer.valueOf(configs[6][2]), Integer.valueOf(configs[6][3]));
                configs[6][5] = "0";
            }
        } else if (configs[6][4].equals("0")) {
            bm_c_bangs = base;
        }

        //cheeks
        if (configs[7][4].equals("1")) {
            if (configs[7][5].equals("1") || full_load) {
                bm_c_cheeks = generar_capa(base, bm_cheeks, Integer.valueOf(configs[7][0]), Integer.valueOf(configs[7][1]), Integer.valueOf(configs[7][2]), Integer.valueOf(configs[7][3]));
                configs[7][5] = "0";
            }
        } else if (configs[7][4].equals("0")) {
            bm_c_cheeks = base;
        }

        if (configs[8][4].equals("1")) {
            if (configs[8][5].equals("1") || full_load) {
                bm_c_layerhair = generar_capa(base, bm_fhair, Integer.valueOf(configs[8][0]), Integer.valueOf(configs[8][1]), Integer.valueOf(configs[8][2]), Integer.valueOf(configs[8][3]));
                configs[8][5] = "0";
            }
        } else if (configs[8][4].equals("0")) {
            bm_c_layerhair = base;
        }

        if (configs[9][4].equals("1")) {
            if (configs[9][5].equals("1") || full_load) {
                bm_c_layereyes = generar_capa(base, bm_ojos, Integer.valueOf(configs[9][0]), Integer.valueOf(configs[9][1]), Integer.valueOf(configs[9][2]), Integer.valueOf(configs[9][3]));
                configs[9][5] = "0";
            }
        } else if (configs[9][4].equals("0")) {
            bm_c_layereyes = base;
        }

        if (configs[10][4].equals("1")) {
            if (configs[10][5].equals("1") || full_load) {
                bm_c_layerfront = generar_capa(base, bm_layerfront, Integer.valueOf(configs[10][0]), Integer.valueOf(configs[10][1]), Integer.valueOf(configs[10][2]), Integer.valueOf(configs[10][3]));
                configs[10][5] = "0";
            }
        } else if (configs[10][4].equals("0")) {
            bm_c_layerfront = base;
        }

        if (configs[11][4].equals("1")) {
            if (configs[11][5].equals("1") || full_load) {
                bm_c_layerfrontall = generar_capa(base, bm_layerfrontall, Integer.valueOf(configs[11][0]), Integer.valueOf(configs[11][1]), Integer.valueOf(configs[11][2]), Integer.valueOf(configs[11][3]));
                configs[11][5] = "0";
            }
        } else if (configs[11][4].equals("0")) {
            bm_c_layerfrontall = base;
        }

        if (configs[12][4].equals("1")) {
            if (configs[12][5].equals("1") || full_load) {
                bm_c_layerhairback = generar_capa(base, bm_backhair, Integer.valueOf(configs[12][0]), Integer.valueOf(configs[12][1]), Integer.valueOf(configs[12][2]), Integer.valueOf(configs[12][3]));
                configs[12][5] = "0";
            }
        } else if (configs[12][4].equals("0")){
            bm_c_layerhairback = base;
        }

        if (configs[13][4].equals("1")){
            if (configs[13][5].equals("1") || full_load){
                bm_c_faceaccs = generar_capa(base, bm_faceaccs, Integer.valueOf(configs[13][0]), Integer.valueOf(configs[13][1]), Integer.valueOf(configs[13][2]), Integer.valueOf(configs[13][3]));
                configs[13][5] = "0";
            }
        } else if (configs[13][4].equals("0")){
            bm_c_faceaccs = base;
        }

        if (configs[14][4].equals("1")){
            if (configs[14][5].equals("1") || full_load){
                bm_c_hairaccs = generar_capa(base, bm_hairaccs, Integer.valueOf(configs[14][0]), Integer.valueOf(configs[14][1]), Integer.valueOf(configs[14][2]), Integer.valueOf(configs[14][3]));
                configs[14][5] = "0";
            }
        } else if (configs[14][4].equals("0")){
            bm_c_hairaccs = base;
        }

        //special_layerfront
        Bitmap bm_special_layerfront;
        //todo permitir manipulacion del height del special_layerfront.
        if (configs_cb2){
            int height = Integer.valueOf(hvalue);
            if (height > 0){
                bm_special_layerfront = Bitmap.createBitmap(bm_c_layerfront, 0, 0, 640, height);
            } else {
                bm_special_layerfront = base;
            }
        } else {
            bm_special_layerfront = base;
        }

        //orden de renderizado de las cosas.
        //todo permitir manipulacion de esto en la app.
        Bitmap[] bitmaps;
        if (configs_cb1){
            bitmaps = new Bitmap[]{
                    bm_c_layerbackground, //0
                    bm_c_layerbackacc2, //1
                    bm_c_layerbackacc, //2
                    bm_c_layerhairback, //3
                    bm_c_layerfront,//5
                    bm_c_layerback, //4
                    //bm_c_head, //6
                    bm_c_bangs, //8
                    bm_c_boca, //7
                    bm_c_cheeks, //10
                    bm_c_layereyes, //11
                    bm_c_faceaccs, //9
                    bm_c_layerhair, //12
                    bm_c_hairaccs,//13
                    bm_special_layerfront,
                    bm_c_layerfrontall //14
            };
        } else {
            bitmaps = new Bitmap[]{
                    bm_c_layerbackground, //0
                    bm_c_layerbackacc2, //1
                    bm_c_layerbackacc, //2
                    bm_c_layerhairback, //3
                    bm_c_layerback, //4
                    bm_c_layerfront,//5
                    //bm_c_head, //6
                    bm_c_bangs, //8
                    bm_c_boca, //7
                    bm_c_cheeks, //10
                    bm_c_layereyes, //11
                    bm_c_faceaccs, //9
                    bm_c_layerhair, //12
                    bm_c_hairaccs,//13
                    bm_special_layerfront,
                    bm_c_layerfrontall //14
            };

            for (int i=0; i < bitmaps.length; i++){
                if (bitmaps[i] == null){
                    Log.d("waifudata", "este es nulo: " + i);
                }
            }

        }

        iv_render.setImageBitmap(unir_todo(bitmaps));
        //global_layers_array = bitmaps;
        mostrar_layers();

    }

    private void mostrar_layers(){
        iv_comp_layerbackground.setImageBitmap(resize(bm_layerbackground, iv_comp_layerbackground.getWidth(), iv_comp_layerbackground.getHeight()));
        iv_comp_backacc2.setImageBitmap(resize(bm_layerbackacc2, iv_comp_backacc2.getWidth(), iv_comp_backacc2.getHeight()));
        iv_comp_backacc.setImageBitmap(resize(bm_layerbackacc, iv_comp_backacc.getWidth(), iv_comp_backacc.getHeight()));
        iv_comp_back.setImageBitmap(resize(bm_layerback, iv_comp_back.getWidth(), iv_comp_back.getHeight()));
        iv_comp_center.setImageBitmap(resize(bm_layercenter, iv_comp_center.getWidth(), iv_comp_center.getHeight()));
        iv_comp_bangs.setImageBitmap(resize(bm_layerbangs, iv_comp_bangs.getWidth(), iv_comp_bangs.getHeight()));
        iv_comp_cheeks.setImageBitmap(resize(bm_cheeks, iv_comp_cheeks.getWidth(), iv_comp_cheeks.getHeight())); //blushing o los demas.
        iv_comp_eyes.setImageBitmap(resize(bm_layereyes, iv_comp_eyes.getWidth(), iv_comp_eyes.getHeight()));
        iv_comp_front.setImageBitmap(resize(bm_layerfront, iv_comp_front.getWidth(), iv_comp_front.getHeight()));
        iv_comp_frontall.setImageBitmap(resize(bm_layerfrontall, iv_comp_frontall.getWidth(), iv_comp_frontall.getHeight()));
        iv_comp_layerhair.setImageBitmap(resize(bm_layerhair, iv_comp_layerhair.getWidth(), iv_comp_layerhair.getHeight()));
        iv_comp_hairback.setImageBitmap(resize(bm_hairback, iv_comp_hairback.getWidth(), iv_comp_hairback.getHeight()));
        iv_comp_faceaccs.setImageBitmap(resize(bm_faceaccs, iv_comp_faceaccs.getWidth(), iv_comp_faceaccs.getHeight()));
        iv_comp_hairaccs.setImageBitmap(resize(bm_hairaccs, iv_comp_hairaccs.getWidth(), iv_comp_hairaccs.getHeight()));

    }

    private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

    private int[] calcular_hair(String face_tam){
        int hair_w, hair_h, p_x, p_y;
        hair_w = 640;
        hair_h = 854;
        p_x = 0;
        p_y = 0;

        switch (face_tam) {
            case "11":
                p_x = 0;
                p_y = 0;
                break;
            case "21":
                hair_h = 840 - 4;
                //p_x = 0;
                p_y = 54 + 7;
                break;
            case "31":
                //hair_w = 640;
                hair_h = 806;
                //p_x = 0;
                p_y = 94 + 18;
                break;
        }
        return new int[]{hair_w, hair_h, p_x, p_y};
    }

    private int[] calcular_bangs(String face_tam){
        int bangs_w, bangs_h, p_x, p_y;
        bangs_w = 146;
        bangs_h = 207;
        p_x = 234;
        p_y = 94;

        switch (face_tam) {
            case "11":
                p_x = 232;
                p_y = 94;
                break;
            case "21":
                p_x = 233;
                p_y = 147;
                break;
            case "31":
                p_x = 234;
                p_y = 190;
                break;
        }

        return new int[]{bangs_w, bangs_h, p_x, p_y};
    }

    private int[] calcular_cheeks(String face_tam){
        int cheeks_w, cheeks_h, p_x, p_y;
        cheeks_w = 146;
        cheeks_h = 207;
        p_x = 232;
        p_y = 94;

        switch (face_tam) {
            case "11":
                p_x = 232;
                p_y = 94;
                break;
            case "21":
                p_x = 233;
                p_y = 151;
                break;
            case "31":
                p_x = 233;
                p_y = 191;
                break;
        }

        return new int[]{cheeks_w, cheeks_h, p_x, p_y};
    }

    private int[] calcular_ojos(String face_tam){
        int ojos_w, ojos_h, p_x, p_y;
        ojos_w = 146;
        ojos_h = 207;
        p_x = 233;
        p_y = 95;

        switch (face_tam) {
            case "11":
                ojos_h = 205;
                break;
            case "21":
                ojos_h = 203;
                p_y = 155;
                break;
            case "31":
                ojos_h = 195;
                p_y = 202;
                break;
        }

        return new int[]{ojos_w, ojos_h, p_x, p_y};
    }

    private int[] calcular_boca(String face_tam){
        int boca_w, boca_h, p_x, p_y;
        boca_w = 146;
        boca_h = 207;
        p_x = 233;
        p_y = 94;

        switch (face_tam) {
            case "11":
                p_x = 233;
                p_y = 94;
                break;
            case "21":
                p_x = 233;
                p_y = 151;
                break;
            case "31":
                p_x = 233;
                p_y = 191;
                break;
        }

        return new int[]{boca_w, boca_h, p_x, p_y};
    }

    private Bitmap[] frames_bmp(Bitmap bmp, int type){
        Bitmap[] grid = new Bitmap[3];
        Bitmap resu = Bitmap.createScaledBitmap(bmp,438,207,false);
        grid[0] = Bitmap.createBitmap(resu,0,0,146,207);
        grid[1] = Bitmap.createBitmap(resu,146,0,146,207);
        grid[2] = Bitmap.createBitmap(resu,146 * 2,0,146,207);
        return grid;
    }

    public Boolean is_framed_expression(String expression){
        String[] c_exp_specials = {"3","8","9","011","26","27","012","013","014"};
        for (String c_exp_special : c_exp_specials) {
            if (expression.equals(c_exp_special)) {
                return false;
            }
        }
        return true;
    }

    private Bitmap cortar_capa(Bitmap bmp){
        Bitmap result = base;
        if (bmp.getWidth() >= 1024 && bmp.getHeight() >= 1024){
            result = Bitmap.createBitmap(bmp,192, 170, 640, 854);
        } else {
            Log.d("dream_designer", "error al cortar la capa: " + bmp.getWidth() + " -- " + bmp.getHeight());

        }
        return result;
    }

    private Bitmap armar_capa(Bitmap base, Bitmap capa, int x, int y){
        int w = base.getWidth();
        int h = base.getHeight();
        Bitmap result = Bitmap.createBitmap(w, h, base.getConfig());
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        canvas.drawBitmap(base, 0, 0, paint);
        paint.setAntiAlias(true);
        canvas.drawBitmap(capa, x, y, paint);
        return result;
    }

    private Bitmap generar_capa(Bitmap base, Bitmap capa, int x, int y, int w, int h){

        if (capa == null){
            Log.d("waifudata", "este es nlo");
        }

        Bitmap result = base.copy(base.getConfig(), true);
        Bitmap capa_r = Bitmap.createScaledBitmap(capa, w, h, true);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(base, 0, 0, null);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawBitmap(capa_r, x, y, paint);
        return result;
    }

    private Bitmap generar_capa2(Bitmap base, Bitmap capa, int x, int y, int w, int h){
        int b_w = base.getWidth();
        int b_h = base.getHeight();
        Bitmap result = Bitmap.createBitmap(b_w, b_h, base.getConfig());
        //Bitmap result = base.copy(base.getConfig(), true);
        Bitmap capa_r = Bitmap.createScaledBitmap(capa, w, h, true);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(base, 0, 0, null);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawBitmap(capa_r, x, y, paint);
        return result;
    }


    private Bitmap unir_todo(Bitmap[] bitmaps){

        int w = base.getWidth();
        int h = base.getHeight();
        Bitmap result = Bitmap.createBitmap(w, h, base.getConfig());
        //capas
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(base, 0, 0, null);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        for (Bitmap bitmap : bitmaps) {
            canvas.drawBitmap(bitmap, 0, 0, paint);
        }
        return result;
    }

}

