package com.hm.his.module.user.service;

import java.util.List;
import java.util.Map;

import com.hm.his.module.login.model.Function;
import com.hm.his.module.user.model.Doctor;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-2 15:16:05
 * @description doctor服务接口
 * @version 3.0
 */
public interface DoctorService {
	
    Doctor getDoctorById(Long doctorId);
    
    Doctor getDoctorByName(String doctorName);

    
    Integer deleteDoctor(Long doctorId, Long hospitalId);
    
    boolean saveDoctor(Doctor doctor);
    
    /**
     * <p>Description:查询医生<p>
     * @author ZhouPengyu
     * @date 2016年3月12日 下午6:33:28
     */
    List<Doctor> searchDoctor(Doctor doctor);
    
    /**
     * <p>Description:获取医生名称<p>
     * @author ZhouPengyu
     * @date 2016年3月7日 下午6:09:11
     */
    Map<Long, String> getDoctorName(List<Long> doctorIdList);
    Map<Long, String> getDoctorRealNames(List<Long> doctorIdList);


	/**
	 * <p>Description:验证用户名密码<p>
	 * @author ZhouPengyu
	 * @date 2016年3月7日 下午6:09:11
	 */
	boolean verifyPassword(String doctorName, String password);

	/**
	 * <p>Description:验证用户名密码  --用于从数据库中查询出来的密码 比较<p>
	 * @author ZhouPengyu
	 * @date 2016年3月7日 下午6:09:11
	 */

	boolean verifyPasswordByHighPasswd(String doctorName, String highPasswd);
    
    /**
     * <p>Description:验证用户名是否存在<p>
     * @author ZhouPengyu
     * @date 2016-1-20 下午3:04:08
     */
    Integer verifyUserName(String userName);
    
    /**
     * <p>Description:验证诊所是否存在<p>
     * @author ZhouPengyu
     * @date 2016-1-20 下午3:04:08
     */
    Integer verifyHospitalName(String userName);
    
    /**
     * <p>Description:获取医生权限<p>
     * @author ZhouPengyu
     * @date 2016-3-3 16:47:35
     */
    List<Function> getDoctorFunction(Long doctorId);
    
    /**
	 * <p>Description:根据医生ID删除用户权限<p>
	 * @author ZhouPengyu
	 * @date 2016年3月23日 下午3:06:24
	 */
	Integer deleteDoctorFunctionByDocId(Long doctorId);
    
    /**
	 * <p>Description:保存用户权限<p>
	 * @author ZhouPengyu
	 * @date 2016年3月23日 下午3:06:24
	 */
	void saveDoctorFunction(Long doctorId, List<Long> functionIdList);
	
	/**
	 * <p>Description:保存医生患者关联关系<p>
	 * @author ZhouPengyu
	 * @date 2016年4月8日 上午11:38:35
	 */
	Integer insertDoctorPatientRelation(Long doctorId, Long patientId) throws Exception;
	
	/**
	 * <p>Description:查询医生患者关联是否存在<p>
	 * @author ZhouPengyu
	 * @date 2016年4月8日 上午11:38:35
	 */
	Integer isDoctorPatientRelation(Long doctorId, Long patientId) throws Exception;

	void saveDoctorPatientRelationForOutpatient(Long doctorId, Long patientId);
}
