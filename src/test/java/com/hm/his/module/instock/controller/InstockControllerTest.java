package com.hm.his.module.instock.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.hm.his.BaseControllerTest;
import com.hm.his.module.drug.pojo.DrugRequest;
import com.hm.his.module.instock.model.BatchInstock;
import com.hm.his.module.instock.model.DrugBatchInstock;
import com.hm.his.module.instock.pojo.InstockRequest;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016-06-02
 * Time: 11:00
 * CopyRight:HuiMei Engine
 */
public class InstockControllerTest   extends BaseControllerTest {

    @Test
    public void testSubmitDrugInsock() throws Exception {
        BatchInstock batchInstock = new BatchInstock();
        batchInstock.setSupplier("同仁堂");
        batchInstock.setSupplierPhoneNo("18611211898");
        batchInstock.setTotalPrice(550D);
        batchInstock.setPurchaseOrderId(23L);

        List<DrugBatchInstock> batchInstockList = Lists.newArrayList();
        DrugBatchInstock instock1 = new DrugBatchInstock();
        instock1.setDrugId(1L);
        instock1.setInstockCount(12D);
        instock1.setInstockUnit("盒");
        instock1.setValidityDate(new Date());
        instock1.setPurchasePrice(18.5);
        instock1.setPrescriptionPrice(32.8);
        batchInstockList.add(instock1);

        DrugBatchInstock instock2 = new DrugBatchInstock();
        instock2.setDrugId(109L);
        instock2.setInstockCount(10D);
        instock2.setInstockUnit("盒");
        instock2.setValidityDate(new Date());
        instock2.setPurchasePrice(10.5);
        batchInstockList.add(instock2);


        DrugBatchInstock instock3 = new DrugBatchInstock();
        instock3.setDrugId(100L);
        instock3.setInstockCount(5D);
        instock3.setInstockUnit("盒");
        instock3.setValidityDate(new Date());
        instock3.setPurchasePrice(105.5);
        batchInstockList.add(instock3);

        batchInstock.setBatchInstockList(batchInstockList);

        super.testRole("/instock/submitDrugInsock", JSON.toJSONString(batchInstock));
    }

    @Test
    public void testSearchDrugInventoryList() throws Exception {
        DrugRequest request = new DrugRequest();
        request.setDrugName("999感冒");
//        request.setDrugType(3);
        request.setStatus(1);
        request.setCurrentPage(1);
        request.setPageSize(15);
        super.testRole("/instock/searchDrugInventoryList", JSON.toJSONString(request));
    }

    @Test
    public void testSearchDrugInventoryListByBatch() throws Exception {
        DrugRequest request = new DrugRequest();
        request.setDrugName("感冒");
//        request.setDrugType(3);
        request.setStatus(1);
        request.setCurrentPage(1);
        request.setPageSize(15);
        super.testRole("/instock/searchDrugInventoryListByBatch", JSON.toJSONString(request));
    }

    @Test
    public void testSearchBatchDrugInventoryDetail() throws Exception {
        InstockRequest instockRequest = new InstockRequest();
        instockRequest.setDrugBatchInstockId(8L);
        instockRequest.setDrugId(109L);
        super.testRole("/instock/searchBatchDrugInventoryDetail", JSON.toJSONString(instockRequest));
    }

    @Test
    public void testModifyDrugInventory() throws Exception {
        DrugBatchInstock drugBatchInstock = new DrugBatchInstock();
        drugBatchInstock.setId(8L);
        drugBatchInstock.setDrugId(109L);
        drugBatchInstock.setPurchasePrice(20.3);
        drugBatchInstock.setPrescriptionPrice(55.8);
        drugBatchInstock.setSurplusCount(5D);
        drugBatchInstock.setValidityDate(new Date());
        super.testRole("/instock/modifyBatchDrugInventory", JSON.toJSONString(drugBatchInstock));
    }

    @Test
    public void testSearchDrugListForInventoryWarning() throws Exception {
        InstockRequest instockRequest = new InstockRequest();
        instockRequest.setHospitalId(1L);
        instockRequest.setPageSize(20);
        super.testRole("/instock/searchDrugListForInventoryWarning", JSON.toJSONString(instockRequest));

    }

    @Test
    public void testSearchDrugListForValidityWarning() throws Exception {
        InstockRequest instockRequest = new InstockRequest();
        instockRequest.setTimeQuantum(365);
        instockRequest.setHospitalId(1L);
        instockRequest.setPageSize(20);
        super.testRole("/instock/searchDrugListForValidityWarning", JSON.toJSONString(instockRequest));
    }

    @Test
    public void testSearchBatchInstockLog() throws Exception {
        InstockRequest instockRequest = new InstockRequest();
        instockRequest.setStartDate("2016-04-15");
        instockRequest.setEndDate("2016-08-15");
        instockRequest.setDrugType(1);
        instockRequest.setHospitalId(1L);
        instockRequest.setPageSize(20);
        super.testRole("/instock/searchBatchInstockLog", JSON.toJSONString(instockRequest));
    }

    @Test
    public void testExportBatchInstockLog() throws Exception {

    }

    @Test
    public void testDeleteBatchInstock() throws Exception {

    }
}