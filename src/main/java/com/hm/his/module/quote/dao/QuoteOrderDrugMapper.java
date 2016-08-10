package com.hm.his.module.quote.dao;

import com.hm.his.module.quote.model.QuoteOrderDrug;

import java.util.List;

public interface QuoteOrderDrugMapper {

    Integer insertQuoteDrug(QuoteOrderDrug drug);

    Integer insertQuoteOrderDrugRecord(QuoteOrderDrug drug);

    QuoteOrderDrug getQuoteOrderDrugDetail(QuoteOrderDrug drug);

    void updateQuoteOrderDrug(QuoteOrderDrug drug);
    /**
     * <p>description: 通过供货单id获取供货单药品list</p>
     * @author SuShaohua
     * @date 2016/6/2 13:14
     * @param
     */
    List<QuoteOrderDrug> getQuoteOrderDrug(Integer quoteOrderId);

    Integer queryQuoteDrugPrimaryKey(QuoteOrderDrug drug);
}