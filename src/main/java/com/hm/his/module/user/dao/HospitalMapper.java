package com.hm.his.module.user.dao;

import com.hm.his.module.user.model.Doctor;
import com.hm.his.module.user.model.Hospital;

import java.util.List;

public interface HospitalMapper {
	
	/**
	 * <p>Description:根据ID查询医院信息<p>
	 * <p>Company: H.M<p>
	 * @author ZhouPengyu
	 * @date 2016-3-23 10:29:22
	 */
	Hospital getHospitalById(Long hospitalId);
	
	/**
	 * 
	 * <p>Description:添加诊所<p>
	 * <p>Company: H.M<p>
	 * @author ZhouPengyu
	 * @date 2016-1-20 下午2:37:31
	 */
	void insertHospital(Hospital hospital);
	
	/**
	 * 
	 * <p>Description:更新诊所<p>
	 * <p>Company: H.M<p>
	 * @author ZhouPengyu
	 * @date 2016-1-20 下午2:37:31
	 */
	void updateHospital(Hospital hospital);
	
	/**
	 * <p>Description:根据医生ID查询医院名称<p>
	 * <p>Company: H.M<p>
	 * @author ZhouPengyu
	 * @date 2016-2-16 下午8:17:21
	 */
	String getHospitalName(Long doctorId);

	Hospital getHospitalByDoctorId(Integer doctorId);

	/**
	 * <p>Description:查询所有的医院<p>
	 * @author ZhouPengyu
	 * @date 2016年3月12日 下午6:33:28
	 */
	List<Hospital> searchHospital(Hospital doctor);
}
