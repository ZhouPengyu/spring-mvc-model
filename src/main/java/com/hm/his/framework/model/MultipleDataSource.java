package com.hm.his.framework.model;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Created by wangjialin on 15/12/17.
 */
public class MultipleDataSource extends AbstractRoutingDataSource {
    private static final ThreadLocal<String> dataSourceKey = new InheritableThreadLocal<String>();
    /**
     * @Parament dataSource :数据源名称
     */
    public static void setDataSourceKey(String dataSource) {
        dataSourceKey.set(dataSource);
    }
    @Override
    protected Object determineCurrentLookupKey() {
        return dataSourceKey.get();
    }

    public static Object getCurDataSource(){
        return dataSourceKey.get();
    }
}
