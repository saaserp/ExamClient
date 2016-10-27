package com.nengfei.regist;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;

public class SleepTask extends AsyncTask<Object, Object, Object>{
	Button btn;
	Context context;
	SleepTask(Context context,Button btn){
		this.btn=btn;
		this.context=context;
	}
	protected void onPreExecute() {
		btn.setClickable(false);
		btn.setEnabled(false);
	};
	Handler hand=new Handler(){
		public void handleMessage(android.os.Message msg) {
			btn.setText((String)msg.obj);
		};
	};
	@Override
	protected Object doInBackground(Object... params) {
		// TODO Auto-generated method stub
		Message msg;
		for(int i=60;i>=0;i--){
			msg=hand.obtainMessage();
			try {
				Thread.sleep(1000);
				msg.obj=""+i+"s";
				msg.sendToTarget();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
		}
		return null;
	}
	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		btn.setText("获取验证码");
		btn.setClickable(true);
		btn.setEnabled(true);
		
	}



}
