package com.hm.his.module.order.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
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
import com.hm.his.module.order.model.ChargeStatus;
import com.hm.his.module.order.model.HospitalOrder;
import com.hm.his.module.order.model.OrderDrug;
import com.hm.his.module.order.model.OrderItemCharge;
import com.hm.his.module.order.model.OrderItemList;
import com.hm.his.module.order.model.OrderItemListType;
import com.hm.his.module.order.model.OrderItemType;
import com.hm.his.module.order.pojo.SaleChannel;
import com.hm.his.module.order.service.OrderDrugService;
import com.hm.his.module.outpatient.model.PatientChineseDrug;
import com.hm.his.module.outpatient.model.PatientChineseDrugPieces;
import com.hm.his.module.outpatient.model.PatientDrug;
import com.hm.his.module.statistics.pojo.NameRequst;

/**
 * 
 * @description
 * @author lipeng
 * @date 2016年3月4日
 */
@Service
public class OrderDrugServiceImpl implements OrderDrugService {
	@Autowired
	OrderItemListMapper orderItemListMapper;
	@Autowired
	OrderDrugMapper orderDrugMapper;
	@Autowired
	DrugService drugService;
	@Autowired
	OrderItemChargeMapper orderItemChargeMapper;
	@Autowired
	HospitalOrderMapper hospitalOrderMapper;
	@Autowired
	DrugTradeService drugTradeService;
	@Autowired
	DrugInventoryService drugInventoryService;

