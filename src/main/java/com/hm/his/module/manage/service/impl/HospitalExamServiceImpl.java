package com.hm.his.module.manage.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hm.his.framework.log.annotation.SystemLogAnno;
import com.hm.his.framework.log.constant.FunctionConst;
import com.hm.his.framework.log.constant.Operation;
import com.hm.his.module.manage.dao.HospitalExamMapper;
import com.hm.his.module.manage.model.HospitalExam;
import com.hm.his.module.manage.service.HospitalExamService;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-8 15:34:15
 * @description 医院检查接口实现类
 * @version 3.0
 */
@Service("HospitalExamService")
public class HospitalExamServiceImpl implements HospitalExamService{
    @Autowired (required = false)
    HospitalExamMapper hospitalExamMapper;

	@Override
	@SystemLogAnno(functionId = FunctionConst.CLINIC_OPER_LOG,operationId = Operation.CLINIC_EXAM_ADD)
	public Integer insertHospitalExam(HospitalExam hospitalExam) {
		return hospitalExamMapper.insertHospitalExam(hospitalExam);
	}

	@Override
	@SystemLogAnno(functionId = FunctionConst.CLINIC_OPER_LOG,operationId = Operation.CLINIC_EXAM_DELETE)
	public Integer deleteHospitalExam(Long examId, Long hospitalId) {
		return hospitalExamMapper.deleteHospitalExam(examId, hospitalId);
	}

	@Override
	@SystemLogAnno(functionId = FunctionConst.CLINIC_OPER_LOG,operationId = Operation.CLINIC_EXAM_MODIFY)
	public Integer updateHospitalExam(HospitalExam hospitalExam) {
		return hospitalExamMapper.updateHospitalExam(hospitalExam);
	}

	@Override
	public Integer verifyExamName(String examName, Long hospitalId) {
		return hospitalExamMapper.verifyExamName(examName, hospitalId);
	}

	@Override
	public List<HospitalExam> searchHospitalExam(
			Map<String, Object> requestParams) {
		return hospitalExamMapper.searchHospitalExam(requestParams);
	}

	@Override
	public List<HospitalExam> searchHospitalExamByIds(List<Long> examIds) throws Exception {
		if(CollectionUtils.isNotEmpty(examIds))
			return hospitalExamMapper.searchHospitalExamByIds(examIds);
		return null;
	}

	@Override
	public Integer searchHospitalExamTotal(Map<String, Object> requestParams) {
		return hospitalExamMapper.searchHospitalExamTotal(requestParams);
	}

	@Override
	public void insertDefaultExam(Long hospitalId, Long doctorId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("hospitalId", 122L);
		params.put("startPage", 0);
		params.put("pageSize", 10);
		List<HospitalExam> hospitalList = this.searchHospitalExam(params);
		for (HospitalExam hospitalExam : hospitalList) {
			hospitalExam.setHospitalId(hospitalId);
			hospitalExam.setCreater(doctorId);
			insertHospitalExam(hospitalExam);
		}
	}
}
