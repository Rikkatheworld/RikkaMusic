package com.rikkathewrold.rikkamusic.personal.event;

import com.rikkathewrold.rikkamusic.personal.bean.UserDetailBean;

public class UserDetailEvent {

    public UserDetailEvent(UserDetailBean userDetailBean) {
        this.userDetailBean = userDetailBean;
    }

    UserDetailBean userDetailBean;

    public UserDetailBean getUserDetailBean() {
        return userDetailBean;
    }

    public void setUserDetailBean(UserDetailBean userDetailBean) {
        this.userDetailBean = userDetailBean;
    }
}
