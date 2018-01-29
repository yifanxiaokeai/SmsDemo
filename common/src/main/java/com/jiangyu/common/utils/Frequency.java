package com.jiangyu.common.utils;

/**
 * <p>Title:  频率控制器</p>
 * <p>Description: </p>
 */
public class Frequency {

	private long sectionStart;
	private long sectionEnd;
	private long interval; // 间隔时间 毫秒

	public Frequency(int interval) {
		sectionStart = System.currentTimeMillis();
		sectionEnd = System.currentTimeMillis();
		this.interval = interval;
	}

	public boolean toRun() {
		sectionEnd = System.currentTimeMillis();
		long result = sectionEnd - sectionStart;
		if (result >= interval) {
			sectionStart = System.currentTimeMillis();
			return true;
		}
		return false;
	}

}
