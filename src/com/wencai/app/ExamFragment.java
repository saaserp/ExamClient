package com.wencai.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wencai.controller.MainTabController;
import com.wencai.controller.TopicController;
import com.wencai.login.LoginActivity;
import com.wencai.project.ProjectConfig;
import com.wencai.util.UiUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


public class ExamFragment  extends Fragment implements OnItemClickListener{
	ListView lv;
 
	private MainTabController mtc;
	public ExamFragment( ){
	 
	}
	
	
	String []items={
			"顺序练习",
			"随机练习",
			"专项练习",
			"未做题目",
			"模拟考试",
			"考试统计",
			"我的收藏",
			"我的错题",
			"考试记录"};
	String []from={"item","image"};
	int []images={R.drawable.ps_sx,
			R.drawable.ks_sx,
			R.drawable.ks_zx,
			R.drawable.ks_wz,
			R.drawable.ks_mn,
			R.drawable.ks_tj,
			R.drawable.ks_sc,
			R.drawable.ks_jl,
			R.drawable.ks_ct};
	int [] to={R.id.item_name,R.id.image_icon};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
//		View view=inflater.inflate(R.layout.fragment_menus, container,false);
//		List<Map<String,String>>data=new ArrayList<Map<String,String>>();
//		for(int i=0;i<items.length;i++){
//			Map<String,String> map=new HashMap<String, String>();
//			map.put("item", items[i]);
//			map.put("image", String.valueOf(images[i]));
//			data.add(map);
//		}
//		lv=(ListView)view.findViewById(R.id.listview_menu);
//		lv.setAdapter(new SimpleAdapter(getActivity(), data, R.layout.item_menu, from,to));
//		lv.setOnItemClickListener(this);
//		mtc = new MainTabController(getActivity());
//		
		
		
		 return inflater.inflate(R.layout.mainpage, container, false);
	//return view;
	}

	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		Intent intent;
		if(((MainTabActivity)getActivity()).login()){
			//如果登录了，就要取出uid
		 
		switch(position){
		case 0:
			//顺序练习
			  intent = new Intent(getActivity(), TopicActivity.class);
			intent.putExtra("mode", TopicController.MODE_SEQUENCE);
			startActivity(intent);
			break;
		case 1:
			//随机练习
			
			  intent = new Intent(getActivity(), TopicActivity.class);
			intent.putExtra("mode", TopicController.MODE_RANDOM);
			startActivity(intent);
			break;
		case 2:
			//专项练习
			if (ProjectConfig.TOPIC_MODE_CHAPTERS_SUPPORT) {
				// Intent intent = new Intent(this, TopicActivity.class);
				// intent.putExtra("mode", TopicController.MODE_CHAPTERS);
				// startActivity(intent);
				  intent = new Intent(getActivity(), ChapterSelectActivity.class);

				startActivity(intent);

			} else {
				UiUtil.showToastShort(getActivity(), R.string.please_wait);
			}
			break;
		case 3:
		//未做题
		if (ProjectConfig.TOPIC_MODE_INTENSIFY_SUPPORT) {
			  intent = new Intent(getActivity(), TopicActivity.class);
			intent.putExtra("mode", TopicController.MODE_INTENSIFY);
			startActivity(intent);
		} else {
			UiUtil.showToastShort(getActivity(), R.string.please_wait);
		}
		break;
		case 4:
			//模拟考试
			  intent = new Intent(getActivity(), TopicActivity.class);
			intent.putExtra("mode", TopicController.MODE_PRACTICE_TEST);
			startActivity(intent);
			break;
		case 5:
			//考试统计
			  intent = new Intent(getActivity(), StatisticsActivity.class);
			startActivity(intent);
			break;
		case 6:
			//收藏
			if (mtc.checkCollectedDataExist()) {
				  intent = new Intent(getActivity(), TopicActivity.class);
				intent.putExtra("mode", TopicController.MODE_COLLECT);
				startActivity(intent);
			} else {
				UiUtil.showToastShort(getActivity(), R.string.data_not_exist);
			}
			break;
		case 7:
			//我的错题
			if (mtc.checkCollectedDataExist()) {
				  intent = new Intent( getActivity(), TopicActivity.class);
				intent.putExtra("mode", TopicController.MODE_WRONG_TOPIC);
				startActivity(intent);
			} else {
				UiUtil.showToastShort(getActivity(), R.string.data_not_exist);
			}

			break;
		case 8:
			//考试记录
			  intent = new Intent(getActivity(), RecordActivity.class);
			startActivity(intent);
			break;
			default:
				Toast.makeText(getActivity(), "非法操作！", Toast.LENGTH_SHORT).show();
			break;
		
		}
		}
	}
}
