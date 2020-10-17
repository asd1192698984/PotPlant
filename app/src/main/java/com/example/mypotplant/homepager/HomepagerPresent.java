package com.example.mypotplant.homepager;

import android.content.Context;
import android.util.Log;

/**
 * Created by MXL on 2020/2/23
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class HomepagerPresent implements IHomepagerPresenter {
    HomepagerModel mModel;
    IHomepagerView mView;
    Context mContext;
    HomepagerPresent(IHomepagerView view, Context context){
        mView=view;
        mContext=context;
        mModel=new HomepagerModel(mContext);
        if(mContext==null){
            Log.d("Homepagerpre", "CardAdapt:111 ");
        }else
            Log.d("Homepagerpre", "CardAdapt:222 "+mContext);
    }

    @Override
    public void initRecycleview() {
      mView.fillRecycleview(mModel.getAdapt());
    }
}
