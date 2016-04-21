package com.nengfei.social;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.nengfei.app.R;
import com.nengfei.util.NetWorkUtil;
import com.nengfei.util.SharedPreferencesUtil;
import com.nengfei.util.UiUtil;
import com.tencent.weibo.api.TAPI;
import com.tencent.weibo.constants.OAuthConstants;
import com.tencent.weibo.oauthv2.OAuthV2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

@SuppressLint("HandlerLeak")
public class TencentWeiboMethods implements WeiboMethods {
	private static String tencent_oauthv2 = "tencent_oauthv2";
	private static OAuthV2 oAuthV2 = null;
	private String response = null;
	private TAPI tAPI = null;
	private Context mContext;
	
	public TencentWeiboMethods(Context mContext) {
		super();
		this.mContext = mContext;
	}

	private static boolean updateOauthV2Flag = false;

	public void setUpdateOauthV2Flag(boolean updateOauthV2Flag) {
		TencentWeiboMethods.updateOauthV2Flag = updateOauthV2Flag;
	}

	@Override
	public void saveOauthV2(Object o) {
		OAuthV2 oAuth = (OAuthV2) o;
		SharedPreferences tencentOauthV2 = mContext.getSharedPreferences(
				tencent_oauthv2, Context.MODE_APPEND);
		SharedPreferences.Editor ed = tencentOauthV2.edit();
		ed.clear();
		ed.putString("AccessToken", oAuth.getAccessToken());
		ed.putString("AppFrom", oAuth.getAppFrom());
		ed.putString("AuthorizeCode", oAuth.getAuthorizeCode());
		ed.putString("ClientId", oAuth.getClientId());
		ed.putString("ClientIP", oAuth.getClientIP());
		ed.putString("ClientSecret", oAuth.getClientSecret());
		ed.putString("ExpiresIn", oAuth.getExpiresIn());
		ed.putString("GrantType", oAuth.getGrantType());
		ed.putString("Msg", oAuth.getMsg());
		ed.putString("OauthVersion", oAuth.getOauthVersion());
		ed.putString("Openid", oAuth.getOpenid());
		ed.putString("Openkey", oAuth.getOpenkey());
		ed.putString("RedirectUri", oAuth.getRedirectUri());
		ed.putString("RefreshToken", oAuth.getRefreshToken());
		ed.putString("ResponeType", oAuth.getResponeType());
		ed.putString("Scope", oAuth.getScope());
		ed.putString("SeqId", oAuth.getSeqId());
		ed.putString("Type", oAuth.getType());
		ed.putInt("Status", oAuth.getStatus());
		ed.commit();
		updateOauthV2Flag = true;
	}

	private OAuthV2 getOauthV2(Context context) {
		if (updateOauthV2Flag) {
			SharedPreferences tencentOauthV2 = context.getSharedPreferences(
					tencent_oauthv2, Context.MODE_APPEND);
			oAuthV2 = new OAuthV2();
			oAuthV2.setAccessToken(tencentOauthV2.getString("AccessToken", ""));
			oAuthV2.setAppFrom(tencentOauthV2.getString("AppFrom", ""));
			oAuthV2.setAuthorizeCode(tencentOauthV2.getString("AuthorizeCode",
					""));
			oAuthV2.setClientId(tencentOauthV2.getString("ClientId", ""));
			oAuthV2.setClientIP(tencentOauthV2.getString("ClientIP", ""));
			oAuthV2.setClientSecret(tencentOauthV2
					.getString("ClientSecret", ""));
			oAuthV2.setExpiresIn(tencentOauthV2.getString("ExpiresIn", ""));
			oAuthV2.setGrantType(tencentOauthV2.getString("GrantType", ""));
			oAuthV2.setMsg(tencentOauthV2.getString("Msg", ""));
			oAuthV2.setOauthVersion(tencentOauthV2
					.getString("OauthVersion", ""));
			oAuthV2.setOpenid(tencentOauthV2.getString("Openid", ""));
			oAuthV2.setOpenkey(tencentOauthV2.getString("Openkey", ""));
			oAuthV2.setRedirectUri(tencentOauthV2.getString("RedirectUri", ""));
			oAuthV2.setRefreshToken(tencentOauthV2
					.getString("RefreshToken", ""));
			oAuthV2.setResponseType(tencentOauthV2.getString("ResponeType", ""));
			oAuthV2.setScope(tencentOauthV2.getString("Scope", ""));
			oAuthV2.setSeqId(tencentOauthV2.getString("SeqId", ""));
			oAuthV2.setType(tencentOauthV2.getString("Type", ""));
			oAuthV2.setStatus(tencentOauthV2.getInt("Status", 0));
			updateOauthV2Flag = false;
		}
		return oAuthV2;
	}

