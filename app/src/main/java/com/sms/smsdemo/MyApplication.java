package com.sms.smsdemo;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.jiangyu.common.base.BaseApplication;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;



public class MyApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.addLogAdapter(new AndroidLogAdapter());
        Fresco.initialize(this);

    }
}
