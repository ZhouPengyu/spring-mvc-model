package com.hm.his.module.outpatient.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hm.his.framework.utils.ChineseToEnglish;
import com.hm.his.framework.utils.DateUtil;
import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.module.outpatient.model.request.PatientInquiryRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hm.his.framework.log.annotation.SystemLogAnno;
import com.hm.his.framework.log.constant.FunctionConst;
import com.hm.his.framework.log.constant.Operation;
import com.hm.his.framework.utils.LangUtils;
import com.hm.his.module.outpatient.dao.PatientMapper;
import com.hm.his.module.outpatient.model.Patient;
import com.hm.his.module.outpatient.service.PatientService;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-8 15:34:15
 * @description 患者服务接口实现类
 * @version 3.0
 */
@Service("PatientService")
public class PatientServiceImpl implements PatientService {
    @Autowired(required = false)
    PatientMapper patientMapper;
    @Override
    public Patient getPatientById(Long patientId) {
        return patientId != null ? patientMapper.getPatientById(patientId) : null;
    }

	@Override
	public Patient savePatientFromOutpatient(PatientInquiryRequest patientInquiryRequest) {

		Patient patient = new Patient();
		patient.setPatientId(patientInquiryRequest.getPatientId());
		patient.setDoctorId(patientInquiryRequest.getDoctorId());
		patient.setPatientName(patientInquiryRequest.getPatientName());
		patient.setGender(patientInquiryRequest.getGender());
		patient.setPhoneNo(patientInquiryRequest.getPhoneNo());
		patient.setAge(patientInquiryRequest.getAge());
		patient.setAgeType(patientInquiryRequest.getAgeType());
		patient.setDiagnosisDate(DateUtil.getGeneralDate(new Date()));

		Patient dbPatient = this.getPatientById(patient.getPatientId());
		if(dbPatient==null){
			patient.setPatientPinyin(ChineseToEnglish.getPingYin(patient.getPatientName()));
			//患者ID已提前生成好，所以不需要再重复生成
			this.addPatient(patient);
		}else{
			dbPatient.setAge(patient.getAge());
			dbPatient.setAgeType(patient.getAgeType());
			dbPatient.setPhoneNo(patient.getPhoneNo());
			dbPatient.setDiagnosisDate(patient.getDiagnosisDate());
			this.savePatient(dbPatient);
		}
		return dbPatient;
	}

	@Override
    public boolean savePatient(Patient patient) {
        if (patient.getPatientId() == null){
            this.insertPatient(patient);
        } else {
            this.updatePatient(patient);
        }
        return true;
    }
    
    @Override
    public void addPatient(Patient patient) {
    	patientMapper.insertPatient(patient);
    }

    @SystemLogAnno(functionId = FunctionConst.OUTPATIENT_LOG,operationId = Operation.OUTPATIENT_PATIENT_ADD)
    private void insertPatient(Patient patient){
    	patient.setPatientId(LangUtils.getLong(patientMapper.getPatientSeq()));
        patientMapper.insertPatient(patient);
    }

    @SystemLogAnno(functionId = FunctionConst.OUTPATIENT_LOG,operationId = Operation.OUTPATIENT_PATIENT_MODIFY)
    private void updatePatient(Patient patient){
        if (patient != null && patient.getPatientId() != null){
            patientMapper.updatePatient(patient);
        }
    }

	@Override
	public List<Patient> searchPatientByDoctorId(Map<String, Object> map) {
		List<Patient> patientList = new ArrayList<Patient>();
    	if(map!=null){
    		patientList = patientMapper.searchPatientByDoctorId(map);
    	}
        return patientList;
	}

	@Override
	public Integer searchPatientTotalByDoctorId(Map<String, Object> map) {
		Integer total = 0;
    	if(map!=null){
    		total = patientMapper.searchPatientTotalByDoctorId(map);
    	}
        return total;
	}

	@Override
	public Patient searchDocContainPatient(Map<String, Object> map) {
		Patient patient = new Patient();
    	if(map!=null){
    		patient = patientMapper.searchDocContainPatient(map);
    	}
        return patient;
	}

	@Override
	public List<Patient> searchPatientBySug(Map<String, Object> params) {
		List<Patient> patients = new ArrayList<Patient>();
		if(params!=null)
			patients = patientMapper.searchPatientBySug(params);
		return patients;
	}

	@Override
	public Integer getPatientSeq() {
		return patientMapper.getPatientSeq();
	}

	@Override
	public List<Patient> findPatientListByIds(List<Long> patientId) {
		return patientMapper.findPatientListByIds(patientId);
	}

	@Override
	public List<Patient> searchPatientWithValidPhone(Map<String, Object> parameterMap) {
		List<Patient> patientList = new ArrayList<>();
		if (null != parameterMap)
			patientList = patientMapper.searchPatientWithValidPhone(parameterMap);
		return patientList;
	}

	@Override
	public Integer searchPatientWithValidPhoneCount(Map<String, Object> parameterMap) {
		Integer result = 0;
		if (null != parameterMap)
			result = patientMapper.searchPatientWithValidPhoneCount(parameterMap);
		return result;
	}
}