	@Override
	public List<OrderItemList<OrderDrug>> createOrderDrugsByPatentPrescriptionList(String orderNo, List<List<PatientDrug>> patentPrescriptionList) {
		if (orderNo == null || CollectionUtils.isEmpty(patentPrescriptionList)) {
			return null;
		}

		List<OrderItemList<OrderDrug>> orderPrescriptions = Lists.newArrayList();
		patentPrescriptionList.stream().filter(p -> CollectionUtils.isNotEmpty(p)).forEach(prescription -> {
			OrderItemList<OrderDrug> orderPrescription = new OrderItemList<OrderDrug>();
			orderPrescription.setChargeStatus(ChargeStatus.TOCHARGE);
			orderPrescription.setOrderNo(orderNo);
			orderPrescription.setType(OrderItemListType.PATIENT_PRESCRIPTION.getType());
			// 设置处方编号
				PatientDrug firstDrug = prescription.get(0);
				orderPrescription.setForeignId(firstDrug == null ? null : firstDrug.getPrescription());
				// 保存订单处方
				orderPrescriptions.add(orderPrescription);
				orderPrescription.setRecordId(firstDrug.getRecordId());
				orderItemListMapper.insert(orderPrescription);
				Double orderPrescriptionReceivableAmt = 0d;
				// 保存订单药物
				List<OrderItemCharge> orderItemCharges = new ArrayList<OrderItemCharge>();
				for (PatientDrug patientDrug : prescription) {
					// 只处方中诊所的药物
					OrderDrug orderDrug = new OrderDrug();
					Integer dosage = patientDrug.getTotalDosage().intValue();
					orderDrug.setDosage(dosage);
					orderDrug.setDrugId(patientDrug.getDrugId());
					if (patientDrug.getDataSource() != null) {
						orderDrug.setDataSource(patientDrug.getDataSource().intValue());
					}
					orderDrug.setDrugName(patientDrug.getDrugName());
					orderDrug.setSaleChannel(SaleChannel.Prescription.getType());
					orderDrug.setOrderItemListId(orderPrescription.getId());
					orderDrug.setOrderNo(orderNo);
					orderDrug.setHospitalId(SessionUtils.getHospitalId());
					orderDrug.setRecordId(patientDrug.getRecordId());
					orderDrug.setSalePrice(patientDrug.getUnivalence());
					orderDrug.setSaleUnit(patientDrug.getTotalDosageUnit());
					// if (patientDrug.getDataSource() != null &&
					// patientDrug.getDataSource() == 0) {
					//
					// // 如果是诊所管理的药，保存进货价和利润
					//
					// Double purchasePrice = null;
					// // Double purchasePrice =
					// // this.getPurchasePrice(patientDrug.getDrugId(),
					// // patientDrug.getTotalDosageUnit(),
					// // SaleChannel.Prescription);
					// orderDrug.setPurchasePrice(purchasePrice);
					// Double profit = this.getProfit(purchasePrice,
					// patientDrug.getUnivalence());
					// orderDrug.setProfit(profit);
					// }

					Double receivableAmt = 0D;
					if (orderDrug.getSalePrice() != null && orderDrug.getDosage() != null) {
						receivableAmt = AmtUtils.multiply(orderDrug.getSalePrice(), dosage);
						// receivableAmt = orderDrug.getSalePrice() *
						// dosage;
					}
					if (patientDrug.getDataSource() != null) {
						if (patientDrug.getDataSource() == 0) {
							// 诊所的药
							HospitalDrug hospitalDrug = drugService.getDrugById(patientDrug.getDrugId());
							if (hospitalDrug != null) {
								orderDrug.setManufacturer(hospitalDrug.getManufacturer());
								orderDrug.setSpecification(hospitalDrug.getSpecification());
								orderDrug.setDrugType(hospitalDrug.getDrugType());
							}
						} else if (patientDrug.getDataSource() == 1) {
							// 惠每的药
							DrugTrade drugTrade = drugTradeService.getDrugById(patientDrug.getDrugId());
							if (drugTrade != null) {
								orderDrug.setManufacturer(drugTrade.getManufacturer());
								orderDrug.setSpecification(drugTrade.getSpecification());
								orderDrug.setDrugType(drugTrade.getDrugType());
							}
						}
					}
					orderDrugMapper.insert(orderDrug);
					OrderItemCharge orderItemCharge = new OrderItemCharge();
					orderItemCharge.setChargeStatus(ChargeStatus.TOCHARGE);
					orderItemCharge.setCreater(SessionUtils.getDoctorId());
					orderItemCharge.setItemId(orderDrug.getId());
					orderItemCharge.setOrderNo(orderNo);
					orderItemCharge.setRecordId(orderDrug.getRecordId());
					orderItemCharge.setItemListId(orderPrescription.getId());
					orderItemCharge.setModifier(SessionUtils.getDoctorId());
					orderItemCharge.setReceivableAmt(receivableAmt);
					orderItemCharge.setItemType(OrderItemType.DRUG.getType());
					orderItemCharges.add(orderItemCharge);
					if (orderItemCharge.getReceivableAmt() != null) {
						// orderPrescriptionReceivableAmt +=
						// orderItemCharge.getReceivableAmt();
						orderPrescriptionReceivableAmt = AmtUtils.add(orderPrescriptionReceivableAmt, orderItemCharge.getReceivableAmt());
					}
				}
				orderItemChargeMapper.insertList(orderItemCharges);
				// 设置处方的应收金额
				orderPrescription.setReceivableAmt(orderPrescriptionReceivableAmt);
				orderItemListMapper.update(orderPrescription);
			});
		return orderPrescriptions;
	}

