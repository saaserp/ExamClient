package com.nengfei.gidance;


import com.nengfei.app.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GuidanceActivity extends Activity {

	// page页面
	LinearLayout[] views = new LinearLayout[3];
	int[] pics = { R.drawable.a, R.drawable.b, R.drawable.c };

	ImageView[] points = new ImageView[3];
	int ids[] = { R.id.point_1, R.id.point_2, R.id.point_3 };
	int index = 0;

	ViewPager guidance_vp;

	TextView btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guidance);
		initView();
	}

	private void initView() {
		guidance_vp = (ViewPager) findViewById(R.id.guidance_vp);
		btn = (TextView) findViewById(R.id.btn);
		for (int i = 0; i < points.length; i++) {
			// 循环创建
			// 创建�?
			points[i] = (ImageView) findViewById(ids[i]);
			// 创建页面
			// 设置大小
			views[i] = new LinearLayout(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			views[i].setLayoutParams(params);
			// 创建ImageView
			ImageView iv = new ImageView(this);
			iv.setImageResource(pics[i]);
			iv.setScaleType(ScaleType.FIT_XY);
			iv.setLayoutParams(params);
			views[i].addView(iv);
		}
		points[0].setSelected(true);
		guidance_vp.setAdapter(adapter);
		guidance_vp.setOnPageChangeListener(listener);
		btn.setOnClickListener(btnlistener);
	}

	OnClickListener btnlistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 设置xml文件，第�?次登录为false
			getSharedPreferences("first", MODE_PRIVATE).edit()
					.putBoolean("isFirst", false).commit();
			finish();
		}
	};
	/**
	 * 
	 */
	OnPageChangeListener listener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			 
			points[index].setSelected(false);
			points[arg0].setSelected(true);
			index = arg0;
			if (arg0 == 2) {
				btn.setVisibility(View.VISIBLE);
			} else
				btn.setVisibility(View.GONE);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	};

	PagerAdapter adapter = new PagerAdapter() {

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			return views.length;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(views[position]);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(views[position]);
			return views[position];
		}
	};

	@Override
	public void onBackPressed() {
		System.out.println("GuidanceActivity.onBackPressed()");
		setResult(RESULT_CANCELED);
		overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);
		finish();
	};
}
