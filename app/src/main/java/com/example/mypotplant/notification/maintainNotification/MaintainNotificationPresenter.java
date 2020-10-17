package com.example.mypotplant.notification.maintainNotification;

import android.content.Context;

/**
 * Created by MXL on 2020/3/28
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class MaintainNotificationPresenter implements IMaintainNotificationPresenter {

    MaintainNotificationModel mModel;
    IMaintainNotificationView mView;
    Context mContext;
    MaintainNotificationPresenter(IMaintainNotificationView view, Context context){
        mView=view;
        mContext=context;
        mModel=new MaintainNotificationModel(mContext);
    }
    @Override
    public void addNotifi(MaintainNotifi notifi) {
     mModel.addData(notifi);
    }

    @Override
    public void initRecycleview() {
        mView.fillRecycleview(mModel.getPhotoAdapt());
    }

    @Override
    public void loadlist() {
        mModel.loadlist();
    }
}
