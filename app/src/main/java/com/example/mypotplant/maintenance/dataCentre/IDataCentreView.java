package com.example.mypotplant.maintenance.dataCentre;

/**
 * Created by MXL on 2020/8/7
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public interface IDataCentreView {
    /**
     *
     * @param humi 湿度
     * @param potX  光照 空气湿度 温度 X坐标
     * @param potY 光照 空气湿度 温度 Y坐标
     * @param dataY  Y坐标轴
     */
    void initDataShowFragments(int[] humi,int[][][] potX,float[][][] potY,float[][] dataY);

    /**
     *
     * @param humi 湿度
     * @param potX  光照 空气湿度 温度 X坐标
     * @param potY 光照 空气湿度 温度 Y坐标
     * @param dataY  Y坐标轴
     * @param  postion 花盆位置
     */
    void updateDataShowFragments(int[] humi, int[][][] potX, float[][][] potY, float[][] dataY,int postion);

    /**
     * 装填养护记录
     * @param  adapt 适配器
     */
    void fillOperationRecyclerView(OperationAdapt adapt);



    /**
     * 设置浇水开关可用
     * @param available
     */
    void setWaterBtn(boolean available,int index);
    /**
     * 设置补光按钮
     */
    void setLightBtn(boolean available,int startindex,int endindex,int index);

    /**
     * 设置显示时间
     */
    void setTip(int operator,String time,int index);
    /**
     * 关闭显示时间
     */
    void shutOffTip(int operator,int index);
}
