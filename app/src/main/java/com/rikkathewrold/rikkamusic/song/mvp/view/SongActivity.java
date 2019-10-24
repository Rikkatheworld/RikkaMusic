package com.rikkathewrold.rikkamusic.song.mvp.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.toast.ToastUtils;
import com.lzx.starrysky.manager.MusicManager;
import com.lzx.starrysky.model.SongInfo;
import com.lzx.starrysky.utils.TimerTaskManager;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseActivity;
import com.rikkathewrold.rikkamusic.login.bean.LoginBean;
import com.rikkathewrold.rikkamusic.main.bean.LikeListBean;
import com.rikkathewrold.rikkamusic.manager.SongPlayManager;
import com.rikkathewrold.rikkamusic.manager.event.MusicPauseEvent;
import com.rikkathewrold.rikkamusic.manager.event.MusicStartEvent;
import com.rikkathewrold.rikkamusic.song.bean.CommentLikeBean;
import com.rikkathewrold.rikkamusic.song.bean.LikeMusicBean;
import com.rikkathewrold.rikkamusic.song.bean.LyricBean;
import com.rikkathewrold.rikkamusic.song.bean.MusicCommentBean;
import com.rikkathewrold.rikkamusic.song.bean.PlayListCommentBean;
import com.rikkathewrold.rikkamusic.song.bean.SongDetailBean;
import com.rikkathewrold.rikkamusic.song.mvp.contract.SongContract;
import com.rikkathewrold.rikkamusic.song.mvp.presenter.SongPresenter;
import com.rikkathewrold.rikkamusic.util.GsonUtil;
import com.rikkathewrold.rikkamusic.util.LogUtil;
import com.rikkathewrold.rikkamusic.util.SharePreferenceUtil;
import com.rikkathewrold.rikkamusic.util.TimeUtil;
import com.rikkathewrold.rikkamusic.widget.LyricView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.rikkathewrold.rikkamusic.main.mvp.view.PlayListActivity.COMPLETED;


public class SongActivity extends BaseActivity<SongPresenter> implements SongContract.View {
    private static final String TAG = "SongActivity";

    public static final String SONG_INFO = "songInfo";

    @BindView(R.id.iv_record)
    CircleImageView ivRecord;
    @BindView(R.id.iv_like)
    ImageView ivLike;
    @BindView(R.id.tv_past_time)
    TextView tvPastTime;
    @BindView(R.id.total_time)
    TextView tvTotalTime;
    @BindView(R.id.seek_bar)
    AppCompatSeekBar seekBar;
    @BindView(R.id.iv_play)
    ImageView ivPlay;
    @BindView(R.id.iv_bg)
    ImageView ivBg;
    @BindView(R.id.iv_play_mode)
    ImageView ivPlayMode;
    @BindView(R.id.ll_info)
    LinearLayout llInfo;
    @BindView(R.id.lrc)
    LyricView lrc;

    private SongInfo currentSongInfo;
    private long ids;
    private SongDetailBean songDetail;
    private TimerTaskManager mTimerTask;
    private boolean isLike = false;
    private int playMode;
    private ObjectAnimator rotateAnimator;
    private ObjectAnimator alphaAnimator;
    private boolean isShowLyrics = false;
    private LyricBean lyricBean;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMusicStartEvent(MusicStartEvent event) {
        LogUtil.d(TAG, "onMusicStartEvent");
        SongInfo songInfo = event.getSongInfo();
        if (!songInfo.getSongId().equals(currentSongInfo.getSongId())) {
            //说明该界面下，切歌了，则要重新设置一遍
            currentSongInfo = songInfo;
            if (lyricBean != null) {
                lyricBean = null;
            }
            checkMusicState();
        } else {
            //如果没有没有切歌，则check一下就行了
            checkMusicPlaying();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMusicPauseEvent(MusicPauseEvent event) {
        LogUtil.d(TAG, "onMusicPauseEvent");
        checkMusicPlaying();
    }

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_song);

        ImmersionBar.with(this)
                .transparentBar()
                .statusBarDarkFont(false)
                .init();
    }

    @Override
    protected SongPresenter onCreatePresenter() {
        return new SongPresenter(this);
    }

    @Override
    protected void initModule() {
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        LogUtil.d(TAG, "initData");
        getIntentData();
        setBackBtn(getString(R.string.colorWhite));
        playMode = SongPlayManager.getInstance().getMode();
        mTimerTask = new TimerTaskManager();
        initTimerTaskWork();

        checkMusicState();
    }