	@Override
	public List<OrderItemList<OrderDrug>> firstCreateOrderDrugsByPatentPrescriptionList(String orderNo, List<PatientDrug> patentPrescriptionList) {
		if (orderNo == null || CollectionUtils.isEmpty(patentPrescriptionList)) {
			return null;
		}
		List<OrderItemList<OrderDrug>> orderPrescriptions = Lists.newArrayList();
		OrderItemList<OrderDrug> orderPrescription = new OrderItemList<OrderDrug>();
		orderPrescription.setChargeStatus(ChargeStatus.TOCHARGE);
		orderPrescription.setOrderNo(orderNo);
		orderPrescription.setType(OrderItemListType.PATIENT_PRESCRIPTION.getType());
		// 设置处方编号
		PatientDrug firstDrug = patentPrescriptionList.get(0);
		orderPrescription.setForeignId(firstDrug == null ? null : firstDrug.getPrescription());
		// 保存订单处方
		orderPrescriptions.add(orderPrescription);
		orderPrescription.setRecordId(firstDrug.getRecordId());
		orderItemListMapper.insert(orderPrescription);
		final Double[] orderPrescriptionReceivableAmt = {0d};

		//查询药品信息
		List<Long> hospDrugIds = patentPrescriptionList.stream().filter(patientDrug1 -> patientDrug1.getDataSource() != null && patientDrug1.getDataSource() == 0).map(PatientDrug::getDrugId ).collect(Collectors.toList());
		List<Long> drugTradeIds = patentPrescriptionList.stream().filter(patientDrug1 -> patientDrug1.getDataSource() != null && patientDrug1.getDataSource() == 1).map(PatientDrug::getDrugId ).collect(Collectors.toList());
		Map<Long,HospitalDrug> hospitalDrugMap = drugService.searchDrugByDrugIds(hospDrugIds);
		Map<Long,DrugTrade> drugTradeMap = drugTradeService.searchDrugByIds(drugTradeIds);

		List<OrderDrug>  orderDrugs = Lists.newArrayList();
		patentPrescriptionList.stream().forEach(patientDrug -> {
			// 只处方中诊所的药物
			OrderDrug orderDrug = new OrderDrug();
			Integer dosage = patientDrug.getTotalDosage().intValue();
			orderDrug.setDosage(dosage);
			orderDrug.setDrugId(patientDrug.getDrugId());
			if (patientDrug.getDataSource() != null) {
				orderDrug.setDataSource(patientDrug.getDataSource().intValue());
			}
			orderDrug.setDrugName(patientDrug.getDrugName());
			orderDrug.setSaleChannel(SaleChannel.Prescription.getType());
			orderDrug.setOrderItemListId(orderPrescription.getId());
			orderDrug.setOrderNo(orderNo);
			orderDrug.setHospitalId(SessionUtils.getHospitalId());
			orderDrug.setRecordId(patientDrug.getRecordId());
			orderDrug.setSalePrice(patientDrug.getUnivalence());
			orderDrug.setSaleUnit(patientDrug.getTotalDosageUnit());


			Double receivableAmt = 0D;
			if (orderDrug.getSalePrice() != null && orderDrug.getDosage() != null) {
				receivableAmt = AmtUtils.multiply(orderDrug.getSalePrice(), dosage);
			}
			orderDrug.setReceivableAmt(receivableAmt);
			if (patientDrug.getDataSource() != null) {
				if (patientDrug.getDataSource() == 0) {
					// 诊所的药
					HospitalDrug hospitalDrug = hospitalDrugMap.get(patientDrug.getDrugId());
					if (hospitalDrug != null) {
						orderDrug.setManufacturer(hospitalDrug.getManufacturer());
						orderDrug.setSpecification(hospitalDrug.getSpecification());
						orderDrug.setDrugType(hospitalDrug.getDrugType());
					}
				} else if (patientDrug.getDataSource() == 1) {
					// 惠每的药
					DrugTrade drugTrade = drugTradeMap.get(patientDrug.getDrugId());
					if (drugTrade != null) {
						orderDrug.setManufacturer(drugTrade.getManufacturer());
						orderDrug.setSpecification(drugTrade.getSpecification());
						orderDrug.setDrugType(drugTrade.getDrugType());
					}
				}
			}
			orderDrugs.add(orderDrug);
		});

		orderDrugMapper.insertList(orderDrugs);

		// 保存订单药物
		List<OrderItemCharge> orderItemCharges = new ArrayList<OrderItemCharge>();
		orderDrugs.stream().forEach(orderDrug -> {
			OrderItemCharge orderItemCharge = new OrderItemCharge();
			orderItemCharge.setChargeStatus(ChargeStatus.TOCHARGE);
			orderItemCharge.setCreater(SessionUtils.getDoctorId());
			orderItemCharge.setItemId(orderDrug.getId());
			orderItemCharge.setOrderNo(orderNo);
			orderItemCharge.setRecordId(orderDrug.getRecordId());
			orderItemCharge.setItemListId(orderPrescription.getId());
			orderItemCharge.setModifier(SessionUtils.getDoctorId());
			orderItemCharge.setReceivableAmt(orderDrug.getReceivableAmt());
			orderItemCharge.setItemType(OrderItemType.DRUG.getType());
			orderItemCharges.add(orderItemCharge);
			if (orderItemCharge.getReceivableAmt() != null) {
				orderPrescriptionReceivableAmt[0] = AmtUtils.add(orderPrescriptionReceivableAmt[0], orderItemCharge.getReceivableAmt());
			}
		});
		orderItemChargeMapper.insertList(orderItemCharges);
		// 设置处方的应收金额
		orderPrescription.setReceivableAmt(orderPrescriptionReceivableAmt[0]);
		orderItemListMapper.update(orderPrescription);

		return orderPrescriptions;
	}

