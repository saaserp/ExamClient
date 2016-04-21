package com.nengfei.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by root on 16-1-6.
 */
public class Tools {
    public static Map<String, String> JArrayToMap(String jsonArray, int postion) {
        Map<String, String> map = new HashMap<String,String>();
        try {
            JSONArray jsa = new JSONArray(jsonArray);
            JSONObject jo = jsa.getJSONObject(postion);
            Iterator<String> keys = jo.keys();
            String key;
            while (keys.hasNext()) {
                key = keys.next();

                map.put(key, jo.getString(key));


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static Map<String, String> JArrayToMap(String jsonArray) {
        Map<String, String> map = new HashMap<String,String>();
        try {
            JSONArray jsa = new JSONArray(jsonArray);
            JSONObject jo = jsa.getJSONObject(0);
            Iterator<String> keys = jo.keys();
            String key;
            while (keys.hasNext()) {
                key = keys.next();

                map.put(key, jo.getString(key));


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static List<Map<String, String>> JArrayToMaps(String jsonArray) {
        Map<String, String> map = null;
        List<Map<String, String>> list;
        list = new ArrayList<Map<String, String>>();
        try {
            JSONArray jsa = new JSONArray(jsonArray);
            JSONObject jo = new JSONObject();
            for (int i = 0; i < jsa.length(); i++) {
                jo = jsa.getJSONObject(i);
                Iterator<String> keys = jo.keys();
                String key;
                map = new HashMap<String, String>();
                while (keys.hasNext()) {
                    key = keys.next();

                    map.put(key, jo.getString(key));


                }
                list.add(map);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }
    public static boolean isMobileNO(String mobiles) {
    	if(mobiles!=null){
    	Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
    	Matcher m = p.matcher(mobiles);
    	return m.matches();
    	
    	}else
    		return false;
    }
}
