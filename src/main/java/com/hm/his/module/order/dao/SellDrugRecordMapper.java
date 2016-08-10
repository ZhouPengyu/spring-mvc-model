package com.hm.his.module.order.dao;

import com.hm.his.module.order.model.SellDrugRecord;

public interface SellDrugRecordMapper {
	void insert(SellDrugRecord sellDrugRecord);

	void delete(Long id);

	SellDrugRecord selectById(Long id);

	void update(SellDrugRecord sellDrugRecord);

}