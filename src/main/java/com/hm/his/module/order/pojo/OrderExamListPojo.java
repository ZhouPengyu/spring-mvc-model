package com.hm.his.module.order.pojo;

import java.util.List;

import com.hm.his.module.order.model.OrderExam;

public class OrderExamListPojo {
	Long orderExamListId;
	List<OrderExam> orderExams;
	public Long getOrderExamListId() {
		return orderExamListId;
	}
	public void setOrderExamListId(Long orderExamListId) {
		this.orderExamListId = orderExamListId;
	}
	public List<OrderExam> getOrderExams() {
		return orderExams;
	}
	public void setOrderExams(List<OrderExam> orderExams) {
		this.orderExams = orderExams;
	}
	
}
