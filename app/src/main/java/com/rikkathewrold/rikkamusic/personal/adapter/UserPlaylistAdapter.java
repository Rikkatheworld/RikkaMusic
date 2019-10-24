package com.rikkathewrold.rikkamusic.personal.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseAdapter;
import com.rikkathewrold.rikkamusic.personal.bean.PlayListItemBean;
import com.rikkathewrold.rikkamusic.widget.RikkaRoundRectView;

import java.util.List;

/**
 * 用户界面的 歌单列表Adapter
 * Created By Rikka on 2019/7/25
 */
public class UserPlaylistAdapter extends BaseAdapter<RecyclerView.ViewHolder, PlayListItemBean> {
    private static final String TAG = "UserPlaylistAdapter";

    private Context mContext;
    private List<PlayListItemBean> list;
    private OnPlayListItemClickListener listener;
    private String nickname;
    private boolean isShowSmartPlay = false;

    public UserPlaylistAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void notifyDataSetChanged(List<PlayListItemBean> dataList) {
        list = dataList;
        notifyDataSetChanged();
    }

    public void setShowSmartPlay(boolean showSmartPlay) {
        isShowSmartPlay = showSmartPlay;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(getInflater().inflate(R.layout.item_playlist_fragment, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof UserPlaylistAdapter.ViewHolder) {
            UserPlaylistAdapter.ViewHolder vh = (UserPlaylistAdapter.ViewHolder) viewHolder;
            PlayListItemBean bean = list.get(i);
            vh.setPlayListItemInfo(mContext, bean, i);
            vh.onSetListClickListener(listener, i);
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setName(String nickName) {
        this.nickname = nickName;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RikkaRoundRectView ivCover;
        private TextView tvName, tvPlayListInfo;
        private LinearLayout llItem;
        private RelativeLayout rlSmartPlay;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.iv_cover);
            tvName = itemView.findViewById(R.id.tv_playlist_name);
            tvPlayListInfo = itemView.findViewById(R.id.tv_playlist_info);
            llItem = itemView.findViewById(R.id.ll_item);
            rlSmartPlay = itemView.findViewById(R.id.rl_heart_play);
        }

        @SuppressLint("SetTextI18n")
        void setPlayListItemInfo(Context mContext, PlayListItemBean bean, int position) {
            Glide.with(mContext).load(bean.getCoverUrl()).transition(new DrawableTransitionOptions().crossFade()).into(ivCover);
            tvName.setText(bean.getPlayListName());
            long playcount = bean.getPlayCount();
            String count;
            if (playcount >= 10000) {
                playcount = playcount / 10000;
                count = playcount + "万次";
            } else {
                count = playcount + "次";
            }
            if (nickname.equals(bean.getPlaylistCreator())) {
                tvPlayListInfo.setText(bean.getSongNumber() + "首，播放" + count);
            } else {
                tvPlayListInfo.setText(bean.getSongNumber() + "首，by " + bean.getPlaylistCreator() + "，播放" + count);
            }
            if (isShowSmartPlay && position == 0) {
                rlSmartPlay.setVisibility(View.VISIBLE);
            } else {
                rlSmartPlay.setVisibility(View.GONE);
            }
        }

        void onSetListClickListener(OnPlayListItemClickListener listener, int i) {
            rlSmartPlay.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onSmartPlayClick(getAdapterPosition());
                }
            });

            llItem.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onPlayListItemClick(i);
                }
            });
        }
    }

    public interface OnPlayListItemClickListener {
        void onPlayListItemClick(int position);

        void onSmartPlayClick(int position);
    }

    public void setListener(OnPlayListItemClickListener listener) {
        this.listener = listener;
    }
}
