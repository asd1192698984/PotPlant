package com.example.mypotplant.person.register;

import android.widget.Toast;

import com.example.mypotplant.Exception.Converter;
import com.example.mypotplant.Judgehelps.AccountJudgeHelp;
import com.example.mypotplant.Judgehelps.IdNumJudgeHelp;
import com.example.mypotplant.MApplication;

import java.text.ParseException;

/**
 * Created by MXL on 2019/12/11
 * <br>类描述：支持<b>LoginModel</b>的数据验证<br/>
 *需要验证的参数 用户名 密码 性别 年龄 手机号码 电子邮箱
 * @version 1.0
 * @since 1.0
 */
public class RegisterModelHelp {

    /**
     * @param idnum 用户输入身份证号码
     * @return 验证未通过的提示字符串
     * <br>支持类:{@linkplain com.example.myapplication.Judgehelps.IdNumJudgeHelp}<br/>
     */
    String judgeIdNum(String idnum){
        String tipInfo="";
        try {
            tipInfo = IdNumJudgeHelp.IDCardValidate(idnum);
        }
        catch (ParseException e){
            Toast.makeText(MApplication.getContext(), Converter.getErrorString(MApplication.getContext(), "E10000"),Toast.LENGTH_SHORT).show();
        }
        return tipInfo;
   }

    /**
     * @param username 用户名
     * @return 用户名判断提示字符串
     * <br>支持类:{@linkplain AccountJudgeHelp}<br/>
     */
    String judgeUsername(String username){
        String tipInfo="";
        try {
            tipInfo = AccountJudgeHelp.usernameValidate(username);
        }
        catch (ParseException e){
            Toast.makeText(MApplication.getContext(), Converter.getErrorString(MApplication.getContext(), "E10001"),Toast.LENGTH_SHORT).show();
        }
        return  tipInfo;
    }
    /**
     * @param password 密码
     * @return 密码判断提示字符串
     * <br>支持类:{@linkplain AccountJudgeHelp}<br/>
     */
    String judgePassword (String password){
        String tipInfo="";
        try {
            tipInfo = AccountJudgeHelp.passwordValidate(password);
        }
        catch (ParseException e){
            Toast.makeText(MApplication.getContext(), Converter.getErrorString(MApplication.getContext(), "E10002"),Toast.LENGTH_SHORT).show();
        }
        return  tipInfo;
    }
    /**
     * @param sex 性别
     * @return 性别判断提示字符串
     * <br>支持类:{@linkplain AccountJudgeHelp}<br/>
     */
    String judgeSex (String sex){
        String tipInfo="";
            tipInfo = AccountJudgeHelp.sexValidate(sex);
        return  tipInfo;
    }
    /**
     * @param number 手机号码
     * @return 手机号码判断提示字符串
     * <br>支持类:{@linkplain AccountJudgeHelp}<br/>
     */
    String judgeNumber (String number){
        String tipInfo="";
        tipInfo = AccountJudgeHelp.phoneNumberValidate(number);
        return  tipInfo;
    }
    /**
     * @param email 邮箱
     * @return 邮箱判断提示字符串
     * <br>支持类:{@linkplain AccountJudgeHelp}<br/>
     */
    String judgeEmail (String email){
        String tipInfo="";
        tipInfo = AccountJudgeHelp.emailValidate(email);
        return  tipInfo;
    }
    /**
     * @param age 年龄
     * @return 年龄判断提示字符串
     * <br>支持类:{@linkplain AccountJudgeHelp}<br/>
     */
    String  judgeAge (int age){
        String tipInfo="";
        tipInfo = AccountJudgeHelp.ageValidate(age);
        return  tipInfo;
    }

    /**
     *  检验两次密码是否一致
     *  <br>支持类:{@linkplain AccountJudgeHelp}<br/>
     * @param password 密码
     * @param password2 确认密码
     * @return 提示字符串
     */
    String judgePassword (String password,String password2){
        String tipInfo="";
            tipInfo = AccountJudgeHelp.passwordIsconsistent(password,password2);
        return  tipInfo;
    }
}