    private void initTimerTaskWork() {
        mTimerTask.setUpdateProgressTask(() -> {
            long position = MusicManager.getInstance().getPlayingPosition();
            //SeekBar 设置 Max
            seekBar.setProgress((int) position);
            tvPastTime.setText(TimeUtil.getTimeNoYMDH(position));
            lrc.updateTime(position);
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SongPlayManager.getInstance().seekTo(seekBar.getProgress());
                SongActivity.this.seekBar.setProgress(seekBar.getProgress());
                lrc.updateTime(seekBar.getProgress());
            }
        });
    }


    private void getIntentData() {
        Intent intent = getIntent();
        currentSongInfo = intent.getParcelableExtra(SONG_INFO);
    }

    private void checkMusicState() {
        LogUtil.d(TAG, "currentSongInfo : " + currentSongInfo);
        setSongInfo(currentSongInfo.getSongName(), currentSongInfo.getArtist());
        if (judgeContainsStr(currentSongInfo.getSongId())) {
            llInfo.setVisibility(View.GONE);
        } else {
            if (lyricBean == null) {
                mPresenter.getLyric(Long.parseLong(currentSongInfo.getSongId()));
            }
            llInfo.setVisibility(View.VISIBLE);
            ids = Long.parseLong(currentSongInfo.getSongId());
            String songId = currentSongInfo.getSongId();
            List<String> likeList = SharePreferenceUtil.getInstance(this).getLikeList();
            LogUtil.d(TAG, "likeList :" + likeList);
            if (likeList.contains(songId)) {
                isLike = true;
                ivLike.setImageResource(R.drawable.shape_like_white);
            } else {
                isLike = false;
            }
            if (SongPlayManager.getInstance().getSongDetail(ids) == null) {
                mPresenter.getSongDetail(ids);
            } else {
                songDetail = SongPlayManager.getInstance().getSongDetail(ids);
                setSongDetailBean(songDetail);
            }
        }

        long duration = currentSongInfo.getDuration();
        if (seekBar.getMax() != duration) {
            seekBar.setMax((int) duration);
        }

        switch (playMode) {
            case SongPlayManager.MODE_LIST_LOOP_PLAY:
                ivPlayMode.setImageResource(R.drawable.shape_list_cycle);
                break;
            case SongPlayManager.MODE_RANDOM:
                ivPlayMode.setImageResource(R.drawable.shape_list_random);
                break;
            case SongPlayManager.MODE_SINGLE_LOOP_PLAY:
                ivPlayMode.setImageResource(R.drawable.shape_single_cycle);
                break;
        }
        tvTotalTime.setText(TimeUtil.getTimeNoYMDH(duration));
        checkMusicPlaying();
    }

    private void checkMusicPlaying() {
        mTimerTask.startToUpdateProgress();
        if (SongPlayManager.getInstance().isPlaying()) {
            LogUtil.d(TAG, "music is Playing");
            if (getRotateAnimator().isPaused()) {
                getRotateAnimator().resume();
            } else if (getRotateAnimator().isRunning()) {
            } else {
                getRotateAnimator().start();
            }
            ivPlay.setImageResource(R.drawable.shape_pause);
        } else {
            LogUtil.d(TAG, "music is Not playing");
            getRotateAnimator().pause();
            ivPlay.setImageResource(R.drawable.shape_play_white);
//            mTimerTask.stopToUpdateProgress();
        }
    }


    /**
     * 该方法主要使用正则表达式来判断字符串中是否包含字母
     */
    public boolean judgeContainsStr(String cardNum) {
        String regex = ".*[a-zA-Z]+.*";
        Matcher m = Pattern.compile(regex).matcher(cardNum);
        return m.matches();
    }

    private ObjectAnimator getRotateAnimator() {
        if (rotateAnimator == null) {
            rotateAnimator = ObjectAnimator.ofFloat(ivRecord, "rotation", 360f);
            rotateAnimator.setDuration(25 * 1000);
            rotateAnimator.setInterpolator(new LinearInterpolator());
            rotateAnimator.setRepeatCount(100000);
            rotateAnimator.setRepeatMode(ValueAnimator.RESTART);
        }
        return rotateAnimator;
    }

    private ObjectAnimator getAlphaAnimator() {
        if (alphaAnimator == null) {
            alphaAnimator = ObjectAnimator.ofFloat(ivBg, "alpha", 0f, 0.13f);
            alphaAnimator.setDuration(1500);
            alphaAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        }
        return alphaAnimator;
    }

    private void setSongDetailBean(SongDetailBean songDetail) {
        String coverUrl = songDetail.getSongs().get(0).getAl().getPicUrl();
        Glide.with(this)
                .load(coverUrl)
                .placeholder(R.drawable.shape_record)
                .into(ivRecord);
        Glide.with(this)
                .load(coverUrl)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 12)))
                .transition(new DrawableTransitionOptions().crossFade(1500))
                .into(ivBg);
