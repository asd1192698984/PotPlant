package com.example.mypotplant.maintenance;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

import androidx.annotation.NonNull;

/**
 * Created by MXL on 2020/1/14
 * <br>类描述：拍照对应的植物类<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class Plant  extends DataSupport  implements Serializable {
    String path;
    String filename;  //唯一 可作为主
    String name;      //植物昵称
    String type;     //植物类别
    int is_intelligent;  //是否智能养护 0智能提醒 1自动养护 2无
    String url;    //植物url路径
    int pot_id;   //如果是自动养护 对应的栏位
    public  final static  String planturl="http://121.36.26.175:8080/image/smart_pot_img/";

    Plant(){
        name="暂无昵称";
        is_intelligent=0;
        type="无";
    }
    Plant(String path,String filename){
        this.filename=filename;
        this.path=path;
        name="暂无昵称";
        is_intelligent=0;
        type="无";
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIs_intelligent() {
        return is_intelligent;
    }

    public void setIs_intelligent(int is_intelligent) {
        this.is_intelligent = is_intelligent;
    }

    @NonNull
    @Override
    public String toString() {
        return name+" "+filename+" ";
    }
}
