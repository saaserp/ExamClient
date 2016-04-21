package com.nengfei.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {

	public static void write(Context context, String name,
			String key, String value) {
		SharedPreferences setting = context.getSharedPreferences(name,
				Context.MODE_APPEND);
		SharedPreferences.Editor ed = setting.edit();
		ed.putString(key, value);
		ed.commit();
	}

	public static String read(Context context, String name,
			String key, String defvalue) {
		SharedPreferences setting = context.getSharedPreferences(name,
				Context.MODE_APPEND);
		return setting.getString(key, defvalue);
	}
	
	public static void write(Context context, String name,
			String key, int value) {
		SharedPreferences setting = context.getSharedPreferences(name,
				Context.MODE_APPEND);
		SharedPreferences.Editor ed = setting.edit();
		ed.putInt(key, value);
		ed.commit();
	}

	public static int read(Context context, String name,
			String key, int defvalue) {
		SharedPreferences setting = context.getSharedPreferences(name,
				Context.MODE_APPEND);
		return setting.getInt(key, defvalue);
	}
}
