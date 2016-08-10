package com.hm.his.module.order.model;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 
 * @description 直接售药记录
 * @author lipeng
 * @date 2016年2月28日
 */
public class SellDrugRecord {
	/**
	 * id
	 */
	private Long id;
	/**
	 * 创建时间
	 */
	@JSONField(format = "yyyy-MM-dd hh:mm:ss")
	private Date createDate;
	/**
	 * 創建人id
	 */
	private Long creater;
	/**
	 * 最后修改时间
	 */
	@JSONField(format = "yyyy-MM-dd hh:mm:ss")
	private Date modifyDate;
	/**
	 * 患者姓名
	 */
	private String patientName;
	/**
	 * 年龄
	 */
	private Double age;
	/**
	 * 年龄类型
	 */
	private String ageType;
	/**
	 * 性别：1男，0女
	 */
	private Integer gender;
	/**
	 * 身份证号
	 */
	private String idCardNo;
	
	private Long hospitalId;

	private String phoneNo;
	/******************以下属性在数据库中没有对应字段****************************/
	/**
	 * 应收金额
	 */
	private Double receivableAmt;
	/**
	 * 实收金额
	 */
	private Double actualAmt;
	/**
	 * 药物集合
	 */
	List<SellDrug> drugs;
	/**
	 * 
	 */
	private String genderDesc;

	/**
	 * 优惠金额
	 */
	private Double discount;
	/**
	 * 付款方式
	 * 1. 现金 2 微信 3支付宝 4 银行卡
	 */
	private Integer payMode;

	/**
	 * 付款金额
	 */
	private Double payAmount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getCreater() {
		return creater;
	}

	public void setCreater(Long creater) {
		this.creater = creater;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
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

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public List<SellDrug> getDrugs() {
		return drugs;
	}

	public void setDrugs(List<SellDrug> sellDrugs) {
		this.drugs = sellDrugs;
	}

	public Double getActualAmt() {
		return actualAmt;
	}

	public void setActualAmt(Double actualAmt) {
		this.actualAmt = actualAmt;
	}

	public Double getReceivableAmt() {
		return receivableAmt;
	}

	public void setReceivableAmt(Double receivableAmt) {
		this.receivableAmt = receivableAmt;
	}

	public String getGenderDesc() {
		if (gender != null) {
			return gender == 1 ? "男" : "女";
		}
		return "";
	}

	public void setGenderDesc(String genderDesc) {
		this.genderDesc = genderDesc;
	}

	public Long getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Integer getPayMode() {
		return payMode;
	}

	public void setPayMode(Integer payMode) {
		this.payMode = payMode;
	}

	public Double getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
}
