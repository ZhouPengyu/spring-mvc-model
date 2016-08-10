package com.hm.his.module.outpatient.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hm.his.framework.model.MultipleDataSource;
import com.hm.his.framework.utils.LangUtils;
import com.hm.his.module.outpatient.dao.DiagnosisMapper;
import com.hm.his.module.outpatient.model.Diagnosis;
import com.hm.his.module.outpatient.service.DiagnosisService;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-2 15:16:05
 * @description 诊断服务接口实现类
 * @version 3.0
 */
@Service("DiagnosisService")
public class DiagnosisServiceImpl implements DiagnosisService{
    @Autowired(required = false)
    DiagnosisMapper diagnosisMapper;
    @Override
    public List<Diagnosis> fuzzyQueryByName(String diseaseName) {
        if (diseaseName == null) diseaseName = "";
        List<Diagnosis> result;
        try {
            MultipleDataSource.setDataSourceKey("hmcdss");
            if(StringUtils.isEmpty(diseaseName)) {
                String defaultDiagnosis = "34051,7733,621,34470,33831,608,5455,863,34145,12608";
                List<String> idStrList = Arrays.asList(StringUtils.split(defaultDiagnosis, ","));
                List<Long> idList = new ArrayList<>();
                for (String str : idStrList){
                    idList.add(LangUtils.getLong(str));
                }
                List<Diagnosis> diagnosisList = diagnosisMapper.getDiagnosisByIdList(idList);
                Map<Long, Diagnosis> map = new HashMap<>();
                for (Diagnosis diagnosis : diagnosisList){
                    map.put(diagnosis.getDiseaseId(), diagnosis);
                }
                result = new ArrayList<>();
                for (Long id : idList){
                    if (map.containsKey(id)) result.add(map.get(id));
                }
            }else {
                result = diagnosisMapper.fuzzyQueryByName(diseaseName);
            }
        } finally {
            MultipleDataSource.setDataSourceKey("his");
        }
        return result;
    }
}
