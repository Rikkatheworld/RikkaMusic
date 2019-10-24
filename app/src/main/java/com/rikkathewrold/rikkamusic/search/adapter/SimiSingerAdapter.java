package com.rikkathewrold.rikkamusic.search.adapter;

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
import com.rikkathewrold.rikkamusic.search.bean.SimiSingerBean;
import com.rikkathewrold.rikkamusic.util.LogUtil;
import com.rikkathewrold.rikkamusic.widget.RikkaRoundRectView;

import java.util.ArrayList;
import java.util.List;

/**
 * 相似歌手的Adapter
 */
public class SimiSingerAdapter extends BaseAdapter<RecyclerView.ViewHolder, SimiSingerBean.ArtistsBean> {
    private static final String TAG = "SimiSingerAdapter";

    private Context mContext;
    private List<SimiSingerBean.ArtistsBean> list = new ArrayList<>();
    private OnSimiSingerClickListener listener;

    public SimiSingerAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void notifyDataSetChanged(List<SimiSingerBean.ArtistsBean> dataList) {
        list = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(getInflater().inflate(R.layout.item_simi_singer, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolder vh = (ViewHolder) viewHolder;
        if (vh != null) {
            vh.setBean(mContext, list.get(i));
            vh.setListener(listener, i);
        }
    }

    @Override
    public int getItemCount() {
        LogUtil.d(TAG, "size:" + list.size());
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rlSimi;
        private RikkaRoundRectView ivCover;
        private TextView tvName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            rlSimi = itemView.findViewById(R.id.rl_simi);
            ivCover = itemView.findViewById(R.id.iv_cover);
            tvName = itemView.findViewById(R.id.tv_name);
        }

        public void setBean(Context context, SimiSingerBean.ArtistsBean bean) {
            Glide.with(context).load(bean.getPicUrl()).into(ivCover);
            tvName.setText(bean.getName());
        }


        public void setListener(OnSimiSingerClickListener listener, int position) {
            rlSimi.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onSimiClick(position);
                }
            });
        }
    }

    public interface OnSimiSingerClickListener {
        void onSimiClick(int position);
    }

    public void setListener(OnSimiSingerClickListener listener) {
        this.listener = listener;
    }
}
