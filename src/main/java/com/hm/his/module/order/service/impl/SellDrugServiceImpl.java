package com.hm.his.module.order.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.hm.his.framework.log.annotation.SystemLogAnno;
import com.hm.his.framework.log.constant.FunctionConst;
import com.hm.his.framework.log.constant.Operation;
import com.hm.his.framework.utils.AmtUtils;
import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.module.drug.model.DrugTrade;
import com.hm.his.module.drug.model.DrugTypeEnum;
import com.hm.his.module.drug.model.HospitalDrug;
import com.hm.his.module.drug.pojo.InventoryOperatePojo;
import com.hm.his.module.drug.pojo.SaleWayPojo;
import com.hm.his.module.drug.service.DrugInventoryService;
import com.hm.his.module.drug.service.DrugService;
import com.hm.his.module.drug.service.DrugTradeService;
import com.hm.his.module.order.dao.HospitalOrderMapper;
import com.hm.his.module.order.dao.OrderDrugMapper;
import com.hm.his.module.order.dao.OrderItemChargeMapper;
import com.hm.his.module.order.dao.OrderItemListMapper;
import com.hm.his.module.order.dao.SellDrugRecordMapper;
import com.hm.his.module.order.model.ChargeStatus;
import com.hm.his.module.order.model.HospitalOrder;
import com.hm.his.module.order.model.OrderDrug;
import com.hm.his.module.order.model.OrderItemCharge;
import com.hm.his.module.order.model.OrderItemList;
import com.hm.his.module.order.model.OrderItemListType;
import com.hm.his.module.order.model.OrderItemType;
import com.hm.his.module.order.model.OrderOperateRecord.OperateItem;
import com.hm.his.module.order.model.SellDrug;
import com.hm.his.module.order.model.SellDrugRecord;
import com.hm.his.module.order.pojo.SaleChannel;
import com.hm.his.module.order.pojo.SellDrugInfoPojo;
import com.hm.his.module.order.service.OrderDrugService;
import com.hm.his.module.order.service.OrderOperateRecordService;
import com.hm.his.module.order.service.OrderService;
import com.hm.his.module.order.service.SellDrugService;
import com.hm.his.module.user.dao.DoctorMapper;
import com.hm.his.module.user.dao.HospitalMapper;
import com.hm.his.module.user.model.Doctor;
import com.hm.his.module.user.service.HospitalService;

/**
 * 
 * @description 直接售药服务
 * @author lipeng
 * @date 2016年3月3日
 */
@Service
public class SellDrugServiceImpl implements SellDrugService {
	@Autowired(required = false)
	SellDrugRecordMapper sellDrugRecordMapper;
	@Autowired
	OrderService orderService;
	@Autowired(required = false)
	HospitalOrderMapper hospitalOrderMapper;
	@Autowired(required = false)
	OrderItemListMapper orderItemListMapper;
	@Autowired(required = false)
	OrderDrugMapper orderDrugMapper;
	@Autowired(required = false)
	OrderItemChargeMapper orderItemChargeMapper;
	@Autowired
	OrderOperateRecordService orderOperateRecordService;
	@Autowired
	DrugService drugService;
	@Autowired
	DrugInventoryService drugInventoryService;
	@Autowired(required = false)
	HospitalMapper hospitalMapper;
	@Autowired
	HospitalService hospitalService;
	@Autowired(required = false)
	DoctorMapper doctorMapper;
	@Autowired
	DrugTradeService drugTradeService;
	@Autowired
	OrderDrugService orderDrugService;

	@Override
	public void saveSellDrugRecord(SellDrugRecord sellDrugRecord) {
		// 1.保存售药记录
		sellDrugRecord.setHospitalId(SessionUtils.getHospitalId());
		sellDrugRecord.setCreater(SessionUtils.getDoctorId());
		sellDrugRecordMapper.insert(sellDrugRecord);

		// 3.计算应收金额
		this.calculateReceivableAmt(sellDrugRecord);
	}

	@Override
	public void calculateReceivableAmt(SellDrugRecord sellDrugRecord) {
		if (sellDrugRecord == null || CollectionUtils.isEmpty(sellDrugRecord.getDrugs())) {
			return;
		}
		List<SellDrug> sellDrugs = sellDrugRecord.getDrugs();
		Double receivableAmt = 0d;
		for (SellDrug sellDrug : sellDrugs) {
			Integer count = sellDrug.getCount();
			Double price = sellDrug.getSalePrice();
			if (count != null && price != null) {
				receivableAmt += AmtUtils.multiply(count, price);
			}
		}
		sellDrugRecord.setReceivableAmt(receivableAmt);
	}

