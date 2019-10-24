package com.rikkathewrold.rikkamusic.main.mvp.view;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hjq.toast.ToastUtils;
import com.lzx.starrysky.model.SongInfo;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseActivity;
import com.rikkathewrold.rikkamusic.main.adapter.SongListAdapter;
import com.rikkathewrold.rikkamusic.main.bean.BannerBean;
import com.rikkathewrold.rikkamusic.main.bean.DailyRecommendBean;
import com.rikkathewrold.rikkamusic.main.bean.HighQualityPlayListBean;
import com.rikkathewrold.rikkamusic.main.bean.MainRecommendPlayListBean;
import com.rikkathewrold.rikkamusic.main.bean.PlaylistDetailBean;
import com.rikkathewrold.rikkamusic.main.bean.RecommendPlayListBean;
import com.rikkathewrold.rikkamusic.main.bean.TopListBean;
import com.rikkathewrold.rikkamusic.main.mvp.contract.WowContract;
import com.rikkathewrold.rikkamusic.main.mvp.presenter.WowPresenter;
import com.rikkathewrold.rikkamusic.manager.SongPlayManager;
import com.rikkathewrold.rikkamusic.manager.bean.MusicCanPlayBean;
import com.rikkathewrold.rikkamusic.song.mvp.view.CommentActivity;
import com.rikkathewrold.rikkamusic.util.AppBarStateChangeListener;
import com.rikkathewrold.rikkamusic.util.DensityUtil;
import com.rikkathewrold.rikkamusic.util.LogUtil;
import com.rikkathewrold.rikkamusic.widget.BottomSongPlayBar;
import com.rikkathewrold.rikkamusic.widget.RikkaRoundRectView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.rikkathewrold.rikkamusic.main.mvp.view.fragments.WowFragment.PLAYLIST_CREATOR_AVATARURL;
import static com.rikkathewrold.rikkamusic.main.mvp.view.fragments.WowFragment.PLAYLIST_CREATOR_NICKNAME;
import static com.rikkathewrold.rikkamusic.main.mvp.view.fragments.WowFragment.PLAYLIST_ID;
import static com.rikkathewrold.rikkamusic.main.mvp.view.fragments.WowFragment.PLAYLIST_NAME;
import static com.rikkathewrold.rikkamusic.main.mvp.view.fragments.WowFragment.PLAYLIST_PICURL;

/**
 * 歌单列表界面
 * 该界面就是充满歌曲的歌单列表
 * <p>
 * Created By Rikka on 2019/7/18
 */
public class PlayListActivity extends BaseActivity<WowPresenter> implements WowContract.View {
    private static final String TAG = "PlayListActivity";

    //计算完成后发送的Handler msg
    public static final int COMPLETED = 0;

    @BindView(R.id.iv_cover)
    RikkaRoundRectView ivCover;
    @BindView(R.id.tv_playlist_name)
    TextView tvListName;
    @BindView(R.id.iv_creator_avatar)
    CircleImageView ivCreatorAvatar;
    @BindView(R.id.tv_creator_name)
    TextView tvCreatorName;
    @BindView(R.id.tv_comment)
    TextView tvComment;
    @BindView(R.id.tv_share)
    TextView tvShare;
    @BindView(R.id.rv_playlist_song)
    RecyclerView rvPlaylist;
    @BindView(R.id.bottom_controller)
    BottomSongPlayBar bottomController;
    @BindView(R.id.background)
    ImageView ivBg;
    @BindView(R.id.appbar)
    AppBarLayout appBar;
    @BindView(R.id.ll_play)
    RelativeLayout llPlay;
    @BindView(R.id.iv_cover_bg)
    ImageView ivCoverBg;

    private SongListAdapter adapter;
    private List<PlaylistDetailBean.PlaylistBean.TracksBean> beanList = new ArrayList<>();

