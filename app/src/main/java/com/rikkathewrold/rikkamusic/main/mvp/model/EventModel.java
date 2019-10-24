package com.rikkathewrold.rikkamusic.main.mvp.model;

import com.rikkathewrold.rikkamusic.api.ApiEngine;
import com.rikkathewrold.rikkamusic.main.bean.MainEventBean;
import com.rikkathewrold.rikkamusic.main.mvp.contract.EventContract;

import io.reactivex.Observable;

public class EventModel implements EventContract.Model {

    @Override
    public Observable<MainEventBean> getMainEvent() {
        return ApiEngine.getInstance().getApiService().getMainEvent();
    }

}
