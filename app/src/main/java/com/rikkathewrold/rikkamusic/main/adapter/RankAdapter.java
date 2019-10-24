package com.rikkathewrold.rikkamusic.main.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseAdapter;
import com.rikkathewrold.rikkamusic.main.bean.TopListBean;
import com.rikkathewrold.rikkamusic.widget.RikkaRoundRectView;

import java.util.ArrayList;
import java.util.List;


public class RankAdapter extends BaseAdapter<RecyclerView.ViewHolder, TopListBean.ListBean> {
    private static final String TAG = "RankAdapter";

    private Context mContext;
    private List<TopListBean.ListBean> list = new ArrayList<>();
    private OnTopListClickListener listener;

    public RankAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void notifyDataSetChanged(List<TopListBean.ListBean> dataList) {
        list = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(getInflater().inflate(R.layout.item_toplist, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof RankAdapter.ViewHolder) {
            ViewHolder vh = (ViewHolder) viewHolder;
            TopListBean.ListBean bean = list.get(i);
            vh.setBean(bean);
            vh.setOnClickListener(listener, i);
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RikkaRoundRectView ivToplistCover;
        RelativeLayout rlToplist;
        TextView tvToplistName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivToplistCover = itemView.findViewById(R.id.iv_toplist);
            rlToplist = itemView.findViewById(R.id.rl_toplist);
            tvToplistName = itemView.findViewById(R.id.tv_toplist_name);
        }

        void setBean(TopListBean.ListBean bean) {
            Glide.with(mContext).load(bean.getCoverImgUrl()).into(ivToplistCover);
            tvToplistName.setText(bean.getName());
        }

        void setOnClickListener(OnTopListClickListener listener, int position) {
            rlToplist.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onClickTopList(position);
                }
            });
        }
    }

    public interface OnTopListClickListener {
        void onClickTopList(int position);
    }

    public void setListener(OnTopListClickListener listener) {
        this.listener = listener;
    }
}
