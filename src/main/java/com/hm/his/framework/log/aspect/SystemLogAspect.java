package com.hm.his.framework.log.aspect;

import java.lang.reflect.Method;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.hm.his.framework.utils.IPAndPathUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.hm.his.framework.log.annotation.HmLogHelper;
import com.hm.his.framework.log.annotation.SystemLogAnno;
import com.hm.his.framework.log.constant.OperResultConst;
import com.hm.his.framework.model.HisResponse;
import com.hm.his.framework.utils.BeanUtils;
import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.module.log.model.SystemLog;
import com.hm.his.module.log.service.SystemLogService;
import com.hm.his.module.user.model.Doctor;

/**
 * 切点类
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/4/1
 * Time: 17:47
 * http://blog.csdn.net/czmchen/article/details/42392985
 * CopyRight:HuiMei Engine
 */

@Aspect
@Component
public class SystemLogAspect {

    //注入Service用于把日志保存数据库
    @Resource
    private SystemLogService logService;
    //本地异常日志记录对象
    private  static  final Logger logger = Logger.getLogger(SystemLogAspect.class);

    //Service层切点
    @Pointcut("@annotation(com.hm.his.framework.log.annotation.SystemLogAnno)")
    public  void serviceAspect() {
    }

    @AfterReturning(pointcut = "serviceAspect()",returning="returnVal")
    public void doAfter(JoinPoint joinPoint ,Object returnVal){
        HttpServletRequest request = SessionUtils.getRequest();
        HttpSession session = SessionUtils.getSession();
        //读取session中的用户
        Doctor doctor = SessionUtils.getCurrentUser();
        if(doctor!=null) {
            doctor.setDoctorId(doctor.getDoctorId());
            doctor.setDoctorName(doctor.getDoctorName());
            doctor.setHospitalId(doctor.getHospitalId());
            doctor.setHospitalName(doctor.getHospitalName());
            //获取请求ip
            String userLoginIP = IPAndPathUtils.getRealIpAddres(request);
            //获取用户请求方法的参数并序列化为JSON格式字符串
            String params = "";
            if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
                for (int i = 0; i < joinPoint.getArgs().length; i++) {
                    params += JSON.toJSONString(joinPoint.getArgs()[i]) + ";";
                }
            }
            try {
                //*========控制台输出=========*/
//            System.out.println("调用方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
//            System.out.println("请求人:" + doctor.getDoctorName());
//            System.out.println("请求IP:" + ip);
//            System.out.println("请求参数:" + params);
                //*==========数据库日志=========*//*
                SystemLog log = new SystemLog();
                log.setDoctorId(doctor.getDoctorId());
                log.setUserName(doctor.getDoctorName());
                log.setHospitalId(doctor.getHospitalId());
                log.setHospitalName(doctor.getHospitalName());
                log.setLoginIp(userLoginIP);
                if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
                    BeanUtils.LogOption logOption = BeanUtils.returnLogOption(joinPoint.getArgs()[0], HmLogHelper.class);
                    log.setOperObjId(logOption.getIdV());
                    if(logOption.getNameValue() !=null){
                        log.setOperObjName(logOption.getNameValue().toString());
                    }
                }

                getServiceMthodDescription(joinPoint, log);

                if (returnVal != null) {
                    if (returnVal instanceof Boolean) {
                        boolean f = (Boolean) returnVal;
                        if (f) {
                            log.setOperationResult(OperResultConst.SUCCESS);
                        } else {
                            log.setOperationResult(OperResultConst.FAILED);
                        }
                    } else if (returnVal instanceof Integer) {
                        int f = (Integer) returnVal;
                        if (f > 0) {
                            log.setOperationResult(OperResultConst.SUCCESS);
                        } else {
                            log.setOperationResult(OperResultConst.FAILED);
                        }
                    } else if (returnVal instanceof Long) {
                        long f = (Long) returnVal;
                        if (f > 0) {
                            log.setOperationResult(OperResultConst.SUCCESS);
                        } else {
                            log.setOperationResult(OperResultConst.FAILED);
                        }
                    } else if (returnVal instanceof HisResponse) {
                        HisResponse hisResponse = (HisResponse) returnVal;
                        if (hisResponse.getHasError())
                            log.setOperationResult(OperResultConst.FAILED);
                        else
                            log.setOperationResult(OperResultConst.SUCCESS);
                    }
                } else {
                    log.setOperationResult(OperResultConst.SUCCESS);
                }

                log.setParams(params);
                log.setCreater(doctor.getDoctorId());
                log.setCreateDate(new Date());
                //保存数据库
                logService.saveSystemLog(log);
            } catch (Exception ex) {
                //记录本地异常日志
                logger.error("==异常通知异常==");
                ex.printStackTrace();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    /**
     * 异常通知 用于拦截service层记录异常日志
     *
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "serviceAspect()", throwing = "e")
    public  void doAfterThrowing(JoinPoint joinPoint, Throwable e) {

        HttpServletRequest request = SessionUtils.getRequest();

        //读取session中的用户
        Doctor doctor = SessionUtils.getCurrentUser();
        if(doctor!=null){
            doctor.setDoctorId(doctor.getDoctorId());
            doctor.setDoctorName(doctor.getDoctorName());
            doctor.setHospitalId(doctor.getHospitalId());
            doctor.setHospitalName(doctor.getHospitalName());
            //获取请求ip
            String loginIP = IPAndPathUtils.getRealIpAddres(request);
            //获取用户请求方法的参数并序列化为JSON格式字符串
            String params = "";
            if (joinPoint.getArgs() !=  null && joinPoint.getArgs().length > 0) {
                for ( int i = 0; i < joinPoint.getArgs().length; i++) {
                    params += JSON.toJSONString(joinPoint.getArgs()[i]) + ";";
                }
            }
            try {
                //*========控制台输出=========*/

                logger.error("调用方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
                logger.error("异常信息:" + e.getMessage());
                logger.error("异常详情:" + e.fillInStackTrace());
                logger.error("异常方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
                logger.error("请求人:" + doctor.getDoctorName());
                logger.error("请求IP:" + loginIP);
                logger.error("请求参数:" + params);
                //*==========数据库日志=========*//*
                SystemLog log = new SystemLog();
                log.setDoctorId(doctor.getDoctorId());
                log.setUserName(doctor.getDoctorName());
                log.setHospitalId(doctor.getHospitalId());
                log.setHospitalName(doctor.getHospitalName());
                log.setLoginIp(loginIP);
                if (joinPoint.getArgs() !=  null && joinPoint.getArgs().length > 0) {
                    BeanUtils.LogOption  logOption = BeanUtils.returnLogOption(joinPoint.getArgs()[0], HmLogHelper.class);
                    log.setOperObjId(logOption.getIdV());
                    if(logOption.getNameValue() !=null){
                        log.setOperObjName(logOption.getNameValue().toString());
                    }
                }

                getServiceMthodDescription(joinPoint,log);
                log.setOperationResult(OperResultConst.EXCEPTION);
                log.setParams(params);
                log.setCreater(doctor.getDoctorId());
                log.setCreateDate(new Date());
                //保存数据库
                logService.saveSystemLog(log);
            }  catch (Exception ex) {
                //记录本地异常日志
                logger.error("==异常通知异常==");
                ex.printStackTrace();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于service层注解
     *
     * @param joinPoint 切点
     * @return 方法描述
     * @throws Exception
     */
    public  static void getServiceMthodDescription(JoinPoint joinPoint,SystemLog log)
            throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();


        String description = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    log.setOperationId(method.getAnnotation(SystemLogAnno. class).operationId());
                    log.setFunctionId(method.getAnnotation(SystemLogAnno. class).functionId());

                    break;
                }
            }
        }

    }




    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param joinPoint 切点
     * @return 方法描述
     * @throws Exception
     */
    /*public  static String getControllerMethodDescription(JoinPoint joinPoint)  throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    description = method.getAnnotation(SystemControllerLog. class).description();
                    break;
                }
            }
        }
        return description;
    }*/
}
