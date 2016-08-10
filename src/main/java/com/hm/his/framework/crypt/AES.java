package com.hm.his.framework.crypt;

import com.hm.his.framework.crypt.Crypt;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;


/**
 * AES加解密算法
 * author：tianye
 *
 * @version 1.1
 */
public class AES implements Crypt {
	private static String algorithm = "AES";// 算法名称
	/**
	 * 根据字符密钥生成密钥对象
	 *
	 * @param strKey
	 * @return
	 */
	private Key getKey(String strKey) {
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );  //追加防止跨操作系统的密钥对象不同问题
			secureRandom.setSeed(strKey.getBytes());
			keyGenerator.init(128,secureRandom);
			Key key = keyGenerator.generateKey();
			return key;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密
	 */
	public String decode(String str, String key) {
		try {
			str = new String(str.getBytes());
			byte[] b = parseHexStr2Byte(str);
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(cipher.DECRYPT_MODE, this.getKey(key));
			byte[] byteFina = cipher.doFinal(b);
			return new String(byteFina);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 加密
	 */
	public String encode(String str, String key) {
		try {
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(cipher.ENCRYPT_MODE, this.getKey(key));
			byte[] byteFina = cipher.doFinal(str.getBytes());
			// base64编码
			return parseByte2HexStr(byteFina);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
	//转换为2进制
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}
	//转换成16进制,防止解密时出现异常
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length()/2];
		for (int i = 0;i< hexStr.length()/2; i++) {
			int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
			int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}
}
