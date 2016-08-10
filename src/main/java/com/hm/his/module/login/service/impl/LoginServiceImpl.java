package com.hm.his.module.login.service.impl;

import com.google.common.collect.Maps;
import com.hm.his.framework.cache.redis.CacheCaller;
import com.hm.his.framework.cache.redis.JedisHelper;
import com.hm.his.framework.utils.BeanUtils;
import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.module.Global;
import com.hm.his.module.login.facade.LoginCache;
import com.hm.his.module.login.model.*;
import com.hm.his.module.login.service.LoginService;
import com.hm.his.module.manage.model.HospitalConfig;
import com.hm.his.module.manage.service.HospitalConfigService;
import com.hm.his.module.user.model.Doctor;
import com.hm.his.module.user.model.Hospital;
import com.hm.his.module.user.service.DoctorService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016-08-10
 * Time: 10:56
 * CopyRight:HuiMei Engine
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired(required = false)
    HospitalConfigService hospitalConfigService;
    @Autowired(required = false)
    DoctorService doctorService;

    @Override
    public SystemFrame getHisSystemFrame(Doctor doctor,Hospital hospital) {

        SystemFrame tempSysFrame = null;
        try {
            tempSysFrame = (SystemFrame) AccessControl.systemFrame.deepClone();

            String headerCont  = tempSysFrame.getHeader_cont().toString();

            boolean isAnHeShouUser  = StringUtils.isNotBlank(hospital.getInvitationCode()) && StringUtils.equals(hospital.getInvitationCode(), Global.ANHESHOU_CLINIC_USER);
            if(isAnHeShouUser){	// 如果是安和寿的诊所用户，则将头部信息替换（替换头部的logo 和 诊所名称）
                tempSysFrame.setSystem_flag("AnHeShouClinic");//安和寿诊所系统
                headerCont = headerCont.replaceAll("clinicSystemName","安和寿诊所系统").replaceAll("header_logo","../../images/header_logo_anheshou.png").replaceAll("two-dimension-code","../../images/ahs-ewm-box.png");
            }else{
                tempSysFrame.setSystem_flag("HMClinic");//惠每云诊所系统
                headerCont = headerCont.replaceAll("clinicSystemName","惠每云诊所系统").replaceAll("header_logo","../../images/header_logo.png").replaceAll("two-dimension-code","../../images/hm-ewm-box.png");
            }
            tempSysFrame.setHeader_cont(headerCont);

            //根据权限定制 权限的json
            Map<String,List<String>> accessFunMap = this.buildUserPermissions(doctor,isAnHeShouUser);
            //创建一级菜单副本
            SystemFrame.MenuConf newMenuConfs  = new SystemFrame.MenuConf();
            //获得模板中的一级菜单
            SystemFrame.MenuConf tempMenuConfs  = tempSysFrame.getMenu_conf();
            final Map<String, List<String>> finalAccessFunMap = accessFunMap;
            accessFunMap.keySet().forEach(funKey ->{
                try {
                    SystemFrame.FirstMenu tempFirstMenu = (SystemFrame.FirstMenu) BeanUtils.forceGetProperty(tempMenuConfs,funKey);
                    LinkedHashMap<String,Object> tempSecMenus = tempFirstMenu.getSon_menu();
                    LinkedHashMap<String,Object> newSecondMenus = Maps.newLinkedHashMap();
                    if(funKey.equals(FunctionEnum.userCenter.toString()) || funKey.equals(FunctionEnum.userHelp.toString())){
                        tempFirstMenu.setSon_menu(tempSecMenus);
                    }else{
                        List<String> secFuns = finalAccessFunMap.get(funKey);
                        tempSecMenus.keySet().forEach(secondKey ->{
                            if(secFuns.contains(secondKey.toString())){
                                newSecondMenus.put(secondKey,tempSecMenus.get(secondKey));
                            }
                        });
                        tempFirstMenu.setSon_menu(newSecondMenus);
                    }

                    BeanUtils.forceSetProperty(newMenuConfs,funKey,tempFirstMenu);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            });
            //安合寿的用户，没有系统帮助。
            if(isAnHeShouUser){
                newMenuConfs.setUserHelp(null);
            }
            tempSysFrame.setMenu_conf(newMenuConfs);
            return tempSysFrame;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return null;

    }

    @Override
    public SystemFrame getSupplierSystemFrame(Doctor doctor) {
        //1.2 读取系统功能的模板框架
        SystemFrame supplierFrame = null;
        try {
            supplierFrame = (SystemFrame) AccessControl.supplierFrame.deepClone();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String supHeaderCont  = supplierFrame.getHeader_cont().toString();
        supHeaderCont = supHeaderCont.replaceAll("clinicSystemName","安和寿诊所系统").replaceAll("header_logo","../../images/header_logo_anheshou.png");
        supplierFrame.setHeader_cont(supHeaderCont);
        supplierFrame.setSystem_flag("AnHeShouClinic");//安和寿诊所系统
        return supplierFrame;

    }

    /**
     *  功能描述：配置 普通用户的 权限
     * @param
     * @return
     * @throws
     * @author:  tangww
     * @createDate   2016/4/14
     *
     */
    private Map<String, List<String>> buildUserPermissions(Doctor doctor, boolean isAnHeShouUser) throws Exception {
        Map<String, List<String>> functionMap = new HashMap<String, List<String>>();
        SecondFunction secondFunction = new SecondFunction();
        // 1 判断用户是否为管理员
        if(doctor.getIsAdmin()!=null && doctor.getIsAdmin()==1L){
            functionMap = secondFunction.getAllSecondFunMap();
        }else{
            // 2 用户不是管理员时，查询用户的权限
            Map<String, List<String>> allSecondFunMap = secondFunction.getAllSecondFunMap();
            List<Function> functions = doctorService.getDoctorFunction(doctor.getDoctorId());
            if(functions!=null && functions.size()>0){
                final Map<String, List<String>> finalFunctionMap = functionMap;
                functions.stream().forEach(function -> {
                    finalFunctionMap.put(function.getFunctionUrl(),allSecondFunMap.get(function.getFunctionUrl()));
                });
            }
            functionMap.put(FunctionEnum.userHelp.toString(),allSecondFunMap.get(FunctionEnum.userHelp.toString()));
            functionMap.put(FunctionEnum.userCenter.toString(),allSecondFunMap.get(FunctionEnum.userCenter.toString()));
            functionMap.put(FunctionEnum.systemMessage.toString(),allSecondFunMap.get(FunctionEnum.systemMessage.toString()));
        }
        //查询用户是否使用 批次管理
        HospitalConfig hospitalConfig = new HospitalConfig();
        hospitalConfig.setHospitalId(doctor.getHospitalId());
        hospitalConfig.setConfigType(1);
        HospitalConfig hospConfig = hospitalConfigService.searchHospitalConfigList(hospitalConfig);
        if(! (hospConfig != null&& hospConfig.getAttrValue()!=null && hospConfig.getAttrValue().equals("1")))
        {
            List<String>  drugManages = functionMap.get(FunctionEnum.drugManage.toString());
            if(CollectionUtils.isNotEmpty(drugManages)){
                drugManages.remove("drugHousing");
                drugManages.remove("stockManage");
                drugManages.remove("storageRecord");
            }
        }
        //查询用户是否使用 订单管理
        if(isAnHeShouUser){
            //如果使用 订单管理 但没有开启批次入库 ，那么将 药品入库功能添加到菜单中
            List<String>  drugManages = functionMap.get(FunctionEnum.drugManage.toString());
            if(CollectionUtils.isNotEmpty(drugManages) && ! drugManages.contains("drugHousing")){
                drugManages.add("drugHousing");
            }
        }else{
            // 不使用 订单管理，则删除 订单管理功能
            List<String>  drugManages = functionMap.get(FunctionEnum.drugManage.toString());
            if(CollectionUtils.isNotEmpty(drugManages)){
                drugManages.remove("orderList");
            }
        }

        return functionMap;
    }


}
