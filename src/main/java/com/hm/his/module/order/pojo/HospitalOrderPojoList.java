package com.hm.his.module.order.pojo;

import java.util.List;

/**
 * 
 * @description
 * @author lipeng
 * @date 2016年3月1日
 */
public class HospitalOrderPojoList {
	List<HospitalOrderPojo> orders;
	Integer totalPage;

	public List<HospitalOrderPojo> getOrders() {
		return orders;
	}

	public void setOrders(List<HospitalOrderPojo> orders) {
		this.orders = orders;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}
}
