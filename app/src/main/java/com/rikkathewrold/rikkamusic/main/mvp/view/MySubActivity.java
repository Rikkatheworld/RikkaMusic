package com.rikkathewrold.rikkamusic.main.mvp.view;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseActivity;
import com.rikkathewrold.rikkamusic.base.BaseFragment;
import com.rikkathewrold.rikkamusic.main.adapter.MultiFragmentPagerAdapter;
import com.rikkathewrold.rikkamusic.main.bean.AlbumSublistBean;
import com.rikkathewrold.rikkamusic.main.bean.ArtistSublistBean;
import com.rikkathewrold.rikkamusic.main.bean.MvSublistBean;
import com.rikkathewrold.rikkamusic.main.bean.MyFmBean;
import com.rikkathewrold.rikkamusic.main.bean.PlayModeIntelligenceBean;
import com.rikkathewrold.rikkamusic.main.mvp.contract.MineContract;
import com.rikkathewrold.rikkamusic.main.mvp.presenter.MinePresenter;
import com.rikkathewrold.rikkamusic.main.mvp.view.fragments.MyAlbumSubFragment;
import com.rikkathewrold.rikkamusic.main.mvp.view.fragments.MyArtistSubFragment;
import com.rikkathewrold.rikkamusic.main.mvp.view.fragments.MyMvSubFragment;
import com.rikkathewrold.rikkamusic.personal.bean.UserPlaylistBean;
import com.rikkathewrold.rikkamusic.widget.BottomSongPlayBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的收藏界面
 */
public class MySubActivity extends BaseActivity<MinePresenter> implements MineContract.View {
    private static final String TAG = "MySubActivity";

    @BindView(R.id.tablayout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.vp_container)
    ViewPager vpFragment;
    @BindView(R.id.bottom_controller)
    BottomSongPlayBar bottomSongPlayBar;

    private List<BaseFragment> fragments = new ArrayList<>();
    private MultiFragmentPagerAdapter pagerAdapter;

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_sub);
        ImmersionBar.with(this)
                .transparentBar()
                .statusBarColor(R.color.colorPrimary)
                .statusBarDarkFont(false)
                .init();

        pagerAdapter = new MultiFragmentPagerAdapter(getSupportFragmentManager());
        fragments.add(new MyAlbumSubFragment());
        fragments.add(new MyArtistSubFragment());
        fragments.add(new MyMvSubFragment());
        pagerAdapter.init(fragments);
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
        setLeftTitleText(getString(R.string.my_collection), getString(R.string.colorWhite));

        vpFragment.setAdapter(pagerAdapter);
        vpFragment.setOffscreenPageLimit(3);
        tabLayout.setViewPager(vpFragment);
        tabLayout.setCurrentTab(1);


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
