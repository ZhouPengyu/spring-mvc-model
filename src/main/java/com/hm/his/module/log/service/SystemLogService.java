package com.hm.his.module.log.service;

import com.hm.his.module.drug.model.Dictionary;
import com.hm.his.module.log.model.SystemLog;

import java.util.List;


/**
 * 字典 服务
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/2/29
 * Time: 10:04
 * CopyRight:HuiMei Engine
 */
public interface SystemLogService {

    /**
     *  功能描述： 日志搜索
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    List<SystemLog> searchSystemLog(SystemLog systemLog);

    Integer saveSystemLog(SystemLog systemLog);

    Integer selpHeadSystemLog(SystemLog systemLog);

    Integer addSystemLogWithOutLogin(SystemLog systemLog);




}
