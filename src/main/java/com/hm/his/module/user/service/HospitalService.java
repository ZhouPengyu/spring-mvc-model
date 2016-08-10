package com.hm.his.module.user.service;

import com.hm.his.module.user.model.Hospital;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-2 15:16:05
 * @description Hospital服务接口
 * @version 3.0
 */
public interface HospitalService {
	
	/**
	 * <p>Description:根据ID查询医院信息<p>
	 * <p>Company: H.M<p>
	 * @author ZhouPengyu
	 * @date 2016-3-23 10:29:22
	 */
	Hospital getHospitalById(Long hospitalId);

	/**
	 * <p>Description:诊所保存<p>
	 * <p>Company: H.M<p>
	 * @author ZhouPengyu
	 * @date 2016-1-20 上午11:09:32
	 */
	boolean saveHospital(Hospital hospital);
	
	/**
	 * <p>Description:根据医生ID查询医院名称<p>
	 * <p>Company: H.M<p>
	 * @author ZhouPengyu
	 * @date 2016-2-16 下午8:17:21
	 */
	String getHospitalName(Long doctorId);


	/**
	 * <p>Description:根据医生ID查询医院信息<p>
	 * <p>Company: H.M<p>
	 * @author SuShaohua
	 */
	Hospital getHospitalByDoctorId(Integer doctorId);
}
