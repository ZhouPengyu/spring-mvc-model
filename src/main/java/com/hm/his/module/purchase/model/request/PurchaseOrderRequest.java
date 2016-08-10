package com.hm.his.module.purchase.model.request;

import com.hm.his.framework.model.PageRequest;

/**
 * 药品请求实体</br>
 * User: TangWenWu</br>
 * Date: 2016-5-30 16:18:13</br>
 * CopyRight:HuiMei Engine</br>
 */
public class PurchaseOrderRequest extends PageRequest {

	private Integer purchaseOrderId;

	/* 订单编号*/
	private String purchaseOrderNumber;

	/**
	 * 采购单状态
	 0.已取消：采购单被取消
	 1.尚无报价：没有供货商报价
	 2.已报价：供货商报价
	 3.待出库：确定供货商后，等待出库
	 4.已发货：供货商发货
	 5.已完成：采购单确认入库
	 */
	private Integer status;
	/**
	 * 时间段, 默认 一周内
	 当天： 1
	 一周内： 7
	 一个月：30
	 两个月：60
	 三个月：90
	 半年内：180
	 一年内：365
	 全部：-1
	 */
	private Integer timeQuantum;
	
	private Long hospitalId;
	
	private String startDate;
	
	private String endDate;
	
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
	public Integer getTimeQuantum() {
		return timeQuantum;
	}
	public void setTimeQuantum(Integer timeQuantum) {
		this.timeQuantum = timeQuantum;
	}
	public Long getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate+" 23:59:59";
	}

}

