package com.rikkathewrold.rikkamusic.base;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Window;
import android.view.WindowManager;

import com.blankj.utilcode.util.ScreenUtils;
import com.rikkathewrold.rikkamusic.R;

/**
 * Dialog基类
 */
public class BaseRikkaMusicDialog extends Dialog {

    public BaseRikkaMusicDialog(@NonNull Context context) {
        this(context, R.style.RikkaBaseDialog);
    }

    public BaseRikkaMusicDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    public void show() {
        super.show();
        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (ScreenUtils.getScreenWidth() * 0.8); // 宽度设置为屏幕的0.65，根据实际情况调整
        dialogWindow.setAttributes(p);
    }
}