//        calculateColors(coverUrl);
    }

    @Override
    @OnClick({R.id.iv_play, R.id.iv_like, R.id.iv_download, R.id.iv_comment, R.id.iv_info,
            R.id.iv_play_mode, R.id.iv_pre, R.id.iv_next, R.id.iv_list, R.id.rl_center, R.id.lrc})
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.rl_center:
                isShowLyrics = true;
                showLyrics(true);
                break;
            case R.id.iv_play:
                if (SongPlayManager.getInstance().isPlaying()) {
                    SongPlayManager.getInstance().pauseMusic();
                } else if (SongPlayManager.getInstance().isPaused()) {
                    SongPlayManager.getInstance().playMusic();
                } else if (SongPlayManager.getInstance().isIdle()) {
                    SongPlayManager.getInstance().clickASong(currentSongInfo);
                }
                break;
            case R.id.iv_like:
                if (isLike) {
                    ToastUtils.show("Sorry啊，我没有找到取消喜欢的接口");
                } else {
                    mPresenter.likeMusic(ids);
                }
                break;
            case R.id.iv_download:
                ToastUtils.show("Sorry啊，歌都不是我的，不能下载的");
                break;
            case R.id.iv_comment:
                if (songDetail == null) {
                    ToastUtils.show("获取不到歌曲信息，稍后再试");
                    return;
                }
                intent.setClass(SongActivity.this, CommentActivity.class);
                intent.putExtra(CommentActivity.ID, songDetail.getSongs().get(0).getId());
                intent.putExtra(CommentActivity.NAME, songDetail.getSongs().get(0).getName());
                intent.putExtra(CommentActivity.ARTIST, songDetail.getSongs().get(0).getAr().get(0).getName());
                intent.putExtra(CommentActivity.COVER, songDetail.getSongs().get(0).getAl().getPicUrl());
                intent.putExtra(CommentActivity.FROM, CommentActivity.SONG_COMMENT);
                startActivity(intent);
                break;
            case R.id.iv_info:
                intent.setClass(SongActivity.this, SongDetailActivity.class);
                intent.putExtra(SONG_INFO, currentSongInfo);
                startActivity(intent);
                overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent);
                break;
            case R.id.iv_play_mode:
                if (playMode == SongPlayManager.MODE_LIST_LOOP_PLAY) {
                    SongPlayManager.getInstance().setMode(SongPlayManager.MODE_SINGLE_LOOP_PLAY);
                    ivPlayMode.setImageResource(R.drawable.shape_single_cycle);
                    playMode = SongPlayManager.MODE_SINGLE_LOOP_PLAY;
                    ToastUtils.show("切换到单曲循环模式");
                } else if (playMode == SongPlayManager.MODE_SINGLE_LOOP_PLAY) {
                    SongPlayManager.getInstance().setMode(SongPlayManager.MODE_RANDOM);
                    ivPlayMode.setImageResource(R.drawable.shape_list_random);
                    playMode = SongPlayManager.MODE_RANDOM;
                    ToastUtils.show("切换到随机播放模式");
                } else if (playMode == SongPlayManager.MODE_RANDOM) {
                    SongPlayManager.getInstance().setMode(SongPlayManager.MODE_LIST_LOOP_PLAY);
                    ivPlayMode.setImageResource(R.drawable.shape_list_cycle);
                    playMode = SongPlayManager.MODE_LIST_LOOP_PLAY;
                    ToastUtils.show("切换到列表循环模式");
                }
                break;
            case R.id.iv_pre:
                SongPlayManager.getInstance().playPreMusic();
                break;
            case R.id.iv_next:
                SongPlayManager.getInstance().playNextMusic();
                break;
            case R.id.iv_list:
                intent.setClass(SongActivity.this, SongListActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent);
                break;
        }
    }

    //根据isShowLyrics来判断是否展示歌词
    private void showLyrics(boolean isShowLyrics) {
        ivRecord.setVisibility(isShowLyrics ? View.GONE : View.VISIBLE);
        lrc.setVisibility(isShowLyrics ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mTimerTask.removeUpdateProgressTask();
        if (rotateAnimator != null) {
            if (rotateAnimator.isRunning()) {
                rotateAnimator.cancel();
            }
            rotateAnimator = null;
        }
        if (alphaAnimator != null) {
            if (alphaAnimator.isRunning()) {
                alphaAnimator.cancel();
            }
            alphaAnimator = null;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == COMPLETED) {
                ivBg.setBackground((Drawable) msg.obj);
                getAlphaAnimator().start();
            }
        }
    };

