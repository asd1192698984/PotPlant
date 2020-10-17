package com.example.mypotplant.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by MXL on 2020/7/30
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class AppUtils {
    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context context
     * @param pkgName 应用包名
     * @return true:已安装；false：未安装
     */
    public static boolean isPkgInstalled(Context context, String pkgName) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        return packageInfo != null;
    }

}
