package com.example.mypotplant.maintenance.dataCentre.datashowfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mypotplant.LazyBaseFragment;
import com.example.mypotplant.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by MXL on 2020/8/11
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class WetFragment extends LazyBaseFragment {

    private  static  final int WET_MIN=255;
    private View view1;
    private View view2;
    private   TextView dry;
    private TextView wet;
    private  int wetlevel;
    private  int move_range;

    private WetFragment(){}

    public  static WetFragment newInstance(int wetlevel){
        WetFragment tempFragment=new WetFragment();
        Bundle data =new Bundle();
        data.putInt("wetlevel",wetlevel);
        tempFragment.setArguments(data);
        return tempFragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_temp,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initView();
        changeTV(iswet());
    }

    @Override
    public View initView() {
        view1=getView().findViewById(R.id.view1);
        view2=getView().findViewById(R.id.view2);
        //获取长度差
        move_range=view1.getWidth()-view2.getWidth();
        dry=getView().findViewById(R.id.tv_1);
        wet=getView().findViewById(R.id.tv_2);
        return null;
    }

    @Override
    public void initData() {
       wetlevel=getArguments().getInt("wetlevel");
    }

    @Override
    protected void lazyLoad() {

    }
    /**
     * 改变干湿度
     * @param wetlevel
     */
    public void setWetlevel(int wetlevel) {
        this.wetlevel = wetlevel;
        changeTV(iswet());
    }

    /**
     * 判断是否潮湿  0-255 0非常湿
     */
    private  boolean  iswet(){
        return  wetlevel<=WET_MIN/2;
    }

    /**
     * <br>引用类：{@link }<br/>
     * 改变干湿度
     * @param iswet 是否湿润
     * @return
     */
    private  void changeTV(boolean iswet){
        if(iswet){
            turnLight(wet);
            turnOff(dry);
        }else {
            turnLight(dry);
            turnOff(wet);
        }
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(view2.getLayoutParams());
        lp.setMargins(move_range*((255-wetlevel)/WET_MIN),0,0,0);
    }

    /**
     * 点亮TV
     * @param tv
     */
    private  void turnLight(TextView tv){
        tv.setTextColor(getResources().getColor(R.color.deepskyblue));
    }
    /**
     * 熄灭TV
     * @param tv
     */
    private  void turnOff(TextView tv){
        tv.setTextColor(getResources().getColor(R.color.color_989898));
    }

}
