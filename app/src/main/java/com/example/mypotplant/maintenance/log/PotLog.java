package com.example.mypotplant.maintenance.log;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by MXL on 2020/7/12
 * <br>类描述：日志类 唯一对应植株的一条日志
 *  javabean<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class PotLog implements Serializable {
    int id;  //编号
    Calendar issuedate ;//发布时间
    String image; //所附图片  图片的全路径
    String word; //个人描述
    String filename; //对应植株的主键
    String time;      //对应格式的时间  Add by MXL 2020/7/26 直接增加时间字符串属性
    String url;      //图片的url
    PotLog(){

    }
    PotLog(Calendar calendar,String image,String word,String filename){
        setIssuedate(calendar);
        this.image=image;
        this.word=word;
        this.filename=filename;
    }
    PotLog(Calendar calendar,String word,String filename){
        this(calendar,null,word,filename);
    }
    public Calendar getIssuedate() {
        return issuedate;
    }

    public void setIssuedate(Calendar issuedate) {
        this.issuedate = issuedate;
        this.time=getTime(issuedate);
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public static String getTime(Calendar calendar){
        int year = calendar.get(Calendar.YEAR);
        //比当前月份少1
        int month = calendar.get(Calendar.MONTH)+1;
        //date表示日期，day表示天数，所以date与day_of_month相同
        int date = calendar.get(Calendar.DATE);
        return  year+"."+month+"."+date;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
