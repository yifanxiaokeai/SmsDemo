package com.jiangyu.common.entity;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * JiangYu
 * 2018/1/4
 */

public class MyBaseViewHolder extends BaseViewHolder {
    public MyBaseViewHolder(View view) {
        super(view);
        AutoUtils.autoSize(view);
    }
}
