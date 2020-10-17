package com.example.mypotplant.person.register;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mypotplant.BaseActivity;
import com.example.mypotplant.R;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import static com.example.mypotplant.MainActivity.PERMISSIONS_REQUEST_CODE;

/**
 * Created by MXL on 2020/1/18
 * <br>类描述：用于短信验证的活动<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class MobActivity extends BaseActivity implements View.OnClickListener {

    EditText phone;
    EditText code;
    Button send;
    Button submit;
    boolean isgrant=false;
    boolean debug=false;
    private MobHelper mobHelper;
   public static void actionStart(Context context){
       Intent intent=new Intent(context,MobActivity.class);
       context.startActivity(intent);
   }
//    private CountDownTimerHelper mCountDownTimerHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mob);
        initview();
        initdata();
    }
    public void initview(){
      phone =findViewById(R.id.edit_mob);
      code=findViewById(R.id.edit_code);
      send=findViewById(R.id.send);
      submit=findViewById(R.id.submit);
      send.setOnClickListener(this);
      submit.setOnClickListener(this);
    };
   public void initdata(){
       mobHelper=new MobHelper();
       //为按钮添加倒计时
       mobHelper.setCountDown(send,30);
   };

    @Override
    public void onClick(View v) {
        if(!isgrant){
            applyPermission();
        }
        if (debug)
        Log.d("MobActivity", "onClick: 经过授权");
        switch (v.getId()){
            case R.id.send:
                if (debug)
                Log.d("MobActivity", "onClick: 发送验证码");
             mobHelper.getVerrificationCode(MobHelper.CN, phone.getText().toString().trim(), new MobHelper.MobGetcodeListener() {
                 @Override
                 public void onSuccess() {
                     Toast.makeText(MobActivity.this,"成功请求验证码",Toast.LENGTH_SHORT).show();
                 }

                 @Override
                 public void onfail() {
                     Toast.makeText(MobActivity.this,"请求验证码失败",Toast.LENGTH_SHORT).show();
                 }
             });
                break;
            case R.id.submit:
                Log.d("MobActivity", "onClick: 验证验证码");
                mobHelper.submitVerrificationCode(MobHelper.CN,phone.getText().toString().trim(),code.getText().toString().trim(), new MobHelper.MobSendListener() {
                    @Override
                    public void onSuccess() {
                        /*
                         * 处理验证成功的逻辑
                         * 弹出吐司提示
                         * 回到注册界面完成用户名和密码的输入
                         */
                        Toast.makeText(MobActivity.this,"验证成功",Toast.LENGTH_SHORT).show();
                        RegisterActivity.actionStart(MobActivity.this,phone.getText().toString());
                    }

                    @Override
                    public void onfail() {
                        Toast.makeText(MobActivity.this,"验证失败",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
                default:
                    break;
        }
    }
    private void applyPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            /**
             * API23以上版本需要发起写文件权限请求
             */
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE,},PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            //上面请求时候的请求码
            case PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager
                        .PERMISSION_GRANTED) {
                    //授权
                    isgrant=true;
                }
                else{
                    Toast.makeText(this,"未同意权限可能导致功能不可使用",Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mobHelper.unregister();
    }
}
