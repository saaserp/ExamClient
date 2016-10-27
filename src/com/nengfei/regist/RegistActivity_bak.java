package com.nengfei.regist;


import com.nengfei.app.R;
import com.nengfei.util.CallBack;
import com.nengfei.util.Tools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.smssdk.EventHandler;

public class RegistActivity_bak extends Activity {
	EditText edtUid;
	EditText edtCode;
	EditText edtPsw;
	Button btnCode;
	Button btnRegist;
	public static String  code="";
	boolean isyanzhengmaok=false;//验证码是否ok
	EventHandler eh;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_regist);
		edtUid=(EditText) this.findViewById(R.id.regist_edtId);
		edtCode=(EditText)this.findViewById(R.id.edit_regist_code);
		btnCode=(Button) this.findViewById(R.id.btn_get_code);
		edtPsw=(EditText)this.findViewById(R.id.rege_edtPwd);
		btnRegist=(Button)this.findViewById(R.id.rege_btn);
		btnRegist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(edtCode.getText().toString().equals("")==true){
					edtCode.setError("请输入验证码");

					return ;
				}else{


					//					if(!edtCode.getText().toString().equals(RegistActivity.code)){
					//						edtCode.setError("请输入正确的验证码");
					//						
					//						return ;
					//					}


					if(edtPsw.getText().toString().equals("")==true){
						edtPsw.setError("请输入密码");

						return ;
					}
					
					cn.smssdk.SMSSDK.submitVerificationCode("86",edtUid.getText().toString(), edtCode.getText().toString());

				}


			}

		});



		btnCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!Tools.isMobileNO(edtUid.getText().toString())){
					edtUid.setError("手机号码无效");
					return ;
				}

				//	new CodeTask(RegistActivity.this, edtUid.getText().toString(), btnCode).execute();
				
				
			 	 
				//获取短信
				cn.smssdk.SMSSDK.getVerificationCode("86",edtUid.getText().toString());



			}
		});

		//初始化短信验证码API
		cn.smssdk.SMSSDK.initSDK(RegistActivity_bak.this, "135b03085e1cc", "9c02390c989f9139e2a47b21141f349a");


		eh=new EventHandler(){

			@Override
			public void afterEvent(int event, int result, Object data) {
				//		            	Toast.makeText(RegistActivity.this, result+"", Toast.LENGTH_SHORT).show();
				Log.i("msmcode", result+"");
				if (result == cn.smssdk.SMSSDK.RESULT_COMPLETE) {
					//回调完成
					if (event == cn.smssdk.SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
						//提交验证码成功

						Log.i("mycode",  "提交验证码成功");
						handRegist.obtainMessage().sendToTarget();

					}else if (event == cn.smssdk.SMSSDK.EVENT_GET_VERIFICATION_CODE){
						//手机收到验证码成功		                	
						//Toast.makeText(RegistActivity.this, "产生验证码成功", Toast.LENGTH_SHORT).show();
						 handSleep.obtainMessage().sendToTarget();
						
						//Toast.makeText(RegistActivity.this, "获取验证码中,请等待3到5秒钟", Toast.LENGTH_LONG).show();
				 
					}else if (event ==cn.smssdk.SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
						//返回支持发送验证码的国家列表
						
					} 
				}else{ 
					
					((Throwable)data).printStackTrace(); 
					 
					Message msg= handToast.obtainMessage();
					msg.obj=((Throwable)data).toString();
					msg.sendToTarget();
				}



			} 
		}; 
		cn.smssdk.SMSSDK.registerEventHandler(eh); //注册短信回调

	}
	public void toBack(View v){
		this.setResult(RESULT_CANCELED	, new Intent().putExtra("uid", edtUid.getText()));  
		this.finish();
	}
	Handler handRegist=new Handler(){
		
		public void handleMessage(Message msg) {

			new RegistTask(RegistActivity_bak.this,edtUid.getText().toString(),edtPsw.getText().toString(),btnRegist,new CallBack() {

				@Override
				public String done(boolean b) {
					// TODO Auto-generated method stub
					Intent intent=new Intent();
					intent.putExtra("uid", edtUid.getText());
					intent.putExtra("password", edtPsw.getText().toString());
					setResult(RESULT_OK,intent);
					RegistActivity_bak.this.finish();
					return null;
				}
			}).execute();
		};
	};
	Handler handSleep=new Handler(){
		public void handleMessage(Message msg) {
			new SleepTask(RegistActivity_bak.this, btnCode).execute();
		};
	};
	Handler handToast=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(((String)msg.obj).contains("600")){
			Toast.makeText(RegistActivity_bak.this, "同一个手机号码一天内只能验证5次，请明天再试", Toast.LENGTH_LONG).show();
			}else if(((String)msg.obj).contains("468")){
				Log.i("mycode", (String)msg.obj);
				Toast.makeText(RegistActivity_bak.this, "验证码不正确", Toast.LENGTH_LONG).show();
			}else {
				Toast.makeText(RegistActivity_bak.this, "网络超时", Toast.LENGTH_LONG).show();
			}
			edtCode.setText("");
			
		};
	};
	Handler handUI=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				btnCode.setEnabled(false);
				break;
			case 1:
				btnCode.setEnabled(true);
				break;
			default:
				break;
			}

		};
	};
}
