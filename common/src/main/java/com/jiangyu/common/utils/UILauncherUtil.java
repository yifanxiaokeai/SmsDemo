package com.jiangyu.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * <p>Title: UI切换工具类 </p>
 * <p> Description:</p>
 * <p>1.添加Fragment</p>
 * <p>2.替换Fragment</p>
 * <p>3.交换Fragment</p>
 * <p>4.隐藏Fragment</p>
 * <p>5.移除Fragment</p>
 * <p>6.启动某个Activity</p>
 * <p>7.启动某个Activity带参数</p>
 * <p>8.启动某个Activity带回调</p>
 *
 */
public class UILauncherUtil {
	private static UILauncherUtil instance;

	public static synchronized UILauncherUtil getIntance() {
		if (instance == null) {
			instance = new UILauncherUtil();
		}
		return instance;
	}

	/**
	 * 添加Fragment
	 * 
	 * @param newFrg
	 * @param container
	 *            容器
	 * @param activity
	 */
	public void addFragment(Fragment newFrg, int container,
							FragmentActivity activity) {
		addFragment(newFrg, container, null, activity);
	}

	public void addFragmentButHide(Fragment newFrg, int container,
								   FragmentActivity activity) {
		try {
			FragmentManager fragmentManager = activity
					.getSupportFragmentManager();
			if (!newFrg.isAdded()) {
				fragmentManager.beginTransaction().add(container, newFrg)
						.hide(newFrg).commitAllowingStateLoss();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 添加Frg
	 * 
	 * @param newFrg
	 * @param container
	 * @param tag
	 * @param activity
	 */
	public void addFragment(Fragment newFrg, int container, String tag,
							FragmentActivity activity) {
		try {
			FragmentManager fragmentManager = activity
					.getSupportFragmentManager();
			if (!newFrg.isAdded()) {
				fragmentManager.beginTransaction().add(container, newFrg, tag)
						.commitAllowingStateLoss();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 替换Fragment
	 * 
	 * @param newFrg
	 * @param container
	 *            容器
	 * @param activity
	 */
	public void replaceFlagment(Fragment newFrg, int container,
								FragmentActivity activity) {
		replaceFlagment(newFrg, container, null, activity);
	}

	/**
	 * 替换Fragment
	 * 
	 * @param newFrg
	 * @param container
	 * @param tag
	 * @param activity
	 */
	public void replaceFlagment(Fragment newFrg, int container, String tag,
								FragmentActivity activity) {
		try {
			FragmentManager fragmentManager = activity
					.getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(container, newFrg, tag)
					.commitAllowingStateLoss();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 交换Fragment
	 * 
	 * @param Current
	 *            当前Fragment
	 * @param from
	 * @param to
	 * @param container
	 *            容器
	 * @param activity
	 */
	public void switchContent(Fragment current, Fragment to, int container,
							  FragmentActivity activity) {
		try {
			if (current != to) {
				FragmentTransaction transaction = activity
						.getSupportFragmentManager().beginTransaction();
				if (!to.isAdded()) { // 先判断是否被add过
					transaction.hide(current).add(container, to)
							.commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到Activity中
				} else {
					transaction.hide(current).show(to)
							.commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 动画
	public void switchContent(Fragment current, Fragment to, int container,
							  int in, int out, FragmentActivity activity) {
		try {
			if (current != to) {
				FragmentTransaction transaction = activity
						.getSupportFragmentManager().beginTransaction()
						.setCustomAnimations(in, out);
				if (!to.isAdded()) { // 先判断是否被add过
					transaction.hide(current).add(container, to)
							.commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到Activity中
				} else {
					transaction.hide(current).show(to)
							.commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void hideFragment(Fragment current, FragmentActivity activity) {
		FragmentTransaction transaction = activity.getSupportFragmentManager()
				.beginTransaction();
		try {
			if (current != null && current.isAdded()) {
				transaction.hide(current).commitAllowingStateLoss();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removeFragment(Fragment current, FragmentActivity activity) {
		FragmentTransaction transaction = activity.getSupportFragmentManager()
				.beginTransaction();
		try {
			if (current != null && current.isAdded()) {
				transaction.remove(current).commitAllowingStateLoss();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showFragment(Fragment current, FragmentActivity activity) {
		FragmentTransaction transaction = activity.getSupportFragmentManager()
				.beginTransaction();
		try {
			if (current != null && current.isAdded()) {
				transaction.show(current).commitAllowingStateLoss();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void launcherActivity(Context context, Class<?> class1) {
		Intent intent = new Intent(context, class1);
		context.startActivity(intent);
	}

	public void launcherActivityWithExtra(Context context, Class<?> class1,
			Bundle bundle) {
		Intent intent = new Intent(context, class1);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}

	public void launcherActivityForResult(Context context, Class<?> class1,
			Bundle bundle, int requestCode) {
		Intent intent = new Intent(context, class1);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		((Activity) context).startActivityForResult(intent, requestCode);
	}

}
