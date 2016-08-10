package com.hm.his.module.order.pojo;

import org.apache.commons.lang3.StringUtils;

import com.hm.his.framework.utils.SessionUtils;

/**
 * 
 * @description 查询订单列表参数
 * @author lipeng
 * @date 2016年3月7日
 */
public class GetOrderListParam {
	String startDate;
	String endDate;
	String patientName;
	Long doctorId;
	Long hospitalId;
	Integer startRecord;
	Integer pageSize;

	/**
	 * 订单中包含的费用状态
	 */
	Integer chargeStatus;
	/**
	 * 排序字段
	 */
	String orderColumn;
	/**
	 * 排序字段：创建时间
	 */
	public static final String OrderByCreateDate = "create_date";
	/**
	 * 排序字段：最后收费时间
	 */
	public static final String OrderByChargeDate = "last_charge_date";
	/**
	 * 排序字段：最后退费时间
	 */
	public static final String OrderByRefundate = "last_refund_date";

	public static GetOrderListParam get(GetOrdersRequest req, Integer includeStatus, String orderColumn) {
		GetOrderListParam param = new GetOrderListParam();
		param.setHospitalId(SessionUtils.getHospitalId());
		param.setDoctorId(req.getDoctorId());
		param.setEndDate(StringUtils.isEmpty(req.getEndDate()) ? null : req.getEndDate()+" 23:59:59");
		param.setOrderColumn(orderColumn);
		param.setPageSize(req.getPageSize());
		param.setPatientName(req.getPatientName());
		param.setStartDate(StringUtils.isEmpty(req.getStartDate()) ? null : req.getStartDate()+" 00:00:00");
		param.setStartRecord(req.getStartRecord());
		param.setChargeStatus(includeStatus);
		return param;
	}

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

	public Long getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}

	public Integer getChargeStatus() {
		return chargeStatus;
	}

	public void setChargeStatus(Integer chargeStatus) {
		this.chargeStatus = chargeStatus;
	}

	public String getOrderColumn() {
		return orderColumn;
	}

	public void setOrderColumn(String orderColumn) {
		this.orderColumn = orderColumn;
	}

	public Integer getStartRecord() {
		return startRecord;
	}

	public void setStartRecord(Integer startRecord) {
		this.startRecord = startRecord;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}
