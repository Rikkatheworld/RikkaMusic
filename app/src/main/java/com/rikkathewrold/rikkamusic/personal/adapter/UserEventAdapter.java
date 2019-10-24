package com.rikkathewrold.rikkamusic.personal.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.lzx.starrysky.model.SongInfo;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseAdapter;
import com.rikkathewrold.rikkamusic.personal.bean.UserEventBean;
import com.rikkathewrold.rikkamusic.personal.bean.UserEventJsonBean;
import com.rikkathewrold.rikkamusic.personal.mvp.view.PictureCheckActivity;
import com.rikkathewrold.rikkamusic.song.mvp.view.SongActivity;
import com.rikkathewrold.rikkamusic.util.GsonUtil;
import com.rikkathewrold.rikkamusic.util.LogUtil;
import com.rikkathewrold.rikkamusic.util.TimeUtil;
import com.rikkathewrold.rikkamusic.video.mvp.view.VideoActivity;
import com.rikkathewrold.rikkamusic.widget.RikkaRoundRectView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.rikkathewrold.rikkamusic.base.BaseActivity.SONG_URL;

/**
 * 用户动态的适配器
 * 动态可以分享歌曲和视频
 */
public class UserEventAdapter extends BaseAdapter<RecyclerView.ViewHolder, UserEventBean.EventsBean> {
    private static final String TAG = "UserEventAdapter";

    private Context mContext;
    private List<UserEventBean.EventsBean> list = new ArrayList<>();
    private boolean isUserEvent;
    private OnEventClickListener listener;

    //是否为用户界面的动态
    public void setUserEvent(boolean userEvent) {
        isUserEvent = userEvent;
    }

