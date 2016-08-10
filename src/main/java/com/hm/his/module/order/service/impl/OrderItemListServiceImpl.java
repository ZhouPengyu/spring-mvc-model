package com.hm.his.module.order.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hm.his.module.order.dao.OrderItemChargeMapper;
import com.hm.his.module.order.dao.OrderItemListMapper;
import com.hm.his.module.order.model.OrderItem;
import com.hm.his.module.order.model.OrderItemList;
import com.hm.his.module.order.model.OrderItemListType;
import com.hm.his.module.order.service.OrderAdditionAmtService;
import com.hm.his.module.order.service.OrderDrugService;
import com.hm.his.module.order.service.OrderExamService;
import com.hm.his.module.order.service.OrderItemListService;

@Service
public class OrderItemListServiceImpl<T extends OrderItem> implements OrderItemListService {
	@Autowired
	OrderItemListMapper orderItemListMapper;
	@Autowired
	OrderAdditionAmtService orderAdditionAmtService;
	@Autowired
	OrderExamService orderExamService;
	@Autowired
	OrderDrugService orderDrugService;
	@Autowired
	OrderItemChargeMapper orderItemChargeMapper;

	@Override
	public List<OrderItemList> getOrderItemListsByOrderNoAndChargeStatus(String orderNo, Integer chargeStatus) {
		return orderItemListMapper.selectByOrderNoAndChargeStatus(orderNo, chargeStatus);
	}

	@Override
	public OrderItemList getOrderItemListById(Long id) {
		return orderItemListMapper.selectById(id);
	}

	@Override
	public OrderItemList getOrderItemListByRecordIdForeignId(Long recordId, Long foreignId) {
		return orderItemListMapper.selectByRecordIdAndForeignId(recordId, foreignId);
	}

	@Override
	public void updateOrderItemListChargeSatus(Long id, Integer chargeStatus) {
		orderItemListMapper.updateOrderItemListChargeSatus(id, chargeStatus);
	}

	@Override
	public List<OrderItemList> getOrderItemListsByOrderNo(String orderNo) {
		return orderItemListMapper.selectByOrderNo(orderNo);
	}

	@Override
	public void deleteOrderItemList(OrderItemList orderItemList) {
		if (orderItemList == null) {
			return;
		}
		Long id = orderItemList.getId();
		Integer type = orderItemList.getType();
		orderItemListMapper.delete(id);
		if (type == OrderItemListType.ADDITION_AMT.getType()) {
			orderAdditionAmtService.deleteOrderAdditionAmtsByOrderItemListId(id);
		} else if (type == OrderItemListType.EXAM.getType()) {
			orderExamService.deleteOrderExamsByOrderItemListId(id);
		} else if (type == OrderItemListType.PATIENT_PRESCRIPTION.getType() || type == OrderItemListType.CHINESE_PRESCRIPTION.getType()) {
			orderDrugService.deleteOrderDrugsByOrderItemListId(id);
		}
		orderItemChargeMapper.deleteByItemListId(id);
	}

}
