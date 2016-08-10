package com.hm.his.module.order.model;

/**
 * 
 * @description 售药项
 * @author lipeng
 * @date 2016年2月29日
 */
public class SellDrug {
	private Long id;
	/**
	 * 售药记录id
	 */
	private Long sellDrugRecordId;
	/**
	 * 药物id
	 */
	private Long drugId;
	/**
	 * 药物类型 @文武
	 */
	private Integer drugType;
	/**
	 * 药物名称
	 */
	private String drugName;
	/**
	 * 厂家名称
	 */
	private String manufacturer;
	/**
	 * 规格
	 */
	private String specification;
	/**
	 * 数量
	 */
	private Integer count;
	/**
	 * 销售单位
	 */
	private String saleUnit;
	/**
	 * 单价
	 */
	private Double salePrice;
	/**
	 * 数据来源：1：来自惠每，0：来自诊所
	 */
	private Integer dataSource;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSellDrugRecordId() {
		return sellDrugRecordId;
	}

	public void setSellDrugRecordId(Long sellDrugRecordId) {
		this.sellDrugRecordId = sellDrugRecordId;
	}

	public Long getDrugId() {
		return drugId;
	}

	public void setDrugId(Long drugId) {
		this.drugId = drugId;
	}

	public Integer getDrugType() {
		return drugType;
	}

	public void setDrugType(Integer drugType) {
		this.drugType = drugType;
	}

	public String getDrugName() {
		return drugName;
	}

	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getSaleUnit() {
		return saleUnit;
	}

	public void setSaleUnit(String saleUnit) {
		this.saleUnit = saleUnit;
	}

	public Double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}

	public Integer getDataSource() {
		return dataSource;
	}

	public void setDataSource(Integer dataSource) {
		this.dataSource = dataSource;
	}
}
