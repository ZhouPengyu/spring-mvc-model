package com.hm.his.module.purchase.model.response;

import com.hm.his.framework.utils.DateUtil;
import com.hm.his.module.quote.model.QuoteOrder;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-23 12:03:22
 * @description 查看采购单下供货单的实体类
 * @version 1.0
 */
public class PurchaseQuoteResponse {
	private String supplierName;
	private Integer quoteOrderId;
    private String phoneNo;
    private Integer outOfStockSpecies;	//缺货数量
    private String scheduleDelivery;
    private Integer quoteWay;
    private String address;
	private String message;
	private Double totalPrice;

	public PurchaseQuoteResponse(QuoteOrder quoteOrder){
		setSupplierName(quoteOrder.getSupplierName());
		setQuoteOrderId(quoteOrder.getQuoteOrderId());
		setPhoneNo(quoteOrder.getPhoneNo());
		setScheduleDelivery(DateUtil.getGeneralDate(quoteOrder.getScheduleDelivery()));
		setQuoteWay(quoteOrder.getQuoteWay());
		setMessage(quoteOrder.getMessage());
		setTotalPrice(quoteOrder.getTotalPrice());
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public Integer getQuoteOrderId() {
		return quoteOrderId;
	}

	public void setQuoteOrderId(Integer quoteOrderId) {
		this.quoteOrderId = quoteOrderId;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public Integer getOutOfStockSpecies() {
		return outOfStockSpecies;
	}

	public void setOutOfStockSpecies(Integer outOfStockSpecies) {
		this.outOfStockSpecies = outOfStockSpecies;
	}

	public String getScheduleDelivery() {
		return scheduleDelivery;
	}

	public void setScheduleDelivery(String scheduleDelivery) {
		this.scheduleDelivery = scheduleDelivery;
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	
}
