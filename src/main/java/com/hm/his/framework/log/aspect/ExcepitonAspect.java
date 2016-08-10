package com.hm.his.framework.log.aspect;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.hm.his.framework.log.annotation.HmLogHelper;
import com.hm.his.framework.log.annotation.SystemLogAnno;
import com.hm.his.framework.log.constant.OperResultConst;
import com.hm.his.framework.model.HisResponse;
import com.hm.his.framework.utils.BeanUtils;
import com.hm.his.framework.utils.IPAndPathUtils;
import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.module.log.model.SystemLog;
import com.hm.his.module.log.service.SystemLogService;
import com.hm.his.module.user.model.Doctor;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

/**
 * 系统异常 切点类
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/4/1
 * Time: 17:47
 * http://blog.csdn.net/czmchen/article/details/42392985
 * CopyRight:HuiMei Engine
 */

@Aspect
@Component
public class ExcepitonAspect {


    //本地异常日志记录对象
    private  static  final Logger logger = Logger.getLogger(ExcepitonAspect.class);

    //Service层切点
//    @Pointcut("@annotation(com.hm.his.framework.log.annotation.SystemLogAnno)")
//    public  void serviceAspect() {
//    }

    @Pointcut("execution(* com.hm.his.module..*.service.impl..*.*(..))")
    public  void serviceAspect() {
    }


    /**
     * 异常通知 用于拦截service层记录异常日志
     *
     * @param joinPoint
//     * @param e
     */
    @AfterThrowing(pointcut = "serviceAspect()", throwing = "e")
    public  void doAfterThrowing(JoinPoint joinPoint, Exception e) {
//    @Around(value = "serviceAspect()")
//    public  void doAfterThrowing(JoinPoint joinPoint) {

        HttpServletRequest request = SessionUtils.getRequest();

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
            logger.error("=========================================");
            logger.error("请求IP:" + loginIP);
            logger.error("请求参数:" + params);
            logger.error("调用方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
            logger.error("异常信息:" + e);
            StackTraceElement[] stEles = e.getStackTrace();
            int stackEleSize = stEles.length > 20? 20:stEles.length;
            List<StackTraceElement> stackList = Lists.newArrayList();
            for(int i=0;i<stackEleSize;i++){
                stackList.add(stEles[i]);
            }
            StringBuffer stackSb = new StringBuffer();
            stackList.stream().forEach(stackTraceElement -> stackSb.append("\t").append(stackTraceElement).append("\r\n"));
            logger.error(" -------异常详情--------\n" + stackSb);

//            logger.error("异常方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));

            e.printStackTrace();


        }  catch (Exception ex) {
            //记录本地异常日志
            logger.error("==异常通知异常==");
            ex.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

}
