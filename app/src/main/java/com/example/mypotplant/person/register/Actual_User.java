package com.example.mypotplant.person.register;

/**
 * Created by MXL on 2020/1/20
 * <br>类描述： 接受注册信息的实体类<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class Actual_User {
    public static  String MAN="男";
    public static  String WOMAN="女";
    String user_name; /** 用户名 不超过8位 不含特殊字符*/
    String user_password; /** 用户密码 6-18位*/
    String user_password_again;  /**重复输入的密码*/
    //去掉性别 年龄  邮箱 Add by MXL 2020-7-5
//    String  user_sex;         /** 性别 男 0/女 1*/
//    String user_age;         /**  大于等于0*/
    String user_phone_number; /** 通过验证码认证 11位 保证唯一注册*/
//    String user_email;      /** 合法的邮箱 保证唯一注册 */

    public String getUser_name() {
        return user_name;
    }

    public String getUser_password() {
        return user_password;
    }

    public String getUser_phone_number() {
        return user_phone_number;
    }


    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public void setUser_phone_number(String user_phone_number) {
        this.user_phone_number = user_phone_number;
    }
    public String getUser_password_again() {
        return user_password_again;
    }
    public void setUser_password_again(String user_password_again) {
        this.user_password_again = user_password_again;
    }
}
