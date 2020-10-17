package com.example.mypotplant.http;

import com.google.gson.internal.$Gson$Types;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by MXL on 2019/12/13
 * <br>类描述：基本的回调<br/>
 *
 * @version 1.0
 * @since 1.0
 */

public abstract class BaseCallback<T> {
    /**
 * type用于方便JSON的解析
 */
public Type mType;    /**
 * 把type转换成对应的类，这里不用看明白也行。
 *
 * @param subclass
 * @return
 */
static Type getSuperclassTypeParameter(Class<?> subclass) {
    Type superclass = subclass.getGenericSuperclass();        if (superclass instanceof Class) {            throw new RuntimeException("Missing type parameter.");
    }
    ParameterizedType parameterized = (ParameterizedType) superclass;        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
}
/**
 * 构造的时候获得type的class
 */
public BaseCallback() {
    mType = getSuperclassTypeParameter(getClass());
}
/**
 * 请求之前调用
 */
public abstract void onRequestBefore();    /**
 * 请求失败调用（网络问题）
 *
 * @param request
 * @param e
 */
public abstract void onFailure(Request request, Exception e);    /**
 * 请求成功而且没有错误的时候调用
 *
 * @param response
 * @param t
 */
public abstract void onSuccess(Response response, T t);    /**
 * 请求成功但是有错误的时候调用，例如Gson解析错误等
 *
 * @param response
 * @param errorCode
 * @param e
 */
public abstract void onError(Response response, int errorCode, Exception e);

}
