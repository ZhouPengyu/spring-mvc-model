package com.hm.his.module.order.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hm.his.module.order.dao.SellDrugRecordMapper;
import com.hm.his.module.order.model.SellDrugRecord;
import com.hm.his.module.order.service.SellDrugRecordService;

@Service
public class SellDrugRecordServiceImpl implements SellDrugRecordService{
	@Autowired
	SellDrugRecordMapper sellDrugRecordMapper;
	@Override
	public SellDrugRecord getSellDrugRecordById(Long id) {
		return sellDrugRecordMapper.selectById(id);
	}

}
