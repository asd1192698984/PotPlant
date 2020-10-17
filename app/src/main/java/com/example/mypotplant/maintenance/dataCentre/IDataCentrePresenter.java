package com.example.mypotplant.maintenance.dataCentre;

/**
 * Created by MXL on 2020/8/7
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public interface IDataCentrePresenter {
    /**
     * 初始化数据显示碎片
     */
    void fillDataShowFragments();

    /**
     * 更新数据显示碎片
     * @param position 花盆位置
     */
    void updataDataShowFragments(int position);

    /**
     * 初始化养护记录
     */
    void initOperationRecyclerView(int index);
    /**
     * 更换养护记录
     * @param index
     */
    void updateOperationRecyclerView(int index);

    /**
     *
     */
    void doWater(int index);
    /**
     *
     */
    void doLight(int index,int startindex,int endindex);

    /**
     * 取消定时器
     */
    void finishTimer();
}
