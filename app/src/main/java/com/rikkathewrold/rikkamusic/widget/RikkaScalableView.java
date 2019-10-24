package com.rikkathewrold.rikkamusic.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.OverScroller;

import com.rikkathewrold.rikkamusic.util.LogUtil;

/**
 * 功能：
 * 双击放大（通过DoubleTap实现）
 * 双指缩放（通过ScaleGestureDetector实现）
 */
public class RikkaScalableView extends View {
    private static final String TAG = "RikkaScalableView";

    //Bitmap图片
    private Bitmap mBitmap;

    //画笔
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    //初始化bitmap偏移量X、Y值,让bitmap居中的偏移量
    float originalOffsetX, originalOffsetY;

    //偏移量 X、Y值
    float offsetX, offsetY;

    //旧的放大系数,和currentScale对应，用于放大前保存当前放大系数
    float oldScale;

    //放大倍数、缩小倍数
    float bigScale, smallScale;

    //放大的倍数还要再大个1.5倍
    private float bigScaleMore = 1.5f;

    //用currentScale表示当前放大倍数,而且之后用到的缩放、动画的差值都是这个以这个值为标准
    private float currentScale;

    //当前是否是放大状态
    private boolean isBig = false;

    //放大和缩小的动画，因为缩小动画就是放大动画的镜像，所以可以直接用放大动画的reverse来做
    private ObjectAnimator bigAnimator, smallAnimator;

    //手势缩放对象
    private GestureDetectorCompat gestureDetector;

    //自定义手势监听器
    private RikkaGestureListener rikkaGestureListener = new RikkaGestureListener();

    //手势缩放对象
    private ScaleGestureDetector scaleDetector;

    //自定义手势缩放监听器
    private RikkaScaleListener rikkaScaleListener = new RikkaScaleListener();

    //用OverScroller去计算滑动值,可以设置回弹动画
    private OverScroller scroller;

    //postOnAnimation里的Runnable
    private RikkaRunnable rikkaRunnable;

    public RikkaScalableView(Context context) {
        this(context, null);
    }

