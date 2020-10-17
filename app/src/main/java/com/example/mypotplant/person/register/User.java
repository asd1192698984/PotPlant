package com.example.mypotplant.person.register;

import java.io.Serializable;

/**
 * Created by MXL on  2019/12/11
 * 类描述：这个类包含用户注册所需信息
 * @version 1.0
 * @since 1.0
 */
public class User implements Serializable {


    public  static  String DEFAULT_SING="这个人很懒，啥也没有";
    public  static  String  DEFAULT_ADDRESS="中国";
    String user_name; /** 用户名 不超过8位 不含特殊字符*/
    String user_password; /** 用户密码 6-18位*/
    int  user_sex;         /** 性别 男 0/女 1*/
    int user_age;         /**  大于等于0*/
    String user_phone_number; /** 通过验证码认证 11位 保证唯一注册*/
    String user_email;      /** 合法的邮箱 保证唯一注册 */
    String url;           /** 用户头像url*/
    String address;       /** 用户住址*/
    String sign;         /** 用户个性签名*/

    public User(){
       address=DEFAULT_ADDRESS;
       sign=DEFAULT_SING;
   }
    public int getUser_age() {
        return user_age;
    }

    public void setUser_age(int user_age) {
        this.user_age = user_age;
    }



    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getUser_password() {
        return user_password;
    }

    public String getUser_phone_number() {
        return user_phone_number;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public void setUser_phone_number(String user_phone_number) {
        this.user_phone_number = user_phone_number;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }


    public int  getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(int  user_sex) {
        this.user_sex = user_sex;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
