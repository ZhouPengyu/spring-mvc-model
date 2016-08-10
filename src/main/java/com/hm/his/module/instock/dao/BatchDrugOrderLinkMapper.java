package com.hm.his.module.instock.dao;

import com.hm.his.module.instock.model.BatchDrugOrderLink;

import java.util.List;

public interface BatchDrugOrderLinkMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BatchDrugOrderLink record);

    int insertSelective(BatchDrugOrderLink record);

    BatchDrugOrderLink selectByPrimaryKey(Integer id);

    List<BatchDrugOrderLink> selectByDrugIdAndOrderId(BatchDrugOrderLink batchDrugOrderLink);

    int updateByPrimaryKeySelective(BatchDrugOrderLink record);

    int updateByPrimaryKey(BatchDrugOrderLink record);
}