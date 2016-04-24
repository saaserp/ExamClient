package com.nengfei.parse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nengfei.login.LoginActivity;
import com.nengfei.net.EDcoder;
import com.nengfei.net.MySocketClient;
import com.nengfei.util.NetWorkUtil;
import com.nengfei.util.Tools;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

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
		map.put("mac",NetWorkUtil.getCPUSerial());
		
		String s=MySocketClient.getInstance().send("LoginProcesser",map);
		if(s==null||s.equals("")){
			return false;
		}
		mp=Tools.JArrayToMap(s);
		 
		
		return result==null?false:true;
	}
	
	final   int MSG_SET_ALIAS=1;
	public   TagAliasCallback setAliasCallback= new TagAliasCallback() {
		
		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			// TODO Auto-generated method stub
			String logs ;
	        switch (code) {
	        case 0:
	            logs = "Set tag and alias success";
	            Log.i("jpush", logs);
	            
	            // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
	            break;
	        case 6002:
	            logs = "Failed to set alias and tags due to timeout. Try again after 5s.";
	            Log.i("jpush", logs);
	            // 延迟 60 秒来调用 Handler 设置别名
	            mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 5);
	            break;
	        default:
	            logs = "Failed with errorCode = " + code;
	            Log.e("jpush", logs);
	        }
	      //  Toast.makeText(context, logs, Toast.LENGTH_LONG).show();
	      
		}
	};
	private       Handler mHandler = new Handler() {
		@Override
		    public void handleMessage(android.os.Message msg) {
		        super.handleMessage(msg);
		        switch (msg.what) {
		            case MSG_SET_ALIAS:
		                Log.d("jpush", "Set alias in handler.");
		                // 调用 JPush 接口来设置别名。
		                JPushInterface.setAliasAndTags(context,
		                                                (String) msg.obj,
		                                                 null,
		                                                 setAliasCallback);
		            break;
		        default:
		            Log.i("jpush", "Unhandled msg - " + msg.what);
		        }
		    }                                       
		};

	@Override
	protected void onPostExecute(Boolean r) {
		// TODO Auto-generated method stub
		 
		if(r){
 		//data=new JSONParser(result).parse();
			if(mp.get("result").equals("true")){
				//Toast.makeText(context, "登录成功", Toast.LENGTH_LONG).show();
				LoginActivity.uid=uid;
				mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, NetWorkUtil.getCPUSerial()));
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
