package com.rikkathewrold.rikkamusic.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzx.starrysky.model.SongInfo;
import com.rikkathewrold.rikkamusic.App;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.manager.SongPlayManager;
import com.rikkathewrold.rikkamusic.manager.event.MusicPauseEvent;
import com.rikkathewrold.rikkamusic.manager.event.MusicStartEvent;
import com.rikkathewrold.rikkamusic.manager.event.StopMusicEvent;
import com.rikkathewrold.rikkamusic.song.mvp.view.SongActivity;
import com.rikkathewrold.rikkamusic.song.mvp.view.SongListActivity;
import com.rikkathewrold.rikkamusic.util.LogUtil;
import com.rikkathewrold.rikkamusic.util.SharePreferenceUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 界面底部的歌曲遥控器
 * 用EventBus来控制它
 * Created By Rikka on 2019/7/22
 */
public class BottomSongPlayBar extends RelativeLayout {
    private static final String TAG = "BottomSongPlayBar";

    private Context mContext;

    private RelativeLayout rlSongController;
    private CircleImageView ivCover;
    private ImageView ivPlay, ivController;
    private TextView tvSongName, tvSongSinger;
    private LinearLayout llSongInfo;

    private SongInfo currentSongInfo;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayMusicEvent(MusicStartEvent event) {
        LogUtil.d(TAG, "MusicStartEvent :" + event);
        setSongBean(event.getSongInfo());
        ivPlay.setImageResource(R.drawable.shape_stop);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStopMusicEvent(StopMusicEvent event) {
        LogUtil.d(TAG, "onStopMusicEvent");
        setSongBean(event.getSongInfo());
        ivPlay.setImageResource(R.drawable.shape_play);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPauseMusicEvent(MusicPauseEvent event) {
        LogUtil.d(TAG, "onPauseMusicEvent");
        ivPlay.setImageResource(R.drawable.shape_play);
    }

    public BottomSongPlayBar(Context context) {
        this(context, null);
    }

    public BottomSongPlayBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomSongPlayBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        EventBus.getDefault().register(this);
        mContext = context;
        initView();
        initListener();
        initSongInfo();
    }

    private void initView() {
        rlSongController = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_bottom_songplay_control, this, true);
        ivCover = rlSongController.findViewById(R.id.iv_cover);
        ivPlay = rlSongController.findViewById(R.id.iv_bottom_play);
        ivController = rlSongController.findViewById(R.id.iv_bottom_controller);
        tvSongName = rlSongController.findViewById(R.id.tv_songname);
        tvSongSinger = rlSongController.findViewById(R.id.tv_singer);
        llSongInfo = rlSongController.findViewById(R.id.ll_songinfo);
    }

    private void initListener() {
        ivCover.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, SongActivity.class);
            intent.putExtra(SongActivity.SONG_INFO, currentSongInfo);
            mContext.startActivity(intent);
        });

        llSongInfo.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, SongActivity.class);
            intent.putExtra(SongActivity.SONG_INFO, currentSongInfo);
            mContext.startActivity(intent);
        });

        ivPlay.setOnClickListener(v -> {
            LogUtil.d(TAG, "ivPlay OnClick");
            if (!SongPlayManager.getInstance().isPlaying()) {
                LogUtil.d(TAG, "playMusic");
                SongPlayManager.getInstance().clickBottomContrllerPlay(currentSongInfo);
            } else {
                LogUtil.d(TAG, "pauseMusic");
                SongPlayManager.getInstance().pauseMusic();
            }
        });

        ivController.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, SongListActivity.class);
            mContext.startActivity(intent);
            ((Activity) mContext).overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent);
        });
    }

    private void initSongInfo() {
        //初始化的时候，要从本地拿最近一次听的歌曲
        currentSongInfo = SharePreferenceUtil.getInstance(App.getContext()).getLatestSong();
        if (currentSongInfo != null) {
            setSongBean(currentSongInfo);
            LogUtil.d(TAG, "isPlaying " + SongPlayManager.getInstance().isPlaying());
            if (SongPlayManager.getInstance().isPlaying()) {
                ivPlay.setImageResource(R.drawable.shape_stop);
            }
        }
    }


    public void setSongBean(SongInfo bean) {
        currentSongInfo = bean;
        tvSongName.setText(bean.getSongName());
        tvSongSinger.setText(bean.getArtist());
        if (!TextUtils.isEmpty(bean.getSongCover())) {
            Glide.with(mContext).load(bean.getSongCover()).into(ivCover);
        }
    }
}