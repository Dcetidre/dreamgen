package com.software.ddk.dreamgen.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SaveManager {

    //private Context context;
    private int savetype;
    public static final int SAVETYPE_DEFAULTDATA = 1;
    public static final int SAVETYPE_WAIFUDATA = 2;
    public static final int SAVETYPE_DESIGNDATA = 3;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Gson gson = new GsonBuilder().create();
    private String[] w_default_data = {"0"};
    private String[] w_default_waifu_data = {"0", "103", "1308", "0", "749", "749", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "10", "10", "01","10", "0", "0" ,"3", "0", "0", "0", "0", "0", "0", "0", "Default Set","0","0","0","0","0","El conjunto por defecto.","0","por defecto."};
    private String[] w_default_design_data = {"0", "0", "0"};

    public SaveManager(Context context, int savetype){
        //this.context = context;
        this.savetype = savetype;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = prefs.edit();
    }

    public void saveArray(String key, ArrayList<String[]> array){
        String jsonArray = gson.toJson(array);
        editor.remove(key);
        editor.putString(key, jsonArray);
        editor.commit();
    }

    public void appendArray(String key, String[] item){
        ArrayList<String[]> array = getArray(key);
        array.add(item);
        saveArray(key, array);
    }

    public ArrayList<String[]> getArray(String key){
        ArrayList<String[]> array;
        String jArrayString = prefs.getString(key, "NOPREFSAVED");
        if (jArrayString.matches("NOPREFSAVED")) {
            return getDefaultArray();
        } else {
            array = gson.fromJson(jArrayString, new TypeToken<ArrayList<String[]>>(){}.getType());
            return array;
        }
    }

    public ArrayList<String[]> Json2Array(String json){
        ArrayList<String[]> array;
        Type type = new TypeToken<ArrayList<String[]>>(){}.getType();
        if (isJson(json)){
            array = gson.fromJson(json, type);
            return array;
        } else {
            return null;
        }

    }

    public String Array2Json(ArrayList<String[]> array){
        return gson.toJson(array);
    }

    public String getArrayJson(String key){
        String jArrayString = prefs.getString(key, "NOPREFSAVED");
        if (jArrayString.matches("NOPREFSAVED")) {
            return null;
        } else {
            return jArrayString;
        }
    }

    public static boolean isJson(String Json) {
        try {
            new JSONObject(Json);
        } catch (JSONException ex) {
            try {
                new JSONArray(Json);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    private ArrayList<String[]> getDefaultArray() {
        ArrayList<String[]> array = new ArrayList<String[]>();
        if (savetype == 1){
            array.add(w_default_data);
        }
        if (savetype == 2){
            array.add(w_default_waifu_data);
        }
        if (savetype == 3){
            array.add(w_default_design_data);
        }
        return array;
    }

    private String getDefaultJson(){
        return gson.toJson(w_default_data);
    }

}
