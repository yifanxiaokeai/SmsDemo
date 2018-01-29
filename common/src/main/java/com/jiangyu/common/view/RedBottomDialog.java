package com.jiangyu.common.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jiangyu.common.R;
import com.jiangyu.common.utils.StringUtil;


public class RedBottomDialog extends AlertDialog {
    private String title;
    private String desc;
    private String left;
    private String right;
    private TextView dialog_title;
    private TextView dialog_desc;
    private TextView dialog_left;
    private TextView dialog_right;
    private Context context;

    public RedBottomDialog(Context context, int theme, String title, String desc, String left, String right) {
        super(context, theme);
        this.context = context;
        this.title = title;
        this.desc = desc;
        this.left = left;
        this.right = right;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.red_bottom_dialog);

         dialog_title = (TextView) findViewById(R.id.dialog_title);
         dialog_desc = (TextView) findViewById(R.id.dialog_desc);
         dialog_left = (TextView) findViewById(R.id.dialog_left);
         dialog_right = (TextView) findViewById(R.id.dialog_right);

        dialog_title.setText(title);
        dialog_desc.setText(desc);
        if (StringUtil.isEmpty(left)) {
            dialog_left.setVisibility(View.GONE);
            dialog_right.setBackgroundResource(R.drawable.shape_radius_bottom_selector);
        } else {
            dialog_left.setText(left);
        }
        dialog_right.setText(right);
    }

    public void setLeftOnclicListener(View.OnClickListener leftOnclicListener){
        dialog_left.setOnClickListener(leftOnclicListener);
    }

    public void setRightOnclicListener(View.OnClickListener rightOnclicListener){
        dialog_right.setOnClickListener(rightOnclicListener);
    }


}