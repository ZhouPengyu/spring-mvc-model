package com.hm.his.module.order.service;

import com.hm.his.framework.model.HisResponse;
import com.hm.his.module.order.pojo.ChargeRequest;

public interface ChargeService {
	/**
	 * 
	 * @description 收费
	 * @date 2016年3月1日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	HisResponse charge(ChargeRequest req) throws RuntimeException;
}
