package com.wencai.regist;

import java.util.HashMap;
import java.util.Map;

import com.wencai.net.EDcoder;
import com.wencai.net.MySocketClient;
import com.wencai.util.CallBack;
import com.wencai.util.Tools;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.Toast;

public class RegistTask extends AsyncTask<Void,Void,Boolean>{
	String phone;
	String password;
	Map<String,String>mp;
	Button btn;
	Context context;
	ProgressDialog pd;
	CallBack d;
	@Override
	protected void onPreExecute() {
		pd.show();
		btn.setEnabled(false);
	};
	RegistTask(Context context,String phone,String password,Button btn,CallBack done){
		this.d=done;
		this.context=context;
		this.phone=phone;
		this.btn=btn;
		this.password=password;
		pd=new ProgressDialog(context);
		pd.setMessage("正在注册...");
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setCancelable(false);
		
	}
	 
	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO Auto-generated method stub
		Map<String,String> map=new HashMap<String,String>();
		map.put("phone", phone);
		map.put("password", EDcoder.getMD5Str(password));
		String r=MySocketClient.getInstance().send("Regist",map);
		if(r==null|r.equals("")){
			
			return false; 
		}
		mp=Tools.JArrayToMap(r);
		
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(result==true){
			if(mp.get("result").equals("true")){
				Toast.makeText(context, "注册成功", Toast.LENGTH_SHORT).show();
				
				
			}else{
				Toast.makeText(context, "该号码已被注册", Toast.LENGTH_LONG).show();
			}
		}else
		{
			Toast.makeText(context, "网络连接失败", Toast.LENGTH_SHORT).show();
		}
		pd.dismiss();
		d.done(result);
		btn.setEnabled(true);
	}
}
