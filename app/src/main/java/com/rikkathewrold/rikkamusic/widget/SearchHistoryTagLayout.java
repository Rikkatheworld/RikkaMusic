package com.rikkathewrold.rikkamusic.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.search.bean.SearchHistoryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索历史的TagLayout
 */
public class SearchHistoryTagLayout extends HorizontalScrollView {
    private static final String TAG = "SearchHistoryTagLayout";

    private List<SearchHistoryBean> stringList = new ArrayList<>();
    private List<TextView> textViewList = new ArrayList<>();
    private OnHistoryTagClickListener listener;
    private Context mContext;
    private RelativeLayout rlHsv;

    public SearchHistoryTagLayout(Context context) {
        this(context, null);
    }

    public SearchHistoryTagLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchHistoryTagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView(context);
    }

    private void initView(Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_searchhistory_tag, this, true);
        rlHsv = v.findViewById(R.id.rl_hsv);
    }

    public interface OnHistoryTagClickListener {
        void onItemClick(int position);
    }


    /**
     * 貌似第一个数据有问题，所以第一个数据不显示
     *
     * @param list
     */
    public void addHistoryText(List<SearchHistoryBean> list, OnHistoryTagClickListener listener) {
        this.listener = listener;
        stringList.clear();
        rlHsv.removeAllViews();
        stringList.add(new SearchHistoryBean(""));
        textViewList.clear();
        stringList.addAll(list);
        for (int i = 0; i < stringList.size(); i++) {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            TextView tvHistory = new TextView(mContext);
            tvHistory.setId(i);
            if (i == 0) {
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                tvHistory.setVisibility(GONE);
                tvHistory.setText(stringList.get(0).getKeyowrds());
            } else {
                lp.addRule(RelativeLayout.RIGHT_OF, rlHsv.getChildAt(i - 1).getId());
                tvHistory.setText(stringList.get(stringList.size() - i).getKeyowrds());
            }
            tvHistory.setTextAppearance(R.style.SearchHistoryTextStyle);
            tvHistory.setBackgroundResource(R.drawable.shape_tag);
            tvHistory.setPadding(40, 10, 40, 10);
            if (i != 1) {
                lp.leftMargin = 25;
            }
            tvHistory.setLayoutParams(lp);//设置布局参数
            rlHsv.addView(tvHistory);
            if (i != 0) {
                textViewList.add(tvHistory);
            }
        }
        initListener();
    }

    private void initListener() {
        for (int i = 0; i < textViewList.size(); i++) {
            int finalI = i;
            textViewList.get(i).setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(textViewList.size() - finalI - 1);
                }
            });
        }
    }
}
