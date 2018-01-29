package com.jiangyu.common.utils;

import android.app.Activity;

import com.jiangyu.common.R;


/**
 * <p>Title: Activity间跳转动画  </p>
 * <p>Description:                     </p>
 */
public class ActivityAnimation {

	/**
	 * 进入动画
	 * @param activity
	 */
	public static void PendingTransitionIn(Activity activity) {
		Activity parent = activity.getParent();
		if (parent == null) {
			parent = activity;
		}
		parent.overridePendingTransition(R.anim.push_left_in, R.anim.nothing);
	}

	/**
	 * 退出动画
	 * @param activity
	 */
	public static void PendingTransitionOut(Activity activity) {
		Activity parent = activity.getParent();
		if (parent == null) {
			parent = activity;
		}
		activity.overridePendingTransition(R.anim.nothing, R.anim.push_right_out);
	}

	/**
	 * 首页到几个特殊的activity跳转动画
	 * @param activity
	 */
	public static void HomePendingTransitionIn(Activity activity) {
		activity.overridePendingTransition(R.anim.bottom_push_up_in, R.anim.nothing);
	}

	/**
	 * 首页到几个特殊的activity跳转动画
	 * @param activity
	 */
	public static void HomePendingTransitionOut(Activity activity) {
		activity.overridePendingTransition(R.anim.nothing, R.anim.bottom_push_up_out);
	}
}
