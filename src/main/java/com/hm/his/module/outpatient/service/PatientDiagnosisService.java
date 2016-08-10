package com.hm.his.module.outpatient.service;

import java.util.List;

import com.hm.his.module.outpatient.model.PatientDiagnosis;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-7 11:42:01
 * @description 患者诊断接口类
 * @version 3.0
 */
public interface PatientDiagnosisService {
	
	/**
     * <p>Description:查询病历集合诊断<p>
     * @author ZhouPengyu
     * @date 2016-3-7 11:30:57
     */
    List<PatientDiagnosis> getDiagnosisByRecordId(List<Long> recordIdList) throws Exception;
    
    /**
     * <p>Description:查询病历诊断<p>
     * @author ZhouPengyu
     * @date 2016-3-7 11:30:57
     */
    List<PatientDiagnosis> getDiagnosisByRecordId(Long recordId) throws Exception;
    
    /**
     * <p>Description:添加患者诊断<p>
     * @author ZhouPengyu
     * @date 2016-3-12 14:31:17
     */
    void insertDiagnosis(Long recordId, List<PatientDiagnosis> diagnosis) throws Exception;
    
    /**
     * <p>Description:根据病历删除诊断<p>
     * @author ZhouPengyu
     * @date 2016年3月12日 下午2:33:16
     */
    void deleteDiagnosisByRecordId(Long recordId) throws Exception;

    /**
     * <p>Description:重置患者病历的诊断记录<p>
     * @author ZhouPengyu
     * @date 2016年3月12日 下午2:33:16
     */
    void resetDiagnosisByRecordId(Long recordId, List<PatientDiagnosis> diagnosis) throws Exception;
}
