package com.software.ddk.dreamgen.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.software.ddk.dreamgen.R;
import com.software.ddk.dreamgen.activity.Act_Main;
import com.software.ddk.dreamgen.adapter.RVDesignItems;
import com.software.ddk.dreamgen.utils.CustomToast;
import com.software.ddk.dreamgen.utils.RecyclerItemClickListener;
import com.software.ddk.dreamgen.utils.SaveManager;
import com.software.ddk.dreamgen.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.internal.Util;

public class designsview extends Fragment {

    //LinearLayout bt_adddesign;
    private FloatingActionButton fab_adddesign;
    private PopupWindow pop_save_design, pop_design_item, pop_design_edit_item;
    private RecyclerView rv_designs;
    private RVDesignItems desadapter;
    private CardView cv_notdesigns;
    private ImageView iv_des_loading;

    private ArrayList<String[]> designs = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_designsview, parent, false);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fab_adddesign = view.findViewById(R.id.fab_adddesign);
        rv_designs = view.findViewById(R.id.rv_designs);
        cv_notdesigns = view.findViewById(R.id.cv_notdesigns);
        iv_des_loading = view.findViewById(R.id.iv_des_loading);

        //cargar datos guardados.
        SaveManager smg = new SaveManager(getContext(), SaveManager.SAVETYPE_DESIGNDATA);
        designs = smg.getArray("designsets");

        check_design_list();

        LinearLayoutManager ll_designs = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        ll_designs.setStackFromEnd(true);
        rv_designs.setLayoutManager(ll_designs);
        desadapter = new RVDesignItems(getContext(), designs);
        rv_designs.setAdapter(desadapter);

        //checkear si designerview esta abierto.
        if (Act_Main.navigation.getSelectedItemId() != R.id.navigation_live2d){
            fab_adddesign.setVisibility(View.GONE);
        }

        //listeners
        rv_designs.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), rv_designs, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                pop_open_design_info(view, position);
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        }));

        fab_adddesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop_save_design(view);
            }
        });

    }


    private void check_design_list(){
        if (designs.size() > 0){
            if (designs.get(0)[0].equals("0")){
                designs.remove(0);
                cv_notdesigns.setVisibility(View.VISIBLE);
            } else {
                cv_notdesigns.setVisibility(View.GONE);
            }
        } else {
            cv_notdesigns.setVisibility(View.VISIBLE);
        }
    }

    private void pop_save_design(View v){
        fab_adddesign.hide();
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View layout = inflater.inflate(R.layout.layout_pop_newdesign, (ViewGroup) v.findViewById(R.id.pop_newdesign));

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics()));

        pop_save_design = new PopupWindow(layout, width, height, true);
        pop_save_design.setContentView(layout);
        pop_save_design.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        pop_save_design.showAtLocation(v, Gravity.BOTTOM, 0,0);
        pop_save_design.setOutsideTouchable(true);
        pop_save_design.setFocusable(true);
        pop_save_design.getContentView().setFocusableInTouchMode(true);
        pop_save_design.getContentView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    fab_adddesign.show();
                    pop_save_design.dismiss();
                    return true;
                }
                return false;
            }
        });

        final EditText et_desname = layout.findViewById(R.id.et_desname);
        final EditText et_desdesc = layout.findViewById(R.id.et_setdesdesc);
        ImageButton bt_addnewdesign = layout.findViewById(R.id.bt_addnewdesign);
        ImageButton bt_cancelnewdesign = layout.findViewById(R.id.bt_cancelnewdesign);

        bt_addnewdesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //save_design(et_desname.getText().toString());
                String des_name = et_desname.getText().toString();
                String des_desc = et_desdesc.getText().toString();

                if (des_name.isEmpty() || des_name.equals(" ")){
                    des_name = "Mi Diseño";
                }
                if (des_desc.isEmpty() || des_desc.equals(" ")){
                    des_desc = "Diseño sin descripción.";
                }

                async_saving(des_name, des_desc);
                fab_adddesign.show();
                pop_save_design.dismiss();
            }
        });

        bt_cancelnewdesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab_adddesign.show();
                pop_save_design.dismiss();
            }
        });

    }

    private void open_design_external(int position){
        //solo se envian los datos del diseño para que designerview los cargue directamente.
        Act_Main.SELECTED_DESIGN_DATA = designs.get(position);
        //se setea el flag de que hay un diseño que se quiere cargar.
        Act_Main.LOAD_DESIGN_DATA = true;

    }

    private void open_design(int position){
        String folder = designs.get(position)[1];

        designerview.bmp_design[0] = open_bitmap(folder, "layer_0.png");
        designerview.bmp_design[1] = open_bitmap(folder, "layer_1.png");
        designerview.bmp_design[2] = open_bitmap(folder, "layer_2.png");
        designerview.bmp_design[3] = open_bitmap(folder, "layer_3.png");
        designerview.bmp_design[4] = open_bitmap(folder, "layer_4.png");
        designerview.bmp_design[5] = open_bitmap(folder, "layer_5.png");
        designerview.bmp_design[6] = open_bitmap(folder, "layer_6.png");
        designerview.bmp_design[7] = open_bitmap(folder, "layer_7.png");
        designerview.bmp_design[8] = open_bitmap(folder, "layer_8.png");
        designerview.bmp_design[9] = open_bitmap(folder, "layer_9.png");
        designerview.bmp_design[10] = open_bitmap(folder, "layer_10.png");
        designerview.bmp_design[11] = open_bitmap(folder, "layer_11.png");
        designerview.bmp_design[12] = open_bitmap(folder, "layer_12.png");
        designerview.bmp_design[13] = open_bitmap(folder, "layer_13.png");

        //configs
        designerview.pub_configs = restore_design_data(new ArrayList<>(Arrays.asList(designs.get(position))));

        //render_configs
        String[] data = designs.get(position);
        designerview.design_name = data[0];
        designerview.pub_render_configs = new String[]{data[3], data[4], data[5]};

    }

    private void delete_design(int position){
        String design_folder_id = designs.get(position)[1];

        delete_bitmap(design_folder_id, "layer_0.png");
        delete_bitmap(design_folder_id, "layer_1.png");
        delete_bitmap(design_folder_id, "layer_2.png");
        delete_bitmap(design_folder_id, "layer_3.png");
        delete_bitmap(design_folder_id, "layer_4.png");
        delete_bitmap(design_folder_id, "layer_5.png");
        delete_bitmap(design_folder_id, "layer_6.png");
        delete_bitmap(design_folder_id, "layer_7.png");
        delete_bitmap(design_folder_id, "layer_8.png");
        delete_bitmap(design_folder_id, "layer_9.png");
        delete_bitmap(design_folder_id, "layer_10.png");
        delete_bitmap(design_folder_id, "layer_11.png");
        delete_bitmap(design_folder_id, "layer_12.png");
        delete_bitmap(design_folder_id, "layer_13.png");
        delete_bitmap(design_folder_id, "design_preview.png");
        delete_folder(design_folder_id);
        designs.remove(position);

        //Toast.makeText(getApplicationContext(), "Diseño eliminado.", Toast.LENGTH_SHORT).show();
    }



    private void async_saving(final String design_name, final String design_desc){
        //async
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                iv_des_loading.setVisibility(View.VISIBLE);

            }
            @Override
            protected Void doInBackground(Void... voids) {
                save_design(design_name, design_desc);
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                //save to data.
                SaveManager smg = new SaveManager(getContext(), SaveManager.SAVETYPE_DESIGNDATA);
                smg.saveArray("designsets", designs);
                iv_des_loading.setVisibility(View.GONE);
                cv_notdesigns.setVisibility(View.GONE);
                rv_designs.setAdapter(desadapter);
                new CustomToast(getContext(), "Diseño Guardado.", Toast.LENGTH_SHORT, Gravity.BOTTOM);
            }
        }.execute();
    }
    private void async_deleting(final int position){
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                iv_des_loading.setVisibility(View.VISIBLE);
            }
            @Override
            protected Void doInBackground(Void... voids) {
                delete_design(position);
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                //save to data.
                SaveManager smg = new SaveManager(getContext(), 3);
                smg.saveArray("designsets", designs);
                desadapter.notifyDataSetChanged();
                iv_des_loading.setVisibility(View.GONE);
                new CustomToast(getContext(), "Diseño eliminado.", Toast.LENGTH_SHORT, Gravity.BOTTOM);
                check_design_list();
            }
        }.execute();
    }
    private void save_design(String design_name, String design_desc){
        //generate design id
        //add unique token to design id.
        String design_id = design_name.replace(" ", "_") + "_" + Utils.randomString(14);
        //Log.d("dreamdata", "design_id: " + design_id);

        //create design folder
        create_dir(design_id);
        //save layers to folder
        save_layerdesign(0, design_id, "layer_0.png");
        save_layerdesign(1, design_id, "layer_1.png");
        save_layerdesign(2, design_id, "layer_2.png");
        save_layerdesign(3, design_id, "layer_3.png");
        save_layerdesign(4, design_id, "layer_4.png");
        save_layerdesign(5, design_id, "layer_5.png");
        save_layerdesign(6, design_id, "layer_6.png");
        save_layerdesign(7, design_id, "layer_7.png");
        save_layerdesign(8, design_id, "layer_8.png");
        save_layerdesign(9, design_id, "layer_9.png");
        save_layerdesign(10, design_id, "layer_10.png");
        save_layerdesign(11, design_id, "layer_11.png");
        save_layerdesign(12, design_id, "layer_12.png");
        save_layerdesign(13, design_id, "layer_13.png");

        //create design preview
        save_bitmap(create_design_preview(), design_id, "design_preview.png");

        //add design name and configs
        designs.add(create_design_data(design_name, design_id, design_desc));
    }

    private Bitmap create_design_preview(){
        return Bitmap.createScaledBitmap(designerview.pub_iv_render, 320, 427, false);
    }
    private String[] create_design_data(String design_name, String design_id, String design_desc){
        ArrayList<String> conf = new ArrayList<>();
        conf.add(design_name);
        conf.add(design_id);
        conf.add(design_desc);
        conf.add(designerview.pub_render_configs[0]); //cb1
        conf.add(designerview.pub_render_configs[1]); //cb2
        conf.add(designerview.pub_render_configs[2]); //hvalue
        conf.add(Utils.getSaveDate()); //date
        conf.add("0"); //reservado para flag de favorito.
        conf.add("0"); //reservado extra.

        //configs
        if (designerview.pub_configs != null){
            for (int i=0; i < designerview.pub_configs.length; i++){
                conf.addAll(Arrays.asList(designerview.pub_configs[i]));
            }
        }

        String[] res = new String[conf.size()];
        res = conf.toArray(res);
        return res;
    }
    private String[][] restore_design_data(ArrayList<String> design_data){
        String[][] res_configs = new String[designerview.pub_configs.length][designerview.pub_configs[0].length];
        if (design_data.size() < 99){
            //formato antiguo.
            design_data.remove(0); //des_name
            design_data.remove(0); //des_id
        } else if (design_data.size() < 103) {
            //formato viejo.
            design_data.remove(0); //des_name
            design_data.remove(0); //des_id
            design_data.remove(0); //des_desc
            design_data.remove(0); //cb1
            design_data.remove(0); //cb2
            design_data.remove(0); //hvalue
        } else {
            //formato nuevo.
            design_data.remove(0); //des_name
            design_data.remove(0); //des_id
            design_data.remove(0); //des_desc
            design_data.remove(0); //cb1
            design_data.remove(0); //cb2
            design_data.remove(0); //hvalue
            design_data.remove(0); //des_date
            design_data.remove(0); //fav flag.
            design_data.remove(0); //extra
        }

        for (int i=0; i < designerview.pub_configs.length; i++){
            for (int j=0; j < designerview.pub_configs[0].length; j++){
                res_configs[i][j] = design_data.get(i * designerview.pub_configs[0].length + j);
            }
        }
        return res_configs;
    }

    private void save_layerdesign(int id, String foldername, String filename){
        save_bitmap(designerview.bmp_design[id], foldername, filename);

    }

    private void delete_bitmap(String foldername, String filename){
        File file = new File(getActivity().getFilesDir() + "/" + foldername, filename);
        if (file.exists()){
            file.delete();
        }
    }
    private void delete_folder(String foldername){
        File dir = new File(getActivity().getFilesDir().toString(), foldername);
        if (dir.exists()){
            dir.delete();
        }
    }

    private void save_bitmap(Bitmap bmp, String foldername ,String filename){
        File file = new File(getActivity().getFilesDir() + "/" + foldername, filename);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (FileNotFoundException e) {
            bmp.recycle();
            e.printStackTrace();
        }
    }
    private Bitmap open_bitmap(String foldername, String filename){
        File file = new File(getActivity().getFilesDir() + "/" + foldername, filename);
        try {
            return BitmapFactory.decodeStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean create_dir(String dirname){
        File newdir = new File(getActivity().getFilesDir().toString(), dirname);
        if (!newdir.exists()){
            return newdir.mkdirs();
        }
        return false;
    }


    private void pop_open_design_info(View view, final int position){
        try {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.layout_pop_design_item, (ViewGroup) view.findViewById(R.id.pop_design_item));

            int width = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 260, getResources().getDisplayMetrics()));
            int height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 380, getResources().getDisplayMetrics()));

            pop_design_item = new PopupWindow(layout, width, height, true);
            pop_design_item.setContentView(layout);
            pop_design_item.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            pop_design_item.showAtLocation(view, Gravity.CENTER, 0, 0);

            ImageView iv_design_preview = layout.findViewById(R.id.iv_design_preview);
            final ImageView iv_fav = layout.findViewById(R.id.iv_fav);
            LinearLayout bt_des_accept = layout.findViewById(R.id.bt_des_accept);
            LinearLayout bt_des_delete = layout.findViewById(R.id.bt_des_delete);
            LinearLayout bt_des_close = layout.findViewById(R.id.bt_des_close);
            LinearLayout bt_des_edit = layout.findViewById(R.id.bt_des_edit);
            TextView tv_design_desname = layout.findViewById(R.id.tv_design_desname);
            TextView tv_design_desc = layout.findViewById(R.id.tv_design_desc);
            TextView tv_date = layout.findViewById(R.id.tv_date);

            //load item preview.
            iv_design_preview.setImageBitmap(open_bitmap(designs.get(position)[1], "design_preview.png"));
            tv_design_desname.setText(designs.get(position)[0]);

            //calcular datos.
            if (designs.get(position).length > 2){
                tv_design_desc.setText(designs.get(position)[2]);
            } else {
                tv_design_desc.setText("No hay descripción del diseño.");
            }
            if (designs.get(position).length > 6){
                if (designs.get(position)[7].equals("1")){
                    iv_fav.setColorFilter(Color.parseColor("#ffab40"));
                } else {
                    iv_fav.setColorFilter(Color.parseColor("#ffffff"));
                }
                tv_date.setText(designs.get(position)[6]);
            }

            bt_des_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //aca comprobar si el designerview esta seleccionado, de ser asi usar el open_design() sino el external.
                    if (Act_Main.navigation.getSelectedItemId() == R.id.navigation_live2d){
                        open_design(position);
                        getActivity().setResult(Activity.RESULT_OK);

                    } else {
                        open_design_external(position);
                        getActivity().setResult(144);
                    }
                    pop_design_item.dismiss();
                    getActivity().finish();
                }
            });

            bt_des_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //delete_design(position);
                    check_alert_desing_delete(position);
                }
            });

            bt_des_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pop_design_item.dismiss();
                    pop_edit_design(view, position);
                }
            });

            bt_des_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pop_design_item.dismiss();
                }
            });

            iv_fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String[] data = designs.get(position);

                    if (data.length > 102){
                        if (data[7].equals("1")){
                            data[7] = "0";
                            iv_fav.setColorFilter(Color.parseColor("#ffffff"));

                        } else {
                            data[7] = "1";
                            iv_fav.setColorFilter(Color.parseColor("#ffab40"));

                        }
                    } else {
                        new CustomToast(getContext(), "Formato antiguo no soportado", Toast.LENGTH_SHORT, Gravity.BOTTOM);
                    }
                    designs.set(position, data);
                    SaveManager smg = new SaveManager(getContext(), SaveManager.SAVETYPE_DESIGNDATA);
                    smg.saveArray("designsets", designs);
                    desadapter.notifyDataSetChanged();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pop_edit_design(View view, final int position){
        //editar el diseño.
        try {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.layout_pop_design_edit_item, (ViewGroup) view.findViewById(R.id.pop_design_edit_item));

            int width = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 280, getResources().getDisplayMetrics()));
            int height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 360, getResources().getDisplayMetrics()));

            pop_design_edit_item = new PopupWindow(layout, width, height, true);
            pop_design_edit_item.setContentView(layout);
            pop_design_edit_item.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            pop_design_edit_item.showAtLocation(getView(), Gravity.CENTER, 0, 0);

            LinearLayout bt_edit_accept = layout.findViewById(R.id.bt_edit_accept);
            LinearLayout bt_edit_close = layout.findViewById(R.id.bt_edit_close);
            final EditText et_edit_name = layout.findViewById(R.id.et_edit_name);
            final EditText et_edit_desc = layout.findViewById(R.id.et_edit_desc);

            //load item data on start
            final String[] data = designs.get(position);


            et_edit_name.setText(data[0]);
            et_edit_desc.setText(data[2]);

            bt_edit_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //guardar datos aca.
                    data[0] = et_edit_name.getText().toString();
                    data[2] = et_edit_desc.getText().toString();
                    data[6] = Utils.getSaveDate();
                    //data[7] = "0"; //set favorite to off.

                    save_edit_data(position, data);
                    pop_design_edit_item.dismiss();
                }
            });

            bt_edit_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pop_design_edit_item.dismiss();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void save_edit_data(int position, String[] data){
        designs.set(position, data);
        SaveManager smg = new SaveManager(getContext(), SaveManager.SAVETYPE_DESIGNDATA);
        smg.saveArray("designsets", designs);
        new CustomToast(getContext(), "Diseño guardado.", Toast.LENGTH_SHORT, Gravity.CENTER);
        desadapter.notifyDataSetChanged();
    }

    private void check_alert_desing_delete(final int position){
        DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case DialogInterface.BUTTON_POSITIVE:
                        async_deleting(position);
                        pop_design_item.dismiss();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:

                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Estas seguro de eliminar este diseño?")
                .setPositiveButton("Si", dialog)
                .setNegativeButton("No", dialog)
                .setIcon(R.drawable.ic_moon)
                .show();

    }

}
