package com.example.mypotplant.http;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;

/**
 * Created by MXL on 2019/12/13
 * <br>类描述：这个类用来辅助OKHttp <br/>
 * 接口文档
 * http://120.79.131.204:8080/swagger-ui.html#
 * https://smartpot.w.eolinker.com/#/share/index?shareCode=KeGXjU
 * @version 1.0
 * @since 1.0
 */

public class OkHttpHelper {
    public  static  String URL_BASE="http://120.79.131.204:8080/";
    public  static  String URL_BASE2="http://121.36.26.175:8080/smartpot/";
    public  static  String URL_REGISTER="user/signUp";
    public   static  String URL_LOGIN="user/login";
    public  static  String  URL_GARDEN_POTLOG="garden/addGrowthJournal";
    public  static  String  URL_GARDEN_DELETE_POTLOG="garden/deleteGrowthJournal";
    public  static String  URLGARDEN_ADD_PLANT="garden/addPlant";
    public  static  String URL_GARDEN_DELETE_PLANT="garden/deletePlant";
    public  static  String URL_GARDEN_GET_ALLPLANT="garden/getAllPlants";
    public  static  String URL_GARDEN_GET_ALLPOTLOG= "garden/getAllGrowthJournal";
    public  static  String URL_FLOWER_GETALL="flowers/getall";
    public  static  String URL_GET_USER_SETTING="user/getsets";
    public  static  String URL_SET_WEATHER_ALERT="user/alert";
    public  static  String URL_SET_MAINTAIN_ALERT="user/maintenance";
    public  static  String URL_SET_INTERVAL="user/interval";
    public  static  String URL_UPLOAD_HEAD="uploadFile";
    public  static String URL_UPDATE_SET="user/updatesets";
    public  static String URL_GET_INFO="user/getinformation";
    public  static String URL_UPDATE_INFO="user/updateinformation";
    public  static String URL_GET_FOLLOWS="user/getfollows";
    public  static String URL_GET_FANS="user/getfans";
    public  static String URL_ADD_FAN="user/addfans";
    public  static String URL_DELETE_FAN="user/cancelfans";
    public  static String URL_SET_POT="garden/setPot";
    public  static String URL_DELETE_POT="garden/deletePot";
    public  static String URL_GET_ALLPOT="garden/getPots";
    public  static String URL_MODIFY_PLANT="garden/updatePlant";
    public  static String URL_GET_PLANTRECORDS="hardware/getPlantRecords";
    public  static  String URL_GET_CURING_RECORDS="conserve/getCuringRecords";
    public static  String  URL_PUT_RECORD="conserve/operation";
    /**
 * 采用单例模式使用OkHttpClient
 */
private static OkHttpHelper mOkHttpHelperInstance;
private static OkHttpClient mClientInstance;
private Handler mHandler;
private Gson mGson;    /**
 * 单例模式，私有构造函数，构造函数里面进行一些初始化
 */
private OkHttpHelper() {
    mClientInstance = new OkHttpClient();
    mClientInstance.setConnectTimeout(10, TimeUnit.SECONDS);
    mClientInstance.setReadTimeout(10, TimeUnit.SECONDS);
    mClientInstance.setWriteTimeout(30, TimeUnit.SECONDS);
    mGson = new Gson();
    mHandler = new Handler(Looper.getMainLooper());
}
/**
 * 获取实例
 *
 * @return
 */
public static OkHttpHelper getinstance() {
    if (mOkHttpHelperInstance == null) {
        synchronized (OkHttpHelper.class) {
     if (mOkHttpHelperInstance == null) {
    mOkHttpHelperInstance = new OkHttpHelper();
        }
  }
}
    return mOkHttpHelperInstance;
}
/**
 * 封装一个request方法，不管post或者get方法中都会用到
 */
public void request(final Request request, final BaseCallback callback) {
    //在请求之前所做的事，比如弹出对话框等
    callback.onRequestBefore();
 //   Log.e("okHttpHelper", "request: "+request.body().toString());
    mClientInstance.newCall(request).enqueue(new Callback() {
        @Override
    public void onFailure(Request request, IOException e) {                //返回失败
        callbackFailure(request, callback, e);
    }
        @Override
    public void onResponse(Response response) throws IOException {
            if (response.isSuccessful()) {                    //返回成功回调
        String resString = response.body().string();
                Log.d("OkHttpHelper", "onResponse: "+resString);
        if (callback.mType == String.class) {                        //如果我们需要返回String类型
            callbackSuccess(response, resString, callback);
        } else {
            //如果返回的是其他类型，则利用Gson去解析
            try {
                Object o = mGson.fromJson(resString, callback.mType);
                callbackSuccess(response, o, callback);
            } catch (JsonParseException e) {
                e.printStackTrace();
                callbackError(response, callback, e);
            }
        }
    } else {                    //返回错误
        callbackError(response, callback, null);
    }
    }
    });
}
    public void request(final Request request, final BaseCallback callback, final String key) {
        //在请求之前所做的事，比如弹出对话框等
        callback.onRequestBefore();
        //   Log.e("okHttpHelper", "request: "+request.body().toString());
        mClientInstance.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {                //返回失败
                callbackFailure(request, callback, e);
            }
            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {                    //返回成功回调
                    String resString = response.body().string();
                    Log.d("OkHttpHelper", "onResponse: "+resString);
                    if (callback.mType == String.class) {                        //如果我们需要返回String类型
                        JSONObject jsonObject= null;
                        try {
                            jsonObject = new JSONObject(resString);
                            Object o =jsonObject.get(key);
                            callbackSuccess(response,o, callback);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        //如果返回的是其他类型，则利用Gson去解析
                        try {
                           // Object o = mGson.fromJson(resString, callback.mType);
                            JSONObject jsonObject= new JSONObject(resString);
                            Object o =jsonObject.get(key);
                            callbackSuccess(response, o, callback);
                        } catch (JsonParseException e) {
                            e.printStackTrace();
                            callbackError(response, callback, e);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {                    //返回错误
                    callbackError(response, callback, null);
                }
            }
        });
    }
/**
 * 在主线程中执行的回调
 *
 * @param response
 * @param o
 * @param callback
 */
private void callbackSuccess(final Response response, final Object o, final BaseCallback callback) {
    mHandler.post(new Runnable() {            @Override
    public void run() {
        callback.onSuccess(response, o);
    }
    });
}
/**
 * 在主线程中执行的回调
 * @param response
 * @param callback
 * @param e
 */
private void callbackError(final Response response, final BaseCallback callback, final Exception e) {
    mHandler.post(new Runnable() {            @Override
    public void run() {
        callback.onError(response, response.code(), e);
    }
    });
}    /**
 * 在主线程中执行的回调
 * @param request
 * @param callback
 * @param e
 */
private void callbackFailure(final Request request, final BaseCallback callback, final Exception e) {
    mHandler.post(new Runnable() {            @Override
    public void run() {
        callback.onFailure(request, e);
    }
    });
}
/**
 * 对外公开的get方法
 *
 * @param url
 * @param callback
 */
public void get(String url, BaseCallback callback) {
    Request request = buildRequest(url, (Map<String, String>) null, HttpMethodType.GET);
    request(request, callback);
}

    /**
     * 对外公开的get方法
     *
     * @param url
     * @param callback
     */
    public void get(String url, BaseCallback callback,String key) {
        Request request = buildRequest(url, (Map<String, String>) null, HttpMethodType.GET);
        request(request, callback,key);
    }

/**
 * 带请求参数的get方法
 */
public void get(String url, Map<String,String> params,BaseCallback callback) {
    Request request = buildRequest(url,params, HttpMethodType.GET);
    request(request, callback);
}

/**
 * 带请求参数和返回键的get方法
 * @param url
 * @param params
 * @param callback
 * @param key
 */
public void get(String url, Map<String,String> params,BaseCallback callback,String key) {
    Request request = buildRequest(url,params, HttpMethodType.GET);
    request(request, callback,key);
}


    /**
 * 对外公开的post方法
 *
 * @param url
 * @param params
 * @param callback
 */
public void post(String url, Map<String, String> params, BaseCallback callback) {
    Request request = buildRequest(url, params, HttpMethodType.POST);
    request(request, callback);
}
    /**
     * 键值对传参post 返回值带键值对
     *
     * @param url
     * @param params
     * @param callback
     */
    public void post(String url, Map<String, String> params, BaseCallback callback,String key) {
        Request request = buildRequest(url, params, HttpMethodType.POST);
        request(request, callback,key);
    }

    /**
     *  直接提交JSON数据
     *
     * @param
     * @return
     */
    public void  post(String url,String jsonStr, BaseCallback callback){
        Request request = buildRequest(url, jsonStr, HttpMethodType.POST);
        request(request, callback);
    }

    /**
     *  直接传json 返回值带键值对的post
     *
     * @param
     * @return
     */
    public void  post(String url,String jsonStr, BaseCallback callback,String key){
        Request request = buildRequest(url, jsonStr, HttpMethodType.POST);
        request(request, callback,key);
    }
    /**
     * 键值对传参post 带图片组 返回值带键值对
     *
     * @param url
     * @param params 附加参数
     * @param callback 回调
     * @param images   图片组
     */
    public void post(String url, Map<String, String> params,Map<String,File> images, BaseCallback callback,String key) {
        Request request = buildRequest(url, params,images,HttpMethodType.POST);
        request(request, callback,key);
    }
    /**
     * 键值对传参post 带图片组 返回值不带键值对
     *
     * @param url
     * @param params 附加参数
     * @param callback 回调
     * @param images   图片组
     */
    public void post(String url, Map<String, String> params,Map<String,File> images, BaseCallback callback) {
        Request request = buildRequest(url, params,images,HttpMethodType.POST);
        request(request, callback);
    }




/**
 * 构建请求对象
 *
 * @param url
 * @param params
 * @param type
 * @return
 */
private Request buildRequest(String url, Map<String, String> params, HttpMethodType type) {
    Request.Builder builder = new Request.Builder();
    builder.url(url);
    if (type == HttpMethodType.GET) {
        if(params==null)
        builder.get();
        else {
            HttpUrl.Builder urlBuilder =HttpUrl.parse(url).newBuilder();
            for(Map.Entry<String,String> item:params.entrySet()){
                urlBuilder.addQueryParameter(item.getKey(),item.getValue());
            }
            builder.url(urlBuilder.build());
        }
    } else if (type == HttpMethodType.POST) {
        builder.post(buildRequestBody(params));
              //  .removeHeader("User-Agent").addHeader("User-Agent",getUserAgent());
        builder.addHeader("Content-Type","application/json");
        builder.addHeader("Accept","application/json");
    }        return builder.build();
}

    /**
     * json构造request
     * @param url
     * @param jsonStr
     * @param type
     * @return
     */
    private Request buildRequest(String url,String jsonStr, HttpMethodType type) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (type == HttpMethodType.GET) {
            builder.get();
        } else if (type == HttpMethodType.POST) {
            builder.post(buildRequestBody(jsonStr));
            builder.addHeader("Accept","application/json");
            builder.addHeader("Content-Type","application/json");
        }        return builder.build();
    }
    /**
     * 构建请求对象
     *
     * @param url
     * @param params 参数组
     * @param type 请求类型
     * @param  images 图片组
     * @return
     */
    private Request buildRequest(String url, Map<String, String> params,Map<String,File> images, HttpMethodType type) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);        if (type == HttpMethodType.GET) {
            builder.get();
        } else if (type == HttpMethodType.POST) {
            builder.post(buildRequestBody(params,images));
//            builder.removeHeader("User-Agent").addHeader("User-Agent",getUserAgent());
            builder.addHeader("Content-Type","application/json");
            builder.addHeader("Accept","application/json");

         //   builder.addHeader("Accept-Charset","UTF-8");
        }        return builder.build();
    }
