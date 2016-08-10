package com.hm.his.module.purchase.dao;

import com.hm.his.module.purchase.model.DeliveryAddress;

public interface DeliveryAddressMapper {

    Integer insertDeliveryAddress(DeliveryAddress deliveryAddress);

    Integer updateDeliveryAddress(DeliveryAddress deliveryAddress);
    
	/**
	 * <p>Description:获取默认地址<p>
	 * @author ZhouPengyu
	 * @date 2016年6月1日 上午10:18:27
	 */
	DeliveryAddress getDefaultAddress(Integer hospitalId) throws Exception;
	
	DeliveryAddress getDeliveryAddressById(Integer deliveryAddressId) throws Exception;

}