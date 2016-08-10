package com.hm.his.module.drug.pojo;

/**
 * 诊所 药品 销售方式 ---根据 支持拆分 来算
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/3/1
 * Time: 14:30
 * CopyRight:HuiMei Engine
 */
public class SaleWayPojo {
    private Double dosage;//默认开药量
    private String unit;//单位
    private Double price;//单价
    private Double inventoryCount;//库存
    private Double purchasePrice;//	double	是	进货价

    public Double getDosage() {
        return dosage;
    }

    public void setDosage(Double dosage) {
        this.dosage = dosage;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getInventoryCount() {
        return inventoryCount;
    }

    public void setInventoryCount(Double inventoryCount) {
        this.inventoryCount = inventoryCount;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    @Override
    public String toString() {
        return "SaleWayPojo{" +
                "dosage=" + dosage +
                ", unit='" + unit + '\'' +
                ", price=" + price +
                ", inventoryCount=" + inventoryCount +
                ", purchasePrice=" + purchasePrice +
                '}';
    }
}
