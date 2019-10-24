package com.rikkathewrold.rikkamusic.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

/**
 * 歌单广场的3个推荐歌单的Pager
 */
public class RikkaPlayListPager extends ViewGroup {
    private static final String TAG = "RikkaViewPager";

    //最小缩放倍数 = 0.75
    private static final float MIN_SCALE = 0.75f;
    //最小透明度 = 0.5
    private static final float MIN_ALPHA = 0.5f;
    //最小滑动距离 = 15
    private static final float MIN_SLOP_DISTANCE = 5;

    private Context mContext;
    //记录刚按下的x、y
    private int mDownX, mDownY;
    //位移距离，我们通过该值来判断View的滑动距离、动画差值
    private float totalOffsetX = 0;
    //单击某一个View时的位移动画
    private ValueAnimator offsetAnimator;
    //位移百分比，用来计算缩放比例、基线位置、透明度
    private float offsetPercent;
    //如果滑动超出了最小滑动距离，则判定为滑动，否则判定为单击
    private boolean isDraged;
    //判断是否交换过层级
    private boolean isReordered;
    //每次都要记录一下点击的位置，来判断是否超出最小滑动距离
    private float mLastX, mLastY;
    //单击监听器
    private OnPlayListClickListener clickListener;

    public RikkaPlayListPager(Context context) {
        this(context, null);
    }