	@Override
	public void createOrderAndCharge(SellDrugRecord sellDrugRecord) {
		if (sellDrugRecord == null) {
			return;
		}
		HospitalOrder hospitalOrder = this.createHospitalOrderBySellDrugRecord(sellDrugRecord);
		String orderNo = hospitalOrder.getOrderNo();
		// 1.创建订单中的售药记录（关系）
		OrderItemList<OrderDrug> orderSellDrugRecord = new OrderItemList<>();
		orderSellDrugRecord.setChargeStatus(ChargeStatus.CHARGED);
		orderSellDrugRecord.setForeignId(sellDrugRecord.getId());
		orderSellDrugRecord.setOrderNo(orderNo);
		orderSellDrugRecord.setType(OrderItemListType.SELL_DRUG.getType());
		orderSellDrugRecord.setReceivableAmt(sellDrugRecord.getReceivableAmt());
		orderItemListMapper.insert(orderSellDrugRecord);
		// 2.创建订单中的药物
		List<OperateItem> operateItems = Lists.newArrayList();
		List<SellDrug> sellDrugs = sellDrugRecord.getDrugs();
		if (CollectionUtils.isNotEmpty(sellDrugs)) {
			List<OrderItemCharge> orderItemCharges = new ArrayList<OrderItemCharge>();
			List<OrderDrug> orderDrugs = new ArrayList<OrderDrug>(sellDrugs.size());
			sellDrugs.forEach(sellDrug -> {
				OrderDrug orderDrug = new OrderDrug();
				Integer count = sellDrug.getCount();
				Double price = sellDrug.getSalePrice();
				Double receivableAmt = null;
				if (count != null && price != null) {
					// receivableAmt = count * price;
					receivableAmt = AmtUtils.multiply(price, count);
				}
				orderDrug.setDosage(count);
				orderDrug.setDrugId(sellDrug.getDrugId());
				// 如果dataSource为空说明是手动输入，默认为5
				orderDrug.setDataSource(sellDrug.getDataSource() == null ? 5 : sellDrug.getDataSource());
				orderDrug.setDrugName(sellDrug.getDrugName());
				orderDrug.setSaleChannel(SaleChannel.Sell.getType());

				if (sellDrug.getDataSource() != null) {
					if (sellDrug.getDataSource() == 0) {
						// 诊所的药
						HospitalDrug hospitalDrug = drugService.getDrugById(sellDrug.getDrugId());
						if (hospitalDrug != null) {
							orderDrug.setManufacturer(hospitalDrug.getManufacturer());
							orderDrug.setDrugType(hospitalDrug.getDrugType());
						}
					} else if (sellDrug.getDataSource() == 1) {
						// 惠每的药
						DrugTrade drugTrade = drugTradeService.getDrugById(sellDrug.getDrugId());
						if (drugTrade != null) {
							orderDrug.setManufacturer(drugTrade.getManufacturer());
							orderDrug.setDrugType(drugTrade.getDrugType());
						}

						//直接售药时，将惠每药物库的药物 添加到 诊所的药库中
						sellDrug.setManufacturer(drugTrade.getManufacturer());
						sellDrug.setDrugType(drugTrade.getDrugType());
						Long drugId = drugService.addDrugByHMDrug(sellDrug);
						orderDrug.setDrugId(drugId);
						orderDrug.setDataSource(0);
					}
				}
				orderDrug.setOrderItemListId(orderSellDrugRecord.getId());
				orderDrug.setOrderNo(orderNo);
				orderDrug.setHospitalId(SessionUtils.getHospitalId());
				orderDrug.setSalePrice(price);
				String saleUnit = sellDrug.getSaleUnit();
				if (StringUtils.isEmpty(saleUnit) && DrugTypeEnum.herbal.getDrugType() == sellDrug.getDrugType()) {
					// 饮片的默认单位是g
					saleUnit = "g";
				}
				orderDrug.setSaleUnit(saleUnit);
				if (sellDrug.getDataSource() != null && sellDrug.getDataSource() == 0) {
					// 如果是诊所管理的药，保存进货价和利润
					Double purchasePrice = this.getPurchasePrice(sellDrug.getDrugId(), saleUnit, SaleChannel.Sell);
					orderDrug.setPurchasePrice(purchasePrice);
					Double profit = this.getProfit(purchasePrice, sellDrug.getSalePrice());
					orderDrug.setProfit(profit);
				}
				orderDrug.setSpecification(sellDrug.getSpecification());
				orderDrugMapper.insert(orderDrug);
				orderDrugs.add(orderDrug);

				OrderItemCharge orderItemCharge = new OrderItemCharge();
				orderItemCharge.setChargeStatus(ChargeStatus.CHARGED);
				orderItemCharge.setCreater(SessionUtils.getDoctorId());
				orderItemCharge.setItemId(orderDrug.getId());
				orderItemCharge.setOrderNo(orderNo);
				orderItemCharge.setRecordId(orderDrug.getRecordId());
				orderItemCharge.setItemListId(orderSellDrugRecord.getId());
				orderItemCharge.setModifier(SessionUtils.getDoctorId());
				orderItemCharge.setReceivableAmt(receivableAmt);
				orderItemCharge.setItemType(OrderItemType.DRUG.getType());
				// 收费日期
				orderItemCharge.setChargeDate(new Date());
				// 收费人id
				orderItemCharge.setChargeOperator(SessionUtils.getDoctorId());
				orderItemCharges.add(orderItemCharge);

				OperateItem operateItem = new OperateItem(OrderItemType.DRUG.getType(), orderItemCharge.getItemId());
				operateItems.add(operateItem);
			});
			orderItemChargeMapper.insertList(orderItemCharges);
			// 减库存
			orderDrugService.cutInventory(orderDrugs);
			// 记录收费日志
			orderOperateRecordService.addChargeRecord(orderNo, sellDrugRecord.getActualAmt(),
					sellDrugRecord.getReceivableAmt(), operateItems, sellDrugRecord.getPayAmount());

		}
	}

