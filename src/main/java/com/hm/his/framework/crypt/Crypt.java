package com.hm.his.framework.crypt;

/**
 * 
 * Description: 字符串加解密接口
 * author：tangww
 * @version 1.0
 */
public interface Crypt {

	/**
	 * 
	 * Definition: 加密字符串
	 * Created date: Jun 15, 2011
	 * @param str
	 * @param key
	 * @return
	 */
	public String encode(String str, String key);
	
	/**
	 * 
	 * Definition: 解密字符串
	 * Created date: Jun 15, 2011
	 * @param str
	 * @param key
	 * @return
	 */
	public String decode(String str, String key);
}
