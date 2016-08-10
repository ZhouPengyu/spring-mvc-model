package com.hm.his.module.order.pojo;

import java.util.List;

public class OrderExamListAndItemIds {
	Long orderExamListId;
	List<Long> orderExamIds;

	public Long getOrderExamListId() {
		return orderExamListId;
	}

	public void setOrderExamListId(Long orderExamListId) {
		this.orderExamListId = orderExamListId;
	}

	public List<Long> getOrderExamIds() {
		return orderExamIds;
	}

	public void setOrderExamIds(List<Long> orderExamIds) {
		this.orderExamIds = orderExamIds;
	}
}
