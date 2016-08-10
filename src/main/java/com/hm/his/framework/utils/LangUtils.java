package com.hm.his.framework.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by wangjialin on 15/12/20.
 */
public class LangUtils {

    public static void main(String[] args) {
        Integer specUnitaryRatio = LangUtils.getInteger("9盒");
        if(specUnitaryRatio !=null && specUnitaryRatio >0){
            System.out.println(specUnitaryRatio);
        }else{
            System.out.println("换算比不是正整数,");
        }
    }
    public static String getString(Object obj){
        if (obj == null) return null;
        return obj.toString();
    }

    //TODO 此处有bug, 现在无法判断负数
    public static Long getLong(Object obj) {
        try {
            if (obj == null) return null;
            String objStr = LangUtils.getString(obj);
            if (StringUtils.isNotEmpty(objStr) && StringUtils.isNumeric(objStr)) {
                return Long.valueOf(objStr);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //TODO 此处有bug, 现在无法判断负数
    public static Integer getInteger(Object obj) {
        try {
            if (obj == null) return null;
            String objStr = LangUtils.getString(obj);
            if (StringUtils.isNotEmpty(objStr)) {
                return Integer.valueOf(objStr);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String getString(Object obj, String defaultVal) {
        String result = LangUtils.getString(obj);
        if (result == null) {
            result = defaultVal;
        }
        return result;
    }

    public static Double getDouble(Object obj) {
        try {
            if (obj == null) return null;
            String objStr = LangUtils.getString(obj);
            if (StringUtils.isNotEmpty(objStr)) {
                return Double.valueOf(objStr);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static boolean isChineseChar(String str){
        boolean temp = false;
        Pattern p=Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m=p.matcher(str);
        if(m.find()){
            temp =  true;
        }
        return temp;
    }
}
