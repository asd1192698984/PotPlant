package com.example.mypotplant.notification.maintainNotification;

/**
 * Created by MXL on 2020/3/28
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public interface IMaintainNotificationModel {
     MaintainNotiAdapter   getPhotoAdapt();
    /**
     * 从数据库中加载原始列表
     */
    void loadlist();
    /**
     * 数据统一保存在数据库<br/>
     * 由于通知可能操作频繁 最后活动结束统一保存一次
     *
     */
    void savelist();
    /**
     * 向列表添加数据
     */
    void addData(MaintainNotifi notifi);
    /**
     * 从列表删除数据
     */
    void removeData(int position);
}
