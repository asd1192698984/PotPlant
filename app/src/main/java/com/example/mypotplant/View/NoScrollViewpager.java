package com.example.mypotplant.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by MXL on 2020/9/10
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class NoScrollViewpager extends ViewPager {
    private boolean isPagingEnabled = false;
    public NoScrollViewpager(@NonNull Context context) {
        super(context);
    }
    public NoScrollViewpager(@NonNull Context context,  @Nullable AttributeSet attrs) {
        super(context,attrs);
    }
    @Override
      public boolean onTouchEvent(MotionEvent event) {
               return this.isPagingEnabled && super.onTouchEvent(event);
            }
             @Override
      public boolean onInterceptTouchEvent(MotionEvent event) {
                return this.isPagingEnabled && super.onInterceptTouchEvent(event);
             }
            public void setPagingEnabled(boolean b) {
                this.isPagingEnabled = b;
            }
}
