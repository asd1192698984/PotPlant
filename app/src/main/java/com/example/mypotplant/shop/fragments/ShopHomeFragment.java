package com.example.mypotplant.shop.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mypotplant.LazyBaseFragment;
import com.example.mypotplant.R;
import com.example.mypotplant.View.ImageBannerFramLayout;
import com.example.mypotplant.shop.Bean.CommodityMsg;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by MXL on 2020/5/16
 * <br>类描述：<br/>
 *
 * @version 1.0
 * @since 1.0
 */
public class ShopHomeFragment extends LazyBaseFragment   implements ImageBannerFramLayout.FramLayoutLisenner{
    public  final  String TAG="ShopHomeFragment";
    NestedScrollView nestedScrollView;
    RecyclerView recyclerView;
    List<CommodityMsg> commodities;
    ShophomeAdapter showhomeAdapter;
    private ImageBannerFramLayout mGroup;
    private int[] ids = new int[] {
            R.drawable.pic1,//图片资源1
            R.drawable.pic2,//图片资源2
            R.drawable.pic3,//图片资源3
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shophome, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initView();
    }

    @Override
    public View initView() {
        fillRecyclerView();
        initBanner();
        return null;
    }

    @Override
    public void initData() {
       initRcvdata();
    }

    @Override
    protected void lazyLoad() {

    }

    /**
     * 装填适配器
     */
    private  void fillRecyclerView(){
        Log.d(TAG, "fillRecyclerView: ");
        //解决滑动冲突
        recyclerView=getView().findViewById(R.id.recyclerview);
        nestedScrollView=getView().findViewById(R.id.scrollView);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        //适配器
        Log.d(TAG, "fillRecyclerView: "+commodities.size());
        showhomeAdapter=new ShophomeAdapter(getContext(),commodities);
        recyclerView.setAdapter(showhomeAdapter);
    }
    private void initBanner(){
        //计算当前手机宽度
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        mGroup = (ImageBannerFramLayout) getView().findViewById(R.id.image_group);
        mGroup.setLisenner(this);
        List<Bitmap> list = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),ids[i]);
            list.add(bitmap);
        }
        mGroup.addBitmaps(list);
    }
    /**
     * 装填商品
     */
    private void initRcvdata(){
        commodities=new ArrayList<>();
        CommodityMsg commodityMsg;
        commodities.add(commodityMsg=new CommodityMsg("发财树盆栽幸福树琴叶榕龙须树室内客厅办公室绿植净化空气",76.80,"https://item.taobao.com/item.htm?id=619207576815&ali_refid=a3_430582_1006:1278330048:N:Hrz1MfgIXm5Ep%2FTdV99NCQ%3D%3D:21f4e42ea5826e2f6d8b501f5a360297&ali_trackid=1_21f4e42ea5826e2f6d8b501f5a360297&spm=a230r.1.14.1#detail"));
        commodityMsg.setImgID(R.drawable.plant1);
        commodities.add(commodityMsg=new CommodityMsg("唐朝家居 仿真植物多肉小盆栽摆件 客厅室内假绿植盆景摆设装饰品",75.00,"https://detail.tmall.com/item.htm?spm=a230r.1.14.24.7803995aFujQnX&id=564481557636&ns=1&abbucket=11"));
        commodityMsg.setImgID(R.drawable.plant2);
        commodities.add(commodityMsg=new CommodityMsg("何首乌 盆栽盆景野生何首乌苗奇异造型何首乌小绿植一物一拍",22.50,"https://item.taobao.com/item.htm?id=618582839134&ali_refid=a3_420434_1006:1109774700:N:Hrz1MfgIXm5Ep%2FTdV99NCQ%3D%3D:04ae9983c3cda75ecb571f9d7239f237&ali_trackid=1_04ae9983c3cda75ecb571f9d7239f237&spm=a230r.1.1957635.30"));
        commodityMsg.setImgID(R.drawable.plant3);
        commodities.add(commodityMsg=new CommodityMsg("茉莉花盆栽室内阳台花卉盆景防蚊带花苞茉莉花苗净化空气驱蚊绿植",18.80,"https://item.taobao.com/item.htm?id=617874198882&ali_refid=a3_420434_1006:1296430056:N:Hrz1MfgIXm5Ep%2FTdV99NCQ%3D%3D:a968f2ef9ffba0a5b67407c80138e1ee&ali_trackid=1_a968f2ef9ffba0a5b67407c80138e1ee&spm=a230r.1.1957635.47"));
        commodityMsg.setImgID(R.drawable.plant4);
        commodities.add(commodityMsg=new CommodityMsg("栀子花盆栽带花苞栽好室内外四季好养清香型大盆花园办公室阳台耐",29.00,"https://detail.tmall.com/item.htm?spm=a230r.1.14.214.7803995aFujQnX&id=612156946822&ns=1&abbucket=11&sku_properties=13665613:118686939"));
        commodityMsg.setImgID(R.drawable.plant5);
        commodities.add(commodityMsg=new CommodityMsg("北欧仿真植物室内大型盆栽旅人蕉假绿植假树盆栽落地花卉客厅摆件",135,"https://detail.tmall.com/item.htm?spm=a230r.1.14.221.7803995aFujQnX&id=611787179740&ns=1&abbucket=11"));
        commodityMsg.setImgID(R.drawable.plant6);
        commodities.add(commodityMsg=new CommodityMsg("栀子花带花苞四季开花植物阳台室内好养枝枝花卉盆栽净化空气绿植",9.90,"https://detail.tmall.com/item.htm?id=564874075663&ali_refid=a3_430585_1006:1121976980:N:Hrz1MfgIXm5Ep/TdV99NCQ==:4bdeeb1ad42faebf62559db27531969e&ali_trackid=1_4bdeeb1ad42faebf62559db27531969e&spm=a230r.1.14.1&sku_properties=13665613:118686939"));
        commodityMsg.setImgID(R.drawable.plant7);
        commodities.add(commodityMsg=new CommodityMsg("如意皇后盆栽植物室内水培吉祥花铜钱草观叶绿植火红万年青花卉",9.50,"https://detail.tmall.com/item.htm?id=573798598281&ali_refid=a3_430585_1006:1151414984:N:Hrz1MfgIXm5Ep/TdV99NCQ==:a84ee22ffd88d12f3d503bc4a6013bf1&ali_trackid=1_a84ee22ffd88d12f3d503bc4a6013bf1&spm=a230r.1.14.6"));
        commodityMsg.setImgID(R.drawable.plant8);
        commodities.add(commodityMsg=new CommodityMsg("何首乌 盆栽盆景野生何首乌苗奇异造型何首乌小绿植一物一拍",6.00,"https://item.taobao.com/item.htm?id=618144811101&ali_refid=a3_430585_1006:1295520013:N:Hrz1MfgIXm5Ep%2FTdV99NCQ%3D%3D:a3a83389b6f9f7418d680e40c7b04e2d&ali_trackid=1_a3a83389b6f9f7418d680e40c7b04e2d&spm=a230r.1.14.11#detail"));
        commodityMsg.setImgID(R.drawable.plant9);
    }
    @Override
    public void chickImageIndex(int pos) {
        Toast.makeText(getActivity(),"索引值 = " + pos,Toast.LENGTH_SHORT).show();
    }
}
