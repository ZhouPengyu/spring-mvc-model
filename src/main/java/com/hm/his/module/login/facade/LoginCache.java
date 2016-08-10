package com.hm.his.module.login.facade;

import com.hm.his.module.Constants;

/**
 * 用户登录缓存定义
 */
public class LoginCache {
	/**
	 * 缓存前缀
	 */
	private static final String PREFIX = "Login_";

	public static final String GETFARMEBYDOCTORID_PREFIX = "Login_getFarmeByDoctorId_";
	public static final int getFarmeByDoctorIdExpires = Constants.EXPIRES_LEVEL_4;

	/**
	 * 方法getFarmeByHospitalId的key
	 */
	public static String getFarmeByDoctorId(Long doctorId) {
		StringBuffer sb = new StringBuffer();
		return sb.append(PREFIX).append("getFarmeByDoctorId_").append(doctorId).toString();
	}
	

}
