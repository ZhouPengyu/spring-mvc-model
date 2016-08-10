package com.hm.his.module.quote.service;

import com.hm.his.module.quote.model.PurchaseQuoteRelation;
import com.hm.his.module.quote.model.QuoteOrder;
import com.hm.his.module.quote.model.QuoteOrderDrug;
import com.hm.his.module.quote.model.SupplyAddress;
import com.hm.his.module.quote.pojo.QuoteOrderListResponse;
import com.hm.his.module.quote.pojo.QuoteOrderRequest;
import com.hm.his.module.quote.pojo.SupplyAddressPojo;

import java.util.List;
import java.util.Map;

/**
 * @param
 * @author SuShaohua
 * @date 2016/5/30
 * @description
 */
public interface QuoteService {
    /**
     * <p>description: 供应商报价</p>
     * @author SuShaohua
     * @date 2016/6/1 22:36
     * @param
     */
    Integer quote(QuoteOrder req) throws Exception;
    /**
     * <p>description: 获取采购单详情</p>
     * @author SuShaohua
     * @date 2016/6/1 22:37
     * @param
     */
    QuoteOrder getPurchaseOrder(QuoteOrderRequest req);
    /**
     * <p>description: 通过id获取供货单详情</p>
     * @author SuShaohua
     * @date 2016/6/1 22:37
     * @param
     */
    QuoteOrder getQuoteOrderDetail(Integer quoteOrderId);
    /**
     * <p>description: 配置自提地址</p>
     * @author SuShaohua
     * @date 2016/6/1 22:45
     * @param
     */
    Integer supplyAddressConfig(SupplyAddressPojo req);
    /**
     * <p>description: 出库</p>
     * @author SuShaohua
     * @date 2016/6/1 22:45
     * @param
     */
    Integer warehouseOut(QuoteOrder req);
    /**
     * <p>description: 通过地址id获取地址详情</p>
     * @author SuShaohua
     * @date 2016/6/1 22:45
     * @param
     */
    SupplyAddress getAddressById(Integer addressId);
    /**
     * <p>description: 查询订单list</p>
     * @author SuShaohua
     * @date 2016/6/1 22:46
     * @param
     */
    List<QuoteOrderListResponse> queryQuoteOrderList(QuoteOrderRequest req) throws Exception;
    /**
     * <p>description: 根据获取当前供应商已报价的订单list</p>
     * @author SuShaohua
     * @date 2016/6/1 22:46
     * @param
     */
    List<QuoteOrder> queryQuoteOrder(QuoteOrderRequest req);
    /**
     * <p>description: 獲取訂單數目，暫時不用</p>
     * @author SuShaohua
     * @date 2016/6/2 20:25
     * @param
     */
    int queryQuoteOrderCount(QuoteOrderRequest req);
    /**
     * <p>description: 查询当前供应商的所有有效订单</p>
     * @author SuShaohua
     * @date 2016/6/1 22:47
     * @param
     */
    List<QuoteOrder> queryQuoteOrderByCreater(QuoteOrderRequest req);
    /**
     * <p>description: 获得当前供应商指定采购单的报价单</p>
     * @author SuShaohua
     * @date 2016/6/2 17:59
     * @param
     */
    QuoteOrder getQuoteOrderDetailByPurchaseOrderId(QuoteOrderRequest req);
    /**
     * <p>description: 取消报价</p>
     * @author SuShaohua
     * @date 2016/6/2 18:20
     * @param
     */
    Integer cancelQuoteOrder(QuoteOrderRequest req);
    /**
     * <p>description: 确认供应商和报价</p>
     * @author SuShaohua
     * @date 2016/6/2 18:20
     * @param
     */
    Integer confirmQuoteOrder(Integer quoteOrderId);
    /**
     * <p>description: 获得当前采购单的所有报价单</p>
     * @author SuShaohua
     * @date 2016/6/2 18:21
     * @param
     */
    List<QuoteOrder>  queryQuoteOrderByPurchaseOrderId(Integer purchaseOrderId);
    /**
     * <p>description: 获取指定供货单的药品列表</p>
     * @author SuShaohua
     * @date 2016/6/2 18:23
     * @param
     */
    List<QuoteOrderDrug> getQuoteOrderDrugByQuoteOrderId(Integer quoteOrderId);
    /**
     * <p>description: 诊所确认收货</p>
     * @author SuShaohua
     * @date 2016/6/3 11:07
     * @param
     */
    Integer confirmReceipt(Integer quoteOrderId);
    /**
     * <p>description: 供应商查看未报价的订单</p>
     * @author SuShaohua
     * @date 2016/6/3 16:34
     * @param
     */
    Map<String, Object> queryPurchaseOrderList(QuoteOrderRequest req) throws Exception;
    /**
     * <p>description: 获取默认字体地址</p>
     * @author SuShaohua
     * @date 2016/6/15 18:51
     * @param
     */
    SupplyAddress getSupplyAddress(Integer hospitalId) throws Exception;
}
