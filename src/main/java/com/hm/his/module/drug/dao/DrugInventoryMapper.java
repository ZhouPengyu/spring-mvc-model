package com.hm.his.module.drug.dao;

import com.hm.his.module.drug.model.DrugInventory;
import com.hm.his.module.drug.model.HospitalDrug;
import com.hm.his.module.instock.pojo.HospitalDrugInventoryPojo;
import com.hm.his.module.instock.pojo.InstockRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DrugInventoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insertDrugInventoryByList(List<DrugInventory> drugInventoryList);

    int insert(DrugInventory record);

    int insertSelective(DrugInventory record);

    DrugInventory selectByPrimaryKey(Integer id);

    DrugInventory selectByDrugId(@Param("drugId") Long drugId);

    int updateByPrimaryKeySelective(DrugInventory record);

    int updateByPrimaryKey(DrugInventory record);

    int updateByHospitalIdAndDrugId(DrugInventory record);

    int cutInventoryByHospitalIdAndDrugId(DrugInventory record);

    int returnInventoryByHospitalIdAndDrugId(DrugInventory record);

    List<HospitalDrugInventoryPojo> searchDrugListForValidityWarning(InstockRequest instockRequest);

    int countDrugListForValidityWarning(InstockRequest instockRequest);
}