package com.example.mypotplant.homepager.flowerRank;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.mypotplant.BaseActivity;
import com.example.mypotplant.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 *  话题界面
 */
public class RankActivity extends BaseActivity {

    public  final static  String TAG="RankActivity";
    public  static  void actionStart(Context context){
        Intent intent=new Intent(context,RankActivity.class);
        context.startActivity(intent);
    }
    TabLayout tabLayout;
    ViewPager viewPager;
    ArrayList<Fragment> fragments;
    String[] titles =new String[]{"周榜","花草热度榜","我的"};
    int[] icons=new int[]{R.mipmap.rank_1,R.mipmap.rank_2,R.mipmap.rank_3};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        init();
    }
    private  void init(){
        tabLayout=findViewById(R.id.tablayout);
        viewPager=findViewById(R.id.viewpager);
        fragments=new ArrayList<Fragment>();
        fragments.add(RankFragment.newInstance());
        fragments.add(RankFragment.newInstance());
        fragments.add(RankFragment.newInstance());

        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
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
                return titles[position];
            }
        });
        viewPager.setOffscreenPageLimit(3);
        for(int i=0;i<titles.length;i++){
            tabLayout.addTab(tabLayout.newTab());
        }
        tabLayout.setupWithViewPager(viewPager);
        for(int i=0;i<titles.length;i++){
//            fragments.add(BlankFragment.newInstance(titles[i]));
            tabLayout.getTabAt(i).setText(titles[i]).setIcon(icons[i]);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
