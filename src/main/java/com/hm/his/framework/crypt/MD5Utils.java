package com.hm.his.framework.crypt;

import java.security.MessageDigest;

public class MD5Utils {

    private final static String THE_AES_KEY = "HMkillHACKER";
	/**
	 * 
	 * <p>Description:MD5加密<p>
	 * <p>Company: H.M<p>
	 * @author ZhouPengyu
	 * @date 2016-1-20 下午2:24:25
	 */
	public final static String encrypt(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};       

        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public final static  String passwordSaltHash(String doctorName, String password) {
//        return password;
        //待微信上线后再 升级
        AES aes = new AES();
        String saltStr =aes.encode(doctorName,THE_AES_KEY);
        password = MD5Utils.encrypt(saltStr + password);

        return password;
    }

	
	public static void main(String[] args) {
        System.out.println(passwordSaltHash("gyp4",""));
//        System.out.println(MD5Utils.encrypt("589651"));
//        System.out.println(MD5Utils.encrypt("589652"));
//        System.out.println(MD5Utils.encrypt("589653"));
//        System.out.println(MD5Utils.encrypt("589654"));
//        System.out.println(MD5Utils.encrypt("589655"));
//        System.out.println(MD5Utils.encrypt("589656"));
//        System.out.println(MD5Utils.encrypt("589657"));
//		String passwd ="123456";
//        System.out.println("level 1="+encrypt(passwd));
//        System.out.println("level 2="+encrypt(encrypt(passwd)));
//        String level3 = "125"+passwd;
//        System.out.println("level 3="+encrypt(level3));
//        String level4 = "2E2B636804EDC1CF18FAD5D0B1852A0B"+encrypt(passwd);
//        System.out.println("level 4="+encrypt(level4));
	}
}
