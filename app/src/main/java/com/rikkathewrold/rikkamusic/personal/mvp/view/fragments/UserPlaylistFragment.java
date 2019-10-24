package com.rikkathewrold.rikkamusic.personal.mvp.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hjq.toast.ToastUtils;
import com.rikkathewrold.rikkamusic.App;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseFragment;
import com.rikkathewrold.rikkamusic.main.mvp.view.PlayListActivity;
import com.rikkathewrold.rikkamusic.personal.adapter.UserPlaylistAdapter;
import com.rikkathewrold.rikkamusic.personal.bean.PlayListItemBean;
import com.rikkathewrold.rikkamusic.personal.bean.UserDetailBean;
import com.rikkathewrold.rikkamusic.personal.bean.UserEventBean;
import com.rikkathewrold.rikkamusic.personal.bean.UserPlaylistBean;
import com.rikkathewrold.rikkamusic.personal.event.UidEvent;
import com.rikkathewrold.rikkamusic.personal.mvp.contract.PersonalContract;
import com.rikkathewrold.rikkamusic.personal.mvp.presenter.PersonalPresenter;
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

public class UserPlaylistFragment extends BaseFragment<PersonalPresenter> implements PersonalContract.View {
    private static final String TAG = "UserPlaylistFragment";

    @BindView(R.id.rv)
    RecyclerView rvPlaylist;

    private long uid = -1;
    private UserPlaylistAdapter adapter;
    private String nickName;
    private List<UserPlaylistBean.PlaylistBean> playlistBeans = new ArrayList<>();
    private List<PlayListItemBean> playListFragmentBeans = new ArrayList<>();

    public UserPlaylistFragment() {
        setFragmentTitle(App.getContext().getString(R.string.user_playlist));
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onGetUidEvent(UidEvent event) {
        LogUtil.d(TAG, "onGetUidEvent : " + event.getUid());
        uid = event.getUid();
        nickName = event.getNickName();
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_playlist, container, false);
        ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        return rootView;
    }

    @Override
    protected void initData() {
        playlistBeans.clear();
        playListFragmentBeans.clear();

        adapter = new UserPlaylistAdapter(getContext());
        adapter.setName(nickName);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvPlaylist.setLayoutManager(manager);
        rvPlaylist.setHasFixedSize(true);
        rvPlaylist.setAdapter(adapter);

        if (uid != -1) {
            showDialog();
            mPresenter.getUserPlaylist(uid);
        }
    }

    @Override
    public PersonalPresenter onCreatePresenter() {
        return new PersonalPresenter(this);
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
    public void onGetUserPlaylistSuccess(UserPlaylistBean bean) {
        hideDialog();
        LogUtil.d(TAG, "onGetUserPlaylistSuccess : " + bean);
        playlistBeans.addAll(bean.getPlaylist());

        for (int i = 0; i < playlistBeans.size(); i++) {
            PlayListItemBean beanInfo = new PlayListItemBean();
            beanInfo.setCoverUrl(playlistBeans.get(i).getCoverImgUrl());
            beanInfo.setPlaylistId(playlistBeans.get(i).getId());
            beanInfo.setPlayCount(playlistBeans.get(i).getPlayCount());
            beanInfo.setPlayListName(playlistBeans.get(i).getName());
            beanInfo.setSongNumber(playlistBeans.get(i).getTrackCount());
            beanInfo.setPlaylistCreator(playlistBeans.get(i).getCreator().getNickname());
            playListFragmentBeans.add(beanInfo);
        }
        adapter.setListener(listener);
        adapter.notifyDataSetChanged(playListFragmentBeans);
    }

    private UserPlaylistAdapter.OnPlayListItemClickListener listener = new UserPlaylistAdapter.OnPlayListItemClickListener() {
        @Override
        public void onPlayListItemClick(int position) {
            Intent intent = new Intent(getContext(), PlayListActivity.class);
            intent.putExtra(PLAYLIST_PICURL, playlistBeans.get(position).getCoverImgUrl());
            intent.putExtra(PLAYLIST_NAME, playlistBeans.get(position).getName());
            intent.putExtra(PLAYLIST_CREATOR_NICKNAME, playlistBeans.get(position).getCreator().getNickname());
            intent.putExtra(PLAYLIST_CREATOR_AVATARURL, playlistBeans.get(position).getCreator().getAvatarUrl());
            intent.putExtra(PLAYLIST_ID, playlistBeans.get(position).getId());
            getContext().startActivity(intent);
        }

        @Override
        public void onSmartPlayClick(int position) {

        }
    };

    @Override
    public void onGetUserPlaylistFail(String e) {
        hideDialog();
        LogUtil.e(TAG, "onGetUserPlaylistFail : " + e);
        ToastUtils.show(e);
    }

    @Override
    public void onGetUserEventSuccess(UserEventBean bean) {

    }

    @Override
    public void onGetUserEventFail(String e) {

    }

    @Override
    public void onGetUserDetailSuccess(UserDetailBean bean) {

    }

    @Override
    public void onGetUserDetailFail(String e) {

    }
}
