package com.hm.his.module.outpatient.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hm.his.module.outpatient.model.PatientChineseDrug;
import com.hm.his.module.outpatient.model.PatientChineseDrugPieces;
import com.hm.his.module.outpatient.model.PatientDrug;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-7 10:55:16
 * @description 病历 数据库实现类
 * @version 3.0
 */
public interface PatientDrugMapper {
	
    List<PatientDrug> getDrugByRecordId(Long recordId);
    
    List<PatientChineseDrug> getChineseDrugByRecordId(Long recordId);
    
    void insertDrug(PatientDrug patientDrug);
    
    void insertDrugByList(@Param("prescription")Long prescription, @Param("list")List<PatientDrug> patientDrugList);
    
    /**
     * <p>Description:根据处方编号删除西药处方<p>
     * @author ZhouPengyu
     * @date 2016年3月12日 下午3:33:21
     */
    void deletePatientDrugPrescription(Long prescription);
    
    void delDrugByRecordId(Long recordId);
    
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
     * 
     * <p>Description:西药处方查询<p>
     * <p>Company: H.M<p>
     * @author ZhouPengyu
     * @date 2016-1-25 下午6:54:59
     */
    List<PatientDrug> getDrugByPrescription(Map<String, Object> map);
    
    /**
     * <p>Description:中药处方保存<p>
     * <p>Company: H.M<p>
     * @author ZhouPengyu
     * @date 2016-1-25 下午6:54:59
     */
    Long insertChineseDrugPrescription(PatientChineseDrug patientChineseDrug);
    
    /**
     * 
     * <p>Description:中药处方药片<p>
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
    void insertPrescriptionPiecesByList(@Param("paChDrugId") Long paChDrugId, @Param("list")List<PatientChineseDrugPieces> patientChinesePiecesList);
    
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
     * <p>Description:根据处方编号获取西药模版<p>
     * @author ZhouPengyu
     * @date 2016年3月29日 下午4:58:18
     */
    List<PatientDrug> getPatientDrugByPrescription(Long prescription);
    
    /**
     * <p>Description:根据处方编号获取中药模版<p>
     * @author ZhouPengyu
     * @date 2016年3月29日 下午4:58:45
     */
    List<PatientChineseDrug> getChineseDrugByPrescription(Long prescription);
}
