package com.rikkathewrold.rikkamusic.dj.mvp.contract;

import com.rikkathewrold.rikkamusic.base.BaseModel;
import com.rikkathewrold.rikkamusic.base.BasePresenter;
import com.rikkathewrold.rikkamusic.base.BaseView;
import com.rikkathewrold.rikkamusic.dj.bean.DjCategoryRecommendBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjCatelistBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjDetailBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjPaygiftBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjProgramBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjRecommendBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjRecommendTypeBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjSubBean;

import io.reactivex.Observable;

public interface DjContract {
    interface View extends BaseView {
        void onGetDjRecommendSuccess(DjRecommendBean bean);

        void onGetDjRecommendFail(String e);

        void onGetDjPaygiftSuccess(DjPaygiftBean bean);

        void onGetDjPaygiftFail(String e);

        void onGetDjRecommendTypeSuccess(DjRecommendTypeBean bean);

        void onGetDjRecommendTypeFail(String e);

        void onGetDjCategoryRecommendSuccess(DjCategoryRecommendBean bean);

        void onGetDjCategoryRecommendFail(String e);

        void onGetDjCatelistSuccess(DjCatelistBean bean);

        void onGetDjCatelistFail(String e);

        void onSubDjSuccess(DjSubBean bean);

        void onSubDjFail(String e);

        void onGetDjProgramSuccess(DjProgramBean bean);

        void onGetDjProgramFail(String e);

        void onGetDjDetailSuccess(DjDetailBean bean);

        void onGetDjDetailFail(String e);
    }

    interface Model extends BaseModel {
        Observable<DjRecommendBean> getDjRecommend();

        Observable<DjPaygiftBean> getDjPaygift(int limit, int offset);

        Observable<DjRecommendTypeBean> getDjRecommendType(int type);

        Observable<DjCategoryRecommendBean> getDjCategoryRecommend();

        Observable<DjCatelistBean> getDjCatelist();

        Observable<DjSubBean> subDj(long rid, int isSub);

        Observable<DjProgramBean> getDjProgram(long rid);

        Observable<DjDetailBean> getDjDetail(long rid);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getDjRecommend();

        public abstract void getDjPaygift(int limit, int offset);

        public abstract void getDjRecommendType(int type);

        public abstract void getDjCategoryRecommend();

        public abstract void getDjCatelist();

        public abstract void subDj(long rid, int isSub);

        public abstract void getDjProgram(long rid);

        public abstract void getDjDetail(long rid);
    }
}
