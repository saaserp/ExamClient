package com.nengfei.backup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nengfei.app.R;
import com.nengfei.net.MySocketClient;
import com.nengfei.parse.HttpClientTool;
import com.nengfei.parse.JSONParser;
import com.nengfei.util.CallBack;
import com.nengfei.web.WebActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

public class GetSystemInfoTask extends AsyncTask<Void, Void, Boolean> {
	Context context;
	View advs[];
	String url;
	public static boolean isfirst=true;
	List<Map<String, String>> list2;

	public GetSystemInfoTask(Context context, View advs[], CallBack cb) {
		this.context = context;
		this.advs = advs;
	}

	private static PackageInfo getPackageInfo(Context context) {
		PackageInfo pi = null;

		try {
			PackageManager pm = context.getPackageManager();
			pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);

			return pi;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pi;
	}

	boolean needUpdate = false;
	List<Map<String, String>> advls;

	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO Auto-generated method stub
		String version = "";

		version = getPackageInfo(context).versionName;
		Map<String, String> mp = new HashMap<String, String>();
		mp.put("version", version);
	//	String result = MySocketClient.getInstance().send("GetSystemInfo", mp);
		String result = HttpClientTool.getInstance().send("GetSystemInfo", mp);
		
		if (result == null||result.equals("")) {
			return false;
		}
		List<Map<String, String>> list = new JSONParser(result).parse();
		if(list==null||list.size()==0){
			return false;
		}
		Map<String, String> map1 = list.get(0);
		String ver = map1.get("version");

		list2 = new JSONParser(ver).parse();

		needUpdate = list2.get(0).get("needUpdate").equals("true") ? true : false;

		String jsona = map1.get("adv");
		advls = new JSONParser(jsona).parse();

		if (advls == null) {
			return false;
		}

		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (result) {
			if(isfirst==true){
				for (int i = 0; i < advls.size(); i++) {
					if (advls.get(i).get("visible").equals("1")) {
						advs[i].setVisibility(View.VISIBLE);
					} else {
						//advs[i].setVisibility(View.GONE);
					}
				}
				//这可以让广告一直在，不消失
				isfirst=false;
			} 

			if (needUpdate == true) {
				url = list2.get(0).get("url");
				AlertDialog dialog = new AlertDialog.Builder(context).setTitle("系统提示").setIcon(R.drawable.smalllogo)
						.setNegativeButton("取消", null).setPositiveButton("确定", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// 处理确认按钮的点击事件
								
								Uri uri = Uri.parse(url);
								Intent downloadIntent = new Intent(Intent.ACTION_VIEW, uri);
								context.startActivity(downloadIntent);

							}
						}).setMessage("发现新版本，是否立即下载安装？").create();
				dialog.show();

			}

		} else {

			Toast.makeText(context, "网络连接失败	", Toast.LENGTH_SHORT).show();
		}

	}

}