/**
 * 通过Map的键值对构建请求对象的body
 *
 * @param params
 * @return
 */
private RequestBody buildRequestBody(Map<String, String> params) {
//    String s = mGson.toJson(params);
//    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//    RequestBody body = RequestBody.create(JSON,s);

    FormEncodingBuilder builder = new FormEncodingBuilder();
    Gson gson=new Gson();
    if (params != null) {
        for (Map.Entry<String, String> entity : params.entrySet()) {
            Log.d("okHttpHelper", "buildRequestBody: "+entity.getKey()+entity.getValue());
        builder.add(entity.getKey(),entity.getValue());
    }
    }
   return builder.build();
//    return  body;
}

    /**
     * 通过JSON字符串构造body
     *
     * @param
     * @return  RequestBody
     */
    private RequestBody buildRequestBody(String jsonStr) {
    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    RequestBody body = RequestBody.create(JSON,jsonStr);
    return  body;
    }

    /**
     * 通过文件和参数构造body
     * @param params 参数组
     * @param files 图片组
     * @return
     */
    private RequestBody buildRequestBody(Map<String, String> params, Map<String,File> files){
                MultipartBuilder builder=new MultipartBuilder();
                builder.type(MultipartBuilder.FORM);
                for(Map.Entry<String,File> entry:files.entrySet()){  //添加图片组
                   builder.addFormDataPart(entry.getKey(),entry.getValue().getName(),RequestBody.create(MediaType.parse("image/jpeg"),entry.getValue()));
                }
               for (Map.Entry<String, String> entity : params.entrySet()) {
                    builder.addFormDataPart(entity.getKey(),entity.getValue());
                }
               return  builder.build();
    }

