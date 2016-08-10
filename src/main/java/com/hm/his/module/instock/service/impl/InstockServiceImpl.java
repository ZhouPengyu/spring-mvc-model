package com.hm.his.module.instock.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.his.framework.log.annotation.SystemLogAnno;
import com.hm.his.framework.log.constant.FunctionConst;
import com.hm.his.framework.log.constant.Operation;
import com.hm.his.framework.model.HisResponse;
import com.hm.his.framework.utils.*;
import com.hm.his.module.drug.dao.DrugInventoryMapper;
import com.hm.his.module.drug.dao.HospitalDrugMapper;
import com.hm.his.module.drug.model.DrugInventory;
import com.hm.his.module.drug.model.DrugTypeEnum;
import com.hm.his.module.drug.model.HospitalDrug;
import com.hm.his.module.drug.pojo.DrugRequest;
import com.hm.his.module.drug.service.DrugInventoryService;
import com.hm.his.module.drug.service.DrugService;
import com.hm.his.module.drug.service.impl.DrugServiceImpl;
import com.hm.his.module.instock.dao.BatchInstockMapper;
import com.hm.his.module.instock.dao.DrugBatchInstockMapper;
import com.hm.his.module.instock.model.BatchInstock;
import com.hm.his.module.instock.model.DrugBatchInstock;
import com.hm.his.module.instock.pojo.HospitalDrugInventoryPojo;
import com.hm.his.module.instock.pojo.InstockRequest;
import com.hm.his.module.instock.service.InstockService;
import com.hm.his.module.manage.service.HospitalConfigService;
import com.hm.his.module.purchase.service.PurchaseService;
import com.hm.his.module.quote.service.QuoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016-06-01
 * Time: 11:30
 * CopyRight:HuiMei Engine
 */
@Service
public class InstockServiceImpl implements InstockService {

    Logger logger = Logger.getLogger(InstockServiceImpl.class);

    @Autowired(required = false)
    BatchInstockMapper batchInstockMapper;

    @Autowired(required = false)
    DrugBatchInstockMapper drugBatchInstockMapper;

    @Autowired(required = false)
    DrugService drugService;

    @Autowired(required = false)
    HospitalDrugMapper hospitalDrugMapper;

    @Autowired(required = false)
    DrugInventoryService drugInventoryService;

    @Autowired(required = false)
    DrugInventoryMapper drugInventoryMapper;
    
    @Autowired(required = false)
    PurchaseService purchaseService;

    @Autowired(required = false)
    QuoteService quoteService;

    @Autowired(required = false)
    HospitalConfigService hospitalConfigService;

