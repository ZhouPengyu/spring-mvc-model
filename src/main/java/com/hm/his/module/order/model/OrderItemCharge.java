package com.hm.his.module.order.model;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.hm.his.framework.utils.AmtUtils;

/**
 * 
 * @description
 * @author lipeng
 * @date 2016年3月12日
 */
public class OrderItemCharge {
	private Long id;

	private Long itemId;

	private Integer itemType;

	private Long itemListId;

	private String orderNo;

	private Integer chargeStatus;

	private Long recordId;

	@JSONField(format = "yyyy-MM-dd hh:mm:ss")
	private Date createDate;

	private Long creater;

	@JSONField(format = "yyyy-MM-dd hh:mm:ss")
	private Date modifyDate;

	private Long modifier;
	/**
	 * 订单中待收费项目的应收金额
	 */
	private Double receivableAmt;

	/**
	 * 收费时间
	 */
	@JSONField(format = "yyyy-MM-dd hh:mm:ss")
	private Date chargeDate;
	/**
	 * 收费操作人id
	 */
	private Long chargeOperator;
	/**
	 * 退费时间
	 */
	@JSONField(format = "yyyy-MM-dd hh:mm:ss")
	private Date refundDate;
	/**
	 * 退费操作人id
	 */
	private Long refundOperator;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Integer getItemType() {
		return itemType;
	}

	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}

	public Long getItemListId() {
		return itemListId;
	}

	public void setItemListId(Long itemListId) {
		this.itemListId = itemListId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo == null ? null : orderNo.trim();
	}

	public Integer getChargeStatus() {
		return chargeStatus;
	}

	public void setChargeStatus(Integer chargeStatus) {
		this.chargeStatus = chargeStatus;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getCreater() {
		return creater;
	}

	public void setCreater(Long creater) {
		this.creater = creater;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public Long getModifier() {
		return modifier;
	}

	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}

	public Double getReceivableAmt() {
		if (receivableAmt != null) {
			return AmtUtils.decimalFormat(receivableAmt);
		}
		return receivableAmt;
	}

	public void setReceivableAmt(Double receivableAmt) {
		this.receivableAmt = receivableAmt;
	}

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public Date getChargeDate() {
		return chargeDate;
	}

	public void setChargeDate(Date chargeDate) {
		this.chargeDate = chargeDate;
	}

	public Date getRefundDate() {
		return refundDate;
	}

	public void setRefundDate(Date refundDate) {
		this.refundDate = refundDate;
	}

	public Long getChargeOperator() {
		return chargeOperator;
	}

	public void setChargeOperator(Long chargeOperator) {
		this.chargeOperator = chargeOperator;
	}

	public Long getRefundOperator() {
		return refundOperator;
	}

	public void setRefundOperator(Long refundOperator) {
		this.refundOperator = refundOperator;
	}
}