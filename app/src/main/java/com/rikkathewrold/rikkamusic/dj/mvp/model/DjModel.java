package com.rikkathewrold.rikkamusic.dj.mvp.model;

import com.rikkathewrold.rikkamusic.api.ApiEngine;
import com.rikkathewrold.rikkamusic.dj.bean.DjCategoryRecommendBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjCatelistBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjDetailBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjPaygiftBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjProgramBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjRecommendBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjRecommendTypeBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjSubBean;
import com.rikkathewrold.rikkamusic.dj.mvp.contract.DjContract;

import io.reactivex.Observable;

public class DjModel implements DjContract.Model {
    @Override
    public Observable<DjRecommendBean> getDjRecommend() {
        return ApiEngine.getInstance().getApiService().getRadioRecommend();
    }

    @Override
    public Observable<DjPaygiftBean> getDjPaygift(int limit, int offset) {
        return ApiEngine.getInstance().getApiService().getDjPaygift(limit, offset);
    }

    @Override
    public Observable<DjRecommendTypeBean> getDjRecommendType(int type) {
        return ApiEngine.getInstance().getApiService().getDjRecommend(type);
    }

    @Override
    public Observable<DjCategoryRecommendBean> getDjCategoryRecommend() {
        return ApiEngine.getInstance().getApiService().getDjCategoryRecommend();
    }

    @Override
    public Observable<DjCatelistBean> getDjCatelist() {
        return ApiEngine.getInstance().getApiService().getDjCatelist();
    }

    @Override
    public Observable<DjSubBean> subDj(long rid, int isSub) {
        return ApiEngine.getInstance().getApiService().subDj(rid, isSub);
    }

    @Override
    public Observable<DjProgramBean> getDjProgram(long rid) {
        return ApiEngine.getInstance().getApiService().getDjProgram(rid);
    }

    @Override
    public Observable<DjDetailBean> getDjDetail(long rid) {
        return ApiEngine.getInstance().getApiService().getDjDetail(rid);
    }
}
