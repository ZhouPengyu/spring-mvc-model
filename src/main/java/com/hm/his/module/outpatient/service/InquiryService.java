package com.hm.his.module.outpatient.service;

import com.hm.his.framework.model.HisResponse;
import com.hm.his.module.outpatient.model.request.PatientInquiryRequest;

/**
 * 病历服务类（重构）
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016-07-26
 * Time: 11:28
 * CopyRight:HuiMei Engine
 */
public interface InquiryService {
    HisResponse firstAddPatinetInquiry(PatientInquiryRequest patientInquiryRequest) throws Exception;
}
