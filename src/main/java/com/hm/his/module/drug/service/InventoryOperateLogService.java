package com.hm.his.module.drug.service;

import com.hm.his.module.drug.model.DrugManufacturer;
import com.hm.his.module.drug.model.HospitalDrug;
import com.hm.his.module.drug.model.InventoryOperateLog;

import java.util.List;

/**
 * 药品库存操作日志 服务
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/3/7
 * Time: 10:04
 * CopyRight:HuiMei Engine
 */
public interface InventoryOperateLogService {

    /**
     *  功能描述：保存库存操作日志
     * @param
     * @return
     * @throws
     * @author:  tangww
     * @createDate   2016/3/7
     *
     */
    Integer saveInventoryOperateLog(InventoryOperateLog log);





}
