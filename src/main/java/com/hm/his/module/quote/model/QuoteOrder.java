package com.hm.his.module.quote.model;

import com.google.common.collect.Lists;
import com.hm.his.module.purchase.model.DeliveryAddress;

import java.util.List;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-5-25 17:20:04
 * @description 报价单实体类
 * @version 1.0
 */
public class QuoteOrder {
	
    private Integer quoteOrderId;
    private Integer purchaseOrderId;
	private String purchaseOrderNumber;
    private Integer quoteWay;
    private Integer wayId;
    private String scheduleDelivery;
    private Double totalPrice;
    private String phoneNo;
    private String remark;
    private String message;
	private String supplierName;
	/**
	 *  1是指定厂家  0 是不指定厂家
	 */
	private Integer designated;

	/**
     * 供货单状态
	 -1. 全部已报价订单
     0.已取消：供货单被取消
     1.待确认：报价后等待诊所确认
     2.待出库：诊所确认后
     3.待收货：供货商发货后
     4.已完成：诊所确定收货入库
     5.未中标：诊所选择了其他供货商
     */
    private Integer status;
    private Integer creater;
    private String createDate;
    private Integer flag;
	private String purchaseCreateDate;

	private String address;

	private String hospitalName;
	private String hospitalMessage;
	private String hospitalPhoneNo;
	private List<QuoteOrderDrug> drugList= Lists.newArrayList();

	private List<SupplyAddress> supplyAddress;
	private DeliveryAddress deliveryAddress;

	public Integer getQuoteOrderId() {
		return quoteOrderId;
	}
	public void setQuoteOrderId(Integer quoteOrderId) {
		this.quoteOrderId = quoteOrderId;
	}
	public Integer getPurchaseOrderId() {
		return purchaseOrderId;
	}
	public void setPurchaseOrderId(Integer purchaseOrderId) {
		this.purchaseOrderId = purchaseOrderId;
	}
	public Integer getQuoteWay() {
		return quoteWay;
	}
	public void setQuoteWay(Integer quoteWay) {
		this.quoteWay = quoteWay;
	}
	public Integer getWayId() {
		return wayId;
	}
	public void setWayId(Integer wayId) {
		this.wayId = wayId;
	}
	public String getScheduleDelivery() {
		return scheduleDelivery;
	}
	public void setScheduleDelivery(String scheduleDelivery) {
		this.scheduleDelivery = scheduleDelivery;
	}
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getCreater() {
		return creater;
	}
	public void setCreater(Integer creater) {
		this.creater = creater;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getPurchaseCreateDate() {
		return purchaseCreateDate;
	}

	public void setPurchaseCreateDate(String purchaseCreateDate) {
		this.purchaseCreateDate = purchaseCreateDate;
	}

	public String getPurchaseOrderNumber() {
		return purchaseOrderNumber;
	}

	public void setPurchaseOrderNumber(String purchaseOrderNumber) {
		this.purchaseOrderNumber = purchaseOrderNumber;
	}

	public List<QuoteOrderDrug> getDrugList() {
		return drugList;
	}

	public void setDrugList(List<QuoteOrderDrug> drugList) {
		this.drugList = drugList;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getHospitalMessage() {
		return hospitalMessage;
	}

	public void setHospitalMessage(String hospitalMessage) {
		this.hospitalMessage = hospitalMessage;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getHospitalPhoneNo() {
		return hospitalPhoneNo;
	}

	public void setHospitalPhoneNo(String hospitalPhoneNo) {
		this.hospitalPhoneNo = hospitalPhoneNo;
	}

	public List<SupplyAddress> getSupplyAddress() {
		return supplyAddress;
	}

	public void setSupplyAddress(List<SupplyAddress> supplyAddress) {
		this.supplyAddress = supplyAddress;
	}

	public DeliveryAddress getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public Integer getDesignated() {
		return designated;
	}

	public void setDesignated(Integer designated) {
		this.designated = designated;
	}
}