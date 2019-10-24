package com.rikkathewrold.rikkamusic.personal.mvp.presenter;

import com.rikkathewrold.rikkamusic.personal.bean.UserDetailBean;
import com.rikkathewrold.rikkamusic.personal.bean.UserEventBean;
import com.rikkathewrold.rikkamusic.personal.bean.UserPlaylistBean;
import com.rikkathewrold.rikkamusic.personal.mvp.contract.PersonalContract;
import com.rikkathewrold.rikkamusic.personal.mvp.model.PersonalModel;
import com.rikkathewrold.rikkamusic.util.LogUtil;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PersonalPresenter extends PersonalContract.Presenter {
    private static final String TAG = "PersonalPresenter";

    public PersonalPresenter(PersonalContract.View v) {
        this.mView = v;
        mModel = new PersonalModel();
    }


    @Override
    public void getUserPlaylist(long uid) {
        mModel.getUserPlaylist(uid).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserPlaylistBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d(TAG, "getUserPlaylist  onSubscribe");
                    }

                    @Override
                    public void onNext(UserPlaylistBean userPlaylistBean) {
                        LogUtil.d(TAG, "getUserPlaylist  onNext : " + userPlaylistBean);
                        mView.onGetUserPlaylistSuccess(userPlaylistBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "getUserPlaylist  onError : " + e.toString());
                        mView.onGetUserPlaylistFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "getUserPlaylist  onComplete");
                    }
                });
    }

    @Override
    public void getUserEvent(long uid, int limit, long lasttime) {
        mModel.getUserEvent(uid, limit, lasttime).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserEventBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d(TAG, "getUserEvent  onSubscribe");
                    }

                    @Override
                    public void onNext(UserEventBean userEventBean) {
                        LogUtil.d(TAG, "getUserEvent  onNext :" + userEventBean);
                        mView.onGetUserEventSuccess(userEventBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "getUserEvent  onError : " + e.toString());
                        mView.onGetUserEventFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "getUserEvent  onSubscribe");
                    }
                });
    }

    @Override
    public void getUserDetail(long uid) {
        mModel.getUserDetail(uid).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserDetailBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d(TAG, "getUserDetail : onSubscribe");
                    }

                    @Override
                    public void onNext(UserDetailBean userDetailBean) {
                        LogUtil.d(TAG, "getUserDetail : onNext : " + userDetailBean);
                        mView.onGetUserDetailSuccess(userDetailBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "getUserDetail : onError" + e.toString());
                        mView.onGetUserDetailFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "getUserDetail : onComplete");
                    }
                });
    }
}