	@Override
	public List<OrderDrug> getOrderDrugsByOrderItemListIdAndChargeStatus(Long orderItemListId, Integer chargeStatus) {
		List<OrderDrug> orderDrugs = orderDrugMapper.selectByOrderItemListIdAndChargeStatus(orderItemListId, chargeStatus);
		return orderDrugs;
	}

	@Override
	public List<OrderItemList<OrderDrug>> createOrderDrugsByChinesePrescriptionList(String orderNo, List<PatientChineseDrug> chinesePrescriptionList) {
		if (orderNo == null || CollectionUtils.isEmpty(chinesePrescriptionList)) {
			return null;
		}
		List<OrderItemList<OrderDrug>> orderPrescriptions = Lists.newArrayList();
		chinesePrescriptionList.stream().forEach(patientChineseDrug -> {
			OrderItemList<OrderDrug> orderPrescription = new OrderItemList<OrderDrug>();
			orderPrescription.setChargeStatus(ChargeStatus.TOCHARGE);
			orderPrescription.setOrderNo(orderNo);
			orderPrescription.setRecordId(patientChineseDrug.getRecordId());
			orderPrescription.setType(OrderItemListType.CHINESE_PRESCRIPTION.getType());
			// 设置处方编号
				orderPrescription.setForeignId(patientChineseDrug.getPrescription());
				// 保存订单处方
				orderPrescriptions.add(orderPrescription);
				orderItemListMapper.insert(orderPrescription);
				// 保存订单药物
				Double orderPrescriptionReceivableAmt = 0d;
				List<PatientChineseDrugPieces> decoctionPiecesList = patientChineseDrug.getDecoctionPiecesList();
				if (CollectionUtils.isNotEmpty(decoctionPiecesList)) {
					List<OrderItemCharge> orderItemCharges = new ArrayList<OrderItemCharge>();
					for (PatientChineseDrugPieces patientChineseDrugPiece : decoctionPiecesList) {
						// 只处方中诊所的药物
						OrderDrug orderDrug = new OrderDrug();
						Double totalGram = 0D;
						Integer totalDosage = patientChineseDrug.getTotalDosage() == null ? null : patientChineseDrug.getTotalDosage().intValue();
						Double gramCount = patientChineseDrugPiece.getValue() == null ? null : patientChineseDrugPiece.getValue();
						if (totalDosage != null && gramCount != null) {
							totalGram = AmtUtils.multiply(gramCount, totalDosage);
						}
						orderDrug.setDosage(totalDosage);
						orderDrug.setGramCount(gramCount);

						if (patientChineseDrugPiece.getDataSource() != null) {
							orderDrug.setDataSource(patientChineseDrugPiece.getDataSource().intValue());
						}
						orderDrug.setDrugId(patientChineseDrugPiece.getDrugId());
						orderDrug.setDrugName(patientChineseDrugPiece.getDrugName());
						orderDrug.setSaleChannel(SaleChannel.Prescription.getType());
						orderDrug.setOrderItemListId(orderPrescription.getId());
						orderDrug.setOrderNo(orderNo);
						orderDrug.setHospitalId(SessionUtils.getHospitalId());
						orderDrug.setRecordId(patientChineseDrug.getRecordId());
						orderDrug.setSalePrice(patientChineseDrugPiece.getUnivalence());
						// 饮片默认单位是g
						String saleUnit = StringUtils.isEmpty(patientChineseDrugPiece.getUnit()) ? "g" : patientChineseDrugPiece.getUnit();
						orderDrug.setSaleUnit(saleUnit);
						// if (patientChineseDrugPiece.getDataSource() != null
						// && patientChineseDrugPiece.getDataSource() == 0) {
						// // 如果是诊所管理的药，保存进货价和利润
						// Double purchasePrice =
						// this.getPurchasePrice(patientChineseDrugPiece.getDrugId(),
						// saleUnit, SaleChannel.Prescription);
						// orderDrug.setPurchasePrice(purchasePrice);
						// Double profit = this.getProfit(purchasePrice,
						// patientChineseDrugPiece.getUnivalence());
						// orderDrug.setProfit(profit);
						// }
						Double receivableAmt = 0D;
						if (patientChineseDrugPiece.getUnivalence() != null && totalGram != 0) {
							// receivableAmt =
							// orderDrug.getSalePrice() *
							// totalGram;
							receivableAmt = AmtUtils.multiply(orderDrug.getSalePrice(), totalGram);
						}
						if (patientChineseDrugPiece.getDataSource() != null) {
							if (patientChineseDrugPiece.getDataSource() == 0) {
								// 诊所的药
								HospitalDrug hospitalDrug = drugService.getDrugById(patientChineseDrugPiece.getDrugId());
								if (hospitalDrug != null) {
									orderDrug.setManufacturer(hospitalDrug.getManufacturer());
									orderDrug.setSpecification(hospitalDrug.getSpecification());
									orderDrug.setDrugType(hospitalDrug.getDrugType());
								}
							} else if (patientChineseDrugPiece.getDataSource() == 1) {
								// 惠每的药
								DrugTrade drugTrade = drugTradeService.getDrugById(patientChineseDrugPiece.getDrugId());
								if (drugTrade != null) {
									orderDrug.setManufacturer(drugTrade.getManufacturer());
									orderDrug.setSpecification(drugTrade.getSpecification());
									orderDrug.setDrugType(drugTrade.getDrugType());
								}
							}
						}
						orderDrugMapper.insert(orderDrug);

						OrderItemCharge orderItemCharge = new OrderItemCharge();
						orderItemCharge.setChargeStatus(ChargeStatus.TOCHARGE);
						orderItemCharge.setCreater(SessionUtils.getDoctorId());
						orderItemCharge.setItemId(orderDrug.getId());
						orderItemCharge.setOrderNo(orderNo);
						orderItemCharge.setRecordId(orderDrug.getRecordId());
						orderItemCharge.setItemListId(orderPrescription.getId());
						orderItemCharge.setModifier(SessionUtils.getDoctorId());
						orderItemCharge.setReceivableAmt(receivableAmt);
						orderItemCharge.setItemType(OrderItemType.DRUG.getType());
						orderItemCharges.add(orderItemCharge);

						if (orderItemCharge.getReceivableAmt() != null) {
							// orderPrescriptionReceivableAmt +=
							// orderItemCharge.getReceivableAmt();
							orderPrescriptionReceivableAmt = AmtUtils.add(orderPrescriptionReceivableAmt, orderItemCharge.getReceivableAmt());
						}
					}
					orderItemChargeMapper.insertList(orderItemCharges);
					// 设置处方的应收金额
					orderPrescription.setReceivableAmt(orderPrescriptionReceivableAmt);
					orderItemListMapper.update(orderPrescription);
				}
			});
		return orderPrescriptions;
	}

