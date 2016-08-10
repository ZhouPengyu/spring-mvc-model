package com.hm.his.module.drug.service.impl;

import com.hm.his.module.drug.dao.DrugManufacturerMapper;
import com.hm.his.module.drug.dao.DrugTradeMapper;
import com.hm.his.module.drug.model.DrugManufacturer;
import com.hm.his.module.drug.service.DrugManufacturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/3/4
 * Time: 18:04
 * CopyRight:HuiMei Engine
 */
@Service
public class DrugManufacturerServiceImpl implements DrugManufacturerService {

    @Autowired(required = false)
    DrugManufacturerMapper drugManufacturerMapper;

    @Override
    public List<DrugManufacturer> searchManufacturerByName(String manufacturer) {
        DrugManufacturer record = new DrugManufacturer();
        record.setCnManufacturer(manufacturer);
        return drugManufacturerMapper.searchManufacturerByName(record);
    }
}
