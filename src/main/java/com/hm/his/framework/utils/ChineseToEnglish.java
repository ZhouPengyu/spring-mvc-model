package com.hm.his.framework.utils;

import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

public class ChineseToEnglish {
	
	// 将汉字转换为全拼
	public static String getPingYin(String src) {

		return PinyinHelper.convertToPinyinString(src, "", PinyinFormat.WITHOUT_TONE);
	}

	// 返回中文的首字母
	public static String getPinYinHeadChar(String str) {

		return PinyinHelper.getShortPinyin(str);
	}

	// 将字符串转移为ASCII码
	public static String getCnASCII(String cnStr) {
		StringBuffer strBuf = new StringBuffer();
		byte[] bGBK = cnStr.getBytes();
		for (int i = 0; i < bGBK.length; i++) {
			strBuf.append(Integer.toHexString(bGBK[i] & 0xff));
		}
		return strBuf.toString();
	}

	public static void main(String[] args) {
		System.out.println(getPingYin("haha"));
		System.out.println(getPinYinHeadChar("haha"));
	}
}
