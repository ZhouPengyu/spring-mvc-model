package com.hm.his.module.statistics.pojo;

public enum StatisticsErrorMessage {
	Null(1, "没有查询到数据");
	private Integer code;
	private String msg;

	private StatisticsErrorMessage(Integer code, String msg) {
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
