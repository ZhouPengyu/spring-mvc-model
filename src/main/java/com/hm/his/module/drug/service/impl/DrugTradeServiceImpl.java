package com.hm.his.module.drug.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.engine.common.BeanUtils;
import com.hm.his.framework.model.HisResponse;
import com.hm.his.framework.utils.DateTools;
import com.hm.his.framework.utils.LangUtils;
import com.hm.his.framework.utils.RegexUtil;
import com.hm.his.module.drug.dao.DrugTradeMapper;
import com.hm.his.module.drug.model.DrugTrade;
import com.hm.his.module.drug.model.HospitalDrug;
import com.hm.his.module.drug.pojo.DrugRequest;
import com.hm.his.module.drug.service.DrugTradeService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/3/21
 * Time: 11:03
 * CopyRight:HuiMei Engine
 */
@Service
public class DrugTradeServiceImpl implements DrugTradeService {

    @Autowired(required = false)
    DrugTradeMapper drugTradeMapper;

    public static String buildStandSpecification(DrugTrade drugTrade) {
        StringBuffer sb = new StringBuffer();
        if(null !=drugTrade.getSpecMinimumDosage() && StringUtils.isNotBlank(drugTrade.getSpecMinimumUnit())){
            sb.append(drugTrade.getSpecMinimumDosage()).append(drugTrade.getSpecMinimumUnit()).append("/");
        }
        sb.append(LangUtils.getString(drugTrade.getSpecUnit(), ""));
        if(null != drugTrade.getSpecUnitaryRatio()){
            sb.append("*").append(drugTrade.getSpecUnitaryRatio());
        }
        sb.append("/").append(LangUtils.getString(drugTrade.getSpecPackageUnit(), ""));
        return sb.toString();
    }

    /**
     *  功能描述：HM药品库的药品 构建规格描述
     * @param
     * @return
     * @throws
     * @author:  tangww
     * @createDate   2016/3/3
     *
     */
    public static String buildHMDrugSpec(DrugTrade drugTrade) {
        if(drugTrade.getDataSource()!=null && drugTrade.getDataSource()==0){
            return buildStandSpecification(drugTrade);
        }else{
            if(StringUtils.isNotBlank(drugTrade.getSpecification())){
                return drugTrade.getSpecification();
            }else{
                return buildStandSpecification(drugTrade);
            }

        }
    }

    @Override
    public DrugTrade getDrugById(Long drugId) {
        DrugTrade trade = drugTradeMapper.selectByPrimaryKey(drugId);
        if(trade !=null)
            trade.setSpecification(this.buildHMDrugSpec(trade));
        return trade;
    }

    @Override
    public Map<Long, DrugTrade> searchDrugByIds(List<Long> drugsId) {
        Map<Long,DrugTrade> drugTradeMap = Maps.newHashMap();
        if(CollectionUtils.isNotEmpty(drugsId)){
            List<DrugTrade> trades = drugTradeMapper.searchDrugTradeByIds(drugsId);
            if(CollectionUtils.isNotEmpty(trades)){
                //return hospitalDrugs.stream().collect(Collectors.groupingBy(hospDrug -> hospDrug.getId()));
                trades.stream().forEach(drugTrade -> {
                    drugTrade.setSpecification(this.buildHMDrugSpec(drugTrade));
                    drugTradeMap.put(drugTrade.getId(),drugTrade);
                });
            }
        }
        return drugTradeMap;
    }

    @Override
    public List<DrugTrade> searchDrugByName(DrugRequest drugRequest) {
        if(StringUtils.isNotBlank(drugRequest.getDrugName())){
            List<DrugTrade> drugTrades = Lists.newArrayList();
            // 如果 药品名称中包括中文，则根据通用名 和商品名模糊搜索
            if(RegexUtil.isContainChinese(drugRequest.getDrugName())){
                drugTrades = drugTradeMapper.searchDrugByDrugName(drugRequest);
                return drugTrades;
            }else{
                // 如果 药品名称中 没有包括中文，则根据 条形码，通用名拼音和商品名拼音 搜索
                drugTrades = drugTradeMapper.searchDrugByDrugNamePinyin(drugRequest);
                return drugTrades;
            }
        }else{
            return null;
        }
    }

    @Override
    public HospitalDrug getDrugByHMDrugId(Long drugId) {
        DrugTrade drugTrade = drugTradeMapper.selectByPrimaryKey(drugId);
        if(drugTrade !=null){
            return hmDrugToHospitalDrug(drugTrade);
        }
        return null;
    }

    @Override
    public HospitalDrug hmDrugToHospitalDrug(DrugTrade drugTrade) {

        HospitalDrug hospitalDrug = new HospitalDrug();
        BeanUtils.copyProperties(drugTrade,hospitalDrug,false);
        if(hospitalDrug.getIsOtc()==null){
            hospitalDrug.setIsOtc(0);
        }
        if(hospitalDrug.getStatus()==null){
            hospitalDrug.setStatus(1);
        }
        hospitalDrug.setHmDrugId(drugTrade.getId());
        hospitalDrug.setDrugName(drugTrade.getCommonName());
        if(StringUtils.isNotBlank(drugTrade.getTradeName())){
            hospitalDrug.setTradeName(drugTrade.getTradeName());
        }
        hospitalDrug.setSpecification(buildHMDrugSpec(drugTrade));
        if(StringUtils.isBlank(hospitalDrug.getSpecification())){
            hospitalDrug.setSpecification("");
        }
        hospitalDrug.setValidityDate(DateTools.formatDateString("2999-01-01",DateTools.ONLY_DATE_FORMAT));
        hospitalDrug.setValidityDateStr("2999-01-01");
        hospitalDrug.setPinyin(drugTrade.getCommonPinyin());
        hospitalDrug.setPinyinFirst(drugTrade.getCommonPinyinFirst());
        hospitalDrug.setDataSource(1);
        return hospitalDrug;

    }

    @Override
    public List<DrugTrade> searchHMDrugByBarCode(DrugRequest drugRequest) {
        List<DrugTrade> drugTrades =drugTradeMapper.searchDrugByBarCode(drugRequest);
        return drugTrades;
    }

    @Override
    public List<DrugTrade> searchDrugList(DrugRequest drugRequest) {
        return null;
    }

    @Override
    public List<DrugTrade> findAllDrugTrade() {
        return drugTradeMapper.findAllDrugTrade();
    }

    @Override
    public DrugTrade selectByDrugTrade(DrugTrade trade) {
        return drugTradeMapper.selectByDrugTrade(trade);
    }


    @Override
    public HisResponse saveDrug(DrugTrade drugTrade) {
        drugTradeMapper.insertSelective(drugTrade);
        return null;
    }

    @Override
    public HisResponse modifyDrug(DrugTrade drugTrade) {
        return null;
    }

    @Override
    public Integer deleteDrug(List<Long> drugsId) {
        return null;
    }
}
