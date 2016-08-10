package com.hm.his.module.manage.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hm.his.framework.log.annotation.SystemLogAnno;
import com.hm.his.framework.log.constant.FunctionConst;
import com.hm.his.framework.log.constant.Operation;
import com.hm.his.module.manage.dao.LaboratoryMapper;
import com.hm.his.module.manage.model.Laboratory;
import com.hm.his.module.manage.service.LaboratoryService;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-7 10:55:16
 * @description 医院科室  实现类
 * @version 3.0
 */
@Service("LaboratoryService")
public class LaboratoryServiceImpl implements LaboratoryService {

	@Autowired(required = false)
	LaboratoryMapper laboratoryMapper;
	
	@Override
	@SystemLogAnno(functionId = FunctionConst.CLINIC_OPER_LOG,operationId = Operation.CLINIC_LABORATORY_ADD)
	public Integer insertLaboratory(Laboratory laboratory) {
		return laboratoryMapper.insertLaboratory(laboratory);
	}

	@Override
	@SystemLogAnno(functionId = FunctionConst.CLINIC_OPER_LOG,operationId = Operation.CLINIC_LABORATORY_DELETE)
	public Integer deleteLaboratory(Long LaboratoryId, Long hospitalId) {
		return laboratoryMapper.deleteLaboratory(LaboratoryId, hospitalId);
	}

	@Override
	@SystemLogAnno(functionId = FunctionConst.CLINIC_OPER_LOG,operationId = Operation.CLINIC_LABORATORY_MODIFY)
	public Integer updateLaboratory(Laboratory laboratory) {
		return laboratoryMapper.updateLaboratory(laboratory);
	}

	@Override
	public List<Laboratory> searchLaboratory(Map<String, Object> requestParams) {
		return laboratoryMapper.searchLaboratory(requestParams);
	}

	@Override
	public Integer searchLaboratoryTotal(Map<String, Object> requestParams) {
		return laboratoryMapper.searchLaboratoryTotal(requestParams);
	}

	@Override
	public Integer verifyLaboratoryName(String laboratoryName, Long hospitalId) {
		return laboratoryMapper.verifyLaboratoryName(laboratoryName, hospitalId);
	}

}
