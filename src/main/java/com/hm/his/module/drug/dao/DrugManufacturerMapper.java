package com.hm.his.module.drug.dao;

import com.hm.his.module.drug.model.DrugManufacturer;

import java.util.List;

public interface DrugManufacturerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DrugManufacturer record);

    int insertSelective(DrugManufacturer record);

    DrugManufacturer selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DrugManufacturer record);

    int updateByPrimaryKey(DrugManufacturer record);

    List<DrugManufacturer> searchManufacturerByName(DrugManufacturer record);

    List<DrugManufacturer> selectAll();
}