	@Override
	public List<OrderItemList<OrderDrug>> firstCreateOrderDrugsByChinesePrescriptionList(String orderNo, List<PatientChineseDrug> chinesePrescriptionList) {
		if (orderNo == null || CollectionUtils.isEmpty(chinesePrescriptionList)) {
			return null;
		}
		List<OrderItemList<OrderDrug>> orderPrescriptions = Lists.newArrayList();
		chinesePrescriptionList.stream().forEach(patientChineseDrug -> {
			OrderItemList<OrderDrug> orderPrescription = new OrderItemList<OrderDrug>();
			orderPrescription.setChargeStatus(ChargeStatus.TOCHARGE);
			orderPrescription.setOrderNo(orderNo);
			orderPrescription.setRecordId(patientChineseDrug.getRecordId());
			orderPrescription.setType(OrderItemListType.CHINESE_PRESCRIPTION.getType());
			// 设置处方编号
			orderPrescription.setForeignId(patientChineseDrug.getPrescription());
			// 保存订单处方
			orderPrescriptions.add(orderPrescription);
			orderItemListMapper.insert(orderPrescription);

			List<PatientChineseDrugPieces> decoctionPiecesList = patientChineseDrug.getDecoctionPiecesList();
			if (CollectionUtils.isNotEmpty(decoctionPiecesList)) {
				List<OrderItemCharge> orderItemCharges = new ArrayList<OrderItemCharge>();

				//查询药品信息
				List<Long> hospDrugIds = decoctionPiecesList.stream().filter(patientDrug1 -> patientDrug1.getDataSource() != null && patientDrug1.getDataSource() == 0).map(PatientChineseDrugPieces::getDrugId ).collect(Collectors.toList());
				List<Long> drugTradeIds = decoctionPiecesList.stream().filter(patientDrug1 -> patientDrug1.getDataSource() != null && patientDrug1.getDataSource() == 1).map(PatientChineseDrugPieces::getDrugId ).collect(Collectors.toList());
				Map<Long,HospitalDrug> hospitalDrugMap = drugService.searchDrugByDrugIds(hospDrugIds);
				Map<Long,DrugTrade> drugTradeMap = drugTradeService.searchDrugByIds(drugTradeIds);

				List<OrderDrug>  orderDrugs = Lists.newArrayList();
				for (PatientChineseDrugPieces patientChineseDrugPiece : decoctionPiecesList) {
					// 只处方中诊所的药物
					OrderDrug orderDrug = new OrderDrug();
					Double totalGram = 0D;
					Integer totalDosage = patientChineseDrug.getTotalDosage() == null ? null : patientChineseDrug.getTotalDosage().intValue();
					Double gramCount = patientChineseDrugPiece.getValue() == null ? null : patientChineseDrugPiece.getValue();
					if (totalDosage != null && gramCount != null) {
						totalGram = AmtUtils.multiply(gramCount, totalDosage);
					}
					orderDrug.setDosage(totalDosage);
					orderDrug.setGramCount(gramCount);

					if (patientChineseDrugPiece.getDataSource() != null) {
						orderDrug.setDataSource(patientChineseDrugPiece.getDataSource().intValue());
					}
					orderDrug.setDrugId(patientChineseDrugPiece.getDrugId());
					orderDrug.setDrugName(patientChineseDrugPiece.getDrugName());
					orderDrug.setSaleChannel(SaleChannel.Prescription.getType());
					orderDrug.setOrderItemListId(orderPrescription.getId());
					orderDrug.setOrderNo(orderNo);
					orderDrug.setHospitalId(SessionUtils.getHospitalId());
					orderDrug.setRecordId(patientChineseDrug.getRecordId());
					orderDrug.setSalePrice(patientChineseDrugPiece.getUnivalence());
					// 饮片默认单位是g
					String saleUnit = StringUtils.isEmpty(patientChineseDrugPiece.getUnit()) ? "g" : patientChineseDrugPiece.getUnit();
					orderDrug.setSaleUnit(saleUnit);

					Double receivableAmt = 0D;
					if (patientChineseDrugPiece.getUnivalence() != null && totalGram != 0) {

						receivableAmt = AmtUtils.multiply(orderDrug.getSalePrice(), totalGram);
					}

					orderDrug.setReceivableAmt(receivableAmt);

					if (patientChineseDrugPiece.getDataSource() != null) {
						if (patientChineseDrugPiece.getDataSource() == 0) {
							// 诊所的药
							HospitalDrug hospitalDrug = hospitalDrugMap.get(patientChineseDrugPiece.getDrugId());
							if (hospitalDrug != null) {
								orderDrug.setManufacturer(hospitalDrug.getManufacturer());
								orderDrug.setSpecification(hospitalDrug.getSpecification());
								orderDrug.setDrugType(hospitalDrug.getDrugType());
							}
						} else if (patientChineseDrugPiece.getDataSource() == 1) {
							// 惠每的药
							DrugTrade drugTrade = drugTradeMap.get(patientChineseDrugPiece.getDrugId());
							if (drugTrade != null) {
								orderDrug.setManufacturer(drugTrade.getManufacturer());
								orderDrug.setSpecification(drugTrade.getSpecification());
								orderDrug.setDrugType(drugTrade.getDrugType());
							}
						}
					}
					orderDrugs.add(orderDrug);
				}
				orderDrugMapper.insertList(orderDrugs);
				// 保存订单药物
				final Double[] orderPrescriptionReceivableAmt = {0d};
				orderDrugs.stream().forEach(orderDrug -> {
					OrderItemCharge orderItemCharge = new OrderItemCharge();
					orderItemCharge.setChargeStatus(ChargeStatus.TOCHARGE);
					orderItemCharge.setCreater(SessionUtils.getDoctorId());
					orderItemCharge.setItemId(orderDrug.getId());
					orderItemCharge.setOrderNo(orderNo);
					orderItemCharge.setRecordId(orderDrug.getRecordId());
					orderItemCharge.setItemListId(orderPrescription.getId());
					orderItemCharge.setModifier(SessionUtils.getDoctorId());
					orderItemCharge.setReceivableAmt(orderDrug.getReceivableAmt());
					orderItemCharge.setItemType(OrderItemType.DRUG.getType());
					orderItemCharges.add(orderItemCharge);

					if (orderItemCharge.getReceivableAmt() != null) {

						orderPrescriptionReceivableAmt[0] = AmtUtils.add(orderPrescriptionReceivableAmt[0], orderItemCharge.getReceivableAmt());
					}
				});

				orderItemChargeMapper.insertList(orderItemCharges);
				// 设置处方的应收金额
				orderPrescription.setReceivableAmt(orderPrescriptionReceivableAmt[0]);
				orderItemListMapper.update(orderPrescription);
			}
		});
		return orderPrescriptions;
	}


