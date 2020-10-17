package com.example.mypotplant.maintenance.dataCentre;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypotplant.BaseActivity;
import com.example.mypotplant.R;
import com.example.mypotplant.maintenance.MaintenanceModel;
import com.example.mypotplant.maintenance.Plant;
import com.example.mypotplant.maintenance.dataCentre.bean.Operation;
import com.example.mypotplant.maintenance.dataCentre.datashowfragments.ChartFragment;
import com.example.mypotplant.maintenance.dataCentre.datashowfragments.WetFragment;
import com.example.mypotplant.person.login.LoginInterceptorProxy;
import com.google.android.material.tabs.TabLayout;
import com.suke.widget.SwitchButton;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

public class DataCentreActivity extends BaseActivity implements  IDataCentreView{

    public  static String TAG="DataCentreActivity";
    public static  int FLAG_INIT_RECORDS=0;
    public  static  void actionStart(Context context){
        Intent intent=new Intent(context,DataCentreActivity.class);
        context.startActivity(intent);
    }
    private  IDataCentrePresenter presenter;
    private  TabLayout tabLayout;
    private  ViewPager datashow;
    private TabLayout potplants;
    private  Plant[] plants;  //花盆中的植株数据
    private  Fragment[] fragments;
    private  Spinner start;
    private Spinner end;
    private Button water;
    private SwitchButton light;
    private RecyclerView operationRec;
    private IDataCentrePresenter presentProxy;
    private boolean[] canWater=new boolean[MaintenanceModel.myPot.POT_SIZE];
    private boolean[] canLight=new boolean[MaintenanceModel.myPot.POT_SIZE];
    private  int[] starttime=new int[MaintenanceModel.myPot.POT_SIZE];
    private  int[] endtime=new int[MaintenanceModel.myPot.POT_SIZE];
    private TextView tip_light;
    private TextView tip_water;
    int[] humi=new int[MaintenanceModel.myPot.POT_SIZE];
    int[][][] potX=new int[MaintenanceModel.myPot.POT_SIZE][][];
    float[][][] potY=new float[MaintenanceModel.myPot.POT_SIZE][][];
    float[][] dataY=new  float[DataCentreModel.RESULT_SIZE][]; //Y轴坐标 都相对统一
    int[] icons={R.mipmap.rain,R.mipmap.sun,R.mipmap.water,R.mipmap.temp};
    int[] select_icons={R.mipmap.rain1,R.mipmap.sun1,R.mipmap.water1,R.mipmap.temp1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_centre);
        initdata();
        initview();
    }

    /**
     * 数据初始化
     */
    private  void initdata(){
        for(int i=0;i<MaintenanceModel.myPot.POT_SIZE;i++){
            canWater[i]=true;
            canLight[i]=true;
        }
        //获取植株数据
        presenter=new DataCentrePresenter(this,this);
        plants= MaintenanceModel.getMyPot().getPlants();
        Log.d(TAG, "initdata: plant="+plants);
        presenter.fillDataShowFragments();
        presentProxy=(new LoginInterceptorProxy(presenter,this)).getProxy();
    }

    /**
     * 控件初始化
     */
    private  void initview(){
        tip_light=findViewById(R.id.tv_tip_light);
        tip_water=findViewById(R.id.tv_tip_water);
        tabLayout=findViewById(R.id.tablayout);
        //加标签
        for(int i=0;i<DataCentreModel.RESULT_SIZE;i++){
        tabLayout.addTab(tabLayout.newTab());
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
               tab.setIcon(icons[tab.getPosition()]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.setIcon(select_icons[tab.getPosition()]);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        datashow=findViewById(R.id.viewpager);
        datashow.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                super.destroyItem(container, position, object);
            }
        });
        datashow.setOffscreenPageLimit(DataCentreModel.RESULT_SIZE);
        tabLayout.setupWithViewPager(datashow);
        for(int i=0;i<DataCentreModel.RESULT_SIZE;i++)
        tabLayout.getTabAt(i).setIcon(icons[i]);
        potplants=(TabLayout)findViewById(R.id.tablayout2);
        for(int i=0;i<plants.length;i++){
            if(plants[i]!=null)
            potplants.addTab(potplants.newTab().setText(plants[i].getName()));
            else
            potplants.addTab(potplants.newTab().setText("暂无植株"));
        }
        potplants.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                Log.d(TAG, "onTabSelected: "+tab.getPosition());
                //更新数据板块
                presenter.updataDataShowFragments(tab.getPosition());
                //养护记录板块
                presenter.updateOperationRecyclerView(tab.getPosition());
                //操作栏板块
                setWaterBtn(canWater[tab.getPosition()],tab.getPosition());
                setLightBtn(canLight[tab.getPosition()],starttime[tab.getPosition()],endtime[tab.getPosition()]
                        ,tab.getPosition());
                //尝试关闭计时
                shutOffTip(Operation.OPERATION_TYPE_ADD_LIGHT_INDEX,tab.getPosition());
                shutOffTip(Operation.OPERATION_TYPE_WATER_INDEX,tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        start=findViewById(R.id.spinner_pre_time);
        //适配器
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, DataCentreModel.Water_Time);
        //设置样式
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        start.setAdapter(adapter);
        end=findViewById(R.id.spinner_last_time);
        //适配器
        adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, DataCentreModel.Water_Time);
        //设置样式
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        end.setAdapter(adapter);
        //加载养护操作
        presenter.initOperationRecyclerView(0);
        water=findViewById(R.id.water);
        water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!water.isEnabled()){
                    Toast.makeText(DataCentreActivity.this,"正在浇水，不可重复浇水",Toast.LENGTH_SHORT).show();
                }else {//执行浇水的逻辑
                   presentProxy.doWater(potplants.getSelectedTabPosition());
                }
            }
        });
        light=findViewById(R.id.give_light);
