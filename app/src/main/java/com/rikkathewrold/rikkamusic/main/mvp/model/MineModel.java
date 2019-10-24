package com.rikkathewrold.rikkamusic.main.mvp.model;

import com.rikkathewrold.rikkamusic.api.ApiEngine;
import com.rikkathewrold.rikkamusic.main.bean.AlbumSublistBean;
import com.rikkathewrold.rikkamusic.main.bean.ArtistSublistBean;
import com.rikkathewrold.rikkamusic.main.bean.MvSublistBean;
import com.rikkathewrold.rikkamusic.main.bean.MyFmBean;
import com.rikkathewrold.rikkamusic.main.bean.PlayModeIntelligenceBean;
import com.rikkathewrold.rikkamusic.main.mvp.contract.MineContract;
import com.rikkathewrold.rikkamusic.personal.bean.UserPlaylistBean;

import io.reactivex.Observable;

public class MineModel implements MineContract.Model {
    @Override
    public Observable<UserPlaylistBean> getUserPlaylist(long uid) {
        return ApiEngine.getInstance().getApiService().getUserPlaylist(uid);
    }

    @Override
    public Observable<PlayModeIntelligenceBean> getIntelligenceList(long id, long pid) {
        return ApiEngine.getInstance().getApiService().getIntelligenceList(id, pid);
    }

    @Override
    public Observable<MvSublistBean> getMvSublist() {
        return ApiEngine.getInstance().getApiService().getMvSublist();
    }

    @Override
    public Observable<ArtistSublistBean> getArtistSublist() {
        return ApiEngine.getInstance().getApiService().getArtistSublist();
    }

    @Override
    public Observable<AlbumSublistBean> getAlbumSublistBean() {
        return ApiEngine.getInstance().getApiService().getAlbumSublist();
    }

    @Override
    public Observable<MyFmBean> getMyFM() {
        return ApiEngine.getInstance().getApiService().getMyFm();
    }

}
