package com.rikkathewrold.rikkamusic.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class RikkaFlowLayout extends ViewGroup {
    public RikkaFlowLayout(Context context) {
        this(context, null);
    }

    public RikkaFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RikkaFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        //累加当前行的行宽
        int lineWidth = 0;
        //当前行的行高
        int lineHeight = 0;
        //当前空间的top坐标和left坐标
        int top = 0, left = 0;

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            if (childWidth + lineWidth > getMeasuredWidth()) {
                //换行
                top += lineHeight;
                left = 0;
                lineHeight = childHeight;
                lineWidth = childWidth;
            } else {
                lineHeight = Math.max(lineHeight, childHeight);
                lineWidth += childWidth;
            }

            int lc = left + lp.leftMargin;
            int tc = top + lp.topMargin;
            int rc = lc + child.getMeasuredWidth();
            int bc = tc + child.getMeasuredHeight();

            child.layout(lc, tc, rc, bc);
            //将left置为下一个子控件的起始点
            left += childWidth;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //记录每一行的宽度
        int lineWidth = 0;
        //记录每一行的高度
        int lineHeight = 0;
        //记录整个Layout的高度
        int height = 0;
        //记录整个Layout的宽度
        int width = 0;

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            if (lineWidth + childWidth > measureWidth) {
                //需要换行
                height += lineHeight;
                //因为当前行放不下当前空间，而将此控件调到下一行，所以将此控件的高度和宽度初始化给lineHeight、lineWidth
                lineHeight = childHeight;
                lineWidth = childWidth;
            } else {
                //否则累加lineWidth，lineHeight取最大值
                lineHeight = Math.max(lineHeight, childHeight);
                lineWidth += childWidth;
            }

            //因为最后一行是不会超出width范围的，所以需要单独处理
            if (i == count - 1) {
                height += lineHeight;
                width = Math.max(width, lineWidth);
            }
        }

        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? measureWidth : width, heightMode == MeasureSpec.EXACTLY ? measureHeight : height);
    }
}
