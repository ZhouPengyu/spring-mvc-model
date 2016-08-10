package com.hm.his.module.outpatient.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hm.his.module.order.service.OrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.hm.his.framework.log.annotation.SystemLogAnno;
import com.hm.his.framework.log.constant.FunctionConst;
import com.hm.his.framework.log.constant.Operation;
import com.hm.his.framework.utils.LangUtils;
import com.hm.his.module.drug.pojo.SaleWayPojo;
import com.hm.his.module.drug.service.DrugService;
import com.hm.his.module.outpatient.dao.PatientDiagnosisMapper;
import com.hm.his.module.outpatient.dao.PatientInquiryMapper;
import com.hm.his.module.outpatient.model.PatientAdditional;
import com.hm.his.module.outpatient.model.PatientChineseDrug;
import com.hm.his.module.outpatient.model.PatientChineseDrugPieces;
import com.hm.his.module.outpatient.model.PatientDiagnosis;
import com.hm.his.module.outpatient.model.PatientDrug;
import com.hm.his.module.outpatient.model.PatientExam;
import com.hm.his.module.outpatient.model.PatientInquiry;
import com.hm.his.module.outpatient.model.request.PatientInquiryRequest;
import com.hm.his.module.outpatient.model.response.PatientChineseDrugPiecesResponse;
import com.hm.his.module.outpatient.model.response.PatientChineseDrugResponse;
import com.hm.his.module.outpatient.service.PatientAdditionalService;
import com.hm.his.module.outpatient.service.PatientDiagnosisService;
import com.hm.his.module.outpatient.service.PatientDrugService;
import com.hm.his.module.outpatient.service.PatientExamService;
import com.hm.his.module.outpatient.service.PatientInquiryService;
import com.hm.his.module.outpatient.service.PatientService;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-7 10:58:04
 * @description 病历接口实现类
 * @version 3.0
 */
@Service("PatientInquiryService")
public class PatientInquiryServiceImpl implements PatientInquiryService {
    @Autowired(required = false)
    PatientInquiryMapper patientInquiryMapper;
    @Autowired(required = false)
    PatientDiagnosisMapper diagnosisMapper;
    @Autowired(required = false)
	PatientService patientService;
    @Autowired(required = false)
    PatientDiagnosisService patientDiagnosisService;
    @Autowired(required = false)
    PatientExamService patientExamService;
    @Autowired(required = false)
    PatientAdditionalService patientAdditionalService;
    @Autowired(required = false)
    PatientDrugService patientDrugService;
    @Autowired(required = false)
    DrugService drugService;
	@Autowired(required = false)
	OrderService orderService;
    
    @Override
    public PatientInquiry getInquiryByRecordId(Long recordId) {
        if (recordId == null) return null;
        PatientInquiry patientInquiry = patientInquiryMapper.getInquiryByRecordId(recordId);
        if(patientInquiry==null) return null;
        if (StringUtils.isNotEmpty(patientInquiry.getCreateDate()) && patientInquiry.getCreateDate().length() > 10){
            patientInquiry.setCreateDate(patientInquiry.getCreateDate().substring(0, 10));
        }
        return patientInquiry;
    }

    @Override
    public List<PatientInquiry> getInquiryByPatient(Long patientId) {
        if (patientId == null) return new ArrayList<>();
        return patientInquiryMapper.getInquiryByPatientId(patientId);
    }

    @Override
    public List<PatientInquiry> getInquiryByDoctorId(Long doctorId) {
        if (doctorId == null) return new ArrayList<>();
        return patientInquiryMapper.getInquiryByDoctorId(doctorId);
    }

    @Override
    @SystemLogAnno(functionId = FunctionConst.OUTPATIENT_LOG,operationId = Operation.OUTPATIENT_MODIFY)
    public Long saveInquiry(PatientInquiry patientInquiry) {
        Long recordId = null;
        if (patientInquiry == null) return recordId;
        if (patientInquiry.getRecordId() == null){
            recordId = this.insertInquiry(patientInquiry);
        } else {
            recordId = this.updateInquiry(patientInquiry);
        }
        return recordId;
    }
    
    @Override
    @SystemLogAnno(functionId = FunctionConst.OUTPATIENT_LOG,operationId = Operation.OUTPATIENT_ADD)
    public void addInquiry(PatientInquiry patientInquiry) {
        patientInquiryMapper.insertInquiry(patientInquiry);
    }

	@Override
	public void addInquiryInfoForOutpatient(PatientInquiryRequest patientInquiryRequest) throws Exception {
		PatientInquiry patientInquiry = this.getInquiryByRecordId(patientInquiryRequest.getRecordId());
		if(patientInquiry == null){
			this.addInquiry(patientInquiryRequest);
			// 为病历创建一个 订单
			orderService.createHospitalOrder(patientInquiryRequest);
		}else{
			this.saveInquiry(patientInquiryRequest);
		}
	}

	@Override
    public List<PatientInquiry> getSymptom(List<Long> patientIdList) {
        return null;
    }

