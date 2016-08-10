package com.hm.his.module.order.dao;

import com.hm.his.module.order.model.OrderOperateRecord;

public interface OrderOperateRecordMapper {

	void insert(OrderOperateRecord orderOperateRecord);

	void deleteById(Long id);

	OrderOperateRecord selectById(Long id);

	void update(OrderOperateRecord record);
}