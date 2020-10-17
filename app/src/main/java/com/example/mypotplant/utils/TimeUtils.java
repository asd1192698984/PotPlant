package com.example.mypotplant.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by MXL on 2020/7/16
 * <br>类描述：处理时间的工具类<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class TimeUtils {
    public  static String getTimeFormat(Date date,String format){
        SimpleDateFormat df = new SimpleDateFormat(format);//设置日期格式
        return  df.format(date);// new Date()为获取当前系统时间
    }
    public  static String getTimeFormat(Calendar calendar, String format){
        SimpleDateFormat df = new SimpleDateFormat(format);//设置日期格式
        return  df.format(calendar);// new Date()为获取当前系统时间
    }
    public  static String getmilltoHMS(long mills){
        int h=0,m=0,s=0;
        h=(int)mills/1000/3600;
        m=(int)mills/1000/60%60;
        s=(int)mills/1000%60;
        return h+"小时"+m+"分"+s+"秒";
    }
}
