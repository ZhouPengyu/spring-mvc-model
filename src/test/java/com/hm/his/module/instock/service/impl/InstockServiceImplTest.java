package com.hm.his.module.instock.service.impl;

import com.hm.his.module.instock.service.InstockService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016-06-17
 * Time: 11:34
 * CopyRight:HuiMei Engine
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-config.xml", "classpath:mybatis.xml" })
public class InstockServiceImplTest {

    @Autowired
    InstockService instockService;

    @Test
    public void testSubmitDrugInsock() throws Exception {

    }

    @Test
    public void testSystemSubmitDrugInsock() throws Exception {
        instockService.systemSubmitDrugInsock();
    }

    @Test
    public void testSystemCleanInsockDrugSurplusCount() throws Exception {
        instockService.systemCleanInsockDrugSurplusCount();
    }
}