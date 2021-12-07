package com.software.ddk.dreamgen.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.software.ddk.dreamgen.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RVFavItems extends RecyclerView.Adapter<RVFavItems.ViewHolder> {

    private LayoutInflater mInflater;
    private ArrayList<String[]> items;

    public RVFavItems(Context context, ArrayList<String[]> items) {
        this.mInflater = LayoutInflater.from(context);
        this.items = items;
    }

    @Override
    public RVFavItems.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.items_listview, parent, false);
        return new RVFavItems.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RVFavItems.ViewHolder holder, int position) {
        download_data(holder, position);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void download_data(RVFavItems.ViewHolder holder, int position){

        if (!items.get(position)[0].equals("nulo")){
            if (items.get(position)[1].equals("0")){
                holder.icon.setImageResource(R.drawable.avatar_slot_none);
            } else {
                Picasso.get()
                        .load(items.get(position)[3])
                        .noFade()
                        .into(holder.item_icon);

                Picasso.get()
                        .load(items.get(position)[0])
                        .noFade()
                        .into(holder.icon);
                holder.item_id.setText(items.get(position)[1]);
                holder.item_name.setText("\u3010" + items.get(position)[4] + "\u3011");
                //"\u3010" + result_name + "\u3011"
                //items.get(position)[4]
            }

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView item_id;
        TextView item_name;
        ImageView item_icon;
        ImageView icon;

        ViewHolder(View itemView) {
            super(itemView);
            item_id = itemView.findViewById(R.id.item_id);
            item_name = itemView.findViewById(R.id.item_name);
            item_icon = itemView.findViewById(R.id.item_icon);
            icon = itemView.findViewById(R.id.icon);
        }
    }

}