package com.nengfei.app;

import java.util.ArrayList;
import java.util.List;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class StatisticsActivity extends FragmentActivity {
	private ViewPager viewPager;// 页卡内容
	private ImageView imageView;// 动画图片
	private TextView voiceAnswer, healthPedia;// 选项名称
	private List<Fragment> fragments;// Tab页面列表
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度
	private int selectedColor, unSelectedColor;
	/** 页卡总数 **/
	private static final int pageSize = 2;
	public void toBack(View v){
		this.finish();
	}
	TextView tv_title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabwidget);
		tv_title=(TextView) this.findViewById(R.id.tv_title);
		tv_title.setText("考试统计");
		tv_title.setTextSize(14);
		tv_title.setTextColor(Color.BLACK);
		tv_title.setTypeface(MainTabActivity.font_apple);
		initView();
	}

	private void initView() {
		selectedColor = getResources()
				.getColor(R.color.tab_title_pressed_color);
		unSelectedColor = getResources().getColor(
				R.color.tab_title_normal_color);

		InitImageView();
		InitTextView();
		InitViewPager();
	}

	/**
	 * 初始化Viewpager页
	 */
	private void InitViewPager() {
		viewPager = (ViewPager) findViewById(R.id.vPager);
		fragments = new ArrayList<Fragment>();
		fragments.add(new StatisticsTestTab());
		fragments.add(new StatisticsTopicTab());
		 
		viewPager.setAdapter(new myPagerAdapter(getSupportFragmentManager(),
				fragments));
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	/**
	 * 初始化头标
	 * 
	 */
	private void InitTextView() {
		voiceAnswer = (TextView) findViewById(R.id.tab_1);
		healthPedia = (TextView) findViewById(R.id.tab_2);
		//pDected = (TextView) findViewById(R.id.tab_3);

		voiceAnswer.setTextColor(selectedColor);
		healthPedia.setTextColor(unSelectedColor);
//		pDected.setTextColor(unSelectedColor);

		voiceAnswer.setText("普通练习");
		healthPedia.setText("模拟测试");
	//	pDected.setText("育儿检测");

		voiceAnswer.setOnClickListener(new MyOnClickListener(0));
		healthPedia.setOnClickListener(new MyOnClickListener(1));
		//pDected.setOnClickListener(new MyOnClickListener(2));
	}

	/**
	 * 初始化动画，这个就是页卡滑动时，下面的横线也滑动的效果，在这里需要计算一些数据
	 */

	private void InitImageView() {
		imageView = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(),
				R.drawable.tab_selected_bg).getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = (screenW / pageSize - bmpW) / 2+1;// 计算偏移量--(屏幕宽度/页卡总数-图片实际宽度)/2
													// = 偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		imageView.setImageMatrix(matrix);// 设置动画初始位置
	}

	/**
	 * 头标点击监听
	 */
	private class MyOnClickListener implements OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		public void onClick(View v) {

			switch (index) {
			case 0:
				voiceAnswer.setTextColor(selectedColor);
				healthPedia.setTextColor(unSelectedColor);
				//pDected.setTextColor(unSelectedColor);
				break;
			case 1:
				healthPedia.setTextColor(selectedColor);
				voiceAnswer.setTextColor(unSelectedColor);
				//pDected.setTextColor(unSelectedColor);
				break;
			case 2:
				//pDected.setTextColor(selectedColor);
				voiceAnswer.setTextColor(unSelectedColor);
				healthPedia.setTextColor(unSelectedColor);
				break;
			}
			viewPager.setCurrentItem(index);
		}

	}

	/**
	 * 为选项卡绑定监听器
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		int two = one * 2;// 页卡1 -> 页卡3 偏移量

		public void onPageScrollStateChanged(int index) {
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		public void onPageSelected(int index) {
			Animation animation = new TranslateAnimation(one * currIndex, one
					* index, 0, 0);// 显然这个比较简洁，只有一行代码。
			currIndex = index;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			imageView.startAnimation(animation);

			switch (index) {
			case 0:
				voiceAnswer.setTextColor(selectedColor);
				healthPedia.setTextColor(unSelectedColor);
				//pDected.setTextColor(unSelectedColor);
				break;
			case 1:
				healthPedia.setTextColor(selectedColor);
				voiceAnswer.setTextColor(unSelectedColor);
				//pDected.setTextColor(unSelectedColor);
				break;
//			case 2:
//				//pDected.setTextColor(selectedColor);
//				voiceAnswer.setTextColor(unSelectedColor);
//				healthPedia.setTextColor(unSelectedColor);
//				break;
			}
		}
	}

	/**
	 * 定义适配器
	 */
	class myPagerAdapter extends FragmentPagerAdapter {
		private List<Fragment> fragmentList;

		public myPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
			super(fm);
			this.fragmentList = fragmentList;
		}

		/**
		 * 得到每个页面
		 */
		@Override
		public Fragment getItem(int arg0) {
			return (fragmentList == null || fragmentList.size() == 0) ? null
					: fragmentList.get(arg0);
		}

		/**
		 * 每个页面的title
		 */
		@Override
		public CharSequence getPageTitle(int position) {
			return null;
		}

		/**
		 * 页面的总个数
		 */
		@Override
		public int getCount() {
			return fragmentList == null ? 0 : fragmentList.size();
		}
	}
	
	
//	private Intent intent;
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
//		 setContentView(R.layout.activity_statistics);
//
////		TabHost tabHost = getTabHost();
//		 TabHost tabHost = (TabHost) findViewById(R.id.host);
//		
////		LayoutInflater.from(this).inflate(R.layout.table_static,
////				tabHost.getTabContentView(), true);
////		LayoutInflater.from(this).inflate(R.layout.table_static, null);
//		intent=new Intent(StatisticsActivity.this, StatisticsTopicTab.class);
//		
//		tabHost.addTab(tabHost.newTabSpec("tab1")
//				.setIndicator(getString(R.string.statistics_topic))
//				.setContent(intent));
//		
//		intent=new Intent(StatisticsActivity.this, StatisticsTestTab.class);
//		tabHost.addTab(tabHost.newTabSpec("tab2")
//				.setIndicator(getString(R.string.statistics_test))
//				.setContent(intent));
//	}
}
