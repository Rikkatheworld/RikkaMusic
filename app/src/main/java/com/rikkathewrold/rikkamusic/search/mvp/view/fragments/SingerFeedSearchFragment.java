package com.rikkathewrold.rikkamusic.search.mvp.view.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rikkathewrold.rikkamusic.App;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseFragment;
import com.rikkathewrold.rikkamusic.search.adapter.FeedAdapter;
import com.rikkathewrold.rikkamusic.search.bean.FeedSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.MvBean;
import com.rikkathewrold.rikkamusic.search.bean.SimiSingerBean;
import com.rikkathewrold.rikkamusic.search.bean.SingerAblumSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.SingerDescriptionBean;
import com.rikkathewrold.rikkamusic.search.bean.SingerSongSearchBean;
import com.rikkathewrold.rikkamusic.search.event.SingIdEvent;
import com.rikkathewrold.rikkamusic.search.mvp.contract.SingerContract;
import com.rikkathewrold.rikkamusic.search.mvp.presenter.SingerPresenter;
import com.rikkathewrold.rikkamusic.util.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SingerFeedSearchFragment extends BaseFragment<SingerPresenter> implements SingerContract.View {
    private static final String TAG = "SingerFeedSearchFragment";

    @BindView(R.id.rv)
    RecyclerView rvFeed;

    private String type;
    private String keywords;
    private List<FeedSearchBean.ResultBean.VideosBean> videoList = new ArrayList<>();
    private List<MvBean> mvList = new ArrayList<>();
    private FeedAdapter adapter;
    private int searchType = 1014;
    private String singerName;

    public SingerFeedSearchFragment() {
        setFragmentTitle(App.getContext().getString(R.string.singer_feed));
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onGetSingerIdEvent(SingIdEvent event) {
        singerName = event.getSingerName();
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
        adapter.setType(2);
        rvFeed.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFeed.setAdapter(adapter);

        if (singerName != null) {
            showDialog();
            mPresenter.getFeedSearch(singerName, searchType);
        }
    }

    @Override
    public SingerPresenter onCreatePresenter() {
        return new SingerPresenter(this);
    }

    @Override
    protected void initVariables(Bundle bundle) {

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
    public void onGetSingerHotSongSuccess(SingerSongSearchBean bean) {

    }

    @Override
    public void onGetSingerHotSongFail(String e) {

    }

    @Override
    public void onGetSingerAlbumSuccess(SingerAblumSearchBean bean) {

    }

    @Override
    public void onGetSingerAlbumFail(String e) {

    }

    @Override
    public void onGetFeedSearchSuccess(FeedSearchBean bean) {
        hideDialog();
        LogUtil.d(TAG, "onGetFeedSearchSuccess : " + bean);
        videoList.clear();
        videoList.addAll(bean.getResult().getVideos());
        addDataToAdapter();
    }

    private void addDataToAdapter() {
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

    }

    @Override
    public void onGetSingerDescSuccess(SingerDescriptionBean bean) {

    }

    @Override
    public void onGetSingerDescFail(String e) {

    }

    @Override
    public void onGetSimiSingerSuccess(SimiSingerBean bean) {

    }

    @Override
    public void onGetSimiSingerFail(String e) {

    }
}
