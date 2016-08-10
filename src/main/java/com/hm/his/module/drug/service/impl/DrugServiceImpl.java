package com.hm.his.module.drug.service.impl;

import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.engine.common.BeanUtils;
import com.hm.his.framework.log.annotation.SystemLogAnno;
import com.hm.his.framework.log.constant.FunctionConst;
import com.hm.his.framework.log.constant.Operation;
import com.hm.his.framework.model.HisResponse;
import com.hm.his.framework.utils.*;
import com.hm.his.module.drug.dao.DrugInventoryMapper;
import com.hm.his.module.drug.dao.DrugTradeMapper;
import com.hm.his.module.drug.dao.HospitalDrugMapper;
import com.hm.his.module.drug.model.*;
import com.hm.his.module.drug.pojo.DrugRequest;
import com.hm.his.module.drug.pojo.DrugResponse;
import com.hm.his.module.drug.pojo.HospitalDrugSug;
import com.hm.his.module.drug.pojo.SaleWayPojo;
import com.hm.his.module.drug.service.DrugInventoryService;
import com.hm.his.module.drug.service.DrugService;
import com.hm.his.module.drug.service.DrugTradeService;
import com.hm.his.module.drug.service.InventoryOperateLogService;

import com.hm.his.module.instock.model.BatchInstock;
import com.hm.his.module.instock.model.DrugBatchInstock;
import com.hm.his.module.instock.service.InstockService;
import com.hm.his.module.manage.model.ConfigAttribute;
import com.hm.his.module.manage.model.HospitalConfig;
import com.hm.his.module.manage.model.HospitalConfigEnum;
import com.hm.his.module.manage.service.HospitalConfigService;
import com.hm.his.module.order.model.SellDrug;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DrugServiceImpl implements DrugService {

    Logger logger = Logger.getLogger(DrugServiceImpl.class);

    private static final Long TEMPLATE_HOSPITAL_ID = 122L;

    @Autowired(required = false)
    HospitalDrugMapper hospitalDrugMapper;

    @Autowired(required = false)
    DrugInventoryMapper drugInventoryMapper;

    @Autowired(required = false)
    DrugTradeMapper drugTradeMapper;
    @Autowired(required = false)
    DrugTradeService drugTradeService;
    @Autowired(required = false)
    InstockService instockService;

    @Autowired(required = false)
    HospitalConfigService hospitalConfigService;

    @Autowired(required = false)
    InventoryOperateLogService inventoryOperateLogService;

    @Autowired(required = false)
    DrugInventoryService drugInventoryService;



    @Override
    public HospitalDrug getDrugById(Long drugId) {
        if(drugId == null){
            return null;
        }
        HospitalDrug hDrug1 = hospitalDrugMapper.selectByPrimaryKey(drugId.intValue());
        if(hDrug1==null){
            return null;
        }else{
            hDrug1.setSpecification(buildDrugSpecForList(hDrug1));
        }
        hDrug1.setDataSource(0);
        DrugInventory inventory = drugInventoryService.selectByDrugId(drugId);
        if(inventory !=null){
            hDrug1.setInventoryCount(inventory.getInventoryCount());
            hDrug1.setGoodsShelfCode(inventory.getGoodsShelfCode());
        }

        return hDrug1;

    }
    
    @Override
    public HospitalDrug getDrugByPurchaseOrderDrugId(Long drugId) {
        if(drugId == null){
            return null;
        }
        HospitalDrug hDrug1 = hospitalDrugMapper.selectByPrimaryKey(drugId.intValue());
        if(hDrug1==null){
            return null;
        }
        return hDrug1;

    }

    @Override
    public HospitalDrug getDrugDetailById(Long drugId) {
        if(drugId == null){
            return null;
        }
        HospitalDrug hospitalDrug = hospitalDrugMapper.selectByPrimaryKey(drugId.intValue());
        if(hospitalDrug==null){
            return null;
        }
        hospitalDrug.setDataSource(0);
        DrugInventory inventory = drugInventoryService.selectByDrugId(drugId);
        if(inventory !=null){
            hospitalDrug.setInventoryCount(inventory.getInventoryCount());
            hospitalDrug.setGoodsShelfCode(inventory.getGoodsShelfCode());
            hospitalDrug.setInventoryThreshold(inventory.getInventoryThreshold());
            if(inventory.getValidityDate() != null){
                hospitalDrug.setValidityDate(inventory.getValidityDate());
                hospitalDrug.setValidityDateStr(DateTools.getDate(DateTools.ONLY_DATE_FORMAT,inventory.getValidityDate()));
            }else{
                hospitalDrug.setValidityDate(DateTools.formatDateString("2999-01-01",DateTools.ONLY_DATE_FORMAT));
                hospitalDrug.setValidityDateStr(DateTools.getDate(DateTools.ONLY_DATE_FORMAT,hospitalDrug.getValidityDate()));
            }
        }
        return hospitalDrug;
    }




    @Override
    public Integer batchModifyDrugInventoryThreshold(DrugRequest drugRequest) {
        if(CollectionUtils.isNotEmpty(drugRequest.getDrugIds())){
            for(Long drugId :drugRequest.getDrugIds()){
                DrugInventory drugInventory = new DrugInventory();
                drugInventory.setHospitalId(drugRequest.getHospitalId());
                drugInventory.setDrugId(drugId);
                drugInventory.setInventoryThreshold(drugRequest.getInventoryThreshold());
                drugInventoryMapper.updateByHospitalIdAndDrugId(drugInventory);

            }
            return drugRequest.getDrugIds().size();
        }
        return 0;
    }

    @Override
    public Integer countHospitalDrug(DrugRequest drugRequest) {
        return hospitalDrugMapper.countHospitalDrug(drugRequest);

    }

    @Override
    public Integer countHospitalDrugAllByHospitalId(Long hospitalId) {
        return hospitalDrugMapper.countHospitalDrugAllByHospitalId(hospitalId);

    }


    @Override
    public List<HospitalDrug> searchDrugList(DrugRequest drugRequest) {
//        System.out.println("searchDrugList============="+drugRequest);
        List<HospitalDrug> hospitalDrugs = hospitalDrugMapper.selectHospitalDrug(drugRequest);
//        System.out.println("searchDrugList======hospitalDrugs======="+hospitalDrugs);
        return  hospitalDrugs.stream().map(hospitalDrug -> {
            hospitalDrug.setSpecification(buildDrugSpecForList(hospitalDrug));
            hospitalDrug.setUseInfo(buildDrugUseInfo(hospitalDrug));
            hospitalDrug.setManufacturer(LangUtils.getString(hospitalDrug.getManufacturer(),""));
            hospitalDrug.setSaleUnit(LangUtils.getString(setSaleUnitByOpenStock(hospitalDrug),""));
            if(hospitalDrug.getInventoryCount() == null){
                hospitalDrug.setInventoryCount(0D);
            }
            hospitalDrug.setDrugTypeName(DrugTypeEnum.getDrugTypeNameByType(hospitalDrug.getDrugType()));
            return hospitalDrug;
        }).collect(Collectors.toList());
    }



    /**
     *  功能描述：根据药品名称搜索药品信息
     *    如果查惠每药物库，需要区分 惠每药物库 和cfda 药物，优先显示 惠每药物
     * @author:  tangww
     * @createDate   2016-06-03
     *
     */
    @Override
    public List<HospitalDrugSug>  searchDrugByName(DrugRequest drugRequest) {

        if(StringUtils.isNotBlank(drugRequest.getDrugName())){
            drugRequest.setDrugName(drugRequest.getDrugName().trim());
        }
        List<HospitalDrugSug> hospitalDrugSugs = Lists.newArrayList();
        if(null != drugRequest.getDataSource()){
            List<HospitalDrug> hospitalDrugs = Lists.newArrayList();
            //药品数据源  1：仅查惠每  0：仅查诊所 2：查询所有库

            if(drugRequest.getDataSource()==0){// 查询药品列表处搜索
                hospitalDrugs = hospitalDrugMapper.searchDrugByName(drugRequest);
                if(CollectionUtils.isNotEmpty(hospitalDrugs)){
                    return hospitalDrugs.stream().map(hospitalDrug -> {
                        HospitalDrugSug hospitalDrugSug = new HospitalDrugSug();
                        BeanUtils.copyProperties(hospitalDrug,hospitalDrugSug,false);
                        hospitalDrugSug.setUsage(hospitalDrug.getInstruction());
                        hospitalDrugSug.setSpecification(buildDrugSpecForList(hospitalDrug));
                        hospitalDrugSug.setDataSource(0);
                        return hospitalDrugSug;
                    }).collect(Collectors.toList());
                }
            }else if(drugRequest.getDataSource()==1){//新增药品处搜索

                List<DrugTrade> drugTrades = drugTradeService.searchDrugByName(drugRequest);
                if(CollectionUtils.isNotEmpty(drugTrades)){
                    return drugTrades.stream().map(drugTrade -> {
                        return processHospitalDrugSugByHMDrug(drugTrade,true);
                    }).collect(Collectors.toList());
                }

            }
        }

        return hospitalDrugSugs;
    }

    @Override

    public DrugResponse searchDrugByBarCodeForWX(DrugRequest drugRequest) {
        DrugResponse drugResponse = new DrugResponse();

        List<HospitalDrug> hospitalDrugs = hospitalDrugMapper.searchDrugByBarCodeForSale(drugRequest);
        //当诊所库的药物不为空时，返回 诊所药品数据，设置价格和库存信息
        if(CollectionUtils.isNotEmpty(hospitalDrugs)){
            hospitalDrugs = hospitalDrugs.stream().map(hospitalDrug -> {
                hospitalDrug.setDataSource(0);
                return hospitalDrug;
            }).collect(Collectors.toList());
            drugResponse.setDataSource(0);
            drugResponse.setHospitalDrugs(hospitalDrugs);
            return drugResponse;
        }else{ //当诊所库的药物为空时，查询惠每药物库， 当惠每药物库不为空时，返回 惠每药品数据，页面应跳转到增加药品页面，并加载药品 信息
            List<DrugTrade> drugTrades = drugTradeMapper.searchDrugByBarCode(drugRequest);
            if(CollectionUtils.isNotEmpty(drugTrades)){
                hospitalDrugs = drugTrades.stream().map(drugTrade -> {

                    return drugTradeService.hmDrugToHospitalDrug(drugTrade);
                }).collect(Collectors.toList());
                drugResponse.setHospitalDrugs(hospitalDrugs);
                drugResponse.setDataSource(1);
                return drugResponse;
            }else{//当诊所库和惠每药物库都为空时，页面应跳转到增加药品页面，完全新增
                drugResponse.setDataSource(-1);
                return drugResponse;
            }
        }

    }



    @Override

    public List<HospitalDrug> searchDrugNameList(DrugRequest drugRequest) {
        if(StringUtils.isNotBlank(drugRequest.getDrugName())){
            drugRequest.setDrugName(drugRequest.getDrugName().trim());
        }
        List<HospitalDrug> hospitalDrugs = hospitalDrugMapper.searchDrugNameList(drugRequest);
        return hospitalDrugs.stream().map(hospitalDrug -> {
            HospitalDrug hDrug = new HospitalDrug();
            hDrug.setId(hospitalDrug.getId());
            hDrug.setDrugName(hospitalDrug.getDrugName());
            return hDrug;
        }).collect(Collectors.toList());
    }

    @Override
    public List<HospitalDrugSug> searchDrugByNameForSale(DrugRequest drugRequest) {
        if(StringUtils.isNotBlank(drugRequest.getDrugName())){
            drugRequest.setDrugName(drugRequest.getDrugName().trim());
        }
        List<HospitalDrug> hospitalDrugs = hospitalDrugMapper.searchDrugByNameForSale(drugRequest);
        //当诊所库的药物不为空时，仅返回 诊所药品数据，并计算药品是否支持拆分以及拆分后的价格信息, 将datasource 设置 为 0
        if(CollectionUtils.isNotEmpty(hospitalDrugs)){
            return hospitalDrugs.stream().map(hospitalDrug -> {
                HospitalDrugSug hospitalDrugSug = new HospitalDrugSug();
                BeanUtils.copyProperties(hospitalDrug,hospitalDrugSug,false);
                hospitalDrugSug.setUsage(hospitalDrug.getInstruction());
                hospitalDrugSug.setSpecification(buildDrugSpecForList(hospitalDrug));
                hospitalDrugSug.setSaleWays(buildSaleWays(hospitalDrug));
                hospitalDrugSug.setManufacturer(LangUtils.getString(hospitalDrugSug.getManufacturer(),""));
                hospitalDrugSug.setDataSource(0);
                return hospitalDrugSug;
            }).collect(Collectors.toList());
        }else if(drugRequest.getDataSource() !=null && drugRequest.getDataSource() !=0){ //当诊所库的药物为空时，查询惠每药品数据 将datasource 设置 为 1
            List<DrugTrade> drugTrades = drugTradeService.searchDrugByName(drugRequest);
            if(CollectionUtils.isNotEmpty(drugTrades)){
                return drugTrades.stream().map(drugTrade -> {
                    return processHospitalDrugSugByHMDrug(drugTrade,true);
                }).collect(Collectors.toList());
            }
        }
        return Lists.newArrayList();
    }


    @Override
    public Map<Long,HospitalDrug> searchDrugByDrugIds(List<Long> drugIds) {
//        String drugIdsStr = StringUtils.join(drugIds,",");
        Map<Long,HospitalDrug> hospitalDrugMap = Maps.newHashMap();
        if(CollectionUtils.isNotEmpty(drugIds)){
            List<HospitalDrug> hospitalDrugs = hospitalDrugMapper.searchDrugByDrugIds(drugIds);
            //当诊所库的药物不为空时，仅返回 诊所药品数据，并计算药品是否支持拆分以及拆分后的价格信息, 将datasource 设置 为 0
            if(CollectionUtils.isNotEmpty(hospitalDrugs)){
                //return hospitalDrugs.stream().collect(Collectors.groupingBy(hospDrug -> hospDrug.getId()));
                hospitalDrugs.stream().forEach(hospitalDrug -> {
                    hospitalDrugMap.put(hospitalDrug.getId(),hospitalDrug);
                });
            }
        }
        return hospitalDrugMap;
    }


    @Override
    public List<HospitalDrugSug> searchDrugByBarCodeForSale(DrugRequest drugRequest) {
        List<HospitalDrug> hospitalDrugs = hospitalDrugMapper.searchDrugByBarCodeForSale(drugRequest);
        //当诊所库的药物不为空时，仅返回 诊所药品数据，并计算药品是否支持拆分以及拆分后的价格信息, 将datasource 设置 为 0
        if(CollectionUtils.isNotEmpty(hospitalDrugs)){
            return hospitalDrugs.stream().map(hospitalDrug -> {
                HospitalDrugSug hospitalDrugSug = new HospitalDrugSug();
                BeanUtils.copyProperties(hospitalDrug,hospitalDrugSug,false);
                hospitalDrugSug.setUsage(hospitalDrug.getInstruction());
                hospitalDrugSug.setSpecification(buildDrugSpecForList(hospitalDrug));
                hospitalDrugSug.setSaleWays(buildSaleWays(hospitalDrug));
                hospitalDrugSug.setDataSource(0);
                return hospitalDrugSug;
            }).collect(Collectors.toList());
        }else if(drugRequest.getDataSource() !=null && drugRequest.getDataSource() !=0){ //当诊所库的药物为空时，查询惠每药品数据 将datasource 设置 为 1
            List<DrugTrade> drugTrades = drugTradeMapper.searchDrugByBarCode(drugRequest);
            if(CollectionUtils.isNotEmpty(drugTrades)){
                return drugTrades.stream().map(drugTrade -> {
                    return processHospitalDrugSugByHMDrug(drugTrade,true);
                }).collect(Collectors.toList());
            }
        }
        return Lists.newArrayList();
    }

    @Override
    public List<SaleWayPojo>  searchDrugSaleWayById(Integer saleChannel,Long drugId) {
        if(drugId == null || saleChannel ==null){
            return null;
        }
        HospitalDrug hDrug = hospitalDrugMapper.searchDrugAndInventory(drugId);
        if(hDrug == null){
            logger.error("根据药品ID查询药品为空==="+drugId);
            return null;
        }
        List<SaleWayPojo> saleWayPojos = buildSaleWays(hDrug);
        return saleWayPojos;
    }

    @Override
    public HospitalDrugSug processHospitalDrugSugByHMDrug(DrugTrade drugTrade,boolean hasManufacturer) {
        HospitalDrugSug hospitalDrugSug = new HospitalDrugSug();
        HospitalDrug hospitalDrug = new HospitalDrug();
        BeanUtils.copyProperties(drugTrade,hospitalDrug,false);
        hospitalDrugSug.setId(drugTrade.getId());
        hospitalDrugSug.setHmDrugId(drugTrade.getId());
        hospitalDrugSug.setDrugName(buildHMDrugName(drugTrade));
        if(drugTrade.getBarCode()!=null){
            hospitalDrugSug.setBarCode(drugTrade.getBarCode());
        }
        if(hasManufacturer){
            hospitalDrugSug.setManufacturer(LangUtils.getString(drugTrade.getManufacturer(),""));
        }
        hospitalDrugSug.setSaleWays(buildSaleWays(hospitalDrug));
        hospitalDrugSug.setSpecification(DrugTradeServiceImpl.buildHMDrugSpec(drugTrade));
        if(StringUtils.isBlank(hospitalDrug.getSpecification())){
            hospitalDrug.setSpecification("");
        }
        hospitalDrugSug.setDataSource(1);
        return hospitalDrugSug;
    }






    //检查药品重复性验证
    //6.5.2.2重复性验证
    //1.名称、规格包装、厂家名称 相同时认为重复


    public Integer checkDrugRepeatedByNameAndSpec(HospitalDrug hospitalDrug) {
        return hospitalDrugMapper.checkDrugRepeatedByNameAndSpec(hospitalDrug);
    }
    
    public Integer getRepeatedDrugId(HospitalDrug hospitalDrug) {
        return hospitalDrugMapper.getRepeatedDrugId(hospitalDrug);
    }

    //检查药品重复性验证
    //6.5.2.2重复性验证
    //2.条形码重复时，认为重复
    //以上两种逻辑，满足任意一条时，认为重复
    public HospitalDrug checkDrugRepeatedByBarCode(HospitalDrug hospitalDrug) {
        Long drugId = hospitalDrugMapper.checkDrugRepeatedByBarCode(hospitalDrug);
        if(drugId !=null){
            return this.getDrugDetailById(drugId);
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    @SystemLogAnno(functionId = FunctionConst.DRUG_OPER_LOG,operationId = Operation.DRUG_ADD)
    public HisResponse  saveDrug(HospitalDrug hospitalDrug) {
        HisResponse hisResponse = new HisResponse();

        if (checkDrugRepeatedByNameAndSpec(hospitalDrug) > 0) {
            hisResponse.setErrorCode(50001L);
            hisResponse.setErrorMessage("药品名称、包装规格描述、包装单位、厂家名称已存在，不能重复");
            return hisResponse;
        }
        // 2016-05-03 条形码不再作为药品重复检查因素
//        if (StringUtils.isNotBlank(hospitalDrug.getBarCode()) && checkDrugRepeatedByBarCode(hospitalDrug) > 0) {
//            hisResponse.setErrorCode(50002L);
//            hisResponse.setErrorMessage("药品条形码：" + hospitalDrug.getBarCode() + "已存在，不能重复添加");
//            return hisResponse;
//        }

        saveDrugToHospitalDrug(hospitalDrug);
        Map<String,Object> body = Maps.newHashMap();
        body.put("drugId",hospitalDrug.getId());
        hisResponse.setBody(body);
        return hisResponse;
    }

    private void saveDrugToHospitalDrug(HospitalDrug hospitalDrug) {
        if(hospitalDrug.getStatus() ==null){
            hospitalDrug.setStatus(1);
        }
        hospitalDrug.setFlag(1);
        hospitalDrug.setPinyin(PinyinHelper.convertToPinyinString(hospitalDrug.getDrugName(), "", PinyinFormat.WITHOUT_TONE));
        hospitalDrug.setPinyinFirst(PinyinHelper.getShortPinyin(hospitalDrug.getDrugName()));
        Integer result = hospitalDrugMapper.insert(hospitalDrug);
        if(result>0 &&hospitalDrug.getId()!=null && hospitalDrug.getId() >0){

            drugInventoryService.initInventory(hospitalDrug);
        }
    }

    /**
     *  功能描述：功能描述： 直接售药时，将惠每药物库的药物 添加到 诊所的蒶库中
     * @param
     * @return  : 默认返回 诊所药品ID，订单中新的药品ID，并将 dataSource 设置 为 0  ，
     * 如果根据HM药物ID 查询 不到HM药品时，返回 -1，直接售药将原来记录的HM药品ID和dataSource 设置为 1 保存到订单中
     * @throws.
     * @author:  tangww
     * @createDate   2016-07-01
     *
     */
    @Override
    public Long addDrugByHMDrug(SellDrug sellDrug) {
        if(sellDrug.getDataSource() != null && sellDrug.getDataSource().equals(1)){
            if(sellDrug.getDrugId() != null){
                HospitalDrug hospitalDrug = drugTradeService.getDrugByHMDrugId(sellDrug.getDrugId());
                hospitalDrug.setId(null);
                if(hospitalDrug!=null){
                    //如果商品名不为空，将商品名补充到 药品名称中
                    if(StringUtils.isNotBlank(hospitalDrug.getTradeName())){
                        hospitalDrug.setDrugName(hospitalDrug.getDrugName()+"["+hospitalDrug.getTradeName()+"]");
                    }
                    hospitalDrug.setHospitalId(SessionUtils.getHospitalId());
//                    hospitalDrug.setHospitalId(200L);
                    hospitalDrug.setInventoryCount(0D);
                    hospitalDrug.setInventoryThreshold(0);
                    //此处，应该还有一个字段，记录药品的 处方价
                    hospitalDrug.setPrescriptionPrice(sellDrug.getSalePrice());
                    hospitalDrug.setOpenStock(0);
                    hospitalDrug.setPurchasePrice(0D);
                    saveDrugToHospitalDrug(hospitalDrug);
                    return hospitalDrug.getId();
                }else{
                    return -1L;
                }
            }
        }
        return null;
    }

    @Override
    public HisResponse saveDrugByWeixin(HospitalDrug hospitalDrug) {
        HisResponse hisResponse = new HisResponse();
        try {
        	 //入库时，需要区分 是否使用批次管理
            Integer isUseBatchManage = 0;
            HospitalConfig hospConfig = new HospitalConfig();
            hospConfig.setHospitalId(hospitalDrug.getHospitalId());
            hospConfig.setConfigType(1);
            HospitalConfig hospConf = hospitalConfigService.searchHospitalConfigList(hospConfig);
            if (hospConf != null) {
                String useBatchManag = hospConf.getAttrValue();
                if(StringUtils.isNotBlank(useBatchManag)){
                    isUseBatchManage =  Integer.parseInt(useBatchManag);
                }
            }
            if(isUseBatchManage == 1){
                // 处理拆零药品 的库存数量  -- 需要将库存 / 换算比 = 入库数量
                if(hospitalDrug.getInventoryCount() !=null && hospitalDrug.getInventoryCount()>0){
                    Double inventoryCount = hospitalDrug.getInventoryCount();
                    if(null != hospitalDrug.getOpenStock() && hospitalDrug.getOpenStock().equals(1)){
                        if(hospitalDrug.getSpecUnitaryRatio() !=null ){
                            hospitalDrug.setAddInventoryCount(Arith.div(inventoryCount,hospitalDrug.getSpecUnitaryRatio()));
                        }
                    }else{
                        hospitalDrug.setAddInventoryCount(inventoryCount);
                    }
                }else{
                    hospitalDrug.setAddInventoryCount(0D);
                }
                // 将库存设置 为 0 ，然后通过 批次入库接口，来修改 总库存
                hospitalDrug.setInventoryCount(0D);
                hospitalDrug.setInventoryThreshold(0);
                hospitalDrug.setValidityDate(DateTools.formatDateString("2999-01-01"));
                hisResponse = this.saveDrug(hospitalDrug);
                if( !hisResponse.getHasError()){
                    addBatchInstockForWeiXin(hospitalDrug);
                    return hisResponse;
                }else{
                    return hisResponse;
                }


            }else{
                //微信添加药品时， 不使用批次管理，可以保存药品
                hospitalDrug.setInventoryThreshold(0);
                hospitalDrug.setValidityDate(DateTools.formatDateString("2999-01-01"));
                hisResponse = this.saveDrug(hospitalDrug);
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
        return hisResponse;
    }

    private void addBatchInstockForWeiXin(HospitalDrug hospitalDrug) {
        BatchInstock batchInstock = new BatchInstock();
        batchInstock.setHospitalId(hospitalDrug.getHospitalId());
        batchInstock.setCreater(SessionUtils.getDoctorId());
        List<DrugBatchInstock> batchInstockList = Lists.newArrayList();
        DrugBatchInstock instock1 = new DrugBatchInstock();
        instock1.setHospitalId(hospitalDrug.getHospitalId());
        instock1.setDrugId(hospitalDrug.getId());
        instock1.setInstockCount(hospitalDrug.getAddInventoryCount());
        instock1.setInstockUnit(hospitalDrug.getSpecPackageUnit());
        instock1.setValidityDate(DateTools.formatDateString("2999-01-01"));
        instock1.setPurchasePrice(hospitalDrug.getPurchasePrice());
        instock1.setPrescriptionPrice(hospitalDrug.getPrescriptionPrice());
        batchInstockList.add(instock1);
        batchInstock.setBatchInstockList(batchInstockList);
        instockService.submitDrugInsock(batchInstock);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    @SystemLogAnno(functionId = FunctionConst.DRUG_OPER_LOG,operationId = Operation.DRUG_MODIFY)
    public HisResponse modifyDrug(HospitalDrug hospitalDrug) {
        HisResponse hisResponse = new HisResponse();
        if (checkDrugRepeatedByNameAndSpec(hospitalDrug) > 0) {
            hisResponse.setErrorCode(50001L);
            hisResponse.setErrorMessage("药品名称、包装规格描述、包装单位、厂家名称已存在，不能重复");
            return hisResponse;
        }
        // 2016-05-03 条形码不再作为药品重复检查因素
//        if (StringUtils.isNotBlank(hospitalDrug.getBarCode()) && checkDrugRepeatedByBarCode(hospitalDrug) > 1) {
//            hisResponse.setErrorCode(50002L);
//            hisResponse.setErrorMessage("药品条形码：" + hospitalDrug.getBarCode() + "已存在，不能重复添加");
//            return hisResponse;
//        }

        hospitalDrug.setModifier(SessionUtils.getDoctorId());
        hospitalDrug.setPinyin(PinyinHelper.convertToPinyinString(hospitalDrug.getDrugName(), "", PinyinFormat.WITHOUT_TONE));
        hospitalDrug.setPinyinFirst(PinyinHelper.getShortPinyin(hospitalDrug.getDrugName()));
        Integer resu = hospitalDrugMapper.updateByPrimaryKeySelective(hospitalDrug);
        if(resu >0){
            Map<String,Object> body = Maps.newHashMap();
            body.put("drugId",hospitalDrug.getId());
            hisResponse.setBody(body);
            DrugInventory dbInventory = drugInventoryService.selectByDrugId(hospitalDrug.getId());
            if(dbInventory==null){
                drugInventoryService.initInventory(hospitalDrug);
            }else  {
                //修改库存
                DrugInventory inventory = new DrugInventory();
                inventory.setDrugId(hospitalDrug.getId());
                inventory.setHospitalId(hospitalDrug.getHospitalId());
                inventory.setInventoryCount(hospitalDrug.getInventoryCount());
                inventory.setGoodsShelfCode(hospitalDrug.getGoodsShelfCode());
                inventory.setInventoryThreshold(hospitalDrug.getInventoryThreshold());
                if(hospitalDrug.getValidityDate() != null){
                    inventory.setValidityDate(hospitalDrug.getValidityDate());
                }
                drugInventoryService.modifyInventory(inventory);
                //记录库存操作日志
                /*InventoryOperateLog log = new InventoryOperateLog();
                log.setHospitalId(hospitalDrug.getHospitalId());
                log.setDrugId(hospitalDrug.getId());
                log.setOperateAction(OperateActionEnum.initInventory.getActionId());
                log.setResult(1);
                log.setBeforeAmount(dbInventory.getInventoryCount());
                log.setAmount(hospitalDrug.getInventoryCount());
                log.setUserId(SessionUtils.getDoctorId());
                inventoryOperateLogService.saveInventoryOperateLog(log);*/
            }
        }else{
            hisResponse.setErrorCode(50003L);
            hisResponse.setErrorMessage("药品" + hospitalDrug.getDrugName() + "修改失败，请重试");
            return hisResponse;
        }
        return hisResponse;
    }



    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    @SystemLogAnno(functionId = FunctionConst.DRUG_OPER_LOG,operationId = Operation.DRUG_MODIFY_PRICE)
    public HisResponse modifyInventoryAndPrice(HospitalDrug hospitalDrug) {
        /***
         * 微信入库和修改价格
         *   1.提交的参数   入库数 ，入库单位 = 包装单位
         *                进货价：  按包装单位 计价
         *                处方价：  根据是否拆零决定 处方价的 单位
         *    2.
         */
        HisResponse hisResponse = new HisResponse();
        hospitalDrug.setModifier(SessionUtils.getDoctorId());
        HospitalDrug dbHospDrug = this.getDrugById(hospitalDrug.getId());
        if(dbHospDrug==null){
            hisResponse.setErrorCode(50004L);
            hisResponse.setErrorMessage("药品ID:" + hospitalDrug.getId() + "找不到对象");
            return hisResponse;
        }
        boolean isOpenStockSale = false;
        if(null != dbHospDrug.getOpenStock() && dbHospDrug.getOpenStock().equals(1)){
            isOpenStockSale = true;
        }
        hospitalDrug.setSpecPackageUnit(dbHospDrug.getSpecPackageUnit());

        Integer modifyPriceResult=0;
        if(dbHospDrug.getPurchasePrice() !=null && dbHospDrug.getPurchasePrice() == hospitalDrug.getPurchasePrice() && dbHospDrug.getPrescriptionPrice()!=null && dbHospDrug.getPrescriptionPrice() == hospitalDrug.getPrescriptionPrice()){
            //价格未修改，不需要更新
            modifyPriceResult=1;
        }else{
            modifyPriceResult = hospitalDrugMapper.updateHospitalDrugPrice(hospitalDrug);
        }
        Integer modifyInventoryCountResult=1;
        if(hospitalDrug.getAddInventoryCount() != 0){

            Integer isUseBatchManage = 0;
            //入库时，需要区分 是否使用批次管理
            try {
            	HospitalConfig hospConfig = new HospitalConfig();
                hospConfig.setHospitalId(hospitalDrug.getHospitalId());
                hospConfig.setConfigType(1);
                HospitalConfig hospConf = hospitalConfigService.searchHospitalConfigList(hospConfig);
                if (hospConf != null) {
                    String useBatchManag = hospConf.getAttrValue();
                    if(StringUtils.isNotBlank(useBatchManag)){
                        isUseBatchManage =  Integer.parseInt(useBatchManag);
                    }
                }
			} catch (Exception e) {
				e.printStackTrace();
			}
            if(isUseBatchManage == 1){
                addBatchInstockForWeiXin(hospitalDrug);
            }else{
                //微信 入库时，也是按包装入库，如果是拆零药品，需要使用 数量 * 换算比
                if(isOpenStockSale){
                    hospitalDrug.setAddInventoryCount(Arith.mul(hospitalDrug.getAddInventoryCount() , dbHospDrug.getSpecUnitaryRatio()));
                }
                modifyInventoryCountResult = drugInventoryService.instockInventory(dbHospDrug,hospitalDrug.getAddInventoryCount());
            }
        }
        if(modifyInventoryCountResult>0&&modifyPriceResult>0){
            Map<String,Object> body = Maps.newHashMap();
            body.put("drugId",hospitalDrug.getId());
            hisResponse = HisResponse.getInstance(body);
            return hisResponse;

        }else{
            hisResponse.setErrorCode(50004L);
            hisResponse.setErrorMessage(modifyPriceResult>0?"修改药品库存失败":"修改药品价格失败");
            return hisResponse;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    @SystemLogAnno(functionId = FunctionConst.DRUG_OPER_LOG,operationId = Operation.DRUG_DELETE)
    public Integer deleteDrug(List<Long> drugIds) {
        if(CollectionUtils.isNotEmpty(drugIds)){
            for(Long drugId :drugIds){
                hospitalDrugMapper.deletByDrugId(drugId.intValue());
                DrugInventory dbInventory = drugInventoryService.selectByDrugId(drugId);
                //清理库存
                DrugInventory inventory = new DrugInventory();
                inventory.setDrugId(drugId);
                inventory.setHospitalId(SessionUtils.getHospitalId());
                inventory.setInventoryCount(0D);
                inventory.setGoodsShelfCode("");
                drugInventoryService.modifyInventory(inventory);
                /*InventoryOperateLog log = new InventoryOperateLog();
                log.setHospitalId(SessionUtils.getHospitalId());
                log.setDrugId(drugId);
                log.setOperateAction(OperateActionEnum.modifyInventory.getActionId());
                log.setResult(1);
                log.setBeforeAmount(dbInventory.getInventoryCount());
                log.setAmount(dbInventory.getInventoryCount());
                log.setUserId(SessionUtils.getDoctorId());
                inventoryOperateLogService.saveInventoryOperateLog(log);*/
            }
            return drugIds.size();
        }
        return 0;
    }


    @Override
    public HisResponse addDefaultDrugForNewHospital(Long newHospitalId,Long doctorId) {
        HisResponse hisResponse = HisResponse.getInstance();
        Date createDate = new Date();
        List<HospitalDrug> hospitalDrugs = hospitalDrugMapper.selectDrugListByDefault(TEMPLATE_HOSPITAL_ID);
        hospitalDrugs = hospitalDrugs.stream().map(hospitalDrug -> {
            hospitalDrug.setId(null);
            hospitalDrug.setHospitalId(newHospitalId);
            hospitalDrug.setCreater(doctorId);
            return hospitalDrug;
        }).collect(Collectors.toList());
        int result = hospitalDrugMapper.insertHospitalDrugByList(hospitalDrugs);
        if(result >0){
            List<HospitalDrug> newHospitalDrugs = hospitalDrugMapper.selectDrugListByHospitalId(newHospitalId);
            List<DrugInventory> drugInventories = newHospitalDrugs.stream().map(hospitalDrug -> {
                DrugInventory drugInventory = new DrugInventory();
                drugInventory.setHospitalId(newHospitalId);
                drugInventory.setDrugId(hospitalDrug.getId());
                drugInventory.setInventoryCount(0D);
                return drugInventory;
            }).collect(Collectors.toList());
            int inventoryResult = drugInventoryMapper.insertDrugInventoryByList(drugInventories);
        }else{
            hisResponse.setErrorCode(4001L);
            hisResponse.setErrorMessage("批量保存药物失败,医院ID="+newHospitalId);
        }
        return hisResponse;
    }


    /**
     *  功能描述：构建药品规格
     * @param
     * @return
     * @throws
     * @author:  tangww
     * @createDate   2016/3/3
     *
     */
    private String buildDrugSpec(HospitalDrug hospitalDrug) {
        if(StringUtils.isNotBlank(hospitalDrug.getSpecification())){
            return hospitalDrug.getSpecification();
        }else{
            StringBuffer sb = new StringBuffer();
            if(null !=hospitalDrug.getSpecMinimumDosage() && StringUtils.isNotBlank(hospitalDrug.getSpecMinimumUnit())){
                sb.append(hospitalDrug.getSpecMinimumDosage()).append(hospitalDrug.getSpecMinimumUnit()).append("/");
            }
            sb.append(hospitalDrug.getSpecUnit());
            if(null != hospitalDrug.getSpecUnitaryRatio()){
                sb.append("*").append(hospitalDrug.getSpecUnitaryRatio());
            }
            sb.append("/").append(hospitalDrug.getSpecPackageUnit());
            return sb.toString();
        }
    }

    /**
     *  功能描述：构建药品规格  ---- 在列表中显示（药品列表，直接售药列表，处方列表）
     * @param
     * @return
     * @throws
     * @author:  tangww
     * @createDate   2016/3/3
     *
     */
    public static String buildDrugSpecForList(HospitalDrug hospitalDrug) {
        if(StringUtils.isNotBlank(hospitalDrug.getSpecification())){
//            return hospitalDrug.getSpecification()+" "+hospitalDrug.getSpecPackageUnit();
            return hospitalDrug.getSpecification();
        }else{
            return hospitalDrug.getSpecPackageUnit();
        }
    }






    /**
     *  功能描述：构建使用信息
     * @param
     * @return
     * @throws
     * @author:  tangww
     * @createDate   2016/3/3
     *
     */
    public static String buildDrugUseInfo(HospitalDrug hospitalDrug) {
        String useFlag = "无";
        if(StringUtils.isNotBlank(hospitalDrug.getSingleDosage())){
            useFlag ="有";
        }else if(StringUtils.isNotBlank(hospitalDrug.getFrequency())){
            useFlag ="有";
        }else if(null != hospitalDrug.getPrescribeAmount()){
            useFlag ="有";
        }else if(StringUtils.isNotBlank(hospitalDrug.getInstruction())){
            useFlag ="有";
        }
        return useFlag;
    }

    /**
     *  功能描述：根据 是否支持拆零 设置销售单位
     * @param
     * @return
     * @throws
     * @author:  tangww
     * @createDate   2016/3/3
     *
     */
    public static String setSaleUnitByOpenStock(HospitalDrug hospitalDrug) {
        if(null != hospitalDrug.getOpenStock() && hospitalDrug.getOpenStock().equals(1)){
            return hospitalDrug.getSpecUnit();
        }else {
            return hospitalDrug.getSpecPackageUnit();
        }
    }

    private String buildHMDrugName(DrugTrade drugTrade){
        if(StringUtils.isNotBlank(drugTrade.getTradeName())){
            return drugTrade.getCommonName()+"["+drugTrade.getTradeName()+"]";
        }else {
            return drugTrade.getCommonName();
        }
    }

    /**
     *  功能描述：构建 售药方式列表
     * @param
     * @return
     * @throws
     * @author:  tangww
     * @createDate   2016/3/3
     *
     */
    private  List<SaleWayPojo> buildSaleWays(HospitalDrug hospitalDrug) {
        List<SaleWayPojo> saleWayPojos = Lists.newArrayList();
        //销售渠道  1：处方 2：直接售药

//        //当 支持拆分为空时，默认按 包装单位配置售药信息
        if(null == hospitalDrug.getOpenStock()) {
            SaleWayPojo defaultSaleWay = new SaleWayPojo();
            defaultSaleWay.setDosage(hospitalDrug.getPrescribeAmount());
            if(defaultSaleWay.getDosage() == null){
                defaultSaleWay.setDosage(1D);
            }
            Double defaultSalePrice = hospitalDrug.getPrescriptionPrice();
            defaultSaleWay.setPrice(defaultSalePrice);
            defaultSaleWay.setPurchasePrice(hospitalDrug.getPurchasePrice());
            defaultSaleWay.setUnit(LangUtils.getString(hospitalDrug.getSpecPackageUnit(),""));
            defaultSaleWay.setInventoryCount(hospitalDrug.getInventoryCount());
            saleWayPojos.add(defaultSaleWay);
        }else if(null != hospitalDrug.getOpenStock() && hospitalDrug.getOpenStock().equals(0)) {
            //0：不支持 支持拆分售药 时,按包装单位信息
            SaleWayPojo defaultSaleWay = new SaleWayPojo();
            defaultSaleWay.setDosage(hospitalDrug.getPrescribeAmount());
            if(defaultSaleWay.getDosage() == null){
                defaultSaleWay.setDosage(1D);
            }
            Double defaultSalePrice = hospitalDrug.getPrescriptionPrice();
            defaultSaleWay.setPrice(defaultSalePrice);
            defaultSaleWay.setPurchasePrice(hospitalDrug.getPurchasePrice());
            defaultSaleWay.setUnit(LangUtils.getString(hospitalDrug.getSpecPackageUnit(),""));
            defaultSaleWay.setInventoryCount(hospitalDrug.getInventoryCount());
            saleWayPojos.add(defaultSaleWay);
        }else if(null != hospitalDrug.getOpenStock() && hospitalDrug.getOpenStock().equals(1)){
            //支持拆分售药 时，需要额外计算 按 包装单位售药的价格信息
            // 按规格单位销售时,做如下配置：
            SaleWayPojo specSaleWay = new SaleWayPojo();
            specSaleWay.setDosage(hospitalDrug.getPrescribeAmount());
            if(specSaleWay.getDosage() ==null){
                specSaleWay.setDosage(1D);
            }
            Double salePrice = hospitalDrug.getPrescriptionPrice();
            specSaleWay.setPrice(salePrice);
            specSaleWay.setPurchasePrice(hospitalDrug.getPurchasePrice());
            specSaleWay.setUnit(LangUtils.getString(hospitalDrug.getSpecUnit(),""));
            specSaleWay.setInventoryCount(hospitalDrug.getInventoryCount());

            // 按包装单位销售时,做如下配置：
            SaleWayPojo packageSaleWay = new SaleWayPojo();
            packageSaleWay.setDosage(hospitalDrug.getPrescribeAmount());  //按 包装单位售药 时，此默认开药量 没有指导意义了
            if(packageSaleWay.getDosage() ==null){
                packageSaleWay.setDosage(1D);
            }
            Double packageSalePrice =  hospitalDrug.getPrescriptionPrice();
            if(hospitalDrug.getSpecUnitaryRatio() != null){
                packageSalePrice = hospitalDrug.getSpecUnitaryRatio() * hospitalDrug.getPrescriptionPrice();
            }else{
                hospitalDrug.setSpecUnitaryRatio(1);
            }
            packageSaleWay.setPrice(packageSalePrice);  // 换算比 *  价格
            packageSaleWay.setPurchasePrice(hospitalDrug.getPurchasePrice());
            packageSaleWay.setUnit(LangUtils.getString(hospitalDrug.getSpecPackageUnit(),""));
            if(null != hospitalDrug.getInventoryCount() && hospitalDrug.getInventoryCount() >0){
                packageSaleWay.setInventoryCount(hospitalDrug.getInventoryCount() / hospitalDrug.getSpecUnitaryRatio());
            }else{
                packageSaleWay.setInventoryCount(0D);
            }
            //根据 默认开药量单位判断 ，将 开药量单位 与 unit 相同的放第一个
            if(hospitalDrug.getPrescribeAmountUnit()!=null && hospitalDrug.getPrescribeAmountUnit().equals(hospitalDrug.getSpecUnit())){
                saleWayPojos.add(specSaleWay);
                saleWayPojos.add(packageSaleWay);
            }else{
                saleWayPojos.add(packageSaleWay);
                saleWayPojos.add(specSaleWay);
            }

        }
        return saleWayPojos;
    }

    @Override
    public List<HospitalDrug> searchDrugByInventoryAlarm(DrugRequest drugRequest) {
        List<HospitalDrug> list = new ArrayList<HospitalDrug>();
        try {
            list = hospitalDrugMapper.searchDrugByInventoryAlarm(drugRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Integer searchDrugByInventoryAlarmCount(DrugRequest drugRequest) {
        Integer count = 0;
        try {
            count = hospitalDrugMapper.countDrugByInventoryAlarm(drugRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

}
