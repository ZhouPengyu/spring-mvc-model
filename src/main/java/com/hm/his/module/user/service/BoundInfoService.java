package com.hm.his.module.user.service;

import java.util.List;
import java.util.Map;

import com.hm.his.module.user.model.BoundInfo;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-5-4 10:56:02
 * @description 绑定服务接口
 * @version 1.0
 */
public interface BoundInfoService {
	
	/**
	 * <p>Description:获取我的诊所<p>
	 * @author ZhouPengyu
	 * @date 2016年5月4日 上午10:52:08
	 */
	List<BoundInfo> getBoundByOpenId(String openId);
	
	/**
	 * <p>Description:绑定诊所<p>
	 * @author ZhouPengyu
	 * @date 2016年5月4日 上午10:52:19
	 */
	Integer insertBoundInfo(BoundInfo boundInfo);
    
	/**
	 * <p>Description:取消绑定<p>
	 * @author ZhouPengyu
	 * @date 2016年5月4日 上午10:52:45
	 */
	Integer rescissionHospital(String openId, Long hospitalId);

	/**
	 * <p>Description:更新绑定信息<p>
	 * @author ZhouPengyu
	 * @date 2016年5月4日 上午10:52:54
	 */
	Integer updateBoundInfo(BoundInfo boundInfo);
	
	/**
	 * <p>Description:绑定诊所权限<p>
	 * @author ZhouPengyu
	 * @date 2016年5月5日 上午11:11:18
	 */
	@SuppressWarnings("rawtypes")
	List<Map> boundHospitalFunction(Map<String, String> params);
	
	/**
	 * <p>Description:判断是否绑定<p>
	 * @author ZhouPengyu
	 * @date 2016年5月11日 下午4:17:40
	 */
	Integer isBoundingByDoctor(String openId, String doctorName);
	
	/**
	 * <p>Description:会话中设置doctorId,hospitalId<p>
	 * @author ZhouPengyu
	 * @date 2016年5月16日 上午11:58:04
	 */
	void setDoctorToSession(Long hospitalId);
	
	/**
	 * <p>Description:判断是否绑定<p>
	 * @author ZhouPengyu
	 * @date 2016年5月4日 上午10:52:45
	 */
	Integer isBoundingByHospital(String openId, Long hospitalId);
}