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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseAdapter;
import com.rikkathewrold.rikkamusic.search.bean.PlayListSearchBean;
import com.rikkathewrold.rikkamusic.widget.RikkaRoundRectView;

import java.util.List;

public class PlayListSearchAdapter extends BaseAdapter<RecyclerView.ViewHolder, PlayListSearchBean.ResultBean.PlaylistsBean> {

    private Context mContext;
    private List<PlayListSearchBean.ResultBean.PlaylistsBean> list;
    private String keywords;
    private OnPlayListClickListener listener;

    public PlayListSearchAdapter(Context context) {
        super(context);
        mContext = context;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    @Override
    public void notifyDataSetChanged(List<PlayListSearchBean.ResultBean.PlaylistsBean> dataList) {
        list = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(getInflater().inflate(R.layout.item_playlist_fragment, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolder vh = (ViewHolder) viewHolder;
        if (vh != null) {
            vh.setBean(mContext, list.get(i), keywords);
            vh.setListener(listener, i);
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RikkaRoundRectView ivCover;
        private TextView tvName, tvPlayListInfo;
        private LinearLayout llItem;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.iv_cover);
            tvName = itemView.findViewById(R.id.tv_playlist_name);
            tvPlayListInfo = itemView.findViewById(R.id.tv_playlist_info);
            llItem = itemView.findViewById(R.id.ll_item);
        }

        @SuppressLint("SetTextI18n")
        void setBean(Context context, PlayListSearchBean.ResultBean.PlaylistsBean bean, String keywords) {
            Glide.with(context).load(bean.getCoverImgUrl()).into(ivCover);

            if (bean.getName().contains(keywords)) {
                int start = bean.getName().indexOf(keywords);
                int end = start + keywords.length();
                String resString = bean.getName();
                SpannableStringBuilder style = new SpannableStringBuilder(resString);
                style.setSpan(new ForegroundColorSpan(Color.parseColor(context.getString(R.string.colorBlue))), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvName.setText(style);
            } else {
                tvName.setText(bean.getName());
            }

            int playcount = bean.getPlayCount();
            String count;
            if (playcount >= 10000) {
                playcount = playcount / 10000;
                count = playcount + "万次";
            } else {
                count = playcount + "次";
            }
            tvPlayListInfo.setText(bean.getTrackCount() + "首，by " + bean.getCreator().getNickname() + "，播放" + count);
        }

        public void setListener(OnPlayListClickListener listener, int i) {
            llItem.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onPlayListClick(i);
                }
            });
        }
    }

    public interface OnPlayListClickListener {
        void onPlayListClick(int position);
    }

    public void setListener(OnPlayListClickListener listener) {
        this.listener = listener;
    }
}
