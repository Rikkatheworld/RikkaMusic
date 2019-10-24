package com.rikkathewrold.rikkamusic.personal.mvp.model;

import com.rikkathewrold.rikkamusic.api.ApiEngine;
import com.rikkathewrold.rikkamusic.personal.bean.UserDetailBean;
import com.rikkathewrold.rikkamusic.personal.bean.UserEventBean;
import com.rikkathewrold.rikkamusic.personal.bean.UserPlaylistBean;
import com.rikkathewrold.rikkamusic.personal.mvp.contract.PersonalContract;

import io.reactivex.Observable;

public class PersonalModel implements PersonalContract.Model {

    @Override
    public Observable<UserPlaylistBean> getUserPlaylist(long uid) {
        return ApiEngine.getInstance().getApiService().getUserPlaylist(uid);
    }

    @Override
    public Observable<UserEventBean> getUserEvent(long uid, int limit, long lasttime) {
        return ApiEngine.getInstance().getApiService().getUserEvent(uid, limit, lasttime);
    }

    @Override
    public Observable<UserDetailBean> getUserDetail(long uid) {
        return ApiEngine.getInstance().getApiService().getUserDetail(uid);
    }
}
