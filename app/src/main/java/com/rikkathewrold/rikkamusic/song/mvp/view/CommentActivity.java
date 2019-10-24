package com.rikkathewrold.rikkamusic.song.mvp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.toast.ToastUtils;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseActivity;
import com.rikkathewrold.rikkamusic.main.bean.LikeListBean;
import com.rikkathewrold.rikkamusic.song.adapter.CommentAdapter;
import com.rikkathewrold.rikkamusic.song.bean.CommentLikeBean;
import com.rikkathewrold.rikkamusic.song.bean.LikeMusicBean;
import com.rikkathewrold.rikkamusic.song.bean.LyricBean;
import com.rikkathewrold.rikkamusic.song.bean.MusicCommentBean;
import com.rikkathewrold.rikkamusic.song.bean.PlayListCommentBean;
import com.rikkathewrold.rikkamusic.song.bean.SongDetailBean;
import com.rikkathewrold.rikkamusic.song.mvp.contract.SongContract;
import com.rikkathewrold.rikkamusic.song.mvp.presenter.SongPresenter;
import com.rikkathewrold.rikkamusic.util.LogUtil;
import com.rikkathewrold.rikkamusic.widget.RikkaRoundRectView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 歌曲、歌单、专辑等的评论界面
 */
public class CommentActivity extends BaseActivity<SongPresenter> implements SongContract.View {
    private static final String TAG = "CommentActivity";

    public static final String COVER = "cover";
    public static final String NAME = "name";
    public static final String ARTIST = "artist";
    public static final String ID = "id";
    public static final String FROM = "from";
    public static final int SONG_COMMENT = 0x001;
    public static final int PLAYLIST_COMMENT = 0x002;
    public static final int ALBUM_COMMENT = 0x003;

    @BindView(R.id.iv_cover)
    RikkaRoundRectView ivCover;
    @BindView(R.id.tv_music_name)
    TextView tvSongName;
    @BindView(R.id.tv_artist)
    TextView tvArtist;
    @BindView(R.id.rv_hot_comment)
    RecyclerView rvHotComment;
    @BindView(R.id.rv_new_comment)
    RecyclerView rvNewComment;

    private int from;
    private CommentAdapter hotAdapter, newAdapter;
    private long id;
    private List<MusicCommentBean.CommentsBean> hotCommentList = new ArrayList<>();
    private List<MusicCommentBean.CommentsBean> newCommentList = new ArrayList<>();

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_comment);
        ImmersionBar.with(this)
                .transparentBar()
                .statusBarColor(R.color.colorPrimary)
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
    }

    @Override
    protected void initData() {
        setBackBtn(getString(R.string.colorWhite));

        Intent intent = getIntent();
        id = intent.getLongExtra(ID, 0);
        Glide.with(this).load(intent.getStringExtra(COVER)).into(ivCover);
        tvSongName.setText(intent.getStringExtra(NAME));
        tvArtist.setText(intent.getStringExtra(ARTIST));
        from = intent.getIntExtra(FROM, 0x001);

        rvHotComment.setLayoutManager(new LinearLayoutManager(this));
        rvNewComment.setLayoutManager(new LinearLayoutManager(this));
        hotAdapter = new CommentAdapter(this);
        newAdapter = new CommentAdapter(this);
        rvHotComment.setAdapter(hotAdapter);
        rvNewComment.setAdapter(newAdapter);

        showDialog();
        switch (from) {
            case SONG_COMMENT:
                mPresenter.getMusicComment(id);
                break;
            case PLAYLIST_COMMENT:
                mPresenter.getPlaylistComment(id);
                break;
        }
    }


    CommentAdapter.OnLikeCommentListener hotListener = position -> {
    };

    CommentAdapter.OnLikeCommentListener newListener = position -> {
    };

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onGetSongDetailSuccess(SongDetailBean bean) {

    }

    @Override
    public void onGetSongDetailFail(String e) {

    }

    @Override
    public void onLikeMusicSuccess(LikeMusicBean bean) {

    }

    @Override
    public void onLikeMusicFail(String e) {

    }

    @Override
    public void onGetLikeListSuccess(LikeListBean bean) {

    }

    @Override
    public void onGetLikeListFail(String e) {

    }

    @Override
    public void onGetMusicCommentSuccess(MusicCommentBean bean) {
        hideDialog();
        LogUtil.d(TAG, "onGetMusicCommentSuccess : " + bean);
        setLeftTitleText(getString(R.string.comment) + "(" + bean.getTotal() + ")", getString(R.string.colorWhite));
        notifyList(bean.getHotComments(), bean.getComments());
    }

    private void notifyList(List<MusicCommentBean.CommentsBean> hotComments, List<MusicCommentBean.CommentsBean> comments) {
        hotCommentList.clear();
        newCommentList.clear();
        if (hotComments != null) {
            hotCommentList = hotComments;
        }
        if (comments != null) {
            newCommentList = comments;
        }
        hotAdapter.notifyDataSetChanged(hotCommentList);
        newAdapter.notifyDataSetChanged(newCommentList);
    }

    @Override
    public void onGetMusicCommentFail(String e) {
        hideDialog();
        LogUtil.d(TAG, "onGetMusicCommentFail : " + e);
        ToastUtils.show(e);
    }

    @Override
    public void onLikeCommentSuccess(CommentLikeBean bean) {
        LogUtil.d(TAG, "onLikeCommentSuccess :" + bean);
        if (bean.getCode() == 200) {
            mPresenter.getMusicComment(id);
        } else {
            ToastUtils.show(bean.getCode());
        }
    }

    @Override
    public void onLikeCommentFail(String e) {
        LogUtil.d(TAG, "onLikeCommentFail :" + e);
        ToastUtils.show(e);
    }

    @Override
    public void onGetLyricSuccess(LyricBean bean) {

    }

    @Override
    public void onGetLyricFail(String e) {

    }

    @Override
    public void onGetPlaylistCommentSuccess(PlayListCommentBean bean) {
        hideDialog();
        LogUtil.d(TAG, "onGetPlaylistCommentSuccess : " + bean);
        setLeftTitleText(getString(R.string.comment) + "(" + bean.getTotal() + ")", getString(R.string.colorWhite));
        notifyList(bean.getHotComments(), bean.getComments());
    }

    @Override
    public void onGetPlaylistCommentFail(String e) {
        hideDialog();
        LogUtil.d(TAG, "onGetPlaylistCommentFail : " + e);
        ToastUtils.show(e);
    }
}
