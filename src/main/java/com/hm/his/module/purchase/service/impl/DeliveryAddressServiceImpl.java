package com.hm.his.module.purchase.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hm.his.module.purchase.dao.DeliveryAddressMapper;
import com.hm.his.module.purchase.model.DeliveryAddress;
import com.hm.his.module.purchase.service.DeliveryAddressService;

@Service("DeliveryAddressService")
public class DeliveryAddressServiceImpl implements DeliveryAddressService {

	@Autowired(required = false)
	DeliveryAddressMapper deliveryAddressMapper;
	
	@Override
	public DeliveryAddress getDefaultAddress(Integer hospitalId) throws Exception {
		DeliveryAddress deliveryAddress = null;
		try {
			deliveryAddress = deliveryAddressMapper.getDefaultAddress(hospitalId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deliveryAddress;
	}

	@Override
	public DeliveryAddress getDeliveryAddressById(Integer deliveryAddressId)
			throws Exception {
		DeliveryAddress deliveryAddress = null;
		try {
			deliveryAddress = deliveryAddressMapper.getDeliveryAddressById(deliveryAddressId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deliveryAddress;
	}

	@Override
	public Integer saveDeliveryAddress(DeliveryAddress deliveryAddress)
			throws Exception {
		Integer deliveryAddressId = deliveryAddress.getDeliveryAddressId();
		try {
			if(null != deliveryAddressId && 0 != deliveryAddressId){
				deliveryAddressMapper.updateDeliveryAddress(deliveryAddress);
			}else{
				deliveryAddressMapper.insertDeliveryAddress(deliveryAddress);
				deliveryAddressId = deliveryAddress.getDeliveryAddressId();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deliveryAddressId;
	}

}
