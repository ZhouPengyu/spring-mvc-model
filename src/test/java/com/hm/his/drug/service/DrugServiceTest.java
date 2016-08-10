package com.hm.his.drug.service;

import com.hm.his.framework.model.HisResponse;
import com.hm.his.module.drug.model.DrugManufacturer;
import com.hm.his.module.drug.model.HospitalDrug;
import com.hm.his.module.drug.pojo.DrugRequest;
import com.hm.his.module.drug.pojo.HospitalDrugSug;
import com.hm.his.module.drug.service.DrugManufacturerService;
import com.hm.his.module.drug.service.DrugTradeService;
import com.hm.his.module.order.model.SellDrug;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hm.his.module.drug.service.DrugService;

import java.util.Date;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-config.xml", "classpath:mybatis.xml" })


public class DrugServiceTest {
    @Autowired
    DrugService drugService;

    @Autowired
    DrugManufacturerService drugManufacturerService;


    @Test
    public void searchDrugList(){
        DrugRequest request = new DrugRequest();
//        request.setDrugName("板蓝根im");
//        request.setDrugType(3);
        request.setStatus(1);
        request.setCurrentPage(1);
        request.setPageSize(15);
        request.setHospitalId(1L);
        List<HospitalDrug> searchDrugList = drugService.searchDrugList(request);
        System.out.println("===============================================================");
        System.out.println(searchDrugList);

    }

    @Test
    public void addDefaultDrugForNewHospital(){

        HisResponse response = drugService.addDefaultDrugForNewHospital(7L,12L);
        System.out.println("===============================================================");
        System.out.println(response);

    }


//    @Test
//    public void addDefaultDrugForNewHospital(){
//
//        HisResponse hisResponse = drugService.addDefaultDrugForNewHospital(22L,1L);
//        System.out.println("===============================================================");
//        System.out.println(hisResponse);
//
//    }

    @Test
    public void searchDrugByName(){
        DrugRequest request = new DrugRequest();
        //request.setDrugName("板蓝根");

        //request.setDrugName("国药准字");
//        request.setDrugName("伪麻");
        request.setDrugName("XI");


        request.setHospitalId(1L);
        request.setDataSource(1);
        request.setLimit(10);
        List<HospitalDrugSug> searchDrugList = drugService.searchDrugByName(request);
        System.out.println(searchDrugList);

    }
    @Test
    public void searchDrugByNameForSale(){
        DrugRequest request = new DrugRequest();

        request.setDrugName("6939261900221");


        request.setHospitalId(1L);
        request.setDataSource(2);
        List<HospitalDrugSug> searchDrugList = drugService.searchDrugByNameForSale(request);
        System.out.println(searchDrugList);
    }

    @Autowired(required = false)
    DrugTradeService drugTradeService;

    @Test
    public void getDrugById(){
        HospitalDrug hospitalDrug = drugService.getDrugById(1L);
        System.out.println("诊所药库---------------");
        System.out.println(hospitalDrug);

        hospitalDrug = drugTradeService.getDrugByHMDrugId(1L);
        System.out.println("惠每药库---------------");
        System.out.println(hospitalDrug);
    }

    @Test
    public void checkDrugRepeatedByNameAndSpec(){
        HospitalDrug hDrug1 = new HospitalDrug();
        hDrug1.setId(1891L);
        hDrug1.setHospitalId(184L);
        hDrug1.setDrugName("复方鳖甲软肝片");
        hDrug1.setDrugType(2);
        hDrug1.setManufacturer("内蒙古福瑞医疗科技股份有限公司");
        hDrug1.setSpecification("0.5g/片*48/盒");
        hDrug1.setSpecPackageUnit("盒");
        Integer isRepate = drugService.checkDrugRepeatedByNameAndSpec(hDrug1);
        System.out.println("检查结果===="+isRepate);
    }

    @Test
    public void addDrugByHMDrug(){
        SellDrug sellDrug = new SellDrug();
        sellDrug.setDataSource(1);
        sellDrug.setDrugId(7L);
        sellDrug.setSalePrice(23.5);
        Long drugId = drugService.addDrugByHMDrug(sellDrug);
        System.out.println("增加结果===="+drugId);
    }

    @Test
    public void saveDrug(){
        HospitalDrug hDrug1 = new HospitalDrug();
        hDrug1.setHospitalId(1L);
        hDrug1.setDrugName("阿莫西林胶囊22");
        hDrug1.setDrugType(3);
        hDrug1.setIsOtc(1);
        hDrug1.setStatus(1);

        hDrug1.setApprovalNumber("国药准字J20150030");
        hDrug1.setBarCode("86900639000429");
        hDrug1.setMnemoniCode("8690");
        hDrug1.setSingleDosage("1");
        hDrug1.setFrequency("每天三次");
        hDrug1.setPrescribeAmount(2D);
        hDrug1.setInstruction("口服");
        hDrug1.setDoctorAdvice("");

        hDrug1.setSpecMinimumDosage(0.5);
        hDrug1.setSpecMinimumUnit("mg");
        hDrug1.setSpecUnit("粒");
        hDrug1.setSpecUnitaryRatio(24);
        hDrug1.setSpecPackageUnit("盒");

        hDrug1.setManufacturer("上海信谊黄河制药有限公司");
        hDrug1.setPurchasePrice(23.5);
        hDrug1.setPrescriptionPrice(25.5);
        hDrug1.setSalePrice(35.5);
        hDrug1.setInventoryCount(10D);
        hDrug1.setGoodsShelfCode("1号柜3排");
        hDrug1.setOpenStock(0);
        hDrug1.setDirectDrugPurchase(1);
        hDrug1.setSupplier("北京药局");
        hDrug1.setCreater(1L);
        HisResponse response = drugService.saveDrug(hDrug1);
        System.out.println("增加结果===="+response);
    }

    @Test
    public void modifyDrug(){
        HospitalDrug hDrug1 = drugService.getDrugById(9L);

        hDrug1.setDrugName("阿莫西林胶囊");
        hDrug1.setDrugType(3);
        hDrug1.setIsOtc(0);
        hDrug1.setStatus(0);

        hDrug1.setApprovalNumber("国药准字J20150030");
        hDrug1.setBarCode("86900639000429");
        hDrug1.setMnemoniCode("86888");
        hDrug1.setSingleDosage("2");
        hDrug1.setFrequency("每天三次");
        hDrug1.setPrescribeAmount(2D);
        hDrug1.setInstruction("口服");
        hDrug1.setDoctorAdvice("");

        hDrug1.setSpecMinimumDosage(0.5);
        hDrug1.setSpecMinimumUnit("mg");
        hDrug1.setSpecUnit("粒");
        hDrug1.setSpecUnitaryRatio(24);
        hDrug1.setSpecPackageUnit("盒");

        hDrug1.setManufacturer("上海信谊黄河制药有限公司");
        hDrug1.setPurchasePrice(26.5);
        hDrug1.setPrescriptionPrice(35.5);
        hDrug1.setSalePrice(40.5);
        hDrug1.setInventoryCount(15D);
        hDrug1.setGoodsShelfCode("2号柜3排");
        hDrug1.setOpenStock(1);
        hDrug1.setDirectDrugPurchase(2);
        hDrug1.setSupplier("北京药局555");
        hDrug1.setModifier(5L);
        HisResponse response = drugService.modifyDrug(hDrug1);
        System.out.println("修改结果===="+response);
    }


    @Test
    public void searchManufacturerByName(){
        List<DrugManufacturer> drugManufacturers = drugManufacturerService.searchManufacturerByName("hn");
        System.out.println(drugManufacturers);
    }
}