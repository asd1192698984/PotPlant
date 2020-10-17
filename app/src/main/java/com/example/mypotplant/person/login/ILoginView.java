package com.example.mypotplant.person.login;

/**
 * Created by MXL on 2019/12/11
 * 类描述：完成登录界面的UI交互
 *
 * @version 1.0
 * @since 1.0
 */
public interface ILoginView {
    /**
     * 获取登录界面控件实例
     */
    void initview();
    /**
     * 完成点击登录后的动画展示
     */
    void showLoginAnimation();
    /*
     * 失败登录弹出的提示框
     */
    void showAlertDialog(String mesg);
    /*
     * 弹出Toast
     */
    void showToast(String mesg);

    /**
     * 关闭动画
     */
    void initLogin();
}
