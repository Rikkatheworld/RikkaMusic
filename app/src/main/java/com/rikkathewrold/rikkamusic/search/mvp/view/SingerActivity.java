package com.rikkathewrold.rikkamusic.search.mvp.view;

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
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseActivity;
import com.rikkathewrold.rikkamusic.base.BaseFragment;
import com.rikkathewrold.rikkamusic.main.adapter.MultiFragmentPagerAdapter;
import com.rikkathewrold.rikkamusic.search.bean.FeedSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.SimiSingerBean;
import com.rikkathewrold.rikkamusic.search.bean.SingerAblumSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.SingerDescriptionBean;
import com.rikkathewrold.rikkamusic.search.bean.SingerSongSearchBean;
import com.rikkathewrold.rikkamusic.search.event.SingIdEvent;
import com.rikkathewrold.rikkamusic.search.mvp.contract.SingerContract;
import com.rikkathewrold.rikkamusic.search.mvp.presenter.SingerPresenter;
import com.rikkathewrold.rikkamusic.search.mvp.view.fragments.SingerAlbumSearchFragment;
import com.rikkathewrold.rikkamusic.search.mvp.view.fragments.SingerFeedSearchFragment;
import com.rikkathewrold.rikkamusic.search.mvp.view.fragments.SingerInfoSearchFragment;
import com.rikkathewrold.rikkamusic.search.mvp.view.fragments.SingerSongSearchFragment;
import com.rikkathewrold.rikkamusic.util.AppBarStateChangeListener;
import com.rikkathewrold.rikkamusic.util.DensityUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * 歌手界面，下面有4个Fragment，分别是单曲、专辑、视频和个人信息
 */
public class SingerActivity extends BaseActivity<SingerPresenter> implements SingerContract.View {
    private static final String TAG = "SingerActivity";
    public static final String SINGER_ID = "singerId";
    public static final String SINGER_PICURL = "singerPicUrl";
    public static final String SINGER_NAME = "singerName";

    @BindView(R.id.iv_singer)
    ImageView ivSinger;
    @BindView(R.id.iv_singer_cover)
    ImageView ivSingerCover;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.appbar)
    AppBarLayout appBar;
    @BindView(R.id.vp_container)
    ViewPager vpContainer;
    @BindView(R.id.rl_info)
    RelativeLayout rlInfo;
    @BindView(R.id.tab_title)
    SlidingTabLayout tabTitle;

    private List<BaseFragment> fragments = new ArrayList<>();
    private long singId;
    private float minDistance, deltaDistance;
    private MultiFragmentPagerAdapter pagerAdapter;

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_singer);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        pagerAdapter = new MultiFragmentPagerAdapter(getSupportFragmentManager());
        fragments.add(new SingerSongSearchFragment());
        fragments.add(new SingerAlbumSearchFragment());
        fragments.add(new SingerFeedSearchFragment());
        fragments.add(new SingerInfoSearchFragment());
        pagerAdapter.init(fragments);
    }

    @Override
    protected SingerPresenter onCreatePresenter() {
        return new SingerPresenter(this);
    }

    @Override
    protected void initModule() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        setBackBtn(getString(R.string.colorWhite));

        if (getIntent() != null) {
            Glide.with(this).load(getIntent().getStringExtra(SINGER_PICURL)).transition(new DrawableTransitionOptions().crossFade()).into(ivSingerCover);
            Glide.with(this)
                    .load(getIntent().getStringExtra(SINGER_PICURL))
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 1)))
                    .into(ivSinger);
            tvName.setText(getIntent().getStringExtra(SINGER_NAME));
            singId = getIntent().getLongExtra(SINGER_ID, -1);
            setLeftTitleText(getIntent().getStringExtra(SINGER_NAME), getString(R.string.colorWhite));
            setLeftTitleTextColorWhite();

            if (singId != -1) {
                EventBus.getDefault().postSticky(new SingIdEvent(singId, tvName.getText().toString().trim()));
            }
            vpContainer.setAdapter(pagerAdapter);
            vpContainer.setOffscreenPageLimit(3);
            vpContainer.setCurrentItem(0);
            pagerAdapter.getItem(0).setUserVisibleHint(true);
            tabTitle.setViewPager(vpContainer);

            minDistance = DensityUtil.dp2px(SingerActivity.this, 85);
            deltaDistance = DensityUtil.dp2px(SingerActivity.this, 250) - minDistance;
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
                ivSingerCover.setImageAlpha((int) (alphaPercent * 255));
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
    public void onClick(View v) {

    }

    @Override
    public void onGetSingerHotSongSuccess(SingerSongSearchBean bean) {

    }

    @Override
    public void onGetSingerHotSongFail(String e) {

    }

    @Override
    public void onGetSingerAlbumSuccess(SingerAblumSearchBean bean) {

    }

    @Override
    public void onGetSingerAlbumFail(String e) {

    }

    @Override
    public void onGetFeedSearchSuccess(FeedSearchBean bean) {

    }

    @Override
    public void onGetFeedSearchFail(String e) {

    }

    @Override
    public void onGetSingerDescSuccess(SingerDescriptionBean bean) {

    }

    @Override
    public void onGetSingerDescFail(String e) {

    }

    @Override
    public void onGetSimiSingerSuccess(SimiSingerBean bean) {

    }

    @Override
    public void onGetSimiSingerFail(String e) {

    }
}
