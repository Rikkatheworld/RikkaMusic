package com.rikkathewrold.rikkamusic.main.mvp.contract;

import com.rikkathewrold.rikkamusic.base.BaseModel;
import com.rikkathewrold.rikkamusic.base.BasePresenter;
import com.rikkathewrold.rikkamusic.base.BaseView;
import com.rikkathewrold.rikkamusic.main.bean.BannerBean;
import com.rikkathewrold.rikkamusic.main.bean.DailyRecommendBean;
import com.rikkathewrold.rikkamusic.main.bean.HighQualityPlayListBean;
import com.rikkathewrold.rikkamusic.main.bean.MainRecommendPlayListBean;
import com.rikkathewrold.rikkamusic.main.bean.PlaylistDetailBean;
import com.rikkathewrold.rikkamusic.main.bean.RecommendPlayListBean;
import com.rikkathewrold.rikkamusic.main.bean.TopListBean;
import com.rikkathewrold.rikkamusic.manager.bean.MusicCanPlayBean;

import io.reactivex.Observable;


public interface WowContract {
    interface View extends BaseView {
        void onGetBannerSuccess(BannerBean bean);

        void onGetBannerFail(String e);

        void onGetRecommendPlayListSuccess(MainRecommendPlayListBean bean);

        void onGetRecommendPlayListFail(String e);

        void onGetDailyRecommendSuccess(DailyRecommendBean bean);

        void onGetDailyRecommendFail(String e);

        void onGetTopListSuccess(TopListBean bean);

        void onGetTopListFail(String e);

        void onGetPlayListSuccess(RecommendPlayListBean bean);

        void onGetPlayListFail(String e);

        void onGetPlaylistDetailSuccess(PlaylistDetailBean bean);

        void onGetPlaylistDetailFail(String e);

        void onGetMusicCanPlaySuccess(MusicCanPlayBean bean);

        void onGetMusicCanPlayFail(String e);

        void onGetHighQualitySuccess(HighQualityPlayListBean bean);

        void onGetHighQualityFail(String e);
    }

    interface Model extends BaseModel {
        Observable<BannerBean> getBanner();

        Observable<MainRecommendPlayListBean> getRecommendPlayList();

        Observable<DailyRecommendBean> getDailyRecommend();

        Observable<TopListBean> getTopList();

        Observable<RecommendPlayListBean> getPlayList(String type, int limit);

        Observable<PlaylistDetailBean> getPlaylistDetail(long id);

        Observable<MusicCanPlayBean> getMusicCanPlay(long id);

        Observable<HighQualityPlayListBean> getHighQuality(int limit, long before);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getBanner();

        public abstract void getRecommendPlayList();

        public abstract void getDailyRecommend();

        public abstract void getTopList();

        public abstract void getPlayList(String type, int limit);

        public abstract void getPlaylistDetail(long id);

        public abstract void getMusicCanPlay(long id);

        public abstract void getHighQuality(int limit, long before);
    }
}
