package com.nengfei.social;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;

public class SinaWeiboOAuthV2 extends Activity {
	private Weibo mWeibo;
	private static final String CONSUMER_KEY = "3124960049";// 替换为开发者的appkey，例如"1646212860";
	private static final String REDIRECT_URL = "http://apps.weibo.com/exam_android";
	public static Oauth2AccessToken accessToken;
	public static final String TAG = "sinasdk";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mWeibo = Weibo.getInstance(CONSUMER_KEY, REDIRECT_URL);
		mWeibo.authorize(SinaWeiboOAuthV2.this, new AuthDialogListener());
	}

	class AuthDialogListener implements WeiboAuthListener {

		@SuppressWarnings("rawtypes")
		@Override
		public void onComplete(Bundle values) {
			String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			SinaWeiboOAuthV2.accessToken = new Oauth2AccessToken(token,
					expires_in);
			if (SinaWeiboOAuthV2.accessToken.isSessionValid()) {
				try {
					@SuppressWarnings("unused")
					Class sso = Class
							.forName("com.weibo.sdk.android.api.WeiboAPI");// 如果支持weiboapi的话，显示api功能演示入口按钮
				} catch (ClassNotFoundException e) {
					// e.printStackTrace();
					Log.i(TAG, "com.weibo.sdk.android.api.WeiboAPI not found");

				}
				new SinaWeiboMethods(SinaWeiboOAuthV2.this)
						.saveOauthV2(accessToken);
				Toast.makeText(SinaWeiboOAuthV2.this, "认证成功",
						Toast.LENGTH_SHORT).show();
				finish();
			}
		}

		@Override
		public void onError(WeiboDialogError e) {
			Toast.makeText(getApplicationContext(),
					"Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
		}

		@Override
		public void onCancel() {
			Toast.makeText(getApplicationContext(), "Auth cancel",
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(getApplicationContext(),
					"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}

	}
}
