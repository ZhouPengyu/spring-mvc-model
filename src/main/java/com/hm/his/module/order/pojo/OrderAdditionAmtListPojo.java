package com.hm.his.module.order.pojo;

import java.util.List;

import com.hm.his.module.order.model.OrderAdditionAmt;

public class OrderAdditionAmtListPojo {
	Long orderAdditionAmtListId;
	List<OrderAdditionAmt> orderAdditionAmts;
	public Long getOrderAdditionAmtListId() {
		return orderAdditionAmtListId;
	}
	public void setOrderAdditionAmtListId(Long orderAdditionAmtListId) {
		this.orderAdditionAmtListId = orderAdditionAmtListId;
	}
	public List<OrderAdditionAmt> getOrderAdditionAmts() {
		return orderAdditionAmts;
	}
	public void setOrderAdditionAmts(List<OrderAdditionAmt> orderAdditionAmts) {
		this.orderAdditionAmts = orderAdditionAmts;
	}
	
}
