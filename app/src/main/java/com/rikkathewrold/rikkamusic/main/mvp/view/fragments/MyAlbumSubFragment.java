package com.rikkathewrold.rikkamusic.main.mvp.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.hjq.toast.ToastUtils;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseFragment;
import com.rikkathewrold.rikkamusic.main.bean.AlbumSublistBean;
import com.rikkathewrold.rikkamusic.main.bean.ArtistSublistBean;
import com.rikkathewrold.rikkamusic.main.bean.MvSublistBean;
import com.rikkathewrold.rikkamusic.main.bean.MyFmBean;
import com.rikkathewrold.rikkamusic.main.bean.PlayModeIntelligenceBean;
import com.rikkathewrold.rikkamusic.main.mvp.contract.MineContract;
import com.rikkathewrold.rikkamusic.main.mvp.presenter.MinePresenter;
import com.rikkathewrold.rikkamusic.main.mvp.view.PlayListActivity;
import com.rikkathewrold.rikkamusic.personal.bean.UserPlaylistBean;
import com.rikkathewrold.rikkamusic.search.adapter.AlbumAdapter;
import com.rikkathewrold.rikkamusic.search.bean.AlbumAdapterBean;
import com.rikkathewrold.rikkamusic.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.rikkathewrold.rikkamusic.main.mvp.view.fragments.WowFragment.PLAYLIST_CREATOR_AVATARURL;
import static com.rikkathewrold.rikkamusic.main.mvp.view.fragments.WowFragment.PLAYLIST_CREATOR_NICKNAME;
import static com.rikkathewrold.rikkamusic.main.mvp.view.fragments.WowFragment.PLAYLIST_ID;
import static com.rikkathewrold.rikkamusic.main.mvp.view.fragments.WowFragment.PLAYLIST_NAME;
import static com.rikkathewrold.rikkamusic.main.mvp.view.fragments.WowFragment.PLAYLIST_PICURL;

public class MyAlbumSubFragment extends BaseFragment<MinePresenter> implements MineContract.View {
    private static final String TAG = "MyAlbumSubFragment";

    @BindView(R.id.rl_my_album)
    RelativeLayout rlMyAlbum;
    @BindView(R.id.rv_album)
    RecyclerView rvAlbum;

    private List<AlbumAdapterBean> albumList = new ArrayList<>();
    private AlbumAdapter albumAdapter;
    private List<AlbumSublistBean.DataBean> sublist = new ArrayList<>();

    public MyAlbumSubFragment() {
        setFragmentTitle("专辑");
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_album_sublist, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected void initData() {
        albumList.clear();
        albumAdapter = new AlbumAdapter(getContext());
        rvAlbum.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAlbum.setAdapter(albumAdapter);
        albumAdapter.setListener(listener);
        albumAdapter.setType(2);

        showDialog();
        mPresenter.getAlbumSublist();
    }

    AlbumAdapter.OnAlbumClickListener listener = position -> {
        Intent intent = new Intent(getContext(), PlayListActivity.class);
        intent.putExtra(PLAYLIST_PICURL, sublist.get(position).getPicUrl());
        intent.putExtra(PLAYLIST_NAME, sublist.get(position).getName());
        intent.putExtra(PLAYLIST_CREATOR_NICKNAME, "");
        intent.putExtra(PLAYLIST_CREATOR_AVATARURL, "");
        intent.putExtra(PLAYLIST_ID, sublist.get(position).getId());
        getActivity().startActivity(intent);
    };

    @Override
    public MinePresenter onCreatePresenter() {
        return new MinePresenter(this);
    }

    @Override
    protected void initVariables(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onGetUserPlaylistSuccess(UserPlaylistBean bean) {

    }

    @Override
    public void onGetUserPlaylistFail(String e) {

    }

    @Override
    public void onGetIntelligenceListSuccess(PlayModeIntelligenceBean bean) {

    }

    @Override
    public void onGetIntelligenceListFail(String e) {

    }

    @Override
    public void onGetMvSublistBeanSuccess(MvSublistBean bean) {

    }

    @Override
    public void onGetMvSublistBeanFail(String e) {

    }

    @Override
    public void onGetArtistSublistBeanSuccess(ArtistSublistBean bean) {

    }

    @Override
    public void onGetArtistSublistBeanFail(String e) {

    }

    @Override
    public void onGetAlbumSublistBeanSuccess(AlbumSublistBean bean) {
        hideDialog();
        LogUtil.d(TAG, "onGetAlbumSublistBeanSuccess :" + bean);
        albumList.clear();
        sublist = bean.getData();
        for (int i = 0; i < sublist.size(); i++) {
            AlbumAdapterBean infoBean = new AlbumAdapterBean();
            infoBean.setAlbumCoverUrl(sublist.get(i).getPicUrl());
            infoBean.setAlbumName(sublist.get(i).getName());
            infoBean.setCreateTime(sublist.get(i).getSubTime());
            infoBean.setSinger(sublist.get(i).getArtists().get(0).getName());
            albumList.add(infoBean);
        }
        albumAdapter.notifyDataSetChanged(albumList);
    }

    @Override
    public void onGetAlbumSublistBeanFail(String e) {
        hideDialog();
        LogUtil.e(TAG, "onGetAlbumSublistBeanFail :" + e);
        ToastUtils.show(e);
    }

    @Override
    public void onGetMyFMSuccess(MyFmBean bean) {

    }

    @Override
    public void onGetMyFMFail(String e) {

    }
}
