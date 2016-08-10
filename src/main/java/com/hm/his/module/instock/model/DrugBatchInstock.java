package com.hm.his.module.instock.model;

import java.util.Date;
/**
 *  功能描述：批次入库的药品信息
 * @author:  tangww
 * @createDate   2016-05-31
 *
 */
public class DrugBatchInstock {
    private Long id;

    private Long batchInstockId;

    private Long hospitalId;

    private Long drugId;

    private Double instockCount;

    private String instockUnit;

    private Double surplusCount;

    private Date validityDate;

    private String drugBatchNo;

    private Double purchasePrice;//进货价

    private Double prescriptionPrice;//处方价

    private Integer flag; //操作标志：  1: 用户入库    0：系统切换生成

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBatchInstockId() {
        return batchInstockId;
    }

    public void setBatchInstockId(Long batchInstockId) {
        this.batchInstockId = batchInstockId;
    }

    public Long getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Long hospitalId) {
        this.hospitalId = hospitalId;
    }

    public Long getDrugId() {
        return drugId;
    }

    public void setDrugId(Long drugId) {
        this.drugId = drugId;
    }

    public Double getInstockCount() {
        return instockCount;
    }

    public void setInstockCount(Double instockCount) {
        this.instockCount = instockCount;
    }

    public String getInstockUnit() {
        return instockUnit;
    }

    public void setInstockUnit(String instockUnit) {
        this.instockUnit = instockUnit;
    }

    public Double getSurplusCount() {
        return surplusCount;
    }

    public void setSurplusCount(Double surplusCount) {
        this.surplusCount = surplusCount;
    }

    public Date getValidityDate() {
        return validityDate;
    }

    public void setValidityDate(Date validityDate) {
        this.validityDate = validityDate;
    }

    public String getDrugBatchNo() {
        return drugBatchNo;
    }

    public void setDrugBatchNo(String drugBatchNo) {
        this.drugBatchNo = drugBatchNo;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Double getPrescriptionPrice() {
        return prescriptionPrice;
    }

    public void setPrescriptionPrice(Double prescriptionPrice) {
        this.prescriptionPrice = prescriptionPrice;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }
}