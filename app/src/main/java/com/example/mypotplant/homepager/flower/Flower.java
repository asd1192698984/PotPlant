package com.example.mypotplant.homepager.flower;

import java.io.Serializable;

/**
 * Created by MXL on 2020/2/29
 * <br>类描述：花百科<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class Flower implements Serializable,Comparable<Flower>{
    private   int imaginID;            //花的图片资源ID
    private  int iconID;             //花朵的小图ID
    private  String url;            //图片资源
    private   String name;             //花名
    private String light_condition;  //光照条件
    private  String suit_humidity;    //适宜湿度
    private   String ph_value;         //ph值
    private  String suit_temp;        //适宜温度
    private   String no_suit_temp;    //不适温度
    private   String opening_word;     //开篇文字
    private   String light_request;  //光照需求
    private   int[] fertilization_times; //施肥次数
    private   int[] spray_times;     //喷雾次数
    private  String[] other_oper;  //其他操作
    private  String recom_fertilizer;  //推荐肥料
    private   String  recom_soil;       //推荐土壤
    private String  recom_medic;      //推荐药剂
    private  int code;               //索引代号 唯一 便于查找
    private  Character fname;       //首字母

    public int getIconID() {
        return iconID;
    }

    public void setIconID(int iconID) {
        this.iconID = iconID;
    }

    public String getName() {
        return name;
    }

    public int[] getFertilization_times() {
        return fertilization_times;
    }

    public int getImaginID() {
        return imaginID;
    }

    public String getLight_condition() {
        return light_condition;
    }

    public int[] getSpray_times() {
        return spray_times;
    }

    public String getLight_request() {
        return light_request;
    }

    public String getNo_suit_temp() {
        return no_suit_temp;
    }

    public String getOpening_word() {
        return opening_word;
    }

    public String getPh_value() {
        return ph_value;
    }

    public String getSuit_humidity() {
        return suit_humidity;
    }

    public String[] getOther_oper() {
        return other_oper;
    }

    public String getSuit_temp() {
        return suit_temp;
    }

    public String getRecom_fertilizer() {
        return recom_fertilizer;
    }

    public String getRecom_medic() {
        return recom_medic;
    }

    public String getRecom_soil() {
        return recom_soil;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFertilization_times(int[] fertilization_times) {
        this.fertilization_times = fertilization_times;
    }

    public void setImaginID(int imaginID) {
        this.imaginID = imaginID;
    }

    public void setLight_condition(String light_condition) {
        this.light_condition = light_condition;
    }

    public void setLight_request(String light_request) {
        this.light_request = light_request;
    }

    public void setNo_suit_temp(String no_suit_temp) {
        this.no_suit_temp = no_suit_temp;
    }

    public void setOpening_word(String opening_word) {
        this.opening_word = opening_word;
    }

    public void setPh_value(String ph_value) {
        this.ph_value = ph_value;
    }

    public void setSpray_times(int[] spray_times) {
        this.spray_times = spray_times;
    }

    public void setRecom_fertilizer(String recom_fertilizer) {
        this.recom_fertilizer = recom_fertilizer;
    }

    public void setOther_oper(String[] other_oper) {
        this.other_oper = other_oper;
    }

    public void setRecom_medic(String recom_medic) {
        this.recom_medic = recom_medic;
    }

    public void setSuit_humidity(String suit_humidity) {
        this.suit_humidity = suit_humidity;
    }

    public void setRecom_soil(String recom_soil) {
        this.recom_soil = recom_soil;
    }

    public void setSuit_temp(String suit_temp) {
        this.suit_temp = suit_temp;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Character getFname() {
        return fname;
    }

    public void setFname(Character fname) {
        this.fname = fname;
    }
    @Override
    public int compareTo(Flower o) {
        if(o.fname>=this.fname){
            return 1;
        }else {
            return -1;
        }
    }
}
