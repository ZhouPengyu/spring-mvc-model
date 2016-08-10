package com.hm.his.module.outpatient.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hm.his.module.outpatient.model.PatientDiagnosis;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-7 10:55:16
 * @description 患者诊断结果 数据库实现类
 * @version 3.0
 */
public interface PatientDiagnosisMapper {
	
    List<PatientDiagnosis> getDiagnosisByRecordId(Long recordId);
    
    /**
     * <p>Description:添加病历诊断<p>
     * @author ZhouPengyu
     * @date 2016年3月12日 下午2:33:39
     */
    void insertDiagnosis(@Param("recordId")Long recordId, @Param("list")List<PatientDiagnosis> diagnosis);
    
    /**
     * <p>Description:根据病历删除诊断<p>
     * @author ZhouPengyu
     * @date 2016年3月12日 下午2:33:16
     */
    void deleteDiagnosisByRecordId(Long recordId);
    
    List<PatientDiagnosis> getDiagnosisByRecordList(List<Long> recordIdList);
}
