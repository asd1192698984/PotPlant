package com.example.mypotplant.person.register;

import com.example.mypotplant.http.BaseCallback;

/**
 * Created by MXL on 2019/12/11
 * 类描述：完成注册的数据管理验证
 *
 * @version 1.0
 * @since 1.0
 */
public interface IRegisterModel {
    public  static  final  String  REGISTER_SUCCESS="成功注册";
    public  static  final  String  REGISTER_FAILED="注册失败";
    /**
     * 判断用户输入是否符合规范
     * <br>需要验证的参数 用户名 密码 性别 年龄 手机号码 电子邮箱<br/>
     * 查重验证参数 用户名 手机号码 电子邮箱 交由服务端
     * <br>引用类：{@link RegisterModelHelp}<br/>
     * @param user 用户输入的信息
     * @return 是否符合要求
     */
    boolean isPass(Actual_User  user);

    /**
     * <br>引用类：{@link }<br/>
     *
     * @param  user 注册的User类
     * @return 提交返回的错误码
     */
    int putdata(User user, BaseCallback callback,String key);

}
