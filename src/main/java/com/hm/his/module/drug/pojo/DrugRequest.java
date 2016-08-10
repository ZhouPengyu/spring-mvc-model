/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.hm.his.module.drug.pojo;

import com.hm.his.framework.model.PageRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 药品请求实体
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/2/29
 * Time: 10:04
 * CopyRight:HuiMei Engine
 */
public class DrugRequest extends PageRequest {


	private String drugName;//
	private String pinyin;//
	private Long drugId; //
	private Long hospitalId; //
	private Integer status;//状态 1：启用 2：禁用

	private Integer drugType; //药品分类:  枚举值  饮片：1   中成药：2    西药：3  耗材:4
	private Integer dataSource;  //药品数据源  1：仅查惠每  0：仅查诊所 2：查询所有库
//	private Integer saleChannel; //销售渠道  1：处方 2：直接售药
	private List<Long> drugIds;//药物ID集合
	private Integer inventoryThreshold;//库存阈值

	private String manufacturer;//
	private Integer isBarCode;//是否为 条形码搜索  1： 是    其他不要传此值
	private String barCode;//药品条形码
	private Long hmDrugId;//惠每药物ID
	private Integer version;//前端请求版本号 --原串返回给前端
	
	private List<Long> alreadyAddList;	//采购列表中已添加过的药品列表


	public String getDrugName() {
		if(StringUtils.isNotBlank(drugName)){
			return drugName.replaceAll("'","");
		}
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

	public Integer getDataSource() {
		return dataSource;
	}

	public void setDataSource(Integer dataSource) {
		this.dataSource = dataSource;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public List<Long> getDrugIds() {
		return drugIds;
	}

	public void setDrugIds(List<Long> drugIds) {
		this.drugIds = drugIds;
	}

	public Integer getInventoryThreshold() {
		return inventoryThreshold;
	}

	public void setInventoryThreshold(Integer inventoryThreshold) {
		this.inventoryThreshold = inventoryThreshold;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	//	public Integer getSaleChannel() {
//		return saleChannel;
//	}
//
//	public void setSaleChannel(Integer saleChannel) {
//		this.saleChannel = saleChannel;
//	}

	public Integer getIsBarCode() {
		return isBarCode;
	}

	public void setIsBarCode(Integer isBarCode) {
		this.isBarCode = isBarCode;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public Long getHmDrugId() {
		return hmDrugId;
	}

	public void setHmDrugId(Long hmDrugId) {
		this.hmDrugId = hmDrugId;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public List<Long> getAlreadyAddList() {
		return alreadyAddList;
	}

	public void setAlreadyAddList(List<Long> alreadyAddList) {
		this.alreadyAddList = alreadyAddList;
	}


	@Override
	public String toString() {
		return "DrugRequest{" +
				"drugName='" + drugName + '\'' +
				", drugId=" + drugId +
				", hospitalId=" + hospitalId +
				", status=" + status +
				", drugType=" + drugType +
				", dataSource=" + dataSource +
				", drugIds=" + drugIds +
				", inventoryThreshold=" + inventoryThreshold +
				", manufacturer='" + manufacturer + '\'' +
				", isBarCode=" + isBarCode +
				", barCode='" + barCode + '\'' +
				", hmDrugId=" + hmDrugId +
				", version=" + version +
				", alreadyAddList=" + alreadyAddList +
				", startRecord=" + getStartRecord() +
				", pageSize=" + getPageSize() +
				'}';
	}
}

