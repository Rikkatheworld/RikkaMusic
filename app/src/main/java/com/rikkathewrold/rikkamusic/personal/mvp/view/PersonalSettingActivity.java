package com.rikkathewrold.rikkamusic.personal.mvp.view;

import android.os.Bundle;
import android.view.View;

import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseActivity;
import com.rikkathewrold.rikkamusic.personal.bean.UserDetailBean;
import com.rikkathewrold.rikkamusic.personal.bean.UserEventBean;
import com.rikkathewrold.rikkamusic.personal.bean.UserPlaylistBean;
import com.rikkathewrold.rikkamusic.personal.mvp.contract.PersonalContract;
import com.rikkathewrold.rikkamusic.personal.mvp.presenter.PersonalPresenter;

public class PersonalSettingActivity extends BaseActivity<PersonalPresenter> implements PersonalContract.View {

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_personal_setting);
    }

    @Override
    protected PersonalPresenter onCreatePresenter() {
        return null;
    }

    @Override
    protected void initModule() {

    }

    @Override
    protected void initData() {

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
