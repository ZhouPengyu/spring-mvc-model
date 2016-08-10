package com.hm.his.module.instock.model;

import java.util.Date;
import java.util.List;

public class BatchInstock {
    private Long id;

    private Long hospitalId;

    private String batchNo;

    private String supplier;

    private String supplierPhoneNo;

    private Long purchaseOrderId;

    private Double totalPrice;

    private Date createDate;

    private Date modifyDate;

    private Long creater;

    private Long modifier;

    //页面属性 ========================
    private List<DrugBatchInstock> batchInstockList; //订单中的药品信息

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Long hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getSupplierPhoneNo() {
        return supplierPhoneNo;
    }

    public void setSupplierPhoneNo(String supplierPhoneNo) {
        this.supplierPhoneNo = supplierPhoneNo;
    }

    public Long getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(Long purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
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

    public Long getCreater() {
        return creater;
    }

    public void setCreater(Long creater) {
        this.creater = creater;
    }

    public Long getModifier() {
        return modifier;
    }

    public void setModifier(Long modifier) {
        this.modifier = modifier;
    }

    public List<DrugBatchInstock> getBatchInstockList() {
        return batchInstockList;
    }

    public void setBatchInstockList(List<DrugBatchInstock> batchInstockList) {
        this.batchInstockList = batchInstockList;
    }
}