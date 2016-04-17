package com.wencai.app;

import android.os.Bundle;
import android.widget.TextView;

public class MoreDetailsActivity extends BaseActivity {
	private TextView tv_title;
	private TextView tv_more_details;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more_details);
		tv_title=(TextView) findViewById(R.id.tv_title);
		tv_more_details=(TextView) findViewById(R.id.tv_more_details);
		
		int position=getIntent().getIntExtra("position", 0);
		tv_title.setText(getResources().getStringArray(R.array.more_items)[position]);
		tv_more_details.setText(getResources().getStringArray(R.array.more_details)[position]);
	}

}

