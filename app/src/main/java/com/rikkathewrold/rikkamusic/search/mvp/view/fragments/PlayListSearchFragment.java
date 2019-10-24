package com.rikkathewrold.rikkamusic.search.mvp.view.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseFragment;
import com.rikkathewrold.rikkamusic.main.mvp.view.PlayListActivity;
import com.rikkathewrold.rikkamusic.search.adapter.PlayListSearchAdapter;
import com.rikkathewrold.rikkamusic.search.bean.AlbumSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.FeedSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.HotSearchDetailBean;
import com.rikkathewrold.rikkamusic.search.bean.PlayListSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.RadioSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.SingerSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.SongSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.SynthesisSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.UserSearchBean;
import com.rikkathewrold.rikkamusic.search.event.KeywordsEvent;
import com.rikkathewrold.rikkamusic.search.mvp.contract.SearchContract.View;
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

import static com.rikkathewrold.rikkamusic.main.mvp.view.fragments.WowFragment.PLAYLIST_CREATOR_AVATARURL;
import static com.rikkathewrold.rikkamusic.main.mvp.view.fragments.WowFragment.PLAYLIST_CREATOR_NICKNAME;
import static com.rikkathewrold.rikkamusic.main.mvp.view.fragments.WowFragment.PLAYLIST_ID;
import static com.rikkathewrold.rikkamusic.main.mvp.view.fragments.WowFragment.PLAYLIST_NAME;
import static com.rikkathewrold.rikkamusic.main.mvp.view.fragments.WowFragment.PLAYLIST_PICURL;


/**
 * 歌单搜索结果Fragment ：1000
 */
@SuppressLint("ValidFragment")
public class PlayListSearchFragment extends BaseFragment<SearchPresenter> implements View {
    private static final String TAG = "PlayListSearchFragment";

    @BindView(R.id.rv)
    RecyclerView rvPlayList;

    private String type;
    private int searchType = 1000;
    private PlayListSearchAdapter adapter;
    private List<PlayListSearchBean.ResultBean.PlaylistsBean> playlistList = new ArrayList<>();
    private String keywords;
    private boolean needRefresh = false;

    public PlayListSearchFragment() {
    }

    public PlayListSearchFragment(String type) {
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
                    mPresenter.getPlayListSearch(keywords, searchType);
                }
            }
            keywords = event.getKeyword();
        }
    }

    @Override
    protected android.view.View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        android.view.View rootView = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        return rootView;
    }

    @Override
    protected void initData() {
        adapter = new PlayListSearchAdapter(getContext());
        adapter.setKeywords(keywords);
        adapter.setListener(listener);
        rvPlayList.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvPlayList.setLayoutManager(manager);

        if (keywords != null) {
            showDialog();
            mPresenter.getPlayListSearch(keywords, searchType);
        }
    }

    private PlayListSearchAdapter.OnPlayListClickListener listener = position -> {
        if (playlistList != null) {
            Intent intent = new Intent(getActivity(), PlayListActivity.class);
            intent.putExtra(PLAYLIST_NAME, playlistList.get(position).getName());
            intent.putExtra(PLAYLIST_PICURL, playlistList.get(position).getCoverImgUrl());
            intent.putExtra(PLAYLIST_CREATOR_NICKNAME, playlistList.get(position).getCreator().getNickname());
            intent.putExtra(PLAYLIST_CREATOR_AVATARURL, "");
            intent.putExtra(PLAYLIST_ID, playlistList.get(position).getId());
            getActivity().startActivity(intent);
        }
    };

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
            mPresenter.getPlayListSearch(keywords, searchType);
        }
    }

    @Override
    public void onClick(android.view.View v) {

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

    }

    @Override
    public void onGetFeedSearchFail(String e) {

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
        hideDialog();
        LogUtil.d(TAG, "onGetPlayListSearchSuccess");
        playlistList.clear();
        if (bean != null) {
            if (bean.getResult().getPlaylists() != null) {
                playlistList.addAll(bean.getResult().getPlaylists());
            }
            adapter.notifyDataSetChanged(playlistList);
        }
    }

    @Override
    public void onGetPlayListSearchFail(String e) {
        hideDialog();
        LogUtil.e(TAG, "onGetPlayListSearchFail : " + e);
        ToastUtils.showShort(e);
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
