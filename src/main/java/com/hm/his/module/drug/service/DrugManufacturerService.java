package com.hm.his.module.drug.service;

import com.hm.his.module.drug.model.DrugManufacturer;
import com.hm.his.module.drug.model.HospitalDrug;
import com.hm.his.module.drug.pojo.DrugRequest;
import com.hm.his.module.drug.pojo.HospitalDrugSug;

import java.util.List;


/**
 * 生产厂家 服务
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/2/29
 * Time: 10:04
 * CopyRight:HuiMei Engine
 */
public interface DrugManufacturerService {

    /**
     *  功能描述： 5.5.生产厂家sug
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    List<DrugManufacturer> searchManufacturerByName(String manufacturer);


}
