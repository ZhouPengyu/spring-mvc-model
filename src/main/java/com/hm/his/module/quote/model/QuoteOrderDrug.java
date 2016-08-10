package com.hm.his.module.quote.model;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-5-25 17:20:04
 * @description 报价单详细实体类
 * @version 1.0
 */
public class QuoteOrderDrug {
	
    private Integer quoteOrderDrugId;
    private Integer quoteOrderId;
    private Integer drugId;
    private String drugName;
    private String drugSpecification;
    private String purchaseDrugManufacturers;
    private String supplyDrugManufacturers;//供货药品厂家
    private Integer purchaseQuantity;//采购量
    private String packageUnit;//包装单位
    private Integer supplyQuantity;//供货量
    private Double unitPrice;//单价

	public Integer getQuoteOrderDrugId() {
		return quoteOrderDrugId;
	}
	public void setQuoteOrderDrugId(Integer quoteOrderDrugId) {
		this.quoteOrderDrugId = quoteOrderDrugId;
	}
	public Integer getQuoteOrderId() {
		return quoteOrderId;
	}
	public void setQuoteOrderId(Integer quoteOrderId) {
		this.quoteOrderId = quoteOrderId;
	}
	public Integer getDrugId() {
		return drugId;
	}
	public void setDrugId(Integer drugId) {
		this.drugId = drugId;
	}
	public String getDrugName() {
		return drugName;
	}
	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}
	public String getDrugSpecification() {
		return drugSpecification;
	}
	public void setDrugSpecification(String drugSpecification) {
		this.drugSpecification = drugSpecification;
	}
	public String getPurchaseDrugManufacturers() {
		return purchaseDrugManufacturers;
	}
	public void setPurchaseDrugManufacturers(String purchaseDrugManufacturers) {
		this.purchaseDrugManufacturers = purchaseDrugManufacturers;
	}
	public String getSupplyDrugManufacturers() {
		return supplyDrugManufacturers;
	}
	public void setSupplyDrugManufacturers(String supplyDrugManufacturers) {
		this.supplyDrugManufacturers = supplyDrugManufacturers;
	}
	public Integer getPurchaseQuantity() {
		return purchaseQuantity;
	}
	public void setPurchaseQuantity(Integer purchaseQuantity) {
		this.purchaseQuantity = purchaseQuantity;
	}
	public String getPackageUnit() {
		return packageUnit;
	}
	public void setPackageUnit(String packageUnit) {
		this.packageUnit = packageUnit;
	}
	public Integer getSupplyQuantity() {
		return supplyQuantity;
	}
	public void setSupplyQuantity(Integer supplyQuantity) {
		this.supplyQuantity = supplyQuantity;
	}
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
}