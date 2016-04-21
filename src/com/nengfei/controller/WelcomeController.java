package com.nengfei.controller;

import com.nengfei.util.DBUtil;

import android.content.Context;

public class WelcomeController {
	public void init(Context context,String dbName) {
		
		new DBUtil(context).openDatabase(dbName);// 导入数据库
		
		//暂时去掉
//		// 可以认为没有获取到外网IP就是没有连上网,并且只检查一次暂不考虑太多
//		NetWorkUtil.getNetIpFromWeb(context);
//		//设置截图的路径
//		new FileUtil((Activity)context).setPic_path();
	}
}
