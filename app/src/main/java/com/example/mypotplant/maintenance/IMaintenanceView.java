package com.example.mypotplant.maintenance;

/**
 * Created by MXL on 2020/1/15
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public interface IMaintenanceView {
    /**
     *   利用适配器装填Recyclerview
     * @param adapt 适配器
     */
    void  fillRecycleview(PlantPhotoAdapt adapt);

}
