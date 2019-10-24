package com.rikkathewrold.rikkamusic.util;

import android.animation.ValueAnimator;
import android.text.TextUtils;
import android.text.format.DateUtils;

import com.rikkathewrold.rikkamusic.song.bean.LrcEntry;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用来解析歌词的工具类
 */
public class LrcUtils {
    private static final String TAG = "LrcUtils";

    /**
     * 从文本解析双语歌词
     * 传入 歌词String，解析成List<Entry>
     */
    public static List<LrcEntry> parseLrc(String[] lrcTexts) {
        if (lrcTexts == null || lrcTexts.length != 2 || TextUtils.isEmpty(lrcTexts[0])) {
            return null;
        }

        List<LrcEntry> mainEntryList = parseLrc(lrcTexts[0]);
        List<LrcEntry> secondEntryList = parseLrc(lrcTexts[1]);

        //设置双语歌词
        if (mainEntryList != null && secondEntryList != null) {
            for (LrcEntry mainEntry : mainEntryList) {
                for (LrcEntry secondEntry : secondEntryList) {
                    if (mainEntry.getTime() == secondEntry.getTime()) {
                        mainEntry.setSecondText(secondEntry.getText());
                    }
                }
            }
        }
        return mainEntryList;
    }

    private static List<LrcEntry> parseLrc(String lrcText) {
        if (TextUtils.isEmpty(lrcText)) {
            return null;
        }

        List<LrcEntry> entryList = new ArrayList<>();
        String[] array = lrcText.split("\\n");
        for (String line : array) {
            List<LrcEntry> list = parseLine(line);
            if (list != null && !list.isEmpty()) {
                entryList.addAll(list);
            }
        }

        Collections.sort(entryList);
        return entryList;
    }

    /**
     * 用正则匹配解析一行歌词
     */
    private static List<LrcEntry> parseLine(String line) {
        if (TextUtils.isEmpty(line)) {
            return null;
        }

        line = line.trim();
        // [00:17.65]让我掉下眼泪的
        Matcher lineMatcher = Pattern.compile("((\\[\\d\\d:\\d\\d\\.\\d{2,3}\\])+)(.+)").matcher(line);
        if (!lineMatcher.matches()) {
            return null;
        }

        String times = lineMatcher.group(1);
        String text = lineMatcher.group(3);
        List<LrcEntry> entryList = new ArrayList<>();

        // [00:17.65]
        Matcher timeMatcher = Pattern.compile("\\[(\\d\\d):(\\d\\d)\\.(\\d{2,3})\\]").matcher(times);
        while (timeMatcher.find()) {
            long min = Long.parseLong(timeMatcher.group(1));
            long sec = Long.parseLong(timeMatcher.group(2));
            String milString = timeMatcher.group(3);
            long mil = Long.parseLong(milString);
            // 如果毫秒是两位数，需要乘以10
            if (milString.length() == 2) {
                mil = mil * 10;
            }
            long time = min * DateUtils.MINUTE_IN_MILLIS + sec * DateUtils.SECOND_IN_MILLIS + mil;
            entryList.add(new LrcEntry(time, text));
        }
        return entryList;
    }

    /**
     * 转为[分:秒]
     */
    public static String formatTime(long milli) {
        int m = (int) (milli / DateUtils.MINUTE_IN_MILLIS);
        int s = (int) ((milli / DateUtils.SECOND_IN_MILLIS) % 60);
        String mm = String.format(Locale.getDefault(), "%02d", m);
        String ss = String.format(Locale.getDefault(), "%02d", s);
        return mm + ":" + ss;
    }

    /**
     * 重置动画缩放时长
     */
    public static void resetDurationScale() {
        try {
            Field mField = ValueAnimator.class.getDeclaredField("sDurationScale");
            mField.setAccessible(true);
            mField.setFloat(null, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
