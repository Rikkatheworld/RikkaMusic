package com.rikkathewrold.rikkamusic.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.rikkathewrold.rikkamusic.util.DensityUtil;

public class PlayListRecommendViewPager extends ViewPager {
    private static final String TAG = "PlayListRecommendViewPager";

    private Context mContext;
    private float mLastY;

    public PlayListRecommendViewPager(@NonNull Context context) {
        this(context, null);
    }

    public PlayListRecommendViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int currentItem = getCurrentItem();
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (currentItem == 0) {
                    if (ev.getY() >= 0 && ev.getY() <= DensityUtil.dp2px(mContext, 165)) {
                        mLastY = ev.getY();
                        return false;
                    }
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public void addOnPageChangeListener(@NonNull OnPageChangeListener listener) {
        super.addOnPageChangeListener(listener);
    }
}
