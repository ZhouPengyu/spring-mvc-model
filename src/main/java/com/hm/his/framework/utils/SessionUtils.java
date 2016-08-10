package com.hm.his.framework.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import com.hm.his.module.user.model.Doctor;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-2 11:35:38
 * @description Session工具类
 * @version 3.0
 */
public class SessionUtils {

	//his 系统用户相关的cookie 名称
	public static final String COOKIE_NAME="hmcookie";

	public static final String USER_AGENT="user-agent";

	public static final String HOSPITAL_IS_USE_BATCH_MANAGE ="hospitalUseBatchManage";

	public static final Integer MAX_AGE = 60 * 60 * 24 * 7;//cookie缓存30天

	public static HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return request;
	}

	public static HttpServletResponse getResponse() {
		HttpServletResponse response = ((ServletWebRequest) RequestContextHolder.getRequestAttributes()).getResponse();
		return response;
	}

	public static HttpSession getSession() {
		HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		return session;
	}

	/**
	 * <p>Description:获取医院医生信息（医生，所属医院）<p>
	 * @author ZhouPengyu
	 * @date 2016年3月7日 下午7:52:31
	 */
	public static Doctor getCurrentUser() {
		//test
		HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		Doctor doctor = (Doctor) session.getAttribute("currentUser");
		return doctor;
	}

	/**
	 * <p>Description:获取医院医生ID<p>
	 * @author ZhouPengyu
	 * @date 2016年3月7日 下午7:52:31
	 */
	public static Long getDoctorId() {
		if(LoginFilter.DEV_MODE){
			//	return 246L;
			return 1L;
		}
		HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		Long doctorId = (Long) session.getAttribute("doctorId");
		return doctorId;
	}

	/**
	 * <p>Description:获取医院ID<p>
	 * @author ZhouPengyu
	 * @date 2016年3月7日 下午7:52:31
	 */
	public static Long getHospitalId() {
		if(LoginFilter.DEV_MODE){
//			return 476L;
		return 184L;
//	return 1L;

		}
		HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		Long hospitalId = (Long) session.getAttribute("hospitalId");
		return hospitalId;
	}
	
	/**
	 * <p>Description:是否管理员<p>
	 * @author ZhouPengyu
	 * @date 2016-4-13 16:07:01
	 */
	public static Boolean isAdmin() {
		HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		Long isAdmin = (Long) session.getAttribute("isAdmin");
		return isAdmin==1l;
	}

	/**
	 * <p>Description:是否使用批次管理<p>
	 * @author ZhouPengyu
	 * @date 2016-4-13 16:07:01
	 */
	public static Integer getUseBatchManage() {
		HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();

		Object isUseBatchManage = session.getAttribute(HOSPITAL_IS_USE_BATCH_MANAGE);
		if(isUseBatchManage != null){
			return Integer.parseInt(isUseBatchManage.toString());
		}
		return null;
	}

	public static void set(String key, Object value) {
		HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		session.setAttribute(key, value);
	}

	public static <T> T get(String key, Class<T> clazz) {
		HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		@SuppressWarnings("unchecked")
		T t = (T) session.getAttribute(key);
		return t;
	}
	
	/**
	 * <p>Description:获取openId<p>
	 * @author ZhouPengyu
	 * @date 2016-5-3 15:49:44
	 */
	public static String getOpenId() {
		HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		String openId = LangUtils.getString(session.getAttribute("openId"));
//		if(null == openId || "".equals(openId))
//			openId = "123456";
		return openId;
	}
	
	/**
	 * <p>Description:清除docId,hosId<p>
	 * @author ZhouPengyu
	 * @date 2016-5-16 14:22:29
	 */
	public static void removeDocIdAndHosId() {
		try {
			HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
			session.removeAttribute("doctorId");
			session.removeAttribute("hospitalId");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
