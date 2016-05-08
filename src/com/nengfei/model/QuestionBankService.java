package com.nengfei.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;

@SuppressLint("UseSparseArrays")
public class QuestionBankService extends CommonEntryDao {
	private HashMap<Integer, Integer> examMap;

	public int getAllCount(Context context) {
		//String whereClause = "type<=2";
		return super.getEntryList(context, null).size();
	}
	
	public ArrayList<Map<String, Object>> sequentialSearch(Context context) {
		String whereClause = "type<=2";
		return super.getEntryList(context, whereClause);
	}

	public ArrayList<Map<String, Object>> errorBookSearch(Context context) {
		String whereClause = "inWrongFlag=1";
		examMap = new HashMap<Integer, Integer>();
		ArrayList<Map<String, Object>> returnList = super.getEntryList(context,
				whereClause);
		int count = 1;
		for (Map<String, Object> map : returnList) {
			examMap.put(count, (Integer) map.get("_id"));
			count++;
		}
		return returnList;
	}
	public ArrayList<Map<String,Object>> neverdoSearch(Context context){
		String whereCase="rightTime=0 and wrongTime=0";		
		examMap=new HashMap<Integer,Integer>();
		ArrayList<Map<String,Object>> returnList=super.getEntryList(context,whereCase);
		int count=1;
		for(Map<String,Object> map:returnList){
			examMap.put(count, (Integer) map.get("_id"));
			count++;
		}
		return returnList;
		
	}
	public int getShouchangCount(Context context){
		return getCollectedEntryList(context).size();
	}
	public int getWrongCount(Context context){
		 
		return errorBookSearch(context).size();
	}
	public int getNeverDoCount(Context context){
		 
		return neverdoSearch(context).size();
	}

	public ArrayList<Map<String, Object>> collectedSearch(Context context) {
		String whereClause = "collectedFlag=1";
		examMap = new HashMap<Integer, Integer>();
		ArrayList<Map<String, Object>> returnList = super.getEntryList(context,
				whereClause);
		int count = 1;
		for (Map<String, Object> map : returnList) {
			examMap.put(count, (Integer) map.get("_id"));
			count++;
		}
		return returnList;
	}

	public ArrayList<Map<String, Object>> randomSearch(Context context) {
		String whereClause = "type<=2";
		examMap = new HashMap<Integer, Integer>();
		ArrayList<Map<String, Object>> tempList = super.getEntryList(context,
				whereClause);
		ArrayList<Map<String, Object>> backList = new ArrayList<Map<String, Object>>();
		Random random = new Random();
		int size = tempList.size();
		int sizeNumber;
		int topicId;
		int count = 1;
		while (size > 0) {
			sizeNumber = random.nextInt(size);
			topicId = (Integer) tempList.get(sizeNumber).get("_id");
			examMap.put(count, topicId);
			backList.add(tempList.get(sizeNumber));
			tempList.remove(sizeNumber);
			size = tempList.size();
			count++;
		}
		return backList;
	}

	public ArrayList<Map<String, Object>> testSearch(Context context) {
		String whereClause = "type<=2";
		examMap = new HashMap<Integer, Integer>();
		ArrayList<Map<String, Object>> tempList = super.getEntryList(context,
				whereClause);
		ArrayList<Map<String, Object>> backList = new ArrayList<Map<String, Object>>();
		Random random = new Random();
		int size = tempList.size();
		System.out.println("SIZE:" + size);
		int sizeNumber;
		int topicId;
		int count = 1;
		while (size > 0) {
			sizeNumber = random.nextInt(size);
			topicId = (Integer) tempList.get(sizeNumber).get("_id");
			examMap.put(count, topicId);
			backList.add(tempList.get(sizeNumber));
			tempList.remove(sizeNumber);
			size = tempList.size();
			if (count == 100) {
				break;
			}
			count++;
		}
		return backList;
	}

	// 非順序取题用
	public HashMap<Integer, Integer> getExamMap() {
		return examMap;
	}

	public void addRightTime(Context context, int id) {
		ContentValues setValues = new ContentValues();
		int useId = (Integer) super.getEntry(context, "_id=" + id).get(
				"rightTime");
		setValues.put("rightTime", useId + 1);
		useId = (Integer) super.getEntry(context, "_id=" + id).get(
				"answeredTime");
		setValues.put("answeredTime", useId + 1);
		String whereClause = "_id=" + id;
		super.update(context, setValues, whereClause);
	}

