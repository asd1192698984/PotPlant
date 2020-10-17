package com.example.mypotplant.person.register;


import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;

import com.example.mypotplant.utils.MeatureUtils;
import com.mob.MobSDK;

import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by MXL on 2020/6/14
 * <br>类描述：该类为发送短信的帮助类<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class  MobHelper {
    private boolean debug=false;
    //利用Handler异步处理UI
    private  Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
           int event=msg.arg1;  //处理的事件
           int result=msg.arg2;  //返回的结果
           Object data=msg.obj;

           if(result==SMSSDK.RESULT_COMPLETE){//处理成功
               if (event==SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){ //验证成功
                   sendlistener.onSuccess();
               }else if(event == SMSSDK.EVENT_GET_VERIFICATION_CODE){ //获取验证码成功
                   codelistener.onSuccess();
               }
           }else {
               if (event==SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){ //验证成功
                   sendlistener.onfail();
               }else if(event == SMSSDK.EVENT_GET_VERIFICATION_CODE){ //获取验证码成功
                   codelistener.onfail();
               }
               ((Throwable)data).printStackTrace();
           }
        }
    };
    public final static  String CN="86";
    private  MobGetcodeListener codelistener;
    private  MobSendListener sendlistener;
    private  CountDownTimer timer;
    private  Button countbt;
    //SDK提供的回调类
    private  EventHandler eh=new EventHandler(){
        @Override
        public void afterEvent(int event, int result, Object data) {
            // TODO 此处不可直接处理UI线程，处理后续操作需传到主线程中操作
            Message msg = new Message();
            msg.arg1 = event;
            msg.arg2 = result;
            msg.obj = data;
            handler.sendMessage(msg);
        }
    };
    /**
     * 表明用户是否同意隐私协议
     * 第一个参数为true表示同意
     * 第二个参数为回调接口可空
     * 该方法在使用SDK任何方法之前调用都行
     */
    static {
        MobSDK.submitPolicyGrantResult(true, null);
    }
   public MobHelper(){
        //这里注册监听
        SMSSDK.registerEventHandler(eh);
    }

    /**
     * 发送短信服务
     * @param country 国家 如中国“86”
     * @param phone   手机号码
     * @param listener 回调接口
     */
   public   void  getVerrificationCode(String country,String phone,MobGetcodeListener listener){
       //实现回调接口
       this.codelistener=listener;
       //如果设置了timer 开始倒计时
       if(timer!=null){
           if(debug)
               Log.d("MobHelper", "getVerrificationCode: 倒计时开始执行");
           timer.start();
       }
       //执行验证短信 回调afterEvent
       SMSSDK.getVerificationCode(country, phone);
   }

    /**
     *  发送短信服务
     * @param country 国家 如中国“86”
     * @param phone 手机号码
     * @param listener 回调接口
     * @param listener2 这个参数是SDK提供的回调接口用于发送短信之前对短信的验证 由于自己的项目没有使用这个参数 所以使用详情参照官方SDK文档
     */
    public   void  getVerrificationCode(String country, String phone, MobGetcodeListener listener, OnSendMessageHandler listener2){
        getVerrificationCode( country, phone,listener);
        SMSSDK.getVerificationCode(country, phone,listener2);
    }
    /**
     * .验证验证码
     * @param country 国家 如中国“86”
     * @param phone   手机号码
     * @param listener 回调接口
     */
    public   void  submitVerrificationCode(String country, String phone,String code, MobSendListener listener){
        //实现回调接口
        this.sendlistener =listener;
        //执行验证短信 回调afterEvent
        // 提交验证码，其中的code表示验证码，如“1357”
        SMSSDK.submitVerificationCode(country, phone, code);
    }

    /**
     * 使用完EventHandler需注销，否则可能出现内存泄漏
     * 这里使用完之后手动注销
     */
    public void unregister(){
        SMSSDK.unregisterEventHandler(eh);
    }

     interface MobGetcodeListener{
       void  onSuccess();
       void   onfail();
     }
     interface MobSendListener{
        void  onSuccess();
        void   onfail();
     }

     /**
     * 为按钮设置倒计时
      * 非必要 可选
     * @param button  获取验证码的按钮实例
     * @param second   秒数
     */
     public void  setCountDown(Button button,int second){
         this.countbt=button;
         initCountDownTime(second);
     }

    /**
     * 初始化倒计时类
     * @param second  剩余秒数
     */
     private void  initCountDownTime( int second){
         //倒计时60秒,这里不直接写60000,而用1000*60是因为后者看起来更直观,每走一步是1000毫秒也就是1秒
          timer = new CountDownTimer(1000 * second, 1000) {
             @SuppressLint("DefaultLocale")
             @Override
             public void onTick(long millisUntilFinished) {
                 if (debug)
                     Log.d("MobHelper", "onTick: ");
                 if(countbt!=null) {
                     countbt.setEnabled(false);
                     countbt.setText("下一次重发剩余"+millisUntilFinished / 1000);
                     countbt.setTextSize(MeatureUtils.dip2px(countbt.getContext(),11));
                 }else {
                     if(debug)
                         Log.d("MobHelper", "onTick: 按钮不能为null");
                 }
             }
             @Override
             public void onFinish() {
                 if(countbt!=null) {
                     countbt.setEnabled(true);
                     countbt.setText("重新获取");
                 }else {
                     if(debug)
                         Log.d("MobHelper", "onFinish: 按钮不能为null");
                 }
             }
         };
     }
}
