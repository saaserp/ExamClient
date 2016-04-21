package com.nengfei.regist;


import com.nengfei.app.R;
import com.nengfei.util.CallBack;
import com.nengfei.util.Tools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class RegistActivity extends Activity {
	EditText edtUid;
	EditText edtCode;
	EditText edtPsw;
	Button btnCode;
	Button btnRegist;
	public static String  code="";

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
				}else
				{
					if(!edtCode.getText().toString().equals(RegistActivity.code)){
						edtCode.setError("请输入正确的验证码");
						return ;
					}
					if(edtPsw.getText().toString().equals("")==true){
						edtPsw.setError("请输入密码");
						return ;
					}
					
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

				new CodeTask(RegistActivity.this, edtUid.getText().toString(), btnCode).execute();
			}
		});
	}
	public void toBack(View v){
		this.setResult(RESULT_CANCELED	, new Intent().putExtra("uid", edtUid.getText()));  
		this.finish();
	}
}
