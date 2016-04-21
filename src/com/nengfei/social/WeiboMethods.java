package com.nengfei.social;

/**
 * 
 * @author suijun 待完善的地方： 1、AccessToken 过期提醒 2、重新认证时的判断、提示及相关的操作
 * 
 *         目前需要用户在使用当天重新认证一次
 */
public interface WeiboMethods {
	public void saveOauthV2(Object oAuth);

	public void clearOauthV2();

	public void setUpdateOauthV2Flag(boolean updateOauthV2Flag);

	public void sendOneWeibo(String content);

	public void sendOneWeibo(String content, String latitude, String longitude,
			String clientip);

	public void sendOneWeibo(String content, String pic, String latitude,
			String longitude, String clientip);

	public void sendOneWeibo(String content, String pic);
}
