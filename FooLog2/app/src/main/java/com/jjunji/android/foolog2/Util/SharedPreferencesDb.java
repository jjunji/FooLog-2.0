package com.jjunji.android.foolog2.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jhjun on 2017-09-14.
 */

public class SharedPreferencesDb {

    private static SharedPreferences storage = null;

    private static void setSharedPreferences(Context context){
        if(storage == null){
            storage = context.getSharedPreferences("storage", Activity.MODE_PRIVATE);
        }
    }

    private static void setString(Context context, String key, String value){
        setSharedPreferences(context);
        SharedPreferences.Editor editor = storage.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private static String getString(Context context, String key){
        setSharedPreferences(context);
        return storage.getString(key, null);
    }

    // 저장소에서 Id를 가져온다.
    public static String getId(Context context, String id){

        return getString(context, id);
    }

    // 저장소에 Id를 넣는다.
    public static void setId(Context context, String key, String value){
        setString(context, key, value);
    }

    // 저장소에서 pwd를 가져온다.
    public static String getPwd(Context context, String pwd){

        return getString(context, pwd);
    }

    // 저장소에 pwd를 넣는다.
    public static void setPwd(Context context, String key, String value){

        setString(context, key, value);
    }

    public static String getToken(Context context, String key){

        return getString(context, key);
    }

    public static String sendToken(Context context, String key){

        return "Token " + getString(context, key);
    }

    public static void setToken(Context context, String key, String value){
        setString(context, key, value);
    }

    public static String getNickName(Context context, String key){

        return getString(context, key);
    }

    public static void setNickName(Context context, String key, String value){
        setString(context, key, value);
    }

    public static void DbClear(){
        SharedPreferences.Editor editor = storage.edit();
        editor.clear();  // editor.clear()는 storage 에 들어있는 모든 정보를 기기에서 지움
        editor.commit();
    }

}
