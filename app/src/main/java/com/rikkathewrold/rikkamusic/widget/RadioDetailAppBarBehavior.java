package com.rikkathewrold.rikkamusic.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rikkathewrold.rikkamusic.App;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.util.DensityUtil;

/**
 * 电台界面的
 */
public class RadioDetailAppBarBehavior extends AppBarLayout.Behavior {

    //最大放大倍数 1+ 0.8
    private static float MAX_SCALE = 0.8f;
    //放大高度为80dp
    private static final float MAX_ZOOM_HEIGHT = DensityUtil.dp2px(App.getContext(), 80);

    private ImageView mImageView;
    private TextView tvName, tvSub, tvInfo, tvHasSub;
    private RelativeLayout rlTop;
    private int mImageViewHeight;
    private int mAppbarHeight;
    private int mCurrentBottom;
    private float mScaleValue;
    private float mTotalDy;
    private boolean isAnimate;
    private ValueAnimator valueAnimator;

    public RadioDetailAppBarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, AppBarLayout abl, int layoutDirection) {
        boolean handled = super.onLayoutChild(parent, abl, layoutDirection);
        init(abl);
        return handled;
    }

    private void init(AppBarLayout abl) {
        abl.setClipChildren(false);
        mAppbarHeight = abl.getHeight();
        rlTop = abl.findViewById(R.id.rl_top);
        mImageView = abl.findViewById(R.id.iv_dj_cover);
        tvInfo = abl.findViewById(R.id.tv_info);
        tvName = abl.findViewById(R.id.tv_name);
        tvSub = abl.findViewById(R.id.tv_sub);
        tvHasSub = abl.findViewById(R.id.tv_has_sub);
        if (mImageView != null) {
            mImageViewHeight = mImageView.getHeight();
        }
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child, View directTargetChild, View target, int nestedScrollAxes, int type) {
        isAnimate = true;
        return true;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed, int type) {
        if (mImageView != null && child.getBottom() >= mAppbarHeight && dy < 0 && type == ViewCompat.TYPE_TOUCH) {
            zoomHeaderImageView(child, dy);
        } else {
            if (mImageView != null && child.getBottom() > mAppbarHeight && dy > 0 && type == ViewCompat.TYPE_TOUCH) {
                consumed[1] = dy;
                zoomHeaderImageView(child, dy);
            } else {
                if (valueAnimator == null || !valueAnimator.isRunning()) {
                    super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
                }

            }
        }
    }

    private void zoomHeaderImageView(AppBarLayout abl, int dy) {
        mTotalDy += -dy;
        mTotalDy = Math.min(mTotalDy, MAX_ZOOM_HEIGHT);
        mScaleValue = Math.max(1f, 1f + mTotalDy / MAX_ZOOM_HEIGHT * MAX_SCALE);
        ViewCompat.setScaleX(mImageView, mScaleValue);
        ViewCompat.setScaleY(mImageView, mScaleValue);
        mCurrentBottom = mAppbarHeight + (int) (mImageViewHeight / 2 * (mScaleValue - 1));
        ViewCompat.setTranslationY(rlTop, (int) (mImageViewHeight / 2 * (mScaleValue - 1)));
        ViewCompat.setTranslationY(tvName, (int) (mImageViewHeight / 2 * (mScaleValue - 1)));
        ViewCompat.setTranslationY(tvInfo, (int) (mImageViewHeight / 2 * (mScaleValue - 1)));
        ViewCompat.setTranslationY(tvSub, (int) (mImageViewHeight / 2 * (mScaleValue - 1)));
        ViewCompat.setTranslationY(tvHasSub, (int) (mImageViewHeight / 2 * (mScaleValue - 1)));
        abl.setBottom(mCurrentBottom);
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY) {
        if (velocityY > 100) {
            isAnimate = false;
        }
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout abl, View target, int type) {
        recovery(abl);
        super.onStopNestedScroll(coordinatorLayout, abl, target, type);
    }

    private void recovery(AppBarLayout abl) {
        if (mTotalDy > 0) {
            mTotalDy = 0;
            if (isAnimate) {
                valueAnimator = ValueAnimator.ofFloat(mScaleValue, 1f).setDuration(220);
                valueAnimator.addUpdateListener(animation -> {
                    float value = (float) animation.getAnimatedValue();
                    ViewCompat.setScaleX(mImageView, value);
                    ViewCompat.setScaleY(mImageView, value);
                    int bottom = (int) (mCurrentBottom - (mCurrentBottom - mAppbarHeight) * animation.getAnimatedFraction());
                    ViewCompat.setTranslationY(rlTop, bottom - mAppbarHeight);
                    ViewCompat.setTranslationY(tvName, bottom - mAppbarHeight);
                    ViewCompat.setTranslationY(tvInfo, bottom - mAppbarHeight);
                    ViewCompat.setTranslationY(tvHasSub, bottom - mAppbarHeight);
                    ViewCompat.setTranslationY(tvSub, bottom - mAppbarHeight);
                    abl.setBottom(bottom);
                });
                valueAnimator.start();
            } else {
                ViewCompat.setScaleX(mImageView, 1f);
                ViewCompat.setScaleY(mImageView, 1f);
                ViewCompat.setTranslationY(rlTop, 0);
                ViewCompat.setTranslationY(tvName, 0);
                ViewCompat.setTranslationY(tvInfo, 0);
                ViewCompat.setTranslationY(tvHasSub, 0);
                ViewCompat.setTranslationY(tvSub, 0);
                abl.setBottom(mAppbarHeight);
            }
        }
    }
}
