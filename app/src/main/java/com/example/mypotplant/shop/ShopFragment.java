package com.example.mypotplant.shop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mypotplant.LazyBaseFragment;
import com.example.mypotplant.R;
import com.example.mypotplant.shop.fragments.ShopDefaultFragment;
import com.example.mypotplant.shop.fragments.ShopHomeFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by MXL on 2020/5/16
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class ShopFragment extends LazyBaseFragment {
    View mView;
    ViewPager viewPager;
    TabLayout tabLayout;
    Fragment[] fragments;
    List<String> titles;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_shop,container,false);
        return mView;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initView();
    }
    private  void initViewpager(){
        viewPager=getView().findViewById(R.id.viewpager);
        tabLayout=getView().findViewById(R.id.tablayout);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return titles.get(position);
            }
        });
         tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public View initView() {
        initViewpager();
        return null;
    }

    @Override
    public void initData() {
        titles=new ArrayList<>();
        fragments=new Fragment[6];
        titles.add("首页");
        titles.add("花种");
        titles.add("花卉");
        titles.add("花科");
        titles.add("花器");
        titles.add("创意");
        fragments[0]=new ShopHomeFragment();
        for(int i=1;i<6;i++)
            fragments[i]= ShopDefaultFragment.newInstance(titles.get(i));
    }

    @Override
    protected void lazyLoad() {

    }
}
