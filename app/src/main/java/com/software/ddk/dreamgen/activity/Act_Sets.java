package com.software.ddk.dreamgen.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.software.ddk.dreamgen.R;
import com.software.ddk.dreamgen.adapter.RVWaifuItems;
import com.software.ddk.dreamgen.utils.SaveManager;
import com.software.ddk.dreamgen.fragment.avatarview;
import com.software.ddk.dreamgen.utils.CustomToast;
import com.software.ddk.dreamgen.utils.RecyclerItemClickListener;
import com.software.ddk.dreamgen.utils.Utils;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Deprecated
public class Act_Sets extends AppCompatActivity {

    private String outfit, bkg, hair, hair2, face_eye1, face_eye2, hairacc1, hairacc2, bodyacc1, bodyacc2, faceacc1, faceacc2, backacc1, backacc2, front, face_pup1, face_pup2, face_blush, face_skin, face_tam, face_cej, face_exp;
    private String front2, bodyacc3, faceacc3, hairacc3, backacc3, desc, date;
    private String server = "0";
    private ImageView iv_waifu_c1, iv_waifu_c2, iv_waifu_c3, iv_waifu_c4, iv_waifu_c5, iv_waifu_c6, iv_waifu_c7, iv_waifu_c8, iv_waifu_c9, iv_waifu_c10, iv_waifu_c11, iv_waifu_c12, iv_waifu_c13;
    private TextView tv_waifu_setname, tv_waifu_desc, tv_msets, tv_mdesigns;

