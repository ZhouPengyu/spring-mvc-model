package com.hm.his.module.drug.service.impl;

import com.hm.his.framework.utils.Arith;
import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.module.drug.dao.DrugInventoryMapper;
import com.hm.his.module.drug.model.DrugInventory;
import com.hm.his.module.drug.model.HospitalDrug;
import com.hm.his.module.drug.model.InventoryOperateLog;
import com.hm.his.module.drug.model.OperateActionEnum;
import com.hm.his.module.drug.pojo.InventoryOperatePojo;
import com.hm.his.module.drug.pojo.SaleWayPojo;
import com.hm.his.module.drug.service.DrugInventoryService;
import com.hm.his.module.drug.service.DrugService;
import com.hm.his.module.drug.service.InventoryOperateLogService;
import com.hm.his.module.instock.dao.BatchDrugOrderLinkMapper;
import com.hm.his.module.instock.dao.DrugBatchInstockMapper;
import com.hm.his.module.instock.model.BatchDrugOrderLink;
import com.hm.his.module.instock.model.DrugBatchInstock;
import com.hm.his.module.manage.service.HospitalConfigService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/3/14
 * Time: 10:13
 * CopyRight:HuiMei Engine
 */
@Service
public class DrugInventoryServiceImpl implements DrugInventoryService {

    Logger logger = Logger.getLogger(DrugInventoryServiceImpl.class);

    @Autowired(required = false)
    DrugInventoryMapper drugInventoryMapper;

    @Autowired(required = false)
    DrugBatchInstockMapper drugBatchInstockMapper;

    @Autowired(required = false)
    BatchDrugOrderLinkMapper batchDrugOrderLinkMapper;

    @Autowired
    InventoryOperateLogService inventoryOperateLogService;
    @Autowired
    DrugService drugService;

    @Autowired(required = false)
    HospitalConfigService hospitalConfigService;

