package com.software.ddk.dreamgen.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.Toast;

import com.software.ddk.dreamgen.R;
import com.software.ddk.dreamgen.fragment.designsview;
import com.software.ddk.dreamgen.utils.SaveManager;
import com.software.ddk.dreamgen.fragment.designerview;
import com.software.ddk.dreamgen.utils.CustomToast;
import com.software.ddk.dreamgen.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class Act_Design extends AppCompatActivity {

    //LinearLayout bt_adddesign;
    private FloatingActionButton fab_adddesign;
    private PopupWindow pop_save_design;
    private ImageView iv_des_loading;

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private Fragment fr_designlist = new designsview();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        fab_adddesign = findViewById(R.id.fab_adddesign);
        iv_des_loading = findViewById(R.id.iv_des_loading);

        //mostrar fragment de la lista de diseños.
        fragmentManager.beginTransaction().replace(R.id.fr_designs, fr_designlist, "1").commit();

    }
}
