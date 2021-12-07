package com.software.ddk.dreamgen.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.software.ddk.dreamgen.R;

public class CustomAdapter extends BaseAdapter {
    //private Context context;}
    private int array[];
    private String sarray[];
    private LayoutInflater inflter;
    private int type;

    public CustomAdapter(Context applicationContext, int[] array, String[] sarray, int type) {
        //this.context = applicationContext;
        this.array = array;
        this.sarray = sarray;
        this.type = type;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return array.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        switch (type){
            case 1:
                //banderas de servers.
                view = inflter.inflate(R.layout.custom_flag_spinner, null);
                ImageView icon = view.findViewById(R.id.iv_flag);
                TextView text = view.findViewById(R.id.tv_server);
                icon.setImageResource(array[i]);
                text.setText(sarray[i]);
                return view;
            case 2:
                //apariencias.
                view = inflter.inflate(R.layout.custom_eyetype_spinner, null);
                ImageView iv_eye_type = view.findViewById(R.id.iv_eye_type);
                TextView tv_eye_type = view.findViewById(R.id.tv_eye_type);
                iv_eye_type.setImageResource(array[i]);
                tv_eye_type.setText(sarray[i]);
                return view;

            case 3:
                //normal
                view = inflter.inflate(R.layout.custom_simple_spinner, null);
                TextView tv_text = view.findViewById(R.id.tv_text);
                tv_text.setText(sarray[i]);
                return view;

            case 4:
                //base spinner para menus, texto pequeño.
                view = inflter.inflate(R.layout.custom_base_spinner, null);
                TextView tv_text2 = view.findViewById(R.id.tv_text);
                tv_text2.setText(sarray[i]);
                return view;

        }
        return null;
    }
}