package com.example.mypotplant.maintenance.dataCentre;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.mypotplant.maintenance.MaintenanceModel;
import com.example.mypotplant.maintenance.dataCentre.bean.Operation;
import com.example.mypotplant.utils.TimeUtils;
import com.example.mypotplant.utils.interfaces.DataInterface;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by MXL on 2020/8/7
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class DataCentrePresenter implements IDataCentrePresenter {
    private static final String TAG = "DataCentrePresenter";
    private final   int MSG_WATER=1;
    private final   int MSG_LIGHT=2;
    private final   int TIMER_GAP=500; //0.5s调用一次
    private  Context context;
    IDataCentreView view;
    IDataCentreModel model;
    DataCentrePresenter (Context context,IDataCentreView view){
        Log.d(TAG, "DataCentrePresenter:  构造函数");
        this.context=context;
        this.view=view;
        initdata();
        model=new DataCentreModel(context);
        for(int i=0;i<MaintenanceModel.myPot.POT_SIZE;i++) {
            setWaterTimer(false, i);  //计时器重新启动
            setLightTimer(-1,-1,i,-1,-1);
        }
    }
     Handler handler = new Handler() {//实现计时器访问外部变量
          @Override
          public void handleMessage(Message msg) {
                         if (msg.what == MSG_WATER){ //浇水操作
                             int index=msg.getData().getInt("index");
                           if(waterlasttime[index]>=TIMER_GAP){
                               waterlasttime[index]-=TIMER_GAP;
                               Log.d(TAG, "handleMessage: 浇水剩余"+waterlasttime[index]/1000+"s");
                               view.setTip(Operation.OPERATION_TYPE_WATER_INDEX,TimeUtils.getmilltoHMS(waterlasttime[index]),index); //显示
                           }else {
                               waterlasttime[index]=0;
                               view.setWaterBtn(true,index); //设置按钮可用
                               Log.d(TAG, "handleMessage: 浇水完成");
                               watertimer[index].cancel();  //取消计时器
                               view.shutOffTip(Operation.OPERATION_TYPE_WATER_INDEX,index);
                           }
                         }else if(msg.what == MSG_LIGHT){//补光计时器
                             int index=msg.getData().getInt("index");
                             if(lightlasttime[index]>=TIMER_GAP){
                                 lightlasttime[index]-=TIMER_GAP;
                                 Log.d(TAG, "handleMessage: 补光剩余"+lightdelaytime[index]);
                                 view.setTip(Operation.OPERATION_TYPE_ADD_LIGHT_INDEX,TimeUtils.getmilltoHMS(lightlasttime[index]),index); //显示
                             }else {
                                 lightlasttime[index]=0;
                                 view.setLightBtn(true,0,0,index);
                                 Log.d(TAG, "handleMessage: 补光完成");
                                 watertimer[index].cancel();
                                 view.shutOffTip(Operation.OPERATION_TYPE_ADD_LIGHT_INDEX,index);
                             }
                         }
                         super.handleMessage(msg);
                     }
      };
    private  Timer[] watertimer;
    private  Timer[] lighttimer;
    private long[] waterlasttime;
    private long[] lightlasttime;
    private  long[] lightdelaytime;
    private  TimerTask watertask[];
    private  TimerTask lighttask[];

    /**
     * 初始化数据
     */
    public void  initdata(){
        watertimer=new Timer[MaintenanceModel.myPot.POT_SIZE];
        lighttimer=new Timer[MaintenanceModel.myPot.POT_SIZE];
        watertask=new TimerTask[MaintenanceModel.myPot.POT_SIZE];
        lighttask=new TimerTask[MaintenanceModel.myPot.POT_SIZE];
        waterlasttime=new long[MaintenanceModel.myPot.POT_SIZE];
        lightlasttime=new long[MaintenanceModel.myPot.POT_SIZE];
        lightdelaytime=new long[MaintenanceModel.myPot.POT_SIZE];
        for(int i=0;i<MaintenanceModel.myPot.POT_SIZE;i++){
            watertask[i]=new MyTask(i,MSG_WATER);
            lighttask[i]=new MyTask(i,MSG_LIGHT);
        }
    }
    @Override
    public void fillDataShowFragments() {
        model.updatedate(new IDataCentreModel.LoadDataListener() {
            @Override
            public void updatedata(int[] humi, int[][][] potX, float[][][] potY) {
                view.initDataShowFragments(humi,potX,potY,((DataCentreModel)model).getDataY());
            }
        });
    }
    @Override
    public void updataDataShowFragments(int postion) {
      Object[] data= model.getCurData();
      view.updateDataShowFragments((int[])data[0],(int[][][])data[1],(float[][][]) data[2],(float[][]) data[3],postion);
    }

    @Override
    public void initOperationRecyclerView(int index) {
     model.getOperationInfos(new DataInterface<List<Operation>>() {
          public void afterLoad(List<Operation> operations) {
              Log.d(TAG, "afterLoad:"+operations.size());
              //这里要先给适配器赋值 不然可能会出现线程同步问题
           //   model.getOperationAdapt().replaceAll(operations);
             view.fillOperationRecyclerView(model.getOperationAdapt());
          }
          @Override
          public void beforeLoad() {
              Log.d(TAG, "beforeLoad: 开始加载盆栽数据");
          }
        },index);
    }

    @Override
    public void updateOperationRecyclerView(int index) {
        Log.d(TAG, "updateOperationRecyclerView: "+DataCentreActivity.FLAG_INIT_RECORDS);  //标志位不变
       if(DataCentreActivity.FLAG_INIT_RECORDS==0){
           initOperationRecyclerView(index);
       }else {
           model.updateOperationInfos(index);
       }
    }
    @Override
    public void doWater(int index) {
        /**
         * 浇水的逻辑
         * 判断登录 判断有网 拦截器判断
         * 判断是否有植株
         * 改变浇水按钮状态
         * 传递数据
         * 加入记录给Adapt
         * 加入定时器判断时间，时间结束可以再次浇水
         */
        if(MaintenanceModel.myPot.getPot(index)==MaintenanceModel.myPot.POT_INAVAILABLE) {
            Log.d(TAG, "doWater: ");
            view.setWaterBtn(false,index);
            //获取当前时间
            String starttime = TimeUtils.getTimeFormat(new Date(), Operation.TIME_FORMAT);
            String endtime = TimeUtils.getTimeFormat(new Date(System.currentTimeMillis() + Operation.WATER_WAIT_TIME), Operation.TIME_FORMAT);
            Log.d(TAG, "doWater: " + starttime + " " + endtime);
            model.putRecord(index, Operation.OPERATION_TYPE_WATER_INDEX, starttime, endtime);
            setWaterTimer(true,index);
        }else {
            Toast.makeText(context,"请先放置植株",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void doLight(int index,int startindex, int endindex) {
        /**
         * 补光的逻辑
         * 判断登录 判断有网
         * 判断是否有植株
         * 判断起始时间是否大于当前时间
         * 改变补光按钮状态
         * 传递数据
         * 加入记录给Adapt
         * 开启服务判断补光是否结束
         */
        //核心逻辑
        if(MaintenanceModel.myPot.getPot(index)==MaintenanceModel.myPot.POT_INAVAILABLE) {
            Log.d(TAG, "doLight: ");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, startindex); //起始时间
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            long startmills = calendar.getTimeInMillis();
            String starttime = TimeUtils.getTimeFormat(calendar.getTime(), Operation.TIME_FORMAT);
            Log.d(TAG, "doLight: mills="+startmills+" time="+starttime);
            if (startmills>=System.currentTimeMillis()) { //延时时间大于0
                view.setLightBtn(false, startindex, endindex,index); //设置按钮状态
                calendar.set(Calendar.HOUR_OF_DAY, endindex);  //结束时间
                String endtime = TimeUtils.getTimeFormat(calendar.getTime(), Operation.TIME_FORMAT);
                long endmilss=calendar.getTimeInMillis();  //得到结束毫秒
                Log.d(TAG, "doLight: mills="+endmilss+" time="+endtime);
                model.putRecord(index, Operation.OPERATION_TYPE_ADD_LIGHT_INDEX, starttime, endtime);
                setLightTimer(startmills-System.currentTimeMillis(),endmilss-startmills,index,startindex,endindex);
            }else {
                Toast.makeText(context,"请选择正确时间段",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(context,"请先放置植株",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void finishTimer() {
        if(watertimer!=null){
            for(int i=0;i<watertimer.length;i++) {
                if(watertimer[i]!=null) {
                    watertimer[i].cancel();
                }
                if (waterlasttime[i]>=0) {
                    //  SharedPreferences.Editor editor=context.getSharedPreferences("timer", Context.MODE_PRIVATE).edit();
                    SharedPreferences.Editor editor = ((Activity) context).getPreferences(Context.MODE_PRIVATE).edit();
                    editor.putLong("waterlasttime"+i, waterlasttime[i]);  //放置浇水剩余时间
                    editor.commit();
                    Log.d(TAG, "finishTimer: 放置waterlasttime"+i + waterlasttime[i]);
                }
            }
        }
        if(lighttimer!=null){
            for(int i=0;i<lighttimer.length;i++) {
                if(lighttimer[i]!=null) {
                    lighttimer[i].cancel();
                }
                if (lightlasttime[i]>=0) {
//                SharedPreferences.Editor editor= context.getSharedPreferences("timer", Context.MODE_PRIVATE).edit();
                    SharedPreferences.Editor editor = ((Activity) context).getPreferences(Context.MODE_PRIVATE).edit();
                    editor.putLong("lightlasttime"+i, lightlasttime[i]);  //放置补光剩余时间
                    editor.putLong("lightdelaytime"+i, lightdelaytime[i]);  //放置补光延迟时间
                    editor.commit();
                    Log.d(TAG, "finishTimer:  放置lightlasttime" + lightlasttime[i]);
                    Log.d(TAG, "finishTimer:  放置lightdelaytime" + lightdelaytime[i]);
                }
            }
        }
        //存入当前时间
        SharedPreferences.Editor editor=((Activity)context).getPreferences(Context.MODE_PRIVATE).edit();
        editor.putLong("lasttime",System.currentTimeMillis());  //放置当前时间
        editor.commit();
    }
    /**
     * 提供更新数据的操作
     * @param index  位置
     * @param listener 监听
     * @deprecated
     */
    private void updateOperations(int index,DataInterface<List<Operation> > listener) {
        model.getOperationInfos(listener,index);
    }

    /**
     * 设置浇水定时器
     * 判断剩余时间是否存在且大于0
     * 否则重置
     */
    private void setWaterTimer(boolean restart,int index){
//        SharedPreferences preferences=context.getSharedPreferences("timer", Context.MODE_PRIVATE);
        //Activity获取
        SharedPreferences preferences=((Activity)context).getPreferences(Context.MODE_PRIVATE);
        long lasttime=preferences.getLong("waterlasttime"+index,-1);
        Log.d(TAG, "setWaterTimer: waterlasttime"+index+"="+lasttime);
                  //设置定时器
            if (watertimer[index] == null) {
                watertimer[index] = new Timer();
            }
            if(lasttime>0){ //剩余时间大于0  开始计时
                long curtime=System.currentTimeMillis();  //判断过去了多长时间
                long latime=preferences.getLong("lasttime",0);
                if(lasttime>(curtime-latime)){ //还有时间剩余 开始计时
                    waterlasttime[index]=lasttime-(curtime-latime);
                    Log.d(TAG, "setWaterTimer: 设置"+index+false);
                    view.setWaterBtn(false,index);
                }else { //否则时间过完了 直接返回
                    waterlasttime[index]=0;
                    watertimer[index].cancel();//取消计时器
                    return;
                }
//              startTimer(watertimer,watertask,0,waterlasttime);
            }else {  //剩余时间小于等于0
                if(restart) { //标志启动 重新计时
                    waterlasttime[index] = Operation.WATER_WAIT_TIME; //设置剩余时间
                }else {  //否则不启动 返回
                    waterlasttime[index]=0;
                    return;
                }
            }
        //开启计时器
        startTimer(watertimer[index], watertask[index], 0, TIMER_GAP);
        Log.d(TAG, "setWaterTimer"+index+": 开始浇水 剩余"+waterlasttime[index]);
    }
    private  void setLightTimer(long curdelaytime,long curlasttime,int index,int startindex,int endindex){
       // SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(MApplication.getContext());
//        SharedPreferences preferences=context.getSharedPreferences("timer", Context.MODE_PRIVATE);
        SharedPreferences preferences=((Activity)context).getPreferences(Context.MODE_PRIVATE);
        long lasttime=preferences.getLong("lightlasttime"+index,-1);
        long delaytime=preferences.getLong("lightdelaytime"+index,-1);
        long latime=preferences.getLong("lasttime",-1);
        if(startindex<0||endindex<0){  //传入参数-1 则从preferences获取
            if((startindex=preferences.getInt("startindex"+index,-1))==-1){
                startindex=0;
            }
            if((endindex=preferences.getInt("endindex"+index,-1))==-1){
                endindex=0;
            }
        }
        Log.d(TAG, "setLightTimer: startindex="+startindex+" "+"endindex="+endindex);
        //设置定时器
        if( lighttimer[index]==null)
        lighttimer[index] = new Timer();
        if(curdelaytime>=0||curlasttime>=0){ //如果当前设置了延时时间或者持续时间 覆盖保存的延时持续时间
            lighttimer[index] = new Timer();
            lightdelaytime[index]=delaytime;
          lightlasttime[index]=curlasttime;
            view.setLightBtn(false,startindex,endindex,index); //按钮状态
            //          开始计时
            startTimer(lighttimer[index], lighttask[index], lightdelaytime[index], TIMER_GAP);
          return;
        }
        //else
        if(delaytime>=0){ //延时大于等于0
            lightdelaytime[index]=delaytime;
            if(lasttime>0){ //剩余时间时间大于等于0
                long curtime=System.currentTimeMillis(); //获取当前时间
               if(latime!=-1) { //如果以前的时间存在
                   if (lasttime > curtime - latime) {  //剩余时间还没计时完成
                       lightlasttime[index] = lasttime;
                   }else{//时间用完了 直接返回
                       lightlasttime[index]=0;
                       lighttimer[index].cancel();//取消计时器
                       return;
                   }
               }
            }else { //时间用完了 直接返回
                lightlasttime[index]=0;
                lighttimer[index].cancel();//取消计时器
                return;
            }
            Log.d(TAG, "setLightTimer: 开始补光计时器");
            //开启计时器
            startTimer(lighttimer[index], lighttask[index], lightdelaytime[index], TIMER_GAP);
            //按钮置位
            view.setLightBtn(false,startindex,endindex,index);
        }else { //没有存过
             //do nothing
            return;
        }
    }

    /**
     * @param timer 定时器
     * @param task   任务
     * @param delaymills  延迟时间
     * @param gapmills    间隔时间
     */
    private void  startTimer(Timer timer, TimerTask task,long delaymills,long gapmills){
        Log.d(TAG, "startTimer: 定时器启动");
        timer.schedule(task,delaymills,gapmills);
    }

    class MyTask extends TimerTask{
        private  int index;  //位置
        private  int operator; //操作
        MyTask(int index,int operator){
            this.index=index;
            this.operator=operator;
        }
        @Override
        public void run() {
            Message message = new Message();
            message.what = operator;
            Bundle data=new Bundle();
            data.putInt("index",index);
            message.setData(data);
            handler.sendMessage(message);
        }
    }
}
