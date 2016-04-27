package com.nengfei.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class NetWorkUtil {
	// IP地址
	private final static int REPONSE_OK = 200;
	// http://www.cmyip.com/
	// http://iframe.ip138.com/ic.asp //这个获取IP比较方便
	private final static String GET_IP_FROM = "http://iframe.ip138.com/ic.asp";
	private static HttpGet get = null; // 用get方式联网
	private static HttpResponse response = null; // 等待应答
	private static int TIME_OUT = 5000; // 由网络状况决定
	public final static String DEFAULT_IP="127.0.0.1"; //默认IP

	public static String GetSavedNetIp(Context context) {
		return SharedPreferencesUtil
				.read(context, "Location","IP", DEFAULT_IP);
	}

	/**
	 * 描述：截取字符串
	 * */
	private final static String splitStr(String _str, String start, String end) {
		int startIndex = _str.indexOf(start);
		int endIndex = _str.indexOf(end);
		return _str.substring(startIndex + 1, endIndex);
	}

	public static void getNetIpFromWeb(final Context context) {

		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
		HttpConnectionParams.setSoTimeout(httpParams, TIME_OUT);
		final HttpClient httpClient = new DefaultHttpClient(httpParams);
		get = new HttpGet(GET_IP_FROM);
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Bundle data = msg.getData();
				int responseStatusCode = data.getInt("responseStatusCode");
				if (responseStatusCode == REPONSE_OK) {
					byte[] b;
					try {
						b = EntityUtils.toByteArray(response.getEntity());
						String xml = new String(b, "utf-8");
						String netip = splitStr(xml, "[", "]");
						SharedPreferencesUtil.write(context,"Location","IP",
								netip);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					SharedPreferencesUtil.write(context, "Location","IP",
							DEFAULT_IP);
				}
			}
		};
		new Thread() {
			@Override
			public void run() {
				try {
					response = httpClient.execute(get);
					Message msg = new Message();
					Bundle data = new Bundle();
					data.putInt("responseStatusCode", response.getStatusLine()
							.getStatusCode());
					msg.setData(data);
					handler.sendMessage(msg);
				} catch (IllegalArgumentException ec) {
					response = null;
					interrupted();
				} catch (ClientProtocolException e) {
					response = null;
					interrupted();
				} catch (IOException e) {
					response = null;
					interrupted();
				}
			}
		}.start();
	}	
	
	/**
	 * 通过wifiManager获取mac地址
	 * @attention Wifi
	 * @return Mac Address
	 */
	public static String getMacFromWifi(Context context){
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);   
        if (!wifiManager.isWifiEnabled()) {   
        	wifiManager.setWifiEnabled(true);    
        }   
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();       
        String mResult = wifiInfo.getMacAddress();
        
        return mResult;
	}
	
//	public static String getMacAddress() {
//        String result = "";
//        String Mac = "";
//        result = callCmd("busybox ifconfig", "HWaddr");
// 
//        if (result == null) {
//            return "网络出错，请检查网络";
//        }
//        if (result.length() > 0 && result.contains("HWaddr")) {
//            Mac = result.substring(result.indexOf("HWaddr") + 6, result.length() - 1);
//            if (Mac.length() > 1) {
//                result = Mac.toLowerCase();
//            }
//        }
//        return result.trim();
//    }
	
	
	public static String getMacAddress(Context context){
		
		//在wifi未开启状态下，仍然可以获取MAC地址，但是IP地址必须在已连接状态下否则为0
		String macAddress = null, ip = null;
		WifiManager wifiMgr = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
		if (null != info) {
		    macAddress = info.getMacAddress();
		   
		}
		return macAddress==null?"virture":macAddress;

	}
	public static String callCmd(String cmd,String filter) { 
		String result = ""; 
		String line = ""; 
		try {
		Process proc = Runtime.getRuntime().exec(cmd);
		InputStreamReader is = new InputStreamReader(proc.getInputStream()); 
		BufferedReader br = new BufferedReader (is); 

		//执行命令cmd，只取结果中含有filter的这一行
		while ((line = br.readLine ()) != null && line.contains(filter)== false) { 
		//result += line;
		Log.i("test","line: "+line);
		}

		result = line;
		Log.i("test","result: "+result);
		} 
		catch(Exception e) { 
		e.printStackTrace(); 
		} 
		return result; 
		}
	public static String getCPUSerial(Context context) {   
		String s=getMacAddress(context) ;
		if(s.equals("")){
			return android.os.Build.MANUFACTURER;
		}else
		{
			return s.replace(":", "");
		}
   } 
}
