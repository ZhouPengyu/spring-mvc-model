package com.hm.his.module.outpatient.service;

import java.util.List;
import java.util.Map;

import com.hm.his.module.outpatient.model.PatientChineseDrug;
import com.hm.his.module.outpatient.model.PatientChineseDrugPieces;
import com.hm.his.module.outpatient.model.PatientDrug;
import com.hm.his.module.outpatient.model.request.PatientInquiryRequest;
import com.hm.his.module.outpatient.model.response.PatientChineseDrugResponse;

/**
 * Created by wangjialin on 15/12/14.
 */
public interface PatientDrugService {
	
    List<PatientDrug> getDrugByRecordId(Long recordId);
    
    List<PatientChineseDrug> getChineseDrugByRecordId(Long recordId);
    
    void insertDrug(PatientDrug patientDrug);
    
    void insertDrugByList(Long prescription, List<PatientDrug> patientDrugList);

    void firstAddDrugPrescription(PatientInquiryRequest patientInquiryRequest);
    
    /**
     * <p>Description:根据病历ID删除西药处方<p>
     * @author ZhouPengyu
     * @date 2016年3月12日 下午3:33:21
     */
    
    void delDrugByRecordId(Long recordId);
    
    /**
     * <p>Description:根据处方编号删除西药处方<p>
     * @author ZhouPengyu
     * @date 2016年3月12日 下午3:33:21
     */
    void deletePatientDrugPrescription(Long prescription);
    
    /**
     * <p>Description:西药处方查询<p>
     * <p>Company: H.M<p>
     * @author ZhouPengyu
     * @date 2016-1-25 下午6:54:59
     */
    List<PatientDrug> getDrugByPrescription(Map<String, Object> map);
    
    /**
     * <p>Description:根据病历ID删除中药处方<p>
     * @author ZhouPengyu
     * @date 2016年3月12日 下午4:02:07
     */
    void delChineseDrugByRecordId(Long recordId);
    
    /**
     * <p>Description:根据病历ID删除中药处方饮片<p>
     * @author ZhouPengyu
     * @date 2016年3月12日 下午4:04:13
     */
    void delChinesePiecesByRecordId(Long recordId);
    
    /**
     * <p>Description:根据处方编号删除中药处方<p>
     * @author ZhouPengyu
     * @date 2016年3月12日 下午3:33:21
     */
    void deleteChineseDrugPrescription(Long prescription);
    
    /**
     * <p>Description:根据处方编号删除中药处方饮片<p>
     * @author ZhouPengyu
     * @date 2016-4-11 17:14:54
     */
	void delChinesePiecesByPrescription(Long prescription);
    
    /**
     * <p>Description:中药处方保存<p>
     * <p>Company: H.M<p>
     * @author ZhouPengyu
     * @date 2016-1-25 下午6:54:59
     */
    Long insertChineseDrugPrescription(PatientChineseDrug patientChineseDrug);

    /**
     * <p>Description:第一次 保存 中药处方<p>
     * <p>Company: H.M<p>
     * @author ZhouPengyu
     * @date 2016-1-25 下午6:54:59
     */
    Long firstAddChineseDrugPrescription(PatientInquiryRequest patientInquiryRequest);

    
    /**
     * <p>Description:中药处方饮片<p>
     * <p>Company: H.M<p>
     * @author ZhouPengyu
     * @date 2016-1-25 下午6:54:59
     */
    List<PatientChineseDrugPieces> getChineseDrugPieces(Long paChDrugId);
    
    /**
     * <p>Description:中药处方饮片保存<p>
     * <p>Company: H.M<p>
     * @author ZhouPengyu
     * @date 2016-1-25 下午6:54:59
     */
    void insertPrescriptionPiecesByList(Long paChDrugId, List<PatientChineseDrugPieces> patientChinesePiecesList);
    
    /**
     * <p>Description:获取处方编号<p>
     * @author ZhouPengyu
     * @date 2016年3月12日 下午3:41:46
     */
    Long getPrescription();
    
    /**
     * <p>Description:根据中药处方表主键ID删除中药处方饮片<p>
     * @author ZhouPengyu
     * @date 2016年3月12日 下午4:04:13
     */
    void delChinesePiecesByPaChDrugId(Long paChDrugId);
    
    /**
     * <p>Description:保存西药处方模版<p>
     * @author ZhouPengyu
     * @date 2016年3月28日 下午3:47:19
     * @return  大于0 表示成功
     */
    Long savePatientDrugPrescriptionTem(Map<String, Object> patentDrugList);
    
    /**
     * <p>Description:保存中药处方模版<p>
     * @author ZhouPengyu
     * @date 2016年3月28日 下午3:47:19
     * @return 大于0 表示成功
     */
    Long saveChineseDrugPrescriptionTem(PatientChineseDrug patientChineseDrug);
    
    /**
     * <p>Description: 根据处方编号西药处方模版<p>
     * @author ZhouPengyu
     * @date 2016年3月28日 下午3:47:19
     */
    Map<String, Object> getPatientDrugPrescriptionTem(Long prescription);
    
    /**
     * <p>Description:根据处方编号中药处方模版<p>
     * @author ZhouPengyu
     * @date 2016年3月28日 下午3:47:19
     */
    PatientChineseDrugResponse getChineseDrugPrescriptionTem(Long prescription);

    List<PatientDrug> getPatientDrugByPrescription(Long prescription);
    
}
