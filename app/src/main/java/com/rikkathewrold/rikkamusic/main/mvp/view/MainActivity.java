package com.rikkathewrold.rikkamusic.main.mvp.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.toast.ToastUtils;
import com.lzx.starrysky.model.SongInfo;
import com.rikkathewrold.rikkamusic.Constants;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseActivity;
import com.rikkathewrold.rikkamusic.base.BaseFragment;
import com.rikkathewrold.rikkamusic.login.bean.LoginBean;
import com.rikkathewrold.rikkamusic.main.adapter.MultiFragmentPagerAdapter;
import com.rikkathewrold.rikkamusic.main.bean.LikeListBean;
import com.rikkathewrold.rikkamusic.main.mvp.contract.MainContract;
import com.rikkathewrold.rikkamusic.main.mvp.presenter.MainPresenter;
import com.rikkathewrold.rikkamusic.main.mvp.view.fragments.CloudVillageFragment;
import com.rikkathewrold.rikkamusic.main.mvp.view.fragments.MineFragment;
import com.rikkathewrold.rikkamusic.main.mvp.view.fragments.WowFragment;
import com.rikkathewrold.rikkamusic.personal.mvp.view.PersonalInfoActivity;
import com.rikkathewrold.rikkamusic.search.mvp.view.SearchActivity;
import com.rikkathewrold.rikkamusic.util.ActivityStarter;
import com.rikkathewrold.rikkamusic.util.ClickUtil;
import com.rikkathewrold.rikkamusic.util.GsonUtil;
import com.rikkathewrold.rikkamusic.util.LogUtil;
import com.rikkathewrold.rikkamusic.util.SharePreferenceUtil;
import com.rikkathewrold.rikkamusic.widget.BottomSongPlayBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.rikkathewrold.rikkamusic.App.getContext;


public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {
    private static final String TAG = "MainActivity";

    //viewPager会缓存的Fragment数量
    private static final int VIEWPAGER_OFF_SCREEN_PAGE_LIMIT = 2;
    public static final String LOGIN_BEAN = "loginBean";

    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_username)
    TextView userName;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.main_viewpager)
    ViewPager viewPager;
    @BindView(R.id.tab_title)
    TabLayout tabTitle;
    @BindView(R.id.bottom_controller)
    BottomSongPlayBar bottomController;

    private long firstTime;

    private List<BaseFragment> fragments = new ArrayList<>();
    private MultiFragmentPagerAdapter mPagerAdapter;
    private List<SongInfo> songInfos;
    private LoginBean loginBean;

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        ImmersionBar.with(this)
                .transparentBar()
                .statusBarColor(R.color.colorPrimary)
                .statusBarDarkFont(false)
                .init();
        connectMusicService();

        mPagerAdapter = new MultiFragmentPagerAdapter(getSupportFragmentManager());
        fragments.add(new MineFragment());
        fragments.add(new WowFragment());
        fragments.add(new CloudVillageFragment());
        mPagerAdapter.init(fragments);
    }

    @Override
    protected MainPresenter onCreatePresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected void initModule() {
        ButterKnife.bind(this);
//        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        String userLoginInfo = SharePreferenceUtil.getInstance(this).getUserInfo("");
        loginBean = GsonUtil.fromJSON(userLoginInfo, LoginBean.class);

        viewPager.setAdapter(mPagerAdapter);
        viewPager.setOffscreenPageLimit(VIEWPAGER_OFF_SCREEN_PAGE_LIMIT);
        viewPager.setCurrentItem(1);
        mPagerAdapter.getItem(1).setUserVisibleHint(true);
        tabTitle.setupWithViewPager(viewPager);
        tabTitle.setTabTextColors(Color.parseColor("#e78c86"), Color.parseColor("#FFFDFD"));
        assert loginBean != null;
        initView(loginBean);


        setSelectTextBoldAndBig(Objects.requireNonNull(tabTitle.getTabAt(1)));
        initTabListener();
        mPresenter.getLikeList(loginBean.getAccount().getId());
    }

    private void initTabListener() {
        tabTitle.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setSelectTextBoldAndBig(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.setCustomView(null);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void setSelectTextBoldAndBig(TabLayout.Tab tab) {
        TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.design_layout_tab_text, null);
        textView.setText(tab.getText());
        textView.setScaleY(1.5f);
        textView.setScaleX(1.5f);
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        textView.setTextColor(Color.parseColor("#FFFDFD"));
        tab.setCustomView(textView);
    }

    //根据用户的LoginBean初始化一些信息
    private void initView(LoginBean bean) {
        if (bean.getProfile().getAvatarUrl() != null) {
            String avatarUrl = bean.getProfile().getAvatarUrl();
            Glide.with(this).load(avatarUrl).into(ivAvatar);
        }
        if (bean.getProfile().getNickname() != null) {
            userName.setText(bean.getProfile().getNickname());
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    @OnClick({R.id.ic_nav, R.id.rl_logout, R.id.rl_avatar_name, R.id.iv_search})
    public void onClick(View v) {
        if (ClickUtil.isFastClick(1000, v)) {
            return;
        }
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.ic_nav:
                drawer.openDrawer(GravityCompat.START);
                break;

            case R.id.rl_logout:
                //退出应用
//                finish();// 销毁当前activity
//                System.exit(0);// 完全退出应用
                showDialog();
                mPresenter.logout();
                break;
            case R.id.rl_avatar_name:
                drawer.closeDrawer(GravityCompat.START);
                intent.setClass(MainActivity.this, PersonalInfoActivity.class);
                String loginbean = GsonUtil.toJson(loginBean.getProfile());
                intent.putExtra(LOGIN_BEAN, loginbean);
                startActivity(intent);
                break;
            case R.id.iv_search:
                intent.setClass(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {// 点击了返回按键
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
            exitApp(2000);// 退出应用
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disconnectMusicService();
//        EventBus.getDefault().unregister(this);
    }

    /**
     * 退出应用
     *
     * @param timeInterval 设置第二次点击退出的时间间隔
     */
    private void exitApp(long timeInterval) {
        if (System.currentTimeMillis() - firstTime >= timeInterval) {
            ToastUtils.show(R.string.press_exit_again);
            firstTime = System.currentTimeMillis();
        } else {
            finish();// 销毁当前activity
            System.exit(0);// 完全退出应用
        }
    }


    @Override
    public void onLogoutSuccess() {
        hideDialog();
        SharePreferenceUtil.getInstance(this).remove(Constants.SpKey.AUTH_TOKEN);
        ActivityStarter.getInstance().startLoginActivity(this);
        this.finish();
    }

    @Override
    public void onLogoutFail(String e) {
        hideDialog();
        LogUtil.e(TAG, "onLogoutFail : " + e);
        ToastUtils.show(e);
    }

    @Override
    public void onGetLikeListSuccess(LikeListBean bean) {
        LogUtil.d(TAG, "onGetLikeListSuccess : " + bean);
        List<Long> idsList = bean.getIds();
        List<String> likeList = new ArrayList<>();
        for (int i = 0; i < idsList.size(); i++) {
            String ids = String.valueOf(idsList.get(i));
            likeList.add(ids);
        }
        SharePreferenceUtil.getInstance(this).saveLikeList(likeList);
    }

    @Override
    public void onGetLikeListFail(String e) {
        LogUtil.d(TAG, "onGetLikeListFail");
        ToastUtils.show(e);
    }

}
