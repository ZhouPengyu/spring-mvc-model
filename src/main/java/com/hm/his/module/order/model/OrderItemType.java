package com.hm.his.module.order.model;

public enum OrderItemType {
	EXAM(1), DRUG(2), ADDITION_AMT(3);

	private int type;

	private OrderItemType(int type) {
		this.type=type;
	}

	public int getType() {
		return type;
	}
	
}
