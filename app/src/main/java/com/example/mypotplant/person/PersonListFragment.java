package com.example.mypotplant.person;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mypotplant.LazyBaseFragment;
import com.example.mypotplant.R;
import com.example.mypotplant.person.login.LoginModel;
import com.example.mypotplant.person.register.User;
import com.example.mypotplant.utils.NetWorkUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * Created by MXL on 2020/9/5
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class PersonListFragment  extends LazyBaseFragment {
    private static final String TAG = "PersonListFragment";
   private PersonAdapt adapt;
   private   RecyclerView recyclerView;
   private   SwipeRefreshLayout swipeRefreshLayout;
   private SwipeRefreshLayout.OnRefreshListener listener;

    public static PersonListFragment newInstance() {
        Bundle args = new Bundle();
        PersonListFragment fragment = new PersonListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view=inflater.inflate(R.layout.fragment_personlist,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initView();
        implRefresh();
    }

    @Override
    public View initView() {
        swipeRefreshLayout=getView().findViewById(R.id.swiperefreshlayout);
        recyclerView=getView().findViewById(R.id.recycler_view);
        List<User> users=new ArrayList<>();  //新建空数组
        fillRecyclerView(users);
        return null;
    }

    @Override
    public void initData() {

    }

    @Override
    protected void lazyLoad() {
        if(LoginModel.getISLogin()&& NetWorkUtils.networkAvailable(getContext())) {
            startRefresh();  //可见刷新一次
            if (listener != null) {
                listener.onRefresh();
            } else {
                cancelRefresh();
            }
        }
    }

    class  PersonAdapt extends RecyclerView.Adapter<PersonAdapt.PersonHolder> {
        List<User> users;
        Context context;
        PersonAdapt(Context context, List<User> users){
            this.context=context;
            this.users=users;
        }
        @NonNull
        @Override
        public PersonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_personlist_item,parent,false);
            final PersonHolder holder=new PersonHolder(view);
            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   alert("Confirm", "确认删除关系", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           //内部删除
                           Log.d(TAG, "onClick: remove"+holder.getAdapterPosition());
                           reomveData(holder.getAdapterPosition());
                           //服务器删除
                       }
                   });
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull PersonHolder holder, int position) {
            User user=users.get(position);
            holder.username.setText(user.getUser_name());
            holder.sign.setText(user.getSign());
            Glide.with(context).load(user.getUrl()).into(holder.head);
        }

        @Override
        public int getItemCount() {
            return users.size();
        }

        class PersonHolder extends  RecyclerView.ViewHolder{
            ImageView head;
            TextView username;
            TextView sign;
            ImageButton remove;
            public PersonHolder(@NonNull View itemView) {
                super(itemView);
                head=itemView.findViewById(R.id.head);
                username=itemView.findViewById(R.id.username);
                sign=itemView.findViewById(R.id.sign);
                remove=itemView.findViewById(R.id.remove);
            }
        }
        public  void refresh(List<User> users){
            this.users.clear();
            for(User user:users){
                this.users.add(user);
            }
            notifyDataSetChanged();
        }
        /**
         *  提供添加数据的方法
         */
        public void addData(User user){
            users.add(user);
            Log.d("PlantPhotoAdapt", "addData: " +users.size());
            notifyItemChanged(users.size()-1);
        }
        /**
         * 提供删除数据的方法
         */
        public void reomveData(int position){
            users.remove(position);
            notifyItemChanged(position);
            notifyDataSetChanged();
        }
    }

    /**
     * 外部刷新碎片数据
     * @param users
     */
    public  void refreshUser(List<User> users){
        //适配器刷新
       adapt.refresh(users);
        if (swipeRefreshLayout.isRefreshing()) {
            Log.d(TAG, "refreshUser: 取消刷新");
            swipeRefreshLayout.setRefreshing(false);
        }
        cancelRefresh();
    }
    private void fillRecyclerView( List<User> users){
        adapt=new PersonAdapt(getActivity(),users);
        recyclerView.setAdapter(adapt);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
    private  void implRefresh(){
        //为下拉刷新进度条设置颜色
        if(swipeRefreshLayout!=null) {
            swipeRefreshLayout.setColorSchemeResources(R.color.red);
            //监听下拉刷新
            if (listener != null) {
                swipeRefreshLayout.setOnRefreshListener(listener);
            }
        }
    }

    /**
     * 设置监听
     * @param listener
     */
    public void  setListener(SwipeRefreshLayout.OnRefreshListener listener) {
        this.listener = listener;
        implRefresh();
    }
    public void cancelRefresh(){
       if(swipeRefreshLayout!=null) {
           if (swipeRefreshLayout.isRefreshing()) {
               Log.d(TAG, "refreshUser: 取消刷新");
//            swipeRefreshLayout.setRefreshing(false);
               swipeRefreshLayout.post(new Runnable() {
                   @Override
                   public void run() {
                       swipeRefreshLayout.setRefreshing(false);
                   }
               });
           }
       }
    }
    public void startRefresh(){
        if(swipeRefreshLayout!=null)
            if (!swipeRefreshLayout.isRefreshing()) {
                Log.d(TAG, "refreshUser: 开始刷新");
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(true);
                    }
                });
            }
    }
    public void alert(String title, String content, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);// 设置标题
        // builder.setIcon(R.drawable.ic_launcher);//设置图标
        builder.setMessage(content);// 为对话框设置内容
        // 为对话框设置取消按钮
        final AlertDialog dialog = builder.create();
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定", listener);
        builder.create().show();// 使用show()方法显示对话框
    }
}
