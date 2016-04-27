package com.nengfei.app;

import com.nengfei.backup.GetSystemInfoTask;
import com.nengfei.login.LoginActivity;
import com.nengfei.model.QuestionBankService;
import com.nengfei.util.CallBack;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;


public class ExamFragment  extends Fragment implements OnItemClickListener{
	ListView lv;

	 public static boolean isloaded=false;
	QuestionBankService qs;
	public ExamFragment( ){
		qs=new QuestionBankService();
		
	}


	//	String []items={
	//			"顺序练习",
	//			"随机练习",
	//			"专项练习",
	//			"未做题目",
	//			"模拟考试",
	//			"考试统计",
	//			"我的收藏",
	//			"我的错题",
	//			"考试记录"};
	//	String []from={"item","image"};
	//	int []images={R.drawable.ps_sx,
	//			R.drawable.ks_sx,
	//			R.drawable.ks_zx,
	//			R.drawable.ks_wz,
	//			R.drawable.ks_mn,
	//			R.drawable.ks_tj,
	//			R.drawable.ks_sc,
	//			R.drawable.ks_jl,
	//			R.drawable.ks_ct};
	//	int [] to={R.id.item_name,R.id.image_icon};

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
		
		View view=inflater.inflate(R.layout.mainpage, container, false);
		View ads[]=new View[3];
		ads[0]=view.findViewById(R.id.adv1);
		ads[1]=view.findViewById(R.id.adv2);
		ads[2]=view.findViewById(R.id.adv3);
		
		
		if(isloaded==false){
		new GetSystemInfoTask(getActivity(),ads,new CallBack(){

			@Override
			public String done(boolean b) {
				// TODO Auto-generated method stub
				return null;
			}}).execute();
		isloaded=true;
		}
		tvNever=(TextView) view.findViewById(R.id.tv_wzt);
	
		tvCuoti=(TextView)view.findViewById(R.id.tv_cuoti);
		tvShouchang=(TextView)view.findViewById(R.id.tv_shouchang);
		tvALl=(TextView)view.findViewById(R.id.quanbu);
		tvHasdo=(TextView)view.findViewById(R.id.yizuoti);
		freashData();
		return view;
		//return view;
	}
	TextView tvNever;
	TextView tvALl;
	TextView tvHasdo;
	TextView tvCuoti;
	TextView tvShouchang;
	public void freashData(){
		int wzt=0;
		int all=qs.getAllCount(getActivity());
		if(LoginActivity.haslogin()){
		wzt=qs.getNeverDoCount(getActivity());
		
		tvNever.setText(wzt+"");
		int ct=qs.getWrongCount(getActivity());
		tvCuoti.setText(ct+"");
		int sc=qs.getShouchangCount(getActivity());
		tvShouchang.setText(sc+"");
		tvHasdo.setText((all-wzt)+"");
		}
		 
		

		
		
		
		tvALl.setText(all+"");
		
		
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		freashData();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
//		Intent intent;
//		if(((MainTabActivity)getActivity()).login()){
//			//如果登录了，就要取出uid
//
//			switch(position){
//			case 0:
//				//顺序练习
//				intent = new Intent(getActivity(), TopicActivity.class);
//				intent.putExtra("mode", TopicController.MODE_SEQUENCE);
//				startActivity(intent);
//				break;
//			case 1:
//				//随机练习
//				intent = new Intent(getActivity(), TopicActivity.class);
//				intent.putExtra("mode", TopicController.MODE_RANDOM);
//				startActivity(intent);
//				break;
//			case 2:
//				//专项练习
//				if (ProjectConfig.TOPIC_MODE_CHAPTERS_SUPPORT) {
//					// Intent intent = new Intent(this, TopicActivity.class);
//					// intent.putExtra("mode", TopicController.MODE_CHAPTERS);
//					// startActivity(intent);
//					intent = new Intent(getActivity(), ChapterSelectActivity.class);
//
//					startActivity(intent);
//
//				} else {
//					UiUtil.showToastShort(getActivity(), R.string.please_wait);
//				}
//				break;
//			case 3:
//				//未做题
//				if (ProjectConfig.TOPIC_MODE_INTENSIFY_SUPPORT) {
//					intent = new Intent(getActivity(), TopicActivity.class);
//					intent.putExtra("mode", TopicController.MODE_INTENSIFY);
//					startActivity(intent);
//				} else {
//					UiUtil.showToastShort(getActivity(), R.string.please_wait);
//				}
//				break;
//			case 4:
//				//模拟考试
//				intent = new Intent(getActivity(), TopicActivity.class);
//				intent.putExtra("mode", TopicController.MODE_PRACTICE_TEST);
//				startActivity(intent);
//				break;
//			case 5:
//				//考试统计
//				intent = new Intent(getActivity(), StatisticsActivity.class);
//				startActivity(intent);
//				break;
//			case 6:
//				//收藏
//				if (mtc.checkCollectedDataExist()) {
//					intent = new Intent(getActivity(), TopicActivity.class);
//					intent.putExtra("mode", TopicController.MODE_COLLECT);
//					startActivity(intent);
//				} else {
//					UiUtil.showToastShort(getActivity(), R.string.data_not_exist);
//				}
//				break;
//			case 7:
//				//我的错题
//				if (mtc.checkCollectedDataExist()) {
//					intent = new Intent( getActivity(), TopicActivity.class);
//					intent.putExtra("mode", TopicController.MODE_WRONG_TOPIC);
//					startActivity(intent);
//				} else {
//					UiUtil.showToastShort(getActivity(), R.string.data_not_exist);
//				}
//
//				break;
//			case 8:
//				//考试记录
//				intent = new Intent(getActivity(), RecordActivity.class);
//				startActivity(intent);
//				break;
//			default:
//				Toast.makeText(getActivity(), "非法操作！", Toast.LENGTH_SHORT).show();
//				break;
//
//			}
//		}
	}
}
