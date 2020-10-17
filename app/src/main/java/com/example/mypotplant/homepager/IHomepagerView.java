package com.example.mypotplant.homepager;

/**
 * Created by MXL on 2020/2/23
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public interface IHomepagerView {
    /**
     *   利用适配器装填Recyclerview
     * @param adapt 适配器
     */
 void  fillRecycleview(CardAdapt adapt);
}
