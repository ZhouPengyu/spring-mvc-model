package com.hm.his.module.order.pojo;

import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.hm.his.framework.utils.AmtUtils;

/**
 * 
 * @description
 * @author lipeng
 * @date 2016年3月1日
 */
public class HospitalOrderPojo {
	String patientName;
	String gender;
	Double age;
	String ageType;
	String diagnosis;
	String doctorName;
	@JSONField(format = "yyyy-MM-dd")
	Date diagnosisDate;
	/**
	 * 应收金额
	 */
	Double receivableAmt;
	String orderNo;
	/**
	 * 操作日期
	 */
	@JSONField(format = "yyyy-MM-dd")
	Date operateDate;
	/**
	 * 实收金额
	 */
	Double actualAmt;
	/**
	 * 实退金额
	 */
	Double refundAmt;
	/**
	 * 操作人
	 */
	String operatorName;
	/**
	 * 病历版本，每次新增或修改病历更新版本，收费时比较版本，不一致表示版本信息过期
	 */
	Long recordVersion;
	/**
	 * 最后收费人
	 */
	String chargeName;
	/**
	 * 最后收费日期
	 */
	@JSONField(format = "yyyy-MM-dd")
	Date chargeDate;

	private String payModeName;

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

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public Date getDiagnosisDate() {
		return diagnosisDate;
	}

	public void setDiagnosisDate(Date diagnosisDate) {
		this.diagnosisDate = diagnosisDate;
	}

	public Double getReceivableAmt() {
		if (receivableAmt != null) {
			return AmtUtils.decimalFormat(receivableAmt);
		}
		return receivableAmt;
	}

	public void setReceivableAmt(Double receivableAmt) {
		if (receivableAmt != null) {
			this.receivableAmt=AmtUtils.decimalFormat(receivableAmt);
		}
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Date getOperateDate() {
		return operateDate;
	}

	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}

	public Double getActualAmt() {
		if (actualAmt != null) {
			return AmtUtils.decimalFormat(actualAmt);
		}
		return actualAmt;
	}

	public void setActualAmt(Double actualAmt) {
		this.actualAmt = actualAmt;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Double getRefundAmt() {
		if (refundAmt != null) {
			return AmtUtils.decimalFormat(refundAmt);
		}
		return refundAmt;
	}

	public void setRefundAmt(Double refundAmt) {
		this.refundAmt = refundAmt;
	}

	public Long getRecordVersion() {
		return recordVersion;
	}

	public void setRecordVersion(Long recordVersion) {
		this.recordVersion = recordVersion;
	}

	public String getChargeName() {
		return chargeName;
	}

	public void setChargeName(String chargeName) {
		this.chargeName = chargeName;
	}

	public Date getChargeDate() {
		return chargeDate;
	}

	public void setChargeDate(Date chargeDate) {
		this.chargeDate = chargeDate;
	}
	
	public static void main(String[] args) {
		HospitalOrderDetailPojo pojo=new HospitalOrderDetailPojo();
		pojo.setReceivableAmt(10D);
		System.out.println(pojo.getReceivableAmt());
		System.out.println(JSON.toJSONString(pojo));
	}

	public String getPayModeName() {
		return payModeName;
	}

	public void setPayModeName(String payModeName) {
		this.payModeName = payModeName;
	}
}
