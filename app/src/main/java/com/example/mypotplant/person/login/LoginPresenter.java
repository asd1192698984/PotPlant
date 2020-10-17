package com.example.mypotplant.person.login;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.mypotplant.Judgehelps.AccountJudgeHelp;
import com.example.mypotplant.http.BaseCallback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import androidx.appcompat.app.AlertDialog;

/**
 * Created by MXL on 2019/12/12
 * <br>类描述：进行登录相关逻辑操作<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class LoginPresenter implements ILoginPresenter {

    LoginModel mModel;
    ILoginView mView;
    Context mContext;
    LoginPresenter(ILoginView view, Context context){
        mModel=new LoginModel();
        mView=view;
        mContext=context;
    }
    /**
     *  检测登录状态
     *  如未登录跳转到登录界面
     * @return  是否是登录状态
     */
    public  static  boolean  carryLoginState(final Context context){
        if(LoginModel.getISLogin()) {
            return  true;
        }
        else{
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle("操作失败");
            dialog.setMessage("请先完成登录");
            dialog.setCancelable(false);
            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    LoginActivity.actionStart(context);
                }
            }).show();
        return  false;
        }
    }

    /**
     * 完成完整的登录操作
     * @param
     * @return
     */
    @Override
    public void doLogin(final String username, final String password, final boolean ischecked) {
      mModel.getLoginAccess(username, password, new BaseCallback<Integer>() {
          @Override
          public void onRequestBefore() {
              mView.showLoginAnimation();
          }
          @Override
          public void onFailure(Request request, Exception e) {
          //    Log.d("LoginPresenter", "onFail: "+111);
          }

          @Override
          public void onSuccess(Response response, Integer result) {
              Log.d("LoginPresenter", "onSuccess: "+result);
         //     Map<String,Integer> map =LoginConstants.getLoginCodeMap();
              //对于不同的返回信息进行处理
              switch(result){
                  case LoginConstants.LOGIN_SUCCESS: //成功的处理
                      mView.showToast(LoginConstants.LOGIN_SUCCESS_tips);
                      LoginModel.setIsLogin(true);  //设置登录状态
                      if(AccountJudgeHelp.phoneNumberValidate(username)==AccountJudgeHelp.PHONE_AVAILABLE){
                          LoginModel.getUser().setUser_phone_number(username); //设置登录信息
                          Log.d("LoginPresenter", "onSuccess: "+"手机号登录");
                      }else {
                          Log.d("LoginPresenter", "onSuccess: " + "账号登录");
                          LoginModel.getUser().setUser_name(username);
                          //设置登录信息
//                          mModel.getSettingMsg();
                      }
                          LoginModel.getUser().setUser_password(password);
                      //保存密码
                      if(ischecked){
                          mModel.savePassword(username,password);
                      }else {
                          mModel.removePassword();
                      }
                      //关闭登录
                     Activity activity= (Activity)mContext;
                     activity.finish();
                   break;
                  case LoginConstants.LOGIN_INVALID_ID: //以下为异常处理
                      mView.showAlertDialog(LoginConstants.LOGIN_INVALID_ID_tips);
                      mView.initLogin();
                      break;
                  case LoginConstants.LOGIN_PASSWORD_ERROR:
                      mView.showAlertDialog(LoginConstants.LOGIN_PASSWORD_ERROR_tips);
                      mView.initLogin();
                      break;
                      default: break;
              }
          }

          @Override
          public void onError(Response response, int errorCode, Exception e) {
              Log.d("LoginPresenter", "onError: "+errorCode);
          }
      }
      );
    }

    @Override
    public void recoverPassword(EditText username, EditText password, CheckBox checkBox) {
        mModel.recoverPassword(username,password,checkBox);
    }

}