	public void addWrongTime(Context context, int id) {
		ContentValues setValues = new ContentValues();
		int useId = (Integer) super.getEntry(context, "_id=" + id).get(
				"wrongTime");
		setValues.put("wrongTime", useId + 1);
		useId = (Integer) super.getEntry(context, "_id=" + id).get(
				"answeredTime");
		setValues.put("answeredTime", useId + 1);
		String whereClause = "_id=" + id;
		super.update(context, setValues, whereClause);
	}
	public void removeWrongTime(Context context, int id) {
		ContentValues setValues = new ContentValues();
		int useId = (Integer) super.getEntry(context, "_id=" + id).get(
				"wrongTime");
		setValues.put("wrongTime", useId -1);
		useId = (Integer) super.getEntry(context, "_id=" + id).get(
				"answeredTime");
		setValues.put("answeredTime", useId -1);
		String whereClause = "_id=" + id;
		super.update(context, setValues, whereClause);
	} 
	public String getAnswer(Context context, int id) {
		return (String) super.getEntry(context, "_id=" + id).get("answer");
	}

	public int getCollectedFlag(Context context, int id) {
		int flag=0;
		try{
		flag=(Integer) super.getEntry(context, "_id=" + id).get(
				"collectedFlag");
		}catch(NullPointerException e){
			flag=0;
		}
		return flag;
	}

	public void setCollectedFlag(Context context, int id) {
		ContentValues setValues = new ContentValues();
		setValues.put("collectedFlag", 1);
		String whereClause = "_id=" + id;
		super.update(context, setValues, whereClause);
	}

	public void resetCollectedFlag(Context context, int id) {
		ContentValues setValues = new ContentValues();
		setValues.put("collectedFlag", 0);
		String whereClause = "_id=" + id;
		super.update(context, setValues, whereClause);
	}

	public void setInWrongFlag(Context context, int id) {
		ContentValues setValues = new ContentValues();
		setValues.put("inWrongFlag", 1);
		String whereClause = "_id=" + id;
		super.update(context, setValues, whereClause);
	}
	public void setBackInWrongFlag(Context context, int id) {
		ContentValues setValues = new ContentValues();
		setValues.put("inWrongFlag", 0);
		String whereClause = "_id=" + id;
		super.update(context, setValues, whereClause);
	}
	public void resetInWrongFlag(Context context, int id) {
		ContentValues setValues = new ContentValues();
		setValues.put("inWrongFlag", 0);
		String whereClause = "_id=" + id;
		super.update(context, setValues, whereClause);
	}

	public int getInWrongFlag(Context context, int id) {
		return (Integer) super.getEntry(context, "_id=" + id).get(
				"inWrongFlag");
	}
	
	public int getUndoQuestionNum(Context context) {
		String whereClause = "answeredTime=0";
		return super.getIntegerList(context, "count(_id)", whereClause).get(0);
	}

	public int getRightAlwaysQuestionNum(Context context) {
		String whereClause = "rightTime>0 AND wrongTime=0";
		return super.getIntegerList(context, "count(_id)", whereClause).get(0);
	}

	public int getWrongAlwaysQuestionNum(Context context) {
		String whereClause = "wrongTime>0 AND rightTime=0";
		return super.getIntegerList(context, "count(_id)", whereClause).get(0);
	}

	public int getWrongOftenQuestionNum(Context context) {
		String whereClause = "wrongTime>rightTime AND rightTime>0";
		return super.getIntegerList(context, "count(_id)", whereClause).get(0);
	}

	public int getRightOftenQuestionNum(Context context) {
		String whereClause = "rightTime>=wrongTime AND wrongTime>0";
		return super.getIntegerList(context, "count(_id)", whereClause).get(0);
	}

	public ArrayList<Map<String, Object>> getErrorEntryList(Context context) {
		String whereClause = "inWrongFlag=1";
		return super.getEntryList(context, whereClause);
	}
	
	public ArrayList<Map<String, Object>> getCollectedEntryList(Context context) {
		String whereClause = "collectedFlag=1";
		return super.getEntryList(context, whereClause);
	}

	public ArrayList<Map<String, Object>> getClassicsEntryList(Context context) {
		String whereClause = "type=3";
		return super.getEntryList(context, whereClause);
	}
	
	public boolean delete(Context context, int id) {
		String whereClause = "_id=" + id;
		return super.delete(context, whereClause);
	}

	public Map<String, Object> getEntry(Context context, String id) {
		String whereValues = "_id=" + id;
		return super.getEntry(context, whereValues);
	}
	
	public ArrayList<Map<String,Object>>getChapter(Context context,int chapterId){
		String whereCase="classId="+chapterId;
		
		examMap = new HashMap<Integer, Integer>();
		ArrayList<Map<String, Object>> tempList = super.getEntryList(context, whereCase);
		ArrayList<Map<String, Object>> backList = new ArrayList<Map<String, Object>>();
		Random random = new Random();
		int size = tempList.size();
		System.out.println("SIZE:" + size);
		int sizeNumber;
		int topicId;
		int count = 1;
		while (size > 0) {
			sizeNumber = random.nextInt(size);
			topicId = (Integer) tempList.get(sizeNumber).get("_id");
			examMap.put(count, topicId);
			backList.add(tempList.get(sizeNumber));
			tempList.remove(sizeNumber);
			size = tempList.size();
			if (count == 50) {
				break;
			}
			count++;
		}
		return backList;
		
		
		
		
		 
	}
}
