package com.rikkathewrold.rikkamusic.main.mvp.contract;

import com.rikkathewrold.rikkamusic.base.BaseModel;
import com.rikkathewrold.rikkamusic.base.BasePresenter;
import com.rikkathewrold.rikkamusic.base.BaseView;
import com.rikkathewrold.rikkamusic.main.bean.LikeListBean;
import com.rikkathewrold.rikkamusic.main.bean.LogoutBean;

import io.reactivex.Observable;


public interface MainContract {
    interface View extends BaseView {
        void onLogoutSuccess();

        void onLogoutFail(String e);

        void onGetLikeListSuccess(LikeListBean bean);

        void onGetLikeListFail(String e);
    }

    interface Model extends BaseModel {
        Observable<LogoutBean> logout();

        Observable<LikeListBean> getLikeList(long uid);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void logout();

        public abstract void getLikeList(long uid);
    }
}
