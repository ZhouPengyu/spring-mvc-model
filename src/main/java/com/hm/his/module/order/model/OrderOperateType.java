package com.hm.his.module.order.model;

/**
 * 
 * @description 收费退费类型
 * @author lipeng
 * @date 2016年3月10日
 */
public enum OrderOperateType {
	CHARGE(1), REFUND(2);
	private Integer type;
	private OrderOperateType(Integer type) {
		this.type = type;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
}