    public RikkaScalableView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RikkaScalableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        gestureDetector = new GestureDetectorCompat(context, rikkaGestureListener);
        scaleDetector = new ScaleGestureDetector(context, rikkaScaleListener);
        scroller = new OverScroller(context);
        rikkaRunnable = new RikkaRunnable();
    }

    //onSizeChanged表示view已经初始化完了，我们在这里初始化一些比例
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        //scalingFraction为放大倍数的百分比
        float scalingFraction = (currentScale - smallScale) / (bigScale - smallScale);
        canvas.translate(offsetX * scalingFraction, offsetY * scalingFraction);
        //这里用scale()方法来控制画布放大还是缩小
        canvas.scale(currentScale, currentScale, getWidth() / 2f, getHeight() / 2f);
        LogUtil.d(TAG, "drawBitmap :" + mBitmap);
        if(mBitmap != null) {
            canvas.drawBitmap(mBitmap, originalOffsetX, originalOffsetY, mPaint);
        }
    }

    //根据缩放或者移动进行判断
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean touchEvent = scaleDetector.onTouchEvent(event);
        if (!scaleDetector.isInProgress()) {
            //如果目前正在使用双指滑动，则使用缩放的监听
            touchEvent = gestureDetector.onTouchEvent(event);
        }
        return touchEvent;
    }

    public float getCurrentScale() {
        return currentScale;
    }

    public void setCurrentScale(float currentScale) {
        this.currentScale = currentScale;
        //变化的时候要重绘
        invalidate();
    }

    //放大动画
    public ObjectAnimator getBigAnimator() {
        if (bigAnimator == null) {
            //懒加载缩放动画，用currentScale缩小到放大,值域在smallScale -> bigScale之间
            bigAnimator = ObjectAnimator.ofFloat(this, "currentScale", currentScale, bigScale);
        }
        //因为每次的currentScale都会变化，每次拿都要重新设置一遍currentScale
        bigAnimator.setFloatValues(currentScale, bigScale);
        return bigAnimator;
    }

    //缩小动画
    public ObjectAnimator getSmallAnimator() {
        if (smallAnimator == null) {
            //懒加载缩放动画，用currentScale缩小到放大,值域在smallScale -> bigScale之间
            smallAnimator = ObjectAnimator.ofFloat(this, "currentScale", currentScale, smallScale);
            smallAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    //每次动画结束的时候要将offsetX/Y置为初始值
                    offsetX = 0;
                    offsetY = 0;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        smallAnimator.setFloatValues(currentScale, smallScale);
        return smallAnimator;
    }

    public void setImage(Bitmap resource) {
        mBitmap = resource;

        //计算初始偏移量，让图片居中显示
        //偏移是以bitmap的左上角为点
        originalOffsetX = (getWidth() - mBitmap.getWidth()) / 2;
        originalOffsetY = (getHeight() - mBitmap.getHeight()) / 2;

        //计算bigScale，smallScale
        //smallScale是初始状态，要求图片：（1）如果图片比例宽大于高的话就是左右贴屏幕的边，（2）如果图片比例高大于宽的话就要上下贴屏幕的边
        //bigScale是放大后的状态，要求图片：（1）如果图片比例宽大于高的话就是上下贴屏幕的边，（2）如果图片比例高大于宽的话就要左右贴屏幕的边
        if ((float) mBitmap.getWidth() / mBitmap.getHeight() > (float) getWidth() / getHeight()) {
            //表示当前图片比例宽大于高
            smallScale = (float) getWidth() / mBitmap.getWidth();
            bigScale = (float) getHeight() / mBitmap.getHeight() * bigScaleMore;
        } else {
            //表示当前图片比例高大于宽
            smallScale = (float) getHeight() / mBitmap.getHeight();
            bigScale = (float) getWidth() / mBitmap.getWidth() * bigScaleMore;
        }
        LogUtil.d(TAG, "width:" + getWidth() + " getHeight:" + getHeight() + " bitmapWidth:" + mBitmap.getWidth() + " bitmapHeight:" + mBitmap.getHeight());
        //初始放大倍数等于smallScale(最小)
        currentScale = smallScale;
        invalidate();
    }

    //继承自SimpleOnGestureListener，这里面做了所有的单手势监听
    class RikkaGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        //双击放大、缩小
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            isBig = !isBig;
            if (isBig && currentScale < bigScale) {
                //记录双击的位置，在这个点上放大
                offsetX = (e.getX() - getWidth() / 2) - ((e.getX() - getWidth() / 2) * bigScale / smallScale);
                offsetY = (e.getY() - getHeight() / 2) - ((e.getY() - getHeight() / 2) * bigScale / smallScale);
                notOutBound();
                //如果当前是放大状态，就做放大动画
                getBigAnimator().start();
            } else {
                //如果是缩小状态，就做缩小动画
                getSmallAnimator().start();
            }
            return false;
        }

        //滑动的时候通过 dX，dY去计算 偏移值offsetX和offsetY
        //而且只有在放大的状态才能滑动，不然缩小状态滑动会超出边界
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            offsetX -= distanceX;
            offsetY -= distanceY;
            notOutBound();
            invalidate();
            return false;
        }

        //惯性滑动，让图片滑动不要那么的僵硬，使用OverScroll去计算滑动值，postOnAnimation去更新滑动动画，滑动速度由比例决定
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            scroller.fling((int) offsetX, (int) offsetY, (int) velocityX, (int) velocityY,
                    (int) -(mBitmap.getWidth() * bigScale - getWidth()) / 2,
                    (int) (mBitmap.getWidth() * bigScale - getWidth()) / 2,
                    (int) -(mBitmap.getHeight() * bigScale - getHeight()) / 2,
                    (int) (mBitmap.getHeight() * bigScale - getHeight()) / 2);
            postOnAnimation(rikkaRunnable);
            return false;
        }
    }

    //offsetX、offsetY不能超出边界
    private void notOutBound() {
        offsetX = Math.max(offsetX, -((mBitmap.getWidth() * bigScale - getWidth()) / 2f));
        offsetX = Math.min(offsetX, (mBitmap.getWidth() * bigScale - getWidth()) / 2f);
        offsetY = Math.max(offsetY, -((mBitmap.getHeight() * bigScale - getHeight()) / 2f));
        offsetY = Math.min(offsetY, (mBitmap.getHeight() * bigScale - getHeight()) / 2f);
    }

    //自定义的缩放监听器
    class RikkaScaleListener implements ScaleGestureDetector.OnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            currentScale = oldScale * detector.getScaleFactor();
            currentScale = Math.max(smallScale, currentScale);
            currentScale = Math.min(currentScale, bigScale * 2);
            invalidate();
            return false;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            oldScale = currentScale;
            //这里一定要设置为true，它和down一样
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {

        }
    }

    class RikkaRunnable implements Runnable {
        @Override
        public void run() {
            if (scroller.computeScrollOffset()) {
                offsetX = scroller.getCurrX();
                offsetY = scroller.getCurrY();
                invalidate();
                postOnAnimation(rikkaRunnable);
            }
        }
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }
}
