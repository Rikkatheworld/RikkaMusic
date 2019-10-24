package com.rikkathewrold.rikkamusic.main.mvp.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.hjq.toast.ToastUtils;
import com.lzx.starrysky.model.SongInfo;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseActivity;
import com.rikkathewrold.rikkamusic.database.DailyRecommendDaoOp;
import com.rikkathewrold.rikkamusic.login.bean.LoginBean;
import com.rikkathewrold.rikkamusic.main.adapter.SongListAdapter;
import com.rikkathewrold.rikkamusic.main.bean.BannerBean;
import com.rikkathewrold.rikkamusic.main.bean.DRGreenDaoBean;
import com.rikkathewrold.rikkamusic.main.bean.DailyRecommendBean;
import com.rikkathewrold.rikkamusic.main.bean.HighQualityPlayListBean;
import com.rikkathewrold.rikkamusic.main.bean.MainRecommendPlayListBean;
import com.rikkathewrold.rikkamusic.main.bean.PlaylistDetailBean;
import com.rikkathewrold.rikkamusic.main.bean.RecommendPlayListBean;
import com.rikkathewrold.rikkamusic.main.bean.TopListBean;
import com.rikkathewrold.rikkamusic.main.mvp.contract.WowContract;
import com.rikkathewrold.rikkamusic.main.mvp.presenter.WowPresenter;
import com.rikkathewrold.rikkamusic.manager.SongPlayManager;
import com.rikkathewrold.rikkamusic.manager.bean.MusicCanPlayBean;
import com.rikkathewrold.rikkamusic.util.AppBarStateChangeListener;
import com.rikkathewrold.rikkamusic.util.DensityUtil;
import com.rikkathewrold.rikkamusic.util.GsonUtil;
import com.rikkathewrold.rikkamusic.util.LogUtil;
import com.rikkathewrold.rikkamusic.util.SharePreferenceUtil;
import com.rikkathewrold.rikkamusic.util.TimeUtil;
import com.rikkathewrold.rikkamusic.widget.BottomSongPlayBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * 日推界面
 */
public class DailyRecommendActivity extends BaseActivity<WowPresenter> implements WowContract.View {
    private static final String TAG = "DailyRecommendActivity";

    //计算完成后发送的Handler msg
    private static final int COMPLETED = 0;

    @BindView(R.id.rv_dailyrecommend)
    RecyclerView rvDailyRecommend;
    @BindView(R.id.bottom_controller)
    BottomSongPlayBar bottomController;
    @BindView(R.id.tv_day)
    TextView tvDay;
    @BindView(R.id.tv_month)
    TextView tvMonth;
    @BindView(R.id.iv_background)
    ImageView ivBg;
    @BindView(R.id.iv_background_cover)
    ImageView ivBgCover;
    @BindView(R.id.appbar)
    AppBarLayout appBar;
    @BindView(R.id.rl_play)
    RelativeLayout rlPlay;


