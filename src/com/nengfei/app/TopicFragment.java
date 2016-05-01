package com.nengfei.app;

import java.util.HashMap;
import java.util.Map;

import com.nengfei.adapter.TopicFragmentCallBacks;
import com.nengfei.backup.SleepTask;
import com.nengfei.controller.TopicController;
import com.nengfei.project.ProjectConfig;
import com.nengfei.util.CallBack;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;

@SuppressLint({ "ValidFragment", "UseSparseArrays" })
public class TopicFragment extends Fragment {
	private static final String KEY_POSITION = "TopicFragment:POSITION";
	private int mPosition;
	private Map<String, Object> dataMap;
	private static TopicController topicController;
	private static Context context;
	private static int topicMode;
	private static Activity activity;
	private static TopicFragmentCallBacks topicFragmentCallBacks;
	private static final String[] PRESUFFIX = new String[] { "A", "B", "C", "D" };
	private RadioGroup rg_topic;

	public static void setController(TopicController topicController) {
		TopicFragment.topicController = topicController;
		context = topicController.getContext();
		activity = (Activity) context;
		topicMode = topicController.getMode();
	}

	public static void setCallBack(TopicFragmentCallBacks topicFragmentCallBacks) {
		TopicFragment.topicFragmentCallBacks = topicFragmentCallBacks;
	}

	public TopicFragment(int mPosition) {
		super();
		this.mPosition = mPosition;
		dataMap = topicController.getTopicList().get(mPosition);
	}

	/**
	 * 一定要有一个空的构造函数，牵涉到反射机制，否则旋屏挂掉
	 * public static Fragment instantiate(Context context, String fname, Bundle args) {
	 *     try {            
	 *         		Class<?> clazz = sClassMap.get(fname);            
	 *         		if (clazz == null) { 
	 *         		// Class not found in the cache, see if it's real, and try to add it                
	 *         		clazz = context.getClassLoader().loadClass(fname); 
	 *         		sClassMap.put(fname, clazz);            
	 *         	}            
	 *         		Fragment f = (Fragment)clazz.newInstance();
	 *         		if (args != null) { 
	 *             	args.setClassLoader(f.getClass().getClassLoader());                
	 *             	f.mArguments = args;           
	 *         	}            
	 *         	return f;        
	 *         	} 
	 *         	catch (ClassNotFoundException e) {
	 *            	throw new InstantiationException("Unable to instantiate fragment " + fname
	 *                      	+ ": make sure class name exists, is public, and has an"
	 *                   		+ " empty constructor that is public", e);        
	 *         	} catch (java.lang.InstantiationException e) {            
	 *         		throw new InstantiationException("Unable to instantiate fragment " + fname
	 *                      	+ ": make sure class name exists, is public, and has an"                    
	 *                       	+ " empty constructor that is public", e);       
	 *         	} catch (IllegalAccessException e) {            
	 *         		throw new InstantiationException("Unable to instantiate fragment " + fname                    
	 *         					+ ": make sure class name exists, is public, and has an"                    
	 *         					+ " empty constructor that is public", e);
	 *    		}    
	 *} 
	 */

