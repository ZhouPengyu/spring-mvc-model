package com.hm.his.module.outpatient.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hm.his.module.order.model.OrderItemListType;
import com.hm.his.module.order.service.UpdateOrderService;
import com.hm.his.module.outpatient.model.request.PatientInquiryRequest;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.hm.his.framework.utils.LangUtils;
import com.hm.his.module.drug.pojo.SaleWayPojo;
import com.hm.his.module.drug.service.DrugService;
import com.hm.his.module.outpatient.dao.PatientDrugMapper;
import com.hm.his.module.outpatient.model.PatientChineseDrug;
import com.hm.his.module.outpatient.model.PatientChineseDrugPieces;
import com.hm.his.module.outpatient.model.PatientDrug;
import com.hm.his.module.outpatient.model.response.PatientChineseDrugPiecesResponse;
import com.hm.his.module.outpatient.model.response.PatientChineseDrugResponse;
import com.hm.his.module.outpatient.service.PatientDrugService;
import com.hm.his.module.outpatient.service.PatientInquiryService;

/**
 * Created by wangjialin on 15/12/14.
 */
@Service("PatientDrugService")
public class PatientDrugServiceImpl implements PatientDrugService{
    @Autowired(required = false)
    PatientDrugMapper patientDrugMapper;
    @Autowired(required = false)
    PatientInquiryService patientInquiryService;
    @Autowired(required = false)
    DrugService drugService;
	@Autowired(required = false)
	UpdateOrderService updateOrderService;
    
    @Override
    public List<PatientDrug> getDrugByRecordId(Long recordId) {
        if (recordId == null) return new ArrayList<>();
        return patientDrugMapper.getDrugByRecordId(recordId);
    }

    @Override
    public void insertDrug(PatientDrug patientDrug) {
        patientDrugMapper.insertDrug(patientDrug);
    }

	@Override
	public List<PatientDrug> getDrugByPrescription(Map<String, Object> map) {
		if (map == null) return new ArrayList<PatientDrug>();
		return patientDrugMapper.getDrugByPrescription(map);
	}

	@Override
	public List<PatientChineseDrug> getChineseDrugByRecordId(Long recordId) {
		if (recordId == null) return new ArrayList<>();
        return patientDrugMapper.getChineseDrugByRecordId(recordId);
	}

	@Override
	public List<PatientChineseDrugPieces> getChineseDrugPieces(Long paChDrugId) {
		if (paChDrugId == null) return new ArrayList<PatientChineseDrugPieces>();
		return patientDrugMapper.getChineseDrugPieces(paChDrugId);
	}

	@Override
	public Long insertChineseDrugPrescription(PatientChineseDrug patientChineseDrug) {
		if (patientChineseDrug == null) return null;
        return patientDrugMapper.insertChineseDrugPrescription(patientChineseDrug);
	}

	@Override
	public Long firstAddChineseDrugPrescription(PatientInquiryRequest patientInquiryRequest) {
		List<PatientChineseDrug> chinesePrescriptionList = patientInquiryRequest.getChinesePrescriptionList();
		if(CollectionUtils.isNotEmpty(chinesePrescriptionList)){
			Long recordId = patientInquiryRequest.getRecordId();
			for (PatientChineseDrug patientChineseDrug : chinesePrescriptionList){
				patientChineseDrug.setRecordId(recordId);
				Long prescription = this.getPrescription();
				patientChineseDrug.setPrescription(prescription);
				Long result = this.insertChineseDrugPrescription(patientChineseDrug);
				if(result!=null){
					this.insertPrescriptionPiecesByList(patientChineseDrug.getPaChDrugId(), patientChineseDrug.getDecoctionPiecesList());
					updateOrderService.firstSaveOrderChinesePrescription(recordId, patientChineseDrug,patientInquiryRequest.getHospitalOrder());
				}
			}
		}
		return  null;
	}

	@Override
	public void insertPrescriptionPiecesByList(Long paChDrugId, List<PatientChineseDrugPieces> patientChinesePiecesList) {
		if(CollectionUtils.isEmpty(patientChinesePiecesList)) return;
		patientDrugMapper.insertPrescriptionPiecesByList(paChDrugId, patientChinesePiecesList);
	}

	@Override
	public void delDrugByRecordId(Long recordId) {
		patientDrugMapper.delDrugByRecordId(recordId);
	}

	@Override
	public void deletePatientDrugPrescription(Long prescription) {
		patientDrugMapper.deletePatientDrugPrescription(prescription);
	}

	@Override
	public Long getPrescription() {
		return patientDrugMapper.getPrescription();
	}

	@Override
	public void delChineseDrugByRecordId(Long recordId) {
        patientDrugMapper.delChineseDrugByRecordId(recordId);
	}

	@Override
	public void delChinesePiecesByRecordId(Long recordId) {
		patientDrugMapper.delChinesePiecesByRecordId(recordId);
	}

	@Override
	public void deleteChineseDrugPrescription(Long prescription) {
		patientDrugMapper.deleteChineseDrugPrescription(prescription);
	}

	@Override
	public void insertDrugByList(Long prescription,
			List<PatientDrug> patientDrugList) {
		if(CollectionUtils.isNotEmpty(patientDrugList))
			patientDrugMapper.insertDrugByList(prescription, patientDrugList);
	}

