package com.example.mypotplant.person.register;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.mypotplant.http.BaseCallback;
import com.example.mypotplant.person.login.LoginActivity;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;


/**
 * Created by MXL on 2019/12/16
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class RegisterPresenter implements IRegisterPresenter {

    IRegisterView mView;
    RegisterModel mModel;
    Context mContext;
    RegisterPresenter(IRegisterView view,Context context){
        mView=view;
        mModel=new RegisterModel();
        mContext=context;
    }
    private  User mUser;
    /**
     * <br>引用类：{@link }<br/>
     *  执行注册的逻辑操作
     *  去掉Actual_User与User互相转化 Add by MXL 2020-7-5
     * @param
     * @return
     */
    @Override
    public void doRegister() {
        Actual_User aUser=mView.getRegiser_mes();
        Log.d("RegisterActivity", "doRegister:  "+aUser.getUser_phone_number()+" "+aUser.getUser_name()+" "+aUser.getUser_password());
        if( mModel.isPass(aUser)) {
            Log.d("RegisterActivity", "doRegister: 111 ");
            mUser = UserUtils.ActualUserToUser(aUser);  //User转化
            mModel.putdata(mUser, new BaseCallback<Integer>() {
                @Override
                public void onRequestBefore() {


                }

                @Override
                public void onFailure(Request request, Exception e) {
                     Log.e("RegisterPresenter", "onFail: ");
                }

                @Override
                public void onSuccess(Response response, Integer integer) {
                    Log.d("RegisterActivity", "onSuccess: 111");
                    if (integer.intValue() == 0) {
                        Toast.makeText(mContext, IRegisterModel.REGISTER_SUCCESS, Toast.LENGTH_SHORT).show();
                        //跳转登录
                        LoginActivity.actionStart(mContext);
                       }else
                        mView.Toast_register_res(integer.intValue());
                }
                @Override
                public void onError(Response response, int errorCode, Exception e) {
                    Log.d("RegisterActivity", "onError: "+errorCode);
                }
            }, "result");
        }else{
            Toast.makeText(mContext,mModel.getTip_info(),Toast.LENGTH_SHORT).show();
        }
    }
}
