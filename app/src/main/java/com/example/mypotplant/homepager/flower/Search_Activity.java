package com.example.mypotplant.homepager.flower;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.mypotplant.BaseActivity;
import com.example.mypotplant.R;
import com.example.mypotplant.http.BaseCallback;
import com.example.mypotplant.http.OkHttpHelper;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import scut.carson_ho.searchview.ICallBack;
import scut.carson_ho.searchview.SearchView;
import scut.carson_ho.searchview.bCallBack;

/**
 *  显示搜索结果的布局
 */
public class Search_Activity extends BaseActivity {
    private SearchView searchView;
    public static final String TAG="Search_Activity";
    public static ArrayList<Flower> list;
    public static ArrayList<String> names;
    private  int start_mode; //启动模式 搜索花资料启动0   选定类别启动 1
    public static final  int TYPESET=1;
    public static final Object lock=new Object();
     public static void  actionStart(Context context){
         Intent intent=new Intent(context,Search_Activity.class);
         intent.putExtra("mode",0);
         context.startActivity(intent);
     }
   static {
       list=new ArrayList<>();
   }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_);
        initdata();
        // 1. 绑定组件
        searchView = (SearchView) findViewById(R.id.search_view);

        // 2. 设置点击键盘上的搜索按键后的操作（通过回调接口）
        // 参数 = 搜索框输入的内容
        searchView.setOnClickSearch(new ICallBack() {
            @Override
            public void SearchAciton(String string) {
                if(start_mode==0) {
                    carry_search(string);
                }
                if(start_mode==1){
                 carry_typeset(string);
                }
            }
        });

        // 3. 设置点击返回按键后的操作（通过回调接口）
        searchView.setOnClickBack(new bCallBack() {
            @Override
            public void BackAciton() {
                if(start_mode==0)
                    finish();
                if(start_mode==1){

                }
            }
        });
    }

    /**
     * 初始化list 加载数据
     * @param
     * @return
     */
    private  void initdata(){
        //获取启动模式
        start_mode=getIntent().getIntExtra("mode",0);
        initflowers(this,null);
    }

    /**
     *  执行搜索操作 模糊搜索 如果匹配多条结果 显示第一条
     *  未找到暂时弹出toast
     * @param result 搜索字符串
     * @return
     */
    private void carry_search(String result){
        if(list!=null){
          for(Flower flower:(ArrayList<Flower>)list){
              if(flower.getName().contains(result)){
                  FlowershowActivity.actionStart(this,flower);
                  return;
              } else { //没找到暂时弹出toast
                  Toast.makeText(this,"未找到该花",Toast.LENGTH_SHORT).show();
              }
          }
        }
    }
    /**
     * 执行类型选择操作
     *  模糊搜索 如果匹配多条结果 显示第一条
     *  未找到暂时弹出toast
     *  返回无
     */
    private  void carry_typeset(String result){
        if(list!=null){
            for(Flower flower:(ArrayList<Flower>)list){
                if(flower.getName().contains(result)){
                    Intent intent=new Intent();
                    intent.putExtra("flower",flower);
                    setResult(RESULT_OK, intent);
                    finish();
                    return;
                } else { //没找到暂时弹出toast
                    Toast.makeText(this,"未找到该花",Toast.LENGTH_SHORT).show();
                    try {
                        Thread.currentThread().sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent intent=new Intent();
                    setResult(TYPESET, intent);
                    finish();
                }
            }
        }
    }
    private static  void initflowers(Context context, final LoadFlowerListener listener){
        Log.d(TAG, "initflowers: 获取flower");
        //等待锁的进入
//        if(list.size()>0){
//            Log.d(TAG, "initflowers: 111");
//            listener.afterload(list);
//        }
        names=new ArrayList<>();
        names.add("君子兰");
        names.add("中国兰花");
        names.add("绿萝");
        names.add("向日葵");
        names.add("茉莉花");
        OkHttpHelper helper=OkHttpHelper.getinstance();
        synchronized (Search_Activity.class) {
            for (int i = 0; i < names.size(); i++) {
                Map data = new HashMap();
                data.put("name", names.get(i));
                helper.post(OkHttpHelper.URL_BASE + OkHttpHelper.URL_FLOWER_GETALL, new Gson().toJson(data), new BaseCallback<String>() {
                    @Override
                    public void onRequestBefore() {
                        Log.d(TAG, "onRequestBefore: ");
                    }

                    @Override
                    public void onFailure(Request request, Exception e) {
                        Log.d(TAG, "onFailure: ");
                    }

                    @Override
                    public void onSuccess(Response response, String s) {
                        Flower flower = new Flower();
                        Log.d(TAG, "onSuccess: " + s);
                        JSONObject o = null;
                        try {
                            o = new JSONObject(s);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            flower.setLight_condition(o.getString("light_condition"));
                            flower.setLight_condition(o.getString("light_request"));
                            flower.setName(o.getString("name"));
                            flower.setNo_suit_temp(o.getString("no_suit_temp"));
                            flower.setOpening_word(o.getString("opening_word"));
                            flower.setPh_value(o.getString("ph_value"));
                            flower.setRecom_fertilizer(o.getString("recom_fertilizer"));
                            flower.setRecom_soil(o.getString("recom_soil"));
                            flower.setSuit_humidity(o.getString("suit_humidity"));
                            flower.setSuit_temp(o.getString("suit_temp"));
                            flower.setUrl(o.getString("url"));
                            if(list.size()<names.size())
                            list.add(flower);
                            if(list.size()==names.size()) {
                                if(listener!=null)
                                listener.afterload(list);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response response, int errorCode, Exception e) {
                        Log.d(TAG, "onError: " + errorCode);
                    }
                });
            }
        }
//        Flower flower =new Flower();
//        flower.setName(context.getResources().getString(R.string.orchid_name));
//        flower.setLight_condition(context.getResources().getString(R.string.orchid_light_condi));
//        flower.setSuit_humidity(context.getResources().getString(R.string.orchid_suit_humidity));
//        flower.setPh_value(context.getResources().getString(R.string.orchid_ph));
//        flower.setSuit_temp(context.getResources().getString(R.string.orchid_suit_temp));
//        flower.setNo_suit_temp(context.getResources().getString(R.string.orchid_no_suit_temp));
//        flower.setOpening_word(context.getResources().getString(R.string.orchid_opening_word));
//        flower.setLight_request(context.getResources().getString(R.string.orchid_light_request));
//        flower.setFertilization_times(context.getResources().getIntArray(R.array.orchid_fertilization_times));
//        flower.setSpray_times((context.getResources().getIntArray(R.array.orchid_spray_times)));
//        flower.setOther_oper((context.getResources().getStringArray(R.array.orchid_other_oper)));
////        flower.setFertilization_times(new int[]{20,20,20,0});
////        flower.setSpray_times(new int[]{1,1,1,1});
////        flower.setOther_oper(new String[]{"换盆、修剪","修剪","修剪","修剪"});
//        flower.setRecom_fertilizer(context.getResources().getString(R.string.orchid_recom_fertilizer));
//        flower.setRecom_soil(context.getResources().getString(R.string.orchid_recom_soil));
//        flower.setRecom_medic(context.getResources().getString(R.string.orchid_recom_medic));
//        flower.setCode(1);
//        list.add(flower);
//        Flower flower2 =new Flower();
//        flower2.setName(context.getResources().getString(R.string.scindapsus_name));
//        flower2.setLight_condition(context.getResources().getString(R.string.scindapsus_light_condi));
//        flower2.setSuit_humidity(context.getResources().getString(R.string.scindapsus_suit_humidity));
//        flower2.setPh_value(context.getResources().getString(R.string.scindapsus_ph));
//        flower2.setSuit_temp(context.getResources().getString(R.string.scindapsus_suit_temp));
//        flower2.setNo_suit_temp(context.getResources().getString(R.string.scindapsus_no_suit_temp));
//        flower2.setOpening_word(context.getResources().getString(R.string.scindapsus_opening_word));
//        flower2.setLight_request(context.getResources().getString(R.string.scindapsus_light_request));
//        flower2.setFertilization_times(context.getResources().getIntArray(R.array.scindapsus_fertilization_times));
//        flower2.setSpray_times(context.getResources().getIntArray(R.array.scindapsus_spray_times));
//        flower2.setOther_oper(context.getResources().getStringArray(R.array.scindapsus_other_oper));
//        flower2.setRecom_fertilizer(context.getResources().getString(R.string.scindapsus_recom_fertilizer));
////        flower2.setFertilization_times(new int[]{15,15,14,15});
////        flower2.setSpray_times(new int[]{1,1,1,1});
////        flower2.setOther_oper(new String[]{"换盆、修剪","修剪","修剪","修剪"});
////        flower2.setRecom_fertilizer("蚯蚓粪+通用型肥料");
//        flower2.setRecom_soil(context.getResources().getString(R.string.scindapsus_recom_soil));
//        flower2.setRecom_medic(context.getResources().getString(R.string.scindapsus_recom_medic));
//        flower.setCode(2);
//        list.add(flower2);
    }

    /**
     * 返回list可能为空
     * @param context
     * @return
     */
    public static ArrayList getList(Context context,LoadFlowerListener listener) {
       if(list==null||list.size()==0){
           initflowers(context, listener);
       }
        return list;
    }

    /**
     * <br>引用类：{@link }<br/>
     * 返回list可能为空
     * @param
     * @return
     */
    public static  Flower getFlower(int code,Context context,LoadFlowerListener listener){
        if(list==null||list.size()==0){
            initflowers(context,listener);
        }
        for (Flower flower:list){
            if(flower.getCode()==code){
                return flower;
            }
        }
        return  null;
    }

    public interface LoadFlowerListener{
        void afterload( ArrayList<Flower> list);
    }
}

