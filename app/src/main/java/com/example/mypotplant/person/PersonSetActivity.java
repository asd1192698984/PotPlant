package com.example.mypotplant.person;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mypotplant.BaseActivity;
import com.example.mypotplant.R;
import com.example.mypotplant.http.BaseCallback;
import com.example.mypotplant.http.OkHttpHelper;
import com.example.mypotplant.person.login.LoginModel;
import com.example.mypotplant.person.login.SettingConStants;
import com.example.mypotplant.person.login.UserSetting;
import com.example.mypotplant.utils.NetWorkUtils;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.suke.widget.SwitchButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PersonSetActivity extends BaseActivity {

    public static String TAG="PersonSetActivity";
    SwitchButton btn_maintain;
    SwitchButton btn_weather;
    Spinner spinner_notifi;
    Spinner spinner_interval;
    Spinner spinner_pre_time;
    Spinner spinner_last_time;
    UserSetting setting;
    public  static  void actionStart(Context context){
        Intent intent=new Intent(context,PersonSetActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_set);
        Log.d(TAG, "onCreate: ");
        initdata();
        initview();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    public  void initview(){
        btn_maintain=(SwitchButton)findViewById(R.id.btn_maintain);
        btn_weather=findViewById(R.id.btn_weather);
        btn_maintain.setChecked(false);
        btn_weather.setChecked(false);
        spinner_notifi=findViewById(R.id.spinner_notifi);
        spinner_interval=findViewById(R.id.spinner_interval);
        spinner_pre_time=findViewById(R.id.spinner_pre_time);
        spinner_last_time=findViewById(R.id.spinner_next_time);
        //适配器
        ArrayAdapter<Integer> adapter= new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, SettingConStants.Reminder_interval);
        //设置样式
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner_interval.setAdapter(adapter);
        adapter= new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, SettingConStants.Reminder_TIME_HOUR);
        //设置样式
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_pre_time.setAdapter(adapter);
        adapter= new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, SettingConStants.Reminder_TIME_HOUR);
        //设置样式
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_last_time.setAdapter(adapter);
    }
    public  void initdata(){
        if(LoginModel.getISLogin()) {
            getSetting();
        }
        else {
            Toast.makeText(this,"未登录，修改的设置将不会生效",Toast.LENGTH_LONG).show();
        }
    }
    private  void updataSetting(){
        /**
         * 判断登录
         * 判断设置信息
         * 更新设置
         */
       if(LoginModel.getISLogin()){
           setting=LoginModel.getSetting();
           if(setting!=null){
               btn_weather.setChecked(setting.getWeather_alert()!=0);
               btn_maintain.setChecked(setting.getMaintenance_reminder()!=0);
               spinner_interval.setSelection(setting.getReminder_interval());
               spinner_pre_time.setSelection(setting.getPre_time());
               spinner_last_time.setSelection(setting.getLast_time());
           }
       }
    }
    /**
     * 这里保存数据
     */
    @Override
    protected void onStop() {
        super.onStop();
        if(LoginModel.getISLogin())
        uploadSetting();
        Log.d(TAG, "onStop: ");
    }

    /**
     * 得到用户设置信息
     * 不需判断登录
     */
    private  void getSetting(){
        OkHttpHelper helper=OkHttpHelper.getinstance();
        Map data = new HashMap();
        data.put("user_id",LoginModel.getUser().getUser_phone_number());
        Log.d(TAG, "getSetting: "+new Gson().toJson(data));
        if(NetWorkUtils.networkAvailable(this)) {
            if(LoginModel.getISLogin()) {
                helper.post(OkHttpHelper.URL_BASE + OkHttpHelper.URL_GET_USER_SETTING, new Gson().toJson(data), new BaseCallback<String>() {
                    @Override
                    public void onRequestBefore() {

                    }

                    @Override
                    public void onFailure(Request request, Exception e) {
                        Log.d(TAG, "onFailure: get setting");
                    }

                    @Override
                    public void onSuccess(Response response, String s) {
                        JSONObject jsonObject = null;
                        Log.d(TAG, "onSuccess: " + s);
                        try {
                            jsonObject = new JSONObject(s);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (jsonObject != null) {
                            try {
                                UserSetting setting = LoginModel.getSetting();
                                setting.setMaintenance_reminder(jsonObject.getInt("maintenance_reminder"));
                                setting.setWeather_alert(jsonObject.getInt("weather_alert"));
                                setting.setReminder_interval(jsonObject.getInt("reminder_interval"));
                                setting.setPre_time(jsonObject.getInt("reminder_interval_start"));
                                setting.setLast_time(jsonObject.getInt("reminder_interval_end"));
                                updataSetting();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Response response, int errorCode, Exception e) {
                        Log.d(TAG, "onError: get setting" + errorCode);
                    }
                });
            }else {

            }
        }else {
            Toast.makeText(this,"获取设置信息失败，请检查网络设置",Toast.LENGTH_SHORT).show();
        }
    }
    private void  uploadSetting(){
        /**
         * 检查联网
         * 检查登录
         */
        if(NetWorkUtils.networkAvailable(this)){
           if(LoginModel.getISLogin()) {
              OkHttpHelper helper=OkHttpHelper.getinstance();
              Map data=new HashMap();
              data.put("user_id",LoginModel.getUser().getUser_phone_number());
              data.put("maintenance_reminder",btn_weather.isChecked()?1:0);
              data.put("weather_alert",btn_weather.isChecked()?1:0);
              data.put("reminder_interval",spinner_interval.getSelectedItemPosition());
              data.put("reminder_interval_start",spinner_pre_time.getSelectedItemPosition());
              data.put("reminder_interval_end",spinner_last_time.getSelectedItemPosition());
               Log.d(TAG, "uploadSetting: "+new Gson().toJson(data));
              helper.post(OkHttpHelper.URL_BASE + OkHttpHelper.URL_UPDATE_SET, new Gson().toJson(data), new BaseCallback<Integer>() {
                  @Override
                  public void onRequestBefore() {

                  }

                  @Override
                  public void onFailure(Request request, Exception e) {
                      Log.d(TAG, "onFailure: upload ");
                  }

                  @Override
                  public void onSuccess(Response response,Integer integer) {
                      Log.d(TAG, "onSuccess: "+integer);
                  }

                  @Override
                  public void onError(Response response, int errorCode, Exception e) {
                      Log.d(TAG, "onError: upload"+errorCode);
                  }
              },"result");
           }else {
//               Toast.makeText(this,"请先登录再完成设置",Toast.LENGTH_SHORT).show();
           }
        }else {
            Toast.makeText(this,"保存失败，请检查网络设置",Toast.LENGTH_SHORT).show();
        }
    }
}
