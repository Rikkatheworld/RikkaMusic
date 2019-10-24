package com.rikkathewrold.rikkamusic.personal.mvp.view.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hjq.toast.ToastUtils;
import com.rikkathewrold.rikkamusic.App;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseFragment;
import com.rikkathewrold.rikkamusic.personal.adapter.UserEventAdapter;
import com.rikkathewrold.rikkamusic.personal.bean.UserDetailBean;
import com.rikkathewrold.rikkamusic.personal.bean.UserEventBean;
import com.rikkathewrold.rikkamusic.personal.bean.UserPlaylistBean;
import com.rikkathewrold.rikkamusic.personal.event.UidEvent;
import com.rikkathewrold.rikkamusic.personal.mvp.contract.PersonalContract;
import com.rikkathewrold.rikkamusic.personal.mvp.presenter.PersonalPresenter;
import com.rikkathewrold.rikkamusic.util.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 用户动态界面
 */
public class UserDynamicsFragment extends BaseFragment<PersonalPresenter> implements PersonalContract.View {
    private static final String TAG = "UserDynamicsFragment";

    @BindView(R.id.rv)
    RecyclerView rvEvent;

    private List<UserEventBean.EventsBean> eventsBeans = new ArrayList<>();
    //动态上次更新时间
    private long lastTime;
    //本人的uid
    private long userUid;
    //本人的名称
    private String nickName;
    private UserEventAdapter adapter;

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onGetUidEvent(UidEvent event) {
        LogUtil.d(TAG, "onGetUidEvent : " + event.getUid());
        userUid = event.getUid();
        nickName = event.getNickName();
    }

    public UserDynamicsFragment() {
        setFragmentTitle(App.getContext().getString(R.string.user_dynamics));
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_nested_scroll_view, container, false);
        ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        return rootView;
    }

    @Override
    protected void initData() {
        eventsBeans.clear();

        adapter = new UserEventAdapter(getContext());
        rvEvent.setLayoutManager(new LinearLayoutManager(getContext()));
        rvEvent.setAdapter(adapter);

        showDialog();
        mPresenter.getUserEvent(userUid, 10, -1);
    }

    @Override
    public PersonalPresenter onCreatePresenter() {
        return new PersonalPresenter(this);
    }

    @Override
    protected void initVariables(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onGetUserPlaylistSuccess(UserPlaylistBean bean) {

    }

    @Override
    public void onGetUserPlaylistFail(String e) {

    }

    @Override
    public void onGetUserEventSuccess(UserEventBean bean) {
        hideDialog();
        LogUtil.d(TAG, "onGetUserEventSuccess : " + bean);
        eventsBeans.clear();
        eventsBeans = bean.getEvents();
        adapter.notifyDataSetChanged(eventsBeans);
    }

    @Override
    public void onGetUserEventFail(String e) {
        hideDialog();
        LogUtil.e(TAG, "onGetUserEventFail :" + e);
        ToastUtils.show(e);
    }

    @Override
    public void onGetUserDetailSuccess(UserDetailBean bean) {

    }

    @Override
    public void onGetUserDetailFail(String e) {

    }
}
