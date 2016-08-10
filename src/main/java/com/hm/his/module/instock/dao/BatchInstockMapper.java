package com.hm.his.module.instock.dao;

import com.hm.his.module.drug.model.HospitalDrug;
import com.hm.his.module.drug.pojo.DrugRequest;
import com.hm.his.module.instock.model.BatchInstock;
import com.hm.his.module.instock.pojo.HospitalDrugInventoryPojo;

import java.util.List;

public interface BatchInstockMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BatchInstock record);

    int insertSelective(BatchInstock record);

    BatchInstock selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BatchInstock record);

    int updateByPrimaryKey(BatchInstock record);

    /**
     *  功能描述：查询当前日期 下最大的 batch_no
     * @author:  tangww
     * @createDate   2016-06-01
     *
     */
    BatchInstock searchCurrentDateBiggestBatchNo(Long hospitalId);


}