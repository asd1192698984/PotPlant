package com.example.mypotplant.maintenance.dataCentre;

import android.content.Context;
import android.util.Log;

import com.example.mypotplant.http.BaseCallback;
import com.example.mypotplant.http.OkHttpHelper;
import com.example.mypotplant.maintenance.MaintenanceModel;
import com.example.mypotplant.maintenance.Plant;
import com.example.mypotplant.maintenance.dataCentre.bean.Operation;
import com.example.mypotplant.maintenance.dataCentre.bean.Plantdata;
import com.example.mypotplant.person.login.LoginModel;
import com.example.mypotplant.utils.interfaces.DataInterface;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MXL on 2020/8/7
 * <br>类描述：<br/>
 * 已知的BUG：没有波动的数据不会显示
 * @version 1.0
 * @since 1.0
 */
public class DataCentreModel implements IDataCentreModel {
    private static final String TAG = "DataCentreModel";
    public  static  final  int RESULT_SIZE=4;
    public  static  final  int MAX_ONCE_RECORD_SIZE=10;
    public static  final String[] Water_Time=new String[24];  //补光时间
    public static  final int DEFAULT_HUMI=0;
    public static  final float[] DEFAULT_DATA_Y={10,20,30,40,30,20,10,10,50,10};
    public static  final int[] DEFAULT_DATA_X={2,4,6,10,12,14,16,18,20,22};
    public static volatile int control=0;
    private  List<Operation> operations;
    int[] humi=new int[MaintenanceModel.myPot.POT_SIZE];      //湿度数组
    int[][][] potX=new int[MaintenanceModel.myPot.POT_SIZE][][];      //光照，空气湿度，温度
    float[][][] potY=new float[MaintenanceModel.myPot.POT_SIZE][][];  //光照，空气湿度，温度
    float[][] dataY=new  float[DataCentreModel.RESULT_SIZE][]; //Y轴坐标 都相对统一
    private Plant[] plants;  //花盆中的植株数据
    private  Context context;
    private  OperationAdapt adapt;
    DataCentreModel (Context context){
        this.context=context;
        //Y轴坐标刻度
        for(int i=0;i<DataCentreModel.RESULT_SIZE-1;i++) {
            dataY[i]=new float[]{0f,50f,100f,150f,200f,255f};
        }
        dataY[DataCentreModel.RESULT_SIZE-1]=new float[]{-10,-5,0,5,10,15,20,25,30,35,40,45};
        plants= MaintenanceModel.getMyPot().getPlants();
    }

    static {
        for(int i=0;i<24;i++)
        Water_Time[i]=i+":00";
    }
    public float[][] getDataY() {
        return dataY;
    }

    @Override
    public void updatedate(LoadDataListener loadDataListener) {
        OkHttpHelper helper = OkHttpHelper.getinstance();

        //获取植株数据
        for(int i=0;i<MaintenanceModel.myPot.POT_SIZE;i++) {
            if(MaintenanceModel.myPot.getPot(i)==MaintenanceModel.myPot.POT_INAVAILABLE) { //如果有数据的话
                OkHttpHelper.MyUrlBuild build=new OkHttpHelper.MyUrlBuild(OkHttpHelper.URL_BASE2+OkHttpHelper.URL_GET_PLANTRECORDS);
                build.add("fileName",plants[i].getFilename());
                build.add("recordNum",MAX_ONCE_RECORD_SIZE);
                final  int i1=i;
                Log.d(TAG, "updatedate: url="+build.toString());
                helper.get(build.toString(), new BaseCallback<JSONArray>() {
                    @Override
                    public void onRequestBefore() {

                    }

                    @Override
                    public void onFailure(Request request, Exception e) {
                        Log.d(TAG, "onFailure: getdata");
                    }

                    @Override
                    public void onSuccess(Response response, JSONArray array) {
                        Log.d(TAG, "onSuccess: array="+array);
                        if(array==null||array.length()==0) {
                            humi[i1]=DEFAULT_HUMI;
                            potX[i1]=new int[RESULT_SIZE][];
                            potY[i1]=new float[RESULT_SIZE][];
                            for(int j=0;j<RESULT_SIZE;j++){
                                potX[i1][j]=DEFAULT_DATA_X;
                                potY[i1][j]=DEFAULT_DATA_Y;
                            }
                            return;
                        }
                        potX[i1]=new int[RESULT_SIZE][MAX_ONCE_RECORD_SIZE];
                        potY[i1]=new float[RESULT_SIZE][MAX_ONCE_RECORD_SIZE];
                       for(int j=0;j<array.length();j++){
                           try {
                               //1 光照 2空气湿度 3温度
                               JSONObject jsonObject=array.getJSONObject(j);
                               int h=Plantdata.ParseHour(jsonObject.getString("recordTime"));
                              // Log.d(TAG, "onSuccess: h="+h);
                               potX[i1][1][j]= h;
                               potX[i1][2][j]=h;
                               potX[i1][3][j]=h;
                               potY[i1][1][j]=Float.valueOf(jsonObject.getString("plantLight"));
                               potY[i1][2][j]=Float.valueOf(jsonObject.getString("airHumidity"));
                               potY[i1][3][j]=Float.valueOf(jsonObject.getString("plantTemperature"));
                               humi[i1]=Integer.valueOf(jsonObject.getString("landWater1"));
                           } catch (JSONException e) {
                               e.printStackTrace();
                           }
                       }
                    }

                    @Override
                    public void onError(Response response, int errorCode, Exception e) {
                        Log.d(TAG, "onError: getdata"+errorCode);
                    }
                },"plantRecords");
                //没有触发回调
                if(potX[i]==null||potY[i]==null) {
                    humi[i] = DEFAULT_HUMI;
                    potX[i] = new int[RESULT_SIZE][];
                    potY[i] = new float[RESULT_SIZE][];
                    for (int j = 0; j < RESULT_SIZE; j++) {
                        potX[i][j] = DEFAULT_DATA_X;
                        potY[i][j] = DEFAULT_DATA_Y;
                    }
                }
            }else {
              humi[i]=DEFAULT_HUMI;
                potX[i]=new int[RESULT_SIZE][];
                potY[i]=new float[RESULT_SIZE][];
              for(int j=0;j<RESULT_SIZE;j++){
                  potX[i][j]=DEFAULT_DATA_X;
                  potY[i][j]=DEFAULT_DATA_Y;
              }
            }
            control++;
            if(control==MaintenanceModel.myPot.POT_SIZE){
                loadDataListener.updatedata(humi,potX,potY);
                control=0;  //控制位清零
            }
        }
    }
    @Override
    public Object[] getCurData() {
        return new Object[]{humi,potX,potY,dataY};
    }


