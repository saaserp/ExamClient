package com.nengfei.regist;


import com.nengfei.app.R;
import com.nengfei.login.LoginActivity;
import com.nengfei.util.CallBack;
import com.nengfei.util.Tools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegistActivity extends Activity {
	EditText edtUid;
	EditText edtCode;
	EditText edtPsw;
	Button btnCode;
	Button btnRegist;
	public static String  code="";

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
		//初始化短信验证码API
		cn.smssdk.SMSSDK.initSDK(RegistActivity.this, "135b03085e1cc", "9c02390c989f9139e2a47b21141f349a");

		/**
		 * 注册按钮点击事件
		 */
		btnRegist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(edtCode.getText().toString().equals("")==true){
					edtCode.setError("请输入验证码");

					return ;
				}
				if(!Tools.isMobileNO(edtUid.getText().toString())){
					edtUid.setError("手机号码无效");
					return ;
				}
				

					if(edtPsw.getText().toString().equals("")==true){
						edtPsw.setError("请输入密码");

						return ;
					}
					
					cn.smssdk.SMSSDK.submitVerificationCode("86",edtUid.getText().toString(), edtCode.getText().toString());

				


			}

		});


		/**
		 * 获取验证码按钮点击事件
		 */
		btnCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!Tools.isMobileNO(edtUid.getText().toString())){
					edtUid.setError("手机号码无效");
					return ;
				}
				handSleep.obtainMessage().sendToTarget();
				//获取短信
				cn.smssdk.SMSSDK.getVerificationCode("86",edtUid.getText().toString());



			}
		});




		eh=new EventHandler(){

			@Override
			public void afterEvent(int event, int result, Object data) {
					Message msg = new Message();  
	                msg.arg1 = event;  
	                msg.arg2 = result;  
	                msg.obj = data;  
	                handler.sendMessage(msg);  
                } 
		}; 
		cn.smssdk.SMSSDK.registerEventHandler(eh); //注册短信回调

	}
	Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			int event = msg.arg1;  
            int result = msg.arg2;  
            Object data = msg.obj;  
           
            if (result == SMSSDK.RESULT_COMPLETE) {  
                // 短信注册成功后，返回MainActivity,然后提示新好友  
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功  
                    
                	new RegistTask(RegistActivity.this,edtUid.getText().toString(),edtPsw.getText().toString(),btnRegist,new CallBack() {

        				@Override
        				public String done(boolean b) {
        					// TODO Auto-generated method stub
        					Intent intent=new Intent();
        					intent.putExtra("uid", edtUid.getText());
        					intent.putExtra("password", edtPsw.getText().toString());
        					setResult(RESULT_OK,intent);
        					RegistActivity.this.finish();
        					return null;
        				}
        			}).execute();
                	
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {  
                    Toast.makeText(getApplicationContext(), "请打开短信查看验证码",  
                            Toast.LENGTH_SHORT).show();  
                }
            }else {  
            	 String res=((Throwable) data).toString();
                	if(res.contains("600")){
        				Toast.makeText(RegistActivity.this, "同一个手机号码一天内只能验证5次，请明天再试", Toast.LENGTH_LONG).show();
        			}else if(res.contains("468")){
        				 
        				Toast.makeText(RegistActivity.this, "验证码不正确", Toast.LENGTH_LONG).show();
        			}else {
        				Toast.makeText(RegistActivity.this, "网络超时（若超过了5次，次日再试）", Toast.LENGTH_LONG).show();
        			}
        			edtCode.setText("");
                    
                }  
            

		};
	};
	public void toBack(View v){
		this.setResult(RESULT_CANCELED	, new Intent().putExtra("uid", edtUid.getText()));  
		this.finish();
	}
	 
	Handler handSleep=new Handler(){
		public void handleMessage(Message msg) {
			new SleepTask(RegistActivity.this, btnCode).execute();
		};
	};
	
	
}
