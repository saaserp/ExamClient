package com.wencai.net;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


public class MySocketClient {

    public static String STATE_TIME_OUT = "网络连接超时";
    public static String STATE_SERVER_BUSY = "服务器繁忙";
    public static String STATE_SERVER_ERROR = "服务器内部错误";
    static MySocketClient qs = null;
    Socket socket = null;
    PrintWriter printWriter;
    BufferedReader bufferedReader;

    private MySocketClient() {
        try {
           
            String ip = "192.168.1.202";
            Log.i("ipp", ip);
            socket = new Socket(ip, 6000);

            socket.setSoTimeout(14000);


        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static MySocketClient getInstance() {
//				if(qs==null){
//					qs=new QufeiSocketClient();
//				}
        qs = new MySocketClient();
        return qs;

    }

    public String send(String cmd, String str) {
        
            return send(">>" + cmd + "<<" + str);
        
    }
    public String send(String cmd,List<Map<String,String>> list){
    	JSONArray jsa=new JSONArray();
    	
    	 for(Map<String,String> map:list){
    		 JSONObject jo=new JSONObject();
    		 for(String key:map.keySet()){
    			 try {
					jo.put(key, map.get(key));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			 
    		 }
    		 jsa.put(jo);
    	 }
    	 return send(cmd,jsa);
    }
    public String send(String cmd, JSONArray str) {
        {
            return send(">>" + cmd + "<<" + str.toString());
        }
    }

    public String send(String cmd, Map<String, String> map) {
        Set<String> keys = map.keySet();
        JSONArray ja = new JSONArray();
        JSONObject jo = new JSONObject();

        for (String key : keys) {
            try {
                jo.put(key, map.get(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ja.put(jo);
        return send(cmd, ja);

    }

    public String send(String str) {
    	str=EDcoder.getBASE64(str);
        String result = null;
        try {

            printWriter = new PrintWriter(socket.getOutputStream(), true);
            //str+="\n";

            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
            printWriter.println(str.toCharArray());
            printWriter.flush();
            result = bufferedReader.readLine();
            result=EDcoder.getFromBASE64(result);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
         


        return result;

    }

    public void close() {
        try {

            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
            printWriter.println("bye".toCharArray());
            printWriter.flush();
            printWriter.close();
            bufferedReader.close();
            socket.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


}
