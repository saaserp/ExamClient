package com.nengfei.app;

import java.util.ArrayList;
import java.util.List;

import com.nengfei.social.TencentWeiboOAuthV2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ShareSettingActivity extends BaseActivity {

	private ListView lv_share_setting;
	private TextView tv_title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share_setting);
		
		lv_share_setting=(ListView) findViewById(R.id.lv_share_setting);
		List<String> shareList=new ArrayList<String>();
		shareList.add(getResources().getString(R.string.tencent_weibo));
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, shareList);
		lv_share_setting.setAdapter(adapter);
		lv_share_setting.setOnItemClickListener(getShareSettingListener());
		
		tv_title=(TextView) findViewById(R.id.tv_title);
		tv_title.setText(R.string.share_friend);
	}
	
	private OnItemClickListener getShareSettingListener(){
		return new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent=null;
				switch(position){
				case 0:
					intent=new Intent(ShareSettingActivity.this,TencentWeiboOAuthV2.class);
					break;
				default:
					break;
				}
				if(intent!=null){
					startActivity(intent);
				}
			}
		};
	}
}
