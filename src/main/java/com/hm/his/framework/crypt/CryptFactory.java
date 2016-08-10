package com.hm.his.framework.crypt;

/**
 * 
 * Description: 加解密工具工厂
 * author：tangww
 * @version 1.0
 */
public class CryptFactory {
	
	/**
	 * BASE64编码
	 */
	public static final String BASE64 = "BASE64";
	
	/**
	 * 3DES with BASE64
	 * 与总线的加解密方式
	 */
	public static final String THREE_DES_BASE64 = "3desbase64";
	/**
	 * AES加解密方式
	 */
	public static final String AES = "AES";

	/**
	 * 
	 * Definition: 根据类型创建字符串加解密工具
	 * @param cryptType
	 * @return
	 */
	public static Crypt createCryptUtils (String cryptType) {
		if (BASE64.equals(cryptType)) {
			return new BASE64Crypt();
		} else if(AES.equals(cryptType)){
			return new AES();
		} else {
			return null;
		}
	}
	

    public static String KEY_PWD_PBEWithMD5AndDES = "KEY_PWD_PBEWithMD5AndDES"; //基于口令保护 实现加解密
    public static String KEY_RANDOM_PBEWithMD5AndDES = "KEY_RANDOM_PBEWithMD5AndDES"; //基于随机口令保护 实现加解密


    /**
     * 私有，防止类实例化
     */
    private CryptFactory() {
    }

	public static void main(String[] args) {
		long a=System.currentTimeMillis();
		String str ="CON-20019580";//8D97FFDA1D408DD2718F0DD54F775240
		//8D97FFDA1D408DD2718F0DD54F775240
		String encodeStr= CryptFactory.createCryptUtils(CryptFactory.AES).encode(str, "hmlipeng");
		System.out.println("encodeStr==========="+encodeStr);

		String decodeStr= CryptFactory.createCryptUtils(CryptFactory.AES).decode(encodeStr, "hmlipeng");
		System.out.println("decodeStr==========="+decodeStr);
		System.out.println("执行耗时:"+(System.currentTimeMillis()-a)/1000f+" 秒 ");
	}


}
