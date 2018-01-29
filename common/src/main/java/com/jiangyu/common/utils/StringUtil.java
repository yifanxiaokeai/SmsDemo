package com.jiangyu.common.utils;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Title: 字符串工具类 </p>
 * <p> Description:</p>
 * <p>1.拼装字符串数组</p>
 * <p>2.判断字符串是否为空p</p>
 * <p>3.创建随机字符串</p>
 * <p>4.比较字符串是否相同</p>
 * <p>5.TextUtils常见功能</p>
 * <p>6.由全角转半角</p>
 * <p>7.将所有的数字、字母及标点全部转为全角字符，使它们与汉字同占两个字节</p>
 * <p>8.char转拼音</p>
 * <p>9.String转拼音</p>
 * <p>11汉字 转为 汉语拼音首字母，英文字符不变</p>
 * <p>12.是否为拼音字符串</p>
 * <p>13.是否包含中文</p>
 * <p>14.得到格式如：FuJian的拼音</p>
 * <p>15.获取汉字首字母大写的拼音</p>
 * <p>16.字节数组转化成16进制字符串</p>
 * <p>17.16进制字符串转化成字节数组</p>
 * <p>18.将字符串编码成16进制数字,适用于所有字符（包括中文）</p>
 * <p>19.转换十六进制编码为字符串</p>
 * <p>20.拆分字符串 </p>
 *
 */
public class StringUtil {
	/**
	 * 随机种子
	 */
	private static final String RANDOMSEED = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	public static String join(Collection<String> s, String delimiter) {
		if (s.size() == 0)
			return "";
		StringBuilder sb = new StringBuilder();
		for (String str : s) {
			sb.append(str).append(delimiter);
		}
		if (sb.length() > 0)
			sb.delete(sb.length() - 1, sb.length());
		return sb.toString();
	}

	/**
	 * 判断字符串是否为空
	 */
	public static boolean isEmpty(String s) {
		return TextUtils.isEmpty(s);
	}

	/**
	 * 创建随机字符串
	 * 
	 * @param length
	 *            随机字符串长度
	 * @return
	 */
	public static String createRandomString(int length) {
		StringBuffer result = new StringBuffer();
		if (length > 0) {
			Random random = new Random();
			int seedLength = RANDOMSEED.length();
			for (int i = 0; i < length; i++) {
				result.append(RANDOMSEED.charAt(random.nextInt(seedLength)));
			}
		}
		return result.toString();
	}

 

	/**
	 * 比较字符串是否相同
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean equals(CharSequence a, CharSequence b) {
		return TextUtils.equals(a, b);
	}

	/**
	 * Returns whether the given CharSequence contains only digits
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isDigitsOnly(CharSequence str) {
		return TextUtils.isDigitsOnly(str);
	}

	/**
	 * html encode
	 * 
	 * @param s
	 * @return
	 */
	public static String htmlEncode(String s) {
		return TextUtils.htmlEncode(s);
	}

	/**
	 * 将所有的数字、字母及标点全部转为全角字符，使它们与汉字同占两个字节
	 * 
	 * @param input
	 * @return
	 */
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	/**
	 * 是否为拼音字符串
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isPinYin(String str) {
		Pattern pattern = Pattern.compile("[a-zA-Z]*");
		return pattern.matcher(str).matches();
	}

	/**
	 * 是否包含中文
	 * 
	 * @param str
	 * @return
	 */
	public static boolean containCn(String str) {
		Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]");
		return pattern.matcher(str).find();
	}

	/*
	 * Convert byte[] to hex
	 * string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
	 * 
	 * @param src byte[] data
	 * 
	 * @return hex string
	 */
	public static String bytesToHex(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * Convert hex string to byte[]
	 * 
	 * @param hexString
	 *            the hex string
	 * @return byte[]
	 */
	public static byte[] hexToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * Convert char to byte
	 * 
	 * @param c
	 *            char
	 * @return byte
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/*
	 * 16进制数字字符集
	 */
	private static String hexString = "0123456789ABCDEF";

	/*
	 * 将字符串编码成16进制数字,适用于所有字符（包括中文）
	 */
	public static String toHexString(String str) {
		// 根据默认编码获取字节数组
		byte[] bytes = null;
		try {
			bytes = str.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (bytes == null)
			return null;
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		// 将字节数组中每个字节拆解成2位16进制整数
		for (int i = 0; i < bytes.length; i++) {
			sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
			sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
		}
		return sb.toString();
	}

	// 转换十六进制编码为字符串
	public static String hexToString(String s) {
		if ("0x".equals(s.substring(0, 2))) {
			s = s.substring(2);
		}
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			s = new String(baKeyword, "utf-8");// UTF-16le:Not
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}
	/** 
	 * 拆分字符串 
	 */
	public static String[] splitString(String string, int len) {
		int x = string.length() / len;
		int y = string.length() % len;
		int z = 0;
		if (y != 0) {
			z = 1;
		}
		String[] strings = new String[x + z];
		String str = "";
		for (int i = 0; i < x + z; i++) {
			if (i == x + z - 1 && y != 0) {
				str = string.substring(i * len, i * len + y);
			} else {
				str = string.substring(i * len, i * len + len);
			}
			strings[i] = str;
		}
		return strings;
	}
	
	public static boolean isPwdValid(String pwd) {
		Pattern p = Pattern.compile("[a-zA-Z0-9]{0,30}");
		Matcher m = p.matcher(pwd);

		return m.matches();
	}

}
