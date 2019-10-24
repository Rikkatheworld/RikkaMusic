package com.rikkathewrold.rikkamusic.search.adapter;

import android.annotation.SuppressLint;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseAdapter;
import com.rikkathewrold.rikkamusic.search.bean.MvBean;
import com.rikkathewrold.rikkamusic.util.LogUtil;
import com.rikkathewrold.rikkamusic.util.TimeUtil;
import com.rikkathewrold.rikkamusic.video.mvp.view.VideoActivity;
import com.rikkathewrold.rikkamusic.widget.RikkaRoundRectView;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 视频搜索结果的Adapter
 */
public class FeedAdapter extends BaseAdapter<RecyclerView.ViewHolder, MvBean> {
    private static final String TAG = "FeedAdapter";

    private Context mContext;
    private List<MvBean> list;
    private String keywords;
    private int type;

    public FeedAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void notifyDataSetChanged(List<MvBean> dataList) {
        this.list = dataList;
        notifyDataSetChanged();
        LogUtil.d(TAG, "notifyDataSetChanged" + dataList);
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    //type==1，显示蓝色字体
    //type==2，不显示
    public void setType(int type) {
        this.type = type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(getInflater().inflate(R.layout.item_feed_search, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolder vh = (ViewHolder) viewHolder;
        if (vh != null) {
            vh.setBean(mContext, list.get(i), keywords, type);
            vh.setListener(mContext, i);
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private RikkaRoundRectView ivCover;
        private TextView tvName;
        private TextView tvCreator, tvMv;
        private RelativeLayout rlFeed;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.iv_cover);
            tvName = itemView.findViewById(R.id.tv_name);
            tvCreator = itemView.findViewById(R.id.tv_creator);
            rlFeed = itemView.findViewById(R.id.rl_feed);
            tvMv = itemView.findViewById(R.id.tv_mv);
        }

        @SuppressLint("SetTextI18n")
        public void setBean(Context context, MvBean videosBean, String keywords, int type) {
            if (!judgeContainsStr(videosBean.getVid())) {
                //不包含字母，则说明是MV
                tvMv.setVisibility(View.VISIBLE);
            } else {
                tvMv.setVisibility(View.GONE);
            }
            if (type == 1) {
                if (videosBean.getTitle().contains(keywords)) {
                    int start = videosBean.getTitle().indexOf(keywords);
                    int end = start + keywords.length();
                    String resString = videosBean.getTitle();
                    SpannableStringBuilder style = new SpannableStringBuilder(resString);
                    style.setSpan(new ForegroundColorSpan(Color.parseColor(context.getString(R.string.colorBlue))), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tvName.setText(style);
                } else {
                    tvName.setText(videosBean.getTitle());
                }
                if (videosBean.getCreator().get(0).getUserName().contains(keywords)) {
                    int start = videosBean.getCreator().get(0).getUserName().indexOf(keywords);
                    int end = start + keywords.length();
                    String resString = videosBean.getCreator().get(0).getUserName();
                    SpannableStringBuilder style = new SpannableStringBuilder(resString);
                    style.setSpan(new ForegroundColorSpan(Color.parseColor(context.getString(R.string.colorBlue))), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tvCreator.setText(TimeUtil.getTimeNoYMDH(videosBean.getDuration()) + "  by " + style);
                } else {
                    tvCreator.setText(TimeUtil.getTimeNoYMDH(videosBean.getDuration()) + "  by " + videosBean.getCreator().get(0).getUserName());
                }
            } else if (type == 2) {
                tvName.setText(videosBean.getTitle());
                tvCreator.setText(TimeUtil.getTimeNoYMDH(videosBean.getDuration()));
            }
            Glide.with(context).load(videosBean.getCoverUrl()).into(ivCover);
        }

        public void setListener(Context context, int position) {
            rlFeed.setOnClickListener(v -> {
                Intent intent = new Intent(context, VideoActivity.class);
                context.startActivity(intent);
            });
        }

        /**
         * 该方法主要使用正则表达式来判断字符串中是否包含字母
         */
        public boolean judgeContainsStr(String cardNum) {
            String regex = ".*[a-zA-Z]+.*";
            Matcher m = Pattern.compile(regex).matcher(cardNum);
            return m.matches();
        }
    }

}
