package com.hm.his.module.quote.model;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-5-25 17:20:04
 * @description 采购单报价单关联表实体类
 * @version 1.0
 */
public class PurchaseQuoteRelation {
	
    private Integer purQuoRelationId;
    private Integer purchaseOrderId;
    private Integer quoteOrderId;
    private Integer status;

    public Integer getPurQuoRelationId() {
        return purQuoRelationId;
    }
    public void setPurQuoRelationId(Integer purQuoRelationId) {
        this.purQuoRelationId = purQuoRelationId;
    }
    public Integer getPurchaseOrderId() {
        return purchaseOrderId;
    }
    public void setPurchaseOrderId(Integer purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }
    public Integer getQuoteOrderId() {
        return quoteOrderId;
    }
    public void setQuoteOrderId(Integer quoteOrderId) {
        this.quoteOrderId = quoteOrderId;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
}