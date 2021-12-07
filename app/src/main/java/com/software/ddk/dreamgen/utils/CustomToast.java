package com.software.ddk.dreamgen.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.software.ddk.dreamgen.R;

public class CustomToast {
    private Toast toast;

    public CustomToast(Context context, String text, int duration, int gravity) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View layout = inflater.inflate(R.layout.layout_customtoast, (ViewGroup) ((Activity) context).findViewById(R.id.toast_root));

        TextView toast_text = layout.findViewById(R.id.toast_text);
        toast_text.setText(text);

        toast = new Toast(context);
        toast.setGravity(gravity, 0, 10);
        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();
    }
}
