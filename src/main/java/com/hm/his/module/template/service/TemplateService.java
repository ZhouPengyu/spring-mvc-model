package com.hm.his.module.template.service;

import com.hm.his.framework.model.HisResponse;
import com.hm.his.module.drug.model.HospitalDrug;
import com.hm.his.module.drug.pojo.DrugRequest;
import com.hm.his.module.drug.pojo.HospitalDrugSug;
import com.hm.his.module.drug.pojo.SaleWayPojo;
import com.hm.his.module.outpatient.model.request.PatientInquiryRequest;
import com.hm.his.module.template.model.Template;
import com.hm.his.module.template.pojo.TemplateRequest;

import java.util.List;

/**
 * 医生模板 服务
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/2/29
 * Time: 10:04
 * CopyRight:HuiMei Engine
 */
public interface TemplateService {
    Template getTemplateById(Long id);


    List<Template> searchTemplateList(TemplateRequest tempRequest);

    List<Template> searchTemplateListForRecipe(TemplateRequest tempRequest);


    Integer countTemplates(TemplateRequest tempRequest);

    Integer countTemplatesForRecipe(TemplateRequest tempRequest);

    /**
     *  功能描述： 8.9.	保存医嘱
     *
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */

    HisResponse saveDoctorAdviceTemplate(Template template);
    /**
     *  功能描述： 8.3.	保存病历模板
     *
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    HisResponse savePatientRecordTemplate(PatientInquiryRequest patientInquiryRequest);
    /**
     *  功能描述： 8.5.	保存中药处方模板
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    HisResponse saveHerbalTemplate(PatientInquiryRequest patientInquiryRequest);

    /**
     *  功能描述：8.7.	保存西药处方模板
     *
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    HisResponse savePatentPrescriptionTemplate(PatientInquiryRequest patientInquiryRequest);

    PatientInquiryRequest getPatientRecordTemplate(Long tempId);
    PatientInquiryRequest getHerbalTemplate(Long tempId);
    PatientInquiryRequest getPatentPrescriptionTemplate(Long tempId);

    /**
     *  功能描述：5.9.	修改保存模板
     *
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    HisResponse modifyDoctorAdviceTemplate(Template template);

    /**
     *  功能描述：5.10.	删除模板
     *  用户删除模板，逻辑删除
     *  return :删除成功的总数
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    Integer deleteTemplate(List<Long> tempIds);



}
