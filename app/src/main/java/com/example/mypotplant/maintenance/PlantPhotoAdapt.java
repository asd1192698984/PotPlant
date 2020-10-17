package com.example.mypotplant.maintenance;

import android.app.Activity;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.mypotplant.R;
import com.example.mypotplant.http.BaseCallback;
import com.example.mypotplant.http.OkHttpHelper;
import com.example.mypotplant.maintenance.log.PotLogActivity;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by MXL on 2020/1/15
 * <br>类描述：植物照片的适配器<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class PlantPhotoAdapt  extends RecyclerView.Adapter<PlantPhotoAdapt.PlantViewHolder> {
   ArrayList<Plant> list;
   Activity activity;
   String TAG= "PlantPhotoAdapt";
    PhotoPopupMenuListener photolistener;
//   PopupMenu.OnMenuItemClickListener mMenulistener;
    PlantPhotoAdapt(ArrayList list,Activity activity){
        this.list=list;
        this.activity=activity;
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        final PlantPhotoAdapt.PlantViewHolder mHolder=new  PlantPhotoAdapt.PlantViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.photo_reycyclerview_item,parent,false));
        mHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=mHolder.getAdapterPosition();
                findoutDefaultimpl(position);
            }
        });
        mHolder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final int position=mHolder.getAdapterPosition();
                showPopupMenu(v, new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.photo_find_out:
                                if(photolistener!=null){
                                    photolistener.OnFindOut(position);
                                }
                                break;
                            case R.id.photo_delete:
                          if(photolistener!=null){
                              photolistener.OnDelete(position);
                          }
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });
                return true;
            }
        });
        mHolder.name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //文本改变后 对类名称进行改变
               list.get(mHolder.getAdapterPosition()).name=mHolder.name.getText().toString();
               //修改mypot
//               Plant plant;
//               if((plant=MaintenanceModel.myPot.getPlantByName(list.get(mHolder.getAdapterPosition()).getFilename()))!=null){
//                   plant.setName(mHolder.name.getText().toString());
//               }
               //将修改植物名称传给服务端
               modifyName2Server(list.get(mHolder.getAdapterPosition()).getFilename(),mHolder.name.getText().toString());
            }
        });
        return mHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        Plant plant=list.get(position);
        //使用Glide加载图片
        Log.d(TAG, "onBindViewHolder: "+Plant.planturl+plant.getUrl());
        holder.name.setText(plant.getName());
        if(plant.getPath()!=null) {
            Glide.with(activity).load(Uri.fromFile(new File(plant.getPath()))).into(holder.imageView);
        }
        else {
            Glide.with(activity).load(Plant.planturl + plant.getUrl()).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class PlantViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        EditText name;
        public PlantViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=(ImageView) itemView.findViewById(R.id.ima_view_photoitem);
            name=(EditText) itemView.findViewById(R.id.edit_name);
        }
    }
    /**
     *  提供添加数据的方法
     */
    public void addData(Plant plant){
        list.add(plant);
        Log.d("PlantPhotoAdapt", "addData: " +list.size());
        notifyItemChanged(list.size()-1);
    }
    /**
     * 提供删除数据的方法
     */
    public void reomveData(int position){
        list.remove(position);
        notifyItemChanged(position);
        notifyDataSetChanged();
    }

    public void setPhotolistener(PhotoPopupMenuListener photolistener) {
        this.photolistener = photolistener;
    }
    /*
     * 点击照片弹出菜单对象
     */
    private void showPopupMenu(View v, PopupMenu.OnMenuItemClickListener listener) {
        //定义PopupMenu对象
        PopupMenu popupMenu = new PopupMenu(activity,v);
        //设置PopupMenu对象的布局
        popupMenu.getMenuInflater().inflate(R.menu.plant_photo_menu, popupMenu.getMenu());
        //设置PopupMenu的点击事件
        popupMenu.setOnMenuItemClickListener(listener);
        //显示菜单
        popupMenu.show();
    }
    interface PhotoPopupMenuListener{
        void OnFindOut(int position);
        void OnDelete(int position);
    }

    /**
     * findout的默认实现方法
     * 启动对应植株的养护日志活动
     */
    public  void findoutDefaultimpl(int position){
        PotLogActivity.actionStart(activity,list.get(position));
    }
    private  void modifyName2Server(String filename,String newname){
        OkHttpHelper helper=OkHttpHelper.getinstance();
        Map data=new HashMap<>();
        data.put("fileName",filename);
        data.put("plantName",newname);
        helper.get(OkHttpHelper.URL_BASE2 + OkHttpHelper.URL_MODIFY_PLANT, data, new BaseCallback<String>() {
            @Override
            public void onRequestBefore() {

            }

            @Override
            public void onFailure(Request request, Exception e) {
                Log.d(TAG, "onFailure: modify name ");
            }

            @Override
            public void onSuccess(Response response, String s) {
                Log.d(TAG, "onSuccess: modify name"+s);
            }

            @Override
            public void onError(Response response, int errorCode, Exception e) {
                Log.d(TAG, "onError: modify name"+errorCode);
            }
        });
    }
}
