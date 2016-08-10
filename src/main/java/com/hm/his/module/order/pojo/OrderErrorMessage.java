package com.hm.his.module.order.pojo;

public enum OrderErrorMessage {
	RecordChangedOrCharged(1, "该订单已经被修改或收费"), 
	RecordChangedOrRefunded(2, "该订单已经被修改或退费"), 
	NonsupportNegative(3, "金额不能为负数"), 
	Already_Charged(4, "已经收费"),
	Already_Refunded(5, "已经退费");
	private Integer code;
	private String msg;

	private OrderErrorMessage(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
