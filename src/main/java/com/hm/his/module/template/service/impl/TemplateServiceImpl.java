package com.hm.his.module.template.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.his.framework.log.annotation.SystemLogAnno;
import com.hm.his.framework.log.constant.FunctionConst;
import com.hm.his.framework.log.constant.Operation;
import com.hm.his.framework.model.HisResponse;
import com.hm.his.framework.utils.BeanUtils;
import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.module.outpatient.model.*;
import com.hm.his.module.outpatient.model.request.PatientInquiryRequest;
import com.hm.his.module.outpatient.model.response.PatientChineseDrugPiecesResponse;
import com.hm.his.module.outpatient.model.response.PatientChineseDrugResponse;
import com.hm.his.module.outpatient.service.PatientDrugService;
import com.hm.his.module.outpatient.service.PatientInquiryService;
import com.hm.his.module.template.dao.TemplateMapper;
import com.hm.his.module.template.model.Template;
import com.hm.his.module.template.model.TemplateTypeEnum;
import com.hm.his.module.template.pojo.TemplateRequest;
import com.hm.his.module.template.service.TemplateOperService;
import com.hm.his.module.template.service.TemplateService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/3/24
 * Time: 18:20
 * CopyRight:HuiMei Engine
 */
@Service
public class TemplateServiceImpl implements TemplateService {

    Logger logger = Logger.getLogger(TemplateServiceImpl.class);

    @Autowired(required = false)
    TemplateMapper templateMapper;

    @Autowired(required = false)
    PatientInquiryService patientInquiryService;

    @Autowired(required = false)
    PatientDrugService patientDrugService;

    @Autowired(required = false)
    TemplateOperService templateOperService;




    @Override
    public Template getTemplateById(Long id) {
        if(id ==null)
            return null;
        return templateMapper.selectByPrimaryKey(id.intValue());
    }

    @Override
    public List<Template> searchTemplateList(TemplateRequest tempRequest) {
        if(tempRequest.getDoctorId() == null || tempRequest.getTempType()== null){
            return Lists.newArrayList();
        }
        List<Template> templates = templateMapper.selectTemplateList(tempRequest);

        if(CollectionUtils.isEmpty(templates)) return Lists.newArrayList();
        return templates.stream().map(template -> {
            template.setTempTypeName(TemplateTypeEnum.getTempTypeNameByType(template.getTempType()));
            return  template;

        }).collect(Collectors.toList());
    }

    @Override
    public List<Template> searchTemplateListForRecipe(TemplateRequest tempRequest) {
        if(tempRequest.getDoctorId() == null || tempRequest.getTempType()== null){
            return Lists.newArrayList();
        }
        List<Template> templates = templateMapper.selectTemplateListForRecipe(tempRequest);


        if(CollectionUtils.isEmpty(templates)) return Lists.newArrayList();
        return templates.stream().map(template -> {

            template.setTempTypeName(TemplateTypeEnum.getTempTypeNameByType(template.getTempType()));
            if(template.getTempType() <3){
                if(template.getTempType().equals(TemplateTypeEnum.herbal.getTempType())){
                    if(StringUtils.isNotBlank(template.getCnDrugNames())){
                        template.setDrugNames(template.getCnDrugNames());
                    }
                }
            }
            return  template;

        }).collect(Collectors.toList());
    }

    @Override
    public Integer countTemplates(TemplateRequest tempRequest) {
        if(tempRequest.getDoctorId() == null || tempRequest.getTempType()== null){
            return 0;
        }
        return templateMapper.countTemplates(tempRequest);
    }

