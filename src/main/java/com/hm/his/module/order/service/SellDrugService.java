package com.hm.his.module.order.service;

import com.hm.his.module.order.model.HospitalOrder;
import com.hm.his.module.order.model.SellDrugRecord;
import com.hm.his.module.order.pojo.SellDrugInfoPojo;

/**
 * 
 *  直接售药service
 * @author lipeng
 * @date 2016年3月3日
 */
public interface SellDrugService {
	/**
	 *  直接售药，1：生成售药记录，2：生成对应订单
	 * @date 2016年3月3日
	 * @author lipeng
	 * @param req
	 */
	void saveSellDrugRecord(SellDrugRecord sellDrugRecord);

	/**
	 * 
	 *  计算应收金额
	 * @date 2016年3月4日
	 * @author lipeng
	 * @param sellDrugRecord
	 */
	void calculateReceivableAmt(SellDrugRecord sellDrugRecord);

	/**
	 * 
	 *  根据直接售药记录生成订单,并收费
	 * @date 2016年3月3日
	 * @author lipeng
	 * @param sellDrugRecord
	 */
	void createOrderAndCharge(SellDrugRecord sellDrugRecord);

	/**
	 * 
	 * 
	 * @date 2016年3月13日
	 * @author lipeng
	 * @param sellDrugRecord
	 */
	void sellDrugs(SellDrugRecord sellDrugRecord);

	/**
	 * 
	 *  获取直接售药记录详情
	 * @date 2016年3月30日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	SellDrugInfoPojo getSellDrugInfo(HospitalOrder req);

}
