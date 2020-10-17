package com.example.mypotplant.maintenance.log;

/**
 * Created by MXL on 2020/7/12
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public interface IPotLogModel {
    PotLogAdapt getPhotoAdapt();
    /**
     * 从数据库中加载原始列表
     */
    void loadlist(PotLogModel.PotLogListener listener);
    /**
     * 数据统一保存在数据库<br/>
     * 可能操作频繁 最后活动结束统一保存一次
     *
     */
    void savelist();
    /**
     * 向列表添加数据
     */
    void addData(PotLog potLog);
    /**
     * 从列表删除数据
     */
    void removeData(int position);
    /**
     * 根据设置修改向服务器传达 同时更新本地
     */
    void modifySetting(Object[] data);

    /**
     * 修改对应植株的类别
     * @param type 类别 code
     */
    void modifytype(String type);
}
