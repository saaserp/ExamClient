package com.wencai.backup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wencai.app.WelcomeActivity;
import com.wencai.login.LoginActivity;
import com.wencai.model.DataSyncService;
import com.wencai.net.MySocketClient;
import com.wencai.parse.JSONParser;
import com.wencai.util.CallBack;
import com.wencai.util.DBHelper;
import com.wencai.util.DBUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class GetDataTask extends AsyncTask<Void, Void, Boolean> {
	List<Map<String, String>> data;
	Context context;
	CallBack cb;
	ProgressDialog pd;
	public GetDataTask(Context context, CallBack cb) {
		data = new ArrayList<Map<String, String>>();
		this.context = context;
		this.cb = cb;
		pd=new ProgressDialog(context);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setCancelable(false);
		//显示正在登录,实际上是正在处理数据
		pd.setMessage("正在更新数据...");
		pd.setMax(100);
		pd.show();
	}
	Handler hand=new Handler(){
		public void handleMessage(android.os.Message msg) {
			pd.setProgress(msg.what);
		};
	};
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		
	}
	int progress=50;
	Message msg;
	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO Auto-generated method stub
		
		Map<String, String> map = new HashMap<String, String>();
		msg=hand.obtainMessage();
		msg.what=10;
		map.put("uid", LoginActivity.uid);
		 map.put("dbName", DBUtil.dbName);
		msg.sendToTarget();
		String res = MySocketClient.getInstance().send("GetInfo", map);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(progress<80){
					
				msg=hand.obtainMessage();
				msg.what=progress;
				msg.sendToTarget();
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					progress++;
				}
			}
		}){}.start();
		
		if (res == null)
			return false;
		 
		data=new JSONParser(res).parse();
		DataSyncService ds = new DataSyncService(context);
		if (data == null || data.size() == 0) {
			
			//抹掉所有数据
			 
			return ds.removeAllRecode();
		}
		 
		boolean bb = false;
		
		 
		List<Map<String, String>> ls;
		for (int i = 0; i < data.size(); i++) {
				progress+=3;
				msg=hand.obtainMessage();
				msg.what=progress;
				msg.sendToTarget();
				if (data.get(i).get("tableName").equals("ExamResult")) {
					ls = new JSONParser(data.get(i).get("list")).parse();
					bb = ds.setAllToExamResult(ls);
				} else if(data.get(i).get("tableName").equals("QuestionBank")) {
					ls = new JSONParser(data.get(i).get("list")).parse();
					bb = ds.setAllToQuestion(ls);
				}else {
					bb=false;
				}
				progress+=5;
				msg=hand.obtainMessage();
				msg.what=progress;
				msg.sendToTarget();
		}
	 
	 
		return bb;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (result) {
			//Toast.makeText(context, "同步成功", Toast.LENGTH_SHORT).show();
			 
			
		} else {
		
			Toast.makeText(context, "同步失败，请与管理员联系", Toast.LENGTH_SHORT).show();
			System.exit(0);
		}
		msg=hand.obtainMessage();
		msg.what=99;
		msg.sendToTarget();
		pd.dismiss();
		cb.done(result);

	}

}
