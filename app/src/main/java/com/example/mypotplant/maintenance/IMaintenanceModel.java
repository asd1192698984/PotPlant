package com.example.mypotplant.maintenance;

/**
 * Created by MXL on 2020/1/15
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public interface IMaintenanceModel {
  PlantPhotoAdapt getPhotoAdapt();

  /**
   * 从数据库中加载原始列表
   */
  void loadlist();

  /**
   * 向列表添加数据
   */
  void addData(Plant plant);
  /**
   * 从列表删除数据
   */
  void removeData(int position);
  /**
   * 设置回调接口
   */
  void setListener(MaintenanceModel.ModelListener listener);
}