package com.rikkathewrold.rikkamusic.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rikkathewrold.rikkamusic.R;

/**
 * 搜索的editText,点击回车后直接进入搜索
 */
public class SearchEditText extends RelativeLayout {
    private static final String TAG = "SearchEditText";

    private TextView tvGap;
    private EditText etTitle;
    private RelativeLayout rlEt;
    private int highlightColor, normalColor;

    public SearchEditText(Context context) {
        this(context, null);
    }

    public SearchEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initListener();
    }

    private void initView(Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_search_et, this, true);
        etTitle = v.findViewById(R.id.et);
        tvGap = v.findViewById(R.id.tv_gap);
        rlEt = v.findViewById(R.id.rl_et);
    }

    private void initListener() {
        rlEt.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                etTitle.requestFocus();
            }
        });

        etTitle.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                tvGap.setBackgroundColor(highlightColor);
            } else {
                tvGap.setBackgroundColor(normalColor);
            }
        });
    }

    public String getKeyWords() {
        return etTitle.getText().toString().trim();
    }

    public void setEditTextColor(String color) {
        highlightColor = Color.parseColor(color);
        etTitle.setTextColor(highlightColor);
        int a = Math.min(255, Math.max(0, (int) (0.7 * 255))) << 24;
        int rgb = 0x00ffffff & Color.parseColor(color);
        normalColor = a + rgb;
        etTitle.setHintTextColor(normalColor);
    }

    public void setText(String text) {
        etTitle.setText(text);
    }
}
