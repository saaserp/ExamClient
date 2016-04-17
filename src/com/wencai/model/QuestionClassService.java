package com.wencai.model;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;

public class QuestionClassService extends CommonEntryDao {
	public ArrayList<Map<String, Object>> getClassicsEntryList(Context context) {
		 
		return super.getEntryList(context);
	}
}
