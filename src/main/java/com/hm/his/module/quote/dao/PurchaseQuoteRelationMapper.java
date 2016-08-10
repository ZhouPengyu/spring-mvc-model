package com.hm.his.module.quote.dao;

import com.hm.his.module.quote.model.PurchaseQuoteRelation;

public interface PurchaseQuoteRelationMapper {
	
    int deleteByPrimaryKey(Integer purQuoRelationId);

    int insertPurQuoRealtion(PurchaseQuoteRelation record);

    int insertSelective(PurchaseQuoteRelation record);

    PurchaseQuoteRelation selectByPrimaryKey(Integer purQuoRelationId);

    int updateByPrimaryKeySelective(PurchaseQuoteRelation record);

    int updateByPrimaryKey(PurchaseQuoteRelation record);

    int updatePurQuoRealtionStatus(PurchaseQuoteRelation record);
}