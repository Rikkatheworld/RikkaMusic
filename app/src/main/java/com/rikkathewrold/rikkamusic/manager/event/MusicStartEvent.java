package com.rikkathewrold.rikkamusic.manager.event;

import com.lzx.starrysky.model.SongInfo;

/**
 * 播放歌曲的Event
 */
public class MusicStartEvent {
    SongInfo songInfo;

    public MusicStartEvent(SongInfo songInfo) {
        this.songInfo = songInfo;
    }

    public SongInfo getSongInfo() {
        return songInfo;
    }

    public void setSongInfo(SongInfo songInfo) {
        this.songInfo = songInfo;
    }
}
