package com.example.mypotplant.homepager.flower;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mypotplant.BaseActivity;
import com.example.mypotplant.R;
import com.google.android.material.tabs.TabLayout;

public class FlowershowActivity extends BaseActivity {
  public static void  actionStart(Context context,Flower flower){
      Intent intent=new Intent(context,FlowershowActivity.class);
      intent.putExtra("flower",flower);
      context.startActivity(intent);
  }
    Flower flower;
   ImageView ima_flower;
   TextView flower_name;
   TextView open_words;
   TextView light_condi;
   TextView  suit_humi;
   TextView ph;
   TextView suit_temp;
   TextView no_suit_temp;

   TextView tv_water_time;
   TextView tv_fetilizer_time;
   TextView tv_spary_time;

   TabLayout tabLayout;
   int[] icons=new int[]{R.mipmap.item_spring,R.mipmap.item_summer,R.mipmap.item_autumn,R.mipmap.item_winter};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flowershow);
        //显示布局
        flower=(Flower) getIntent().getSerializableExtra("flower");
        initview();
        setdata(flower);
        inittablayout();
    }

    /**
     *  初始化控件
     *
     * @param
     * @return
     */
    private  void initview(){
        ima_flower=(ImageView) findViewById(R.id.ima_view_flower_show);
        flower_name=(TextView)findViewById(R.id.tv_flowershow_name);
        open_words=(TextView)findViewById(R.id.tv_flower_open_word);
        light_condi=(TextView)findViewById(R.id.tv_flower_light_condi);
        suit_humi=(TextView)findViewById(R.id.tv_flower_suit_humidity);
        ph=(TextView)findViewById(R.id.tv_flower_PH);
        suit_temp=(TextView)findViewById(R.id.tv_flower_suit_temp);
        no_suit_temp=(TextView)findViewById(R.id.tv_flower_unfit_temp);
        tv_water_time=findViewById(R.id.tv_water_time);
        tv_fetilizer_time=findViewById(R.id.tv_fetilizer_time);
        tv_spary_time=findViewById(R.id.tv_spary_time);
        tabLayout=findViewById(R.id.tablayout);
    }

    /**
     * 装填控件内容
     *
     * @param
     * @return
     */
    private void setdata(Flower flower){
        if(ima_flower!=null){
            Glide.with(this).load(flower.getUrl()).into(ima_flower);
        }
        if(flower_name!=null){
            flower_name.setText(flower.getName());
        }
        if(open_words!=null){
            open_words.setText(flower.getOpening_word());
        }
        if(light_condi!=null){
            light_condi.setText(flower.getLight_condition());
        }
        if(suit_humi!=null){
            suit_humi.setText(flower.getSuit_humidity());
        }
        if(ph!=null){
            ph.setText(flower.getPh_value());
        }
        if(suit_temp!=null){
            suit_temp.setText(flower.getSuit_temp());
        }
        if(no_suit_temp!=null){
            no_suit_temp.setText(flower.getNo_suit_temp());
        }
        //写死的数据
        randomData();
    }

    /**
     * 装填tablayout
     */
    private void  inittablayout(){
        for(int i=0;i<icons.length;i++) {
            tabLayout.addTab(tabLayout.newTab());
        }
        for(int i=0;i<icons.length;i++) {
            tabLayout.getTabAt(i).setIcon(icons[i]);
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                randomData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private  void randomData(){
        if(tv_water_time!=null){
            int sult=(int)(Math.random()*15);
            if(sult==0){
                tv_water_time.setText("不需");
            }else{
                tv_water_time.setText(sult+"天一次");
            }
        }
        if(tv_fetilizer_time!=null){
            int sult=(int)(Math.random()*15);
            if(sult==0){
                tv_fetilizer_time.setText("不需");
            }else{
                tv_fetilizer_time.setText(sult+"天一次");
            }
        }
        if(tv_spary_time!=null){
            int sult=(int)(Math.random()*15);
            if(sult==0){
                tv_spary_time.setText("不需");
            }else{
                tv_spary_time.setText(sult+"天一次");
            }
        }
    }
}
