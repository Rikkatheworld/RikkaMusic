package com.rikkathewrold.rikkamusic.search.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseAdapter;
import com.rikkathewrold.rikkamusic.search.bean.AlbumAdapterBean;
import com.rikkathewrold.rikkamusic.util.LogUtil;
import com.rikkathewrold.rikkamusic.util.TimeUtil;
import com.rikkathewrold.rikkamusic.widget.RikkaRoundRectView;

import java.util.List;

public class AlbumAdapter extends BaseAdapter<RecyclerView.ViewHolder, AlbumAdapterBean> {
    private static final String TAG = "AlbumAdapter";

    private Context mContext;
    private List<AlbumAdapterBean> list;
    private String keywords;
    private OnAlbumClickListener listener;
    private int type;

    public AlbumAdapter(Context context) {
        super(context);
        mContext = context;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    @Override
    public void notifyDataSetChanged(List<AlbumAdapterBean> dataList) {
        list = dataList;
        notifyDataSetChanged();
    }

    //type == 1 显示singgername、创建时间
    //type == 2 显示歌曲数目、创建时间
    public void setType(int type) {
        this.type = type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(getInflater().inflate(R.layout.item_album_search, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolder vh = (ViewHolder) viewHolder;
        if (vh != null) {
            vh.setBean(mContext, list.get(i), keywords, type);
            vh.setListener(listener, i);
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RikkaRoundRectView ivCover;
        private TextView tvName, tvDescription, tvSinger;
        private RelativeLayout rlAlbum;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.iv_cover);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDescription = itemView.findViewById(R.id.tv_description);
            rlAlbum = itemView.findViewById(R.id.rl_album);
            tvSinger = itemView.findViewById(R.id.tv_singer);
        }

        @SuppressLint("SetTextI18n")
        void setBean(Context context, AlbumAdapterBean bean, String keywords, int type) {
            Glide.with(context)
                    .load(bean.getAlbumCoverUrl())
                    .placeholder(R.drawable.shape_album)
                    .transition(new DrawableTransitionOptions().crossFade())
                    .into(ivCover);
            if (type == 1) {
                tvSinger.setVisibility(View.VISIBLE);
                if (bean.getAlbumName().contains(keywords)) {
                    int start = bean.getAlbumName().indexOf(keywords);
                    int end = start + keywords.length();
                    String resString = bean.getAlbumName();
                    SpannableStringBuilder style = new SpannableStringBuilder(resString);
                    style.setSpan(new ForegroundColorSpan(Color.parseColor(context.getString(R.string.colorBlue))), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tvName.setText(style);
                } else {
                    tvName.setText(bean.getAlbumName());
                }
                String artistName = bean.getSinger();
                LogUtil.d(TAG, "artistName :" + artistName + " keywords:" + keywords);
                if (artistName.contains(keywords)) {
                    int start = artistName.indexOf(keywords);
                    int end = start + keywords.length();
                    LogUtil.d(TAG, "start :" + start + " end:" + end);
                    SpannableStringBuilder style = new SpannableStringBuilder(artistName);
                    style.setSpan(new ForegroundColorSpan(Color.parseColor(context.getString(R.string.colorBlue))), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tvSinger.setText(style);
                } else {
                    tvSinger.setText(artistName);
                }
                tvDescription.setText(TimeUtil.getTimeStandardOnlyYMD(bean.getCreateTime()));
            } else if (type == 2) {
                tvName.setText(bean.getAlbumName());
                tvDescription.setText(TimeUtil.getTimeStandardOnlyYMD(bean.getCreateTime()) + " 歌曲 " + bean.getSongCount());
            }
        }

        public void setListener(OnAlbumClickListener listener, int i) {
            rlAlbum.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAlbumClick(i);
                }
            });
        }
    }

    public interface OnAlbumClickListener {
        void onAlbumClick(int position);

    }

    public void setListener(OnAlbumClickListener listener) {
        this.listener = listener;
    }
}