    public static void main(String[] args) {
        String batchNo="20160601102";
        String bigBatchNo = batchNo.substring(8,batchNo.length());
        System.out.println("bigBatchNo======="+bigBatchNo);
        Integer intNo = Integer.parseInt(bigBatchNo)+1;
        System.out.println("intNo======="+intNo);
        if(intNo<10){
            System.out.println("最终======="+"0"+intNo);
        }else{
            System.out.println("最终======="+intNo);
        }

    }
    /**
     *  功能描述：5.2.	提交药品入库
     *
     * @author:  tangwenwu
     * @createDate   2016/6/1
     *
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    @SystemLogAnno(functionId = FunctionConst.BATCH_INSTOCK_LOG,operationId = Operation.BATCH_INSTOCK_ADD)
    public HisResponse submitDrugInsock(BatchInstock batchInstock) {
        HisResponse hisResponse = HisResponse.getInstance();
        // 需要区分  是否使用 批次管理
        Integer isUseBatchManage = hospitalConfigService.getHospitalIsUseBatchManage();
        if (isUseBatchManage == 1) {
            // 使用批次
            if (drugBatchInsockForUseBatchManage(batchInstock, hisResponse)) return hisResponse;

            Map<String,Object> body = Maps.newHashMap();
            body.put("batchInstockId",batchInstock.getId());
            hisResponse = HisResponse.getInstance(body);
        } else {
            updateDrugInventoryForNotBatchInstock(batchInstock);
            hisResponse.setBody(null);
        }

        return hisResponse;
    }

    private boolean drugBatchInsockForUseBatchManage(BatchInstock batchInstock, HisResponse hisResponse) {
        // 1. 生成批号
        String nextBatchNo = buildNextBatchNo(batchInstock);
        batchInstock.setBatchNo(nextBatchNo);

        //2. 保存批次信息
        int addResult = batchInstockMapper.insert(batchInstock);
        if(addResult <1){
            hisResponse.setErrorCode(80002);
            hisResponse.setErrorMessage("批次入库信息保存失败");
            return true;
        }

        Integer purchaseOrderId = LangUtils.getInteger(batchInstock.getPurchaseOrderId());	//采购订单入库
        if(null != purchaseOrderId && purchaseOrderId > 0){
            try {
            	//采购订单入库  更新采购订单状态
                purchaseService.updatePurchaseOrderStatus(purchaseOrderId, 5);
                quoteService.confirmReceipt(purchaseService.getQuoteOrderId(purchaseOrderId));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //3. 处理批次入库中药品的信息
        List<DrugBatchInstock> batchInstockList = batchInstock.getBatchInstockList();
        String errorDrugIds ="";
        if(CollectionUtils.isNotEmpty(batchInstockList)){
            //4. 查询药品ID集合的基本信息和库存信息
            List<Long> drugIds = batchInstockList.stream().map(DrugBatchInstock::getDrugId).collect(Collectors.toList());
            Map<Long,HospitalDrug> hospDrugMap = drugService.searchDrugByDrugIds(drugIds);
            for(DrugBatchInstock drugBatchInstock : batchInstockList){
                Long inStokcDrugId = drugBatchInstock.getDrugId();
                HospitalDrug hospitalDrug = hospDrugMap.get(inStokcDrugId);
                if(hospitalDrug!=null){
                    //药品的进货价统一设置为最近一次进货的进货价、药品的效期统一设置为最近一次进货的效期
                    hospitalDrug.setValidityDate(drugBatchInstock.getValidityDate());
                    double addInventoryCount= 0D;
                    //5.根据药品是否支持拆零， 设置 本批次的剩余库存数  支持拆零   1:支持  0：不支持
                    if(hospitalDrug.getOpenStock()!=null && hospitalDrug.getOpenStock() ==1){
                        //5.1 支持拆零药品 ， 入库数 * 换算比= 剩余数
                        double surplusCount = drugBatchInstock.getInstockCount();
                        if(hospitalDrug.getSpecUnitaryRatio() !=null && drugBatchInstock.getInstockCount()!=null){
                            surplusCount =  Arith.mul(drugBatchInstock.getInstockCount() , hospitalDrug.getSpecUnitaryRatio());
                            drugBatchInstock.setSurplusCount(surplusCount);
                        }
                        addInventoryCount =  surplusCount;
                    }else{
                        //5.2 不支持拆零药品 ， 入库数  = 剩余数
                        addInventoryCount =  drugBatchInstock.getInstockCount();
                        drugBatchInstock.setSurplusCount(drugBatchInstock.getInstockCount());
                    }
                    //6. 更新药品库存
                    if(addInventoryCount != 0){
                        drugInventoryService.instockInventory(hospitalDrug,addInventoryCount);
                    }
                    //7. 暂时先不这样：更新药品基本信息中的 处方价和进货价，以便在切换为不使用批次入库时，有进货价

                    //8. 更新 药品的 处方价 和进货价    --解决 从使用切换 不使用时，进货价为空的问题

                    hospitalDrug.setPurchasePrice(drugBatchInstock.getPurchasePrice());
                    hospitalDrug.setPrescriptionPrice(drugBatchInstock.getPrescriptionPrice());
                    hospitalDrugMapper.updateHospitalDrugPrice(hospitalDrug);

                    drugBatchInstock.setBatchInstockId(batchInstock.getId());
                    drugBatchInstock.setHospitalId(batchInstock.getHospitalId());
                    drugBatchInstock.setFlag(1);
                    drugBatchInstockMapper.insert(drugBatchInstock);
                }else{
                    logger.error("药品ID："+inStokcDrugId+" 找不到对应的药品");
                }
            }
        }else{
            //batchInstockList 为空，设置出错信息
            hisResponse.setErrorCode(80001);
            hisResponse.setErrorMessage("批次入库中的药品列表为空");
            return true;
        }
        return false;
    }

    private void updateDrugInventoryForNotBatchInstock(BatchInstock batchInstock) {
        List<DrugBatchInstock> batchInstockList = batchInstock.getBatchInstockList();
        Integer purchaseOrderId = LangUtils.getInteger(batchInstock.getPurchaseOrderId());	//采购订单入库
        if(null != purchaseOrderId && purchaseOrderId > 0){
            try {
                //采购订单入库  更新采购订单状态
                purchaseService.updatePurchaseOrderStatus(purchaseOrderId, 5);
                quoteService.confirmReceipt(purchaseService.getQuoteOrderId(purchaseOrderId));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 不使用批次
        if(CollectionUtils.isNotEmpty(batchInstockList)){
            //4. 查询药品ID集合的基本信息和库存信息
            List<Long> drugIds = batchInstockList.stream().map(DrugBatchInstock::getDrugId).collect(Collectors.toList());
            Map<Long,HospitalDrug> hospDrugMap = drugService.searchDrugByDrugIds(drugIds);
            for(DrugBatchInstock drugBatchInstock : batchInstockList){
                Long inStokcDrugId = drugBatchInstock.getDrugId();
                HospitalDrug hospitalDrug = hospDrugMap.get(inStokcDrugId);
                if(hospitalDrug!=null){
                    //药品的进货价统一设置为最近一次进货的进货价、药品的效期统一设置为最近一次进货的效期
                    hospitalDrug.setValidityDate(drugBatchInstock.getValidityDate());
                    double addInventoryCount= 0D;
                    //5.根据药品是否支持拆零， 设置 本批次的剩余库存数  支持拆零   1:支持  0：不支持
                    if(hospitalDrug.getOpenStock()!=null && hospitalDrug.getOpenStock() ==1){
                        //5.1 支持拆零药品 ， 入库数 * 换算比= 剩余数
                        if(hospitalDrug.getSpecUnitaryRatio() !=null && drugBatchInstock.getInstockCount()!=null){
                            addInventoryCount =  Arith.mul(drugBatchInstock.getInstockCount() , hospitalDrug.getSpecUnitaryRatio());
                        }
                    }else{
                        //5.2 不支持拆零药品 ， 入库数  = 剩余数
                        addInventoryCount =  drugBatchInstock.getInstockCount();
                    }
                    //6. 更新药品库存
                    if(addInventoryCount != 0){
                        drugInventoryService.instockInventory(hospitalDrug,addInventoryCount);
                    }

                    //8. 更新 药品的 处方价 和进货价
                    hospitalDrug.setPurchasePrice(drugBatchInstock.getPurchasePrice());
                    hospitalDrug.setPrescriptionPrice(drugBatchInstock.getPrescriptionPrice());
                    hospitalDrugMapper.updateHospitalDrugPrice(hospitalDrug);

                }else{
                    logger.error("药品ID："+inStokcDrugId+" 找不到对应的药品");
                }
            }
        }
    }

    /**
     *  功能描述：是否使用批次管理 变更,从 不使用 到 使用 ：将现有库存 生成 一个批次 （将 药品上的数量，进货价和剩余库存 记录为批次信息） 需要注意拆零药品折成盒的问题
     *  从不使用 到 使用
     * @param
     * @return
     * @throws
     * @author:  tangww
     * @createDate   2016-06-16
     *
     */
    @Override
    public Long systemSubmitDrugInsock() {
        /**
         * 1. 查询药品库存 大于0 的药品列表
         * 2. 生成一个批次，提交药品批次信息
         */
        Long hospitalId = SessionUtils.getHospitalId();
        Long doctorId = SessionUtils.getDoctorId();
//        Long hospitalId = 1L;
//        Long doctorId =1L;
        if(hospitalId !=null){
            List<HospitalDrug> hospitalDrugs = hospitalDrugMapper.searchDrugInventoryMoreThanZero(hospitalId);
            if(CollectionUtils.isNotEmpty(hospitalDrugs)){
                BatchInstock batchInstock = new BatchInstock();
//                batchInstock.setSupplier("系统生成-切换为使用批次管理");

                batchInstock.setHospitalId(hospitalId);
                batchInstock.setCreater(doctorId);
                // 1. 生成批号
                String nextBatchNo = buildNextBatchNo(batchInstock);
                batchInstock.setBatchNo(nextBatchNo);

                //2. 保存批次信息
                int addResult = batchInstockMapper.insert(batchInstock);
                if(addResult > 0){
                    List<DrugBatchInstock> batchInstockList = hospitalDrugs.stream().map(hospitalDrug -> {
                        DrugBatchInstock instock = new DrugBatchInstock();
                        instock.setBatchInstockId(batchInstock.getId());
                        instock.setHospitalId(hospitalId);
                        instock.setDrugId(hospitalDrug.getId());
                        instock.setSurplusCount(hospitalDrug.getInventoryCount());
                        instock.setInstockUnit(hospitalDrug.getSpecPackageUnit());

                        //5.根据药品是否支持拆零， 设置 本批次的入库数量 单位为包装单位   支持拆零   1:支持  0：不支持
                        if(hospitalDrug.getOpenStock()!=null && hospitalDrug.getOpenStock() ==1){
                            //5.1 支持拆零药品 ，入库数(按包装单位) = 库存数 / 换算比
                            Double instockCount = 0D;
                            if(hospitalDrug.getInventoryCount() !=null && hospitalDrug.getSpecUnitaryRatio() !=null){
                                instockCount = Arith.div(hospitalDrug.getInventoryCount() , hospitalDrug.getSpecUnitaryRatio());
                            }
                            instock.setInstockCount(instockCount);
                        }else{
                            instock.setInstockCount(hospitalDrug.getInventoryCount());
                        }

                        instock.setValidityDate(hospitalDrug.getValidityDate());
                        instock.setPurchasePrice(hospitalDrug.getPurchasePrice());
                        instock.setFlag(0);
                        return instock;
                    }).collect(Collectors.toList());
                    // 3 .批量 插入批次药品 信息
                    drugBatchInstockMapper.batchInsertByList(batchInstockList);
                }
                return batchInstock.getId();
            }
        }
        return null;
    }

