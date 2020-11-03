package com.jstechnologies.androidpostman.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

public class PrefManager {

    static SharedPreferences preferences;
    static SharedPreferences.Editor editor;

    private static String KEY_CONNTIMEOUT="contimeout";
    private static String KEY_READTIMEOUT="readtimeout";
    private static String KEY_WRITETIMEOUT="writetimeout";
    private static String KEY_FCMURL="fcmUrl";
    private static String KEY_FCMBACKUP="savefcm";
    private static String KEY_NETWORKBACKUP="savenetwork";
    private static String KEY_DARKMODE="darkmode";
    public static void SetConnTimeout(Preference preference,int timeout)
    {
        preferences=preference.getSharedPreferences();
        editor=preferences.edit();
        editor.putInt(KEY_CONNTIMEOUT,timeout);
        editor.commit();
    }
    public static void SetReadTimeout(Preference preference,int timeout)
    {
        preferences=preference.getSharedPreferences();
        editor=preferences.edit();
        editor.putInt(KEY_READTIMEOUT,timeout);
        editor.commit();
    }
    public static void SetWriteTimeout(Preference preference,int timeout)
    {
        preferences=preference.getSharedPreferences();
        editor=preferences.edit();
        editor.putInt(KEY_WRITETIMEOUT,timeout);
        editor.commit();
    }
    public static void SetFCMurl(Preference preference,String url)
    {
        preferences=preference.getSharedPreferences();
        editor=preferences.edit();
        editor.putString(KEY_FCMURL,url);
        editor.commit();
    }
    public static void Setnewtworkbackupenabled(Preference preference,boolean enabled)
    {
        preferences=preference.getSharedPreferences();
        editor=preferences.edit();
        editor.putBoolean(KEY_NETWORKBACKUP,enabled);
        editor.commit();
    }
    public static void SetFCMbackupenabled(Preference preference,boolean enabled)
    {
        preferences=preference.getSharedPreferences();
        editor=preferences.edit();
        editor.putBoolean(KEY_FCMBACKUP,enabled);
        editor.commit();
    }
    public static int getConnTimeout(Context context)
    {
        SharedPreferences preference=PreferenceManager.getDefaultSharedPreferences(context);
        return preference.getInt(KEY_CONNTIMEOUT,5);
    }
    public static int getReadTimeout(Context context)
    {
        SharedPreferences preference=PreferenceManager.getDefaultSharedPreferences(context);
        return preference.getInt(KEY_READTIMEOUT,5);
    }
    public static int getWriteTimeout(Context context)
    {
        SharedPreferences preference=PreferenceManager.getDefaultSharedPreferences(context);
        return preference.getInt(KEY_WRITETIMEOUT,5);
    }
    public static String getFCMurl(Context context)
    {
        SharedPreferences preference=PreferenceManager.getDefaultSharedPreferences(context);
        return preference.getString(KEY_FCMURL,"https://fcm.googleapis.com/fcm/send");
    }
    public static boolean getnetworkbackupenabled(Context context)
    {
        SharedPreferences preference=PreferenceManager.getDefaultSharedPreferences(context);
        return preference.getBoolean(KEY_NETWORKBACKUP,true);
    }
    public static boolean getFCMbackupenabled(Context context)
    {
        SharedPreferences preference=PreferenceManager.getDefaultSharedPreferences(context);
        return preference.getBoolean(KEY_FCMBACKUP,true);
    }
    public static boolean getdarkmodeEnabled(Context context)
    {
        SharedPreferences preference=PreferenceManager.getDefaultSharedPreferences(context);
        return preference.getBoolean(KEY_DARKMODE,false);
    }
}
