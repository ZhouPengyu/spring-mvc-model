package com.hm.his.module.statistics.pojo;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 
 * @description
 * @author lipeng
 * @date 2016年3月24日
 */
public class OrderStatisticsItemPojo {
	String orderNo;
	Integer orderType;
	Long recordId;
	Long patientId;
	Long sellDrugRecordId;
	String patientName;
	String gender;
	Double age;
	String ageType;
	String phoneNo;
	String doctorName;
	String diagnosis;
	@JSONField(format = "yyyy-MM-dd")
	Date chargeDate;
	Double receivableAmt;
	Double actualAmt;
	/**
	 * 年龄描述
	 */
	String ageDesc;
	/**
	 * 付款方式
	 */
	String payModeName;

	/*******************************中间值**********************************/
	/**
	 * 患者信息 = gender,phoneNo,age,ageType
	 */
	@JSONField(serialize = false)
	String patientInfo;
	
	/**
	 * 订单实收金额
	 */
	@JSONField(serialize = false)
	Double orderActualAmt;
	/**
	 * 订单实退金额
	 */
	@JSONField(serialize = false)
	Double orderRefundAmt;
	/**
	 * 订单中未收费项目的 应收金额
	 */
	@JSONField(serialize = false)
	Double orderReceivableAmtAmt;
	/**
	 * 订单中全部项目的应收金额
	 */
	@JSONField(serialize = false)
	Double orderTotalReceivableAmtAmt;
	/**
	 * 订单退费项目的应退金额
	 */
	@JSONField(serialize = false)
	Double orderRefundReceivableAmtAmt;

	/**
	 * 付款方式编号	
	 */
	@JSONField(serialize = false)
	Integer payMode;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public Long getSellDrugRecordId() {
		return sellDrugRecordId;
	}

	public void setSellDrugRecordId(Long sellDrugRecordId) {
		this.sellDrugRecordId = sellDrugRecordId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Double getAge() {
		return age;
	}

	public void setAge(Double age) {
		this.age = age;
	}

	public String getAgeType() {
		return ageType;
	}

	public void setAgeType(String ageType) {
		this.ageType = ageType;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public Date getChargeDate() {
		return chargeDate;
	}

	public void setChargeDate(Date chargeDate) {
		this.chargeDate = chargeDate;
	}

	public Double getReceivableAmt() {
		return receivableAmt;
	}

	public void setReceivableAmt(Double receivableAmt) {
		this.receivableAmt = receivableAmt;
	}

	public Double getActualAmt() {
		return actualAmt;
	}

	public void setActualAmt(Double actualAmt) {
		this.actualAmt = actualAmt;
	}

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public String getAgeDesc() {
		if (age == null) {
			return "";
		}
		if (ageType == null) {
			ageType = "";
		}
		return age + ageType;
	}

	public void setAgeDesc(String ageDesc) {
		this.ageDesc = ageDesc;
	}

	public String getChargeDateDesc() {
		if (chargeDate == null) {
			return "";
		}
		String re = "";
		try {
			re = DateFormatUtils.format(chargeDate, "yyyy-MM-dd");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return re;
	}

	public String getPayModeName() {
		return payModeName;
	}

	public void setPayModeName(String payModeName) {
		this.payModeName = payModeName;
	}

	public Integer getPayMode() {
		return payMode;
	}

	public void setPayMode(Integer payMode) {
		this.payMode = payMode;
	}

	public Double getOrderActualAmt() {
		return orderActualAmt;
	}

	public void setOrderActualAmt(Double orderActualAmt) {
		this.orderActualAmt = orderActualAmt;
	}

	public Double getOrderRefundAmt() {
		return orderRefundAmt;
	}

	public void setOrderRefundAmt(Double orderRefundAmt) {
		this.orderRefundAmt = orderRefundAmt;
	}

	public Double getOrderReceivableAmtAmt() {
		return orderReceivableAmtAmt;
	}

	public void setOrderReceivableAmtAmt(Double orderReceivableAmtAmt) {
		this.orderReceivableAmtAmt = orderReceivableAmtAmt;
	}

	public Double getOrderRefundReceivableAmtAmt() {
		return orderRefundReceivableAmtAmt;
	}

	public void setOrderRefundReceivableAmtAmt(Double orderRefundReceivableAmtAmt) {
		this.orderRefundReceivableAmtAmt = orderRefundReceivableAmtAmt;
	}

	public Double getOrderTotalReceivableAmtAmt() {
		return orderTotalReceivableAmtAmt;
	}

	public void setOrderTotalReceivableAmtAmt(Double orderTotalReceivableAmtAmt) {
		this.orderTotalReceivableAmtAmt = orderTotalReceivableAmtAmt;
	}
	/**
	 * gender,phoneNo,age,ageType
	 * @date 2016年7月15日
	 * @author lipeng
	 * @return
	 */
	public String getPatientInfo() {
		return patientInfo;
	}

	public void setPatientInfo(String patientInfo) {
		this.patientInfo = patientInfo;
	}
}
