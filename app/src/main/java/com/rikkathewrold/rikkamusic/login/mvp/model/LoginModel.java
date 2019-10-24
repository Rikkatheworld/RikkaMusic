package com.rikkathewrold.rikkamusic.login.mvp.model;


import com.rikkathewrold.rikkamusic.api.ApiEngine;
import com.rikkathewrold.rikkamusic.login.bean.LoginBean;
import com.rikkathewrold.rikkamusic.login.mvp.contract.LoginContract;

import io.reactivex.Observable;

public class LoginModel implements LoginContract.Model {
    @Override
    public Observable<LoginBean> login(String phone, String password) {
        return ApiEngine.getInstance().getApiService().login(phone, password);
    }
}
