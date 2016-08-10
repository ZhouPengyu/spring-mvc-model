package com.hm.his.module.quote.service.impl;

import com.hm.his.framework.utils.LangUtils;
import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.framework.utils.SmsCaptchaUtil;
import com.hm.his.module.purchase.model.DeliveryAddress;
import com.hm.his.module.purchase.model.PurchaseOrder;
import com.hm.his.module.purchase.model.PurchaseOrderDrug;
import com.hm.his.module.purchase.model.request.PurchaseOrderRequest;
import com.hm.his.module.purchase.service.DeliveryAddressService;
import com.hm.his.module.purchase.service.PurchaseService;
import com.hm.his.module.quote.dao.PurchaseQuoteRelationMapper;
import com.hm.his.module.quote.dao.QuoteOrderDrugMapper;
import com.hm.his.module.quote.dao.QuoteOrderMapper;
import com.hm.his.module.quote.dao.SupplyAddressMapper;
import com.hm.his.module.quote.model.PurchaseQuoteRelation;
import com.hm.his.module.quote.model.QuoteOrder;
import com.hm.his.module.quote.model.QuoteOrderDrug;
import com.hm.his.module.quote.model.SupplyAddress;
import com.hm.his.module.quote.pojo.QuoteOrderListResponse;
import com.hm.his.module.quote.pojo.QuoteOrderRequest;
import com.hm.his.module.quote.pojo.SupplyAddressPojo;
import com.hm.his.module.quote.service.QuoteService;
import com.hm.his.module.user.service.DoctorService;
import com.hm.his.module.user.service.HospitalService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @param
 * @author SuShaohua
 * @date 2016/5/30
 * @description
 */
@Service
public class QuoteServiceImpl  implements QuoteService {
    @Autowired(required = false)
    QuoteOrderMapper quoteOrderMapper;
    @Autowired(required = false)
    QuoteOrderDrugMapper quoteOrderDrugMapper;
    @Autowired(required = false)
    PurchaseQuoteRelationMapper purchaseQuoteRelationMapper;
    @Autowired(required = false)
    SupplyAddressMapper supplyAddressMapper;
    @Autowired
    PurchaseService purchaseService;
    @Autowired
    DoctorService doctorService;
    @Autowired
    HospitalService hospitalService;
    @Autowired
    DeliveryAddressService deliveryAddressService;

