package com.rikkathewrold.rikkamusic.main.mvp.model;

import com.rikkathewrold.rikkamusic.api.ApiEngine;
import com.rikkathewrold.rikkamusic.main.bean.LikeListBean;
import com.rikkathewrold.rikkamusic.main.bean.LogoutBean;
import com.rikkathewrold.rikkamusic.main.mvp.contract.MainContract;

import io.reactivex.Observable;


public class MainModel implements MainContract.Model {

    @Override
    public Observable<LogoutBean> logout() {
        return ApiEngine.getInstance().getApiService().logout();
    }

    @Override
    public Observable<LikeListBean> getLikeList(long uid) {
        return ApiEngine.getInstance().getApiService().getLikeList(uid);
    }
}
