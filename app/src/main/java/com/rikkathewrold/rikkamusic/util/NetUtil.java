package com.rikkathewrold.rikkamusic.util;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.rikkathewrold.rikkamusic.App;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class NetUtil {

    private static final String TAG = "NetUtil";

    /**
     * 判断网络是否连接
     *
     * @return 网络是否连接
     */
    public static boolean isConnected() {

        ConnectivityManager connectivity = (ConnectivityManager) App.getContext()
                .getSystemService(CONNECTIVITY_SERVICE);

        if (null != connectivity) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否是wifi连接
     *
     * @return 是否是wifi连接
     */
    public static boolean isWifi() {
        ConnectivityManager cm = (ConnectivityManager) App.getContext()
                .getSystemService(CONNECTIVITY_SERVICE);
        return cm != null && cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

    }

}
