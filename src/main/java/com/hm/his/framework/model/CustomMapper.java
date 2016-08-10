package com.hm.his.framework.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016-08-05
 * Time: 12:46
 * CopyRight:HuiMei Engine
 */
public class CustomMapper extends ObjectMapper{
    public CustomMapper() {
        this.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 设置 SerializationFeature.FAIL_ON_EMPTY_BEANS 为 false
        this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }
}
