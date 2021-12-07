package com.software.ddk.dreamgen.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.software.ddk.dreamgen.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class RVDesignItems extends RecyclerView.Adapter<RVDesignItems.ViewHolder> {

    private LayoutInflater mInflater;
    private ArrayList<String[]> items = new ArrayList<>();
    private Context context;


    public RVDesignItems(Context context, ArrayList<String[]> items) {
        this.mInflater = LayoutInflater.from(context);
        this.items = items;
        this.context = context;
    }
    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.items_listdesignview, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.desname.setText(items.get(position)[0]);
        Bitmap bitm = open_bitmap(items.get(position)[1], "design_preview.png");
        if (bitm != null){
            holder.desicon.setImageBitmap(bitm);
        }
        holder.desdate.setText(items.get(position)[6]);
        if (items.get(position)[7].equals("1")){
            holder.desfav.setVisibility(View.VISIBLE);
        } else {
            holder.desfav.setVisibility(View.GONE);
        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return items.size();
    }
    // stores and recycles views as they are scrolled off screen

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView desname;
        TextView desdate;
        ImageView desicon;
        ImageView desfav;

        ViewHolder(View itemView) {
            super(itemView);
            desname = itemView.findViewById(R.id.item_name);
            desicon = itemView.findViewById(R.id.item_icon);
            desdate = itemView.findViewById(R.id.item_date);
            desfav = itemView.findViewById(R.id.item_fav);
        }
    }

    private Bitmap open_bitmap(String foldername, String filename){
        File file = new File(context.getFilesDir() + "/" + foldername, filename);
        try {
            return BitmapFactory.decodeStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
        }
        return null;
    }

}