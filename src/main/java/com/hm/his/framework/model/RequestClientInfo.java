package com.hm.his.framework.model;

import java.util.Enumeration;

/**
 * 记录用户的客户端信息
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/4/15
 * Time: 20:51
 * CopyRight:HuiMei Engine
 */
public class RequestClientInfo {

    private String protocol;//获取请求使用的通信协议，如http/1.1等
    private String servletPath;//获取请求的JSP也面所在的目录。
    private Integer contentLength;//获取HTTP请求的长度。
    private String method;//获取表单提交信息的方式，如POST或者GET。
//    private String header;//获取请求中头的值。一般来说，S参数可取的头名有accept,referrer、accept-language、content-type、accept-encoding、user-agent、host、cookie等，比如，S取值user-agent将获得用户的浏览器的版本号等信息。
    private java.util.Enumeration<java.lang.String>  headerNames;//获取头名字的一个枚举。
    private Header headers;//获取头的全部值的一个枚举。
    private String remoteAddr;//获取客户的IP地址。
    private String remoteHost;//获取客户机的名称（如果获取不到，就获取IP地址）。
    private String serverName;//获取服务器的名称。
    private Integer serverPort;//获取服务器的端口。
    private java.util.Enumeration<java.lang.String>  parameterNames;//获取表单提交的信息体部分中name参数值的一个枚举


    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getServletPath() {
        return servletPath;
    }

    public void setServletPath(String servletPath) {
        this.servletPath = servletPath;
    }



    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }




    public Header getHeaders() {
        return headers;
    }

    public void setHeaders(Header headers) {
        this.headers = headers;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public Integer getContentLength() {
        return contentLength;
    }

    public void setContentLength(Integer contentLength) {
        this.contentLength = contentLength;
    }

    public Enumeration<String> getHeaderNames() {
        return headerNames;
    }

    public void setHeaderNames(Enumeration<String> headerNames) {
        this.headerNames = headerNames;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public Enumeration<String> getParameterNames() {
        return parameterNames;
    }

    public void setParameterNames(Enumeration<String> parameterNames) {
        this.parameterNames = parameterNames;
    }

    public static class Header{
        private String host;
        private String referer;
        private String acceptLanguage;
        private String acceptEncoding;
        private String userAgent;
        private String connection;
        private String cookie;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getReferer() {
            return referer;
        }

        public void setReferer(String referer) {
            this.referer = referer;
        }

        public String getAcceptLanguage() {
            return acceptLanguage;
        }

        public void setAcceptLanguage(String acceptLanguage) {
            this.acceptLanguage = acceptLanguage;
        }

        public String getAcceptEncoding() {
            return acceptEncoding;
        }

        public void setAcceptEncoding(String acceptEncoding) {
            this.acceptEncoding = acceptEncoding;
        }

        public String getUserAgent() {
            return userAgent;
        }

        public void setUserAgent(String userAgent) {
            this.userAgent = userAgent;
        }

        public String getConnection() {
            return connection;
        }

        public void setConnection(String connection) {
            this.connection = connection;
        }

        public String getCookie() {
            return cookie;
        }

        public void setCookie(String cookie) {
            this.cookie = cookie;
        }
    }

}
