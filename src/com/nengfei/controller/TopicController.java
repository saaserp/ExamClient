package com.nengfei.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.nengfei.adapter.TopicFragmentCallBacks;
import com.nengfei.adapter.TopicPagerAdapter;
import com.nengfei.app.ExamFragment;
import com.nengfei.model.ExamResultService;
import com.nengfei.model.QuestionBankService;
import com.nengfei.model.TempTableService;
import com.nengfei.util.SharedPreferencesUtil;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;

@SuppressLint("UseSparseArrays")
public class TopicController {
	private QuestionBankService questionBankService;
	private ExamResultService examResultService;
	private TempTableService tempTableService;
	//选项对应的数据库的字段
	private HashMap<Integer, String> itemMap;
	private HashMap<Integer, Integer> examMap;
	private HashMap<Integer, HashMap<String, Integer>> totalTopicItemsMap;
	private boolean[] selectedFlags;
	private Context context;
	private ArrayList<Map<String, Object>> topicList;
	//已做的题，以及选择的项
	private HashMap<Integer, String> selecetedChoice;
	private boolean answerShow;
	private FragmentPagerAdapter answerHideFpa;
	private FragmentPagerAdapter answerShowFpa;
	// 根据测试方式，获得题库:0-顺序；1-随机；2-章节；3-强化；4-测试；5-错题本；6-我的收藏
	private int mode;
	private int subClass; // 章节练习中的章节，强化练习中的类型
	public static final int MODE_SEQUENCE = 0;//顺序练习
	public static final int MODE_RANDOM = 1;//随机
	public static final int MODE_CHAPTERS = 2;//章节练习
	public static final int MODE_INTENSIFY = 3;//未做题
	public static final int MODE_PRACTICE_TEST = 4;//测试
	public static final int MODE_WRONG_TOPIC = 5;//错题集
	public static final int MODE_COLLECT = 6;//收藏
	public static final int TYPE_CHOICE = 1;
	public static final int TYPE_CHOICE2 = 13;//三个选项的选择题
	public static final int TYPE_RW = 2;

	//考试
	private int wrongCount;
	private int rightCount;
 
	public TopicController(Context context, int mode, int subClass) {
		questionBankService = new QuestionBankService();
		examResultService = new ExamResultService();
		tempTableService = new TempTableService();

		this.context = context;
		this.mode = mode;
		this.subClass = subClass;

		itemMap = new HashMap<Integer, String>();
		itemMap.put(1, "opt1");
		itemMap.put(2, "opt2");
		itemMap.put(3, "opt3");
		itemMap.put(4, "opt4");

		totalTopicItemsMap = new HashMap<Integer, HashMap<String, Integer>>();
		selecetedChoice = new HashMap<Integer, String>();
	}

	public Context getContext() {
		return context;
	}

	public int getMode() {
		return mode;
	}

	public int getSubClass() {
		return subClass;
	}

	
	public int getWrongCount() {
		return wrongCount;
	}

	public int getRightCount() {
		return rightCount;
	}

	public void countWrong(){
		wrongCount++;
	}
	public void subWrong(){
		if(wrongCount>0)
		wrongCount--;
	}
	public void countRight(){
		rightCount++;
	}
	public void subRight(){
		rightCount--;
	}
	public ArrayList<Map<String, Object>> getTopicList() {
		return topicList;
	}

	public boolean isAnswerShow() {
		return answerShow;
	}

	public void setAnswerShow(boolean answerShow) {
		this.answerShow = answerShow;
	}

	public ArrayList<Map<String, Object>> sequentialSearch() {
		return questionBankService.sequentialSearch(context);
	}

	public ArrayList<Map<String, Object>> randomSearch() {
		return questionBankService.randomSearch(context);
	}
	public ArrayList<Map<String,Object>> chapterSearch(int id){
		return questionBankService.getChapter(context,id);
	}
	public ArrayList<Map<String, Object>> testSearch() {
		return questionBankService.testSearch(context);
	}

	public ArrayList<Map<String, Object>> errorBookSearch() {
		return questionBankService.errorBookSearch(context);
	}
	public ArrayList<Map<String,Object>> neverdoSearch(){
		return questionBankService.neverdoSearch(context);
	}
	 
	public ArrayList<Map<String, Object>> collectedSearch() {
		return questionBankService.collectedSearch(context);
	}

	public boolean checkTableExist(String table) {
		return tempTableService.checkTableExist(context, table);
	}

	public void dropTable(String table) {
		tempTableService.dropTable(context, table);
	}

	public void createTable(String table, ArrayList<Map<String, Object>> mapList) {
		tempTableService.createTable(context, table);
		this.initTable(table, mapList);
	}

	protected void initTable(String table,
			ArrayList<Map<String, Object>> mapList) {
		ContentValues values = new ContentValues();
		int count = 1;
		for (Map<String, Object> map : mapList) {
			values.put("tempId", count);
			values.put("_id", Integer.valueOf((String) map.get("_id")));
			tempTableService.add(context, table, values);
			count++;
		}
	}

