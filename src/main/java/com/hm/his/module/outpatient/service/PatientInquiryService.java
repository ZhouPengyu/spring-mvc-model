package com.hm.his.module.outpatient.service;

import java.util.List;
import java.util.Map;

import com.hm.his.module.outpatient.model.PatientInquiry;
import com.hm.his.module.outpatient.model.request.PatientInquiryRequest;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-7 10:58:04
 * @description 病历接口类
 * @version 3.0
 */
public interface PatientInquiryService {
	
    PatientInquiry getInquiryByRecordId(Long recordId);
    
    List<PatientInquiry> getInquiryByPatient(Long patientId);
    
    List<PatientInquiry> getInquiryByDoctorId(Long doctorId);
    
    Long saveInquiry(PatientInquiry patientInquiry) throws Exception;
    
    void addInquiry(PatientInquiry patientInquiry) throws Exception;

    void addInquiryInfoForOutpatient(PatientInquiryRequest patientInquiryRequest) throws Exception;
    
    List<PatientInquiry> getSymptom(List<Long> patientIdList);
    
    /**
     * <p>Description:查询患者最新一次病历<p>
     * @author ZhouPengyu
     * @date 2015-12-23 下午7:44:06
     */
    PatientInquiry getInquiryNewByPatientId(Long patientId);
    
    /**
     * <p>Description:查询今天就诊病患<p>
     * @author ZhouPengyu
     * @date 2016-1-23 下午2:10:43
     */
    List<PatientInquiry> getTodayPatientInquiry(Map<String, Object> map);
    
    /**
     * <p>Description:获取病历ID序列值<p>
     * @author ZhouPengyu
     * @date 2016-1-23 下午2:56:07
     */
    Integer getRecordSeq();
    
    /**
     * <p>Description:保存病历模版<p>
     * @author ZhouPengyu
     * @date 2016年3月28日 上午11:16:56
     */
    Long saveInquiryTemplate(PatientInquiryRequest patientInquiryRequest);
    
    /**
     * <p>Description:获取病历模版<p>
     * @author ZhouPengyu
     * @date 2016年3月28日 上午11:16:56
     */
    Map<String, Object> getInquiryTemplateById(Long recordId);
    
}
