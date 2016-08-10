package com.hm.his.module.statistics.pojo;

/**
 * 
 * @description
 * @author lipeng
 * @date 2016年3月24日
 */
public class DrugSaleStatisticsItemPojo {
	Long drugId;
	String drugName;
	String drugTypeName;
	/**
	 * 规格
	 */
	String specification;
	/**
	 * 销售渠道名称
	 */
	String saleChannelName;
	/**
	 * 厂家
	 */
	String manufacturer;
	/**
	 * 数量
	 */
	Double count;
	/**
	 * 数量+单位
	 */
	String countDesc;
	/**
	 * 单位
	 */
	String saleUnit;
	/**
	 * 进货价
	 */
	Double purchaseAmt;
	/**
	 * 销售价
	 */
	Double saleAmt;
	/**
	 * 利润
	 */
	Double profit;

	/**
	 * 医生姓名
	 * @return
     */
	String doctorName;

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

	public String getDrugTypeName() {
		return drugTypeName;
	}

	public void setDrugTypeName(String drugTypeName) {
		this.drugTypeName = drugTypeName;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getSaleChannelName() {
		return saleChannelName;
	}

	public void setSaleChannelName(String saleChannelName) {
		this.saleChannelName = saleChannelName;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public Double getCount() {
		return count;
	}

	public void setCount(Double count) {
		this.count = count;
	}

	public String getSaleUnit() {
		return saleUnit;
	}

	public void setSaleUnit(String saleUnit) {
		this.saleUnit = saleUnit;
	}

	public Double getPurchaseAmt() {
		return purchaseAmt;
	}

	public void setPurchaseAmt(Double purchaseAmt) {
		this.purchaseAmt = purchaseAmt;
	}

	public Double getSaleAmt() {
		return saleAmt;
	}

	public void setSaleAmt(Double saleAmt) {
		this.saleAmt = saleAmt;
	}

	public Double getProfit() {
		return profit;
	}

	public void setProfit(Double profit) {
		this.profit = profit;
	}

	public String getCountDesc() {
		String s1 = count == null ? "" : count.toString();
		String s2 = saleUnit == null ? "" : saleUnit;
		return s1 + s2;
	}

	public void setCountDesc(String countDesc) {
		this.countDesc = countDesc;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
}
