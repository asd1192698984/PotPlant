package com.example.mypotplant.person;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypotplant.BlankFragment;
import com.example.mypotplant.LazyBaseFragment;
import com.example.mypotplant.MainActivity;
import com.example.mypotplant.R;
import com.example.mypotplant.http.BaseCallback;
import com.example.mypotplant.http.OkHttpHelper;
import com.example.mypotplant.person.Group.GroupActivity;
import com.example.mypotplant.person.login.LoginActivity;
import com.example.mypotplant.person.login.LoginModel;
import com.example.mypotplant.person.register.User;
import com.example.mypotplant.utils.MeatureUtils;
import com.example.mypotplant.utils.NetWorkUtils;
import com.example.mypotplant.utils.interfaces.DataInterface;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by MXL on 2020/1/17
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class PersonFragment extends LazyBaseFragment implements IPersonView, View.OnClickListener {

    public  final static  String TAG="PersonFragment";
    public  final  static  int TAB_SIZE=4;
    private  final  static  int MODIFY_INFO=1;
    PersonPresenter mPresenter;
    NavigationView nav_view;
    CircleImageView head_portrait;
    PersonFragment fragment;
    TextView  tv_username;
    ImageButton setting;
    ImageButton addfriend;
    TabLayout tabLayout;
    ViewPager viewPager;
    List<Fragment> fragments;
    List<String> titles; //标签题目
    int[] mGetCount;  //角标数量

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_person, container, false);
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initView();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Cirv_head_portrait:   //点击头像判断登录 如未登录跳转登录
                if(!LoginModel.getISLogin())
                LoginActivity.actionStart(getActivity());
                else{
                    //跳转个人信息完善界面 for result 如果信息修改 则改变相应信息
//                    ModifyInfoActivity.actionStart(getActivity());
                    Intent intent=new Intent(getActivity(),ModifyInfoActivity.class);
                    intent.putExtra("mode",ModifyInfoActivity.MODE_MODIFY);
                    startActivityForResult(intent, MODIFY_INFO);
                }
                break;
        }
    }
    /**
     * 实例化控件和设置按钮监听
     */
    @Override
    public View initView() {
//        mPresenter=new PersonPresenter();
        MainActivity mainActivity =(MainActivity)getActivity();
        fragment=(PersonFragment)mainActivity.getSingleFragment(4);      //获取对应碎片
        nav_view=fragment.getView().findViewById(R.id.nav_view_person_frag);
        View headerLayout = nav_view.getHeaderView(0);
        head_portrait=(CircleImageView) headerLayout.findViewById(R.id.Cirv_head_portrait);
        tv_username=(TextView)headerLayout.findViewById(R.id.tv_person_username);
        head_portrait.setOnClickListener(this);
        fillviewpage();  //初始化viewpager
        setting=(ImageButton)headerLayout.findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
             //   Toast.makeText(getActivity(),"点击了",Toast.LENGTH_LONG).show();
                PersonSetActivity.actionStart(getActivity());
            }
        });
        addfriend=(ImageButton)headerLayout.findViewById(R.id.addfriend);
        addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupActivity.actionStart(getActivity());
            }
        });
        return null;
    }

    @Override
    public void initData() {
        mPresenter=new PersonPresenter();
        fragments=new ArrayList<>();
        fragments.add(BlankFragment.newInstance(""));
        fragments.add(BlankFragment.newInstance(""));
         final PersonListFragment fragment=PersonListFragment.newInstance();
        fragment.setListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                final List<User> users=new ArrayList<>();
//               PersonListener listener=new PersonListener() {
//                   @Override
//                   public void afterload(List<User> users) {
//                       fragment.refreshUser(users);
//                   }
                 DataInterface<List<String> > listener=new DataInterface<List<String>>() {
                     @Override
                     public void afterLoad(final List<String> ids) {
                         for(String id:ids){
                             findUserbyId(id, new DataInterface<User>() {
                                 @Override
                                 public void afterLoad(User user) {
                                     users.add(user);
                                     if(users.size()==ids.size()){  //刷新
                                         fragment.refreshUser(users);
                                     }
                                 }

                                 @Override
                                 public void beforeLoad() {

                                 }
                             });
                         }
                     }
                     @Override
                     public void beforeLoad() {

                     }
                 };
                getPersonList(fragment,listener,OkHttpHelper.URL_BASE+OkHttpHelper.URL_GET_FOLLOWS);
               };
        });
        fragments.add(fragment);
        final PersonListFragment fragment2=PersonListFragment.newInstance();
        fragment2.setListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                final List<User> users=new ArrayList<>();
                DataInterface<List<String> > listener=new DataInterface<List<String>>() {
                    @Override
                    public void afterLoad(final List<String> ids) {
                        for(String id:ids){
                            findUserbyId(id, new DataInterface<User>() {
                                @Override
                                public void afterLoad(User user) {
                                    users.add(user);
                                    if(users.size()==ids.size()){  //刷新
                                        fragment2.refreshUser(users);
                                    }
                                }

                                @Override
                                public void beforeLoad() {

                                }
                            });
                        }
                    }
                    @Override
                    public void beforeLoad() {

                    }
                };
                getPersonList(fragment2,listener,OkHttpHelper.URL_BASE+OkHttpHelper.URL_GET_FANS);
            }
        });
        fragments.add(fragment2);
        titles=new ArrayList<>();
        titles.add("动态");
        titles.add("植株");
        titles.add("关注");
        titles.add("粉丝");
        mGetCount=new int[]{0,0,0,0};
}

    @Override
    protected void lazyLoad() {

    }

    /**
     * 初始化viewpager
     */
    public  void fillviewpage(){
        tabLayout=getView().findViewById(R.id.tablay);
        viewPager=getView().findViewById(R.id.viewpager);
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
        //viewpager滑动监听
       viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
           @Override
           public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

           }

           @Override
           public void onPageSelected(int position) {
            tabLayout.getTabAt(position).select();
           }

           @Override
           public void onPageScrollStateChanged(int state) {

           }
       });
        setTabStyle();
        //设置角标
        for(int i=0;i<titles.size();i++){
            setTabcount(i,mGetCount[i]);
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //选中viewpager
               viewPager.setCurrentItem(tab.getPosition());
               //改变字体颜色
                    for(int i=0;i<TAB_SIZE;i++) {
                       TabLayout.Tab t= tabLayout.getTabAt(i);
                        if(t.getPosition()==tab.getPosition()){
                            if(t.getCustomView()!=null) {
                                TextView tv = t.getCustomView().findViewById(R.id.title);
                                tv.setTextColor(getResources().getColor(R.color.color_1A9AF7));
                            }
                        }else {
                            if(t.getCustomView()!=null) {
                            TextView tv =t.getCustomView().findViewById(R.id.title);
                            tv.setTextColor(getResources().getColor(R.color.black));
                            }
                        }
                    }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
//        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * 该方法为了自定义Tablayout
     */
    private void setTabStyle(){
        for(int i = 0;i <titles.size();i++){//根据Tab数量循环来设置
            tabLayout.addTab(tabLayout.newTab());
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if(tab != null) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.person_tab_item, null);
             //   ((TextView) view.findViewById(R.id.msgnum)).setText(String.valueOf(mGetCount[i]));//设置角标数量
                ((TextView) view.findViewById(R.id.title)).setText(titles.get(i));//设置Tab标题
                if(i == 0) {//第一个默认为选择样式
                    ((TextView) view.findViewById(R.id.title)).setTextColor(getResources().getColor(R.color.color_1A9AF7));//将第一个Tab标题颜色设为蓝色
//                    ((AppCompatImageView) view.findViewById(R.id.tabicon)).setImageResource(mSelectArray[i]);//将第一个Tab图标设为蓝色
                }else {
//                    ((AppCompatImageView) view.findViewById(R.id.tabicon)).setImageResource(mUnSelectArray[i]);//将其他Tab图标设为灰色
                }
                tab.setCustomView(view);//最后添加view到Tab上面
            }
        }
    }

    /**
     * <br>引用类：{@link }<br/>
     *
     * @param i 第几个tab
     * @param count 数量
     * @return
     */
