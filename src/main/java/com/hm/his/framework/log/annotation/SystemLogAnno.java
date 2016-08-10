package com.hm.his.framework.log.annotation;

import com.hm.his.framework.log.constant.FunctionConst;
import com.hm.his.framework.log.constant.Operation;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/4/1
 * Time: 17:12
 * CopyRight:HuiMei Engine
 */
@Target({ElementType.PARAMETER,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemLogAnno {
    int functionId() default FunctionConst.LOG_UNKNOWN;
    int operationId() default Operation.UNKNOWN_VALUE;

}
