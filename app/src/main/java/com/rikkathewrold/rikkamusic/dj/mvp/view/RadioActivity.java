package com.rikkathewrold.rikkamusic.dj.mvp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.flyco.tablayout.SlidingTabLayout;
import com.hjq.toast.ToastUtils;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseActivity;
import com.rikkathewrold.rikkamusic.base.BaseFragment;
import com.rikkathewrold.rikkamusic.dj.bean.DjCategoryRecommendBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjCatelistBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjDetailBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjPaygiftBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjProgramBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjRecommendBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjRecommendTypeBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjSubBean;
import com.rikkathewrold.rikkamusic.dj.event.RidEvent;
import com.rikkathewrold.rikkamusic.dj.mvp.contract.DjContract;
import com.rikkathewrold.rikkamusic.dj.mvp.presenter.DjPresenter;
import com.rikkathewrold.rikkamusic.dj.mvp.view.fragments.RadioDetailFragment;
import com.rikkathewrold.rikkamusic.dj.mvp.view.fragments.RadioProgramFragment;
import com.rikkathewrold.rikkamusic.main.adapter.MultiFragmentPagerAdapter;
import com.rikkathewrold.rikkamusic.util.AppBarStateChangeListener;
import com.rikkathewrold.rikkamusic.util.ClickUtil;
import com.rikkathewrold.rikkamusic.util.DensityUtil;
import com.rikkathewrold.rikkamusic.util.LogUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * 歌单详情界面
 * 里面有歌曲
 */
public class RadioActivity extends BaseActivity<DjPresenter> implements DjContract.View {
    private static final String TAG = "RadioActivity";

    public static final String IS_SUB = "isSub";
    public static final String SUB_COUNT = "subCount";
    public static final String COVER_URL = "coverUrl";
    public static final String RADIO_NAME = "radioName";
    public static final String RID = "rid";

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_sub)
    TextView tvSub;
    @BindView(R.id.tv_has_sub)
    TextView tvHasSub;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.iv_cover)
    ImageView ivDj;
    @BindView(R.id.iv_dj_cover)
    ImageView ivDjCover;
    @BindView(R.id.vp_container)
    ViewPager vpContainer;
    @BindView(R.id.tab_title)
    SlidingTabLayout tabTitle;
    @BindView(R.id.rl_info)
    RelativeLayout rlInfo;
    @BindView(R.id.appbar)
    AppBarLayout appBar;

    private MultiFragmentPagerAdapter pagerAdapter;
    private List<BaseFragment> fragments = new ArrayList<>();
    private float minDistance, deltaDistance;
    private long rid;
    private boolean isSub;

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_radio_detail);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        pagerAdapter = new MultiFragmentPagerAdapter(getSupportFragmentManager());
        fragments.add(new RadioDetailFragment());
        fragments.add(new RadioProgramFragment());
        pagerAdapter.init(fragments);
    }

    @Override
    protected DjPresenter onCreatePresenter() {
        return new DjPresenter(this);
    }

    @Override
    protected void initModule() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        setBackBtn(getString(R.string.colorWhite));

        Intent intent = getIntent();
        if (getIntent() != null) {
            Glide.with(this)
                    .load(intent.getStringExtra(COVER_URL))
                    .transition(new DrawableTransitionOptions().crossFade())
                    .into(ivDjCover);
            Glide.with(this)
                    .load(intent.getStringExtra(COVER_URL))
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 1)))
                    .into(ivDj);
            tvName.setText(intent.getStringExtra(RADIO_NAME));
            setLeftTitleText(getIntent().getStringExtra(RADIO_NAME), getString(R.string.colorWhite));

            String subCount;
            long count = intent.getIntExtra(SUB_COUNT, 0);
            if (count >= 10000) {
                count = count / 10000;
                subCount = count + "万";
            } else {
                subCount = count + "";
            }
            tvInfo.setText(subCount + "人已订阅");
            rid = intent.getLongExtra(RID, 0);
            isSub = intent.getBooleanExtra(IS_SUB, false);
            if (isSub) {
                tvHasSub.setVisibility(View.VISIBLE);
                tvSub.setVisibility(View.GONE);
            } else {
                tvHasSub.setVisibility(View.GONE);
                tvSub.setVisibility(View.VISIBLE);
            }

            vpContainer.setAdapter(pagerAdapter);
            vpContainer.setOffscreenPageLimit(2);
            vpContainer.setCurrentItem(0, true);
            pagerAdapter.getItem(0).setUserVisibleHint(true);
            tabTitle.setViewPager(vpContainer);

            minDistance = DensityUtil.dp2px(RadioActivity.this, 85);
            deltaDistance = DensityUtil.dp2px(RadioActivity.this, 250) - minDistance;

            EventBus.getDefault().postSticky(new RidEvent(rid));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        appBar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, AppBarStateChangeListener.State state) {
                if (state == State.COLLAPSED) {
                    setLeftTitleAlpha(255f);
                } else if (state == State.EXPANDED) {
                    tvName.setAlpha(1f);
                }
            }

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout) {
                float alphaPercent = (rlInfo.getTop() - minDistance) / deltaDistance;
                tvName.setAlpha(alphaPercent);
                tvInfo.setAlpha(alphaPercent);
                tvSub.setAlpha(alphaPercent);
                tvHasSub.setAlpha(alphaPercent);
                ivDjCover.setImageAlpha((int) (alphaPercent * 255));
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
    @OnClick({R.id.tv_sub, R.id.tv_has_sub})
    public void onClick(View v) {
        if (ClickUtil.isFastClick(500, v)) {
            return;
        }
        switch (v.getId()) {
            case R.id.tv_sub:
                showDialog();
                mPresenter.subDj(rid, 1);
                break;
            case R.id.tv_has_sub:
                showDialog();
                mPresenter.subDj(rid, 0);
                break;
        }
    }

    @Override
    public void onGetDjRecommendSuccess(DjRecommendBean bean) {

    }

    @Override
    public void onGetDjRecommendFail(String e) {

    }

    @Override
    public void onGetDjPaygiftSuccess(DjPaygiftBean bean) {

    }

    @Override
    public void onGetDjPaygiftFail(String e) {

    }

    @Override
    public void onGetDjRecommendTypeSuccess(DjRecommendTypeBean bean) {

    }

    @Override
    public void onGetDjRecommendTypeFail(String e) {

    }

    @Override
    public void onGetDjCategoryRecommendSuccess(DjCategoryRecommendBean bean) {

    }

    @Override
    public void onGetDjCategoryRecommendFail(String e) {

    }

    @Override
    public void onGetDjCatelistSuccess(DjCatelistBean bean) {

    }

    @Override
    public void onGetDjCatelistFail(String e) {

    }

    @Override
    public void onSubDjSuccess(DjSubBean bean) {
        hideDialog();
        LogUtil.d(TAG, "onSubDjSuccess :" + bean);
        if (bean.getCode() == 200) {
            isSub = !isSub;
            if (isSub) {
                tvHasSub.setVisibility(View.VISIBLE);
                tvSub.setVisibility(View.GONE);
            } else {
                tvHasSub.setVisibility(View.GONE);
                tvSub.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onSubDjFail(String e) {
        hideDialog();
        LogUtil.e(TAG, "onSubDjFail:" + e);
        ToastUtils.show(e);
    }

    @Override
    public void onGetDjProgramSuccess(DjProgramBean bean) {

    }

    @Override
    public void onGetDjProgramFail(String e) {

    }

    @Override
    public void onGetDjDetailSuccess(DjDetailBean bean) {

    }

    @Override
    public void onGetDjDetailFail(String e) {

    }
}
