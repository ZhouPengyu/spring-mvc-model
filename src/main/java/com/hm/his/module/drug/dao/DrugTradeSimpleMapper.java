package com.hm.his.module.drug.dao;

import com.hm.his.module.drug.model.DrugTradeSimple;

public interface DrugTradeSimpleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DrugTradeSimple record);

    int insertSelective(DrugTradeSimple record);

    DrugTradeSimple selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DrugTradeSimple record);

    int updateByPrimaryKey(DrugTradeSimple record);
}