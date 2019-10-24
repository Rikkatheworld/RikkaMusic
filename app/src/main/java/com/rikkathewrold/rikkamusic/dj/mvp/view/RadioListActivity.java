package com.rikkathewrold.rikkamusic.dj.mvp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gyf.immersionbar.ImmersionBar;
import com.hjq.toast.ToastUtils;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseActivity;
import com.rikkathewrold.rikkamusic.dj.adapter.RadioListAdapter;
import com.rikkathewrold.rikkamusic.dj.bean.DjBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjCategoryRecommendBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjCatelistBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjDetailBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjPaygiftBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjProgramBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjRecommendBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjRecommendTypeBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjSubBean;
import com.rikkathewrold.rikkamusic.dj.mvp.contract.DjContract;
import com.rikkathewrold.rikkamusic.dj.mvp.presenter.DjPresenter;
import com.rikkathewrold.rikkamusic.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 电台列表界面
 */
public class RadioListActivity extends BaseActivity<DjContract.Presenter> implements DjContract.View {
    private static final String TAG = "RadioListActivity";

    public static final String TITLE_NAME = "titleName";
    public static final String TYPE = "type";

    @BindView(R.id.rv_dj_list)
    RecyclerView rvDj;

    private RadioListAdapter adapter;

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_radio_list);

        ImmersionBar.with(this)
                .transparentBar()
                .statusBarColor(R.color.colorPrimary)
                .statusBarDarkFont(false)
                .init();
    }

    @Override
    protected DjContract.Presenter onCreatePresenter() {
        return new DjPresenter(this);
    }

    @Override
    protected void initModule() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            String titleName = intent.getStringExtra(TITLE_NAME);
            setLeftTitleText(titleName, getString(R.string.colorWhite));
            setBackBtn(getString(R.string.colorWhite));

            rvDj.setLayoutManager(new LinearLayoutManager(this));
            adapter = new RadioListAdapter(this);
            rvDj.setAdapter(adapter);

            int type = intent.getIntExtra(TYPE, 0);
            if (type != 0) {
                showDialog();
                mPresenter.getDjRecommendType(type);
            } else {
                showDialog();
                mPresenter.getDjPaygift(30, 1);
            }
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onGetDjRecommendSuccess(DjRecommendBean bean) {

    }

    @Override
    public void onGetDjRecommendFail(String e) {

    }

    @Override
    public void onGetDjPaygiftSuccess(DjPaygiftBean bean) {
        hideDialog();
        LogUtil.d(TAG, "onGetDjPaygiftSuccess :" + bean);
        LogUtil.d(TAG, "onGetDjRecommendTypeSuccess :" + bean);
        List<DjBean> djList = new ArrayList<>();
        List<DjPaygiftBean.DataBean.ListBean> djBeans = bean.getData().getList();
        for (int i = 0; i < djBeans.size(); i++) {
            DjBean djBean = new DjBean();
            djBean.setDjName(djBeans.get(i).getName());
            djBean.setRid(djBeans.get(i).getId());
            djBean.setRcmdName(djBeans.get(i).getRcmdText());
            djBean.setCoverUrl(djBeans.get(i).getPicUrl());
            djBean.setArtistName("");
            djBean.setPrice(djBeans.get(i).getOriginalPrice() / 100);
            djBean.setProgramCount(djBeans.get(i).getProgramCount());
            djBean.setRegisterCount(-1);
            djBean.setSubed(false);
            djList.add(djBean);
        }
        adapter.notifyDataSetChanged(djList);
    }

    @Override
    public void onGetDjPaygiftFail(String e) {
        hideDialog();
        LogUtil.e(TAG, "onGetDjPaygiftFail : " + e);
        ToastUtils.show(e);
    }

    @Override
    public void onGetDjRecommendTypeSuccess(DjRecommendTypeBean bean) {
        hideDialog();
        LogUtil.d(TAG, "onGetDjRecommendTypeSuccess :" + bean);
        List<DjBean> djList = new ArrayList<>();
        for (int i = 0; i < bean.getDjRadios().size(); i++) {
            DjBean djBean = new DjBean();
            djBean.setDjName(bean.getDjRadios().get(i).getName());
            djBean.setRid(bean.getDjRadios().get(i).getId());
            djBean.setRcmdName(bean.getDjRadios().get(i).getRcmdtext());
            djBean.setCoverUrl(bean.getDjRadios().get(i).getPicUrl());
            djBean.setArtistName(bean.getDjRadios().get(i).getDj().getNickname());
            djBean.setPrice(bean.getDjRadios().get(i).getOrginalPrice());
            djBean.setProgramCount(bean.getDjRadios().get(i).getProgramCount());
            djBean.setRegisterCount(bean.getDjRadios().get(i).getSubCount() / 100);
            djBean.setSubed(bean.getDjRadios().get(i).isSubed());
            djList.add(djBean);
        }
        adapter.notifyDataSetChanged(djList);
    }

    @Override
    public void onGetDjRecommendTypeFail(String e) {
        hideDialog();
        LogUtil.e(TAG, "onGetDjRecommendTypeFail : " + e);
        ToastUtils.show(e);
    }

    @Override
    public void onGetDjCategoryRecommendSuccess(DjCategoryRecommendBean bean) {
    }

    @Override
    public void onGetDjCategoryRecommendFail(String e) {

    }

    @Override
    public void onGetDjCatelistSuccess(DjCatelistBean bean) {

    }

    @Override
    public void onGetDjCatelistFail(String e) {

    }

    @Override
    public void onSubDjSuccess(DjSubBean bean) {

    }

    @Override
    public void onSubDjFail(String e) {

    }

    @Override
    public void onGetDjProgramSuccess(DjProgramBean bean) {

    }

    @Override
    public void onGetDjProgramFail(String e) {

    }

    @Override
    public void onGetDjDetailSuccess(DjDetailBean bean) {

    }

    @Override
    public void onGetDjDetailFail(String e) {

    }
}
