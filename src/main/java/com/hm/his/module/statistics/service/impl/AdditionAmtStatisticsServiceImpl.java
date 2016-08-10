package com.hm.his.module.statistics.service.impl;

import com.hm.his.framework.utils.AmtUtils;
import com.hm.his.framework.utils.LangUtils;
import com.hm.his.module.order.dao.OrderAdditionAmtMapper;
import com.hm.his.module.order.model.OrderAdditionAmt;
import com.hm.his.module.statistics.pojo.AdditionAmtStatisticsItemPojo;
import com.hm.his.module.statistics.pojo.AdditionAmtStatisticsPojo;
import com.hm.his.module.statistics.pojo.StatisticsRequest;
import com.hm.his.module.statistics.pojo.WorkloadStatisticsItemPojo;
import com.hm.his.module.statistics.service.AdditionAmtStatisticsService;
import com.hm.his.module.user.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdditionAmtStatisticsServiceImpl implements AdditionAmtStatisticsService {
    @Autowired(required = false)
    OrderAdditionAmtMapper orderAdditionAmtMapper;
    @Autowired
    DoctorService doctorService;

    @Override
    public AdditionAmtStatisticsPojo additionAmtStatistics(StatisticsRequest req) {
        StatisticsRequest.handleStatisticsRequest(req);
        AdditionAmtStatisticsPojo re = new AdditionAmtStatisticsPojo();
        // 只统计已收费且未退费的附加费用
        List<AdditionAmtStatisticsItemPojo> additionAmts = orderAdditionAmtMapper.selectAdditionAmtStatisticsItemPojos(req);
        re.setAdditionAmts(additionAmts);
        Integer totalCount = orderAdditionAmtMapper.selectCountOfAdditionAmtStatisticsItemPojos(req);
        req.setTotalCount(totalCount);
        re.setTotalPage(req.getTotalPage());
        AdditionAmtStatisticsPojo totalSaleAmtAndCount = orderAdditionAmtMapper.selectTotalSaleAmtAndCount(req);
        if (totalSaleAmtAndCount != null) {
            re.setAdditionAmtCount(totalSaleAmtAndCount.getAdditionAmtCount());
            re.setTotalSaleAmt(totalSaleAmtAndCount.getTotalSaleAmt());
        }
        return re;
    }

    @Override
    public List<WorkloadStatisticsItemPojo> loadAdditionalStatistics(StatisticsRequest req) {
        List<WorkloadStatisticsItemPojo> loadsAdditonal = new ArrayList<>();
        List<OrderAdditionAmt> orderAdditionAmts = orderAdditionAmtMapper.selectLoadsAdditionalStatistics(req);
        if (CollectionUtils.isEmpty(orderAdditionAmts)) {
            return null;
        }
        List<String> orderNoList = orderAdditionAmts.stream().map(orderAdditionAmt -> {
            return orderAdditionAmt.getOrderNo();
        }).collect(Collectors.toList());

        Map<String, List<OrderAdditionAmt>> orderAmtMap = orderAdditionAmts.stream().collect(Collectors.groupingBy(orderAmt -> {
            return orderAmt.getAmtName() + orderAmt.getCreater();
        }));
        Collection<List<OrderAdditionAmt>> orderAmtLists = orderAmtMap.values();
        for (List<OrderAdditionAmt> orderAmtList : orderAmtLists) {
            WorkloadStatisticsItemPojo itemPojo = createAmtSaleStatisticsItemPojo(orderAmtList);
            if (null != itemPojo) {
                itemPojo.setOrderNoList(orderNoList);
                loadsAdditonal.add(itemPojo);
            }
        }
        return loadsAdditonal;
    }

    private WorkloadStatisticsItemPojo createAmtSaleStatisticsItemPojo(List<OrderAdditionAmt> orderAmtList) {
        WorkloadStatisticsItemPojo itemPojo = null;
        OrderAdditionAmt orderAmt = orderAmtList.get(0);
        if (null != orderAmt) {
            itemPojo = new WorkloadStatisticsItemPojo();
            itemPojo.setItemTypeName("附加费用");
            itemPojo.setItemName(orderAmt.getAmtName());
            itemPojo.setSpecification("");
            itemPojo.setManufacturer("");
            itemPojo.setSaleUnit("");
            //医生工作统计
            itemPojo.setDoctorName(orderAmt.getDoctorName());
            Double totalcount = orderAmtList.stream().mapToDouble(order -> {
                Double count = LangUtils.getDouble(order.getCount());
                return count == null ? 1 : count;
            }).sum();
            itemPojo.setCount(totalcount);
            itemPojo.setCountDesc(String.valueOf(Math.ceil(totalcount)));
            Double receivableAmt = orderAmtList.stream().filter(order -> order.getSalePrice() != null).mapToDouble(order -> {
                Double receivable = order.getReceivableAmt();
                return AmtUtils.decimalFormat(null == receivable ? 0D : receivable);
            }).sum();
            itemPojo.setReceivableAmt(receivableAmt);
        }
        return itemPojo;
    }
}
