package com.hm.his.module.outpatient.service.impl;

import java.util.List;

import com.hm.his.module.order.service.UpdateOrderService;
import com.hm.his.module.outpatient.model.request.PatientInquiryRequest;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hm.his.module.outpatient.dao.PatientAdditionalMapper;
import com.hm.his.module.outpatient.model.PatientAdditional;
import com.hm.his.module.outpatient.service.PatientAdditionalService;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-2 15:16:05
 * @description 附加费用服务接口实现类
 * @version 3.0
 */
@Service("PatientAdditionalService")
public class PatientAdditionalServiceImpl implements PatientAdditionalService {

	@Autowired(required = false)
	PatientAdditionalMapper patientAdditionalMapper;
	@Autowired(required = false)
	UpdateOrderService updateOrderService;
	
	@Override
	public void insertPatientAdditional(PatientAdditional patientAdditional) {
		patientAdditionalMapper.insertPatientAdditional(patientAdditional);
	}

	@Override
	public void firstAddPatientAdditional(PatientInquiryRequest patientInquiryRequest) throws Exception {
		List<PatientAdditional> patientAdditionalList = patientInquiryRequest.getAdditionalList();
		if(CollectionUtils.isNotEmpty(patientAdditionalList)){
			for (PatientAdditional patientAdditional : patientAdditionalList) {
				patientAdditional.setRecordId(patientInquiryRequest.getRecordId());
				patientAdditional.setFlag(1L);
			}
			patientAdditionalMapper.insertPatientAdditionalList(patientAdditionalList);
			updateOrderService.firstAddOrderAdditionAmtList(patientInquiryRequest.getRecordId(), patientAdditionalList,patientInquiryRequest.getHospitalOrder());
		}

	}

	@Override
	public void deletePatientAdditional(Long additionalId) {
		patientAdditionalMapper.deletePatientAdditional(additionalId);
	}

	@Override
	public void updatePatientAdditional(PatientAdditional patientAdditional) {
		patientAdditionalMapper.updatePatientAdditional(patientAdditional);
	}

	@Override
	public List<PatientAdditional> searchPatientAdditional(
			PatientAdditional patientAdditional) {
		return patientAdditionalMapper.searchPatientAdditional(patientAdditional);
	}

	@Override
	public void delAdditionalByRecordId(Long recordId) {
		patientAdditionalMapper.delAdditionalByRecordId(recordId);
	}

}