    /**
     *  功能描述：// 是否使用批次管理 变更,从 不使用 到 使用 ：将批次中的剩余库存更新 为 0
     * @param
     * @return
     * @throws
     * @author:  tangww
     * @createDate   2016-06-16
     *
     */
    @Override
    public int systemCleanInsockDrugSurplusCount() {
        Long hospitalId = SessionUtils.getHospitalId();
//        Long hospitalId = 1L;
        if(hospitalId !=null) {
            return drugBatchInstockMapper.cleanInsockDrugSurplusCountByHospitalId(hospitalId);
        }else{
            return 0;
        }
    }

    /**
     *  功能描述：批次编号 规则：YYYYMMDD+两位 或更多
     * @author:  tangww
     * @createDate   2016-06-01
     *
     */
    private String buildNextBatchNo(BatchInstock batchInstock) {
        String currentDate = DateTools.getShortDateFormatDate();
        String batchNo="01";
        BatchInstock bigBatchInstock  = batchInstockMapper.searchCurrentDateBiggestBatchNo(batchInstock.getHospitalId());
        if(bigBatchInstock !=null){
//            System.out.println("beforeBatchNo======="+bigBatchInstock.getBatchNo());
            String bigBatchNo = bigBatchInstock.getBatchNo().substring(8,bigBatchInstock.getBatchNo().length());
            Integer nextBatchNo = Integer.parseInt(bigBatchNo)+1;
            if(nextBatchNo<10){
                batchNo = "0"+nextBatchNo;
            }else{
                batchNo = nextBatchNo.toString();
            }
        }

        String nextBatchNoStr = currentDate +batchNo;
//        System.out.println("nextBatchNoStr======="+nextBatchNoStr);
        return nextBatchNoStr;
    }

