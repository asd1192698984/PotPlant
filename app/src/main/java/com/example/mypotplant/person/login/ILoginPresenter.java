package com.example.mypotplant.person.login;

import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Created by MXL on 2019/12/11
 * 类描述：完成登录界面的逻辑实现
 *
 * @version 1.0
 * @since 1.0
 */
public interface ILoginPresenter {
    /*
     * 完成登录的逻辑操作
     */
    void  doLogin(String username, String password,boolean ischecked);
    /**
     * 执行恢复密码
     */
    void recoverPassword(EditText username, EditText password, CheckBox checkBox);
}
