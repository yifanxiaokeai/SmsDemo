package com.jiangyu.common.base;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.zhy.autolayout.config.AutoLayoutConifg;

/**
 * 主要用来获取全局的Context
 */
public class BaseApplication extends Application {

    private Handler handler;
    private static final String TAG = BaseApplication.class.getSimpleName();
    private static BaseApplication sInstance;

    /**
     * 获取全局Application对象
     *
     * @return
     * @since 2013.08.02 修改错误提示内容为Application by pcqpcq
     */
    public static BaseApplication getInstance() {
        if (sInstance == null) {
            Log.e(TAG, "THE APPLICATION OF YOUR PROJECT MUST BE 'TandyApplication', OR SOMEONE EXTEND FROM IT");
        }
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AutoLayoutConifg.getInstance().useDeviceSize();
        handler = new Handler(getMainLooper());
        sInstance = this;

        OkGo.getInstance().init(this);
    }


    public void runOnUIThread(Runnable runnable) {
        handler.post(runnable);
    }
}
