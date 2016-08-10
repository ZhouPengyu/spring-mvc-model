package com.hm.his.framework.crypt;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

/**
 * Description: BASE64字符串加解密
 * author：tangww
 * @version 1.0
 */
public class BASE64Crypt implements Crypt {

	public String decode(String str, String key) {
		if (str == null || "".equals(str)) {
			return null;
		}
		try {
			return new String(new BASE64Decoder().decodeBuffer(str));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String encode(String str, String key) {
		if (str == null || "".equals(str)) {
			return null;
		}
		return (new BASE64Encoder()).encode(str.getBytes());
	}
	

}
