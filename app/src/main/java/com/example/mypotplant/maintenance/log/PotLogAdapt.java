package com.example.mypotplant.maintenance.log;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mypotplant.R;
import com.example.mypotplant.maintenance.Plant;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by MXL on 2020/7/12
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class PotLogAdapt extends RecyclerView.Adapter<PotLogAdapt.ViewHolder> {

    Context context;
    List<PotLog> potLogs;
    PotlogEditListener editlistener;
    public  static final String TAG="PotLogAdapt";
    PotLogAdapt(Context context,List<PotLog> potLogs){
        this.context=context;
        this.potLogs=potLogs;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      final  ViewHolder holder=new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.potlog_item,parent,false));
       holder.menu.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               showPopupMenu(v, new PopupMenu.OnMenuItemClickListener() {
                   @Override
                   public boolean onMenuItemClick(MenuItem item) {
                       switch (item.getItemId()){
                           case  R.id.delete:
                               if(editlistener!=null){
                                   editlistener.onDelete(holder.getAdapterPosition());
                               }
                               break;
                           case R.id.more:
                               if(editlistener!=null){
                                   editlistener.onFindMore();
                               }
                               break;
                              default:
                                  break;
                       }
                       return true;
                   }
               });
           }
       });
      return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //设置图片 测试 使用统一图片
//        holder.image.setImageResource(R.drawable.air_pineapple_card);
        //设置图片
        if( potLogs.get(position).getImage()!=null) {
            Glide.with(context).load(Uri.fromFile(new File(potLogs.get(position).getImage()))).into(holder.image);
        }
        else {
            Glide.with(context).load(Plant.planturl +potLogs.get(position).getUrl()).into(holder.image);
        }
        //设置时间
//        Calendar cal = potLogs.get(position).getIssuedate();
        holder.date.setText( potLogs.get(position).getTime());     //Add by XML 2020/7/26 直接通过时间字符串获取
        //设置描述
        holder.desc.setText(potLogs.get(position).getWord());
    }

    @Override
    public int getItemCount() {
        return potLogs.size();
    }

    class  ViewHolder extends  RecyclerView.ViewHolder{
        TextView desc;
        TextView date;
        ImageView image;
        ImageButton menu;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            desc=(TextView) itemView.findViewById(R.id.desc);
            date=(TextView) itemView.findViewById(R.id.date);
            image=(ImageView)itemView.findViewById(R.id.image);
            menu=(ImageButton)itemView.findViewById(R.id.menu);
        }
    }

    /**
     * 增加一条记录
     */
    public  void adddata(PotLog potLog){
        Log.d(TAG, "adddata: ");
      potLogs.add(potLog);
      notifyItemInserted(potLogs.size()-1);
    }
    /**
     * 删除一条记录
     */
    public void reomveData(int position){
        potLogs.remove(position);
        notifyItemChanged(position);
        notifyDataSetChanged();
    }

    public void setEditlistener(PotlogEditListener editlistener) {
        this.editlistener = editlistener;
    }
    /*
     * 点击菜单键弹出菜单对象
     */
    private void showPopupMenu(View v, PopupMenu.OnMenuItemClickListener listener) {
        //定义PopupMenu对象
        PopupMenu popupMenu = new PopupMenu(context,v);
        //设置PopupMenu对象的布局
        popupMenu.getMenuInflater().inflate(R.menu.potlog_menu, popupMenu.getMenu());
        //设置PopupMenu的点击事件
        popupMenu.setOnMenuItemClickListener(listener);
        //显示菜单
        popupMenu.show();
    }

    interface PotlogEditListener{
        void onDelete(int position);
        void onFindMore();
    }
}
