package com.rikkathewrold.rikkamusic.login.mvp.view;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.hjq.toast.ToastUtils;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseActivity;
import com.rikkathewrold.rikkamusic.base.BasePresenter;
import com.rikkathewrold.rikkamusic.util.ClickUtil;
import com.rikkathewrold.rikkamusic.util.ScreenUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectLoginActivity extends BaseActivity {
    private static final String TAG = "SelectLoginActivity";

    private long firstTime;

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_select_login);
    }

    @Override
    protected BasePresenter onCreatePresenter() {
        return null;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initModule() {
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScreenUtils.setStatusBarColor(this, Color.parseColor("#Db2C1F"));
    }

    @Override
    @OnClick(R.id.btn_phone_login)
    public void onClick(View v) {
        if (ClickUtil.isFastClick(1000, v)) {
            return;
        }
        switch (v.getId()) {
            case R.id.btn_phone_login:
                startActivity(LoginActivity.class, null, false);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {// 点击了返回按键
            exitApp(2000);// 退出应用
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 退出应用
     *
     * @param timeInterval 设置第二次点击退出的时间间隔
     */
    private void exitApp(long timeInterval) {
        if (System.currentTimeMillis() - firstTime >= timeInterval) {
            ToastUtils.show(R.string.press_exit_again);
            firstTime = System.currentTimeMillis();
        } else {
            finish();// 销毁当前activity
            System.exit(0);// 完全退出应用
        }
    }

}
