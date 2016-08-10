package com.hm.his.module.order.pojo;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.hm.his.module.order.model.OrderDrug;

/**
 * 
 * @description 直接售药详情
 * @author lipeng
 * @date 2016年3月30日
 */
public class SellDrugInfoPojo {
	@JSONField(format = "yyyy-MM-dd")
	Date chargeDate;
	String hospitalName;
	String chargeName;
	String patientName;
	Double age;
	String ageType;
	String gender;
	/**
	 * 身份证号
	 */
	String IDCardNo;
	/**
	 * 联系方式
	 */
	String phoneNo;

	List<OrderDrug> drugs;

	public Date getChargeDate() {
		return chargeDate;
	}

	public void setChargeDate(Date chargeDate) {
		this.chargeDate = chargeDate;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getChargeName() {
		return chargeName;
	}

	public void setChargeName(String chargeName) {
		this.chargeName = chargeName;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getIDCardNo() {
		return IDCardNo;
	}

	public void setIDCardNo(String iDCardNo) {
		IDCardNo = iDCardNo;
	}

	public List<OrderDrug> getDrugs() {
		return drugs;
	}

	public void setDrugs(List<OrderDrug> drugs) {
		this.drugs = drugs;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
}
