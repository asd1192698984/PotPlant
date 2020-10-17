package com.example.mypotplant.person.register;


import android.util.Log;

import com.example.mypotplant.Judgehelps.AccountJudgeHelp;
import com.example.mypotplant.http.BaseCallback;
import com.example.mypotplant.http.OkHttpHelper;
import com.google.gson.Gson;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by MXL on 2019/12/13
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class RegisterModel implements IRegisterModel {
    private RegisterModelHelp mHelp=new RegisterModelHelp();
    private String tip_info=REGISTER_SUCCESS;
    /**
     * <br>引用类：{@link }<br/>
     *
     * @param  user 接受用户输入信息
     * @return true 注册成功 false 注册失败
     */
    @Override
    public boolean isPass(Actual_User user) {
        //判断用户名
        if((tip_info=mHelp.judgeUsername(user.getUser_name()))!= AccountJudgeHelp.USERNAME_AVAILABLE){
            return false;
        }
        //判断密码
        if((tip_info=mHelp.judgePassword(user.getUser_password()))!= AccountJudgeHelp.PASSWORD_AVAILABLE){
            return false;
        }
        //判断重复密码 Add by MXL 2020-7-5
        if((tip_info=mHelp.judgePassword(user.getUser_password(),user.getUser_password_again()))!= AccountJudgeHelp.PASSWORD_INPUT_CONSISTENT){
            return false;
        }
        /**
         * 去掉性别 邮箱 手机号码 年龄验证 Add by MXL 2020-7-5
         */

        //判断性别
//        if((tip_info=mHelp.judgeSex(user.getUser_sex()))!= AccountJudgeHelp.SEX_AVAILABLE){
//            return false;
//        }
        //判断手机号码
//        if((tip_info=mHelp.judgeNumber(user.getUser_phone_number()))!= AccountJudgeHelp.PHONE_AVAILABLE){
//            return false;
//        }
        //判断邮箱
//        if((tip_info=mHelp.judgeEmail(user.getUser_email()))!= AccountJudgeHelp.EMAIL_AVAILABLE){
//            return false;
//        }
        //判断年龄
//        int a=19;
//        if(!JudgeHelpUtil.isInteger(user.getUser_age())){
//            tip_info=AccountJudgeHelp.AGE_NOINT_ERROR;
//            return false;
//        }else{
//            try {
//                 a = Integer.parseInt(user.getUser_age());
//            } catch (NumberFormatException e) {
//                e.printStackTrace();
//                tip_info=AccountJudgeHelp.AGE_NOINT_ERROR;
//            }
//        }
//        if((tip_info=mHelp.judgeAge(a))!= AccountJudgeHelp.AGE_AVAILABLE){
//            return false;
//        }else{
//
//        }
        tip_info=REGISTER_SUCCESS;
        return true;
    }
    public String getTip_info() {
        return tip_info;
    }

    /**
     * <br>引用类：{@link }<br/>
     * 将数据传给服务端
     * 修改：数据改为用户名 密码 手机号 Add by MXL 2020-7-5
     * @param  user 注册验证后的User类
     * @return 注册是否成功 1用户名重复 10电话重复
     */
    @Override
    public int putdata(User user,BaseCallback callback,String key) {
        Gson gson=new Gson();
        final int[] result = new int [1];
        //获取实例
        OkHttpHelper mHelp=OkHttpHelper.getinstance();
        /*
         *  version 1.0
         */
//        String jsonStr=gson.toJson(user);
//        mHelp.post(OkHttpHelper.URL_BASE+OkHttpHelper.URL_REGISTER,jsonStr,callback,key);
        Map<String,String> data =new LinkedHashMap<>();
        data.put("user_id",user.getUser_phone_number());
        data.put("user_name",user.getUser_name());
        data.put("user_password",user.getUser_password());
        String jsStr=gson.toJson(data);
        Log.d("RegisterModel", "getLoginAccess: "+jsStr);
        mHelp.post(OkHttpHelper.URL_BASE+OkHttpHelper.URL_REGISTER,jsStr,callback,key);
        return result[0];
    }
}
