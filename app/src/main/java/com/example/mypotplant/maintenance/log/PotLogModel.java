package com.example.mypotplant.maintenance.log;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import com.example.mypotplant.http.BaseCallback;
import com.example.mypotplant.http.OkHttpHelper;
import com.example.mypotplant.maintenance.Plant;
import com.example.mypotplant.person.login.LoginModel;
import com.example.mypotplant.person.login.LoginPresenter;
import com.example.mypotplant.utils.NetWorkUtils;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MXL on 2020/7/12
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class PotLogModel implements IPotLogModel{

    public  final  static  String TAG="PotLogModel";
    private List<PotLog> potlogs;
    Context context;
    PotLogAdapt adapt;
    Plant plant;
    PotLogListener listener;
    PotLogModel(Context context,Plant plant){
        this.context=context;
        this.plant=plant;
    }
    @Override
    public PotLogAdapt getPhotoAdapt() {
        if(adapt==null&&potlogs!=null){
          adapt=new PotLogAdapt(context,potlogs);
          adapt.setEditlistener(new PotLogAdapt.PotlogEditListener() {
              @Override
              public void onDelete(int position) {
                  //调用适配器方法删除数据
                  removeData(position);
                  //这里进行与服务端交互
              }

              @Override
              public void onFindMore() {

              }
          });
        }
        return adapt;
    }

    /**
     * 从服务端获取list
     * 暂无交互 直接初始化list
     */
    @Override
    public void loadlist(final PotLogListener listener) {
        /*这里加上从服务端获取数据的代码
         *
         */
        OkHttpHelper helper=OkHttpHelper.getinstance();
         Map<String,String> data=new HashMap<>();
         data.put("fileName",plant.getFilename());
        Log.d(TAG, "loadlist: "+plant.getFilename());
         potlogs=null;
        helper.get(OkHttpHelper.URL_BASE2 + OkHttpHelper.URL_GARDEN_GET_ALLPOTLOG, data, new BaseCallback<JSONArray>() {
            @Override
            public void onRequestBefore() {

            }

            @Override
            public void onFailure(Request request, Exception e) {
                Log.d(TAG, "onFailure: list");
            }

            @Override
            public void onSuccess(Response response, JSONArray array) {
                Log.d(TAG, "onSuccess: "+array);
                potlogs = new ArrayList<>();
                if(array!=null) {
                   for (int i = 0; i < array.length(); i++) {
                       try {
                           JSONObject jsonObject=array.getJSONObject(i);
                            PotLog potLog=new PotLog();
                            potLog.setId(jsonObject.getInt("brId"));
                            potLog.setWord(jsonObject.getString("brContent"));
                            potLog.setUrl(jsonObject.getString("brUrl"));
                            potLog.setTime(jsonObject.getString("brTime"));
                            potlogs.add(potLog);
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                   }
               }
               listener.afrerLoadlist();
            }
            @Override
            public void onError(Response response, int errorCode, Exception e) {
                Log.d(TAG, "onError: list"+errorCode);
            }
        }, "allJournals");
        //测试方法
//        testdata();
    }

    /**
     * 向服务端保存数据
     * 暂不实现
     */
    @Override
    public void savelist() {

    }

    /**
     * 添加数据
     * 本地添加  实现
     * 服务端添加 暂无交互
     * @param potLog
     */
    @Override
    public void addData(PotLog potLog) {
        Log.d(TAG, "addData: ");
        adapt.adddata(potLog);
        /*
         * 这里是与服务端交互
         * 要求 已经登陆 网络开启
         * 添加时间 图片 文件名 用户id给服务端
         */
      addPotLogtoServer(potLog);
    }

    /**
     * 删除指定位置的数据
     * 条件 登录 ，有网
     * 本地删除 实现
     * 服务端删除
     * @param position
     */
    @Override
    public void removeData(final int position) {
        if(NetWorkUtils.networkAvailable(context)) {
            if (LoginModel.getISLogin()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("确认删除");
                builder.setMessage("您确定要删除该日志吗？");
                builder.setCancelable(false);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //核心代码
                        deletePotLogfromServer(potlogs.get(position).getId());
                        adapt.reomveData(position);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("再想想", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {
                Toast.makeText(context, "请先完成登录", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(context, "请检查网络连接", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 测试方法 向list中添加数据 模拟请求数据
     * 加入loadlist方法生成数据
     */
    private  void testdata(){
        PotLog potLog;
        int num=30;
        if(potlogs!=null){
            for(int i=0;i<num;i++) {
                potLog = new PotLog(Calendar.getInstance(), "第一天",plant.getFilename());
                potlogs.add(potLog);
            }
        }
    }

    /**
     * 首先修改本地类型
     * 向服务端传递修改信息
     * 养护模式和类型信息
     * @param data
     */
    @Override
    public void modifySetting(Object[] data) {
        plant.setType((String) data[0]);
        plant.setIs_intelligent((Integer)data[1]);
        //传递给服务器端 未实现
    }

    @Override
    public void modifytype(String type) {
        OkHttpHelper helper=OkHttpHelper.getinstance();
        Map data=new HashMap<>();
        data.put("fileName",plant.getFilename());
     //   data.put("plantName",type);
        helper.get(OkHttpHelper.URL_BASE2 + OkHttpHelper.URL_MODIFY_PLANT, data, new BaseCallback<String>() {
            @Override
            public void onRequestBefore() {

            }

            @Override
            public void onFailure(Request request, Exception e) {
                Log.d(TAG, "onFailure: modify name ");
            }

            @Override
            public void onSuccess(Response response, String s) {
                Log.d(TAG, "onSuccess: modify name"+s);
            }

            @Override
            public void onError(Response response, int errorCode, Exception e) {
                Log.d(TAG, "onError: modify name"+errorCode);
            }
        });
    }

    public Plant getPlant() {
        return plant;
    }
    private void addPotLogtoServer(PotLog potLog){
        if(LoginPresenter.carryLoginState(context)){
            if(NetWorkUtils.networkAvailable(context)){
                OkHttpHelper helper=OkHttpHelper.getinstance();
                Map<String,String> data=new HashMap();
                data.put("userId", LoginModel.getUser().getUser_phone_number()); //手机号
                data.put("brContent",potLog.getWord());       //正文
                data.put("fileName",plant.getFilename());        //图片名
                Log.d(TAG, "addPotLogtoServer: "+data);
                //图片组
                Map<String, File> images=new HashMap<>();
                images.put("brUrl",new File(potLog.getImage()));
                helper.post(OkHttpHelper.URL_BASE2 + OkHttpHelper.URL_GARDEN_POTLOG, data,images,new BaseCallback<Integer>() {
                    @Override
                    public void onRequestBefore() {

                    }

                    @Override
                    public void onFailure(Request request, Exception e) {
                        Log.d(TAG, "onFailure: add log");
                    }

                    @Override
                    public void onSuccess(Response response, Integer o) {
                        Log.d(TAG, "onSuccess: addlog"+o);
                        if(o==0){
                            Toast.makeText(context,"添加日志成功",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Response response, int errorCode, Exception e) {
                        Log.d(TAG, "c"+errorCode);
                    }
                }, "state");
            }else {
                Toast.makeText(context,"当前网络不可用，请检查网络设置",Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     *
     * @param id 日志编号
     * @return
     */
    private void deletePotLogfromServer(int id){
        OkHttpHelper helper=OkHttpHelper.getinstance();
        Map<String,String> data=new HashMap<>();
        data.put("brId",String.valueOf(id));
        data.put("fileName",plant.getFilename());
//        String jsStr=new Gson().toJson(data);
        Log.d(TAG, "deletePotLogfromServer: ");
        helper.get(OkHttpHelper.URL_BASE2 + OkHttpHelper.URL_GARDEN_DELETE_POTLOG,data,new BaseCallback<Integer>() {
            @Override
            public void onRequestBefore() {

            }

            @Override
            public void onFailure(Request request, Exception e) {
                Log.d(TAG, "onFailure: deletepotlog");
            }

            @Override
            public void onSuccess(Response response, Integer o) {
               if(o==0){
                   Toast.makeText(context,"删除日志成功",Toast.LENGTH_LONG).show();
               }
            }

            @Override
            public void onError(Response response, int errorCode, Exception e) {
                Log.d(TAG, "onError: delete"+errorCode);
            }
        },"state");
    }
    interface  PotLogListener{
        void afrerLoadlist();
    }
}

