//package com.hm.his.outpatient;
//
//import java.util.concurrent.TimeUnit;
//
//import com.hm.cache.Cache;
//import com.hm.cache.manager.HmCacheManager;
//
//public class gaga {
//	private static final String cacheName ="GeneralDiseaseMappingService";
//    public static void main(String[] args) throws InterruptedException {
//        Cache cache = HmCacheManager.ManagerDefault.get().getCache(cacheName);
//
//        cache.put("xxx","xxx",1, TimeUnit.SECONDS);
//        cache.put("xxx1","xxx");
//
//        cache.evict("xxx1");
//
//        Thread.sleep(2000);
//        String obj = cache.get("xxx");
//        System.out.print(obj==null);
//    }
//}
