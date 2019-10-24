package com.rikkathewrold.rikkamusic.dj.mvp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gyf.immersionbar.ImmersionBar;
import com.hjq.toast.ToastUtils;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseActivity;
import com.rikkathewrold.rikkamusic.dj.bean.DjCategoryRecommendBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjCatelistBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjDetailBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjPaygiftBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjProgramBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjRecommendBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjRecommendTypeBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjSubBean;
import com.rikkathewrold.rikkamusic.dj.mvp.contract.DjContract;
import com.rikkathewrold.rikkamusic.dj.mvp.presenter.DjPresenter;
import com.rikkathewrold.rikkamusic.main.adapter.PlayListAdapter;
import com.rikkathewrold.rikkamusic.main.bean.PlaylistBean;
import com.rikkathewrold.rikkamusic.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 电台主界面
 * type 2001 创作|翻唱 10002 3D|电子
 * 3 情感调频    2  音乐故事
 * 3001 二次元   10001 有声书
 * 453050 知识技能 453051 商业财经
 * 11 人文历史   13 外语世界
 * 14 亲子宝贝   8 相声曲艺
 * 6 美文读物    5 脱口秀
 * 7 广播剧      1 明星做主播
 * 4 娱乐|影视   453052 科技科学
 * 4001 校园|教育 12 旅途|城市
 */
public class RadioRecommendActivity extends BaseActivity<DjPresenter> implements DjContract.View {
    private static final String TAG = "RadioRecommendActivity";

    @BindView(R.id.rv_recommend_dj)
    RecyclerView rvRecDj;
    @BindView(R.id.rv_paygift_dj)
    RecyclerView rvPaygift;
    @BindView(R.id.rv_cover_dj)
    RecyclerView rvCover;
    @BindView(R.id.rv_voice_book_dj)
    RecyclerView rvVoiceBook;
    @BindView(R.id.rv_motion_dj)
    RecyclerView rvMotion;
    @BindView(R.id.rv_broadcast_dj)
    RecyclerView rvBroadcast;
    @BindView(R.id.rv_music_story_dj)
    RecyclerView rvMusicStory;
    @BindView(R.id.rv_entertainment_dj)
    RecyclerView rvEntertainment;
    @BindView(R.id.rv_3d_dj)
    RecyclerView rvElectric;
    @BindView(R.id.rv_beautiful_dj)
    RecyclerView rvBeautiful;
    @BindView(R.id.rv_anim_dj)
    RecyclerView rvAnim;
    @BindView(R.id.rv_show_dj)
    RecyclerView rvShow;

