package com.nengfei.app;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class StatisticsActivity extends TabActivity {
	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.activity_statistics);

		TabHost tabHost = getTabHost();
		
		LayoutInflater.from(this).inflate(R.layout.activity_statistics,
				tabHost.getTabContentView(), true);
		intent=new Intent(StatisticsActivity.this, StatisticsTopicTab.class);
		
		tabHost.addTab(tabHost.newTabSpec("tab1")
				.setIndicator(getString(R.string.statistics_topic))
				.setContent(intent));
		
		intent=new Intent(StatisticsActivity.this, StatisticsTestTab.class);
		tabHost.addTab(tabHost.newTabSpec("tab2")
				.setIndicator(getString(R.string.statistics_test))
				.setContent(intent));
	}
}
