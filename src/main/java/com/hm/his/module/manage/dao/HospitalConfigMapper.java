package com.hm.his.module.manage.dao;



import com.hm.his.module.manage.model.HospitalConfig;

import java.util.List;

public interface HospitalConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HospitalConfig record);

    int insertSelective(HospitalConfig record);

    HospitalConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(HospitalConfig record);

    int updateByPrimaryKey(HospitalConfig record);

    List<HospitalConfig> searchHospitalConfigList(HospitalConfig record);

    int deleteHospitalConfigByHospitalIdAndConfigType(HospitalConfig record);

    int batchInsertHospitalConfig(List<HospitalConfig> configs);
}