package com.software.ddk.dreamgen.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.software.ddk.dreamgen.utils.MapValues;
import com.software.ddk.dreamgen.R;
import com.software.ddk.dreamgen.adapter.RVFavItems;
import com.software.ddk.dreamgen.adapter.RVItems;
import com.software.ddk.dreamgen.utils.SaveManager;
import com.software.ddk.dreamgen.fragment.avatarview;
import com.software.ddk.dreamgen.utils.CustomToast;
import com.software.ddk.dreamgen.utils.GridAutoFitLayoutManager;
import com.software.ddk.dreamgen.utils.RecyclerItemClickListener;
import com.software.ddk.dreamgen.utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Act_Items extends AppCompatActivity {

    private PopupWindow pop_item, pop_search, pop_scan;
    private ProgressBar pb_anprogress;
    private OkHttpClient client = new OkHttpClient();

    private ArrayList<String[]> items = new ArrayList<>();
    private ArrayList<String[]> items_toshow = new ArrayList<>();
    private ArrayList<String[]> items_favs = new ArrayList<>();
    private ArrayList<String[]> items_saved_search = new ArrayList<>();
    private ArrayList<String[]> items_data = new ArrayList<>();

    private RecyclerView rv_items;
    private ImageButton bt_prev, bt_next, bt_update, bt_search, bt_favs;
    private ConstraintLayout cl_currentsearchinfo;
    private TextView tv_searchstring;
    private ImageButton bt_closesearchstring;
    private ConstraintLayout cl_items_loading;
    private ImageView iv_item_loading;
    private CardView cv_nofavs;

    private RVItems adapter;
    private RVFavItems favadapter;

    private TextView tv_info, sp_type, tv_item_name;
    //Spinner sp_itemsort;
    private ImageButton bt_itemsort;
    private ImageButton bt_serverselect;

    //int CONST_BASEITEMSLIMIT = 19500;
    private int item_type = 1;
    private int item_kind = 1;
    private int item_actual_rarity = 0;
    private String item_selected = "0";
    private boolean item_positem = false;

    private boolean cancel_thread = false;
    private boolean running_thread = false;
    private boolean favs_active = false;
    private boolean search_active = false;
    private boolean is_faved = false;
    private boolean is_modifying = false;
    private boolean is_filtering = false;
    private String selected_txtfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        rv_items = findViewById(R.id.rv_items);
        bt_prev = findViewById(R.id.bt_prev);
        bt_next = findViewById(R.id.bt_next);
        bt_update = findViewById(R.id.bt_update);
        bt_search = findViewById(R.id.bt_search);
        bt_favs = findViewById(R.id.bt_favs);
        tv_info = findViewById(R.id.tv_info);
        sp_type = findViewById(R.id.sp_type);
        //sp_serverselect = findViewById(R.id.sp_serverselect);
        bt_serverselect = findViewById(R.id.bt_serverselect);
        bt_itemsort = findViewById(R.id.bt_itemsort);
        pb_anprogress = findViewById(R.id.pb_anprogres);
        cl_currentsearchinfo = findViewById(R.id.cl_currentsearchinfo);
        tv_searchstring = findViewById(R.id.tv_searchstring);
        bt_closesearchstring = findViewById(R.id.bt_closesearchstring);
        cl_items_loading = findViewById(R.id.cl_items_loading);
        cv_nofavs = findViewById(R.id.cv_nofavs);

        //setear spinner de server con las banderitas
        bt_serverselect.setImageResource(get_res_server(avatarview.server));
        bt_serverselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_pop_serverselect();
            }
        });

        //obtener datos del main
        Intent in = getIntent();
        item_type = in.getIntExtra("item_type", 1);
        item_kind = in.getIntExtra("item_kind", 1);
        item_selected = in.getStringExtra("item_selected");
        item_positem = in.getBooleanExtra("item_positem", false);

        //actualizar informacion visible desde el main
        sp_type.setText(define_typename(item_type));

        //chequear si el modo de busqueda de este tipo de item esta activo.
        if (avatarview.items_searchstate[item_type]){
            //cargar el array guardado si tiene datos.
            if (load_savsearch(item_type)){
                //cargar array.
                items_toshow.clear();
                items_toshow.addAll(items_saved_search);
                load_adapter();
                //abrir el menu search
                search_active = true;
                tv_searchstring.setText(avatarview.items_searchstring[item_type]);
                cl_currentsearchinfo.setVisibility(View.VISIBLE);
            }

        } else {
            //cargar lo inicial
            load_favs();
            init_load();
        }

        //posicionar grid al ultimo seleccionado (si no esta en modo de busqueda.)
        if (item_positem){
            scroll_loader(get_id_position_in_array(item_selected));
        } else {
            scroll_loader(avatarview.items_pos[item_type]);
        }

        //listener de los items.
        rv_items.addOnItemTouchListener(new RecyclerItemClickListener(this, rv_items, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // body 1
                // bodyacc 2
                // hairacc 3
                // faceacc 4
                // backacc 5
                // background 6
                // hair 7
                // front 8

                String[] data = {items_toshow.get(position)[1], String.valueOf(item_type), get_itemname(position), "","",""};
                //checkear si es un item o el eliminador
                if (items_toshow.get(position)[1].equals("0")){
                    //abrir directamente
                    accept_item(Integer.valueOf(items_toshow.get(position)[1]));
                } else {
                    open_popup(view, data);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {}
        }));

        //area de listeners
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendto_last(view);
            }
        });
        bt_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendto_first(view);
            }
        });
        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!favs_active){
                    if (!is_filtering){
                        //llamar popup.
                        scan_popup(view);
                    } else {
                        new CustomToast(Act_Items.this, "Necesitas desactivar el filtro", Toast.LENGTH_SHORT, Gravity.BOTTOM);
                    }
                } else {
                    new CustomToast(Act_Items.this, "Necesitas cerrar el menu de favoritos.", Toast.LENGTH_SHORT, Gravity.BOTTOM);
                }
            }
        });
        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_popup(view);
            }
        });
        bt_favs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!avatarview.items_searchstate[item_type]){
                    if (!running_thread){
                        if (!favs_active){
                            scroll_saver();
                            init_favs();
                            favs_active = true;
                            bt_favs.setColorFilter(Color.argb(255,255,255,0));
                        } else {
                            cv_nofavs.setVisibility(View.GONE);
                            init_load();
                            scroll_loader(avatarview.items_pos[item_type]);
                            favs_active = false;
                            bt_favs.setColorFilter(getResources().getColor(R.color.icons_tint));
                        }
                    } else {
                        new CustomToast(Act_Items.this, "Necesitas detener el escaneo.", Toast.LENGTH_SHORT, Gravity.BOTTOM);
                    }
                } else {
                    new CustomToast(Act_Items.this, "Necesitas cerrar la búsqueda activa.", Toast.LENGTH_SHORT, Gravity.BOTTOM);
                }
            }
        });
        bt_closesearchstring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cerrar el modo de busqueda
                cl_currentsearchinfo.setVisibility(View.GONE);
                //save search state
                search_active = false;
                avatarview.items_searchstate[item_type] = false;
                //recargar la lista de items.
                if (favs_active){
                    init_load_favs();
                } else {
                    init_load();
                }
            }
        });

        bt_itemsort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!running_thread){
                    show_pop_rarityselect();
                } else {
                    new CustomToast(Act_Items.this, "No puedes filtrar ahora mismo.", Toast.LENGTH_SHORT, Gravity.BOTTOM);
                }
            }
        });
    }



    private void show_pop_rarityselect(){
        PopupMenu popup = new PopupMenu(this, bt_itemsort);
        popup.getMenuInflater().inflate(R.menu.rarity, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                //si esta activo el favs, cargar el array de favoritos.
                if (favs_active){
                    generate_favsarray();
                } else {
                    generate_validarray();
                }

                switch ((String) menuItem.getTitle()){
                    case "Todos":
                        is_filtering = false;
                        bt_itemsort.setImageResource(R.drawable.all_icon);
                        item_actual_rarity = 0;
                        break;
                    case "MR y MR+":
                        is_filtering = true;
                        bt_itemsort.setImageResource(R.drawable.mr_icon);
                        generate_sortarray("5");
                        item_actual_rarity = 5;
                        break;
                    case "SR":
                        is_filtering = true;
                        bt_itemsort.setImageResource(R.drawable.sr_icon);
                        generate_sortarray("4");
                        item_actual_rarity = 4;
                        break;
                    case "RR":
                        is_filtering = true;
                        bt_itemsort.setImageResource(R.drawable.rr_icon);
                        generate_sortarray("3");
                        item_actual_rarity = 3;
                        break;
                    case "R":
                        is_filtering = true;
                        bt_itemsort.setImageResource(R.drawable.r_icon);
                        generate_sortarray("2");
                        item_actual_rarity = 2;
                        break;
                    case "N":
                        is_filtering = true;
                        bt_itemsort.setImageResource(R.drawable.n_icon);
                        generate_sortarray("1");
                        item_actual_rarity = 1;
                        break;
                }

                //cargar adapter dependiendo del contexto.
                if (favs_active){
                    load_favs_adapter();
                } else {
                    load_adapter();
                }
                return true;
            }
        });
        popup.show();
    }

    private void show_pop_serverselect(){
        PopupMenu popup = new PopupMenu(this, bt_serverselect);
        popup.getMenuInflater().inflate(R.menu.server, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch ((String) menuItem.getTitle()){
                    case "USA server":
                        avatarview.server = 0;
                        bt_serverselect.setImageResource(R.drawable.usa_server);
                        avatarview.bt_srv_select.setImageResource(R.drawable.usa_server);
                        break;
                    case "JP server":
                        avatarview.server = 1;
                        bt_serverselect.setImageResource(R.drawable.jp_server);
                        avatarview.bt_srv_select.setImageResource(R.drawable.jp_server);
                        break;
                }

                //si favs esta activo.
                favs_active = false;
                bt_favs.setColorFilter(getResources().getColor(R.color.icons_tint));

                init_load();
                return true;
            }
        });
        popup.show();
    }

    private int get_res_server(int server){
        switch (server){
            case 0:
                return R.drawable.usa_server;
            case 1:
                return R.drawable.jp_server;
        }
        return 0;
    }

    private void remove_fav(String item){
        for (int i=0; i < items_favs.size(); i++){
            if (items_favs.get(i)[0].equals(item)){
                items_favs.remove(i);
            }
        }
    }

    private Boolean item_is_fav(String item){
        for (int i=0; i < items_favs.size(); i++){
            if (items_favs.get(i)[0].equals(item)){
                return true;
            }
        }
        return false;
    }

    private void load_favs(){
        SaveManager smg = new SaveManager(getApplicationContext(), 1);
        items_favs = smg.getArray("favoritos");
    }

    private void init_favs(){
        generate_favsarray();
        load_favs_adapter();
    }

    private void init_load(){
        //se genera el array base.
        generate_basearray(0, Utils.CONST_BASEITEMSLIMIT);
        //se obtiene la base de datos guardada.
        generate_database(item_type);
        //se genera la lista de items validos.
        generate_validarray();
        //se carga la lista del adapter.
        load_adapter();
    }

    private void init_load_favs(){
        generate_basearray(0, Utils.CONST_BASEITEMSLIMIT);
        generate_database(item_type);
        generate_favsarray();
        load_favs_adapter();
    }

    private int get_id_position_in_array(String item_id){
        for (int i=0; i<items_toshow.size(); i++){
            if (items_toshow.get(i)[1].equals(item_id)){
                return i;
            }
        }
        return 0;
    }

    private void scan_popup(View v){
        //chequear si esta activo y desactivarlo.
        if (running_thread){
            cancel_thread = true;
            return;
        }

        try{
            LayoutInflater inflater = (LayoutInflater) Act_Items.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            View layout = inflater.inflate(R.layout.layout_pop_scan, (ViewGroup) findViewById(R.id.pop_scan));

            int width = LinearLayout.LayoutParams.MATCH_PARENT;
            int height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics()));

            pop_scan = new PopupWindow(layout, width, height, true);
            pop_scan.setContentView(layout);
            pop_scan.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            pop_scan.showAtLocation(v, Gravity.BOTTOM, 0,0);
            pop_scan.setOutsideTouchable(true);
            pop_scan.setFocusable(true);
            pop_scan.getContentView().setFocusableInTouchMode(true);
            pop_scan.getContentView().setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        pop_scan.dismiss();
                        return true;
                    }
                    return false;
                }
            });
            //cosas del popup
            final EditText et_scan_start = layout.findViewById(R.id.et_scan_start);
            final EditText et_scan_end = layout.findViewById(R.id.et_scan_end);
            LinearLayout bt_scannow = layout.findViewById(R.id.bt_scannow);
            LinearLayout bt_scan_close = layout.findViewById(R.id.bt_scan_close);

            //ampliacion de rango de cobertura de 500 items, para maximizar posibilidades de encontrar items nuevos.
            int desde = Integer.valueOf(items_toshow.get(getGridVisibleItem())[1]);
            if (desde > 500){
                desde = desde - 500;
            }
            //obtener rango predefinido.
            et_scan_start.setText("" + desde);
            et_scan_end.setText("" + Utils.CONST_BASEITEMSLIMIT);

            bt_scannow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    check_itemrange(Integer.valueOf(et_scan_start.getText().toString()) , Integer.valueOf(et_scan_end.getText().toString()));
                    pop_scan.dismiss();
                }
            });

            bt_scan_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pop_scan.dismiss();
                }
            });

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void search_popup(View v){
        try{
            LayoutInflater inflater = (LayoutInflater) Act_Items.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            View layout = inflater.inflate(R.layout.layout_pop_search, (ViewGroup) findViewById(R.id.pop_search));

            int width = LinearLayout.LayoutParams.MATCH_PARENT;
            int height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics()));

            pop_search = new PopupWindow(layout, width, height, true);
            pop_search.setContentView(layout);
            pop_search.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            pop_search.showAtLocation(v, Gravity.BOTTOM, 0,0);
            pop_search.setOutsideTouchable(true);
            pop_search.setFocusable(true);
            pop_search.getContentView().setFocusableInTouchMode(true);
            pop_search.getContentView().setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        pop_search.dismiss();
                        return true;
                    }
                    return false;
                }
            });
            //cosas del popup
            ImageButton bt_searchquery, bt_cancelquery;
            bt_searchquery = layout.findViewById(R.id.bt_searchquery);
            bt_cancelquery = layout.findViewById(R.id.bt_cancelquery);
            final EditText et_query = layout.findViewById(R.id.et_query);

            et_query.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if(actionId == EditorInfo.IME_ACTION_SEARCH){
                        search_query(et_query.getText().toString());
                        pop_search.dismiss();
                    }
                    return false;
                }
            });

            bt_searchquery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //buscar
                    search_query(et_query.getText().toString());
                    pop_search.dismiss();
                }
            });
            bt_cancelquery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //cancelar
                    //init_load();
                    pop_search.dismiss();
                }
            });



        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private class task_searchquery extends AsyncTask<String, String, String>{
        ArrayList<String[]> reslist = new ArrayList<>();
        String cadena;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cl_items_loading.setVisibility(View.VISIBLE);
            search_active = true;
        }

        @Override
        protected String doInBackground(String... strings) {
            cadena = strings[0];
            String[] words = cadena.split("\\s+");
            String cadena_p = "^";
            for (int a = 0; a < words.length; a++) {
                words[a] = words[a].replaceAll("[^\\w]", "");
                cadena_p = cadena_p + "(?=.*\\b"+ words[a] +"\\b)";
            }
            Pattern regex = Pattern.compile(cadena_p, Pattern.CASE_INSENSITIVE);

            for (int i=0; i < Act_Main.item_names.length; i++){
                Matcher match = regex.matcher(Act_Main.item_names[i]);
                if (match.find()){
                    if (is_this_type(i)){
                        reslist.add(new String[]{get_url(item_type,i),String.valueOf(i), "0"});
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            cl_items_loading.setVisibility(View.GONE);
            if (reslist.isEmpty()){
                //si esta vacio hacer esto.
                new CustomToast(Act_Items.this, "No hay coincidencias.", Toast.LENGTH_SHORT, Gravity.CENTER);
            } else {
                //reload all and replace items with reslist items.
                items.clear();
                items.addAll(reslist);
                reslist.clear();

                generate_validarray();
                if (favs_active){
                    generate_favsarray();
                    load_favs_adapter();
                } else {
                    load_adapter();
                }

                //open search data
                tv_searchstring.setText(cadena);
                cl_currentsearchinfo.setVisibility(View.VISIBLE);
                //save search state
                avatarview.items_searchstate[item_type] = true;
                avatarview.items_searchstring[item_type] = cadena;

            }
        }
    }

    private void search_query(String cadena){
        //detector de comandos:
        if (cadena.length() >= 3){
            //comandos.
            switch (cadena.substring(0,3).toLowerCase()){
                case ".id":
                    if (cadena.length() > 4){
                        String id = cadena.substring(3, cadena.length()).replace(" ","");
                        int id_position = get_id_position_in_array(id);
                        scroll_loader(id_position);
                        String[] data = {id, String.valueOf(item_type), get_itemname(id_position), "","",""};
                        open_popup(getWindow().getDecorView().getRootView(), data);
                    }
                    return;
                case ".rn":
                    Random random = new Random();
                    String r_item = items_toshow.get(random.nextInt(items_toshow.size()))[1];
                    String[] data = {r_item, String.valueOf(item_type), "", "", "", ""};
                    open_popup(getWindow().getDecorView().getRootView(), data);
                    return;
            }
        } else if (cadena.isEmpty() || cadena.equals(" ")){
            //si tiene un espacio o esta vacio.
            new CustomToast(Act_Items.this, "Necesita escribir una palabra clave.", Toast.LENGTH_SHORT, Gravity.BOTTOM);
            return;
        }
        new task_searchquery().execute(cadena);
    }

    private void save_savsearch(int item_type, ArrayList<String[]> savarray){
        SaveManager smg = new SaveManager(getApplicationContext(), 1);
        smg.saveArray("search_" + item_type, savarray);
    }

    private Boolean load_savsearch(int item_type){
        SaveManager smg = new SaveManager(getApplicationContext(), 1);
        items_saved_search = smg.getArray("search_" + item_type);
        return !items_saved_search.get(0)[0].equals("0");
    }

    public Boolean is_this_type(int item){
        if (favs_active && !search_active){
            for (int i=0; i<items.size(); i++){
                if (!items.get(i)[1].equals("nulo")){
                    if (Integer.parseInt(items.get(i)[1]) == item){
                        return true;
                    }
                }
            }
            return false;
        }

        for (int i=0; i<items_toshow.size(); i++){
            if (Integer.parseInt(items_toshow.get(i)[1]) == item){
                return true;
            }
        }
        return false;
    }

    private void open_popup(View v, final String[] data){
        try {
            LayoutInflater inflater = (LayoutInflater) Act_Items.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            View layout = inflater.inflate(R.layout.layout_pop_item, (ViewGroup) findViewById(R.id.pop_item));
            pop_item = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
            pop_item.setContentView(layout);
            pop_item.showAtLocation(v, Gravity.TOP, 0, 0);
            pop_item.setOutsideTouchable(true);
            pop_item.setFocusable(true);
            pop_item.getContentView().setFocusableInTouchMode(true);
            pop_item.getContentView().setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        pop_item.dismiss();
                        return true;
                    }
                    return false;
                }
            });

            //cosas del popup
            ImageView iv_item_preview = layout.findViewById(R.id.iv_item_preview);
            ImageView iv_rarity_preview = layout.findViewById(R.id.iv_rarity_preview);
            iv_item_loading = layout.findViewById(R.id.iv_item_loading);
            tv_item_name = layout.findViewById(R.id.tv_item_name);
            TextView tv_item_id = layout.findViewById(R.id.tv_item_id);
            TextView tv_item_type = layout.findViewById(R.id.tv_item_type);
            final TextView tv_item_data = layout.findViewById(R.id.tv_item_data);
            final EditText et_item_data = layout.findViewById(R.id.et_item_data);
            final TextView tv_modify_data_text = layout.findViewById(R.id.tv_modifydata_text);

            LinearLayout bt_select = layout.findViewById(R.id.bt_select);
            final LinearLayout bt_addfavs = layout.findViewById(R.id.bt_addfavs);
            final ImageView iv_addfavs = layout.findViewById(R.id.iv_addfavs);

            LinearLayout bt_modifydata = layout.findViewById(R.id.bt_modifydata);

            //cargar vista previa
            load_preview(layout.getContext(),iv_item_preview,Integer.valueOf(data[0]),Integer.valueOf(data[1]));
            //cargar datos del item
            tv_item_id.setText("" + data[0]);
            tv_item_type.setText("" + define_typename(Integer.valueOf(data[1])));
            tv_item_name.setText("\u3010" + data[2] + "\u3011");

            //datos del usuario.
            SaveManager smg = new SaveManager(getApplicationContext(), 3);
            items_data = smg.getArray("ItemsData");
            tv_item_data.setText("" + get_user_items_data(data[0]));

            //check rarity.
            iv_rarity_preview.setImageDrawable(get_rarity_drawable(Integer.valueOf(data[0])));
            if (item_is_fav(data[0])){
                is_faved = true;
                //bt_addfavs.setColorFilter(getResources().getColor(R.color.icons_tint));
                iv_addfavs.setColorFilter(getResources().getColor(R.color.icons_tint));
            } else {
                is_faved = false;
            }

            //carga del nombre desde internet.
            new thread_getname().execute(data[0]);

            bt_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    accept_item(Integer.valueOf(data[0]));
                }
            });

            bt_modifydata.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!is_modifying){
                        //ocultar el tv y mostrar el et.
                        tv_item_data.setVisibility(View.GONE);
                        et_item_data.setVisibility(View.VISIBLE);

                        //cambiar texto del boton de modify.
                        tv_modify_data_text.setText("Guardar");

                        //activar toggle
                        is_modifying = true;
                    } else {
                        String datos = et_item_data.getText().toString();
                        if (!datos.isEmpty()){
                            //checkear si el item ya tiene datos.
                            int position = check_user_item_exists_position(data[0]);
                            if (position == -1){
                                items_data.add(new String[]{data[0], datos});
                            } else {
                                items_data.set(position, new String[]{data[0], datos});
                            }

                            //guardar los resultados.
                            SaveManager smg = new SaveManager(getApplicationContext(), 3);
                            smg.saveArray("ItemsData", items_data);
                            tv_item_data.setText(datos);
                        }
                        tv_item_data.setVisibility(View.VISIBLE);
                        et_item_data.setVisibility(View.GONE);
                        tv_modify_data_text.setText("Ed. Notas");
                        is_modifying = false;
                        //Toast.makeText(getApplicationContext(), "Datos Añadidos.", Toast.LENGTH_SHORT).show();
                        new CustomToast(Act_Items.this, "Datos Añadidos.", Toast.LENGTH_SHORT, Gravity.BOTTOM);
                    }

                }
            });

            bt_addfavs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SaveManager sgr = new SaveManager(getApplicationContext(), 1);

                    if (!is_faved){
                        items_favs.add(new String[]{data[0]});
                        iv_addfavs.setColorFilter(getResources().getColor(R.color.icons_tint));

                    } else {
                        //remove id
                        remove_fav(data[0]);
                        iv_addfavs.setColorFilter(Color.argb(255, 0, 0, 0));

                    }
                    sgr.saveArray("favoritos",items_favs);
                    if (favs_active){
                        init_favs();
                    }
                    is_faved = item_is_fav(data[0]);

                }
            });


        }catch (Exception e){
            //e.printStackTrace();
        }

    }

    private Drawable get_rarity_drawable(int id){
        int rarity = Integer.valueOf(Act_Main.item_rarity[id]);
        switch (rarity){
            case 1:
                return getResources().getDrawable(R.drawable.n_icon);
            case 2:
                return getResources().getDrawable(R.drawable.r_icon);
            case 3:
                return getResources().getDrawable(R.drawable.rr_icon);
            case 4:
                return getResources().getDrawable(R.drawable.sr_icon);
            case 5:
                return getResources().getDrawable(R.drawable.mr_icon);
            default:
                return getResources().getDrawable(R.drawable.all_icon);
        }
    }

    private String get_user_items_data(String item_id){
        //0 id
        //1 texto
        for (int i=0; i<items_data.size(); i++){
            if (items_data.get(i)[0].equals(item_id)){
                return items_data.get(i)[1];
            }
        }
        return "No hay datos.";

    }

    private int check_user_item_exists_position(String item_id){
        for (int i=0; i<items_data.size(); i++){
            if (items_data.get(i)[0].equals(item_id)){
                return i;
            }
        }
        return -1;
    }

    private String define_typename(int type){
        // body 1
        // bodyacc 2
        // hairacc 3
        // faceacc 4
        // backacc 5
        // background 6
        // hair 7
        // front 8
        switch (type){
            case 1:
                return "Body Outfit";
            case 2:
                return "Body Accessory";
            case 3:
                return "Hair Accessory";
            case 4:
                return "Face Accessory";
            case 5:
                return "Back Accessory";
            case 6:
                return "Background";
            case 7:
                return "Hair";
            case 8:
                return "Front";
        }
        return "null";
    }

    private void accept_item(int item){
        set_itemdata(item_type, item_kind, item);
        setResult(RESULT_OK);
        finish();
    }

    private void set_itemdata(int item_type, int item_kind, int item){
        // body 1
        // bodyacc 2
        // hairacc 3
        // faceacc 4
        // backacc 5
        // background 6
        // hair 7
        // front 8

        //check if locked
        if (!avatarview.is_locked(item_type, item_kind)){

            //setear el nombre.
            avatarview.setname = get_itemnamebyid(item);
            switch (item_type){
                case 1:
                    avatarview.outfit = String.valueOf(item);
                    break;
                case 2:
                    if (item_kind == 1){
                        avatarview.bodyacc1 = String.valueOf(item);
                    } else {
                        avatarview.bodyacc2 = String.valueOf(item);
                    }
                    break;
                case 3:
                    if (item_kind == 1){
                        avatarview.hairacc1 = String.valueOf(item);
                    } else {
                        avatarview.hairacc2 = String.valueOf(item);
                    }
                    break;
                case 4:
                    if (item_kind == 1){
                        avatarview.faceacc1 = String.valueOf(item);
                    } else {
                        avatarview.faceacc2 = String.valueOf(item);
                    }
                    break;
                case 5:
                    if (item_kind == 1){
                        avatarview.backacc1 = String.valueOf(item);
                    } else {
                        avatarview.backacc2 = String.valueOf(item);
                    }
                    break;
                case 6:
                    avatarview.bkg = String.valueOf(item);
                    break;
                case 7:
                    if (item_kind == 1){
                        avatarview.hair = String.valueOf(item);
                    } else {
                        avatarview.hair2 = String.valueOf(item);
                    }
                    break;
                case 8:
                    avatarview.front = String.valueOf(item);
                    break;
            }
        }
    }

    private void sendto_first(View view){
        rv_items.scrollToPosition(0);
    }
    private void sendto_last(View view){
        rv_items.scrollToPosition(items_toshow.size() - 1);
    }

    private void load_preview(Context context, ImageView iv, int id, int type){
        iv_item_loading.setVisibility(View.VISIBLE);
        Picasso.get()
                .load(get_preview_url(id, type))
                .into(iv, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        iv_item_loading.setVisibility(View.GONE);
                    }
                    @Override
                    public void onError(Exception e) {
                        iv_item_loading.setVisibility(View.GONE);
                    }
                });

    }

    private String get_preview_url(int id, int type){
        String default_data = "0";
        String image_quality = "1x1";
        switch (type){
            case 1:
                //body
                default_data = "http://" + avatarview.a_server[avatarview.server] + "/img/php/all_param2.php?12&id1=0,"+ id +",0,0,0,0,0,0,0,0,103,1308,749,749,0,0,0,0,0,0,0,0,0&id2=2&id3=1&id4=2&id5=0&id6=0x1&id7=" + image_quality;
                break;
            case 2:
                //bodyacc
                default_data = "http://" + avatarview.a_server[avatarview.server] + "/img/php/all_param2.php?12&id1=0,0,"+ id +",0,0,0,0,0,0,0,103,1308,749,749,0,0,0,0,0,0,0,0,0&id2=2&id3=1&id4=2&id5=0&id6=0x1&id7=" + image_quality;
                break;
            case 3:
                //hairacc
                default_data = "http://" + avatarview.a_server[avatarview.server] + "/img/php/all_param2.php?12&id1=0,0,0,0,"+id+",0,0,0,0,0,103,1308,749,749,0,0,0,0,0,0,0,0,0&id2=2&id3=1&id4=2&id5=0&id6=0x1&id7=" + image_quality;
                break;
            case 4:
                //faceacc
                default_data = "http://" + avatarview.a_server[avatarview.server] +"/img/php/all_param2.php?12&id1=0,0,0,0,0,0,"+ id +",0,0,0,103,1308,749,749,0,0,0,0,0,0,0,0,0&id2=2&id3=1&id4=2&id5=0&id6=0x1&id7=" + image_quality;
                break;
            case 5:
                //backacc
                default_data = "http://" + avatarview.a_server[avatarview.server] +"/img/php/all_param2.php?12&id1=0,0,0,0,0,0,0,0,"+ id +",0,103,1308,749,749,0,0,0,0,0,0,0,0,0&id2=2&id3=1&id4=2&id5=0&id6=0x1&id7=" + image_quality;
                break;
            case 6:
                //background
                default_data = "http://" + avatarview.a_server[avatarview.server] +"/img/php/all_param2.php?12&id1=0,0,0,0,0,0,0,0,0,0,"+ id +",1308,749,749,0,0,0,0,0,0,0,0,0&id2=2&id3=1&id4=2&id5=0&id6=0x1&id7=" + image_quality;
                break;
            case 7:
                //hair
                default_data = "http://" + avatarview.a_server[avatarview.server] +"/img/php/all_param2.php?12&id1=0,0,0,0,0,0,0,0,0,0,103,"+ id +",749,749,0,0,0,0,0,0,0,0,0&id2=2&id3=1&id4=2&id5=0&id6=0x1&id7=" + image_quality;
                break;
            case 8:
                //front
                default_data = "http://" + avatarview.a_server[avatarview.server] +"/img/php/all_param2.php?12&id1="+ id +",0,0,0,0,0,0,0,0,0,103,1308,749,749,0,0,0,0,0,0,0,0,0&id2=2&id3=1&id4=2&id5=0&id6=0x1&id7=" + image_quality;
                break;
        }
        return default_data;
    }

    private void update_adapter(){
       generate_validarray();
       adapter.notifyDataSetChanged();
       //rv_items.setAdapter(adapter);
    }

    private void load_favs_adapter(){
        //adapter = new RVItems(this, items_toshow);
        //rv_items.setAdapter(adapter);
        //GridLayoutManager rv_glm = new GridLayoutManager(this, 7, GridLayoutManager.HORIZONTAL, false);
        //LinearLayoutManager rv_glm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        //rv_items.setLayoutManager(rv_glm);
        favadapter = new RVFavItems(this, items_toshow);
        rv_items.setAdapter(favadapter);
        rv_items.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
    }

    private void load_adapter(){
        adapter = new RVItems(this, items_toshow);
        rv_items.setAdapter(adapter);
        //GridLayoutManager rv_glm = new GridLayoutManager(this, 7, GridLayoutManager.HORIZONTAL, false);
        //appy adaptive layout manager.
        GridAutoFitLayoutManager rv_galm = new GridAutoFitLayoutManager(this, 66, GridAutoFitLayoutManager.HORIZONTAL, false);
        rv_items.setLayoutManager(rv_galm);
    }

    private void generate_validarray(){
        items_toshow.clear();

        //agregar el quita items.
        items_toshow.add(new String[]{get_url(item_type,0),"0","0","0"});

        for (int i=0; i < items.size(); i++){
            if (!items.get(i)[0].equals("nulo")){
                items_toshow.add(items.get(i));
            }
        }
    }

    private void generate_favsarray(){
        //items_toshow.clear();
        ArrayList<String[]> temparray = new ArrayList<>();
        for (int i=0; i< items_favs.size(); i++){
            if (!items_favs.get(i)[0].equals("0")){
                cv_nofavs.setVisibility(View.GONE);
                if (is_this_type(Integer.valueOf(items_favs.get(i)[0]))){
                    String prev_url = get_preview_url(Integer.valueOf(items_favs.get(i)[0]),item_type);
                    String icon_url = get_url(item_type, Integer.valueOf(items_favs.get(i)[0]));
                    temparray.add(new String[]{prev_url,items_favs.get(i)[0],"0", icon_url, get_itemnamebyid(Integer.valueOf(items_favs.get(i)[0]))});
                }
            } else {
                cv_nofavs.setVisibility(View.VISIBLE);
            }
        }
        items_toshow.clear();
        items_toshow.addAll(temparray);
    }

    private void generate_sortarray(String item_rarity){
        ArrayList<String[]> temparray = new ArrayList<>();
        for (int i=0; i < items_toshow.size(); i++) {
            if (i < Act_Main.item_rarity.length){
                if (Act_Main.item_rarity[Integer.valueOf(items_toshow.get(i)[1])].equals(item_rarity)){
                    temparray.add(items_toshow.get(i));
                }
            }
        }
        items_toshow.clear();
        items_toshow.addAll(temparray);
    }

    private ArrayList<String> get_xmlname(int itemid){
        //cambiar useragent segun el server seleccionado.
        String uag = "";
        String url = "";
        if (avatarview.server == 0){
            //usa
            uag = new String(Base64.decode(MapValues.usa_uag, Base64.DEFAULT));
            url = "http://us-nizi2d-app.amz-aws.jp/fitting.php?avatar_id=";
        } else {
            //jp
            uag = new String(Base64.decode(MapValues.jp_uag, Base64.DEFAULT));;
            url = "http://app.nizikano-2d.jp/fitting.php?avatar_id=";
        }

        ArrayList<String> lines = new ArrayList<>();
        Request request = new Request.Builder()
                .header("User-Agent", uag)
                .url(url + itemid)
                .build();

        try {
            Response response = client.newCall(request).execute();
            BufferedReader in = new BufferedReader(new InputStreamReader(response.body().byteStream(), "UTF-8"));
            String line = null;
            while ((line = in.readLine()) != null) {
                lines.add(line);
            }
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    private class thread_getname extends AsyncTask<String, String, String>{
        ArrayList<String> item_xml = new ArrayList<>();
        String param_itemid;
        String result_name = "5pm";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //tv_info.setText("Obteniendo nombre...");
                    //tv_item_name.setText("Cargando...");
                }
            });

        }

        @Override
        protected String doInBackground(String... strings) {

            param_itemid = strings[0];
            item_xml = get_xmlname(Integer.valueOf(param_itemid));

            String cadena;
            if (item_xml.size() > 51){
                if (avatarview.server == 0){
                    cadena = item_xml.get(51);
                } else {
                    cadena = item_xml.get(53);
                }
                String value1 = String.valueOf("\u3010");
                String value2 = String.valueOf("\u3011");
                int char1 = cadena.indexOf(value1);
                int char2 = cadena.indexOf(value2);
                if (char1 != -1 || char2 != -1){
                    result_name = cadena.substring((char1 + 1), (char2));
                }

                //actualizar archivo de indice solo de usa:
                if (avatarview.server == 0 && !result_name.equals("5pm")){
                    try {
                        ArrayList<String> temp_index = Utils.array2list(Act_Main.item_names);
                        int item_id = Integer.valueOf(param_itemid);
                        if (item_id < temp_index.size()){
                            temp_index.set(item_id, result_name);
                            String arr_temp_index[] = Utils.list2array(temp_index);
                            Utils.create_file_by_array(getApplicationContext(), "indice.txt", arr_temp_index);
                            Act_Main.item_names = arr_temp_index;
                            avatarview.setname = result_name;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            } else {
                result_name = "Unknown";
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //tv_info.setText(" " + result_name);
                    if (tv_item_name != null){
                        tv_item_name.setText("\u3010" + result_name + "\u3011");
                    }
                }
            });

        }
    }





    private String get_itemnamebyid(int itemid){
        String itemname = "Unknown";
        if (itemid <= Act_Main.item_names.length){
            itemname = Act_Main.item_names[itemid];
            if (itemname.equals("") || itemname.equals(" ")){
                itemname = "Unknown";
            }
        }
        return itemname;
    }

    private String get_itemname(int position){
        String itemname = "Unknown";
        if (Integer.valueOf(items_toshow.get(position)[1]) <= Act_Main.item_names.length){
            itemname = Act_Main.item_names[Integer.valueOf(items_toshow.get(position)[1])];
            if (itemname.equals("") || itemname.equals(" ")){
                itemname = "Unknown";
            }
        }
        return itemname;
    }

    private void generate_basearray(int start, int end){
        items.clear();
        for (int i=start; i<end; i++){
            //url - id - activo/recien escaneado? - nuevo?
            items.add(new String[]{"nulo","nulo","0","0"});
        }
    }

    private void generate_database(int item_type){
        // body 1
        // bodyacc 2
        // hairacc 3
        // faceacc 4
        // backacc 5
        // background 6
        // hair 7
        // front 8

        switch (item_type){
            case 1:
                selected_txtfile = get_server_asset_name("list");
                if (avatarview.server == 0){
                    generate_database_byfile(Act_Main.list_outfits);
                }
                if (avatarview.server == 1){
                    generate_database_byfile(Act_Main.list_outfits_jp);
                }
                break;
            case 2:
                selected_txtfile = get_server_asset_name("body_acc");
                if (avatarview.server == 0){
                    generate_database_byfile(Act_Main.list_bodyacc);
                }
                if (avatarview.server == 1){
                    generate_database_byfile(Act_Main.list_bodyacc_jp);
                }
                break;
            case 3:
                selected_txtfile = get_server_asset_name("hats");
                if (avatarview.server == 0){
                    generate_database_byfile(Act_Main.list_hats);
                }
                if (avatarview.server == 1){
                    generate_database_byfile(Act_Main.list_hats_jp);
                }
                break;
            case 4:
                selected_txtfile = get_server_asset_name("hats");
                if (avatarview.server == 0){
                    generate_database_byfile(Act_Main.list_face_acc);
                }
                if (avatarview.server == 1){
                    generate_database_byfile(Act_Main.list_face_acc_jp);
                }
                break;
            case 5:
                selected_txtfile = get_server_asset_name("back_acc");
                if (avatarview.server == 0){
                    generate_database_byfile(Act_Main.list_back_acc);
                }
                if (avatarview.server == 1){
                    generate_database_byfile(Act_Main.list_back_acc_jp);
                }
                break;
            case 6:
                selected_txtfile = get_server_asset_name("bkg");
                if (avatarview.server == 0){
                    generate_database_byfile(Act_Main.list_bkg);
                }
                if (avatarview.server == 1){
                    generate_database_byfile(Act_Main.list_bkg_jp);
                }
                break;
            case 7:
                selected_txtfile = get_server_asset_name("hairs");
                if (avatarview.server == 0){
                    generate_database_byfile(Act_Main.list_hairs);
                }
                if (avatarview.server == 1){
                    generate_database_byfile(Act_Main.list_hairs_jp);
                }
                break;
            case 8:
                selected_txtfile = get_server_asset_name("front");
                if (avatarview.server == 0){
                    generate_database_byfile(Act_Main.list_front);
                }
                if (avatarview.server == 1){
                    generate_database_byfile(Act_Main.list_front_jp);
                }
                break;
        }
    }

    private String get_server_asset_name(String filena){
        if (avatarview.server == 0){
            //server usa
            return filena + ".txt";
        }
        if (avatarview.server == 1) {
            //server jp
            return filena + "_jp.txt";
        }
        //sino retornar el default
        return filena + ".txt";
    }

    private void generate_database_byfile(String[] array){
        //String[] dbase = array
        for (String aDbase : array) {
            items.set(Integer.valueOf(aDbase), new String[]{get_url(item_type, Integer.valueOf(aDbase)), aDbase});
        }
    }

    private void check_itemrange(int desde, int hasta){
        //bt_update.setTintMode
        if (!running_thread){
            bt_update.setColorFilter(Color.argb(255,255,170,170));
            new thread_itemcheck().execute(String.valueOf(desde), String.valueOf(hasta));
        } else {
            bt_update.setColorFilter(getResources().getColor(R.color.icons_tint));
            cancel_thread = true;
        }

    }

    private class thread_itemcheck extends AsyncTask<String, String, String> {
        int ant_itempos;
        int pbprogress = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //guardar posicion del gridview para futuro.
            running_thread = true;
            cancel_thread = false;
            ant_itempos = getGridVisibleItem();
            //tv_info.setTextColor(Color.argb(255,0,0,0));
            tv_info.setText("Analizando: " + Integer.valueOf(items_toshow.get(getGridVisibleItem())[1]));
            pb_anprogress.setProgress(pbprogress);
        }

        @Override
        protected String doInBackground(String... params) {
            int desde = Integer.valueOf(params[0]);
            int hasta = Integer.valueOf(params[1]);

            //dividir el rango en 3 partes iguales.
            int tst = ((hasta - desde) / 3) + 2;
            int tst_tas = tst * 3;
            int tstlm = desde + tst;

            pb_anprogress.setMax(tst_tas);

            for (int i=desde; i<tstlm; i++){
                if (!cancel_thread) {
                    if (items.size() > i){
                        if (items.get(i)[0].equals("nulo") && items.get(i)[2].equals("0")) {
                            int rc1 = get_rcode(i);
                            if (rc1 == 200) {
                                publishProgress(String.valueOf(i));
                            }
                            String[] it = items.get(i);
                            items.set(i,new String[]{it[0],it[1],"1"});
                        }
                        pbprogress++;
                        print_info("Item: " + i);
                    }

                    int i2 = tst + i;
                    if (items.size() > i2){
                        if (items.get(i2)[0].equals("nulo") && items.get(i2)[2].equals("0")) {
                            int rc2 = get_rcode(i2);
                            if (rc2 == 200) {
                                publishProgress(String.valueOf(i2));
                            }
                            String[] it = items.get(i2);
                            items.set(i2,new String[]{it[0],it[1],"1"});
                        }
                        pbprogress++;
                        print_info("Item: " + i2);
                    }

                    int i3 = tst + tst + i;
                    if (items.size() > i3){
                        if (items.get(i3)[0].equals("nulo") && items.get(i3)[2].equals("0")) {
                            int rc3 = get_rcode(i3);
                            if (rc3 == 200) {
                                publishProgress(String.valueOf(i3));
                            }
                            String[] it = items.get(i3);
                            items.set(i3,new String[]{it[0],it[1],"1"});
                        }
                        pbprogress++;
                        print_info("Item: " + i3);
                    }
                    //actualizar progressbar
                    pb_anprogress.setProgress(pbprogress);
                } else {
                    //cancelar thread
                    running_thread = false;
                    break;
                }
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            int item_id = Integer.valueOf(values[0]);
            //aca actualizar la lista con nuevos items.
            //url - id - activo/recien escaneado? - nuevo?
            items.set(item_id, new String[]{get_url(item_type,item_id),values[0], "1", "1"});
            update_adapter();
            try {
                Utils.create_file_by_array(getApplicationContext(), selected_txtfile, Utils.create_array_by_list(items));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        @Override
        protected void onPostExecute(String params) {
            super.onPostExecute(params);
            tv_info.setText("Análisis completado.");
            pb_anprogress.setProgress(0);
            bt_update.setColorFilter(getResources().getColor(R.color.icons_tint));
            running_thread = false;
        }
    }

    private void print_info(final String text){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_info.setText(text);
            }
        });
    }

    private int getGridVisibleItem(){
        return ((GridLayoutManager)rv_items.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
    }
    private int getGridLastVisibleItem(){
        return ((GridLayoutManager) rv_items.getLayoutManager()).findLastCompletelyVisibleItemPosition();
    }
    public void scroll_loader(int position){
        rv_items.scrollToPosition(position);
    }
    public void scroll_saver(){
        avatarview.items_pos[item_type] = ((GridLayoutManager)rv_items.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
    }

    private String get_url(int item_type, int id){
        // body 1
        // bodyacc 2
        // hairacc 3
        // faceacc 4
        // backacc 5
        // background 6
        // hair 7
        // front 8
        String url = "http://us-nizi2d-r53.amz-aws.jp/img/avatar_thum/body1/1.png";
        switch (item_type){
            case 1:
                url = "http://" + avatarview.a_server[avatarview.server] + "/img/avatar_thum/body1/" + id + ".png";
                break;
            case 2:
                url = "http://" + avatarview.a_server[avatarview.server] + "/img/avatar_thum/body_acc/" + id +".png";
                break;
            case 3:
                url = "http://" + avatarview.a_server[avatarview.server] + "/img/avatar_thum/hair_acc/" + id +".png";
                break;
            case 4:
                url = "http://" + avatarview.a_server[avatarview.server] + "/img/avatar_thum/face_acc/" + id +".png";
                break;
            case 5:
                url = "http://" + avatarview.a_server[avatarview.server] + "/img/avatar_thum/back_acc/" + id +".png";
                break;
            case 6:
                url = "http://" + avatarview.a_server[avatarview.server] + "/img/avatar_thum/bg/" + id +".png";
                break;
            case 7:
                url = "http://" + avatarview.a_server[avatarview.server] + "/img/avatar_thum/hair/" + id +".png";
                break;
            case 8:
                url = "http://" + avatarview.a_server[avatarview.server] + "/img/avatar_thum/front/" + id +".png";
                break;
        }

        return url;
    }



    private int get_async_rcode(int num){
        final int[] codigo = {0};
        Request request = new Request.Builder()
                .url("http://us-nizi2d-r53.amz-aws.jp/img/avatar_thum/body1/" + num + ".png")
                .method("HEAD", null)
                .build();

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, IOException e) {
                     codigo[0] = 404;
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        codigo[0] = response.code();
                    }
                });
        return codigo[0];
    }

    private int get_rcode(int num){
        Request request = new Request.Builder()
                .url(get_url(item_type, num))
                .method("HEAD", null)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.code();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void read_savedata() {
        //indice
        Act_Main.item_names = checkread_filearray("indice.txt");

        //sorting
        Act_Main.item_rarity = checkread_filearray("items_sort.txt");

        //back_acc y jp
        Act_Main.list_back_acc = checkread_filearray("back_acc.txt");
        Act_Main.list_back_acc_jp = checkread_filearray("back_acc_jp.txt");

        //bkg y jp
        Act_Main.list_bkg = checkread_filearray("bkg.txt");
        Act_Main.list_bkg_jp = checkread_filearray("bkg_jp.txt");

        //body_acc y jp
        Act_Main.list_bodyacc = checkread_filearray("body_acc.txt");
        Act_Main.list_bodyacc_jp = checkread_filearray("body_acc_jp.txt");

        //face_acc y jp
        Act_Main.list_face_acc = checkread_filearray("face_acc.txt");
        Act_Main.list_face_acc_jp = checkread_filearray("face_acc_jp.txt");

        //front y jp
        Act_Main.list_front = checkread_filearray("front.txt");
        Act_Main.list_front_jp = checkread_filearray("front_jp.txt");

        //hairs y jp
        Act_Main.list_hairs = checkread_filearray("hairs.txt");
        Act_Main.list_hairs_jp = checkread_filearray("hairs_jp.txt");

        //hats y jp
        Act_Main.list_hats = checkread_filearray("hats.txt");
        Act_Main.list_hats_jp = checkread_filearray("hats_jp.txt");

        //outfits y jp
        Act_Main.list_outfits = checkread_filearray("list.txt");
        Act_Main.list_outfits_jp = checkread_filearray("list_jp.txt");

    }

    //todo mover todas estas funciones a una clase externa para no repetir codigo.
    private String[] checkread_filearray(String filename){
        String[] lines = {};
        try {
            if (file_exist(filename)){
                if (bigger_than_assets(filename) || filename.equals("indice.txt")){
                    //si es mas extenso que assets cargar.
                    lines = open_file(filename);
                } else {
                    lines = readLines(filename);
                }
            } else {
                //read assets.
                lines = readLines(filename);

            }} catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }
    public Boolean bigger_than_assets(String filename) throws IOException {
        String[] assetsfile = readLines(filename);
        String[] opfile = open_file(filename);
        return assetsfile.length < opfile.length;
    }

    public Boolean file_exist(String filename){
        File file = getBaseContext().getFileStreamPath(filename);
        return file.exists();
    }
    public String[] readLines(String filename) throws IOException {
        InputStream is = getResources().getAssets().open(filename);
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        //FileReader fileReader = new FileReader()
        //BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = in.readLine()) != null) {
            lines.add(line);
        }
        in.close();
        return lines.toArray(new String[lines.size()]);
    }
    public String[] open_file(String filename) throws IOException {
        //Byte[] txt = {0};
        FileInputStream fis;
        String line;
        fis = openFileInput(filename);
        BufferedReader in = new BufferedReader(new InputStreamReader(fis));
        List<String> lines = new ArrayList<>();

        while ((line = in.readLine()) != null) {
            lines.add(line);
        }
        in.close();
        return lines.toArray(new String[lines.size()]);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancel_thread = true;
        //guardar posicion.
        if (!favs_active){
            scroll_saver();
        }

        //si esta activa la busqueda guardarla
        if (search_active){
            save_savsearch(item_type, items_toshow);
        }

        //aca guardar los datos nuevos del sorting.
        (new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                //Log.d("bta:", "se esta guardando el array, tamaño: " + Act_Main.item_rarity.length);
                try {
                    Utils.create_file_by_array(getApplicationContext(), "items_sort.txt", Act_Main.item_rarity);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }).execute();

        //aca reactualizar la lista de todos los items.
        read_savedata();

    }
}