//    public void calculateColors(String url) {
//        new Thread(() -> {
//            try {
//                //渐变色的两个颜色
//                URL fileUrl;
//                Bitmap bitmap;
//                fileUrl = new URL(url);
//                HttpURLConnection conn = (HttpURLConnection) fileUrl.openConnection();
//                conn.setDoInput(true);
//                conn.connect();
//                InputStream is = conn.getInputStream();
//                BitmapFactory.Options opt = new BitmapFactory.Options();
//                opt.inSampleSize = 27;
//                bitmap = BitmapFactory.decodeStream(is, new Rect(), opt);
//                LogUtil.d(TAG, "bitmap : width : " + bitmap.getWidth() + " height : " + bitmap.getHeight());
//
//                Message msg = Message.obtain();
//                msg.what = COMPLETED;
//                msg.obj = new BitmapDrawable(bitmap);
//                handler.sendMessage(msg);
//
//                is.close();
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }).start();
//    }

    @Override
    public void onGetSongDetailSuccess(SongDetailBean bean) {
        LogUtil.d(TAG, "onGetSongDetailSuccess : " + bean);
        songDetail = bean;
        setSongDetailBean(songDetail);
        SongPlayManager.getInstance().putSongDetail(songDetail);
    }

    @Override
    public void onGetSongDetailFail(String e) {
        LogUtil.d(TAG, "onGetSongDetailFail : " + e);
        ToastUtils.show(e);
    }

    @Override
    public void onLikeMusicSuccess(LikeMusicBean bean) {
        LogUtil.d(TAG, "onLikeMusicSuccess : " + bean);
        if (bean.getCode() == 200) {
            ToastUtils.show("喜欢成功");
            ivLike.setImageResource(R.drawable.shape_like_white);
            isLike = true;
            LoginBean loginBean = GsonUtil.fromJSON(SharePreferenceUtil.getInstance(this).getUserInfo(""), LoginBean.class);
            mPresenter.getLikeList(loginBean.getAccount().getId());
        } else {
            ToastUtils.show("喜欢失败TAT ErrorCode = " + bean.getCode());
        }
    }

    @Override
    public void onLikeMusicFail(String e) {
        LogUtil.e(TAG, "onLikeMusicFail : " + e);
        ToastUtils.show(e);
    }

    @Override
    public void onGetLikeListSuccess(LikeListBean bean) {
        LogUtil.d(TAG, "onGetLikeListSuccess : " + bean);
        List<Long> idsList = bean.getIds();
        List<String> likeList = new ArrayList<>();
        for (int i = 0; i < idsList.size(); i++) {
            String ids = String.valueOf(idsList.get(i));
            likeList.add(ids);
        }
        SharePreferenceUtil.getInstance(this).saveLikeList(likeList);
    }

    @Override
    public void onGetLikeListFail(String e) {
        LogUtil.d(TAG, "onGetLikeListFail");
        ToastUtils.show(e);
    }

    @Override
    public void onGetMusicCommentSuccess(MusicCommentBean bean) {

    }

    @Override
    public void onGetMusicCommentFail(String e) {

    }

    @Override
    public void onLikeCommentSuccess(CommentLikeBean bean) {

    }

    @Override
    public void onLikeCommentFail(String e) {

    }

    @Override
    public void onGetLyricSuccess(LyricBean bean) {
        LogUtil.d(TAG, "onGetLyricSuccess : " + bean);
        if (bean.getLrc() != null) {
            if (bean.getTlyric().getLyric() != null) {
                lrc.loadLrc(bean.getLrc().getLyric(), bean.getTlyric().getLyric());
            } else {
                lrc.loadLrc(bean.getLrc().getLyric(), "");
            }
        } else {
            lrc.loadLrc("", "");
        }
        initLrcListener();
    }

    private void initLrcListener() {
        lrc.setListener(time -> {
            SongPlayManager.getInstance().seekTo(time);
            if (SongPlayManager.getInstance().isPaused()) {
                SongPlayManager.getInstance().playMusic();
            } else if (SongPlayManager.getInstance().isIdle()) {
                SongPlayManager.getInstance().clickASong(currentSongInfo);
            }
            return true;
        });

        lrc.setCoverChangeListener(() -> {
            showLyrics(false);
        });
    }

    @Override
    public void onGetLyricFail(String e) {
        LogUtil.e(TAG, "onGetLyricFail: " + e);
        ToastUtils.show(e);
    }

    @Override
    public void onGetPlaylistCommentSuccess(PlayListCommentBean bean) {

    }

    @Override
    public void onGetPlaylistCommentFail(String e) {

    }
}
