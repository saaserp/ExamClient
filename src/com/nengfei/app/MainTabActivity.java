package com.nengfei.app;

import java.util.Timer;
import java.util.TimerTask;

import com.nengfei.adapter.MainTabPagerAdapter;
import com.nengfei.backup.PullDataTask;
import com.nengfei.controller.MainTabController;
import com.nengfei.controller.TopicController;
import com.nengfei.gidance.GuidanceActivity;
import com.nengfei.login.LoginActivity;
import com.nengfei.project.ProjectConfig;
import com.nengfei.tiku.CustomDialog;
import com.nengfei.util.CallBack;
import com.nengfei.util.DBUtil;
import com.nengfei.util.FileUtil;
import com.nengfei.util.UiUtil;
import com.nengfei.web.WebActivity;
import com.nengfei.widget.IconPageIndicator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

public class MainTabActivity extends FragmentActivity
implements MoreListFragment.Callbacks, ClassicsListFragment.Callbacks ,OnClickListener{
	static int FLAGE_GAI = 1;// 引导页
	static int FLAGE_LOGIN = 2;// 登录

	private ViewPager main_tab_pager;// viewpager
	private IconPageIndicator main_tab_icon_indicator;
	private MainTabPagerAdapter mtpa;
	private MainTabController mtc;
	private TextView tv_title;
	private FileUtil fu;

	// for exit
	private static Timer tExit;
	private static TimerTask task;
	private static Boolean isExit = false;
	private static Boolean hasTask = false;
	public static MainTabActivity mtb;
	static String tpuid;
	public static Handler handRelogin=new Handler(){
		public void handleMessage(android.os.Message msg) {
			 
			
			
			new PullDataTask(mtb,new CallBack(){

				@Override
				public String done(boolean b) {
					// TODO Auto-generated method stub

					 
					if(b){
						
						tpuid=LoginActivity.uid;
						mtb.getSharedPreferences("user", Activity.MODE_PRIVATE).edit().putString("uid", "anonymous").commit();
						mtb.startActivity(new Intent(mtb,LoginActivity.class));
						 
						LoginActivity.logout();
					}else{
						//数据恢复
						Toast.makeText(mtb, "无法退出当前账户", Toast.LENGTH_SHORT).show();
						mtb.getSharedPreferences("user", Activity.MODE_PRIVATE).edit().putString("uid", tpuid).commit();
						LoginActivity.uid=tpuid;
					}
					return null;
				}

			},true).execute();
			 
			 
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main_tab);
		
		SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
		String uid = sp.getString("uid", "anonymous");
		//设置用户名
		LoginActivity.uid=uid;


		// 引导页

		isFirst();

		mtc = new MainTabController(this); 

		main_tab_pager = (ViewPager) findViewById(R.id.main_tab_pager);
		main_tab_icon_indicator = (IconPageIndicator) findViewById(R.id.main_tab_icon_indicator);
		main_tab_icon_indicator.setBackgroundResource(R.drawable.zx);

		mtpa = mtc.getPagerAdapter(getSupportFragmentManager());
		main_tab_pager.setAdapter(mtpa);

		main_tab_icon_indicator.setViewPager(main_tab_pager);

		int page = getIntent().getIntExtra("page", -1);

		if (page < 0) {
			switchPage(0);
		} else {
			switchPage(page);
		}

		mtb=this;
	}

	private void switchPage(int position) {

		tv_title = (TextView) findViewById(R.id.tv_title);
		main_tab_pager.setCurrentItem(position);

		tv_title.setText(mtpa.getTitles().get(position));
		main_tab_pager.setOnPageChangeListener(getOnPageChangeListener());
	}

	@Override
	// for more
	public void onMoreItemSelected(int position) {
		Intent intent = null;

		switch (position) {
		case 0:
			//用户协议
		case 1:
			//问题反馈
		case 2:
			intent = new Intent(this, MoreDetailsActivity.class);
			intent.putExtra("position", position);
			break;
		case 3:
			intent = new Intent(this, ShareSettingActivity.class);
			break;
		case 4:
		{



			new AlertDialog.Builder(MainTabActivity.this).setTitle("系统提示")//设置对话框标题  

			.setMessage("你确定要重新设置数据库吗？")//设置显示的内容  

			.setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮  



				@Override  

				public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件  

					// TODO Auto-generated method stub  
					if(LoginActivity.haslogin()){
					//先保存数据,回调
					new PullDataTask(MainTabActivity.this,new CallBack() {
						
						@Override
						public String done(boolean b) {
							// TODO Auto-generated method stub
							getSharedPreferences("first", MODE_PRIVATE).edit()
							.putBoolean("isFirst", false).commit();
							MainTabActivity.this.getSharedPreferences("tiku", Activity.MODE_PRIVATE).edit()
							.putString("tiku",  "").commit();
											
							DBUtil.dbName="";
							startActivity(new Intent(MainTabActivity.this,WelcomeActivity.class) );
							MainTabActivity.this.finish();
							//Toast.makeText(MainTabActivity.this, "重设成功，请手动重新启动", Toast.LENGTH_LONG).show();

							return null;
						}
					}).execute();
					}
					else{
						 login();
						
					}
					
					
					 
					
					
					
				

				}  

			}).setNegativeButton("取消",new DialogInterface.OnClickListener() {//添加返回按钮  



				@Override  

				public void onClick(DialogInterface dialog, int which) {//响应事件  

					// TODO Auto-generated method stub  

					Log.i("alertdialog"," 请保存数据！");  

				}  

			}).show();//在按键响应事件中显示此对话框  


		}
		default:
			break;
		}
		if (intent != null) {
			startActivity(intent);
		}
	}

	@Override
	public void onClassicsIdSelected(int classicsId) {
		Intent intent = new Intent(this, ClassicsActivity.class);
		intent.putExtra("questionId", classicsId);
		if(login()){
			startActivity(intent);
		} 

	}

	public void shotView(View view) {
		fu = new FileUtil(this);
		Bitmap bm = fu.shotAndSave(fu.getPic_path());

		Intent toShare = new Intent(this, ShareFriendActivity.class);
		startActivity(toShare);
		// 保存完毕，及时回收
		if (!bm.isRecycled()) {
			bm.recycle();
		}
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		JPushInterface.onPause(getApplicationContext());
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		JPushInterface.onResume(getApplicationContext());
	}
	public void onClick(View v){
		Intent intent;
		if(login()){
			switch(v.getId()){
			case R.id.sxlx:
				//顺序练习
				intent = new Intent(this, TopicActivity.class);
				intent.putExtra("mode", TopicController.MODE_SEQUENCE);
				startActivity(intent);
				break;
			case R.id.sjlx:
				//随机练习
				intent = new Intent(this, TopicActivity.class);
				intent.putExtra("mode", TopicController.MODE_RANDOM);
				startActivity(intent);
				break;
			case R.id.zxlx:
				//专项练习
				if (ProjectConfig.TOPIC_MODE_CHAPTERS_SUPPORT) {
			 
					intent = new Intent(this, ChapterSelectActivity.class);

					startActivity(intent);

				} else {
					UiUtil.showToastShort(this, R.string.please_wait);
				}
				break;
			case R.id.wzt:
				//未做题
				if (ProjectConfig.TOPIC_MODE_INTENSIFY_SUPPORT) {
					intent = new Intent(this, TopicActivity.class);
					intent.putExtra("mode", TopicController.MODE_INTENSIFY);
					startActivity(intent);
				} else {
					UiUtil.showToastShort(this, R.string.please_wait);
				}
				break;
			case R.id.mnks:

				//模拟考试
				intent = new Intent(this, TopicActivity.class);
				intent.putExtra("mode", TopicController.MODE_PRACTICE_TEST);
				startActivity(intent);
				break;

			case R.id.wdsc:
				//收藏
				if (mtc.checkCollectedDataExist()) {
					intent = new Intent(this, TopicActivity.class);
					intent.putExtra("mode", TopicController.MODE_COLLECT);
					startActivity(intent);
				} else {
					UiUtil.showToastShort(this, R.string.data_not_exist);
				}
				break;
			case R.id.wdct:
				//我的错题
				if (mtc.checkCollectedDataExist()) {
					intent = new Intent(this, TopicActivity.class);
					intent.putExtra("mode", TopicController.MODE_WRONG_TOPIC);
					startActivity(intent);
				} else {
					UiUtil.showToastShort(this, R.string.data_not_exist);
				}
				break;
			case R.id.ksjl:
				//考试记录
				intent = new Intent(this, RecordActivity.class);
				startActivity(intent);
				break;

			case R.id.kstj:
				//考试统计
				intent = new Intent(this, StatisticsActivity.class);
				startActivity(intent);
				break;
			case R.id.ad1:
				//视频教学
				intent=new Intent(this,WebActivity.class);
				intent.putExtra("url", "http://m.gdgdpowerfly.webportal.cc/col.jsp?id=107");
				startActivity(intent);
				break;
			case R.id.ad2:
				//能飞航空
				intent=new Intent(this,WebActivity.class);
				intent.putExtra("url", "http://m.gdgdpowerfly.webportal.cc");
				startActivity(intent);
				break;
			case R.id.ad3:
				//无人机培训报名
				intent=new Intent(this,WebActivity.class);
				intent.putExtra("url", "http://m.gdgdpowerfly.webportal.cc");
				startActivity(intent);
				break;
			
			}
			
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// for exit
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if(isExit==false){
				UiUtil.showToastShort(MainTabActivity.this, R.string.main_exit_prompt);
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						isExit=true;
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						isExit=false;
					}
				}){}.start();
			}else{

				if(LoginActivity.haslogin()){

					this.finish();
					System.exit(0);
				}else{
					this.finish();
					System.exit(0);
				}

			}
		}
		return false;
	}

	private OnPageChangeListener getOnPageChangeListener() {
		return (new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				tv_title.setText(mtpa.getTitles().get(position));
				main_tab_icon_indicator.setCurrentItem(position);
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
			}
		});
	}

	//	private void setDatabase() {
	//		SharedPreferences sp = getSharedPreferences("db", MODE_PRIVATE);
	//		String dbName = sp.getString("dbName", "data.db");// 获取设定的数据库
	//
	//	}

	boolean login() {
		SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
		String uid = sp.getString("uid", "anonymous");
		LoginActivity.uid=uid;
		if (!LoginActivity.haslogin()) {
			// 还没有登录
			startActivityForResult(new Intent(this, LoginActivity.class), FLAGE_LOGIN);
			return false;
		}else{
			return true;
		}

	}

	private boolean isFirst() {

		SharedPreferences sp = getSharedPreferences("first", MODE_PRIVATE);
		boolean isFirst = sp.getBoolean("isFirst", true);
		if (isFirst) {

			overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
			startActivityForResult(new Intent(this, GuidanceActivity.class), FLAGE_GAI);
		}else{

		}
		return isFirst;
	}

	@Override
	protected void onActivityResult(int request, int result, Intent it) {
		// TODO Auto-generated method stub

		if (request == FLAGE_LOGIN) {
			if (result == RESULT_OK) {

				String uid = it.getStringExtra("uid");
				if (uid != null) {
					// 登录成功
					getSharedPreferences("user", MODE_PRIVATE).edit().putString("uid", uid).commit();
					LoginActivity.uid=uid;
				}

			} else if (result == RESULT_CANCELED) {
				// 取消登录
				Log.i("user", "login cancer");
				if(LoginActivity.uid.equals("anonymous")){
					Toast.makeText(this, "请先登录，亲~", Toast.LENGTH_SHORT).show();
					getSharedPreferences("user", MODE_PRIVATE).edit().putString("uid", "anonymous").commit();

				}
			}
		}
		if (request == FLAGE_GAI) {

		}

	}




}
