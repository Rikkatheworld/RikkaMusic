package com.rikkathewrold.rikkamusic.dj.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseAdapter;
import com.rikkathewrold.rikkamusic.dj.bean.DjCatelistBean;
import com.rikkathewrold.rikkamusic.dj.mvp.view.RadioListActivity;

import java.util.ArrayList;
import java.util.List;

public class DjCateAdapter extends BaseAdapter<RecyclerView.ViewHolder, DjCatelistBean.CategoriesBean> {
    private static final String TAG = "DjCateAdapter";

    private Context mContext;
    private List<DjCatelistBean.CategoriesBean> list = new ArrayList<>();

    public DjCateAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void notifyDataSetChanged(List<DjCatelistBean.CategoriesBean> dataList) {
        list = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(getInflater().inflate(R.layout.item_dj_cate, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder != null) {
            ViewHolder vh = (ViewHolder) viewHolder;
            vh.setBean(i);
            vh.setListener(i);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rlItem;
        private ImageView ivIcon;
        private TextView tvName;
        private TextView tvTop;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rlItem = itemView.findViewById(R.id.rl_item);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvName = itemView.findViewById(R.id.tv_cate_name);
            tvTop = itemView.findViewById(R.id.tv_top);
        }


        public void setBean(int i) {
            DjCatelistBean.CategoriesBean bean = list.get(i);
            Glide.with(mContext)
                    .load(bean.getPic84x84IdUrl())
                    .transition(new DrawableTransitionOptions().crossFade())
                    .into(ivIcon);
            tvName.setText(bean.getName());
            if (i >= 2) {
                tvTop.setVisibility(View.GONE);
            }
        }

        public void setListener(int i) {
            rlItem.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, RadioListActivity.class);
                intent.putExtra(RadioListActivity.TYPE, list.get(i).getId());
                intent.putExtra(RadioListActivity.TITLE_NAME, list.get(i).getName());
                mContext.startActivity(intent);
            });
        }
    }
}
