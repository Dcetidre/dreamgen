package com.software.ddk.dreamgen.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.software.ddk.dreamgen.R;
import com.software.ddk.dreamgen.activity.Act_Main;
import com.software.ddk.dreamgen.utils.MapValues;
import com.software.ddk.dreamgen.utils.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class RVItems extends RecyclerView.Adapter<RVItems.ViewHolder> {

    private LayoutInflater mInflater;
    private ArrayList<String[]> items;
    private float[] pixhsv = new float[3];
    private Context context;

    public RVItems(Context context, ArrayList<String[]> items) {
        this.mInflater = LayoutInflater.from(context);
        this.items = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.items_gridview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        download_data(holder, position);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void download_data(final ViewHolder holder, final int position){
        if (!items.get(position)[0].equals("nulo")){
            if (items.get(position)[1].equals("0")){
                holder.icon.setImageResource(R.drawable.avatar_slot_none);
            } else {
                Picasso.get()
                        .load(items.get(position)[0])
                        //.noFade()
                        .placeholder(R.drawable.progress_animation2)
                        .into(holder.icon, new Callback() {
                            @Override
                            public void onSuccess() {
                                get_pixel_value(holder, position);
                                check_if_new(holder, position);
                            }

                            @Override
                            public void onError(Exception e) {
                                //retry again please.
                            }
                        });
            }

        }
    }

    private void check_if_new(ViewHolder holder, int position){
        //checkear si el item es uno nuevo, si lo es, modificar el bitmap para agregar un puntito.
        //si el dato de item, indica un 1 en nuevo, obtener bitmap.
        if (items.size() > position){
            if (items.get(position).length > 3){
                if (items.get(position)[3].equals("1")){
                    //Log.d("dream_scan", "NUEVO ITEM: " + position);
                    Bitmap bitmap = ((BitmapDrawable) holder.icon.getDrawable()).getBitmap();
                    Bitmap new_item = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.new_item), 128, 128, false);
                    Bitmap bmp_result = Utils.item_capa(bitmap, new_item);
                    holder.icon.setImageBitmap(bmp_result);
                }
            }
        }
    }

    private void get_pixel_value(ViewHolder holder, int position){
        //activar cuando se requiera filtrar items por rareza. el metodo se encarga de obtener los colores de cada pixel
        Point pixel = new Point(26, 18);
        Bitmap bitmap = ((BitmapDrawable) holder.icon.getDrawable()).getBitmap();
        int pix = bitmap.getPixel(pixel.x,pixel.y);
        Color.colorToHSV(pix, pixhsv);
        float hue = pixhsv[0];

        if (items.size() > position && Act_Main.item_rarity.length > Integer.valueOf(items.get(position)[1])){
            int id = Integer.valueOf(items.get(position)[1]);
            //Log.d("dreampixel","pos: " + Act_Main.item_rarity[Integer.valueOf(items.get(position)[1])]);
            if (Act_Main.item_rarity[id].equals("0")){
                //Log.d("dreampixel","hue: " + hue + " - pos: " + items.get(position)[1]);
                if (hue >= MapValues.MIN_MR_HUE && hue <= MapValues.MAX_MR_HUE){
                    if (Act_Main.item_rarity.length > position){
                        Act_Main.item_rarity[id] = "5";
                        //Log.d("dreampixel","MR item");
                    }
                }
                if (hue >= MapValues.MIN_SR_HUE && hue <= MapValues.MAX_SR_HUE){
                    if (Act_Main.item_rarity.length > position){
                        Act_Main.item_rarity[id] = "4";
                        //Log.d("dreampixel","SR item");

                    }
                }
                if (hue >= MapValues.MIN_RR_HUE && hue <= MapValues.MAX_RR_HUE){
                    if (Act_Main.item_rarity.length > position){
                        Act_Main.item_rarity[id] = "3";
                        //Log.d("dreampixel","RR item");
                    }
                }
                if (hue >= MapValues.MIN_R_HUE && hue <= MapValues.MAX_R_HUE){
                    if (Act_Main.item_rarity.length > position){
                        Act_Main.item_rarity[id] = "2";
                        //Log.d("dreampixel","R item");

                    }
                }
                if (hue >= MapValues.MIN_N_HUE && hue <= MapValues.MAX_N_HUE){
                    if (Act_Main.item_rarity.length > position){
                        Act_Main.item_rarity[id] = "1";
                        //Log.d("dreampixel","N item");
                    }
                }
            }
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView pid;
        ImageView icon;

        ViewHolder(View itemView) {
            super(itemView);
            pid = itemView.findViewById(R.id.pid);
            icon = itemView.findViewById(R.id.icon);
        }
    }

}