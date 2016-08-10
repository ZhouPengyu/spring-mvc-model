package com.hm.his.module.order.pojo;

import java.util.List;

import com.hm.his.module.order.model.OrderDrug;
/**
 * 
 * @description
 * @author lipeng
 * @date 2016年3月1日
 */
public class SellDrugRecordPojo {
	Long sellDrugRecordId;
	List<OrderDrug> orderDrugs;
	public Long getSellDrugRecordId() {
		return sellDrugRecordId;
	}
	public void setSellDrugRecordId(Long sellDrugRecordId) {
		this.sellDrugRecordId = sellDrugRecordId;
	}
	public List<OrderDrug> getOrderDrugs() {
		return orderDrugs;
	}
	public void setOrderDrugs(List<OrderDrug> orderDrugs) {
		this.orderDrugs = orderDrugs;
	}
	
}
