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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseAdapter;
import com.rikkathewrold.rikkamusic.search.bean.UserSearchBean;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

//搜索出用户的Adapter
public class UserSearchAdapter extends BaseAdapter<RecyclerView.ViewHolder, UserSearchBean.ResultBean.UserprofilesBean> {

    private Context mContext;
    private String keywords;
    private List<UserSearchBean.ResultBean.UserprofilesBean> list;
    private OnUserClickListener listener;

    public UserSearchAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void notifyDataSetChanged(List<UserSearchBean.ResultBean.UserprofilesBean> dataList) {
        list = dataList;
        notifyDataSetChanged();
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(getInflater().inflate(R.layout.item_user_search, viewGroup, false));
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
        private RelativeLayout rlUser;
        private TextView tvName, tvSgin;
        private ImageView ivGender;
        private TextView tvFollow, tvAlreadyFollow;
        private CircleImageView ivAvatar;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            rlUser = itemView.findViewById(R.id.rl_user);
            tvName = itemView.findViewById(R.id.tv_name);
            ivGender = itemView.findViewById(R.id.iv_gender);
            tvAlreadyFollow = itemView.findViewById(R.id.tv_already_follow);
            tvFollow = itemView.findViewById(R.id.tv_follow);
            tvSgin = itemView.findViewById(R.id.tv_description);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
        }

        void setBean(Context context, UserSearchBean.ResultBean.UserprofilesBean bean, String keywords) {
            Glide.with(context).load(bean.getAvatarUrl()).into(ivAvatar);
            if (!TextUtils.isEmpty(bean.getSignature())) {
                tvSgin.setText(bean.getSignature());
            }
            if (!bean.isFollowed()) {
                tvFollow.setVisibility(View.VISIBLE);
                tvAlreadyFollow.setVisibility(View.GONE);
            } else {
                tvFollow.setVisibility(View.GONE);
                tvAlreadyFollow.setVisibility(View.VISIBLE);
            }

            if (bean.getGender() == 0) {
                ivGender.setBackgroundResource(R.drawable.shape_female);
                ivGender.setVisibility(View.VISIBLE);
            } else if (bean.getGender() == 1) {
                ivGender.setBackgroundResource(R.drawable.shape_male);
                ivGender.setVisibility(View.VISIBLE);
            }

            if (bean.getNickname().contains(keywords)) {
                int start = bean.getNickname().indexOf(keywords);
                int end = start + keywords.length();
                SpannableStringBuilder style = new SpannableStringBuilder(bean.getNickname());
                style.setSpan(new ForegroundColorSpan(Color.parseColor(context.getString(R.string.colorBlue))), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvName.setText(style);
            } else {
                tvName.setText(bean.getNickname());
            }
        }

        public void setListener(OnUserClickListener listener, int i) {
            rlUser.setOnClickListener(v -> {
                listener.OnUserClick(i, OnUserClickListener.USER_CHECK);
            });

            tvFollow.setOnClickListener(v -> {
                listener.OnUserClick(i, OnUserClickListener.USER_FOLLOW);
            });
        }
    }


    //type = 1表示点击查看，type = 2，表示关注
    public interface OnUserClickListener {
        int USER_CHECK = 1;
        int USER_FOLLOW = 2;

        void OnUserClick(int position, int type);
    }

    public void setListener(OnUserClickListener listener) {
        this.listener = listener;
    }
}
