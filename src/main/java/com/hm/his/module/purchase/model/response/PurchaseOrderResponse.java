package com.hm.his.module.purchase.model.response;

import java.util.List;

import com.hm.his.framework.utils.DateUtil;
import com.hm.his.module.purchase.model.PurchaseOrder;
import com.hm.his.module.quote.model.QuoteOrder;

public class PurchaseOrderResponse {
	
	private Integer purchaseOrderId;
	private String purchaseOrderNumber;	//订单编号
	private Integer status;	//状态
	private String createrName;	//创建人
	private String createDate;	//采购单时间
	private Double totalPrice;	//供货总价
	private Integer supplier;	//供货商ID
	private String supplierName;	//供货商
	private String supplierPhoneNo;	//供货商联系方式
	private String scheduleDelivery;	//预计送货时间
	private Integer quoteWay;	//配送方式
	private String address;	//配送地址
	private String hospitalMessage;	//诊所留言
	private String supplierMessage;	//供货商留言
    private Integer designated;	//1是指定厂家  0 是不指定厂家
	private List<PurchaseOrderDrugResponse> purchaseOrderDrugList;	//药品详细
	
	public PurchaseOrderResponse(PurchaseOrder purchaseOrder, QuoteOrder quoteOrder){
		setPurchaseOrderId(purchaseOrder.getPurchaseOrderId());
		setPurchaseOrderNumber(purchaseOrder.getPurchaseOrderNumber());
		setCreaterName(purchaseOrder.getCreaterName());
		setStatus(purchaseOrder.getStatus());
		setCreateDate(DateUtil.getGeneralDate(purchaseOrder.getCreateDate()));
		setAddress(purchaseOrder.getDeliveryAddress());
		setHospitalMessage(purchaseOrder.getMessage());
		setDesignated(purchaseOrder.getDesignated());
		
		setTotalPrice(quoteOrder.getTotalPrice());
		setSupplier(quoteOrder.getCreater());
		setQuoteWay(quoteOrder.getQuoteWay());
		setSupplierName(quoteOrder.getSupplierName());
		setSupplierPhoneNo(quoteOrder.getPhoneNo());
		setScheduleDelivery(DateUtil.getGeneralDate(quoteOrder.getScheduleDelivery()));
		setSupplierMessage(quoteOrder.getMessage());
	}
	
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
	public String getCreaterName() {
		return createrName;
	}
	public void setCreaterName(String createrName) {
		this.createrName = createrName;
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
	public Integer getSupplier() {
		return supplier;
	}
	public void setSupplier(Integer supplier) {
		this.supplier = supplier;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getSupplierPhoneNo() {
		return supplierPhoneNo;
	}
	public void setSupplierPhoneNo(String supplierPhoneNo) {
		this.supplierPhoneNo = supplierPhoneNo;
	}
	public Integer getQuoteWay() {
		return quoteWay;
	}
	public void setQuoteWay(Integer quoteWay) {
		this.quoteWay = quoteWay;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getScheduleDelivery() {
		return scheduleDelivery;
	}
	public void setScheduleDelivery(String scheduleDelivery) {
		this.scheduleDelivery = scheduleDelivery;
	}
	public String getHospitalMessage() {
		return hospitalMessage;
	}
	public void setHospitalMessage(String hospitalMessage) {
		this.hospitalMessage = hospitalMessage;
	}
	public String getSupplierMessage() {
		return supplierMessage;
	}
	public void setSupplierMessage(String supplierMessage) {
		this.supplierMessage = supplierMessage;
	}
	public Integer getDesignated() {
		return designated;
	}
	public void setDesignated(Integer designated) {
		this.designated = designated;
	}
	public List<PurchaseOrderDrugResponse> getPurchaseOrderDrugList() {
		return purchaseOrderDrugList;
	}
	public void setPurchaseOrderDrugList(
			List<PurchaseOrderDrugResponse> purchaseOrderDrugList) {
		this.purchaseOrderDrugList = purchaseOrderDrugList;
	}
	
}
