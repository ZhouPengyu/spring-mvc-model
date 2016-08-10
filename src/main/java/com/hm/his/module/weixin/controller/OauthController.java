package com.hm.his.module.weixin.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hm.his.framework.cache.redis.JedisHelper;
import com.hm.his.framework.model.HisResponse;
import com.hm.his.framework.utils.LangUtils;
import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.module.Constants;
import com.hm.his.module.Global;
import com.hm.his.module.user.model.User;
import com.hm.his.module.user.service.UserService;
import com.hm.his.module.weixin.pojo.OAuthInfo;
import com.hm.his.module.weixin.pojo.WeixinUserInfo;
import com.hm.his.module.weixin.utils.WeixinUtil;

@RestController
public class OauthController {
	
	@Autowired(required = false)
	UserService userService;

	/**
     * <p>Description:网页授权<p>
     * @author ZhouPengyu
     * @date 2016-4-20 11:38:19
     */
    @RequestMapping(value = {"/oauth"}, method = RequestMethod.GET)
    @ResponseBody
    public void oauth(HttpServletRequest request, HttpServletResponse response){
    	try {
    		request.setCharacterEncoding("UTF-8");
        	response.setCharacterEncoding("UTF-8");
        	
        	// 用户同意授权后，获取code
        	String code = request.getParameter("code");
        	// 跳转申明
        	String state = request.getParameter("state");
        	if(null != code && !"".equals(code)){
        		OAuthInfo oa = WeixinUtil.getOAuthInfo(Constants.AppId,Constants.AppSecret,code);
        		if(null == oa){
        			return ;
        		}
        		String openId = oa.getOpenId();
        		HttpSession session = SessionUtils.getSession();
        		session.setAttribute("openId", openId);
        		session.setAttribute("state", state);
        		User user = userService.getUserByOpenId(openId);
        		if(null == user){
        			WeixinUserInfo weixinUserInfo = WeixinUtil.getWeixinUserInfo(oa.getAccessToken(), openId);
    				user = new User();
    				user.setOpenId(weixinUserInfo.getOpenId());
    				user.setNickname(weixinUserInfo.getNickname());
    				user.setSex(LangUtils.getLong(weixinUserInfo.getSex()));
    				user.setCity(weixinUserInfo.getCity());
    				user.setProvince(weixinUserInfo.getProvince());
    				user.setCountry(weixinUserInfo.getCountry());
    				user.setHeadimgurl(weixinUserInfo.getHeadImgUrl());
    				user.setSubscribeTime(weixinUserInfo.getSubscribeTime());
    				user.setUnionid(weixinUserInfo.getUnionid());
    				user.setRemark(weixinUserInfo.getRemark());
    				userService.insertUser(user);
        		}
        		if("myhis".equals(state))
        			response.sendRedirect(Constants.WebUrl+"view/login/login.html");
        		else if("statistics".equals(state))
        			response.sendRedirect(Constants.WebUrl+"view/statics/statics.html");
        		else if("scan_inventory".equals(state))
        			response.sendRedirect(Constants.WebUrl+"view/choosehospital/scan.html");
        		else if("direct_inventory".equals(state))
        			response.sendRedirect(Constants.WebUrl+"view/choosehospital/direct.html");
        	}else{
        		response.sendRedirect("http://wxtest.huimeionline.com/weixin/openId.jsp");
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    /**
     * <p>Description:JS—SDK授权<p>
     * @author ZhouPengyu
     * @date 2016-4-20 11:38:19
     */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/weixin/oauth/getWxJsConfig"}, produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String getWxConfig(HttpServletRequest request, HttpServletResponse response){
    	HisResponse hisResponse = new HisResponse();
//    	StringBuffer requestUrl = request.getRequestURL();
//    	String queryString = request.getQueryString();
        String url = request.getHeader("Referer");
        
		Map<String, String> tokenMap = JedisHelper.get(HashMap.class, Global.WeiXinToken);
		
		Map<String, String> wxConfig = new HashMap<String, String>();
		if(null==tokenMap){
			String accessToken = WeixinUtil.getAccessToken(Constants.AppId,Constants.AppSecret);
    		String jsapiTicket = WeixinUtil.getJsapiTicket(accessToken);
    		
    		wxConfig = WeixinUtil.getJsapiTicketSignature(jsapiTicket, url);
    		
    		tokenMap = new HashMap<String, String>();
    		tokenMap.put("accessToken", accessToken);
    		tokenMap.put("jsapiTicket", jsapiTicket);
    		
    		JedisHelper.set(Global.WeiXinToken, 7000, tokenMap);
		}else{
			wxConfig = WeixinUtil.getJsapiTicketSignature(tokenMap.get("jsapiTicket"), url);
		}
		
		hisResponse.setBody(wxConfig);
		return hisResponse.toString();
    }
}
