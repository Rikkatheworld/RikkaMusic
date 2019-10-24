package com.rikkathewrold.rikkamusic.main.mvp.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rikkathewrold.rikkamusic.App;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseFragment;
import com.rikkathewrold.rikkamusic.base.BasePresenter;

import butterknife.ButterKnife;

public class FriendFragment extends BaseFragment {

    public FriendFragment() {
        setFragmentTitle(App.getContext().getString(R.string.friend));
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friend, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected void initData() {

    }

    @Override
    public BasePresenter onCreatePresenter() {
        return null;
    }

    @Override
    protected void initVariables(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {

    }
}
