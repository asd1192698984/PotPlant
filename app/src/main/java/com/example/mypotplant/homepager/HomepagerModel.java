package com.example.mypotplant.homepager;

import android.content.Context;
import android.util.Log;

import com.example.mypotplant.R;

import java.util.ArrayList;

/**
 * Created by MXL on 2020/2/23
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class HomepagerModel implements IHomepagerModel {
    CardAdapt mAdapt=null;
    ArrayList mlist=null;
    Context mContext;
    String TAG="HomepagerModel";
    HomepagerModel(Context context){
        mContext=context;
    }
    @Override
    public CardAdapt getAdapt() {
        if(mAdapt!=null)
            return  mAdapt;
        else{
          mAdapt=new CardAdapt(getlist(),mContext);
          return  mAdapt;
        }
    }
    private ArrayList<Carditem> getlist(){
        Log.d(TAG, "getlist: ");
        if(mlist!=null)
            return  mlist;
        else {
            ArrayList mlist = new ArrayList<Carditem>();
            mlist.add(new Carditem(R.drawable.cactus_card,"仙人掌"));
            mlist.add(new Carditem(R.drawable.air_pineapple_card,"空气凤梨"));
            return mlist;
        }
    }
}