//      <TextView
//    android:background="@drawable/tv_jiaobiao"
//    android:text="1"
//    android:textColor="@color/white"
//    android:id="@+id/msgnum"
//    android:gravity="center"
//    android:layout_width="wrap_content"
//    android:layout_height="wrap_content"
//    android:layout_marginLeft="35dp">
    private  void  setTabcount(int i,int count){
       RelativeLayout relativeLayout =(RelativeLayout)tabLayout.getTabAt(i).getCustomView();
       if(relativeLayout.findViewById(R.id.msgnum)!=null) {
             if(count>0){ //数量大于0
                 ((TextView)relativeLayout.findViewById(R.id.msgnum)).setText(count);
             }
       }else {
           if (count>0){ //数量大于 0
              TextView textView=new TextView(getActivity());
               RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
              lp.setMargins(MeatureUtils.dip2px(getActivity(),35),0,0,0);
              textView.setLayoutParams(lp);
              textView.setBackground(getResources().getDrawable(R.drawable.tv_jiaobiao));
              textView.setText(String.valueOf(count));
              textView.setTextColor(getResources().getColor(R.color.white));
              textView.setGravity(Gravity.CENTER);
              textView.setId(R.id.msgnum);
              relativeLayout.addView(textView);
           }
       }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case  MODIFY_INFO:
                if(resultCode== Activity.RESULT_OK) {
                    String imagepath = data.getStringExtra("path");
                    if (imagepath != null) {
                        head_portrait.setImageBitmap(BitmapFactory.decodeFile(imagepath));
                    }
                    String username = data.getStringExtra("username");
                    if (username != null) {
                        tv_username.setText(username);
                    }
                }
                break;
        }
    }

    /**
     * 得到粉丝或者关注列表
     * @param listener
     */
    private void getPersonList(PersonListFragment fragment, final DataInterface<List<String> > listener, final String url){
        final List<User> users=new ArrayList<>();
        final List<String> ids=new ArrayList<>();
        if(NetWorkUtils.networkAvailable(getActivity())) {
            if(LoginModel.getISLogin()) {
                Log.d(TAG, "getPersonList: ");
                OkHttpHelper helper = OkHttpHelper.getinstance();
                Map data = new HashMap<>();
                data.put("user_id", LoginModel.getUser().getUser_phone_number());
                Log.d(TAG, "getPersonList: "+new Gson().toJson(data));
                helper.post(url, new Gson().toJson(data), new BaseCallback<JSONArray>() {
                    @Override
                    public void onRequestBefore() {

                    }

                    @Override
                    public void onFailure(Request request, Exception e) {
                        Log.d(TAG, "onFailure: ");
                    }

                    @Override
                    public void onSuccess(Response response, JSONArray array) {
                        Log.d(TAG, "onSuccess: "+array);
                        List<String> ids=new ArrayList<>();
                        for(int i=0;i<array.length();i++){
                            JSONObject jsonObject=null;
                            try {
                                String id=array.getString(i);
                              //  String id=jsonObject.getString("user_id");
                                ids.add(id);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        listener.afterLoad(ids);
                    }

                    @Override
                    public void onError(Response response, int errorCode, Exception e) {
                        Log.d(TAG, "onError: " + errorCode);
                    }
                },"result");
            }else { //未登录
               fragment.cancelRefresh();
               Toast.makeText(getContext(),"请完成登录",Toast.LENGTH_SHORT).show();
            }
        }else { //未联网
            fragment.cancelRefresh();
            Toast.makeText(getContext(),"请检查网络设置",Toast.LENGTH_SHORT).show();
        }
    }
//    interface  PersonListener{
//        void afterload(List<User> users);
//    }
    /**
     * 通过id请求user
     */
    private void findUserbyId(String id, final DataInterface<User> listener){
        final User user=new User();
        if(NetWorkUtils.networkAvailable(getContext())) {
            OkHttpHelper helper = OkHttpHelper.getinstance();
            Map data = new HashMap();
            data.put("user_id",id);
            Log.d(TAG, "getinfo: "+new Gson().toJson(data));
            helper.post(OkHttpHelper.URL_BASE + OkHttpHelper.URL_GET_INFO, new Gson().toJson(data), new BaseCallback<String>() {
                @Override
                public void onRequestBefore() {

                }

                @Override
                public void onFailure(Request request, Exception e) {

                }

                @Override
                public void onSuccess(Response response, String s) {
                    Log.d(TAG, "onSuccess: "+s);
                    try {
                        JSONObject jsonObject=new JSONObject(s);
                        user.setUser_phone_number(LoginModel.getUser().getUser_phone_number());
                        user.setUser_name(jsonObject.getString("user_name"));
                        user.setAddress(jsonObject.getString("user_address")==null?User.DEFAULT_ADDRESS:jsonObject.getString("user_address"));
                        user.setSign(jsonObject.getString("user_signature")==null?User.DEFAULT_SING:jsonObject.getString("user_signature"));
                        user.setUrl(jsonObject.getString("user_pic"));
                        if(listener!=null){
                            listener.afterLoad(user);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Response response, int errorCode, Exception e) {
                    Log.d(TAG, "onError: "+errorCode);
                }
            });
        }else {
            Toast.makeText(getContext(),"请检查网络设置",Toast.LENGTH_SHORT).show();
        }
    }
}
