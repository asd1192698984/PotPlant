package com.example.mypotplant.maintenance.dataCentre.bean;

import com.example.mypotplant.R;

import androidx.annotation.NonNull;

/**
 * Created by MXL on 2020/9/21
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class Operation {
    public final static  String[] types=new String[]{"补光","浇水"};
    public final static  int[] draws=new int[]{R.drawable.tab_flower_light,R.drawable.tab_water};
    public final static  int OPERATION_TYPE_ADD_LIGHT_INDEX=0;
    public final static  int OPERATION_TYPE_WATER_INDEX=1;
    public final static  String TIME_FORMAT="yyyy-MM-dd HH:mm:ss";
    public  final  static long WATER_WAIT_TIME=10*1000; //10s
    private String filename;
    private String crType;
    private String crStartTime;
    private  String crEndTime;
    private  String name;
    private int type;

    public Operation(String filename, String crType, String name) {
        this.filename = filename;
        setCrType(crType);
        this.name = name;
    }

    public Operation(String filename, String crType, String crStartTime, String crEndTime, String name) {
        this.filename = filename;
        setCrType(crType);
        this.crStartTime = crStartTime;
        this.crEndTime = crEndTime;
        this.name = name;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getCrType() {
        return crType;
    }

    public void setCrType(String crType) {
        this.crType = crType;
        if(crType.equals(types[OPERATION_TYPE_ADD_LIGHT_INDEX])){
            type=OPERATION_TYPE_ADD_LIGHT_INDEX;
        }else if(crType.equals(types[OPERATION_TYPE_WATER_INDEX])) {
            type = OPERATION_TYPE_WATER_INDEX;
        }
    }

    public String getCrStartTime() {
        return crStartTime;
    }

    public void setCrStartTime(String crStartTime) {
        this.crStartTime = crStartTime;
    }

    public String getCrEndTime() {
        return crEndTime;
    }

    public void setCrEndTime(String crEndTime) {
        this.crEndTime = crEndTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public int getdrawId(){
        return  draws[type];
    }
    @NonNull
    @Override
    public String toString() {
        return "于"+getCrStartTime()+"对"+getName()+"进行"+getCrType();
    }
}
