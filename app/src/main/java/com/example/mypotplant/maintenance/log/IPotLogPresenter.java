package com.example.mypotplant.maintenance.log;

/**
 * Created by MXL on 2020/7/12
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public interface IPotLogPresenter {
    /**
     * 初始化适配器 无数据
     * 如未登录 不加载页面
     */
    void initRecycleview();
    /**
     *  执行设置修改
     */
    void domodifySetting();
    /**
     * 执行类型选择
     */
    void dotypeset();
    /**
     * 执行编辑日志
     */
    void editPotLog();
    /**
     * 添加日志
     */
    void addPotLog(PotLog potLog);

}