/**
 * 这个枚举用于指明是哪一种提交方式
 */
enum HttpMethodType {
    GET,
    POST
}
    private static String getUserAgent () {

        String userAgent = "";

        StringBuffer sb = new StringBuffer();

        userAgent = System.getProperty("http.agent");//Dalvik/2.1.0 (Linux; U; Android 6.0.1; vivo X9L Build/MMB29M)

        for (int i = 0, length = userAgent.length(); i < length; i++) {

            char c = userAgent.charAt(i);

            if (c <= '\u001f' || c >= '\u007f') {

                sb.append(String.format("\\u%04x", (int) c)); } else { sb.append(c); }

        }

    //    LogUtils.tag("xxx").e("User-Agent","User-Agent: "+ sb.toString());

        return sb.toString();

    }
    public static final class  MyUrlBuild{
       private  StringBuilder builder;
       boolean isfirst=true;
       public MyUrlBuild(String url){
           builder=new StringBuilder(url);
       }
       public  MyUrlBuild add(String key,String value){
           if(isIsfirst()) {
               builder.append(key + "=" + value);
           }else {
               builder.append("&"+key + "=" + value);
           }
           return  this;
       }
        public  MyUrlBuild add(String key,int value){
            if(isIsfirst()) {
                builder.append(key + "=" + value);
            }else {
                builder.append("&"+key + "=" + value);
            }
            return  this;
        }

        private boolean isIsfirst() {
           if(isfirst==true){
               builder.append("?");
             isfirst=false;
           }
           return  isfirst;
        }
        public  MyUrlBuild build (){
           return this;
        }

        @NonNull
        @Override
        public String toString() {
            return builder.toString();
        }
    }
}
