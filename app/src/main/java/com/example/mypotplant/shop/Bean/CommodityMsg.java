package com.example.mypotplant.shop.Bean;

/**
 * Created by MXL on 2020/5/16
 * 商品类<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class CommodityMsg {
//    private byte[] img; //商品图片
    private int imgID;//商品图片
    private String name;//商品名称
    private double price; //单价
    private  String linkUrl; //商品地址

//    private String desc ;//商品描述
//    private  int sellcount;//销售数
//    private int praise_count;//好评
//    private int hate_count; //差评
//    private int supplierID;//供应商id

   public CommodityMsg(String name, double price,String url){
        this.name=name;
        this.price=price;
        this.linkUrl=url;
//        this.desc=desc;
//        this.supplierID=supplierID;
    }


//    public byte[] getImg() {
//        return img;
//    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

//    public String getDesc() {
//        return desc;
//    }
//
//    public int getSupplierID() {
//        return supplierID;
//    }

//    public void setImg(byte[] img) {
//        this.img = img;
//    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getImgID() {
        return imgID;
    }

    public void setImgID(int imgID) {
        this.imgID = imgID;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }
    //    public void setDesc(String desc) {
//        this.desc = desc;
//    }
//
//    public void setSupplierID(int supplierID) {
//        this.supplierID = supplierID;
//    }
//
//    public int getHate_count() {
//        return hate_count;
//    }
//
//    public int getPraise_count() {
//        return praise_count;
//    }
//
//    public void setHate_count(int hate_count) {
//        this.hate_count = hate_count;
//    }
//
//    public void setPraise_count(int praise_count) {
//        this.praise_count = praise_count;
//    }
}
