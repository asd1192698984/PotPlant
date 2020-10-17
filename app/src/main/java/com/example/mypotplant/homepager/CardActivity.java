package com.example.mypotplant.homepager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.mypotplant.BaseActivity;
import com.example.mypotplant.R;

public class CardActivity extends BaseActivity {

     public  static  void actionStart(Context context,int imageID){
         Intent intent=new Intent(context,CardActivity.class);
         intent.putExtra("imageid",imageID);
         context.startActivity(intent);
     }
    ImageView mview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        mview=findViewById(R.id.ima_view_card_activ);
        mview.setImageResource(getIntent().getIntExtra("imageid",0));
    }
}
