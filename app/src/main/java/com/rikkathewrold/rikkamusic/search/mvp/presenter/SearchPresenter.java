package com.rikkathewrold.rikkamusic.search.mvp.presenter;

import com.rikkathewrold.rikkamusic.search.bean.AlbumSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.FeedSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.HotSearchDetailBean;
import com.rikkathewrold.rikkamusic.search.bean.PlayListSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.RadioSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.SingerSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.SongSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.SynthesisSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.UserSearchBean;
import com.rikkathewrold.rikkamusic.search.mvp.contract.SearchContract;
import com.rikkathewrold.rikkamusic.search.mvp.model.SearchModel;
import com.rikkathewrold.rikkamusic.util.LogUtil;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchPresenter extends SearchContract.Presenter {
    private static final String TAG = "SearchPresenter";

    public SearchPresenter(SearchContract.View v) {
        this.mView = v;
        this.mModel = new SearchModel();
    }


    @Override
    public void getHotSearchDetail() {
        mModel.getHotSearchDetail().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HotSearchDetailBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d(TAG, "getHotSearchDetail  onSubscribe");
                    }

                    @Override
                    public void onNext(HotSearchDetailBean hotSearchDetailBean) {
                        LogUtil.d(TAG, "getHotSearchDetail onNext :" + hotSearchDetailBean);
                        mView.onGetHotSearchDetailSuccess(hotSearchDetailBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "getHotSearchDetail  onError:" + e.toString());
                        mView.onGetHotSearchDetailFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "getHotSearchDetail  onComplete");
                    }
                });
    }

    @Override
    public void getSongSearch(String keywords, int type) {
        mModel.getSongSearch(keywords, type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SongSearchBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d(TAG, "getSongSearch  onSubscribe");
                    }

                    @Override
                    public void onNext(SongSearchBean bean) {
                        LogUtil.d(TAG, "getSongSearch onNext :" + bean);
                        mView.onGetSongSearchSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "getSongSearch  onError : " + e.toString());
                        mView.onGetSongSearchFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "getSongSearch  onComplete");
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
    public void getSingerSearch(String keywords, int type) {
        mModel.getSingerSearch(keywords, type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SingerSearchBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d(TAG, "getSingerSearch onSubscribe ");
                    }

                    @Override
                    public void onNext(SingerSearchBean bean) {
                        LogUtil.d(TAG, "getSingerSearch onNext : " + bean);
                        mView.onGetSingerSearchSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "getSingerSearch onError: " + e.toString());
                        mView.onGetSingerSearchFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "getSingerSearch onComplete ");
                    }
                });
    }

    @Override
    public void getAlbumSearch(String keywords, int type) {
        mModel.getAlbumSearch(keywords, type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AlbumSearchBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d(TAG, "getAlbumSearch  onSubscribe");
                    }

                    @Override
                    public void onNext(AlbumSearchBean bean) {
                        LogUtil.d(TAG, "getAlbumSearch onNext ：" + bean);
                        mView.onGetAlbumSearchSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "getAlbumSearch onError : " + e);
                        mView.onGetAlbumSearchFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "getAlbumSearch  onComplete");
                    }
                });
    }

    @Override
    public void getPlayListSearch(String keywords, int type) {
        mModel.getPlayListSearch(keywords, type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PlayListSearchBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d(TAG, "getPlayListSearch onSubscribe");
                    }

                    @Override
                    public void onNext(PlayListSearchBean bean) {
                        LogUtil.d(TAG, "getPlayListSearch onNext : " + bean);
                        mView.onGetPlayListSearchSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "getPlayListSearch  onError :" + e);
                        mView.onGetPlayListSearchFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "getPlayListSearch onComplete");
                    }
                });
    }

    @Override
    public void getRadioSearch(String keywords, int type) {
        mModel.getRadioSearch(keywords, type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RadioSearchBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d(TAG, "getRadioSearch onSubscribe");
                    }

                    @Override
                    public void onNext(RadioSearchBean bean) {
                        LogUtil.d(TAG, "getRadioSearch : " + bean);
                        mView.onGetRadioSearchSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "getRadioSearch onError : " + e.toString());
                        mView.onGetRadioSearchFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "getRadioSearch onComplete");
                    }
                });
    }

    @Override
    public void getUserSearch(String keywords, int type) {
        mModel.getUserSearch(keywords, type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserSearchBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d(TAG, "getUserSearch  onSubscribe");
                    }

                    @Override
                    public void onNext(UserSearchBean bean) {
                        LogUtil.d(TAG, "getUserSearch onNext : " + bean);
                        mView.onGetUserSearchSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "getUserSearch  onError : " + e.toString());
                        mView.onGetUserSearchFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "getUserSearch  onComplete");
                    }
                });
    }

    @Override
    public void getSynthesisSearch(String keywords, int type) {
        mModel.getSynthesisSearch(keywords, type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SynthesisSearchBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d(TAG, "getSynthesisSearch  onSubscribe");
                    }

                    @Override
                    public void onNext(SynthesisSearchBean bean) {
                        LogUtil.d(TAG, "getSynthesisSearch  onNext");
                        mView.onGetSynthesisSearchSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "getSynthesisSearch  onError : " + e.toString());
                        mView.onGetSynthesisSearchFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "getSynthesisSearch  onComplete");
                    }
                });
    }
}
