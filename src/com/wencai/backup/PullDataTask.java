package com.wencai.backup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wencai.model.DataSyncService;
import com.wencai.net.MySocketClient;
import com.wencai.parse.JSONParser;
import com.wencai.util.CallBack;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class PullDataTask extends AsyncTask<Void,Void,Boolean> {
	Context context;
	 
	List<Map<String,String>> list1;
	List<Map<String,String>>list2;
	ProgressDialog pd;
	CallBack cb;
	public PullDataTask(Context context,CallBack cb){
		this.context=context;
		list1=new ArrayList<Map<String,String>>();
		list2=new ArrayList<Map<String,String>>();
		pd=new ProgressDialog(context);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setCancelable(false);
		pd.setMessage("正在同步数据");
		this.cb=cb;
	}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		pd.show();
		 
	}
	List<Map<String,String>> translate(List<Map<String,Object>>l1 ){
		List<Map<String,String >>list = new ArrayList<Map<String,String>>();
		for(Map<String,Object> m:l1){
			Map<String,String> map=new HashMap<String,String>();
			
			for(String key:m.keySet()){
				map.put(key, String.valueOf(m.get(key)));
				 
				
			}
			list.add(map);
		}
		return list;
	}
	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO Auto-generated method stub
		//获取所有的题目
		DataSyncService ds=new DataSyncService(context);
		List<Map<String,Object>> l1=ds.getAllFromQuestion();
		List<Map<String,Object>>l2=ds.getAllFromExamResult();
	
		List<Map<String,String >>listQuestion;
		List<Map<String,String>>listExam;
		listQuestion=translate(l1);
		listExam=translate(l2);

		 String result2=MySocketClient.getInstance().send("PullInfo",listExam);
			String result1=MySocketClient.getInstance().send("PullInfo",listQuestion);
			if(result1==null||result2==null)
			{
				return false;
			}
		 list1=new JSONParser(result1).parse();
		 list2=new JSONParser(result2).parse();
		if(list1==null||list1.size()==0){
			return false;
			
		}
		if(list2==null||list2.size()==0){
			return false;
		}
		
		 
		return ds.removeAllRecode();
	}
	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		
		if(result==true){
			// Toast.makeText(context, "数据同步成功", Toast.LENGTH_LONG).show();
			 
		}else{
		 
			 Toast.makeText(context, "数据同步失败,请求超时", Toast.LENGTH_LONG).show();
			
		}
		 cb.done(result);
		pd.dismiss();
		
	}

	 

}
