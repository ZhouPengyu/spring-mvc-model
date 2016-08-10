package com.hm.his.module.outpatient.service;

import java.util.List;

import com.hm.his.module.outpatient.model.PatientAdditional;
import com.hm.his.module.outpatient.model.request.PatientInquiryRequest;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-2 15:16:05
 * @description 附加费用服务接口
 * @version 3.0
 */
public interface PatientAdditionalService {

	void insertPatientAdditional(PatientAdditional patientAdditional) throws Exception;

	void firstAddPatientAdditional(PatientInquiryRequest patientInquiryRequest) throws Exception;
	
	void deletePatientAdditional(Long additionalId) throws Exception;
	
	void updatePatientAdditional(PatientAdditional patientAdditional) throws Exception;
	
	List<PatientAdditional> searchPatientAdditional(PatientAdditional patientAdditional) throws Exception;
	
	public void delAdditionalByRecordId(Long recordId) throws Exception;
}
