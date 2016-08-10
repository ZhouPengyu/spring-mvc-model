package com.hm.his.module.order.pojo;

public class OrderTotalAmtPojo {
	/**
	 * 订单总实收金额（实收-实退）
	 */
	Double totalFinalActualAmt;
	/**
	 * 订单中已经收过费的项目的应收金额合计
	 */
	Double totalAlreadyReceivableAmt;

	public Double getTotalFinalActualAmt() {
		return totalFinalActualAmt;
	}

	public void setTotalFinalActualAmt(Double totalFinalActualAmt) {
		this.totalFinalActualAmt = totalFinalActualAmt;
	}

	public Double getTotalAlreadyReceivableAmt() {
		return totalAlreadyReceivableAmt;
	}

	public void setTotalAlreadyReceivableAmt(Double totalAlreadyReceivableAmt) {
		this.totalAlreadyReceivableAmt = totalAlreadyReceivableAmt;
	}

}
