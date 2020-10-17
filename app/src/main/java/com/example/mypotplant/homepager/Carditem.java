package com.example.mypotplant.homepager;

/**
 * Created by MXL on 2020/2/23
 * <br>类描述：卡片<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class Carditem {
    private int  imageViewID;
    private   String name;
    private int thumb_num;
    private int collect_num;
    Carditem(){

    }
    Carditem(int imageId){
        imageViewID=imageId;
    }
    Carditem(String name){
        this.name=name;
    }
    Carditem(int imageId,String name){
        this.imageViewID=imageId;
        this.name=name;
    }
    public void setImageViewID(int imageViewID) {
        this.imageViewID = imageViewID;
    }

    public int getImageViewID() {
        return imageViewID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCollect_num() {
        return collect_num;
    }

    public void setCollect_num(int collect_num) {
        this.collect_num = collect_num;
    }

    public int getThumb_num() {
        return thumb_num;
    }

    public void setThumb_num(int thumb_num) {
        this.thumb_num = thumb_num;
    }
}
