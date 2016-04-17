package com.wencai.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;
import android.util.Log;

public class BaseDao  {
	private SQLiteDatabase db;
	 
	private SQLiteDatabase openOrGetDB(Context context,String dbName)
	{
		return DBHelper.openOrGetDB(context,dbName);
	}

	private Cursor getCursorByQuery(Context context , boolean distinct, String table, String[] showColumns, String whereClause, String groupBy, String having, String orderBy, String limit)
	{
		this.db = openOrGetDB(context,DBUtil.dbName);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		if (distinct) {
			sql.append(" DISTINCT ");
		}

		if (showColumns == null)
			sql.append(" * ");
		else {
			appendColumns(sql, showColumns);
		}
		sql.append(" FROM ");
		sql.append(table);

		appendWhereClause(sql, whereClause);

		if (groupBy != null) {
			sql.append(" GROUP BY ");
			sql.append(groupBy);
			if (having != null) {
				sql.append(" HAVING ");
				sql.append(having);
			}
		}

		if (orderBy != null) {
			sql.append(" ORDER BY ");
			sql.append(orderBy);
		}

		if (limit != null) {
			sql.append(" LIMIT ");
			sql.append(limit);
		}

		sql.append(";");
		
		return this.db.rawQuery(sql.toString(), null);
	}

	private ArrayList<Map<String, Object>> getMapListByCursor(Cursor cursor)
	{
		ArrayList<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		String[] columnNames = cursor.getColumnNames();
		while (cursor.moveToNext()) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 0; i < columnNames.length; i++) {
				switch (cursor.getType(i)) {
				case 4:
					map.put(columnNames[i], cursor.getBlob(i));
					break;
				case 2:
					map.put(columnNames[i], Float.valueOf(cursor.getFloat(i)));
					break;
				case 1:
					map.put(columnNames[i], Integer.valueOf(cursor.getInt(i)));
					break;
				case 3:
					map.put(columnNames[i], cursor.getString(i));
					break;
				case 0:
					map.put(columnNames[i], null);
					break;
				default:
					Log.e("getEntryListByCursor", "unKnown type");
				}
			}

