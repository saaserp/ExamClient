package com.wencai.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.wencai.login.LoginActivity;
import com.wencai.util.DBUtil;

import android.content.ContentValues;
import android.content.Context;

public class DataSyncService extends SimplifyDao {
	Context context;
	String tableName;

	public DataSyncService(Context context) {
		this.context = context;
	}

	DataSyncService(Context context, String tableName) {
		this.context = context;
		this.tableName = tableName;
	}

	public boolean removeAllRecode() {
		ContentValues c1 = new ContentValues();
		c1.put("answeredTime", 0);
		c1.put("rightTime", 0);
		c1.put("wrongTime", 0);
		c1.put("collectedFlag",0);
		c1.put("inWrongFlag", 0);
		return update(context, "QuestionBank", c1, null) && delete(context, "ExamResult", null);

	}

	public List<Map<String, Object>> getAllFromQuestion() {
		return getAllFromTable("QuestionBank");
	}

	public List<Map<String, Object>> getAllFromExamResult() {
		return getAllFromTable("ExamResult");
	}

	public boolean setAllToExamResult(List<Map<String, String>> list) {
		return setAllToTable("ExamResult", list);
	}

	public boolean setAllToQuestion(List<Map<String, String>> list) {
		return setAllToTable("QuestionBank", list);
	}

	private List<Map<String, Object>> getAllFromTable(String tableName) {
		// 获取所有的答题信息，以便后期上传同步
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("tableName", tableName);
		map.put("uid", LoginActivity.uid);
		 map.put("dbName", DBUtil.dbName);
		list.add(map);
		List<Map<String, Object>> ls2;
		if (tableName.equals("QuestionBank"))
			ls2 = super.getMapList(context, tableName,
					new String[] { "_id","answeredTime", "rightTime", "wrongTime", "collectedFlag", "inWrongFlag" },
					null);
		else
			ls2 = super.getMapList(context, tableName, 
					new String[] { "_id", "totalScore", "dateTime", "useTime","totalCount", "wrongCount", "rightCount" }, null);
		for (int i = 0; i < ls2.size(); i++)
			list.add(ls2.get(i));
		return list;
	}

	private boolean setAllToTable(String tableName, List<Map<String, String>> list) {

		List<Map<String, Object>> tempList = getAllFromTable(tableName);
		String _id;
		boolean b = false;
		boolean b1 = true;
		for (int i = 0; i < list.size(); i++) {
			_id = list.get(i).get("_id");

			Set<String> keys = list.get(i).keySet();


			ContentValues c = new ContentValues();
			for (String key : keys) {

				c.put(key, list.get(i).get(key));
			}
			if (tableName.equals("ExamResult")) {

				b = super.add(context, tableName, c);

			} else {

				b = super.update(context, tableName, c, "_id=" + _id);
			}
			if (b == false) {
				break;
			}
		}

		if (b == false) {
			for(int i=0;i<tempList.size();i++){
				Set<String> keys2 = tempList.get(i).keySet();
				for (String key : keys2) {
					ContentValues c = new ContentValues();
					c.put(key, list.get(i).get(key));
					b1 = super.update(context, tableName, c, "_id=" + list.get(i).get("_id"));
					if (b1 == false) {
						break;
					}
				}
			}
		}
			if (b1 == false)
				return false;
			return true;
		}

	}
