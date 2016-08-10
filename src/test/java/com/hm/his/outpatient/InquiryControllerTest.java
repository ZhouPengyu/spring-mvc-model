package com.hm.his.outpatient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.hm.his.BaseControllerTest;
import com.hm.his.module.outpatient.model.PatientAdditional;
import com.hm.his.module.outpatient.model.PatientChineseDrug;
import com.hm.his.module.outpatient.model.PatientChineseDrugPieces;
import com.hm.his.module.outpatient.model.PatientDiagnosis;
import com.hm.his.module.outpatient.model.PatientDrug;
import com.hm.his.module.outpatient.model.PatientExam;
import com.hm.his.module.outpatient.model.request.PatientInquiryRequest;

public class InquiryControllerTest  extends BaseControllerTest {

    @Test
    public void savePatinetRecord() throws Exception {
    	PatientInquiryRequest inquiryRequest = new PatientInquiryRequest();
    	inquiryRequest.setRecordId(13000l);
    	inquiryRequest.setPatientId(2843l);
    	inquiryRequest.setPatientName("王五");
    	inquiryRequest.setGender(1l);
    	inquiryRequest.setAge(12D);
    	inquiryRequest.setAgeType("岁");
    	inquiryRequest.setDbp(23);
    	inquiryRequest.setSbp(235);
    	inquiryRequest.setHeartRate(33);
    	
    	List<PatientExam> patientExamList = new ArrayList<PatientExam>();
    	PatientExam patientExam = new PatientExam();
    	patientExam.setExamName("血常规");
    	patientExam.setExamId(1000L);
    	patientExam.setPrice(200D);
    	patientExam.setIsCharged(0);
    	
    	PatientExam patientExam1 = new PatientExam();
    	patientExam1.setExamName("大便常规");
    	patientExam1.setPrice(20D);
    	patientExam1.setExamId(1003L);
    	patientExam1.setIsCharged(0);
    	patientExamList.add(patientExam1);
    	patientExamList.add(patientExam);
    	inquiryRequest.setExamList(patientExamList);
    	
    	List<PatientDiagnosis> patientDiagnosisList = new ArrayList<PatientDiagnosis>();
    	PatientDiagnosis patientDiagnosis = new PatientDiagnosis();
    	patientDiagnosis.setDiseaseName("腰疼");
    	patientDiagnosisList.add(patientDiagnosis);
    	inquiryRequest.setDiagnosis(patientDiagnosisList);
    	
    	List<PatientAdditional> patientAdditionals = new ArrayList<PatientAdditional>();
    	PatientAdditional additional = new PatientAdditional();
    	additional.setAdditionalName("嘿嘿嘿");
    	additional.setAdditionalPrice(500D);
    	patientAdditionals.add(additional);
    	inquiryRequest.setAdditionalList(patientAdditionals);
    	
    	List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
    	Map<String, Object> map = new HashMap<String, Object>();
    	List<PatientDrug> drugs = new ArrayList<PatientDrug>();
    	PatientDrug drug = new PatientDrug();
    	drug.setDrugName("大力丸");
    	drug.setDrugId(1l);
    	drug.setUnivalence(10D);
    	drug.setTotalDosage(5D);
    	drug.setTotalDosageUnit("盒");
    	drug.setDosage("1");
    	drug.setDosageUnit("片");
    	drug.setDataSource(1l);
    	drugs.add(drug);
    	map.put("patentDrugList", drugs);
    	map.put("isCharged", 0);
    	listMap.add(map);
    	inquiryRequest.setPatentPrescriptionList(listMap);
    	
    	List<PatientChineseDrug> chinesePrescriptionList = new ArrayList<PatientChineseDrug>();
    	PatientChineseDrug chineseDrug = new PatientChineseDrug();
    	chineseDrug.setDailyDosage(2l);
    	chineseDrug.setTotalDosage(2l);
    	List<PatientChineseDrugPieces> chineseDrugPieces = new ArrayList<PatientChineseDrugPieces>();
    	PatientChineseDrugPieces drugPieces = new PatientChineseDrugPieces();
    	drugPieces.setDataSource(1l);
    	drugPieces.setValue(10D);
    	drugPieces.setDrugId(2l);
    	drugPieces.setDrugName("六味地黄丸");
    	drugPieces.setPrice(20D);
    	drugPieces.setUnivalence(2D);
    	chineseDrugPieces.add(drugPieces);
    	chineseDrug.setDecoctionPiecesList(chineseDrugPieces);
    	chinesePrescriptionList.add(chineseDrug);
    	inquiryRequest.setChinesePrescriptionList(chinesePrescriptionList);

    	drugPieces.setPrice(20D);
        super.testRole("outpatient/savePatinetRecord", JSON.toJSONString(inquiryRequest));
    }
    
    @Test
    public void getRecordInfo() throws Exception {
    	Map<String, Integer> requestParams = new HashMap<String, Integer>();
    	requestParams.put("recordId", 13000);
    	requestParams.put("patientId", 2843);
        super.testRole("outpatient/getRecordInfo", JSON.toJSONString(requestParams));
    }

}
