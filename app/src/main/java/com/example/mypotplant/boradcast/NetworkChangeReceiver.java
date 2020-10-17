package com.example.mypotplant.boradcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.mypotplant.utils.NetWorkUtils;

/**
 * Created by MXL on 2020/9/25
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
    private static final String TAG = "NetworkChangeReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(!NetWorkUtils.networkAvailable(context)){
            Toast.makeText(context,"当前网络不可用",Toast.LENGTH_SHORT).show();
            return;
        }else{
//            if(NetWorkUtils.isMobile(context)){
//                Toast.makeText(context,"当前使用的是数据流量请注意",Toast.LENGTH_SHORT).show();
//            }
        }
    }
}
