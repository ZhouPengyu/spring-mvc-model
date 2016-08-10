package com.hm.his.module.order.pojo;

import java.util.List;

import com.hm.his.module.order.model.OrderDrug;

public class OrderPrescriptionPojo {
	Long orderPrescriptionId;
	/**
	 * 处方类型:1 成药; 2 饮片
	 */
	Integer presciptionType;
	List<OrderDrug> orderDrugs;
	private String herbDailyDosage;
	private String herbUsage;

	public Long getOrderPrescriptionId() {
		return orderPrescriptionId;
	}

	public void setOrderPrescriptionId(Long orderAdditionAmtListId) {
		this.orderPrescriptionId = orderAdditionAmtListId;
	}

	public List<OrderDrug> getOrderDrugs() {
		return orderDrugs;
	}

	public void setOrderDrugs(List<OrderDrug> orderDrugs) {
		this.orderDrugs = orderDrugs;
	}

	public Integer getPresciptionType() {
		return presciptionType;
	}

	public void setPresciptionType(Integer presciptionId) {
		this.presciptionType = presciptionId;
	}

	public String getHerbDailyDosage() {
		return herbDailyDosage;
	}

	public void setHerbDailyDosage(String herbDailyDosage) {
		this.herbDailyDosage = herbDailyDosage;
	}

	public String getHerbUsage() {
		return herbUsage;
	}

	public void setHerbUsage(String herbUsage) {
		this.herbUsage = herbUsage;
	}
}