	@Override
	public void firstAddDrugPrescription(PatientInquiryRequest patientInquiryRequest) {
		List<Map<String, Object>> patentPrescriptionList = patientInquiryRequest.getPatentPrescriptionList();
		Long recordId = patientInquiryRequest.getRecordId();
		if(CollectionUtils.isNotEmpty(patentPrescriptionList)){

			for (Map<String, Object> patentDrugList : patentPrescriptionList) {
				List<Object> patientDrugObjectList = (List<Object>) patentDrugList.get("patentDrugList");

				Integer isCharged = LangUtils.getInteger(patentDrugList.get("isCharged"));
				Long prescription = LangUtils.getLong(patentDrugList.get("prescription"));
				if (patientDrugObjectList != null && patientDrugObjectList.size()>0){
					if(prescription==null || prescription==0L){
						prescription = this.getPrescription();
					}
					if(isCharged == 0){
						List<PatientDrug> patientDrugList = new ArrayList<PatientDrug>();
						for (Object object : patientDrugObjectList) {
							PatientDrug patientDrug = JSON.parseObject(JSON.toJSONString(object), PatientDrug.class);
							patientDrug.setDoctorId(patientInquiryRequest.getDoctorId());
							patientDrug.setPatientId(patientInquiryRequest.getPatientId());
							patientDrug.setRecordId(recordId);
							patientDrug.setPrescription(prescription);
							if(patientDrug.getDataSource()==null)
								patientDrug.setDataSource(5L);
							patientDrugList.add(patientDrug);
						}
						this.insertDrugByList(prescription, patientDrugList);
						updateOrderService.firstSaveOrderPatentPrescription(recordId, patientDrugList,patientInquiryRequest.getHospitalOrder());
					}
				}
			}
		}
	}

	@Override
	public Long savePatientDrugPrescriptionTem(Map<String, Object> patentDrugList) {
		@SuppressWarnings("unchecked")
		List<Object> patientDrugObjectList = (List<Object>) patentDrugList.get("patentDrugList");
    	
    	Long prescription = LangUtils.getLong(patentDrugList.get("prescription"));
    	if(prescription==null || prescription==0l){
    		prescription = this.getPrescription();
    	}else{
			this.deletePatientDrugPrescription(prescription);
		}
		List<PatientDrug> patientDrugList = new ArrayList<PatientDrug>();
    	for (Object object : patientDrugObjectList) {
    		PatientDrug patientDrug = JSON.parseObject(JSON.toJSONString(object), PatientDrug.class);
    		patientDrug.setPrescription(prescription);
    		patientDrugList.add(patientDrug);
		}
    	this.insertDrugByList(prescription, patientDrugList);
		return prescription;
	}

	@Override
	public Long saveChineseDrugPrescriptionTem(PatientChineseDrug patientChineseDrug) {
		Long prescription = patientChineseDrug.getPrescription();
		if(prescription!=null && prescription!=0l){
			this.deleteChineseDrugPrescription(prescription);
			this.delChinesePiecesByPrescription(prescription);
		}else{
			prescription = this.getPrescription();
			patientChineseDrug.setPrescription(prescription);
		}
		Long result = this.insertChineseDrugPrescription(patientChineseDrug);
		if(result!=null)
			this.insertPrescriptionPiecesByList(patientChineseDrug.getPaChDrugId(), patientChineseDrug.getDecoctionPiecesList());
        else
        	return 0L;
		return prescription;
	}

	@Override
	public void delChinesePiecesByPrescription(Long prescription) {
		patientDrugMapper.delChinesePiecesByPrescription(prescription);
	}
	
	@Override
	public void delChinesePiecesByPaChDrugId(Long paChDrugId) {
		patientDrugMapper.delChinesePiecesByPaChDrugId(paChDrugId);
	}
	
	@Override
	public List<PatientDrug> getPatientDrugByPrescription(Long prescription){
		List<PatientDrug> patentDrugList = patientDrugMapper.getPatientDrugByPrescription(prescription);
		return patentDrugList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getPatientDrugPrescriptionTem(Long prescription) {
		List<PatientDrug> patentDrugList = patientDrugMapper.getPatientDrugByPrescription(prescription);
		if(patentDrugList==null || patentDrugList.size()<1)
			return null;
		Map<String, Object> patentPrescription = new HashMap<String, Object>();
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
    	patentPrescription.put("prescription", patentDrugList.get(0).getPrescription());
    	patentPrescription.put("patentDrugList", patientDrugListMap);
		return patentPrescription;
	}

	@Override
	public PatientChineseDrugResponse getChineseDrugPrescriptionTem(Long prescription) {
		List<PatientChineseDrug> chinesePrescriptionList = patientDrugMapper.getChineseDrugByPrescription(prescription);
		if(chinesePrescriptionList==null || chinesePrescriptionList.size()<1)
			return null;
		PatientChineseDrug patientChineseDrug = chinesePrescriptionList.get(0);
		PatientChineseDrugResponse chineseDrugResponse = new PatientChineseDrugResponse(patientChineseDrug);
    	List<PatientChineseDrugPieces> chineseDrugPiecesList = patientDrugMapper.getChineseDrugPieces(patientChineseDrug.getPaChDrugId());
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
    	return chineseDrugResponse;
	}
}
