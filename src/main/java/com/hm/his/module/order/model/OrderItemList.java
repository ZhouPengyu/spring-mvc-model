package com.hm.his.module.order.model;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.hm.his.framework.utils.AmtUtils;

/**
 * 
 * @description 订单项集合
 * @author lipeng
 * @date 2016年2月28日
 */
public class OrderItemList<T extends OrderItem> {
	private Long id;
	/**
	 * 订单号
	 */
	private String orderNo;
	/**
	 * 创建时间
	 */
	@JSONField(format = "yyyy-MM-dd hh:mm:ss")
	private Date createDate;
	/**
	 * 最后修改人id
	 */
	private Long modifier;
	/**
	 * 修改时间
	 */
	@JSONField(format = "yyyy-MM-dd hh:mm:ss")
	private Date modifyDate;
	/**
	 * 订单项集合类型
	 */
	private Integer type;
	/**
	 * 外键id
	 */
	private Long foreignId;
	/**
	 * 费用情况
	 */
	private Integer chargeStatus;

	/**
	 * 应收金额
	 */
	private Double receivableAmt;
	/**
	 * 病历id
	 */
	private Long recordId;
	/******************************以下为非数据库字段**************************************/
	/**
	 * 具体项的集合
	 */
	private List<T> items;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getForeignId() {
		return foreignId;
	}

	public void setForeignId(Long foreignId) {
		this.foreignId = foreignId;
	}

	public Integer getChargeStatus() {
		return chargeStatus;
	}

	public void setChargeStatus(Integer chargeStatus) {
		this.chargeStatus = chargeStatus;
	}

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
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

	public Long getModifier() {
		return modifier;
	}

	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}
}
