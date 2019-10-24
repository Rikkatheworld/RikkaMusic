package com.rikkathewrold.rikkamusic.personal.event;

public class UidEvent {

    public UidEvent(long uid, String name) {
        this.uid = uid;
        nickName = name;
    }

    private long uid;
    private String nickName;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }
}
