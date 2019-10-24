package com.rikkathewrold.rikkamusic.search.mvp.view.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hjq.toast.ToastUtils;
import com.lzx.starrysky.model.SongInfo;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseFragment;
import com.rikkathewrold.rikkamusic.main.adapter.SongListAdapter;
import com.rikkathewrold.rikkamusic.manager.SongPlayManager;
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
import com.rikkathewrold.rikkamusic.util.ClickUtil;
import com.rikkathewrold.rikkamusic.util.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.rikkathewrold.rikkamusic.base.BaseActivity.SONG_URL;

/**
 * 单曲搜索结果 type = 1
 */
@SuppressLint("ValidFragment")
public class SongSearchFragment extends BaseFragment<SearchPresenter> implements SearchContract.View {
    private static final String TAG = "SongSearchFragment";

    @BindView(R.id.rv_song_search)
    RecyclerView rvSong;

    private String type;
    private String keywords;
    private int searchType = 1;
    private SongListAdapter adapter;
    private List<SongSearchBean.ResultBean.SongsBean> resultBeans = new ArrayList<>();
    private boolean needRefresh = false;
    private List<SongInfo> songInfos = new ArrayList<>();

    public SongSearchFragment() {
    }

    public SongSearchFragment(String type) {
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
                    mPresenter.getSongSearch(keywords, searchType);
                }
            }
            keywords = event.getKeyword();
        }
    }


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_song, container, false);
        ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        return rootView;
    }

    @Override
    protected void initData() {
        resultBeans.clear();

        adapter = new SongListAdapter(getContext());
        adapter.setType(3);
        adapter.setKeywords(keywords);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvSong.setLayoutManager(manager);
        rvSong.setAdapter(adapter);

        if (keywords != null) {
            showDialog();
            mPresenter.getSongSearch(keywords, searchType);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d(TAG, "onResume");
    }

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
        LogUtil.d(TAG, "onVisible");
        if (needRefresh) {
            needRefresh = false;
            showDialog();
            mPresenter.getSongSearch(keywords, searchType);
        }
    }

    @Override
    @OnClick({R.id.rl_playall})
    public void onClick(View v) {
        if (ClickUtil.isFastClick(1000, v)) {
            return;
        }
        switch (v.getId()) {
            case R.id.rl_playall:
                SongPlayManager.getInstance().clickPlayAll(songInfos, 0);
                break;
        }
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
        hideDialog();
        LogUtil.d(TAG, "onGetSongSearchSuccess : " + bean);
        resultBeans.clear();
        if (bean.getResult().getSongs() != null) {
            resultBeans.addAll(bean.getResult().getSongs());
        }
        songInfos.clear();
        for (int i = 0; i < resultBeans.size(); i++) {
            SongInfo songInfo = new SongInfo();
            songInfo.setSongId(String.valueOf(resultBeans.get(i).getId()));
            songInfo.setArtist(resultBeans.get(i).getArtists().get(0).getName());
            songInfo.setSongCover(resultBeans.get(i).getArtists().get(0).getPicUrl() != null ? resultBeans.get(i).getArtists().get(0).getPicUrl() :
                    resultBeans.get(i).getArtists().get(0).getImg1v1Url());
            songInfo.setSongName(resultBeans.get(i).getName());
            songInfo.setSongUrl(SONG_URL + resultBeans.get(i).getId() + ".mp3");
            songInfo.setDuration(resultBeans.get(i).getDuration());
            songInfo.setArtistId(String.valueOf(resultBeans.get(i).getArtists().get(0).getId()));
            songInfo.setArtistKey(resultBeans.get(i).getArtists().get(0).getPicUrl());
            songInfos.add(songInfo);
        }
        adapter.notifyDataSetChanged(songInfos);
    }

    @Override
    public void onGetSongSearchFail(String e) {
        hideDialog();
        LogUtil.e(TAG, "onGetSongSearchFail : " + e);
        ToastUtils.show(e);
    }

    @Override
    public void onGetFeedSearchSuccess(FeedSearchBean bean) {

    }

    @Override
    public void onGetFeedSearchFail(String e) {

    }

    @Override
    public void onGetSingerSearchSuccess(SingerSearchBean bean) {

    }

    @Override
    public void onGetSingerSearchFail(String e) {

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
