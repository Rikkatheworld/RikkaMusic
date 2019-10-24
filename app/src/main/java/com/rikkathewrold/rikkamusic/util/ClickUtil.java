package com.rikkathewrold.rikkamusic.util;

import android.util.SparseArray;
import android.view.View;

public class ClickUtil {
    //两次按钮点击时间间隔不能少于1s
    private static SparseArray<Long> lastClickViewArray = new SparseArray<>();

    public static boolean isFastClick(int minDelayTime, View view) {
        long curClickTime = System.currentTimeMillis();
        long lastClickTime = lastClickViewArray.get(view.getId(), -1L);
        if ((curClickTime - lastClickTime) > minDelayTime) {
            lastClickViewArray.put(view.getId(), curClickTime);
            return false;
        } else {
            return true;
        }
    }
}
