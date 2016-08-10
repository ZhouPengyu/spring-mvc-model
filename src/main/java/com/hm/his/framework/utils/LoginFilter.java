package com.hm.his.framework.utils;

import com.alibaba.fastjson.JSON;
import com.hm.his.framework.log.constant.FunctionConst;
import com.hm.his.framework.log.constant.OperResultConst;
import com.hm.his.framework.log.constant.Operation;
import com.hm.his.framework.model.HisResponse;
import com.hm.his.framework.model.RequestClientInfo;
import com.hm.his.module.log.model.SystemLog;
import com.hm.his.module.log.service.SystemLogService;
import com.hm.his.module.user.model.Doctor;
import com.hm.his.module.user.service.DoctorService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.List;

public class LoginFilter implements Filter {

	Logger logger = Logger.getLogger(LoginFilter.class);
	
	
//	private DoctorService doctorService;

	private static List<String> WHITE_LIST_URL = new ArrayList<String>();
// 是否为开发都测试模式，true 则一切URL放行， 应常设置为 false
	public static final Boolean DEV_MODE = false;

	static {
		WHITE_LIST_URL.add("/login/login");
		WHITE_LIST_URL.add("/user/hospitalRegister");
		WHITE_LIST_URL.add("/login/logout");
		WHITE_LIST_URL.add("/login/loginStatus");
		WHITE_LIST_URL.add("/user/verifyUserName");
		WHITE_LIST_URL.add("/user/register");
		WHITE_LIST_URL.add("/login/sendSmsCaptcha");
		WHITE_LIST_URL.add("/user/activationPhoneNo");
		WHITE_LIST_URL.add("/user/resetPassword");
		WHITE_LIST_URL.add("/user/retrievePassword");
		WHITE_LIST_URL.add("/outpatient/searchDisease");
		WHITE_LIST_URL.add("/outpatient/searchDisease");
		WHITE_LIST_URL.add("/oauth");
		WHITE_LIST_URL.add("/login/imgCaptcha");
//		WHITE_LIST_URL.add("/user/batchUpdateDocterInfo");
//		WHITE_LIST_URL.add("/weixin/user/batchUpdateBoundInfo");
//		WHITE_LIST_URL.add("/drugData/batchUpdateHospitalDrug");
		WHITE_LIST_URL.add("/drugData/batchUpdateHospitalDrugLittle");
//		WHITE_LIST_URL.add("/drugData/batchUpdateDrugTrade");
		WHITE_LIST_URL.add("/drugData/batchUpdateDrugTradeLittle");
		WHITE_LIST_URL.add("/message/saveAndSendMessage");



//		WHITE_LIST_URL.add("/inventory/searchDrugByNameForSale");
	}
	


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
//		ServletContext sc = filterConfig.getServletContext();
//		WebApplicationContext wac = (WebApplicationContext) sc.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		//获得WebApplicationContext还可以调用
		//WebApplicationContextUtils.getWebApplicationContext(sc);
		//当然最后Spring都是调用getAttribute方法.
//		this.doctorService = (DoctorService) wac.getBean("doctorService");
	}

	private boolean isCanAccess(HttpServletRequest request, String url){

		if(DEV_MODE){
			return true;
		}

		if(WHITE_LIST_URL !=null && WHITE_LIST_URL.size() > 0){
			int i = 0;
			int length = WHITE_LIST_URL.size();
			for(;i<length;i++){
				String nocheck_url = request.getContextPath() + WHITE_LIST_URL.get(i);
				if(nocheck_url.equals(url)){
					return true;
				}
			}
		}
		return false;
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpSession session = httpRequest.getSession();
		String userLoginIP = IPAndPathUtils.getRealIpAddres(httpRequest);
		String requestURI = httpRequest.getRequestURI().replaceAll("//", "/");
		HisResponse hisResponse = new HisResponse();
		hisResponse.setErrorCode(401);
		hisResponse.setErrorMessage("对不起，您尚未登录，请先登录");
		String reqMethod = httpRequest.getMethod();
		if(StringUtils.isNotBlank(reqMethod)&& reqMethod.equalsIgnoreCase("OPTIONS")){
			chain.doFilter(httpRequest, response);
			return;
		}
		if (isCanAccess(httpRequest,requestURI) ) {
			chain.doFilter(httpRequest, response);
			return;
		} else {
			
			//微信登录标识
			Object openId = session.getAttribute("openId");
			
			//惠每云诊所登录标识
			Long doctorId = LangUtils.getLong(session.getAttribute("doctorId"));
			Long hospitalId = LangUtils.getLong(session.getAttribute("hospitalId"));
			
			if (null != openId) {	//微信登录
				chain.doFilter(httpRequest, response);
				return ;
			}else if(null != doctorId && null != hospitalId){	//云诊所登录
				chain.doFilter(httpRequest, response);
				return ;
			}else{
				
				if(requestURI.contains("/weixin/")){	//处理微信业务
					hisResponse.setErrorCode(401);
					hisResponse.setErrorMessage("微信登录授权失败！");
					response.getWriter().write(hisResponse.toString());
					response.getWriter().flush();
					response.getWriter().close();
					return ;
				}
				
				String cookieValue = CookieUtils.getCookieValue(httpRequest, SessionUtils.COOKIE_NAME);

				if(StringUtils.isEmpty(cookieValue)){
					addFaileLogFor401(httpRequest,"用户未登录，cookie为空");
					//记录登录 失败的日志
					response.getWriter().write(hisResponse.toString());
					response.getWriter().flush();
					response.getWriter().close();
//					logger.error("用户未登录，cookie为空，IP："+userLoginIP+" 访问地址："+requestURI+" method:"+reqMethod);
					return ;
				}else{
					//记录通过cookie登录成功的日志
					try {
						cookieValue = HmDesUtils.decrypt(cookieValue);
						doctorId = Long.parseLong(cookieValue.split("-")[0]);
						
//						ServletContext sc = httpRequest.getSession().getServletContext();
//						ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(sc);
						DoctorService doctorService = (DoctorService)DynamicBeanManager.getBean("doctorService");
						Doctor doctor = doctorService.getDoctorById(doctorId);
						if(doctor!=null){
							session.setAttribute("isAdmin", doctor.getIsAdmin());
							session.setAttribute("doctorId", doctorId);
							session.setAttribute("currentUser",doctor);
							session.setAttribute("hospitalId", Long.parseLong(cookieValue.split("-")[1]));
							//验证有效性
							chain.doFilter(httpRequest, response);
							return ;
						}else{
							addFaileLogFor401(httpRequest,doctorId+"用户ID未登录,无法查找到用户");
							response.getWriter().write(hisResponse.toString());
							response.getWriter().flush();
							response.getWriter().close();
							logger.error(doctorId+"用户ID未登录,无法查找到用户，IP："+userLoginIP+" 访问地址："+requestURI);
							return ;
						}

					} catch (Exception e) {
						e.printStackTrace();
						CookieUtils.cleanCookie((HttpServletResponse) response, SessionUtils.COOKIE_NAME);
						addFaileLogFor401(httpRequest,"用户未登录,解析cookie异常");
						response.getWriter().write(hisResponse.toString());
						response.getWriter().flush();
						response.getWriter().close();
						logger.error("用户未登录,解析cookie异常，IP："+userLoginIP+" 访问地址："+requestURI);
						return ;
					}
				}
			}
		}
	}

	private void addFaileLogFor401(HttpServletRequest httpRequest,String description) {
		SystemLog faileLog = new SystemLog();
		faileLog.setFunctionId(FunctionConst.LOGON);
		faileLog.setOperationId(Operation.LOGIN_FAILE);
		faileLog.setOperationResult(OperResultConst.FAILED);

		RequestClientInfo info = new RequestClientInfo();
		info.setProtocol(httpRequest.getProtocol());//获取请求使用的通信协议，如http/1.1等
		info.setServletPath(httpRequest.getServletPath());//获取请求的JSP也面所在的目录。
		info.setContentLength(httpRequest.getContentLength());//获取HTTP请求的长度。
		info.setMethod(httpRequest.getMethod());//获取表单提交信息的方式，如POST或者GET。
		//httpRequest.getHeader(String s);//获取请求中头的值。一般来说，S参数可取的头名有accept,referrer、accept-language、content-type、accept-encoding、user-agent、host、cookie等，比如，S取值user-agent将获得用户的浏览器的版本号等信息。
		info.setHeaderNames(httpRequest.getHeaderNames());//获取头名字的一个枚举。
		//httpRequest.getHeaders(String s);//获取头的全部值的一个枚举。
		info.setRemoteAddr(httpRequest.getRemoteAddr());//获取客户的IP地址。
		info.setRemoteHost(httpRequest.getRemoteHost());//获取客户机的名称（如果获取不到，就获取IP地址）。
		info.setServerName(httpRequest.getServerName());//获取服务器的名称。
		info.setServerPort(httpRequest.getServerPort());//获取服务器的端口。
		info.setParameterNames(httpRequest.getParameterNames());
		;//获取表单提交的信息体部分中name参数值的一个枚举

		RequestClientInfo.Header header = new RequestClientInfo.Header();
		header.setAcceptEncoding(httpRequest.getHeader("Accept-Encoding"));
		header.setAcceptLanguage(httpRequest.getHeader("Accept-Language"));
		header.setConnection(httpRequest.getHeader("Connection"));
		header.setCookie(httpRequest.getHeader("Cookieg"));
		header.setHost(httpRequest.getHeader("Host"));
		header.setReferer(httpRequest.getHeader("Referer"));
		header.setUserAgent(httpRequest.getHeader("User-Agent"));
		info.setHeaders(header);

		faileLog.setParams(JSON.toJSONString(info));
		faileLog.setDescription(description);
		//获取请求ip
		String loginIP = IPAndPathUtils.getRealIpAddres(httpRequest);
		faileLog.setLoginIp(loginIP);
		SystemLogService systemLogService = (SystemLogService) DynamicBeanManager.getBean("systemLogService");
		systemLogService.addSystemLogWithOutLogin(faileLog);
	}

	public static final byte[] readBytes(PushbackInputStream is, int contentLen) {
		if (contentLen > 0) {
			int readLen = 0;
			int readLengthThisTime = 0;
			byte[] message = new byte[contentLen];

			try {
				while (readLen != contentLen) {
					readLengthThisTime = is.read(message, readLen, contentLen - readLen);
					if (readLengthThisTime == -1) {
						break;
					}
					readLen += readLengthThisTime;
				}

				return message;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return new byte[] {};
	}

	public void destroy() {

	}

}
