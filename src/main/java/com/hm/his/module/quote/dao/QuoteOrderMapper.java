package com.hm.his.module.quote.dao;

import com.hm.his.module.quote.model.PurchaseQuoteRelation;
import com.hm.his.module.quote.model.QuoteOrder;
import com.hm.his.module.quote.model.QuoteOrderDrug;
import com.hm.his.module.quote.pojo.QuoteOrderRequest;

import java.util.List;

public interface QuoteOrderMapper {

    int insertQuoteOrder(QuoteOrder record);

    int updateQuoteOrder(QuoteOrder record);

    int insertQuoteOrderRecord(QuoteOrder req);

    QuoteOrder queryQuoteOrderByOrderId(Integer quoteOrderId);

    List<QuoteOrder> queryQuoteOrder(QuoteOrderRequest req);

    Integer queryQuoteOrderCount(QuoteOrderRequest req);

    List<QuoteOrder> queryQuoteOrderByCreater(QuoteOrderRequest req);

    Integer cancelQuoteOrder(QuoteOrderRequest req);

    QuoteOrder getQuoteOrderDetailByPurchaseOrderId(QuoteOrderRequest req);

    List<QuoteOrder> queryQuoteOrderByPurchaseOrderId(Integer purchaseOrderId);

    List<Integer> queryQuoteOrderId(Integer quoteOrderId);

    Integer queryQuoteOrderPrimaryKey(QuoteOrderRequest req);
}