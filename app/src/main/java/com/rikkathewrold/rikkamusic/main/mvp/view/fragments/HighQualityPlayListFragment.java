package com.rikkathewrold.rikkamusic.main.mvp.view.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hjq.toast.ToastUtils;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseFragment;
import com.rikkathewrold.rikkamusic.main.adapter.EndlessRecyclerOnScrollListener;
import com.rikkathewrold.rikkamusic.main.adapter.PlayListAdapter;
import com.rikkathewrold.rikkamusic.main.bean.BannerBean;
import com.rikkathewrold.rikkamusic.main.bean.DailyRecommendBean;
import com.rikkathewrold.rikkamusic.main.bean.HighQualityPlayListBean;
import com.rikkathewrold.rikkamusic.main.bean.MainRecommendPlayListBean;
import com.rikkathewrold.rikkamusic.main.bean.PlaylistBean;
import com.rikkathewrold.rikkamusic.main.bean.PlaylistDetailBean;
import com.rikkathewrold.rikkamusic.main.bean.RecommendPlayListBean;
import com.rikkathewrold.rikkamusic.main.bean.TopListBean;
import com.rikkathewrold.rikkamusic.main.mvp.contract.WowContract;
import com.rikkathewrold.rikkamusic.main.mvp.presenter.WowPresenter;
import com.rikkathewrold.rikkamusic.main.mvp.view.PlayListActivity;
import com.rikkathewrold.rikkamusic.manager.bean.MusicCanPlayBean;
import com.rikkathewrold.rikkamusic.util.LogUtil;
import com.rikkathewrold.rikkamusic.widget.PlayListCover;
import com.rikkathewrold.rikkamusic.widget.RikkaPlayListPager;

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
 * 精品歌单的Fragment，上面有三个展示图
 * 因为PlayListFragment要复用，所以这边还要再写一个
 */
@SuppressLint("ValidFragment")
public class HighQualityPlayListFragment extends BaseFragment<WowPresenter> implements WowContract.View {
    private static final String TAG = "HighQualityPlayListFrag";

    private static final int INIT_LOAD_LIMIT = 21;
    @BindView(R.id.rv)
    RecyclerView rvPlaylist;
    @BindView(R.id.pager)
    RikkaPlayListPager pager;

    private String type;
    private PlayListAdapter adapter;
    private List<HighQualityPlayListBean.PlaylistsBean> playlist = new ArrayList<>();
    private List<PlaylistBean> list = new ArrayList<>();
    private GridLayoutManager manager;

    public HighQualityPlayListFragment(String type) {
        this.type = type;
        setFragmentTitle(type);
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_high_quality, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }


    @Override
    protected void initData() {
        list.clear();
        playlist.clear();

        adapter = new PlayListAdapter(getContext());
        adapter.setType(2);
        adapter.setListener(listener);
        manager = new GridLayoutManager(getContext(), 3);
        rvPlaylist.setLayoutManager(manager);
        rvPlaylist.addOnScrollListener(new EndlessRecyclerOnScrollListener(manager) {
            @Override
            public void onLoadMore(int currentPage) {
                LogUtil.d(TAG, "onLoadMore");
            }
        });
        rvPlaylist.setHasFixedSize(true);
        rvPlaylist.setAdapter(adapter);

        showDialog();
        mPresenter.getHighQuality(INIT_LOAD_LIMIT, 0);
    }

    private PlayListAdapter.OnPlayListClickListener listener = position -> {
        if (playlist != null || !playlist.isEmpty()) {
            Intent intent = new Intent(getActivity(), PlayListActivity.class);
            HighQualityPlayListBean.PlaylistsBean bean = playlist.get(position + 3);
            String playlistName = bean.getName();
            intent.putExtra(PLAYLIST_NAME, playlistName);
            String playlistPicUrl = bean.getCoverImgUrl();
            intent.putExtra(PLAYLIST_PICURL, playlistPicUrl);
            String playlistCreatorNickName = bean.getCreator().getNickname();
            intent.putExtra(PLAYLIST_CREATOR_NICKNAME, playlistCreatorNickName);
            String playlistCreatorAvatarUrl = bean.getCreator().getAvatarUrl();
            intent.putExtra(PLAYLIST_CREATOR_AVATARURL, playlistCreatorAvatarUrl);
            long playlistId = bean.getId();
            intent.putExtra(PLAYLIST_ID, playlistId);
            startActivity(intent);
        }
    };

