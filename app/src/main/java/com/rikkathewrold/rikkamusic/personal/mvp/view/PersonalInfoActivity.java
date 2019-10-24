package com.rikkathewrold.rikkamusic.personal.mvp.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.flyco.tablayout.SlidingTabLayout;
import com.hjq.toast.ToastUtils;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseActivity;
import com.rikkathewrold.rikkamusic.base.BaseFragment;
import com.rikkathewrold.rikkamusic.login.bean.LoginBean;
import com.rikkathewrold.rikkamusic.main.adapter.MultiFragmentPagerAdapter;
import com.rikkathewrold.rikkamusic.personal.bean.UserDetailBean;
import com.rikkathewrold.rikkamusic.personal.bean.UserEventBean;
import com.rikkathewrold.rikkamusic.personal.bean.UserPlaylistBean;
import com.rikkathewrold.rikkamusic.personal.event.UidEvent;
import com.rikkathewrold.rikkamusic.personal.event.UserDetailEvent;
import com.rikkathewrold.rikkamusic.personal.mvp.contract.PersonalContract;
import com.rikkathewrold.rikkamusic.personal.mvp.presenter.PersonalPresenter;
import com.rikkathewrold.rikkamusic.personal.mvp.view.fragments.UserDynamicsFragment;
import com.rikkathewrold.rikkamusic.personal.mvp.view.fragments.UserInfoFragment;
import com.rikkathewrold.rikkamusic.personal.mvp.view.fragments.UserPlaylistFragment;
import com.rikkathewrold.rikkamusic.search.bean.UserSearchBean;
import com.rikkathewrold.rikkamusic.util.AppBarStateChangeListener;
import com.rikkathewrold.rikkamusic.util.ClickUtil;
import com.rikkathewrold.rikkamusic.util.DensityUtil;
import com.rikkathewrold.rikkamusic.util.GsonUtil;
import com.rikkathewrold.rikkamusic.util.LogUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.rikkathewrold.rikkamusic.main.mvp.view.MainActivity.LOGIN_BEAN;
import static com.rikkathewrold.rikkamusic.personal.mvp.view.PictureCheckActivity.PIC_URL;

/**
 * 个人信息界面
 * 包含了头像、音乐、动态、关于的信息
 */
public class PersonalInfoActivity extends BaseActivity<PersonalPresenter> implements PersonalContract.View {
    private static final String TAG = "PersonalInfoActivity";