    @Override
    public Integer countTemplatesForRecipe(TemplateRequest tempRequest) {
        if(tempRequest.getDoctorId() == null || tempRequest.getTempType()== null){
            return 0;
        }
        return templateMapper.countTemplatesForRecipe(tempRequest);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    @SystemLogAnno(functionId = FunctionConst.TEMPLATE_LOG,operationId = Operation.TEMPLATE_ADD)
    public HisResponse saveDoctorAdviceTemplate(Template template) {

        HisResponse hisResponse = new HisResponse();
        if(template.getTempName()==null ||template.getDoctorId()==null || template.getTempType()==null){
            hisResponse.setErrorCode(50001L);
            hisResponse.setErrorMessage("医嘱模板名称为空，请填写模板名称");
            return hisResponse;
        }

        if (templateOperService.checkTemplateByName(template) > 0) {
            hisResponse.setErrorCode(50001L);
            hisResponse.setErrorMessage("医嘱模板名称已存在，请修改模板名称");
            return hisResponse;
        }
        template.setStatus(1);
        template.setCreateDate(new Date());
        template.setCreater(SessionUtils.getDoctorId());
        int result = templateMapper.insert(template);
        if(result >0){
            return hisResponse;
        }else{
            hisResponse.setErrorCode(60001L);
            hisResponse.setErrorMessage("保存医嘱模板失败，请重试.");
            return hisResponse;
        }
    }

    @Override
    @SystemLogAnno(functionId = FunctionConst.TEMPLATE_LOG,operationId = Operation.TEMPLATE_MODIFY)
    public HisResponse modifyDoctorAdviceTemplate(Template template) {
        HisResponse hisResponse = new HisResponse();
        if(template.getTempName()==null ||template.getDoctorId()==null || template.getTempType()==null){
            hisResponse.setErrorCode(50001L);
            hisResponse.setErrorMessage("医嘱模板名称为空，请填写模板名称");
            return hisResponse;
        }

        if (templateOperService.checkTemplateByName(template) > 1) {
            hisResponse.setErrorCode(50001L);
            hisResponse.setErrorMessage("医嘱模板名称已存在，请修改模板名称");
            return hisResponse;
        }
        template.setModifyDate(new Date());
        template.setModifier(SessionUtils.getDoctorId());
        int result = templateMapper.updateByPrimaryKeySelective(template);
        if(result >0){
            return hisResponse;
        }else{
            hisResponse.setErrorCode(60001L);
            hisResponse.setErrorMessage("修改医嘱模板失败，请重试.");
            return hisResponse;
        }
    }


    @Override
    public HisResponse savePatientRecordTemplate(PatientInquiryRequest patientInquiryRequest) {
        //patientInquiryService.saveInquiryTemplate(patientInquiryRequest);

        return HisResponse.getInstance();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public HisResponse saveHerbalTemplate(PatientInquiryRequest patientInquiryRequest) {
        HisResponse hisResponse = new HisResponse();
        //ID不为空，为修改模板  --需要考虑 只有模板，没有药的情况
        Long prescription = patientInquiryRequest.getLinkId();
        if(patientInquiryRequest.getId()!=null){
            Template template = new Template();
            template.setId(patientInquiryRequest.getId());
            template.setTempType(patientInquiryRequest.getTempType());
            template.setTempName(patientInquiryRequest.getTempName());
            template.setTempDesc(patientInquiryRequest.getTempDesc());
            hisResponse = templateOperService.checkTemplateDate(template,1);
            if(hisResponse.getHasError()) return hisResponse;

            if(CollectionUtils.isNotEmpty(patientInquiryRequest.getChinesePrescriptionList())){
                PatientChineseDrug patientChineseDrug = patientInquiryRequest.getChinesePrescriptionList().get(0);
                // 可能出现 linkId 为空的情况， 如果linkId 为空，则需要保存处方
                patientChineseDrug.setPrescription(patientInquiryRequest.getLinkId());
                prescription = patientDrugService.saveChineseDrugPrescriptionTem(patientChineseDrug);
            }
            template.setLinkId(prescription);
            return templateOperService.modifyTemplate(template);
        }else{
            Template template = new Template();
            template.setTempType(TemplateTypeEnum.herbal.getTempType());
            template.setTempName(patientInquiryRequest.getTempName());
            template.setTempDesc(patientInquiryRequest.getTempDesc());
            hisResponse = templateOperService.checkTemplateDate(template,0);
            if(hisResponse.getHasError()) return hisResponse;
            if(CollectionUtils.isNotEmpty(patientInquiryRequest.getChinesePrescriptionList())){
                PatientChineseDrug patientChineseDrug = patientInquiryRequest.getChinesePrescriptionList().get(0);
                prescription = patientDrugService.saveChineseDrugPrescriptionTem(patientChineseDrug);
                if(prescription!=null && prescription >0){
                    template.setLinkId(prescription);
                    return templateOperService.saveTemplate(template);
                }else{
                    hisResponse.setErrorCode(50007L);
                    hisResponse.setErrorMessage("保存中药模板失败，请重试");
                    return hisResponse;
                }
            }else{
                //当中药处方 为空时，直接保存模板信息即可

                return templateOperService.saveTemplate(template);
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public HisResponse savePatentPrescriptionTemplate(PatientInquiryRequest patientInquiryRequest) {
        HisResponse hisResponse = new HisResponse();
        //ID不为空，为修改模板  --需要考虑 只有模板，没有药的情况
        Long prescription = patientInquiryRequest.getLinkId();
        if(patientInquiryRequest.getId()!=null){
            Template template = new Template();
            template.setId(patientInquiryRequest.getId());
            template.setTempType(patientInquiryRequest.getTempType());
            template.setTempName(patientInquiryRequest.getTempName());
            template.setTempDesc(patientInquiryRequest.getTempDesc());
            hisResponse = templateOperService.checkTemplateDate(template,1);
            if(hisResponse.getHasError()) return hisResponse;
            if(CollectionUtils.isNotEmpty(patientInquiryRequest.getPatentPrescriptionList())){
                Map<String, Object> patentPrescription = patientInquiryRequest.getPatentPrescriptionList().get(0);
                patentPrescription.put("prescription",prescription);
                prescription = patientDrugService.savePatientDrugPrescriptionTem(patentPrescription);
            }

            template.setLinkId(prescription);
            return templateOperService.modifyTemplate(template);

        }else{
            Template template = new Template();
            template.setTempType(TemplateTypeEnum.westernMedicine.getTempType());
            template.setTempName(patientInquiryRequest.getTempName());
            template.setTempDesc(patientInquiryRequest.getTempDesc());
            hisResponse = templateOperService.checkTemplateDate(template,0);
            if(hisResponse.getHasError()) return hisResponse;
            if(CollectionUtils.isNotEmpty(patientInquiryRequest.getPatentPrescriptionList())){
                Map<String, Object> patentPrescription = patientInquiryRequest.getPatentPrescriptionList().get(0);
                prescription = patientDrugService.savePatientDrugPrescriptionTem(patentPrescription);
                if(prescription!=null && prescription >0){
                    template.setLinkId(prescription);
                    return templateOperService.saveTemplate(template);
                }else{
                    hisResponse.setErrorCode(50007L);
                    hisResponse.setErrorMessage("保存西药模板失败，请重试");
                    return hisResponse;
                }
            }else{
                //当中药处方 为空时，直接保存模板信息即可
                return templateOperService.saveTemplate(template);
            }
        }
    }

    @Override
    public PatientInquiryRequest getPatientRecordTemplate(Long tempId) {


        PatientInquiryRequest request = new PatientInquiryRequest();
        request.setId(10L);
        request.setTempName("测试病历模板");
        request.setTempType(TemplateTypeEnum.medicalRecord.getTempType());
        request.setLinkId(10L);
        request.setTemperature(38.5);
        request.setSbp(90);
        request.setDbp(120);
        request.setHeartRate(86);
        request.setHeight(170.2);
        request.setWeight(60.2);
        request.setOtherPhysique("没啥其他的");
        request.setSymptom("反复咳嗽咳痰30余年，再发伴气促半月");
        request.setPreviousHistory("无");
        request.setPersonalHistory("肺结核");
        request.setAllergyHistory("花粉过敏");
        request.setFamilyHistory("痨病");
        List<PatientExam> examList = Lists.newArrayList();
        PatientExam exam = new PatientExam();
        exam.setExamId(12L);
        exam.setExamName("血常规");
        exam.setPrice(45.5);
        examList.add(exam);
        request.setExamList(examList);
        List<PatientDiagnosis> diagnosis = Lists.newArrayList();
        PatientDiagnosis diagnosis1 = new PatientDiagnosis();
        diagnosis1.setDiseaseId(1L);
        diagnosis1.setDiseaseName("上呼吸道感染");
        diagnosis.add(diagnosis1);
        PatientDiagnosis diagnosis2 = new PatientDiagnosis();
        diagnosis2.setDiseaseId(3L);
        diagnosis2.setDiseaseName("头晕");
        diagnosis.add(diagnosis2);
        request.setDiagnosis(diagnosis);

        //西药处方
        List<Map<String, Object>> patentPrescriptionList = Lists.newArrayList();
        request.setPatentPrescriptionList(patentPrescriptionList);

        // 西药处方
        List<PatientDrug> patientDrugs = Lists.newArrayList();
        PatientDrug patientDrug = new PatientDrug();
        patientDrug.setDrugId(1L);
        patientDrug.setDrugName("板蓝根颗粒");
        patientDrug.setAssId(1L);
        patientDrug.setUsage("口服");
        patientDrug.setFrequency("一天三次");
        patientDrug.setDosage("3D");
        patientDrug.setDosageUnit("g");
        patientDrug.setTotalDosage(10D);
        patientDrug.setTotalDosageUnit("片");
        patientDrug.setPrescription(1L);
        patientDrug.setDataSource(0L);
        patientDrug.setRecordId(1L);
        patientDrugs.add(patientDrug);
        PatientDrug patientDrug1 = new PatientDrug();
        patientDrug1.setDrugId(3L);
        patientDrug1.setDrugName("盐酸伪麻黄碱缓释片");
        patientDrug.setAssId(1L);
        patientDrug.setUsage("口服");
        patientDrug.setFrequency("一天三次");
        patientDrug.setDosage("3D");
        patientDrug.setDosageUnit("g");
        patientDrug.setTotalDosage(10D);
        patientDrug.setTotalDosageUnit("片");
        patientDrug.setPrescription(1L);
        patientDrug.setDataSource(0L);
        patientDrug.setRecordId(1L);
        // patientDrugs.add(patientDrug1);
        Map<String, Object> map = Maps.newHashMap();
        map.put("patentDrugList", patientDrugs);
        List<Map<String, Object>> patientDrugMapList = Lists.newArrayList();
        patientDrugMapList.add(map);
        request.setPatentPrescriptionList(patientDrugMapList);

        // 中药处方
        List<PatientChineseDrug> chinesePrescriptionList = Lists.newArrayList();
        PatientChineseDrug patientChineseDrug = new PatientChineseDrug();
        patientChineseDrug.setPrescription(2L);
        patientChineseDrug.setTotalDosage(5L);
        patientChineseDrug.setRecordId(1L);
        List<PatientChineseDrugPieces> decoctionPiecesList = Lists.newArrayList();
        PatientChineseDrugPieces patientChineseDrugPieces = new PatientChineseDrugPieces();
        patientChineseDrugPieces.setDrugId(17L);
        patientChineseDrugPieces.setDrugName("天麻");
        patientChineseDrugPieces.setUnit("g");
        patientChineseDrugPieces.setValue(5D);
        patientChineseDrugPieces.setDataSource(0L);
        patientChineseDrugPieces.setComment("先煎");
        decoctionPiecesList.add(patientChineseDrugPieces);

        PatientChineseDrugPieces patientChineseDrugPieces3 = new PatientChineseDrugPieces();
        patientChineseDrugPieces3.setDrugId(10L);
        patientChineseDrugPieces3.setDrugName("党生");
        patientChineseDrugPieces3.setUnit("g");
        patientChineseDrugPieces3.setValue(10D);
        patientChineseDrugPieces3.setDataSource(1L);
        patientChineseDrugPieces3.setComment("水煎");
        decoctionPiecesList.add(patientChineseDrugPieces3);
        patientChineseDrug.setDecoctionPiecesList(decoctionPiecesList);
        chinesePrescriptionList.add(patientChineseDrug);
        request.setChinesePrescriptionList(chinesePrescriptionList);

        // 附加费用
        List<PatientAdditional> additionalList = Lists.newArrayList();
        PatientAdditional patientAdditional = new PatientAdditional();
        patientAdditional.setAdditionalId(1L);
        patientAdditional.setAdditionalName("注射费");
        patientAdditional.setAdditionalPrice(100.00d);
        additionalList.add(patientAdditional);
        request.setAdditionalList(additionalList);
        request.setDoctorAdvice("多喝热水，少吃饭");

        return request;
    }

    @Override
    public PatientInquiryRequest getHerbalTemplate(Long tempId) {
        PatientInquiryRequest request = new PatientInquiryRequest();
        if(null != tempId){
            Template template = templateMapper.selectByPrimaryKey(tempId.intValue());
            if(template!=null){
                PatientChineseDrugResponse patientChineseDrugResponse = null;
                Long prescription = template.getLinkId();//处方ID
                if(null != prescription){
                    patientChineseDrugResponse = patientDrugService.getChineseDrugPrescriptionTem(prescription);
                }
                BeanUtils.copyProperties(template,request,false);
                request.setPatientChineseDrug(patientChineseDrugResponse);
                return request;
            }
        }
        return request;
    }

    @Override
    public PatientInquiryRequest getPatentPrescriptionTemplate(Long tempId) {
        PatientInquiryRequest request = new PatientInquiryRequest();
        if(null != tempId){
            Template template = templateMapper.selectByPrimaryKey(tempId.intValue());
            if(template!=null){
                Map<String, Object> patentPrescriptionDrug = Maps.newHashMap();
                Long prescription = template.getLinkId();//处方ID
                List<Map<String, Object>> patentPrescriptionList =Lists.newArrayList();
                if(null != prescription){
                    patentPrescriptionDrug = patientDrugService.getPatientDrugPrescriptionTem(prescription);
                    patentPrescriptionList.add(patentPrescriptionDrug);
                }
                BeanUtils.copyProperties(template,request,false);
                request.setPatentPrescriptionList(patentPrescriptionList);
                return request;
            }
        }

        return request;


    }

    @Override
    @SystemLogAnno(functionId = FunctionConst.TEMPLATE_LOG,operationId = Operation.TEMPLATE_DELETE)
    public Integer deleteTemplate(List<Long> tempIds) {
        if(CollectionUtils.isNotEmpty(tempIds)){
            for(Long tempId :tempIds){
                templateMapper.deletByTempId(tempId.intValue());
            }
            return tempIds.size();
        }
        return 0;
    }
}
