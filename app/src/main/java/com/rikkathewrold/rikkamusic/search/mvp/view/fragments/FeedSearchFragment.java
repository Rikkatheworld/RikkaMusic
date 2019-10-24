package com.rikkathewrold.rikkamusic.search.mvp.view.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hjq.toast.ToastUtils;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseFragment;
import com.rikkathewrold.rikkamusic.search.adapter.FeedAdapter;
import com.rikkathewrold.rikkamusic.search.bean.AlbumSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.FeedSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.HotSearchDetailBean;
import com.rikkathewrold.rikkamusic.search.bean.MvBean;
import com.rikkathewrold.rikkamusic.search.bean.PlayListSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.RadioSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.SingerSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.SongSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.SynthesisSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.UserSearchBean;
import com.rikkathewrold.rikkamusic.search.event.KeywordsEvent;
import com.rikkathewrold.rikkamusic.search.mvp.contract.SearchContract;
import com.rikkathewrold.rikkamusic.search.mvp.presenter.SearchPresenter;
import com.rikkathewrold.rikkamusic.search.mvp.view.SearchResultActivity;
import com.rikkathewrold.rikkamusic.util.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 视频搜索结果Fragment 1014
 */
@SuppressLint("ValidFragment")
public class FeedSearchFragment extends BaseFragment<SearchPresenter> implements SearchContract.View {
    private static final String TAG = "FeedSearchFragment";

    @BindView(R.id.rv)
    RecyclerView rvFeed;

    private String type;
    private String keywords;
    private List<FeedSearchBean.ResultBean.VideosBean> videoList = new ArrayList<>();
    private List<MvBean> mvList = new ArrayList<>();
    private FeedAdapter adapter;
    private boolean needRefresh = false;
    private int searchType = 1014;

    public FeedSearchFragment() {
    }

    public FeedSearchFragment(String type) {
        this.type = type;
        setFragmentTitle(type);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onGetKeywordsEvent(KeywordsEvent event) {
        LogUtil.d(TAG, "onGetKeywordsEvent : " + event.toString());
        if (event != null) {
            if (keywords != null && !event.getKeyword().equals(keywords)) {
                needRefresh = true;
                if (((SearchResultActivity) getActivity()).getPosition() == 2) {
                    needRefresh = false;
                    keywords = event.getKeyword();
                    showDialog();
                    mPresenter.getFeedSearch(keywords, searchType);
                }
            }
            keywords = event.getKeyword();
        }
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        return rootView;
    }

    @Override
    protected void initData() {
        adapter = new FeedAdapter(getContext());
        adapter.setType(1);
        adapter.setKeywords(keywords == null ? "" : keywords);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvFeed.setLayoutManager(manager);
        rvFeed.setAdapter(adapter);

        if (keywords != null) {
            showDialog();
            mPresenter.getFeedSearch(keywords, searchType);
        }
    }

    @Override
    public SearchPresenter onCreatePresenter() {
        return new SearchPresenter(this);
    }

    @Override
    protected void initVariables(Bundle bundle) {

    }

    @Override
    protected void onVisible() {
        super.onVisible();
        if (needRefresh) {
            needRefresh = false;
            showDialog();
            mPresenter.getSongSearch(keywords, searchType);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onGetHotSearchDetailSuccess(HotSearchDetailBean bean) {

    }

    @Override
    public void onGetHotSearchDetailFail(String e) {

    }

    @Override
    public void onGetSongSearchSuccess(SongSearchBean bean) {

    }

    @Override
    public void onGetSongSearchFail(String e) {

    }

    @Override
    public void onGetFeedSearchSuccess(FeedSearchBean bean) {
        hideDialog();
        LogUtil.d(TAG, "onGetFeedSearchSuccess : " + bean);
        videoList.clear();
        if (bean.getResult().getVideos() != null) {
            videoList.addAll(bean.getResult().getVideos());
        }
        mvList.clear();
        for (int i = 0; i < videoList.size(); i++) {
            MvBean mvBean = new MvBean();
            mvBean.setCoverUrl(videoList.get(i).getCoverUrl());
            mvBean.setCreator(videoList.get(i).getCreator());
            mvBean.setDuration(videoList.get(i).getDurationms());
            mvBean.setPlayTime(videoList.get(i).getPlayTime());
            mvBean.setTitle(videoList.get(i).getTitle());
            mvBean.setType(videoList.get(i).getType());
            mvBean.setVid(videoList.get(i).getVid());
            mvList.add(mvBean);
        }
        adapter.notifyDataSetChanged(mvList);
    }

    @Override
    public void onGetFeedSearchFail(String e) {
        hideDialog();
        LogUtil.d(TAG, "onGetFeedSearchFail : " + e);
        ToastUtils.show(e);
    }

    @Override
    public void onGetSingerSearchSuccess(SingerSearchBean bean) {

    }

    @Override
    public void onGetSingerSearchFail(String e) {

    }

    @Override
    public void onGetAlbumSearchSuccess(AlbumSearchBean bean) {

    }

    @Override
    public void onGetAlbumSearchFail(String e) {

    }

    @Override
    public void onGetPlayListSearchSuccess(PlayListSearchBean bean) {

    }

    @Override
    public void onGetPlayListSearchFail(String e) {

    }

    @Override
    public void onGetRadioSearchSuccess(RadioSearchBean bean) {

    }

    @Override
    public void onGetRadioSearchFail(String e) {

    }

    @Override
    public void onGetUserSearchSuccess(UserSearchBean bean) {

    }

    @Override
    public void onGetUserSearchFail(String e) {

    }

    @Override
    public void onGetSynthesisSearchSuccess(SynthesisSearchBean bean) {

    }

    @Override
    public void onGetSynthesisSearchFail(String e) {

    }
}
