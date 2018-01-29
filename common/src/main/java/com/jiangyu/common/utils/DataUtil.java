package com.jiangyu.common.utils;

import android.content.Context;
import android.text.TextUtils;

/**
 * <p>Title: 数据处理工具类 </p>
 * <p> Description:</p>
 * <p>1.转换dip为px</p>
 * <p>2.转换px为dip</p>
 * <p>3.将px转sp</p>
 * <p>4.将sp转px</p>
 * <p>5.浮点数转换整数</p>
 * <p>6.双字节数转换整数</p>
 * <p>7.转换字符串为布尔值</p>
 * <p>8.字符串转换为整数</p>
 * <p>9.转换为long</p>
 * <p>11.转换为double</p>
 * <p>12.转换为float</p>
 * <p>13.ASCII码转BCD码 </p>
 * <p>14.BCD转字符串</p>
 * <p>15.拆分字节数组  </p>
 *
 */
public class DataUtil {
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	/**
	 * 转换dip为px
	 * 
	 * @param context
	 * @param dip
	 *            值
	 * @return
	 */
	public static int convertDipToPx(Context context, double dip) {
		float scale = AppHelper.getScreenDensity(context);
		return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
	}

	/**
	 * 转换px为dip
	 * 
	 * @param context
	 * @param px
	 *            值
	 * @return
	 */
	public static int convertPxToDip(Context context, int px) {
		float scale = AppHelper.getScreenDensity(context);
		return (int) (px / scale + 0.5f * (px >= 0 ? 1 : -1));
	}

	/**
	 * 将PX转SP
	 * 
	 * @param context
	 * @param pxValue
	 *            px值
	 * @return
	 */
	public static int convertPxToSp(Context context, float pxValue) {
		float fontScale = AppHelper.getScaledDensity(context);
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将SP转PX
	 * 
	 * @param context
	 * @param spValue
	 *            sp值
	 * @return
	 */
	public static int convertSpToPx(Context context, float spValue) {
		float fontScale = AppHelper.getScaledDensity(context);
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * 浮点数转换整数
	 * 
	 * @param f
	 * @return
	 */
	public static int float2Int(float f) {
		return Math.round(f);
	}

	/**
	 * 双字节数转换整数
	 * 
	 * @param d
	 * @return
	 */
	public static int double2Int(double d) {
		return Long.valueOf(Math.round(d)).intValue();
	}
	/**
	 * 转换字符串为布尔值
	 * @param booleanStr 布尔字符串
	 * @return 布尔值
	 */
	public static boolean string2Boolean(String booleanStr){
		if (booleanStr == null){
			return false;
		}
		return booleanStr.equals(TRUE);
	}
	/**
	 * 转换布尔值为字符串
	 * @param bool 布尔值
	 * @return 布尔字符串
	 */
	public static String boolean2String (boolean bool){
		return bool ? TRUE : FALSE;
	}
	/**
	 * 判断是否是真
	 * @param booleanStr　真假字符串
	 * @return
	 */
	public static boolean isTrue(String booleanStr){
		if (TextUtils.isEmpty(booleanStr)){
			return false;
		}
		return DataUtil.TRUE.equals(booleanStr.trim());
	}

	/**
	 * 转换为int
	 * @param data 字符串
	 * @return
	 */
	public static int toInt(String data){
		int result = 0;
		try{
			result = Integer.valueOf(data);
		}catch(Exception ex){
			result = Integer.MIN_VALUE;
		}
		return result;
	}
	/**
	 * 字符串转换为整数
	 * @param data 字符串
	 * @param defaultValue 默认值
	 * @return
	 */
	public static int toInt(String data, int defaultValue){
		int result = 0;
		try{
			result = Integer.valueOf(data);
		}catch(Exception ex){
			result = defaultValue;
		}
		return result;
	}
	/**
	 * 转换为long
	 * @param data 字符串
	 * @return
	 */
	public static long toLong(String data){
		long result = 0;
		try{
			result = Long.valueOf(data);
		}catch(Exception ex){
			result = Long.MIN_VALUE;
		}
		return result;
	}
	/**
	 * 转换为float
	 * @param data 字符串
	 * @return
	 */
	public static float toFloat(String data){
		float result = 0;
		try{
			result = Float.valueOf(data);
		}catch(Exception ex){
			result = Float.MIN_VALUE;
		}
		return result;
	}
	/**
	 * 转换为double
	 * @param data 字符串
	 * @return
	 */
	public static double toDouble(String data){
		double result = 0;
		try{
			result = Double.valueOf(data);
		}catch(Exception ex){
			result = Double.MIN_VALUE;
		}
		return result;
	}
	/** 
	 * ASCII码转BCD码 
	 *  
	 */
	public static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {
		byte[] bcd = new byte[asc_len / 2];
		int j = 0;
		for (int i = 0; i < (asc_len + 1) / 2; i++) {
			bcd[i] = asc_to_bcd(ascii[j++]);
			bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));
		}
		return bcd;
	}

	public static byte asc_to_bcd(byte asc) {
		byte bcd;

		if ((asc >= '0') && (asc <= '9'))
			bcd = (byte) (asc - '0');
		else if ((asc >= 'A') && (asc <= 'F'))
			bcd = (byte) (asc - 'A' + 10);
		else if ((asc >= 'a') && (asc <= 'f'))
			bcd = (byte) (asc - 'a' + 10);
		else
			bcd = (byte) (asc - 48);
		return bcd;
	}

	/** 
	 * BCD转字符串 
	 */
	public static String bcd2Str(byte[] bytes) {
		char temp[] = new char[bytes.length * 2], val;

		for (int i = 0; i < bytes.length; i++) {
			val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
			temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

			val = (char) (bytes[i] & 0x0f);
			temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
		}
		return new String(temp);
	}

	/** 
	 *拆分数组  
	 */
	public static byte[][] splitArray(byte[] data, int len) {
		int x = data.length / len;
		int y = data.length % len;
		int z = 0;
		if (y != 0) {
			z = 1;
		}
		byte[][] arrays = new byte[x + z][];
		byte[] arr;
		for (int i = 0; i < x + z; i++) {
			arr = new byte[len];
			if (i == x + z - 1 && y != 0) {
				System.arraycopy(data, i * len, arr, 0, y);
			} else {
				System.arraycopy(data, i * len, arr, 0, len);
			}
			arrays[i] = arr;
		}
		return arrays;
	}
}
