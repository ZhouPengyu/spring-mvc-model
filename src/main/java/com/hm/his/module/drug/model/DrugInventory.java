package com.hm.his.module.drug.model;

import java.util.Date;

/**
 * 诊所药品库存数量表
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/2/26
 * Time: 14:42
 * CopyRight:HuiMei Engine
 */
public class DrugInventory {
    private Long id;
    private Long hospitalId;//	int	是	诊所ID
    private Long drugId;//	Int	是	药品ID

    private Double inventoryCount;//	int	是	库存统计
    private String goodsShelfCode;//	String	否	货架码
    private Integer inventoryThreshold;//阈值
    private Integer inventoryCeiling;//库存上限数量
    private Date validityDate;//有效期


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

    public Double getInventoryCount() {
        return inventoryCount;
    }

    public void setInventoryCount(Double inventoryCount) {
        this.inventoryCount = inventoryCount;
    }

    public String getGoodsShelfCode() {
        return goodsShelfCode;
    }

    public void setGoodsShelfCode(String goodsShelfCode) {
        this.goodsShelfCode = goodsShelfCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getInventoryThreshold() {
        return inventoryThreshold;
    }

    public void setInventoryThreshold(Integer inventoryThreshold) {
        this.inventoryThreshold = inventoryThreshold;
    }

    public Integer getInventoryCeiling() {
        return inventoryCeiling;
    }

    public void setInventoryCeiling(Integer inventoryCeiling) {
        this.inventoryCeiling = inventoryCeiling;
    }

    public Date getValidityDate() {
        return validityDate;
    }

    public void setValidityDate(Date validityDate) {
        this.validityDate = validityDate;
    }
}
