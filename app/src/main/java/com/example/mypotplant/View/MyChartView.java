package com.example.mypotplant.View;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;

import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by MXL on 2020/9/16
 * <br>类描述：<br/>
 *https://blog.csdn.net/qq_40090482/article/details/81113162
 * 解决Viewpager和ChartView滑动冲突
 * @version 1.0
 * @since 1.0
 */
public class MyChartView extends LineChartView {

    PointF downPoint = new PointF();
    public MyChartView(Context context) {
        super(context);
    }

    public MyChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);// 用getParent去请求,
        // 不拦截
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        switch (evt.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downPoint.x = evt.getX();
                downPoint.y = evt.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (getScaleX() > 1 && Math.abs(evt.getX() - downPoint.x) > 5) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
        }
        return super.onTouchEvent(evt);
    }

}
