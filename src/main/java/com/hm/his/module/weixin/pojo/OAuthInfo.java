package com.hm.his.module.weixin.pojo;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-4-27 19:09:08
 * @description 网页授权信息
 * @version 1.0
 */
public class OAuthInfo {
	
    private String accessToken;	// 网页授权接口调用凭证
    private int expiresIn;	// 凭证有效时长
    private String refreshToken;	// 用于刷新凭证
    private String openId;	// 用户标识
    private String scope;	// 用户授权作用域
    private String unionId;
    
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public int getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getUnionId() {
		return unionId;
	}
	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}
    
}
