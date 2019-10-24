package com.rikkathewrold.rikkamusic.search.mvp.view.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseFragment;
import com.rikkathewrold.rikkamusic.main.mvp.view.PlayListActivity;
import com.rikkathewrold.rikkamusic.search.adapter.AlbumAdapter;
import com.rikkathewrold.rikkamusic.search.bean.AlbumAdapterBean;
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

import static com.rikkathewrold.rikkamusic.main.mvp.view.fragments.WowFragment.PLAYLIST_CREATOR_AVATARURL;
import static com.rikkathewrold.rikkamusic.main.mvp.view.fragments.WowFragment.PLAYLIST_CREATOR_NICKNAME;
import static com.rikkathewrold.rikkamusic.main.mvp.view.fragments.WowFragment.PLAYLIST_ID;
import static com.rikkathewrold.rikkamusic.main.mvp.view.fragments.WowFragment.PLAYLIST_NAME;
import static com.rikkathewrold.rikkamusic.main.mvp.view.fragments.WowFragment.PLAYLIST_PICURL;

/**
 * 专辑搜索结果Fragment 10
 */
@SuppressLint("ValidFragment")
public class AlbumSearchFragment extends BaseFragment<SearchPresenter> implements SearchContract.View {
    private static final String TAG = "AlbumSearchFragment";

    @BindView(R.id.rv)
    RecyclerView rvAlbum;

    private String type;
    private int searchType = 10;
    private AlbumAdapter adapter;
    private List<AlbumSearchBean.ResultBean.AlbumsBean> albumList = new ArrayList<>();
    private List<AlbumAdapterBean> adapterList = new ArrayList<>();
    private String keywords;
    private boolean needRefresh = false;

    public AlbumSearchFragment() {
    }

    public AlbumSearchFragment(String type) {
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
                    mPresenter.getAlbumSearch(keywords, searchType);
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
        adapter = new AlbumAdapter(getContext());
        adapter.setKeywords(keywords);
        adapter.setListener(listener);
        adapter.setType(1);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvAlbum.setLayoutManager(manager);
        rvAlbum.setAdapter(adapter);

        if (keywords != null) {
            showDialog();
            mPresenter.getAlbumSearch(keywords, searchType);
        }
    }

    AlbumAdapter.OnAlbumClickListener listener = type -> {
        Intent intent = new Intent(getContext(), PlayListActivity.class);
        intent.putExtra(PLAYLIST_PICURL, albumList.get(type).getBlurPicUrl());
        intent.putExtra(PLAYLIST_NAME, albumList.get(type).getName());
        intent.putExtra(PLAYLIST_CREATOR_NICKNAME, "");
        intent.putExtra(PLAYLIST_CREATOR_AVATARURL, "");
        intent.putExtra(PLAYLIST_ID, albumList.get(type).getId());
        getActivity().startActivity(intent);
    };

    @Override
    public SearchPresenter onCreatePresenter() {
        return new SearchPresenter(this);
    }

    @Override
    protected void initVariables(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onVisible() {
        super.onVisible();
        if (needRefresh) {
            needRefresh = false;
            showDialog();
            mPresenter.getAlbumSearch(keywords, searchType);
        }
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
        hideDialog();
        LogUtil.d(TAG, "onGetAlbumSearchSuccess : " + bean);
        albumList.clear();
        if (bean.getResult().getAlbums() != null) {
            albumList.addAll(bean.getResult().getAlbums());
        }
        adapterList.clear();
        addBeanToAdapter();
    }

    private void addBeanToAdapter() {
        for (int i = 0; i < albumList.size(); i++) {
            AlbumAdapterBean infoBean = new AlbumAdapterBean();
            infoBean.setAlbumCoverUrl(albumList.get(i).getPicUrl());
            infoBean.setAlbumName(albumList.get(i).getName());
            infoBean.setCreateTime(albumList.get(i).getPublishTime());
            infoBean.setSinger(albumList.get(i).getArtist().getName() + "(" + albumList.get(i).getArtist().getTrans() + ")");
            adapterList.add(infoBean);
        }
        adapter.notifyDataSetChanged(adapterList);
    }

    @Override
    public void onGetAlbumSearchFail(String e) {
        hideDialog();
        LogUtil.e(TAG, "onGetAlbumSearchFail : " + e);
        ToastUtils.showShort(e);
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
