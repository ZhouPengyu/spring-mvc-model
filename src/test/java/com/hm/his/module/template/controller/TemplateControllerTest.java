package com.hm.his.module.template.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.his.BaseControllerTest;
import com.hm.his.module.outpatient.model.*;
import com.hm.his.module.outpatient.model.request.PatientInquiryRequest;
import com.hm.his.module.template.model.Template;
import com.hm.his.module.template.model.TemplateTypeEnum;
import com.hm.his.module.template.pojo.TemplateRequest;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/3/24
 * Time: 19:16
 * CopyRight:HuiMei Engine
 */
public class TemplateControllerTest   extends BaseControllerTest {

    @Test
    public void testSearchTemplateList() throws Exception {
        TemplateRequest request = new TemplateRequest();
//        request.setDrugName("板蓝根");
//        request.setDrugType(3);
        //request.setTempName("病历模板");
        request.setDoctorId(1L);
        request.setTempType(3);
        request.setCurrentPage(1);
        request.setPageSize(15);
        super.testRole("/template/searchTemplateList", JSON.toJSONString(request));
    }

    @Test
    public void testDeleteTemplate() throws Exception {
        TemplateRequest request = new TemplateRequest();
        Long[] ids = new Long[] { 8L,9L };
        request.setTempIds(Arrays.asList(ids));
        super.testRole("/template/deleteTemplate", JSON.toJSONString(request));
    }

    @Test
    public void testSavePatientRecordTemplate() throws Exception {
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
        patientDrug.setDosage("3");
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
        patientDrug.setDosage("3");
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

        super.testRole("/template/savePatientRecordTemplate", JSON.toJSONString(request));
    }

    @Test
    public void testGetPatientRecordTemplate() throws Exception {
        Template template = new Template();
        template.setId(3L);
        super.testRole("/template/getPatientRecordTemplate", JSON.toJSONString(template));
    }

    @Test
    public void testSaveHerbalTemplate() throws Exception {
        //新增模板
        PatientInquiryRequest request = new PatientInquiryRequest();
//        request.setId(10L);
//        request.setLinkId(15L);
        request.setTempType(TemplateTypeEnum.herbal.getTempType());
        request.setTempName("中药模板");
        request.setTempDesc("中药模板555555555555");

        // 中药处方
        List<PatientChineseDrug> chinesePrescriptionList = Lists.newArrayList();
        PatientChineseDrug patientChineseDrug = new PatientChineseDrug();
//        patientChineseDrug.setPrescription(2L);
        patientChineseDrug.setTotalDosage(5L);
        patientChineseDrug.setDailyDosage(2L);
        patientChineseDrug.setFrequency("一天三次");
        patientChineseDrug.setUsage("水煎服");
        patientChineseDrug.setRequirement("你想咋吃？");
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


        //修改模板
        /*PatientInquiryRequest request = new PatientInquiryRequest();
        request.setId(7L);
        request.setLinkId(427L);
        request.setTempType(TemplateTypeEnum.herbal.getTempType());
        request.setTempName("提神醒脑妙方");
        request.setTempDesc("用于治疗像总理这种富贵病");

        // 中药处方
        List<PatientChineseDrug> chinesePrescriptionList = Lists.newArrayList();
        PatientChineseDrug patientChineseDrug = new PatientChineseDrug();
        patientChineseDrug.setPrescription(427L);
        patientChineseDrug.setTotalDosage(9L);
        patientChineseDrug.setDailyDosage(5L);
        patientChineseDrug.setFrequency("一天两次");
        patientChineseDrug.setUsage("水煎服");
        patientChineseDrug.setRequirement("你想咋吃？");
        List<PatientChineseDrugPieces> decoctionPiecesList = Lists.newArrayList();
        PatientChineseDrugPieces patientChineseDrugPieces = new PatientChineseDrugPieces();
        patientChineseDrugPieces.setDrugId(18L);
        patientChineseDrugPieces.setDrugName("天麻子");
        patientChineseDrugPieces.setUnit("g");
        patientChineseDrugPieces.setValue(10D);
        patientChineseDrugPieces.setDataSource(0L);
        patientChineseDrugPieces.setComment("先煎");
        decoctionPiecesList.add(patientChineseDrugPieces);

        PatientChineseDrugPieces patientChineseDrugPieces3 = new PatientChineseDrugPieces();
        patientChineseDrugPieces3.setDrugId(10L);
        patientChineseDrugPieces3.setDrugName("党生哥");
        patientChineseDrugPieces3.setUnit("g");
        patientChineseDrugPieces3.setValue(8D);
        patientChineseDrugPieces3.setDataSource(1L);
        patientChineseDrugPieces3.setComment("水煎");
        decoctionPiecesList.add(patientChineseDrugPieces3);
        patientChineseDrug.setDecoctionPiecesList(decoctionPiecesList);
        chinesePrescriptionList.add(patientChineseDrug);
        request.setChinesePrescriptionList(chinesePrescriptionList);*/

        super.testRole("/template/saveHerbalTemplate", JSON.toJSONString(request));
    }

    @Test
    public void testGetHerbalTemplate() throws Exception {
        Template template = new Template();
        template.setId(7L);
        super.testRole("/template/getHerbalTemplate", JSON.toJSONString(template));
    }

    @Test
    public void testGetMoreHerbalTemplate() throws Exception {
        TemplateRequest template = new TemplateRequest();
        Long[] ids = new Long[] {6L,7L };
        template.setTempIds(Arrays.asList(ids));
        super.testRole("/template/getMoreHerbalTemplate", JSON.toJSONString(template));
    }

