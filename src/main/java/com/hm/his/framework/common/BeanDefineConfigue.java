package com.hm.his.framework.common;

import com.hm.his.module.login.facade.LoginFacade;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016-08-10
 * Time: 16:47
 * CopyRight:HuiMei Engine
 */
@Component("BeanDefineConfigue")
public class BeanDefineConfigue implements ApplicationListener<ContextRefreshedEvent>{

//    private static boolean isStarted = false;
    Logger logger = Logger.getLogger(BeanDefineConfigue.class);
    @Autowired
    LoginFacade loginFacade;

    /**
     *  当一个ApplicationContext被初始化或刷新触发
     *  applicationontext和使用MVC之后的webApplicationontext会两次调用下面的方法，所以需要处理一下，仅调用 一次；
     * @param event
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
//        if (!isStarted) {
//            isStarted = true;
//            this.task();
//        }

        if(event.getApplicationContext().getDisplayName().equals("Root WebApplicationContext")){
            logger.info("spring容器初始化完毕================================================");
            //需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。
            this.initSystemTask();
        }
    }



    private void initSystemTask(){
        //清空用户的 SystemFrame 缓存 --- frame.json 会经常变更，需要读新的模板。
        loginFacade.deleteAllSystemFrameCache();
    }

}


