package com.hm.his.module.outpatient.service;

import java.util.List;

import com.hm.his.module.outpatient.model.Diagnosis;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-2 15:16:05
 * @description 诊断服务接口
 * @version 3.0
 */
public interface DiagnosisService {
    List<Diagnosis> fuzzyQueryByName(String diseaseName) throws Exception;
}
