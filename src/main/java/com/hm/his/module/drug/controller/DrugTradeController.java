package com.hm.his.module.drug.controller;

import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.google.common.collect.Lists;
import com.hm.his.framework.crypt.MD5Utils;
import com.hm.his.framework.model.HisResponse;
import com.hm.his.module.drug.dao.DrugTradeMapper;
import com.hm.his.module.drug.dao.HospitalDrugMapper;
import com.hm.his.module.drug.model.DrugTrade;
import com.hm.his.module.drug.model.HospitalDrug;
import com.hm.his.module.drug.pojo.DrugRequest;
import com.hm.his.module.user.dao.DoctorMapper;
import com.hm.his.module.user.dao.HospitalMapper;
import com.hm.his.module.user.model.Doctor;
import com.hm.his.module.user.model.Hospital;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016-07-15
 * Time: 11:49
 * CopyRight:HuiMei Engine
 */
@RestController
@RequestMapping("/drugData")
public class DrugTradeController {

    private static String UPDATE_KEY="3d21552032f046ba34705778f174f608";


    @Autowired(required = false)
    private DoctorMapper doctorMapper;

    @Autowired(required = false)
    private DrugTradeMapper tradeMapper;

    @Autowired(required = false)
    private HospitalMapper hospitalMapper;

    @Autowired(required = false)
    private HospitalDrugMapper hospitalDrugMapper;





