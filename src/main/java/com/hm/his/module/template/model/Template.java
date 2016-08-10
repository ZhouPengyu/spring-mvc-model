package com.hm.his.module.template.model;

import com.hm.his.framework.log.annotation.HmLogHelper;

import java.util.Date;

/**
 * 
 * @description 模板基本信息表
 * @author tangwenwu
 * @date 2016年3月24日
 */
public class Template {

	@HmLogHelper
	private Long id;

	/**
	 * 医院ID
	 */
	private Long hospitalId;

	/**
	 * 医生ID
	 */
	private Long doctorId;

	/**
	 * 模板类型     1:中药处方模板     2：西药处方 模板   5 ：病历模板    6：医嘱模板
	 */
	private Integer tempType;
	/**
	 * 模板名称
	 */
	@HmLogHelper
	private String tempName;
	/**
	 * 模板描述
	 */
	private String tempDesc;

	/**
	 * 病历ID 或者处方ID
	 */
	private Long linkId;
	/**
	 * 状态 1 可用 0 删除
	 */
	private Integer status;

	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 最后修改时间
	 */
	private Date modifyDate;
	/**
	 * 创建人id
	 */
	private Long creater;

	/**
	 * 最后修改人id
	 */
	private Long modifier;

//--------------页面属性---------------------
	private String tempTypeName;

	private String cnDrugNames;

	private String drugNames;//药品名称的集合，多个用 、号隔开

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTempName() {
		return tempName;
	}

	public void setTempName(String tempName) {
		this.tempName = tempName;
	}

	public String getTempDesc() {
		return tempDesc;
	}

	public void setTempDesc(String tempDesc) {
		this.tempDesc = tempDesc;
	}

	public Integer getTempType() {
		return tempType;
	}

	public void setTempType(Integer tempType) {
		this.tempType = tempType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getLinkId() {
		return linkId;
	}

	public void setLinkId(Long linkId) {
		this.linkId = linkId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public Long getCreater() {
		return creater;
	}

	public void setCreater(Long creater) {
		this.creater = creater;
	}

	public Long getModifier() {
		return modifier;
	}

	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}

	public Long getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}

	public Long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}

	public String getTempTypeName() {
		return tempTypeName;
	}

	public void setTempTypeName(String tempTypeName) {
		this.tempTypeName = tempTypeName;
	}

	public String getDrugNames() {
		return drugNames;
	}

	public void setDrugNames(String drugNames) {
		this.drugNames = drugNames;
	}

	public String getCnDrugNames() {
		return cnDrugNames;
	}

	public void setCnDrugNames(String cnDrugNames) {
		this.cnDrugNames = cnDrugNames;
	}
}
