package com.hm.his.module.log.service.impl;

import com.hm.his.framework.utils.IPAndPathUtils;
import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.module.log.dao.SystemLogMapper;
import com.hm.his.module.log.model.SystemLog;
import com.hm.his.module.log.service.SystemLogService;
import com.hm.his.module.user.model.Doctor;
import org.apache.log4j.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/4/5
 * Time: 11:42
 * CopyRight:HuiMei Engine
 */
@Service("systemLogService")
public class SystemLogServiceImpl implements SystemLogService {

    //本地异常日志记录对象
//    static  Logger logger = Logger.getLogger(SystemLogServiceImpl.class);
    static Log log = LogFactory.getLog("AccessLog");
    @Autowired
    SystemLogMapper systemLogMapper;

    @Override
    public List<SystemLog> searchSystemLog(SystemLog systemLog) {
        return null;
    }

    @Override
    public Integer saveSystemLog(SystemLog systemLog) {
//        logger.error(systemLog);
        log.info(systemLog);
        return 1;
        //return systemLogMapper.insert(systemLog);
    }

    @Override
    public Integer selpHeadSystemLog(SystemLog systemLog) {
        Doctor doctor = SessionUtils.getCurrentUser();
        if(doctor!=null) {
            systemLog.setDoctorId(doctor.getDoctorId());
            systemLog.setUserName(doctor.getDoctorName());
            systemLog.setHospitalId(doctor.getHospitalId());
            systemLog.setHospitalName(doctor.getHospitalName());
        }
        HttpServletRequest request = SessionUtils.getRequest();
        //获取请求ip
        String loginIP = IPAndPathUtils.getRealIpAddres(request);
        systemLog.setLoginIp(loginIP);
        systemLog.setCreater(systemLog.getDoctorId());
        systemLog.setCreateDate(new Date());
//        logger.error(systemLog);
        log.warn(systemLog);
        return 1;
        //return systemLogMapper.insert(systemLog);
    }


    @Override
    public Integer addSystemLogWithOutLogin(SystemLog systemLog) {

        systemLog.setCreateDate(new Date());
//        logger.error(systemLog);
        log.error(systemLog);
        return 1;
        //return systemLogMapper.insert(systemLog);
    }
}
