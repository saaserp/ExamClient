package com.nengfei.regist;

import java.util.HashMap;
import java.util.Map;

import com.nengfei.net.MySocketClient;
import com.nengfei.parse.HttpClientTool;
import com.nengfei.util.Tools;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.Toast;

public class CodeTask extends AsyncTask<Void,Void,Boolean>{
	Context context;
	String phone;
	Button btn;
	CodeTask(Context context,String phone,Button btn){
		this.context=context;
		this.phone=phone;
		this.btn=btn;
	}
	Map<String,String> m;
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		btn.setEnabled(false);
		new SleepTask(context, btn).execute();
		Toast.makeText(context, "获取验证码中,请等待3到5秒钟", Toast.LENGTH_LONG).show();
		
	}
	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO Auto-generated method stub
		Map<String,String> map=new HashMap<String,String>();
		map.put("phone", phone);
		String s= HttpClientTool.getInstance().send("GetCode",map);
		if(s==null||s.equals("")){
			return false;
		}
		
		 m=Tools.JArrayToMap(s);
		 
		return true;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		if(result){
			if(m.get("result").equals("true")){
				
				 RegistActivity.code=m.get("code");
				 
				
			}else{
				 Toast.makeText(context, "验证码获取失败", Toast.LENGTH_SHORT).show();
				 
				
			}
			
		}
		
		 
	}
	 
	 

}
