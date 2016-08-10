package com.hm.his.module.weixin.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hm.his.framework.utils.SessionUtils;

@RestController
public class OpenIdController {
	
	/**
     * <p>Description:接口处理<p>
     * @author ZhouPengyu
     * @date 2016-4-20 11:38:19
     */
    @RequestMapping(value = {"/getOpenId"}, method = RequestMethod.GET)
    @ResponseBody
    public void oauth(HttpServletRequest request, HttpServletResponse response){
    	if(SessionUtils.getSession().getAttribute("openId")!=null){
    		try {
    			response.sendRedirect("http://zhoupengyu.bceapp.com/");
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
    	try {
			response.sendRedirect("https://www.baidu.com/");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