    public static final String USER_BEAN = "userBean";

    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_nickname)
    TextView tvNickName;
    @BindView(R.id.btn_edit)
    Button btnEdit;
    @BindView(R.id.tv_like)
    TextView tvLike;
    @BindView(R.id.tv_fans)
    TextView tvFans;
    @BindView(R.id.tab_title)
    SlidingTabLayout tabTitle;
    @BindView(R.id.vp_container)
    ViewPager vpContainer;
    @BindView(R.id.appbar)
    AppBarLayout appBar;
    @BindView(R.id.iv_background)
    ImageView ivBg;
    @BindView(R.id.iv_background_cover)
    ImageView ivBgCover;
    @BindView(R.id.rl_info)
    RelativeLayout rlInfo;

    private UserInfoFragment userInfoFragment;
    private LoginBean.ProfileBean loginBean;
    private List<BaseFragment> fragments = new ArrayList<>();
    private MultiFragmentPagerAdapter pagerAdapter;
    private float deltaDistance;
    private float minDistance;
    private String coverUrl;
    private UserSearchBean.ResultBean.UserprofilesBean userSearchBean;

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_personal_info);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        pagerAdapter = new MultiFragmentPagerAdapter(getSupportFragmentManager());
        fragments.add(new UserPlaylistFragment());
        fragments.add(new UserDynamicsFragment());
        userInfoFragment = new UserInfoFragment();
        fragments.add(userInfoFragment);
        pagerAdapter.init(fragments);
    }

    @Override
    protected PersonalPresenter onCreatePresenter() {
        return new PersonalPresenter(this);
    }

    @Override
    protected void initModule() {
        ButterKnife.bind(this);
//        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        setBackBtn(getString(R.string.colorWhite));

        if (getIntent().getStringExtra(LOGIN_BEAN) != null) {
            //如果是查看本人的信息
            btnEdit.setVisibility(View.VISIBLE);
            String loginProfileBean = getIntent().getStringExtra(LOGIN_BEAN);
            loginBean = GsonUtil.fromJSON(loginProfileBean, LoginBean.ProfileBean.class);
            setMyInfoBean();
            mPresenter.getUserDetail(loginBean.getUserId());
        } else if (getIntent().getStringExtra(USER_BEAN) != null) {
            //如果是查看别人的信息
            userInfoFragment.setFragmentTitle(getString(R.string.about_ta));
            String otherProfileBean = getIntent().getStringExtra(USER_BEAN);
            userSearchBean = GsonUtil.fromJSON(otherProfileBean, UserSearchBean.ResultBean.UserprofilesBean.class);
            addDataToPersonalInfo();
            showDialog();
            mPresenter.getUserDetail(userSearchBean.getUserId());
        }

        vpContainer.setAdapter(pagerAdapter);
        vpContainer.setOffscreenPageLimit(3);
        vpContainer.setCurrentItem(0);
        pagerAdapter.getItem(0).setUserVisibleHint(true);
        tabTitle.setViewPager(vpContainer);

        minDistance = DensityUtil.dp2px(PersonalInfoActivity.this, 85);
        deltaDistance = DensityUtil.dp2px(PersonalInfoActivity.this, 200) - minDistance;
    }

    @SuppressLint("SetTextI18n")
    private void setMyInfoBean() {
        setLeftTitleText(loginBean.getNickname(), getString(R.string.colorWhite));
        setLeftTitleTextColorWhite();
        tvLike.setText(getString(R.string.follows) + loginBean.getFollows());
        tvFans.setText(getString(R.string.followeds) + loginBean.getFolloweds());
        Glide.with(this).load(loginBean.getAvatarUrl()).into(ivAvatar);
        coverUrl = loginBean.getBackgroundUrl();
        Glide.with(this)
                .load(coverUrl)
                .into(ivBgCover);
        Glide.with(this)
                .load(coverUrl)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 1)))
                .into(ivBg);
        tvNickName.setText(loginBean.getNickname());
        EventBus.getDefault().postSticky(new UidEvent(loginBean.getUserId(), loginBean.getNickname()));
    }

    @SuppressLint("SetTextI18n")
    private void addDataToPersonalInfo() {
        setLeftTitleText(userSearchBean.getNickname(), getString(R.string.colorWhite));
        setLeftTitleTextColorWhite();
        Glide.with(this).load(userSearchBean.getAvatarUrl()).into(ivAvatar);
        coverUrl = userSearchBean.getBackgroundUrl();
        Glide.with(this)
                .load(coverUrl)
                .into(ivBgCover);
        Glide.with(this)
                .load(coverUrl)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 1)))
                .into(ivBg);
        tvNickName.setText(userSearchBean.getNickname());
        EventBus.getDefault().postSticky(new UidEvent(userSearchBean.getUserId(), userSearchBean.getNickname()));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        appBar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.COLLAPSED) {
                    setLeftTitleAlpha(255f);
                } else if (state == State.EXPANDED) {
                    ivBgCover.setImageAlpha(255);
                    ivAvatar.setAlpha(255);
                    btnEdit.setAlpha(1);
                    tvNickName.setAlpha(1);
                    tvLike.setAlpha(1);
                    tvFans.setAlpha(1);
                }
            }

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout) {
                float alphaPercent = (rlInfo.getTop() - minDistance) / deltaDistance;
                int alpha = (int) (alphaPercent * 255);
                ivAvatar.setAlpha(alphaPercent);
                btnEdit.setAlpha(alphaPercent);
                ivBgCover.setImageAlpha(alpha);
                tvNickName.setAlpha(alphaPercent);
                tvLike.setAlpha(alphaPercent);
                tvFans.setAlpha(alphaPercent);
                if (alphaPercent < 0.2f) {
                    float leftTitleAlpha = (1.0f - alphaPercent / 0.2f);
                    setLeftTitleAlpha(leftTitleAlpha);
                } else {
                    setLeftTitleAlpha(0);
                }
            }
        });
    }

    @Override
    @OnClick({R.id.btn_edit, R.id.iv_avatar})
    public void onClick(View v) {
        if (ClickUtil.isFastClick(1000, v)) {
            return;
        }
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_edit:
                intent.setClass(PersonalInfoActivity.this, PersonalSettingActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_avatar:
                intent.setClass(PersonalInfoActivity.this, PictureCheckActivity.class);
                if (loginBean != null) {
                    intent.putExtra(PIC_URL, loginBean.getAvatarUrl());
                } else {
                    intent.putExtra(PIC_URL, userSearchBean.getAvatarUrl());
                }
                startActivity(intent);
                break;
            case R.id.tv_fans:
            case R.id.tv_like:
                ToastUtils.show("进入我的好友界面");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onGetUserPlaylistSuccess(UserPlaylistBean bean) {

    }

    @Override
    public void onGetUserPlaylistFail(String e) {

    }

    @Override
    public void onGetUserEventSuccess(UserEventBean bean) {

    }

    @Override
    public void onGetUserEventFail(String e) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onGetUserDetailSuccess(UserDetailBean bean) {
        hideDialog();
        LogUtil.d(TAG, "onGetUserDetailSuccess :" + bean);
        tvLike.setText(getString(R.string.follows) + bean.getProfile().getFollows());
        tvFans.setText(getString(R.string.followeds) + bean.getProfile().getFolloweds());
        EventBus.getDefault().postSticky(new UserDetailEvent(bean));
    }

    @Override
    public void onGetUserDetailFail(String e) {
        hideDialog();
        LogUtil.d(TAG, "onGetUserDetailFail :" + e);
        ToastUtils.show(e);
    }

}
