package com.rikkathewrold.rikkamusic.main.mvp.presenter;

import com.rikkathewrold.rikkamusic.main.bean.MainEventBean;
import com.rikkathewrold.rikkamusic.main.mvp.contract.EventContract;
import com.rikkathewrold.rikkamusic.main.mvp.model.EventModel;
import com.rikkathewrold.rikkamusic.util.LogUtil;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EventPresenter extends EventContract.Presenter {
    private static final String TAG = "EventPresenter";

    public EventPresenter(EventContract.View view) {
        this.mView = view;
        this.mModel = new EventModel();
    }

    @Override
    public void getMainEvent() {
        mModel.getMainEvent().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MainEventBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(MainEventBean bean) {
                        LogUtil.d(TAG, "onNext :" + bean);
                        mView.onGetMainEventSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "onError :" + e.getLocalizedMessage());
                        mView.onGetMainEventFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "onComplete");
                    }
                });
    }

}