	@Override
	public List<OrderItemList<OrderDrug>> createOrderDrugsByPatentPrescriptionList(Long recordId, List<List<PatientDrug>> patentPrescriptionList) {
		if (recordId == null || CollectionUtils.isEmpty(patentPrescriptionList)) {
			return null;
		}
		HospitalOrder hospitalOrder = hospitalOrderMapper.selectByRecordId(recordId);
		if (hospitalOrder == null) {
			return null;
		}
		String orderNo = hospitalOrder.getOrderNo();
		return this.createOrderDrugsByPatentPrescriptionList(orderNo, patentPrescriptionList);
	}

	@Override
	public void deleteOrderDrugsByOrderItemListId(Long orderItemListId) {
		orderDrugMapper.deleteByOrderItemListId(orderItemListId);
		orderItemChargeMapper.deleteByItemListId(orderItemListId);
	}

	@Override
	public List<OrderItemList<OrderDrug>> createOrderDrugsByChinesePrescriptionList(Long recordId, List<PatientChineseDrug> chinesePrescriptionList) {
		if (recordId == null || CollectionUtils.isEmpty(chinesePrescriptionList)) {
			return null;
		}
		HospitalOrder hospitalOrder = hospitalOrderMapper.selectByRecordId(recordId);
		if (hospitalOrder == null) {
			return null;
		}
		String orderNo = hospitalOrder.getOrderNo();

		return this.createOrderDrugsByChinesePrescriptionList(orderNo, chinesePrescriptionList);
	}