    @Override
    public List<Operation> getOperationInfos(final DataInterface<List<Operation>> listener,int index) {
         operations=new ArrayList<>();
        OkHttpHelper helper=OkHttpHelper.getinstance();
        Map map =new HashMap();
        String fileName=MaintenanceModel.myPot.getPlant(index).getFilename(); //获取文件名
        map.put("fileName",fileName);
        helper.get(OkHttpHelper.URL_BASE2 + OkHttpHelper.URL_GET_CURING_RECORDS, map, new BaseCallback<JSONArray>() {
            @Override
            public void onRequestBefore() {
                if(listener!=null)
                 listener.beforeLoad();
            }

            @Override
            public void onFailure(Request request, Exception e) {

            }

            @Override
            public void onSuccess(Response response, JSONArray array) {
                Log.d(TAG, "onSuccess: getOperations"+array);
                for(int j=0;j<array.length();j++) {
                    try {
                        JSONObject js = array.getJSONObject(j);
                        Operation operation =new Operation(js.getString("fileName"),js.getString("crType")
                                ,js.getString("crStartTime"),js.getString("crEndTime"),js.getString("plantName"));
                        operations.add(operation);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.d(TAG, "onSuccess: "+operations.size());
                    if(listener!=null)
                    listener.afterLoad(operations);
            }

            @Override
            public void onError(Response response, int errorCode, Exception e) {

            }
        },"allCuringRecords");
        return operations;
    }

    @Override
    public void updateOperationInfos(int index) {
        Log.d(TAG, "updateOperationInfos: 更新数据");
        if(MaintenanceModel.myPot.getPot(index)==MaintenanceModel.myPot.POT_INAVAILABLE) {  //如果盆栽有植物 请求数据
            getOperationInfos(new DataInterface<List<Operation>>() {
                @Override
                public void afterLoad(List<Operation> operations) {
                    adapt.replaceAll(operations);
                }
                @Override
                public void beforeLoad() {

                }
            }, index);
        }else {//如果盆栽没有植物 替换空数组
            operations=new ArrayList<>();
            adapt.replaceAll(operations);
        }
    }

    @Override
    public OperationAdapt getOperationAdapt() {
       if(adapt==null){
           adapt=new OperationAdapt(context,operations);
           Log.d(TAG, "getOperationAdapt: 初始化"+operations.size());
       }
       return  adapt;
    }

    @Override
    public void putRecord(int index, int type,String starttime,String endtime) {
        /**
         * 获取filename
         * 首先本地加记录
         * 传数据
         */
        //本地加记录
        String filename=MaintenanceModel.myPot.getPlant(index).getFilename();
        String name=MaintenanceModel.myPot.getPlant(index).getName();;
        adapt.addData(new Operation(filename,Operation.types[type],starttime,endtime,name));
        //传数据
         OkHttpHelper helper=OkHttpHelper.getinstance();
         OkHttpHelper.MyUrlBuild build=new OkHttpHelper.MyUrlBuild(OkHttpHelper.URL_BASE2 + OkHttpHelper.URL_PUT_RECORD);
         build.add("fileName",filename);
         build.add("userId", LoginModel.getUser().getUser_phone_number());
         build.add("crType",Operation.types[type]);
         if(starttime!=null) build.add("crStartTime",starttime);
         if(endtime!=null) build.add("crEndTime",endtime);
         helper.get(build.toString(), new BaseCallback<String>() {
             @Override
             public void onRequestBefore() {

             }

             @Override
             public void onFailure(Request request, Exception e) {

             }

             @Override
             public void onSuccess(Response response, String s) {
                 Log.d(TAG, "onSuccess: putOperation"+s);
             }

             @Override
             public void onError(Response response, int errorCode, Exception e) {

             }
         });
    }

}
