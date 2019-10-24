package com.rikkathewrold.rikkamusic.search.adapter;

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
import com.rikkathewrold.rikkamusic.search.bean.RadioSearchBean;
import com.rikkathewrold.rikkamusic.widget.RikkaRoundRectView;

import java.util.List;

public class RadioSearchAdapter extends BaseAdapter<RecyclerView.ViewHolder, RadioSearchBean.ResultBean.DjRadiosBean> {

    private Context mContext;
    private List<RadioSearchBean.ResultBean.DjRadiosBean> list;
    private OnRadioClickListener listener;
    private String keywords;

    public RadioSearchAdapter(Context context) {
        super(context);
        mContext = context;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    @Override
    public void notifyDataSetChanged(List<RadioSearchBean.ResultBean.DjRadiosBean> dataList) {
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


        void setBean(Context context, RadioSearchBean.ResultBean.DjRadiosBean bean, String keywords) {
            Glide.with(context).load(bean.getPicUrl()).into(ivCover);

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

            if (bean.getDj().getNickname().contains(keywords)) {
                int start = bean.getDj().getNickname().indexOf(keywords);
                int end = start + keywords.length();
                String resString = bean.getDj().getNickname();
                SpannableStringBuilder style = new SpannableStringBuilder(resString);
                style.setSpan(new ForegroundColorSpan(Color.parseColor(context.getString(R.string.colorBlue))), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvPlayListInfo.setText(style);
            } else {
                tvPlayListInfo.setText(bean.getDj().getNickname());
            }
        }
    }

    public interface OnRadioClickListener {
        void onRadioClick(int position);
    }

    public void setListener(OnRadioClickListener listener) {
        this.listener = listener;
    }
}
