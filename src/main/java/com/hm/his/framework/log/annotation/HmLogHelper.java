package com.hm.his.framework.log.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/4/5
 * Time: 15:57
 * CopyRight:HuiMei Engine
 */
@Retention(RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface HmLogHelper {
    long id() default 0L;
    String name() default "";
}
