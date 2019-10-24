package com.rikkathewrold.rikkamusic.personal.mvp.contract;

import com.rikkathewrold.rikkamusic.base.BaseModel;
import com.rikkathewrold.rikkamusic.base.BasePresenter;
import com.rikkathewrold.rikkamusic.base.BaseView;
import com.rikkathewrold.rikkamusic.personal.bean.UserDetailBean;
import com.rikkathewrold.rikkamusic.personal.bean.UserEventBean;
import com.rikkathewrold.rikkamusic.personal.bean.UserPlaylistBean;

import io.reactivex.Observable;


public interface PersonalContract {
    interface View extends BaseView {
        void onGetUserPlaylistSuccess(UserPlaylistBean bean);

        void onGetUserPlaylistFail(String e);

        void onGetUserEventSuccess(UserEventBean bean);

        void onGetUserEventFail(String e);

        void onGetUserDetailSuccess(UserDetailBean bean);

        void onGetUserDetailFail(String e);
    }

    interface Model extends BaseModel {
        Observable<UserPlaylistBean> getUserPlaylist(long uid);

        Observable<UserEventBean> getUserEvent(long uid, int limit, long lasttime);

        Observable<UserDetailBean> getUserDetail(long uid);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getUserPlaylist(long uid);

        public abstract void getUserEvent(long uid, int limit, long lasttime);

        public abstract void getUserDetail(long uid);
    }
}
