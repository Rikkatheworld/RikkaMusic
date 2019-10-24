package com.rikkathewrold.rikkamusic.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public class RikkaTextView extends android.support.v7.widget.AppCompatTextView {
    private static final String TAG = "RikkaTextView";

    private Context mContext;

    public RikkaTextView(Context context) {
        super(context);
    }

    public RikkaTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RikkaTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
