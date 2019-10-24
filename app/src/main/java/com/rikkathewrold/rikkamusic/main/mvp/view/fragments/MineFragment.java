package com.rikkathewrold.rikkamusic.main.mvp.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hjq.toast.ToastUtils;
import com.lzx.starrysky.model.SongInfo;
import com.rikkathewrold.rikkamusic.App;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseFragment;
import com.rikkathewrold.rikkamusic.login.bean.LoginBean;
import com.rikkathewrold.rikkamusic.main.bean.AlbumSublistBean;
import com.rikkathewrold.rikkamusic.main.bean.ArtistSublistBean;
import com.rikkathewrold.rikkamusic.main.bean.MvSublistBean;
import com.rikkathewrold.rikkamusic.main.bean.MyFmBean;
import com.rikkathewrold.rikkamusic.main.bean.PlayModeIntelligenceBean;
import com.rikkathewrold.rikkamusic.main.mvp.contract.MineContract;
import com.rikkathewrold.rikkamusic.main.mvp.presenter.MinePresenter;
import com.rikkathewrold.rikkamusic.main.mvp.view.LocalMusicActivity;
import com.rikkathewrold.rikkamusic.main.mvp.view.MySubActivity;
import com.rikkathewrold.rikkamusic.main.mvp.view.PlayListActivity;
import com.rikkathewrold.rikkamusic.manager.SongPlayManager;
import com.rikkathewrold.rikkamusic.personal.adapter.UserPlaylistAdapter;
import com.rikkathewrold.rikkamusic.personal.bean.PlayListItemBean;
import com.rikkathewrold.rikkamusic.personal.bean.UserPlaylistBean;
import com.rikkathewrold.rikkamusic.song.mvp.view.SongActivity;
import com.rikkathewrold.rikkamusic.util.ClickUtil;
import com.rikkathewrold.rikkamusic.util.GsonUtil;
import com.rikkathewrold.rikkamusic.util.LogUtil;
import com.rikkathewrold.rikkamusic.util.SharePreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.rikkathewrold.rikkamusic.base.BaseActivity.SONG_URL;
import static com.rikkathewrold.rikkamusic.main.mvp.view.fragments.WowFragment.PLAYLIST_CREATOR_AVATARURL;
import static com.rikkathewrold.rikkamusic.main.mvp.view.fragments.WowFragment.PLAYLIST_CREATOR_NICKNAME;
import static com.rikkathewrold.rikkamusic.main.mvp.view.fragments.WowFragment.PLAYLIST_ID;
import static com.rikkathewrold.rikkamusic.main.mvp.view.fragments.WowFragment.PLAYLIST_NAME;
import static com.rikkathewrold.rikkamusic.main.mvp.view.fragments.WowFragment.PLAYLIST_PICURL;

public class MineFragment extends BaseFragment<MinePresenter> implements MineContract.View {
    private static final String TAG = "MineFragment";

    @BindView(R.id.rv_mine_playlist)
    RecyclerView rvPlayList;

    private LoginBean loginBean;
    private long uid;
    private UserPlaylistAdapter adapter;
    private List<UserPlaylistBean.PlaylistBean> playlistBeans = new ArrayList<>();
    private List<PlayListItemBean> adapterList = new ArrayList<>();

    public MineFragment() {
        setFragmentTitle(App.getContext().getString(R.string.mine));
    }


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected void initData() {
        loginBean = GsonUtil.fromJSON(SharePreferenceUtil.getInstance(getContext()).getUserInfo(""), LoginBean.class);
        uid = loginBean.getAccount().getId();

        adapter = new UserPlaylistAdapter(getContext());
        rvPlayList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPlayList.setAdapter(adapter);
        adapter.setListener(listener);
        adapter.setShowSmartPlay(true);
        adapter.setName(loginBean.getAccount().getUserName());

        showDialog();
        mPresenter.getUserPlaylist(uid);
    }

