package com.example.mypotplant.maintenance.log;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypotplant.BaseActivity;
import com.example.mypotplant.R;
import com.example.mypotplant.maintenance.CameraHelper;

import java.io.File;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class EditPotLogActivity extends BaseActivity {


    ImageButton addphoto;
    ImageView photo;
    EditText word;
    TextView submit;
    CameraHelper helper;
    PotLog res;
    public  static int EDIT_POTLOG=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_potlog);
        initdata();
        initview();
    }
    void initview(){
        addphoto=findViewById(R.id.addphoto);
        photo=findViewById(R.id.photo);
        word=findViewById(R.id.edit_word);
        submit=findViewById(R.id.submit);
        addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(EditPotLogActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(EditPotLogActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else {
                      helper.openAlbum(EditPotLogActivity.this);
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                res.setWord(word.getText().toString()); //设置描述
                res.setIssuedate(Calendar.getInstance()); //设置时间
                Intent intent=new Intent();
                intent.putExtra("potlog",res);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

    }
    void initdata(){
        helper=CameraHelper.getInstance();
        res=new PotLog();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    helper.openAlbum(EditPotLogActivity.this);
                }else {
                    Toast.makeText(this,"You denied the permission",Toast.LENGTH_LONG).show();
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
                }
                break;
             default:
                 break;
        }
    }
    private void displayImage(String imagePath){
        if(imagePath!=null){
            //显示照片
            Bitmap bitmap= BitmapFactory.decodeFile(imagePath);
            photo.setImageBitmap(bitmap);
            //设置结果
            res.setImage(imagePath);
            res.setFilename(new File(imagePath).getName());
        }else {
            Toast.makeText(this,"failed to get image",Toast.LENGTH_LONG).show();
        }
    }
}
