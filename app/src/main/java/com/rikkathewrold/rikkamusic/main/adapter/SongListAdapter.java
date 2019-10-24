package com.rikkathewrold.rikkamusic.main.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
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
import com.rikkathewrold.rikkamusic.manager.SongPlayManager;
import com.rikkathewrold.rikkamusic.song.mvp.view.SongActivity;
import com.rikkathewrold.rikkamusic.song.mvp.view.SongDetailActivity;
import com.rikkathewrold.rikkamusic.util.LogUtil;
import com.rikkathewrold.rikkamusic.widget.RikkaRoundRectView;

import java.util.List;

/**
 * 歌单所有歌曲列表的adapter
 * 用SongListbean来解耦
 * 内容是 songInfo
 */
public class SongListAdapter extends BaseAdapter<RecyclerView.ViewHolder, SongInfo> {
    private static final String TAG = "SongListAdapter";

    public List<SongInfo> list;
    private Context mContext;
    private int type;
    public String keywords;


    public SongListAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void notifyDataSetChanged(List<SongInfo> dataList) {
        list = dataList;
        notifyDataSetChanged();
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    /**
     * type=1：有封面 ,用于日推列表
     * type=2：有序号  用于歌单列表
     * type=3：无序号、无封面  用于搜索列表
     * type=4：无序号、无封面，有手机icon 用于本地音乐
     */
    public void setType(int type) {
        this.type = type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(getInflater().inflate(R.layout.item_songlist, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolder) {
            if (list == null || list.isEmpty()) {
                return;
            }
            ViewHolder vh = (ViewHolder) viewHolder;
            SongInfo bean = list.get(position);
            if (type == 3) {
                vh.setSongInfo(mContext, bean, keywords);
            } else {
                vh.setSongInfo(mContext, bean, position, type);
            }
            vh.setSongClick(bean, position);
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : type == 1 ? 20 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvSinger;
        TextView tvSongNumber;
        RikkaRoundRectView ivCover;
        ImageView ivSongDetail;
        LinearLayout llSong;
        ImageView ivPhone;
        RelativeLayout rlSong;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_songname);
            tvSinger = itemView.findViewById(R.id.tv_singer);
            tvSongNumber = itemView.findViewById(R.id.iv_songnumber);
            ivCover = itemView.findViewById(R.id.iv_songcover);
            ivSongDetail = itemView.findViewById(R.id.iv_songdetail);
            llSong = itemView.findViewById(R.id.ll_song);
            ivPhone = itemView.findViewById(R.id.iv_phone);
            rlSong = itemView.findViewById(R.id.rl_song);
        }


        void setSongInfo(Context context, SongInfo bean, int position, int type) {
            LogUtil.d(TAG, "setSongBean : " + tvName + " ," + tvSinger);
            tvName.setText(bean.getSongName());
            tvSinger.setText(bean.getArtist());
            if (type == 1) {
                ivCover.setVisibility(View.VISIBLE);
                Glide.with(context).load(bean.getSongCover()).transition(new DrawableTransitionOptions().crossFade()).into(ivCover);
            } else if (type == 2) {
                tvSongNumber.setVisibility(View.VISIBLE);
                LogUtil.d(TAG, "position : " + position);
                tvSongNumber.setText((position + 1) + "");
            } else if (type == 4) {
                ivPhone.setVisibility(View.VISIBLE);
                ivSongDetail.setVisibility(View.GONE);
            }
        }

        void setSongInfo(Context context, SongInfo bean, String keywords) {
            LogUtil.d(TAG, "setSongBean : " + tvName + " ," + tvSinger);
            if (bean.getSongName().contains(keywords)) {
                int start = bean.getSongName().indexOf(keywords);
                int end = start + keywords.length();
                String resString = bean.getSongName();
                SpannableStringBuilder style = new SpannableStringBuilder(resString);
                style.setSpan(new ForegroundColorSpan(Color.parseColor(context.getString(R.string.colorBlue))), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvName.setText(style);
            } else {
                tvName.setText(bean.getSongName());
            }
            if (bean.getArtist().contains(keywords)) {
                int start = bean.getArtist().indexOf(keywords);
                int end = start + keywords.length();
                String resString = bean.getArtist();
                SpannableStringBuilder style = new SpannableStringBuilder(resString);
                style.setSpan(new ForegroundColorSpan(Color.parseColor(context.getString(R.string.colorBlue))), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvSinger.setText(style);
            } else {
                tvSinger.setText(bean.getArtist());
            }
        }

        void setSongClick(SongInfo songInfo, int position) {
            rlSong.setOnClickListener(v -> {
                if (type == 3) {
                    SongPlayManager.getInstance().clickASong(songInfo);
                } else {
                    SongPlayManager.getInstance().clickPlayAll(list, position);
                }
                Intent intent = new Intent(mContext, SongActivity.class);
                intent.putExtra(SongActivity.SONG_INFO, songInfo);
                mContext.startActivity(intent);
            });

            ivSongDetail.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, SongDetailActivity.class);
                intent.putExtra(SongActivity.SONG_INFO, songInfo);
                mContext.startActivity(intent);
                ((Activity) mContext).overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent);
            });
        }

    }
}
