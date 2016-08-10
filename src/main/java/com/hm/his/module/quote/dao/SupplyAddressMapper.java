package com.hm.his.module.quote.dao;

import com.hm.his.module.quote.model.SupplyAddress;
import com.hm.his.module.quote.pojo.SupplyAddressPojo;

public interface SupplyAddressMapper {
    int deleteByPrimaryKey(Integer supplyAddressId);

    int insert(SupplyAddress record);

    int insertSelective(SupplyAddress record);

    SupplyAddress selectByPrimaryKey(Integer supplyAddressId);

    int updateByPrimaryKeySelective(SupplyAddress record);

    int updateByPrimaryKey(SupplyAddress record);

    Integer insertSupplyAddress(SupplyAddressPojo req);

    Integer updateSupplyAddress(SupplyAddressPojo req);

    SupplyAddress getAddressById(Integer supplyAddressId);

    SupplyAddress getSupplyAddress(Integer hospitalId);
}