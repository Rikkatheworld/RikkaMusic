package com.rikkathewrold.rikkamusic.search.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseAdapter;
import com.rikkathewrold.rikkamusic.search.bean.SingerSearchBean;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SingerSearchAdapter extends BaseAdapter<RecyclerView.ViewHolder, SingerSearchBean.ResultBean.ArtistsBean> {

    private Context mContext;
    private String keywords;
    private List<SingerSearchBean.ResultBean.ArtistsBean> list;
    private OnSingerClickListener listener;

    public SingerSearchAdapter(Context context) {
        super(context);
        mContext = context;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public void setListener(OnSingerClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void notifyDataSetChanged(List<SingerSearchBean.ResultBean.ArtistsBean> dataList) {
        list = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(getInflater().inflate(R.layout.item_singer_search, viewGroup, false));
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

    class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView ivCover;
        private TextView tvName;
        private RelativeLayout rlSinger;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.iv_singeravatar);
            tvName = itemView.findViewById(R.id.tv_singername);
            rlSinger = itemView.findViewById(R.id.rl_singer);
        }

        void setBean(Context context, SingerSearchBean.ResultBean.ArtistsBean bean, String keywords) {
            Glide.with(context).load(bean.getPicUrl()).into(ivCover);
            String name = bean.getName();
            if (!TextUtils.isEmpty(bean.getTrans())) {
                name += "(" + bean.getTrans() + ")";
            }
            if (name.contains(keywords)) {
                int start = name.indexOf(keywords);
                int end = start + keywords.length();
                SpannableStringBuilder style = new SpannableStringBuilder(name);
                style.setSpan(new ForegroundColorSpan(Color.parseColor(context.getString(R.string.colorBlue))), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvName.setText(style);
            } else {
                tvName.setText(name);
            }
        }

        public void setListener(OnSingerClickListener listener, int i) {
            rlSinger.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onSingerClick(i);
                }
            });
        }
    }

    public interface OnSingerClickListener {
        void onSingerClick(int position);
    }
}

