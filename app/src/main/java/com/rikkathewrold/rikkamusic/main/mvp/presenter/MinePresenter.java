package com.rikkathewrold.rikkamusic.main.mvp.presenter;

import com.rikkathewrold.rikkamusic.main.bean.AlbumSublistBean;
import com.rikkathewrold.rikkamusic.main.bean.ArtistSublistBean;
import com.rikkathewrold.rikkamusic.main.bean.MvSublistBean;
import com.rikkathewrold.rikkamusic.main.bean.MyFmBean;
import com.rikkathewrold.rikkamusic.main.bean.PlayModeIntelligenceBean;
import com.rikkathewrold.rikkamusic.main.mvp.contract.MineContract;
import com.rikkathewrold.rikkamusic.main.mvp.model.MineModel;
import com.rikkathewrold.rikkamusic.personal.bean.UserPlaylistBean;
import com.rikkathewrold.rikkamusic.util.LogUtil;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MinePresenter extends MineContract.Presenter {
    private static final String TAG = "PersonalPresenter";

    public MinePresenter(MineContract.View v) {
        this.mView = v;
        mModel = new MineModel();
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
    public void getIntelligenceList(long id, long pid) {
        mModel.getIntelligenceList(id, pid).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PlayModeIntelligenceBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d(TAG, "getIntelligenceList onSubscribe");
                    }

                    @Override
                    public void onNext(PlayModeIntelligenceBean bean) {
                        LogUtil.d(TAG, "getIntelligenceList onNext :" + bean);
                        mView.onGetIntelligenceListSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "onError :" + e.getLocalizedMessage());
                        mView.onGetIntelligenceListFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "getIntelligenceList onComplete");
                    }
                });
    }

    @Override
    public void getMvSublist() {
        mModel.getMvSublist().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MvSublistBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d(TAG, "getMvSublist onSubscribe");
                    }

                    @Override
                    public void onNext(MvSublistBean bean) {
                        LogUtil.d(TAG, "getMvSublist onNext : " + bean);
                        mView.onGetMvSublistBeanSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "getMvSublist onError" + e.getLocalizedMessage());
                        mView.onGetMvSublistBeanFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "getMvSublist onComplete");
                    }
                });
    }

    @Override
    public void getArtistSublist() {
        mModel.getArtistSublist().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArtistSublistBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d(TAG, "getArtistSublist onSubscribe");
                    }

                    @Override
                    public void onNext(ArtistSublistBean bean) {
                        LogUtil.d(TAG, "getArtistSublist onNext : " + bean);
                        mView.onGetArtistSublistBeanSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "getArtistSublist onError" + e.getLocalizedMessage());
                        mView.onGetArtistSublistBeanFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "getArtistSublist onComplete");
                    }
                });
    }

    @Override
    public void getAlbumSublist() {
        mModel.getAlbumSublistBean().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AlbumSublistBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d(TAG, "getAlbumSublist onSubscribe");
                    }

                    @Override
                    public void onNext(AlbumSublistBean bean) {
                        LogUtil.d(TAG, "getAlbumSublist onNext : " + bean);
                        mView.onGetAlbumSublistBeanSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "getAlbumSublist onError" + e.getLocalizedMessage());
                        mView.onGetAlbumSublistBeanFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "getAlbumSublist onComplete");
                    }
                });
    }

    @Override
    public void getMyFM() {
        mModel.getMyFM().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MyFmBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d(TAG, "getMyFM onSubscribe");
                    }

                    @Override
                    public void onNext(MyFmBean bean) {
                        LogUtil.d(TAG, "getMyFM onNext:" + bean);
                        mView.onGetMyFMSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "getMyFM onError :" + e.getLocalizedMessage());
                        mView.onGetMyFMFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "getMyFM onComplete");
                    }
                });
    }
}
