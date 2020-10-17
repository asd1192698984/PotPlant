package com.example.mypotplant.maintenance;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.mypotplant.LazyBaseFragment;
import com.example.mypotplant.MainActivity;
import com.example.mypotplant.R;
import com.example.mypotplant.maintenance.dataCentre.DataCentreActivity;

import java.io.File;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by MXL on 2020/1/14
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class MaintenanceFragment extends LazyBaseFragment implements IMaintenanceView {
    ImageButton take_photo;
    MaintenancePresenter mPresenter;
    RecyclerView recyclerView;
    String TAG="MaintenanceFragment";
    File imagefile;
    ImageButton center;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: ");
        View rootView = inflater.inflate(R.layout.fragment_maintanance, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: ");
        initData();
        initView();
    }

    @Override
    public void fillRecycleview(PlantPhotoAdapt adapt) {
        if(recyclerView!=null) {
            GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapt);
        }
    }


    @Override
    public View initView() {
        MainActivity mainActivity =(MainActivity)getActivity();
//        MaintenanceFragment fragment=(MaintenanceFragment) mainActivity.getSingleFragment(1);      //获取对应碎片
        take_photo=getView().findViewById(R.id.maintanance_take_photo);
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               imagefile= mPresenter.addPhoto(MaintenanceFragment.this);
            }
        });
        recyclerView = getView().findViewById(R.id.recyclerview_maintanance);
        mPresenter.initRecycleview();
        center=getView().findViewById(R.id.imgbt_datacenter);
        center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataCentreActivity.actionStart(getActivity());
            }
        });
        return null;
    }

    @Override
    public void initData() {
       mPresenter=new MaintenancePresenter(this,getActivity());
    }

    @Override
    protected void lazyLoad() {
        Log.d(TAG, "lazyLoad: ");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case CameraHelper.TAKE_PHOTO :
                 if(resultCode==RESULT_OK){
                     try{
                       //拍完照后进行addData
                         if(imagefile!=null){
                             mPresenter.addData(imagefile);
                         }
                     }catch (Exception e){
                         e.printStackTrace();
                     }
                 }
                break;
            default:
                break;
        }
    }
}
