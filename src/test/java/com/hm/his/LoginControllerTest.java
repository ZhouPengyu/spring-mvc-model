package com.hm.his;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSON;

public class LoginControllerTest  extends BaseControllerTest {

    @Test
    public void Login() throws Exception {
        Map map = new HashMap();
        map.put("doctorName", "gyp1");
        map.put("password", "96e79218965eb72c92a549dd5a330112");
        super.testRole("login/login", JSON.toJSONString(map));
    }

    @Test
    public void getSysFrame() throws Exception {
        Map map = new HashMap();
        super.testRole("/login/getSysFrame", JSON.toJSONString(map));
    }




}
