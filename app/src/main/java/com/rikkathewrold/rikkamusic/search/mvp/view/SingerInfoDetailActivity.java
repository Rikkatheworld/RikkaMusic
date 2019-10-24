package com.rikkathewrold.rikkamusic.search.mvp.view;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseActivity;
import com.rikkathewrold.rikkamusic.search.bean.FeedSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.SimiSingerBean;
import com.rikkathewrold.rikkamusic.search.bean.SingerAblumSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.SingerDescriptionBean;
import com.rikkathewrold.rikkamusic.search.bean.SingerSongSearchBean;
import com.rikkathewrold.rikkamusic.search.mvp.contract.SingerContract;
import com.rikkathewrold.rikkamusic.search.mvp.presenter.SingerPresenter;
import com.rikkathewrold.rikkamusic.util.GsonUtil;
import com.rikkathewrold.rikkamusic.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 歌手详细计划界面
 */
public class SingerInfoDetailActivity extends BaseActivity<SingerPresenter> implements SingerContract.View {
    private static final String TAG = "SingerInfoDetailActivity";

    public static final String INFO_DETAIL = "infoDetail";

    @BindView(R.id.rl_desc)
    LinearLayout rlDesc;

    private List<SingerDescriptionBean.IntroductionBean> simiList = new ArrayList<>();
    private SingerDescriptionBean descBean;

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_singer_info_detail);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ScreenUtils.setStatusBarDarkFont(this, true);
    }

    @Override
    protected SingerPresenter onCreatePresenter() {
        return new SingerPresenter(this);
    }

    @Override
    protected void initModule() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        setBackBtn(getString(R.string.colorWhite));
        setLeftTitleText(R.string.singer_desc);
        setLeftTitleTextColorWhite();

        if (getIntent() != null) {
            descBean = GsonUtil.fromJSON(getIntent().getStringExtra(INFO_DETAIL), SingerDescriptionBean.class);
            simiList.addAll(descBean.getIntroduction());

            addDescToLayout(simiList);
        }
    }

    private void addDescToLayout(List<SingerDescriptionBean.IntroductionBean> simiList) {
        for (int i = 0; i < simiList.size(); i++) {
            TextView tvTitle = new TextView(this);
            tvTitle.setText(simiList.get(i).getTi());
            tvTitle.setTextSize(16);
            tvTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            tvTitle.setTextColor(Color.parseColor("#303030"));
            rlDesc.addView(tvTitle);
            if (i > 0) {
                tvTitle.setPadding(0, 30, 0, 10);
            }else {
                tvTitle.setPadding(0, 0, 0, 10);
            }

            TextView tvContent = new TextView(this);
            tvContent.setText(simiList.get(i).getTxt());
            tvContent.setTextSize(12);
            tvContent.setTextColor(Color.parseColor("#A9A9A9"));
            rlDesc.addView(tvContent);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tvContent.getLayoutParams();
            lp.topMargin = 10;
            tvContent.setLayoutParams(lp);
        }
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onGetSingerHotSongSuccess(SingerSongSearchBean bean) {

    }

    @Override
    public void onGetSingerHotSongFail(String e) {

    }

    @Override
    public void onGetSingerAlbumSuccess(SingerAblumSearchBean bean) {

    }

    @Override
    public void onGetSingerAlbumFail(String e) {

    }

    @Override
    public void onGetFeedSearchSuccess(FeedSearchBean bean) {

    }

    @Override
    public void onGetFeedSearchFail(String e) {

    }

    @Override
    public void onGetSingerDescSuccess(SingerDescriptionBean bean) {

    }

    @Override
    public void onGetSingerDescFail(String e) {

    }

    @Override
    public void onGetSimiSingerSuccess(SimiSingerBean bean) {

    }

    @Override
    public void onGetSimiSingerFail(String e) {

    }
}
