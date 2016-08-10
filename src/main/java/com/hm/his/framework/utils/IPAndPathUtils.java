package com.hm.his.framework.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取配置文件的相对路径信息
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/4/14
 * Time: 11:45
 * CopyRight:HuiMei Engine
 */
public class IPAndPathUtils {
    private static String filePath = "";
    // 获取配置文件的相对路径信息
    public static String getResourcePath() {
        if ("".equals(filePath)) {
            String result = IPAndPathUtils.class.getResource("IPAndPathUtils.class").toString();
            int index = result.indexOf("WEB-INF");
            if (index == -1) {
                index = result.indexOf("bin");
            }
            result = result.substring(0, index);
            if (result.startsWith("jar")) {
                int k = result.indexOf("jar");
                result = result.substring(k+10);// 当class文件在jar文件中时，返回”jar:file:/F:/…”样的路径
            } else if (result.startsWith("file")) {
                int k = result.indexOf("file");
                result = result.substring(k+6);// 当class文件在jar文件中时，返回”file:/F:/…”样的路径
            } else if (result.startsWith("zip:")) {
                int k = result.indexOf("zip");
                result = result.substring(k+5);// 当class文件在jar文件中时，返回”/zip:/F:/…”样的路径
            }
            result = result.replace("%20", " ");
            filePath = "/"+result;
        }
        return filePath;
    }

    /**
     * 获取用户登录时本地的ip地址
     *
     * @param request
     * @return
     */
    public static String getRealIpAddres(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
    }
}
