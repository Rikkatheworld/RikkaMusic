package com.rikkathewrold.rikkamusic.dj.event;

public class RidEvent {
    long rid;

    public RidEvent(long rid) {
        this.rid = rid;
    }

    public long getRid() {
        return rid;
    }

    public void setRid(long rid) {
        this.rid = rid;
    }

    @Override
    public String toString() {
        return "RidEvent{" +
                "rid=" + rid +
                '}';
    }
}