			mapList.add(map);
		}
		cursor.close();
		return mapList;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getMapByCursor(Cursor cursor)
	{
		ArrayList<?> mapList = getMapListByCursor(cursor);
		if (mapList.isEmpty()) {
			return null;
		}
		return (Map<String, Object>)mapList.get(0);
	}

	private ArrayList<String> getStringListByCursor(Cursor cursor)
	{
		ArrayList<String> stringList = new ArrayList<String>();
		while (cursor.moveToNext()) {
			stringList.add(cursor.getString(0));
		}
		cursor.close();
		return stringList;
	}

	private ArrayList<Integer> getIntegerListByCursor(Cursor cursor) {
		ArrayList<Integer> integerList = new ArrayList<Integer>();
		while (cursor.moveToNext()) {
			integerList.add(Integer.valueOf(cursor.getInt(0)));
		}
		cursor.close();
		return integerList;
	}

	private ArrayList<Float> getFloatListByCursor(Cursor cursor) {
		ArrayList<Float> floatList = new ArrayList<Float>();
		while (cursor.moveToNext()) {
			floatList.add(Float.valueOf(cursor.getFloat(0)));
		}
		cursor.close();
		return floatList;
	}

	private ArrayList<byte[]> getBlobListByCursor(Cursor cursor) {
		ArrayList<byte[]> blobList = new ArrayList<byte[]>();
		while (cursor.moveToNext()) {
			blobList.add(cursor.getBlob(0));
		}
		cursor.close();
		return blobList;
	}

	private Integer getIntegerByCursor(Cursor cursor)
	{
		ArrayList<?> integerList = getIntegerListByCursor(cursor);
		if (integerList.isEmpty()) {
			return Integer.valueOf(0);
		}
		return (Integer)integerList.get(0);
	}

	private Float getFloatByCursor(Cursor cursor)
	{
		ArrayList<?> floatList = getFloatListByCursor(cursor);
		if (floatList.isEmpty()) {
			return Float.valueOf(0.0F);
		}
		return (Float)floatList.get(0);
	}

	private String getStringByCursor(Cursor cursor)
	{
		ArrayList<?> stringList = getStringListByCursor(cursor);
		if (stringList.isEmpty()) {
			return null;
		}
		return (String)stringList.get(0);
	}

	private byte[] getBlobByCursor(Cursor cursor)
	{
		ArrayList<?> blobList = getBlobListByCursor(cursor);
		if (blobList.isEmpty()) {
			return null;
		}
		return (byte[])blobList.get(0);
	}

	protected ArrayList<Map<String, Object>> getMapList(Context context, String table, String whereClause, boolean distinct, String groupBy, String having, String orderBy, String limit)
	{
		Cursor cursor = getCursorByQuery(context, distinct, table, null, 
				whereClause, groupBy, having, orderBy, limit);
		return getMapListByCursor(cursor);
	}
	protected ArrayList<Map<String, Object>> getMapList(Context context, String table,String []names ,String whereClause, boolean distinct, String groupBy, String having, String orderBy, String limit)
	{
		Cursor cursor = getCursorByQuery(context, distinct, table, names, 
				whereClause, groupBy, having, orderBy, limit);
		return getMapListByCursor(cursor);
	}
	protected Map<String, Object> getMap(Context context, String table, String whereClause, boolean distinct, String groupBy, String having, String orderBy, String limit)
	{
		Cursor cursor = getCursorByQuery(context, distinct, table, null, 
				whereClause, groupBy, having, orderBy, limit);
		return getMapByCursor(cursor);
	}

	protected ArrayList<String> getStringList(Context context, String table, String showColumn, boolean distinct, String whereClause, String groupBy, String having, String limit, String orderBy)
	{
		String[] showColumns = { showColumn };
		Cursor cursor = getCursorByQuery(context, distinct, table, showColumns, 
				whereClause, groupBy, having, orderBy, limit);
		return getStringListByCursor(cursor);
	}

	protected ArrayList<Integer> getIntegerList(Context context, String table, String showColumn, boolean distinct, String whereClause, String groupBy, String having, String limit, String orderBy)
	{
		String[] showColumns = { showColumn };
		Cursor cursor = getCursorByQuery(context, distinct, table, showColumns, 
				whereClause, groupBy, having, orderBy, limit);
		return getIntegerListByCursor(cursor);
	}

	protected ArrayList<Float> getFloatList(Context context, String table, String showColumn, boolean distinct, String whereClause, String groupBy, String having, String limit, String orderBy)
	{
		String[] showColumns = { showColumn };
		Cursor cursor = getCursorByQuery(context, distinct, table, showColumns, 
				whereClause, groupBy, having, orderBy, limit);
		return getFloatListByCursor(cursor);
	}

	protected ArrayList<byte[]> getBlobList(Context context, String table, String showColumn, boolean distinct, String whereClause, String groupBy, String having, String limit, String orderBy)
	{
		String[] showColumns = { showColumn };
		Cursor cursor = getCursorByQuery(context, distinct, table, showColumns, 
				whereClause, groupBy, having, orderBy, limit);
		return getBlobListByCursor(cursor);
	}

	protected Integer getInteger(Context context, String table, String showColumn, boolean distinct, String whereClause, String groupBy, String having, String limit, String orderBy)
	{
		String[] showColumns = { showColumn };
		Cursor cursor = getCursorByQuery(context, distinct, table, showColumns, 
				whereClause, groupBy, having, orderBy, limit);
		return getIntegerByCursor(cursor);
	}

	protected String getString(Context context, String table, String showColumn, boolean distinct, String whereClause, String groupBy, String having, String limit, String orderBy)
	{
		String[] showColumns = { showColumn };
		Cursor cursor = getCursorByQuery(context, distinct, table, showColumns, 
				whereClause, groupBy, having, orderBy, limit);
		return getStringByCursor(cursor);
	}

	protected Float getFloat(Context context, String table, String showColumn, boolean distinct, String whereClause, String groupBy, String having, String limit, String orderBy)
	{
		String[] showColumns = { showColumn };
		Cursor cursor = getCursorByQuery(context, distinct, table, showColumns, 
				whereClause, groupBy, having, orderBy, limit);
		return getFloatByCursor(cursor);
	}

	protected byte[] getBlob(Context context, String table, String showColumn, boolean distinct, String whereClause, String groupBy, String having, String limit, String orderBy)
	{
		String[] showColumns = { showColumn };
		Cursor cursor = getCursorByQuery(context, distinct, table, showColumns, 
				whereClause, groupBy, having, orderBy, limit);
		return getBlobByCursor(cursor);
	}

	protected boolean add(Context context, String table, ContentValues values)
	{
		this.db = openOrGetDB(context,DBUtil.dbName);
		if (this.db.insert(table, null, values) < 0L) {
			return false;
		}
		return true;
	}

	protected boolean update(Context context, String table, ContentValues setValues, String whereClause)
	{
		this.db = openOrGetDB(context,DBUtil.dbName);
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE " + table + " SET ");
		appendSetValues(sql, setValues);
		appendWhereClause(sql, whereClause);
		sql.append(";");
		
		 
		 
		return db.update(table,setValues,whereClause, null)==0?false:true;
			 
		 
	 
	}

	protected boolean delete(Context context, String table, String whereClause)
	{
		this.db = openOrGetDB(context,DBUtil.dbName);
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM " + table);
		appendWhereClause(sql, whereClause);
		sql.append(";");
		try {
			this.db.execSQL(sql.toString());
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} 
	}

	protected void createTable(Context context, String table, String value)
	{
		this.db = openOrGetDB(context,DBUtil.dbName);
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE " + table + "(" + value + ");");
		this.db.execSQL(sql.toString());
	}

	protected boolean checkTableExist(Context context, String table) {
		this.db = openOrGetDB(context,DBUtil.dbName);
		String sql = "SELECT count(*) FROM sqlite_master WHERE type='table' AND name='" + 
				table + "';";
		Cursor cursor = this.db.rawQuery(sql, null);
		if (cursor.getInt(cursor.getColumnIndex("count(*)")) == 1) {
			return true;
		}
		return false;
	}

	protected void dropTable(Context context, String table)
	{
		this.db = openOrGetDB(context,DBUtil.dbName);
		String sql = "DROP TABLE " + table + ";";
		this.db.execSQL(sql);
	}

	private void appendColumns(StringBuilder s, String[] columns) {
		int n = columns.length;

		for (int i = 0; i < n; i++) {
			String column = columns[i];

			if (column != null) {
				if (i > 0) {
					s.append(", ");
				}
				s.append(column);
			}
		}
		s.append(' ');
	}

	private void appendWhereClause(StringBuilder s, String whereClause) {
		if (!TextUtils.isEmpty(whereClause)) {
			s.append(" WHERE ");
			s.append(whereClause);
		}
	}

	private void appendSetValues(StringBuilder s, ContentValues setValues) {
		for (String colName : setValues.keySet()) {
			s.append(colName + "=");
			Object value = setValues.get(colName);
			if ((value instanceof String))
				s.append("'" + value + "'");
			else {
				s.append(value);
			}
			s.append(",");
		}

		s.deleteCharAt(s.length() - 1);
	}

	protected String getWhereClause(ArrayList<String> whereList) {
		StringBuilder whereClause = new StringBuilder();
		whereClause.append("(");
		for (String subWhere : whereList) {
			whereClause.append(subWhere);
			whereClause.append(" ");
		}
		whereClause.append(") ");
		return whereClause.toString();
	}
}