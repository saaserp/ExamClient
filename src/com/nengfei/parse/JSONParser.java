package com.nengfei.parse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONParser {
	String jsonStr;
	public JSONParser(String jsonStr) {
		// TODO Auto-generated constructor stub
		this.jsonStr=jsonStr;
	}
	 
	public List<Map<String,String>>parse(){
		List<Map<String, String>> list = new ArrayList<Map<String,String>>() ;
		try {
			///
			//
			if(jsonStr==null||jsonStr.equals(""))
				return list;
			JSONArray jsa=new JSONArray(jsonStr);
			list = new ArrayList<Map<String,String>>();
			String key;
			 for(int i=0;i<jsa.length();i++){
				 JSONObject j=jsa.getJSONObject(i);
				 Map<String,String> map=new HashMap<String,String>();
				 Iterator it=j.keys();
				 while(it.hasNext()){
					 key=it.next().toString();
					 map.put(key, j.getString(key));
				 }
				 list.add(map);
			 }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return list;
	}
}
