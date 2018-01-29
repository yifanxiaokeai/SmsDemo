package com.jiangyu.common.utils;

import java.io.Closeable;

/**
 * <p>Title: 数据流工具类 </p>
 * <p> Description:</p>
 * <p>1.关闭流</p>
 *
 */
public final class StreamUtil {
/**
 * 关闭流
 * @param c
 */
	public static void closeSilently(Closeable c) {
		if (c == null)
			return;
		try {
			c.close();
		} catch (Throwable t) {
		}
	}
}
