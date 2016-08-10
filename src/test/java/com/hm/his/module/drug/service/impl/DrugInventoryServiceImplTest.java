package com.hm.his.module.drug.service.impl;

import com.hm.his.module.drug.pojo.InventoryOperatePojo;
import com.hm.his.module.drug.service.DrugInventoryService;
import com.hm.his.module.drug.service.DrugService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/3/15
 * Time: 9:51
 * CopyRight:HuiMei Engine
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-config.xml", "classpath:mybatis.xml" })

public class DrugInventoryServiceImplTest {

    @Autowired
    DrugInventoryService drugInventoryService;

    @Test
    public void testCutInventory() throws Exception {
        InventoryOperatePojo inventoryOperatePojo = new InventoryOperatePojo();
        inventoryOperatePojo.setHospitalId(1L);
        inventoryOperatePojo.setDrugId(1L);
        inventoryOperatePojo.setAmount(5D);
        inventoryOperatePojo.setUnit("袋");
        InventoryOperatePojo operatePojo = drugInventoryService.cutInventory(inventoryOperatePojo);
        System.out.println(operatePojo);
    }


    @Test
    public void testCutHospitalDrugInventory() throws Exception {
        InventoryOperatePojo inventoryOperatePojo = new InventoryOperatePojo();
        inventoryOperatePojo.setHospitalId(1L);
        inventoryOperatePojo.setDrugId(109L);
        inventoryOperatePojo.setAmount(15D);
        inventoryOperatePojo.setUnit("盒");
        inventoryOperatePojo.setOrderDrugId(1889L);
        drugInventoryService.cutHospitalDrugInventory(inventoryOperatePojo);
    }



    @Test
    public void testReturnInventory() throws Exception {
        InventoryOperatePojo inventoryOperatePojo = new InventoryOperatePojo();
        inventoryOperatePojo.setHospitalId(1L);
        inventoryOperatePojo.setDrugId(109L);
        inventoryOperatePojo.setAmount(20D);
        inventoryOperatePojo.setUnit("粒");
        drugInventoryService.returnInventory(inventoryOperatePojo);
    }

    @Test
    public void testHospitalDrugInventory() throws Exception {
        InventoryOperatePojo inventoryOperatePojo = new InventoryOperatePojo();
        inventoryOperatePojo.setHospitalId(1L);
        inventoryOperatePojo.setDrugId(109L);
        inventoryOperatePojo.setAmount(2D);
        inventoryOperatePojo.setUnit("盒");
        inventoryOperatePojo.setOrderDrugId(12L);
        drugInventoryService.returnHospitalDrugInventory(inventoryOperatePojo);
    }
}