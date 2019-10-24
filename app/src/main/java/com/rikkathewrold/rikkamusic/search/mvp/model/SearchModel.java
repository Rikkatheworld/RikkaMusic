package com.rikkathewrold.rikkamusic.search.mvp.model;

import com.rikkathewrold.rikkamusic.api.ApiEngine;
import com.rikkathewrold.rikkamusic.search.bean.AlbumSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.FeedSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.HotSearchDetailBean;
import com.rikkathewrold.rikkamusic.search.bean.PlayListSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.RadioSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.SingerSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.SongSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.SynthesisSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.UserSearchBean;
import com.rikkathewrold.rikkamusic.search.mvp.contract.SearchContract;

import io.reactivex.Observable;

public class SearchModel implements SearchContract.Model {
    @Override
    public Observable<HotSearchDetailBean> getHotSearchDetail() {
        return ApiEngine.getInstance().getApiService().getSearchHotDetail();
    }

    @Override
    public Observable<SongSearchBean> getSongSearch(String keywords, int type) {
        return ApiEngine.getInstance().getApiService().getSongSearch(keywords, type);
    }

    @Override
    public Observable<FeedSearchBean> getFeedSearch(String keywords, int type) {
        return ApiEngine.getInstance().getApiService().getFeedSearch(keywords, type);
    }

    @Override
    public Observable<SingerSearchBean> getSingerSearch(String keywords, int type) {
        return ApiEngine.getInstance().getApiService().getSingerSearch(keywords, type);
    }

    @Override
    public Observable<AlbumSearchBean> getAlbumSearch(String keywords, int type) {
        return ApiEngine.getInstance().getApiService().getAlbumSearch(keywords, type);
    }

    @Override
    public Observable<PlayListSearchBean> getPlayListSearch(String keywords, int type) {
        return ApiEngine.getInstance().getApiService().getPlayListSearch(keywords, type);
    }

    @Override
    public Observable<RadioSearchBean> getRadioSearch(String keywords, int type) {
        return ApiEngine.getInstance().getApiService().getRadioSearch(keywords, type);
    }

    @Override
    public Observable<UserSearchBean> getUserSearch(String keywords, int type) {
        return ApiEngine.getInstance().getApiService().getUserSearch(keywords, type);
    }

    @Override
    public Observable<SynthesisSearchBean> getSynthesisSearch(String keywords, int type) {
        return ApiEngine.getInstance().getApiService().getSynthesisSearch(keywords, type);
    }

}
