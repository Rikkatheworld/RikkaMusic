package com.rikkathewrold.rikkamusic.dj.mvp.presenter;

import com.rikkathewrold.rikkamusic.dj.bean.DjCategoryRecommendBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjCatelistBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjDetailBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjPaygiftBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjProgramBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjRecommendBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjRecommendTypeBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjSubBean;
import com.rikkathewrold.rikkamusic.dj.mvp.contract.DjContract;
import com.rikkathewrold.rikkamusic.dj.mvp.model.DjModel;
import com.rikkathewrold.rikkamusic.util.LogUtil;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DjPresenter extends DjContract.Presenter {
    private static final String TAG = "DjPresenter";

    public DjPresenter(DjContract.View view) {
        this.mView = view;
        this.mModel = new DjModel();
    }

    @Override
    public void getDjRecommend() {
        mModel.getDjRecommend().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DjRecommendBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d(TAG, "getDjRecommend onSubscribe");
                    }

                    @Override
                    public void onNext(DjRecommendBean bean) {
                        LogUtil.d(TAG, "getDjRecommend onNext :" + bean);
                        mView.onGetDjRecommendSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "getDjRecommend onError :" + e.getLocalizedMessage());
                        mView.onGetDjRecommendFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "getDjRecommend onComplete");
                    }
                });
    }

    @Override
    public void getDjPaygift(int limit, int offset) {
        mModel.getDjPaygift(limit, offset).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DjPaygiftBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d(TAG, "getDjPaygift onSubscribe");
                    }

                    @Override
                    public void onNext(DjPaygiftBean bean) {
                        LogUtil.d(TAG, "getDjPaygift onNext : " + bean);
                        mView.onGetDjPaygiftSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "getDjPaygift onError : " + e.getLocalizedMessage());
                        mView.onGetDjPaygiftFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "getDjPaygift onComplete");
                    }
                });
    }

    @Override
    public void getDjRecommendType(int type) {
        mModel.getDjRecommendType(type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DjRecommendTypeBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d(TAG, "getDjRecommendType onSubscribe");
                    }

                    @Override
                    public void onNext(DjRecommendTypeBean bean) {
                        LogUtil.d(TAG, "getDjRecommendType onNext :" + bean);
                        mView.onGetDjRecommendTypeSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "getDjRecommendType onError :" + e.getLocalizedMessage());
                        mView.onGetDjRecommendTypeFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "getDjRecommendType onComplete");
                    }
                });
    }

    @Override
    public void getDjCategoryRecommend() {
        mModel.getDjCategoryRecommend().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DjCategoryRecommendBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d(TAG, "getDjCategoryRecommend onSubscribe");
                    }

                    @Override
                    public void onNext(DjCategoryRecommendBean bean) {
                        LogUtil.d(TAG, "getDjCategoryRecommend onNext:" + bean);
                        mView.onGetDjCategoryRecommendSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "getDjCategoryRecommend onError:" + e.getLocalizedMessage());
                        mView.onGetDjCategoryRecommendFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "getDjCategoryRecommend onComplete");
                    }
                });
    }

    @Override
    public void getDjCatelist() {
        mModel.getDjCatelist().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DjCatelistBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d(TAG, "getDjCatelist onSubscribe");
                    }

                    @Override
                    public void onNext(DjCatelistBean bean) {
                        LogUtil.d(TAG, "getDjCatelist onNext:" + bean);
                        mView.onGetDjCatelistSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "getDjCatelist onError:" + e.getLocalizedMessage());
                        mView.onGetDjCatelistFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "getDjCatelist onComplete");
                    }
                });
    }

    @Override
    public void subDj(long rid, int isSub) {
        mModel.subDj(rid, isSub).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DjSubBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d(TAG, "subDj onSubscribe");
                    }

                    @Override
                    public void onNext(DjSubBean bean) {
                        LogUtil.d(TAG, "subDj onNext:" + bean);
                        mView.onSubDjSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "subDj onError:" + e.getLocalizedMessage());
                        mView.onSubDjFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "subDj onComplete");
                    }
                });
    }

    @Override
    public void getDjProgram(long rid) {
        mModel.getDjProgram(rid).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DjProgramBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d(TAG, "getDjProgram onSubscribe");
                    }

                    @Override
                    public void onNext(DjProgramBean bean) {
                        LogUtil.d(TAG, "getDjProgram onNext:" + bean);
                        mView.onGetDjProgramSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "getDjProgram onError:" + e.getLocalizedMessage());
                        mView.onGetDjProgramFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "getDjProgram onComplete");
                    }
                });
    }

    @Override
    public void getDjDetail(long rid) {
        mModel.getDjDetail(rid).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DjDetailBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d(TAG, "getDjDetail onSubscribe");
                    }

                    @Override
                    public void onNext(DjDetailBean bean) {
                        LogUtil.d(TAG, "getDjDetail onNext:" + bean);
                        mView.onGetDjDetailSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "getDjDetail onError:" + e.getLocalizedMessage());
                        mView.onGetDjDetailFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "getDjDetail onComplete");
                    }
                });
    }
}