    private Long insertInquiry(PatientInquiry patientInquiry) {
        if (patientInquiry == null || patientInquiry.getPatientId() == null || patientInquiry.getDoctorId() == null) {
            return null;
        }
        patientInquiryMapper.insertInquiry(patientInquiry);
        return patientInquiry.getRecordId();
    }

    private Long updateInquiry(PatientInquiry patientInquiry) {
        if (patientInquiry == null || patientInquiry.getPatientId() == null
                || patientInquiry.getDoctorId() == null || patientInquiry.getRecordId() == null) {
            return null;
        }
        int ver = patientInquiryMapper.getRecordVer(patientInquiry.getRecordId());
        patientInquiry.setVer(ver + 1);
        patientInquiryMapper.updateInquiry(patientInquiry);
        diagnosisMapper.deleteDiagnosisByRecordId(patientInquiry.getRecordId());
        return patientInquiry.getRecordId();
    }

	@Override
	public PatientInquiry getInquiryNewByPatientId(Long patientId) {
		if (patientId == null) return new PatientInquiry();
        return patientInquiryMapper.getInquiryNewByPatientId(patientId);
	}

	@Override
	public List<PatientInquiry> getTodayPatientInquiry(Map<String, Object> map) {
		List<PatientInquiry> patientInquiryList = new ArrayList<PatientInquiry>();
    	if(map!=null){
    		patientInquiryList = patientInquiryMapper.getTodayPatientInquiry(map);
    	}
        return patientInquiryList;
	}

	@Override
	public Integer getRecordSeq() {
		return patientInquiryMapper.getRecordSeq();
	}

