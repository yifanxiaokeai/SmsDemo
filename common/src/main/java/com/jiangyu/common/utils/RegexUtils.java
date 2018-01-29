package com.jiangyu.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Title: 正则表示式工具类 </p>
 * <p> Description:</p>
 * <p>1.判断是否是字母</p>
 * <p>2.判断是否是数组</p>
 * <p>3.判断是否是字母数字下划线</p>
 * <p>4.是否为中文字符</p>
 * <p>5.是否有除中文以外的字符</p>
 * <p>6.是否表情符号</p>
 * <p>7.正则匹配手机号</p>
 *
 */
public class RegexUtils {
	/**
	 * 
	 * 匹配由数字、26个英文字母或者下划线组成的字符串
	 * 
	 */
	public static final String letter_number_underline_regexp = "^//w+$";
	/**
	 * 
	 * 匹配由数字和26个英文字母组成的字符串
	 * 
	 */
	public static final String letter_number_regexp = "^[A-Za-z0-9]+$";
	public static final String number_regexp = "^[0-9]*$";
	public static final String checknum_regexp = "[0-9]{4,6}";
	public static final String illegal = "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]";

	public static boolean isLetter_num(String resource) {
		return Pattern.matches(letter_number_regexp, resource);
	}

	public static boolean isLetter_num_underline(String resource) {
		return Pattern.matches(letter_number_underline_regexp, resource);
	}

	public static boolean isNum(String resource) {
		return Pattern.matches(number_regexp, resource);
	}

	public static String getCheckNum(String resource) {
		String result = "";
		Pattern p = Pattern.compile(checknum_regexp);
		Matcher m = p.matcher(resource);
		if (m.find()) {
			String g = m.group();
			result = g;
		}
		return result;
	}

	/**
	 * 是否为中文字符
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isChineseChar(char c) {
		boolean match = (String.valueOf(c)).matches("([\u4E00-\u9FA5])");
		return match;
	}

	/**
	 * 是否有除中文以外的字符
	 * 
	 * @param str
	 * @return 返回true，说明不全是中文，返回false，说明全是中文
	 */
	public static boolean judgeIsChineseString(String str) {
		char[] newchar = new char[str.length()];
		str.getChars(0, str.length(), newchar, 0);
		for (int i = 0; i < newchar.length; i++) {
			boolean matches = RegexUtils.isChineseChar(newchar[i]);
			if (!matches) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否表情符号
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmojiString(String str) {
		String regex = illegal;
		Pattern pattern = Pattern.compile(regex, Pattern.UNICODE_CASE
				| Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(str);
		return matcher.find();
	}
	/**
	 * 正则匹配手机号码
	 * 
	 * @param mobileNo
	 * @return
	 */
	public static boolean isMobileNoValid(String mobileNo) {
		String regExp = "^[1][3-8]{10}$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(mobileNo);
		return m.find();
	}
}