    @Override
    public DrugInventory selectByDrugId(Long drugId){
        return  drugInventoryMapper.selectByDrugId(drugId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Integer initInventory(HospitalDrug hospitalDrug) {
        DrugInventory inventory = new DrugInventory();
        inventory.setDrugId(hospitalDrug.getId());
        inventory.setHospitalId(hospitalDrug.getHospitalId());
        inventory.setInventoryCount(hospitalDrug.getInventoryCount());
        inventory.setGoodsShelfCode(hospitalDrug.getGoodsShelfCode());
        inventory.setInventoryThreshold(hospitalDrug.getInventoryThreshold());
        if(hospitalDrug.getValidityDate() != null){
            inventory.setValidityDate(hospitalDrug.getValidityDate());
        }
        drugInventoryMapper.insert(inventory);
        //记录库存操作日志
       /* InventoryOperateLog log = new InventoryOperateLog();
        log.setHospitalId(hospitalDrug.getHospitalId());
        log.setDrugId(hospitalDrug.getId());
        log.setOperateAction(OperateActionEnum.initInventory.getActionId());
        log.setResult(1);
        log.setBeforeAmount(0D);
        log.setAmount(hospitalDrug.getInventoryCount());
        log.setUserId(SessionUtils.getDoctorId());
        inventoryOperateLogService.saveInventoryOperateLog(log);*/
        return 1;
    }

    @Override
    public Integer modifyInventory(DrugInventory inventory) {
        DrugInventory drugInventory = drugInventoryMapper.selectByDrugId(inventory.getDrugId());
        if(drugInventory!=null){
            return drugInventoryMapper.updateByHospitalIdAndDrugId(inventory);
        }else{
            return drugInventoryMapper.insert(inventory);
        }
    }

    /**
     *  功能描述：删减库存---不使用批次管理
     * @param
     * @return
     * @throws
     * @author:  tangww
     * @createDate   2016/3/14
     *
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public InventoryOperatePojo cutInventory(InventoryOperatePojo inventoryOperatePojo) {
        if(null == inventoryOperatePojo.getDrugId() || null == inventoryOperatePojo.getAmount())
            return inventoryOperatePojo;
        HospitalDrug hospitalDrug = drugService.getDrugById(inventoryOperatePojo.getDrugId());
        if(hospitalDrug ==null){
            logger.error("根据药品ID无法找到药品信息，药品ID:"+inventoryOperatePojo.getDrugId());
            return inventoryOperatePojo;
        }
        //支持拆分售药 时，需要额外计算 按 包装单位售药时的 数量  ---需要将 进货价 /  换算比，得到拆零的进货价
        if(null != hospitalDrug.getOpenStock() && hospitalDrug.getOpenStock().equals(1)){
            if(hospitalDrug.getSpecUnitaryRatio() !=null){
                hospitalDrug.setPurchasePrice(Arith.div(hospitalDrug.getPurchasePrice() , hospitalDrug.getSpecUnitaryRatio()));
            }
            // 按包装单位出售时，需要使用 数量 * 换算比
            if(hospitalDrug.getSpecPackageUnit().equals(inventoryOperatePojo.getUnit())){
                if(hospitalDrug.getSpecUnitaryRatio() !=null){
                    inventoryOperatePojo.setAmount(Arith.mul(inventoryOperatePojo.getAmount() , hospitalDrug.getSpecUnitaryRatio()));
                }
            }
        }

        //修改库存
        DrugInventory inventory = new DrugInventory();
        inventory.setDrugId(hospitalDrug.getId());
        inventory.setHospitalId(hospitalDrug.getHospitalId());


        inventory.setInventoryCount(inventoryOperatePojo.getAmount());
        //产品要求 ，库存不能出现 负数， 将库存设置 为 0
        if(null != hospitalDrug.getInventoryCount() && inventoryOperatePojo.getAmount() > hospitalDrug.getInventoryCount()){
            inventory.setInventoryCount(hospitalDrug.getInventoryCount());
        }
        inventoryOperatePojo.setTotalPurchasePrice(Arith.mul(inventoryOperatePojo.getAmount() , hospitalDrug.getPurchasePrice()));

        drugInventoryMapper.cutInventoryByHospitalIdAndDrugId(inventory);

        /*InventoryOperateLog log = new InventoryOperateLog();
        log.setHospitalId(inventoryOperatePojo.getHospitalId());
        log.setDrugId(inventoryOperatePojo.getDrugId());
        log.setOperateAction(OperateActionEnum.cutInventory.getActionId());
        log.setResult(1);
        log.setBeforeAmount(hospitalDrug.getInventoryCount());
        log.setAmount(inventoryOperatePojo.getAmount());
        log.setUserId(SessionUtils.getDoctorId());
        inventoryOperateLogService.saveInventoryOperateLog(log);*/
        return inventoryOperatePojo;
    }

    /**
     *  功能描述：删减库存--使用批次管理
     * @param
     * @return
     * @throws
     * @author:  tangww
     * @createDate   2016/3/14
     *
     */
    @Transactional(propagation = Propagation.REQUIRED)
    private InventoryOperatePojo cutInventoryByBatchInsock(InventoryOperatePojo inventoryOperatePojo) {
         /*
        *  主要业务处理流程
        * 1. 查询出本药品批次中剩余数量不为 0 的批次列表，按批次药品ID 排序 （正序）
        * 2. 判断库存操作数 与 当前库存总数的 大小
        * 3. 循环药品批次列表，更新 剩余数量和 记录 批次药品订单关系
        *    3.1 更新药品批次的剩余数量
        *    3.2 批次药品订单关系
        *    3.3 订单本次的总进货价
        *    3.4 异常情况 时，记录日志（如 药品批次的总剩余数  小于 此次库存操作数）
        * 4. 修改库存总量
        *
        * */
        if(null == inventoryOperatePojo.getDrugId() || null == inventoryOperatePojo.getAmount())
            return inventoryOperatePojo;
        HospitalDrug hospitalDrug = drugService.getDrugById(inventoryOperatePojo.getDrugId());
        if(hospitalDrug ==null){
            logger.error("根据药品ID无法找到药品信息，药品ID:"+inventoryOperatePojo.getDrugId());
            return inventoryOperatePojo;
        }
        String inventoryUnit = inventoryOperatePojo.getUnit();
        boolean isOpenStockSale = false;
        double currentPurchasePrice = 0D; //本药品的进货价
        //支持拆分售药 时，需要额外计算 按 包装单位售药时的 数量
        if(null != hospitalDrug.getOpenStock() && hospitalDrug.getOpenStock().equals(1)){
            isOpenStockSale = true;
            // 按包装单位出售时，需要使用 数量 * 换算比
            if(hospitalDrug.getSpecPackageUnit().equals(inventoryOperatePojo.getUnit())){
                if(hospitalDrug.getSpecUnitaryRatio() !=null){
                    inventoryOperatePojo.setAmount(Arith.mul(inventoryOperatePojo.getAmount() , hospitalDrug.getSpecUnitaryRatio()));

                }
                inventoryUnit = hospitalDrug.getSpecUnit();
            }
        }



        List<DrugBatchInstock> drugBatchInstocks = drugBatchInstockMapper.searchDrugBatchInstockAndSurplusGreaterThanZero(inventoryOperatePojo.getDrugId());
        Double totalPurchasePrice = 0D;// 本药品 本次的所有的进货价

        if(CollectionUtils.isNotEmpty(drugBatchInstocks)){
            //总计 可用的剩余库存数
            Double availableSurplusCount = drugBatchInstocks.stream().mapToDouble(DrugBatchInstock::getSurplusCount).sum();
            //定义 当前库存操作数 ，默认为 总库存操作数，随着 批次不断递减
            Double currentAmount = inventoryOperatePojo.getAmount();

            if(availableSurplusCount >= inventoryOperatePojo.getAmount() ){
                // 可用的剩余库存数 >=  操作库存总数 可直接更新即可
                for(DrugBatchInstock drugBatchInstock :drugBatchInstocks){
                    // 计算 拆零药品 的进货价 = 药品批次的进货价 / 药品换算比
                    currentPurchasePrice = getCurrentPurchasePrice(hospitalDrug, isOpenStockSale, drugBatchInstock);
                    Double linkAmount = drugBatchInstock.getSurplusCount();
                    if(drugBatchInstock.getSurplusCount() >= currentAmount){
                        //更新本批次的剩余数量
                        drugBatchInstock.setSurplusCount(drugBatchInstock.getSurplusCount() - currentAmount);
                        linkAmount = currentAmount;
                        drugBatchInstockMapper.updateSurplusCountByDrugBatchInstock(drugBatchInstock);// 要改
                        //设置 本药品 所有进货价
                        totalPurchasePrice += Arith.mul(linkAmount , currentPurchasePrice);
                        addBatchDrugOrderLink(inventoryOperatePojo, inventoryUnit, drugBatchInstock, linkAmount);
                        break;
                    }else{
                        //更新本批次的剩余数量
                        drugBatchInstock.setSurplusCount(0D);
                        drugBatchInstockMapper.updateSurplusCountByDrugBatchInstock(drugBatchInstock);// 要改
                        currentAmount = Math.abs(linkAmount - currentAmount);
                        // 计算 拆零药品 的进货价 = 药品批次的进货价 / 药品换算比
                        currentPurchasePrice = getCurrentPurchasePrice(hospitalDrug, isOpenStockSale,  drugBatchInstock);
                        //设置 本药品 所有进货价
                        totalPurchasePrice += Arith.mul(linkAmount , currentPurchasePrice);
                        addBatchDrugOrderLink(inventoryOperatePojo, inventoryUnit, drugBatchInstock, linkAmount);
                    }

                }
            }else{
                // 可用的剩余库存数 <  操作库存总数  在记录 批次药品订单记录里，需要将 虚多出来看库存记录到 批次药品订单记录 表里
                boolean isAddVirtualInventory = false; // 是否已添加了虚拟多出来的库存记录
                Double virtualInventorCount  =  inventoryOperatePojo.getAmount() - availableSurplusCount;
                for(DrugBatchInstock drugBatchInstock :drugBatchInstocks){
                    Double linkAmount = drugBatchInstock.getSurplusCount();
                    //更新本批次的剩余数量
                    drugBatchInstock.setSurplusCount(0D);
                    drugBatchInstockMapper.updateSurplusCountByDrugBatchInstock(drugBatchInstock);// 要改

                    if(!isAddVirtualInventory){
                        linkAmount = linkAmount + virtualInventorCount;
                        isAddVirtualInventory = true;
                    }
                    // 计算 拆零药品 的进货价 = 药品批次的进货价 / 药品换算比
                    currentPurchasePrice = getCurrentPurchasePrice(hospitalDrug, isOpenStockSale,  drugBatchInstock);
                    //设置 本药品 所有进货价
                    totalPurchasePrice += Arith.mul(linkAmount , currentPurchasePrice);
                    addBatchDrugOrderLink(inventoryOperatePojo, inventoryUnit, drugBatchInstock, linkAmount);
                }

            }
        }else{
            logger.error("根据药品ID："+inventoryOperatePojo.getDrugId()+" 查询，没有可用的批次信息！");
            //如果 没有 可用的批次药品，则将本药品 最近的一个批次 设置为 此次订单的批次药品
            DrugBatchInstock  dbDrugBatchInstock = drugBatchInstockMapper.searchDrugBatchInstockByDrugId(inventoryOperatePojo.getDrugId());
            if(dbDrugBatchInstock !=null){
                // 计算 拆零药品 的进货价 = 药品批次的进货价 / 药品换算比
                currentPurchasePrice = getCurrentPurchasePrice(hospitalDrug, isOpenStockSale, dbDrugBatchInstock);
                totalPurchasePrice= Arith.mul(inventoryOperatePojo.getAmount() , currentPurchasePrice);
                //将本药品 最近的一个批次 设置为 此次订单的 批次药品销售记录 ，以备 退货时处理。
                addBatchDrugOrderLink(inventoryOperatePojo, inventoryUnit, dbDrugBatchInstock, inventoryOperatePojo.getAmount());
            }
        }
        inventoryOperatePojo.setTotalPurchasePrice(totalPurchasePrice);

        //修改库存
        DrugInventory inventory = new DrugInventory();
        inventory.setDrugId(hospitalDrug.getId());
        inventory.setHospitalId(hospitalDrug.getHospitalId());

        //  把数量 设置 到 库存字段中，在数据库中 对库存进行减操作
        inventory.setInventoryCount(inventoryOperatePojo.getAmount());
        //产品要求 ，库存不能出现 负数， 将库存设置 为 0
        if(null != hospitalDrug.getInventoryCount() && inventoryOperatePojo.getAmount() > hospitalDrug.getInventoryCount()){
            inventory.setInventoryCount(hospitalDrug.getInventoryCount());
        }


        drugInventoryMapper.cutInventoryByHospitalIdAndDrugId(inventory);

        /*InventoryOperateLog log = new InventoryOperateLog();
        log.setHospitalId(inventoryOperatePojo.getHospitalId());
        log.setDrugId(inventoryOperatePojo.getDrugId());
        log.setOperateAction(OperateActionEnum.cutInventory.getActionId());
        log.setResult(1);
        log.setBeforeAmount(hospitalDrug.getInventoryCount());
        log.setAmount(inventoryOperatePojo.getAmount());
        log.setUserId(SessionUtils.getDoctorId());
        inventoryOperateLogService.saveInventoryOperateLog(log);*/
        return inventoryOperatePojo;
    }

    private double getCurrentPurchasePrice(HospitalDrug hospitalDrug, boolean isOpenStockSale,  DrugBatchInstock drugBatchInstock) {
        double currentPurchasePrice = 0D;
        if(isOpenStockSale){
            if(hospitalDrug.getSpecUnitaryRatio() != null && drugBatchInstock.getPurchasePrice() !=null)
                currentPurchasePrice = Arith.div(drugBatchInstock.getPurchasePrice() , hospitalDrug.getSpecUnitaryRatio());
        }else if(drugBatchInstock.getPurchasePrice() !=null){
            currentPurchasePrice = drugBatchInstock.getPurchasePrice();
        }
        drugBatchInstock.setPurchasePrice(currentPurchasePrice);
        return currentPurchasePrice;
    }

    /**
     *  功能描述：记录药品批次的销售记录信息
     * @author:  tangww
     * @createDate   2016-06-15
     *
     */
    private void addBatchDrugOrderLink(InventoryOperatePojo inventoryOperatePojo, String inventoryUnit, DrugBatchInstock drugBatchInstock, Double linkAmount) {
        BatchDrugOrderLink batchDrugOrderLink = new BatchDrugOrderLink();
        batchDrugOrderLink.setAmount(linkAmount);
        batchDrugOrderLink.setBatchId(drugBatchInstock.getId());
        batchDrugOrderLink.setStatus(1);
        batchDrugOrderLink.setDrugId(inventoryOperatePojo.getDrugId());
        batchDrugOrderLink.setPurchasePrice(drugBatchInstock.getPurchasePrice());
        batchDrugOrderLink.setOrderDrugId(inventoryOperatePojo.getOrderDrugId());
        batchDrugOrderLink.setSaleUnit(inventoryUnit);
        batchDrugOrderLinkMapper.insert(batchDrugOrderLink);
    }


    /**
     *  功能描述：更新 药品批次的销售记录的状态
     * @author:  tangww
     * @createDate   2016-06-15
     *
     */
    private void updateBatchDrugOrderLinkStatus(BatchDrugOrderLink batchDrugOrderLink) {
        batchDrugOrderLink.setStatus(0);
        batchDrugOrderLinkMapper.updateByPrimaryKeySelective(batchDrugOrderLink);
    }

    @Override
    public InventoryOperatePojo cutHospitalDrugInventory(InventoryOperatePojo inventoryOperatePojo) {
    	try {
    		HttpSession session = SessionUtils.getSession();
            Integer isUseBatchManage = hospitalConfigService.getHospitalIsUseBatchManage();
//            String isUseBatchManage ="1";

            //使用批次管理
            if(isUseBatchManage !=null && isUseBatchManage.equals(1)){
                return cutInventoryByBatchInsock(inventoryOperatePojo);
            }else{// 不使用批次管理
                return this.cutInventory(inventoryOperatePojo);
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }

    @Override
    public InventoryOperatePojo returnHospitalDrugInventory(InventoryOperatePojo inventoryOperatePojo) {
        HttpSession session = SessionUtils.getSession();
        Integer isUseBatchManage = hospitalConfigService.getHospitalIsUseBatchManage();
//        String isUseBatchManage ="1";
        //使用批次管理
        if(isUseBatchManage !=null && isUseBatchManage.equals(1)){
            return returnInventoryByBatchInsock(inventoryOperatePojo);
        }else{// 不使用批次管理
            return this.returnInventory(inventoryOperatePojo);
        }
    }

    /**
     *  功能描述：退还库存  --不使用批次管理
     * @param
     * @return
     * @throws
     * @author:  tangww
     * @createDate   2016/3/14
     *
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public InventoryOperatePojo returnInventory(InventoryOperatePojo inventoryOperatePojo) {
        if(null == inventoryOperatePojo.getDrugId() || null == inventoryOperatePojo.getAmount())
            return inventoryOperatePojo;
        HospitalDrug hospitalDrug = drugService.getDrugById(inventoryOperatePojo.getDrugId());
        if(hospitalDrug ==null){
            logger.error("根据药品ID无法找到药品信息，药品ID:"+inventoryOperatePojo.getDrugId());
            return inventoryOperatePojo;
        }
        //支持拆分售药 时，需要额外计算 按 包装单位售药时的 数量
        if(null != hospitalDrug.getOpenStock() && hospitalDrug.getOpenStock().equals(1)){
            if(hospitalDrug.getSpecPackageUnit().equals(inventoryOperatePojo.getUnit())){
                inventoryOperatePojo.setAmount(Arith.mul(inventoryOperatePojo.getAmount() , hospitalDrug.getSpecUnitaryRatio()));
            }
        }
        //修改库存
        DrugInventory inventory = new DrugInventory();
        inventory.setDrugId(hospitalDrug.getId());
        inventory.setHospitalId(hospitalDrug.getHospitalId());

        //Integer afterInventory = hospitalDrug.getInventoryCount() + inventoryOperatePojo.getAmount();
        inventory.setInventoryCount(inventoryOperatePojo.getAmount());
        drugInventoryMapper.returnInventoryByHospitalIdAndDrugId(inventory);

        /*InventoryOperateLog log = new InventoryOperateLog();
        log.setHospitalId(inventoryOperatePojo.getHospitalId());
        log.setDrugId(inventoryOperatePojo.getDrugId());
        log.setOperateAction(OperateActionEnum.returnInventory.getActionId());
        log.setResult(1);
        log.setBeforeAmount(hospitalDrug.getInventoryCount());
        log.setAmount(inventoryOperatePojo.getAmount());
        log.setUserId(SessionUtils.getDoctorId());
        inventoryOperateLogService.saveInventoryOperateLog(log);*/
        return inventoryOperatePojo;
    }


    /**
     *  功能描述：退还库存  --使用批次管理
     * @param
     * @return
     * @throws
     * @author:  tangww
     * @createDate   2016/6/15
     *
     */
    @Transactional(propagation = Propagation.REQUIRED)
    private InventoryOperatePojo returnInventoryByBatchInsock(InventoryOperatePojo inventoryOperatePojo) {
         /*
        *  主要业务处理流程
        *  1.查询 订单中药品 对应的 批次药品订单关系 记录
        *  2. 判断库存操作数 与 批次药品订单关系中记录库存 的总数  ---理论上应该一致
        *  3. 循环批次药品订单关系
        *     3.1 更新 对应批次药品的剩余库存
        *     3.2 将批次药品订单关系 的状态 设置为已退货
        *  4. 退回总库存
        * */
        if(null == inventoryOperatePojo.getDrugId() || null == inventoryOperatePojo.getAmount())
            return inventoryOperatePojo;
        HospitalDrug hospitalDrug = drugService.getDrugById(inventoryOperatePojo.getDrugId());
        if(hospitalDrug ==null){
            logger.error("根据药品ID无法找到药品信息，药品ID:"+inventoryOperatePojo.getDrugId());
            return inventoryOperatePojo;
        }
        //支持拆分售药 时，需要额外计算 按 包装单位售药时的 数量
        if(null != hospitalDrug.getOpenStock() && hospitalDrug.getOpenStock().equals(1)){
            // 按包装单位出售时，需要使用 数量 * 换算比
            if(hospitalDrug.getSpecPackageUnit().equals(inventoryOperatePojo.getUnit())){
                if(hospitalDrug.getSpecUnitaryRatio() != null)
                    inventoryOperatePojo.setAmount(Arith.mul(inventoryOperatePojo.getAmount() , hospitalDrug.getSpecUnitaryRatio()));
            }
        }
        BatchDrugOrderLink selectLink  = new BatchDrugOrderLink();
        selectLink.setDrugId(inventoryOperatePojo.getDrugId());
        selectLink.setOrderDrugId(inventoryOperatePojo.getOrderDrugId());

        List<BatchDrugOrderLink> batchDrugOrderLinks = batchDrugOrderLinkMapper.selectByDrugIdAndOrderId(selectLink);
        Double totalPurchasePrice = 0D;// 本药品 本次的所有的进货价
        if(CollectionUtils.isNotEmpty(batchDrugOrderLinks)){
            //计算 批次药品订单关系中记录库存 的总数
            Double totalAmount = batchDrugOrderLinks.stream().mapToDouble(BatchDrugOrderLink::getAmount).sum();

            if(totalAmount.equals(inventoryOperatePojo.getAmount()) ){
                // 可用的剩余库存数 <  操作库存总数  在记录 批次药品订单记录里，需要将 虚多出来看库存记录到 批次药品订单记录 表里
                logger.error("根据药品ID："+inventoryOperatePojo.getDrugId()+" 和订单ID："+inventoryOperatePojo.getOrderDrugId()+" 查询，对应的批次药品订单关系的库存操作数："+totalAmount+" 订单退货数："+inventoryOperatePojo.getAmount());
            }

            // 循环批次药品订单关系
            //     3.1 更新对应批次药品的剩余库存
            //     3.2 将批次药品订单关系 的状态 设置为已退货
            for(BatchDrugOrderLink batchDrugOrderLink : batchDrugOrderLinks){
                DrugBatchInstock drugBatchInstock = new DrugBatchInstock();
                drugBatchInstock.setId(batchDrugOrderLink.getBatchId());
                drugBatchInstock.setDrugId(batchDrugOrderLink.getDrugId());
                //将 记录的数量 写到 剩余库存中，在SQL中数据库的值  与 此值 相加
                drugBatchInstock.setSurplusCount(batchDrugOrderLink.getAmount());
                drugBatchInstockMapper.updateSurplusCountForReturnInventory(drugBatchInstock);
                updateBatchDrugOrderLinkStatus(batchDrugOrderLink);
            }

        }
        //修改库存
        DrugInventory inventory = new DrugInventory();
        inventory.setDrugId(hospitalDrug.getId());
        inventory.setHospitalId(hospitalDrug.getHospitalId());
        inventory.setInventoryCount(inventoryOperatePojo.getAmount());
        drugInventoryMapper.returnInventoryByHospitalIdAndDrugId(inventory);

//        InventoryOperateLog log = new InventoryOperateLog();
//        log.setHospitalId(inventoryOperatePojo.getHospitalId());
//        log.setDrugId(inventoryOperatePojo.getDrugId());
//        log.setOperateAction(OperateActionEnum.cutInventory.getActionId());
//        log.setResult(1);
//        log.setBeforeAmount(hospitalDrug.getInventoryCount());
//        log.setAmount(inventoryOperatePojo.getAmount());
//        log.setUserId(SessionUtils.getDoctorId());
//        inventoryOperateLogService.saveInventoryOperateLog(log);
        return inventoryOperatePojo;
    }




    /**
     *  功能描述：增量库存入库
     * @param
     * @return
     * @throws
     * @author:  tangww
     * @createDate   2016/3/14
     *
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Integer instockInventory(HospitalDrug hospDrug,Double addInventoryCount) {
        Integer modifyInventoryCountResult=1;
        Double inventoryCount=0D;
        if(hospDrug.getInventoryCount() != null && hospDrug.getInventoryCount()>0){
            inventoryCount = hospDrug.getInventoryCount();
        }
        double totalInventoryCount = addInventoryCount + inventoryCount;
        //修改库存
        DrugInventory inventory = new DrugInventory();
        inventory.setHospitalId(hospDrug.getHospitalId());
        inventory.setDrugId(hospDrug.getId());
        inventory.setInventoryCount(totalInventoryCount);
        inventory.setValidityDate(hospDrug.getValidityDate());
        modifyInventoryCountResult = modifyInventory(inventory);
        //记录库存操作日志
        /*InventoryOperateLog log = new InventoryOperateLog();
        log.setHospitalId(hospDrug.getHospitalId());
        log.setDrugId(hospDrug.getId());
        log.setOperateAction(OperateActionEnum.initInventory.getActionId());
        log.setResult(1);
        log.setBeforeAmount(inventoryCount);
        log.setAmount(totalInventoryCount);
        log.setUserId(SessionUtils.getDoctorId());
        inventoryOperateLogService.saveInventoryOperateLog(log);*/

        return modifyInventoryCountResult;
    }


}
