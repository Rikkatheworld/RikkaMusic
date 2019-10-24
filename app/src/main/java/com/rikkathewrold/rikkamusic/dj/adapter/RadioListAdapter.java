package com.rikkathewrold.rikkamusic.dj.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.hjq.toast.ToastUtils;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseAdapter;
import com.rikkathewrold.rikkamusic.dj.bean.DjBean;
import com.rikkathewrold.rikkamusic.dj.mvp.view.RadioActivity;
import com.rikkathewrold.rikkamusic.widget.RikkaRoundRectView;

import java.util.ArrayList;
import java.util.List;

import static com.rikkathewrold.rikkamusic.dj.mvp.view.RadioActivity.COVER_URL;
import static com.rikkathewrold.rikkamusic.dj.mvp.view.RadioActivity.IS_SUB;
import static com.rikkathewrold.rikkamusic.dj.mvp.view.RadioActivity.RADIO_NAME;
import static com.rikkathewrold.rikkamusic.dj.mvp.view.RadioActivity.RID;
import static com.rikkathewrold.rikkamusic.dj.mvp.view.RadioActivity.SUB_COUNT;

public class RadioListAdapter extends BaseAdapter<RecyclerView.ViewHolder, DjBean> {
    private static final String TAG = "RadioListAdapter";

    private List<DjBean> list = new ArrayList<>();
    private Context mContext;

    public RadioListAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void notifyDataSetChanged(List<DjBean> dataList) {
        list = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(getInflater().inflate(R.layout.item_dj_list, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder != null) {
            ViewHolder vh = (ViewHolder) viewHolder;
            vh.setBean(list.get(i));
            vh.setListener(i);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rlItem;
        RikkaRoundRectView ivCover;
        TextView tvName, tvRecm, tvInfo, tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rlItem = itemView.findViewById(R.id.rl_item);
            ivCover = itemView.findViewById(R.id.dj_cover);
            tvName = itemView.findViewById(R.id.tv_name);
            tvRecm = itemView.findViewById(R.id.tv_recm);
            tvInfo = itemView.findViewById(R.id.tv_dj_info);
            tvPrice = itemView.findViewById(R.id.tv_price);
        }

        @SuppressLint("SetTextI18n")
        public void setBean(DjBean djBean) {
            Glide.with(mContext)
                    .load(djBean.getCoverUrl())
                    .transition(new DrawableTransitionOptions().crossFade())
                    .into(ivCover);
            tvName.setText(djBean.getDjName());
            if (djBean.getPrice() != 0) {
                tvPrice.setVisibility(View.VISIBLE);
                tvPrice.setText("￥" + djBean.getPrice());
            }
            tvRecm.setText(djBean.getRcmdName());
            if (djBean.getRegisterCount() == -1) {
                tvInfo.setText("最新上架");
            } else {
                tvInfo.setText("节目:" + djBean.getProgramCount() + "  订阅:" + djBean.getRegisterCount());
            }
        }

        public void setListener(int position) {
            if (list.get(position).getPrice() != 0) {
                ToastUtils.show("我没有充钱，所以这里没有做");
            } else {
                rlItem.setOnClickListener(v -> {
                    Intent intent = new Intent(mContext, RadioActivity.class);
                    intent.putExtra(IS_SUB, list.get(position).isSubed());
                    intent.putExtra(SUB_COUNT, list.get(position).getRegisterCount());
                    intent.putExtra(RID, list.get(position).getRid());
                    intent.putExtra(COVER_URL, list.get(position).getCoverUrl());
                    intent.putExtra(RADIO_NAME, list.get(position).getDjName());
                    mContext.startActivity(intent);
                });
            }
        }
    }
}
