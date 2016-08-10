package com.hm.his.module.drug.service.impl;

import com.hm.his.module.drug.dao.InventoryOperateLogMapper;
import com.hm.his.module.drug.model.HospitalDrug;
import com.hm.his.module.drug.model.InventoryOperateLog;
import com.hm.his.module.drug.model.OperateActionEnum;
import com.hm.his.module.drug.service.InventoryOperateLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/3/7
 * Time: 18:02
 * CopyRight:HuiMei Engine
 */
@Service
public class InventoryOperateLogServiceImpl implements InventoryOperateLogService {

    @Autowired
    InventoryOperateLogMapper inventoryOperateLogMapper;

    @Override
    public Integer saveInventoryOperateLog(InventoryOperateLog log) {
          return 1;//暂时无用，先注释
//        log.setOperateDate(new Date());
//        return inventoryOperateLogMapper.insert(log);

    }


}
