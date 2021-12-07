package com.software.ddk.dreamgen.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.Toast;

import com.software.ddk.dreamgen.BuildConfig;
import com.software.ddk.dreamgen.utils.CustomToast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Utils {

    public static int CONST_BASEITEMSLIMIT = 25000;

    public static boolean check_storage_permission(Activity activity){
        int permissionCheck = ContextCompat.checkSelfPermission(activity, WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_DENIED){
            //si no tiene permiso pedirlo.
            ActivityCompat.requestPermissions(activity, new String[]{WRITE_EXTERNAL_STORAGE}, 231);
            return false;
        } else {
            //si ya tiene permiso, devolver true.
            return permissionCheck == PackageManager.PERMISSION_GRANTED;
        }
    }

    public static String[] open_file(Activity activity, String filename) throws IOException {
        //Byte[] txt = {0};
        FileInputStream fis;
        String line;
        fis = activity.openFileInput(filename);
        BufferedReader in = new BufferedReader(new InputStreamReader(fis));
        List<String> lines = new ArrayList<>();

        while ((line = in.readLine()) != null) {
            lines.add(line);
        }
        in.close();
        return lines.toArray(new String[lines.size()]);
    }

    public static String[] readLines(Resources resources, String filename) throws IOException {
        InputStream is = resources.getAssets().open(filename);
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = in.readLine()) != null) {
            lines.add(line);
        }
        in.close();
        return lines.toArray(new String[lines.size()]);
    }

    public static String getCapturedFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "DreamGen");
        if (!file.exists()) {
            Boolean result = file.mkdirs();
        }
        return (file.getAbsolutePath() + "/" + "Dream-" + System.currentTimeMillis() + ".png");
    }

    public static String getW2XFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "DreamGen");
        if (!file.exists()) {
            Boolean result = file.mkdirs();
        }
        return (file.getAbsolutePath() + "/" + "W2X_Dream-" + System.currentTimeMillis() + ".png");
    }

    public static String getLayerFilename(String nombre) {
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/DreamGen/", "Layers");
        if (!file.exists()){
            Boolean result = file.mkdirs();
        }
        return (file.getAbsolutePath() + "/" + "Layer-" + nombre + "-" + System.currentTimeMillis() + ".png");
    }



    public static void save_bitmap(Context context, ImageView imageview, String filename){
        //picture saver
        if (imageview.getDrawable() != null) {
            Bitmap bmp = ((BitmapDrawable) imageview.getDrawable()).getBitmap();
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(filename);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            } catch (Exception e) {
                bmp.recycle();
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                        new CustomToast(context, "Imagen Guardada.", Toast.LENGTH_SHORT, Gravity.CENTER);

                        //aca se actualizaria la galeria
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                            File f = new File(filename);
                            Uri contentUri = Uri.fromFile(f);
                            mediaScanIntent.setData(contentUri);
                            context.sendBroadcast(mediaScanIntent);
                        } else {
                            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse(filename)));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            new CustomToast(context, "Error de Guardado.", Toast.LENGTH_SHORT, Gravity.BOTTOM);
        }
    }

    public static void save_bitmap_toPath(Context context, Bitmap imagen, String path){
        //picture saver
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            imagen.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            imagen.recycle();
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                    new CustomToast(context, "Capa Guardada", Toast.LENGTH_SHORT, Gravity.BOTTOM);
                    //aca se actualizaria la galeria
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        File f = new File(path);
                        Uri contentUri = Uri.fromFile(f);
                        mediaScanIntent.setData(contentUri);
                        context.sendBroadcast(mediaScanIntent);
                    } else {
                        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse(path)));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void save_bitmap_w2x_toPath(Context context, Bitmap imagen, String path){
        //picture saver
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            imagen.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            imagen.recycle();
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                    new CustomToast(context, "Imagen Guardada.", Toast.LENGTH_SHORT, Gravity.CENTER);
                    //aca se actualizaria la galeria
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        File f = new File(path);
                        Uri contentUri = Uri.fromFile(f);
                        mediaScanIntent.setData(contentUri);
                        context.sendBroadcast(mediaScanIntent);
                    } else {
                        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse(path)));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void share_bitmap(Context context, ImageView iview){
        if (iview.getDrawable() != null) {
            iview.buildDrawingCache();
            Bitmap bitmap = iview.getDrawingCache();
            try {
                File file = new File(context.getApplicationContext().getCacheDir(), bitmap + ".png");
                FileOutputStream fOut = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.flush();
                fOut.close();
                file.setReadable(true, false);

                Uri uri = FileProvider.getUriForFile(context.getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", file);
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/*");
                share.putExtra(Intent.EXTRA_STREAM, uri);
                share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                share.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                context.startActivity(Intent.createChooser(share,"Compartir Via..."));

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            new CustomToast(context, "Imposible Compartir.", Toast.LENGTH_SHORT, Gravity.BOTTOM);
        }
    }

    public static ArrayList<String> array2list(String[] array){
        ArrayList<String> arr = new ArrayList<>();
        for (int i=0; i<CONST_BASEITEMSLIMIT; i++){
            if (array.length > i){
                arr.add(array[i]);
            } else {
                arr.add("Unknown");
            }
        }

        return arr;
    }

    public static String[] list2array(ArrayList<String> array){
        String[] res = new String[array.size()];

        for (int i=0; i<array.size(); i++){
            res[i] = array.get(i);
        }
        return res;
    }


    public static String[] create_array_by_list(ArrayList<String[]> array) {
        ArrayList<String> internal_array = new ArrayList<>();
        for (int i=0;i<array.size(); i++){
            if (!array.get(i)[0].equals("nulo")){
                internal_array.add(array.get(i)[1]);
            }
        }
        String[] res = new String[internal_array.size()];
        for (int i=0; i<internal_array.size(); i++){
            res[i] = internal_array.get(i);
        }
        return res;
    }


    public static void create_file_by_array(Context context, String filename, String[] txt) throws IOException {
        FileOutputStream fos = null;
        fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
        PrintWriter pw = new PrintWriter(fos,true);
        for(String s : txt) pw.println(s);
        pw.close();
        fos.close();
        //Log.d("output:", "file created: " + filename);
    }

    public static Bitmap item_capa(Bitmap base, Bitmap capa){
        //todo actualmente se asume que las imagenes del array son del mismo tamaño.
        int w = base.getWidth();
        int h = base.getHeight();
        Bitmap result = Bitmap.createBitmap(w, h, base.getConfig());
        //capas
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(base, 0, 0, null);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawBitmap(capa, 0, 0, paint);
        return result;
    }

    public static Bitmap open_bitmap(Context context, String foldername, String filename){
        File file = new File(context.getFilesDir() + "/" + foldername, filename);
        try {
            return BitmapFactory.decodeStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getSaveDate(){
        SimpleDateFormat dfecha = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat dhora = new SimpleDateFormat("HH:mm");

        return "Creado el " + dfecha.format(new Date()) + " a las " + dhora.format(new Date());
    }

    public static String randomString(int len){
        String AB = "0123456789abcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(len);
        for(int i=0; i<len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }

}
