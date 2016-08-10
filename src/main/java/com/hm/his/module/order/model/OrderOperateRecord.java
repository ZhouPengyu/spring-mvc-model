package com.hm.his.module.order.model;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * 
 * @description 订单操作记录表
 * @author lipeng
 * @date 2016年2月28日
 */
public class OrderOperateRecord {

	private Long id;
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
	 * 最后操作人id
	 */
	private Long creater;
	/**
	 * 理论金额
	 */
	private Double receivableAmt;
	/**
	 * 实际金额
	 */
	private Double actualAmt;
	/**
	 * 操作类型
	 */
	private Integer operateType;
	/**
	 * 
	 */
	private String items;

	/**
	 * 备注
	 */
	private String comments;
	/**
	 * 付款金额
	 */
	private Double payAmount;

	/************************************一下字段在不是数据库字段************************************/

	private List<OperateItem> itemList;

	public static class OperateItem {
		private Integer type;
		private Long id;

		public OperateItem() {

		}

		public OperateItem(Integer type, Long id) {
			super();
			this.type = type;
			this.id = id;
		}

		public Integer getType() {
			return type;
		}

		public void setType(Integer type) {
			this.type = type;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}
	}

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

	public Long getCreater() {
		return creater;
	}

	public void setCreater(Long creater) {
		this.creater = creater;
	}

	public Double getReceivableAmt() {
		return receivableAmt;
	}

	public void setReceivableAmt(Double receivableAmt) {
		this.receivableAmt = receivableAmt;
	}

	public Double getActualAmt() {
		return actualAmt;
	}

	public void setActualAmt(Double actualAmt) {
		this.actualAmt = actualAmt;
	}

	public Integer getOperateType() {
		return operateType;
	}

	public void setOperateType(Integer operateType) {
		this.operateType = operateType;
	}

	public String getItems() {
		if (StringUtils.isEmpty(items)) {
			return JSON.toJSONString(itemList);
		}
		return items;
	}

	public void setItems(String items) {
		this.items = items;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public List<OperateItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<OperateItem> itemList) {
		this.itemList = itemList;
	}

	public Double getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
	}
}
