package com.rikkathewrold.rikkamusic.search.mvp.presenter;

import com.rikkathewrold.rikkamusic.search.bean.FeedSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.SimiSingerBean;
import com.rikkathewrold.rikkamusic.search.bean.SingerAblumSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.SingerDescriptionBean;
import com.rikkathewrold.rikkamusic.search.bean.SingerSongSearchBean;
import com.rikkathewrold.rikkamusic.search.mvp.contract.SingerContract;
import com.rikkathewrold.rikkamusic.search.mvp.model.SingerModel;
import com.rikkathewrold.rikkamusic.util.LogUtil;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SingerPresenter extends SingerContract.Presenter {
    private static final String TAG = "SingerPresenter";

    public SingerPresenter(SingerContract.View v) {
        this.mView = v;
        this.mModel = new SingerModel();
    }

    @Override
    public void getSingerHotSong(long id) {
        mModel.getSingerHotSong(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SingerSongSearchBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d(TAG, "getSingerHotSong  onSubscribe");
                    }

                    @Override
                    public void onNext(SingerSongSearchBean bean) {
                        LogUtil.d(TAG, "getSingerHotSong  onNext : " + bean);
                        mView.onGetSingerHotSongSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "getSingerHotSong  onError:" + e.toString());
                        mView.onGetSingerHotSongFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "getSingerHotSong  onComplete");
                    }
                });
    }

    @Override
    public void getSingerAlbum(long id) {
        mModel.getSingerAlbum(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SingerAblumSearchBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d(TAG, "getSingerAlbum  onSubscribe");
                    }

                    @Override
                    public void onNext(SingerAblumSearchBean bean) {
                        LogUtil.d(TAG, "getSingerAlbum  onNext:" + bean);
                        mView.onGetSingerAlbumSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "getSingerAlbum  onError :" + e);
                        mView.onGetSingerAlbumFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "getSingerAlbum  onComplete");
                    }
                });
    }

    @Override
    public void getFeedSearch(String keywords, int type) {
        mModel.getFeedSearch(keywords, type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FeedSearchBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d(TAG, "getFeedSearch  onSubscribe");
                    }

                    @Override
                    public void onNext(FeedSearchBean feedSearchBean) {
                        LogUtil.d(TAG, "getFeedSearch  onNext : " + feedSearchBean);
                        mView.onGetFeedSearchSuccess(feedSearchBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "getFeedSearch  onError : " + e.toString());
                        mView.onGetFeedSearchFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "getFeedSearch  onComplete");
                    }
                });
    }

    @Override
    public void getSingerDesc(long id) {
        mModel.getSingerDesc(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SingerDescriptionBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d(TAG, "getSingerDesc  onSubscribe");
                    }

                    @Override
                    public void onNext(SingerDescriptionBean bean) {
                        LogUtil.d(TAG, "getSingerDesc onNext:" + bean);
                        mView.onGetSingerDescSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "getSingerDesc  onError:" + e);
                        mView.onGetSingerDescFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "getSingerDesc  onComplete");
                    }
                });
    }

    @Override
    public void getSimiSinger(long id) {
        mModel.getSimiSinger(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SimiSingerBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d(TAG, "getSimiSinger  onSubscribe");
                    }

                    @Override
                    public void onNext(SimiSingerBean bean) {
                        LogUtil.d(TAG, "getSimiSinger onNext:" + bean);
                        mView.onGetSimiSingerSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "getSimiSinger  onError");
                        mView.onGetSimiSingerFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "getSimiSinger  onComplete");
                    }
                });
    }
}
