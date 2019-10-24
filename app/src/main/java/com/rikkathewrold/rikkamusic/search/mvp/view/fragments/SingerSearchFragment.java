package com.rikkathewrold.rikkamusic.search.mvp.view.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseFragment;
import com.rikkathewrold.rikkamusic.search.adapter.SingerSearchAdapter;
import com.rikkathewrold.rikkamusic.search.bean.AlbumSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.FeedSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.HotSearchDetailBean;
import com.rikkathewrold.rikkamusic.search.bean.PlayListSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.RadioSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.SingerSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.SongSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.SynthesisSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.UserSearchBean;
import com.rikkathewrold.rikkamusic.search.event.KeywordsEvent;
import com.rikkathewrold.rikkamusic.search.mvp.contract.SearchContract;
import com.rikkathewrold.rikkamusic.search.mvp.presenter.SearchPresenter;
import com.rikkathewrold.rikkamusic.search.mvp.view.SearchResultActivity;
import com.rikkathewrold.rikkamusic.search.mvp.view.SingerActivity;
import com.rikkathewrold.rikkamusic.util.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.rikkathewrold.rikkamusic.search.mvp.view.SingerActivity.SINGER_ID;
import static com.rikkathewrold.rikkamusic.search.mvp.view.SingerActivity.SINGER_NAME;
import static com.rikkathewrold.rikkamusic.search.mvp.view.SingerActivity.SINGER_PICURL;

/**
 * 歌手搜索界面 100
 */
@SuppressLint("ValidFragment")
public class SingerSearchFragment extends BaseFragment<SearchPresenter> implements SearchContract.View {
    private static final String TAG = "SingerSearchFragment";

    @BindView(R.id.rv)
    RecyclerView rvSinger;

    private String type;
    private String keywords;
    private boolean needRefresh = false;
    private int searchType = 100;
    private SingerSearchAdapter adapter;
    private List<SingerSearchBean.ResultBean.ArtistsBean> list = new ArrayList<>();

    public SingerSearchFragment() {
    }

    public SingerSearchFragment(String type) {
        this.type = type;
        setFragmentTitle(type);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onGetKeywordsEvent(KeywordsEvent event) {
        LogUtil.d(TAG, "onGetKeywordsEvent : " + event);
        if (event != null) {
            if (keywords != null && !event.getKeyword().equals(keywords)) {
                needRefresh = true;
                if (((SearchResultActivity) getActivity()).getPosition() == 1) {
                    needRefresh = false;
                    keywords = event.getKeyword();
                    showDialog();
                    mPresenter.getSingerSearch(keywords, searchType);
                }
            }
            keywords = event.getKeyword();
        }
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        return rootView;
    }

    @Override
    protected void initData() {
        adapter = new SingerSearchAdapter(getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvSinger.setLayoutManager(manager);
        rvSinger.setAdapter(adapter);
        adapter.setListener(listener);
        adapter.setKeywords(keywords);


        if (keywords != null) {
            showDialog();
            mPresenter.getSingerSearch(keywords, searchType);
        }
    }

    SingerSearchAdapter.OnSingerClickListener listener = position -> {
        if (list != null) {
            //进入歌手界面
            Intent intent = new Intent(getActivity(), SingerActivity.class);
            intent.putExtra(SINGER_ID, list.get(position).getId());
            intent.putExtra(SINGER_PICURL, list.get(position).getPicUrl());
            String name = list.get(position).getName();
            if (!TextUtils.isEmpty(list.get(position).getTrans())) {
                name += "(" + list.get(position).getTrans() + ")";
            }
            intent.putExtra(SINGER_NAME, name);
            getActivity().startActivity(intent);
        }
    };

    @Override
    public SearchPresenter onCreatePresenter() {
        return new SearchPresenter(this);
    }

    @Override
    protected void initVariables(Bundle bundle) {

    }

    @Override
    protected void onVisible() {
        super.onVisible();
        if (needRefresh) {
            needRefresh = false;
            showDialog();
            mPresenter.getSingerSearch(keywords, searchType);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onGetHotSearchDetailSuccess(HotSearchDetailBean bean) {

    }

    @Override
    public void onGetHotSearchDetailFail(String e) {

    }

    @Override
    public void onGetSongSearchSuccess(SongSearchBean bean) {

    }

    @Override
    public void onGetSongSearchFail(String e) {

    }

    @Override
    public void onGetFeedSearchSuccess(FeedSearchBean bean) {

    }

    @Override
    public void onGetFeedSearchFail(String e) {

    }

    @Override
    public void onGetSingerSearchSuccess(SingerSearchBean bean) {
        hideDialog();
        LogUtil.d(TAG, "onGetSingerSearchSuccess : " + bean);
        list.clear();
        if (bean.getResult().getArtists() != null) {
            list.addAll(bean.getResult().getArtists());
        }
        adapter.notifyDataSetChanged(list);
    }

    @Override
    public void onGetSingerSearchFail(String e) {
        hideDialog();
        LogUtil.d(TAG, "onGetSingerSearchFail : " + e);
        ToastUtils.showShort(e);
    }

    @Override
    public void onGetAlbumSearchSuccess(AlbumSearchBean bean) {

    }

    @Override
    public void onGetAlbumSearchFail(String e) {

    }

    @Override
    public void onGetPlayListSearchSuccess(PlayListSearchBean bean) {

    }

    @Override
    public void onGetPlayListSearchFail(String e) {

    }

    @Override
    public void onGetRadioSearchSuccess(RadioSearchBean bean) {

    }

    @Override
    public void onGetRadioSearchFail(String e) {

    }

    @Override
    public void onGetUserSearchSuccess(UserSearchBean bean) {

    }

    @Override
    public void onGetUserSearchFail(String e) {

    }

    @Override
    public void onGetSynthesisSearchSuccess(SynthesisSearchBean bean) {

    }

    @Override
    public void onGetSynthesisSearchFail(String e) {

    }
}
