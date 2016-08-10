package com.hm.his.module.manage.service.impl;

import com.google.common.collect.Lists;
import com.hm.his.framework.model.HisResponse;
import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.module.manage.dao.HospitalConfigMapper;
import com.hm.his.module.manage.model.ConfigAttribute;
import com.hm.his.module.manage.model.HospitalConfig;
import com.hm.his.module.manage.model.HospitalConfigEnum;
import com.hm.his.module.manage.service.HospitalConfigService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016-05-31
 * Time: 15:51
 * CopyRight:HuiMei Engine
 */
@Service
public class HospitalConfigServiceImpl implements HospitalConfigService {

    @Autowired(required = false)
    HospitalConfigMapper hospitalConfigMapper;

    @Override
    public HospitalConfig searchHospitalConfigList(HospitalConfig hospitalConfig) {
        List<HospitalConfig> hospitalConfigs = hospitalConfigMapper.searchHospitalConfigList(hospitalConfig);
        HospitalConfig config = new HospitalConfig();
        config.setHospitalId(hospitalConfig.getHospitalId());
        config.setConfigType(hospitalConfig.getConfigType());
        if(CollectionUtils.isNotEmpty(hospitalConfigs) ){
            if(hospitalConfigs.size() == 1){
                config.setAttrName(hospitalConfigs.get(0).getAttrName());
                config.setAttrValue(hospitalConfigs.get(0).getAttrValue());
            }

            List<ConfigAttribute> configAttributes = hospitalConfigs.stream().map(hospitalConfig1 -> {
                ConfigAttribute configAttr = new ConfigAttribute();
                configAttr.setAttrName(hospitalConfig1.getAttrName());
                configAttr.setAttrValue(hospitalConfig1.getAttrValue());
                return  configAttr;
            }).collect(Collectors.toList());
            config.setConfigAttrs(configAttributes);
        }
        return config;
    }

    @Override
    public Integer getHospitalIsUseBatchManage() {
        Integer isUseBatchManage = SessionUtils.getUseBatchManage();
        if (isUseBatchManage == null) {
            HospitalConfig hospConfig = new HospitalConfig();
            hospConfig.setHospitalId(SessionUtils.getHospitalId());
            hospConfig.setConfigType(1);
            HospitalConfig hospConf = searchHospitalConfigList(hospConfig);
            if (hospConf != null) {
                putUseBatchManageToSession(hospConf);
            } else {
                //如果用户没有的话，则默认设置为不使用批次管理
                putUseBatchManageToSession(hospConfig);
            }
            isUseBatchManage = SessionUtils.getUseBatchManage();
            return isUseBatchManage == null ? 0: isUseBatchManage;
        } else {
            return isUseBatchManage;
        }

//        Integer isUseBatchManage = 0;
//        HospitalConfig hospConfig = new HospitalConfig();
//        hospConfig.setHospitalId(SessionUtils.getHospitalId());
//        hospConfig.setConfigType(1);
//        HospitalConfig hospConf = searchHospitalConfigList(hospConfig);
//        if (hospConf != null) {
//            putUseBatchManageToSession(hospConf);
//        } else {
//            //如果用户没有的话，则默认设置为不使用批次管理
//            hospConf = new HospitalConfig();
//            hospConf.setHospitalId(SessionUtils.getHospitalId());
//            hospConf.setConfigType(1);
//            List<ConfigAttribute> configAttrs = Lists.newArrayList();
//            ConfigAttribute attr1 = new ConfigAttribute();
//            attr1.setAttrName(HospitalConfigEnum.useBatchManage.name());
//            attr1.setAttrValue("0");
//            configAttrs.add(attr1);
//            hospConf.setConfigAttrs(configAttrs);
//            // 保存用户的是否使用批次信息
//            saveHospitalConfig(hospConf);
//        }
//        putUseBatchManageToSession(hospConf);
//        isUseBatchManage = SessionUtils.getUseBatchManage();
//        return isUseBatchManage == null ? 0: isUseBatchManage;
    }




    @Override
    public HospitalConfig putUseBatchManageToSession(HospitalConfig hospConfig) {
        if(hospConfig.getConfigAttrs() !=null && hospConfig.getConfigAttrs().size() > 0 ){
            HttpSession session = SessionUtils.getSession();
            ConfigAttribute configAttr = hospConfig.getConfigAttrs().get(0);
            if(configAttr.getAttrName().equals(HospitalConfigEnum.useBatchManage.name())){
                session.setAttribute(SessionUtils.HOSPITAL_IS_USE_BATCH_MANAGE,hospConfig.getAttrValue());
            }
        }
        else{
            HttpSession session = SessionUtils.getSession();
            ConfigAttribute configAttr = new ConfigAttribute();
            configAttr.setAttrName(HospitalConfigEnum.useBatchManage.name());
            configAttr.setAttrValue("0");
            session.setAttribute(SessionUtils.HOSPITAL_IS_USE_BATCH_MANAGE,configAttr.getAttrValue());
            hospConfig.setAttrName(HospitalConfigEnum.useBatchManage.name());
            hospConfig.setAttrValue("0");
            List<ConfigAttribute> configAttributes = Lists.newArrayList();
            configAttributes.add(configAttr);
            hospConfig.setConfigAttrs(configAttributes);
        }
        return hospConfig;
    }

    @Override
    public boolean saveHospitalConfig(HospitalConfig hospitalConfig) {
        int delResult = hospitalConfigMapper.deleteHospitalConfigByHospitalIdAndConfigType(hospitalConfig);
        List<HospitalConfig> hospitalConfigs = hospitalConfig.getConfigAttrs().stream().map(configAttribute -> {
            HospitalConfig finalConfig = new HospitalConfig();
            finalConfig.setHospitalId(hospitalConfig.getHospitalId());
            finalConfig.setConfigType(hospitalConfig.getConfigType());
            finalConfig.setAttrName(configAttribute.getAttrName());
            finalConfig.setAttrValue(configAttribute.getAttrValue());
            return finalConfig;
        }).collect(Collectors.toList());
        int addResult = hospitalConfigMapper.batchInsertHospitalConfig(hospitalConfigs);
        return addResult>0? true:false;
    }
}
