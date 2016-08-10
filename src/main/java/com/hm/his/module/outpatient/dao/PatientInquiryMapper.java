package com.hm.his.module.outpatient.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;

import com.hm.his.module.outpatient.model.PatientInquiry;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-7 10:55:16
 * @description 病历 数据库实现类
 * @version 3.0
 */
public interface PatientInquiryMapper {
    PatientInquiry getInquiryByRecordId(Long recordId);
    List<PatientInquiry> getInquiryByPatientId(Long patientId);
    List<PatientInquiry> getInquiryByDoctorId(Long doctorId);
    void insertInquiry(PatientInquiry inquiry);
    void updateInquiry(PatientInquiry inquiry);
    @Select("select ver from inquiry_info where record_id = #{recordId}")
    int getRecordVer(long recordId);
    
    /**
     * <p>Description:查询患者最新一次病历<p>
     * @author ZhouPengyu
     * @date 2015-12-23 下午7:44:40
     */
    PatientInquiry getInquiryNewByPatientId(Long patientId);
    
	 /**
	  * <p>Description:查询今天就诊病患<p>
	  * @author ZhouPengyu
	  * @date 2016-1-23 下午2:11:30
	  */
    List<PatientInquiry> getTodayPatientInquiry(Map<String, Object> map);
    
    /**
     * <p>Description:获取病历ID序列值<p>
     * @author ZhouPengyu
     * @date 2016-1-23 下午2:56:07
     */
    Integer getRecordSeq();
}
