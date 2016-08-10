package com.hm.his.module.drug.model;

import java.util.Date;

/**
 * 药品库存操作日志表
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/2/29
 * Time: 10:04
 * CopyRight:HuiMei Engine
 */
public class InventoryOperateLog {
    private Long id;
    private Long drugId;
    private Long hospitalId;
    private Long userId;
    private String userName;
    private Integer result;//操作结果   1：成功； :0：失败；
    private Date operateDate;
    private Double beforeAmount;//操作前库存数量
    private Double amount;//本次操作库存数量
    private Integer operateAction; //操作动作

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Date getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(Date operateDate) {
        this.operateDate = operateDate;
    }

    public Double getBeforeAmount() {
        return beforeAmount;
    }

    public void setBeforeAmount(Double beforeAmount) {
        this.beforeAmount = beforeAmount;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getOperateAction() {
        return operateAction;
    }

    public void setOperateAction(Integer operateAction) {
        this.operateAction = operateAction;
    }
}
