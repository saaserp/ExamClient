package com.nengfei.service;

import com.nengfei.app.R;
import com.nengfei.login.LoginActivity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.Looper;

public class CheckWifi extends Service {  
    private boolean quit = false;  
    private WifiManager wifiM;  
  
 
  
    @SuppressWarnings("deprecation")
	@Override  
    public void onCreate() {  
        super.onCreate();  
        Context context = getApplicationContext();  
        final NotificationManager notificationManager = (NotificationManager) context  
                .getSystemService(NOTIFICATION_SERVICE);  
        int icon = R.drawable.logo; //图标随便用了个，美工没空T T  
        CharSequence cs = "Wifi连接中断";  
        long when = System.currentTimeMillis();  
        final Notification notification = new Notification(icon, cs, when);  
        CharSequence contentTitle = "能飞app提示";  
        CharSequence contentText = "5分钟离线时长已过，请重新联网！";  
        Intent notificationIntent = new Intent();  
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,  
                notificationIntent, 0);  
        notification.setLatestEventInfo(context, contentTitle, contentText,  
                contentIntent);  
        new Thread() {  
            @Override  
            public void run() {  
                Looper.prepare();  
                while (!quit) {  
                    try {  
                         Thread.sleep(1000*60*5);    //每5分钟检查一次
                    	//Thread.sleep(1000);
                    } catch (InterruptedException e) {  
                        e.printStackTrace();  
                    }  
                    
                  
                    
                    ConnectivityManager con=(ConnectivityManager)getSystemService(Activity.CONNECTIVITY_SERVICE);  
                    boolean wifi=con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();  
                    boolean internet=con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();  
                    if(wifi|internet){  
                        //执行相关操作  
                    }else{  
                    	 notificationManager.notify(1, notification);  
                         LoginActivity.logout();
                        
                    }  
                    
                
                }  
            }  
        }.start();  
    }

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}  
}  
