package com.nengfei.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;

public class ExamResultService extends CommonEntryDao {
	ExamResultEntry entry;

	public int getBestScoreTimes(Context context) {
		String whereClause = "totalScore=100";
		return super.getIntegerList(context, "count(_id)", whereClause).get(0);
	}

	public int getBetterScoreTimes(Context context) {
		String whereClause = "totalScore<100 AND totalScore>=80";
		return super.getIntegerList(context, "count(_id)", whereClause).get(0);
	}

	public int getJustSoSoScoreTimes(Context context) {
		String whereClause = "totalScore<80 AND totalScore>=60";
		return super.getIntegerList(context, "count(_id)", whereClause).get(0);
	}

	public int getBadScoreTimes(Context context) {
		String whereClause = "totalScore<60 AND totalScore>=40";
		return super.getIntegerList(context, "count(_id)", whereClause).get(0);
	}

	public int getWorseScoreTimes(Context context) {
		String whereClause = "totalScore<40 AND totalScore>=20";
		return super.getIntegerList(context, "count(_id)", whereClause).get(0);
	}

	public int getWorstScoreTimes(Context context) {
		String whereClause = "totalScore<20";
		return super.getIntegerList(context, "count(_id)", whereClause).get(0);
	}

	public int getTestTimes(Context context) {
		return super.getEntryList(context).size();
	}

	public void addTestScore(Context context, int totalScore, int rightCount,
			int wrongCount, int totalCount, String dateTime, String useTime) {
		entry = new ExamResultEntry(totalScore, rightCount, wrongCount,
				totalCount, dateTime, useTime);
		super.add(context, entry.getContentValueByEntry());
	}
	public ExamResultEntry getThisTestScore(Context context) {
		HashMap<String, Object> map = (HashMap<String, Object>) super.getMap(
				context, "ExamResult", null, "_id Desc", "1");
		entry = new ExamResultEntry((Integer) map.get("_id"),
				(Integer) map.get("totalScore"),
				(Integer) map.get("rightCount"),
				(Integer) map.get("wrongCount"),
				(Integer) map.get("totalCount"), (String) map.get("dateTime"),
				(String) map.get("useTime"));
		return entry;
	}

	public ArrayList<Map<String, Object>> getRecordEntryList(Context context) {
		return super.getEntryList(context);
	}

	public boolean delete(Context context, int id) {
		String whereClause = "_id=" + id;
		return super.delete(context, whereClause);
	}
}
