package com.wencai.model;

import java.util.ArrayList;
import java.util.Map;

import com.wencai.util.BaseDao;

import android.content.Context;

/**
 * BaseDao Simplified
 * 
 * @author sxenon
 */
public class SimplifyDao extends BaseDao {

	// EntryList Simplify
	protected ArrayList<Map<String, Object>> getMapList(Context context,
			String table) {
		return getMapList(context, table, null, false, null, null, null, null);
	}

	protected ArrayList<Map<String, Object>> getMapList(Context context,
			String table, String whereClause, String limit) {
		return getMapList(context, table, whereClause, false, null, null, null,
				limit);
	}

	protected ArrayList<Map<String, Object>> getMapList(Context context,
			String table, String whereClause) {
		return getMapList(context, table, whereClause, false, null, null, null,
				null);
	}
	protected ArrayList<Map<String,Object>>getMapList(Context context,String table,String[] cl,String whereClause){
		return getMapList(context, table, cl,whereClause, false, null, null, null,
				null);
	}

	// Map,List Simplify
	protected ArrayList<Integer> getIntegerList(Context context, String table,
			String showColumn) {
		return getIntegerList(context, table, showColumn, false, null, null,
				null, null, null);
	}

	protected ArrayList<Integer> getIntegerList(Context context, String table,
			String showColumn, String whereClause) {
		return getIntegerList(context, table, showColumn, false, whereClause,
				null, null, null, null);
	}

	protected ArrayList<Float> getFloatList(Context context, String table,
			String showColumn) {
		return getFloatList(context, table, showColumn, false, null, null,
				null, null, null);
	}

	protected ArrayList<Float> getFloatList(Context context, String table,
			String showColumn, String whereClause) {
		return getFloatList(context, table, showColumn, false, whereClause,
				null, null, null, null);
	}

	protected ArrayList<String> getStringList(Context context, String table,
			String showColumn) {
		return getStringList(context, table, showColumn, false, null, null,
				null, null, null);
	}

	protected ArrayList<String> getStringList(Context context, String table,
			String showColumn, String whereClause) {
		return getStringList(context, table, showColumn, false, whereClause,
				null, null, null, null);
	}

	protected ArrayList<byte[]> getBlobList(Context context, String table,
			String showColumn) {
		return getBlobList(context, table, showColumn, false, null, null, null,
				null, null);
	}

	protected ArrayList<byte[]> getBlobList(Context context, String table,
			String showColumn, String whereClause) {
		return getBlobList(context, table, showColumn, false, whereClause,
				null, null, null, null);
	}

	protected Map<String, Object> getMap(Context context, String table,
			String whereClause) {
		return getMap(context, table, whereClause, false, null, null, null,
				null);
	}
	protected Map<String, Object> getMap(Context context, String table,
			String whereClause,String orderBy,String limit) {
		return getMap(context, table, whereClause, false, null, null, orderBy, limit);
	}
}
