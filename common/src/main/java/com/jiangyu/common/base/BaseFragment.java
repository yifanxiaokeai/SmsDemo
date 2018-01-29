package com.jiangyu.common.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.jiangyu.common.R;
import com.jiangyu.common.entity.EventCenter;
import com.jiangyu.common.entity.MySelfInfo;
import com.jiangyu.common.utils.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * <p>Title: Fragment基类  </p>
 * <p>Description:
 * Fragment基类
 * </p>
 */
public abstract class BaseFragment extends SupportFragment {
    protected Activity mActivity;
    protected Context mContext;
    protected LayoutInflater mInflater;
    private View mView = null;
    protected MaterialDialog mProgress;

    public abstract void initComponents(View view);

    public abstract void initData();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mActivity = getActivity();
        this.mContext = getActivity().getApplicationContext();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(getMainContentViewId(), null);
            mInflater = inflater;
            ButterKnife.bind(this, mView);
            EventBus.getDefault().register(this);
            initComponents(mView);
            initData();
        } else {
//            ((ViewGroup) mView.getParent()).removeAllViews();
            return mView;
        }
        return mView;
    }

    public abstract int getMainContentViewId();

    @Subscribe
    public void onEventMainThread(EventCenter center) {
        if (null != center) {
            this.onEventComing(center);
        }
    }

    protected abstract void onEventComing(EventCenter var1);

    /**
     * 查找View
     *
     * @param resId
     * @return
     */
    public <T extends View> T findView(int resId) {
        T view = (T) mView.findViewById(resId);
        return view;
    }

    public void showProgressDialog() {
        showProgressDialog("");// 默认显示"数据加载，请稍候..."
    }

    public void showProgressDialog(String msg) {
        if (StringUtil.isEmpty(msg)) {
            msg = "数据加载，请稍候...";
        }
        mProgress = new MaterialDialog.Builder(mActivity)
                .content(msg)
                .theme(Theme.LIGHT)
                .widgetColorRes(R.color.main_color)
                .canceledOnTouchOutside(false)
                .progress(true, 0)
                .show();
    }

    public void dismissProgressDialog() {
        if (null != mProgress && mProgress.isShowing()) {
            mProgress.dismiss();
        }
    }

    @Override
    public void onResume() {
        MySelfInfo.getInstance().getUserCache(mContext);
        super.onResume();
    }

    @Override
    public void onDestroy() {
        MySelfInfo.getInstance().writeUserToCache(mContext);
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
