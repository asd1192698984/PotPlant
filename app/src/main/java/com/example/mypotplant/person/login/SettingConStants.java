package com.example.mypotplant.person.login;

import java.util.ArrayList;

/**
 * Created by MXL on 2020/8/27
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class SettingConStants {
    private  SettingConStants(){};
    public  static ArrayList<Integer> Reminder_interval;
    public  static ArrayList<Integer> Reminder_TIME_HOUR;
    static {
        Reminder_interval=new ArrayList<>();
        Reminder_interval.add(15);
        Reminder_interval.add(30);
        Reminder_interval.add(45);
        Reminder_interval.add(60);
        Reminder_interval.add(120);
        Reminder_interval.add(240);
        Reminder_TIME_HOUR=new ArrayList<>();
        for(int i=0;i<24;i++)
        Reminder_TIME_HOUR.add(i);
    }
}
