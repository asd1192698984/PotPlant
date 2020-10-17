package com.example.mypotplant.person;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mypotplant.BaseActivity;
import com.example.mypotplant.R;
import com.example.mypotplant.http.BaseCallback;
import com.example.mypotplant.http.OkHttpHelper;
import com.example.mypotplant.maintenance.CameraHelper;
import com.example.mypotplant.person.login.LoginModel;
import com.example.mypotplant.person.login.LoginPresenter;
import com.example.mypotplant.person.register.User;
import com.example.mypotplant.utils.MeatureUtils;
import com.example.mypotplant.utils.NetWorkUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ModifyInfoActivity extends BaseActivity implements View.OnClickListener {

    private final  static  String TAG="ModifyInfoActivity";
    public final  static int MODE_MODIFY=0;
    public final  static int MODE_FIND=1;
    int mode;
    private NestedScrollView scrollView;
    private CoordinatorLayout coordinatorLayout;
    private FloatingActionButton floatingActionButton;
   private LinearLayout linearLayout;
   private ImageButton addtab;
   private CircleImageView head;
   private Dialog mCameraDialog;
   private  String imagePath;
    CameraHelper helper;
    private TextView username;
    private TextView username2;
    private TextView home;
    private TextView sign;
    private Button editinfo;
    private  TextView tv_editinfo;
    String useid;
    User user;
    private int[] backimg=new int[]{R.drawable.modify_back,R.drawable.modify_back2,R.drawable.modify_back3
            ,R.drawable.modify_back4,R.drawable.modify_back5,R.drawable.modify_back6};
    public  static  void actionStart(Context context,User user,int mode){
        Intent intent=new Intent(context,ModifyInfoActivity.class);
        intent.putExtra("user",user);
        intent.putExtra("mode",mode);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_info);
        initdata();
        initview();
        updatainfo();
        changeback();
    }
    private  void initdata(){
//        user=(User)getIntent().getSerializableExtra("user");
       mode=getIntent().getIntExtra("mode",0);
       user=(User) getIntent().getSerializableExtra("user");
    }

    /**
     * 获取登录用户资料
     * 加载资料
     */
    private  void getinfo(){
        if(NetWorkUtils.networkAvailable(this)) {
            OkHttpHelper helper = OkHttpHelper.getinstance();
            Map data = new HashMap();
            data.put("user_id",LoginModel.getUser().getUser_phone_number());
            Log.d(TAG, "getinfo: "+new Gson().toJson(data));
            helper.post(OkHttpHelper.URL_BASE + OkHttpHelper.URL_GET_INFO, new Gson().toJson(data), new BaseCallback<String>() {
                @Override
                public void onRequestBefore() {

                }

                @Override
                public void onFailure(Request request, Exception e) {

                }

                @Override
                public void onSuccess(Response response, String s) {
                    Log.d(TAG, "onSuccess: "+s);
                        try {
                            Log.d(TAG, "onSuccess: "+1111);
                            JSONObject jsonObject=new JSONObject(s);
                            user=new User();
                            user.setUser_phone_number(LoginModel.getUser().getUser_phone_number());
                            user.setUser_name(jsonObject.getString("user_name"));
                            user.setAddress(jsonObject.getString("user_address")==null?User.DEFAULT_ADDRESS:jsonObject.getString("user_address"));
                            user.setSign(jsonObject.getString("user_signature")==null?User.DEFAULT_SING:jsonObject.getString("user_signature"));
                            user.setUrl(jsonObject.getString("user_pic"));
                            //更新信息
                            updatainfo();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                }

                @Override
                public void onError(Response response, int errorCode, Exception e) {
                    Log.d(TAG, "onError: "+errorCode);
                }
            });
        }else {
            Toast.makeText(ModifyInfoActivity.this,"请检查网络设置",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 设置用户资料
     */
    private  void updatainfo(){
      if(user!=null){
        username.setText(user.getUser_name());
        username2.setText(user.getUser_name());
        Glide.with(this).load(user.getUrl()).placeholder(R.drawable.non_login).into(head);
        home.setText(user.getAddress());
        sign.setText(user.getSign());
      }else {
          getinfo();
      }
    };
    private  void initview(){
        username=findViewById(R.id.tv_username);
        username2=findViewById(R.id.tv_username2);
        home=findViewById(R.id.tv_home);
        sign=findViewById(R.id.tv_sign);
        scrollView=findViewById(R.id.nestscrollview);
        scrollView.setAlpha(0.7f);
        linearLayout=findViewById(R.id.tabs);
        //添加按钮
        addtab=new ImageButton(this);
        addtab.setImageResource(R.drawable.add);
        addtab.setBackgroundColor(getResources().getColor(R.color.smssdk_common_transparent));
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(MeatureUtils.dip2px(this,20),MeatureUtils.dip2px(this,20));
        lp.setMargins(MeatureUtils.dip2px(this,20),0,0,0);
        addtab.setLayoutParams(lp);
        linearLayout.addView(addtab);
        addtab("学习");
        addtab("电影");
        addtab("技术宅");
        //头像
        head=findViewById(R.id.Cirv_head_portrait);
        head.setOnClickListener(this);
        //修改资料
        editinfo=findViewById(R.id.btn_edit_info);
        editinfo.setOnClickListener(this);
        if(mode==MODE_FIND){ //如果为查看模式 改变按钮效果
            editinfo.setText("关注");
        }
        tv_editinfo=findViewById(R.id.tv_edit_info);
        tv_editinfo.setOnClickListener(this);
        coordinatorLayout=findViewById(R.id.coordinatorlayout);
        floatingActionButton=findViewById(R.id.my_head);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode==MODE_MODIFY)
                changeback();
            }
        });
    }

    /**
     * 添加个性标签
     */
    private void addtab(String tabtitle){
        linearLayout.removeView(addtab);
        TextView textView=new TextView(this);
        textView.setBackgroundResource(R.drawable.textview_border);
        textView.setText(tabtitle);
        textView.setTextColor(getResources().getColor(R.color.black));
//        textView.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
//        textView.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(MeatureUtils.dip2px(this,20f),0,0,0);
        textView.setLayoutParams(lp);
        if(linearLayout!=null){
          linearLayout.addView(textView);
        }
        linearLayout.addView(addtab);
    }
    /**
     * 点击头像弹出底部按钮对话框
     */
    private  void setDialog(){
         mCameraDialog = new Dialog(this, R.style.BottomDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.info_bottom_dialog, null);
        //初始化视图
        root.findViewById(R.id.btn_choose_img).setOnClickListener(this);
        root.findViewById(R.id.btn_open_camera).setOnClickListener(this);
        root.findViewById(R.id.btn_cancel).setOnClickListener(this);
        mCameraDialog.setContentView(root);
        Window dialogWindow = mCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
//        dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        mCameraDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Cirv_head_portrait:
                if(mode==MODE_MODIFY)
                setDialog();
                break;
            case R.id.btn_choose_img:
                openAlbum();
                mCameraDialog.dismiss();
//                Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_open_camera:
                mCameraDialog.dismiss();
//                Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_cancel:
                if(mCameraDialog!=null){
                    mCameraDialog.dismiss();
                }
//                Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_edit_info: //修改信息
                if(mode==MODE_MODIFY){  //用户自己点击 可修改模式
                    Intent intent=new Intent(this,EditInfoActivity.class);
                    User user=new User();
                    user.setUser_name(username.getText().toString());
                    user.setAddress(home.getText().toString());
                    user.setSign(sign.getText().toString());
                    intent.putExtra("user",user);
                    intent.putExtra("mode",EditInfoActivity.MODE_MODIFY);
                    startActivityForResult(intent,MODE_MODIFY);
                }else  if(mode==MODE_FIND){
                    Log.d(TAG, "onClick: 点击了关注");
                   //关注用户
                    if(editinfo.isEnabled()) {
                        addFollow(user.getUser_phone_number());
                    }else {
                    }
                }
                break;
            case R.id.tv_edit_info:
                if(mode==MODE_MODIFY){
                    Intent intent=new Intent(this,EditInfoActivity.class);
                    startActivityForResult(intent,MODE_MODIFY);
                }else  if(mode==MODE_FIND){

                }
                break;

        }
    }
    private  void openAlbum(){
        helper=CameraHelper.getInstance();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else {
            helper.openAlbum(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    helper.openAlbum(this);
                }else {
                    Toast.makeText(this,"拒绝权限，无法打开相册",Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case CameraHelper.CHOOSE_PHOTO:
                if(resultCode==RESULT_OK){
                    if(Build.VERSION.SDK_INT>=19){
                        displayImage( helper.handleImageOnKitKat(this,data));
                    }else {
                        displayImage( helper.handleImageBeforeKitKat(this,data));
                    }
                    uploadPhoto();
                }
                break;
            case  MODE_MODIFY:
                if(resultCode== Activity.RESULT_OK) {  //修改
                    username.setText(data.getStringExtra("username"));
                    username2.setText(data.getStringExtra("username"));
                    home.setText(data.getStringExtra("home"));
                    sign.setText(data.getStringExtra("sign"));
                }
                break;
            default:
                break;
        }
    }
    /**
     * 展示图片
     * 传递数据
     * @param imagePath
     */
    private void displayImage(String imagePath){
        if(imagePath!=null){
            //显示照片
            Log.d(TAG, "displayImage: "+imagePath);
            Bitmap bitmap= BitmapFactory.decodeFile(imagePath);
            head.setImageBitmap(bitmap);
            //设置结果
            this.imagePath=imagePath;
        }else {
            Toast.makeText(this,"failed to get image",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 上传头像
     */
    private void uploadPhoto(){
        if(LoginPresenter.carryLoginState(this)){
            if(NetWorkUtils.networkAvailable(this)){
                OkHttpHelper helper=OkHttpHelper.getinstance();
                Map<String,String> data=new HashMap();
                data.put("user_id", LoginModel.getUser().getUser_phone_number()); //手机号
                //图片组
                Map<String, File> images=new HashMap<>();
                images.put("file",new File(imagePath));
                helper.post(OkHttpHelper.URL_BASE + OkHttpHelper.URL_UPLOAD_HEAD,data,images,new BaseCallback<String>(){
                    @Override
                    public void onRequestBefore() {

                    }

                    @Override
                    public void onFailure(Request request, Exception e) {
                    }

                    @Override
                    public void onSuccess(Response response, String s) {
                        //得到文件名  -590a5951f7cec90a_20201801171831.jpg
                        Log.d(TAG, "onSuccess: "+s);
                    }

                    @Override
                    public void onError(Response response, int errorCode, Exception e) {
                    }
                });
            }else {
                Toast.makeText(this,"当前网络不可用，请检查网络设置",Toast.LENGTH_LONG).show();
            }
            LoginPresenter.carryLoginState(this);
        }
    }
    @Override
    public void onBackPressed() {
        if(mode==MODE_MODIFY) {   //修改模式改变
            Intent intent = new Intent();
            if (imagePath != null) {
                intent.putExtra("path", imagePath);
            }
            intent.putExtra("username", username.getText().toString());
            setResult(RESULT_OK, intent);
        }
            //父类方法自动调用finish
            super.onBackPressed();
            finish();
    }

    /**
     *  添加好友
     *  要求
     *  1.联网
     *  2.登录
     */
    private void addFollow(String fan_id){
        if(NetWorkUtils.networkAvailable(this)){
            if(LoginModel.getISLogin()){
                changeFriendBtn();  //改变按钮状态
                OkHttpHelper helper=OkHttpHelper.getinstance();
                Map data=new HashMap();
                data.put("user_id",LoginModel.getUser().getUser_phone_number());
                data.put("fan_id",fan_id);
                Log.d(TAG, "addFollow: "+data);
                helper.post(OkHttpHelper.URL_BASE + OkHttpHelper.URL_ADD_FAN, new Gson().toJson(data),
                        new BaseCallback<String>() {
                    @Override
                    public void onRequestBefore() {

                    }

                    @Override
                    public void onFailure(Request request, Exception e) {
                        Log.d(TAG, "onFailure: ");
                    }

                    @Override
                    public void onSuccess(Response response, String s) {
                        Log.d(TAG, "onSuccess: 关注"+s);
                    }

                    @Override
                    public void onError(Response response, int errorCode, Exception e) {
                        Log.d(TAG, "onError: 关注"+errorCode);
                    }
                });
            }else {
                Toast.makeText(this,"请完成登录",Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this,"请检查网络设置",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 改变皮肤
     */
    private void changeback(){
        if(coordinatorLayout!=null){
            int index=(int)(Math.random()*backimg.length);
            coordinatorLayout.setBackgroundResource(backimg[index]);
        }
    }

    /**
     * 改变按钮状态
     */
    private void changeFriendBtn(){
        if(mode==MODE_FIND){
            if (editinfo.isEnabled()){
                editinfo.setText("已关注");
                editinfo.setEnabled(false);
            }
        }
    }
}
