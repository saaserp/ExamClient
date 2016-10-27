package com.nengfei.parse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class HttpClientTool {
	static String ip="http://malloto.com:8080/NengfeiServer4.0/";
	HttpClientTool(String ip){
		this.ip=ip;
	}
	public static HttpClientTool getInstance() {
		// TODO Auto-generated constructor stub
		return new HttpClientTool(ip);
	}
	// 响应
	private HttpResponse mHttpResponse = null;
	// 实体
	private HttpEntity mHttpEntity = null;
	InputStream inputStream;
	String result="";
	public String send(String addr,Map<String ,String> map){
		Set<String> keys=map.keySet();
		String args="?";
		for(String key:keys){
			args+=key+"="+map.get(key)+"&";
		}
		args=args.substring(0, args.length()-1);
		return send(addr,args);
	}
	
	public String send(String add,String arg) {
		// 生成一个请求对象
		HttpGet httpGet = new HttpGet(ip+add+arg);
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
	
	public String post(String addr,Map<String ,String> map){
		 List<NameValuePair> pairList = new ArrayList<NameValuePair>();
	        
		 NameValuePair pair1;
        Set<String> keys=map.keySet();
		for(String key:keys){
			pair1 = new BasicNameValuePair(key, map.get(key));
			pairList.add(pair1);
		}
        
        
       

        try
        {
            HttpEntity requestHttpEntity = new UrlEncodedFormEntity(
                    pairList);
            // URL使用基本URL即可，其中不需要加参数
            HttpPost httpPost = new HttpPost(ip+addr);
            // 将请求体内容加入请求中
            httpPost.setEntity(requestHttpEntity);
            // 需要客户端对象来发送请求
            HttpClient httpClient = new DefaultHttpClient();
            // 发送请求
            HttpResponse response = httpClient.execute(httpPost);
            // 显示响应
           
            return getResult(response);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
		return "";

    }
	
	private String getResult(HttpResponse response)
    {
        if (null == response)
        {
            return "";
        }

        HttpEntity httpEntity = response.getEntity();
        try
        {
            InputStream inputStream = httpEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));
            String result = "";
            String line = "";
            while (null != (line = reader.readLine()))
            {
                result += line;

            }

           // System.out.println(result);
            return result;
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";

    }

}