	/**
	 *  获取进货价 
	 *  0616修改为减库存时，设置进货价
	 * @date 2016年3月28日
	 * @author lipeng
	 * @param drugId
	 * @param saleUnit
	 * @return
	 */
	@Deprecated
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
	@Deprecated
	private Double getProfit(Double purchasePrice, Double salePrice) {
		if (purchasePrice == null || salePrice == null) {

			return 0D;
		}
		return AmtUtils.subtract(salePrice, purchasePrice);
	}

	@Override
	public List<String> searchOrderDrugNames(NameRequst req) {
		req.setCurrentPage(1);
		req.setPageSize(5);
		req.setHospitalId(SessionUtils.getHospitalId());
		return orderDrugMapper.searchOrderDrugNames(req);
	}

	@Override
	public boolean hasOrderByDrugIdAndDataSource(Long drugId, Integer dataSource) {
		Integer count = orderDrugMapper.selectCountByDrugIdAndDataSource(drugId, dataSource);
		return count != null && count > 0 ? true : false;
	}

	@Override
	public void cutInventory(List<OrderDrug> orderDrugs) {
		if (CollectionUtils.isEmpty(orderDrugs)) {
			return;
		}
		orderDrugs
				.stream()
				.filter(orderDrug -> orderDrug.getDrugId() != null && orderDrug.getDataSource() != null && orderDrug.getDataSource() == 0)
				.forEach(orderDrug -> {
					// 如果药物来自诊所的话，进行库存操作
						Double amount = null;
						HospitalOrder hospitalOrder = hospitalOrderMapper.selectByOrderNo(orderDrug.getOrderNo());
						if (HospitalOrder.TYPE_PATIENT_RECORD == hospitalOrder.getOrderType()
								&& DrugTypeEnum.herbal.getDrugType() == orderDrug.getDrugType()) {
							// 如果来自病历并且药物时饮片类型的话，操作总克数
							amount = orderDrug.getTotalGram();
						} else {
							amount = orderDrug.getDosage() == null ? null : orderDrug.getDosage().doubleValue();
						}
						if (amount != null) {
							InventoryOperatePojo pojo = new InventoryOperatePojo();
							pojo.setDrugId(orderDrug.getDrugId());
							pojo.setAmount(amount);
							pojo.setHospitalId(SessionUtils.getHospitalId());
							pojo.setOrderDrugId(orderDrug.getId());
							pojo.setUnit(orderDrug.getSaleUnit());
							// 减库存
							InventoryOperatePojo reInventoryOperatePojo = drugInventoryService.cutHospitalDrugInventory(pojo);

							// 设置进货价
							if (reInventoryOperatePojo != null && reInventoryOperatePojo.getTotalPurchasePrice() != null) {
								Double purchasePrice = reInventoryOperatePojo.getTotalPurchasePrice();
								orderDrug.setPurchasePrice(purchasePrice);
								orderDrugMapper.update(orderDrug);
							}
						}
					});
	}

