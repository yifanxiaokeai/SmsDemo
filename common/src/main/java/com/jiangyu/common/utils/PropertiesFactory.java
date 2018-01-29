package com.jiangyu.common.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * 获取属性文件工厂类
 */
public class PropertiesFactory {
	private static final String TAG = "PropertiesFactory";
	private static Properties props;

	public static Properties getInstance() {
		if (props == null) {
			props = new Properties();
		}

		return props;
	}

	/**
	 * 通过key获取properties的value
	 * @param filePath
	 * @param key
	 * @return
	 */
	public static String readValue(String filePath, String key) {

		try {
			if (props.size() == 0) {
				InputStream in = new FileInputStream(filePath);
				props.load(in);
			}
			String value = props.getProperty(key);
			Log.d(key + value);
			return value;
		} catch (Exception e) {
			Log.e(TAG, e);
			return null;
		}
	}
}
