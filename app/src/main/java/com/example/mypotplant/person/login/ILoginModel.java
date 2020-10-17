package com.example.mypotplant.person.login;

import android.widget.CheckBox;
import android.widget.TextView;

import com.example.mypotplant.http.BaseCallback;

/**
 * Created by MXL on 2019/12/12
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public interface ILoginModel {
   /**
    * 从服务端获取登录授权
    */
    boolean getLoginAccess(String username, String password,BaseCallback callback);
    /**
     * 加载设置信息
     */
//    boolean getSettingMsg();

    /**
     * 保存密码
     * @param username 账号
     * @param password 密码
     * @return
     */
    void savePassword(String username, String password);
    /**
     * 删除密码
     * @return
     */
    void removePassword();

    /**
     * 恢复密码
     * @param username 账号
     * @param password 密码
     */
    void recoverPassword(TextView username, TextView password, CheckBox checkBox);
}
