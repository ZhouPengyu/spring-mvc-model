package com.hm.his.module.order.service;

import com.hm.his.module.order.model.OrderItemListType;


/**
 * 
 * @description 获取是否收费服务
 * @author lipeng
 * @date 2016年3月10日
 */
public interface GetIsChargedService {
	/**
	 * 
	 * @description 查询某一项或单是否已收费  1：是，0：否
	 * @date 2016年3月10日
	 * @author lipeng
	 * @param id
	 * @param type
	 * @return
	 */
	int isCharged(Long recordId,Long id, OrderItemListType type);

}
