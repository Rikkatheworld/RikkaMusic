package com.rikkathewrold.rikkamusic.main.mvp.presenter;

import com.rikkathewrold.rikkamusic.main.bean.BannerBean;
import com.rikkathewrold.rikkamusic.main.bean.DailyRecommendBean;
import com.rikkathewrold.rikkamusic.main.bean.HighQualityPlayListBean;
import com.rikkathewrold.rikkamusic.main.bean.MainRecommendPlayListBean;
import com.rikkathewrold.rikkamusic.main.bean.PlaylistDetailBean;
import com.rikkathewrold.rikkamusic.main.bean.RecommendPlayListBean;
import com.rikkathewrold.rikkamusic.main.bean.TopListBean;
import com.rikkathewrold.rikkamusic.main.mvp.contract.WowContract;
import com.rikkathewrold.rikkamusic.main.mvp.model.WowModel;
import com.rikkathewrold.rikkamusic.manager.bean.MusicCanPlayBean;
import com.rikkathewrold.rikkamusic.util.LogUtil;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WowPresenter extends WowContract.Presenter {
    private static final String TAG = "WowPresenter";

    public WowPresenter(WowContract.View view) {
        this.mView = view;
        this.mModel = new WowModel();
    }


    @Override
    public void getBanner() {
        mModel.getBanner().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BannerBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d(TAG, "getBanner onSubscribe");
                    }

                    @Override
                    public void onNext(BannerBean bean) {
                        LogUtil.d(TAG, "BannerBean : " + bean);
                        mView.onGetBannerSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onGetBannerFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "getBanner onComplete");
                    }
                });
    }

    @Override
    public void getRecommendPlayList() {
        mModel.getRecommendPlayList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MainRecommendPlayListBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d(TAG, "getRecommendPlayList onSubscribe");
                    }

                    @Override
                    public void onNext(MainRecommendPlayListBean recommendPlayListBean) {
                        LogUtil.d(TAG, "onNext" + recommendPlayListBean.toString());
                        mView.onGetRecommendPlayListSuccess(recommendPlayListBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "onError:" + e);
                        mView.onGetRecommendPlayListFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "getRecommendPlayList onComplete");
                    }
                });
    }

    @Override
    public void getDailyRecommend() {
        mModel.getDailyRecommend().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DailyRecommendBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d(TAG, "getDailyRecommend Subscribe");
                    }

                    @Override
                    public void onNext(DailyRecommendBean bean) {
                        LogUtil.d(TAG, "onNext" + bean);
                        mView.onGetDailyRecommendSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "onError" + e);
                        mView.onGetDailyRecommendFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "getDailyRecommend onComplete");
                    }
                });
    }

    @Override
    public void getTopList() {
        mModel.getTopList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TopListBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d(TAG, "getTopList onSubscribe");
                    }

                    @Override
                    public void onNext(TopListBean bean) {
                        LogUtil.d(TAG, "onNext : " + bean);
                        mView.onGetTopListSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, e.getMessage());
                        mView.onGetTopListFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "getTopList onComplete");
                    }
                });
    }

    @Override
    public void getPlayList(String type, int limit) {
        mModel.getPlayList(type, limit).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RecommendPlayListBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d(TAG, "getPlayList onSubscribe");
                    }

                    @Override
                    public void onNext(RecommendPlayListBean bean) {
                        LogUtil.d(TAG, "onNext : " + bean);
                        mView.onGetPlayListSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "onError : " + e);
                        mView.onGetPlayListFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "getPlayList onComplete");
                    }
                });
    }

    @Override
    public void getPlaylistDetail(long id) {
        mModel.getPlaylistDetail(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PlaylistDetailBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d(TAG, "getPlaylistDetail  onSubscribe");
                    }

                    @Override
                    public void onNext(PlaylistDetailBean playlistDetailBean) {
                        LogUtil.d(TAG, "PlaylistDetailBean : " + playlistDetailBean);
                        mView.onGetPlaylistDetailSuccess(playlistDetailBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "onError : " + e);
                        mView.onGetPlaylistDetailFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "getPlaylistDetail  onComplete");
                    }
                });
    }

    @Override
    public void getMusicCanPlay(long id) {
        LogUtil.d(TAG, "getMusicCanPlay  id = " + id);
        mModel.getMusicCanPlay(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MusicCanPlayBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d(TAG, "getMusicCanPlay : onSubscribe");
                    }

                    @Override
                    public void onNext(MusicCanPlayBean musicCanPlayBean) {
                        LogUtil.d(TAG, "getMusicCanPlay : onNext " + musicCanPlayBean);
                        mView.onGetMusicCanPlaySuccess(musicCanPlayBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "getMusicCanPlay : onError " + e);
                        mView.onGetMusicCanPlayFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "getMusicCanPlay : onComplete");
                    }
                });
    }

    @Override
    public void getHighQuality(int limit, long before) {
        mModel.getHighQuality(limit, before)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HighQualityPlayListBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(HighQualityPlayListBean bean) {
                        LogUtil.d(TAG, "onNext : " + bean);
                        mView.onGetHighQualitySuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "onError : " + e);
                        mView.onGetHighQualityFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "onComplete");
                    }
                });
    }
}
