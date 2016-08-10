package com.hm.his.module.statistics.pojo;

import java.util.List;

public class OrderStatisticsPojo {
	/**
	 * 总人次
	 */
	Integer grossImpression;
	/**
	 * 应收金额合计
	 */
	Double totalReceivableAmt;
	/**
	 * 实收金额合计
	 */
	Double totalActualAmt;
	List<OrderStatisticsItemPojo> orders;
	Integer totalPage;
	public Integer getGrossImpression() {
		return grossImpression;
	}
	public void setGrossImpression(Integer grossImpression) {
		this.grossImpression = grossImpression;
	}
	public Double getTotalReceivableAmt() {
		return totalReceivableAmt;
	}
	public void setTotalReceivableAmt(Double totalReceivableAmt) {
		this.totalReceivableAmt = totalReceivableAmt;
	}
	public Double getTotalActualAmt() {
		return totalActualAmt;
	}
	public void setTotalActualAmt(Double totalActualAmt) {
		this.totalActualAmt = totalActualAmt;
	}
	public List<OrderStatisticsItemPojo> getOrders() {
		return orders;
	}
	public void setOrders(List<OrderStatisticsItemPojo> orders) {
		this.orders = orders;
	}
	public Integer getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

}
