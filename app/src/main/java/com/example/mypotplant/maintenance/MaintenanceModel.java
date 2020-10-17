package com.example.mypotplant.maintenance;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import com.example.mypotplant.http.BaseCallback;
import com.example.mypotplant.http.OkHttpHelper;
import com.example.mypotplant.maintenance.PlantPhotoAdapt.PhotoPopupMenuListener;
import com.example.mypotplant.person.login.LoginModel;
import com.example.mypotplant.utils.NetWorkUtils;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by MXL on 2020/1/15
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class MaintenanceModel implements IMaintenanceModel{
    public final  String TAG="MaintenanceModel";
    private ArrayList<Plant>  mlist;
    private  PlantPhotoAdapt adapt;
    private Context mContext;
    private  ModelListener listener;
    public static MyPot myPot;
   public MaintenanceModel(Context context){
        mContext=context;
    }
    @Override
    public PlantPhotoAdapt getPhotoAdapt() {
        if(adapt!=null)return  adapt;
        else  {
            Log.d(TAG, "getPhotoAdapt: list:"+mlist);
            adapt=new PlantPhotoAdapt(mlist,(Activity) mContext);
            adapt.setPhotolistener(new PhotoPopupMenuListener() { //照片长按菜单点击事件
                @Override
                public void OnFindOut(int position) {
                   adapt.findoutDefaultimpl(position);
                }

                @Override
                public void OnDelete(int position) {
                 removeData(position);

                }
            });
            return  adapt;
        }
    }

    @Override
    public void loadlist() {
      //  ArrayList<Plant> list=(ArrayList<Plant>)DataSupport.findAll(Plant.class);
       final ArrayList<Plant> list=new ArrayList<>();
        OkHttpHelper helper=OkHttpHelper.getinstance();
        Map<String,String> data=new HashMap<>();
        data.put("userId",LoginModel.getUser().getUser_phone_number());
        helper.get(OkHttpHelper.URL_BASE2 + OkHttpHelper.URL_GARDEN_GET_ALLPLANT,data, new BaseCallback<JSONArray>() {
            @Override
            public void onRequestBefore() {

            }

            @Override
            public void onFailure(Request request, Exception e) {
                Log.d(TAG, "onFailure: getlist");
            }

            @Override
            public void onSuccess(Response response, JSONArray jsonArray) {
//                JSONArray jsonArray= null;
//                try {
//                    jsonArray = new JSONArray(jsStr);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                Log.d(TAG, "onSuccess: +"+jsonArray);
                Log.d(TAG, "onSuccess: 请求到"+jsonArray.length());
                for(int i=0;i<jsonArray.length();i++){
                    Plant plant=new Plant();
                    try {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        plant.setFilename(jsonObject.getString("fileName"));
                        plant.setName(jsonObject.getString("plantName"));
                        plant.setUrl(jsonObject.getString("plantUrl"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    list.add(plant);
                }
                if(list==null){
                    mlist=new ArrayList();
                }else {
                    mlist=list;
                }
                if(listener!=null){  //调用回调接口
                    listener.afterListLoad();
                    //加载内部类
                    myPot=new MyPot(); //内部类赋值传递this指针
                    myPot.updatainfo();
                }
            }

            @Override
            public void onError(Response response, int errorCode, Exception e) {
                Log.d(TAG, "onError: getlist"+errorCode);
            }
        },"allPlants");
    }

    @Override
    public void addData(Plant plant) {
        if(adapt!=null) {
            adapt.addData(plant);
        // addDataToDatebase(plant);
         //添加到服务器
         addDataToServer(plant);
        }
    }

    @Override
    public void removeData(final int position) {
        /**
         * 逻辑
         * 弹出对话框询问是否删除
         * 同意则删除照片文件
         * 删除适配器中数据
         * 删除数据库中数据  测试本地
         * 删除服务端数据    交互
         * 删除mypot记录
         */
        Log.d(TAG, "removeData: ");

                AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
                builder.setTitle("确认删除");
                builder.setMessage("您确定要删除该植株以及全部记录吗？");
                builder.setCancelable(false);
                builder.setPositiveButton("确定",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //核心代码
          //      removeDataToDatebase(position);
                String filename=mlist.get(position).getFilename();  //删除mypot记录
                myPot.deletePlant(filename);
                removeDataFromServer(mlist.get(position).getFilename());
                CameraHelper.deletePhoto(mlist.get(position));
                adapt.reomveData(position);
                dialog.dismiss();
                      }
        });
             builder.setNegativeButton("再想想",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();
    }

    @Override
    public void setListener(ModelListener listener) {
        this.listener=listener;
    }

    /**
     *  将数据保存在数据库中
     */
    private void addDataToDatebase(Plant plant){
        plant.save();
    }
    /**
     *  删除数据库中数据
     */
    private void removeDataToDatebase(int position){
        String filename =mlist.get(position).getFilename();
        DataSupport.deleteAll(Plant.class,"filename = ?",filename);
    }

    /**
     * 将数据保存在服务端
     * @param plant 植株类
     */
    private void addDataToServer(Plant plant){
        OkHttpHelper helper=OkHttpHelper.getinstance();
        Map<String,String> params=new HashMap<>();
        params.put("fileName",plant.getFilename());
        params.put("userId", LoginModel.getUser().getUser_phone_number());
        params.put("plantName",plant.getName());
        Map<String, File> images=new HashMap<>();
        images.put("plantUrl",new File(plant.getPath()));
        helper.post(OkHttpHelper.URL_BASE2 + OkHttpHelper.URLGARDEN_ADD_PLANT, params, images, new BaseCallback<Integer>() {
            @Override
            public void onRequestBefore() {

            }

            @Override
            public void onFailure(Request request, Exception e) {
                Log.d(TAG, "onFail: ");
            }

            @Override
            public void onSuccess(Response response, Integer o) {
                if(o==0)
                Toast.makeText(mContext,"植株添加成功",Toast.LENGTH_LONG).show();
                else {
                    Toast.makeText(mContext,"植株添加异常",Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onSuccess: "+o);
                }
            }

            @Override
            public void onError(Response response, int errorCode, Exception e) {
                Log.d(TAG, "onError: "+errorCode);
            }
        }, "state");
    }
    /**
     * 将数据从服务端删除
     * @param filename 植株主键
     */
    private void removeDataFromServer(String filename){
        OkHttpHelper helper=OkHttpHelper.getinstance();
        Map<String,String> data= new HashMap<>();
        data.put("fileName",filename);
//        String jsStr=new Gson().toJson(data);
        helper.get(OkHttpHelper.URL_BASE2 + OkHttpHelper.URL_GARDEN_DELETE_PLANT, data, new BaseCallback<Integer>() {
            @Override
            public void onRequestBefore() {

            }

            @Override
            public void onFailure(Request request, Exception e) {
                Log.d(TAG, "onFailure: ");
            }

            @Override
            public void onSuccess(Response response, Integer o) {
                Log.d(TAG, "onSuccess: 从服务器删除成功"+o);
                Toast.makeText(mContext,"删除成功",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Response response, int errorCode, Exception e) {
                Log.d(TAG, "onError: "+errorCode);
            }
        }, "state");
    }

    interface ModelListener{
        void afterListLoad();
    }
      public  class MyPot {
        int pots[]; //记录盆栽状态
        Plant plants[];
        public    int POT_AVAILABLE=0;
          public   int POT_SIZE=4;
        public   int POT_INAVAILABLE=1;
        public MyPot(){
            pots=new int[POT_SIZE];
            plants=new Plant[POT_SIZE];
            for(int i=0;i<POT_SIZE;i++){
                plants[i]=new Plant();
            }
        }

          /**
           * 获得第i个花盆植株状态
           * @param i
           * @return
           */
          public int getPot(int i) {
            return pots[i];
        }

          /**
           * 得到植株
           * @param i
           * @return
           */
          public Plant getPlant(int i) {
              return plants[i];
          }

          /**
           * 设置植株
           * @param i
           * @param plant
           */
          public void setPlant(int i, Plant plant){
                    plants[i]=plant;   //放置植株
                    //传递数据给服务器
                    setPot2Server(plant.getFilename(),i);
                    setPot(i,POT_INAVAILABLE);
           }
           public  void deletePlant(int i){
                   if(getPot(i)==POT_INAVAILABLE){
                       //传递数据给服务器
                       deletePot2Server(i);
                       plants[i]=new Plant();   //放置植株
                       setPot(i,POT_AVAILABLE);  //设置植株状态
                   }else {
                       Log.d(TAG, "deletePlant: 删除植株失败");
                   }
           }
          public  void deletePlant(String fliename){
              for(int i=0;i<POT_SIZE;i++){
                  if(plants[i].getFilename()!=null&&plants[i].getFilename().equals(fliename)){
                      //传递数据给服务器
                      deletePot2Server(i);
                      plants[i]=new Plant();   //放置植株
                      setPot(i,POT_AVAILABLE);  //设置植株状态
                  }
              }
          }
          public  Plant getPlantByName(String fliename){
              for(int i=0;i<POT_SIZE;i++){
                  if(plants[i].getFilename()!=null&&plants[i].getFilename().equals(fliename)){
                     return plants[i];
                  }
              }
              return null;
          }
          /**
           * 得到全部植株数据
           * @return
           */
          public Plant[] getPlants() {
              return plants;
          }

          /**
           * 判断是否包含植株
           * @param filename 文件名
           * @return 位置 不存在返回-1
           */
          public  int  iscontain(String filename){
              int index=0;
              for(Plant plant:plants){
                  if(plant.getFilename()!=null){
                      if(plant.getFilename().equals(filename)) {
                          return index;
                      }
                  }
                  index++;
              }
              return -1;
          }
          /**
           * 将文件名为filename的植株移到i位置
           * 判断是否存在
           * 移动
           * @param filename
           * @param i
           * @return
           */
          public  void replace(String filename,int i){
              int index;
              if((index=iscontain(filename))!=-1){
//                  plants[i]=plants[index];
//                  plants[index]=new Plant();
                  setPlant(i,plants[index]);
                  deletePlant(index);
              }
          }

          /**
           * 设置植株位置
           * @param i
           * @param state
           */
        private void setPot(int i,int state) {
            this.pots[i]=state;
        }

        /**
         * 从服务端拿到植株数据
         * 栏位号 文件名
         */
        private  void updatainfo(){
           if (NetWorkUtils.networkAvailable(mContext)) {
               if(LoginModel.getISLogin()) {
                   OkHttpHelper helper = OkHttpHelper.getinstance();
                   Map data = new HashMap();
                   data.put("userId",LoginModel.getUser().getUser_phone_number());
                   helper.get(OkHttpHelper.URL_BASE2 + OkHttpHelper.URL_GET_ALLPOT, data, new BaseCallback<JSONArray>() {
                       @Override
                       public void onRequestBefore() {

                       }

                       @Override
                       public void onFailure(Request request, Exception e) {

                       }

                       @Override
                       public void onSuccess(Response response, JSONArray array) {
                           Log.d(TAG, "onSuccess: "+array);
                           Log.d(TAG, "onSuccess: 拿到栏位数量" + array.length());
                           for (int i = 0; i < array.length(); i++) {
                               try {
                                   JSONObject jsonObject = array.getJSONObject(i);
                                   int potNum = jsonObject.getInt("potNum")-1;
                                   String filename = jsonObject.getString("fileName");
                                   for(int j=0;j<mlist.size();j++) {
                                       if(mlist.get(j).getFilename().equals(filename)){
                                        //   setPlant(potNum,mlist.get(i)); 取代setplant
                                           plants[potNum]=mlist.get(j);
                                           setPot(potNum,POT_INAVAILABLE);
                                           Log.d(TAG, "onSuccess: 设置"+potNum+"为"+mlist.get(i));
                                       }
                                      // Plant plant = new Plant(null, filename);
                                   }
                                   for(int j=0;j<POT_SIZE;j++){
                                       Log.d(TAG, "potstate"+pots[j]);
                                       Log.d(TAG, "plant: "+plants[j]);
                                   }
                                 //  setPlant(potNum, plant);
                               } catch (JSONException e) {
                                   e.printStackTrace();
                               }

                           }
                       }

                       @Override
                       public void onError(Response response, int errorCode, Exception e) {

                       }
                   }, "allPots");
               }else {
                   Toast.makeText(mContext,"请先登录",Toast.LENGTH_SHORT).show();
               }
           }else {
               Toast.makeText(mContext,"请检查网络设置",Toast.LENGTH_SHORT).show();
           }
        }

          /**
           *  传递植株位置到服务器
           * @param filename  植物名
           * @param potNum   盆栽位置
           */
        private  void setPot2Server(String filename,int potNum){
            OkHttpHelper helper = OkHttpHelper.getinstance();
//            Map data =new HashMap();
//            data.put("fileName",filename);
//            data.put("potNum",potNum);
            OkHttpHelper.MyUrlBuild build=new OkHttpHelper.MyUrlBuild(OkHttpHelper.URL_BASE2 + OkHttpHelper.URL_SET_POT);
            build.add("fileName",filename);
            build.add("potNum",potNum+1);
            Log.d(TAG, "setPot2Server: build"+build.build().toString());
            helper.get(build.build().toString(), new BaseCallback<String>(){
                @Override
                public void onRequestBefore() {

                }

                @Override
                public void onFailure(Request request, Exception e) {

                }

                @Override
                public void onSuccess(Response response, String s) {
                    Log.d(TAG, "onSuccess: 设置栏位"+s);
                }

                @Override
                public void onError(Response response, int errorCode, Exception e) {
                    Log.d(TAG, "onError: 设置栏位"+errorCode);
                }
            });
        }

          /**
           *  @param potNum 盆栽位置
           */
        private  void deletePot2Server(int potNum){
            OkHttpHelper helper = OkHttpHelper.getinstance();
//            Map data =new HashMap();
//            data.put("userId",LoginModel.getUser().getUser_phone_number());
//            data.put("potNum",potNum);
            OkHttpHelper.MyUrlBuild build=new OkHttpHelper.MyUrlBuild(OkHttpHelper.URL_BASE2 + OkHttpHelper.URL_SET_POT);
            build.add("userId",LoginModel.getUser().getUser_phone_number());
            build.add("potNum",potNum+1);
            Log.d(TAG, "deletePot2Server: "+build.build().toString());
            helper.get(build.build().toString(), new BaseCallback<String>(){
                @Override
                public void onRequestBefore() {

                }

                @Override
                public void onFailure(Request request, Exception e) {

                }

                @Override
                public void onSuccess(Response response, String s) {
                    Log.d(TAG, "onSuccess: 删除栏位"+s);
                }

                @Override
                public void onError(Response response, int errorCode, Exception e) {
                    Log.d(TAG, "onError: 删除栏位"+errorCode);
                }
            });
        }

      }
    public static synchronized MyPot getMyPot() {
        return myPot;
    }
}
