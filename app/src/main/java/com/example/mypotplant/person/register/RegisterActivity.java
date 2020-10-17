package com.example.mypotplant.person.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypotplant.BaseActivity;
import com.example.mypotplant.R;

public class RegisterActivity extends BaseActivity implements IRegisterView, View.OnClickListener {

    /**
     * 传入手机号码进行注册
     * @param context 上下文
     * @param phone   手机号码 Add by MXL 2020-7-5
     */
   public static void  actionStart(Context context,String phone){
       Intent intent=new Intent(context,RegisterActivity.class);
       intent.putExtra("phone",phone);
       context.startActivity(intent);
   }
    TextView register_bt;
    EditText username_edit;
    EditText password_edit;
    EditText password_again_edit;//去掉邮箱 年龄等注册内容  Add by MXL 2020-7-5
    RegisterPresenter mPresenter=new RegisterPresenter(this,this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    /**
     * 控件初始化
     * 去掉邮箱 年龄等注册内容 初始化 Add by MXL 2020-7-5
     */
    private void init() {
     register_bt=(TextView)findViewById(R.id.register_botton);
     username_edit=(EditText)findViewById(R.id.register_edit_username);
     password_edit=(EditText)findViewById(R.id.register_edit_password);
     password_again_edit=(EditText)findViewById(R.id.register_edit_password_again);
     register_bt.setOnClickListener(this);
    }

    /**
     * <br>引用类：{@link }<br/>
     * 直接从控件取得信息
     * 去掉邮箱 年龄等注册信息提取  Add by MXL 2020-7-5
     */
    @Override
    public  Actual_User getRegiser_mes() {
        Actual_User user=new Actual_User();
        user.setUser_name(username_edit.getText().toString());
        user.setUser_password(password_edit.getText().toString());
        user.setUser_password_again(password_again_edit.getText().toString());//增加重复密码的提取 Add by MXL 2020-7-5
        user.setUser_phone_number(getIntent().getStringExtra("phone"));
        return user;
    }

    @Override
    public void Toast_register_res(int result) {
        if(result==1){ //001
            Toast.makeText(this, "用户名重复", Toast.LENGTH_SHORT).show();
        }else if(result==10){ //010
            Toast.makeText(this, "电话重复", Toast.LENGTH_SHORT).show();
        }else if(result==11) {//011
            Toast.makeText(this, "用户名和电话重复", Toast.LENGTH_SHORT).show();
        }
//        }else if (result==100){//100
//            Toast.makeText(this, "邮箱重复", Toast.LENGTH_SHORT).show();
//        }else if (result==101){//101
//            Toast.makeText(this, "用户名和邮箱重复", Toast.LENGTH_SHORT).show();
//        }else if (result==110){//110
//            Toast.makeText(this, "电话和邮箱重复", Toast.LENGTH_SHORT).show();
//        }else if (result==111){//111
//            Toast.makeText(this, "用户名和电话和邮箱重复", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_botton:
                Log.d("RegisterAct", "onClick: 111 ");
                mPresenter.doRegister();
                break;
                default:
                    break;
        }
    }
}