    private PlayListAdapter djRecAdapter, djPaygiftAdapter, coverAdapter, voiceBookAdapter;
    private PlayListAdapter motionAdapter, broadcastAdapter, musicStoryAdapter, entertainmentAdapter;
    private PlayListAdapter beautifulAdapter, animAdapter, showAdapter, electricAdapter;
    private List<DjRecommendBean.DjRadiosBean> djRecRadios = new ArrayList<>();
    private List<PlaylistBean> recList = new ArrayList<>();
    private List<PlaylistBean> paygiftList = new ArrayList<>();
    private int recIndex;

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_radio);

        ImmersionBar.with(this)
                .transparentBar()
                .statusBarColor(R.color.colorPrimary)
                .statusBarDarkFont(false)
                .init();
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
        setLeftTitleText(getString(R.string.radio), getString(R.string.colorWhite));

        djRecAdapter = new PlayListAdapter(this);
        GridLayoutManager recManager = new GridLayoutManager(this, 3);
        rvRecDj.setLayoutManager(recManager);
        rvRecDj.setAdapter(djRecAdapter);
        djRecAdapter.setListener(recListener);

        djPaygiftAdapter = new PlayListAdapter(this);
        GridLayoutManager paygiftManager = new GridLayoutManager(this, 3);
        rvPaygift.setLayoutManager(paygiftManager);
        rvPaygift.setAdapter(djPaygiftAdapter);
        djPaygiftAdapter.setListener(paygiftListener);

        coverAdapter = new PlayListAdapter(this);
        rvCover.setLayoutManager(new GridLayoutManager(this, 3));
        rvCover.setAdapter(coverAdapter);

        voiceBookAdapter = new PlayListAdapter(this);
        rvVoiceBook.setLayoutManager(new GridLayoutManager(this, 3));
        rvVoiceBook.setAdapter(voiceBookAdapter);

        motionAdapter = new PlayListAdapter(this);
        rvMotion.setLayoutManager(new GridLayoutManager(this, 3));
        rvMotion.setAdapter(motionAdapter);

        broadcastAdapter = new PlayListAdapter(this);
        rvBroadcast.setLayoutManager(new GridLayoutManager(this, 3));
        rvBroadcast.setAdapter(broadcastAdapter);

        musicStoryAdapter = new PlayListAdapter(this);
        rvMusicStory.setLayoutManager(new GridLayoutManager(this, 3));
        rvMusicStory.setAdapter(musicStoryAdapter);

        entertainmentAdapter = new PlayListAdapter(this);
        rvEntertainment.setLayoutManager(new GridLayoutManager(this, 3));
        rvEntertainment.setAdapter(entertainmentAdapter);

        beautifulAdapter = new PlayListAdapter(this);
        rvBeautiful.setLayoutManager(new GridLayoutManager(this, 3));
        rvBeautiful.setAdapter(beautifulAdapter);

        animAdapter = new PlayListAdapter(this);
        rvAnim.setLayoutManager(new GridLayoutManager(this, 3));
        rvAnim.setAdapter(animAdapter);

        showAdapter = new PlayListAdapter(this);
        rvShow.setLayoutManager(new GridLayoutManager(this, 3));
        rvShow.setAdapter(showAdapter);

        electricAdapter = new PlayListAdapter(this);
        rvElectric.setLayoutManager(new GridLayoutManager(this, 3));
        rvElectric.setAdapter(electricAdapter);

        showDialog();
        mPresenter.getDjRecommend();
        mPresenter.getDjPaygift(3, 0);
        mPresenter.getDjCategoryRecommend();
    }

    PlayListAdapter.OnPlayListClickListener recListener = position -> {
        DjRecommendBean.DjRadiosBean djRadiosBean = new DjRecommendBean.DjRadiosBean();
        for (int i = 0; i < djRecRadios.size(); i++) {
            if (recList.get(position).getPlaylistName().equals(djRecRadios.get(i).getName())) {
                djRadiosBean = djRecRadios.get(i);
            }
        }
        Intent intent = new Intent(RadioRecommendActivity.this, RadioActivity.class);
        intent.putExtra(RadioActivity.SUB_COUNT, djRadiosBean.getSubCount());
        intent.putExtra(RadioActivity.RADIO_NAME, djRadiosBean.getName());
        intent.putExtra(RadioActivity.RID, djRadiosBean.getId());
        intent.putExtra(RadioActivity.IS_SUB, false);
        intent.putExtra(RadioActivity.COVER_URL, djRadiosBean.getPicUrl());
        startActivity(intent);
    };

    PlayListAdapter.OnPlayListClickListener paygiftListener = position -> {
        ToastUtils.show("我没有钱");
    };


    @Override
    @OnClick({R.id.rl_dj_rank, R.id.tv_voice_book, R.id.tv_cover, R.id.tv_dj_playground, R.id.tv_dj_paygift,
            R.id.rl_dj_hq, R.id.tv_motion, R.id.tv_broadcast, R.id.tv_music_story, R.id.tv_entertainment,
            R.id.tv_3d, R.id.tv_beautiful, R.id.tv_show, R.id.tv_anim, R.id.rl_dj_cat})
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.rl_dj_rank:
                ToastUtils.show("这版不做!");
                break;
            case R.id.tv_dj_playground:
                replaceRecDj();
                break;
            case R.id.rl_dj_cat:
                intent.setClass(RadioRecommendActivity.this, RadioCatActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_dj_hq:
            case R.id.tv_dj_paygift:
                intent.setClass(RadioRecommendActivity.this, RadioListActivity.class);
                intent.putExtra(RadioListActivity.TYPE, 0);
                intent.putExtra(RadioListActivity.TITLE_NAME, "付费精选");
                startActivity(intent);
                break;
            case R.id.tv_cover:
                intent.setClass(RadioRecommendActivity.this, RadioListActivity.class);
                intent.putExtra(RadioListActivity.TYPE, 2001);
                intent.putExtra(RadioListActivity.TITLE_NAME, "创作|翻唱");
                startActivity(intent);
                break;
            case R.id.tv_voice_book:
                intent.setClass(RadioRecommendActivity.this, RadioListActivity.class);
                intent.putExtra(RadioListActivity.TYPE, 10001);
                intent.putExtra(RadioListActivity.TITLE_NAME, "有声书");
                startActivity(intent);
                break;
            case R.id.tv_motion:
                intent.setClass(RadioRecommendActivity.this, RadioListActivity.class);
                intent.putExtra(RadioListActivity.TYPE, 3);
                intent.putExtra(RadioListActivity.TITLE_NAME, "情感频道");
                startActivity(intent);
                break;
            case R.id.tv_broadcast:
                intent.setClass(RadioRecommendActivity.this, RadioListActivity.class);
                intent.putExtra(RadioListActivity.TYPE, 7);
                intent.putExtra(RadioListActivity.TITLE_NAME, "广播剧");
                startActivity(intent);
                break;
            case R.id.tv_music_story:
                intent.setClass(RadioRecommendActivity.this, RadioListActivity.class);
                intent.putExtra(RadioListActivity.TYPE, 2);
                intent.putExtra(RadioListActivity.TITLE_NAME, "音乐故事");
                startActivity(intent);
                break;
            case R.id.tv_entertainment:
                intent.setClass(RadioRecommendActivity.this, RadioListActivity.class);
                intent.putExtra(RadioListActivity.TYPE, 4);
                intent.putExtra(RadioListActivity.TITLE_NAME, "娱乐|影视");
                startActivity(intent);
                break;
            case R.id.tv_3d:
                intent.setClass(RadioRecommendActivity.this, RadioListActivity.class);
                intent.putExtra(RadioListActivity.TYPE, 10002);
                intent.putExtra(RadioListActivity.TITLE_NAME, "3D|电子");
                startActivity(intent);
                break;
            case R.id.tv_beautiful:
                intent.setClass(RadioRecommendActivity.this, RadioListActivity.class);
                intent.putExtra(RadioListActivity.TYPE, 6);
                intent.putExtra(RadioListActivity.TITLE_NAME, "美文读物");
                startActivity(intent);
                break;
            case R.id.tv_anim:
                intent.setClass(RadioRecommendActivity.this, RadioListActivity.class);
                intent.putExtra(RadioListActivity.TYPE, 3001);
                intent.putExtra(RadioListActivity.TITLE_NAME, "二次元");
                startActivity(intent);
                break;
            case R.id.tv_show:
                intent.setClass(RadioRecommendActivity.this, RadioListActivity.class);
                intent.putExtra(RadioListActivity.TYPE, 5);
                intent.putExtra(RadioListActivity.TITLE_NAME, "脱口秀");
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onGetDjRecommendSuccess(DjRecommendBean bean) {
        hideDialog();
        LogUtil.d(TAG, "onGetDjRecommendSuccess : " + bean);
        djRecRadios.clear();
        djRecRadios = bean.getDjRadios();
        recList.clear();
        for (int i = 0; i < 3; i++) {
            PlaylistBean playlistBean = new PlaylistBean();
            playlistBean.setPlaylistName(djRecRadios.get(i).getName());
            playlistBean.setPlaylistCoverUrl(djRecRadios.get(i).getPicUrl());
            recList.add(playlistBean);
        }
        recIndex = 3;
        djRecAdapter.notifyDataSetChanged(recList);
    }

    private void replaceRecDj() {
        int count = 2;
        int i;
        recList.clear();
        for (; count >= 0; count--) {
            PlaylistBean playlistBean = new PlaylistBean();
            if (recIndex > djRecRadios.size() - 1) {
                i = recIndex % djRecRadios.size();
            } else {
                i = recIndex;
            }
            playlistBean.setPlaylistName(djRecRadios.get(i).getName());
            playlistBean.setPlaylistCoverUrl(djRecRadios.get(i).getPicUrl());
            recList.add(playlistBean);
            recIndex++;
        }
        djRecAdapter.notifyDataSetChanged(recList);
    }

    @Override
    public void onGetDjRecommendFail(String e) {
        hideDialog();
        LogUtil.e(TAG, "onGetDjRecommendFail");
        ToastUtils.show(e);
    }

    @Override
    public void onGetDjPaygiftSuccess(DjPaygiftBean bean) {
        hideDialog();
        paygiftList.clear();
        List<DjPaygiftBean.DataBean.ListBean> list = bean.getData().getList();
        for (int i = 0; i < list.size(); i++) {
            PlaylistBean playlistBean = new PlaylistBean();
            playlistBean.setPlaylistName(list.get(i).getName());
            playlistBean.setPlaylistCoverUrl(list.get(i).getPicUrl());
            paygiftList.add(playlistBean);
        }
        djPaygiftAdapter.notifyDataSetChanged(paygiftList);
    }

    @Override
    public void onGetDjPaygiftFail(String e) {
        hideDialog();
        LogUtil.e(TAG, "onGetDjPaygiftFail : " + e);
        ToastUtils.show(e);
    }

    @Override
    public void onGetDjRecommendTypeSuccess(DjRecommendTypeBean bean) {
        hideDialog();
    }

    private void notifyAdapter(List<DjCategoryRecommendBean.DataBean.RadiosBean> djRadios, int type) {
        List<PlaylistBean> playlistBeans = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            PlaylistBean playlistBean = new PlaylistBean();
            playlistBean.setPlaylistName(djRadios.get(i).getName());
            playlistBean.setPlaylistCoverUrl(djRadios.get(i).getPicUrl());
            playlistBeans.add(playlistBean);
        }
        switch (type) {
            case 2001:
                coverAdapter.notifyDataSetChanged(playlistBeans);
                coverAdapter.setListener(position -> {
                    toRadioActivity(djRadios.get(position));
                });
                break;
            case 10001:
                voiceBookAdapter.notifyDataSetChanged(playlistBeans);
                voiceBookAdapter.setListener(position -> {
                    toRadioActivity(djRadios.get(position));
                });
                break;
            case 3:
                motionAdapter.notifyDataSetChanged(playlistBeans);
                motionAdapter.setListener(position -> {
                    toRadioActivity(djRadios.get(position));
                });
                break;
            case 7:
                broadcastAdapter.notifyDataSetChanged(playlistBeans);
                broadcastAdapter.setListener(position -> {
                    toRadioActivity(djRadios.get(position));
                });
                break;
            case 2:
                musicStoryAdapter.notifyDataSetChanged(playlistBeans);
                musicStoryAdapter.setListener(position -> {
                    toRadioActivity(djRadios.get(position));
                });
                break;
            case 4:
                entertainmentAdapter.notifyDataSetChanged(playlistBeans);
                entertainmentAdapter.setListener(position -> {
                    toRadioActivity(djRadios.get(position));
                });
                break;
            case 10002:
                electricAdapter.notifyDataSetChanged(playlistBeans);
                electricAdapter.setListener(position -> {
                    toRadioActivity(djRadios.get(position));
                });
                break;
            case 6:
                beautifulAdapter.notifyDataSetChanged(playlistBeans);
                beautifulAdapter.setListener(position -> {
                    toRadioActivity(djRadios.get(position));
                });
                break;
            case 3001:
                animAdapter.notifyDataSetChanged(playlistBeans);
                animAdapter.setListener(position -> {
                    toRadioActivity(djRadios.get(position));
                });
                break;
            case 5:
                showAdapter.notifyDataSetChanged(playlistBeans);
                showAdapter.setListener(position -> {
                    toRadioActivity(djRadios.get(position));
                });
                break;
        }
    }

    private void toRadioActivity(DjCategoryRecommendBean.DataBean.RadiosBean radioBean) {
        Intent intent = new Intent(RadioRecommendActivity.this, RadioActivity.class);
        intent.putExtra(RadioActivity.SUB_COUNT, 0);
        intent.putExtra(RadioActivity.RADIO_NAME, radioBean.getName());
        intent.putExtra(RadioActivity.RID, radioBean.getId());
        intent.putExtra(RadioActivity.IS_SUB, false);
        intent.putExtra(RadioActivity.COVER_URL, radioBean.getPicUrl());
        startActivity(intent);
    }


    @Override
    public void onGetDjRecommendTypeFail(String e) {
        hideDialog();
        LogUtil.e(TAG, "onGetDjRecommendTypeFail :" + e);
        ToastUtils.show(e);
    }

    @Override
    public void onGetDjCategoryRecommendSuccess(DjCategoryRecommendBean bean) {
        hideDialog();
        LogUtil.d(TAG, "onGetDjCategoryRecommendSuccess : " + bean);
        for (int i = 0; i < bean.getData().size(); i++) {
            notifyAdapter(bean.getData().get(i).getRadios(), bean.getData().get(i).getCategoryId());
        }
    }

    @Override
    public void onGetDjCategoryRecommendFail(String e) {
        hideDialog();
        LogUtil.d(TAG, "onGetDjCategoryRecommendFail : " + e);
        ToastUtils.show(e);
    }

    @Override
    public void onGetDjCatelistSuccess(DjCatelistBean bean) {

    }

    @Override
    public void onGetDjCatelistFail(String e) {

    }

    @Override
    public void onSubDjSuccess(DjSubBean bean) {

    }

    @Override
    public void onSubDjFail(String e) {

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