    //日推集合
    private List<DailyRecommendBean.RecommendBean> dailyList = new ArrayList<>();
    private List<SongInfo> songInfos = new ArrayList<>();
    private List<DRGreenDaoBean> greenDaoList = new ArrayList<>();
    private SongListAdapter songAdapter;
    int deltaDistance;
    int minDistance;

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_daily_recommend);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    @Override
    protected WowPresenter onCreatePresenter() {
        return new WowPresenter(this);
    }

    @Override
    protected void initModule() {
        ButterKnife.bind(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        setLeftTitleText(R.string.day_recommend);
        setBackBtn(getString(R.string.colorWhite));
        dailyList.clear();

        songAdapter = new SongListAdapter(this);
        songAdapter.setType(1);
        rvDailyRecommend.setLayoutManager(new LinearLayoutManager(this));
        rvDailyRecommend.setAdapter(songAdapter);

        String coverUrl = GsonUtil.fromJSON(SharePreferenceUtil.getInstance(this).getUserInfo(""), LoginBean.class).getProfile().getBackgroundUrl();
        if (coverUrl != null) {
            Glide.with(this)
                    .load(coverUrl)
                    .transition(new DrawableTransitionOptions().crossFade())
                    .into(ivBgCover);
            Glide.with(this)
                    .load(coverUrl)
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 1)))
                    .transition(new DrawableTransitionOptions().crossFade())
                    .into(ivBg);
        }
        tvDay.setText(TimeUtil.getDay(System.currentTimeMillis()));
        tvMonth.setText("/" + TimeUtil.getMonth(System.currentTimeMillis()));

        long updateTime = SharePreferenceUtil.getInstance(this).getDailyUpdateTime();
        LogUtil.d(TAG, "上次日推更新时间： " + TimeUtil.getTimeStandard(updateTime));
        //上次更新日推时间小于当天7点，则更新日推
        if (!TimeUtil.isOver7am(updateTime)) {
            DailyRecommendDaoOp.deleteAllData(this);
            LogUtil.d(TAG, "getDailyRecommend");
            showDialog();
            mPresenter.getDailyRecommend();
        } else {
            //从GreenDao里面取出日推
            greenDaoList = DailyRecommendDaoOp.queryAll(this);
            if (greenDaoList != null) {
                notifyAdapter(greenDaoList);
            } else {
                DailyRecommendDaoOp.deleteAllData(this);
                showDialog();
                mPresenter.getDailyRecommend();
            }
        }

        minDistance = DensityUtil.dp2px(DailyRecommendActivity.this, 85);
        deltaDistance = DensityUtil.dp2px(DailyRecommendActivity.this, 200) - minDistance;
    }

    private void notifyAdapter(List<DRGreenDaoBean> greenDaoList) {
        songInfos.clear();
        for (int i = 0; i < greenDaoList.size(); i++) {
            SongInfo songInfo = new SongInfo();
            songInfo.setSongCover(greenDaoList.get(i).getSongCover());
            songInfo.setSongName(greenDaoList.get(i).getSongName());
            songInfo.setDuration(greenDaoList.get(i).getDuration());
            songInfo.setArtist(greenDaoList.get(i).getArtist());
            songInfo.setSongId(greenDaoList.get(i).getSongId());
            songInfo.setSongUrl(greenDaoList.get(i).getSongUrl());
            songInfo.setArtistId(greenDaoList.get(i).getArtistId());
            songInfo.setArtistKey(greenDaoList.get(i).getArtistAvatar());
            songInfos.add(songInfo);
        }
        songAdapter.notifyDataSetChanged(songInfos);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initAppBarLayoutListener();
    }

    //根据AppBarLayout的滑动来模糊图片
    private void initAppBarLayoutListener() {
        appBar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, AppBarStateChangeListener.State state) {
                if (state == State.COLLAPSED) {
                    setLeftTitleAlpha(255f);
                }
            }

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout) {
                float alphaPercent = (float) (rlPlay.getTop() - minDistance) / (float) deltaDistance;
                int alpha = (int) (alphaPercent * 255);
                LogUtil.d(TAG, "alpha : " + alpha);
                ivBgCover.setImageAlpha(alpha);
                tvMonth.setAlpha(alphaPercent);
                tvDay.setAlpha(alphaPercent);
                setLeftTitleTextColorWhite();
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
    @OnClick({R.id.rl_playall})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_playall:
                if (songInfos != null && !songInfos.isEmpty()) {
                    SongPlayManager.getInstance().clickPlayAll(songInfos, 0);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
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
        hideDialog();
        LogUtil.d(TAG, "onGetDailyRecommendSuccess : " + bean);
        SharePreferenceUtil.getInstance(this).saveDailyUpdateTime(System.currentTimeMillis());
        dailyList.addAll(bean.getRecommend());

        greenDaoList.clear();
        for (int i = 0; i < dailyList.size(); i++) {
            DRGreenDaoBean listInfo = new DRGreenDaoBean();
            listInfo.setSongId(String.valueOf(bean.getRecommend().get(i).getId()));
            listInfo.setSongName(bean.getRecommend().get(i).getName());
            listInfo.setArtist(bean.getRecommend().get(i).getArtists().get(0).getName());
            listInfo.setSongCover(bean.getRecommend().get(i).getAlbum().getPicUrl());
            listInfo.setSongUrl(SONG_URL + bean.getRecommend().get(i).getId() + ".mp3");
            listInfo.setDuration(bean.getRecommend().get(i).getDuration());
            listInfo.setArtistId(String.valueOf(bean.getRecommend().get(i).getArtists().get(0).getId()));
            listInfo.setArtistAvatar(bean.getRecommend().get(i).getArtists().get(0).getPicUrl());
            greenDaoList.add(listInfo);
        }

        //先删除GreenDao里的所有数据，再将日推存储到GreenDao
        DailyRecommendDaoOp.saveData(this, greenDaoList);

        notifyAdapter(greenDaoList);
    }

    @Override
    public void onGetDailyRecommendFail(String e) {
        hideDialog();
        LogUtil.e(TAG, "onGetDailyRecommendFail : " + e);
        ToastUtils.show(e);
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

    }

    @Override
    public void onGetHighQualityFail(String e) {

    }


}
