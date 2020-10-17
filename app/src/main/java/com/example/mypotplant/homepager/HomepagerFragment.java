package com.example.mypotplant.homepager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.mypotplant.LazyBaseFragment;
import com.example.mypotplant.MainActivity;
import com.example.mypotplant.R;
import com.example.mypotplant.homepager.flower.Search_Activity;
import com.example.mypotplant.homepager.flowerRank.RankActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by MXL on 2020/2/23
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class HomepagerFragment extends LazyBaseFragment implements IHomepagerView {
    HomepagerPresent mPresent ;
    RecyclerView mRecyclerView;
    ImageButton search_bt;
    ImageButton topic_bt;
    Toolbar toolbar;
//    Button test;
    @Override
    public View initView() {
        MainActivity mainActivity =(MainActivity)getActivity();
//        HomepagerFragment fragment=(HomepagerFragment)mainActivity.getSingleFragment(0);      //获取对应碎片
        mRecyclerView =getView().findViewById(R.id.homepager_recyclerview);
        search_bt=(ImageButton) (getView().findViewById(R.id.search_bt_homepager));
        search_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入搜索页面
                Log.d("HomepagerFragment", "onClick: search");
               Search_Activity.actionStart(getContext());
            }
        });
//        test=(Button)getView().findViewById(R.id.bt_test);
//        test.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DataSupport.deleteAll(Plant.class);
//            }
//        });
        topic_bt=getView().findViewById(R.id.topic_bt);
        topic_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RankActivity.actionStart(getActivity());
            }
        });
        return null;
    }

    @Override
    public void initData() {
      mPresent=new HomepagerPresent(this,getContext());
    }

    @Override
    protected void lazyLoad() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_homepager,container,false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
        mPresent.initRecycleview();
    }

    @Override
    public void fillRecycleview(CardAdapt adapt) {
      if(mRecyclerView!=null){
          LinearLayoutManager m=new LinearLayoutManager(getContext()) ;
          m.setOrientation(LinearLayoutManager.HORIZONTAL);
          mRecyclerView.setLayoutManager(m);
          mRecyclerView.setAdapter(adapt);
      }
    }
}
