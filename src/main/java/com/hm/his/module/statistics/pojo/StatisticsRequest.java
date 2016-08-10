package com.hm.his.module.statistics.pojo;

import org.apache.commons.lang3.StringUtils;

import com.hm.his.framework.model.PageRequest;
import com.hm.his.framework.utils.SessionUtils;

/**
 * 
 * @description
 * @author lipeng
 * @date 2016年3月24日
 */
public class StatisticsRequest extends PageRequest {
	String startDate;
	String endDate;
	/**
	 * 药物类型 参考drugTypeEnum
	 */
	Integer drugType;
	/**
	 * 销售渠道，参考SaleChannel
	 */
	Integer saleChannel;
	String drugName;
	String examName;
	String additionAmtName;

	String patientName;
	String phoneNo;
	Long doctorId;
	String diseaseName;

	/**
	 * 医院id ，从session中获取
	 */
	Long hospitalId;
	/**
	 * 当前登录用户id
	 */
	Long loginId;

	Integer payMode;

	/**
	 * 医生姓名
	 */
	String doctorName;


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

	public Integer getDrugType() {
		return drugType;
	}

	public void setDrugType(Integer drugType) {
		this.drugType = drugType;
	}

	public Integer getSaleChannel() {
		return saleChannel;
	}

	public void setSaleChannel(Integer saleChannel) {
		this.saleChannel = saleChannel;
	}

	public String getDrugName() {
		return drugName;
	}

	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}

	public String getExamName() {
		return examName;
	}

	public void setExamName(String examName) {
		this.examName = examName;
	}

	public String getAdditionAmtName() {
		return additionAmtName;
	}

	public void setAdditionAmtName(String additionAmtName) {
		this.additionAmtName = additionAmtName;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public Long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}

	public String getDiseaseName() {
		return diseaseName;
	}

	public void setDiseaseName(String diseaseName) {
		this.diseaseName = diseaseName;
	}

	public Long getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}

	public Long getLoginId() {
		return loginId;
	}

	public void setLoginId(Long loginId) {
		this.loginId = loginId;
	}

	public static void handleStatisticsRequest(StatisticsRequest req) {
		req.setLoginId(SessionUtils.getDoctorId());
		if (req.getHospitalId() == null){
			req.setHospitalId(SessionUtils.getHospitalId());
		}
		req.setStartDate(StringUtils.isEmpty(req.getStartDate()) ? null : req.getStartDate() + " 00:00:00");
		req.setEndDate(StringUtils.isEmpty(req.getEndDate()) ? null : req.getEndDate() + " 23:59:59");
	}

	public Integer getPayMode() {
		return payMode;
	}

	public void setPayMode(Integer payMode) {
		this.payMode = payMode;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

}
