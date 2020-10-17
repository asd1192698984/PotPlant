package com.example.mypotplant.utils.interfaces;

/**
 * Created by MXL on 2020/9/22
 * <br>类描述：用于数据加载的接口<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public interface DataInterface<T> {
    void afterLoad(T t);
    void beforeLoad();
}