    public RikkaPlayListPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RikkaPlayListPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //先测量子View
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        //因为这个时候已经测量完子View了，所以通过子View来计算整个View的宽高
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);

        //根据获取的宽高拿去用
        setMeasuredDimension(width, height);
    }

    //整个View的宽度是三个子View的和
    private int measureWidth(int widthMeasureSpec) {
        int totalWidth = 0;

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            totalWidth = widthSize;
        } else {
            for (int i = 0; i < getChildCount(); i++) {
                RikkaLayoutParams lp = (RikkaLayoutParams) getChildAt(i).getLayoutParams();
                totalWidth += getChildAt(i).getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            }
        }
        return totalWidth;
    }

    //整个View的高 取三个View的最大值
    private int measureHeight(int heightMeasureSpec) {
        int maxHeight = 0;

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (heightMode == MeasureSpec.EXACTLY) {
            //如果是具体值就取具体值
            maxHeight = heightSize;
        } else {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                RikkaLayoutParams lp = (RikkaLayoutParams) child.getLayoutParams();
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
            }
        }
        return maxHeight;
    }


    /**
     * 根据基准线去布局子View
     * 基准线有四条，子View分别在这四条线上
     * 如果是第一次layout，则记录每个view的index
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            int baseLineX = calBaseLine(i);
            int baseLineY = getHeight() / 2;

            //滑动的过程也是layout的过程，所以在layout的时候也要更改其透明度和缩放度
            View child = getChildAt(i);
            RikkaLayoutParams lp = (RikkaLayoutParams) child.getLayoutParams();
            child.setScaleX(lp.getScale());
            child.setScaleY(lp.getScale());
            child.setAlpha(lp.getAlpha());
            int left = baseLineX - child.getMeasuredWidth() / 2;
            int top = baseLineY - child.getMeasuredHeight() / 2;
            int right = left + child.getMeasuredWidth();
            int bottom = top + child.getMeasuredHeight();

            child.layout(left + lp.leftMargin + getPaddingLeft(),
                    top + lp.topMargin + getPaddingTop(),
                    right + lp.rightMargin + getPaddingRight(),
                    bottom + lp.bottomMargin + getPaddingBottom());
        }
    }

    /**
     * 根据offsetPercent来计算基线位置,子View是根据基线来布局的
     */
    private int calBaseLine(int index) {
        float baseline = 0;
        //最左边的baseline
        float baselineLeft = getWidth() / 4;
        //最中间的baseline
        float baselineCenter = getWidth() / 2;
        //最右边的baseline
        float baselineRight = getWidth() - baselineLeft;

        RikkaLayoutParams lp = (RikkaLayoutParams) getChildAt(index).getLayoutParams();
        //根据lp的from 和 to来确定基线位置
        switch (lp.getFrom()) {
            case 0:
                if (lp.getTo() == 1) {
                    baseline = baselineLeft + (baselineRight - baselineLeft) * -offsetPercent;
                } else if (lp.getTo() == 2) {
                    baseline = baselineLeft + (baselineCenter - baselineLeft) * offsetPercent;
                } else {
                    baseline = baselineLeft;
                }
                break;
            case 1:
                if (lp.getTo() == 0) {
                    baseline = baselineRight - (baselineRight - baselineLeft) * offsetPercent;
                } else if (lp.getTo() == 2) {
                    baseline = baselineRight + (baselineRight - baselineCenter) * offsetPercent;
                } else {
                    baseline = baselineRight;
                }
                break;
            case 2:
                if (lp.getTo() == 1) {
                    baseline = baselineCenter + (baselineRight - baselineCenter) * offsetPercent;
                } else if (lp.getTo() == 0) {
                    baseline = baselineCenter + (baselineCenter - baselineLeft) * offsetPercent;
                } else {
                    baseline = baselineCenter;
                }
                break;
        }

        return (int) baseline;
    }

    /**
     * 判断是否正在滑动该View
     */
    public boolean isScrolling() {
        return isDraged;
    }

    /**
     * 这里要自己写一个ViewGroup的LayoutParams来记录 scale、alpha、from、to,以及位置index
     */
    public class RikkaLayoutParams extends MarginLayoutParams {
        float scale = 0f;
        float alpha = 0f;
        int from;
        int to;
        int index;

        public float getScale() {
            return scale;
        }

        public void setScale(float scale) {
            this.scale = scale;
        }

        public float getAlpha() {
            return alpha;
        }

        public void setAlpha(float alpha) {
            this.alpha = alpha;
        }

        public int getFrom() {
            return from;
        }

        public void setFrom(int from) {
            this.from = from;
        }

        public int getTo() {
            return to;
        }

        public void setTo(int to) {
            this.to = to;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public RikkaLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public RikkaLayoutParams(int width, int height) {
            super(width, height);
        }

        public RikkaLayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

    /**
     * 要支持margin，所以要重写generate方法
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new RikkaLayoutParams(mContext, attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new RikkaLayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new RikkaLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }


    /**
     * addView的时候需要初始化
     */
    @Override
    public void addView(View child, int index, LayoutParams params) {
        RikkaLayoutParams lp;
        if (params instanceof RikkaLayoutParams) {
            lp = (RikkaLayoutParams) params;
        } else {
            lp = new RikkaLayoutParams(params);
        }
        if (getChildCount() < 2) {
            lp.setAlpha(MIN_ALPHA);
            lp.setScale(MIN_SCALE);
        } else {
            lp.setAlpha(1f);
            lp.setScale(1f);
        }
        super.addView(child, index, params);
    }

    /**
     * 如果是滑动，则调用onTouchEvent，如果只是单击，可以切换View
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        isDraged = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = x;
                mDownY = y;
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                //如果滑动超出规定的距离，则可以滑动View
                int offsetX = (int) (x - mLastX);
                int offsetY = (int) (y - mLastY);
                if (Math.abs(offsetX) > MIN_SLOP_DISTANCE && Math.abs(offsetY) > MIN_SLOP_DISTANCE) {
                    mLastX = x;
                    mLastY = y;
                    isDraged = true;
                }
            case MotionEvent.ACTION_UP:
                //抬起来的时候需要做单击或者 View的回弹动画
                handleActionUp(x, y);
                isDraged = false;
                break;
        }
        return isDraged;
    }

    /**
     * onTouchEvent就是确定是要滑动了，根据滑动距离，做子View的位移动画
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //通过总位移量除以View长来得到百分比
                int offsetX = (int) (x - mLastX);
                totalOffsetX += offsetX;
                moveItem();
                break;
            case MotionEvent.ACTION_UP:
                handleActionUp(x, y);
                isDraged = false;
                break;
        }
        mLastX = x;
        mLastY = y;
        //能走到onTouchEvent就肯定是返回true的
        return true;
    }

    /**
     * 通过百分比的正负值来确定每个View要去到哪里、设置透明度和缩放、交换View的层级
     */
    private void moveItem() {
        offsetPercent = totalOffsetX / getWidth();
        setViewFromAndTo();
        changeViewLevel();
        changeAlphaAndScale();
        requestLayout();
    }

    /**
     * 根据百分比的正负值，来设置View的from和to
     * 如果是负则说明手指正在往左边滑动，则 0->1,1->2,2->0，反之亦然
     */
    private void setViewFromAndTo() {
        //如果滑动距离超出了屏幕的宽度，则超出的部分要更新
        if (Math.abs(offsetPercent) >= 1) {
            //在每次完整的滑完一次后，需要重置isReordered，不然当一次滑动很长距离时，会产生问题
            isReordered = false;
            for (int i = 0; i < getChildCount(); i++) {
                RikkaLayoutParams lp = (RikkaLayoutParams) getChildAt(i).getLayoutParams();
                lp.setFrom(lp.getTo());
            }

            totalOffsetX %= getWidth();
            offsetPercent %= 1f;
        } else {
            //否则就要判断from和to
            for (int i = 0; i < getChildCount(); i++) {
                RikkaLayoutParams lp = (RikkaLayoutParams) getChildAt(i).getLayoutParams();
                switch (lp.getFrom()) {
                    case 0:
                        lp.setTo(offsetPercent > 0 ? 2 : 1);
                        break;
                    case 1:
                        lp.setTo(offsetPercent > 0 ? 0 : 2);
                        break;
                    case 2:
                        lp.setTo(offsetPercent > 0 ? 1 : 0);
                        break;
                }
            }
        }
    }

    /**
     * 当滑动进度超出了0.5则需要交换层级，2是最上层，0和1在下层，交换的时候交换1,2就行了
     * isReordered判断有没有交换过层级，每次onInterceptTouchEvent的时候都要重置
     * 因为可能会交换了还要交换回来
     */
    private void changeViewLevel() {
        if (Math.abs(offsetPercent) >= 0.5f) {
            if (!isReordered) {
                exchangeOrder(1, 2);
                isReordered = true;
            }
        } else {
            if (isReordered) {
                //如果没有超出0.5f，但是又交换过层级，说明滑到一半后又往回滑了，需要交换回来
                exchangeOrder(1, 2);
                isReordered = false;
            }
        }
    }

    /**
     * 使用attachViewToParent和detachAllViewsFromParent来交换两个Index的层级
     */
    public void exchangeOrder(int fromIndex, int toIndex) {
        if (fromIndex == toIndex) {
            return;
        }
        View fromChild = getChildAt(fromIndex);
        View toChild = getChildAt(toIndex);

        detachViewFromParent(fromChild);
        detachViewFromParent(toChild);

        attachViewToParent(fromChild, toIndex > getChildCount() ? getChildCount() : toIndex, fromChild.getLayoutParams());
        attachViewToParent(toChild, fromIndex > getChildCount() ? getChildCount() : fromIndex, toChild.getLayoutParams());

        invalidate();
    }

    /**
     * 改变正在移动的View的Scale和透明度
     */
    private void changeAlphaAndScale() {
        for (int i = 0; i < getChildCount(); i++) {
            RikkaLayoutParams lp = (RikkaLayoutParams) getChildAt(i).getLayoutParams();
            switch (lp.getFrom()) {
                case 0:
                    if (lp.getTo() == 2) {
                        lp.setAlpha(MIN_ALPHA + (1f - MIN_ALPHA) * offsetPercent);
                        lp.setScale(MIN_SCALE + (1f - MIN_SCALE) * offsetPercent);
                    } else if (lp.getTo() == 1) {
                        //将View和低层的View交换
                        exchangeOrder(indexOfChild(getChildAt(i)), 0);
                    }
                    break;
                case 1:
                    if (lp.getTo() == 0) {
                        exchangeOrder(indexOfChild(getChildAt(i)), 0);
                    } else if (lp.getTo() == 2) {
                        lp.setAlpha(MIN_ALPHA + (1f - MIN_ALPHA) * Math.abs(offsetPercent));
                        lp.setScale(MIN_SCALE + (1f - MIN_SCALE) * Math.abs(offsetPercent));
                    }
                    break;
                case 2:
                    lp.setAlpha(1f - (1f - MIN_ALPHA) * Math.abs(offsetPercent));
                    lp.setScale(1f - (1f - MIN_SCALE) * Math.abs(offsetPercent));
            }
        }
    }

    /**
     * 每次抬起手指的时候需要判断当前要不要做动画
     */
    private void handleActionUp(int x, int y) {
        if (Math.abs(x - mDownX) < MIN_SLOP_DISTANCE && Math.abs(y - mLastY) < MIN_SLOP_DISTANCE) {
            for (int i = getChildCount() - 1; i >= 0; i--) {
                //确定是单击，首先要判断是点击的是哪一个View，因为传入的points会改变，所以每次都要重新定义
                float[] points = new float[2];
                points[0] = x;
                points[1] = y;

                View clickView = getChildAt(i);
                if (isPointInView(clickView, points)) {
                    Log.d(TAG, "isPointInView:" + i);
                    if (indexOfChild(clickView) != 2) {
                        //如果点到1、0View，则将他们移到最前方
                        setSelection(clickView);
                    } else {
                        RikkaLayoutParams lp = (RikkaLayoutParams) clickView.getLayoutParams();
                        if (clickListener != null) {
                            clickListener.onPlayListClick(lp.getIndex());
                        }
                    }
                    break;
                }
            }
            return;
        }
        initAnimator();
    }

    /**
     * 也是做动画，只是它是做一次完整的动画，起始值
     */
    private void setSelection(View clickView) {
        int start = 0;
        int end = 0;
        RikkaLayoutParams lp = (RikkaLayoutParams) clickView.getLayoutParams();
        if (lp.getFrom() == 0) {
            //从0到2
            end = getWidth();
        } else if (lp.getFrom() == 1) {
            //从1到2
            end = -getWidth();
        }
        startAnimator(start, end);
    }

    private void initAnimator() {
        if ((offsetAnimator != null && offsetAnimator.isRunning())) {
            offsetAnimator.cancel();
        }
        //初始值是当前已经位移的值
        int start = (int) totalOffsetX;
        // 终点是到View的长度，如果滑动没有超过一半，就要回到起点，即0
        int end = 0;
        if (offsetPercent >= 0.5f) {
            end = getWidth();
        } else if (offsetPercent <= -0.5f) {
            end = -getWidth();
        }
        startAnimator(start, end);
    }

    private void startAnimator(int start, int end) {
        if (offsetAnimator == null) {
            //懒加载
            offsetAnimator = ValueAnimator.ofFloat(start, end);
//            offsetAnimator.setDuration(500);
            offsetAnimator.setInterpolator(new LinearInterpolator());
            offsetAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    //每次都要改变位移
                    totalOffsetX = (float) animation.getAnimatedValue();
                    moveItem();
                }
            });
        }
        //因为是懒加载，所以每次都要设置起始值和终点值.
        offsetAnimator.setFloatValues(start, end);
        offsetAnimator.start();
    }


    /**
     * 用矩阵的方法，来定义一个点是否位于一个区域内
     */
    private boolean isPointInView(View view, float[] points) {
        // 像ViewGroup那样，先对齐一下Left和Top
        points[0] -= view.getLeft();
        points[1] -= view.getTop();
        // 获取View所对应的矩阵
        Matrix matrix = view.getMatrix();
        // 如果矩阵有应用过变换
        if (!matrix.isIdentity()) {
            // 反转矩阵
            matrix.invert(matrix);
            // 映射坐标点
            matrix.mapPoints(points);
        }
        //判断坐标点是否在view范围内
        return points[0] >= 0 && points[1] >= 0 && points[0] < view.getWidth() && points[1] < view.getHeight();
    }

    /**
     * 点击事件
     */
    public interface OnPlayListClickListener {
        void onPlayListClick(int position);
    }

    public void setClickListener(OnPlayListClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