	public TopicFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if ((savedInstanceState != null)
				&& savedInstanceState.containsKey(KEY_POSITION)) {
			mPosition = savedInstanceState.getInt(KEY_POSITION);
			dataMap = topicController.getTopicList().get(mPosition);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(KEY_POSITION, mPosition);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		ScrollView scrollView = new ScrollView(context);
		LinearLayout topicLayout = new LinearLayout(context);
		topicLayout.setOrientation(LinearLayout.VERTICAL);
		// 题目生成
		final TextView tv_topic = new TextView(context);
		tv_topic.setPadding(4, 5, 4, 5);
		tv_topic.setTextSize(20);
		tv_topic.setTypeface(MainTabActivity.font_apple);
		tv_topic.setText(String.valueOf(mPosition + 1) + "."
				+ String.valueOf(dataMap.get("question")));
		topicLayout.addView(tv_topic, lp);
		final LinearLayout explainLayout = new LinearLayout(context);

		// 根据题型形成选项
		int type = Integer.valueOf(String.valueOf(dataMap.get("type")));
		HashMap<String, Integer> savedOrderMap = topicController
				.getSavedOrderMap(mPosition);
		final HashMap<String, Integer> orderMap;
		if (savedOrderMap == null) {
			orderMap = topicController.getOrderMap(type);
			topicController.setOrderMap(mPosition, orderMap);
		} else {
			orderMap = savedOrderMap;
		}
		rg_topic = new RadioGroup(context);
		switch (type) {
		// 选择题
		case TopicController.TYPE_CHOICE: {
			 
			
			RadioButton[] rbs = new RadioButton[4];
			for (int i = 0; i < 3; i++) {
				rbs[i] = new RadioButton(context);
			 
				rbs[i].setButtonDrawable(R.drawable.radio);
				rbs[i].setText(PRESUFFIX[i]
						+ "."
						+ String.valueOf(dataMap.get(topicController
								.getItemValue(orderMap.get(PRESUFFIX[i])))));
				 
				rbs[i].setTypeface(MainTabActivity.font_yahei);
				rg_topic.addView(rbs[i], lp);
			}
			break;
		}
		 
				 
		// 判断题
		case TopicController.TYPE_RW: {
			RadioButton[] rbs = new RadioButton[2];
			for (int i = 0; i < 2; i++) {
				rbs[i] = new RadioButton(context);
				rbs[i].setText(PRESUFFIX[i]
						+ "."
						+ String.valueOf(dataMap.get(topicController
								.getItemValue(orderMap.get(PRESUFFIX[i])))));
				rbs[i].setTypeface(MainTabActivity.font_yahei);
				rg_topic.addView(rbs[i], lp);
			}
			break;
		}
		default:
			break;
		}

		if(topicController.isAnswerShow()){		
			if(topicController.getSelectedFlag(mPosition)){
				SetSelectedRadioButton(rg_topic,
						topicController.getSelecetedChoice(mPosition));
			}
			topicController.getCorrectRadioButton(rg_topic, orderMap,
					(String) dataMap.get("answer")).setTextColor(
					Color.GREEN);
			topicController.setRadioButtonState(rg_topic, false);
		}else{
			// 已答
			if (topicController.getSelectedFlag(mPosition)) {
				SetSelectedRadioButton(rg_topic,
						topicController.getSelecetedChoice(mPosition));
				if (topicMode == TopicController.MODE_PRACTICE_TEST) {

				} else {
					topicController.getCorrectRadioButton(rg_topic, orderMap,
							(String) dataMap.get("answer")).setTextColor(
							Color.GREEN);
				}
				topicController.setRadioButtonState(rg_topic, false);
			}
		}
		
		// 单选框绑定事件
		rg_topic.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				topicController.setSelectedFlag(mPosition, true);
				RadioButton tempRb = (RadioButton) activity
						.findViewById(checkedId);
				String tempString = tempRb.getText().toString();
				tempString = tempString.substring(0, tempString.indexOf("."));
				topicController.setSelecetedChoice(mPosition, tempString);
				
				// 选择正确时处理
				if (String.valueOf(orderMap.get(tempString)).equals(
						dataMap.get("answer"))) {
					topicController.setRadioButtonState(rg_topic, false);
					topicController.addRightCount(topicController
							.getDaoId(mPosition + 1));
					tempRb.setTextColor(Color.GREEN);
					new SleepTask(getActivity(), rg_topic, new CallBack(){

						@Override
						public String done(boolean b) {
							// TODO Auto-generated method stub
							snapToScreen(mPosition + 1);
							if (topicMode == TopicController.MODE_PRACTICE_TEST) {
								topicController.countRight();
							}
							return null;
						}}).execute();
					 
					
				}
				// 选择错误时处理
				else {
					// 比较选项和正确答案
					topicController.setRadioButtonState(rg_topic, false);
					topicController.addWrongCount(topicController
							.getDaoId(mPosition + 1));
					if (topicMode == TopicController.MODE_PRACTICE_TEST) {
						topicController.countWrong();
						tempRb.setTextColor(Color.GREEN);
						new SleepTask(getActivity(), rg_topic, new CallBack(){

							@Override
							public String done(boolean b) {
								// TODO Auto-generated method stub
								snapToScreen(mPosition + 1);
								return null;
							}}).execute();
						
					} else {
						tempRb.setTextColor(Color.RED);
						topicController.getCorrectRadioButton(rg_topic,
								orderMap, (String) dataMap.get("answer"))
								.setTextColor(Color.GREEN);
						if (ProjectConfig.TOPIC_EXPLAIN_SHOW){
							explainLayout.setVisibility(LinearLayout.VISIBLE);
						}		
					}
				}
			}
		});
		
		topicLayout.addView(rg_topic, lp);
		if (ProjectConfig.TOPIC_EXPLAIN_SHOW) {
			TextView tv_explain = new TextView(context);
			tv_explain.setText(String.valueOf(dataMap.get("explain")));
			explainLayout.addView(tv_explain, lp);
			explainLayout.setVisibility(LinearLayout.INVISIBLE);
			topicLayout.addView(explainLayout, lp);
		}
		scrollView.addView(topicLayout);
		return scrollView;
	}

	private void snapToScreen(int position) {
		if (position < topicController.getTopicList().size() && position >= 0) {
			topicFragmentCallBacks.snapToScreen(position);
		}
	}

	private void SetSelectedRadioButton(RadioGroup rg_topic,
			String selectedChoice) {

		for (int i = 0; i < rg_topic.getChildCount(); i++) {
			RadioButton rb = (RadioButton) rg_topic.getChildAt(i);
			if (selectedChoice.equals(rb.getText().toString().substring(0, 1))) {
				if (topicMode == TopicController.MODE_PRACTICE_TEST) {
					rb.setTextColor(Color.GREEN);
				} else {
					rb.setTextColor(Color.RED);
				}
				rb.setChecked(true);
			}
		}

	}
}
