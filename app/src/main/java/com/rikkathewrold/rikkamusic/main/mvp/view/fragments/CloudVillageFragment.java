package com.rikkathewrold.rikkamusic.main.mvp.view.fragments;

import android.content.Intent;
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
import com.rikkathewrold.rikkamusic.main.bean.MainEventBean;
import com.rikkathewrold.rikkamusic.main.mvp.contract.EventContract;
import com.rikkathewrold.rikkamusic.main.mvp.presenter.EventPresenter;
import com.rikkathewrold.rikkamusic.personal.adapter.UserEventAdapter;
import com.rikkathewrold.rikkamusic.personal.bean.UserEventBean;
import com.rikkathewrold.rikkamusic.personal.mvp.view.PersonalInfoActivity;
import com.rikkathewrold.rikkamusic.search.bean.UserSearchBean;
import com.rikkathewrold.rikkamusic.util.GsonUtil;
import com.rikkathewrold.rikkamusic.util.LogUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 云村Fragment，里面是全是动态
 */
public class CloudVillageFragment extends BaseFragment<EventPresenter> implements EventContract.View {
    private static final String TAG = "CloudVillageFragment";

    @BindView(R.id.rv_event)
    RecyclerView rvEvent;

    private List<UserEventBean.EventsBean> eventList;
    private UserEventAdapter adapter;

    public CloudVillageFragment() {
        setFragmentTitle(App.getContext().getString(R.string.feed));
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected void initData() {
        rvEvent.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new UserEventAdapter(getContext());
        rvEvent.setAdapter(adapter);
        adapter.setListener(listener);

        showDialog();
        mPresenter.getMainEvent();
    }

    UserEventAdapter.OnEventClickListener listener = new UserEventAdapter.OnEventClickListener() {
        @Override
        public void onAvatarClick(int position) {
            UserSearchBean.ResultBean.UserprofilesBean userSearchBean = new UserSearchBean.ResultBean.UserprofilesBean();
            UserEventBean.EventsBean.UserBean user = eventList.get(position).getUser();
            userSearchBean.setUserId(user.getUserId());
            userSearchBean.setAvatarUrl(user.getAvatarUrl());
            userSearchBean.setBackgroundUrl(user.getBackgroundUrl());
            userSearchBean.setNickname(user.getNickname());
            Intent intent = new Intent(getContext(), PersonalInfoActivity.class);
            String userSearchBeanString = GsonUtil.toJson(userSearchBean);
            intent.putExtra(PersonalInfoActivity.USER_BEAN, userSearchBeanString);
            startActivity(intent);
        }

        @Override
        public void onRelayClick(int position) {
            ToastUtils.show("onRelayClick");
        }

        @Override
        public void onCommentClick(int position) {
            ToastUtils.show("onCommentClick");
        }

        @Override
        public void onLikeClick(int position) {
            ToastUtils.show("onLikeClick");
        }
    };

    @Override
    public EventPresenter onCreatePresenter() {
        return new EventPresenter(this);
    }


    @Override
    protected void initVariables(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onGetMainEventSuccess(MainEventBean bean) {
        hideDialog();
        LogUtil.d(TAG, "onGetMainEventSuccess :" + bean);
        eventList = bean.getEvent();
        adapter.notifyDataSetChanged(eventList);
    }

    @Override
    public void onGetMainEventFail(String e) {
        hideDialog();
        LogUtil.e(TAG, "onGetMainEventFail :" + e);
        ToastUtils.show("e");
    }

}
