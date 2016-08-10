package com.hm.his.module.outpatient.model;

/**
 * Created by wangjialin on 15/12/14.
 */
public class PatientDrug {
	private Long id;
    private Long recordId;
    private Long patientId;
    private Long doctorId;
    private Long drugId;
    private String drugName;
    private String usage;	//用法
    private String frequency;	//用药频率
    private String dosage;		//单次剂量
    private String dosageUnit;		//单次剂量单位
    private String duration;
    private Double totalDosage;		//开药量
    private String totalDosageUnit;		//开药单位
    private Integer type;
    private String comment;
    private String dailyDosage;
    private String requirement;
    private Integer status;
    private String createDate;
    private String modifyDate;
    private Long  assId;	//组号
    private Long prescription;		//药方编号
    private Double price;	//价格
    private Double univalence;		//出售单价
    private Long dataSource;	//药品来源 1,惠每 0,诊所
    private String specification;	//药品规格
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Long getDrugId() {
		return drugId;
	}
	public void setDrugId(Long drugId) {
		this.drugId = drugId;
	}
	public String getDrugName() {
		return drugName;
	}
	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}
	public String getUsage() {
		return usage;
	}
	public void setUsage(String usage) {
		this.usage = usage;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getDosage() {
		return dosage;
	}
	public void setDosage(String dosage) {
		this.dosage = dosage;
	}
	public String getDosageUnit() {
		return dosageUnit;
	}
	public void setDosageUnit(String dosageUnit) {
		this.dosageUnit = dosageUnit;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public Double getTotalDosage() {
		return totalDosage;
	}
	public void setTotalDosage(Double totalDosage) {
		this.totalDosage = totalDosage;
	}
	public String getTotalDosageUnit() {
		return totalDosageUnit;
	}
	public void setTotalDosageUnit(String totalDosageUnit) {
		this.totalDosageUnit = totalDosageUnit;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getDailyDosage() {
		return dailyDosage;
	}
	public void setDailyDosage(String dailyDosage) {
		this.dailyDosage = dailyDosage;
	}
	public String getRequirement() {
		return requirement;
	}
	public void setRequirement(String requirement) {
		this.requirement = requirement;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
	public Long getAssId() {
		return assId;
	}
	public void setAssId(Long assId) {
		this.assId = assId;
	}
	public Long getPrescription() {
		return prescription;
	}
	public void setPrescription(Long prescription) {
		this.prescription = prescription;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Long getDataSource() {
		return dataSource;
	}
	public void setDataSource(Long dataSource) {
		this.dataSource = dataSource;
	}
	public Double getUnivalence() {
		return univalence;
	}
	public void setUnivalence(Double univalence) {
		this.univalence = univalence;
	}
	public String getSpecification() {
		return specification;
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
}
