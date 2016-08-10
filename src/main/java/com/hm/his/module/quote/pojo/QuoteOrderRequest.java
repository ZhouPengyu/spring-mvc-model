package com.hm.his.module.quote.pojo;

import com.hm.his.framework.model.PageRequest;
import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.module.purchase.model.request.PurchaseOrderRequest;
import com.hm.his.module.quote.model.QuoteOrder;
import org.apache.commons.lang3.StringUtils;

/**
 * @param
 * @author SuShaohua
 * @date 2016/6/1
 * @description
 */
public class QuoteOrderRequest extends PageRequest {
    private Integer purchaseOrderId;

    private String purchaseOrderNumber;

    private Integer status;

    private Long creater;

    private Integer quoteOrderId;

    private Integer isQuoted;

    private String startDate;

    private String endDate;

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

    public Long getCreater() {
        return creater;
    }

    public void setCreater(Long creater) {
        this.creater = creater;
    }

    public Integer getQuoteOrderId() {
        return quoteOrderId;
    }

    public void setQuoteOrderId(Integer quoteOrderId) {
        this.quoteOrderId = quoteOrderId;
    }

    public Integer getIsQuoted() {
        return isQuoted;
    }

    public void setIsQuoted(Integer isQuoted) {
        this.isQuoted = isQuoted;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public static void handleQuoteOrderRequest(QuoteOrderRequest req) {
        req.setCreater(SessionUtils.getDoctorId());
        req.setStartDate(StringUtils.isEmpty(req.getStartDate()) ? null : req.getStartDate() + " 00:00:00");
        req.setEndDate(StringUtils.isEmpty(req.getEndDate()) ? null : req.getEndDate() + " 23:59:59");
    }
}
