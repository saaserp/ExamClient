package com.wencai.parse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpClientTool {
	String ip;
	HttpClientTool(String ip){
		this.ip=ip;
	}
	// 响应
	private HttpResponse mHttpResponse = null;
	// 实体
	private HttpEntity mHttpEntity = null;
	InputStream inputStream;
	String result;
	public String send(String addr,Map<String ,String> map){
		Set<String> keys=map.keySet();
		String args="?";
		for(String key:keys){
			args+=key+"="+map.get(key)+"&";
		}
		return send(addr,args);
	}
	public String send(String add,String arg) {
		// 生成一个请求对象
		HttpGet httpGet = new HttpGet("http://"+ip+"/"+arg);
		// 生成一个Http客户端对象
		HttpClient httpClient = new DefaultHttpClient();
		// 发送请求并获得响应对象
		try {
			mHttpResponse = httpClient.execute(httpGet);
			// 获得响应的消息实体
			mHttpEntity = mHttpResponse.getEntity();

			// 获取一个输入流
			inputStream = mHttpEntity.getContent();

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

			String line = "";

			while (null != (line = bufferedReader.readLine())) {
				result += line;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
