package com.example.mypotplant.person;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.mypotplant.BaseActivity;
import com.example.mypotplant.R;
import com.example.mypotplant.http.BaseCallback;
import com.example.mypotplant.http.OkHttpHelper;
import com.example.mypotplant.person.register.User;
import com.example.mypotplant.utils.MeatureUtils;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.HashMap;
import java.util.Map;

public class EditInfoActivity extends BaseActivity {

    /**
     * <br>引用类：{@link }<br/>
     *  启动传递user和mode
     * @param
     * @return
     */
    public  static  void actionStart(Context context){
        Intent intent=new Intent(context,EditInfoActivity.class);
        intent.putExtra("mode",MODE_FIND);
        context.startActivity(intent);
    }
    public  final  static  int MODE_FIND=0;
    public  final  static  int MODE_MODIFY=1;
    private static final String TAG = "EditInfoActivity";
    boolean mode;
    EditText name;
    EditText home;
    EditText sign;
    Button edit;
    User user;
    RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        initdata();
        initview();
        uploadinfo();
    }
    private  void initdata(){
        mode=getIntent().getIntExtra("mode",MODE_FIND)==MODE_FIND?false:true;
        user=(User)getIntent().getSerializableExtra("user");
    }
    private  void initview(){
      name=findViewById(R.id.edit_name);
      home=findViewById(R.id.edit_home);
      sign=findViewById(R.id.edit_sign);
      relativeLayout=findViewById(R.id.parent);
      setEditTextEnable(name,false);
      setEditTextEnable(home,false);
      setEditTextEnable(sign,false);
       if(mode){
          addEdit();
          edit.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  setEditTextEnable(name,true);
                  setEditTextEnable(home,true);
                  setEditTextEnable(sign,true);
              }
          });
       }
    }
    private  void setEditTextEnable(EditText editText,boolean mode){
        editText.setFocusable(mode);
        editText.setFocusableInTouchMode(mode);
        editText.setLongClickable(mode);
        editText.setInputType(mode? InputType.TYPE_CLASS_TEXT:InputType.TYPE_NULL);
    };
    private  void addEdit(){
        //设置父布局
        LinearLayout linearLayout=new LinearLayout(this);
        RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        linearLayout.setLayoutParams(lp);
        linearLayout.setBackgroundColor(getResources().getColor(R.color.white));
        edit=new Button(this);
        LinearLayout.LayoutParams lp2=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.setMargins(MeatureUtils.dip2px(this,10),MeatureUtils.dip2px(this,5),MeatureUtils.dip2px(this,10),MeatureUtils.dip2px(this,5));
        edit.setLayoutParams(lp2);
        edit.setText("编辑资料");
        edit.setTextSize(22);
        edit.setBackground(getResources().getDrawable(R.drawable.bt_radius_blue));
        linearLayout.addView(edit);
        relativeLayout.addView(linearLayout);
    }
    private  void uploadinfo(){
       if(user!=null){
           name.setText(user.getUser_name());
           home.setText(user.getAddress());
           sign.setText(user.getSign());
       }
    }
    @Override
    public void onBackPressed() {
        if(mode==true){ //可修改的
            //修改数据给服务端
            User u=new User();
            u.setAddress(name.getText().toString());
            u.setUser_name(home.getText().toString());
            u.setSign(sign.getText().toString());
            putinfo2Server(u);
            //返回数据
            Intent intent=new Intent();
            intent.putExtra("username",name.getText().toString());
            intent.putExtra("home",home.getText().toString());
            intent.putExtra("sign",sign.getText().toString());
            setResult(RESULT_OK,intent);
        }
        super.onBackPressed();
    }
    private void putinfo2Server(User user){
        OkHttpHelper helper=OkHttpHelper.getinstance();
        Map data=new HashMap();
        data.put("user_name",user.getUser_name());
        data.put("user_address",user.getAddress());
        data.put("user_signature",user.getSign());
        helper.post(OkHttpHelper.URL_BASE + OkHttpHelper.URL_UPDATE_INFO, new Gson().toJson(data),
                new BaseCallback<String>() {
                    @Override
                    public void onRequestBefore() {

                    }

                    @Override
                    public void onFailure(Request request, Exception e) {

                    }

                    @Override
                    public void onSuccess(Response response, String s) {
                        Log.d(TAG, "onSuccess: putinfo"+s);
                    }

                    @Override
                    public void onError(Response response, int errorCode, Exception e) {

                    }
                });
    }

}
