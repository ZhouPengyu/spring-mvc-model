package com.hm.his.module.outpatient.model;

/**
 * <p>Description:中药处方饮片<p>
 * <p>Company: H.M<p>
 * @author ZhouPengyu
 * @date 2016-1-26 14:20:31
 */
public class PatientChineseDrugPieces {
	private Long paChDrugPiecesId;
    private Long paChDrugId;	//中药处方ID
    private Long drugId;
    private String drugName;
    private Double value;		//药片剂量
    private String unit;	//剂量单位
    private String  comment;
    private Double price;	//价格
    private Double univalence;	//出售单价
    private Long dataSource;	//药品来源 1,惠每 0,诊所
    private String specification;	//药品规格
    
	public Long getPaChDrugPiecesId() {
		return paChDrugPiecesId;
	}
	public void setPaChDrugPiecesId(Long paChDrugPiecesId) {
		this.paChDrugPiecesId = paChDrugPiecesId;
	}
	public Long getPaChDrugId() {
		return paChDrugId;
	}
	public void setPaChDrugId(Long paChDrugId) {
		this.paChDrugId = paChDrugId;
	}
	public Long getDrugId() {
		return drugId;
	}
	public void setDrugId(Long drugId) {
		this.drugId = drugId;
	}
	public String getDrugName() {
		return drugName;
	}
	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Long getDataSource() {
		return dataSource;
	}
	public void setDataSource(Long dataSource) {
		this.dataSource = dataSource;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getUnivalence() {
		return univalence;
	}
	public void setUnivalence(Double univalence) {
		this.univalence = univalence;
	}
	public String getSpecification() {
		return specification;
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
    
}
