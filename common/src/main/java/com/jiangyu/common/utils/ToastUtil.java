package com.jiangyu.common.utils;

import android.widget.Toast;

import com.jiangyu.common.base.BaseApplication;


/**
 * Toast相关方法
 */
public class ToastUtil {
	private static Toast toast = null;
	
	public static void showMessage(String msg, int duration) {
//		Toast.makeText(BaseApplication.getInstance(), msg, duration).show();
		if(toast == null)
		  {
		   toast = Toast.makeText(BaseApplication.getInstance(), msg, duration);
		  }
		  else {
		   toast.setText(msg);
		  }
		  toast.show();
	}

	public static void showMessage(int resid, int duration) {
		Toast.makeText(BaseApplication.getInstance(), resid, duration).show();
	}

	public static void showShort(int resid) {
		showMessage(resid, Toast.LENGTH_SHORT);
	}

	public static void showShort(String msg) {
		showMessage(msg, Toast.LENGTH_SHORT);
	}

	public static void showLong(int resid) {
		showMessage(resid, Toast.LENGTH_LONG);
	}

	public static void showLong(String msg) {
		showMessage(msg, Toast.LENGTH_LONG);
	}
}
