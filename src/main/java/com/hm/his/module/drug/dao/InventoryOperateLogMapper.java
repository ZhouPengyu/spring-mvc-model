package com.hm.his.module.drug.dao;

import com.hm.his.module.drug.model.InventoryOperateLog;

public interface InventoryOperateLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(InventoryOperateLog record);

    int insertSelective(InventoryOperateLog record);

    InventoryOperateLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(InventoryOperateLog record);

    int updateByPrimaryKey(InventoryOperateLog record);
}