    @Test
    public void testSavePatentPrescriptionTemplate() throws Exception {
        //新建西药模板
        PatientInquiryRequest request = new PatientInquiryRequest();
//        request.setId(23L);
        request.setTempType(TemplateTypeEnum.westernMedicine.getTempType());
        request.setTempName("感冒用药55");
        request.setTempDesc("西药模板666");
//        request.setLinkId(20L);
        //西药处方
//        List<Map<String, Object>> patentPrescriptionList = Lists.newArrayList();
//        request.setPatentPrescriptionList(patentPrescriptionList);
//
//        // 西药处方
//        List<PatientDrug> patientDrugs = Lists.newArrayList();
//        PatientDrug patientDrug = new PatientDrug();
//        patientDrug.setDrugId(1L);
//        patientDrug.setDrugName("板蓝根颗粒");
//        patientDrug.setAssId(1L);
//        patientDrug.setUsage("口服");
//        patientDrug.setFrequency("一天三次");
//        patientDrug.setDosage(3D);
//        patientDrug.setDosageUnit("g");
//        patientDrug.setTotalDosage(10D);
//        patientDrug.setTotalDosageUnit("片");
////        patientDrug.setPrescription(1L);
//        patientDrug.setDataSource(0L);
//        patientDrugs.add(patientDrug);
//        PatientDrug patientDrug1 = new PatientDrug();
//        patientDrug1.setDrugId(3L);
//        patientDrug1.setDrugName("盐酸伪麻黄碱缓释片");
//        patientDrug1.setAssId(1L);
//        patientDrug1.setUsage("口服");
//        patientDrug1.setFrequency("一天三次");
//        patientDrug1.setDosage(3D);
//        patientDrug1.setDosageUnit("g");
//        patientDrug1.setTotalDosage(10D);
//        patientDrug1.setTotalDosageUnit("片");
////        patientDrug.setPrescription(1L);
//        patientDrug1.setDataSource(0L);
//         patientDrugs.add(patientDrug1);
//        Map<String, Object> map = Maps.newHashMap();
//        map.put("patentDrugList", patientDrugs);
//        List<Map<String, Object>> patientDrugMapList = Lists.newArrayList();
//        patientDrugMapList.add(map);
//        request.setPatentPrescriptionList(patientDrugMapList);

        //修改西药模板
//        PatientInquiryRequest request = new PatientInquiryRequest();
//        request.setId(9L);
//        request.setTempType(TemplateTypeEnum.westernMedicine.getTempType());
//        request.setTempName("西药模板");
//        request.setTempDesc("西药模板666");
//        request.setLinkId(466L);
//        //西药处方
//        List<Map<String, Object>> patentPrescriptionList = Lists.newArrayList();
//        request.setPatentPrescriptionList(patentPrescriptionList);
//
//        // 西药处方
//        List<PatientDrug> patientDrugs = Lists.newArrayList();
//        PatientDrug patientDrug = new PatientDrug();
//        patientDrug.setDrugId(1L);
//        patientDrug.setDrugName("板蓝根颗粒33");
//        patientDrug.setAssId(1L);
//        patientDrug.setUsage("口服速下");
//        patientDrug.setFrequency("一天两次");
//        patientDrug.setDosage(6D);
//        patientDrug.setDosageUnit("g");
//        patientDrug.setTotalDosage(5D);
//        patientDrug.setTotalDosageUnit("粒");
////        patientDrug.setPrescription(466L);
//        patientDrug.setDataSource(0L);
//        patientDrugs.add(patientDrug);
//        PatientDrug patientDrug1 = new PatientDrug();
//        patientDrug1.setDrugId(3L);
//        patientDrug1.setDrugName("888盐酸伪麻黄碱缓释片");
//        patientDrug1.setAssId(1L);
//        patientDrug1.setUsage("口服");
//        patientDrug1.setFrequency("一天一次");
//        patientDrug1.setDosage(10D);
//        patientDrug1.setDosageUnit("g");
//        patientDrug1.setTotalDosage(18D);
//        patientDrug1.setTotalDosageUnit("片");
////        patientDrug.setPrescription(466L);
//        patientDrug1.setDataSource(0L);
//        patientDrugs.add(patientDrug1);
//        Map<String, Object> map = Maps.newHashMap();
//        map.put("patentDrugList", patientDrugs);
//        List<Map<String, Object>> patientDrugMapList = Lists.newArrayList();
//        patientDrugMapList.add(map);
//        request.setPatentPrescriptionList(patientDrugMapList);

        super.testRole("/template/savePatentPrescriptionTemplate", JSON.toJSONString(request));
    }

    @Test
    public void testGetPatentPrescriptionTemplate() throws Exception {
        Template template = new Template();
        template.setId(9L);
        super.testRole("/template/getPatentPrescriptionTemplate", JSON.toJSONString(template));
    }

    @Test
    public void testGetMorePatentPrescriptionTemplate() throws Exception {
        TemplateRequest template = new TemplateRequest();
        Long[] ids = new Long[] { 1L,9L };
        template.setTempIds(Arrays.asList(ids));
        super.testRole("/template/getMorePatentPrescriptionTemplate", JSON.toJSONString(template));
    }

    @Test
    public void testSaveDoctorAdviceTemplate() throws Exception {
        Template template = new Template();
//        template.setId(23L);
        template.setTempType(TemplateTypeEnum.westernMedicine.getTempType());
        template.setTempName("医嘱模板223");
        template.setTempDesc("wqwq暖男三句半：多喝热水，多休息，早点睡");
        super.testRole("/template/saveDoctorAdviceTemplate", JSON.toJSONString(template));

    }

    @Test
    public void testGetDoctorAdviceTemplate() throws Exception {
        Template template = new Template();
        template.setId(3L);
        super.testRole("/template/getDoctorAdviceTemplate", JSON.toJSONString(template));
    }
}