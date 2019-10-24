package com.rikkathewrold.rikkamusic.personal.mvp.view;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseActivity;
import com.rikkathewrold.rikkamusic.personal.bean.UserDetailBean;
import com.rikkathewrold.rikkamusic.personal.bean.UserEventBean;
import com.rikkathewrold.rikkamusic.personal.bean.UserPlaylistBean;
import com.rikkathewrold.rikkamusic.personal.mvp.contract.PersonalContract;
import com.rikkathewrold.rikkamusic.personal.mvp.presenter.PersonalPresenter;
import com.rikkathewrold.rikkamusic.util.LogUtil;
import com.rikkathewrold.rikkamusic.widget.RikkaScalableView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 图片查看界面,使用了ScalableView
 */
public class PictureCheckActivity extends BaseActivity<PersonalPresenter> implements PersonalContract.View {
    private static final String TAG = "PictureCheckActivity";

    public static final String PIC_URL = "picUrl";

    @BindView(R.id.iv_avatar)
    RikkaScalableView ivAvatar;

    private String avatarUrl;
    private Bitmap mBitmap;

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_avatar_check);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    @Override
    protected PersonalPresenter onCreatePresenter() {
        return new PersonalPresenter(this);
    }

    @Override
    protected void initModule() {
        ButterKnife.bind(this);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initData() {
        if (getIntent().getStringExtra(PIC_URL) != null) {
            avatarUrl = getIntent().getStringExtra(PIC_URL);
            Glide.with(this)
                    .asBitmap()
                    .load(avatarUrl)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            LogUtil.d(TAG, "resource : " + resource);
                            mBitmap = resource;
                            ivAvatar.setImage(mBitmap);
                        }
                    });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new Thread(() -> Glide.get(PictureCheckActivity.this).clearDiskCache()).start();
        Glide.get(PictureCheckActivity.this).clearMemory();
    }


    @Override
    public void onGetUserPlaylistSuccess(UserPlaylistBean bean) {

    }

    @Override
    public void onGetUserPlaylistFail(String e) {

    }

    @Override
    public void onGetUserEventSuccess(UserEventBean bean) {

    }

    @Override
    public void onGetUserEventFail(String e) {

    }

    @Override
    public void onGetUserDetailSuccess(UserDetailBean bean) {

    }

    @Override
    public void onGetUserDetailFail(String e) {

    }
}
