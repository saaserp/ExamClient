package com.nengfei.adapter;

import java.util.ArrayList;

import com.nengfei.app.MainTabActivity;
import com.nengfei.app.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MyTextAdapter extends BaseAdapter{
	Context context;
	ArrayList<String> contents;
	public MyTextAdapter(Context context,ArrayList<String> contents){
		this.context=context;
		this.contents=contents;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		 

		 
			convertView=LayoutInflater.from(context).inflate(R.layout.layout_more_item, null);
			TextView tv=(TextView) convertView.findViewById(R.id.item_name);
			tv.setTypeface(MainTabActivity.font_apple);
			tv.setText(contents.get(position));
			tv.setTextSize(14);
			 
		 
		 
		return convertView;
	}
	
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return contents.get(position);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return contents.size();
	}

}
