package com.hm.his.module.instock.service;

import com.hm.his.framework.model.HisResponse;
import com.hm.his.module.drug.model.DrugTrade;
import com.hm.his.module.drug.model.HospitalDrug;
import com.hm.his.module.drug.pojo.DrugRequest;
import com.hm.his.module.drug.pojo.DrugResponse;
import com.hm.his.module.drug.pojo.HospitalDrugSug;
import com.hm.his.module.drug.pojo.SaleWayPojo;
import com.hm.his.module.instock.model.BatchInstock;
import com.hm.his.module.instock.model.DrugBatchInstock;
import com.hm.his.module.instock.pojo.HospitalDrugInventoryPojo;
import com.hm.his.module.instock.pojo.InstockRequest;

import java.util.List;

/**
 * 药品批次入库 服务
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/2/29
 * Time: 10:04
 * CopyRight:HuiMei Engine
 */
public interface InstockService {

    HisResponse submitDrugInsock(BatchInstock batchInstock);

    Long systemSubmitDrugInsock();

    int systemCleanInsockDrugSurplusCount();

    void searchDrugInventoryList(DrugRequest drugRequest);

    List<HospitalDrugInventoryPojo>  searchDrugInventoryListByBatch(DrugRequest drugRequest);

    int countDrugInventoryListByBatch(DrugRequest drugRequest);

    HospitalDrugInventoryPojo searchBatchDrugInventoryDetail(InstockRequest instockRequest);

    HisResponse modifyBatchDrugInventory(DrugBatchInstock drugBatchInstock);

    List<HospitalDrug> searchDrugListForInventoryWarning(InstockRequest instockRequest);

    int countDrugListForInventoryWarning(InstockRequest instockRequest);


    List<HospitalDrugInventoryPojo> searchDrugListForValidityWarning(InstockRequest instockRequest);

    int countDrugListForValidityWarning(InstockRequest instockRequest);

    List<HospitalDrugInventoryPojo>  searchBatchInstockLog(InstockRequest instockRequest);

    int countBatchInstockLog(InstockRequest instockRequest);

 }
