package com.example.mypotplant.maintenance.dataCentre.bean;

/**
 * Created by MXL on 2020/9/16
 * <br>类描述：<br/>
 *  植物数据储存
 * @version 1.0
 * @since 1.0
 */
public class Plantdata {
    public final static  int DEFAULT_GET_SIZE=10; //其他默认一次获取记录条数
    public final static  int DEFAULT_WET_GET_SIZE=1; //湿度默认一次获取记录条数
    private String filename;
    private  double airHumidity; //空气湿度
    private  int light;        //光照强度
    private  double temp;      //温度
    private  int land_water1;       //土壤湿度1
    private  int land_water2;       //土壤湿度2
    private  int land_water3;       //土壤湿度3
    private  int land_water4;       //土壤湿度4
    private  int hour;              //每天的多少小时 0-23
    public String getFilename() {
        return filename;
    }

    public int getLand_water2() {
        return land_water2;
    }

    public void setLand_water2(int land_water2) {
        this.land_water2 = land_water2;
    }

    public int getLand_water3() {
        return land_water3;
    }

    public void setLand_water3(int land_water3) {
        this.land_water3 = land_water3;
    }

    public int getLand_water4() {
        return land_water4;
    }

    public void setLand_water4(int land_water4) {
        this.land_water4 = land_water4;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public double getAirHumidity() {
        return airHumidity;
    }

    public void setAirHumidity(double airHumidity) {
        this.airHumidity = airHumidity;
    }

    public int getLight() {
        return light;
    }

    public void setLight(int light) {
        this.light = light;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public int getLand_water1() {
        return land_water1;
    }

    public void setLand_water1(int land_water1) {
        this.land_water1 = land_water1;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    /**
     * date格式
     * yyyy-mm-dd hh:mm:ss
     * @param data
     * @return
     */
    public static int  ParseHour(String data){
        int hour=0;
        int r=data.indexOf(":");
        int l=r-2;
        String h=data.substring(l,r);
        return  Integer.valueOf(h);
    }
}
