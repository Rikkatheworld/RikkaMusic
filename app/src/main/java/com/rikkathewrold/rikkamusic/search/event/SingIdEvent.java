package com.rikkathewrold.rikkamusic.search.event;

public class SingIdEvent {

    public SingIdEvent(long singId, String singerName) {
        this.singId = singId;
        this.singerName = singerName;
    }

    long singId;

    String singerName;

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public long getSingId() {
        return singId;
    }

    public void setSingId(long singId) {
        this.singId = singId;
    }
}
