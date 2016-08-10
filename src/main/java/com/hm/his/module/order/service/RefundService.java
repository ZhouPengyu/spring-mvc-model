package com.hm.his.module.order.service;

import com.hm.his.framework.model.HisResponse;
import com.hm.his.module.order.pojo.RefundRequest;

/**
 * 
 * @description
 * @author lipeng
 * @date 2016年3月10日
 */
public interface RefundService {
	/**
	 * @description
	 * @date 2016年3月10日
	 * @author lipeng
	 * @param refundRequest
	 */
	HisResponse refund(RefundRequest refundRequest)throws RuntimeException ;
}
