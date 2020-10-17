package com.example.mypotplant.notification.maintainNotification;

/**
 * Created by MXL on 2020/3/28
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public interface IMaintainNotificationPresenter {
    /**
     * 产生一条通知<br/>
     *
     */
    void addNotifi(MaintainNotifi notifi);
    /**
     *初始化适配器 无数据
     * 如未登录 不加载页面
     */
    void initRecycleview();
    /**
     *  从数据库加载数据
     */
    void loadlist();
}
