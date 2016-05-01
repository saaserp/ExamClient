package com.nengfei.backup;

import com.nengfei.util.CallBack;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.RadioGroup;

public class SleepTask extends AsyncTask<Void,Void,Boolean>{
	Context context;
	RadioGroup group;
	CallBack cb;
	public SleepTask(Context context,RadioGroup  group,CallBack cb){
		this.context=context;
		this.group=group;
		this.cb=cb;
		
	}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		this.group.setEnabled(false);
	}
	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		this.group.setEnabled(true );
		cb.done(true);
	}
	

	
	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO Auto-generated method stub
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	

}
