package com.example.mypotplant.maintenance.log;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.mypotplant.maintenance.Plant;

/**
 * Created by MXL on 2020/7/12
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class PotLogPresenter implements IPotLogPresenter{
    IPotLogModel model;
    IPotLogView view;
   private Context context;
 //  private PotLogListener listener;
    PotLogPresenter(Context context, IPotLogView view, Plant plant){
        model=new PotLogModel(context,plant);
        this.view=view;
        this.context=context;
    }
    @Override
    public void initRecycleview() {
        model.loadlist(new PotLogModel.PotLogListener() {
            @Override
            public void afrerLoadlist() {
                view.fillRecycleview(model.getPhotoAdapt());
            }
        });

    }
    /**
     * 执行设置操作
     */
    @Override
    public void domodifySetting() {
        ((PotLogActivity)context).setDialogListener(new PotLogActivity.DialogListener() {
            @Override
            public void afterSetting(Object[] res) {
                model.modifySetting(res);
            }
        });
        view.modifySetting(((PotLogModel)model).getPlant());
    }

    /**
     * 执行类型选择
     */
    @Override
    public  void dotypeset(){
        Intent intent=new Intent(context, TypeSetActivity.class);
        ((Activity)context).startActivityForResult(intent,PotLogActivity.TYPESET);
//        TypeSetActivity.actionStart(context);
    }

    @Override
    public void editPotLog() {
        Intent intent =new Intent(context,EditPotLogActivity.class);
        ((Activity)context).startActivityForResult(intent,PotLogActivity.EDIT_POTLOG);
    }

    @Override
    public void addPotLog(PotLog potLog) {
        model.addData(potLog);
    }

//    public void setListener(PotLogListener listener) {
//        this.listener = listener;
//    }
//
//    interface PotLogListener{
//       void  afterEditPotLog(PotLog potLog);
//    }
}
