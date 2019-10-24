package com.rikkathewrold.rikkamusic.main.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseAdapter;
import com.rikkathewrold.rikkamusic.main.bean.PlaylistBean;
import com.rikkathewrold.rikkamusic.widget.RikkaRoundRectView;

import java.util.List;

public class PlayListAdapter extends BaseAdapter<RecyclerView.ViewHolder, PlaylistBean> {
    private static final String TAG = "PlayListAdapter";

    private List<PlaylistBean> list;
    private Context mContext;
    private OnPlayListClickListener listener;
    private int type;

    public PlayListAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void notifyDataSetChanged(List<PlaylistBean> dataList) {
        list = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(getInflater().inflate(R.layout.item_playlist, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ViewHolder) {
            ViewHolder vh = (ViewHolder) viewHolder;
            PlaylistBean bean = list.get(i);
            vh.setPlayListInfo(mContext, bean);
            vh.onSetListClickListener(listener, i);
        }
    }

    public void setType(int type) {
        this.type = type;
    }

    //type == 1 展示6个  type == 2 展示3个
    @Override
    public int getItemCount() {
        return list == null ? 0 : type == 1 ? 6 : list.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout rlPlayList;
        private RikkaRoundRectView imgPlayList;
        private TextView tvPlayListName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            rlPlayList = itemView.findViewById(R.id.rl_playlist);
            imgPlayList = itemView.findViewById(R.id.iv_playlist);
            tvPlayListName = itemView.findViewById(R.id.tv_playlist_name);
        }

        void setPlayListInfo(Context context, PlaylistBean bean) {
            Glide.with(context)
                    .load(bean.getPlaylistCoverUrl())
                    .transition(new DrawableTransitionOptions().crossFade())
                    .into(imgPlayList);
            tvPlayListName.setText(bean.getPlaylistName());
        }

        void onSetListClickListener(OnPlayListClickListener listener, int position) {
            rlPlayList.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onClickListener(position);
                }
            });
        }
    }

    public interface OnPlayListClickListener {
        void onClickListener(int position);
    }

    public void setListener(OnPlayListClickListener listener) {
        this.listener = listener;
    }
}