	public void initDaoIdMap() {
		if (mode != MODE_SEQUENCE) {
			examMap = questionBankService.getExamMap();
			System.out.println("EXAMMAPCOUNT:" + examMap.size());
		}
	}

	public boolean isDataIdMapNull() {
		if (examMap.size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	public int getDaoId(int id) {
		int returnId = -1;
		switch (mode) {
		case MODE_SEQUENCE: {
			returnId = id;
			break;
		}
		case MODE_RANDOM: {
			returnId = examMap.get(id);
			break;
		}
		case MODE_CHAPTERS:
			//章节练习
			returnId=examMap.get(id);
			break;
		case MODE_INTENSIFY:
			//强化训练（未做题）
			returnId=examMap.get(id);
			break;
		case MODE_PRACTICE_TEST: {
			returnId = examMap.get(id);
			break;
		}
		case MODE_WRONG_TOPIC: {
			returnId = examMap.get(id);
			break;
		}
		case MODE_COLLECT: {
			returnId = examMap.get(id);
			break;
		}
		default:
			break;
		}
		// System.out.println("" + returnId);
		return returnId;
	}

	public int getCollectedFlag(int id) {
		return questionBankService.getCollectedFlag(context, id);
	}

	public void setCollectedFlag(int id) {
		questionBankService.setCollectedFlag(context, id);
	}

	public void resetCollectedFlag(int id) {
		questionBankService.resetCollectedFlag(context, id);
	}

	public int getInWrongFlag(int id) {
		return questionBankService.getInWrongFlag(context, id);
	}
	
	public void setInWrongFlag(int id) {
		questionBankService.setInWrongFlag(context, id);
	}

	public void resetInWrongFlag(int id) {
		questionBankService.resetInWrongFlag(context, id);
	}
	
	public void addRightCount(int id) {
		questionBankService.addRightTime(context, id);
	}

	public void addWrongCount(int id) {
		questionBankService.addWrongTime(context, id);
		questionBankService.setInWrongFlag(context, id);
	}
	public void RemoveWrongCount(int id) {
		questionBankService.removeWrongTime(context, id);
		questionBankService.setBackInWrongFlag(context, id);
	}
	public String getAnswer(int id) {
		return questionBankService.getAnswer(context, id);
	}
	//获取正确选项的按钮
	public RadioButton getCorrectRadioButton(RadioGroup rg,
			HashMap<String, Integer> orderMap, String answer) {
		RadioButton returnRb = new RadioButton(context);
		switch (rg.getChildCount()) {
		case 2: {
			RadioButton rb_item1 = (RadioButton) rg.getChildAt(0);
			RadioButton rb_item2 = (RadioButton) rg.getChildAt(1);
			if (String
					.valueOf(
							orderMap.get(rb_item1
									.getText()
									.toString()
									.substring(
											0,
											rb_item1.getText().toString()
													.indexOf(".")))).equals(
							answer)) {
				returnRb = rb_item1;
			} else {
				returnRb = rb_item2;
			}
			break;
		}
		case 4: {
			RadioButton rb_item1 = (RadioButton) rg.getChildAt(0);
			RadioButton rb_item2 = (RadioButton) rg.getChildAt(1);
			RadioButton rb_item3 = (RadioButton) rg.getChildAt(2);
			RadioButton rb_item4 = (RadioButton) rg.getChildAt(3);
			if (String
					.valueOf(
							orderMap.get(rb_item1
									.getText()
									.toString()
									.substring(
											0,
											rb_item1.getText().toString()
													.indexOf(".")))).equals(
							answer)) {
				returnRb = rb_item1;
			} else if (String
					.valueOf(
							orderMap.get(rb_item2
									.getText()
									.toString()
									.substring(
											0,
											rb_item2.getText().toString()
													.indexOf(".")))).equals(
							answer)) {
				returnRb = rb_item2;
			} else if (String
					.valueOf(
							orderMap.get(rb_item3
									.getText()
									.toString()
									.substring(
											0,
											rb_item3.getText().toString()
													.indexOf(".")))).equals(
							answer)) {
				returnRb = rb_item3;
			} else {
				returnRb = rb_item4;
			}
			break;
		}
		default:
			break;
		}
		return returnRb;
	}
	//设置按钮组的状态
	public void setRadioButtonState(RadioGroup rg, boolean bl) {
		switch (rg.getChildCount()) {
		case 2: {
			RadioButton rb_item1 = (RadioButton) rg.getChildAt(0);
			RadioButton rb_item2 = (RadioButton) rg.getChildAt(1);
			rb_item1.setEnabled(bl);
			rb_item2.setEnabled(bl);
			 
			break;
		}
		case 4: {
			RadioButton rb_item1 = (RadioButton) rg.getChildAt(0);
			RadioButton rb_item2 = (RadioButton) rg.getChildAt(1);
			RadioButton rb_item3 = (RadioButton) rg.getChildAt(2);
			RadioButton rb_item4 = (RadioButton) rg.getChildAt(3);
			rb_item1.setEnabled(bl);
			rb_item2.setEnabled(bl);
			rb_item3.setEnabled(bl);
			rb_item4.setEnabled(bl);
			 
			break;
		}
		default:
			break;
		}
	}

	public void addTestScore(int totalScore, int rightCount, int wrongCount,
			int totalCount, String dateTime, String useTime) {
		examResultService.addTestScore(context, totalScore, rightCount,
				wrongCount, totalCount, dateTime, useTime);
	}

//	public ExamResultEntry getThisTestScore() {
//		return examResultService.getThisTestScore();
//	}

	public HashMap<String, Integer> getOrderMap(int topicType) {
		HashMap<String, Integer> orderMap = new HashMap<String, Integer>();
		ArrayList<Integer> tempList = new ArrayList<Integer>();
		if (topicType == TYPE_CHOICE) {
			tempList.add(1);
			tempList.add(2);
			tempList.add(3);
			//tempList.add(4);
			Random random = new Random();
			int size = tempList.size();
			int sizeNumber;
			int tempInt;
			int count = 1;
			while (size > 0) {
				sizeNumber = random.nextInt(size);
				tempInt = count + 64;
				orderMap.put(String.valueOf((char) tempInt),
						tempList.get(sizeNumber));
				tempList.remove(sizeNumber);
				size = tempList.size();
				count++;
			}
		} 
		 
		
		else {
			orderMap.put("A", 1);
			orderMap.put("B", 2);
		}

		return orderMap;
	}

	public String getItemValue(Integer tempInt) {
		return itemMap.get(tempInt);
	}

	public FragmentPagerAdapter getPagerAdapter(FragmentManager fm,
			TopicFragmentCallBacks topicFragmentCallBacks) {
		if (topicList == null) {
			switch (mode) {
			case MODE_SEQUENCE:
				topicList = sequentialSearch();
				break;
			case MODE_RANDOM:
				topicList = randomSearch();
				break;
			case MODE_CHAPTERS:
				//章节练习
				topicList=chapterSearch(subClass);
				break;
			case MODE_INTENSIFY:
				//未做题
				topicList=neverdoSearch();				 
				break;
			case MODE_PRACTICE_TEST:
				topicList = testSearch();
				break;
			case MODE_WRONG_TOPIC:
				topicList = errorBookSearch();
				break;
			case MODE_COLLECT:
				topicList = collectedSearch();
				break;
			default:
				break;
			}
			initDaoIdMap();
			selectedFlags = new boolean[topicList.size()];
		}
		if (answerShow) {
			if (answerShowFpa == null) {
				answerShowFpa = new TopicPagerAdapter(fm, this,
						topicFragmentCallBacks);
			}
			return answerShowFpa;
		} else {
			if (answerHideFpa == null) {
				answerHideFpa = new TopicPagerAdapter(fm, this,
						topicFragmentCallBacks);
			}
			return answerHideFpa;
		}
	}

	public void setSelectedFlag(int position, boolean flag) {
		selectedFlags[position] = flag;
	}

	public void setOrderMap(int position, HashMap<String, Integer> orderMap) {
		totalTopicItemsMap.put(position, orderMap);
	}

	public HashMap<String, Integer> getSavedOrderMap(int position) {
		return totalTopicItemsMap.get(position);
	}

	public boolean getSelectedFlag(int position) {
		return selectedFlags[position];
	}

	public void setSelecetedChoice(int position, String selectedChoice) {
		selecetedChoice.put(position, selectedChoice);
	}

	public String getSelecetedChoice(int position) {
		return selecetedChoice.get(position);
	}

	public void dataSave(int currentItem) {
		switch (mode) {
		case MODE_SEQUENCE:
		case MODE_RANDOM:
			SharedPreferencesUtil.write(context, "topic", mode + "",
					currentItem);
			break;
		case MODE_COLLECT:
		case MODE_WRONG_TOPIC:
			SharedPreferencesUtil.write(context, "topic",
					mode + "-" + subClass, 0);
			break;
		case MODE_CHAPTERS:
		case MODE_INTENSIFY:
			SharedPreferencesUtil.write(context, "topic",
					mode + "-" + subClass, currentItem);
			break;
		default:
			break;
		}
	}

	public int dataLoad() {
		int initItem=0;
		switch (mode) {
		case MODE_SEQUENCE:
		case MODE_RANDOM:	
			initItem=SharedPreferencesUtil.read(context, "topic", mode + "", 0);
			break;
		case MODE_CHAPTERS:
		case MODE_INTENSIFY:
		case MODE_COLLECT:
		case MODE_WRONG_TOPIC:
			initItem=SharedPreferencesUtil.read(context, "topic", mode + "-"
					+ subClass, 0);
			break;
		default:
			break;
		}
		return initItem;
	}
}
