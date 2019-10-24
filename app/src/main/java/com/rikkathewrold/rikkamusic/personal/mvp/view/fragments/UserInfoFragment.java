package com.rikkathewrold.rikkamusic.personal.mvp.view.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rikkathewrold.rikkamusic.App;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseFragment;
import com.rikkathewrold.rikkamusic.login.bean.LoginBean;
import com.rikkathewrold.rikkamusic.personal.bean.UserDetailBean;
import com.rikkathewrold.rikkamusic.personal.bean.UserEventBean;
import com.rikkathewrold.rikkamusic.personal.bean.UserPlaylistBean;
import com.rikkathewrold.rikkamusic.personal.event.UserDetailEvent;
import com.rikkathewrold.rikkamusic.personal.mvp.contract.PersonalContract;
import com.rikkathewrold.rikkamusic.personal.mvp.presenter.PersonalPresenter;
import com.rikkathewrold.rikkamusic.util.LogUtil;
import com.rikkathewrold.rikkamusic.util.TimeUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserInfoFragment extends BaseFragment<PersonalPresenter> implements PersonalContract.View {
    private static final String TAG = "UserInfoFragment";

    public UserInfoFragment() {
        setFragmentTitle(App.getContext().getString(R.string.user_info));
    }

    @BindView(R.id.level)
    TextView tvLevel;
    @BindView(R.id.region)
    TextView tvRegion;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.birthday)
    TextView tvBirthday;

    private LoginBean loginBean;
    private long uid = -1;

    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onUserDetailEvent(UserDetailEvent event) {
        if (event == null) {
            return;
        }
        LogUtil.d(TAG, "onUserDetailEvent :" + event);
        UserDetailBean bean = event.getUserDetailBean();
        tvLevel.setText(bean.getLevel() + "");
        if (!TextUtils.isEmpty(bean.getProfile().getDetailDescription())) {
            tvDescription.setText(bean.getProfile().getDetailDescription());
        } else {
            tvDescription.setText("这个人什么也没有留下~");
        }
        tvRegion.setText(bean.getProfile().getProvince() + " " + bean.getProfile().getCity());
        if (bean.getBindings().get(0).getBindingTime() != 0) {
            tvBirthday.setText(TimeUtil.getTimeStandard(bean.getBindings().get(0).getBindingTime()));
        }
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_info, container, false);
        ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        return rootView;
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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

    }

    @Override
    public void onGetUserEventFail(String e) {

    }

    @Override
    public void onGetUserDetailSuccess(UserDetailBean bean) {
    }

    @Override
    public void onGetUserDetailFail(String e) {
    }
}
