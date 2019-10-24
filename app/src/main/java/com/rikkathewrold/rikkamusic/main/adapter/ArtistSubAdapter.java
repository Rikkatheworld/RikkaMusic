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
import com.rikkathewrold.rikkamusic.main.bean.ArtistSublistBean;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 收藏歌手列表adapter
 */
public class ArtistSubAdapter extends BaseAdapter<RecyclerView.ViewHolder, ArtistSublistBean.DataBean> {

    private List<ArtistSublistBean.DataBean> list = new ArrayList<>();
    private Context mContext;
    private OnArtistClickListener listener;

    public ArtistSubAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void notifyDataSetChanged(List<ArtistSublistBean.DataBean> dataList) {
        list = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(getInflater().inflate(R.layout.item_artist_sub, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder != null) {
            ViewHolder vh = (ViewHolder) viewHolder;
            vh.setBean(list.get(i));
            vh.setListener(listener, i);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvArtistName, tvMvCount;
        private CircleImageView ivAvatar;
        private RelativeLayout rlArtist;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvArtistName = itemView.findViewById(R.id.tv_artist_name);
            tvMvCount = itemView.findViewById(R.id.tv_mv_album_count);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            rlArtist = itemView.findViewById(R.id.rl_artist);
        }

        public void setBean(ArtistSublistBean.DataBean dataBean) {
            Glide.with(mContext).load(dataBean.getPicUrl()).into(ivAvatar);
            tvArtistName.setText(dataBean.getName());
            String mvalCount = "专辑:" + dataBean.getAlbumSize() + " MV:" + dataBean.getMvSize();
            tvMvCount.setText(mvalCount);
        }

        public void setListener(OnArtistClickListener listener, int position) {
            rlArtist.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onArtistClick(position);
                }
            });
        }
    }

    public interface OnArtistClickListener {
        void onArtistClick(int position);
    }

    public void setListener(OnArtistClickListener listener) {
        this.listener = listener;
    }
}
