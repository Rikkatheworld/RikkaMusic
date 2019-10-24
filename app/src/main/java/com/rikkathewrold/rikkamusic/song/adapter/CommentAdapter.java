package com.rikkathewrold.rikkamusic.song.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseAdapter;
import com.rikkathewrold.rikkamusic.song.bean.MusicCommentBean;
import com.rikkathewrold.rikkamusic.util.LogUtil;
import com.rikkathewrold.rikkamusic.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 评论统一的Adapter
 */
public class CommentAdapter extends BaseAdapter<RecyclerView.ViewHolder, MusicCommentBean.CommentsBean> {
    private static final String TAG = "CommentAdapter";

    private List<MusicCommentBean.CommentsBean> list = new ArrayList<>();
    private Context mContext;
    private OnLikeCommentListener listener;

    public CommentAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void notifyDataSetChanged(List<MusicCommentBean.CommentsBean> dataList) {
        list = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(getInflater().inflate(R.layout.item_comment, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder != null) {
            ViewHolder vh = (ViewHolder) viewHolder;
            MusicCommentBean.CommentsBean bean = list.get(i);
            vh.setBean(bean, i);
            vh.setListener(listener, i);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvContent, tvPublishTime;
        CircleImageView ivAvatar;
        TextView tvLikeCount;
        ImageView ivLike;
        RelativeLayout rlLike, rlGap;
        TextView tvGap;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tv_username);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvPublishTime = itemView.findViewById(R.id.tv_publish_time);
            tvLikeCount = itemView.findViewById(R.id.tv_like_count);
            ivLike = itemView.findViewById(R.id.iv_like);
            rlLike = itemView.findViewById(R.id.rl_like);
            rlGap = itemView.findViewById(R.id.rl_gap);
            tvGap = itemView.findViewById(R.id.tv_gap);
        }

        public void setBean(MusicCommentBean.CommentsBean bean, int position) {
            Glide.with(mContext).load(bean.getUser().getAvatarUrl()).transition(new DrawableTransitionOptions().crossFade()).into(ivAvatar);
            tvUserName.setText(bean.getUser().getNickname());

            tvPublishTime.setText(TimeUtil.getTimeStandard(bean.getTime()));
            if (bean.isLiked()) {
                ivLike.setImageResource(R.drawable.shape_comment_like);
            } else {
                ivLike.setImageResource(R.drawable.shape_comment_unlike);
            }
            float likeCount = bean.getLikedCount();
            boolean moreThanWan = false;
            if (likeCount > 10000) {
                likeCount = likeCount / 10000f;
                moreThanWan = true;
            }
            String res;
            if (moreThanWan) {
                LogUtil.w(TAG, "bean : " + bean + " likeCount" + likeCount);
                res = String.format("%.1f", likeCount) + "万";
            } else {
                int likeCountInt = (int) likeCount;
                res = String.valueOf(likeCountInt);
            }
            tvLikeCount.setText(res);
            if (!TextUtils.isEmpty(bean.getContent())) {
                tvContent.setVisibility(View.VISIBLE);
                tvContent.setText(bean.getContent());
            }
            if (position > 0) {
                rlGap.setVisibility(View.VISIBLE);
            }
            if (position == list.size() - 1) {
                tvGap.setVisibility(View.GONE);
            }
        }

        public void setListener(OnLikeCommentListener listener, int position) {
            rlLike.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onLikeClick(position);
                }
            });
        }
    }

    public interface OnLikeCommentListener {
        void onLikeClick(int position);
    }

    public void setListener(OnLikeCommentListener listener) {
        this.listener = listener;
    }
}
