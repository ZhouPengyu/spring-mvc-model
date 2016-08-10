package com.hm.his.module.order.pojo;

import com.hm.his.framework.model.PageRequest;

/**
 * 
 * @description 查询订单请求
 * @author lipeng
 * @date 2016年3月1日
 */
public class GetOrdersRequest extends PageRequest {
	String startDate;
	String endDate;
	String patientName;
	Long doctorId;
	String orderNo;
	/**
	 * 查询类型：1：详情；2：收费，3：退费
	 */
	Integer queryType;

	public static final Integer INFO = 1;
	public static final Integer CHARGE = 2;
	public static final Integer REFUND = 3;

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public Long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getQueryType() {
		return queryType;
	}

	public void setQueryType(Integer queryType) {
		this.queryType = queryType;
	}

}
