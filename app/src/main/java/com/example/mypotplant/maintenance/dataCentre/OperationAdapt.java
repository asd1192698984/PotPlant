package com.example.mypotplant.maintenance.dataCentre;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mypotplant.R;
import com.example.mypotplant.maintenance.dataCentre.bean.Operation;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by MXL on 2020/9/21
 * <br>类描述：养护记录适配器<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class OperationAdapt extends RecyclerView.Adapter<OperationAdapt.OperationHolder> {
    private static final String TAG = "OperationAdapt";
   private Context context;
   private List<Operation> operations;
   public  OperationAdapt(Context context, List<Operation> operations){
    this.context=context;
    this.operations=operations;
   }
    @NonNull
    @Override
    public OperationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.operationinfo_item,parent,false);
        OperationHolder holder=new OperationHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OperationHolder holder, int position) {
         holder.imageView.setImageResource(operations.get(position).getdrawId());
         holder.mes.setText(operations.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return operations.size();
    }

    class  OperationHolder extends RecyclerView.ViewHolder{
       ImageView imageView;
       TextView mes;
        public OperationHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image);
            mes=itemView.findViewById(R.id.mes);
        }
    }
    /**
     *  提供添加数据的方法
     */
    public void addData(Operation operation){
        operations.add(operation);
        notifyItemChanged(operations.size()-1);
    }
    /**
     * 提供删除数据的方法
     */
    public void reomveData(int position){
        operations.remove(position);
        notifyItemChanged(position);
        notifyDataSetChanged();
    }
    /**
     * 提供替换
     */
    public void replaceAll(List<Operation> operations){
        Log.d(TAG, "replaceAll: "+operations.size());
        this.operations.clear();
        this.operations.addAll(operations);
        notifyDataSetChanged();
    }
}
