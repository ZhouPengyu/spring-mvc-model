package com.hm.his.module.drug.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.his.framework.model.HisResponse;
import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.module.drug.model.Dictionary;
import com.hm.his.module.drug.pojo.HospitalDrugSug;
import com.hm.his.module.drug.service.DictionaryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 *  字典Controller
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/2/26
 * Time: 14:42
 * CopyRight:HuiMei Engine
 */
@RestController
@RequestMapping("/dict")
public class DictionaryController {

    @Autowired(required = false)
    DictionaryService dictionaryService;

    /**
     *  功能描述： 5.11.字典sug
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    @RequestMapping(value = "/searchDictionary", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String searchDictionary(@RequestBody Dictionary dictionary){
        List<Dictionary> dictionaryList = Lists.newArrayList();
        HisResponse hisResponse = new HisResponse();
        if(null==dictionary.getType()){
            hisResponse.setErrorCode(50001L);
            hisResponse.setErrorMessage("字典类型不能为空");
            return hisResponse.toString();
        }
        if (StringUtils.isNotBlank(dictionary.getCnName())){
            dictionaryList = dictionaryService.searchDictionaryByName(dictionary.getType(),dictionary.getCnName());
        }else{
            dictionaryList = dictionaryService.searchDefaultDictionary(dictionary.getType());
        }

        Map<String,Object> body = Maps.newHashMap();
        body.put("dictList",dictionaryList);
        hisResponse.setBody(body);
        return hisResponse.toString();

    }

    /**
     *  功能描述： 获取服务端 session ID
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    @RequestMapping(value = "/getSessionId", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String getSessionId(){

        Map<String,Object> body = Maps.newHashMap();
        body.put("JSESSIONID", SessionUtils.getSession().getId());
        HisResponse hisResponse = HisResponse.getInstance(body);
        return hisResponse.toString();

    }
}
