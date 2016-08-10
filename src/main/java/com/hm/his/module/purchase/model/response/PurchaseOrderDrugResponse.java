package com.hm.his.module.purchase.model.response;

import com.hm.his.module.purchase.model.PurchaseOrderDrug;
import com.hm.his.module.quote.model.QuoteOrderDrug;

public class PurchaseOrderDrugResponse {
	
	private Integer purchaseOrderDrugId;
    private Integer purchaseOrderId;
    private Integer quoteOrderDrugId;
    private Integer quoteOrderId;
    private Integer drugId;
    private String drugName;	//药品名称
    private String drugSpecification;	//药品规格
    private String purchaseDrugManufacturers;	//采购药品厂家
    private Integer purchaseQuantity;	//采购量
    private String supplyDrugManufacturers;	//供货药品厂家
    private Integer supplyQuantity; //供货数量
    private String packageUnit;	//包装单位
    private Double unitPrice;//单价
    private Integer isNew;	//药品是否存在
    private Integer isChangeManufacturer;	//厂商是否发生变更
    
    public PurchaseOrderDrugResponse(Object drug){
    	if(drug instanceof PurchaseOrderDrug){
    		PurchaseOrderDrug purchaseOrderDrug = (PurchaseOrderDrug)drug;
    		setPurchaseOrderDrugId(purchaseOrderDrug.getPurchaseOrderDrugId());
    		setPurchaseOrderId(purchaseOrderDrug.getPurchaseOrderId());
    		setDrugId(purchaseOrderDrug.getDrugId());
    		setDrugName(purchaseOrderDrug.getDrugName());
    		setDrugSpecification(purchaseOrderDrug.getDrugSpecification());
    		setPurchaseDrugManufacturers(purchaseOrderDrug.getPurchaseDrugManufacturers());
    		setPurchaseQuantity(purchaseOrderDrug.getPurchaseQuantity());
    		setPackageUnit(purchaseOrderDrug.getPackageUnit());
    	}
    	if(drug instanceof QuoteOrderDrug){
    		QuoteOrderDrug quoteOrderDrug = (QuoteOrderDrug)drug;
    		setQuoteOrderDrugId(quoteOrderDrug.getQuoteOrderDrugId());
    		setQuoteOrderId(quoteOrderDrug.getQuoteOrderId());
    		setDrugId(quoteOrderDrug.getDrugId());
    		setDrugName(quoteOrderDrug.getDrugName());
    		setDrugSpecification(quoteOrderDrug.getDrugSpecification());
    		setPurchaseDrugManufacturers(quoteOrderDrug.getPurchaseDrugManufacturers());
    		setPurchaseQuantity(quoteOrderDrug.getPurchaseQuantity());
    		setSupplyDrugManufacturers(quoteOrderDrug.getSupplyDrugManufacturers());
    		setSupplyQuantity(quoteOrderDrug.getSupplyQuantity());
    		setPackageUnit(quoteOrderDrug.getPackageUnit());
    		setUnitPrice(quoteOrderDrug.getUnitPrice());
    	}
    };
    
	public Integer getPurchaseOrderDrugId() {
		return purchaseOrderDrugId;
	}
	public void setPurchaseOrderDrugId(Integer purchaseOrderDrugId) {
		this.purchaseOrderDrugId = purchaseOrderDrugId;
	}
	public Integer getPurchaseOrderId() {
		return purchaseOrderId;
	}
	public void setPurchaseOrderId(Integer purchaseOrderId) {
		this.purchaseOrderId = purchaseOrderId;
	}
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
	public Integer getSupplyQuantity() {
		return supplyQuantity;
	}
	public void setSupplyQuantity(Integer supplyQuantity) {
		this.supplyQuantity = supplyQuantity;
	}
	public String getPackageUnit() {
		return packageUnit;
	}
	public void setPackageUnit(String packageUnit) {
		this.packageUnit = packageUnit;
	}
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Integer getIsNew() {
		return isNew;
	}
	public void setIsNew(Integer isNew) {
		this.isNew = isNew;
	}
	public Integer getIsChangeManufacturer() {
		return isChangeManufacturer;
	}
	public void setIsChangeManufacturer(Integer isChangeManufacturer) {
		this.isChangeManufacturer = isChangeManufacturer;
	}
	
}
