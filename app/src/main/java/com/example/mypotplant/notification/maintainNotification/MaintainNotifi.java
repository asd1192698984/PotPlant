package com.example.mypotplant.notification.maintainNotification;

import com.example.mypotplant.R;

import androidx.annotation.NonNull;

/**
 * Created by MXL on 2020/3/28
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class MaintainNotifi {
    private int type=-1; //通知类类型
    private String flower_name;
    private int image_res;         //左边图标
    private  static  int TAB_WATER= R.drawable.tab_water;
    private  static  int TAB_CUT= R.drawable.tab_cut;
    private  static  int  TAB_FERTILIZER;
    MaintainNotifi(){

    }
    MaintainNotifi(MaintainNotifiConstants.MaintainNotifi_Type type, String flower_name){
        this.type=type.ordinal();
        this.flower_name=flower_name;
        switch ( type) {
            case  WATER:
               image_res=TAB_WATER;
               break;
            case CUT:
                image_res=TAB_CUT;
               break;
            case FERTILIZER:
                image_res=TAB_FERTILIZER;
              break;
            default:
               break;
        }
    }
    @NonNull
    @Override
    public String toString() {
        if(type!=-1) {
            final MaintainNotifiConstants.MaintainNotifi_Type mtype= MaintainNotifiConstants.MaintainNotifi_Type.values()[type];
            switch ( mtype) {
                case  WATER:
                    return "您的"+flower_name+"应该浇水了";
                case CUT:
                    return "您的"+flower_name+"应该修剪了";
                case FERTILIZER:
                    return "您的"+flower_name+"应该施肥了";
                    default:
                        return super.toString();
            }
        }
        else
        return super.toString();
    }

    public int getImage_res() {
        return  image_res;
    }

    public void setImage_res(int image_res) {
        this.image_res = image_res;
    }

    public String getFlower_name() {
        return flower_name;
    }

    public void setFlower_name(String flower_name) {
        this.flower_name = flower_name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
