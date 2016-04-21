package com.nengfei.app;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nengfei.controller.WelcomeController;
import com.nengfei.tiku.CustomDialog;
import com.nengfei.util.DBUtil;
import com.nengfei.util.PackageUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;

public class WelcomeActivity extends InstrumentedActivity {
	private WelcomeController wc = new WelcomeController();
	private Handler mHandler = new Handler();
	private ImageView iv_welcome;
	private RelativeLayout layout;
	public final int LOGIN = 0;
	Button btn_login;
	private int alpha = 255;

	private int b = 0;

	class ShowDialogTask extends AsyncTask<Void, Void, Boolean> {
		CustomDialog.Builder builder;
		List<Map<String, String>> list;
		File[] fileList;
		Context context;

		ShowDialogTask(Context context) {
			list = new ArrayList<Map<String, String>>();
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub

			// 文件名
			String[] dbs = getResources().getStringArray(R.array.db_list);
			for (String s : dbs) {
				wc.init(context, s);
			}

			SharedPreferences sp = getSharedPreferences("tiku", MODE_PRIVATE);
			DBUtil.dbName = sp.getString("tiku", "");

			if (DBUtil.dbName.equals("")) {
				File file = new File("/data" + Environment.getDataDirectory().getAbsolutePath() + "/"
						+ PackageUtil.getAppInfo(context).getAsString("packageName") + "/databases/");
				fileList = file.listFiles();
				return fileList != null;
			} else {
				// 题库已经设好了，或者没有发现题库

				return false;
			}

		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result) {

				for (int i = 0; i < fileList.length; i++) {
					if (fileList[i].isFile()) {
						Map<String, String> map = new HashMap<String, String>();
						if (fileList[i].getName().contains("journal")) {
							continue;
						}
						if (fileList[i].getName().contains("zh")) {

							map.put("key", fileList[i].getName());
							map.put("value", "综合题库（部分）");
						}
						if (fileList[i].getName().contains("data")) {
							map.put("key", fileList[i].getName());
							map.put("value", "测试题库（完整）");
						}

						list.add(map);
					}
				}

				builder = new CustomDialog.Builder(context, list);
				builder.setMessage("请选择一个题库");
				builder.setTitle("温馨提示");

				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (CustomDialog.selectedKey != null || !CustomDialog.selectedKey.equals("")) {
							dialog.dismiss();
							context.getSharedPreferences("tiku", Activity.MODE_PRIVATE).edit()
							.putString("tiku", CustomDialog.selectedKey).commit();
							DBUtil.dbName = CustomDialog.selectedKey;
							thread.start();

						} else {

							Toast.makeText(context, "请选择一个题库", Toast.LENGTH_SHORT).show();

							// wc.init(WelcomeActivity.this,
							// CustomDialog.Builder.selectedKey);

							return;
						}

					}
				});

				builder.create().show();

			} else {
				if (DBUtil.dbName.equals("")) {
					if (fileList == null || fileList.length == 0) {
						Toast.makeText(context, "未发现数据文件，请升级到最新版本", Toast.LENGTH_LONG).show();
						// myThread.run();
						return;

					}
				} else {

					// 已经设置好题库了

					thread.start();
				}
			}
		}
	}

	// 极光推送
	private MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "com.nengfei.app.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";

	// 极光推送
	public static boolean isEmpty(String s) {
		if (null == s)
			return true;
		if (s.length() == 0)
			return true;
		if (s.trim().length() == 0)
			return true;
		return false;
	}

	// 极光推送
	public class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
				String messge = intent.getStringExtra(KEY_MESSAGE);
				String extras = intent.getStringExtra(KEY_EXTRAS);
				StringBuilder showMsg = new StringBuilder();
				showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
				if (!isEmpty(extras)) {
					showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
				}

				Toast.makeText(WelcomeActivity.this, showMsg.toString(), Toast.LENGTH_LONG).show();
			}
		}
	}

	// 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
	private void init() {
		JPushInterface.init(getApplicationContext());
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(mMessageReceiver);
		super.onDestroy();
	}

	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		registerReceiver(mMessageReceiver, filter);
	}

	Thread myThread;

	Thread thread;

	@SuppressLint("HandlerLeak")
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// FullScreen
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		// 极光推送
//		JPushInterface.setDebugMode(true);
//		// 极光推送
//		JPushInterface.init(this);
		// 极光推送
		registerMessageReceiver(); // used for receive msg

		setContentView(R.layout.activity_welcome);
		layout = (RelativeLayout) this.findViewById(R.id.RelativeLayout1);
		int[] src = new int[] { R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.welcome };
		iv_welcome = (ImageView) findViewById(R.id.iv_welcome);
		int i = (int) (Math.random() * src.length);

		layout.setBackgroundResource(src[i]);
		layout.setAlpha(alpha);

		thread = new Thread(new Runnable() {
			public void run() {
				while (b < 2) {
					try {
						if (b == 0) {
							Thread.sleep(500);
							b = 1;
						} else {
							Thread.sleep(100);
						}

						updateApp();

					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		});

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				iv_welcome.setAlpha(alpha);
				iv_welcome.invalidate();
			}
		};
		myThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				new ShowDialogTask(WelcomeActivity.this).execute();
			}
		});
		new ShowDialogTask(this).execute();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub


		super.onResume(); 
		JPushInterface.onResume(getApplicationContext());
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub



		super.onPause();
		JPushInterface.onPause(this);

	}

	public void updateApp() {
		alpha -= 11;
		// 避免出现白屏
		if (alpha <= 30) {
			b = 2;
			Intent intent = new Intent(WelcomeActivity.this, MainTabActivity.class);
			startActivity(intent);
			this.finish();
			// 查询需要很多内存开销，提前回收一些
			System.gc();
		}

		mHandler.sendMessage(mHandler.obtainMessage());
	}

}
