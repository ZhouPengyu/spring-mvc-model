package com.hm.his.module.outpatient.dao;

import java.util.List;
import java.util.Map;

import com.hm.his.module.outpatient.model.Patient;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-7 10:55:16
 * @description 患者 数据库实现类
 * @version 3.0
 */
public interface PatientMapper {
	
    Patient getPatientById(Long id);
    
    void insertPatient(Patient patient);
    
    void updatePatient(Patient patient);
    
    /**
     * <p>Description:根据名称、拼音手机 查询医生患者<p>
     * @author ZhouPengyu
     * @date 2016年3月8日 下午5:48:59
     */
    List<Patient> searchPatientByDoctorId(Map<String, Object> map);
    
    /**
     * <p>Description:根据名称、拼音手机 查询医生患者数量<p>
     * @author ZhouPengyu
     * @date 2016年3月8日 下午5:48:59
     */
    Integer searchPatientTotalByDoctorId(Map<String, Object> map);
    
    /**
     * <p>Description:医生是否存在该患者<p>
     * @author ZhouPengyu
     * @date 2016年3月8日 下午5:46:43
     */
    Patient searchDocContainPatient(Map<String, Object> map);
    
    /**
     * <p>Description:病患sug查询<p>
     * @author ZhouPengyu
     * @date 2016-1-23 下午2:56:07
     */
    List<Patient> searchPatientBySug(Map<String, Object> params);
    
    /**
     * <p>Description:获取患者ID序列值<p>
     * @author ZhouPengyu
     * @date 2016-1-23 下午2:56:07
     */
    Integer getPatientSeq();
    
	/**
	 * <p>Description:患者ID集合查询<p>
	 * @author ZhouPengyu
	 * @date 2016年8月2日 下午4:59:37
	 * @param
	 * @return
	 */
	List<Patient> findPatientListByIds(List<Long> patientIdList);
    /**
     * @description 根据条件查询电话号码有效的患者
     * @author SuShaohua
     * @date 2016-08-08 15:26
     * @param
    */
    List<Patient> searchPatientWithValidPhone(Map<String, Object> parameterMap);
    /**
     * @description 根据条件查询电话号码有效的患者数量
     * @author SuShaohua
     * @date 2016-08-09 15:43
     * @param
    */
    Integer searchPatientWithValidPhoneCount(Map<String, Object> parameterMap);
}
