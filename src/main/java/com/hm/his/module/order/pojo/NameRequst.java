package com.hm.his.module.order.pojo;

import com.hm.his.framework.model.PageRequest;

/**
 * 
 * @description
 * @author lipeng
 * @date 2016年3月30日
 */
public class NameRequst extends PageRequest {
	String name;
	Long hospitalId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}
}
