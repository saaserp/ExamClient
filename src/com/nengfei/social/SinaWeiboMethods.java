package com.nengfei.social;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.util.Log;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.keep.AccessTokenKeeper;
import com.weibo.sdk.android.net.RequestListener;

public class SinaWeiboMethods implements WeiboMethods {
	private static boolean updateOauthV2Flag = false;
	private static Oauth2AccessToken oauth2AccessToken;
	private StatusesAPI statusesAPI;
	private Context mContext;
	
	public SinaWeiboMethods(Context mContext) {
		super();
		this.mContext = mContext;
	}

	public void setUpdateOauthV2Flag(boolean updateOauthV2Flag) {
		SinaWeiboMethods.updateOauthV2Flag = updateOauthV2Flag;
	}

	@Override
	public void saveOauthV2(Object oAuth) {
		// TODO Auto-generated method stub
		Oauth2AccessToken accessToken = (Oauth2AccessToken) oAuth;
		AccessTokenKeeper.keepAccessToken(mContext, accessToken);
		updateOauthV2Flag = true;
	}

	private Oauth2AccessToken getOauth2AccessToken() {
		if (updateOauthV2Flag) {
			oauth2AccessToken = AccessTokenKeeper.readAccessToken(mContext);
			if (oauth2AccessToken.getToken().equals("")) {
				Log.e("getOauth2AccessToken", "empty");
			}
			updateOauthV2Flag = false;
		}
		return oauth2AccessToken;
	}

	@Override
	public void sendOneWeibo(final String content) {
		this.sendOneWeibo(content, "", "", "");
	}

	@Override
	public void sendOneWeibo(final String content,
			final String latitude, final String longitude, String clientip) {
		statusesAPI = new StatusesAPI(getOauth2AccessToken());
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				statusesAPI.update(content, latitude, longitude,
						getSendRequestListener());
			}
		}.start();
	}

	@Override
	public void sendOneWeibo(final String content,
			final String file, final String latitude, final String longitude,
			String clientip) {
		// TODO Auto-generated method stub
		statusesAPI = new StatusesAPI(getOauth2AccessToken());
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				statusesAPI.upload(content, file, latitude, longitude,
						getSendRequestListener());
			}
		}.start();
	}

	@Override
	public void sendOneWeibo(String content, String pic) {
		// TODO Auto-generated method stub
		this.sendOneWeibo(content, pic,"", "", "");
	}

	private boolean parseSendResult(String response) {
		boolean sendResult = false;
		if(response!=null){
			try {
				JSONTokener jsonParser = new JSONTokener(response);
				JSONObject result = (JSONObject) jsonParser.nextValue();
				if (!result.has("errcode")) {
					sendResult = true;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sendResult;
	}

	private RequestListener getSendRequestListener() {
		return new RequestListener() {

			@Override
			public void onIOException(IOException e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onError(WeiboException e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onComplete(String response) {
				// TODO Auto-generated method stub
				if (parseSendResult(response)) {
					
				}else{
				
				}
			}
		};
	}

	@Override
	public void clearOauthV2() {
		// TODO Auto-generated method stub

	}

}
