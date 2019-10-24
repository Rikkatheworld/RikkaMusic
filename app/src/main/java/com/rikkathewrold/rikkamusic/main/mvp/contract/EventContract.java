package com.rikkathewrold.rikkamusic.main.mvp.contract;

import com.rikkathewrold.rikkamusic.base.BaseModel;
import com.rikkathewrold.rikkamusic.base.BasePresenter;
import com.rikkathewrold.rikkamusic.base.BaseView;
import com.rikkathewrold.rikkamusic.main.bean.MainEventBean;

import io.reactivex.Observable;

public interface EventContract {
    interface View extends BaseView {
        void onGetMainEventSuccess(MainEventBean bean);

        void onGetMainEventFail(String e);

    }

    interface Model extends BaseModel {
        Observable<MainEventBean> getMainEvent();

    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getMainEvent();

    }
}
