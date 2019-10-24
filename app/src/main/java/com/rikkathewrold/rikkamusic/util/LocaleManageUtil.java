package com.rikkathewrold.rikkamusic.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;


import com.rikkathewrold.rikkamusic.R;

import java.util.Locale;

/**
 * 系统的Locale配置
 *
 * Created By Rikka on 2019/7/12
 */
public class LocaleManageUtil {
    private static final String TAG = "LocaleManageUtil";
    public static final int LOCAL_CHINA = 0;
    public static final int LOACL_ENGLISH = 1;

    /**
     * 设置系统的Locale
     *
     * @param context
     * @return
     */
    public static Context setLocal(Context context) {
        return updateResources(context, getSetLanguageLocale(context));
    }

    private static Context updateResources(Context context, Locale locale) {
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        return context;
    }

    /**
     * 获取选择的语言设置Locale
     *
     * @param context
     * @return
     */
    public static Locale getSetLanguageLocale(Context context) {
        switch (SharePreferenceUtil.getInstance(context).getSelectLanguage()) {
            case 0:
                return Locale.CHINA;
            case 1:
                return Locale.ENGLISH;
            default:
                if (getSystemLocale(context).getLanguage().contains("zh") ||
                        getSystemLocale(context).getCountry().contains("CN")) {
                    return Locale.CHINA;
                } else return Locale.ENGLISH;
        }
    }

    /**
     * 获取系统的locale
     *
     * @return Locale对象
     */
    public static Locale getSystemLocale(Context context) {
        return SharePreferenceUtil.getInstance(context).getSystemCurrentLocal();
    }


    public static String getSelectLanguage(Context context) {
        switch (SharePreferenceUtil.getInstance(context).getSelectLanguage()) {
            case 0:
                return context.getString(R.string.language_cn);
            case 1:
                return context.getString(R.string.language_en);
            default:
                return context.getString(R.string.language_cn);
        }
    }
}
