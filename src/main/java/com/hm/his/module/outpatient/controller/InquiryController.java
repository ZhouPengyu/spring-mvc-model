package com.hm.his.module.outpatient.controller;

import com.alibaba.fastjson.JSON;
import com.hm.his.framework.model.HisResponse;
import com.hm.his.framework.utils.ChineseToEnglish;
import com.hm.his.framework.utils.DateUtil;
import com.hm.his.framework.utils.LangUtils;
import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.module.drug.pojo.SaleWayPojo;
import com.hm.his.module.drug.service.DrugService;
import com.hm.his.module.login.model.Function;
import com.hm.his.module.order.model.OrderItemListType;
import com.hm.his.module.order.service.GetIsChargedService;
import com.hm.his.module.order.service.OrderService;
import com.hm.his.module.order.service.UpdateOrderService;
import com.hm.his.module.outpatient.model.*;
import com.hm.his.module.outpatient.model.request.PatientInquiryRequest;
import com.hm.his.module.outpatient.model.response.PatientChineseDrugPiecesResponse;
import com.hm.his.module.outpatient.model.response.PatientChineseDrugResponse;
import com.hm.his.module.outpatient.service.*;
import com.hm.his.module.user.model.Doctor;
import com.hm.his.module.user.service.DoctorService;
import com.hm.his.module.user.service.HospitalService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-2 11:35:38
 * @description 病历管理
 * @version 3.0
 */
@RestController
@RequestMapping("/outpatient")
public class InquiryController {
	
	@Autowired(required = false)
	PatientService patientService;
	@Autowired(required = false)
	PatientInquiryService patientInquiryService;
	@Autowired(required = false)
	DoctorService doctorService;
	@Autowired(required = false)
	PatientDrugService patientDrugService;
	@Autowired(required = false)
	PatientExamService patientExamService;
	@Autowired(required = false)
	HospitalService hospitalService;
	@Autowired(required = false)
    DiagnosisService diagnosisService;
	@Autowired(required = false)
	PatientDiagnosisService patientDiagnosisService;
	@Autowired(required = false)
	PatientAdditionalService patientAdditionalService;
	@Autowired(required = false)
	GetIsChargedService getIsChargedService;
	@Autowired(required = false)
	UpdateOrderService updateOrderService;
	@Autowired(required = false)
	OrderService orderService;
	@Autowired(required = false)
	DrugService drugService;
	@Autowired(required = false)
	InquiryService inquiryService;
	
	Logger logger = Logger.getLogger(InquiryController.class);
    
