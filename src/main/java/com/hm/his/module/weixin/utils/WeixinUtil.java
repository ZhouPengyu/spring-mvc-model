package com.hm.his.module.weixin.utils;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.hm.his.module.Constants;
import com.hm.his.module.weixin.pojo.OAuthInfo;
import com.hm.his.module.weixin.pojo.WeixinUserInfo;

public class WeixinUtil {
	
	private static Logger logger = Logger.getLogger(WeixinUtil.class.getName());
	
	/**
	 * <p>Description:获取认证信息<p>
	 * @author ZhouPengyu
	 * @date 2016年5月4日 下午6:07:52
	 */
	public static OAuthInfo getOAuthInfo(String appid, String secret, String code ) {
        OAuthInfo oAuthInfo = null;
        String o_auth_openid_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
        String requestUrl = o_auth_openid_url.replace("APPID", appid).replace("SECRET", secret).replace("CODE", code);
        
        JSONObject jsonObject = HttpsRequestUtil.handleHttpsRequest(requestUrl, "GET", null);
         
        // 获取网页授权凭证
        if (null != jsonObject) {
            try {
                oAuthInfo = new OAuthInfo();
                oAuthInfo.setAccessToken(jsonObject.getString("access_token"));
                oAuthInfo.setExpiresIn(jsonObject.getInteger("expires_in"));
                oAuthInfo.setRefreshToken(jsonObject.getString("refresh_token"));
                oAuthInfo.setOpenId(jsonObject.getString("openid"));
                oAuthInfo.setScope(jsonObject.getString("scope"));
            } catch (JSONException e) {
                oAuthInfo = null;
                // 获取token失败
                logger.error("网页授权获取openId失败");
            }
        }
        return oAuthInfo;
    }
	
	/**
	 * <p>Description:获取AccessToken<p>
	 * @author ZhouPengyu
	 * @date 2016年5月4日 下午6:07:52
	 */
	public static String getAccessToken(String appId, String secret) {
        String access_token = null;
        String token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
        String requestUrl = token_url.replace("APPID", appId).replace("APPSECRET", secret);
        
        JSONObject jsonObject = HttpsRequestUtil.handleHttpsRequest(requestUrl, "GET", null);
         
        // 获取网页授权凭证
        if (null != jsonObject) {
            try {
            	access_token = jsonObject.getString("access_token");
            } catch (JSONException e) {
            	access_token = null;
                // 获取token失败
                logger.error("获取token失败");
            }
        }
        return access_token;
    }
	
	/**
	 * <p>Description:获取用户基本信息<p>
	 * @author ZhouPengyu
	 * @date 2016年5月4日 下午6:08:12
	 */
	public static WeixinUserInfo getWeixinUserInfo(String accessToken, String openId){
		WeixinUserInfo wxUserInfo = null;
		String requestUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
		requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
		// 通过网页授权获取用户信息
        JSONObject jsonObject = HttpsRequestUtil.handleHttpsRequest(requestUrl, "GET", null);
        if (null != jsonObject) {
        	try {
        		wxUserInfo = new WeixinUserInfo();
//        		wxUserInfo.setSubscribe(jsonObject.getInteger("subscribe"));
                wxUserInfo.setOpenId(jsonObject.getString("openid"));
                wxUserInfo.setNickname(jsonObject.getString("nickname"));
                wxUserInfo.setSex(jsonObject.getInteger("sex"));
                wxUserInfo.setCountry(jsonObject.getString("country"));
                wxUserInfo.setProvince(jsonObject.getString("province"));
                wxUserInfo.setCity(jsonObject.getString("city"));
                wxUserInfo.setHeadImgUrl(jsonObject.getString("headimgurl"));
                wxUserInfo.setUnionid(jsonObject.getString("unionid"));
                wxUserInfo.setSubscribeTime(jsonObject.getString("subscribe_time"));
                wxUserInfo.setRemark(jsonObject.getString("remark"));
//                wxUserInfo.setGroupid(jsonObject.getInteger("groupid"));
                wxUserInfo.setLanguage(jsonObject.getString("language"));
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        return wxUserInfo;
	}
	
	/**
	 * <p>Description:获取jsapi_ticket<p>
	 * @author ZhouPengyu
	 * @date 2016-5-5 16:25:16
	 */
	public static String getJsapiTicket(String accessToken) {
        String jsapi_ticket_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
        String requestUrl = jsapi_ticket_url.replace("ACCESS_TOKEN", accessToken);
        
        JSONObject jsonObject = HttpsRequestUtil.handleHttpsRequest(requestUrl, "GET", null);
        String jsapi_ticket = "";
        // 获取网页授权凭证
        if (null != jsonObject) {
            try {
            	jsapi_ticket = jsonObject.getString("ticket");
            } catch (JSONException e) {
                // 获取jsapi_ticket失败
                logger.error("网页授权获取jsapi_ticket失败");
            }
        }
        return jsapi_ticket;
    }
	
	/**
	 * <p>Description:获取jsapi_ticket的签名<p>
	 * @author ZhouPengyu
	 * @date 2016-5-5 16:25:16
	 */
	public static Map<String, String> getJsapiTicketSignature(String jsapi_ticket, String url) {
		Map<String, String> map = new HashMap<String, String>();
        StringBuffer buffer = new StringBuffer();
        String noncestr = UUID.randomUUID().toString().replace("-", "");
        String timestamp = Long.toString(System.currentTimeMillis() / 1000);
        buffer.append("jsapi_ticket=").append(jsapi_ticket).
        	append("&").
        	append("noncestr=").append(noncestr).
        	append("&").
        	append("timestamp=").append(timestamp).
        	append("&").
        	append("url=").append(url);
        map.put("appId", Constants.AppId);
        map.put("jsapi_ticket", jsapi_ticket);
        map.put("noncestr", noncestr);
        map.put("timestamp", timestamp);
        map.put("url", url);
        map.put("signature", sha1(buffer));
        
		return map;
    }
	
	/**
	 * <p>Description:sha1签名算法<p>
	 * @author ZhouPengyu
	 * @date 2016年5月6日 下午4:11:50
	 */
	public static String sha1(StringBuffer buffer){
		MessageDigest digest = null;
		StringBuilder hexString  = new StringBuilder();
		try {
			digest = MessageDigest.getInstance("SHA-1");
			byte[] digestByte = digest.digest(buffer.toString().getBytes());
			for (int i = 0; i < digestByte.length; i++) {
				String hex = Integer.toHexString(digestByte[i] & 0xFF);	//字节转换16进制字符串
				if(hex.length() < 2){
					hexString.append(0);
				}
				hexString.append(hex);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hexString.toString();
	}
}
