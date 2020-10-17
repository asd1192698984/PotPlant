package com.example.mypotplant;

/**
 * Created by MXL on 2019/12/12
 * <br>类描述：用于全局获取Context<br/>
 *
 * @version 1.0
 * @since 1.0
 */

import android.content.Context;

import org.litepal.LitePalApplication;

public class MApplication extends LitePalApplication {
   private static Context context;

    @Override
   public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
                context=getApplicationContext();
            }

            public static Context getContext() {
                 return context;
            }
 }
