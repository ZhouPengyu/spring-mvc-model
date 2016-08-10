package com.hm.his.module.outpatient.model;

import com.hm.his.framework.log.annotation.HmLogHelper;

/**
 * Created by wangjialin on 15/12/14.
 */
public class PatientInquiry {
	
	@HmLogHelper
	private Long recordId;		//病历ID
	private Long patientId;		//患者ID
	private Long doctorId;		//医生ID
	private Double temperature;	//温度
	private Double age;		//年龄
	private String ageType;		//年龄类型
	private Integer sbp;
	private Integer dbp;
	private Integer heartRate;
	private Double height;
	private Double weight;
	private String symptom;
	private String otherPhysique;
	private String presentIllness;
	private String previousHistory;
	private String personalHistory;
	private String allergyHistory;
	private String familyHistory;
	private String physExam;
	private String createDate;
	private String modifyDate;
	private String doctorAdvice;	//医嘱
	private Integer ver;
	private Double price;
	@HmLogHelper
	private Integer modle;//  1:新开就诊 - 病历标识     2.处方标识

	public Integer getSbp() {
		return sbp;
	}

	public void setSbp(Integer sbp) {
		this.sbp = sbp;
	}

	public Integer getDbp() {
		return dbp;
	}

	public void setDbp(Integer dbp) {
		this.dbp = dbp;
	}

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public Long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
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

	public Integer getHeartRate() {
		return heartRate;
	}

	public void setHeartRate(Integer heartRate) {
		this.heartRate = heartRate;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getOtherPhysique() {
		return otherPhysique;
	}

	public void setOtherPhysique(String otherPhysique) {
		this.otherPhysique = otherPhysique;
	}

	public String getSymptom() {
		return symptom;
	}

	public void setSymptom(String symptom) {
		this.symptom = symptom;
	}

	public String getPresentIllness() {
		return presentIllness;
	}

	public void setPresentIllness(String presentIllness) {
		this.presentIllness = presentIllness;
	}

	public String getPreviousHistory() {
		return previousHistory;
	}

	public void setPreviousHistory(String previousHistory) {
		this.previousHistory = previousHistory;
	}

	public String getPersonalHistory() {
		return personalHistory;
	}

	public void setPersonalHistory(String personalHistory) {
		this.personalHistory = personalHistory;
	}

	public String getAllergyHistory() {
		return allergyHistory;
	}

	public void setAllergyHistory(String allergyHistory) {
		this.allergyHistory = allergyHistory;
	}

	public String getFamilyHistory() {
		return familyHistory;
	}

	public void setFamilyHistory(String familyHistory) {
		this.familyHistory = familyHistory;
	}

	public String getPhysExam() {
		return physExam;
	}

	public void setPhysExam(String physExam) {
		this.physExam = physExam;
	}

	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	public Integer getVer() {
		return ver;
	}

	public void setVer(Integer ver) {
		this.ver = ver;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getDoctorAdvice() {
		return doctorAdvice;
	}

	public void setDoctorAdvice(String doctorAdvice) {
		this.doctorAdvice = doctorAdvice;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getModle() {
		return modle;
	}

	public void setModle(Integer modle) {
		this.modle = modle;
	}
}
