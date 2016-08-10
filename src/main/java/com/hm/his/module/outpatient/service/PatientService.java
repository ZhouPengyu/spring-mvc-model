package com.hm.his.module.outpatient.service;

import java.util.List;
import java.util.Map;

import com.hm.his.module.outpatient.model.Patient;
import com.hm.his.module.outpatient.model.request.PatientInquiryRequest;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-8 15:34:15
 * @description 患者服务接口
 * @version 3.0
 */
public interface PatientService {
	
    Patient getPatientById(Long patientId);

    /**
     *  功能描述：门诊-- 保存或修改患者信息
     * @param  
     * @return  
     * @throws  
     * @author:  tangww
     * @createDate   2016-07-25
     *
     */
    Patient savePatientFromOutpatient(PatientInquiryRequest patientInquiryRequest);
    
    boolean savePatient(Patient patient) throws Exception;
    
    void addPatient(Patient patient) throws Exception;
    
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
     * <p>Description:病患sug查询<p>
     * @author ZhouPengyu
     * @date 2016-1-23 下午2:56:27
     */
    List<Patient> searchPatientBySug(Map<String, Object> params);
    
    /**
     * <p>Description:获取患者ID序列值<p>
     * @author ZhouPengyu
     * @date 2016-1-23 下午2:56:07
     */
    Integer getPatientSeq();

    /**
     * <p>Description:医生是否包含患者<p>
     * @author ZhouPengyu
     * @date 2016-3-4 10:10:35
     */
	Patient searchDocContainPatient(Map<String, Object> map);
	
	/**
	 * <p>Description:患者ID集合查询<p>
	 * @author ZhouPengyu
	 * @date 2016年8月2日 下午4:59:37
	 * @param patientId
	 * @return
	 */
	List<Patient> findPatientListByIds(List<Long> patientId);

    /**
     * <p>根据条件查询联系方式有效的患者</p>
     * @param parameterMap
     * @return
     */
    List<Patient> searchPatientWithValidPhone(Map<String, Object> parameterMap);
    /**
     * @description <p>根据条件查询联系方式有效的患者数目</p>
     * @author SuShaohua
     * @date 2016-08-09 15:42
     * @param
    */
    Integer searchPatientWithValidPhoneCount(Map<String, Object> parameterMap);
}
