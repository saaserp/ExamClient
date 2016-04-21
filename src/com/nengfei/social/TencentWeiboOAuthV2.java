package com.nengfei.social;

import com.nengfei.app.R;
import com.tencent.weibo.oauthv2.OAuthV2;
import com.tencent.weibo.oauthv2.OAuthV2Client;
import com.tencent.weibo.webview.OAuthV2AuthorizeWebView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class TencentWeiboOAuthV2 extends Activity {
	// !!!请根据您的实际情况修改!!! 认证成功后浏览器会被重定向到这个url中 必须与注册时填写的一致
	private static String redirectUri = "http://qq.web.com/exam_android";
	// !!!请根据您的实际情况修改!!! 换为您为自己的应用申请到的APP KEY
	private static String clientId = "801329206";
	// !!!请根据您的实际情况修改!!! 换为您为自己的应用申请到的APP SECRET
	private static String clientSecret = "f752e44ee503f5632de8904b96165954";

	private static OAuthV2 oAuth;
	private TencentWeiboMethods tencentWeiboMethods;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		tencentWeiboMethods=new TencentWeiboMethods(this);
		
		oAuth = new OAuthV2(redirectUri);
		oAuth.setClientId(clientId);
		oAuth.setClientSecret(clientSecret);

		// 关闭OAuthV2Client中的默认开启的QHttpClient。
		OAuthV2Client.getQHttpClient().shutdownConnection();

		Intent intent = new Intent(TencentWeiboOAuthV2.this,
				OAuthV2AuthorizeWebView.class);
		intent.putExtra("oauth", oAuth);
		startActivityForResult(intent, 2);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 2:
			switch (resultCode) {
			case OAuthV2AuthorizeWebView.RESULT_CODE:
				oAuth = (OAuthV2) data.getExtras().getSerializable("oauth");
				if (oAuth.getStatus() == 0) {
					Toast.makeText(getApplicationContext(),
							R.string.weibo_auth_success, Toast.LENGTH_LONG)
							.show();
					tencentWeiboMethods.saveOauthV2(oAuth);
					//认证完成，直接推荐,临时的
					{
						TencentWeiboMethods twm=new TencentWeiboMethods(TencentWeiboOAuthV2.this);
						String recommendation=getString(R.string.recommendation);
						twm.sendOneWeibo(recommendation);
						
					}
				} else {
					Toast.makeText(getApplicationContext(),
							R.string.weibo_auth_fail, Toast.LENGTH_LONG)
							.show();
				}
				finish();
				break;
			case RESULT_CANCELED:
				finish();
				break;
			}
			break;
		default:
			break;
		}
	}

}
