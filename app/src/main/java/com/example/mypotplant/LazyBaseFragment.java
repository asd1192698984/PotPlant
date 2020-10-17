package com.example.mypotplant;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by MXL on 2020/1/20
 * ViewPager + Fragment 结构中
 * ViewPager 有预加载功能，在访问网络的时候会同时加载多个页面的网络，体验很不好，
 *  更会影响一些带有页面进度条的显示
 * 所以ViewPager中的Fragment 都继承这个类。 效果是只预加载布局，但是不会访问网络。
 * @version 1.0
 * @since 1.0
 */
public abstract class LazyBaseFragment extends Fragment  {
    public View view;

    /**
     * Fragment当前状态是否可见
     */
 //   protected boolean isVisible;
    boolean mIsPrepare = false;		//视图还没准备好
    boolean mIsVisible= false;		//不可见
    boolean mIsFirstLoad = true;	//第一次加载

    /**
     * 初始化view对象，这里在Fragment中的onCreateView方法中进行实现，返回一个View对象
     */
    public abstract View initView();


    /**
     * 初始化数据
     */
    public abstract void initData();


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mIsPrepare = true;
        loadData();
    }
    //先于oncreatview执行的方法
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            mIsVisible = true;
            onVisible();
        } else {
            mIsVisible =false;
            onInvisible();
        }
    }

    /**
     * 可见
     */
    protected void onVisible() {
        loadData();
    }

    /**
     * 不可见
     */
    protected void onInvisible() {

    }

    /**
     * 延迟加载
     */
    protected abstract void lazyLoad();

    private void loadData() {
        //这里进行三个条件的判断，如果有一个不满足，都将不进行加载
        if (!mIsPrepare || !mIsVisible||!mIsFirstLoad) {
            return;
        }
        lazyLoad();
        //数据加载完毕,恢复标记,防止重复加载
       // mIsFirstLoad = false;
    }

}