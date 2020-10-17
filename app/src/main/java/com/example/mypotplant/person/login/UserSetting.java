package com.example.mypotplant.person.login;

/**
 * Created by MXL on 2020/8/27
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class UserSetting {
    private  String userid;
    private int maintenance_reminder ;//是否开启养护提醒 0 FALSE
    private  int reminder_interval; //时间间隔数组中坐标 单位 分钟
    private int  weather_alert;//是否开启天气提醒  0 FALSE
    private int pre_time;          //提醒的前半段时间
    private int last_time;          //提醒的后半段时间
    UserSetting (){
        pre_time=SettingConStants.Reminder_TIME_HOUR.get(0);
        last_time=SettingConStants.Reminder_TIME_HOUR.get(0);
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getMaintenance_reminder() {
        return maintenance_reminder;
    }

    public void setMaintenance_reminder(int maintenance_reminder) {
        this.maintenance_reminder = maintenance_reminder;
    }

    public int getReminder_interval() {
        return reminder_interval;
    }

    public void setReminder_interval(int reminder_interval) {
        this.reminder_interval = reminder_interval;
    }

    public int getWeather_alert() {
        return weather_alert;
    }

    public void setWeather_alert(int weather_alert) {
        this.weather_alert = weather_alert;
    }

    public int getPre_time() {
        return pre_time;
    }

    public void setPre_time(int pre_time) {
        this.pre_time = pre_time;
    }

    public int getLast_time() {
        return last_time;
    }

    public void setLast_time(int last_time) {
        this.last_time = last_time;
    }
}
