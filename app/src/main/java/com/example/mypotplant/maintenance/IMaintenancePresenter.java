package com.example.mypotplant.maintenance;

import java.io.File;

import androidx.fragment.app.Fragment;

/**
 * Created by MXL on 2020/1/15
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public interface IMaintenancePresenter {
   /**
    * <br>引用类：调用相机进行拍照{@link }<br/>
    *
    * @param  fragment 所在碎片
    * @return  照片保存的文件类 如未登录 返回null
    */
   File addPhoto(Fragment fragment);
   /**
    * 执行删除照片的逻辑操作<br/>
    */
    void removePhoto();
   /**
    *初始化适配器
    */
   void initRecycleview();

   /**
    * 执行数据保存
    * @param  file 图片文件
    */
   void  addData(File file);
}
