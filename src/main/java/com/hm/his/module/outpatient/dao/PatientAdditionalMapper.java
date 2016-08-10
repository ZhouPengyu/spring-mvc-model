package com.hm.his.module.outpatient.dao;

import java.util.List;

import com.hm.his.module.outpatient.model.PatientAdditional;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-7 10:55:16
 * @description 附加费用 数据库实现类
 * @version 3.0
 */
public interface PatientAdditionalMapper {

	void insertPatientAdditional(PatientAdditional patientAdditional);

	void insertPatientAdditionalList(List<PatientAdditional> patientAdditionals);
	
	void deletePatientAdditional(Long additionalId);
	
	void updatePatientAdditional(PatientAdditional patientAdditional);
	
	List<PatientAdditional> searchPatientAdditional(PatientAdditional patientAdditional);
	
	public void delAdditionalByRecordId(Long recordId);
	
	
}
