package com.rikkathewrold.rikkamusic.manager.event;

import com.lzx.starrysky.model.SongInfo;

/**
 * 停止歌曲的Event
 */
public class StopMusicEvent {
    SongInfo songInfo;

    public StopMusicEvent(SongInfo songInfo) {
        this.songInfo = songInfo;
    }

    public SongInfo getSongInfo() {
        return songInfo;
    }

    public void setSongInfo(SongInfo songInfo) {
        this.songInfo = songInfo;
    }
}
