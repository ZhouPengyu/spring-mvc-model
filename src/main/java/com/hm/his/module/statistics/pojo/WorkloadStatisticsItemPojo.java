package com.hm.his.module.statistics.pojo;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;
import java.util.List;

/**
 * Created by SuShaohua on 2016/7/5.
 */
public class WorkloadStatisticsItemPojo {
    Long itemId;
    String orderNo;
    String doctorName;
    Integer itemType;
    String itemTypeName;
    String itemName;
    /**
     * 单位
     */
    String saleUnit;
    String specification;
    String manufacturer;
    Double count;
    /**
     * 数量+单位
     */
    String countDesc;
    Double receivableAmt;
    @JSONField(format = "yyyy-MM-dd")
    Date date;
    List<String> orderNoList;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    public String getSaleUnit() {
        return saleUnit;
    }

    public void setSaleUnit(String saleUnit) {
        this.saleUnit = saleUnit;
    }

    public Double getReceivableAmt() {
        return receivableAmt;
    }

    public void setReceivableAmt(Double receivableAmt) {
        this.receivableAmt = receivableAmt;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemTypeName() {
        return itemTypeName;
    }

    public void setItemTypeName(String itemTypeName) {
        this.itemTypeName = itemTypeName;
    }

    public String getCountDesc() {
        String s1 = count == null ? "" : count.toString();
        String s2 = saleUnit == null ? "" : saleUnit;
        return s1 + s2;
    }

    public void setCountDesc(String countDesc) {
        this.countDesc = countDesc;
    }

    public List<String> getOrderNoList() {
        return orderNoList;
    }

    public void setOrderNoList(List<String> orderNoList) {
        this.orderNoList = orderNoList;
    }
}
