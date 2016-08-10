package com.hm.his.module.order.model;

/**
 * 
 * @description 
 * @author lipeng
 * @date 2016年2月29日
 */
public enum OrderItemListType {
	/**
	 * 西药处方
	 */
	PATIENT_PRESCRIPTION(1),
	/**
	 * 中药处方
	 */
	CHINESE_PRESCRIPTION(2),
	/**
	 * 检查
	 */
	EXAM (3),
	/**
	 * 附加费用
	 */
	ADDITION_AMT (4),
	/**
	 * 直接售药
	 */
	SELL_DRUG (5);
	
	private Integer type;
	private  OrderItemListType(Integer type){
		this.type=type;
	}
	public Integer getType() {
		return type;
	}
}
