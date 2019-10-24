package com.rikkathewrold.rikkamusic.login.mvp.contract;


import com.rikkathewrold.rikkamusic.base.BaseModel;
import com.rikkathewrold.rikkamusic.base.BasePresenter;
import com.rikkathewrold.rikkamusic.base.BaseView;
import com.rikkathewrold.rikkamusic.login.bean.LoginBean;

import io.reactivex.Observable;

public interface LoginContract {
    interface View extends BaseView {
        void onLoginSuccess(LoginBean bean);

        void onLoginFail(String e);
    }

    interface Model extends BaseModel {
        Observable<LoginBean> login(String phone, String password);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void login(String phone, String password);
    }
}
