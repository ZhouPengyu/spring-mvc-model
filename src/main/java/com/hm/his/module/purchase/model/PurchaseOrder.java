package com.hm.his.module.purchase.model;

import java.util.List;


public class PurchaseOrder {
    private Integer purchaseOrderId;

    /* 订单编号*/
    private String purchaseOrderNumber;

    /**
     * 采购单状态
     0.已取消：采购单被取消
     1.尚无报价：没有供货商报价
     2.已报价：供货商报价
     3.待出库：确定供货商后，等待出库
     4.已发货：供货商发货
     5.已完成：采购单确认入库
     */
    private Integer status;

    private String remark;

    private String message;

    private Integer deliveryAddressId;
    
    private String deliveryAddress;

    private Integer creater;
    
    private String createrName;

    private String createDate;
    /*删除标识:1正常，0删除*/
    private Integer flag;
    
    private Integer designated;	//1是指定厂家  0 是不指定厂家
    private List<PurchaseOrderDrug> purchaseOrderDrugList;

    public Integer getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(Integer purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    public void setPurchaseOrderNumber(String purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public Integer getDeliveryAddressId() {
        return deliveryAddressId;
    }

    public void setDeliveryAddressId(Integer deliveryAddressId) {
        this.deliveryAddressId = deliveryAddressId;
    }

    public Integer getCreater() {
        return creater;
    }

    public void setCreater(Integer creater) {
        this.creater = creater;
    }

    public String getCreaterName() {
		return createrName;
	}

	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}

	public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

	public Integer getDesignated() {
		return designated;
	}

	public void setDesignated(Integer designated) {
		this.designated = designated;
	}

	public List<PurchaseOrderDrug> getPurchaseOrderDrugList() {
		return purchaseOrderDrugList;
	}

	public void setPurchaseOrderDrugList(
			List<PurchaseOrderDrug> purchaseOrderDrugList) {
		this.purchaseOrderDrugList = purchaseOrderDrugList;
	}
}