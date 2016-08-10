package com.hm.his.module.outpatient.model;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-4 11:28:35
 * @description 附加费用实体类
 * @version 3.0
 */
public class PatientAdditional {

	private Long additionalId;
	private String additionalName;
	private Double additionalPrice;
	private Long recordId;
	private Long flag;
	
	private Integer isCharged;	//是否收费
	
	public Long getAdditionalId() {
		return additionalId;
	}
	public void setAdditionalId(Long additionalId) {
		this.additionalId = additionalId;
	}
	public String getAdditionalName() {
		return additionalName;
	}
	public void setAdditionalName(String additionalName) {
		this.additionalName = additionalName;
	}
	public Double getAdditionalPrice() {
		return additionalPrice;
	}
	public void setAdditionalPrice(Double additionalPrice) {
		this.additionalPrice = additionalPrice;
	}
	public Long getRecordId() {
		return recordId;
	}
	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}
	public Long getFlag() {
		return flag;
	}
	public void setFlag(Long flag) {
		this.flag = flag;
	}
	public Integer getIsCharged() {
		return isCharged;
	}
	public void setIsCharged(Integer isCharged) {
		this.isCharged = isCharged;
	}
	
}
