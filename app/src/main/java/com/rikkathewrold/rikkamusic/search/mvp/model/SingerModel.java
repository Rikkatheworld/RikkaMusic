package com.rikkathewrold.rikkamusic.search.mvp.model;

import com.rikkathewrold.rikkamusic.api.ApiEngine;
import com.rikkathewrold.rikkamusic.search.bean.FeedSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.SimiSingerBean;
import com.rikkathewrold.rikkamusic.search.bean.SingerAblumSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.SingerDescriptionBean;
import com.rikkathewrold.rikkamusic.search.bean.SingerSongSearchBean;
import com.rikkathewrold.rikkamusic.search.mvp.contract.SingerContract;

import io.reactivex.Observable;

public class SingerModel implements SingerContract.Model {
    @Override
    public Observable<SingerSongSearchBean> getSingerHotSong(long id) {
        return ApiEngine.getInstance().getApiService().getSingerHotSong(id);
    }

    @Override
    public Observable<SingerAblumSearchBean> getSingerAlbum(long id) {
        return ApiEngine.getInstance().getApiService().getSingerAlbum(id);
    }

    @Override
    public Observable<FeedSearchBean> getFeedSearch(String keywords, int type) {
        return ApiEngine.getInstance().getApiService().getFeedSearch(keywords, type);
    }

    @Override
    public Observable<SingerDescriptionBean> getSingerDesc(long id) {
        return ApiEngine.getInstance().getApiService().getSingerDesc(id);
    }

    @Override
    public Observable<SimiSingerBean> getSimiSinger(long id) {
        return ApiEngine.getInstance().getApiService().getSimiSinger(id);
    }
}
