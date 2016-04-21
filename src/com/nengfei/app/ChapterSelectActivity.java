package com.nengfei.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nengfei.controller.MainTabController;
import com.nengfei.controller.TopicController;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
/**
 * 章节练习
 * @author wencai
 *
 */
public class ChapterSelectActivity extends BaseActivity implements OnItemClickListener{
	private MainTabController mtc;
	private ListView listview;
	private List<Map<String,String>> list;//id,chapter
	private ArrayList<Map<String,Object>> classicsEntryList;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mtc=new MainTabController(this);
		setContentView(R.layout.activity_chapters);
		list =new ArrayList<Map<String,String>>();
		listview=(ListView) this.findViewById(R.id.lv_chapter);


		classicsEntryList=mtc.getChapterListEntryList();
		ArrayList<String> contents=new ArrayList<String>();
		for(int i=0;i<classicsEntryList.size();i++){
			contents.add((i+1)+". "+(String.valueOf(classicsEntryList.get(i).get("description"))));
			Map<String,String> map=new HashMap<String,String>();
			map.put("id", (String.valueOf(classicsEntryList.get(i).get("id"))));
			list.add(map);
		}

		
		listview.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_activated_1,
				android.R.id.text1, contents));
		listview.setOnItemClickListener(this);
	}



	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, TopicActivity.class);
		intent.putExtra("mode", TopicController.MODE_CHAPTERS);
		//subClass就是章节的ID 从1开始，所以＋1
		intent.putExtra("subClass",position+1);
		startActivity(intent);

	}
}
