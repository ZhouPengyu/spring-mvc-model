package com.hm.his.module.outpatient.service.impl;

import com.hm.his.framework.model.HisResponse;
import com.hm.his.module.drug.service.DrugService;
import com.hm.his.module.order.model.HospitalOrder;
import com.hm.his.module.order.service.GetIsChargedService;
import com.hm.his.module.order.service.OrderService;
import com.hm.his.module.order.service.UpdateOrderService;
import com.hm.his.module.outpatient.model.request.PatientInquiryRequest;
import com.hm.his.module.outpatient.service.*;
import com.hm.his.module.user.service.DoctorService;
import com.hm.his.module.user.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016-07-26
 * Time: 11:30
 * CopyRight:HuiMei Engine
 */
@Service
public class InquiryServiceImpl implements InquiryService {

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

    /**
     *  功能描述：首次新增保存 门诊病历
     * @author:  tangww
     * @createDate   2016-07-26
     *
     */
    @Override
    public HisResponse firstAddPatinetInquiry(PatientInquiryRequest patientInquiryRequest) throws Exception {
        HisResponse hisResponse = new HisResponse();

        Long recordId = patientInquiryRequest.getRecordId();

        try {
            // 1.  门诊-- 保存或修改患者信息
            patientService.savePatientFromOutpatient(patientInquiryRequest);

            // 2. 保存医生和患者关联关系
            doctorService.saveDoctorPatientRelationForOutpatient(patientInquiryRequest.getDoctorId(),patientInquiryRequest.getPatientId());

            // 3. 保存 患者病历信息
            patientInquiryService.addInquiry(patientInquiryRequest);
            // 4. 为病历 创建一个 订单  ---能否根据 检查项  --药品--附加费用 的size 更新状态
            HospitalOrder hospitalOrder = orderService.createHospitalOrder(patientInquiryRequest);
            patientInquiryRequest.setHospitalOrder(hospitalOrder);

            // 5. 保存患者此次病历 的诊断信息
            patientDiagnosisService.insertDiagnosis(recordId,patientInquiryRequest.getDiagnosis());

            // 6. 保存患者的 检查项
            patientExamService.firstAddInquiryExam(patientInquiryRequest);

            //7. 附加费用信息保存
            patientAdditionalService.firstAddPatientAdditional(patientInquiryRequest);

            //8. 西药处方信息保存
            patientDrugService.firstAddDrugPrescription(patientInquiryRequest);

            //9.中药处方信息保存
            patientDrugService.firstAddChineseDrugPrescription(patientInquiryRequest);
        }catch (Exception ex){
            ex.printStackTrace();
            throw ex;
        }
        Map<String, Long> result = new HashMap<>();
        result.put("recordId", recordId);
        hisResponse.setBody(result);
        return hisResponse;
    }
}