	@Override
	public void returnInventory(List<OrderDrug> orderDrugs) {
		if (CollectionUtils.isEmpty(orderDrugs)) {
			return;
		}
		orderDrugs
				.stream()
				.filter(orderDrug -> orderDrug.getDrugId() != null && orderDrug.getDataSource() != null && orderDrug.getDataSource() == 0)
				.forEach(orderDrug -> {
					// 如果药物来自诊所的话，进行库存操作
						Double amount = null;
						HospitalOrder hospitalOrder = hospitalOrderMapper.selectByOrderNo(orderDrug.getOrderNo());
						if (HospitalOrder.TYPE_PATIENT_RECORD == hospitalOrder.getOrderType()
								&& DrugTypeEnum.herbal.getDrugType() == orderDrug.getDrugType()) {
							// 如果来自病历并且药物时饮片类型的话，操作总克数
							amount = orderDrug.getTotalGram();
						} else {
							amount = orderDrug.getDosage() == null ? null : orderDrug.getDosage().doubleValue();
						}
						if (amount != null) {
							InventoryOperatePojo inventoryOperatePojo = new InventoryOperatePojo();
							inventoryOperatePojo.setDrugId(orderDrug.getDrugId());
							inventoryOperatePojo.setAmount(amount);
							inventoryOperatePojo.setHospitalId(SessionUtils.getHospitalId());
							inventoryOperatePojo.setUnit(orderDrug.getSaleUnit());
							inventoryOperatePojo.setOrderDrugId(orderDrug.getId());
							drugInventoryService.returnHospitalDrugInventory(inventoryOperatePojo);
						}
					});
	}

}
