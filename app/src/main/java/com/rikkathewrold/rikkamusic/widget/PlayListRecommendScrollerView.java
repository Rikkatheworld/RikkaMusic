package com.rikkathewrold.rikkamusic.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.rikkathewrold.rikkamusic.util.DensityUtil;
import com.rikkathewrold.rikkamusic.util.LogUtil;

public class PlayListRecommendScrollerView extends ScrollView {
    private static final String TAG = "PlayListRecommendHorizontalScrollerView";

    private Context mContext;

    public PlayListRecommendScrollerView(Context context) {
        this(context, null);
    }

    public PlayListRecommendScrollerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayListRecommendScrollerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        LogUtil.d(TAG, "x:" + ev.getX() + " Y:" + ev.getY());
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (ev.getY() >= 0 && ev.getY() <= DensityUtil.dp2px(mContext, 165)) {
                    return false;
                }
                break;
        }
        return false;
    }
}
