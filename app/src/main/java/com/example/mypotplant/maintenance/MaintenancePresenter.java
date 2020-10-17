package com.example.mypotplant.maintenance;

import android.content.Context;
import android.util.Log;

import com.example.mypotplant.person.login.LoginPresenter;
import com.example.mypotplant.utils.NetWorkUtils;

import java.io.File;

import androidx.fragment.app.Fragment;

/**
 * Created by MXL on 2020/1/15
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class MaintenancePresenter  implements IMaintenancePresenter {
  MaintenanceModel mModel;
    IMaintenanceView mview ;
    String TAG ="MaintenancePresenter";
    Context mContext;
    MaintenancePresenter(IMaintenanceView view, Context context){
        mview=view;
        mContext=context;
        mModel=new MaintenanceModel(mContext);
    }
    @Override
    public File addPhoto(Fragment fragment) {
       if(LoginPresenter.carryLoginState(fragment.getActivity())) {
           if(NetWorkUtils.networkAvailable(mContext)) { //新增检查网络功能 Add By MXL 2020/7/17
               CameraHelper helper = CameraHelper.getInstance();
               //执行拍照逻辑 返回路径
               File file = helper.do_take_photo(fragment, helper.getPhotofilename(fragment.getActivity(), "image", CameraHelper.filedirs));
               return file;
           }else {
               return null;
           }
       }else {
           return  null;
       }
    }

    @Override
    public void removePhoto() {

    }

    /*
     *  加载适配器数据 耗时操作 应放在lazyload
     * @param
     * @return
     */
    @Override
    public void initRecycleview() {
        Log.d(TAG, "initRecycleview: ");
        mModel.setListener(new MaintenanceModel.ModelListener() {
            @Override
            public void afterListLoad() {
                // 加载完数据后装填适配器
                mview.fillRecycleview(mModel.getPhotoAdapt());
            }
        });
        //mModel先加载数据
        mModel.loadlist();
    }

    @Override
    public void addData(File file) {
        mModel.addData(new Plant(file.getPath(), file.getName()));
    }

}