	/**
     * <p>Description:载入新病例 患者ID不为空载入上次病历<p>
     * @author ZhouPengyu
     * @date 2016-3-7 11:30:57
     */
	@RequestMapping(value = "/newRecord", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String getPatientHistoryInfo(@RequestBody Map<String, String> requestParams){
        HisResponse hisResponse = new HisResponse();
        Long patientId = null;
        Long doctorId = LangUtils.getLong(SessionUtils.getDoctorId());
        if (StringUtils.isNotEmpty(requestParams.get("patientId"))) {
            patientId = Long.valueOf(requestParams.get("patientId"));
        }
        Patient patient = new Patient();
        patient.setDoctorId(doctorId);
        Map<String, Object> body = new HashMap<String, Object>();
        if (patientId == null) {
        	patient.setPatientId(LangUtils.getLong(patientService.getPatientSeq()));
        }else{
        	Map<String, Object> mapPatient = new HashMap<String, Object>();
     		mapPatient.put("patientId", patientId);
     		patient = patientService.getPatientById(patientId);
        	PatientInquiry patientInquiryRec= patientInquiryService.getInquiryNewByPatientId(patientId);
			body.put("patientName", patient.getPatientName());
			body.put("gender", patient.getGender());
			body.put("phoneNo", patient.getPhoneNo());
			if(StringUtils.isEmpty(patient.getAgeType())){
	        	//得到当前的年份
	            String cYear = DateUtil.formatDate(new Date()).substring(0,4);
	            //得到生日年份
	            String birthYear = patient.getBirthday().substring(0,4);
	            int age = Integer.parseInt(cYear) - Integer.parseInt(birthYear);
	            patient.setAge(Double.valueOf(age));
	            patient.setAgeType("岁");
	        }
			body.put("age", patient.getAge());
			body.put("ageType", patient.getAgeType());
			if(patientInquiryRec!=null){
				body.put("previousHistory", patientInquiryRec.getPreviousHistory());
				body.put("personalHistory", patientInquiryRec.getPersonalHistory());
				body.put("allergyHistory", patientInquiryRec.getAllergyHistory());
				body.put("familyHistory", patientInquiryRec.getFamilyHistory());
			}
        }
        patientId = patient.getPatientId();
        Long recordId = LangUtils.getLong(patientInquiryService.getRecordSeq());
        body.put("recordId", recordId);
        body.put("patientId", patientId);
        
        hisResponse.setBody(body);
        return hisResponse.toString();
    }
	
    /**
     * <p>Description:保存病历详细<p>
     * @author ZhouPengyu
     * @date 2016年3月12日 下午4:29:48
     */
	@RequestMapping(value = "/savePatinetRecord", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String savePatinetRecord(@RequestBody PatientInquiryRequest patientInquiryRequest){
    	HisResponse hisResponse = new HisResponse();
    	try{
        	Long doctorId = SessionUtils.getDoctorId();
        	Long recordId = patientInquiryRequest.getRecordId();
        	Long patientId = patientInquiryRequest.getPatientId();

			if(checkPatientInquiryData(patientInquiryRequest,hisResponse)){
				return hisResponse.toString();
			}

        	Patient patient = new Patient();
        	patient.setPatientId(patientId);
        	patient.setDoctorId(doctorId);
        	patient.setPatientName(patientInquiryRequest.getPatientName());
        	patient.setGender(patientInquiryRequest.getGender());
        	patient.setPhoneNo(patientInquiryRequest.getPhoneNo());
        	patient.setAge(patientInquiryRequest.getAge());
        	patient.setAgeType(patientInquiryRequest.getAgeType());
        	patient.setDiagnosisDate(DateUtil.getGeneralDate(new Date()));

    		Patient exist = patientService.getPatientById(patientId);
    		if(exist==null){
    			patient.setPatientPinyin(ChineseToEnglish.getPingYin(patient.getPatientName()));
    			patientService.addPatient(patient);
    		}else{
    			exist.setAge(patient.getAge());
    			exist.setAgeType(patient.getAgeType());
    			exist.setPhoneNo(patient.getPhoneNo());
    			exist.setDiagnosisDate(patient.getDiagnosisDate());
    			patientService.savePatient(exist);
    		}

    		//判断关联关系是否存
    		if(doctorService.isDoctorPatientRelation(doctorId, patientId)<1){
    			doctorService.insertDoctorPatientRelation(doctorId, patientId);
    		}

    		patientInquiryRequest.setModle(1);	//病历标识
    		patientInquiryRequest.setVer(1);
    		patientInquiryRequest.setDoctorId(doctorId);
    		if(patientInquiryService.getInquiryByRecordId(recordId)==null){
    			patientInquiryService.addInquiry(patientInquiryRequest);
            	orderService.createHospitalOrder(patientInquiryRequest);
            }else{
            	patientInquiryService.saveInquiry(patientInquiryRequest);
            }

    		patientDiagnosisService.deleteDiagnosisByRecordId(recordId);
            List<PatientDiagnosis> diagnosis = patientInquiryRequest.getDiagnosis();
            if(diagnosis!=null && diagnosis.size()>0)
            	patientDiagnosisService.insertDiagnosis(recordId, diagnosis);		//添加诊断


            List<PatientExam> patientExamList = patientInquiryRequest.getExamList();
            if(patientExamList==null || patientExamList.size()<1){
            	patientExamService.delExamByRecordId(recordId);
            	updateOrderService.deleteAllRecordItems(recordId, OrderItemListType.EXAM);
            }else{
            	for (PatientExam patientExam : patientExamList) {
            		patientExam.setRecordId(recordId);
            		patientExam.setPatientId(patientId);
            		patientExam.setDoctorId(doctorId);
            		patientExam.setStatus(1L);
            		if(patientExam.getPatientExamId()==null){
            			patientExamService.insertExam(patientExam);
            			updateOrderService.saveOrderExam(recordId, patientExam);
            		}else if(patientExam.getIsCharged()==0){
            			patientExamService.updateExam(patientExam);
            			updateOrderService.saveOrderExam(recordId, patientExam);
					}
				}
            	if(patientInquiryRequest.getDeleteExamIdList()!=null){
            		for (Long patientExamId : patientInquiryRequest.getDeleteExamIdList()) {
                    	patientExamService.deleteExam(patientExamId);
                    	updateOrderService.deleteRecordItem(recordId, patientExamId, OrderItemListType.EXAM);
        			}
            	}
            }

//			Long doctorId = SessionUtils.getDoctorId();
//			Long recordId = patientInquiryRequest.getRecordId();
//			Long patientId = patientInquiryRequest.getPatientId();
//			patientInquiryRequest.setDoctorId(doctorId);
//			patientInquiryRequest.setModle(1);	//病历标识
//			patientInquiryRequest.setVer(1);
//
//			if(patientId==null){
//				hisResponse.setErrorCode(4011);
//				hisResponse.setErrorMessage("患者ID为空，无法创建病历");//???没有患者ID，直接创建一个患者不行吗？
//				return hisResponse.toString();
//			}
//			// 1.  门诊-- 保存或修改患者信息
//			patientService.savePatientFromOutpatient(patientInquiryRequest);
//
//			// 2. 保存医生和患者关联关系
//			doctorService.saveDoctorPatientRelationForOutpatient(doctorId,patientId);
//
//			// 3. 保存或更新 患者病历信息
//			patientInquiryService.addInquiryInfoForOutpatient(patientInquiryRequest);
//
//			// 4. 重置患者此次病历 的诊断信息
//			patientDiagnosisService.resetDiagnosisByRecordId(recordId,patientInquiryRequest.getDiagnosis());
//
//			// 5. 保存患者的 检查项
//			patientExamService.addInquiryExam(patientInquiryRequest);

            //附件费用信息保存
            List<PatientAdditional> patientAdditionalList = patientInquiryRequest.getAdditionalList();
            if(patientAdditionalList==null || patientAdditionalList.size()<1){
            	patientAdditionalService.delAdditionalByRecordId(recordId);
            	updateOrderService.deleteAllRecordItems(recordId, OrderItemListType.ADDITION_AMT);
            }else{
            	for (PatientAdditional patientAdditional : patientAdditionalList) {
            		patientAdditional.setRecordId(recordId);
            		patientAdditional.setFlag(1l);
            		if(patientAdditional.getAdditionalId()==null){
            			patientAdditionalService.insertPatientAdditional(patientAdditional);
            			updateOrderService.saveOrderAdditionAmt(recordId, patientAdditional);;
            		}else if(patientAdditional.getIsCharged()==0){
            			patientAdditionalService.updatePatientAdditional(patientAdditional);
            			updateOrderService.saveOrderAdditionAmt(recordId, patientAdditional);
					}
				}
            	
            	if(patientInquiryRequest.getDeleteAdditionalIdList()!=null){
            		for (Long additionalId : patientInquiryRequest.getDeleteAdditionalIdList()) {
                		patientAdditionalService.deletePatientAdditional(additionalId);
                		updateOrderService.deleteRecordItem(recordId, additionalId, OrderItemListType.ADDITION_AMT);
        			}
            	}
            }
            
            //西药处方信息保存
            List<Map<String, Object>> patentPrescriptionList = patientInquiryRequest.getPatentPrescriptionList();
            if(patentPrescriptionList==null || patentPrescriptionList.size()<1){
            	patientDrugService.delDrugByRecordId(recordId);
            	updateOrderService.deleteAllRecordItems(recordId, OrderItemListType.PATIENT_PRESCRIPTION);
            }else{
            	for (Map<String, Object> patentDrugList : patentPrescriptionList) {
                	@SuppressWarnings("unchecked")
					List<Object> patientDrugObjectList = (List<Object>) patentDrugList.get("patentDrugList");
                	
                	Integer isCharged = LangUtils.getInteger(patentDrugList.get("isCharged"));
                	Long prescription = LangUtils.getLong(patentDrugList.get("prescription"));
                    if (patientDrugObjectList != null && patientDrugObjectList.size()>0){
                    	if(prescription==null || prescription==0l){
                    		prescription = patientDrugService.getPrescription();
                    	}
                    	if(isCharged == 0){
                    		patientDrugService.deletePatientDrugPrescription(prescription);
                    		List<PatientDrug> patientDrugList = new ArrayList<PatientDrug>();
                        	for (Object object : patientDrugObjectList) {
                        		PatientDrug patientDrug = JSON.parseObject(JSON.toJSONString(object), PatientDrug.class);
                        		patientDrug.setDoctorId(doctorId);
                        		patientDrug.setPatientId(patientId);
                        		patientDrug.setRecordId(recordId);
                        		patientDrug.setPrescription(prescription);
                        		if(patientDrug.getDataSource()==null)
                        			patientDrug.setDataSource(5L);
                        		patientDrugList.add(patientDrug);
        					}
                        	patientDrugService.insertDrugByList(prescription, patientDrugList);
                        	updateOrderService.saveOrderPatentPrescription(recordId, patientDrugList);
                    	}
                    }
        		}
            	
            	if(patientInquiryRequest.getDeletePatentPrescriptionList()!=null){
            		for (Long prescription : patientInquiryRequest.getDeletePatentPrescriptionList()) {
                		patientDrugService.deletePatientDrugPrescription(prescription);
                		updateOrderService.deleteRecordItem(recordId, prescription, OrderItemListType.PATIENT_PRESCRIPTION);
        			}
            	}
            }
            
            //中药处方信息保存
            List<PatientChineseDrug> chinesePrescriptionList = patientInquiryRequest.getChinesePrescriptionList();
            if(chinesePrescriptionList==null || chinesePrescriptionList.size()<1){
            	patientDrugService.delChineseDrugByRecordId(recordId);
            	updateOrderService.deleteAllRecordItems(recordId, OrderItemListType.CHINESE_PRESCRIPTION);
            }else{
            	for (PatientChineseDrug patientChineseDrug : chinesePrescriptionList){
            		patientChineseDrug.setRecordId(recordId);
            		Integer isCharged = patientChineseDrug.getIsCharged();
                	Long prescription = patientChineseDrug.getPrescription();
                	if(prescription==null || prescription==0l){
                		prescription = patientDrugService.getPrescription();
                		patientChineseDrug.setPrescription(prescription);
                		Long result = patientDrugService.insertChineseDrugPrescription(patientChineseDrug);
                		if(result!=null){
                            patientDrugService.insertPrescriptionPiecesByList(patientChineseDrug.getPaChDrugId(), patientChineseDrug.getDecoctionPiecesList());
                            updateOrderService.saveOrderChinesePrescription(recordId, patientChineseDrug);
                        }
                	}else if(isCharged == 0){
                		patientDrugService.deleteChineseDrugPrescription(prescription);
                		Long result = patientDrugService.insertChineseDrugPrescription(patientChineseDrug);
                		if(result!=null){
                            patientDrugService.insertPrescriptionPiecesByList(patientChineseDrug.getPaChDrugId(), patientChineseDrug.getDecoctionPiecesList());
                            updateOrderService.saveOrderChinesePrescription(recordId, patientChineseDrug);
                            
                        }
                	}
                }
            	
            	if(patientInquiryRequest.getDeleteChinesePrescriptionList()!=null){
            		for (Long prescription : patientInquiryRequest.getDeleteChinesePrescriptionList()) {
                		patientDrugService.deleteChineseDrugPrescription(prescription);
                		updateOrderService.deleteRecordItem(recordId, prescription, OrderItemListType.CHINESE_PRESCRIPTION);
        			}
            	}
            }
            
            Map<String, Long> result = new HashMap<>();
            result.put("recordId", recordId);
            hisResponse.setBody(result);
    	}catch (Exception e) {
    		hisResponse.setErrorCode(4011);
    		hisResponse.setErrorMessage("保存病历出错!");
    		e.printStackTrace();
		}
    	return hisResponse.toString();
    }


	/**
     * <p>Description:保存处方<p>
     * @author ZhouPengyu
     * @date 2016-7-16 11:25:49
     */
	@RequestMapping(value = "/savePrescription", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String savePrescription(@RequestBody PatientInquiryRequest patientInquiryRequest){
    	HisResponse hisResponse = new HisResponse();
    	try{
        	Long doctorId = SessionUtils.getDoctorId();
        	Long recordId = patientInquiryRequest.getRecordId();
        	Long patientId = patientInquiryRequest.getPatientId();

			if(checkPatientInquiryData(patientInquiryRequest,hisResponse)){
				return hisResponse.toString();
			}
        	
        	Patient patient = new Patient();
        	patient.setPatientId(patientId);
        	patient.setDoctorId(doctorId);
        	patient.setPatientName(patientInquiryRequest.getPatientName());
        	patient.setGender(patientInquiryRequest.getGender());
        	patient.setPhoneNo(patientInquiryRequest.getPhoneNo());
        	patient.setAge(patientInquiryRequest.getAge());
        	patient.setAgeType(patientInquiryRequest.getAgeType());
        	patient.setDiagnosisDate(DateUtil.getGeneralDate(new Date()));
        	
    		Patient exist = patientService.getPatientById(patientId);
    		if(exist==null){
    			patient.setPatientPinyin(ChineseToEnglish.getPingYin(patient.getPatientName()));
    			patientService.addPatient(patient);
    		}else{
    			exist.setAge(patient.getAge());
    			exist.setAgeType(patient.getAgeType());
    			exist.setPhoneNo(patient.getPhoneNo());
    			exist.setDiagnosisDate(patient.getDiagnosisDate());
    			patientService.savePatient(exist);
    		}
    		
    		//判断关联关系是否存
    		if(doctorService.isDoctorPatientRelation(doctorId, patientId)<1){
    			doctorService.insertDoctorPatientRelation(doctorId, patientId);
    		}
        	
    		patientInquiryRequest.setModle(2);	//处方标识
    		patientInquiryRequest.setVer(1);
    		patientInquiryRequest.setDoctorId(doctorId);
    		if(patientInquiryService.getInquiryByRecordId(recordId)==null){
    			patientInquiryService.addInquiry(patientInquiryRequest);
            	orderService.createHospitalOrder(patientInquiryRequest);
            }else{
            	patientInquiryService.saveInquiry(patientInquiryRequest);
            }
    		
    		patientDiagnosisService.deleteDiagnosisByRecordId(recordId);
            List<PatientDiagnosis> diagnosis = patientInquiryRequest.getDiagnosis();
            if(diagnosis!=null && diagnosis.size()>0)
            	patientDiagnosisService.insertDiagnosis(recordId, diagnosis);		//添加诊断
            
            
            List<PatientExam> patientExamList = patientInquiryRequest.getExamList();
            if(patientExamList==null || patientExamList.size()<1){
            	patientExamService.delExamByRecordId(recordId);
            	updateOrderService.deleteAllRecordItems(recordId, OrderItemListType.EXAM);
            }else{
            	for (PatientExam patientExam : patientExamList) {
            		patientExam.setRecordId(recordId);
            		patientExam.setPatientId(patientId);
            		patientExam.setDoctorId(doctorId);
            		patientExam.setStatus(1L);
            		if(patientExam.getPatientExamId()==null){
            			patientExamService.insertExam(patientExam);
            			updateOrderService.saveOrderExam(recordId, patientExam);
            		}else if(patientExam.getIsCharged()==0){
            			patientExamService.updateExam(patientExam);
            			updateOrderService.saveOrderExam(recordId, patientExam);
					}
				}
            	if(patientInquiryRequest.getDeleteExamIdList()!=null){
            		for (Long patientExamId : patientInquiryRequest.getDeleteExamIdList()) {
                    	patientExamService.deleteExam(patientExamId);
                    	updateOrderService.deleteRecordItem(recordId, patientExamId, OrderItemListType.EXAM);
        			}
            	}
            }
            
            //附件费用信息保存
            List<PatientAdditional> patientAdditionalList = patientInquiryRequest.getAdditionalList();
            if(patientAdditionalList==null || patientAdditionalList.size()<1){
            	patientAdditionalService.delAdditionalByRecordId(recordId);
            	updateOrderService.deleteAllRecordItems(recordId, OrderItemListType.ADDITION_AMT);
            }else{
            	for (PatientAdditional patientAdditional : patientAdditionalList) {
            		patientAdditional.setRecordId(recordId);
            		patientAdditional.setFlag(1l);
            		if(patientAdditional.getAdditionalId()==null){
            			patientAdditionalService.insertPatientAdditional(patientAdditional);
            			updateOrderService.saveOrderAdditionAmt(recordId, patientAdditional);;
            		}else if(patientAdditional.getIsCharged()==0){
            			patientAdditionalService.updatePatientAdditional(patientAdditional);
            			updateOrderService.saveOrderAdditionAmt(recordId, patientAdditional);
					}
				}
            	
            	if(patientInquiryRequest.getDeleteAdditionalIdList()!=null){
            		for (Long additionalId : patientInquiryRequest.getDeleteAdditionalIdList()) {
                		patientAdditionalService.deletePatientAdditional(additionalId);
                		updateOrderService.deleteRecordItem(recordId, additionalId, OrderItemListType.ADDITION_AMT);
        			}
            	}
            }
            
            //西药处方信息保存
            List<Map<String, Object>> patentPrescriptionList = patientInquiryRequest.getPatentPrescriptionList();
            if(patentPrescriptionList==null || patentPrescriptionList.size()<1){
            	patientDrugService.delDrugByRecordId(recordId);
            	updateOrderService.deleteAllRecordItems(recordId, OrderItemListType.PATIENT_PRESCRIPTION);
            }else{
            	for (Map<String, Object> patentDrugList : patentPrescriptionList) {
                	@SuppressWarnings("unchecked")
					List<Object> patientDrugObjectList = (List<Object>) patentDrugList.get("patentDrugList");
                	
                	Integer isCharged = LangUtils.getInteger(patentDrugList.get("isCharged"));
                	Long prescription = LangUtils.getLong(patentDrugList.get("prescription"));
                    if (patientDrugObjectList != null && patientDrugObjectList.size()>0){
                    	if(prescription==null || prescription==0l){
                    		prescription = patientDrugService.getPrescription();
                    	}
                    	if(isCharged == 0){
                    		patientDrugService.deletePatientDrugPrescription(prescription);
                    		List<PatientDrug> patientDrugList = new ArrayList<PatientDrug>();
                        	for (Object object : patientDrugObjectList) {
                        		PatientDrug patientDrug = JSON.parseObject(JSON.toJSONString(object), PatientDrug.class);
                        		patientDrug.setDoctorId(doctorId);
                        		patientDrug.setPatientId(patientId);
                        		patientDrug.setRecordId(recordId);
                        		patientDrug.setPrescription(prescription);
                        		if(patientDrug.getDataSource()==null)
                        			patientDrug.setDataSource(5L);
                        		patientDrugList.add(patientDrug);
        					}
                        	patientDrugService.insertDrugByList(prescription, patientDrugList);
                        	updateOrderService.saveOrderPatentPrescription(recordId, patientDrugList);
                    	}
                    }
        		}
            	
            	if(patientInquiryRequest.getDeletePatentPrescriptionList()!=null){
            		for (Long prescription : patientInquiryRequest.getDeletePatentPrescriptionList()) {
                		patientDrugService.deletePatientDrugPrescription(prescription);
                		updateOrderService.deleteRecordItem(recordId, prescription, OrderItemListType.PATIENT_PRESCRIPTION);
        			}
            	}
            }
            
            //中药处方信息保存
            List<PatientChineseDrug> chinesePrescriptionList = patientInquiryRequest.getChinesePrescriptionList();
            if(chinesePrescriptionList==null || chinesePrescriptionList.size()<1){
            	patientDrugService.delChineseDrugByRecordId(recordId);
            	updateOrderService.deleteAllRecordItems(recordId, OrderItemListType.CHINESE_PRESCRIPTION);
            }else{
            	for (PatientChineseDrug patientChineseDrug : chinesePrescriptionList){
            		patientChineseDrug.setRecordId(recordId);
            		Integer isCharged = patientChineseDrug.getIsCharged();
                	Long prescription = patientChineseDrug.getPrescription();
                	if(prescription==null || prescription==0l){
                		prescription = patientDrugService.getPrescription();
                		patientChineseDrug.setPrescription(prescription);
                		Long result = patientDrugService.insertChineseDrugPrescription(patientChineseDrug);
                		if(result!=null){
                            patientDrugService.insertPrescriptionPiecesByList(patientChineseDrug.getPaChDrugId(), patientChineseDrug.getDecoctionPiecesList());
                            updateOrderService.saveOrderChinesePrescription(recordId, patientChineseDrug);
                        }
                	}else if(isCharged == 0){
                		patientDrugService.deleteChineseDrugPrescription(prescription);
                		Long result = patientDrugService.insertChineseDrugPrescription(patientChineseDrug);
                		if(result!=null){
                            patientDrugService.insertPrescriptionPiecesByList(patientChineseDrug.getPaChDrugId(), patientChineseDrug.getDecoctionPiecesList());
                            updateOrderService.saveOrderChinesePrescription(recordId, patientChineseDrug);
                            
                        }
                	}
                }
            	
            	if(patientInquiryRequest.getDeleteChinesePrescriptionList()!=null){
            		for (Long prescription : patientInquiryRequest.getDeleteChinesePrescriptionList()) {
                		patientDrugService.deleteChineseDrugPrescription(prescription);
                		updateOrderService.deleteRecordItem(recordId, prescription, OrderItemListType.CHINESE_PRESCRIPTION);
        			}
            	}
            }
            
            Map<String, Long> result = new HashMap<>();
            result.put("recordId", recordId);
            hisResponse.setBody(result);
    	}catch (Exception e) {
    		hisResponse.setErrorCode(4011);
    		hisResponse.setErrorMessage("保存病历出错!");
    		e.printStackTrace();
		}
    	return hisResponse.toString();
    }

    /**
     * <p>Description:今日就诊<p>
     * @author ZhouPengyu
     * @date 2016-3-7 11:31:46
     */
    @RequestMapping(value = "/todayPatientInfo", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String todayPatientInfo(@RequestBody Map<String, String> requestParams){
        HisResponse hisResponse = new HisResponse();
        Long doctorId = SessionUtils.getDoctorId();
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        
        String today = DateUtil.formatDate(new Date());
        String startToday = today+" 00:00:00";
        String endToday = today+" 23:59:59";
        parameterMap.put("startToday", startToday);
        parameterMap.put("endToday", endToday);
        parameterMap.put("doctorId", doctorId);
        
        List<PatientInquiry> patientInquirys = patientInquiryService.getTodayPatientInquiry(parameterMap);
        List<Map<String, Object>> patientList = new ArrayList<Map<String,Object>>();
        for (PatientInquiry patientInquiry : patientInquirys) {
        	Map<String, Object> patientInfo = new HashMap<String, Object>();
        	Patient patient = patientService.getPatientById(patientInquiry.getPatientId());
        	if(patient==null)
        		continue;
        	patientInfo.put("patientId", patient.getPatientId());
        	patientInfo.put("patientName", patient.getPatientName());
        	patientInfo.put("gender", patient.getGender());
        	patientInfo.put("recordId", patientInquiry.getRecordId());
        	patientInfo.put("modle", patientInquiry.getModle());
        	if(StringUtils.isEmpty(patient.getAgeType())){
	        	//得到当前的年份
	            String cYear = DateUtil.formatDate(new Date()).substring(0,4);
	            //得到生日年份
	            String birthYear = patient.getBirthday().substring(0,4);
	            int age = Integer.parseInt(cYear) - Integer.parseInt(birthYear);
	            patient.setAge(Double.valueOf(age));
	            patient.setAgeType("岁");
	        }
        	patientInfo.put("age", patient.getAge());
        	patientInfo.put("ageType", patient.getAgeType());
        	if(patientInquiry.getModifyDate().contains("."))
        		patientInfo.put("updateDate", patientInquiry.getModifyDate().substring(0, patientInquiry.getModifyDate().indexOf(".")));
			else
				patientInfo.put("updateDate", patientInquiry.getModifyDate());
        	patientInfo.put("updateDate", StringUtils.substringAfter(patientInfo.get("updateDate").toString(), " ").substring(0, 5));
        	patientList.add(patientInfo);
		}
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("patientList", patientList);
        hisResponse.setBody(body);
        return hisResponse.toString();
    }

    /**
     * <p>Description:病历详情<p>
     * @author ZhouPengyu
     * @date 2016年3月8日 下午7:41:20
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/getRecordInfo", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String getRecordInfo(@RequestBody Map<String, String> requestParams){
		Long doctorId = SessionUtils.getDoctorId();
		
    	HisResponse hisResponse = new HisResponse();
        try {
        	Long patientId = null;
            if (requestParams.containsKey("patientId")) {
                patientId = Long.valueOf(requestParams.get("patientId"));
            }
            Patient patient;
            Doctor doctor = null;
            if (patientId == null) {
                hisResponse.setErrorCode(1);
                hisResponse.setErrorMessage("patientId is required!");
                return hisResponse.toString();
            }
            Map<String, Object> mapPatient = new HashMap<String, Object>();
    		mapPatient.put("doctorId", doctorId);
    		mapPatient.put("patientId", patientId);
    		patient = patientService.searchDocContainPatient(mapPatient);
    		
    		Map<String, Object> body = new HashMap<String, Object>();
    		Long recordId = LangUtils.getLong(requestParams.get("recordId"));
    		
    		List<Function> functionList = doctorService.getDoctorFunction(SessionUtils.getDoctorId());
    		if(functionList==null)
    			functionList = new ArrayList<Function>();
    		Optional<Function> functionListOptional = functionList.stream().filter(function ->function.getFunctionId()==5000L).findFirst();
    		if(SessionUtils.isAdmin() || functionListOptional.isPresent())
    			patient = patientService.getPatientById(patientId);
    		
    		if(patient!=null){
    	        PatientInquiry patientInquiry = patientInquiryService.getInquiryByRecordId(recordId);
    	        if(patientInquiry.getPatientId().equals(patient.getPatientId())){
    	        	doctor = doctorService.getDoctorById(patientInquiry.getDoctorId());
    	        	body = JSON.parseObject(JSON.toJSONString(patientInquiry), HashMap.class);
    		        if (doctor != null){
    		        	body.put("doctorName", doctor.getDoctorName());
    		        	body.put("realName", doctor.getRealName());
    		        }
    		        body.put("organizationName", hospitalService.getHospitalName(doctorId));
    		        body.put("patientId", patient.getPatientId());
    				body.put("patientName", patient.getPatientName());
    				body.put("gender", patient.getGender());
    				body.put("phoneNo", patient.getPhoneNo());
    				if(patientInquiry.getAgeType()==null){
    					if(StringUtils.isEmpty(patient.getAgeType())){
    			        	//得到当前的年份
    			            String cYear = DateUtil.formatDate(new Date()).substring(0,4);
    			            //得到生日年份
    			            String birthYear = patient.getBirthday().substring(0,4);
    			            int age = Integer.parseInt(cYear) - Integer.parseInt(birthYear);
    			            patient.setAge(Double.valueOf(age));
    			            patient.setAgeType("岁");
    			        }
    					body.put("age", patient.getAge());
    					body.put("ageType", patient.getAgeType());
    				}
    				List<PatientDiagnosis> diagnosisList = patientDiagnosisService.getDiagnosisByRecordId(recordId);
    				body.put("diagnosis", diagnosisList);
    				
    		        List<PatientExam> patientExamList = patientExamService.getExamByRecordId(recordId);
    		        List<Map<String, Object>> examList = new ArrayList<>();
    		        for (PatientExam patientExam : patientExamList){
    		            Map<String, Object> map = new HashMap<>();
    		            map.put("patientExamId", patientExam.getPatientExamId());
    		            map.put("examName", patientExam.getExamName());
    		            map.put("examId", patientExam.getExamId());
    		            map.put("examPrice", patientExam.getPrice());
    		            map.put("dataSource", patientExam.getDataSource());
    		            map.put("total", patientExam.getTotal());
    		            map.put("isCharged", getIsChargedService.isCharged(recordId, patientExam.getPatientExamId(), OrderItemListType.EXAM));
    		            examList.add(map);
    		        }
    		        body.put("examList", examList);
    		        
    		        List<PatientDrug> patientDrugList = patientDrugService.getDrugByRecordId(recordId);
    		        Set<Long> prescriptionList = new HashSet<Long>();
    		        for (PatientDrug patientDrug : patientDrugList) {
    		        	prescriptionList.add(patientDrug.getPrescription());
    				}
    		        List<Map<String, Object>> patentPrescriptionList = new ArrayList<>();
    		        for (Long prescription : prescriptionList){
    		        	Map<String, Object> map = new HashMap<String, Object>();
    		        	map.put("recordId", recordId);
    		        	map.put("prescription", prescription);
    		        	List<PatientDrug> patentDrugList = patientDrugService.getDrugByPrescription(map);
    		        	if(patentDrugList==null || patentDrugList.size()<1){
    		        		map.put("prescription", null);
    		        		patentDrugList = patientDrugService.getDrugByPrescription(map);
    		        	}
    		        	
    		        	if(patentDrugList!=null && patentDrugList.size()>0){
    		        		Integer isCharged = getIsChargedService.isCharged(recordId, patentDrugList.get(0).getPrescription(), OrderItemListType.PATIENT_PRESCRIPTION);
    		        		List<Map<String, Object>> patientDrugListMap = new ArrayList<Map<String,Object>>();
    			        	for (PatientDrug patientDrug : patentDrugList) {
    			        		Map<String, Object> patientDrugMap = new HashMap<String, Object>();
    			        		patientDrugMap = JSON.parseObject(JSON.toJSONString(patientDrug), Map.class);
    			        		List<SaleWayPojo> saleWayPojoList = new ArrayList<SaleWayPojo>();
    			        		if(patientDrug.getDataSource()!=null && patientDrug.getDataSource()==0l && patientDrug.getDrugId()!=null){
    			        			if(isCharged==1){	//兼容前端
    			        				SaleWayPojo saleWayPojo = new SaleWayPojo();
    			        				saleWayPojo.setDosage(1D);
    			        				saleWayPojo.setInventoryCount(99999999D);
    			        				saleWayPojo.setUnit(patientDrug.getTotalDosageUnit());
    			        				saleWayPojo.setPrice(patientDrug.getUnivalence());
    			        				saleWayPojoList.add(saleWayPojo);
    			        			}else
    			        				saleWayPojoList = drugService.searchDrugSaleWayById(1, patientDrug.getDrugId());
    			        		}
    			        		patientDrugMap.put("saleWays", saleWayPojoList);	//查询售药方式
    			        		patientDrugListMap.add(patientDrugMap);
    						}
    		        		
    		        		Map<String, Object> patentPrescription = new HashMap<String, Object>();
    			        	patentPrescription.put("isCharged", isCharged);
    			        	patentPrescription.put("prescription", patentDrugList.get(0).getPrescription());
    			        	patentPrescription.put("patentDrugList", patientDrugListMap);
    			        	patentPrescriptionList.add(patentPrescription);
    		        	}
    		        }
    		        body.put("patentPrescriptionList", patentPrescriptionList);
    		        
    		        List<PatientChineseDrug> chinesePrescriptionList = patientDrugService.getChineseDrugByRecordId(recordId);
    		        List<PatientChineseDrugResponse> chinesePrescriptionResponseList = new ArrayList<PatientChineseDrugResponse>();
    		        for (PatientChineseDrug patientChineseDrug : chinesePrescriptionList){
    		        	PatientChineseDrugResponse chineseDrugResponse = new PatientChineseDrugResponse(patientChineseDrug);
    		        	List<PatientChineseDrugPieces> chineseDrugPiecesList = patientDrugService.getChineseDrugPieces(patientChineseDrug.getPaChDrugId());
    		        	List<PatientChineseDrugPiecesResponse> chineseDrugPiecesResponseList = new ArrayList<PatientChineseDrugPiecesResponse>();
    		        	
    		        	Integer isCharged = getIsChargedService.isCharged(recordId, patientChineseDrug.getPrescription(), OrderItemListType.CHINESE_PRESCRIPTION);
    		        	for (PatientChineseDrugPieces patientChineseDrugPieces : chineseDrugPiecesList) {
    		        		PatientChineseDrugPiecesResponse chineseDrugPiecesResponse = new PatientChineseDrugPiecesResponse(patientChineseDrugPieces);
    		        		if(patientChineseDrugPieces.getDataSource()!=null && patientChineseDrugPieces.getDataSource()==0l && patientChineseDrugPieces.getDrugId()!=null){
    		        			//查询售药方式
    		        			List<SaleWayPojo> saleWayPojoList = new ArrayList<SaleWayPojo>();
    		        			if(isCharged==1){	//兼容前端
    		        				SaleWayPojo saleWayPojo = new SaleWayPojo();
    		        				saleWayPojo.setDosage(1D);
    		        				saleWayPojo.setInventoryCount(99999999D);
    		        				saleWayPojo.setUnit("g");
    		        				saleWayPojo.setPrice(patientChineseDrugPieces.getUnivalence());
    		        				saleWayPojoList.add(saleWayPojo);
    		        			}else
    		        				saleWayPojoList = drugService.searchDrugSaleWayById(1, patientChineseDrugPieces.getDrugId());
    		        			chineseDrugPiecesResponse.setSaleWays(saleWayPojoList);
    		        		}
    		        		chineseDrugPiecesResponseList.add(chineseDrugPiecesResponse);
    					}
    		        	chineseDrugResponse.setDecoctionPiecesResponseList(chineseDrugPiecesResponseList);
    		        	chineseDrugResponse.setIsCharged(isCharged);
    		        	chinesePrescriptionResponseList.add(chineseDrugResponse);
    		        }
    		        
    		        body.put("chinesePrescriptionList", chinesePrescriptionResponseList);
    		        
    		        PatientAdditional searchPatientAdditional = new PatientAdditional();
    		        searchPatientAdditional.setRecordId(recordId);
    		        List<PatientAdditional> patientAdditionalList = patientAdditionalService.searchPatientAdditional(searchPatientAdditional);
    		        if(patientAdditionalList!=null){
    		        	for (PatientAdditional patientAdditional : patientAdditionalList) {
    						patientAdditional.setIsCharged(getIsChargedService.isCharged(recordId, patientAdditional.getAdditionalId(),
    								OrderItemListType.ADDITION_AMT));
    					}
    		        }
    		        body.put("additionalList", patientAdditionalList);
    		        
    		        hisResponse.setBody(body);
    	        }else{
    	        	hisResponse.setErrorCode(401);
    	        }
    		}else{
    			hisResponse.setErrorCode(401);
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
        return hisResponse.toString();
    }
    
    /**
     * <p>Description:根据名称查询诊断<p>
     * @author ZhouPengyu
     * @date 2016年3月8日 下午3:46:25
     */
    @RequestMapping(value = "/searchDisease", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String searchDisease(@RequestBody Map<String, String> requestParams){
        HisResponse hisResponse = new HisResponse();
        try {
        	String diseaseName = requestParams.get("diseaseName");
            String version = requestParams.get("version");
            List<Diagnosis> diagnosisList = diagnosisService.fuzzyQueryByName(diseaseName);
            Map<String, Object> result = new HashMap<>();
            result.put("diseaseList", diagnosisList);
            result.put("version", version);
            hisResponse.setBody(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return hisResponse.toString();
    }

	private static boolean checkPatientInquiryData(PatientInquiryRequest patientInquiryRequest, HisResponse hisResponse) {
		if (patientInquiryRequest.getPatientId() == null){
			hisResponse.setErrorCode(4001L);
			hisResponse.setErrorMessage("患者ID为空，无法创建病历");
			return true;
		}

		if (patientInquiryRequest.getAge() == null) {
			hisResponse.setErrorCode(4002L);
			hisResponse.setErrorMessage("患者年龄为空，请填写");
			return true;
		}

		if (CollectionUtils.isEmpty(patientInquiryRequest.getDiagnosis())) {
			hisResponse.setErrorCode(4003L);
			hisResponse.setErrorMessage("诊断不能为空，请填写");
			return true;
		}
		return false;
	}


	/**
	 * <p>Description:第一次 （增加）门诊病历<p>
	 * @author Tangwenwu
	 * @date 2016年3月12日 下午4:29:48
	 */
	@RequestMapping(value = "/addPatinetRecordFirst", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String addPatinetRecordFirst(@RequestBody PatientInquiryRequest patientInquiryRequest){
		long beginTime = System.currentTimeMillis();// 1、开始时间
		HisResponse hisResponse = new HisResponse();
		try{
			patientInquiryRequest.setDoctorId(SessionUtils.getDoctorId());
			if(checkPatientInquiryData(patientInquiryRequest,hisResponse)){
				return hisResponse.toString();
			}
			patientInquiryRequest.setModle(1);//门诊病历
			patientInquiryRequest.setVer(1);
			hisResponse = inquiryService.firstAddPatinetInquiry(patientInquiryRequest);
		}catch (Exception e) {
			hisResponse.setErrorCode(4011);
			hisResponse.setErrorMessage("保存病历出错!");
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();// 2、结束时间
		long consumeTime = endTime - beginTime;// 3、消耗的时间
		System.out.println(String.format("%s consume %d millis", "==========addPatinetRecordFirst", consumeTime));
		return hisResponse.toString();
	}


	/**
	 * <p>Description:第一次 （增加）门诊处方<p>
	 * @author Tangwenwu
	 * @date 2016年3月12日 下午4:29:48
	 */
	@RequestMapping(value = "/addPrescriptionFirst", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String addPrescriptionFirst(@RequestBody PatientInquiryRequest patientInquiryRequest){
		long beginTime = System.currentTimeMillis();// 1、开始时间
		HisResponse hisResponse = new HisResponse();
		try{
			patientInquiryRequest.setDoctorId(SessionUtils.getDoctorId());
			if(checkPatientInquiryData(patientInquiryRequest,hisResponse)){
				return hisResponse.toString();
			}
			patientInquiryRequest.setModle(2);//门诊病历
			patientInquiryRequest.setVer(1);
			hisResponse = inquiryService.firstAddPatinetInquiry(patientInquiryRequest);
		}catch (Exception e) {
			hisResponse.setErrorCode(4011);
			hisResponse.setErrorMessage("保存病历出错!");
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();// 2、结束时间
		long consumeTime = endTime - beginTime;// 3、消耗的时间
		System.out.println(String.format("%s consume %d millis", "---------------addPrescriptionFirst", consumeTime));
		return hisResponse.toString();
	}



}
