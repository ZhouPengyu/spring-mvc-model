package com.hm.his.module.order.pojo;

import java.util.List;

public class SellDrugRecordAndItemIds {
	Long sellDrugRecordId;
	List<Long> orderDrugIds;

	public Long getSellDrugRecordId() {
		return sellDrugRecordId;
	}

	public void setSellDrugRecordId(Long sellDrugRecordId) {
		this.sellDrugRecordId = sellDrugRecordId;
	}

	public List<Long> getOrderDrugIds() {
		return orderDrugIds;
	}

	public void setOrderDrugIds(List<Long> orderDrugIds) {
		this.orderDrugIds = orderDrugIds;
	}
}