	/**
	 * 
	 * @description 减库存
	 * @date 2016年3月15日
	 * @author lipeng
	 * @param orderDrugs
	 */
	@Deprecated
	private void cutInventory(List<OrderDrug> orderDrugs) {
		if (CollectionUtils.isEmpty(orderDrugs)) {
			return;
		}
		orderDrugs.stream().filter(orderDrug -> orderDrug.getDrugId() != null && orderDrug.getDataSource() != null && orderDrug.getDataSource() == 0)
				.forEach(orderDrug -> {
					// 如果药物来自诊所的话，进行库存操作
						Double amount = orderDrug.getDosage() == null ? null : orderDrug.getDosage().doubleValue();
						if (amount != null) {
							InventoryOperatePojo inventoryOperatePojo = new InventoryOperatePojo();
							inventoryOperatePojo.setDrugId(orderDrug.getDrugId());
							inventoryOperatePojo.setAmount(amount);
							inventoryOperatePojo.setHospitalId(SessionUtils.getHospitalId());
							inventoryOperatePojo.setUnit(orderDrug.getSaleUnit());
							drugInventoryService.cutInventory(inventoryOperatePojo);
						}
					});
	}

	/**
	 * 
	 * @description 根据直接售药记录创建订单主表数据
	 * @date 2016年3月4日
	 * @author lipeng
	 * @param sellDrugRecord
	 * @return
	 */
	private HospitalOrder createHospitalOrderBySellDrugRecord(SellDrugRecord sellDrugRecord) {
		HospitalOrder hospitalOrder = new HospitalOrder();
		String orderNo = orderService.createOrderNo();
		hospitalOrder.setOrderNo(orderNo);
		hospitalOrder.setCreater(SessionUtils.getDoctorId());
		hospitalOrder.setHospitalId(SessionUtils.getHospitalId());
		hospitalOrder.setOrderType(HospitalOrder.TYPE_SELL_DRUG);
		hospitalOrder.setChargeStatus(ChargeStatus.CHARGED);
		hospitalOrder.setModifier(SessionUtils.getDoctorId());
		hospitalOrder.setPatientName(sellDrugRecord.getPatientName());
		hospitalOrder.setSellDrugRecordId(sellDrugRecord.getId());
		hospitalOrder.setReceivableAmt(0d);
		hospitalOrder.setTotalReceivableAmt(sellDrugRecord.getReceivableAmt());
		hospitalOrder.setActualAmt(sellDrugRecord.getActualAmt());
		hospitalOrder.setRecordVersion(System.currentTimeMillis());
		hospitalOrder.setTotalReceivableAmt(sellDrugRecord.getReceivableAmt());
		hospitalOrder.setLastChargeDate(new Date());
		hospitalOrder.setLastChargeId(SessionUtils.getDoctorId());
		//Double discount = AmtUtils.subtract(sellDrugRecord.getReceivableAmt(), sellDrugRecord.getActualAmt());
		hospitalOrder.setDiscount(sellDrugRecord.getDiscount());
		hospitalOrder.setPayMode(sellDrugRecord.getPayMode());
		hospitalOrderMapper.insert(hospitalOrder);
		return hospitalOrder;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@SystemLogAnno(functionId = FunctionConst.ORDER_OPER_LOG, operationId = Operation.SELL_DRUG)
	public void sellDrugs(SellDrugRecord sellDrugRecord) {
		this.saveSellDrugRecord(sellDrugRecord);
		this.createOrderAndCharge(sellDrugRecord);
	}

	/**
	 * 
	 * @description 获取进货价
	 * @date 2016年3月28日
	 * @author lipeng
	 * @param drugId
	 * @param saleUnit
	 * @return
	 */
	private Double getPurchasePrice(Long drugId, String saleUnit, SaleChannel saleChannel) {
		if (drugId == null || saleUnit == null || saleChannel == null) {
			return null;
		}
		List<SaleWayPojo> saleWayPojos = drugService.searchDrugSaleWayById(saleChannel.getType(), drugId);
		if (CollectionUtils.isEmpty(saleWayPojos)) {
			return null;
		}
		SaleWayPojo saleWayPojo = null;
		for (SaleWayPojo pojo : saleWayPojos) {
			if (saleUnit.equals(pojo.getUnit())) {
				saleWayPojo = pojo;
				break;
			}
		}
		return saleWayPojo == null ? null : saleWayPojo.getPurchasePrice();
	}

	/**
	 * 
	 * @description 获取利润
	 * @date 2016年3月28日
	 * @author lipeng
	 * @param purchasePrice
	 * @param salePrice
	 * @return
	 */
	private Double getProfit(Double purchasePrice, Double salePrice) {
		if (purchasePrice == null || salePrice == null) {
			return 0D;
		}
		return AmtUtils.subtract(salePrice, purchasePrice);
	}

	@Override
	public SellDrugInfoPojo getSellDrugInfo(HospitalOrder req) {
		if (req == null || StringUtils.isEmpty(req.getOrderNo())) {
			return null;
		}
		HospitalOrder order = hospitalOrderMapper.selectByOrderNo(req.getOrderNo());
		if (order == null || HospitalOrder.TYPE_SELL_DRUG != order.getOrderType() || order.getSellDrugRecordId() == null) {
			return null;
		}
		SellDrugRecord sellDrugRecord = sellDrugRecordMapper.selectById(order.getSellDrugRecordId());
		if (sellDrugRecord != null) {
			SellDrugInfoPojo pojo = new SellDrugInfoPojo();
			pojo.setAge(sellDrugRecord.getAge());
			pojo.setAgeType(sellDrugRecord.getAgeType());
			pojo.setChargeDate(order.getLastChargeDate());
			// 收费员
			Doctor doctor = doctorMapper.getDoctorById(sellDrugRecord.getCreater());
			String chargeName = doctor == null ? null : doctor.getRealName();
			pojo.setChargeName(chargeName);
			if (sellDrugRecord.getGender() != null) {
				pojo.setGender(sellDrugRecord.getGender() == 1 ? "男" : "女");
			}else {
				pojo.setGender("");
			}
			// 医院名称
			String hospitalName = hospitalService.getHospitalName(sellDrugRecord.getCreater());
			pojo.setHospitalName(hospitalName);
			pojo.setIDCardNo(sellDrugRecord.getIdCardNo());
			pojo.setPatientName(sellDrugRecord.getPatientName());
			pojo.setPhoneNo(sellDrugRecord.getPhoneNo());
			List<OrderDrug> drugs = orderDrugMapper.selectListByOrderNo(order.getOrderNo());
			pojo.setDrugs(drugs);
			return pojo;
		}
		return null;
	}
}