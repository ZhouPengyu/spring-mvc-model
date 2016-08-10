package com.hm.his.module.instock.pojo;

import com.hm.his.framework.utils.DoubleWrap;
import com.hm.his.module.drug.model.HospitalDrug;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016-06-03
 * Time: 14:39
 * CopyRight:HuiMei Engine
 */
public class HospitalDrugInventoryPojo extends HospitalDrug {
    private Long batchInstockId;
    private Long drugBatchInstockId;
    private String batchNo;

    private Double instockCount;

    private String instockUnit;

    private Date validityDate;

    private String drugBatchNo;

    private String statusDesc;
    private String isOTCDesc;
    private Double surplusCount;
    private Double totalPrice;



    public Long getDrugBatchInstockId() {
        return drugBatchInstockId;
    }

    public void setDrugBatchInstockId(Long drugBatchInstockId) {
        this.drugBatchInstockId = drugBatchInstockId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
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

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getIsOTCDesc() {
        return isOTCDesc;
    }

    public void setIsOTCDesc(String isOTCDesc) {
        this.isOTCDesc = isOTCDesc;
    }

    public Double getSurplusCount() {
        return surplusCount;
    }

    public void setSurplusCount(Double surplusCount) {
        this.surplusCount = surplusCount;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Long getBatchInstockId() {
        return batchInstockId;
    }

    public void setBatchInstockId(Long batchInstockId) {
        this.batchInstockId = batchInstockId;
    }
}
