package com.rikkathewrold.rikkamusic.search.mvp.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.rikkathewrold.rikkamusic.App;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseFragment;
import com.rikkathewrold.rikkamusic.search.adapter.SimiSingerAdapter;
import com.rikkathewrold.rikkamusic.search.bean.FeedSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.SimiSingerBean;
import com.rikkathewrold.rikkamusic.search.bean.SingerAblumSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.SingerDescriptionBean;
import com.rikkathewrold.rikkamusic.search.bean.SingerSongSearchBean;
import com.rikkathewrold.rikkamusic.search.event.SingIdEvent;
import com.rikkathewrold.rikkamusic.search.mvp.contract.SingerContract;
import com.rikkathewrold.rikkamusic.search.mvp.presenter.SingerPresenter;
import com.rikkathewrold.rikkamusic.search.mvp.view.SingerInfoDetailActivity;
import com.rikkathewrold.rikkamusic.util.GsonUtil;
import com.rikkathewrold.rikkamusic.util.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 歌手详细信息Fragment
 * Created By Rikka on 2019/8/17
 */
public class SingerInfoSearchFragment extends BaseFragment<SingerPresenter> implements SingerContract.View {
    private static final String TAG = "SingerInfoSearchFragment";

    @BindView(R.id.tv_singername)
    TextView tvName;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.singer_info_detail)
    TextView tvInfoDetail;
    @BindView(R.id.rv)
    RecyclerView rvSimSinger;

    private long singerId = -1;
    private SimiSingerAdapter adapter;
    private List<SimiSingerBean.ArtistsBean> simiList = new ArrayList<>();
    private SingerDescriptionBean descBean;

    public SingerInfoSearchFragment() {
        setFragmentTitle(App.getContext().getString(R.string.singer_info));
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onGetSingerIdEvent(SingIdEvent event) {
        singerId = event.getSingId();
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_singer_info, container, false);
        ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        return rootView;
    }

    @Override
    protected void initData() {
        adapter = new SimiSingerAdapter(getContext());
        adapter.setListener(listener);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        rvSimSinger.setLayoutManager(manager);
        rvSimSinger.setAdapter(adapter);

        if (singerId != -1) {
            showDialog();
            mPresenter.getSingerDesc(singerId);
            mPresenter.getSimiSinger(singerId);
        }
    }

    SimiSingerAdapter.OnSimiSingerClickListener listener = position -> {
        ToastUtils.showShort(position);
    };

    @Override
    public SingerPresenter onCreatePresenter() {
        return new SingerPresenter(this);
    }

    @Override
    protected void initVariables(Bundle bundle) {

    }

    @Override
    @OnClick({R.id.singer_info_detail})
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.singer_info_detail:
                intent.setClass(getActivity(), SingerInfoDetailActivity.class);
                if (simiList.size() != 0) {
                    String infoDetail = GsonUtil.toJson(descBean);
                    intent.putExtra(SingerInfoDetailActivity.INFO_DETAIL, infoDetail);
                    getActivity().startActivity(intent);
                }
        }
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
        hideDialog();
        LogUtil.d(TAG, "onGetSingerDescSuccess :" + bean);
        descBean = bean;
        if(bean.getIntroduction().size()!=0){
            tvName.setText(bean.getIntroduction().get(0).getTi());
            tvDesc.setText(bean.getIntroduction().get(0).getTxt());
        }else {
            tvName.setText("");
            tvDesc.setText("");
        }
    }

    @Override
    public void onGetSingerDescFail(String e) {
        hideDialog();
        LogUtil.e(TAG, "onGetSingerDescFail : " + e);
        ToastUtils.showShort(e);
    }

    @Override
    public void onGetSimiSingerSuccess(SimiSingerBean bean) {
        hideDialog();
        LogUtil.d(TAG, "onGetSimiSingerSuccess :" + bean + " size:" + bean.getArtists().size());
        simiList.clear();
        simiList.addAll(bean.getArtists());
        addDataToAdapter();
    }

    private void addDataToAdapter() {
        adapter.notifyDataSetChanged(simiList);
    }

    @Override
    public void onGetSimiSingerFail(String e) {
        hideDialog();
        LogUtil.e(TAG, "onGetSimiSingerFail : " + e);
        ToastUtils.showShort(e);
    }
}
