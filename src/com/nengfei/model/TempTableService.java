package com.nengfei.model;

import android.content.ContentValues;
import android.content.Context;

public class TempTableService extends CommonEntryDao {
	public boolean checkTableExist(Context context, String table) {
		return super.checkTableExist(context, table);
	}
	public void createTable(Context context, String table) {
		String value="tempId  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,_id  INTEGER NOT NULL";
		super.createTable(context, table, value);
	}
	public void dropTable(Context context, String table) {
		super.dropTable(context, table);
	}
	public boolean add(Context context, String table, ContentValues values) {
		return super.add(context, table, values);
	}
}