    public UserEventAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void notifyDataSetChanged(List<UserEventBean.EventsBean> dataList) {
        this.list = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(getInflater().inflate(R.layout.item_user_event, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder != null) {
            ViewHolder vh = (ViewHolder) viewHolder;
            vh.setBean(mContext, i);
            vh.setListener(listener, i);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView relayCount, commentCount, likeCount;
        private CircleImageView ivAvatar;
        private TextView tvNickName, tvTitle, tvPublishTime;
        private TextView tvContent;
        private RikkaRoundRectView ivShow1, ivShow2, ivShow3, ivShow4, ivShow5, ivShow6, ivShow7, ivShow8, ivShow9;
        private RikkaRoundRectView ivSongCover, ivVideo;
        private TextView tvSongName, tvSongSinger;
        private RelativeLayout rlImg, rlShare, rlVideo, rlRelay, rlComment, rlLike;
        private LinearLayout llImg1, llImg2, llImg3;
        private List<ImageView> imgList = new ArrayList<>();

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgList.clear();
            relayCount = itemView.findViewById(R.id.tv_relayout_count);
            commentCount = itemView.findViewById(R.id.tv_comment_count);
            likeCount = itemView.findViewById(R.id.tv_like_count);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            tvNickName = itemView.findViewById(R.id.tv_nickname);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvPublishTime = itemView.findViewById(R.id.tv_publish_time);
            tvContent = itemView.findViewById(R.id.tv_content);
            ivShow1 = itemView.findViewById(R.id.iv_img_1);
            ivShow2 = itemView.findViewById(R.id.iv_img_2);
            ivShow3 = itemView.findViewById(R.id.iv_img_3);
            ivShow4 = itemView.findViewById(R.id.iv_img_4);
            ivShow5 = itemView.findViewById(R.id.iv_img_5);
            ivShow6 = itemView.findViewById(R.id.iv_img_6);
            ivShow7 = itemView.findViewById(R.id.iv_img_7);
            ivShow8 = itemView.findViewById(R.id.iv_img_8);
            ivShow9 = itemView.findViewById(R.id.iv_img_9);
            imgList.add(ivShow1);
            imgList.add(ivShow2);
            imgList.add(ivShow3);
            imgList.add(ivShow4);
            imgList.add(ivShow5);
            imgList.add(ivShow6);
            imgList.add(ivShow7);
            imgList.add(ivShow8);
            imgList.add(ivShow9);
            ivSongCover = itemView.findViewById(R.id.iv_song_cover);
            tvSongName = itemView.findViewById(R.id.tv_songname);
            tvSongSinger = itemView.findViewById(R.id.tv_creator_name);
            rlImg = itemView.findViewById(R.id.rl_img);
            llImg1 = itemView.findViewById(R.id.ll_img_group1);
            llImg2 = itemView.findViewById(R.id.ll_img_group2);
            llImg3 = itemView.findViewById(R.id.ll_img_group3);
            rlShare = itemView.findViewById(R.id.rl_share);
            ivVideo = itemView.findViewById(R.id.iv_vid);
            rlVideo = itemView.findViewById(R.id.rl_video);
            rlRelay = itemView.findViewById(R.id.rl_relay);
            rlComment = itemView.findViewById(R.id.rl_comment);
            rlLike = itemView.findViewById(R.id.rl_like);
        }

        /**
         * 根据evevnttype/type字段来判断动态是什么类型
         * 17、28：电台  18：单曲  22：转发  39：视频 35：图片
         */
        public void setBean(Context mContext, int i) {
            Glide.with(mContext).load(list.get(i).getUser().getAvatarUrl()).into(ivAvatar);
            tvNickName.setText(list.get(i).getUser().getNickname());
            tvPublishTime.setText(TimeUtil.getTimeStandardOnlyYMD(list.get(i).getShowTime()));
            relayCount.setText(list.get(i).getInfo().getShareCount() == 0 ? "转发" : "" + list.get(i).getInfo().getShareCount());
            commentCount.setText(list.get(i).getInfo().getCommentCount() == 0 ? "评论" : "" + list.get(i).getInfo().getCommentCount());
            likeCount.setText(list.get(i).getInfo().getLikedCount() == 0 ? "点赞" : "" + list.get(i).getInfo().getLikedCount());

            String json = list.get(i).getJson();
            LogUtil.d(TAG, "json : " + json);
            UserEventJsonBean jsonBean = GsonUtil.fromJSON(json, UserEventJsonBean.class);
            if (jsonBean != null) {
                LogUtil.d(TAG, "jsonBean:" + json);
                if (TextUtils.isEmpty(jsonBean.getMsg())) {
                    tvContent.setVisibility(View.GONE);
                } else {
                    tvContent.setVisibility(View.VISIBLE);
                    tvContent.setText(jsonBean.getMsg());
                }
            }

            int type;
            if (list.get(i).getInfo().getCommentThread().getResourceInfo() == null) {
                type = list.get(i).getType();
            } else {
                type = list.get(i).getInfo().getCommentThread().getResourceInfo().getEventType();
            }
            showImg(mContext, i);
            showShareLayout(mContext, jsonBean);
            switch (type) {
                case 18:
                    tvTitle.setText("分享单曲：");
                    break;
                case 17:
                case 28:
                    tvTitle.setText("分享电台：");
                    showDjBean(jsonBean);
                    break;
                case 22:
                    tvTitle.setText("转发：");
                    break;
                case 39:
                    tvTitle.setText("发布视频");
                    showVideoBean(jsonBean);
                    break;
                case 35:
                    break;
            }
        }

        private void showDjBean(UserEventJsonBean jsonBean) {

        }

        private void showVideoBean(UserEventJsonBean jsonBean) {
            rlVideo.setVisibility(View.VISIBLE);
            ivVideo.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(jsonBean.getVideo().getCoverUrl()).transition(new DrawableTransitionOptions().crossFade()).into(ivVideo);
        }

        //显示图片
        private void showImg(Context mContext, int position) {
            if (list.get(position).getPics() != null || list.get(position).getPics().size() != 0) {
                rlImg.setVisibility(View.VISIBLE);
                LogUtil.d(TAG, "size:" + list.get(position).getPics().size());
                for (int i = 0; i < list.get(position).getPics().size(); i++) {
                    if (i == 0) {
                        llImg1.setVisibility(View.VISIBLE);
                    }
                    if (i == 3) {
                        llImg2.setVisibility(View.VISIBLE);
                    }
                    if (i == 6) {
                        llImg3.setVisibility(View.VISIBLE);
                    }
                    Glide.with(mContext).load(list.get(position).getPics().get(i).getRectangleUrl()).transition(new DrawableTransitionOptions().crossFade()).into(imgList.get(i));
                }
            }
        }

        //分享 layout
        private void showShareLayout(Context mContext, UserEventJsonBean jsonBean) {
            if (jsonBean != null && jsonBean.getSong() != null && !TextUtils.isEmpty(jsonBean.getSong().getName())) {
                rlShare.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(jsonBean.getSong().getAlbum().getPicUrl()).transition(new DrawableTransitionOptions().crossFade()).into(ivSongCover);
                tvSongSinger.setText(jsonBean.getSong().getArtists().get(0).getName());
                tvSongName.setText(jsonBean.getSong().getName());
            } else if (jsonBean != null && jsonBean.getProgram() != null && !TextUtils.isEmpty(jsonBean.getProgram().getName())) {
                rlShare.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(jsonBean.getProgram().getCoverUrl()).transition(new DrawableTransitionOptions().crossFade()).into(ivSongCover);
                tvSongSinger.setText(jsonBean.getProgram().getRadio().getName());
                tvSongName.setText(jsonBean.getProgram().getName());
            } else {
                rlShare.setVisibility(View.GONE);
            }
        }

        public void setListener(OnEventClickListener listener, int position) {
            Intent intent = new Intent();
            ivShow1.setOnClickListener(v -> {
                intent.setClass(mContext, PictureCheckActivity.class);
                intent.putExtra(PictureCheckActivity.PIC_URL, list.get(position).getPics().get(0).getRectangleUrl());
                mContext.startActivity(intent);
            });

            ivShow2.setOnClickListener(v -> {
                intent.setClass(mContext, PictureCheckActivity.class);
                intent.putExtra(PictureCheckActivity.PIC_URL, list.get(position).getPics().get(1).getRectangleUrl());
                mContext.startActivity(intent);
            });

            ivShow3.setOnClickListener(v -> {
                intent.setClass(mContext, PictureCheckActivity.class);
                intent.putExtra(PictureCheckActivity.PIC_URL, list.get(position).getPics().get(2).getRectangleUrl());
                mContext.startActivity(intent);
            });

            ivShow4.setOnClickListener(v -> {
                intent.setClass(mContext, PictureCheckActivity.class);
                intent.putExtra(PictureCheckActivity.PIC_URL, list.get(position).getPics().get(3).getRectangleUrl());
                mContext.startActivity(intent);
            });

            ivShow5.setOnClickListener(v -> {
                intent.setClass(mContext, PictureCheckActivity.class);
                intent.putExtra(PictureCheckActivity.PIC_URL, list.get(position).getPics().get(4).getRectangleUrl());
                mContext.startActivity(intent);
            });

            ivShow6.setOnClickListener(v -> {
                intent.setClass(mContext, PictureCheckActivity.class);
                intent.putExtra(PictureCheckActivity.PIC_URL, list.get(position).getPics().get(5).getRectangleUrl());
                mContext.startActivity(intent);
            });

            ivShow7.setOnClickListener(v -> {
                intent.setClass(mContext, PictureCheckActivity.class);
                intent.putExtra(PictureCheckActivity.PIC_URL, list.get(position).getPics().get(6).getRectangleUrl());
                mContext.startActivity(intent);
            });

            ivShow8.setOnClickListener(v -> {
                intent.setClass(mContext, PictureCheckActivity.class);
                intent.putExtra(PictureCheckActivity.PIC_URL, list.get(position).getPics().get(7).getRectangleUrl());
                mContext.startActivity(intent);
            });

            ivShow9.setOnClickListener(v -> {
                intent.setClass(mContext, PictureCheckActivity.class);
                intent.putExtra(PictureCheckActivity.PIC_URL, list.get(position).getPics().get(8).getRectangleUrl());
                mContext.startActivity(intent);
            });
            String json = list.get(position).getJson();
            UserEventJsonBean jsonBean = GsonUtil.fromJSON(json, UserEventJsonBean.class);
            UserEventJsonBean.VideoBean videoBean = new UserEventJsonBean.VideoBean();
            if (jsonBean != null && jsonBean.getVideo() != null) {
                videoBean = jsonBean.getVideo();
            }

            rlShare.setOnClickListener(v -> {
                intent.setClass(mContext, SongActivity.class);
                SongInfo songInfo = new SongInfo();
                if (jsonBean.getSong() != null) {
                    UserEventJsonBean.SongBean songBean = new UserEventJsonBean.SongBean();
                    songInfo.setDuration(songBean.getDuration());
                    songInfo.setArtist(songBean.getArtists().get(0).getName());
                    songInfo.setSongId(String.valueOf(songBean.getId()));
                    songInfo.setSongUrl(SONG_URL + songInfo.getSongId() + ".mp3");
                    songInfo.setSongName(songBean.getName());
                    songInfo.setSongCover(songBean.getAlbum().getBlurPicUrl());
                } else if (jsonBean.getProgram() != null) {
                    UserEventJsonBean.ProgramBean programBean = jsonBean.getProgram();
                    songInfo.setDuration(programBean.getDuration());
                    songInfo.setArtist(programBean.getDj().getNickname());
                    songInfo.setSongId(String.valueOf(programBean.getId()));
                    songInfo.setSongUrl(SONG_URL + programBean.getId() + ".mp3");
                    songInfo.setSongName(programBean.getName());
                    songInfo.setSongCover(programBean.getCoverUrl());
                }
                intent.putExtra(SongActivity.SONG_INFO, songInfo);
                mContext.startActivity(intent);
            });

            rlVideo.setOnClickListener(v -> {
                intent.setClass(mContext, VideoActivity.class);
                mContext.startActivity(intent);
            });

            ivAvatar.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAvatarClick(position);
                }
            });

            rlRelay.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onRelayClick(position);
                }
            });

            rlComment.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCommentClick(position);
                }
            });

            rlLike.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onLikeClick(position);
                }
            });
        }
    }

    public interface OnEventClickListener {
        void onAvatarClick(int position);

        void onRelayClick(int position);

        void onCommentClick(int position);

        void onLikeClick(int position);
    }

    public void setListener(OnEventClickListener listener) {
        this.listener = listener;
    }
}
