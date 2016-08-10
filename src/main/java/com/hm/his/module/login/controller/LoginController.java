package com.hm.his.module.login.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.hm.his.framework.cache.redis.JedisHelper;
import com.hm.his.framework.log.constant.FunctionConst;
import com.hm.his.framework.log.constant.OperResultConst;
import com.hm.his.framework.log.constant.Operation;
import com.hm.his.framework.model.HisResponse;
import com.hm.his.framework.model.RequestClientInfo;
import com.hm.his.framework.utils.*;
import com.hm.his.module.Global;
import com.hm.his.module.log.model.SystemLog;
import com.hm.his.module.log.service.SystemLogService;
import com.hm.his.module.login.facade.LoginFacade;
import com.hm.his.module.login.model.*;
import com.hm.his.module.manage.model.HospitalConfig;
import com.hm.his.module.manage.service.HospitalConfigService;
import com.hm.his.module.user.model.Doctor;
import com.hm.his.module.user.model.Hospital;
import com.hm.his.module.user.service.DoctorService;
import com.hm.his.module.user.service.HospitalService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-2 11:35:38
 * @description 登录管理
 * @version 3.0
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired(required = false)
    DoctorService doctorService;
    @Autowired(required = false)
    HospitalService hospitalService;
	@Autowired(required = false)
	SystemLogService systemLogService;
	@Autowired(required = false)
	HospitalConfigService hospitalConfigService;
    
    Logger logger = Logger.getLogger(LoginController.class);
	static Log log = LogFactory.getLog("AccessLog");


    
    /**
     * <p>Description:用户登录<p>
     * @author ZhouPengyu
     * @date 2016-3-3 17:18:05
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = {"/login"}, produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String login(@RequestBody Map<String, String> requestParams, HttpServletRequest request, HttpServletResponse response){
    	HisResponse hisResponse = new HisResponse();
		SystemLog systemLog = new SystemLog();
		systemLog.setFunctionId(FunctionConst.LOGON);
		systemLog.setOperationId(Operation.LOGON_VALUE);
		systemLog.setParams(request.getHeader(SessionUtils.USER_AGENT));

		HttpSession session = request.getSession();
		Integer errorTime = session.getAttribute("errorTime") == null ? 0 : LangUtils.getInteger(session.getAttribute("errorTime"));	//登录错误次数
		
		try{
			if(errorTime >= 3){
				String imgCaptcha = requestParams.get("imgCaptcha");
				if(StringUtils.isEmpty(imgCaptcha)){
					hisResponse.setErrorCode(5005);
					hisResponse.setErrorMessage("请输入验证码!");
					return hisResponse.toString();
				}else if(!StringUtils.equalsIgnoreCase(LangUtils.getString(session.getAttribute("imgCaptcha")), imgCaptcha)){
					session.removeAttribute("imgCaptcha");
					hisResponse.setErrorCode(5004);
					hisResponse.setErrorMessage("验证码错误!");
					return hisResponse.toString();
				}
			}
			String doctorName = requestParams.get("doctorName");
			systemLog.setUserName(doctorName);
			String password = requestParams.get("password");
			String loginStatus = requestParams.get("loginStatus");//下次自动登录密码
			Doctor doctor = doctorService.getDoctorByName(doctorName);
			if (doctor !=null){
				boolean success = doctorService.verifyPassword(doctorName, password);
//				success = true;
				if(success){
					systemLog.setOperObjId(doctor.getDoctorId());
					systemLog.setOperObjName(doctor.getDoctorName());
					if(doctor.getFlag() !=null && doctor.getFlag().equals(0L)){
						doctor.setStatus(-1L);
						systemLog.setDescription("用户："+doctorName+",已禁用，禁止登录");
					}else if(doctor.getStatus() !=null &&doctor.getStatus()==1L){
						// 下次自动登录密码 为 1 时，将用户信息保存到 cookie
						if(loginStatus!=null && loginStatus.equals("1")){
							//cooke赋值    doctorId-hospitalId
							CookieUtils.setCookie(response, SessionUtils.COOKIE_NAME, HmDesUtils.encrypt(doctor.getDoctorId().toString()+"-"+doctor.getHospitalId().toString()),SessionUtils.MAX_AGE);
						}else{
							CookieUtils.cleanCookie(response, SessionUtils.COOKIE_NAME);
						}
						session.setAttribute("hospitalId", doctor.getHospitalId());
						session.setAttribute("isAdmin", doctor.getIsAdmin());
						if(doctor.getIsAdmin()!=null && doctor.getIsAdmin()!=1L){
							processUserPermissions(doctor);
						}
						session.setAttribute("doctorId", doctor.getDoctorId());
						doctor.setPassword(null);
						session.setAttribute("currentUser",doctor);
						Map<String, Object> body = new HashMap<String, Object>();
						body = JSON.parseObject(JSON.toJSONString(doctor), HashMap.class);
						Hospital hospital = hospitalService.getHospitalById(doctor.getHospitalId());
						if(StringUtils.equals(hospital.getInvitationCode(), Global.ANHESHOU_SUPPLIER)){	//判断是否为供应商
							body.put(Global.ANHESHOU_SUPPLIER, 1);
						}
						if(hospital.getInvitationCode()!=null && hospital.getInvitationCode().equals(Global.ANHESHOU_CLINIC_USER))
							body.put("changeImg", 1);
						else
							body.put("changeImg", 0);
						hisResponse.setBody(body);
						//记录登录成功的日志

						systemLog.setOperationResult(OperResultConst.SUCCESS);
						systemLogService.selpHeadSystemLog(systemLog);
						//登录成功，返回
//						hospitalConfigService.loginInitConfigToSession();	//加载诊所配置信息
						return hisResponse.toString();
					}else if(doctor.getStatus() ==0L){
						systemLog.setDescription("用户："+doctorName+"不能登录,用户状态：未审核 ");
					}else if(doctor.getStatus() ==2L){
						systemLog.setDescription("用户："+doctorName+"不能登录,用户状态：账号锁定 ");
					}else if(doctor.getStatus() ==3L){
						systemLog.setDescription("用户："+doctorName+"不能登录,用户状态：手机未激活 ");
					}else if(doctor.getStatus() ==4L){
						systemLog.setDescription("用户："+doctorName+"不能登录,用户状态：已删除 ");
					}
					systemLog.setOperationResult(OperResultConst.FAILED);
					systemLogService.selpHeadSystemLog(systemLog);
					//登录成功,但状态不正确，设置用户对象处，返回
					session.setAttribute("doctorId", doctor.getDoctorId());
					doctor.setPassword(null);
					session.setAttribute("currentUser",doctor);
					hisResponse.setBody(doctor);
					return hisResponse.toString();
				}else{
					systemLog.setDescription("用户："+doctorName+",密码错误");
				}
				systemLog.setDoctorId(doctor.getDoctorId());
				systemLog.setHospitalId(doctor.getHospitalId());
				systemLog.setHospitalName(doctor.getHospitalName());
            }else{
				systemLog.setDescription("用户："+doctorName+",不存在");
			}
			errorTime+=1;
			session.setAttribute("errorTime", errorTime);
			if(errorTime >= 3)
				hisResponse.setErrorCode(5003);
			else
				hisResponse.setErrorCode(4011);
			systemLog.setOperationResult(OperResultConst.FAILED);
			hisResponse.setErrorMessage("用户名或密码错误!");
			systemLogService.selpHeadSystemLog(systemLog);
        }catch (Exception e) {
        	e.printStackTrace();
			hisResponse.setErrorCode(4011);
			hisResponse.setErrorMessage("系统异常，请联系管理员!");
		}
        return hisResponse.toString();
    }

	/**
	 *  功能描述：配置 普通用户的 权限
	 * @param
	 * @return
	 * @throws
	 * @author:  tangww
	 * @createDate   2016/4/14
	 *
	 */
	private void processUserPermissions(Doctor doctor) {
		//目前 只控制一级菜单

		Map<String, Object> functionMap = new HashMap<String, Object>();
		List<Long> longList = new ArrayList<Long>();
		List<Function> firstFun = doctorService.getDoctorFunction(doctor.getDoctorId());
		if(firstFun!=null && firstFun.size()>0){
            longList = firstFun.stream().map(Function::getFunctionId).collect(Collectors.toList());
        }
		functionMap.put("first", longList);
		doctor.setFunction(functionMap);
	}





	/**
     * <p>Description:查看用户登录状态<p>
     * @author ZhouPengyu
     * @date 2016-3-3 17:18:05
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = {"/loginStatus"}, produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String loginStatus(HttpServletRequest request, HttpServletResponse response){
    	HisResponse hisResponse = new HisResponse();
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			Boolean bool = false;
			Doctor doctor = new Doctor();
			String cookieValue = CookieUtils.getCookieValue((HttpServletRequest) request, SessionUtils.COOKIE_NAME);
			if(StringUtils.isNotBlank(cookieValue)){
				cookieValue = HmDesUtils.decrypt(cookieValue);
				HttpSession session = request.getSession();
				if(session.getAttribute("doctorId")==null){
					Long doctorId = Long.parseLong(cookieValue.split("-")[0]);
					doctor = doctorService.getDoctorById(doctorId);
					if(doctor!=null){
						session.setAttribute("isAdmin", doctor.getIsAdmin());
						session.setAttribute("doctorId", doctorId);
						session.setAttribute("currentUser",doctor);
						session.setAttribute("hospitalId", Long.parseLong(cookieValue.split("-")[1]));
						bool = true;
					}
				}else{
					doctor = doctorService.getDoctorById((Long) session.getAttribute("doctorId"));
					bool = true;
				}
			}
            if(bool){
            	map = JSON.parseObject(JSON.toJSONString(doctor), HashMap.class);
            	Hospital hospital = hospitalService.getHospitalById(doctor.getHospitalId());
            	if(StringUtils.equals(hospital.getInvitationCode(), Global.ANHESHOU_SUPPLIER)){	//判断是否为供应商
            		map.put(Global.ANHESHOU_SUPPLIER, 1);
				}
				if(hospital.getInvitationCode()!=null && hospital.getInvitationCode().equals(Global.ANHESHOU_CLINIC_USER))
					map.put("changeImg", 1);
				else
					map.put("changeImg", 0);
				map.put("login", "true");
			}else{
				map.put("login", "false");
				addFaileLogFor401(request,"用户状态检测：用户未登录，需要重新登录");
			}
//            hospitalConfigService.loginInitConfigToSession();	//加载诊所配置信息
            hisResponse.setBody(map);
    	}catch (Exception e) {
    		e.printStackTrace();
			//若发生异常将用户设置 为未登录，
			map.put("login", "false");
			hisResponse.setBody(map);
		}
        return hisResponse.toString();
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

		systemLogService.selpHeadSystemLog(faileLog);
	}
    
    /**
     * <p>Description:用户登出<p>
     * @author ZhouPengyu
     * @date 2016-3-3 17:18:05
     */
    @RequestMapping(value = {"/logout"}, produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String logout(HttpServletRequest request, HttpServletResponse response){
    	HisResponse hisResponse = HisResponse.getInstance();
    	HttpSession session = request.getSession();
		SystemLog systemLog = new SystemLog();
		systemLog.setFunctionId(FunctionConst.LOGOFF);
		systemLog.setOperationId(Operation.LOGOFF_VALUE);
		Doctor doctor = SessionUtils.getCurrentUser();
		if(doctor!=null) {
			systemLog.setDoctorId(doctor.getDoctorId());
			systemLog.setUserName(doctor.getDoctorName());
			systemLog.setHospitalId(doctor.getHospitalId());
			systemLog.setHospitalName(doctor.getHospitalName());
		}
		systemLog.setParams(request.getHeader(SessionUtils.USER_AGENT));
		systemLog.setOperationResult(OperResultConst.SUCCESS);
		systemLog.setLoginIp(IPAndPathUtils.getRealIpAddres(request));
		systemLogService.selpHeadSystemLog(systemLog);
		session.invalidate();//注销用户的session
		CookieUtils.cleanCookie(response, SessionUtils.COOKIE_NAME);
		Map<String,String> map = Maps.newHashMap();
		Hospital hospital = hospitalService.getHospitalById(doctor.getHospitalId());
		if(hospital.getInvitationCode()!=null && hospital.getInvitationCode().equals(Global.ANHESHOU_CLINIC_USER))
			map.put("from", "anheshou");
		hisResponse.setBody(map);

    	return hisResponse.toString();
    }


	@Autowired(required = false)
	LoginFacade loginFacade;


	/**
	 * <p>Description:获得诊所端的功能框架<p>
	 * @author Tangwenwu
	 * @date 2016-3-3 17:18:05
	 */
	@RequestMapping(value = {"/getSysFrame"}, produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String getSysFrame(HttpServletRequest request, HttpServletResponse response){

		try {
			//1. 处理用户的相关信息 根据用户信息 替换头部的logo 和 诊所名称
			Doctor doctor = SessionUtils.getCurrentUser();
			Hospital hospital = hospitalService.getHospitalById(doctor.getHospitalId());
			//1.1 如果是供应商，获得供应商端的功能框架
			boolean isAnHeShouSupplier  = StringUtils.isNotBlank(hospital.getInvitationCode()) && StringUtils.equals(hospital.getInvitationCode(), Global.ANHESHOU_SUPPLIER);
			if(isAnHeShouSupplier){
				//1.2 读取系统功能的模板框架
				SystemFrame supplierFrame = loginFacade.getSupplierSystemFrame(doctor);
				return JSON.toJSONString(supplierFrame);
			}else{
				SystemFrame tempSysFrame = loginFacade.getHisSystemFrame(doctor,hospital);
				return JSON.toJSONString(tempSysFrame);
			}
			//2. 读取系统功能的模板框架
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


    
    /**
     * <p>Description:手发送短信验证码——手机激活<p>
     * @author ZhouPengyu
     * @date 2016-3-3 17:18:05
     */
    @RequestMapping(value = "/sendSmsCaptcha", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String sendSmsCaptcha(@RequestBody Map<String, String> requestParams, HttpServletRequest request){
		HisResponse hisResponse = new HisResponse();
		String phoneNo = requestParams.get("phoneNo");
		String userName = requestParams.get("userName");
		String objective = requestParams.get("objective");
		
		if(StringUtils.isEmpty(phoneNo)){
			hisResponse.setErrorCode(5005);
			hisResponse.setErrorMessage("手机号错误");
			return hisResponse.toString();
		}
		Map<String, Object> body = new HashMap<String, Object>();
		String sessionId = SessionUtils.getSession().getId();
		String smsCaptcha = "";
		if(JedisHelper.get(Object.class, sessionId) != null){
			smsCaptcha = JedisHelper.get(String.class, sessionId).split("-")[0];
		}else{
			smsCaptcha = SmsCaptchaUtil.smsCaptchaUtil();
			JedisHelper.set(sessionId, 1*60*30, smsCaptcha+"-"+phoneNo);
//			JedisHelper.set(smsCaptcha, 1*60*30, phoneNo);
		}
		if(StringUtils.isNotEmpty(objective)){
			if(objective.equals("activate")){
				if(SessionUtils.getDoctorId()!=null){
					Integer result = SmsCaptchaUtil.sendSms(phoneNo, "【惠每云诊所】校验码："+smsCaptcha+"，此验证码用于手机号验证，30分钟内有效。");
					log.warn(phoneNo+"----操作:"+objective+"----系统验证码:"+smsCaptcha+"----缓存验证码:"+JedisHelper.get(String.class, sessionId)+"----状态:"+result+"----IP:"+request.getRemoteAddr());
//					Integer result = 1;
					if(result==1)
						body.put("result", 1);
					else
						body.put("result", 0);
				}else
					body.put("result", 2);
				
			}else if(objective.equals("forget")){
				Doctor doctor = new Doctor();
				doctor.setDoctorName(userName);
				doctor.setPhone(phoneNo);
				List<Doctor> doctorList = doctorService.searchDoctor(doctor);
				
				if(StringUtils.isNotEmpty(userName) && StringUtils.isNotEmpty(phoneNo) && doctorList!=null && doctorList.size()>0){
					Integer result = SmsCaptchaUtil.sendSms(phoneNo, "【惠每云诊所】校验码："+smsCaptcha+"，此验证码用于重置密码，30分钟内有效。");
					log.warn(phoneNo+"----操作:"+objective+"----系统验证码:"+smsCaptcha+"----缓存验证码:"+JedisHelper.get(String.class, sessionId)+"----状态:"+result+"----IP:"+request.getRemoteAddr());
//					Integer result = 1;
					if(result==1)
						body.put("result", 1);
					else
						body.put("result", 0);
				}else{
					body.put("result", 2);
				}
			}else if(objective.equals("register")){
				Integer result = SmsCaptchaUtil.sendSms(phoneNo, "【惠每云诊所】校验码："+smsCaptcha+"，此验证码用于账号注册，30分钟内有效。");
				log.warn(phoneNo+"----操作:"+objective+"----系统验证码:"+smsCaptcha+"----缓存验证码:"+JedisHelper.get(String.class, sessionId)+"----状态:"+result+"----IP:"+request.getRemoteAddr());
				if(result==1)
					body.put("result", 1);
				else
					body.put("result", 0);
			}
		}
		
		hisResponse.setBody(body);
		return hisResponse.toString();
	};
	
	@RequestMapping(value = "/imgCaptcha", method = RequestMethod.GET)
	public void imgCaptcha(HttpServletRequest request, HttpServletResponse response){
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		
		HttpSession session = SessionUtils.getSession();
		try {
			ValidateCodeUtils vCode = new ValidateCodeUtils(120,40,5,100);
			session.setAttribute("imgCaptcha", vCode.getCode());
			vCode.write(response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
