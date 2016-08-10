package com.hm.his.module.order.pojo;

import java.util.List;

/**
 * 
 * @description 退费请求
 * @author lipeng
 * @date 2016年3月1日
 */
public class RefundRequest {
	String orderNo;
	OrderExamListAndItemIds orderExamList;
	List<PrescriptionAndItemIds> orderPrescriptions;
	OrderAdditionAmtListAndItemIds orderAdditionAmtList;
	SellDrugRecordAndItemIds sellDrugRecord;
	Double refundAmt;
	Long recordVersion;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}


	public Double getRefundAmt() {
		return refundAmt;
	}

	public void setRefundAmt(Double refundAmt) {
		this.refundAmt = refundAmt;
	}

	public OrderExamListAndItemIds getOrderExamList() {
		return orderExamList;
	}

	public void setOrderExamList(OrderExamListAndItemIds orderExamList) {
		this.orderExamList = orderExamList;
	}

	public List<PrescriptionAndItemIds> getOrderPrescriptions() {
		return orderPrescriptions;
	}

	public void setOrderPrescriptions(List<PrescriptionAndItemIds> orderPrescriptions) {
		this.orderPrescriptions = orderPrescriptions;
	}

	public OrderAdditionAmtListAndItemIds getOrderAdditionAmtList() {
		return orderAdditionAmtList;
	}

	public void setOrderAdditionAmtList(OrderAdditionAmtListAndItemIds orderAdditionAmtList) {
		this.orderAdditionAmtList = orderAdditionAmtList;
	}

	public SellDrugRecordAndItemIds getSellDrugRecord() {
		return sellDrugRecord;
	}

	public void setSellDrugRecord(SellDrugRecordAndItemIds sellDrugRecord) {
		this.sellDrugRecord = sellDrugRecord;
	}

	public Long getRecordVersion() {
		return recordVersion;
	}

	public void setRecordVersion(Long recordVersion) {
		this.recordVersion = recordVersion;
	}

}
