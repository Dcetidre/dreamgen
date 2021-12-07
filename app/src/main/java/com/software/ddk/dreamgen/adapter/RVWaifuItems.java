package com.software.ddk.dreamgen.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.software.ddk.dreamgen.R;
import com.software.ddk.dreamgen.fragment.avatarview;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RVWaifuItems extends RecyclerView.Adapter<RVWaifuItems.ViewHolder> {

    private LayoutInflater mInflater;
    private ArrayList<String[]> items = new ArrayList<>();

    public RVWaifuItems(Context context, ArrayList<String[]> items) {
        this.mInflater = LayoutInflater.from(context);
        this.items = items;
    }
    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.items_listwaifuview, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String[] w_data = items.get(position);
        String outfit = w_data[0];
        String bkg = w_data[1];
        String hair = w_data[2];
        String hair2 = w_data[3];
        String face_eye1 = w_data[4];
        String face_eye2 = w_data[5];
        String hairacc1 = w_data[6];
        String hairacc2 = w_data[7];
        String bodyacc1 = w_data[8];
        String bodyacc2 = w_data[9];
        String faceacc1 = w_data[10];
        String faceacc2 = w_data[11];
        String backacc1 = w_data[12];
        String backacc2 = w_data[13];
        String front = w_data[14];
        String face_pup1 = w_data[15];
        String face_pup2 = w_data[16];
        String face_blush = w_data[17];
        String face_skin = w_data[18];
        String face_tam = w_data[19];
        String face_cej = w_data[20];
        String face_exp = "10";

        String front2, hairacc3, bodyacc3, faceacc3, backacc3;
        String desc;
        String server = "0";

        if (w_data.length > 33){
            front2 = w_data[33];
            hairacc3 = w_data[34];
            bodyacc3 = w_data[35];
            faceacc3 = w_data[36];
            backacc3 = w_data[37];
        }
        if (w_data.length > 38){
            //check server
            if (w_data[39].equals("0")){
                holder.item_server.setImageResource(R.drawable.usa_server);
            } else if (w_data[39].equals("1")){
                holder.item_server.setImageResource(R.drawable.jp_server);
            }
            server = w_data[39];
        }
        if (w_data.length > 39){
            //date
            if (w_data[40].equals("-date-")){
                holder.item_date.setText("Sin información de fecha.");
            } else {
                holder.item_date.setText("" + w_data[40]);
            }
        }
        if (w_data.length > 41){
            if (w_data[41].equals("0")){
                holder.item_fav.setVisibility(View.GONE);
            } else {
                holder.item_fav.setVisibility(View.VISIBLE);
            }
        }
        String url = "http://"+ avatarview.a_server[Integer.valueOf(server)] +"/img/php/all_param2.php?&id1=" + generate_ids(2,front) + ","+ generate_ids(1, outfit) + "," + generate_ids(1, bodyacc1)+ ","+generate_ids(1, bodyacc2)+","+generate_ids(1, hairacc1)+","+generate_ids(1, hairacc2)+","+generate_ids(1, faceacc1)+","+generate_ids(1, faceacc2)+","+generate_ids(1, backacc1)+","+generate_ids(1, backacc2)+","+generate_ids(1, bkg)+","+generate_ids(1, hair)+","+generate_ids(1, face_eye1)+","+generate_ids(1, face_eye2)+","+generate_ids(1, hair2)+",x0x,x0x,x0x,x0x,x0x,"+generate_ids(1, face_pup1)+","+generate_ids(1, face_pup2)+",x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,x0x,"+generate_ids(4, face_cej)+"&id2="+generate_ids(3,face_skin)+"&id3="+generate_ids(3,face_tam)+"&id4="+generate_ids(3,face_exp)+"&id5="+generate_ids(3,face_blush)+"&id6=0x1&id7=2x1";
        Picasso.get().load(url).into(holder.waifuicon);
        holder.waifuname.setText("" + w_data[32]);

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


    // total number of cells
    @Override
    public int getItemCount() {
        return items.size();
    }
    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView waifuicon;
        ImageView item_server;
        ImageView item_fav;
        TextView waifuname;
        TextView item_date;

        ViewHolder(View itemView) {
            super(itemView);
            waifuicon = itemView.findViewById(R.id.icon);
            waifuname = itemView.findViewById(R.id.item_name);
            item_server = itemView.findViewById(R.id.item_server);
            item_date = itemView.findViewById(R.id.item_date);
            item_fav = itemView.findViewById(R.id.item_fav);
        }
    }

}