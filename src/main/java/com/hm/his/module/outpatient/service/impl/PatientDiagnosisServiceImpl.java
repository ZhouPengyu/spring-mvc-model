package com.hm.his.module.outpatient.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hm.his.module.outpatient.dao.PatientDiagnosisMapper;
import com.hm.his.module.outpatient.model.PatientDiagnosis;
import com.hm.his.module.outpatient.service.PatientDiagnosisService;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-7 11:42:01
 * @description 患者诊断接口实现类
 * @version 3.0
 */
@Service("PatientDiagnosisService")
public class PatientDiagnosisServiceImpl implements PatientDiagnosisService{
	
    @Autowired(required = false)
    PatientDiagnosisMapper patientDiagnosisMapper;
    
    @Override
    public List<PatientDiagnosis> getDiagnosisByRecordId(List<Long> recordIdList) {
        if (recordIdList == null || recordIdList.size() < 1) return new ArrayList<>();
        List<PatientDiagnosis> patientDiagnosisList = patientDiagnosisMapper.getDiagnosisByRecordList(recordIdList);
        return patientDiagnosisList;
    }

    @Override
    public List<PatientDiagnosis> getDiagnosisByRecordId(Long recordId) {
        if (recordId == null) return new ArrayList<>();
        List<PatientDiagnosis> patientDiagnosisList = patientDiagnosisMapper.getDiagnosisByRecordId(recordId);
        return patientDiagnosisList;
    }

	@Override
	public void insertDiagnosis(Long recordId, List<PatientDiagnosis> diagnosis) {
        if(CollectionUtils.isNotEmpty(diagnosis)){
            patientDiagnosisMapper.insertDiagnosis(recordId, diagnosis);
        }
	}

	@Override
	public void deleteDiagnosisByRecordId(Long recordId) {
		patientDiagnosisMapper.deleteDiagnosisByRecordId(recordId);
	}

    @Override
    public void resetDiagnosisByRecordId(Long recordId, List<PatientDiagnosis> diagnosis) throws Exception {
        //删除病历的诊断记录
        this.deleteDiagnosisByRecordId(recordId);
        if(diagnosis!=null && diagnosis.size()>0)
            this.insertDiagnosis(recordId, diagnosis); //添加诊断
    }
}
