package com.hm.his.framework.utils;

import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjialin on 15/12/11.
 */
public class SimpleCORSFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        List<String> allowOrigin = new ArrayList<>();
        allowOrigin.add("null");
        allowOrigin.add("huimeionline.com");
        allowOrigin.add("wxtest.huimeionline.com");
        allowOrigin.add("10.171.221.103");
        allowOrigin.add("121.40.104.98");
        allowOrigin.add("localhost");
        allowOrigin.add("huimei.com");
        allowOrigin.add("127.0.0.1");
//        String contentType =  request.getContentType();
//        System.out.println("====================="+contentType);
////        if(StringUtils.isBlank(contentType) ){
//            ServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper(request);
//            String body = HttpHelper.getBodyString(requestWrapper);
//
//            System.out.println("====================="+body);
////        }
        String origin = request.getHeader("Origin");
        if (origin != null) {
            for (String str : allowOrigin) {
                if (origin.contains(str)) {
                    response.setHeader("Access-Control-Allow-Origin", origin);
                    break;
                }
            }
        }
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type,Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        MDC.put("ip", req.getRemoteAddr());
        MDC.put("url", request.getRequestURL().toString());
        chain.doFilter(req, res);
    }

    public void init(FilterConfig filterConfig) {}

    public void destroy() {}
}
