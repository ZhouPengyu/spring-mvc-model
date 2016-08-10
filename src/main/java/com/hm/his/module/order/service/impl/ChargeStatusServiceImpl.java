package com.hm.his.module.order.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hm.his.module.order.dao.HospitalOrderMapper;
import com.hm.his.module.order.dao.OrderItemChargeMapper;
import com.hm.his.module.order.dao.OrderItemListMapper;
import com.hm.his.module.order.model.ChargeStatus;
import com.hm.his.module.order.model.HospitalOrder;
import com.hm.his.module.order.model.OrderItemCharge;
import com.hm.his.module.order.model.OrderItemList;
import com.hm.his.module.order.model.OrderItemType;
import com.hm.his.module.order.service.ChargeStatusService;

/**
 * 
 * @description
 * @author lipeng
 * @date 2016年3月13日
 */
@Service
public class ChargeStatusServiceImpl implements ChargeStatusService {
	@Autowired
	OrderItemListMapper orderItemListMapper;
	@Autowired
	HospitalOrderMapper hospitalOrderMapper;
	@Autowired
	OrderItemChargeMapper orderItemChargeMapper;

	@Override
	public void updateHospitalOrderChargeStatus(String orderNo) {
		if (StringUtils.isEmpty(orderNo)) {
			return;
		}
		// 状态
		List<OrderItemList> orderItemLists = orderItemListMapper.selectByOrderNo(orderNo);
		Integer chargeStatus = null;
		if (CollectionUtils.isNotEmpty(orderItemLists)) {
			List<Integer> statusList = orderItemLists.stream().map(itemList -> itemList.getChargeStatus()).collect(Collectors.toList());
			chargeStatus = ChargeStatus.addAll(statusList);
		}
		if (chargeStatus != null) {
			HospitalOrder hospitalOrderParam = new HospitalOrder();
			hospitalOrderParam.setOrderNo(orderNo);
			hospitalOrderParam.setChargeStatus(chargeStatus);
			hospitalOrderMapper.update(hospitalOrderParam);
		}
	}

	@Override
	public void updateHospitalOrderChargeStatus(Long recordId) {
		if (recordId == null) {
			return;
		}
		// 状态
		List<OrderItemList> orderItemLists = orderItemListMapper.selectByRecordId(recordId);
		Integer chargeStatus = null;
		if (CollectionUtils.isNotEmpty(orderItemLists)) {
			List<Integer> statusList = orderItemLists.stream().map(itemList -> itemList.getChargeStatus()).collect(Collectors.toList());
			chargeStatus = ChargeStatus.addAll(statusList);
			if (chargeStatus != null) {
				HospitalOrder hospitalOrderParam = new HospitalOrder();
				hospitalOrderParam.setPatientRecordId(recordId);
				hospitalOrderParam.setChargeStatus(chargeStatus);
				hospitalOrderMapper.updateByRecordId(hospitalOrderParam);
			}
		}
	}

	@Override
	public void updateOrderItemListChargeStatus(Long itemListId) {
		if (itemListId == null) {
			return;
		}
		// 状态
		List<OrderItemCharge> orderItemCharges = orderItemChargeMapper.selectByItemListId(itemListId);
		Integer chargeStatus = null;
		if (CollectionUtils.isNotEmpty(orderItemCharges)) {
			List<Integer> statusList = orderItemCharges.stream().map(itemList -> itemList.getChargeStatus()).collect(Collectors.toList());
			chargeStatus = ChargeStatus.addAll(statusList);
			if (chargeStatus != null) {
				OrderItemList orderItemList = new OrderItemList();
				orderItemList.setId(itemListId);
				orderItemList.setChargeStatus(chargeStatus);
				orderItemListMapper.update(orderItemList);
			}
		}
	}

	@Override
	public void updateOrderItemListChargeStatusByOrderItemIdAndType(Long itemId, OrderItemType type) {
		if (itemId == null || type == null) {
			return;
		}
		OrderItemCharge orderItemCharge = orderItemChargeMapper.selectByItemIdAndItemType(itemId, type.getType());
		if (orderItemCharge != null) {
			Long orderItemListId = orderItemCharge.getItemListId();
			this.updateOrderItemListChargeStatus(orderItemListId);
		}
	}

}
