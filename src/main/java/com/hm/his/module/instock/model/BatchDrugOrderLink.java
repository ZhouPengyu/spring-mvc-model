package com.hm.his.module.instock.model;

public class BatchDrugOrderLink {
    private Long id;

    private Long orderDrugId;//订单药品关联ID

    private Long drugId;

    private Integer status;//状态 ：1 :已收费  0：已退费

    private Long batchId;//药品批次id

    private Double purchasePrice;//进货价

    private Double amount;//数量

    private String saleUnit;//库存单位

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderDrugId() {
        return orderDrugId;
    }

    public void setOrderDrugId(Long orderDrugId) {
        this.orderDrugId = orderDrugId;
    }

    public Long getDrugId() {
        return drugId;
    }

    public void setDrugId(Long drugId) {
        this.drugId = drugId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getSaleUnit() {
        return saleUnit;
    }

    public void setSaleUnit(String saleUnit) {
        this.saleUnit = saleUnit == null ? null : saleUnit.trim();
    }
}