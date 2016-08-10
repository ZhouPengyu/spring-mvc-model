package com.hm.his.module.order.pojo;

import java.util.List;

public class OrderAdditionAmtListAndItemIds {
	Long orderAdditionAmtListId;
	List<Long> orderAdditionAmtIds;

	public Long getOrderAdditionAmtListId() {
		return orderAdditionAmtListId;
	}

	public void setOrderAdditionAmtListId(Long orderAdditionAmtListId) {
		this.orderAdditionAmtListId = orderAdditionAmtListId;
	}

	public List<Long> getOrderAdditionAmtIds() {
		return orderAdditionAmtIds;
	}

	public void setOrderAdditionAmtIds(List<Long> orderAdditionAmtIds) {
		this.orderAdditionAmtIds = orderAdditionAmtIds;
	}
}