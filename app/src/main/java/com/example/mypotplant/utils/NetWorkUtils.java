package com.example.mypotplant.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by MXL on 2020/7/16
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class NetWorkUtils {
    private NetWorkUtils() {

    }

    /**
     * 判断当前网络是否可用
     *
     * @param context 上下文
     * @return 是否可用
     */
    public static boolean networkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) { //判断网络
            return networkInfo.isConnected();
        }
        return false;
    }

    /**
     * 判断网络类型：移动网络
     */
    public static boolean isMobile(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return null != networkInfo && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    /**
     * 判断网络类型：Wi-Fi类型
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return null != networkInfo && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }
    /**
     * android10版本
     * 判断网络类型：移动网络
     */
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    public static boolean isMobileQ29(Context context) {
//        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        Network network = cm.getActiveNetwork();
//        if (null == network) {
//            return false;
//        }
//        NetworkCapabilities capabilities = cm.getActiveNetworkCapabilities(network);
//        if (null == capabilities) {
//            return false;
//        }
//        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
//    }


    /**
     * android10版本
     * 判断网络类型：Wi-Fi类型
     */
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    public static boolean isWifiQ29(Context context) {
//        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        Network network = cm.getActiveNetwork();
//        if (null == network) {
//            return false;
//        }
//        NetworkCapabilities capabilities = cm.getActiveNetworkCapabilities(network);
//        if (null == capabilities) {
//            return false;
//        }
//        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
//    }

    /**
     * android10版本
     * 判断网络是否连接
     */
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    public static boolean isConnectedQ29(Context context) {
//        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        Network network = cm.getActiveNetwork();
//        if (null == network) {
//            return false;
//        }
//        NetworkCapabilities capabilities = cm.getActiveNetworkCapabilities(network);
//        if (null == capabilities) {
//            return false;
//        }
//        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
//    }
}