	@Override
	public void sendOneWeibo(final String content) {
		this.sendOneWeibo(content, "", "", SharedPreferencesUtil
				.read(mContext, "Location", "IP",
						NetWorkUtil.DEFAULT_IP));
	}

	@Override
	public void sendOneWeibo(final String content,
			final String latitude, final String longitude, final String clientip) {
		tAPI = new TAPI(OAuthConstants.OAUTH_VERSION_2_A);
		oAuthV2 = getOauthV2(mContext);
		if (oAuthV2 == null || oAuthV2.getAccessToken().equals("")) {
			Log.e("Tencent sendOneWeibo", "AccessToken !!!");
			return;
		}
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Bundle data = msg.getData();
				String response = data.getString("response");
				if (parseSendResult(response)) {
					//��ʱ����
					if(response!=null){
						UiUtil.showToastLong(mContext, R.string.thanks_for_recommend);
					}else{
						UiUtil.showToastLong(mContext, R.string.send_weibo_success);
					}					
				}else{
					UiUtil.showToastLong(mContext, R.string.send_weibo_fail);
				}
			}
		};
		new Thread() {
			@Override
			public void run() {
				try {
					response = tAPI.add(oAuthV2, "json", content, clientip,
							latitude, longitude, "");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				tAPI.shutdownConnection();
				Message msg = new Message();
				Bundle data = new Bundle();
				data.putString("response", response);
				msg.setData(data);
				handler.sendMessage(msg);
			}
		}.start();

	}

	@Override
	public void sendOneWeibo(final String content,
			final String pic, final String latitude, final String longitude,
			final String clientip) {
		// TODO Auto-generated method stub
		tAPI = new TAPI(OAuthConstants.OAUTH_VERSION_2_A);
		oAuthV2 = getOauthV2(mContext);
		if (oAuthV2 == null || oAuthV2.getAccessToken().equals("")) {
			Log.e("Tencent sendOneWeibo", "AccessToken !!!");
			return;
		}
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Bundle data = msg.getData();
				String response = data.getString("response");
				if (parseSendResult(response)) {
					UiUtil.showToastLong(mContext, R.string.send_weibo_success);
				}else{
					UiUtil.showToastLong(mContext, R.string.send_weibo_fail);
				}
			}
		};
		new Thread() {
			@Override
			public void run() {
				try {
					response = tAPI.addPic(oAuthV2, "json", content, clientip,
							latitude, longitude, pic, "");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				tAPI.shutdownConnection();
				Message msg = new Message();
				Bundle data = new Bundle();
				data.putString("response", response);
				msg.setData(data);
				handler.sendMessage(msg);
			}
		}.start();
	}

	@Override
	public void sendOneWeibo(String content, String pic) {
		// TODO Auto-generated method stub
		this.sendOneWeibo(content, pic, "", "", SharedPreferencesUtil
				.read(mContext, "Location", "IP",
						NetWorkUtil.DEFAULT_IP));
	}

	private boolean parseSendResult(String response) {
		boolean sendResult = false;
		if(response!=null){
			try {
				JSONTokener jsonParser = new JSONTokener(response);
				JSONObject result = (JSONObject) jsonParser.nextValue();
				if (result.getInt("errcode") == 0) {
					sendResult = true;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		return sendResult;
	}

	@Override
	public void clearOauthV2() {
		// TODO Auto-generated method stub
		SharedPreferences tencentOauthV2 = mContext.getSharedPreferences(
				tencent_oauthv2, Context.MODE_APPEND);
		SharedPreferences.Editor ed = tencentOauthV2.edit();
		ed.clear();
		ed.commit();
		oAuthV2 = null;
		updateOauthV2Flag = true;
	}

}
