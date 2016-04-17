package com.wencai.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

public class DBUtil {
	public static String dbName="data.db";
	public static String getDbName() {
		return dbName;
	}

	public static void setDbName(String dbName) {
		DBUtil.dbName = dbName;
	}

	private Context context;
	private final int BUFFER_SIZE = 1024;
	// 保存的数据库文件名 与 DBHelper 统一
	//private static final String DB_NAME ="data.db"; 
	private final String PACKAGE_NAME;// 包名
	private final String DB_PATH; // 在手机里存放数据库的位置

	public DBUtil(Context context) {
		this.context = context;
		PACKAGE_NAME = PackageUtil.getAppInfo(context).getAsString(
				"packageName");
		DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath()
				+ "/" + PACKAGE_NAME + "/databases/"; // 在手机里存放数据库的位置
	}

	public void openDatabase(String dbName) {
		//设置全局的数据库名字
		if(dbName==null){
			//默认的数据库名字，不动
			dbName=this.dbName;
		}else{
			DBUtil.setDbName(dbName);
		}
		File dir = new File(DB_PATH);
		if (!dir.exists()) {
			dir.mkdir();
		}
		File db_file = new File(DB_PATH, dbName);
		if (!db_file.exists()) {
			AssetManager am = context.getAssets();
			try {
				InputStream is = am.open(dbName);
				FileOutputStream fos = new FileOutputStream(db_file);
				byte[] buffer = new byte[BUFFER_SIZE];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // 欲导入的数据库
		}
	}
}
