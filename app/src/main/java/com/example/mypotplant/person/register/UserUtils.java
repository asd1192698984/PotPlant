package com.example.mypotplant.person.register;

/**
 * Created by MXL on 2020/1/20
 * <br>类描述：完成类转化<br/>
 *
 * @version 2.0
 * @since 1.0
 */
public class UserUtils {
    /**
     * 完成ActualUserToUser
     * 去掉多余数据转化 Add by MXL 2020-7-5
     * @param Auser
     * @return
     */
    public static User ActualUserToUser(Actual_User Auser){
        /*
         * 只保留 用户名 密码 手机号
         */
         User user=new User();
//         user.setUser_email(Auser.getUser_email());
         user.setUser_name(Auser.getUser_name());
         user.setUser_password(Auser.getUser_password());
         user.setUser_phone_number(Auser.getUser_phone_number());
//         if(JudgeHelpUtil.isInteger(Auser.getUser_age())){
//             try {
//               user.setUser_age(Integer.parseInt(Auser.getUser_age()));
//             } catch (NumberFormatException e) {
//                 e.printStackTrace();
//             }
//         }
//         if (Auser.getUser_sex().equals(Actual_User.MAN)){
//             user.setUser_sex(0);
//         }
//         else if(Auser.getUser_sex().equals(Actual_User.WOMAN)){
//             user.setUser_sex(1);
//         }
        return user;
    }
}
