package com.hm.his.module.order.pojo;

import java.util.List;

public class PrescriptionAndItemIds {
	Long orderPrescriptionId;
	List<Long> orderDrugIds;

	public Long getOrderPrescriptionId() {
		return orderPrescriptionId;
	}

	public void setOrderPrescriptionId(Long orderPrescriptionId) {
		this.orderPrescriptionId = orderPrescriptionId;
	}

	public List<Long> getOrderDrugIds() {
		return orderDrugIds;
	}

	public void setOrderDrugIds(List<Long> orderDrugIds) {
		this.orderDrugIds = orderDrugIds;
	}
}