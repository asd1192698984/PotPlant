package com.example.mypotplant.person.register;

/**
 * Created by MXL on 2019/12/12
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public interface IRegisterView {
    /**
     * <br>引用类：{@link }<br/>
     * 直接从控件取得当前注册信息
     * @return  注册信息封装的user
     */
    Actual_User getRegiser_mes();

    /**
     *<br>引用类：{@link }<br/>
     * @param result 注册返回的错误码
     */
    void Toast_register_res(int result);
}
