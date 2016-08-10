package com.hm.his.module.instock.dao;

import com.hm.his.module.drug.model.HospitalDrug;
import com.hm.his.module.drug.pojo.DrugRequest;
import com.hm.his.module.instock.model.DrugBatchInstock;
import com.hm.his.module.instock.pojo.HospitalDrugInventoryPojo;
import com.hm.his.module.instock.pojo.InstockRequest;

import java.util.List;

public interface DrugBatchInstockMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DrugBatchInstock record);

    int batchInsertByList(List<DrugBatchInstock> drugBatchInstocks);

    int insertSelective(DrugBatchInstock record);

    DrugBatchInstock selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DrugBatchInstock record);

    int updateSurplusCountByDrugBatchInstock(DrugBatchInstock record);

    int updateSurplusCountForReturnInventory(DrugBatchInstock record);

    int cleanInsockDrugSurplusCountByHospitalId(Long hospitalId);


    int updateByPrimaryKey(DrugBatchInstock record);

    List<HospitalDrugInventoryPojo> searchDrugInventoryListByBatch(DrugRequest drugRequest);

    int countDrugInventoryListByBatch(DrugRequest drugRequest);

    HospitalDrugInventoryPojo searchBatchDrugInventoryDetail(InstockRequest instockRequest);

    List<HospitalDrugInventoryPojo> searchDrugListForValidityWarning(InstockRequest instockRequest);

    int countDrugListForValidityWarning(InstockRequest instockRequest);

    List<HospitalDrugInventoryPojo> searchBatchInstockLog(InstockRequest instockRequest);

    int countBatchInstockLog(InstockRequest instockRequest);

    List<DrugBatchInstock> searchDrugBatchInstockAndSurplusGreaterThanZero(Long drugId);

    DrugBatchInstock searchDrugBatchInstockByDrugId(Long drugId);
}