    //LinearLayout bt_addset, bt_sets_close;
    private FloatingActionButton fab_addset;
    private ArrayList<String[]> sets_waifus = new ArrayList<>();
    private PopupWindow pop_setname, pop_setinfo;
    private RecyclerView rv_sets;
    private RVWaifuItems waifuadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sets);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        fab_addset = findViewById(R.id.fab_addset);
        rv_sets = findViewById(R.id.rv_sets);
        tv_mdesigns = findViewById(R.id.tv_mdesigns);
        tv_msets = findViewById(R.id.tv_msets);

        //preparar datos de sets
        SaveManager smg = new SaveManager(getApplicationContext(), SaveManager.SAVETYPE_WAIFUDATA);
        sets_waifus = smg.getArray("waifusets");
        load_data(0);

        //poblar recyclerview
        LinearLayoutManager waifuLayout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        waifuLayout.setStackFromEnd(true);
        rv_sets.setLayoutManager(waifuLayout);
        waifuadapter = new RVWaifuItems(getApplicationContext(), sets_waifus);
        rv_sets.setAdapter(waifuadapter);

        rv_sets.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), rv_sets, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //open_set(position);
                pop_open_setinfo(view, position);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        //listeners
        fab_addset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_pop_addset(view);
            }
        });

        tv_mdesigns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //todo abrir el fragment de design
                //Intent in = new Intent(getApplicationContext(), Act_Design.class);
                //startActivity(in);
                //finish();
            }
        });

    }

    private void load_data(int position){
        String[] w_data = sets_waifus.get(position);
        outfit = w_data[0];
        bkg = w_data[1];
        hair = w_data[2];
        hair2 = w_data[3];
        face_eye1 = w_data[4];
        face_eye2 = w_data[5];
        hairacc1 = w_data[6];
        hairacc2 = w_data[7];
        bodyacc1 = w_data[8];
        bodyacc2 = w_data[9];
        faceacc1 = w_data[10];
        faceacc2 = w_data[11];
        backacc1 = w_data[12];
        backacc2 = w_data[13];
        front = w_data[14];
        face_pup1 = w_data[15];
        face_pup2 = w_data[16];
        face_blush = w_data[17];
        face_skin = w_data[18];
        face_tam = w_data[19];
        face_cej = w_data[20];
        face_exp = "10";
        if (tv_waifu_setname != null){
            tv_waifu_setname.setText(w_data[32]);
        }

        front2 = "0";
        hairacc3 = "0";
        bodyacc3 = "0";
        faceacc3 = "0";
        backacc3 = "0";
        if (w_data.length > 33){
            front2 = w_data[33];
            hairacc3 = w_data[34];
            bodyacc3 = w_data[35];
            faceacc3 = w_data[36];
            backacc3 = w_data[37];
        }

        desc = "No hay descripcion del conjunto.";
        server = "0";
        if (w_data.length > 38){
            desc = w_data[38];
            server = w_data[39];
        }
        if (w_data.length > 39){
            //date here.
            date = w_data[40];
        }
    }

    private void open_set(int position) {
        String[] data_waifu = sets_waifus.get(position);
        avatarview.outfit = data_waifu[0];
        avatarview.bkg = data_waifu[1];
        avatarview.hair = data_waifu[2];
        avatarview.hair2 = data_waifu[3];
        avatarview.face_eye1 = data_waifu[4];
        avatarview.face_eye2 = data_waifu[5];
        avatarview.hairacc1 = data_waifu[6];
        avatarview.hairacc2 = data_waifu[7];
        avatarview.bodyacc1 = data_waifu[8];
        avatarview.bodyacc2 = data_waifu[9];
        avatarview.faceacc1 = data_waifu[10];
        avatarview.faceacc2 = data_waifu[11];
        avatarview.backacc1 = data_waifu[12];
        avatarview.backacc2 = data_waifu[13];
        avatarview.front = data_waifu[14];
        avatarview.face_pup1 = data_waifu[15];
        avatarview.face_pup2 = data_waifu[16];
        avatarview.face_blush = data_waifu[17];
        avatarview.face_skin = data_waifu[18];
        avatarview.face_tam = data_waifu[19];
        avatarview.face_cej = data_waifu[20];

        //Log.d("sets_manager", "fp5: " + data_waifu[24]);

        avatarview.face_pos[1] = Integer.parseInt(data_waifu[22]); //skin
        avatarview.face_pos[4] = Integer.parseInt(data_waifu[23]); //cej
        avatarview.face_pos[5] = Integer.parseInt(data_waifu[24]);
        avatarview.face_pos[6] = Integer.parseInt(data_waifu[25]);
        avatarview.face_pos[2] = Integer.parseInt(data_waifu[26]);
        avatarview.face_pos[3] = Integer.parseInt(data_waifu[27]);
        avatarview.face_pos[8] = Integer.parseInt(data_waifu[28]);
        avatarview.face_pos[9] = Integer.parseInt(data_waifu[29]);
        avatarview.face_pos[10] = Integer.parseInt(data_waifu[30]);
        avatarview.face_pos[7] = Integer.parseInt(data_waifu[31]);

        avatarview.setinfo = data_waifu[32];
        avatarview.setname = data_waifu[32];

        if (data_waifu.length > 33){
            avatarview.front2 = data_waifu[33];
            avatarview.hairacc3 = data_waifu[34];
            avatarview.bodyacc3 = data_waifu[35];
            avatarview.faceacc3 = data_waifu[36];
            avatarview.backacc3 = data_waifu[37];
        }

        if (data_waifu.length > 38){
            desc = data_waifu[38];
            avatarview.server = Integer.valueOf(data_waifu[39]);
            switch (data_waifu[39]){
                case "0":
                    avatarview.bt_srv_select.setImageResource(R.drawable.usa_server);
                    break;
                case "1":
                    avatarview.bt_srv_select.setImageResource(R.drawable.jp_server);
                    break;
            }
        } else {
            //server default es usa.
            avatarview.server = 0;
            avatarview.bt_srv_select.setImageResource(R.drawable.usa_server);
        }
        if (data_waifu.length > 39){
            date = data_waifu[40];
        }

        setResult(RESULT_OK);
        finish();
    }

    private void load_set(String url, ImageView iview) {
        Picasso.get()
                .load(url)
                .noFade()
                .noPlaceholder()
                .into(iview);
    }

    private void open_pop_addset(View view) {
        fab_addset.hide();
        try {
            LayoutInflater inflater = (LayoutInflater) Act_Sets.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.layout_pop_newset, (ViewGroup) findViewById(R.id.pop_newset));

            int width = LinearLayout.LayoutParams.MATCH_PARENT;
            int height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics()));

            pop_setname = new PopupWindow(layout, width, height, true);
            pop_setname.setContentView(layout);
            pop_setname.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            pop_setname.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            pop_setname.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    pop_setname.dismiss();
                    fab_addset.show();
                }
            });

            final EditText et_setname = layout.findViewById(R.id.et_setname);
            final EditText et_setdesc = layout.findViewById(R.id.et_setdesc);
            ImageButton bt_addnewset = layout.findViewById(R.id.bt_addnewset);
            ImageButton bt_cancelnewset = layout.findViewById(R.id.bt_cancelnewset);

            //mostrar teclado.
            et_setname.requestFocus();
            et_setname.setFocusableInTouchMode(true);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(et_setname, InputMethodManager.SHOW_IMPLICIT);

            bt_addnewset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //agregar aca.
                    String setname = et_setname.getText().toString();
                    String setdesc = et_setdesc.getText().toString();

                    if (setname.isEmpty() || setname.equals(" ")){
                        setname = "Mi conjunto";
                    }
                    if (setdesc.isEmpty() || setdesc.equals(" ")){
                        setdesc = "Conjunto sin descripción.";
                    }

                    addset(view, setname, setdesc);
                    pop_setname.dismiss();
                    fab_addset.show();

                }
            });

            bt_cancelnewset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pop_setname.dismiss();
                    fab_addset.show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void pop_open_setinfo(View view, final int position) {
        LayoutInflater inflater = (LayoutInflater) Act_Sets.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.layout_pop_waifu_item, (ViewGroup) findViewById(R.id.pop_waifu_item));

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 320, getResources().getDisplayMetrics()));

        pop_setinfo = new PopupWindow(layout, width, height, true);
        pop_setinfo.setContentView(layout);
        pop_setinfo.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        pop_setinfo.showAtLocation(view, Gravity.CENTER, 0, 0);

        ImageView iv_waifu_preview;
        LinearLayout bt_conf_accept, bt_conf_delete, bt_conf_close;

        tv_waifu_setname = layout.findViewById(R.id.tv_waifu_setname);
        tv_waifu_desc = layout.findViewById(R.id.tv_waifu_desc);
        iv_waifu_preview = layout.findViewById(R.id.iv_waifu_preview);
        iv_waifu_c1 = layout.findViewById(R.id.iv_waifu_c1);
        iv_waifu_c2 = layout.findViewById(R.id.iv_waifu_c2);
        iv_waifu_c3 = layout.findViewById(R.id.iv_waifu_c3);
        iv_waifu_c4 = layout.findViewById(R.id.iv_waifu_c4);
        iv_waifu_c5 = layout.findViewById(R.id.iv_waifu_c5);
        iv_waifu_c6 = layout.findViewById(R.id.iv_waifu_c6);
        iv_waifu_c7 = layout.findViewById(R.id.iv_waifu_c7);
        iv_waifu_c8 = layout.findViewById(R.id.iv_waifu_c8);
        iv_waifu_c9 = layout.findViewById(R.id.iv_waifu_c9);
        iv_waifu_c10 = layout.findViewById(R.id.iv_waifu_c10);
        iv_waifu_c11 = layout.findViewById(R.id.iv_waifu_c11);
        iv_waifu_c12 = layout.findViewById(R.id.iv_waifu_c12);
        iv_waifu_c13 = layout.findViewById(R.id.iv_waifu_c13);

        bt_conf_accept = layout.findViewById(R.id.bt_conf_accept);
        bt_conf_delete = layout.findViewById(R.id.bt_conf_delete);
        bt_conf_close = layout.findViewById(R.id.bt_conf_close);

        //carga de previews.
        load_data(position);
        preview_load(iv_waifu_preview);
        load_seticons();
        if (desc != null){
            tv_waifu_desc.setText(desc);
        } else if (position == 0) {
            tv_waifu_desc.setText("El conjunto por defecto.");
        } else {
            tv_waifu_desc.setText("No hay descripcion del conjunto.");
        }

        if (position == 0){
            //si es el 0 hacer que el boton aparezca desactivado.
            bt_conf_delete.setBackgroundResource(R.drawable.button_shade3);
        }

        //listeners
        bt_conf_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_set(position);
            }
        });

        bt_conf_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //eliminar item.
                check_alert_delete(position);

            }
        });

        bt_conf_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop_setinfo.dismiss();
            }
        });
    }


    private void check_alert_delete(final int position){
        DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case DialogInterface.BUTTON_POSITIVE:
                        delete_set(position);
                        pop_setinfo.dismiss();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:

                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(Act_Sets.this);
        builder.setMessage("Estas seguro de eliminar este set?")
                .setPositiveButton("Si", dialog)
                .setNegativeButton("No", dialog)
                .setIcon(R.drawable.ic_moon)
                .show();

    }

    private void delete_set(int position) {
        if (position > 0){
            SaveManager smg = new SaveManager(getApplicationContext(), 2);
            sets_waifus.remove(position);
            smg.saveArray("waifusets", sets_waifus);
            update_adapter();
            new CustomToast(Act_Sets.this, "Conjunto eliminado.", Toast.LENGTH_SHORT, Gravity.BOTTOM);
        } else {
            new CustomToast(Act_Sets.this, "No puedes eliminar este set.", Toast.LENGTH_SHORT, Gravity.BOTTOM);
        }
    }

    private void addset(View view, String setname, String setdesc) {
        String[] datos_del_set = {
                avatarview.outfit, //0
                avatarview.bkg,
                avatarview.hair,
                avatarview.hair2,
                avatarview.face_eye1,
                avatarview.face_eye2,
                avatarview.hairacc1,
                avatarview.hairacc2,
                avatarview.bodyacc1,
                avatarview.bodyacc2,
                avatarview.faceacc1,
                avatarview.faceacc2,
                avatarview.backacc1,
                avatarview.backacc2,
                avatarview.front,
                avatarview.face_pup1,
                avatarview.face_pup2,
                avatarview.face_blush,
                avatarview.face_skin,
                avatarview.face_tam,
                avatarview.face_cej, //20
                "10", //exp 21
                String.valueOf(avatarview.face_pos[1]), //skin 22
                String.valueOf(avatarview.face_pos[4]), //cej
                String.valueOf(avatarview.face_pos[5]), //ftype
                String.valueOf(avatarview.face_pos[6]), //fcolor
                String.valueOf(avatarview.face_pos[2]), //tam
                String.valueOf(avatarview.face_pos[3]), //blush
                String.valueOf(avatarview.face_pos[8]), //pupcol
                String.valueOf(avatarview.face_pos[9]), //puptam
                String.valueOf(avatarview.face_pos[10]), //pupgrad
                String.valueOf(avatarview.face_pos[7]), //puptype //31
                setname, //32
                avatarview.front2,
                avatarview.hairacc3,
                avatarview.bodyacc3,
                avatarview.faceacc3,
                avatarview.backacc3,
                setdesc,
                String.valueOf(avatarview.server),
                Utils.getSaveDate()//40

        };

        //cargar save
        SaveManager smg = new SaveManager(getApplicationContext(), 2);
        sets_waifus.add(datos_del_set);
        smg.saveArray("waifusets", sets_waifus);
        new CustomToast(Act_Sets.this, "Conjunto guardado.", Toast.LENGTH_SHORT, Gravity.BOTTOM);

        pop_setname.dismiss();
        update_adapter();

        //cargar popup con informacion.
        //pop_open_setinfo(view, sets_waifus.size() - 1);

    }

    private void update_adapter() {
        SaveManager smg = new SaveManager(getApplicationContext(), 2);
        sets_waifus = smg.getArray("waifusets");
        //waifuadapter.notifyDataSetChanged();
        //todo - lo de arriba no funciona correctamente, asi que voy a usar esto por ahora
        waifuadapter = new RVWaifuItems(getApplicationContext(), sets_waifus);
        rv_sets.setAdapter(waifuadapter);
    }

    private void preview_load(ImageView iview){
        String url = "http://"+ avatarview.a_server[Integer.valueOf(server)] +"/img/php/all_param2.php?&id1=" + generate_ids(2,front) + ","+ generate_ids(1, outfit) + "," + generate_ids(1, bodyacc1)+ ","+generate_ids(1, bodyacc2)+","+generate_ids(1, hairacc1)+","+generate_ids(1, hairacc2)+","+generate_ids(1, faceacc1)+","+generate_ids(1, faceacc2)+","+generate_ids(1, backacc1)+","+generate_ids(1, backacc2)+","+generate_ids(1, bkg)+","+generate_ids(1, hair)+","+generate_ids(1, face_eye1)+","+generate_ids(1, face_eye2)+","+generate_ids(1, hair2)+",x0x,x0x,x0x,x0x,x0x,"+generate_ids(1, face_pup1)+","+generate_ids(1, face_pup2)+",x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,"+generate_ids(4, face_cej)+"&id2="+generate_ids(3,face_skin)+"&id3="+generate_ids(3,face_tam)+"&id4="+generate_ids(3,face_exp)+"&id5="+generate_ids(3,face_blush)+"&id6=0x1&id7=2x1";
        Picasso.get().load(url).into(iview);
    }

    private String generate_ids(int type, String id){
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

    private void load_seticons(){
        String thumb = "avatar_cate_thum";

        //outfit
        if (!outfit.equals("0")){
            load_set("http://" + avatarview.a_server[Integer.valueOf(server)] + "/img/" + thumb + "/body1/" + outfit + ".png", iv_waifu_c1);
        } else {
            iv_waifu_c1.setImageResource(R.drawable.avatar_slot1);
        }

        //front
        if (!front.equals("0")){
            load_set("http://" + avatarview.a_server[Integer.valueOf(server)] + "/img/" + thumb + "/front/" + front + ".png", iv_waifu_c2);

        } else {
            iv_waifu_c2.setImageResource(R.drawable.avatar_slot0);
        }

        //background
        if (!bkg.equals("0")){
            load_set("http://"+ avatarview.a_server[Integer.valueOf(server)] +"/img/" + thumb + "/bg/" + bkg + ".png", iv_waifu_c3);

        } else {
            iv_waifu_c3.setImageResource(R.drawable.avatar_slot10);
        }
        //bodyacc1
        if (!bodyacc1.equals("0")) {
            load_set("http://"+ avatarview.a_server[Integer.valueOf(server)] +"/img/" + thumb + "/body_acc/" + bodyacc1 + ".png", iv_waifu_c4);
        } else {
            iv_waifu_c4.setImageResource(R.drawable.avatar_slot2);
        }

        //bodyacc2
        if (!bodyacc2.equals("0")){
            load_set("http://"+ avatarview.a_server[Integer.valueOf(server)] +"/img/" + thumb + "/body_acc/" + bodyacc2 + ".png", iv_waifu_c5);

        } else {
            iv_waifu_c5.setImageResource(R.drawable.avatar_slot3);
        }

        //backacc1
        if (!backacc1.equals("0")){
            load_set("http://"+ avatarview.a_server[Integer.valueOf(server)] +"/img/" + thumb + "/back_acc/" + backacc1 + ".png", iv_waifu_c6);
        } else {
            iv_waifu_c6.setImageResource(R.drawable.avatar_slot8);
        }

        //backacc2
        if (!backacc2.equals("0")){
            load_set("http://"+ avatarview.a_server[Integer.valueOf(server)] +"/img/" + thumb + "/back_acc/" + backacc2 + ".png", iv_waifu_c7);
        } else {
            iv_waifu_c7.setImageResource(R.drawable.avatar_slot9);
        }

        //faceacc1
        if (!faceacc1.equals("0")){
            load_set("http://"+ avatarview.a_server[Integer.valueOf(server)] +"/img/" + thumb + "/face_acc/" + faceacc1 + ".png", iv_waifu_c8);

        } else {
            iv_waifu_c8.setImageResource(R.drawable.avatar_slot6);
        }

        //faceacc2
        if (!faceacc2.equals("0")){
            load_set("http://"+ avatarview.a_server[Integer.valueOf(server)] +"/img/" + thumb + "/face_acc/" + faceacc2 + ".png", iv_waifu_c9);
        } else {
            iv_waifu_c9.setImageResource(R.drawable.avatar_slot7);
        }

        //hairacc1
        if (!hairacc1.equals("0")){
            load_set("http://"+ avatarview.a_server[Integer.valueOf(server)] +"/img/" + thumb + "/hair_acc/" + hairacc1 + ".png", iv_waifu_c10);
        } else {
            iv_waifu_c10.setImageResource(R.drawable.avatar_slot4);
        }

        //hairacc2
        if (!hairacc2.equals("0")){
            load_set("http://"+ avatarview.a_server[Integer.valueOf(server)] +"/img/" + thumb + "/hair_acc/" + hairacc2 + ".png", iv_waifu_c11);
        } else {
            iv_waifu_c11.setImageResource(R.drawable.avatar_slot5);
        }
        //hair
        if (!hair.equals("0")){
            load_set("http://"+ avatarview.a_server[Integer.valueOf(server)] +"/img/" + thumb + "/hair/" + hair + ".png", iv_waifu_c12);
        } else {
            iv_waifu_c12.setImageResource(R.drawable.avatar_slot11);
        }

        //hair2
        if (!hair2.equals("0")){
            load_set("http://"+ avatarview.a_server[Integer.valueOf(server)] +"/img/" + thumb + "/hair/" + hair2 + ".png", iv_waifu_c13);
        } else {
            iv_waifu_c13.setImageResource(R.drawable.avatar_slot14);
        }
    }


}
