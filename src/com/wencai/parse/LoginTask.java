package com.wencai.parse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wencai.login.LoginActivity;
import com.wencai.net.EDcoder;
import com.wencai.net.MySocketClient;
import com.wencai.util.Tools;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class LoginTask extends AsyncTask<Void, Void, Boolean> {

	String uid;
	String password;
	String result = "";
	TaskCallBack taskCallBack;
	List<Map<String,String>>data;
	Context context;
	public LoginTask(Context context,String uid, String password,TaskCallBack taskCallBack) {
		this.uid = uid;
		this.password = password;
		this.taskCallBack=taskCallBack;
		this.context=context;
	}
	Map<String,String>mp;
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
	
		super.onPreExecute();

	}

	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO Auto-generated method stub
		Map<String,String> map=new HashMap<String, String>();
		
		map.put("uid", uid);
		map.put("password",  EDcoder.getMD5Str( password));
		//result=new HttpClientTool("www.malloto.com").send("Login", map);
		String s=MySocketClient.getInstance().send("LoginProcesser",map);
		if(s==null||s.equals("")){
			return false;
		}
		mp=Tools.JArrayToMap(s);
		 
		
		return result==null?false:true;
	}

	@Override
	protected void onPostExecute(Boolean r) {
		// TODO Auto-generated method stub
		 
		if(r){
 		//data=new JSONParser(result).parse();
			if(mp.get("result").equals("true")){
				//Toast.makeText(context, "登录成功", Toast.LENGTH_LONG).show();
				LoginActivity.uid=uid;
				context.getSharedPreferences("user", Activity.MODE_PRIVATE).edit().putString("uid", uid).commit();
				r=true;
			}else{
				r=false;
			}
			
			
		}else{
			//登录失败
			Toast.makeText(context, "网络连接失败", Toast.LENGTH_SHORT).show();
			
		}
		taskCallBack.todo(r,mp);
	}

}
