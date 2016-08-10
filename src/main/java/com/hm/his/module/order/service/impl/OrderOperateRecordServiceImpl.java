package com.hm.his.module.order.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.module.order.dao.OrderOperateRecordMapper;
import com.hm.his.module.order.model.OrderOperateRecord;
import com.hm.his.module.order.model.OrderOperateType;
import com.hm.his.module.order.model.OrderOperateRecord.OperateItem;
import com.hm.his.module.order.service.OrderOperateRecordService;

/**
 * @description
 * @author lipeng
 * @date 2016年3月4日
 */
@Service
public class OrderOperateRecordServiceImpl implements OrderOperateRecordService {
	@Autowired(required = false)
	OrderOperateRecordMapper orderOperateRecordMapper;

	@Override
	public void addChargeRecord(String orderNo, Double actualAmt, Double receivableAmt, List<OperateItem> itemList, Double payAmount) {
		OrderOperateRecord orderOperateRecord = new OrderOperateRecord();
		orderOperateRecord.setCreater(SessionUtils.getDoctorId());
		orderOperateRecord.setItemList(itemList);
		orderOperateRecord.setOrderNo(orderNo);
		orderOperateRecord.setOperateType(OrderOperateType.CHARGE.getType());
		orderOperateRecord.setActualAmt(actualAmt);
		orderOperateRecord.setReceivableAmt(receivableAmt);
		orderOperateRecord.setPayAmount(payAmount);
		orderOperateRecordMapper.insert(orderOperateRecord);
	}

	@Override
	public void addRefundRecord(String orderNo, Double actualAmt, Double receivableAmt, List<OperateItem> itemList) {
		OrderOperateRecord orderOperateRecord = new OrderOperateRecord();
		orderOperateRecord.setCreater(SessionUtils.getDoctorId());
		orderOperateRecord.setItemList(itemList);
		orderOperateRecord.setOrderNo(orderNo);
		orderOperateRecord.setOperateType(OrderOperateType.REFUND.getType());
		orderOperateRecord.setActualAmt(actualAmt);
		orderOperateRecord.setReceivableAmt(receivableAmt);
		orderOperateRecordMapper.insert(orderOperateRecord);
	}

}