    /**
     *  功能描述：批量更新 诊所药品 的药品拼音字段
     * @author:  tangww
     * @createDate   2016-07-13
     *
     */
    @RequestMapping(value = {"/batchUpdateHospitalDrug"}, produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    @ResponseBody
    public String batchUpdateHospitalDrug(HttpServletRequest request){
        HisResponse hisResponse = new HisResponse();

        String key = request.getParameter("key");
        if(StringUtils.isNotBlank(key)&&key.equals(UPDATE_KEY)){
            Map<String, Object> body = new HashMap<String, Object>();

            List<Hospital> hospitals = hospitalMapper.searchHospital(null);
            System.out.println("batchUpdateHospitalDrug===hospitals："+hospitals.size());
            for(int i=0;i<hospitals.size();i++){
                Hospital hospital = hospitals.get(i);
                List<HospitalDrug> hospitalDrugs = hospitalDrugMapper.searchDrugByHospitalId(hospital.getHospitalId());
                if(CollectionUtils.isNotEmpty(hospitalDrugs)){
                    System.out.println("batchUpdateHospitalDrug===HospitalName："+hospital.getHospitalName());
                    hospitalDrugs = hospitalDrugs.stream().map(hospitalDrug -> {
                        hospitalDrug.setPinyin(PinyinHelper.convertToPinyinString(hospitalDrug.getDrugName(), "", PinyinFormat.WITHOUT_TONE));
                        hospitalDrug.setPinyinFirst(PinyinHelper.getShortPinyin(hospitalDrug.getDrugName()));
                        return  hospitalDrug;
                    }).collect(Collectors.toList());
                    hospitalDrugMapper.batchUpdateDrugPinyin(hospitalDrugs);
                    body.put(hospital.getHospitalName(), "共更新"+hospitalDrugs.size()+"条记录");
                }
            }
            hisResponse.setBody(body);
        }else{
            hisResponse.setErrorCode(400L);
            hisResponse.setErrorMessage("");
        }
        return hisResponse.toString();
    }


    /**
     *  功能描述：批量更新 指定多音字 诊所库药品 的药品拼音字段
     * @author:  tangww
     * @createDate   2016-07-13
     *
     */
    @RequestMapping(value = {"/batchUpdateHospitalDrugLittle"}, produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    @ResponseBody
    public String batchUpdateHospitalDrugLittle(HttpServletRequest request){
        HisResponse hisResponse = new HisResponse();

        String key = request.getParameter("key");
        if(StringUtils.isNotBlank(key)&&key.equals(UPDATE_KEY)){
            Map<String, Object> body = new HashMap<String, Object>();

            List<DrugRequest> drugRequests = Lists.newArrayList();
            DrugRequest drugRequest = new DrugRequest();
            drugRequest.setDrugName("参");
            drugRequest.setPinyin("can");
            drugRequest.setCurrentPage(1);
            drugRequest.setPageSize(1000);
            drugRequests.add(drugRequest);
            DrugRequest drugRequest2 = new DrugRequest();
            drugRequest2.setDrugName("术");
            drugRequest2.setPinyin("shu");
            drugRequest2.setCurrentPage(1);
            drugRequest2.setPageSize(1000);
            drugRequests.add(drugRequest2);

            for(DrugRequest drugReq :drugRequests){
                List<HospitalDrug> hospitalDrugs = hospitalDrugMapper.searchHospitalDrugForPinyin(drugReq);
                if(CollectionUtils.isNotEmpty(hospitalDrugs)){
                    hospitalDrugs = hospitalDrugs.stream().map(hospitalDrug -> {
                        hospitalDrug.setPinyin(PinyinHelper.convertToPinyinString(hospitalDrug.getDrugName(), "", PinyinFormat.WITHOUT_TONE));
                        hospitalDrug.setPinyinFirst(PinyinHelper.getShortPinyin(hospitalDrug.getDrugName()));
                        return  hospitalDrug;
                    }).collect(Collectors.toList());
                    hospitalDrugMapper.batchUpdateDrugPinyin(hospitalDrugs);
                    body.put(drugReq.getDrugName()+" 字", "共更新条"+hospitalDrugs.size()+"记录");
                }
            }


            hisResponse.setBody(body);
        }else{
            hisResponse.setErrorCode(400L);
            hisResponse.setErrorMessage("");
        }
        return hisResponse.toString();
    }

    /**
     *  功能描述：批量更新 惠每库药品 的药品拼音字段
     * @author:  tangww
     * @createDate   2016-07-13
     *
     */
    @RequestMapping(value = {"/batchUpdateDrugTrade"}, produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    @ResponseBody
    public String batchUpdateDrugTrade(HttpServletRequest request){
        HisResponse hisResponse = new HisResponse();

        String key = request.getParameter("key");
        if(StringUtils.isNotBlank(key)&&key.equals(UPDATE_KEY)){
            Map<String, Object> body = new HashMap<String, Object>();
            //310
            for(int i=1;i<310;i++){
                DrugRequest drugRequest = new DrugRequest();
                drugRequest.setCurrentPage(i);
                drugRequest.setPageSize(1000);
                List<DrugTrade> tradeList = tradeMapper.searchDrugTradeList(drugRequest);
                if(CollectionUtils.isNotEmpty(tradeList)){
                    tradeList = tradeList.stream().map(drugTrade -> {
                        drugTrade.setCommonPinyin(PinyinHelper.convertToPinyinString(drugTrade.getCommonName(), "", PinyinFormat.WITHOUT_TONE));
                        drugTrade.setCommonPinyinFirst(PinyinHelper.getShortPinyin(drugTrade.getCommonName()));
                        if(StringUtils.isNotEmpty(drugTrade.getTradeName())){
                            drugTrade.setTradePinyin(PinyinHelper.convertToPinyinString(drugTrade.getTradeName(), "", PinyinFormat.WITHOUT_TONE));
                            drugTrade.setTradePinyinFirst(PinyinHelper.getShortPinyin(drugTrade.getTradeName()));
                        }

                        return drugTrade;
                    }).collect(Collectors.toList());
                     int  updateResult = tradeMapper.batchUpdateDrugPinyin(tradeList);
                    body.put("第"+i+"页", "共更新条"+tradeList.size()+"记录，第一个ID："+tradeList.get(0).getId());
                }
            }
            hisResponse.setBody(body);
        }else{
            hisResponse.setErrorCode(400L);
            hisResponse.setErrorMessage("");
        }
        return hisResponse.toString();
    }

    /**
     *  功能描述：批量更新  指定多音字 惠每库药品 的药品拼音字段
     * @author:  tangww
     * @createDate   2016-07-13
     *
     */
    @RequestMapping(value = {"/batchUpdateDrugTradeLittle"}, produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    @ResponseBody
    public String batchUpdateDrugTradeLittle(HttpServletRequest request){
        HisResponse hisResponse = new HisResponse();

        String key = request.getParameter("key");
        if(StringUtils.isNotBlank(key)&&key.equals(UPDATE_KEY)){
            Map<String, Object> body = new HashMap<String, Object>();



            List<DrugRequest> drugRequests = Lists.newArrayList();
            DrugRequest drugRequest = new DrugRequest();
            drugRequest.setDrugName("参");
            drugRequest.setPinyin("can");
            drugRequest.setCurrentPage(1);
            drugRequest.setPageSize(1000);
            drugRequests.add(drugRequest);
            DrugRequest drugRequest2 = new DrugRequest();
            drugRequest2.setDrugName("术");
            drugRequest2.setPinyin("shu");
            drugRequest2.setCurrentPage(1);
            drugRequest2.setPageSize(1000);
            drugRequests.add(drugRequest2);

            for(DrugRequest drugReq :drugRequests){
                List<DrugTrade> tradeList = tradeMapper.searchDrugTradeList(drugReq);
                if(CollectionUtils.isNotEmpty(tradeList)){
                    tradeList = tradeList.stream().map(drugTrade -> {
                        drugTrade.setCommonPinyin(PinyinHelper.convertToPinyinString(drugTrade.getCommonName(), "", PinyinFormat.WITHOUT_TONE));
                        drugTrade.setCommonPinyinFirst(PinyinHelper.getShortPinyin(drugTrade.getCommonName()));
                        if(StringUtils.isNotEmpty(drugTrade.getTradeName())){
                            drugTrade.setTradePinyin(PinyinHelper.convertToPinyinString(drugTrade.getTradeName(), "", PinyinFormat.WITHOUT_TONE));
                            drugTrade.setTradePinyinFirst(PinyinHelper.getShortPinyin(drugTrade.getTradeName()));
                        }

                        return drugTrade;
                    }).collect(Collectors.toList());
                    int  updateResult = tradeMapper.batchUpdateDrugPinyin(tradeList);
                    body.put(drugReq.getDrugName()+" 字", "共更新条"+tradeList.size()+"记录");
                }
            }


            hisResponse.setBody(body);
        }else{
            hisResponse.setErrorCode(400L);
            hisResponse.setErrorMessage("");
        }
        return hisResponse.toString();
    }
}
