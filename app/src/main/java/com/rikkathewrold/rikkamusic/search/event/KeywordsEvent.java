package com.rikkathewrold.rikkamusic.search.event;

public class KeywordsEvent {

    public KeywordsEvent(String keyword) {
        this.keyword = keyword;
    }

    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return "KeywordsEvent{" +
                "keyword='" + keyword + '\'' +
                '}';
    }
}
