package com.software.ddk.dreamgen.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.software.ddk.dreamgen.R;
import com.software.ddk.dreamgen.fragment.designsview;
import com.software.ddk.dreamgen.fragment.setsview;

public class Act_Mis extends AppCompatActivity  {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private Fragment fr_sets = new setsview();
    private Fragment fr_designs = new designsview();

    private TextView tv_msets, tv_mdesigns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        tv_msets = findViewById(R.id.tv_msets);
        tv_mdesigns = findViewById(R.id.tv_mdesigns);

        //seleccion inicial
        fragmentManager.beginTransaction().replace(R.id.fr_mis_container, fr_sets, "1").commit();

        tv_msets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cambiar a fragment de sets
                tv_msets.setBackgroundResource(R.color.popbarcolor);
                tv_mdesigns.setBackgroundResource(R.color.popbarcolor_disabled);
                if (fragmentManager.findFragmentByTag("1") != null){
                    fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("1")).commit();
                } else {
                    fragmentManager.beginTransaction().add(R.id.fr_mis_container, fr_sets, "1").commit();
                }
                if(fragmentManager.findFragmentByTag("2") != null){
                    fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("2")).commit();
                }
            }
        });

        tv_mdesigns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cambiar a fragment de designs
                tv_msets.setBackgroundResource(R.color.popbarcolor_disabled);
                tv_mdesigns.setBackgroundResource(R.color.popbarcolor);
                if (fragmentManager.findFragmentByTag("2") != null){
                    fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("2")).commit();
                } else {
                    fragmentManager.beginTransaction().add(R.id.fr_mis_container, fr_designs, "2").commit();
                }
                if(fragmentManager.findFragmentByTag("1") != null){
                    fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("1")).commit();
                }
            }
        });


    }

}
