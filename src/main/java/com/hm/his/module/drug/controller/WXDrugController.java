package com.hm.his.module.drug.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.his.framework.model.HisResponse;
import com.hm.his.framework.utils.Arith;
import com.hm.his.framework.utils.DateTools;
import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.module.drug.model.HospitalDrug;
import com.hm.his.module.drug.pojo.DrugRequest;
import com.hm.his.module.drug.pojo.DrugResponse;
import com.hm.his.module.drug.pojo.HospitalDrugSug;
import com.hm.his.module.drug.service.DrugManufacturerService;
import com.hm.his.module.drug.service.DrugService;
import com.hm.his.module.drug.service.DrugTradeService;
import com.hm.his.module.instock.model.BatchInstock;
import com.hm.his.module.instock.model.DrugBatchInstock;
import com.hm.his.module.manage.model.HospitalConfig;
import com.hm.his.module.manage.service.HospitalConfigService;
import com.hm.his.module.user.service.BoundInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 *  微信公众号诊所药品Controller
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/4/27
 * Time: 14:42
 * CopyRight:HuiMei Engine
 */
@RestController
@RequestMapping("/weixin/drug")
public class WXDrugController {

    @Autowired(required = false)
    DrugService drugService;
    @Autowired(required = false)
    DrugManufacturerService drugManufacturerService;
    @Autowired(required = false)
    BoundInfoService boundInfoService;
    @Autowired(required = false)
    DrugTradeService drugTradeService;
    @Autowired(required = false)
    HospitalConfigService hospitalConfigService;




    /**
     *  功能描述： 4.1.	根据条形码查询药品信息  --扫码进行药物录入或者修改库存
     * @author:  tangwenwu
     * @createDate   2016/4/26
     *
     */
    @RequestMapping(value = "/searchDrugByBarCode", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String searchDrugByBarCode(@RequestBody DrugRequest drugRequest){

        HisResponse hisResponse = new HisResponse();
        if(StringUtils.isBlank(drugRequest.getBarCode())){
            hisResponse.setErrorCode(50001L);
            hisResponse.setErrorMessage("药品条形码不能为空");
            return hisResponse.toString();
        }else{
            drugRequest.setBarCode(drugRequest.getBarCode().trim());
        }
        DrugResponse drugResponse = drugService.searchDrugByBarCodeForWX(drugRequest);

        HisResponse response = HisResponse.getInstance(drugResponse);
        return response.toString();
    }



    /**
     *  功能描述： 4.2.	新增药品
     *
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    @RequestMapping(value = "/saveDrug", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String saveDrug(@RequestBody HospitalDrug hospitalDrug){
        HisResponse hisResponse = new HisResponse();
        if (DrugCtlHelper.checkPostDrugData(hospitalDrug, hisResponse)) return hisResponse.toString();
        //weixin 设置 hospitalID
        boundInfoService.setDoctorToSession(hospitalDrug.getHospitalId());
        hospitalDrug.setCreater(SessionUtils.getDoctorId());
        hisResponse = drugService.saveDrugByWeixin(hospitalDrug);
        //处理完成后，将weixin 的 hospitalID 和doctorId 设置为空
        SessionUtils.removeDocIdAndHosId();
        return hisResponse.toString();
    }

    /**
     *  功能描述：5.9.	修改保存药品
     *
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    @RequestMapping(value = "/modifyInventoryAndPrice", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String modifyInventoryAndPrice(@RequestBody HospitalDrug hospitalDrug){
        HisResponse hisResponse = new HisResponse();
        Map<String,Object> body = Maps.newHashMap();
        if (DrugCtlHelper.checkPostDrugData4ModifyInventoryAndPrice(hospitalDrug, hisResponse)) return hisResponse.toString();
        //weixin 设置 hospitalID
        boundInfoService.setDoctorToSession(hospitalDrug.getHospitalId());
        hospitalDrug.setModifier(SessionUtils.getDoctorId());
        hisResponse = drugService.modifyInventoryAndPrice(hospitalDrug);
        //处理完成后，将weixin 的 hospitalID 和doctorId 设置为空
        SessionUtils.removeDocIdAndHosId();
        return hisResponse.toString();
    }


    /**
     *  功能描述： 4.5.	根据药品名称查询诊所药品库
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    @RequestMapping(value = "/searchDrugByName", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String searchDrugByName(@RequestBody DrugRequest drugRequest){
        HisResponse hisResponse = new HisResponse();
        drugRequest.setLimit(50);
        if(StringUtils.isBlank(drugRequest.getDrugName())){
            hisResponse.setErrorCode(50001L);
            hisResponse.setErrorMessage("药品名称为空");
            return hisResponse.toString();
        }

        List<HospitalDrugSug> hospList = drugService.searchDrugByName(drugRequest);
        Map<String,Object> body = Maps.newHashMap();
        body.put("version",drugRequest.getVersion());
        body.put("drugList",hospList);
        hisResponse = HisResponse.getInstance(body);
        return hisResponse.toString();
    }

    /**
     *  功能描述：4.6.	根据药品ID查询库存，价格信息
     *
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    @RequestMapping(value = "/drugDetail", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String drugDetail(@RequestBody DrugRequest drugRequest){
        HospitalDrug hospDrug = new HospitalDrug();
        if(null != drugRequest.getDataSource() && drugRequest.getDataSource().equals(1)){
            hospDrug = drugTradeService.getDrugByHMDrugId(drugRequest.getDrugId());
        }else{
            hospDrug = drugService.getDrugDetailById(drugRequest.getDrugId());
        }
        HisResponse hisResponse = HisResponse.getInstance(hospDrug);
        return hisResponse.toString();
    }


}