//        light.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
        light.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(isChecked) { //被选中
                    if (!light.isEnabled()) {
                        Toast.makeText(DataCentreActivity.this, "正在补光，不可重复点击", Toast.LENGTH_SHORT).show();
                    } else {//执行补光的逻辑
                        presentProxy.doLight(potplants.getSelectedTabPosition(), start.getSelectedItemPosition(), end
                                .getSelectedItemPosition());
                    }
                }
            }
        });
        //这里更新按钮状态
        setWaterBtn(canWater[0],0);
        setLightBtn(canLight[0],starttime[0],endtime[0],0);
    }
//    int[] humi=new int[MaintenanceModel.myPot.POT_SIZE];
//    int[][][] potX=new int[MaintenanceModel.myPot.POT_SIZE][][];
//    float[][][] potY=new float[MaintenanceModel.myPot.POT_SIZE][][];
//    float[][] dataY=new  float[DataCentreModel.RESULT_SIZE][]; //Y轴坐标 都相对统一

    @Override
    public   void initDataShowFragments(int[] humi,int[][][] potX,float[][][] potY,float[][] dataY){
        fragments=new Fragment[DataCentreModel.RESULT_SIZE];
        //添加数据碎片
        for(int i=0;i<DataCentreModel.RESULT_SIZE;i++){
            if(i==0){
                //湿度
                fragments[i]= WetFragment.newInstance(humi[0]);
            }else
                fragments[i]= ChartFragment.newInstance(potX[0][i],potY[0][i],dataY[i]);
        }
    }


    @Override
    public void updateDataShowFragments(int[] humi, int[][][] potX, float[][][] potY, float[][] dataY,int position) {
        ((WetFragment)fragments[0]).setWetlevel(humi[position]);
        for(int i=1;i<DataCentreModel.RESULT_SIZE;i++){
            ((ChartFragment)fragments[i]).changeData(potX[position][i],potY[position][i]);
        }
    }

    @Override
    public void fillOperationRecyclerView(OperationAdapt adapt) {
        Log.d(TAG, "fillOperationRecyclerView: "+adapt.getItemCount());
        operationRec=findViewById(R.id.recyclerview);
       // opeAdapt=new OperationAdapt(this,operations);
        operationRec.setAdapter(adapt);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        operationRec.setLayoutManager(linearLayoutManager);
        FLAG_INIT_RECORDS=1;
    }

