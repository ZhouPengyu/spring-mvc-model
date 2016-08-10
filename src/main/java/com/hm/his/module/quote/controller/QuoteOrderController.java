package com.hm.his.module.quote.controller;

import com.google.common.collect.Maps;
import com.hm.his.framework.model.HisResponse;
import com.hm.his.framework.utils.LangUtils;
import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.framework.utils.SmsCaptchaUtil;
import com.hm.his.module.quote.model.QuoteOrder;
import com.hm.his.module.quote.model.SupplyAddress;
import com.hm.his.module.quote.pojo.QuoteOrderListResponse;
import com.hm.his.module.quote.pojo.QuoteOrderRequest;
import com.hm.his.module.quote.pojo.SupplyAddressPojo;
import com.hm.his.module.quote.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @param
 * @author SuShaohua
 * @date 2016/5/30
 * @description
 */
@RestController
@RequestMapping("/quote")
public class QuoteOrderController {
    @Autowired
    QuoteService quoteService;

    /**
     * @description 订单报价
     */
    @RequestMapping(value = "/quote", method = RequestMethod.POST, produces = "application/json;charset=UTF-8", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String QuoteOrderQuote(@RequestBody QuoteOrder req) {
        HisResponse hisResponse = new HisResponse();
        Map<String, Integer> body = new HashMap<>();
        try{
            Integer result = quoteService.quote(req);
            body.put("result", result);
            hisResponse.setBody(body);
        }catch (Exception e){
            e.printStackTrace();
            hisResponse.setErrorCode(5000);
            hisResponse.setErrorMessage("报价失败");
        }
        return hisResponse.toString();
    }

    /**
     * @description 获取订单列表
     */
    @RequestMapping(value = "/getPurchaseOrderList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String getPurchaseOrderList(@RequestBody QuoteOrderRequest req) {
        HisResponse hisResponse = new HisResponse();
        Map<String, Object> body = new HashMap<>();
        List<QuoteOrderListResponse> quoteOrderListResponseList = new ArrayList<>();
        try {
            if (req.getIsQuoted() == 1){
                req.handleQuoteOrderRequest(req);
                quoteOrderListResponseList = quoteService.queryQuoteOrderList(req);
                body.put("orderList", quoteOrderListResponseList);
                Integer totalSize = quoteService.queryQuoteOrderCount(req);
                body.put("totalPage", Math.ceil((double)totalSize / req.getPageSize()));
            }
            else {
                body = quoteService.queryPurchaseOrderList(req);
            }
            hisResponse.setBody(body);
        } catch (Exception e) {
            e.printStackTrace();
            hisResponse.setErrorCode(5000);
            hisResponse.setErrorMessage("获取订单列表失败");
        }
        return hisResponse.toString();
    }

    @RequestMapping(value = "/getSupplyAddress", method = RequestMethod.POST, produces = "application/json;charset=UTF-8", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String getSupplyAddress() {
        HisResponse hisResponse = new HisResponse();
        try{
            SupplyAddress supplyAddress = quoteService.getSupplyAddress(LangUtils.getInteger(SessionUtils.getHospitalId()));
            hisResponse.setBody(supplyAddress);
        }catch (Exception e){
            e.printStackTrace();
            hisResponse.setErrorCode(5000);
            hisResponse.setErrorMessage("获取地址失敗");
        }
        return hisResponse.toString();
    }


    /**
     * @description 更新自提地址
     */
    @RequestMapping(value = "/supplyAddressConfig", method = RequestMethod.POST, produces = "application/json;charset=UTF-8", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String supplyAddressConfig(@RequestBody SupplyAddressPojo req) {
        HisResponse hisResponse = new HisResponse();
        Map<String, Integer> body = Maps.newHashMap();
        try{
            Integer result = quoteService.supplyAddressConfig(req);
            body.put("result", result);
        }catch (Exception e){
            e.printStackTrace();
            hisResponse.setErrorCode(5000);
            hisResponse.setErrorMessage("更新地址失敗");
        }
        return HisResponse.getInstance(body).toString();
    }

    /**
     * @description 出库
     */
    @RequestMapping(value = "/warehouseOut", method = RequestMethod.POST, produces = "application/json;charset=UTF-8", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String warehouseOut(@RequestBody QuoteOrder req) {
        HisResponse hisResponse = new HisResponse();
        Map<String, Integer> body = Maps.newHashMap();
        try{
            Integer result = quoteService.warehouseOut(req);
            body.put("result", result);
        }catch (Exception e){
            e.printStackTrace();
            hisResponse.setErrorCode(5000);
            hisResponse.setErrorMessage("出庫失敗");
        }
        return HisResponse.getInstance(body).toString();
    }

    /**
     * @description 获取采购单详情
     */
    @RequestMapping(value = "/getPurchaseOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String getPurchaseOrder(@RequestBody QuoteOrderRequest req) {
        HisResponse hisResponse = new HisResponse();
        try{
            QuoteOrder body = quoteService.getPurchaseOrder(req);
            hisResponse.setBody(body);
        }catch (Exception e){
            e.printStackTrace();
            hisResponse.setErrorCode(5000);
            hisResponse.setErrorMessage("获取采购单详情失敗");
        }

        return hisResponse.toString();
    }
    /**
     * <p>description: 取消订单报价</p>
     * @author SuShaohua
     * @date 2016/6/2 14:20
     * @param
     */
    @RequestMapping(value = "/cancelQuoteOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String cancelQuoteOrder(@RequestBody QuoteOrderRequest req) {
        HisResponse hisResponse = new HisResponse();
        Map<String, Integer> body = new HashMap<>();
        Integer result = 0;
        try {
            result = quoteService.cancelQuoteOrder(req);
        }catch (Exception e){
            e.printStackTrace();
            hisResponse.setErrorCode(5000);
            hisResponse.setErrorMessage("取消订单报价失敗");
        }
        body.put("result", result);
        return HisResponse.getInstance(body).toString();
    }
}
