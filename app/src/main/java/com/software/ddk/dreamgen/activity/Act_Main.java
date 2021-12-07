package com.software.ddk.dreamgen.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.software.ddk.dreamgen.R;
import com.software.ddk.dreamgen.fragment.aboutview;
import com.software.ddk.dreamgen.fragment.avatarview;
import com.software.ddk.dreamgen.fragment.designerview;
import com.software.ddk.dreamgen.utils.CustomToast;
import com.software.ddk.dreamgen.utils.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Act_Main extends AppCompatActivity {

    public static String[] item_rarity, item_names, list_outfits, list_outfits_jp, list_back_acc, list_back_acc_jp, list_bkg, list_bkg_jp, list_bodyacc, list_bodyacc_jp, list_face_acc, list_face_acc_jp, list_front, list_front_jp, list_hairs, list_hairs_jp, list_hats, list_hats_jp;
    public static BottomNavigationView navigation;
    public static Boolean UPDATE_DESIGN_VIEW = false;

    //designer view stuff.
    //todo esto es horrible pero android sucks...
    public static Boolean LOAD_DESIGN_DATA = false;
    public static String[] SELECTED_DESIGN_DATA = new String[]{};
    public static String[][] pub_configs = new String[5][16];

    private TextView tv_hora;
    private BroadcastReceiver broadcastReceiver;
    private final SimpleDateFormat sdfWatchTime = new SimpleDateFormat("HH:mm");
    //private final SimpleDateFormat sdfWatchTime = new SimpleDateFormat("", Locale.US);

    private Boolean DOBLE_SALIR = false;
    private Handler mHandler = new Handler();

    //guardar cada fragment en un objeto globalmente.
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private Fragment fragment_1 = new avatarview();
    private Fragment fragment_2 = new designerview();
    private Fragment fragment_3 = new aboutview();
    private String selected_f_index = "1";

    @Override
    protected void onStart() {
        super.onStart();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0)
                    tv_hora.setText(sdfWatchTime.format(new Date()));
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        tv_hora = findViewById(R.id.tv_hora);
        navigation = findViewById(R.id.navigation);

        //seteamos la hora actual porque el broadcast podria no estar actuando aun
        tv_hora.setText(sdfWatchTime.format(new Date()));

        //cargamos los datos de uso global en la aplicacion.
        //todo si es posible hacer esto en un thread aparte a futuro.
        read_savedata();

        //carga del fragment inicial.
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment_1, "1").commit();
        selected_f_index = "1";

        //listeners del bottom navigation
        BottomNavigationView.OnNavigationItemSelectedListener navlistener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.navigation_home:
                        change_fragment(selected_f_index,"1");
                        selected_f_index = "1";
                        break;
                    case R.id.navigation_live2d:
                        change_fragment(selected_f_index, "2");
                        selected_f_index = "2";
                        break;
                    case R.id.navigation_acercade:
                        change_fragment(selected_f_index, "3");
                        selected_f_index = "3";
                        break;
                }

                return true;
            }
        };
        navigation.setOnNavigationItemSelectedListener(navlistener);

    }

    private Fragment get_selected_fragment(String id) {
        switch (id){
            case "1":
                return fragment_1;
            case "2":
                return fragment_2;
            case "3":
                return fragment_3;
        }
        return fragment_1;
    }


    private void change_fragment(String f_previous, String f_new){
        if(fragmentManager.findFragmentByTag(f_new) != null) {
            //si el fragment existe mostrarlo.
            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(f_new)).commit();
        } else {
            //si el fragment no existe crearlo.
            fragmentManager.beginTransaction().add(R.id.fragment_container, get_selected_fragment(f_new), f_new).commit();
        }
        if(fragmentManager.findFragmentByTag(f_previous) != null && !f_previous.equals(f_new)){
            //quitar el fragment anterior.
            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(f_previous)).commit();
        }
    }

    private void read_savedata() {
        //indice
        item_names = checkread_filearray("indice.txt");

        //indice de rareza
        item_rarity = checkread_filearray("items_sort.txt");

        //back_acc y jp
        list_back_acc = checkread_filearray("back_acc.txt");
        list_back_acc_jp = checkread_filearray("back_acc_jp.txt");

        //bkg y jp
        list_bkg = checkread_filearray("bkg.txt");
        list_bkg_jp = checkread_filearray("bkg_jp.txt");

        //body_acc y jp
        list_bodyacc = checkread_filearray("body_acc.txt");
        list_bodyacc_jp = checkread_filearray("body_acc_jp.txt");

        //face_acc y jp
        list_face_acc = checkread_filearray("face_acc.txt");
        list_face_acc_jp = checkread_filearray("face_acc_jp.txt");

        //front y jp
        list_front = checkread_filearray("front.txt");
        list_front_jp = checkread_filearray("front_jp.txt");

        //hairs y jp
        list_hairs = checkread_filearray("hairs.txt");
        list_hairs_jp = checkread_filearray("hairs_jp.txt");

        //hats y jp
        list_hats = checkread_filearray("hats.txt");
        list_hats_jp = checkread_filearray("hats_jp.txt");

        //outfits y jp
        list_outfits = checkread_filearray("list.txt");
        list_outfits_jp = checkread_filearray("list_jp.txt");

    }

    private String[] checkread_filearray(String filename){
        String[] lines = {};
        try {
            if (file_exist(filename)){
                //open file.
                // || filename.equals("indice.txt")
                if (bigger_than_assets(filename)){
                    //si es mas extenso que assets, o es el indice cargar.
                    lines = open_file(filename);
                } else {
                    lines = readLines(filename);
                }
                //check if filename is items_sort
                if (filename.equals("items_sort.txt")){
                    //add lines to baseitemslimit
                    if (lines.length < Utils.CONST_BASEITEMSLIMIT){
                        lines = arrayAddLines(lines, Utils.CONST_BASEITEMSLIMIT);
                    }
                }

            } else {
                //read assets.
                lines = readLines(filename);
            }} catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    private String[] arrayAddLines(String[] array, int maxlines){
        ArrayList<String> linesArray = new ArrayList<>();
        for (int i=0; i < maxlines; i++){
            if (i < array.length){
                linesArray.add(array[i]);
            } else {
                linesArray.add("0");
            }
        }
        return linesArray.toArray(new String[linesArray.size()]);
    }

    public Boolean bigger_than_assets(String filename) throws IOException {
        String[] assetsfile = readLines(filename);
        String[] opfile = open_file(filename);
        Log.d("bta:","filename: " + filename + " assets: " + assetsfile.length + " opfile: " + opfile.length);
        return assetsfile.length <= opfile.length;
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

    public String[] readLines(String filename) throws IOException {
        InputStream is = getAssets().open(filename);
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = in.readLine()) != null) {
            lines.add(line);
        }
        in.close();
        return lines.toArray(new String[lines.size()]);
    }

    public Boolean file_exist(String filename){
        File file = getBaseContext().getFileStreamPath(filename);
        return file.exists();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (broadcastReceiver != null)
            unregisterReceiver(broadcastReceiver);
    }

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            DOBLE_SALIR = false;
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) { mHandler.removeCallbacks(mRunnable); }
    }
    @Override
    public void onBackPressed(){
        if (DOBLE_SALIR) {
            super.onBackPressed();
            return;
        }
        this.DOBLE_SALIR = true;
        //Toast.makeText(this, "Vuelve a presionar para salir", Toast.LENGTH_SHORT).show();
        new CustomToast(this, "Vuelve a presionar para salir", Toast.LENGTH_SHORT, Gravity.BOTTOM);
        mHandler.postDelayed(mRunnable,2000);
    }

}
