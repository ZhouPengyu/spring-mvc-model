package com.hm.his.module.template.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.his.framework.model.HisResponse;
import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.module.outpatient.model.request.PatientInquiryRequest;
import com.hm.his.module.template.model.Template;
import com.hm.his.module.template.model.TemplateTypeEnum;
import com.hm.his.module.template.pojo.TemplateRequest;
import com.hm.his.module.template.service.TemplateService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 *  诊所医生模板Controller
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/2/26
 * Time: 14:42
 * CopyRight:HuiMei Engine
 */
@RestController
@RequestMapping("/template")
public class TemplateController {

    @Autowired(required = false)
    TemplateService templateService;


    /**
     *  功能描述：  8.1.	获取模板列表
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    @RequestMapping(value = "/searchTemplateList", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String searchTemplateList(@RequestBody TemplateRequest templateRequest){
        templateRequest.setHospitalId(SessionUtils.getHospitalId());
        templateRequest.setDoctorId(SessionUtils.getDoctorId());
        Map<String,Object> body = Maps.newHashMap();
        List<Template> hospList = Lists.newArrayList();
        if(templateRequest.getTempType() ==null ){
            body.put("totalPage",0);
            body.put("templates",hospList);
            HisResponse hisResponse = HisResponse.getInstance(body);
            return hisResponse.toString();
        }
        if( templateRequest.getTempType() >3){
            hospList = templateService.searchTemplateList(templateRequest);
            templateRequest.setTotalCount(templateService.countTemplates(templateRequest));
        }else {
            hospList = templateService.searchTemplateListForRecipe(templateRequest);
            templateRequest.setTotalCount(templateService.countTemplatesForRecipe(templateRequest));
        }

        body.put("totalPage",templateRequest.getTotalPage());
        body.put("templates",hospList);
        HisResponse hisResponse = HisResponse.getInstance(body);
        return hisResponse.toString();
    }

    /**
     *  功能描述：8.2.	删除模板
     *  用户删除模板，逻辑删除
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    @RequestMapping(value = "/deleteTemplate", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String deleteTemplate(@RequestBody TemplateRequest templateRequest){
        templateRequest.setHospitalId(SessionUtils.getHospitalId());
        templateRequest.setDoctorId(SessionUtils.getDoctorId());
        Integer count = templateService.deleteTemplate(templateRequest.getTempIds());
        Map<String,Object> body = Maps.newHashMap();
        body.put("deleteCount",count);
        HisResponse hisResponse = HisResponse.getInstance(body);
        return hisResponse.toString();
    }

    /**
     *  功能描述： 8.3.	保存病历模板
     *
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    @RequestMapping(value = "/savePatientRecordTemplate", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String savePatientRecordTemplate(@RequestBody PatientInquiryRequest patientInquiryRequest){
        HisResponse hisResponse = new HisResponse();
        hisResponse = templateService.savePatientRecordTemplate(patientInquiryRequest);
        return hisResponse.toString();
    }

    /**
     *  功能描述：8.4.	获取病历模板详情
     *
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    @RequestMapping(value = "/getPatientRecordTemplate", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String getPatientRecordTemplate(@RequestBody Template template){
        PatientInquiryRequest patientInquiryRequest = templateService.getPatientRecordTemplate(template.getId());
        HisResponse hisResponse = HisResponse.getInstance(patientInquiryRequest);
        return hisResponse.toString();
    }

    /**
     *  功能描述： 8.5.	保存中药处方模板
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    @RequestMapping(value = "/saveHerbalTemplate", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String saveHerbalTemplate(@RequestBody PatientInquiryRequest patientInquiryRequest){
        HisResponse hisResponse = new HisResponse();
        hisResponse = templateService.saveHerbalTemplate(patientInquiryRequest);
        return hisResponse.toString();
    }

    /**
     *  功能描述：8.6.	获取中药处方模板详情
     *
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    @RequestMapping(value = "/getHerbalTemplate", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String getHerbalTemplate(@RequestBody Template template){

        PatientInquiryRequest patientInquiryRequest = templateService.getHerbalTemplate(template.getId());
        HisResponse hisResponse = HisResponse.getInstance(patientInquiryRequest);
        return hisResponse.toString();
    }


    /**
     *  功能描述：8.6.	获取中药处方模板详情
     *
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    @RequestMapping(value = "/getMoreHerbalTemplate", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String getMoreHerbalTemplate(@RequestBody TemplateRequest templateRequest){
        List<Long> tempIds = templateRequest.getTempIds();
        List<PatientInquiryRequest> requestList = Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(tempIds)){
            for(Long tempId : tempIds){
                PatientInquiryRequest patientInquiryRequest = templateService.getHerbalTemplate(tempId);
                requestList.add(patientInquiryRequest);
            }
        }
        Map<String,Object> body = Maps.newHashMap();
        body.put("templates",requestList);
        HisResponse hisResponse = HisResponse.getInstance(body);
        return hisResponse.toString();
    }

    /**
     *  功能描述：8.7.	保存西药处方模板
     *
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    @RequestMapping(value = "/savePatentPrescriptionTemplate", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String savePatentPrescriptionTemplate(@RequestBody PatientInquiryRequest patientInquiryRequest){
        HisResponse hisResponse = new HisResponse();
        hisResponse = templateService.savePatentPrescriptionTemplate(patientInquiryRequest);
        return hisResponse.toString();
    }

    /**
     *  功能描述：8.8.	获取西药处方模板详情
     *
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    @RequestMapping(value = "/getPatentPrescriptionTemplate", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String getPatentPrescriptionTemplate(@RequestBody Template template){

        PatientInquiryRequest patientInquiryRequest = templateService.getPatentPrescriptionTemplate(template.getId());
        HisResponse hisResponse = HisResponse.getInstance(patientInquiryRequest);
        return hisResponse.toString();
    }

    /**
     *  功能描述：8.8.	获取西药处方模板详情
     *
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    @RequestMapping(value = "/getMorePatentPrescriptionTemplate", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String getMorePatentPrescriptionTemplate(@RequestBody TemplateRequest templateRequest){
        List<Long> tempIds = templateRequest.getTempIds();
        List<PatientInquiryRequest> requestList = Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(tempIds)){
            for(Long tempId : tempIds){
                PatientInquiryRequest patientInquiryRequest = templateService.getPatentPrescriptionTemplate(tempId);
                requestList.add(patientInquiryRequest);
            }
        }
        Map<String,Object> body = Maps.newHashMap();
        body.put("templates",requestList);
        HisResponse hisResponse = HisResponse.getInstance(body);
        return hisResponse.toString();
    }

    /**
     *  功能描述： 8.9.	保存医嘱
     *
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    @RequestMapping(value = "/saveDoctorAdviceTemplate", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String saveDoctorAdviceTemplate(@RequestBody Template template){
        HisResponse hisResponse = new HisResponse();
        template.setHospitalId(SessionUtils.getHospitalId());
        template.setDoctorId(SessionUtils.getDoctorId());
        template.setTempType(TemplateTypeEnum.doctorAdvice.getTempType());
        if(template.getId() == null){
            hisResponse = templateService.saveDoctorAdviceTemplate(template);
        }else{
            hisResponse = templateService.modifyDoctorAdviceTemplate(template);
        }
        return hisResponse.toString();
    }

    /**
     *  功能描述：8.10.	获取医嘱
     *
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    @RequestMapping(value = "/getDoctorAdviceTemplate", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String getDoctorAdviceTemplate(@RequestBody Template template){
        Template template1 =templateService.getTemplateById(template.getId());
        HisResponse hisResponse = HisResponse.getInstance(template1);
        return hisResponse.toString();
    }


}
