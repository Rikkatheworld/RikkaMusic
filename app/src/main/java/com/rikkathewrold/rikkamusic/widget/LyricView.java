package com.rikkathewrold.rikkamusic.widget;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

import com.rikkathewrold.rikkamusic.App;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.song.bean.LrcEntry;
import com.rikkathewrold.rikkamusic.util.LogUtil;
import com.rikkathewrold.rikkamusic.util.LrcUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 歌词View
 */
public class LyricView extends View {
    private static final String TAG = "LyricView";
    //播放图片的宽高
    private static final float PLAY_DRAWABLE_WIDTH = App.getContext().getResources().getDimension(R.dimen.dp_30);
    //时间 也就是 [XX:XX] 的宽度
    private static final float TIME_TEXT_WIDTH = App.getContext().getResources().getDimension(R.dimen.dp_40);
    //时间线从一行 滑到另一行的时间
    private static final int ANIMATION_DURATION = 1000;
    //时间线从当前行 的某处 移到中间的时间
    private static final int ADJUST_DURATION = 100;

    private List<LrcEntry> mLrcEntryList = new ArrayList<>();
    //歌词画笔
    private TextPaint mLrcPaint = new TextPaint();
    //时间线 画笔
    private TextPaint mTimePaint = new TextPaint();
    //时间线 画笔属性
    private Paint.FontMetrics mTimeFontMetrics;
    //播放按钮
    private Drawable mPlayDrawable;
    //普通字体的颜色
    private int mNormalTextColor;
    //字体的大小(普通和选中的都一样大
    private float mTextSize;
    //选中行 字体颜色
    private int mCurrentTextColor;
    //时间线 所在的一行歌词 已经时间的颜色
    private int mTimelineTextColor;
    //时间线的颜色
    private int mTimelineColor;
    //padding值
    private float mPadding;
    //没有加载完歌词的 默认显示text
    private String mDefaultLabel;
    //滑动的动画
    private ValueAnimator mAnimator;
    //手势监听器
    private GestureDetector mGestureDetector;
    //播放监听器
    private OnPlayClickListener mListener;
    //滑动类
    private Scroller mScroller;
    //是否正在滑动/正在点击/显示时间线
    private boolean isFling, isTouching, isShowTimeline;
    //歌词位置
    private int mTextGravity;
    //手指在 屏幕上滑动的 偏移量
    private float mOffset;
    //每句歌词之间的间隔
    private float mDividerHeight = 0;
    //选中的歌词，即时间线所在的歌词
    private int mCurrentLine;
    //单击该view，如果不是点击播放按钮，则歌词界面消失
    private OnCoverChangeListener coverChangeListener;

    /**
     * 播放按钮的监听器，如果成功消费该事件，则更新Ui
     */
    public interface OnPlayClickListener {
        boolean onPlayClick(long time);
    }

    public LyricView(Context context) {
        this(context, null);
    }

