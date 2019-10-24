package com.rikkathewrold.rikkamusic.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rikkathewrold.rikkamusic.R;


public class MusicDrawerItemView extends RelativeLayout {

    private ImageView itemIcon;
    private TextView itemName;
    private TextView tvGap;


    public MusicDrawerItemView(Context context) {
        this(context, null);
    }

    public MusicDrawerItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MusicDrawerItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initTypeArray(context, attrs);
    }

    private void initView(Context context) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.layout_music_drawer_item, this, true);
        itemIcon = contentView.findViewById(R.id.iv_item_icon);
        itemName = contentView.findViewById(R.id.iv_item_name);
        tvGap = contentView.findViewById(R.id.tv_gap);
    }

    private void initTypeArray(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MusicDrawerItemView);
        if (ta != null) {
            Drawable drawable = ta.getDrawable(R.styleable.MusicDrawerItemView_item_icon);
            itemIcon.setImageDrawable(drawable);

            String text = ta.getString(R.styleable.MusicDrawerItemView_item_name);
            itemName.setText(text);

            boolean isShowGap = ta.getBoolean(R.styleable.MusicDrawerItemView_is_show_gap, false);
            if (isShowGap) {
                tvGap.setVisibility(VISIBLE);
            }

            ta.recycle();
        }
    }

    public void setText(String text) {
        itemName.setText(text);
    }

}
