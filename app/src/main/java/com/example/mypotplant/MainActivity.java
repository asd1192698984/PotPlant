package com.example.mypotplant;
 import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.mypotplant.boradcast.NetworkChangeReceiver;
import com.example.mypotplant.homepager.HomepagerFragment;
import com.example.mypotplant.maintenance.MaintenanceFragment;
import com.example.mypotplant.notification.NotificationFragment;
import com.example.mypotplant.person.PersonFragment;
import com.example.mypotplant.person.login.LoginModel;
import com.example.mypotplant.shop.ShopFragment;
import com.example.mypotplant.utils.NetWorkUtils;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends BaseActivity {
    public static final int PERMISSIONS_REQUEST_CODE = 1;
    private ViewPager mViewPager;
    private RadioGroup mTabRadioGroup;
    private List<Fragment> mFragments;
    private FragmentStatePagerAdapter mAdapter;
    private  NetworkChangeReceiver networkChangeReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        applyPermission(); //获取权限
        initView();   //初始化控件
        LitePal.getDatabase();  //创建数据库
    }

    private void initView() {
        // find view
        mViewPager = findViewById(R.id.fragment_vp);
        mTabRadioGroup = findViewById(R.id.tabs_rg);
        // init fragment
        mFragments = new ArrayList<>(5);
        mFragments.add(new HomepagerFragment());
        if(NetWorkUtils.networkAvailable(this)) {
            if(LoginModel.getISLogin()) {
                mFragments.add(new MaintenanceFragment());
            }
            else {
                NoConnectFragment fragment=NoConnectFragment.newInstance("您还没有登陆，请先登陆");
                fragment.setListener(new NoConnectFragment.NoConnectListener() {
                    @Override
                    public void onRefresh() {
                        if(LoginModel.getISLogin()&&NetWorkUtils.networkAvailable(MainActivity.this)) { //如果网络可用且登录
                            MaintenanceFragment maintenanceFragment = new MaintenanceFragment();
                            replaceFragment(1, maintenanceFragment);
                        }
                    }
                });
                mFragments.add(fragment);
            }
        }else {
            NoConnectFragment fragment=NoConnectFragment.newInstance("请检查网络设置");
            fragment.setListener(new NoConnectFragment.NoConnectListener() {
                @Override
                public void onRefresh() {
                    if(LoginModel.getISLogin()&&NetWorkUtils.networkAvailable(MainActivity.this)) { //如果网络可用且登录
                        MaintenanceFragment maintenanceFragment = new MaintenanceFragment();
                        replaceFragment(1, maintenanceFragment);
                    }
                }
            });
            mFragments.add(fragment);
        }
        //商城板块
        if(NetWorkUtils.networkAvailable(this)){
            mFragments.add(new ShopFragment());
        }
        else {
            NoConnectFragment fragment=NoConnectFragment.newInstance("没有网络了,请检查网络设置");
            fragment.setListener(new NoConnectFragment.NoConnectListener() {
                @Override
                public void onRefresh() {
                  if(NetWorkUtils.networkAvailable(MainActivity.this)){ //网络可用 显示商城
                      replaceFragment(2,new ShopFragment());
                  }
                }
            });
            mFragments.add(fragment);
        }
        mFragments.add(new NotificationFragment());
        mFragments.add(new PersonFragment());
        // init view pager
        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(mAdapter);
        // register listener
        mViewPager.addOnPageChangeListener(mPageChangeListener);
        mTabRadioGroup.setOnCheckedChangeListener(mOnCheckedChangeListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewPager.removeOnPageChangeListener(mPageChangeListener);
     //   UnRegistBroadcast(); //取消注册广播
    }

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            RadioButton radioButton = (RadioButton) mTabRadioGroup.getChildAt(position);
            radioButton.setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            for (int i = 0; i < group.getChildCount(); i++) {
                if (group.getChildAt(i).getId() == checkedId) {
                    mViewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    };

    private class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> mList;

        public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.mList = list;
        }

        @Override
        public Fragment getItem(int position) {
            return this.mList == null ? null : this.mList.get(position);
        }

        @Override
        public int getCount() {
            return this.mList == null ? 0 : this.mList.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }
    }
   public Fragment getSingleFragment(int i){
        return mAdapter.getItem(i);
   }

   private void applyPermission(){
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           /**
            * API23以上版本需要发起写文件权限请求
            */
           ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
      }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            //上面请求时候的请求码
            case PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager
                        .PERMISSION_GRANTED&&grantResults[1]==PackageManager
                        .PERMISSION_GRANTED) ;
                else{
                    Toast.makeText(this,"未同意权限可能导致功能不可使用",Toast.LENGTH_LONG).show();
                }
              break;
            default:
                break;
        }
    }


    /**
     * 替换Viewpaget中碎片
     * @param i  位置
     * @param fragment 新碎片
     */
    private  void replaceFragment(int i,Fragment fragment){
        mFragments.set(i,fragment);
        mAdapter.notifyDataSetChanged();
    }

//    /**
//     * 注册广播
//     */
//    private void  RegistBroadcast(){
//        IntentFilter intentFilter=new IntentFilter();
//        intentFilter.addAction("android.net.conn.CONNECTCTIVITY_CHANGE");
//        networkChangeReceiver=new NetworkChangeReceiver();
//        registerReceiver(networkChangeReceiver,intentFilter);
//    }
//
//    /**
//     * 取消注册广播
//     */
//    private void  UnRegistBroadcast(){
//     unregisterReceiver(networkChangeReceiver);
//    }

}
