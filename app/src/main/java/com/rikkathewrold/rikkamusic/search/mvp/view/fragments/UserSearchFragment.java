package com.rikkathewrold.rikkamusic.search.mvp.view.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hjq.toast.ToastUtils;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseFragment;
import com.rikkathewrold.rikkamusic.personal.mvp.view.PersonalInfoActivity;
import com.rikkathewrold.rikkamusic.search.adapter.UserSearchAdapter;
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
import com.rikkathewrold.rikkamusic.util.GsonUtil;
import com.rikkathewrold.rikkamusic.util.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 用户搜索结果Fragment 1002
 */
@SuppressLint("ValidFragment")
public class UserSearchFragment extends BaseFragment<SearchPresenter> implements SearchContract.View {
    private static final String TAG = "UserSearchFragment";

    @BindView(R.id.rv)
    RecyclerView rvUser;

    private String type;
    private int searchType = 1002;
    private String keywords;
    private List<UserSearchBean.ResultBean.UserprofilesBean> userList = new ArrayList<>();
    private UserSearchAdapter adapter;
    private boolean needRefresh = false;
    private UserSearchBean userBean;

    public UserSearchFragment() {
    }

    public UserSearchFragment(String type) {
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
                    mPresenter.getUserSearch(keywords, searchType);
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
        adapter = new UserSearchAdapter(getContext());
        adapter.setKeywords(keywords);
        adapter.setListener(listener);
        rvUser.setLayoutManager(new LinearLayoutManager(getContext()));
        rvUser.setAdapter(adapter);

        if (keywords != null) {
            showDialog();
            mPresenter.getUserSearch(keywords, searchType);
        }
    }

    UserSearchAdapter.OnUserClickListener listener = (position, type) -> {
        if (type == UserSearchAdapter.OnUserClickListener.USER_CHECK) {
            if (userBean != null) {
                UserSearchBean.ResultBean.UserprofilesBean bean = userBean.getResult().getUserprofiles().get(position);
                Intent intent = new Intent(getActivity(), PersonalInfoActivity.class);
                intent.putExtra(PersonalInfoActivity.USER_BEAN, GsonUtil.toJson(bean));
                getActivity().startActivity(intent);
            }
        } else {
            ToastUtils.show("关注 ： " + position);
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
    public void onClick(View v) {

    }

    @Override
    protected void onVisible() {
        super.onVisible();
        LogUtil.d(TAG, "onVisible");
        if (needRefresh) {
            needRefresh = false;
            showDialog();
            mPresenter.getUserSearch(keywords, searchType);
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
        hideDialog();
        LogUtil.d(TAG, "onGetUserSearchSuccess : " + bean);
        userBean = bean;
        userList.clear();
        userList.addAll(bean.getResult().getUserprofiles());
        adapter.notifyDataSetChanged(userList);
    }

    @Override
    public void onGetUserSearchFail(String e) {
        hideDialog();
        LogUtil.e(TAG, "onGetUserSearchFail : " + e);
        ToastUtils.show(e);
    }

    @Override
    public void onGetSynthesisSearchSuccess(SynthesisSearchBean bean) {

    }

    @Override
    public void onGetSynthesisSearchFail(String e) {

    }
}
