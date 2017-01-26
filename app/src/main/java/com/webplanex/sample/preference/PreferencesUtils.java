package com.webplanex.sample.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.webplanex.sample.utils.Constant;

import java.util.ArrayList;
import java.util.Map;

public class PreferencesUtils {

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(Constant.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
    }

    public static void setPreferenceString(Context context, String key, String val) {
        SharedPreferences settings = PreferencesUtils.getSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, val);
        editor.apply();
    }

    public static void setPreferenceFloat(Context context, String key, float val) {
        SharedPreferences settings = PreferencesUtils.getSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(key, val);
        editor.apply();
    }

    public static void setPreferenceBoolean(Context context, String key, boolean val) {
        SharedPreferences settings = PreferencesUtils.getSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, val);
        editor.apply();
    }

    public static void setPreferenceInt(Context context, String key, int val) {
        SharedPreferences settings = PreferencesUtils.getSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, val);
        editor.apply();
    }

    public static void setPreferenceLong(Context context, String key, long val) {
        SharedPreferences settings = PreferencesUtils.getSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, val);
        editor.apply();
    }

    public static boolean setPreferenceArray(Context mContext, String key, ArrayList<String> array) {
        SharedPreferences prefs = PreferencesUtils.getSharedPreferences(mContext);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key + "_size", array.size());
        for (int i = 0; i < array.size(); i++)
            editor.putString(key + "_" + i, array.get(i));
        return editor.commit();
    }

    public static void clearPreferenceArray(Context c, String key) {
        SharedPreferences settings = PreferencesUtils.getSharedPreferences(c);

        if (getPreferenceArray(c, key) != null && getPreferenceArray(c, key).size() > 0) {
            for (String element : getPreferenceArray(c, key)) {
                if (findPrefrenceKey(c, element) != null && settings.contains(findPrefrenceKey(c, element))) {
                    SharedPreferences.Editor editor = settings.edit();
                    editor.remove(findPrefrenceKey(c, element));
                    editor.apply();
                }
            }
        }
    }

    public static String findPrefrenceKey(Context con, String value) {
        SharedPreferences settings = PreferencesUtils.getSharedPreferences(con);
        Map<String, ?> editor = settings.getAll();
        for (Map.Entry<String, ?> entry : editor.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null; // not found
    }

    public static void removePreference(Context context, String key) {
        SharedPreferences settings = PreferencesUtils.getSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(key);
        editor.apply();
    }

    public static String getPreferenceString(Context context, String key) {
        SharedPreferences prefs = PreferencesUtils.getSharedPreferences(context);
        return prefs.getString(key, "");
    }

    public static ArrayList<String> getPreferenceArray(Context mContext, String key) {
        SharedPreferences prefs = PreferencesUtils.getSharedPreferences(mContext);
        int size = prefs.getInt(key + "_size", 0);
        ArrayList<String> array = new ArrayList<String>(size);
        for (int i = 0; i < size; i++)
            array.add(prefs.getString(key + "_" + i, null));
        return array;
    }

    public static long getPreferenceLong(Context context, String key) {
        SharedPreferences prefs = PreferencesUtils.getSharedPreferences(context);
        return prefs.getLong(key, 0);
    }

    public static boolean getPreferenceBoolean(Context context, String key) {
        SharedPreferences prefs = PreferencesUtils.getSharedPreferences(context);
        return prefs.getBoolean(key, false);
    }

    public static int getPreferenceInt(Context context, String key) {
        SharedPreferences prefs = PreferencesUtils.getSharedPreferences(context);
        return prefs.getInt(key, 0);

    }

    public static void removeAllPreference(Context context) {
        SharedPreferences settings = PreferencesUtils.getSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.apply();
    }

    public static String getAllPreference(Context context) {
        SharedPreferences settings = PreferencesUtils.getSharedPreferences(context);
        Map<String, ?> editor = settings.getAll();
        String text = "";

        try {
            for (Map.Entry<String, ?> entry : editor.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                // do stuff
                text += "\t" + key + " = " + value + "\t";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }
}
