package com.hm.his.module.instock.controller;

import com.google.common.collect.Maps;
import com.hm.his.framework.exception.ExcelException;
import com.hm.his.framework.model.HisResponse;
import com.hm.his.framework.utils.DateTools;
import com.hm.his.framework.utils.DateUtil;
import com.hm.his.framework.utils.ExcelUtil;
import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.module.drug.model.HospitalDrug;
import com.hm.his.module.drug.pojo.DrugRequest;
import com.hm.his.module.drug.service.DrugService;
import com.hm.his.module.instock.model.BatchInstock;
import com.hm.his.module.instock.model.DrugBatchInstock;
import com.hm.his.module.instock.pojo.HospitalDrugInventoryPojo;
import com.hm.his.module.instock.pojo.InstockRequest;
import com.hm.his.module.instock.service.InstockService;
import com.hm.his.module.manage.service.HospitalConfigService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *  批次入库Controller
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/6/1
 * Time: 14:42
 * CopyRight:HuiMei Engine
 */
@RestController
@RequestMapping("/instock")
public class InstockController {

    @Autowired(required = false)
    DrugService drugService;
    @Autowired(required = false)
    InstockService instockService;
    @Autowired(required = false)
    HospitalConfigService hospitalConfigService;



    /**
     *  功能描述：5.2.	提交药品入库
     *
     * @author:  tangwenwu
     * @createDate   2016/6/1
     *
     */
    @RequestMapping(value = "/submitDrugInsock", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String submitDrugInsock(@RequestBody BatchInstock batchInstock){
        batchInstock.setHospitalId(SessionUtils.getHospitalId());
        batchInstock.setCreater(SessionUtils.getDoctorId());
        HisResponse hisResponse = instockService.submitDrugInsock(batchInstock);

        return hisResponse.toString();
    }

    /**
     *  功能描述：  5.3.	查询库存列表（总库存）
     * @author:  tangwenwu
     * @createDate   2016/6/1
     *
     */
    @RequestMapping(value = "/searchDrugInventoryList", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String searchDrugInventoryList(@RequestBody DrugRequest drugRequest,HttpServletRequest request){
        HttpSession session = request.getSession();
        drugRequest.setHospitalId(SessionUtils.getHospitalId());
        Map<String,Object> body = Maps.newHashMap();
        List<HospitalDrug> hospList = drugService.searchDrugList(drugRequest);
        drugRequest.setTotalCount(drugService.countHospitalDrug(drugRequest));
        body.put("totalPage",drugRequest.getTotalPage());
        body.put("drugList",hospList);
        HisResponse hisResponse = HisResponse.getInstance(body);
        return hisResponse.toString();
    }

    /**
     *  功能描述：5.11. 使用批次管理 -- 导出 总库存 记录
     * @author:  tangwenwu
     * @createDate   2016/6/1
     *
     */
    @RequestMapping(value = "/exportDrugInventoryList", produces = "application/json;charset=UTF-8", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String exportDrugInventoryList(HttpServletRequest request, HttpServletResponse response){
        HisResponse hisResponse = new HisResponse();
        DrugRequest drugRequest = new DrugRequest();
        drugRequest.setPageSize(null);
        drugRequest.setHospitalId(SessionUtils.getHospitalId());

        drugRequest.setDrugName(request.getParameter("drugName"));
        String drugTypeStr = request.getParameter("drugType");
        if(StringUtils.isNotBlank(drugTypeStr)){
            drugRequest.setDrugType(Integer.parseInt(drugTypeStr));
        }
        String statusStr = request.getParameter("status");
        if(StringUtils.isNotBlank(statusStr)){
            drugRequest.setStatus(Integer.parseInt(statusStr));
        }
        List<HospitalDrug> hospList = drugService.searchDrugList(drugRequest);
        try {
            if(CollectionUtils.isNotEmpty(hospList)){
                LinkedHashMap<String, String> fieldMap = InstockCtlHelper.buildMapExportDrugInventoryListHead();
                //将list集合转化为excle
                ExcelUtil.listToExcel(hospList, fieldMap, "药品信息", response);
            }else{
                Map<String,Object> body = Maps.newHashMap();
                body.put("status","0");
                body.put("message","没有药品信息，更换查询条件试试");
                hisResponse.setBody(body);
            }
        } catch (ExcelException e) {
            e.printStackTrace();
        }


        return hisResponse.toString();
    }

    /**
     *  功能描述：  5.4.	查询库存列表（分批次库存）
     *  对于 采用批次入库 的用户，用户进入可以进入分批次库存管理，按批次查看药品的库存
     * @author:  tangwenwu
     * @createDate   2016/6/1
     *
     */
    @RequestMapping(value = "/searchDrugInventoryListByBatch", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String searchDrugInventoryListByBatch(@RequestBody DrugRequest drugRequest,HttpServletRequest request){

        drugRequest.setHospitalId(SessionUtils.getHospitalId());
        Map<String,Object> body = Maps.newHashMap();

        List<HospitalDrugInventoryPojo> hospitalDrugInventoryPojos = instockService.searchDrugInventoryListByBatch(drugRequest);
        drugRequest.setTotalCount(instockService.countDrugInventoryListByBatch(drugRequest));
        body.put("totalPage",drugRequest.getTotalPage());
        body.put("drugList",hospitalDrugInventoryPojos);

        HisResponse hisResponse = HisResponse.getInstance(body);
        return hisResponse.toString();
    }





    /**
     *  功能描述：5.5.	查询批次药品的库存详细信息
     *
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    @RequestMapping(value = "/searchBatchDrugInventoryDetail", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String searchBatchDrugInventoryDetail(@RequestBody InstockRequest instockRequest){
        instockRequest.setHospitalId(SessionUtils.getHospitalId());
        HisResponse hisResponse = new HisResponse();
        if(instockRequest.getDrugId() ==null || instockRequest.getDrugBatchInstockId() == null){
            hisResponse.setErrorCode(80001L);
            hisResponse.setErrorMessage("药品ID或药品批次ID不能为空");
            return hisResponse.toString();
        }
        HospitalDrugInventoryPojo hospitalDrugInventoryPojo = instockService.searchBatchDrugInventoryDetail(instockRequest);
        hisResponse = HisResponse.getInstance(hospitalDrugInventoryPojo);
        return hisResponse.toString();
    }

    /**
     *  功能描述：5.6.	修改药品库存
     *
     * @author:  tangwenwu
     * @createDate   2016/6/1
     *
     */
    @RequestMapping(value = "/modifyBatchDrugInventory", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String modifyBatchDrugInventory(@RequestBody DrugBatchInstock drugBatchInstock){
        HisResponse hisResponse = new HisResponse();
        drugBatchInstock.setHospitalId(SessionUtils.getHospitalId());
//        if (InstockCtlHelper.checkPostDrugData(hospitalDrug, hisResponse)) return hisResponse.toString();

        hisResponse  = instockService.modifyBatchDrugInventory(drugBatchInstock);
        return hisResponse.toString();
    }

    /**
     *  5.7.	查询库存预警 药品列表
     *
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    @RequestMapping(value = "/searchDrugListForInventoryWarning", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String searchDrugListForInventoryWarning(@RequestBody InstockRequest instockRequest){
        instockRequest.setHospitalId(SessionUtils.getHospitalId());
        List<HospitalDrug> hospList = instockService.searchDrugListForInventoryWarning(instockRequest);
        instockRequest.setTotalCount(instockService.countDrugListForInventoryWarning(instockRequest));
        Map<String,Object> body = Maps.newHashMap();
        body.put("totalPage",instockRequest.getTotalPage());
        body.put("drugList",hospList);
        HisResponse hisResponse = HisResponse.getInstance(body);
        return hisResponse.toString();
    }



    /**
     *  5.9.	查询效期预警 药品列表
     *
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    @RequestMapping(value = "/searchDrugListForValidityWarning", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String searchDrugListForValidityWarning(@RequestBody InstockRequest instockRequest){
        instockRequest.setHospitalId(SessionUtils.getHospitalId());
        Integer isUseBatchManage = hospitalConfigService.getHospitalIsUseBatchManage();

        if(isUseBatchManage !=null){
            instockRequest.setIsUseBatchManage(isUseBatchManage);
        }else{
            instockRequest.setIsUseBatchManage(0);
        }
        if(instockRequest.getTimeQuantum()!=null){
            instockRequest.setCompareValidityDate(DateUtil.addDay(instockRequest.getTimeQuantum()));
        }
        List<HospitalDrugInventoryPojo> hospList = instockService.searchDrugListForValidityWarning(instockRequest);
        instockRequest.setTotalCount(instockService.countDrugListForValidityWarning(instockRequest));
        Map<String,Object> body = Maps.newHashMap();
        body.put("totalPage",instockRequest.getTotalPage());
        body.put("drugList",hospList);
        HisResponse hisResponse = HisResponse.getInstance(body);
        return hisResponse.toString();
    }

    /**
     *  5.9.	入库日志查询
     *
     * @author:  tangwenwu
     * @createDate   2016/6/1
     *
     */
    @RequestMapping(value = "/searchBatchInstockLog", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String searchBatchInstockLog(@RequestBody InstockRequest instockRequest){
        instockRequest.setHospitalId(SessionUtils.getHospitalId());
        instockRequest.setIsDescSort(1);
        if(StringUtils.isEmpty(instockRequest.getStartDate())){
            instockRequest.setStartDate(DateTools.getDate(DateTools.ONLY_DATE_FORMAT,DateUtil.subtractDay(30)));
        }
        if(StringUtils.isEmpty(instockRequest.getEndDate())){
            instockRequest.setEndDate(DateTools.getDate(DateTools.ONLY_DATE_FORMAT,new Date())+DateTools.THE_DAY_MAX_TIME);
        }else{
            instockRequest.setEndDate(instockRequest.getEndDate()+DateTools.THE_DAY_MAX_TIME);
        }
        List<HospitalDrugInventoryPojo> hospList = instockService.searchBatchInstockLog(instockRequest);
        instockRequest.setTotalCount(instockService.countBatchInstockLog(instockRequest));
        Map<String,Object> body = Maps.newHashMap();
        body.put("totalPage",instockRequest.getTotalPage());
        body.put("drugList",hospList);
        HisResponse hisResponse = HisResponse.getInstance(body);
        return hisResponse.toString();
    }




    /**
     *  功能描述：5.11.	导出入库日志
     * @author:  tangwenwu
     * @createDate   2016/6/1
     *
     */
    @RequestMapping(value = "/exportBatchInstockLog", produces = "application/json;charset=UTF-8", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String exportBatchInstockLog(HttpServletRequest request, HttpServletResponse response){
        InstockRequest instockRequest =new InstockRequest();
        instockRequest.setPageSize(null);
        instockRequest.setHospitalId(SessionUtils.getHospitalId());
        instockRequest.setStartDate(request.getParameter("startDate"));
        instockRequest.setEndDate(request.getParameter("endDate"));
        instockRequest.setDrugName(request.getParameter("drugName"));
        String drugTypeStr = request.getParameter("drugType");
        HisResponse hisResponse = new HisResponse();
        if(StringUtils.isNotBlank(drugTypeStr)){
            instockRequest.setDrugType(Integer.parseInt(drugTypeStr));
        }
        if(StringUtils.isEmpty(instockRequest.getStartDate())){
            instockRequest.setStartDate(DateTools.getDate(DateTools.ONLY_DATE_FORMAT,DateUtil.subtractDay(30)));
        }
        if(StringUtils.isEmpty(instockRequest.getEndDate())){
            instockRequest.setEndDate(DateTools.getDate(DateTools.ONLY_DATE_FORMAT,new Date())+DateTools.THE_DAY_MAX_TIME);
        }else{
            instockRequest.setEndDate(instockRequest.getEndDate()+DateTools.THE_DAY_MAX_TIME);
        }
        List<HospitalDrugInventoryPojo> hospList = instockService.searchBatchInstockLog(instockRequest);
        try {
            if(CollectionUtils.isNotEmpty(hospList)){
             LinkedHashMap<String, String> fieldMap = InstockCtlHelper.buildMapExportBatchInstockLogHead();
            //将list集合转化为excle
            ExcelUtil.listToExcel(hospList, fieldMap, "入库日志", response);
            }else{
                Map<String,Object> body = Maps.newHashMap();
                body.put("status","0");
                body.put("message","没有入库日志，更换查询条件试试");
                hisResponse.setBody(body);
            }
        } catch (ExcelException e) {
            e.printStackTrace();
        }


        return hisResponse.toString();
    }

    /**
     *  功能描述：5.10.	删除药品批次入库
     *  用户删除药品，逻辑删除
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    @RequestMapping(value = "/deleteBatchInstock", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String deleteBatchInstock(@RequestBody DrugRequest drugRequest){

        HisResponse hisResponse = HisResponse.getInstance();
        return hisResponse.toString();
    }
}
