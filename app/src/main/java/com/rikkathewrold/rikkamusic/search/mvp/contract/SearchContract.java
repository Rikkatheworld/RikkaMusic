package com.rikkathewrold.rikkamusic.search.mvp.contract;

import com.rikkathewrold.rikkamusic.base.BaseModel;
import com.rikkathewrold.rikkamusic.base.BasePresenter;
import com.rikkathewrold.rikkamusic.base.BaseView;
import com.rikkathewrold.rikkamusic.search.bean.AlbumSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.FeedSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.HotSearchDetailBean;
import com.rikkathewrold.rikkamusic.search.bean.PlayListSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.RadioSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.SingerSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.SongSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.SynthesisSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.UserSearchBean;

import io.reactivex.Observable;


public interface SearchContract {
    interface View extends BaseView {
        void onGetHotSearchDetailSuccess(HotSearchDetailBean bean);

        void onGetHotSearchDetailFail(String e);

        void onGetSongSearchSuccess(SongSearchBean bean);

        void onGetSongSearchFail(String e);

        void onGetFeedSearchSuccess(FeedSearchBean bean);

        void onGetFeedSearchFail(String e);

        void onGetSingerSearchSuccess(SingerSearchBean bean);

        void onGetSingerSearchFail(String e);

        void onGetAlbumSearchSuccess(AlbumSearchBean bean);

        void onGetAlbumSearchFail(String e);

        void onGetPlayListSearchSuccess(PlayListSearchBean bean);

        void onGetPlayListSearchFail(String e);

        void onGetRadioSearchSuccess(RadioSearchBean bean);

        void onGetRadioSearchFail(String e);

        void onGetUserSearchSuccess(UserSearchBean bean);

        void onGetUserSearchFail(String e);

        void onGetSynthesisSearchSuccess(SynthesisSearchBean bean);

        void onGetSynthesisSearchFail(String e);
    }

    interface Model extends BaseModel {
        Observable<HotSearchDetailBean> getHotSearchDetail();

        Observable<SongSearchBean> getSongSearch(String keywords, int type);

        Observable<FeedSearchBean> getFeedSearch(String keywords, int type);

        Observable<SingerSearchBean> getSingerSearch(String keywords, int type);

        Observable<AlbumSearchBean> getAlbumSearch(String keywords, int type);

        Observable<PlayListSearchBean> getPlayListSearch(String keywords, int type);

        Observable<RadioSearchBean> getRadioSearch(String keywords, int type);

        Observable<UserSearchBean> getUserSearch(String keywords, int type);

        Observable<SynthesisSearchBean> getSynthesisSearch(String keywords, int type);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getHotSearchDetail();

        public abstract void getSongSearch(String keywords, int type);

        public abstract void getFeedSearch(String keywords, int type);

        public abstract void getSingerSearch(String keywords, int type);

        public abstract void getAlbumSearch(String keywords, int type);

        public abstract void getPlayListSearch(String keywords, int type);

        public abstract void getRadioSearch(String keywords, int type);

        public abstract void getUserSearch(String keywords, int type);

        public abstract void getSynthesisSearch(String keywords, int type);
    }
}
