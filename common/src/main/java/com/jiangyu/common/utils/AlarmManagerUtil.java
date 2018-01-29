/**
 * 
 */
package com.jiangyu.common.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

/**
 * <p>Title: 闹钟定时器        </p>
 * <p>Description: 
 *  定时启动服务，并且开始获取推送信息
 * </p>
 */
public class AlarmManagerUtil {
	private static int DEFAULT_TIME = 1000 * 60 * 5; // 默认5分钟

	private static final String K_MAX_TIME = "k_max_time"; // 间隔时间key

	private static AlarmManager getAlarmManager(Context ctx) {
		return (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
	}

	/**
	 * 启动定时器
	 * @param ctx
	 * @param recevicer 当被触发事件时，广播的接收器
	 * 
	*/
	public static void startAlarmRepeat(Context ctx, Class<?> recevicer, Long frequency, String action) {
		Log.i("start alarm repeat...");
		
		if(frequency == null) {
			frequency = 2L;
		}

		AlarmManager alarm = getAlarmManager(ctx);

		Intent intent = new Intent();
		intent.setAction(action);
		intent.setClass(ctx, recevicer);
		PendingIntent pi = PendingIntent.getBroadcast(ctx, 0, intent, 0);

		// 开始时间
		long firstTime = SystemClock.elapsedRealtime();

		// 间隔时间
//		long frequency = getFrequency(ctx);

		alarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, frequency, pi); // 启动定时任务

		Log.i("start alarm repeat successfully...");
	}

	/**
	 * 取消定时器
	 * @param ctx
	 * @param recevicer 当被触发事件时，广播的接收器
	 */
	public static void stopAlarmRepeat(Context ctx, Class<?> recevicer) {
		Log.e("stop alarm repeat...");

		AlarmManager alarm = getAlarmManager(ctx);

		Intent intent = new Intent();
		intent.setClass(ctx, recevicer);
		PendingIntent pi = PendingIntent.getBroadcast(ctx, 0, intent, 0);

		alarm.cancel(pi);

		Log.e("stop alarm repeat successfully...");
	}

	public static long getFrequency(Context context) {
		String frequency = SharedPreferencesUtil.getValue(context, K_MAX_TIME);
		if (StringUtil.isEmpty(frequency) || !StringUtil.isDigitsOnly(frequency)) {
			return DEFAULT_TIME;
		}

		return Long.parseLong(frequency);
	}

	/**
	 * 设置定时间隔时间，需要在启动定时器之前设置
	 * @param maxTime  间隔时间值
	 */
	public static void setFrequency(Context context, Long frequency) {
		if(frequency == null) {
			SharedPreferencesUtil.setValue(context, K_MAX_TIME, String.valueOf(DEFAULT_TIME));
		}else {
			SharedPreferencesUtil.setValue(context, K_MAX_TIME, String.valueOf(frequency));
		}
	}
}
