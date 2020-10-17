package com.example.mypotplant;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;

import com.example.mypotplant.boradcast.NetworkChangeReceiver;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    protected NetworkChangeReceiver networkChangeReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        registerBroadcast();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegistBroadcast();
    }

    /**
     * 注册广播
     */
    private void  registerBroadcast(){
        Log.d(TAG, "registerBroadcast: 注册网络变化广播");
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver=new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver,intentFilter);
    }

    /**
     * 取消注册广播
     */
    private void  unRegistBroadcast(){
        unregisterReceiver(networkChangeReceiver);
    }
}
