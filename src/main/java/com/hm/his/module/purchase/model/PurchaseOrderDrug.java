package com.hm.his.module.purchase.model;

public class PurchaseOrderDrug {
    private Integer purchaseOrderDrugId;

    private Integer purchaseOrderId;

    private Integer drugId;

    private String drugName;

    private String drugSpecification;

    private String purchaseDrugManufacturers;

    private Integer purchaseQuantity;

    private String packageUnit;
    
    private Integer flag;

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

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}
}