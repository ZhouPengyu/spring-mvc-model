package com.hm.his.module.drug.controller;

import com.alibaba.fastjson.JSON;
import com.hm.his.BaseControllerTest;
import com.hm.his.module.drug.model.HospitalDrug;
import com.hm.his.module.drug.pojo.DrugRequest;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/4/27
 * Time: 14:37
 * CopyRight:HuiMei Engine
 */
public class WXDrugControllerTest  extends BaseControllerTest {

    @Test
    public void testSearchDrugByBarCode() throws Exception {
        DrugRequest request = new DrugRequest();
//条形码没有找到药物：1234567890124
//        request.setBarCode("1234567890124");
//诊所 只有一个药品：6939261900221
//        request.setBarCode("6939261900221");
        //诊所有多个药：6939261900221
//        request.setBarCode("6943118000033");
        //诊所没有，惠每中只有一条药品： 6923935900136
//        request.setBarCode("6923935900136");
        //诊所没有，惠每有三个药:6936693500173
        request.setBarCode("6901706210066");
        request.setHospitalId(1L);
        request.setDataSource(0);
        super.testRole("weixin/drug/searchDrugByBarCode", JSON.toJSONString(request));
    }


    @Test
    public void searchDrugByName() throws Exception {
        DrugRequest request = new DrugRequest();
//条形码没有找到药物：1234567890124
//        request.setBarCode("1234567890124");
//诊所 只有一个药品：6939261900221
//        request.setBarCode("6939261900221");
        //诊所有多个药：6939261900221
//        request.setBarCode("6943118000033");
        //诊所没有，惠每中只有一条药品： 6923935900136
//        request.setBarCode("6923935900136");
        //诊所没有，惠每有三个药:6936693500173
        request.setDrugName("BOG09021C");
        request.setHospitalId(1L);
        super.testRole("/weixin/drug/searchDrugByName", JSON.toJSONString(request));
    }

    @Test
    public void testSaveDrug() throws Exception {
        HospitalDrug hDrug = new HospitalDrug();
        hDrug.setHospitalId(200L);
        hDrug.setDrugName("阿莫西林胶囊66");
        hDrug.setHmDrugId(16L);
        hDrug.setDrugType(3);
        hDrug.setIsOtc(1);
        hDrug.setStatus(1);

        hDrug.setApprovalNumber("国药准字J20150030");
        hDrug.setBarCode("86900639000658");
        hDrug.setSingleDosage("1");
        hDrug.setSingleDosageUnit("粒");
        hDrug.setFrequency("每天三次");
        hDrug.setPrescribeAmount(2D);
        hDrug.setPrescribeAmountUnit("粒");
        hDrug.setInstruction("口服");
        hDrug.setDoctorAdvice("多喝水，少吹风");

        hDrug.setSpecMinimumDosage(0.5);
        hDrug.setSpecMinimumUnit("mg");
        hDrug.setSpecUnit("粒");
        hDrug.setSpecUnitaryRatio(3);
        hDrug.setSpecPackageUnit("盒");

        hDrug.setManufacturer("上海信谊黄河制药有限公司");
        hDrug.setPurchasePrice(24D);
        hDrug.setPrescriptionPrice(2D);
        hDrug.setInventoryCount(110D);
        hDrug.setGoodsShelfCode("1号柜3排");
        hDrug.setOpenStock(1);
        super.testRole("/weixin/drug/saveDrug", JSON.toJSONString(hDrug));
    }

    @Test
    public void testModifyDrug() throws Exception {
        HospitalDrug hDrug = new HospitalDrug();
        hDrug.setId(15L);
        hDrug.setApprovalNumber("国药准字J20150030");
        hDrug.setBarCode("86900639000658");
        hDrug.setSingleDosage("1");
        hDrug.setSingleDosageUnit("粒");
        hDrug.setFrequency("每天三次");
        hDrug.setPrescribeAmount(2D);
        hDrug.setPrescribeAmountUnit("粒");
        hDrug.setInstruction("口服");
        hDrug.setDoctorAdvice("多喝水，少吹风");



        super.testRole("/weixin/drug/modifyDrug", JSON.toJSONString(hDrug));
    }

    @Test
    public void testModifyInventoryAndPrice() throws Exception {
        HospitalDrug hDrug = new HospitalDrug();
        hDrug.setId(3437L);
        hDrug.setHospitalId(180L);
        hDrug.setPurchasePrice(9D);
        hDrug.setPrescriptionPrice(1.5);
        hDrug.setAddInventoryCount(15D);
        super.testRole("/weixin/drug/modifyInventoryAndPrice", JSON.toJSONString(hDrug));
    }

    @Test
    public void testSearchDrugByName() throws Exception {
        DrugRequest request = new DrugRequest();
        request.setDrugName("阿");
        request.setHospitalId(1L);
        request.setDataSource(0);
        super.testRole("/weixin/drug/searchDrugByName", JSON.toJSONString(request));

    }

    @Test
    public void testDrugDetail() throws Exception {
        DrugRequest request = new DrugRequest();
        request.setDrugId(12607L);
        request.setHospitalId(1L);
        request.setDataSource(1);
        super.testRole("/weixin/drug/drugDetail", JSON.toJSONString(request));

    }
}