package com.hm.his.module.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hm.his.module.user.model.Doctor;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-2 15:16:05
 * @description doctor 数据库实现类
 * @version 3.0
 */
public interface DoctorMapper {
    Doctor getDoctorById(Long doctorId);

    Doctor getDoctorByName(String doctorName);

    List<Doctor> getDoctorByIdList(List<Long> doctorIdList);
    
    /**
     * <p>Description:查询医生<p>
     * @author ZhouPengyu
     * @date 2016年3月12日 下午6:33:28
     */
    List<Doctor> searchDoctor(Doctor doctor);

    /**
     * <p>Description:查询所有的医生<p>
     * @author ZhouPengyu
     * @date 2016年3月12日 下午6:33:28
     */
    List<Doctor> searchDoctorForUpdatePwd(Doctor doctor);

    Doctor getPassword(String doctorName);

    Integer deleteDoctor(@Param("doctorId")Long doctorId, @Param("hospitalId")Long hospitalId);
    
    Integer updateDoctor(Doctor doctor);

    int updateByPrimaryKeySelective(Doctor record);

    void insertDoctor(Doctor doctor);
    
    /**
     * 
     * <p>Description:验证用户名是否存在<p>
     * <p>Company: H.M<p>
     * @author ZhouPengyu
     * @date 2016-1-20 下午3:04:08
     */
    Integer verifyUserName(String userName);
    
    /**
	 * <p>Description:保存医生患者关联关系<p>
	 * @author ZhouPengyu
	 * @date 2016年4月8日 上午11:38:35
	 */
	Integer insertDoctorPatientRelation(@Param("doctorId")Long doctorId, @Param("patientId")Long patientId);
	
	/**
	 * <p>Description:查询医生患者关联是否存在<p>
	 * @author ZhouPengyu
	 * @date 2016年4月8日 上午11:38:35
	 */
	Integer isDoctorPatientRelation(@Param("doctorId")Long doctorId, @Param("patientId")Long patientId);
}
