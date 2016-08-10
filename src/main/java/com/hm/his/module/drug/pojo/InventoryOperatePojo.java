package com.hm.his.module.drug.pojo;

import java.util.Date;

/**
 * 诊所药品库存操作对象
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/2/29
 * Time: 10:04
 * CopyRight:HuiMei Engine
 */
public class InventoryOperatePojo {

    private Long drugId;
    private Long hospitalId;
    private String unit;
    private Double amount;//本次操作库存数量
    private Long orderDrugId;//订单药品关联ID
    private Double totalPurchasePrice;// 本药品 本次的所有的进货价

    public Long getDrugId() {
        return drugId;
    }

    public void setDrugId(Long drugId) {
        this.drugId = drugId;
    }

    public Long getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Long hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getOrderDrugId() {
        return orderDrugId;
    }

    public void setOrderDrugId(Long orderDrugId) {
        this.orderDrugId = orderDrugId;
    }

    public Double getTotalPurchasePrice() {
        return totalPurchasePrice;
    }

    public void setTotalPurchasePrice(Double totalPurchasePrice) {
        this.totalPurchasePrice = totalPurchasePrice;
    }

    @Override
    public String toString() {
        return "InventoryOperatePojo{" +
                "drugId=" + drugId +
                ", hospitalId=" + hospitalId +
                ", unit='" + unit + '\'' +
                ", amount=" + amount +
                ", orderDrugId=" + orderDrugId +
                ", totalPurchasePrice=" + totalPurchasePrice +
                '}';
    }
}
