package yin.deng.dyutils.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashSet;

/**
 * 保存用户信息
 */
public class SharedPreferenceUtil {
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String Tag="SharedPreferenceUtil";

    public SharedPreferenceUtil(Context context,String nameKey) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(nameKey, Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveString(String tag, String s) {
        editor.putString(tag, s);
        editor.commit();
        showSuccessLog(tag, s);
    }

    public void saveBoolean(String tag, boolean s) {
        editor.putBoolean(tag, s);
        editor.commit();
        showSuccessLog(tag, s);
    }

    public void saveInt(String tag, int s) {
        editor.putInt(tag, s);
        editor.commit();
        showSuccessLog(tag, s);
    }

    public void saveLong(String tag, long s) {
        editor.putLong(tag, s);
        editor.commit();
        showSuccessLog(tag, s);
    }

    public void saveSetMap(String tag, HashSet<String> map) {
        editor.putStringSet(tag, map);
        editor.commit();
        showSuccessLog(tag, "mapSaveSuccess!");
    }

    public void clearString(String tag) {
        editor.putString(tag, null);
    }

    public void clearLong(String tag) {
        editor.putLong(tag, -1);
    }

    public void clearAll() {
        editor.clear();
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, -1);
    }

    public String getStr(String key) {
        return sharedPreferences.getString(key, "");
    }

    public long getLong(String key) {
        return sharedPreferences.getLong(key, -1L);
    }

    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public HashSet<String> getSetMap(String key) {
        return (HashSet<String>) sharedPreferences.getStringSet(key, null);
    }


    private void showSuccessLog(String tag, String s) {
        Log.i(Tag,"save------" + "tag:" + tag + ",msg:" + s);
    }

    private void showSuccessLog(String tag, boolean s) {
        Log.i(Tag,"save------" + "tag:" + tag + ",msg:" + s);
    }

    private void showSuccessLog(String tag, int s) {
        Log.i(Tag,"save------" + "tag:" + tag + ",msg:" + s);
    }

    private void showSuccessLog(String tag, long s) {
        Log.i(Tag,"save------" + "tag:" + tag + ",msg:" + s);
    }

}
