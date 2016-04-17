package com.wencai.util;

import android.content.ContentValues;
import android.content.Context;

public class PackageUtil {
	public static ContentValues getAppInfo(Context context) {
		ContentValues appInfoValues=new ContentValues();
		try {
			String pkName = context.getPackageName();
			String versionName = context.getPackageManager().getPackageInfo(
					pkName, 0).versionName;
			int versionCode = context.getPackageManager().getPackageInfo(
					pkName, 0).versionCode;
			appInfoValues.put("packageName", pkName);
			appInfoValues.put("versionName", versionName);
			appInfoValues.put("versionCode",versionCode);
			
		} catch (Exception e) {
			appInfoValues.put("packageName", "Get Failed");
			appInfoValues.put("versionName", "Get Failed");
			appInfoValues.put("versionCode","Get Failed");
		}
		return appInfoValues;
	}
}
