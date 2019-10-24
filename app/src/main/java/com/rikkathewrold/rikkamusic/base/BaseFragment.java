package com.rikkathewrold.rikkamusic.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rikkathewrold.rikkamusic.widget.LoadingDialog;

import java.util.Objects;


/**
 * 考虑是用懒加载来加载Fragment
 * Created By Rikka on 2019/7/14
 */
public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements View.OnClickListener {
    private static final String TAG = "BaseFragment";

    protected P mPresenter;

    protected LoadingDialog mDialog;

    protected Activity activity;

    public String fragmentTitle;

    private boolean isFragmentVisible;

    //View是否加载完成
    private boolean isPrepared;

    //是否为第一次加载
    private boolean isFirstLoad = true;

    //强制刷新数据 但仍然要 visible & Prepared，采取reset数据的方式，所以要重新走initData
    private boolean forceLoad = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createDialog();
        Bundle bundle = getArguments();
        if (bundle != null && bundle.size() > 0) {
            initVariables(bundle);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPresenter = onCreatePresenter();
        isFirstLoad = true;
        isPrepared = true;
        View view = initView(inflater, container, savedInstanceState);
        lazyLoad();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter = null;
        }
        isPrepared = false;
    }

    protected abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * 要实现延迟加载Fragment内容,需要在 onCreateView
     * isPrepared = true;
     */
    protected void lazyLoad() {
        if (isPrepared() && isFragmentVisible()) {
            if (forceLoad || isFirstLoad()) {
                forceLoad = false;
                isFirstLoad = false;
                initData();
            }
        }
    }

    protected abstract void initData();

    /**
     * ViewPager联合使用
     * isVisibleToUser表示是否显示出来了
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            onVisible();
        } else {
            onInvisible();
        }
    }

    protected void onVisible() {
        isFragmentVisible = true;
        lazyLoad();
    }

    protected void onInvisible() {
        isFragmentVisible = false;
    }

    /**
     * 如果是通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.
     * 若是初始就show的Fragment 为了触发该事件 需要先hide再show
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            onVisible();
        } else {
            onInvisible();
        }
    }

    public abstract P onCreatePresenter();


    /**
     * 被ViewPager移出的Fragment 下次显示时会从getArguments()中重新获取数据
     * 所以若需要刷新被移除Fragment内的数据需要重新put数据 eg:
     * Bundle args = getArguments();
     * if (args != null) {
     * args.putParcelable(KEY, info);
     * }
     */
    protected abstract void initVariables(Bundle bundle);

    private void createDialog() {
        if (mDialog == null) {
            mDialog = new LoadingDialog(Objects.requireNonNull(getContext()), "Loading...");
        }
    }

    /**
     * 忽略isFirstLoad的值，强制刷新数据，但仍要Visible & Prepared
     */
    public void setForceLoad(boolean forceLoad) {
        this.forceLoad = forceLoad;
    }

    public boolean isPrepared() {
        return isPrepared;
    }

    public boolean isFragmentVisible() {
        return isFragmentVisible;
    }

    public boolean isFirstLoad() {
        return isFirstLoad;
    }

    /**
     * 刷新数据
     */
    public void refreshData() {
        if (isFragmentVisible()) {
            initData();
        } else {
            setForceLoad(true);
        }
    }

    public void setFragmentTitle(String title) {
        fragmentTitle = title;
    }

    public String getTitle() {
        return fragmentTitle;
    }

    public void showDialog() {
        if (mDialog != null && !mDialog.isShowing()) {
            mDialog.show();
        }
    }

    public void hideDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
}
