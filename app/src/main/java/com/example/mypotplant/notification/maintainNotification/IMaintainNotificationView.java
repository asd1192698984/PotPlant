package com.example.mypotplant.notification.maintainNotification;

/**
 * Created by MXL on 2020/3/28
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public interface IMaintainNotificationView {
    /**
     *   利用适配器装填Recyclerview
     * @param adapt 适配器
     */
    void  fillRecycleview(MaintainNotiAdapter adapt);
}
