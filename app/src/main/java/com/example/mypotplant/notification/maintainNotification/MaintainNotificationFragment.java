package com.example.mypotplant.notification.maintainNotification;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mypotplant.LazyBaseFragment;
import com.example.mypotplant.R;
import com.example.mypotplant.notification.SwipeItemLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by MXL on 2020/3/28
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class MaintainNotificationFragment extends LazyBaseFragment implements IMaintainNotificationView {

    private MaintainNotificationPresenter mPresent;
    private RecyclerView recyclerView;
    private String TAG="MaintainNotificationFragment";
    private  View rootView;


    /**
     * 用这个方法创建碎片新实例
     * 这个碎片用这种方式保护参数

     * @return  MaintainNotificationFragment 的一个新实例
     */

    public static MaintainNotificationFragment newInstance() {
    //    Log.d("Fragment", "newInstance: ");
        MaintainNotificationFragment fragment = new MaintainNotificationFragment();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: ");
        if(rootView!=null){
            return  rootView;
        }
        else {
            rootView = inflater.inflate(R.layout.fragment_maintain_notification, container, false);
            return rootView;
        }
    }

     /*
      *  由于延迟加载调用在方法之前 每次数据在这里重新加载
      */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: ");
        initData();
        initView();
        mPresent.loadlist();

    }

    @Override
    public View initView() {
        recyclerView=getView().findViewById(R.id.recyleview_maintainNotifi_fragment);
        Button test=getView().findViewById(R.id.bt_test2);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresent.addNotifi(new MaintainNotifi(MaintainNotifiConstants.MaintainNotifi_Type.WATER,"绿萝"));
            }
        });
        test.setVisibility(View.INVISIBLE);
        mPresent.initRecycleview();
        return null;
    }

    @Override
    public void initData() {
        Log.d(TAG, "initData: ");
        mPresent=new MaintainNotificationPresenter(this,getContext());
       }

    @Override
    protected void lazyLoad() {
        Log.d(TAG, "lazyLoad: ");
    }

    @Override
    public void fillRecycleview(MaintainNotiAdapter adapt) {
        if(recyclerView!=null){
            LinearLayoutManager m=new LinearLayoutManager(getContext()) ;
            m.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(m);
            //完成左右滑动交互
            recyclerView.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(getContext()));
            recyclerView.setAdapter(adapt);
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL));
        }
    }

}
