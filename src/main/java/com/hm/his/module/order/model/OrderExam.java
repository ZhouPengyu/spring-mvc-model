package com.hm.his.module.order.model;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.hm.his.framework.utils.AmtUtils;

/**
 * 
 * @description 订单检查项
 * @author lipeng
 * @date 2016年2月28日
 */
public class OrderExam extends OrderItem {
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
	 * 医院id
	 */
	private Long hospitalId;
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
	 * 检查项id
	 */
	private Long examId;
	/**
	 * patientExamId
	 */
	private Long patientExamId;
	/**
	 * 检查项名称
	 */
	private String examName;
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
	 * 病历id
	 */
	private Long recordId;
	/**
	 * 数据来源：1：来自惠每，0：来自诊所
	 */
	private Integer dataSource;
	/**
	 * 检查治疗项成本
	 */
	private Double cost;
	/**************************************************************/
	private OrderItemCharge orderItemCharge;
	/**
	 * 是否已退费
	 */
	private boolean isRefunded;

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

	public Long getExamId() {
		return examId;
	}

	public void setExamId(Long examId) {
		this.examId = examId;
	}

	public String getExamName() {
		return examName;
	}

	public void setExamName(String examName) {
		this.examName = examName;
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

	public Long getPatientExamId() {
		return patientExamId;
	}

	public void setPatientExamId(Long patientExamId) {
		this.patientExamId = patientExamId;
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

	public Integer getDataSource() {
		return dataSource;
	}

	public void setDataSource(Integer dataSource) {
		this.dataSource = dataSource;
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

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
}
