package com.example.mypotplant;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

/**
 * Created by MXL on 2020/7/22
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class NoConnectFragment extends LazyBaseFragment {
    Button refresh;
    TextView tips;
    NoConnectListener listener;
    Bundle data;
    String TAG="NoConnectFragment";
    private NoConnectFragment(){
    }
    public static NoConnectFragment newInstance(String tips){
        NoConnectFragment fragment=new NoConnectFragment();
        Bundle args=new Bundle();
        args.putString("tips",tips);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
          data = getArguments();
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_noconnect, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initView();
        tips.setText(data.getString("tips"));
    }

    @Override
    public View initView() {
        refresh=getView().findViewById(R.id.refresh);
        tips=getView().findViewById(R.id.tv_tips);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick:  refresh");
                listener.onRefresh();
            }
        });
        return null;
    }

    @Override
    public void initData() {

    }

    @Override
    protected void lazyLoad() {

    }

    public void setListener(NoConnectListener listener) {
        this.listener = listener;
    }

    interface NoConnectListener{
        void onRefresh();
    }
}
