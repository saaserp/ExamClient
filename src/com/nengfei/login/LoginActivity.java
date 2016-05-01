package com.nengfei.login;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import com.nengfei.app.R;
import com.nengfei.backup.GetDataTask;
import com.nengfei.parse.LoginTask;
import com.nengfei.parse.TaskCallBack;
import com.nengfei.regist.RegistActivity;
import com.nengfei.util.CallBack;
import com.nengfei.util.NetWorkUtil;
import com.nengfei.util.Tools;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener,
OnItemClickListener, OnDismissListener {
	protected static final String TAG = "LoginActivity";
	static int FLAG_REGEST;
	private LinearLayout mLoginLinearLayout; 
	private LinearLayout mUserIdLinearLayout;
	private Animation mTranslate; 
	private Dialog mLoginingDlg; 
	private EditText mIdEditText; 
	private EditText mPwdEditText; 
	private ImageView mMoreUser;
	private Button mLoginButton; 
	private ImageView mLoginMoreUserView; 
	private String mIdString;
	private String mPwdString;
	private ArrayList<User> mUsers; 
	private ListView mUserIdListView; 
	private MyAapter mAdapter; 
	private PopupWindow mPop; 	 

	public static String uid="";

	public static boolean haslogin(){
		 
		if(LoginActivity.uid==null){
			return false;
		}
		 if(LoginActivity.uid.equals("anonymous")){
			 return false;
		 }
		if(LoginActivity.uid.equals("")){
			return false;
		}
		return true;
	}
	 
	public static LoginActivity  m;
	public static void logout(){
		LoginActivity.uid="";
		
		 

	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);

		initView();
		setListener();
		mLoginLinearLayout.startAnimation(mTranslate); 

		
		mUsers = Utils.getUserList(LoginActivity.this);

		if (mUsers.size() > 0) {

			mIdEditText.setText(mUsers.get(0).getId());
			mPwdEditText.setText(mUsers.get(0).getPwd());
		}

		LinearLayout parent = (LinearLayout) getLayoutInflater().inflate(
				R.layout.userifo_listview, null);
		mUserIdListView = (ListView) parent.findViewById(android.R.id.list);
		parent.removeView(mUserIdListView); 
		mUserIdListView.setOnItemClickListener(this); 
		mAdapter = new MyAapter(mUsers);
		mUserIdListView.setAdapter(mAdapter);
		m=this;
	}
	public void regist(View v){
		this.startActivityForResult(new Intent(this,RegistActivity.class),FLAG_REGEST);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==FLAG_REGEST){
			if(resultCode==RESULT_OK){
				mIdEditText.setText(data.getStringExtra("uid"));
			}
		}
	}
	class MyAapter extends ArrayAdapter<User> {

		public MyAapter(ArrayList<User> users) {
			super(LoginActivity.this, 0, users);
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						R.layout.listview_item, null);
			}

			TextView userIdText = (TextView) convertView
					.findViewById(R.id.listview_userid);
			userIdText.setText(getItem(position).getId());

			ImageView deleteUser = (ImageView) convertView
					.findViewById(R.id.login_delete_user);
			deleteUser.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (getItem(position).getId().equals(mIdString)) {

						mIdString = "";
						mPwdString = "";
						mIdEditText.setText(mIdString);
						mPwdEditText.setText(mPwdString);
					}
					mUsers.remove(getItem(position));
					mAdapter.notifyDataSetChanged(); 
				}
			});
			return convertView;
		}

	}

	private void setListener() {
		mIdEditText.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				mIdString = s.toString();
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
			}
		});
		mPwdEditText.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				mPwdString = s.toString();
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
			}
		});
		mLoginButton.setOnClickListener(this);
		mLoginMoreUserView.setOnClickListener(this);
	}

	private void initView() {
		mIdEditText = (EditText) findViewById(R.id.login_edtId);
		mPwdEditText = (EditText) findViewById(R.id.login_edtPwd);
		mMoreUser = (ImageView) findViewById(R.id.login_more_user);
		mLoginButton = (Button) findViewById(R.id.login_btnLogin);
		mLoginMoreUserView = (ImageView) findViewById(R.id.login_more_user);
		mLoginLinearLayout = (LinearLayout) findViewById(R.id.login_linearLayout);
		mUserIdLinearLayout = (LinearLayout) findViewById(R.id.userId_LinearLayout);
		mTranslate = AnimationUtils.loadAnimation(this, R.anim.my_translate); 
		initLoginingDlg();
	}

	public void initPop() {
		int width = mUserIdLinearLayout.getWidth() - 4;
		int height = LayoutParams.WRAP_CONTENT;
		mPop = new PopupWindow(mUserIdListView, width, height, true);
		mPop.setOnDismissListener(this);
		mPop.setBackgroundDrawable(new ColorDrawable(0xffffffff));

	}


	private void initLoginingDlg() {

		mLoginingDlg = new Dialog(this, R.style.loginingDlg);
		mLoginingDlg.setContentView(R.layout.logining_dlg);
		Window window = mLoginingDlg.getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int cxScreen = dm.widthPixels;
		int cyScreen = dm.heightPixels;

		int height = (int) getResources().getDimension(
				R.dimen.loginingdlg_height);
		getResources().getDimension(
				R.dimen.loginingdlg_lr_margin); 
		int topMargin = (int) getResources().getDimension(
				R.dimen.loginingdlg_top_margin); 

		params.y = (-(cyScreen - height) / 2) + topMargin; // -199


		params.width = cxScreen;
		params.height = height;
		mLoginingDlg.setCanceledOnTouchOutside(true); 
	}


	private void showLoginingDlg() {
		if (mLoginingDlg != null)
			mLoginingDlg.show();
	}


	private void closeLoginingDlg() {
		if (mLoginingDlg != null && mLoginingDlg.isShowing())
			mLoginingDlg.dismiss();
	}
	public void toBack(View v){
		setResult(RESULT_CANCELED);
		this.finish();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_btnLogin:
			if(!Tools.isMobileNO(mIdString)){
				mIdEditText.setError("请输入手机号码");
				return ;
			}
			showLoginingDlg(); 
			Log.i(TAG, mIdString + "  " + mPwdString);
			if (mIdString == null || mIdString.equals("")) { 
				Toast.makeText(LoginActivity.this, "账号不为空！", Toast.LENGTH_SHORT)
				.show();
			} else if (mPwdString == null || mPwdString.equals("")) {
				Toast.makeText(LoginActivity.this, "密码不为空！", Toast.LENGTH_SHORT)
				.show();
			} else {
				
				//Toast.makeText(this, NetWorkUtil.getCPUSerial() , Toast.LENGTH_LONG).show();
				new LoginTask(LoginActivity.this,mIdString, mPwdString, new TaskCallBack() {

					@Override
					public Object todo(boolean b,Map<String, String> map) {
						// TODO Auto-generated method stub
						closeLoginingDlg();
						if(b){
							new GetDataTask(LoginActivity.this,new CallBack(){

								@Override
								public String done(boolean b) {
									// TODO Auto-generated method stub
									boolean mIsSave = true;
									try {
										for (User user : mUsers) {
											if (user.getId().equals(mIdString)) {
												mIsSave = false;
												break;
											}
										}
										if (mIsSave) { 
											User user = new User(mIdString, mPwdString);
											mUsers.add(user);
										}

									} catch (Exception e) {
										e.printStackTrace();
									}
									setResult(RESULT_OK,new Intent().putExtra("uid", mIdString));
									LoginActivity.this.finish();
									return null;
								}

							}).execute();


						}else{
							Toast.makeText(LoginActivity.this, "验证不通过", Toast.LENGTH_SHORT).show();
							setResult(RESULT_OK,new Intent().putExtra("uid", "anonymous"));
						}

						return null;
					}


				}).execute();


			}
			break;
		case R.id.login_more_user:  
			if (mPop == null) {
				initPop();
			}
			if (!mPop.isShowing() && mUsers.size() > 0) {

				mMoreUser.setImageResource(R.drawable.login_more_down);  
				mPop.showAsDropDown(mUserIdLinearLayout, 2, 1);  
			}
			break;
		default:
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		mIdEditText.setText(mUsers.get(position).getId());
		mPwdEditText.setText(mUsers.get(position).getPwd());
		mPop.dismiss();
	}


	@Override
	public void onDismiss() {

		mMoreUser.setImageResource(R.drawable.login_more_up);
	}


	@Override
	public void onPause() {
		super.onPause();
		try {
			Utils.saveUserList(LoginActivity.this, mUsers);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
