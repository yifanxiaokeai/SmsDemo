package com.jiangyu.common.view;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.jiangyu.common.R;


public class MaterialDialogHelper {

    public static MaterialDialog.Builder getDialogBuilder(Context context) {
        MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(context)
                .theme(Theme.LIGHT)
                .btnSelector(R.drawable.button_selector)
                .positiveText("确定")
                .positiveColorRes(R.color.main_color)
                .negativeColorRes(R.color.main_color)
                .negativeText("取消");
        return dialogBuilder;
    }
}
