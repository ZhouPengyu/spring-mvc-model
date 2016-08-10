package com.hm.his.module.purchase.model.response;

public class PurchaseOrderListResponse {
	
	private Integer purchaseOrderId;
	private String purchaseOrderNumber;
	private Integer status;
	private String createDate;
	private Double totalPrice;
	private String supplierName;
	private Integer supplier;
	private String supplierPhoneNo;
	private String scheduleDelivery;
	
	public Integer getPurchaseOrderId() {
		return purchaseOrderId;
	}
	public void setPurchaseOrderId(Integer purchaseOrderId) {
		this.purchaseOrderId = purchaseOrderId;
	}
	public String getPurchaseOrderNumber() {
		return purchaseOrderNumber;
	}
	public void setPurchaseOrderNumber(String purchaseOrderNumber) {
		this.purchaseOrderNumber = purchaseOrderNumber;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public Integer getSupplier() {
		return supplier;
	}
	public void setSupplier(Integer supplier) {
		this.supplier = supplier;
	}
	public String getSupplierPhoneNo() {
		return supplierPhoneNo;
	}
	public void setSupplierPhoneNo(String supplierPhoneNo) {
		this.supplierPhoneNo = supplierPhoneNo;
	}
	public String getScheduleDelivery() {
		return scheduleDelivery;
	}
	public void setScheduleDelivery(String scheduleDelivery) {
		this.scheduleDelivery = scheduleDelivery;
	}
	
}
