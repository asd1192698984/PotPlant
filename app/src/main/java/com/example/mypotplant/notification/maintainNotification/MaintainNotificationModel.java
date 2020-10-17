package com.example.mypotplant.notification.maintainNotification;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MXL on 2020/3/28
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class MaintainNotificationModel implements IMaintainNotificationModel {

    Context mContext;
    MaintainNotiAdapter adapter;
    List<MaintainNotifi>  notifis;
    MaintainNotificationModel (Context context){
        mContext=context;
    }
    @Override
    public MaintainNotiAdapter getPhotoAdapt() {
        if(adapter==null){
            adapter=new MaintainNotiAdapter(notifis
                    ==null?new ArrayList<MaintainNotifi>():notifis,mContext);
            return  adapter;
        }else {
            return  adapter;
        }
    }

    @Override
    public void loadlist() {
        addData(new MaintainNotifi(MaintainNotifiConstants.MaintainNotifi_Type.WATER,"绿萝"));
        addData(new MaintainNotifi(MaintainNotifiConstants.MaintainNotifi_Type.CUT,"绿萝"));
        addData(new MaintainNotifi(MaintainNotifiConstants.MaintainNotifi_Type.FERTILIZER,"兰花"));

    }

    @Override
    public void savelist() {

    }

    @Override
    public void addData(MaintainNotifi notifi) {
       adapter.addData(notifi);
    }

    @Override
    public void removeData(int position) {
     adapter.reomveData(position);
    }
}
