package com.rikkathewrold.rikkamusic.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.rikkathewrold.rikkamusic.R;

public class PlayListCover extends RelativeLayout {

    private Context mContext;
    private ImageView ivCover;
    private TextView tvName;

    public PlayListCover(Context context) {
        this(context, null);
    }

    public PlayListCover(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayListCover(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_playlist_cover, this, true);
        ivCover = v.findViewById(R.id.iv_cover);
        tvName = v.findViewById(R.id.tv_name);
    }

    public void setText(String text) {
        tvName.setText(text);
    }

    public void setCover(String url) {
        Glide.with(mContext).load(url).transition(new DrawableTransitionOptions().crossFade()).into(ivCover);
    }
}
