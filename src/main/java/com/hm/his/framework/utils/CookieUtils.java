package com.hm.his.framework.utils;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by wangjialin on 15/12/20.
 */
public class CookieUtils {

    public static Cookie getCookieByName(HttpServletRequest request,String name){
        Map<String,Cookie> cookieMap = ReadCookieMap(request);
        if(cookieMap.containsKey(name)){
            Cookie cookie = (Cookie)cookieMap.get(name);
            return cookie;
        }else{
            return null;
        }
    }
    private static Map<String,Cookie> ReadCookieMap(HttpServletRequest request){
        Map<String,Cookie> cookieMap = new HashMap<String,Cookie>();
        Cookie[] cookies = request.getCookies();
        if(null!=cookies){
            for(Cookie cookie : cookies){
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }

    /**
     * 根据Cookie名称得到Cookie的值，没有返回Null
     *
     * 2006-7-28
     *
     * @param request
     * @param name
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie cookie = getCookie(request, name);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

    /**
     * 根据Cookie名称得到Cookie的值，没有返回Null(当Cookie的值含有空格时使用该方法)
     * @param request
     * @param name
     * @return
     */
    public static String getSpaceCookieValue(HttpServletRequest request, String name) {
        String cookies = request.getHeader("Cookie");
        if (cookies == null) {
            return null;
        }
        int start = cookies.indexOf(name + "=");
        if (start == -1) {
            return null;
        }
        String value = cookies.substring(start + name.length() + 1);
        start = value.indexOf(";");
        if (start == -1) {
            start = value.length();
        }
        if (value.startsWith("\"")) {
            value = value.substring(1, start - 1);
        } else {
            value = value.substring(0, start);
        }
        value = value.replace("\\\"", "\"").replace("\\\\", "\\");
        return value;
    }

    /**
     * 根据Cookie名称得到Cookie对象，不存在该对象则返回Null
     *
     * 2006-7-28
     *
     * @param request
     * @param name
     * @return
     */
    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie cookies[] = request.getCookies();
        if (cookies == null || name == null || name.length() == 0) {
            return null;
        }
        Cookie cookie = null;
        for (int i = 0; i < cookies.length; i++) {
            if (!cookies[i].getName().equals(name)) {
                continue;
            }
            cookie = cookies[i];
            if (request.getServerName().equals(cookie.getDomain())) {
                break;
            }
        }
        return cookie;
    }

    /**
     * 删除指定Cookie
     *
     * 2006-7-28
     *
     * @param response
     * @param cookie
     */
    public static void deleteCookie(HttpServletResponse response, Cookie cookie) {
        if (cookie != null) {
            cookie.setPath("/");
            cookie.setValue("");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }

    /**
     * 删除指定Cookie
     *
     * 2006-7-28
     *
     * @param response
     * @param cookie
     */
    public static void deleteCookie(HttpServletResponse response,
                                    Cookie cookie, String domain) {
        if (cookie != null) {
            cookie.setPath("/");
            cookie.setValue("");
            cookie.setMaxAge(0);
            cookie.setDomain(domain);
            response.addCookie(cookie);
        }
    }
    
    /**
     * 删除指定Cookie
     *
     * 2006-7-28
     *
     * @param response
     * @param cookie
     */
    public static void cleanCookie(HttpServletResponse response, String cookieName) {
    	Cookie cookie= new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 添加一条新的Cookie信息，默认有效时间为一个月
     *
     * 2006-7-28
     *
     * @param response
     * @param name
     * @param value
     */
    public static void setCookie(HttpServletResponse response, String name,
                                 String value) {
        setCookie(response, name, value, 0x278d00);
    }

    /**
     * 添加一条新的Cookie信息，可以设置其最长有效时间(单位：秒)
     *
     * 2006-7-28
     *
     * @param response
     * @param name
     * @param value
     * @param maxAge
     */
    public static void setCookie(HttpServletResponse response, String name,
                                 String value, int maxAge) {
        if (value == null)
            value = "";
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 添加一条新的Cookie信息，可以设置其Name，Value，MaxAge，Path，Domain(单位：秒)
     *
     * 2006-8-23
     *
     * @param response
     * @param name
     * @param value
     * @param maxAge
     */
    public static void setCookie(HttpServletResponse response, String name,
                                 String value, int maxAge, String path, String domain) {
        if (value == null)
            value = "";
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath(path);
        cookie.setDomain(domain);
        response.addCookie(cookie);
    }
}
