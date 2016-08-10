package com.hm.his.module.drug.dao;

import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.hm.his.module.drug.model.DrugManufacturer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/3/22
 * Time: 14:49
 * CopyRight:HuiMei Engine
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-config.xml", "classpath:mybatis.xml" })
public class DrugManufacturerMapperTest {


    @Autowired(required = false)
    DrugManufacturerMapper dao;


    @Test
    public void testSearchManufacturerByName() throws Exception {
        List<DrugManufacturer> drugManufacturers = dao.selectAll();
        System.out.println("========================="+drugManufacturers.size());
        drugManufacturers.stream().map(manufacturer->{
            manufacturer.getCnManufacturer();

            manufacturer.setPinyin(PinyinHelper.convertToPinyinString(manufacturer.getCnManufacturer(), "", PinyinFormat.WITHOUT_TONE));
            manufacturer.setPinyinFirst(PinyinHelper.getShortPinyin(manufacturer.getCnManufacturer()));
            dao.updateByPrimaryKey(manufacturer);
            return manufacturer;
        }).collect(Collectors.toList());
        System.out.println(drugManufacturers);
    }
}