    @Override
    public Integer quote(QuoteOrder req) throws Exception{
        List<QuoteOrderDrug> drugList =  req.getDrugList();
        Integer purchaseOrderId = req.getPurchaseOrderId();
        PurchaseOrder purchaseOrder = purchaseService.getPurchaseOrder(purchaseOrderId);
        if (0 == purchaseOrder.getStatus()){
            return 0; //采购单已取消
        }
        req.setCreater(LangUtils.getInteger(SessionUtils.getDoctorId()));
        req.setStatus(1);
        req.setFlag(1);
        req.setPurchaseCreateDate(purchaseOrder.getCreateDate().substring(0, 19));
        req.setPurchaseOrderNumber(purchaseOrder.getPurchaseOrderNumber());
        //获得当前采购单的供货单号
        QuoteOrderRequest request = new QuoteOrderRequest();
        request.setCreater(SessionUtils.getDoctorId());
        request.setPurchaseOrderId(purchaseOrderId);
        QuoteOrder quoteOrder = this.getQuoteOrderDetailByPurchaseOrderId(request);
        Integer quoteOrderId = req.getQuoteOrderId();
        if (quoteOrder != null){
            quoteOrderId = quoteOrder.getQuoteOrderId();
        }
        if (StringUtils.isBlank(req.getScheduleDelivery())){
            req.setScheduleDelivery(null);
        }
        try {
            if (req.getQuoteOrderId() == null && quoteOrderId == null){
                quoteOrderMapper.insertQuoteOrder(req);
                quoteOrderId = quoteOrderMapper.queryQuoteOrderPrimaryKey(request);
                //采购单和报价单关系表管理
                PurchaseQuoteRelation record = new PurchaseQuoteRelation();
                record.setQuoteOrderId(quoteOrderId);
                record.setPurchaseOrderId(purchaseOrderId);
                record.setStatus(1);//1 已报价
                purchaseQuoteRelationMapper.insertPurQuoRealtion(record);
                //订单记录
                req.setQuoteOrderId(quoteOrderId);
                quoteOrderMapper.insertQuoteOrderRecord(req);
                //发送短信
                String hospitalName = this.getHospitalName(purchaseOrderId);
                String purchaseCreateDate = purchaseOrder.getCreateDate().substring(0, 10).replace("-", ".");
                QuoteOrder quote = this.getQuoteOrderDetail(quoteOrderId);
                String supplierName = quote.getSupplierName();
                String phoneNo = quote.getPhoneNo();
                Double totalPrice = (null == quote.getTotalPrice()) ? 0D : quote.getTotalPrice();
                String hospitalPhoneNo = this.getHospitalPhoneNo(purchaseOrderId);
                String smsDetail = hospitalName + "，您" + purchaseCreateDate + "的采购订单，已获得" + supplierName + "（联系电话：" + phoneNo + "）"
                        + "的报价（合计金额：" + totalPrice + "元），请登录惠每云诊所查看详细信息";
                SmsCaptchaUtil.sendSms(hospitalPhoneNo, smsDetail);

                for (QuoteOrderDrug drug : drugList){
                    if (drug.getDrugId() != null){
                        PurchaseOrderDrug purchaseOrderDrug = purchaseService.getPurchaseOrderDrug(purchaseOrderId, drug.getDrugId());
                        drug.setDrugName(purchaseOrderDrug.getDrugName());
                        drug.setDrugSpecification(purchaseOrderDrug.getDrugSpecification());
                        drug.setPurchaseQuantity(purchaseOrderDrug.getPurchaseQuantity());
                        drug.setPackageUnit(purchaseOrderDrug.getPackageUnit());
                        drug.setPurchaseDrugManufacturers(purchaseOrderDrug.getPurchaseDrugManufacturers());
                        drug.setQuoteOrderId(req.getQuoteOrderId());
                        quoteOrderDrugMapper.insertQuoteDrug(drug);
                        QuoteOrderDrug quoteOrderDrug = quoteOrderDrugMapper.getQuoteOrderDrugDetail(drug);
                        drug.setQuoteOrderDrugId(quoteOrderDrug.getQuoteOrderDrugId());
                        quoteOrderDrugMapper.insertQuoteOrderDrugRecord(drug);
                    }
                }
            }else {
                req.setQuoteOrderId(quoteOrderId);
                quoteOrderMapper.updateQuoteOrder(req);
                quoteOrderMapper.insertQuoteOrderRecord(req);
                for (QuoteOrderDrug drug : drugList){
                    if (drug.getDrugId() != null){
                        drug.setQuoteOrderId(req.getQuoteOrderId());
                        QuoteOrderDrug quoteOrderDrug = quoteOrderDrugMapper.getQuoteOrderDrugDetail(drug);
                        if (null != quoteOrderDrug) {
                            drug.setQuoteOrderDrugId(quoteOrderDrug.getQuoteOrderDrugId());
                            quoteOrderDrugMapper.updateQuoteOrderDrug(drug);
                            QuoteOrderDrug drugRecord = quoteOrderDrugMapper.getQuoteOrderDrugDetail(drug);
                            quoteOrderDrugMapper.insertQuoteOrderDrugRecord(drugRecord);
                        }
                    }
                }
            }
            purchaseService.updatePurchaseOrderStatus(purchaseOrderId, 2);
        }catch (Exception e){
            e.printStackTrace();
        }
        return quoteOrderId;
    }

    private String getHospitalName(Integer purchaseOrderId) throws Exception{
        return hospitalService.getHospitalByDoctorId(
                purchaseService.getPurchaseOrder(purchaseOrderId).getCreater())
                .getHospitalName();
    }

