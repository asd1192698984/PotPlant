package com.example.mypotplant.notification;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mypotplant.BlankFragment;
import com.example.mypotplant.LazyBaseFragment;
import com.example.mypotplant.R;
import com.example.mypotplant.notification.maintainNotification.MaintainNotificationFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by MXL on 2020/3/26
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class NotificationFragment extends LazyBaseFragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    List<Fragment> fragments;
    List<String> titles;
  //  FragmentStatePagerAdapter adapter;
    private  String TAG="NotificationFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: ");
        initData();
        initView();
    }
    @Override
    public View initView() {
        fillViewPager();
        return null;
    }

    @Override
    public void initData() {
        Log.d(TAG, "initData: ");
        fragments=new ArrayList<>();
        fragments.add(MaintainNotificationFragment.newInstance());
        fragments.add(BlankFragment.newInstance(""));
        fragments.add(BlankFragment.newInstance(""));
        titles=new ArrayList<>();
        titles.add("养护");
        titles.add("互动");
        titles.add("私信");
    }

    /*
     *  BUG
     *  NotificationFragment预加载时会调用
     *  不写代码
     */
    @Override
    protected void lazyLoad()
    {
        Log.d(TAG, "lazyLoad: ");
    }
    private  void   fillViewPager(){
        tabLayout=getView().findViewById(R.id.tablay_notification);
        viewPager=getView().findViewById(R.id.viewpager_notifi);
        viewPager.setAdapter(new FragmentStatePagerAdapter(this.getChildFragmentManager()){
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return titles.get(position);
            }
        });
        tabLayout.setupWithViewPager(viewPager);
    }
}