	@Override
	public Long saveInquiryTemplate(PatientInquiryRequest patientInquiryRequest) {
		Long recordId = patientInquiryRequest.getRecordId();
		try{
        	
    		patientInquiryRequest.setVer(null);		//存为病历模版
    		if(recordId==null){
    			addInquiry(patientInquiryRequest);
            }else{
            	recordId = LangUtils.getLong(getRecordSeq());
            	saveInquiry(patientInquiryRequest);
            }
    		
    		patientDiagnosisService.deleteDiagnosisByRecordId(recordId);
            List<PatientDiagnosis> diagnosis = patientInquiryRequest.getDiagnosis();
            if(diagnosis!=null && diagnosis.size()>0)
            	patientDiagnosisService.insertDiagnosis(recordId, diagnosis);		//添加诊断
            
            
            List<PatientExam> patientExamList = patientInquiryRequest.getExamList();
            if(patientExamList==null || patientExamList.size()<1){
            	patientExamService.delExamByRecordId(recordId);
            }else{
            	for (PatientExam patientExam : patientExamList) {
            		patientExam.setRecordId(recordId);
            		patientExam.setStatus(1l);
            		if(patientExam.getPatientExamId()==null){
            			patientExamService.insertExam(patientExam);
            		}else if(patientExam.getIsCharged()==0){
            			patientExamService.updateExam(patientExam);
					}
				}
            	if(patientInquiryRequest.getDeleteExamIdList()!=null){
            		for (Long patientExamId : patientInquiryRequest.getDeleteExamIdList()) {
                    	patientExamService.deleteExam(patientExamId);
        			}
            	}
            }
            
            //附件费用信息保存
            List<PatientAdditional> patientAdditionalList = patientInquiryRequest.getAdditionalList();
            if(patientAdditionalList==null || patientAdditionalList.size()<1){
            	patientAdditionalService.delAdditionalByRecordId(recordId);
            }else{
            	for (PatientAdditional patientAdditional : patientAdditionalList) {
            		patientAdditional.setRecordId(recordId);
            		patientAdditional.setFlag(1l);
            		if(patientAdditional.getAdditionalId()==null){
            			patientAdditionalService.insertPatientAdditional(patientAdditional);
            		}else if(patientAdditional.getIsCharged()==0){
            			patientAdditionalService.updatePatientAdditional(patientAdditional);
					}
				}
            	
            	if(patientInquiryRequest.getDeleteAdditionalIdList()!=null){
            		for (Long additionalId : patientInquiryRequest.getDeleteAdditionalIdList()) {
                		patientAdditionalService.deletePatientAdditional(additionalId);
        			}
            	}
            }
            
            //西药处方信息保存
            List<Map<String, Object>> patentPrescriptionList = patientInquiryRequest.getPatentPrescriptionList();
            if(patentPrescriptionList==null || patentPrescriptionList.size()<1){
            	patientDrugService.delDrugByRecordId(recordId);
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
                        		patientDrug.setRecordId(recordId);
                        		patientDrug.setPrescription(prescription);
                        		if(patientDrug.getDataSource()==null)
                        			patientDrug.setDataSource(5L);
                        		patientDrugList.add(patientDrug);
        					}
                        	patientDrugService.insertDrugByList(prescription, patientDrugList);
                    	}
                    }
        		}
            	
            	if(patientInquiryRequest.getDeletePatentPrescriptionList()!=null){
            		for (Long prescription : patientInquiryRequest.getDeletePatentPrescriptionList()) {
                		patientDrugService.deletePatientDrugPrescription(prescription);
        			}
            	}
            }
            
            //中药处方信息保存
            List<PatientChineseDrug> chinesePrescriptionList = patientInquiryRequest.getChinesePrescriptionList();
            if(chinesePrescriptionList==null || chinesePrescriptionList.size()<1){
            	patientDrugService.delChineseDrugByRecordId(recordId);
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
                        }
                	}else if(isCharged == 0){
                		patientDrugService.deleteChineseDrugPrescription(prescription);
                		Long result = patientDrugService.insertChineseDrugPrescription(patientChineseDrug);
                		if(result!=null){
                            patientDrugService.insertPrescriptionPiecesByList(patientChineseDrug.getPaChDrugId(), patientChineseDrug.getDecoctionPiecesList());
                            
                        }
                	}
                }
            	
            	if(patientInquiryRequest.getDeleteChinesePrescriptionList()!=null){
            		for (Long prescription : patientInquiryRequest.getDeleteChinesePrescriptionList()) {
                		patientDrugService.deleteChineseDrugPrescription(prescription);
        			}
            	}
            }
            
    	}catch (Exception e) {
    		e.printStackTrace();
		}
		return recordId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getInquiryTemplateById(Long recordId) {
		Map<String, Object> body = new HashMap<String, Object>();
		try {
			PatientInquiry patientInquiry = getInquiryByRecordId(recordId);
			if(patientInquiry == null)
				return null;
			body = JSON.parseObject(JSON.toJSONString(patientInquiry), HashMap.class);
			
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
	        	
	        	List<Map<String, Object>> patientDrugListMap = new ArrayList<Map<String,Object>>();
	        	for (PatientDrug patientDrug : patentDrugList) {
	        		Map<String, Object> patientDrugMap = new HashMap<String, Object>();
	        		patientDrugMap = JSON.parseObject(JSON.toJSONString(patientDrug), Map.class);
	        		List<SaleWayPojo> saleWayPojoList = new ArrayList<SaleWayPojo>();
	        		if(patientDrug.getDataSource()!=null && patientDrug.getDataSource()==0l && patientDrug.getDrugId()!=null){
	        			saleWayPojoList = drugService.searchDrugSaleWayById(1, patientDrug.getDrugId());
	        		}
	        		patientDrugMap.put("saleWays", saleWayPojoList);	//查询售药方式
	        		patientDrugListMap.add(patientDrugMap);
				}
	        	Map<String, Object> patentPrescription = new HashMap<String, Object>();
	        	patentPrescription.put("prescription", patentDrugList.get(0).getPrescription());
	        	patentPrescription.put("patentDrugList", patientDrugListMap);
	        	patentPrescriptionList.add(patentPrescription);
	        }
	        body.put("patentPrescriptionList", patentPrescriptionList);
	        
	        List<PatientChineseDrug> chinesePrescriptionList = patientDrugService.getChineseDrugByRecordId(recordId);
	        List<PatientChineseDrugResponse> chinesePrescriptionResponseList = new ArrayList<PatientChineseDrugResponse>();
	        for (PatientChineseDrug patientChineseDrug : chinesePrescriptionList){
	        	PatientChineseDrugResponse chineseDrugResponse = new PatientChineseDrugResponse(patientChineseDrug);
	        	List<PatientChineseDrugPieces> chineseDrugPiecesList = patientDrugService.getChineseDrugPieces(patientChineseDrug.getPaChDrugId());
	        	List<PatientChineseDrugPiecesResponse> chineseDrugPiecesResponseList = new ArrayList<PatientChineseDrugPiecesResponse>();
	        	for (PatientChineseDrugPieces patientChineseDrugPieces : chineseDrugPiecesList) {
	        		PatientChineseDrugPiecesResponse chineseDrugPiecesResponse = new PatientChineseDrugPiecesResponse(patientChineseDrugPieces);
	        		if(patientChineseDrugPieces.getDataSource()!=null && patientChineseDrugPieces.getDataSource()==0l && patientChineseDrugPieces.getDrugId()!=null){
	        			//查询售药方式
	        			chineseDrugPiecesResponse.setSaleWays(drugService.searchDrugSaleWayById(1, patientChineseDrugPieces.getDrugId()));
	        		}
	        		chineseDrugPiecesResponseList.add(chineseDrugPiecesResponse);
				}
	        	chineseDrugResponse.setDecoctionPiecesResponseList(chineseDrugPiecesResponseList);
	        	chinesePrescriptionResponseList.add(chineseDrugResponse);
	        }
	        body.put("chinesePrescriptionList", chinesePrescriptionResponseList);
	        
	        PatientAdditional searchPatientAdditional = new PatientAdditional();
	        searchPatientAdditional.setRecordId(recordId);
	        List<PatientAdditional> patientAdditionalList = patientAdditionalService.searchPatientAdditional(searchPatientAdditional);
	        body.put("additionalList", patientAdditionalList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return body;
	}
}
