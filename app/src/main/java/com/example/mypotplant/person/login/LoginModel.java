package com.example.mypotplant.person.login;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.mypotplant.MApplication;
import com.example.mypotplant.http.BaseCallback;
import com.example.mypotplant.http.OkHttpHelper;
import com.example.mypotplant.person.register.User;
import com.google.gson.Gson;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by MXL on 2019/12/12
 * <br>类描述 ：该类对登录所用数据进行管理br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class LoginModel implements ILoginModel {
    private static  boolean ISLogin=false;
    public static String TAG="LoginModel";
    private static User mUser=new User();
    private static UserSetting setting=new UserSetting();

    /**
     *  从服务器得到登录允许
     * @param  username 输入用户名
     * @param  password 输入密码
     * @param  callback 登录回调接口
     * @return  是否允许登录
     */
    @Override
    public boolean getLoginAccess(String username,String password,BaseCallback callback) {
        OkHttpHelper mHelp=OkHttpHelper.getinstance();
        Map<String,String> m=new LinkedHashMap<>();
        m.put("user_id",username);
        m.put("user_password",password);
        Gson gson =new Gson();
        String jsStr=gson.toJson(m);
        Log.d("RegisterModel", "getLoginAccess: "+jsStr);
        mHelp.post(OkHttpHelper.URL_BASE + OkHttpHelper.URL_LOGIN, jsStr, callback,"result");
        return true;
    }

    @Override
    public void savePassword(String username, String password) {
       SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(MApplication.getContext()).edit();
       editor.putString("username",username);
       editor.putString("password",password);
       editor.putBoolean("issave",true);
       editor.apply();
    }

    @Override
    public void removePassword() {
        SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(MApplication.getContext()).edit();
        editor.remove("username");
        editor.remove("password");
        editor.putBoolean("issave",false);
    }


    @Override
    public void recoverPassword(TextView username, TextView password, CheckBox checkBox) {
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(MApplication.getContext());
        if(preferences.getBoolean("issave",false)){
            username.setText(preferences.getString("username", "0"));
            password.setText(preferences.getString("password", "0"));
            checkBox.setChecked(true);
        }else{
            checkBox.setChecked(false);
        }
    }

//    @Override
//    public boolean getSettingMsg() {
//        OkHttpHelper helper=OkHttpHelper.getinstance();
//        Map data = new HashMap();
//        data.put("user_id",mUser.getUser_phone_number());
//        helper.post(OkHttpHelper.URL_BASE + OkHttpHelper.URL_GET_USER_SETTING, new Gson().toJson(data), new BaseCallback<String>() {
//            @Override
//            public void onRequestBefore() {
//
//            }
//
//            @Override
//            public void onFailure(Request request, Exception e) {
//
//            }
//
//            @Override
//            public void onSuccess(Response response, String s) {
//                JSONObject jsonObject=null;
//                Log.d(TAG, "onSuccess: "+s);
//                try {
//                     jsonObject=new JSONObject(s);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                if(jsonObject!=null) {
//                    try {
//                        LoginModel.getSetting().setMaintenance_reminder(jsonObject.getInt("maintenance_reminder"));
//                        LoginModel.getSetting().setWeather_alert(jsonObject.getInt("weather_alert"));
//                        LoginModel.getSetting().setReminder_interval(jsonObject.getInt("reminder_interval"));
//                        LoginModel.getSetting().setPre_time(jsonObject.getInt("remind_interval_start"));
//                        LoginModel.getSetting().setLast_time(jsonObject.getInt("remind_interval_end"));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onError(Response response, int errorCode, Exception e) {
//
//            }
//        },"result");
//        return true;
//    }

    public static void setIsLogin(boolean isLogin) {
        LoginModel.ISLogin = isLogin;
    }
    public static boolean getISLogin(){
        return  ISLogin;
    }
    public static User getUser(){
        return mUser;
    }

    public static UserSetting getSetting() {
        return setting;
    }
}
