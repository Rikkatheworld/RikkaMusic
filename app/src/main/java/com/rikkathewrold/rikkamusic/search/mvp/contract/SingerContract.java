package com.rikkathewrold.rikkamusic.search.mvp.contract;

import com.rikkathewrold.rikkamusic.base.BaseModel;
import com.rikkathewrold.rikkamusic.base.BasePresenter;
import com.rikkathewrold.rikkamusic.base.BaseView;
import com.rikkathewrold.rikkamusic.search.bean.FeedSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.SimiSingerBean;
import com.rikkathewrold.rikkamusic.search.bean.SingerAblumSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.SingerDescriptionBean;
import com.rikkathewrold.rikkamusic.search.bean.SingerSongSearchBean;

import io.reactivex.Observable;

public interface SingerContract {
    interface View extends BaseView {
        void onGetSingerHotSongSuccess(SingerSongSearchBean bean);

        void onGetSingerHotSongFail(String e);

        void onGetSingerAlbumSuccess(SingerAblumSearchBean bean);

        void onGetSingerAlbumFail(String e);

        void onGetFeedSearchSuccess(FeedSearchBean bean);

        void onGetFeedSearchFail(String e);

        void onGetSingerDescSuccess(SingerDescriptionBean bean);

        void onGetSingerDescFail(String e);

        void onGetSimiSingerSuccess(SimiSingerBean bean);

        void onGetSimiSingerFail(String e);
    }

    interface Model extends BaseModel {
        Observable<SingerSongSearchBean> getSingerHotSong(long id);

        Observable<SingerAblumSearchBean> getSingerAlbum(long id);

        Observable<FeedSearchBean> getFeedSearch(String keywords, int type);

        Observable<SingerDescriptionBean> getSingerDesc(long id);

        Observable<SimiSingerBean> getSimiSinger(long id);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getSingerHotSong(long id);

        public abstract void getSingerAlbum(long id);

        public abstract void getFeedSearch(String keywords, int type);

        public abstract void getSingerDesc(long id);

        public abstract void getSimiSinger(long id);
    }
}