    private String getHospitalPhoneNo(Integer purchaseOrderId) throws Exception {
        return deliveryAddressService.getDeliveryAddressById(
                purchaseService.getPurchaseOrder(purchaseOrderId).getDeliveryAddressId()
        ).getPhoneNo();
    }

    @Override
    public QuoteOrder getPurchaseOrder(QuoteOrderRequest req) {
        req.handleQuoteOrderRequest(req);
        QuoteOrder quoteOrder = new QuoteOrder();
        try {
            //当前供应商已报价的采购单idList
            List<QuoteOrder> quoteOrderList = this.queryQuoteOrderByCreater(req);
            Set<Integer> purchaseOrderIdSet = new HashSet<>();
            for (QuoteOrder qo : quoteOrderList){
                Integer purchaseOrderId = qo.getPurchaseOrderId();
                purchaseOrderIdSet.add(purchaseOrderId);
            }
            PurchaseOrder purchaseOrder = purchaseService.getPurchaseOrder(req.getPurchaseOrderId());
            if (purchaseOrderIdSet.contains(req.getPurchaseOrderId())){
                quoteOrder = this.getQuoteOrderDetailByPurchaseOrderId(req);
                quoteOrder.setPurchaseOrderNumber(purchaseOrder.getPurchaseOrderNumber());
                quoteOrder.setDesignated(purchaseOrder.getDesignated());
                List<QuoteOrderDrug> quoteOrderDrugList = quoteOrderDrugMapper.getQuoteOrderDrug(quoteOrder.getQuoteOrderId());
                quoteOrder.setDrugList(quoteOrderDrugList);
                //诊所端配送信息
                String hospitalPhoneNo = deliveryAddressService.getDeliveryAddressById(
                        purchaseOrder.getDeliveryAddressId()
                ).getPhoneNo();
                quoteOrder.setHospitalPhoneNo(hospitalPhoneNo);
                quoteOrder.setHospitalName(
                        hospitalService.getHospitalByDoctorId(
                                purchaseOrder.getCreater()).getHospitalName());
                quoteOrder.setHospitalMessage(purchaseOrder.getMessage());
                //获取自提地址
                List<SupplyAddress> supplyAddressList = new ArrayList<>();
                SupplyAddress supplyAddress = this.getSupplyAddress(LangUtils.getInteger(SessionUtils.getHospitalId()));
                if (null != supplyAddress){
                    supplyAddressList.add(supplyAddress);
                }
                quoteOrder.setSupplyAddress(supplyAddressList);

                //默认配送地址
                DeliveryAddress deliveryAddress = new DeliveryAddress();
                deliveryAddress.setAddress(purchaseOrder.getDeliveryAddress());
                deliveryAddress.setDeliveryAddressId(purchaseOrder.getDeliveryAddressId());
                quoteOrder.setDeliveryAddress(deliveryAddress);

                if (quoteOrder.getQuoteWay() == 1){
                    quoteOrder.setAddress(deliveryAddressService.getDeliveryAddressById(quoteOrder.getWayId()).getAddress());
                }else {
                    quoteOrder.setAddress(this.getAddressById(quoteOrder.getWayId()).getAddress());
                }
            }else {
                quoteOrder.setPurchaseOrderId(purchaseOrder.getPurchaseOrderId());
                quoteOrder.setPurchaseOrderNumber(purchaseOrder.getPurchaseOrderNumber());
                quoteOrder.setQuoteWay(1);
                quoteOrder.setWayId(purchaseOrder.getDeliveryAddressId());
                quoteOrder.setDesignated(purchaseOrder.getDesignated());
                quoteOrder.setAddress(deliveryAddressService.getDeliveryAddressById(purchaseOrder.getDeliveryAddressId()).getAddress());
                quoteOrder.setHospitalMessage(purchaseOrder.getMessage());
                quoteOrder.setHospitalPhoneNo(deliveryAddressService.getDeliveryAddressById(quoteOrder.getWayId()).getPhoneNo());
                quoteOrder.setHospitalName(hospitalService.getHospitalById(
                        (long)deliveryAddressService.getDeliveryAddressById(purchaseOrder.getDeliveryAddressId()).getHospitalId())
                        .getHospitalName());
                quoteOrder.setStatus(-1);
                //供应商默认联系方式
                quoteOrder.setPhoneNo(
                        doctorService.getDoctorById(SessionUtils.getDoctorId()).getPhone()
                );
                //获取自提地址
                List<SupplyAddress> supplyAddressList = new ArrayList<>();
                SupplyAddress supplyAddress = this.getSupplyAddress(LangUtils.getInteger(SessionUtils.getHospitalId()));
                if (null != supplyAddress){
                    supplyAddressList.add(supplyAddress);
                }
                quoteOrder.setSupplyAddress(supplyAddressList);
                //默认配送地址
                DeliveryAddress deliveryAddress = new DeliveryAddress();
                deliveryAddress.setAddress(purchaseOrder.getDeliveryAddress());
                deliveryAddress.setDeliveryAddressId(purchaseOrder.getDeliveryAddressId());
                quoteOrder.setDeliveryAddress(deliveryAddress);

                List<PurchaseOrderDrug> purchaseOrderDrugList = purchaseService.getPurchaseOrderDrugList(req.getPurchaseOrderId());
                List<QuoteOrderDrug> quoteOrderDrugList = new ArrayList<>();
                for (PurchaseOrderDrug purchaseOrderDrug : purchaseOrderDrugList){
                    QuoteOrderDrug quoteOrderDrug = new QuoteOrderDrug();
                    quoteOrderDrug.setDrugId(purchaseOrderDrug.getDrugId());
                    quoteOrderDrug.setDrugName(purchaseOrderDrug.getDrugName());
                    quoteOrderDrug.setDrugSpecification(purchaseOrderDrug.getDrugSpecification());
                    quoteOrderDrug.setSupplyDrugManufacturers(purchaseOrderDrug.getPurchaseDrugManufacturers());
                    quoteOrderDrug.setPurchaseQuantity(purchaseOrderDrug.getPurchaseQuantity());
                    quoteOrderDrug.setPackageUnit(purchaseOrderDrug.getPackageUnit());
                    quoteOrderDrug.setSupplyQuantity(purchaseOrderDrug.getPurchaseQuantity());
                    quoteOrderDrugList.add(quoteOrderDrug);
                }
                quoteOrder.setDrugList(quoteOrderDrugList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return quoteOrder;
    }

    @Override
    public QuoteOrder getQuoteOrderDetail(Integer quoteOrderId) {
        QuoteOrder quoteOrder = quoteOrderMapper.queryQuoteOrderByOrderId(quoteOrderId);
        List<QuoteOrderDrug> quoteOrderDrugList = quoteOrderDrugMapper.getQuoteOrderDrug(quoteOrder.getQuoteOrderId());
        quoteOrder.setDrugList(quoteOrderDrugList);
        quoteOrder.setSupplierName(hospitalService.getHospitalByDoctorId(quoteOrder.getCreater()).getHospitalName());
        return quoteOrder;
    }

    @Override
    public Integer supplyAddressConfig(SupplyAddressPojo req) {
        Integer result = 0;
        try{
            req.setCreater(LangUtils.getInteger(SessionUtils.getDoctorId()));
            req.setHospitalId(LangUtils.getInteger(SessionUtils.getHospitalId()));
            /**
             * 自提地址只新增，因为更新会把先前订单的自提地址覆盖掉，多个地址考虑地址管理功能
             */
            supplyAddressMapper.insertSupplyAddress(req);
            result = supplyAddressMapper.getSupplyAddress(LangUtils.getInteger(SessionUtils.getHospitalId())).getSupplyAddressId();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


    @Override
    public SupplyAddress getAddressById(Integer addressId) {
        return supplyAddressMapper.getAddressById(addressId);
    }

    @Override
    public List<QuoteOrderListResponse> queryQuoteOrderList(QuoteOrderRequest req) throws Exception {
        List<QuoteOrder> quoteOrderList = this.queryQuoteOrder(req);
        return this.getQuoteOrderListResponse(quoteOrderList);
    }

    private List<QuoteOrderListResponse> getQuoteOrderListResponse(List<QuoteOrder> quoteOrderList) throws Exception {
        List<QuoteOrderListResponse> quoteOrderListResponseList = new ArrayList<>();
        for (QuoteOrder quoteOrder : quoteOrderList){
            QuoteOrderListResponse quoteOrderListResponse = new QuoteOrderListResponse();
            Integer purchaseOrderId = quoteOrder.getPurchaseOrderId();
            PurchaseOrder purchaseOrder = purchaseService.getPurchaseOrder(purchaseOrderId);

            quoteOrderListResponse.setPurchaseOrderId(purchaseOrderId);
            quoteOrderListResponse.setPurchaseOrderNumber(purchaseOrder.getPurchaseOrderNumber());
            quoteOrderListResponse.setCreateDate(quoteOrder.getPurchaseCreateDate().substring(0, 10).replace("-", "."));
            quoteOrderListResponse.setStatus(quoteOrder.getStatus());
            quoteOrderListResponse.setTotalPrice(quoteOrder.getTotalPrice());

            DeliveryAddress deliveryAddress = deliveryAddressService.getDeliveryAddressById(purchaseOrder.getDeliveryAddressId());
            quoteOrderListResponse.setHospitalName(hospitalService.getHospitalById((long)deliveryAddress.getHospitalId()).getHospitalName());
            quoteOrderListResponse.setHospitalAddress(deliveryAddress.getAddress());
            quoteOrderListResponse.setPhoneNo(deliveryAddress.getPhoneNo());

            quoteOrderListResponseList.add(quoteOrderListResponse);
        }
        return quoteOrderListResponseList;
    }


    @Override
    public Map<String, Object> queryPurchaseOrderList(QuoteOrderRequest req) throws Exception {
        Map<String, Object> result = new HashMap<>();
        req.handleQuoteOrderRequest(req);
        List<QuoteOrderListResponse> quoteOrderListResponseList = new ArrayList<>();
        if (StringUtils.isNotBlank(req.getPurchaseOrderNumber())){
            PurchaseOrder purchaseOrder = purchaseService.getPurchaseOrderByNo(req.getPurchaseOrderNumber());
            if (purchaseOrder != null){
                req.setPurchaseOrderId(purchaseOrder.getPurchaseOrderId());
            }
        }

        PurchaseOrderRequest por = new PurchaseOrderRequest();
        por.setStartDate(req.getStartDate());
        por.setEndDate(req.getEndDate());
        por.setPurchaseOrderNumber(StringUtils.isEmpty(req.getPurchaseOrderNumber()) ? null : req.getPurchaseOrderNumber());
        List<PurchaseOrder> purchaseOrderList = purchaseService.queryPurchaseOrderAll(por);
        //当前供应商已报价的采购单id
        List<QuoteOrder> quoteOrderList = this.queryQuoteOrderByCreater(req);
        Set<Integer> purchaseOrderIdSet = new HashSet<>();
        for (QuoteOrder quoteOrder : quoteOrderList){
            Integer purchaseOrderId = quoteOrder.getPurchaseOrderId();
            purchaseOrderIdSet.add(purchaseOrderId);
        }
        //获得当前条件不分页且未报价的所用采购单
        int totalSize = 0;
        List<PurchaseOrder> purchaseOrders = new ArrayList<>();
        for (PurchaseOrder purchaseOrder : purchaseOrderList){
            if (!purchaseOrderIdSet.contains(purchaseOrder.getPurchaseOrderId())){
                purchaseOrders.add(purchaseOrder);
                totalSize++;
            }
        }
        Integer pageSize = req.getPageSize();
        Integer currentPage = req.getCurrentPage();
        for (int i = 0; i < req.getPageSize(); i++){
            if (((currentPage - 1) * pageSize + i) < purchaseOrders.size()){
                PurchaseOrder purchaseOrder = purchaseOrders.get((currentPage - 1) * pageSize + i);
                QuoteOrderListResponse quoteOrderListResponse = new QuoteOrderListResponse();
                quoteOrderListResponse.setPurchaseOrderId(purchaseOrder.getPurchaseOrderId());
                quoteOrderListResponse.setPurchaseOrderNumber(purchaseOrder.getPurchaseOrderNumber());
                quoteOrderListResponse.setCreateDate(purchaseOrder.getCreateDate().substring(0, 10).replace("-", "."));
                quoteOrderListResponse.setStatus(-1);
                DeliveryAddress deliveryAddress = deliveryAddressService.getDeliveryAddressById(purchaseOrder.getDeliveryAddressId());
                quoteOrderListResponse.setHospitalName(hospitalService.getHospitalById((long)deliveryAddress.getHospitalId()).getHospitalName());
                quoteOrderListResponse.setHospitalAddress(deliveryAddress.getAddress());
                quoteOrderListResponse.setPhoneNo(deliveryAddress.getPhoneNo());
                quoteOrderListResponseList.add(quoteOrderListResponse);
            }
        }
        result.put("totalPage",  Math.ceil((double)totalSize / req.getPageSize()));
        result.put("orderList", quoteOrderListResponseList);
        return result;
    }

    @Override
    public SupplyAddress getSupplyAddress(Integer hospitalId) throws Exception{
        SupplyAddress supplyAddress = null;
        try {
            supplyAddress = supplyAddressMapper.getSupplyAddress(hospitalId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return supplyAddress;
    }

    @Override
    public List<QuoteOrder> queryQuoteOrder(QuoteOrderRequest req) {
        List<QuoteOrder> result = new ArrayList<>();
        if (req.getStatus() != null && req.getStatus() == -1){
            req.setStatus(null);
        }
        try{
            List<QuoteOrder> quoteOrderList = quoteOrderMapper.queryQuoteOrder(req);
            result = this.getQuoteOrderPojo(quoteOrderList);
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public int queryQuoteOrderCount(QuoteOrderRequest req) {
        Integer count = 0;
        try {
            count = quoteOrderMapper.queryQuoteOrderCount(req);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public List<QuoteOrder> queryQuoteOrderByCreater(QuoteOrderRequest req) {
        List<QuoteOrder> result = new ArrayList<>();
        try {
            List<QuoteOrder> quoteOrderList = quoteOrderMapper.queryQuoteOrderByCreater(req);
            result = this.getQuoteOrderPojo(quoteOrderList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    private List<QuoteOrder> getQuoteOrderPojo(List<QuoteOrder> quoteOrderList) {
        List<QuoteOrder> result = new ArrayList<>();
        for (QuoteOrder quoteOrder : quoteOrderList){
            quoteOrder.setSupplierName(hospitalService.getHospitalByDoctorId(quoteOrder.getCreater()).getHospitalName());
            result.add(quoteOrder);
        }
        return result;
    }

    @Override
    public QuoteOrder getQuoteOrderDetailByPurchaseOrderId(QuoteOrderRequest req) {
        QuoteOrder quoteOrder = new QuoteOrder();
        try {
            quoteOrder = quoteOrderMapper.getQuoteOrderDetailByPurchaseOrderId(req);
            if (quoteOrder != null){
                quoteOrder.setSupplierName(hospitalService.getHospitalByDoctorId(quoteOrder.getCreater()).getHospitalName());
                quoteOrder.setScheduleDelivery(
                        StringUtils.isEmpty(quoteOrder.getScheduleDelivery()) ? null : quoteOrder.getScheduleDelivery().substring(0, 10).replace("-", ".")
                );
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return quoteOrder;
    }


    @Override
    public Integer warehouseOut(QuoteOrder req) {
        Integer result = 0;
        Integer quoteOrderId = req.getQuoteOrderId();
        Integer purchaseOrderId = this.getQuoteOrderDetail(quoteOrderId).getPurchaseOrderId();
        req.setStatus(3);
        try{
            result = quoteOrderMapper.updateQuoteOrder(req);

            String hospitalName = this.getHospitalName(purchaseOrderId);
            String purchaseCreateDate = purchaseService.getPurchaseOrder(purchaseOrderId).getCreateDate().substring(0, 10).replace("-", ".");
            String supplierName = this.getQuoteOrderDetail(quoteOrderId).getSupplierName();
            String phoneNo = this.getQuoteOrderDetail(quoteOrderId).getPhoneNo();
            String hospitalPhoneNo = this.getHospitalPhoneNo(purchaseOrderId);
            String smsDetail = hospitalName + "，您" + purchaseCreateDate + "的采购订单，" + supplierName + "（联系电话：" + phoneNo + "）"
                    + "已确认药品出库，请保持联系方式畅通";
            SmsCaptchaUtil.sendSms(hospitalPhoneNo, smsDetail);
            purchaseService.updatePurchaseOrderStatus(purchaseOrderId, 4);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Integer cancelQuoteOrder(QuoteOrderRequest req) {
        Integer result = 0;
        try {
            result = quoteOrderMapper.cancelQuoteOrder(req);
            //采购单和报价单关系表管理
            PurchaseQuoteRelation record = new PurchaseQuoteRelation();
            QuoteOrder quoteOrder = this.getQuoteOrderDetail(req.getQuoteOrderId());
            Integer quoteOrderId = quoteOrder.getQuoteOrderId();
            Integer purchaseOrderId = quoteOrder.getPurchaseOrderId();
            record.setQuoteOrderId(quoteOrderId);
            record.setPurchaseOrderId(purchaseOrderId);
            record.setStatus(0);//1 已报价
            purchaseQuoteRelationMapper.updatePurQuoRealtionStatus(record);
            if (0 == purchaseService.getQuoteCount(purchaseOrderId)){
                purchaseService.updatePurchaseOrderStatus(purchaseOrderId, 1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Integer confirmQuoteOrder(Integer quoteOrderId) {
        Integer result = 0;
        Integer purchaseOrderId = this.getQuoteOrderDetail(quoteOrderId).getPurchaseOrderId();
        try{
            List<QuoteOrder> quoteOrderList = this.queryQuoteOrderByPurchaseOrderId(purchaseOrderId);
            for (QuoteOrder quoteOrder : quoteOrderList){
                if (quoteOrder.getStatus() == 1 && quoteOrderId.equals(quoteOrder.getQuoteOrderId())){
                    quoteOrder.setStatus(2);
                }else if (quoteOrder.getStatus() == 1 && !quoteOrderId.equals(quoteOrder.getQuoteOrderId())){
                    quoteOrder.setStatus(5);
                }
                result = quoteOrderMapper.updateQuoteOrder(quoteOrder);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
    /**
     * <p>description: 获得确定供货单的采购单的所用报价单</p>
     * @author SuShaohua
     * @date 2016/6/3 14:23
     * @param
     */
    private List<Integer> queryQuoteOrderId(Integer quoteOrderId) {
        return quoteOrderMapper.queryQuoteOrderId(quoteOrderId);
    }

    @Override
    public List<QuoteOrder> queryQuoteOrderByPurchaseOrderId(Integer purchaseOrderId) {
        List<QuoteOrder> result = new ArrayList<>();
        try{
            List<QuoteOrder> quoteOrderList = quoteOrderMapper.queryQuoteOrderByPurchaseOrderId(purchaseOrderId);
            result = this.getQuoteOrderPojo(quoteOrderList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<QuoteOrderDrug> getQuoteOrderDrugByQuoteOrderId(Integer quoteOrderId) {
        List<QuoteOrderDrug> result = new ArrayList<>();
        try {
            result = quoteOrderDrugMapper.getQuoteOrderDrug(quoteOrderId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Integer confirmReceipt(Integer quoteOrderId) {
        Integer result = 0;
        try{
            QuoteOrder quoteOrder = new QuoteOrder();
            quoteOrder.setQuoteOrderId(quoteOrderId);
            quoteOrder.setStatus(4);
            result = quoteOrderMapper.updateQuoteOrder(quoteOrder);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

}