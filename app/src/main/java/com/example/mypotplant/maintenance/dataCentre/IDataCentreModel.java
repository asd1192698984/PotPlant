package com.example.mypotplant.maintenance.dataCentre;

import com.example.mypotplant.maintenance.dataCentre.bean.Operation;
import com.example.mypotplant.utils.interfaces.DataInterface;

import java.util.List;

/**
 * Created by MXL on 2020/8/7
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public interface IDataCentreModel {
    void updatedate(LoadDataListener loadDataListener);
    /**
     * 得到当前的数据信息
     * @return 土壤湿度，光强，空气湿度，温度
     */
    Object[] getCurData();

    /**
     * 获得养护操作信息
     * @param listener 回调接口
     * @param index 盆栽位置
     * @return 养护操作信息
     */
    List<Operation> getOperationInfos(DataInterface<List<Operation>> listener,int index);


    /**
     * 更新养护信息
     * @param index 植株盆栽位置
     */
    void updateOperationInfos(int index);

    /**
     *
     * @return 养护记录适配器
     */
    OperationAdapt getOperationAdapt();

    /**
     * 传递养护记录
     * @param index 植株位置
     * @param type 操作类型
     * @param starttime  起始时间 可空
     * @param  endtime 结束时间 可空
     */
    void putRecord(int index,int type,String starttime,String endtime);
    interface  LoadDataListener{
        void updatedata(int[] humi,int[][][] potX, float[][][] potY);
    }

}
