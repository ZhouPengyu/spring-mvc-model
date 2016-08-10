package com.hm.his.framework.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by ant_shake_tree on 15/10/14.
 */
@Component
public class DynamicBeanManager implements
        ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
        
    }
    public static Object getBean(String name){
        return applicationContext.getBean(name);
    }
}