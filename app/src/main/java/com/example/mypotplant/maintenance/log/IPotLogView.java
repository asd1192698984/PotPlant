package com.example.mypotplant.maintenance.log;

import android.content.DialogInterface;

import com.example.mypotplant.maintenance.Plant;

/**
 * Created by MXL on 2020/7/12
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public interface IPotLogView {
    /**
     *   利用适配器装填Recyclerview
     * @param adapt 适配器
     */
    void  fillRecycleview(PotLogAdapt adapt);
    /**
     *  利用适配器修改植株设置
     *  修改 1.种类  int数字编号 code
     *       2.养护模式 0智能提醒 1自动养护 2无
     */
    void  modifySetting(Plant plant);

    /**
     * 设置设置接口
     * @param dialogListener
     */
    void setDialogListener(PotLogActivity.DialogListener dialogListener);

    /**
     * 构造对话框
     * @param title 标题
     * @param content  内容
     * @param listener 确定按钮监听
     */
    void alert(String title, String content, DialogInterface.OnClickListener listener);
}
