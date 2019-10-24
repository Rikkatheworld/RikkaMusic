package com.rikkathewrold.rikkamusic.main.mvp.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.toast.ToastUtils;
import com.lzx.starrysky.model.SongInfo;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseActivity;
import com.rikkathewrold.rikkamusic.main.adapter.SongListAdapter;
import com.rikkathewrold.rikkamusic.main.bean.AlbumSublistBean;
import com.rikkathewrold.rikkamusic.main.bean.ArtistSublistBean;
import com.rikkathewrold.rikkamusic.main.bean.MvSublistBean;
import com.rikkathewrold.rikkamusic.main.bean.MyFmBean;
import com.rikkathewrold.rikkamusic.main.bean.PlayModeIntelligenceBean;
import com.rikkathewrold.rikkamusic.main.mvp.contract.MineContract;
import com.rikkathewrold.rikkamusic.main.mvp.presenter.MinePresenter;
import com.rikkathewrold.rikkamusic.manager.SongPlayManager;
import com.rikkathewrold.rikkamusic.personal.bean.UserPlaylistBean;
import com.rikkathewrold.rikkamusic.util.LogUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocalMusicActivity extends BaseActivity<MinePresenter> implements MineContract.View {
    private static final String TAG = "LocalMusicActivity";

    @BindView(R.id.rv_local_song)
    RecyclerView rvLocal;

    private List<SongInfo> localSongInfoList;
    private SongListAdapter adapter;

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_local_music);
        ImmersionBar.with(this)
                .transparentBar()
                .statusBarColor(R.color.colorPrimary)
                .statusBarDarkFont(false)
                .init();
    }

    @Override
    protected MinePresenter onCreatePresenter() {
        return null;
    }

    @Override
    protected void initModule() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        setBackBtn(getString(R.string.colorWhite));
        setLeftTitleText(getString(R.string.local_music), "#ffffff");

        adapter = new SongListAdapter(this);
        adapter.setType(4);
        rvLocal.setLayoutManager(new LinearLayoutManager(this));
        rvLocal.setAdapter(adapter);

        //申请权限
        checkNeedPermission();
    }

    private void checkNeedPermission() {
        PermissionUtils.permission(PermissionConstants.STORAGE)
                .rationale(shouldRequest -> {
                    LogUtil.d(TAG, "rationale ==");
                    ToastUtils.show("您拒绝了某些必要的权限，请重新授权，否则程序的功能不能正常使用！");
                })
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {
                        LogUtil.d(TAG, "onGranted");
                        localSongInfoList = SongPlayManager.getInstance().getLocalSongInfoList();
                        if (localSongInfoList != null && localSongInfoList.size() > 0) {
                            setSongList(localSongInfoList);
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                        if (!permissionsDeniedForever.isEmpty()) {
                            ToastUtils.show("您永久的拒绝了某些重要的权限，导致程序无法正常使用，请前往程序的设置界面重新授权！");
                        }
                    }
                })
                .theme(activity -> {
                }).request();
    }

    private void setSongList(List<SongInfo> songList) {
        adapter.notifyDataSetChanged(songList);
    }

    @Override
    @OnClick({R.id.rl_playall})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_playall:
                SongPlayManager.getInstance().clickPlayAll(localSongInfoList, 0);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    }

    @Override
    public void onGetAlbumSublistBeanFail(String e) {

    }

    @Override
    public void onGetMyFMSuccess(MyFmBean bean) {

    }

    @Override
    public void onGetMyFMFail(String e) {

    }
}
