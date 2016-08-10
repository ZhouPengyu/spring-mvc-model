package com.hm.his.module.order.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hm.his.module.order.dao.HospitalOrderMapper;
import com.hm.his.module.order.model.ChargeStatus;
import com.hm.his.module.order.model.HospitalOrder;
import com.hm.his.module.order.model.OrderAdditionAmt;
import com.hm.his.module.order.model.OrderExam;
import com.hm.his.module.order.model.OrderItemList;
import com.hm.his.module.order.model.OrderItemListType;
import com.hm.his.module.order.service.GetIsChargedService;
import com.hm.his.module.order.service.OrderAdditionAmtService;
import com.hm.his.module.order.service.OrderExamService;
import com.hm.his.module.order.service.OrderItemListService;

/**
 * 
 * @description
 * @author lipeng
 * @date 2016年3月11日
 */
@Service
public class GetIsChargedServiceImpl implements GetIsChargedService {
	@Autowired
	OrderItemListService orderItemListService;
	@Autowired
	OrderExamService orderExamService;
	@Autowired
	OrderAdditionAmtService orderAdditionAmtService;
	@Autowired
	HospitalOrderMapper hospitalOrderMapper;

	@Override
	public int isCharged(Long recordId, Long id, OrderItemListType type) {
		HospitalOrder hospitalOrder = hospitalOrderMapper.selectByRecordId(recordId);
		if (hospitalOrder == null) {
			return 0;
		}
		Integer chargeStatus = null;
		if (type == OrderItemListType.ADDITION_AMT) {
			// 附加费用
			OrderAdditionAmt orderAdditionAmt = orderAdditionAmtService.getOrderAdditionAmtByRecordIdAndAdditionalId(recordId, id);
			if (orderAdditionAmt != null) {
				chargeStatus = orderAdditionAmt.getChargeStatus();
			}
		} else if (type == OrderItemListType.EXAM) {
			// 检查项
			OrderExam orderExam = orderExamService.getOrderExamByRecordIdAndPatientExamId(recordId, id);
			if (orderExam != null) {
				chargeStatus = orderExam.getChargeStatus();
			}

		} else if (type == OrderItemListType.CHINESE_PRESCRIPTION || type == OrderItemListType.PATIENT_PRESCRIPTION) {
			// 处方
			OrderItemList orderItemList = orderItemListService.getOrderItemListByRecordIdForeignId(recordId, id);
			if (orderItemList != null) {
				chargeStatus = orderItemList.getChargeStatus();
			}
		}
		if (chargeStatus != null) {
			return ChargeStatus.hasStatus(chargeStatus, ChargeStatus.CHARGED) ? 1 : 0;
		}
		return 0;
	}
}