    public LyricView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LyricView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);

        mGestureDetector = new GestureDetector(context, mSimpleOnGestureListener);
        mGestureDetector.setIsLongpressEnabled(false);
        mScroller = new Scroller(context);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.LyricView);
        mTextSize = ta.getDimension(R.styleable.LyricView_text_size, getResources().getDimension(R.dimen.sp_16));

        mDividerHeight = ta.getDimension(R.styleable.LyricView_text_divider, getResources().getDimension(R.dimen.dp_10));

        mNormalTextColor = ta.getColor(R.styleable.LyricView_normal_color, Color.parseColor("#ccffffff"));
        mCurrentTextColor = ta.getColor(R.styleable.LyricView_current_color, Color.parseColor("#ffffff"));
        mTimelineTextColor = ta.getColor(R.styleable.LyricView_time_color, Color.parseColor("#ccffffff"));
        mDefaultLabel = ta.getString(R.styleable.LyricView_default_label);
        mPadding = ta.getDimension(R.styleable.LyricView_lrc_padding, 0);
        mTimelineColor = ta.getColor(R.styleable.LyricView_timeline_color, Color.parseColor("#f0f0f0"));
        mPlayDrawable = getResources().getDrawable(R.drawable.ic_lrc_play);

        float timeTextSize = ta.getDimension(R.styleable.LyricView_time_text_size, getResources().getDimension(R.dimen.sp_10));
        mTextGravity = ta.getInteger(R.styleable.LyricView_text_gravity, LrcEntry.GRAVITY_CENTER);

        ta.recycle();

        mLrcPaint.setAntiAlias(true);
        mLrcPaint.setTextSize(mTextSize);
        mLrcPaint.setTextAlign(Paint.Align.LEFT);
        mTimePaint.setAntiAlias(true);
        mTimePaint.setTextSize(timeTextSize);
        mTimePaint.setTextAlign(Paint.Align.CENTER);
        mTimePaint.setStrokeWidth(getResources().getDimension(R.dimen.dp_2));
        mTimePaint.setStrokeCap(Paint.Cap.ROUND);
        mTimeFontMetrics = mTimePaint.getFontMetrics();
    }

    /**
     * 加载双语歌词
     */
    public void loadLrc(String mainLrcText, String secondLrcText) {
        LogUtil.d(TAG,"mainLrcText : "+mainLrcText+" ");
        reset();

        String[] lrc = new String[2];
        lrc[0] = mainLrcText;
        lrc[1] = secondLrcText;

        List<LrcEntry> parseList = LrcUtils.parseLrc(lrc);
        if (parseList != null && !parseList.isEmpty()) {
            mLrcEntryList.addAll(parseList);
        }

        Collections.sort(mLrcEntryList);
        initEntryList();
        invalidate();
    }

    private void reset() {
        endAnimation();
        mScroller.forceFinished(true);
        isShowTimeline = false;
        isTouching = false;
        isFling = false;
        removeCallbacks(hideTimelineRunnable);
        mLrcEntryList.clear();
        mOffset = 0;
        mCurrentLine = 0;
        invalidate();
    }

    private Runnable hideTimelineRunnable = () -> {
        if (lrcNotEmpty() && isShowTimeline) {
            isShowTimeline = false;
            smoothScrollTo(mCurrentLine, ANIMATION_DURATION);
        }
    };

    private void endAnimation() {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.end();
        }
    }

    /**
     * 传入时间刷新歌词
     */
    public void updateTime(long time) {
        if (!lrcNotEmpty()) {
            return;
        }

        runOnUi(() -> {
            int line = findShowLine(time);
            if (line != mCurrentLine) {
                mCurrentLine = line;
                if (!isShowTimeline) {
                    smoothScrollTo(line, ANIMATION_DURATION);
                } else {
                    invalidate();
                }
            }
        });
    }

    /**
     * 用二分查找 对应的时间应该显示第几行歌词
     */
    private int findShowLine(long time) {
        int left = 0;
        int right = mLrcEntryList.size();
        while (left <= right) {
            int middle = (left + right) / 2;
            long middleTime = mLrcEntryList.get(middle).getTime();

            if (time < middleTime) {
                right = middle - 1;
            } else {
                if (middle + 1 >= mLrcEntryList.size() || time < mLrcEntryList.get(middle + 1).getTime()) {
                    return middle;
                }

                left = middle + 1;
            }
        }

        return 0;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //一有变化就重新布局子View
        if (changed) {
            initPlayDrawable();
            initEntryList();
            if (lrcNotEmpty()) {
                smoothScrollTo(mCurrentLine, 0);
            }
        }
    }

    /**
     * 滑动到指定的行
     */
    private void smoothScrollTo(int line, int duration) {
//        if (!isShowTimeline) {
//        mCurrentLine = line;
//        }
        float offset = getOffset(line);
        endAnimation();

        mAnimator = ValueAnimator.ofFloat(mOffset, offset);
        mAnimator.setDuration(duration);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(animation -> {
            mOffset = (float) animation.getAnimatedValue();
            invalidate();
        });
        LrcUtils.resetDurationScale();
        mAnimator.start();
    }

    /**
     * 获取 改行歌词到顶部的 offset
     */
    private float getOffset(int line) {
        if (mLrcEntryList.get(line).getOffset() == Float.MIN_VALUE) {
            float offset = getHeight() / 2;
            for (int i = 1; i <= line; i++) {
                offset -= (mLrcEntryList.get(i - 1).getHeight() + mLrcEntryList.get(i).getHeight()) / 2 + mDividerHeight;
            }
            mLrcEntryList.get(line).setOffset(offset);
        }
        return mLrcEntryList.get(line).getOffset();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centerY = getHeight() / 2;

        //没有歌词 则显示默认
        if (!lrcNotEmpty()) {
            mLrcPaint.setColor(mCurrentTextColor);
            @SuppressLint("DrawAllocation")
            StaticLayout staticLayout = new StaticLayout(mDefaultLabel, mLrcPaint, (int) getLrcWidth(), Layout.Alignment.ALIGN_CENTER, 1f, 0f, false);
            drawText(canvas, staticLayout, centerY);
        }

        int centerLine = getCenterLine();

        //画时间线、时间和播放图标
        if (isShowTimeline) {
            mPlayDrawable.draw(canvas);

            mTimePaint.setColor(mTimelineColor);
            canvas.drawLine(TIME_TEXT_WIDTH, centerY, getWidth() - TIME_TEXT_WIDTH, centerY, mTimePaint);

            mTimePaint.setColor(mTimelineTextColor);
            String timeText = LrcUtils.formatTime(mLrcEntryList.get(centerLine).getTime());
            //高度居中显示
            float timeX = getWidth() - TIME_TEXT_WIDTH / 2;
            float timeY = centerY - (mTimeFontMetrics.descent + mTimeFontMetrics.ascent) / 2;
            canvas.drawText(timeText, timeX, timeY, mTimePaint);
        }

        //画布偏移offset 位置
        canvas.translate(0, mOffset);

        float y = 0;
        mLrcPaint.setTextSize(mTextSize);
        for (int i = 0; i < mLrcEntryList.size(); i++) {
            if (i > 0) {
                y += (mLrcEntryList.get(i - 1).getHeight() + mLrcEntryList.get(i).getHeight()) / 2 + mDividerHeight;
            }
            if (i == mCurrentLine) {
                mLrcPaint.setColor(mCurrentTextColor);
            } else if (isShowTimeline && i == centerLine) {
                mLrcPaint.setColor(mTimelineTextColor);
            } else {
                mLrcPaint.setColor(mNormalTextColor);
            }
            drawText(canvas, mLrcEntryList.get(i).getStaticLayout(), y);
        }
    }

    /**
     * 获取当前在视图中央的行数
     */
    private int getCenterLine() {
        int centerLine = 0;
        float minDistance = Float.MAX_VALUE;
        for (int i = 0; i < mLrcEntryList.size(); i++) {
            if (Math.abs(mOffset - getOffset(i)) <= minDistance) {
                minDistance = Math.abs(mOffset - getOffset(i));
                centerLine = i;
            }
        }
        return centerLine;
    }

    private void drawText(Canvas canvas, StaticLayout staticLayout, float y) {
        canvas.save();
        canvas.translate(mPadding, y - staticLayout.getHeight() / 2);
        staticLayout.draw(canvas);
        canvas.restore();
    }

    private void initPlayDrawable() {
        int l = (int) ((TIME_TEXT_WIDTH - PLAY_DRAWABLE_WIDTH) / 2);
        int t = (int) (getHeight() / 2 - PLAY_DRAWABLE_WIDTH / 2);
        int r = (int) (l + PLAY_DRAWABLE_WIDTH);
        int b = (int) (t + PLAY_DRAWABLE_WIDTH);
        mPlayDrawable.setBounds(l, t, r, b);
    }

    /**
     * 在布局的时候或者 解析完歌词的时候需要初始化list里面每一句歌词
     */
    private void initEntryList() {
        if (getWidth() == 0 || !lrcNotEmpty()) {
            return;
        }
        for (LrcEntry lrcEntry : mLrcEntryList) {
            lrcEntry.init(mLrcPaint, (int) getLrcWidth(), mTextGravity);
        }
        //以view高度 1/2 为中轴线
        mOffset = getHeight() / 2f;
    }

    /**
     * 获取歌词宽度
     */
    private float getLrcWidth() {
        return getWidth() - 2 * mPadding;
    }

    /**
     * 歌词 是否 不为空
     */
    private boolean lrcNotEmpty() {
        return !mLrcEntryList.isEmpty();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_UP || event.getActionMasked() == MotionEvent.ACTION_CANCEL) {
            isTouching = false;
            if (lrcNotEmpty() && !isFling) {
                smoothScrollTo(getCenterLine(), ADJUST_DURATION);
            }
        }
        return mGestureDetector.onTouchEvent(event);
    }

    /**
     * 手势监听器
     */
    private GestureDetector.SimpleOnGestureListener mSimpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            if (lrcNotEmpty() && mListener != null) {
                mScroller.forceFinished(true);
                removeCallbacks(hideTimelineRunnable);
                isTouching = true;
                invalidate();
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (lrcNotEmpty()) {
                isShowTimeline = true;
                mOffset += -distanceY;
                //不能超出边界
                mOffset = Math.min(mOffset, getOffset(0));
                mOffset = Math.max(mOffset, getOffset(mLrcEntryList.size() - 1));
                invalidate();
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (lrcNotEmpty()) {
                //在 fling开始滑动
                mScroller.fling(0, (int) mOffset, 0, (int) velocityY, 0, 0,
                        (int) getOffset(mLrcEntryList.size() - 1), (int) getOffset(0));
                isFling = true;
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            LogUtil.d(TAG, "onSingleTapConfirmed");
            if (lrcNotEmpty() && isShowTimeline && mPlayDrawable.getBounds().contains((int) e.getX(), (int) e.getY())) {
                int centerLine = getCenterLine();
                long centerLineTime = mLrcEntryList.get(centerLine).getTime();
                //按了播放才回去更新UI
                if (mListener != null && mListener.onPlayClick(centerLineTime)) {
                    postDelayed(hideTimelineRunnable, 400);
                    mCurrentLine = centerLine;
                    invalidate();
                }
            } else {
                if (coverChangeListener != null) {
                    coverChangeListener.onCoverChange();
                    postDelayed(hideTimelineRunnable, 400);
                }
            }
            return super.onSingleTapConfirmed(e);
        }
    };

    public interface OnCoverChangeListener {
        void onCoverChange();
    }

    public void setCoverChangeListener(OnCoverChangeListener coverChangeListener) {
        this.coverChangeListener = coverChangeListener;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mOffset = mScroller.getCurrY();
            invalidate();
        }

        if (isFling && mScroller.isFinished()) {
            isFling = false;
            if (lrcNotEmpty() && !isTouching) {
                smoothScrollTo(getCenterLine(), ADJUST_DURATION);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        removeCallbacks(hideTimelineRunnable);
        super.onDetachedFromWindow();
    }

    /**
     * 主线程中运行
     */
    private void runOnUi(Runnable r) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            r.run();
        } else {
            post(r);
        }
    }

    public void setListener(OnPlayClickListener mListener) {
        this.mListener = mListener;
    }
}