    UserPlaylistAdapter.OnPlayListItemClickListener listener = new UserPlaylistAdapter.OnPlayListItemClickListener() {
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
            showDialog();
            mPresenter.getIntelligenceList(1, playlistBeans.get(position).getId());
        }
    };

    @Override
    public MinePresenter onCreatePresenter() {
        return new MinePresenter(this);
    }

    @Override
    protected void initVariables(Bundle bundle) {

    }

    @Override
    @OnClick({R.id.rl_fm, R.id.local_music, R.id.my_radio, R.id.my_collection})
    public void onClick(View v) {
        if (ClickUtil.isFastClick(1000, v)) {
            return;
        }
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.rl_fm:
                showDialog();
                mPresenter.getMyFM();
                break;
            case R.id.local_music:
                intent.setClass(getContext(), LocalMusicActivity.class);
                startActivity(intent);
                break;
            case R.id.my_radio:
                break;
            case R.id.my_collection:
                intent.setClass(getContext(), MySubActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onGetUserPlaylistSuccess(UserPlaylistBean bean) {
        hideDialog();
        LogUtil.d(TAG, "onGetUserPlaylistSuccess :" + bean);
        playlistBeans.clear();

        playlistBeans.addAll(bean.getPlaylist());

        for (int i = 0; i < playlistBeans.size(); i++) {
            PlayListItemBean beanInfo = new PlayListItemBean();
            beanInfo.setCoverUrl(playlistBeans.get(i).getCoverImgUrl());
            beanInfo.setPlaylistId(playlistBeans.get(i).getId());
            beanInfo.setPlayCount(playlistBeans.get(i).getPlayCount());
            beanInfo.setPlayListName(playlistBeans.get(i).getName());
            beanInfo.setSongNumber(playlistBeans.get(i).getTrackCount());
            beanInfo.setPlaylistCreator(playlistBeans.get(i).getCreator().getNickname());
            adapterList.add(beanInfo);
        }
        adapter.notifyDataSetChanged(adapterList);
    }

    @Override
    public void onGetUserPlaylistFail(String e) {
        hideDialog();
        LogUtil.d(TAG, "onGetUserPlaylistFail : " + e);
        ToastUtils.show(e);
    }

    @Override
    public void onGetIntelligenceListSuccess(PlayModeIntelligenceBean bean) {
        hideDialog();
        LogUtil.d(TAG, "onGetIntelligenceListSuccess");

    }

    @Override
    public void onGetIntelligenceListFail(String e) {
        hideDialog();
        LogUtil.d(TAG, "onGetIntelligenceListFail : " + e);
        ToastUtils.show(e);
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

    }

    @Override
    public void onGetAlbumSublistBeanFail(String e) {

    }

    @Override
    public void onGetMyFMSuccess(MyFmBean bean) {
        hideDialog();
        LogUtil.d(TAG, "onGetMyFMSuccess：" + bean);
        List<MyFmBean.DataBean> fmList = bean.getData();
        List<SongInfo> songList = new ArrayList<>();
        for (int i = 0; i < fmList.size(); i++) {
            SongInfo songInfo = new SongInfo();
            songInfo.setSongName(fmList.get(i).getName());
            songInfo.setSongUrl(SONG_URL + fmList.get(i).getId() + ".mp3");
            songInfo.setSongCover(fmList.get(i).getAlbum().getBlurPicUrl());
            songInfo.setArtist(fmList.get(i).getArtists().get(0).getName());
            songInfo.setSongId(String.valueOf(fmList.get(i).getId()));
            songInfo.setDuration(fmList.get(i).getDuration());
            songInfo.setArtistId(String.valueOf(fmList.get(i).getArtists().get(0).getId()));
            songInfo.setArtistKey(fmList.get(i).getArtists().get(0).getPicUrl());
            songList.add(songInfo);
        }
        SongPlayManager.getInstance().clickPlayAll(songList, 0);
        SongInfo songInfo = songList.get(0);
        Intent intent = new Intent(getContext(), SongActivity.class);
        intent.putExtra(SongActivity.SONG_INFO, songInfo);
        startActivity(intent);
    }

    @Override
    public void onGetMyFMFail(String e) {
        hideDialog();
        LogUtil.e(TAG, "onGetMyFMFail :" + e);
        ToastUtils.show(e);
    }
}
