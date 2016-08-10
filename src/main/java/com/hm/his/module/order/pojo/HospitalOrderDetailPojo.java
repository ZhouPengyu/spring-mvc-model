package com.hm.his.module.order.pojo;

import java.util.List;

/**
 * 
 * @description
 * @author lipeng
 * @date 2016年3月9日
 */
public class HospitalOrderDetailPojo extends HospitalOrderPojo {
	/**
	 * 检查治疗
	 */
	OrderExamListPojo orderExamList;
	/**
	 * 处方列表
	 */
	List<OrderPrescriptionPojo> orderPrescriptions;
	/**
	 * 附加费用单
	 */
	OrderAdditionAmtListPojo orderAdditionAmtList;
	/**
	 * 直接售药
	 */
	SellDrugRecordPojo sellDrugRecord;

	private Integer payMode;

	private Double discount;

	public OrderExamListPojo getOrderExamList() {
		return orderExamList;
	}

	public void setOrderExamList(OrderExamListPojo orderExamList) {
		this.orderExamList = orderExamList;
	}

	public List<OrderPrescriptionPojo> getOrderPrescriptions() {
		return orderPrescriptions;
	}

	public void setOrderPrescriptions(List<OrderPrescriptionPojo> orderPrescriptions) {
		this.orderPrescriptions = orderPrescriptions;
	}

	public OrderAdditionAmtListPojo getOrderAdditionAmtList() {
		return orderAdditionAmtList;
	}

	public void setOrderAdditionAmtList(OrderAdditionAmtListPojo orderAdditionAmtList) {
		this.orderAdditionAmtList = orderAdditionAmtList;
	}

	public SellDrugRecordPojo getSellDrugRecord() {
		return sellDrugRecord;
	}

	public void setSellDrugRecord(SellDrugRecordPojo sellDrugRecord) {
		this.sellDrugRecord = sellDrugRecord;
	}

	public Integer getPayMode() {
		return payMode;
	}

	public void setPayMode(Integer payMode) {
		this.payMode = payMode;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}
}
