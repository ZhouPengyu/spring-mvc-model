/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.hm.his.module.template.pojo;

import com.hm.his.framework.model.PageRequest;

import java.util.List;

/**
 * 模板请求实体
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/2/29
 * Time: 10:04
 * CopyRight:HuiMei Engine
 */
public class TemplateRequest extends PageRequest {


	private String tempName;//
	private Long id; //
	private Long hospitalId; //

	/**
	 * 医生ID
	 */
	private Long doctorId;
	private Integer status;//状态 1：启用 2：禁用
	private Integer tempType;//1:中药处方模板     2：西药处方 模板   3:所有处方模板  5 ：病历模板    6：医嘱模板

	private List<Long> tempIds;//模板ID集合




	public Long getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getTempName() {
		return tempName;
	}

	public void setTempName(String tempName) {
		this.tempName = tempName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getTempType() {
		return tempType;
	}

	public void setTempType(Integer tempType) {
		this.tempType = tempType;
	}

	public List<Long> getTempIds() {
		return tempIds;
	}

	public void setTempIds(List<Long> tempIds) {
		this.tempIds = tempIds;
	}

	public Long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}
}

