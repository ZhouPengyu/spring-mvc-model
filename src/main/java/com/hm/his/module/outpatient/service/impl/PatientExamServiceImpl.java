package com.hm.his.module.outpatient.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.hm.his.module.order.service.UpdateOrderService;
import com.hm.his.module.outpatient.model.request.PatientInquiryRequest;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hm.his.module.outpatient.dao.PatientExamMapper;
import com.hm.his.module.outpatient.model.PatientExam;
import com.hm.his.module.outpatient.service.PatientExamService;

/**
 * Created by wangjialin on 15/12/14.
 */
@Service("PatientExamService")
public class PatientExamServiceImpl implements PatientExamService{
	
    @Autowired (required = false)
    PatientExamMapper patientExamMapper;
	@Autowired(required = false)
	UpdateOrderService updateOrderService;
    
    @Override
    public List<PatientExam> getExamByRecordId(Long recordId) {
        if (recordId == null) return new ArrayList<>();
        return patientExamMapper.getExamByRecordId(recordId);
    }

    @Override
    public Integer insertExamList( List<PatientExam> patientExamList) {
		return  patientExamMapper.insertExamList( patientExamList);
    }

	@Override
	public void delExamByRecordId(Long recordId) {
		patientExamMapper.delExamByRecordId(recordId);
	}

	@Override
	public void firstAddInquiryExam(PatientInquiryRequest patientInquiryRequest) {
		Long recordId = patientInquiryRequest.getRecordId();
		List<PatientExam> patientExamList = patientInquiryRequest.getExamList();
		if(CollectionUtils.isNotEmpty(patientExamList)) {
			for (PatientExam patientExam : patientExamList) {
				patientExam.setRecordId(recordId);
				patientExam.setDoctorId(patientInquiryRequest.getDoctorId());
				patientExam.setPatientId(patientInquiryRequest.getPatientId());
				patientExam.setStatus(0L);
			}
			this.insertExamList(patientExamList);
			//批量保存订单的检查项
			updateOrderService.firstAddOrderExamList(recordId, patientExamList, patientInquiryRequest.getHospitalOrder());
		}
	}

	@Override
	public Long insertExam(PatientExam patientExam) {
		return patientExamMapper.insertExam(patientExam);
	}

	@Override
	public Long updateExam(PatientExam patientExam) {
		return patientExamMapper.updateExam(patientExam);
	}

	@Override
	public Long deleteExam(Long patientExamId) {
		return patientExamMapper.deleteExam(patientExamId);
	}
    
}
