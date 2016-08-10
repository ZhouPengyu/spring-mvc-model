package com.hm.his.module.weixin.pojo;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-5-4 16:03:03
 * @description 微信票据实体类
 * @version 1.0
 */
public class Token {
	
	private String accessToken;	// 接口访问凭证
	private int expiresIn;	// 凭证有效期，单位：秒
	
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
	
}
