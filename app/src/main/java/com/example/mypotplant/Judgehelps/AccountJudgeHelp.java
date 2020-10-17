package com.example.mypotplant.Judgehelps;


import com.example.mypotplant.person.register.Actual_User;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by MXL on 2019/12/12
 * <br>类描述：对用户名密码等信息进行判断<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class AccountJudgeHelp {
    public  static  final  int  USERNAME_MAX_LENGTH=8;  /**  用户名最大长度*/
    public  static  final  int  USERNAME_MIN_LENGTH=2;  /**  用户名最少长度*/
    public  static  final  int  PASSWORD_MAX_LENGTH=18;  /**  密码最大长度*/
    public  static  final  int  PASSWORD_MIN_LENGTH=6;  /**  密码最少长度*/
    public  static  final  String USERNAME_AVAILABLE="用户名有效";
    public  static  final  String USERNAME_INVAILD_LENGTH="用户名长度应大于等于2位，小于等于8位英文字符,小于等于4位汉字";
    public  static  final  String USERNAME_ILLEGAL_CHAR="用户名不应含有@#$%^&*()+= 等非法字符";
    public  static  final  String PASSWORD_AVAILABLE="密码有效";
    public  static  final  String PASSWORD_INVAILD_LENGTH="密码长度应为8-16位英文字符";
    public  static  final  String PASSWORD_INPUT_CONSISTENT="两次密码输入一致";
    public  static  final  String PASSWORD_INPUT_INCONSISTENT="两次密码输入不一致";
    public  static  final  String SEX_AVAILABLE="性别有效";
    public  static  final  String SEX_INVAILD="性别应为男/女";
    public  static  final  String PHONE_AVAILABLE="手机号码有效";
    public  static  final  String PHONE_INVAILD="手机格式错误有效";
    public  static  final  String EMAIL_AVAILABLE="电子邮箱有效";
    public  static  final  String EMAIL_INVAILD="无效的邮箱格式";
    public  static  final  String AGE_AVAILABLE="年龄有效";
    public  static  final  String AGE_NOINT_ERROR="年龄应为数字";
    public  static  final  String AGE_INVAILD="非法年龄";
    /**
     * 该方法对输入用户名进行检测
     *<br>帮助类：{@link  JudgeHelpUtil}<br/>
     * @param username 用户名
     * @return 提示字符串
     */
    public static String usernameValidate(String username) throws ParseException {
        String tipInfo =USERNAME_AVAILABLE;// 记录错误信息
        if(username.length()<=USERNAME_MIN_LENGTH||username.length()>=USERNAME_MAX_LENGTH){
            tipInfo=USERNAME_INVAILD_LENGTH;
        }
        if(JudgeHelpUtil.isSpecialChar(username)){
            tipInfo=USERNAME_ILLEGAL_CHAR;
        }
        return  tipInfo;
    }

    /**
     *<br>帮助类：{@link  JudgeHelpUtil}<br/>
     * 该方法对输入密码进行检测
     * @param password 密码
     * @return 提示字符串
     */
    public static String passwordValidate(String password) throws ParseException {
        String tipInfo =PASSWORD_AVAILABLE;// 记录错误信息
        if( password.length()<=PASSWORD_MIN_LENGTH|| password.length()>=PASSWORD_MAX_LENGTH
           ||JudgeHelpUtil.isContainChinese(password)){ //判断长度和包含中字
            tipInfo=PASSWORD_INVAILD_LENGTH;
        }
        return  tipInfo;
    }

    /**
     * 检验两次密码是否一致
     * @param password 密码
     * @param password2 确认密码
     * @return 提示字符串
     */
    public static String passwordIsconsistent(String password,String password2){
        String tipInfo =PASSWORD_INPUT_CONSISTENT;// 记录错误信息
        if(!password.equals(password2)){
            tipInfo=PASSWORD_INPUT_INCONSISTENT;
        }
        return  tipInfo;
    }
    /**
     * 检验性别是否符合要求
     * @param sex 输入的性别字符
     * @return 提示字符串
     * @deprecated  该方法未被调用 废弃
     */
    public static String sexValidate(String sex){
        String tipInfo=SEX_AVAILABLE;
        if(!sex.equals(Actual_User.MAN)&&!sex.equals(Actual_User.WOMAN)){
            tipInfo=SEX_INVAILD;
        }
            return  tipInfo;
    }

    /**
     *确认手机号码是否符合要求
     * @param  number 手机号码
     * @return 提示字符串
     */
    public static String phoneNumberValidate(String number) {
        String tipInfo=PHONE_AVAILABLE;
            String regex = "(1[0-9][0-9]|15[0-9]|18[0-9])\\d{8}";
            Pattern p = Pattern.compile(regex);
           if (!p.matches(regex, number)){  //如果不是号码，则返回false，是号码则返回true
              tipInfo=PHONE_INVAILD;
           }
        return tipInfo;
    }

    /**
     * <br>引用类：{@link }<br/>
     * 验证邮箱是否有效
     * @param email 输入邮箱
     * @return 提示字符串
     */
    public static String emailValidate(String email) {
        String tipInfo=EMAIL_AVAILABLE;
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher matcher = pattern.matcher(email);
        if(!matcher.matches()){
            tipInfo=EMAIL_INVAILD;
        }
        return  tipInfo;
    }

    /**
     * <br>引用类：{@link }<br/>
     * 判断年龄是否有效
     * @param age 输入年龄
     * @return 提示字符串
     */
    public static String ageValidate(int age) {
        String tipInfo=AGE_AVAILABLE;
        if(age<0&&age>120){
            tipInfo=AGE_INVAILD;
        }
        return  tipInfo;
    }
    public static void main(String[] args) {
        String username="1192698984";
        String password="asd8872749";
        try {
            System.out.println(usernameValidate(username));
            System.out.println(usernameValidate(password));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        }
}
