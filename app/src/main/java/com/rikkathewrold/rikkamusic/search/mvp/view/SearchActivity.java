package com.rikkathewrold.rikkamusic.search.mvp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.gyf.immersionbar.ImmersionBar;
import com.hjq.toast.ToastUtils;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseActivity;
import com.rikkathewrold.rikkamusic.database.SearchHistoryDaoOp;
import com.rikkathewrold.rikkamusic.search.adapter.HotSearchAdapter;
import com.rikkathewrold.rikkamusic.search.bean.AlbumSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.FeedSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.HotSearchDetailBean;
import com.rikkathewrold.rikkamusic.search.bean.PlayListSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.RadioSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.SearchHistoryBean;
import com.rikkathewrold.rikkamusic.search.bean.SingerSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.SongSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.SynthesisSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.UserSearchBean;
import com.rikkathewrold.rikkamusic.search.mvp.contract.SearchContract;
import com.rikkathewrold.rikkamusic.search.mvp.presenter.SearchPresenter;
import com.rikkathewrold.rikkamusic.util.ClickUtil;
import com.rikkathewrold.rikkamusic.util.LogUtil;
import com.rikkathewrold.rikkamusic.widget.RikkaMusicDialog;
import com.rikkathewrold.rikkamusic.widget.SearchEditText;
import com.rikkathewrold.rikkamusic.widget.SearchHistoryTagLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity<SearchPresenter> implements SearchContract.View {
    private static final String TAG = "SearchActivity";

    public static final String KEYWORDS = "keywords";

    @BindView(R.id.tl_searchhistory)
    SearchHistoryTagLayout tlHistory;
    @BindView(R.id.rv_hotsearch)
    RecyclerView rvHotSearch;
    @BindView(R.id.et_search)
    SearchEditText etSearch;

    private HotSearchAdapter adapter;
    private List<SearchHistoryBean> stringList = new ArrayList<>();
    private HotSearchDetailBean searchDetailBean;
    private RikkaMusicDialog isDeleteDialog;

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_search);

        ImmersionBar.with(this)
                .transparentBar()
                .statusBarColor(R.color.colorPrimary)
                .statusBarDarkFont(false)
                .init();
    }

    @Override
    protected SearchPresenter onCreatePresenter() {
        return new SearchPresenter(this);
    }

    @Override
    protected void initModule() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        setBackBtn(getString(R.string.colorWhite));
        setEditText(getString(R.string.colorTransWithe));
        setRightSearchButton();

        adapter = new HotSearchAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvHotSearch.setLayoutManager(manager);
        rvHotSearch.setAdapter(adapter);
        adapter.setListener(searchListener);
        showDialog();
        mPresenter.getHotSearchDetail();
    }

    @Override
    protected void onResume() {
        super.onResume();
        stringList.clear();

        //从GreenDao里拿搜索历史
        if (SearchHistoryDaoOp.queryAll(this) != null) {
            stringList = SearchHistoryDaoOp.queryAll(this);
        }
        tlHistory.addHistoryText(stringList, tagListener);
    }

    private SearchHistoryTagLayout.OnHistoryTagClickListener tagListener = position -> {
        String keywords = stringList.get(position).getKeyowrds();
        searchSong(keywords);
    };

    private HotSearchAdapter.OnHotSearchAdapterClickListener searchListener = position -> {
        if (searchDetailBean != null) {
            String keywords = searchDetailBean.getData().get(position).getSearchWord();
            searchSong(keywords);
        }
    };


    @Override
    @OnClick({R.id.btn_search, R.id.iv_rubbish_bin})
    public void onClick(View v) {
        if (ClickUtil.isFastClick(1000, v)) {
            return;
        }
        switch (v.getId()) {
            case R.id.btn_search:
                String keywords = etSearch.getKeyWords();
                if (!TextUtils.isEmpty(keywords)) {
                    searchSong(keywords);
                } else {
                    ToastUtils.show("请输入关键字！");
                }
                break;
            case R.id.iv_rubbish_bin:
                showIsDeleteAllDialog();
                break;
        }
    }

    private void showIsDeleteAllDialog() {
        if (isDeleteDialog == null) {
            isDeleteDialog = new RikkaMusicDialog.Builder(this)
                    .setMsg(R.string.should_delete_all_search_history)
                    .setNegativeText(R.string.dialog_cancel)
                    .setPositiveText(R.string.dialog_search_history_clear)
                    .setNegativeClickListener((dialog, which) -> {
                        dialog.dismiss();
                    })
                    .setPositiveClickListener((dialog, which) -> {
                        dialog.dismiss();
                        SearchHistoryDaoOp.deleteAllData(this);
                        stringList = SearchHistoryDaoOp.queryAll(this);
                        tlHistory.addHistoryText(stringList, tagListener);
                    }).create();
        }
        if (!isDeleteDialog.isShowing()) {
            isDeleteDialog.show();
        }
    }

    //根据关键字去搜索
    private void searchSong(String keywords) {
        stringList.add(new SearchHistoryBean(keywords));
        if (stringList.size() > 10) {
            stringList.remove(0);
        }
        for (int i = 0; i < stringList.size() - 1; i++) {
            //去重
            if (stringList.get(i).getKeyowrds().equals(keywords)) {
                stringList.remove(i);
                break;
            }
        }
        SearchHistoryDaoOp.saveData(this, stringList);

        Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
        intent.putExtra(KEYWORDS, keywords);
        startActivity(intent);
    }

    @Override
    public void onGetHotSearchDetailSuccess(HotSearchDetailBean bean) {
        hideDialog();
        LogUtil.d(TAG, "onGetHotSearchDetailSuccess : " + bean);
        searchDetailBean = bean;
        List<HotSearchDetailBean> adapterList = new ArrayList<>();
        adapterList.add(searchDetailBean);
        adapter.notifyDataSetChanged(adapterList);
    }

    @Override
    public void onGetHotSearchDetailFail(String e) {
        hideDialog();
        LogUtil.e(TAG, "onGetHotSearchDetailFail : " + e);
        ToastUtils.show(e);
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
