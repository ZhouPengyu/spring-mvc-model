package com.hm.his.module.order.model;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.hm.his.framework.utils.AmtUtils;
import com.hm.his.module.drug.model.DrugTypeEnum;
import com.hm.his.module.order.pojo.SaleChannel;

/**
 * 
 * @description 订单中的药品或耗材
 * @author lipeng
 * @date 2016年2月28日
 */
public class OrderDrug extends OrderItem {
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
	 * 药物id
	 */
	private Long drugId;
	/**
	 * 药物类型 
	 */
	private Integer drugType;
	/**
	 * 销售单位
	 */
	private String saleUnit;
	/**
	 * 药物名称
	 */
	private String drugName;
	/**
	 * 厂家名称 
	 */
	private String manufacturer;
	/**
	 * 药品规格 
	 */
	private String specification;

	/**
	 * 单价
	 */
	private Double salePrice;
	/**
	 *处方中 西药数量或中药剂数，或者直接售药中的数量
	 */
	private Integer dosage;
	/**
	 * 饮片单剂量
	 */
	private Double gramCount;
	/**
	 * 数据标识  0 删除  1 可用
	 */
	private Integer flag;

	/**
	 * 病历id
	 */
	private Long recordId;
	/**
	 * 数据来源：1：来自惠每，0：来自诊所，5：手动录入
	 */
	private Integer dataSource;
	/**
	 * 销售渠道 
	 */
	private Integer saleChannel;
	/**
	 * 进货价
	 */
	private Double purchasePrice;
	/**
	 * 利润
	 */
	private Double profit;

	/**
	 * 医院id
	 */
	private Long hospitalId;

	/*************************************************************/

	private OrderItemCharge orderItemCharge;
	/**
	 * 数量描述
	 */
	private String count;
	/**
	 * 中药 ：总克数=dosage*gramCount
	 */
	private Double totalGram;
	/**
	 * 应收金额
	 */
	private Double receivableAmt;
	/**
	 * 费用状态 待收费，已收费，已退费
	 */
	private Integer chargeStatus;
	/**
	 * 是否已退费
	 */
	private boolean isRefunded;
	/**
	 * 成药用药频率
	 */
	private String frequency;
	/**
	 * 成药用法
	 */
	private String usage;
	/**
	 * 成药单次剂量
	 */
	private String onceDosage;
	/**
	 * 成药单次剂量单位
	 */
	private String onceDosageUnit;
	/**
	 * 中药备注
	 */
	private String remark;
	/**
	 * 医生id
	 */
	private Long creater;

	private String doctorName;
	/**
	 * 
	 * @description 药物标识，非诊所药物的 DrugIdentify一致表明是同一个药
	 * @author lipeng
	 * @date 2016年4月5日
	 */
	public static class DrugIdentify {
		String drugName;
		String specification;
		String manufacturer;

		public String getDrugName() {
			return drugName;
		}

		public void setDrugName(String drugName) {
			this.drugName = drugName;
		}

		public String getSpecification() {
			return specification;
		}

		public void setSpecification(String specification) {
			this.specification = specification;
		}

		public String getManufacturer() {
			return manufacturer;
		}

		public void setManufacturer(String manufacturer) {
			this.manufacturer = manufacturer;
		}

		public String toJsonString() {
			return JSON.toJSONString(this);
		}
	}

	public String getDrugIdentify() {
		DrugIdentify drugIdentify = new DrugIdentify();
		drugIdentify.setDrugName(this.getDrugName());
		drugIdentify.setManufacturer(this.getManufacturer());
		drugIdentify.setSpecification(this.getSpecification());
		return drugIdentify.toJsonString();
	}

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

	public Long getDrugId() {
		return drugId;
	}

	public void setDrugId(Long drugId) {
		this.drugId = drugId;
	}

	public Integer getDrugType() {
		return drugType;
	}

	public void setDrugType(Integer drugType) {
		this.drugType = drugType;
	}

	public String getSaleUnit() {
		return saleUnit;
	}

	public void setSaleUnit(String saleUnit) {
		this.saleUnit = saleUnit;
	}

	public String getDrugName() {
		return drugName;
	}

	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
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

	public String getCount() {
		String saleUnit = this.saleUnit == null ? "" : this.saleUnit;
		String dosage = this.dosage == null ? "0" : this.dosage + "";
		String gramCount = this.gramCount == null ? "0" : this.gramCount + "";
		String count = "";
		if (DrugTypeEnum.herbal.getDrugType() == drugType && SaleChannel.Prescription.getType() == this.getSaleChannel()) {
			if (StringUtils.isEmpty(saleUnit)) {
				// 中药默认单位为g
				saleUnit = "g";
			}
			if (StringUtils.isEmpty(gramCount) || "0".equals(gramCount)) {
				count = dosage + saleUnit;
			} else {
				count = gramCount + saleUnit + "*" + dosage;
			}
			return count;
		} else {
			return dosage + saleUnit;
		}
	}

	public void setCount(String count) {
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

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
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

	/**
	 * 
	 * @description 处方中 西药数量或中药剂数，或者直接售药中的数量
	 * @date 2016年4月8日
	 * @author lipeng
	 * @return
	 */
	public Integer getDosage() {
		return dosage;
	}

	public void setDosage(Integer dosage) {
		this.dosage = dosage;
	}

	/**
	 * 
	 * @description 饮片单剂量,中药处方中使用
	 * @date 2016年4月8日
	 * @author lipeng
	 * @return
	 */
	public Double getGramCount() {
		return gramCount;
	}

	public void setGramCount(Double gramCount) {
		this.gramCount = gramCount;
	}

	/**
	 * 
	 * @description 返回总克数，用于处方开药，是中药的情况，返回剂数*单剂量
	 * @date 2016年6月16日
	 * @author lipeng
	 * @return
	 */
	public Double getTotalGram() {
		if (this.saleChannel != null && this.saleChannel == SaleChannel.Prescription.getType()) {
			if (dosage != null && gramCount != null) {
				return dosage * gramCount;
			}
			return 0D;
		} else if (this.saleChannel != null && this.saleChannel == SaleChannel.Sell.getType()) {
			return dosage == null ? 0D : dosage.doubleValue();
		}
		return 0D;

	}

	public void setTotalGram(Double totalGram) {
		this.totalGram = totalGram;
	}

	public Integer getDataSource() {
		return dataSource;
	}

	public void setDataSource(Integer dataSource) {
		this.dataSource = dataSource;
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

	public Integer getSaleChannel() {
		return saleChannel;
	}

	public void setSaleChannel(Integer saleChannel) {
		this.saleChannel = saleChannel;
	}

	public Double getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(Double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public Double getProfit() {
		return profit;
	}

	public void setProfit(Double profit) {
		this.profit = profit;
	}

	public Long getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

	public String getOnceDosage() {
		return onceDosage;
	}

	public void setOnceDosage(String onceDosage) {
		this.onceDosage = onceDosage;
	}

	public String getOnceDosageUnit() {
		return onceDosageUnit;
	}

	public void setOnceDosageUnit(String onceDosageUnit) {
		this.onceDosageUnit = onceDosageUnit;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
