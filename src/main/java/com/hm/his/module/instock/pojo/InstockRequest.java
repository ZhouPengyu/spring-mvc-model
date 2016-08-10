/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.hm.his.module.instock.pojo;

import com.hm.his.framework.model.PageRequest;

import java.util.Date;
import java.util.List;

/**
 * 药品请求实体
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/2/29
 * Time: 10:04
 * CopyRight:HuiMei Engine
 */
public class InstockRequest extends PageRequest {


	private String drugName;//
	private Long drugId; //
	private Long hospitalId; //
	private Integer status;//状态 1：启用 2：禁用
	private Integer drugType; //药品分类:  枚举值  饮片：1   中成药：2    西药：3  耗材:4
	private String batchNo;// 批次号（有规则的字符串）
	private Long instockId;// 批次入库ID
	private Long drugBatchInstockId;//药品批次入库ID
	private List<Long> drugIds;//药物ID集合

	private Integer version;//前端请求版本号 --原串返回给前端
	private Integer timeQuantum;//即将过期时间段, 默认 一周内 当天： 1 一周内： 7     一个月：30  两个月：60  三个月：90 半年内：180  一年内：365    全部：-1
	private String startDate;
	private String endDate;
	private Integer isUseBatchManage;// 所在医院是否使用 批次入库管理   1:是   0： 否

	private Date compareValidityDate;// 有效期的比较日期


	public String getDrugName() {
		return drugName;
	}

	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}

	public Long getDrugId() {
		return drugId;
	}

	public void setDrugId(Long drugId) {
		this.drugId = drugId;
	}

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

	public Integer getDrugType() {
		return drugType;
	}

	public void setDrugType(Integer drugType) {
		this.drugType = drugType;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Long getInstockId() {
		return instockId;
	}

	public void setInstockId(Long instockId) {
		this.instockId = instockId;
	}

	public List<Long> getDrugIds() {
		return drugIds;
	}

	public void setDrugIds(List<Long> drugIds) {
		this.drugIds = drugIds;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getTimeQuantum() {
		return timeQuantum;
	}

	public void setTimeQuantum(Integer timeQuantum) {
		this.timeQuantum = timeQuantum;
	}

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

	public Integer getIsUseBatchManage() {
		return isUseBatchManage;
	}

	public void setIsUseBatchManage(Integer isUseBatchManage) {
		this.isUseBatchManage = isUseBatchManage;
	}

	public Date getCompareValidityDate() {
		return compareValidityDate;
	}

	public void setCompareValidityDate(Date compareValidityDate) {
		this.compareValidityDate = compareValidityDate;
	}

	public Long getDrugBatchInstockId() {
		return drugBatchInstockId;
	}

	public void setDrugBatchInstockId(Long drugBatchInstockId) {
		this.drugBatchInstockId = drugBatchInstockId;
	}

	@Override
	public String toString() {
		return "InstockRequest{" +
				"drugName='" + drugName + '\'' +
				", drugId=" + drugId +
				", hospitalId=" + hospitalId +
				", status=" + status +
				", drugType=" + drugType +
				", batchNo='" + batchNo + '\'' +
				", instockId=" + instockId +
				", drugBatchInstockId=" + drugBatchInstockId +
				", drugIds=" + drugIds +
				", version=" + version +
				", timeQuantum=" + timeQuantum +
				", startDate='" + startDate + '\'' +
				", endDate='" + endDate + '\'' +
				", isUseBatchManage=" + isUseBatchManage +
				", compareValidityDate=" + compareValidityDate +
				'}';
	}
}