    private List<SongInfo> songInfos = new ArrayList<>();
    private long playlistId;
    private int position = -1;
    int deltaDistance;
    int minDistance;
    private String creatorUrl;
    private ObjectAnimator alphaAnimator;
    private ObjectAnimator coverAlphaAnimator;
    private String playlistName;
    private String playlistPicUrl;
    private String creatorName;

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_play_list);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    @Override
    protected WowPresenter onCreatePresenter() {
        return new WowPresenter(this);
    }

    @Override
    protected void initModule() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        setBackBtn(getString(R.string.colorWhite));
        setLeftTitleTextColorWhite();
        setLeftTitleText(R.string.playlist);
        beanList.clear();

        adapter = new SongListAdapter(this);
        adapter.setType(2);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvPlaylist.setLayoutManager(manager);
        rvPlaylist.setAdapter(adapter);

        if (getIntent() != null) {
            playlistPicUrl = getIntent().getStringExtra(PLAYLIST_PICURL);
            Glide.with(this).load(playlistPicUrl).into(ivCover);
            playlistName = getIntent().getStringExtra(PLAYLIST_NAME);
            tvListName.setText(playlistName);
            creatorName = getIntent().getStringExtra(PLAYLIST_CREATOR_NICKNAME);
            tvCreatorName.setText(creatorName);
            creatorUrl = getIntent().getStringExtra(PLAYLIST_CREATOR_AVATARURL);
            Glide.with(this).load(creatorUrl).into(ivCreatorAvatar);
            playlistId = getIntent().getLongExtra(PLAYLIST_ID, 0);

            calculateColors(playlistPicUrl);
            Glide.with(this)
                    .load(playlistPicUrl)
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(10, 10)))
                    .into(ivCoverBg);
            ivCoverBg.setAlpha(0f);
            showDialog();
            LogUtil.d(TAG, "playlistId : " + playlistId);
            mPresenter.getPlaylistDetail(playlistId);
        }
        minDistance = DensityUtil.dp2px(PlayListActivity.this, 85);
        deltaDistance = DensityUtil.dp2px(getApplication().getApplicationContext(), 300) - minDistance;
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == COMPLETED) {
                ivBg.setBackground((Drawable) msg.obj);
                getAlphaAnimatorBg().start();
                getAlphaAnimatorCover().start();
            }
        }
    };

    private ObjectAnimator getAlphaAnimatorCover() {
        if (coverAlphaAnimator == null) {
            coverAlphaAnimator = ObjectAnimator.ofFloat(ivCoverBg, "alpha", 0f, 0.5f);
            coverAlphaAnimator.setDuration(1500);
            coverAlphaAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        }
        return coverAlphaAnimator;
    }

    private ObjectAnimator getAlphaAnimatorBg() {
        if (alphaAnimator == null) {
            alphaAnimator = ObjectAnimator.ofFloat(ivBg, "alpha", 0f, 0.5f);
            alphaAnimator.setDuration(1500);
            alphaAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        }
        return alphaAnimator;
    }

    /**
     * 该方法用url申请一个图片bitmap，并将其压缩成原图1/300，计算上半部分和下半部分颜色RGB平均值
     * 两个RGB去作为渐变色的两个点
     * 还要开子线程去计算...
     */
    public void calculateColors(String url) {
        new Thread(() -> {
            try {
                //渐变色的两个颜色
                URL fileUrl;
                Bitmap bitmap;
                fileUrl = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) fileUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inSampleSize = 270;
                bitmap = BitmapFactory.decodeStream(is, new Rect(), opt);

                Message msg = Message.obtain();
                msg.what = COMPLETED;
                msg.obj = new BitmapDrawable(bitmap);
                handler.sendMessage(msg);

                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    @OnClick({R.id.rl_playall, R.id.rl_comment, R.id.rl_download})
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.rl_playall:
                SongPlayManager.getInstance().clickPlayAll(songInfos, 0);
                break;
            case R.id.rl_download:
                ToastUtils.show("下载不了哦~");
                break;
            case R.id.rl_comment:
                intent.setClass(PlayListActivity.this, CommentActivity.class);
                intent.putExtra(CommentActivity.FROM, CommentActivity.PLAYLIST_COMMENT);
                intent.putExtra(CommentActivity.ID, playlistId);
                intent.putExtra(CommentActivity.NAME, playlistName);
                intent.putExtra(CommentActivity.ARTIST, creatorName);
                intent.putExtra(CommentActivity.COVER, playlistPicUrl);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        appBar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {

            }

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout) {
                float alphaPercent = (float) (llPlay.getTop() - minDistance) / (float) deltaDistance;
                ivCover.setAlpha(alphaPercent);
                ivCreatorAvatar.setAlpha(alphaPercent);
                tvListName.setAlpha(alphaPercent);
                tvCreatorName.setAlpha(alphaPercent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (alphaAnimator != null) {
            if (alphaAnimator.isRunning()) {
                alphaAnimator.cancel();
            }
            alphaAnimator = null;
        }
        if (coverAlphaAnimator != null) {
            if (coverAlphaAnimator.isRunning()) {
                coverAlphaAnimator.cancel();
            }
            coverAlphaAnimator = null;
        }
    }

    @Override
    public void onGetBannerSuccess(BannerBean bean) {

    }

    @Override
    public void onGetBannerFail(String e) {

    }

    @Override
    public void onGetRecommendPlayListSuccess(MainRecommendPlayListBean bean) {

    }

    @Override
    public void onGetRecommendPlayListFail(String e) {

    }

    @Override
    public void onGetDailyRecommendSuccess(DailyRecommendBean bean) {

    }

    @Override
    public void onGetDailyRecommendFail(String e) {

    }

    @Override
    public void onGetTopListSuccess(TopListBean bean) {

    }

    @Override
    public void onGetTopListFail(String e) {

    }

    @Override
    public void onGetPlayListSuccess(RecommendPlayListBean bean) {

    }

    @Override
    public void onGetPlayListFail(String e) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onGetPlaylistDetailSuccess(PlaylistDetailBean bean) {
        hideDialog();
        LogUtil.d(TAG, "onGetPlaylistDetailSuccess : " + bean);
        if (!TextUtils.isEmpty(creatorUrl)) {
            Glide.with(this).load(bean.getPlaylist().getCreator().getAvatarUrl()).into(ivCreatorAvatar);
        }
        beanList.addAll(bean.getPlaylist().getTracks());
        songInfos.clear();
        for (int i = 0; i < beanList.size(); i++) {
            SongInfo beanInfo = new SongInfo();
            beanInfo.setArtist(beanList.get(i).getAr().get(0).getName());
            beanInfo.setSongName(beanList.get(i).getName());
            beanInfo.setSongCover(beanList.get(i).getAl().getPicUrl());
            beanInfo.setSongId(String.valueOf(beanList.get(i).getId()));
            beanInfo.setDuration(beanList.get(i).getDt());
            beanInfo.setSongUrl(SONG_URL + beanList.get(i).getId() + ".mp3");
            beanInfo.setArtistId(String.valueOf(beanList.get(i).getAr().get(0).getId()));
            beanInfo.setArtistKey(beanList.get(i).getAl().getPicUrl());
            songInfos.add(beanInfo);
        }
        adapter.notifyDataSetChanged(songInfos);
        tvShare.setText(bean.getPlaylist().getShareCount() + "");
        tvComment.setText(bean.getPlaylist().getCommentCount() + "");

    }

    @Override
    public void onGetPlaylistDetailFail(String e) {
        hideDialog();
        LogUtil.e(TAG, "onGetPlaylistDetailFail : " + e);
        ToastUtils.show(e);
    }

    @Override
    public void onGetMusicCanPlaySuccess(MusicCanPlayBean bean) {
    }

    @Override
    public void onGetMusicCanPlayFail(String e) {
    }

    @Override
    public void onGetHighQualitySuccess(HighQualityPlayListBean bean) {

    }

    @Override
    public void onGetHighQualityFail(String e) {

    }
}
