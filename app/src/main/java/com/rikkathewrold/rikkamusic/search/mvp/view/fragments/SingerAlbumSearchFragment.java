package com.rikkathewrold.rikkamusic.search.mvp.view.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.rikkathewrold.rikkamusic.App;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseFragment;
import com.rikkathewrold.rikkamusic.search.adapter.AlbumAdapter;
import com.rikkathewrold.rikkamusic.search.bean.AlbumAdapterBean;
import com.rikkathewrold.rikkamusic.search.bean.FeedSearchBean;
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

/**
 * 歌手专辑界面
 */
public class SingerAlbumSearchFragment extends BaseFragment<SingerPresenter> implements SingerContract.View {
    private static final String TAG = "SingerAlbumSearchFragment";

    @BindView(R.id.rv)
    RecyclerView rvAlbum;

    private AlbumAdapter adapter;
    private List<SingerAblumSearchBean.HotAlbumsBean> hotAlbumsList = new ArrayList<>();
    private List<AlbumAdapterBean> adapterList = new ArrayList<>();

    private long singerId = -1;


    public SingerAlbumSearchFragment() {
        setFragmentTitle(App.getContext().getString(R.string.singer_albume));
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onGetSingerIdEvent(SingIdEvent event) {
        singerId = event.getSingId();
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
        adapter = new AlbumAdapter(getContext());
        rvAlbum.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAlbum.setAdapter(adapter);
        adapter.setType(2);
        adapter.setListener(listener);

        if (singerId != -1) {
            showDialog();
            mPresenter.getSingerAlbum(singerId);
        }
    }

    AlbumAdapter.OnAlbumClickListener listener = new AlbumAdapter.OnAlbumClickListener() {
        @Override
        public void onAlbumClick(int position) {
            ToastUtils.showShort(position + "");
        }
    };


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
        hideDialog();
        adapterList.clear();
        hotAlbumsList.clear();
        hotAlbumsList.addAll(bean.getHotAlbums());
        addBeanToAdapter();
    }

    private void addBeanToAdapter() {
        for (int i = 0; i < hotAlbumsList.size(); i++) {
            AlbumAdapterBean infoBean = new AlbumAdapterBean();
            infoBean.setCreateTime(hotAlbumsList.get(i).getPublishTime());
            infoBean.setAlbumCoverUrl(hotAlbumsList.get(i).getBlurPicUrl());
            infoBean.setAlbumName(hotAlbumsList.get(i).getName());
            infoBean.setSongCount(hotAlbumsList.get(i).getSize());
            adapterList.add(infoBean);
        }
        adapter.notifyDataSetChanged(adapterList);
    }

    @Override
    public void onGetSingerAlbumFail(String e) {
        hideDialog();
        LogUtil.e(TAG, "onGetSingerAlbumFail : " + e);
        ToastUtils.showShort(e);
    }

    @Override
    public void onGetFeedSearchSuccess(FeedSearchBean bean) {

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
