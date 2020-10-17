package com.example.mypotplant.notification.maintainNotification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mypotplant.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by MXL on 2020/3/28
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class MaintainNotiAdapter extends RecyclerView.Adapter<MaintainNotiAdapter.MaintainNotiHolder> {

    List<MaintainNotifi> notifis;
    Context mContext;
    MaintainNotiAdapter(List<MaintainNotifi> notifis,Context context){
        this.notifis=notifis;
        mContext=context;
    }
    @NonNull
    @Override
    public MaintainNotiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final MaintainNotiHolder holder=new MaintainNotiHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.maintain_notifi_recy_item1,parent,false));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reomveData(holder.getAdapterPosition());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MaintainNotiHolder holder, int position) {
        holder.imageView.setImageResource(notifis.get(position).getImage_res());
        holder.textView.setText(notifis.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return notifis.size();
    }

    class MaintainNotiHolder  extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        Button stick;
        Button mark;
        Button delete;
        public MaintainNotiHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.ima_maintain_notifi);
            textView=itemView.findViewById(R.id.tv_maintain_notifi);
            stick=itemView.findViewById(R.id.btn_main_notifi_stick);
            mark=itemView.findViewById(R.id.btn_main_notifi_mark);
            delete=itemView.findViewById(R.id.btn_main_notifi_delete);
        }
    }
   interface MaintainNotifiItemListener{
        void OnDelete();
    }
    /*
     * 适配器加入一条数据
     */
    void  addData(MaintainNotifi notifi){
        notifis.add(notifi);
        notifyItemChanged(notifis.size()-1);
    }
    /**
     * 从适配器删除一条数据
     */
    public void reomveData(int position){
        notifis.remove(position);
        notifyItemChanged(position);
        notifyDataSetChanged();
    }

}
