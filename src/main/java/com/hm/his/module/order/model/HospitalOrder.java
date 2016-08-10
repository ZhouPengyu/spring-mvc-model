package com.hm.his.module.order.model;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.hm.his.framework.utils.AmtUtils;
import com.hm.his.framework.utils.DoubleWrap;

/**
 * 
 * @description 订单主表
 * @author lipeng
 * @date 2016年2月27日
 */
public class HospitalOrder {
	/**
	 * 类型-病历
	 */
	public static final int TYPE_PATIENT_RECORD = 1;
	/**
	 * 类型-直接售药
	 */
	public static final int TYPE_SELL_DRUG = 2;
	/**
	 * 订单编号：20160227 201012 001 	+ 医生id 
	 */
	private String orderNo;
	/**
	 * 医院id
	 */
	private Long hospitalId;
	/**
	 * 创建时间
	 */
	@JSONField(format = "yyyy-MM-dd hh:mm:ss")
	private Date createDate;
	/**
	 * 订单类型
	 */
	private Integer orderType;
	/**
	 * 订单状态
	 */
	private Integer orderStatus;
	/**
	 * 創建人id
	 */
	private Long creater;
	/**
	 * 病历id
	 */
	private Long patientRecordId;
	/**
	 * 直接售药记录id
	 */
	private Long sellDrugRecordId;
	/**
	 * 最后修改时间
	 */
	@JSONField(format = "yyyy-MM-dd hh:mm:ss")
	private Date modifyDate;
	/**
	 * 最后修改人id
	 */
	private Long modifier;
	/**
	 * 患者id
	 */
	private Long patientId;
	/**
	 * 患者
	 */
	private String patientName;
	/**
	 * 费用情况
	 */
	private Integer chargeStatus;
	/**
	 * 剩余应收金额
	 */
	private Double receivableAmt;
	/**
	 * 总共应收金额
	 */
	private Double totalReceivableAmt;
	/**
	 * 实收金额
	 */
	private Double actualAmt;
	/**
	 * 实退金额
	 */
	private Double refundAmt;

	/**
	 * 最后收费时间
	 */
	@JSONField(format = "yyyy-MM-dd hh:mm:ss")
	private Date lastChargeDate;
	/**
	 * 最后收费操作人id
	 */
	private Long lastChargeId;
	/**
	 * 最后退费时间
	 */
	@JSONField(format = "yyyy-MM-dd hh:mm:ss")
	private Date lastRefundDate;
	/**
	 * 最后退费操作人id
	 */
	private Long lastRefundId;
	/**
	 * 病历版本，每次新增或修改病历更新版本，收费时比较版本，不一致表示版本信息过期
	 */
	private Long recordVersion;

	/**
	 * 优惠金额
	 */
	private Double discount;

	private Integer payMode;

	/*****************************以下字段为非数据库字段******************************************/
	/**
	 * 订单检查单
	 */
	private OrderItemList<OrderExam> orderExamList;
	/**
	 * 订单处方单
	 */
	private List<OrderItemList<OrderDrug>> orderPrescriptions;
	/**
	 * 订单附加费用单
	 */
	private OrderItemList<OrderAdditionAmt> orderAdditionAmtList;

	/**
	 * 
	 * @description 是否包含待收费内容
	 * @date 2016年2月27日
	 * @author lipeng
	 * @return
	 */
	public boolean hasToChargeContent() {
		return false;
	}

	/**
	 * 
	 * @description 是否包含已收费内容
	 * @date 2016年2月27日
	 * @author lipeng
	 * @return
	 */
	public boolean hasChargedContent() {
		return false;
	}

	/**
	 * 
	 * @description 是否包含退费内容
	 * @date 2016年2月27日
	 * @author lipeng
	 * @return
	 */
	public boolean hasRefundContent() {
		return false;
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

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Long getCreater() {
		return creater;
	}

	public void setCreater(Long creater) {
		this.creater = creater;
	}

	public Long getPatientRecordId() {
		return patientRecordId;
	}

	public void setPatientRecordId(Long patientRecordId) {
		this.patientRecordId = patientRecordId;
	}

	public Long getSellDrugRecordId() {
		return sellDrugRecordId;
	}

	public void setSellDrugRecordId(Long sellDrugRecordId) {
		this.sellDrugRecordId = sellDrugRecordId;
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

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public Integer getChargeStatus() {
		return chargeStatus;
	}

	public void setChargeStatus(Integer chargeStatus) {
		this.chargeStatus = chargeStatus;
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

	public Double getActualAmt() {
		if (actualAmt != null) {
			return AmtUtils.decimalFormat(actualAmt);
		}
		return actualAmt;
	}

	public void setActualAmt(Double actualAmt) {
		this.actualAmt = actualAmt;
	}

	public Double getRefundAmt() {
		if (refundAmt != null) {
			return AmtUtils.decimalFormat(refundAmt);
		}
		return refundAmt;
	}

	public void setRefundAmt(Double refundAmt) {
		this.refundAmt = refundAmt;
	}

	public Long getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public OrderItemList<OrderExam> getOrderExamList() {
		return orderExamList;
	}

	public void setOrderExamList(OrderItemList<OrderExam> orderExamList) {
		this.orderExamList = orderExamList;
	}

	public List<OrderItemList<OrderDrug>> getOrderPrescriptions() {
		return orderPrescriptions;
	}

	public void setOrderPrescriptions(List<OrderItemList<OrderDrug>> orderPrescriptions) {
		this.orderPrescriptions = orderPrescriptions;
	}

	public OrderItemList<OrderAdditionAmt> getOrderAdditionAmtList() {
		return orderAdditionAmtList;
	}

	public void setOrderAdditionAmtList(OrderItemList<OrderAdditionAmt> orderAdditionAmtList) {
		this.orderAdditionAmtList = orderAdditionAmtList;
	}

	public Date getLastChargeDate() {
		return lastChargeDate;
	}

	public void setLastChargeDate(Date lastChargeDate) {
		this.lastChargeDate = lastChargeDate;
	}

	public Date getLastRefundDate() {
		return lastRefundDate;
	}

	public void setLastRefundDate(Date lastRefundDate) {
		this.lastRefundDate = lastRefundDate;
	}

	public Long getLastChargeId() {
		return lastChargeId;
	}

	public void setLastChargeId(Long lastChargeId) {
		this.lastChargeId = lastChargeId;
	}

	public Long getLastRefundId() {
		return lastRefundId;
	}

	public void setLastRefundId(Long lastRefundId) {
		this.lastRefundId = lastRefundId;
	}

	public Double getTotalReceivableAmt() {
		if (totalReceivableAmt != null) {
			return AmtUtils.decimalFormat(totalReceivableAmt);
		}
		return totalReceivableAmt;
	}

	public void setTotalReceivableAmt(Double totalReceivableAmt) {
		this.totalReceivableAmt = totalReceivableAmt;
	}

	public Long getRecordVersion() {
		return recordVersion;
	}

	public void setRecordVersion(Long recordVersion) {
		this.recordVersion = recordVersion;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Integer getPayMode() {
		return payMode;
	}

	public void setPayMode(Integer payMode) {
		this.payMode = payMode;
	}
}