    @Override
    public void searchDrugInventoryList(DrugRequest drugRequest) {

    }

    @Override
    public List<HospitalDrugInventoryPojo> searchDrugInventoryListByBatch(DrugRequest drugRequest) {
        List<HospitalDrugInventoryPojo> hospitalDrugInventoryPojos = drugBatchInstockMapper.searchDrugInventoryListByBatch(drugRequest);
        return  hospitalDrugInventoryPojos.stream().map(hospitalDrug -> {
            hospitalDrug.setSpecification(DrugServiceImpl.buildDrugSpecForList(hospitalDrug));
            hospitalDrug.setManufacturer(LangUtils.getString(hospitalDrug.getManufacturer(),""));
            hospitalDrug.setDrugTypeName(DrugTypeEnum.getDrugTypeNameByType(hospitalDrug.getDrugType()));
            hospitalDrug.setSaleUnit(LangUtils.getString(DrugServiceImpl.setSaleUnitByOpenStock(hospitalDrug),""));
            return hospitalDrug;
        }).collect(Collectors.toList());
    }

    @Override
    public int countDrugInventoryListByBatch(DrugRequest drugRequest) {
        return drugBatchInstockMapper.countDrugInventoryListByBatch(drugRequest);
    }

    @Override
    public HospitalDrugInventoryPojo searchBatchDrugInventoryDetail(InstockRequest instockRequest) {
        HospitalDrugInventoryPojo hospitalDrugInventoryPojo = drugBatchInstockMapper.searchBatchDrugInventoryDetail(instockRequest);
        if(hospitalDrugInventoryPojo !=null){
            hospitalDrugInventoryPojo.setDrugTypeName(DrugTypeEnum.getDrugTypeNameByType(hospitalDrugInventoryPojo.getDrugType()));
            hospitalDrugInventoryPojo.setStatusDesc(hospitalDrugInventoryPojo.getStatus()!=null && hospitalDrugInventoryPojo.getStatus() == 1 ? "启用":"禁用");
            hospitalDrugInventoryPojo.setIsOTCDesc(hospitalDrugInventoryPojo.getIsOtc() !=null && hospitalDrugInventoryPojo.getIsOtc() == 1 ? "是":"否");
            hospitalDrugInventoryPojo.setSaleUnit(DrugServiceImpl.setSaleUnitByOpenStock(hospitalDrugInventoryPojo));
            if(hospitalDrugInventoryPojo.getValidityDate() != null)
                hospitalDrugInventoryPojo.setValidityDateStr(DateTools.getDate(DateTools.ONLY_DATE_FORMAT,hospitalDrugInventoryPojo.getValidityDate()));
        }
        return  hospitalDrugInventoryPojo;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    @SystemLogAnno(functionId = FunctionConst.BATCH_INSTOCK_LOG,operationId = Operation.BATCH_INSTOCK_MODIFY)
    public HisResponse modifyBatchDrugInventory(DrugBatchInstock drugBatchInstock) {
        if(drugBatchInstock.getValidityDate() !=null){
            System.out.println("modifyBatchDrugInventory==getValidityDate="+ DateTools.getDate(DateTools.COMMON_DATE_FORMAT,drugBatchInstock.getValidityDate()));
        }else{
            drugBatchInstock.setValidityDate(DateTools.formatDateString("2999-01-01"));
        }
        HisResponse hisResponse = HisResponse.getInstance();
        // 1. 查询出药品原来的库存信息
        Long drugId = drugBatchInstock.getDrugId();
        HospitalDrug hospitalDrug = drugService.getDrugDetailById(drugId);
        if(hospitalDrug==null){
            hisResponse.setErrorCode(80001);
            hisResponse.setErrorMessage("药品ID找不到药品信息"+drugId);
        }
        // 2. 查询出药品当前批次的信息
        DrugBatchInstock oldDrugBI = drugBatchInstockMapper.selectByPrimaryKey(drugBatchInstock.getId());
        if(oldDrugBI==null){
            hisResponse.setErrorCode(80002);
            hisResponse.setErrorMessage("药品批次ID找不到批次信息"+drugBatchInstock.getId());
        }
        //3. 比较原来的批次药品 的剩余数量 与 修改后的剩余库存数量
        if(oldDrugBI.getSurplusCount() > drugBatchInstock.getSurplusCount()){
            Double toLoseCount = oldDrugBI.getSurplusCount() - drugBatchInstock.getSurplusCount();
            /*// 3.2 根据药品是否支持拆零， 设置 本批次的剩余库存数  支持拆零   1:支持  0：不支持
            if(hospitalDrug.getOpenStock()!=null && hospitalDrug.getOpenStock() ==1){
                //3.2.1 支持拆零药品 ， 入库数 * 换算比= 剩余数
                toLoseCount =  toLoseCount * hospitalDrug.getSpecUnitaryRatio();

            }else{
                //3.2.2  不支持拆零药品 ， 入库数  = 剩余数
            }*/

            // 3.1 当 旧的药品批次剩余数量大于 新的剩余数量时，需要 减总库存，并更新进货价和有效期
            hospitalDrug.setInventoryCount(hospitalDrug.getInventoryCount() - toLoseCount);
            if(hospitalDrug.getInventoryCount() <0){
                hospitalDrug.setInventoryCount(0D);
            }

        }else if(oldDrugBI.getSurplusCount() < drugBatchInstock.getSurplusCount()){
            // 3.2当 旧的药品批次剩余数量 小于 新的剩余数量时，需要 增加总库存，并更新进货价和有效期
            Double toAddCount = drugBatchInstock.getSurplusCount() - oldDrugBI.getSurplusCount();

            /*// 3.2 根据药品是否支持拆零， 设置 本批次的剩余库存数  支持拆零   1:支持  0：不支持
            if(hospitalDrug.getOpenStock()!=null && hospitalDrug.getOpenStock() ==1){
                //3.2.1 支持拆零药品 ， 入库数 * 换算比= 剩余数
                toAddCount =  toAddCount * hospitalDrug.getSpecUnitaryRatio();
            }else{
                //3.2.2  不支持拆零药品 ， 入库数  = 剩余数
            }*/
            // 3.1 当 旧的药品批次剩余数量大于 新的剩余数量时，需要 加总库存，并更新进货价和有效期

            hospitalDrug.setInventoryCount(hospitalDrug.getInventoryCount() + toAddCount);
        }
        //4. 更新药品批次表中的信息
        int modifyResult = drugBatchInstockMapper.updateByPrimaryKeySelective(drugBatchInstock);


        if(modifyResult >0){
            //5. 更新药品库存
            DrugInventory drugInventory = new DrugInventory();
            drugInventory.setHospitalId(drugBatchInstock.getHospitalId());
            drugInventory.setDrugId(drugBatchInstock.getDrugId());
            drugInventory.setInventoryCount(hospitalDrug.getInventoryCount());
            drugInventoryService.modifyInventory(drugInventory);

            //6. 当药品的 处方价有变化时，更新药品的处方价
            if(null != drugBatchInstock.getPrescriptionPrice() && drugBatchInstock.getPrescriptionPrice() != hospitalDrug.getPrescriptionPrice()){
                hospitalDrug.setPurchasePrice(null);
                hospitalDrug.setPrescriptionPrice(drugBatchInstock.getPrescriptionPrice());
                hospitalDrugMapper.updateHospitalDrugPrice(hospitalDrug);
            }

        }else{
            hisResponse.setErrorCode(80003);
            hisResponse.setErrorMessage("修改批次入库信息失败");
        }

        Map<String,Object> body = Maps.newHashMap();
        body.put("drugBatchInstockId",drugBatchInstock.getId());
        hisResponse = HisResponse.getInstance(body);
        return hisResponse;
    }

    @Override
    public List<HospitalDrug> searchDrugListForInventoryWarning(InstockRequest instockRequest) {
        DrugRequest drugRequest = new DrugRequest();
        drugRequest.setPageSize(instockRequest.getPageSize());
        drugRequest.setHospitalId(instockRequest.getHospitalId());
        drugRequest.setDrugType(instockRequest.getDrugType());
        List<HospitalDrug> hospitalDrugs = drugService.searchDrugByInventoryAlarm(drugRequest);

        hospitalDrugs.stream().forEach(hospitalDrug -> {
            hospitalDrug.setDrugTypeName(DrugTypeEnum.getDrugTypeNameByType(hospitalDrug.getDrugType()));
            if(hospitalDrug.getInventoryCount() ==null) hospitalDrug.setInventoryCount(0D);
            //支持拆零   1:支持  0：不支持
            hospitalDrug.setSaleUnit(LangUtils.getString(DrugServiceImpl.setSaleUnitByOpenStock(hospitalDrug),""));
            hospitalDrug.setManufacturer(LangUtils.getString(hospitalDrug.getManufacturer(),""));
        });
        return hospitalDrugs;
    }

    @Override
    public int countDrugListForInventoryWarning(InstockRequest instockRequest) {
        DrugRequest drugRequest = new DrugRequest();
        drugRequest.setPageSize(instockRequest.getPageSize());
        drugRequest.setHospitalId(instockRequest.getHospitalId());
        drugRequest.setDrugType(instockRequest.getDrugType());
        return drugService.searchDrugByInventoryAlarmCount(drugRequest);
    }

    @Override
    public List<HospitalDrugInventoryPojo> searchDrugListForValidityWarning(InstockRequest instockRequest) {
        List<HospitalDrugInventoryPojo> hospitalDrugs = Lists.newArrayList();
        if(instockRequest.getIsUseBatchManage() == null){
            hospitalDrugs = drugInventoryMapper.searchDrugListForValidityWarning(instockRequest);
        }else if(instockRequest.getIsUseBatchManage()== 0){
            hospitalDrugs = drugInventoryMapper.searchDrugListForValidityWarning(instockRequest);
        }else{
            hospitalDrugs = drugBatchInstockMapper.searchDrugListForValidityWarning(instockRequest);
            if(CollectionUtils.isNotEmpty(hospitalDrugs)){
                hospitalDrugs.stream().forEach(hospitalDrugInventoryPojo -> hospitalDrugInventoryPojo.setInventoryCount(hospitalDrugInventoryPojo.getSurplusCount()));
            }
        }
        if(CollectionUtils.isNotEmpty(hospitalDrugs)){
            final Date currentDate = DateUtil.addDay(new Date(),1);
            hospitalDrugs.stream().filter(hospitalDrug -> hospitalDrug.getValidityDate().before(currentDate)).forEach(hospitalDrug1 -> hospitalDrug1.setStatus(1));
            hospitalDrugs.stream().forEach(hospitalDrugInventoryPojo -> {
                hospitalDrugInventoryPojo.setDrugTypeName(DrugTypeEnum.getDrugTypeNameByType(hospitalDrugInventoryPojo.getDrugType()));
                hospitalDrugInventoryPojo.setValidityDateStr(DateTools.getDate(DateTools.ONLY_DATE_FORMAT,hospitalDrugInventoryPojo.getValidityDate()));
                //支持拆零   1:支持  0：不支持
                if(hospitalDrugInventoryPojo.getOpenStock()!=null && hospitalDrugInventoryPojo.getOpenStock() == 1){
                    hospitalDrugInventoryPojo.setSaleUnit(hospitalDrugInventoryPojo.getSpecUnit());
                }else{
                    hospitalDrugInventoryPojo.setSaleUnit(hospitalDrugInventoryPojo.getSpecPackageUnit());
                }
            });
        }
        return hospitalDrugs;
    }

    @Override
    public int countDrugListForValidityWarning(InstockRequest instockRequest) {
        if(instockRequest.getIsUseBatchManage() == null){
            return drugInventoryMapper.countDrugListForValidityWarning(instockRequest);
        }else if(instockRequest.getIsUseBatchManage()== 0){
            return drugInventoryMapper.countDrugListForValidityWarning(instockRequest);
        }else{
            return  drugBatchInstockMapper.countDrugListForValidityWarning(instockRequest);
        }

    }

    @Override
    public List<HospitalDrugInventoryPojo> searchBatchInstockLog(InstockRequest instockRequest) {
        List<HospitalDrugInventoryPojo> hospitalDrugInventoryPojos = drugBatchInstockMapper.searchBatchInstockLog(instockRequest);
        if(CollectionUtils.isNotEmpty(hospitalDrugInventoryPojos)){
            hospitalDrugInventoryPojos.stream().forEach(hospitalDrugInventoryPojo -> {
                hospitalDrugInventoryPojo.setValidityDateStr(DateTools.getDate("yyyy-MM-dd",hospitalDrugInventoryPojo.getCreateDate()));
                hospitalDrugInventoryPojo.setDrugTypeName(DrugTypeEnum.getDrugTypeNameByType(hospitalDrugInventoryPojo.getDrugType()));
                if(hospitalDrugInventoryPojo.getInstockCount() != null && hospitalDrugInventoryPojo.getPurchasePrice()!=null){
                    hospitalDrugInventoryPojo.setTotalPrice(Arith.mul(hospitalDrugInventoryPojo.getInstockCount(), hospitalDrugInventoryPojo.getPurchasePrice()));
                }else {
                    hospitalDrugInventoryPojo.setTotalPrice(0D);
                }
                if(hospitalDrugInventoryPojo.getSpecification()  == null){
                    hospitalDrugInventoryPojo.setSpecification(hospitalDrugInventoryPojo.getSpecPackageUnit());
                }
            });
        }
        return  hospitalDrugInventoryPojos;
    }

    @Override
    public int countBatchInstockLog(InstockRequest instockRequest) {
        return drugBatchInstockMapper.countBatchInstockLog(instockRequest);

    }
}
