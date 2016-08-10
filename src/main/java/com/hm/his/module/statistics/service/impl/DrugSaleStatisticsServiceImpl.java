package com.hm.his.module.statistics.service.impl;

import com.google.common.collect.Lists;
import com.hm.his.framework.utils.AmtUtils;
import com.hm.his.module.drug.dao.HospitalDrugMapper;
import com.hm.his.module.drug.model.DrugTypeEnum;
import com.hm.his.module.drug.model.HospitalDrug;
import com.hm.his.module.order.dao.OrderDrugMapper;
import com.hm.his.module.order.model.OrderDrug;
import com.hm.his.module.order.pojo.SaleChannel;
import com.hm.his.module.statistics.pojo.DrugSaleStatisticsItemPojo;
import com.hm.his.module.statistics.pojo.DrugSaleStatisticsPojo;
import com.hm.his.module.statistics.pojo.StatisticsRequest;
import com.hm.his.module.statistics.pojo.WorkloadStatisticsItemPojo;
import com.hm.his.module.statistics.service.DrugSaleStatisticsService;
import com.hm.his.module.user.service.DoctorService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DrugSaleStatisticsServiceImpl implements DrugSaleStatisticsService {
    @Autowired(required = false)
    OrderDrugMapper orderDrugMapper;
    @Autowired(required = false)
    HospitalDrugMapper hospitalDrugMapper;
    @Autowired
    DoctorService doctorService;

    //	public DrugSaleStatisticsPojo drugSaleStatistics1(StatisticsRequest req) {
    //		StatisticsRequest.handleStatisticsRequest(req);
    //		DrugSaleStatisticsPojo re = new DrugSaleStatisticsPojo();
    //		// 1.按条件查询满足的药物，条件：收费日期，药名，药物类型，销售渠道得到drugs
    //		List<OrderDrug> orderDrugs = orderDrugMapper.selectByStatisticsRequest(req);
    //		if (CollectionUtils.isEmpty(orderDrugs)) {
    //			return null;
    //		}
    //		// 2.按照dataSource分组，如果datasource=0 来自诊所，按 drugId统计，否则按 药物名+厂家+规格统计
    //		Map<Integer, List<OrderDrug>> orderDrugMap = orderDrugs.stream().collect(Collectors.groupingBy(orderDrug -> {
    //			// 根据dataSource分组，如果dataSource为null时，默认为手动录入的即5，（但是为空的情况不应该出现！）
    //			return orderDrug.getDataSource() == null ? 5 : orderDrug.getDataSource();
    //		}));
    //		// 全部诊所药
    //		List<OrderDrug> hospitalOrderDrugs = orderDrugMap.get(0);
    //		List<DrugSaleStatisticsItemPojo> hospitalDrugSaleStatisticsItemPojos = null;
    //		if (CollectionUtils.isNotEmpty(hospitalOrderDrugs)) {
    //			hospitalDrugSaleStatisticsItemPojos = Lists.newArrayList();
    //			// map:key= 药物id+销售渠道，value=对应的药物集合
    //			Map<String, List<OrderDrug>> drugMap = hospitalOrderDrugs.stream()
    //					.collect(Collectors.groupingBy(orderDrug -> {
    //						return orderDrug.getSaleChannel() + "" + orderDrug.getDrugId();
    //					}));
    //			Collection<List<OrderDrug>> orderDrugLists = drugMap.values();
    //			for (List<OrderDrug> orderDrugList : orderDrugLists) {
    //				DrugSaleStatisticsItemPojo itemPojo = this.createDrugSaleStatisticsItemPojo(orderDrugList);
    //				if (itemPojo != null) {
    //					hospitalDrugSaleStatisticsItemPojos.add(itemPojo);
    //				}
    //			}
    //		}
    //
    //		// 除诊所药外的其他药
    //		List<OrderDrug> otherOrderDrugs = orderDrugMap.values().stream()
    //				.flatMap(orderDrugList -> orderDrugList.stream()).filter(orderDrug -> {
    //					return orderDrug.getDataSource() != null && orderDrug.getDataSource() != 0;
    //				}).collect(Collectors.toList());
    //		List<DrugSaleStatisticsItemPojo> otherDrugSaleStatisticsItemPojos = null;
    //		if (CollectionUtils.isNotEmpty(otherOrderDrugs)) {
    //			otherDrugSaleStatisticsItemPojos = Lists.newArrayList();
    //			Map<String, List<OrderDrug>> drugMap = otherOrderDrugs.stream().collect(Collectors.groupingBy(orderDrug -> {
    //				return orderDrug.getSaleChannel() + "" + orderDrug.getDrugIdentify();
    //			}));
    //			Collection<List<OrderDrug>> orderDrugLists = drugMap.values();
    //			for (List<OrderDrug> orderDrugList : orderDrugLists) {
    //				DrugSaleStatisticsItemPojo itemPojo = this.createDrugSaleStatisticsItemPojo(orderDrugList);
    //				if (itemPojo != null) {
    //					otherDrugSaleStatisticsItemPojos.add(itemPojo);
    //				}
    //			}
    //		}
    //		// 合并诊所药物和其他药物
    //		List<DrugSaleStatisticsItemPojo> allDrugs = Lists.newArrayList();
    //		if (CollectionUtils.isNotEmpty(hospitalDrugSaleStatisticsItemPojos)) {
    //			allDrugs.addAll(hospitalDrugSaleStatisticsItemPojos);
    //		}
    //		if (CollectionUtils.isNotEmpty(otherDrugSaleStatisticsItemPojos)) {
    //			allDrugs.addAll(otherDrugSaleStatisticsItemPojos);
    //		}
    //		// .计算出总记录数
    //		Integer totalCount = allDrugs.size();
    //		req.setTotalCount(totalCount);
    //		re.setTotalPage(req.getTotalPage());
    //		// 排序
    //		List<DrugSaleStatisticsItemPojo> drugs = allDrugs.stream().sorted((drug1, drug2) -> {
    //			Double drug1SalePrice = drug1.getSaleAmt() == null ? 0D : drug1.getSaleAmt();
    //			Double drug2SalePrice = drug2.getSaleAmt() == null ? 0D : drug2.getSaleAmt();
    //			return drug2SalePrice.compareTo(drug1SalePrice);
    //		}).collect(Collectors.toList());
    //		// 分页
    //		if (req.getStartRecord() != null & req.getPageSize() != null) {
    //			drugs = drugs.stream().skip(req.getStartRecord()).limit(req.getPageSize()).collect(Collectors.toList());
    //		}
    //		re.setDrugs(drugs);
    //
    //		Double totalProfit = allDrugs.stream().mapToDouble(DrugSaleStatisticsItemPojo::getProfit).sum();
    //		re.setTotalProfit(totalProfit);
    //		Double totalSaleAmt = allDrugs.stream().mapToDouble(DrugSaleStatisticsItemPojo::getSaleAmt).sum();
    //		re.setTotalSaleAmt(totalSaleAmt);
    //		return re;
    //	}

    @Override
    public DrugSaleStatisticsPojo drugSaleStatistics(StatisticsRequest req) {
        StatisticsRequest.handleStatisticsRequest(req);
        DrugSaleStatisticsPojo re = new DrugSaleStatisticsPojo();
        // 1.按条件查询满足的药物，条件：收费日期，药名，药物类型，销售渠道得到drugs
        List<OrderDrug> orderDrugs = orderDrugMapper.selectByStatisticsRequest(req);
        if (CollectionUtils.isEmpty(orderDrugs)) {
            return null;
        }
        List<Long> createrIds = orderDrugs.stream().map(OrderDrug::getCreater).collect(Collectors.toList());
        Map<Long, String> doctorMap = doctorService.getDoctorRealNames(createrIds);

        // 2.按照dataSource分组，如果datasource=0 来自诊所，按 drugId统计，否则按 药物名+厂家+规格统计
        Map<Integer, List<OrderDrug>> orderDrugMap = orderDrugs.stream().collect(Collectors.groupingBy(orderDrug -> {
            // 根据dataSource分组，如果dataSource为null时，默认为手动录入的即5，（但是为空的情况不应该出现！）
            return orderDrug.getDataSource() == null ? 5 : orderDrug.getDataSource();
        }));
        // 全部诊所药
        List<OrderDrug> hospitalOrderDrugs = orderDrugMap.get(0);
        List<DrugSaleStatisticsItemPojo> hospitalDrugSaleStatisticsItemPojos = null;
        if (CollectionUtils.isNotEmpty(hospitalOrderDrugs)) {
            List<Long> hospitalDrugIds = hospitalOrderDrugs.stream().map(OrderDrug::getDrugId)
                    .collect(Collectors.toList());
            Map<Long, HospitalDrug> hospitalDrugMap = new HashMap<>();
            List<HospitalDrug> hospitalDrugs = hospitalDrugMapper.searchDrugByDrugIds(hospitalDrugIds);
            for (HospitalDrug hospitalDrug : hospitalDrugs) {
                hospitalDrugMap.put(hospitalDrug.getId(), hospitalDrug);
            }
            hospitalDrugSaleStatisticsItemPojos = Lists.newArrayList();
            // map:key= 药物id+销售渠道，value=对应的药物集合
            Map<String, List<OrderDrug>> drugMap = hospitalOrderDrugs.stream()
                    .collect(Collectors.groupingBy(orderDrug -> {
                        return orderDrug.getSaleChannel() + "_" + orderDrug.getDrugId();
                    }));
            Collection<List<OrderDrug>> orderDrugLists = drugMap.values();
            for (List<OrderDrug> orderDrugList : orderDrugLists) {
                DrugSaleStatisticsItemPojo itemPojo = this.createDrugSaleStatisticsItemPojo(orderDrugList,
                        hospitalDrugMap, doctorMap);
                if (itemPojo != null) {
                    hospitalDrugSaleStatisticsItemPojos.add(itemPojo);
                }
            }
        }

        // 除诊所药外的其他药
        List<OrderDrug> otherOrderDrugs = orderDrugMap.values().stream()
                .flatMap(orderDrugList -> orderDrugList.stream()).filter(orderDrug -> {
                    return orderDrug.getDataSource() != null && orderDrug.getDataSource() != 0;
                }).collect(Collectors.toList());
        List<DrugSaleStatisticsItemPojo> otherDrugSaleStatisticsItemPojos = null;
        if (CollectionUtils.isNotEmpty(otherOrderDrugs)) {
            otherDrugSaleStatisticsItemPojos = Lists.newArrayList();
            Map<String, List<OrderDrug>> drugMap = otherOrderDrugs.stream().collect(Collectors.groupingBy(orderDrug -> {
                return orderDrug.getSaleChannel() + "_" + orderDrug.getDrugIdentify();
            }));
            Collection<List<OrderDrug>> orderDrugLists = drugMap.values();
            for (List<OrderDrug> orderDrugList : orderDrugLists) {
                DrugSaleStatisticsItemPojo itemPojo = this.createDrugSaleStatisticsItemPojo(orderDrugList,
                        new HashMap<>(), doctorMap);
                if (itemPojo != null) {
                    otherDrugSaleStatisticsItemPojos.add(itemPojo);
                }
            }
        }
        // 合并诊所药物和其他药物
        List<DrugSaleStatisticsItemPojo> allDrugs = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(hospitalDrugSaleStatisticsItemPojos)) {
            allDrugs.addAll(hospitalDrugSaleStatisticsItemPojos);
        }
        if (CollectionUtils.isNotEmpty(otherDrugSaleStatisticsItemPojos)) {
            allDrugs.addAll(otherDrugSaleStatisticsItemPojos);
        }
        // .计算出总记录数
        Integer totalCount = allDrugs.size();
        req.setTotalCount(totalCount);
        re.setTotalPage(req.getTotalPage());
        // 排序
        List<DrugSaleStatisticsItemPojo> drugs = allDrugs.stream().sorted((drug1, drug2) -> {
            Double drug1SalePrice = drug1.getSaleAmt() == null ? 0D : drug1.getSaleAmt();
            Double drug2SalePrice = drug2.getSaleAmt() == null ? 0D : drug2.getSaleAmt();
            return drug2SalePrice.compareTo(drug1SalePrice);
        }).collect(Collectors.toList());
        // 分页
        if (req.getStartRecord() != null & req.getPageSize() != null) {
            drugs = drugs.stream().skip(req.getStartRecord()).limit(req.getPageSize()).collect(Collectors.toList());
        }
        re.setDrugs(drugs);

        Double totalProfit = allDrugs.stream().mapToDouble(DrugSaleStatisticsItemPojo::getProfit).sum();
        re.setTotalProfit(totalProfit);
        Double totalSaleAmt = allDrugs.stream().mapToDouble(DrugSaleStatisticsItemPojo::getSaleAmt).sum();
        re.setTotalSaleAmt(totalSaleAmt);
        return re;
    }

    private DrugSaleStatisticsItemPojo createDrugSaleStatisticsItemPojo(List<OrderDrug> orderDrugList,
                                                                        Map<Long, HospitalDrug> hospitalDrugMap, Map<Long, String> doctorMap) {
        // orderDrugList=某种药物id+销售渠道下面对应的药
        DrugSaleStatisticsItemPojo itemPojo = null;
        OrderDrug firstDrug = orderDrugList.get(0);
        if (firstDrug != null) {
            itemPojo = new DrugSaleStatisticsItemPojo();
            Long drugId = firstDrug.getDrugId();
            itemPojo.setDrugId(drugId);
            // 如果是诊所或者惠每的药物，取最新的对应药物名称,否则取orderDrug对应的值
            Integer dataSource = firstDrug.getDataSource();
            String drugName = "";
            Integer drugType = null;
            String manufacturer = "";
            String specification = "";

            if (dataSource != null & dataSource == 0 & drugId != null) {
                // 诊所的药
                HospitalDrug hospitalDrug = hospitalDrugMap.get(drugId);
                drugName = hospitalDrug.getDrugName();
                drugType = hospitalDrug.getDrugType();
                manufacturer = hospitalDrug.getManufacturer();
                specification = hospitalDrug.getSpecification();
            } else {
                // 惠每的药或者手动录入的药
                drugName = firstDrug.getDrugName();
                drugType = firstDrug.getDrugType();
                manufacturer = firstDrug.getManufacturer();
                specification = firstDrug.getSpecification();
            }
            //医生工作统计
            String doctorRealName = doctorMap.get(firstDrug.getCreater());
            itemPojo.setDoctorName(doctorRealName);

            itemPojo.setDrugName(drugName);
            String drugTypeName = DrugTypeEnum.getDrugTypeNameByType(drugType);
            itemPojo.setDrugTypeName(drugTypeName);
            itemPojo.setManufacturer(manufacturer);
            itemPojo.setSpecification(specification);

            String saleChannelName = SaleChannel.getNameByType(firstDrug.getSaleChannel());
            itemPojo.setSaleChannelName(saleChannelName);
            // 设置单位和数量
            this.setUnitToHospitalDrugSaleStatisticsItemPojo(itemPojo, firstDrug, hospitalDrugMap);
            Double totalCount = orderDrugList.stream().mapToDouble(orderDrug -> {
                Double count = this.getSaleStatisticsCount(orderDrug, hospitalDrugMap);
                return count == null ? 0 : count;
            }).sum();
            itemPojo.setCount(totalCount);
            // 进货价=每个药物的(进货单价*数量)的和
            Double purchasePrice = orderDrugList.stream().filter(orderDrug -> orderDrug.getPurchasePrice() != null)
                    .mapToDouble(orderDrug -> {
                        return orderDrug.getPurchasePrice();
                    }).sum();
            itemPojo.setPurchaseAmt(purchasePrice == null ? 0D : AmtUtils.decimalFormat(purchasePrice));
            // 销售价=每个药物的应收金额的和
            Double salePrice = orderDrugList.stream().filter(orderDrug -> orderDrug.getSalePrice() != null)
                    .mapToDouble(orderDrug -> {
                        return null == orderDrug.getReceivableAmt() ? 0D : orderDrug.getReceivableAmt();
                    }).sum();
            itemPojo.setSaleAmt(salePrice == null ? 0D : AmtUtils.decimalFormat(salePrice));
            // 利润=销售价-进货价
            Double profit = AmtUtils.subtract(salePrice, purchasePrice);
            itemPojo.setProfit(profit);
        }
        return itemPojo;
    }

    private void setUnitToHospitalDrugSaleStatisticsItemPojo(DrugSaleStatisticsItemPojo itemPojo, OrderDrug orderDrug,
                                                             Map<Long, HospitalDrug> hospitalDrugMap) {
        if (itemPojo == null || orderDrug == null) {
            return;
        }
        String saleUnit = orderDrug.getSaleUnit();
        if (orderDrug.getDataSource() == 0 && orderDrug.getDrugId() != null) {
            HospitalDrug hospitalDrug = hospitalDrugMap.get(orderDrug.getDrugId().intValue());
            if (hospitalDrug != null) {
                // 是否支持拆零
                Integer openStock = hospitalDrug.getOpenStock();
                if (openStock != null && openStock == 1) {
                    // 如果支持拆零，则统计单位为规格单位
                    saleUnit = hospitalDrug.getSpecUnit();
                }
            }
        }
        itemPojo.setSaleUnit(saleUnit);
    }

    /**
     * @param orderDrug
     * @return
     * @description 根据药物的的类型（ 饮片其他），销售渠道，可拆零与否，单位，计算出对应的数量,如果是可拆零卖的诊所药物以包装规格买的，
     * 则需要根据包装单位、换算比和数量，换算得到规格单位对应的数量
     * @date 2016年4月8日
     * @author lipeng
     */
    public Double getSaleStatisticsCount(OrderDrug orderDrug, Map<Long, HospitalDrug> hospitalDrugMap) {
        if (orderDrug == null) {
            return 0D;
        }

        Integer dataSource = orderDrug.getDataSource();
        Integer saleChannel = orderDrug.getSaleChannel();
        Integer drugType = orderDrug.getDrugType();
        Double gramCount = orderDrug.getGramCount();// 如果是直接售药，gramCount为null
        // 处方中 西药数量或中药剂数，或者直接售药中的数量
        Integer dosage = orderDrug.getDosage();
        if (dosage == null) {
            return 0D;
        }
        Double realSaleCount = 0D; // 销售单位对应的实际数量
        if (saleChannel == SaleChannel.Prescription.getType() && drugType == DrugTypeEnum.herbal.getDrugType()) {
            // 如果是处方开药 并且是饮片
            realSaleCount = AmtUtils.multiply(dosage, gramCount == null ? 0D : gramCount);
        } else {
            realSaleCount = dosage.doubleValue();
        }
        Double count = realSaleCount;
        // 如果是诊所的可拆零的药物，并且按包装单位出售的，换算成规格单位对应的数量
        if (dataSource == 0 && orderDrug.getDrugId() != null) {
            HospitalDrug hospitalDrug = hospitalDrugMap.get(orderDrug.getDrugId());
            if (hospitalDrug != null) {
                Integer openStock = hospitalDrug.getOpenStock();
                if (openStock != null && openStock == 1) {
                    // 如果支持拆零，按规格单位统计
                    // 换算比
                    Integer specUnitaryRatio = hospitalDrug.getSpecUnitaryRatio();
                    // 包装单位
                    String specPackageUnit = hospitalDrug.getSpecPackageUnit();
                    // 销售单位
                    String saleUnit = orderDrug.getSaleUnit();
                    if (StringUtils.equals(saleUnit, specPackageUnit) && specUnitaryRatio != null) {
                        // 如果药物是以包装单位售出，需要换算成规格单位的数目
                        count = (double) (realSaleCount * specUnitaryRatio);
                    }
                    return count;
                }
            }
        }
        return count;
    }

    @Override
    public List<WorkloadStatisticsItemPojo> loadDrugStatistics(StatisticsRequest req) {
        List<WorkloadStatisticsItemPojo> loadsDrug = new ArrayList<>();
        List<OrderDrug> orderDrugs = orderDrugMapper.selectLoadsDrugStatistics(req);
        if (CollectionUtils.isEmpty(orderDrugs)) {
            return null;
        }
        List<Long> createrIds = orderDrugs.stream().map(OrderDrug::getCreater).collect(Collectors.toList());
        Map<Long, String> doctorMap = doctorService.getDoctorRealNames(createrIds);
        List<String> orderNoList = orderDrugs.stream().map(orderDrug -> {
            return orderDrug.getOrderNo();
        }).collect(Collectors.toList());

        Map<Integer, List<OrderDrug>> orderDrugMap = orderDrugs.stream().collect(Collectors.groupingBy(orderDrug -> {
            return orderDrug.getDataSource() == null ? 5 : orderDrug.getDataSource();
        }));
        // 全部诊所药
        List<OrderDrug> hospitalOrderDrugs = orderDrugMap.get(0);
        List<DrugSaleStatisticsItemPojo> hospitalDrugSaleStatisticsItemPojos = null;
        if (CollectionUtils.isNotEmpty(hospitalOrderDrugs)) {
            List<Long> hospitalDrugIds = hospitalOrderDrugs.stream().map(OrderDrug::getDrugId)
                    .collect(Collectors.toList());
            Map<Long, HospitalDrug> hospitalDrugMap = new HashMap<>();
            List<HospitalDrug> hospitalDrugs = hospitalDrugMapper.searchDrugByDrugIds(hospitalDrugIds);
            for (HospitalDrug hospitalDrug : hospitalDrugs) {
                hospitalDrugMap.put(hospitalDrug.getId(), hospitalDrug);
            }
            hospitalDrugSaleStatisticsItemPojos = Lists.newArrayList();
            Map<String, List<OrderDrug>> drugMap = hospitalOrderDrugs.stream()
                    .collect(Collectors.groupingBy(orderDrug -> {
                        return orderDrug.getSaleChannel() + "" + orderDrug.getDrugId() + "" + orderDrug.getCreater();
                    }));
            Collection<List<OrderDrug>> orderDrugLists = drugMap.values();
            for (List<OrderDrug> orderDrugList : orderDrugLists) {
                DrugSaleStatisticsItemPojo itemPojo = this.createDrugSaleStatisticsItemPojo(orderDrugList,
                        hospitalDrugMap, doctorMap);
                if (itemPojo != null) {
                    hospitalDrugSaleStatisticsItemPojos.add(itemPojo);
                }
            }
        }
        // 除诊所药外的其他药
        List<OrderDrug> otherOrderDrugs = orderDrugMap.values().stream()
                .flatMap(orderDrugList -> orderDrugList.stream()).filter(orderDrug -> {
                    return orderDrug.getDataSource() != null && orderDrug.getDataSource() != 0;
                }).collect(Collectors.toList());
        List<DrugSaleStatisticsItemPojo> otherDrugSaleStatisticsItemPojos = null;
        if (CollectionUtils.isNotEmpty(otherOrderDrugs)) {
            otherDrugSaleStatisticsItemPojos = Lists.newArrayList();
            Map<String, List<OrderDrug>> drugMap = otherOrderDrugs.stream().collect(Collectors.groupingBy(orderDrug -> {
                return orderDrug.getSaleChannel() + "" + orderDrug.getDrugIdentify() + "" + orderDrug.getCreater();
            }));
            Collection<List<OrderDrug>> orderDrugLists = drugMap.values();
            for (List<OrderDrug> orderDrugList : orderDrugLists) {
                DrugSaleStatisticsItemPojo itemPojo = this.createDrugSaleStatisticsItemPojo(orderDrugList,
                        new HashMap<>(), doctorMap);
                if (itemPojo != null) {
                    otherDrugSaleStatisticsItemPojos.add(itemPojo);
                }
            }
        }
        // 合并诊所药物和其他药物
        List<DrugSaleStatisticsItemPojo> allDrugs = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(hospitalDrugSaleStatisticsItemPojos)) {
            allDrugs.addAll(hospitalDrugSaleStatisticsItemPojos);
        }
        if (CollectionUtils.isNotEmpty(otherDrugSaleStatisticsItemPojos)) {
            allDrugs.addAll(otherDrugSaleStatisticsItemPojos);
        }
        loadsDrug = allDrugs.stream().map(orderDrug -> {
            return this.getWorkloadStatisticsItemPojoByOrderDrug(orderDrug);
        }).collect(Collectors.toList());
        for (WorkloadStatisticsItemPojo itemPojo : loadsDrug) {
            itemPojo.setOrderNoList(orderNoList);
        }
        return loadsDrug;
    }

    private WorkloadStatisticsItemPojo getWorkloadStatisticsItemPojoByOrderDrug(DrugSaleStatisticsItemPojo orderDrug) {
        if (null == orderDrug) {
            return null;
        }
        WorkloadStatisticsItemPojo po = new WorkloadStatisticsItemPojo();
        po.setDoctorName(orderDrug.getDoctorName());
        po.setItemTypeName(orderDrug.getDrugTypeName());
        po.setItemName(orderDrug.getDrugName());
        po.setCount(orderDrug.getCount());
        po.setSpecification(orderDrug.getSpecification());
        po.setManufacturer(orderDrug.getManufacturer());
        po.setSaleUnit(orderDrug.getSaleUnit());
        po.setCount(orderDrug.getCount());
        po.setCountDesc(orderDrug.getCountDesc());
        po.setReceivableAmt(orderDrug.getSaleAmt());
        return po;
    }
}
