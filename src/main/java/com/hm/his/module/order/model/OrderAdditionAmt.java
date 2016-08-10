package com.hm.his.module.order.model;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.hm.his.framework.utils.AmtUtils;

/**
 * 
 * @description 订单附加费用项
 * @author lipeng
 * @date 2016年2月28日
 */
public class OrderAdditionAmt extends OrderItem {
	private Long id;
	/**
	 * 所属订单项集合id
	 */
	private Long orderItemListId;
	/**
	 * 订单号
	 */
	private String orderNo;
	/**
	 * 创建日期
	 */
	@JSONField(format = "yyyy-MM-dd hh:mm:ss")
	private Date createDate;
	/**
	 * 最后修改日期
	 */
	@JSONField(format = "yyyy-MM-dd hh:mm:ss")
	private Date modifyDate;
	/**
	 * 费用名称
	 */
	private String amtName;
	/**
	 * 单价
	 */
	private Double salePrice;
	/**
	 * 数量
	 */
	private Integer count;
	/**
	 * 应收金额
	 */
	private Double receivableAmt;
	/**
	 * 费用状态
	 */
	private Integer chargeStatus;
	/**
	 * 附加费用id
	 */
	private Long additionalId;
	/**
	 * 病历id
	 */
	private Long recordId;
	/**
	 * 医院id
	 */
	private Long hospitalId;
	/********************************************/
	/**
	 * 是否已退费
	 */
	private boolean isRefunded;
	private OrderItemCharge orderItemCharge;

	/*****************************************/
	/**
	 * 医生id
	 */
	private Long creater;

	private String doctorName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrderItemListId() {
		return orderItemListId;
	}

	public void setOrderItemListId(Long orderItemListId) {
		this.orderItemListId = orderItemListId;
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

	public String getAmtName() {
		return amtName;
	}

	public void setAmtName(String amtName) {
		this.amtName = amtName;
	}

	public Double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(Double salePrice) {
		if (salePrice != null) {
			salePrice = AmtUtils.decimalFormat(salePrice);
		}
		this.salePrice = salePrice;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
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

	public Integer getChargeStatus() {
		return chargeStatus;
	}

	public void setChargeStatus(Integer chargeStatus) {
		this.chargeStatus = chargeStatus;
	}

	public Long getAdditionalId() {
		return additionalId;
	}

	public void setAdditionalId(Long additionalId) {
		this.additionalId = additionalId;
	}

	public OrderItemCharge getOrderItemCharge() {
		return orderItemCharge;
	}

	public void setOrderItemCharge(OrderItemCharge orderItemCharge) {
		this.orderItemCharge = orderItemCharge;
	}

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public boolean getIsRefunded() {
		if (this.getChargeStatus() == null) {
			return false;
		}
		if (ChargeStatus.hasStatus(this.getChargeStatus(), ChargeStatus.REFUND)) {
			return true;
		} else {
			return false;
		}
	}

	public void setRefunded(boolean isRefunded) {
		this.isRefunded = isRefunded;
	}

	public Long getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}

	public Long getCreater() {
		return creater;
	}

	public void setCreater(Long creater) {
		this.creater = creater;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
}
