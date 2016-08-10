package com.hm.his.module.order.pojo;

import java.util.List;

/**
 * 
 * @description 收费请求参数
 * @author lipeng
 * @date 2016年3月1日
 */
public class ChargeRequest {
	String orderNo;
	Long recordVersion;
	Long orderAdditionAmtListId;
	Long orderExamListId;
	List<Long> orderPrescriptionIds;
	Long sellDrugRecordId;
	Double actualAmt;
	/**
	 * 优惠金额
	 */
	Double discount;
	/**
	 * 付款方式
	 * 1. 现金 2 微信 3支付宝 4 银行卡
	 */
	Integer payMode;
	/**
	 * 付款金额
	 */
	Double payAmount;

	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Long getRecordVersion() {
		return recordVersion;
	}
	public void setRecordVersion(Long recordVersion) {
		this.recordVersion = recordVersion;
	}
	public Long getOrderAdditionAmtListId() {
		return orderAdditionAmtListId;
	}
	public void setOrderAdditionAmtListId(Long orderAdditionAmtListId) {
		this.orderAdditionAmtListId = orderAdditionAmtListId;
	}
	public Long getOrderExamListId() {
		return orderExamListId;
	}
	public void setOrderExamListId(Long orderExamListId) {
		this.orderExamListId = orderExamListId;
	}
	public List<Long> getOrderPrescriptionIds() {
		return orderPrescriptionIds;
	}
	public void setOrderPrescriptionIds(List<Long> orderPrescriptionIds) {
		this.orderPrescriptionIds = orderPrescriptionIds;
	}
	public Long getSellDrugRecordId() {
		return sellDrugRecordId;
	}
	public void setSellDrugRecordId(Long sellDrugRecordId) {
		this.sellDrugRecordId = sellDrugRecordId;
	}
	public Double getActualAmt() {
		return actualAmt;
	}
	public void setActualAmt(Double actualAmt) {
		this.actualAmt = actualAmt;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Integer getPayMode() {
		return payMode;
	}

	public void setPayMode(Integer payMode) {
		this.payMode = payMode;
	}

	public Double getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
	}
}
