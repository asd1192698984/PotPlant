package com.example.mypotplant.person.login;

import android.content.Context;
import android.widget.Toast;

import com.example.mypotplant.utils.JDKDynamicProxy;
import com.example.mypotplant.utils.NetWorkUtils;

/**
 * Created by MXL on 2020/9/25
 * <br>类描述：<br/>
 * 该类继承JDK代理类
 * 实现对方法进行操作前登录判断和联网操作的拦截
 * @version 1.0
 * @since 1.0
 */
public class LoginInterceptorProxy extends JDKDynamicProxy {

    public final static int LOGIN_INTERCEPT_TO_LOGIN=1; //直接跳转登录界面
    public final static int LOGIN_INTERCEPT_TIP=0; // 吐司提示
    private  Context context;
    private int loginInterceptType;  //默认吐司提示
    public LoginInterceptorProxy(Object target, Context context) {
        super(target);
        this.context=context;
    }

    @Override
    protected boolean before() {
       boolean issuccess=true;
      if(!NetWorkUtils.networkAvailable(context)){//没有网络
          Toast.makeText(context,"请检查网络设置",Toast.LENGTH_SHORT).show();
          issuccess=false;
          return  issuccess;
      }
      if(!LoginModel.getISLogin()){
         switch (loginInterceptType){
             case LOGIN_INTERCEPT_TIP:
                 Toast.makeText(context,"请先完成登录",Toast.LENGTH_SHORT).show();
                 issuccess=false;
                 return  issuccess;
             case LOGIN_INTERCEPT_TO_LOGIN:
                LoginPresenter.carryLoginState(context);
                 issuccess=false;
                 return  issuccess;
         }
      }
      return  issuccess;
    }
    @Override
    protected void after() {

    }

    /**
     * 限制值
     * @param loginInterceptType 限制值范围在0 1
     */
    public void setLoginInterceptType(int loginInterceptType) {
        this.loginInterceptType = loginInterceptType;
    }
}
