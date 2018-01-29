package com.jiangyu.common.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.gyf.barlibrary.ImmersionBar;
import com.jiangyu.common.R;
import com.jiangyu.common.entity.EventCenter;
import com.jiangyu.common.entity.MySelfInfo;
import com.jiangyu.common.utils.ActivityAnimation;
import com.jiangyu.common.utils.GlobalExceptionHanlder;
import com.jiangyu.common.utils.StringUtil;
import com.lzy.okgo.OkGo;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * <p>
 * Activity 基础类 <strong>所有Activity必须继承该类。</strong>
 * </p>
 * <p>
 * <p>
 * onCreate方法执行操作: <br/>
 * <i>1.getMainContentViewId</i> 渲染界面<br/>
 * <i>2.initComponents</i> 初始化控件<br/>
 * <i>3.initData</i> 初始化数据，填充数据<br/>
 * <i>4.setCustomTitleTop</i><br/>
 * <i>5.registerExitReceiver</i> 注册退出广播<br/>
 * </p>
 */
public abstract class BaseActivity extends SupportActivity {
    //autoLayout
    private static final String LAYOUT_LINEARLAYOUT = "LinearLayout";
    private static final String LAYOUT_FRAMELAYOUT = "FrameLayout";
    private static final String LAYOUT_RELATIVELAYOUT = "RelativeLayout";

    protected ImmersionBar mImmersionBar;
    protected Context mContext;
    protected Activity mActivity;
    private BroadcastReceiver exitBroadcastReceiver;
    protected View mView;
    public static final String ACTION_EXIT_APP = "action_exit_app";
    protected MaterialDialog mProgress;
    protected static Resources resource;
    protected static String pkgName;
    private String name;

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        //autoLayout
        View view = null;
        if (name.equals(LAYOUT_FRAMELAYOUT)) {
            view = new AutoFrameLayout(context, attrs);
        }

        if (name.equals(LAYOUT_LINEARLAYOUT)) {
            view = new AutoLinearLayout(context, attrs);
        }

        if (name.equals(LAYOUT_RELATIVELAYOUT)) {
            view = new AutoRelativeLayout(context, attrs);
        }
        if (view != null) return view;
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        resource = this.getResources();
        pkgName = this.getPackageName();
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // remove title
        EventBus.getDefault().register(this);
        if (getMainContentViewId() != 0) {
            mView = View.inflate(this, getMainContentViewId(), null);
            setContentView(mView); // set view
        }
        ButterKnife.bind(this);
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();

        name = getClass().getName();
        if (!name.contains("WelcomeActivity")) {
            ActivityAnimation.PendingTransitionIn(this);
        }

        mContext = getApplicationContext(); // get context
        mActivity = this;

        handIntent();

        initComponents(); // init all components

        initData(); // init the whole activity's data

        registerExitReceiver();

//        registerException();

    }

    @Subscribe
    public void onEventMainThread(EventCenter center) {
        if (null != center) {
            this.onEventComing(center);
        }
    }

    protected abstract void onEventComing(EventCenter var1);

    /**
     * 注册全局异常处理类
     */
    private void registerException() {
        GlobalExceptionHanlder.getInstance().register(mContext);
    }

    /**
     * 查找View
     *
     * @param resId
     * @return
     */
    public <T extends View> T findView(int resId) {
        T view = (T) findViewById(resId);
        return view;
    }

    /**
     * 注册退出应用广播,默认注册 发送动作为action_exit_app的广播，可销毁所有Activity <br/>
     * 不想当前activity被销毁，可覆盖该方法
     */
    protected void registerExitReceiver() {
        exitBroadcastReceiver = new ExitBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_EXIT_APP);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(
                exitBroadcastReceiver, filter);
    }

    /**
     * 注销广播 当前activity销毁，自动调用该方法
     */
    protected void unRegisterExitReceiver() {
        if (exitBroadcastReceiver != null) {
            LocalBroadcastManager.getInstance(mContext).unregisterReceiver(
                    exitBroadcastReceiver);
            exitBroadcastReceiver = null;
        }
    }

    /**
     * 退出应用程序 发送动作为action_exit_app的广播，销毁所有注册了广播的activity
     */
    public void exitApp() {
        sendBroadcast(new Intent(ACTION_EXIT_APP));
    }

    // 接收退出广播
    private class ExitBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            finish();
        }
    }

    /**
     * 处理其他活动传递过来的数据
     */
    protected void handIntent() {
    }

    /**
     * 初始化UI组件及数据
     */
    protected abstract void initComponents();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 布局ID
     */
    protected abstract int getMainContentViewId();

    /**
     * 获取相应的组件的ID
     *
     * @param idName
     * @param idType
     * @return
     */
    protected int getIdentifier(String idName, String idType) {
        return resource.getIdentifier(idName, idType, pkgName);
    }

    public static class IdentifierType {
        public static final String ID = "id";
        public static final String STRING = "string";
        public static final String COLOR = "color";
        public static final String TYPE = "type";
        public static final String DRAWABLE = "drawable";
        public static final String LAYOUT = "layout";
    }

    private class OnReturnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        MySelfInfo.getInstance().getUserCache(mContext);
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (!name.contains("WelcomeActivity")) {
            ActivityAnimation.PendingTransitionOut(this);
        }
        super.onPause();
    }

    public void showProgressDialog() {
        showProgressDialog("");// 默认显示"数据加载，请稍候..."
    }

    public void showProgressDialog(String msg) {
        if (StringUtil.isEmpty(msg)) {
            msg = "数据加载，请稍候...";
        }
        mProgress = new MaterialDialog.Builder(this)
                .content(msg)
                .theme(Theme.LIGHT)
                .widgetColorRes(R.color.main_color)
                .canceledOnTouchOutside(false)
                .progress(true, 0)
                .show();
        mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                BaseActivity.this.finish();
            }
        });
    }

    public void dismissProgressDialog() {
        if (null != mProgress && mProgress.isShowing()) {
            mProgress.dismiss();
        }
    }

    @Override
    public void finish() {
        super.finish();
        dismissProgressDialog();
        MySelfInfo.getInstance().writeUserToCache(mContext);
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        EventBus.getDefault().unregister(this);
        ButterKnife.unbind(this);
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
        unRegisterExitReceiver();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
        MySelfInfo.getInstance().writeUserToCache(mContext);
    }

}
