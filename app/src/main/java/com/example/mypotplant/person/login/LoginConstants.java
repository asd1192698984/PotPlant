package com.example.mypotplant.person.login;

/**
 * Created by MXL on 2020/3/8
 * <br>类描述：该类存放登录用到的常量<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public  final class LoginConstants {
   // private static Map<String,Integer> map;
    private  LoginConstants(){
    }
    //处理成功
    public  static final int LOGIN_SUCCESS=0;
    //id不存在
    public  static final int  LOGIN_INVALID_ID=1;
    //密码错误
    public  static final int  LOGIN_PASSWORD_ERROR=10;

    public  static final String LOGIN_SUCCESS_tips="登录成功";
    public  static final String LOGIN_INVALID_ID_tips="用户id不存在";
    public  static final String LOGIN_PASSWORD_ERROR_tips="密码错误";

//    /**
//     * 该类得到登录返回编码与数字映射关系<br>
//     * 便于进行switch操作
//     * @param
//     * @return  Map 映射关系
//     */
//    public  static  Map getLoginCodeMap(){
//       if(map!=null) return map;
//       else{
//           map=new HashMap<>();
//           map.put(LOGIN_SUCCESS,1);
//           map.put(LOGIN_PARA_ERROR,2);
//           map.put(LOGIN_DEALWITH_ERROR,3);
//           map.put(LOGIN_NULL_DATA,4);
//           map.put(LOGIN_UNKONWN_ERROR,5);
//           return  map;
//       }
//    }
}
