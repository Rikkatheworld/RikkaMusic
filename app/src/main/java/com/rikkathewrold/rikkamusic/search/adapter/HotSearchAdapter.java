package com.rikkathewrold.rikkamusic.search.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseAdapter;
import com.rikkathewrold.rikkamusic.search.bean.HotSearchDetailBean;

import java.util.List;

public class HotSearchAdapter extends BaseAdapter<RecyclerView.ViewHolder, HotSearchDetailBean> {

    private HotSearchDetailBean list;
    private OnHotSearchAdapterClickListener listener;

    public HotSearchAdapter(Context context) {
        super(context);
    }

    @Override
    public void notifyDataSetChanged(List<HotSearchDetailBean> dataList) {
        list = dataList.get(0);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(getInflater().inflate(R.layout.item_hot_search, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ViewHolder) {
            ViewHolder vh = (ViewHolder) viewHolder;
            vh.setBean(list, i);
            vh.setListener(listener, i);
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.getData().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvNumber;
        private TextView tvCount;
        private TextView tvDescription;
        private RelativeLayout rlHotSearch;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvNumber = itemView.findViewById(R.id.tv_number);
            tvCount = itemView.findViewById(R.id.tv_count);
            tvDescription = itemView.findViewById(R.id.tv_description);
            rlHotSearch = itemView.findViewById(R.id.rl_hot_search);
        }

        @SuppressLint("SetTextI18n")
        void setBean(HotSearchDetailBean list, int position) {
            tvName.setText(list.getData().get(position).getSearchWord());
            tvNumber.setText(position + 1 + "");
            tvCount.setText(list.getData().get(position).getScore() + "");
            tvDescription.setText(list.getData().get(position).getContent());
//            if(position <=2){
//                tvNumber.setTextColor(Color.parseColor(App.getContext().getString(R.string.colorPrimary)));
//            }else {
//                tvNumber.setTextColor(Color.parseColor("#aa333333"));
//            }
        }

        public void setListener(OnHotSearchAdapterClickListener listener, int i) {
            rlHotSearch.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onHotSearchClick(i);
                }
            });
        }
    }

    public interface OnHotSearchAdapterClickListener {
        void onHotSearchClick(int position);
    }

    public void setListener(OnHotSearchAdapterClickListener listener) {
        this.listener = listener;
    }
}
