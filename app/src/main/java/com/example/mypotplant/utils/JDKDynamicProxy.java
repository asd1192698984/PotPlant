package com.example.mypotplant.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by MXL on 2020/9/25
 * <br>类描述：动态代理类<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public abstract class JDKDynamicProxy implements InvocationHandler {
    private Object target;

    public JDKDynamicProxy(Object target) {
        this.target = target;
    }

    /**
     * 获取被代理接口实例对象
     * @param <T>
     * @return
     */
    public <T> T getProxy() {
        return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result=null;
        if(before()){
             result = method.invoke(target, args);
        }
        after();
        return result;
    }
    protected  abstract boolean before();
    protected  abstract void after();
}