//    @Override
//    public void updateOperationsShow(int index) {
////       opeAdapt.replaceAll(operations);
//        //presenter.updateOperationRecyclerView(index);
//    }

    @Override
    public void setWaterBtn(boolean available,int index) {
        Log.d(TAG, "setWaterBtn: 设置"+index+"为"+available);
       if(potplants!=null&&index!=potplants.getSelectedTabPosition()){ //如果按钮的设置位置不是当前选择植物位置
          canWater[index]=available;
          return;
       }
      if(water!=null&&potplants!=null&&potplants.getSelectedTabPosition()==index) { //按钮的设置位置的是当前选择植株位置
          if (available) {
              water.setBackgroundColor(getResources().getColor(R.color.cornflowerblue));
          }else {
              Log.d(TAG, "setWaterBtn: 设置不可用");
              water.setBackgroundColor(getResources().getColor(R.color.smssdk_gray));
          }
          water.setEnabled(available);
          //设置标志
          canWater[potplants.getSelectedTabPosition()]=available;
      }else {
              canWater[index]=available;
      }
      String msg=new String();
      for(int i=0;i<4;i++) {
          msg+=canWater[i];
      }
        Log.d(TAG, "setWaterBtn: 设置"+msg);
    }

    @Override
    public void setLightBtn(boolean available, int startindex, int endindex,int index) {
        if(potplants!=null&&index!=potplants.getSelectedTabPosition()){ //如果按钮的设置位置不是当前选择植物位置
            canLight[index]=available;
            starttime[index]=startindex;
            endtime[index]=endindex;
            return;
        }
       if(light!=null&&start!=null&&end!=null&&
               potplants!=null&&potplants.getSelectedTabPosition()==index) {//选择位置为当前选择植株
           start.setSelection(startindex);
           end.setSelection(endindex);
           light.setEnabled(available);
           //   light.setClickable(available);
           start.setEnabled(available);
           //  start.setClickable(available);
           end.setEnabled(available);
           if (!available) {
               Log.d(TAG, "setLightBtn: 设置不可用");
               light.setChecked(true);  //设置为选择状态
           }else {
               light.setChecked(false);  //设置为未选状态
           }
           //设置标志位
           canLight[potplants.getSelectedTabPosition()]=available;
           starttime[potplants.getSelectedTabPosition()]=startindex;
           endtime[potplants.getSelectedTabPosition()]=endindex;
       }else{
//           if(potplants==null) { //第一次还没初始化
//               canLight[0] = available;
//           }else { //根据当前选择植株来放置标志
               canLight[index]=available;
               starttime[index]=startindex;
               endtime[index]=endindex;
//           }
       }
    }
    @Override
    public void setTip(int operator,String time,int index) {
        /**
         * 先判断index是否为当前选中植株
         * 是则改变时间，不是直接返回
         */
        if(index!=potplants.getSelectedTabPosition()){
            return;
        }
       switch (operator){
           case Operation.OPERATION_TYPE_ADD_LIGHT_INDEX:  //补光
               if(tip_light.getVisibility()!=View.VISIBLE) {
                   tip_light.setVisibility(View.VISIBLE);
               }
               tip_light.setText("补光时间剩余"+time);
               break;
           case Operation.OPERATION_TYPE_WATER_INDEX:  //浇水
               if(tip_water.getVisibility()!=View.VISIBLE) {
                   tip_water.setVisibility(View.VISIBLE);
               }
               tip_water.setText("浇水时间剩余"+time);
               break;
       }
    }

    @Override
    public void shutOffTip(int operator,int index) {
        /**
         * 先判断index是否为当前选中植株,是则关闭，不是则直接返回
         * 每次切换都可以尝试关闭
         */
        if(index!=potplants.getSelectedTabPosition()){
            return;
        }
        switch (operator){
            case Operation.OPERATION_TYPE_ADD_LIGHT_INDEX:  //补光
                if(tip_light.getVisibility()==View.VISIBLE) {
                    tip_light.setVisibility(View.INVISIBLE);
                }
                break;
            case Operation.OPERATION_TYPE_WATER_INDEX:  //浇水
                if(tip_water.getVisibility()==View.VISIBLE) {
                    tip_water.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }

    private void alert(String title, String content, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);// 设置标题
        // builder.setIcon(R.drawable.ic_launcher);//设置图标
        builder.setMessage(content);// 为对话框设置内容
        // 为对话框设置取消按钮
        final AlertDialog dialog = builder.create();
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定", listener);
        builder.create().show();// 使用show()方法显示对话框
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.finishTimer();
        savedata();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        FLAG_INIT_RECORDS=0;
    }

    /**
     * 关闭后要保存的数据
     * 保存时间段
     */
    private void savedata(){
        SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
        for(int i=0;i<MaintenanceModel.myPot.POT_SIZE;i++) {
            editor.putInt("startindex" + i, starttime[i]);  //放置浇水剩余时间
            editor.putInt("endindex" + i, endtime[i]);  //放置浇水剩余时间
        }
        editor.commit();
    }
}
