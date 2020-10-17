package com.example.mypotplant.utils;

import android.content.Context;

/**
 * Created by MXL on 2020/9/1
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class MeatureUtils {
    private MeatureUtils(){}
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
