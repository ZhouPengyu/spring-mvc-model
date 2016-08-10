package com.hm.his.framework.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/4/29
 * Time: 14:44
 * CopyRight:HuiMei Engine
 */
public class RegexUtil {

    public static boolean isNumbersOrLetters(String str) {
        Pattern ptn = Pattern.compile("^[A-Za-z0-9]+$");
        Matcher mc =ptn.matcher(str);
        return mc.find();
    }

    public static boolean isContainChinese(String str) {
        Pattern ptn = Pattern.compile("^.*[\u4e00-\u9fa5].*$");
        Matcher mc =ptn.matcher(str);
        return mc.find();
    }

    public static void main(String[] args) {
//        String s1="5641236546874651235456";
//        System.out.println(isNumbersOrLetters(s1));
//
//        String s2="5641sdf2365468AEFDETEF74651235456";
//        System.out.println(isNumbersOrLetters(s2));
//
//        String s3="5641sdf23654中国DETEF74651235456";
//        System.out.println(isNumbersOrLetters(s3));
//        String s4=" ";
//        System.out.println(isNumbersOrLetters(s4));

        String s5="5641ASDAD1235456";
        System.out.println(isContainChinese(s5));

        String s6="中651235456";
        System.out.println(isContainChinese(s6));

        String s7="564123中";
        System.out.println(isContainChinese(s7));

        String s8="564123ASD_-65-1235456";
        System.out.println(isContainChinese(s8));

        String s9="5641湯ASD_-65-1235456";
        System.out.println(isContainChinese(s9));
    }
}