    @Override
    public WowPresenter onCreatePresenter() {
        return new WowPresenter(this);
    }

    @Override
    protected void initVariables(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onGetBannerSuccess(BannerBean bean) {

    }

    @Override
    public void onGetBannerFail(String e) {

    }

    @Override
    public void onGetRecommendPlayListSuccess(MainRecommendPlayListBean bean) {

    }

    @Override
    public void onGetRecommendPlayListFail(String e) {

    }

    @Override
    public void onGetDailyRecommendSuccess(DailyRecommendBean bean) {

    }

    @Override
    public void onGetDailyRecommendFail(String e) {

    }

    @Override
    public void onGetTopListSuccess(TopListBean bean) {

    }

    @Override
    public void onGetTopListFail(String e) {

    }

    @Override
    public void onGetPlayListSuccess(RecommendPlayListBean bean) {

    }

    @Override
    public void onGetPlayListFail(String e) {

    }

    @Override
    public void onGetPlaylistDetailSuccess(PlaylistDetailBean bean) {

    }

    @Override
    public void onGetPlaylistDetailFail(String e) {

    }

    @Override
    public void onGetMusicCanPlaySuccess(MusicCanPlayBean bean) {

    }

    @Override
    public void onGetMusicCanPlayFail(String e) {

    }

    @Override
    public void onGetHighQualitySuccess(HighQualityPlayListBean bean) {
        hideDialog();
        playlist.addAll(bean.getPlaylists());
        addInfoToPager();
        for (int i = 3; i < playlist.size(); i++) {
            PlaylistBean beanInfo = new PlaylistBean();
            beanInfo.setPlaylistName(playlist.get(i).getName());
            beanInfo.setPlaylistCoverUrl(playlist.get(i).getCoverImgUrl());
            list.add(beanInfo);
        }
        adapter.notifyDataSetChanged(list);
    }

    private void addInfoToPager() {
        for (int i = 0; i < 3; i++) {
            PlayListCover cover = new PlayListCover(getContext());
            cover.setCover(playlist.get(i).getCoverImgUrl());
            cover.setText(playlist.get(i).getName());
            pager.addView(cover);
            RikkaPlayListPager.RikkaLayoutParams lp = (RikkaPlayListPager.RikkaLayoutParams) cover.getLayoutParams();
            lp.setFrom(i);
            lp.setTo(i);
            lp.setIndex(i);
        }
        pager.setClickListener(playlistClickListener);
    }

    RikkaPlayListPager.OnPlayListClickListener playlistClickListener = position -> {
        if (playlist != null || !playlist.isEmpty()) {
            Intent intent = new Intent(getActivity(), PlayListActivity.class);
            HighQualityPlayListBean.PlaylistsBean bean = playlist.get(position);
            String playlistName = bean.getName();
            intent.putExtra(PLAYLIST_NAME, playlistName);
            String playlistPicUrl = bean.getCoverImgUrl();
            intent.putExtra(PLAYLIST_PICURL, playlistPicUrl);
            String playlistCreatorNickName = bean.getCreator().getNickname();
            intent.putExtra(PLAYLIST_CREATOR_NICKNAME, playlistCreatorNickName);
            String playlistCreatorAvatarUrl = bean.getCreator().getAvatarUrl();
            intent.putExtra(PLAYLIST_CREATOR_AVATARURL, playlistCreatorAvatarUrl);
            long playlistId = bean.getId();
            intent.putExtra(PLAYLIST_ID, playlistId);
            startActivity(intent);
        }
    };

    @Override
    public void onGetHighQualityFail(String e) {
        hideDialog();
        LogUtil.e(TAG, "onGetHighQualityFail : " + e);
        ToastUtils.show(e);
    }
}
