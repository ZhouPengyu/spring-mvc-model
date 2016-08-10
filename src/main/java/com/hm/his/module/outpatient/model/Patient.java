package com.hm.his.module.outpatient.model;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-11 17:28:36
 * @description 患者实体类
 * @version 3.0
 */
public class Patient {
	private Long patientId;
	private String patientCardId;
	private String patientName;
	private Long gender;
	private String medicalRecordId;
	private String birthday;
	private String idCardNo;
	private String miCardNo;
	private String phoneNo;
	private String address;
	private String linkman; // 联系人
	private String payer;
	private String payType;
	private Long doctorId; // 最后一次修改用户信息的医生id
	private String createDate;
	private String modifyDate;
	private String patientPinyin;
	private Double age;
	private String ageType;
	private String linkmanPhone; // 联系人电话
	private String comment; // 备注
	private String diagnosisDate;	//就诊时间

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public String getPatientCardId() {
		return patientCardId;
	}

	public void setPatientCardId(String patientCardId) {
		this.patientCardId = patientCardId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getPatientPinyin() {
		return patientPinyin;
	}

	public void setPatientPinyin(String patientPinyin) {
		this.patientPinyin = patientPinyin;
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

	public String getMedicalRecordId() {
		return medicalRecordId;
	}

	public void setMedicalRecordId(String medicalRecordId) {
		this.medicalRecordId = medicalRecordId;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getMiCardNo() {
		return miCardNo;
	}

	public void setMiCardNo(String miCardNo) {
		this.miCardNo = miCardNo;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getLinkmanPhone() {
		return linkmanPhone;
	}

	public void setLinkmanPhone(String linkmanPhone) {
		this.linkmanPhone = linkmanPhone;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getPayer() {
		return payer;
	}

	public void setPayer(String payer) {
		this.payer = payer;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public Long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}

	public Long getGender() {
		return gender;
	}

	public void setGender(Long gender) {
		this.gender = gender;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getDiagnosisDate() {
		return diagnosisDate;
	}

	public void setDiagnosisDate(String diagnosisDate) {
		this.diagnosisDate = diagnosisDate;
	}
}
