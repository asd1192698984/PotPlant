package com.example.mypotplant.maintenance.dataCentre.datashowfragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mypotplant.LazyBaseFragment;
import com.example.mypotplant.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import lecho.lib.hellocharts.formatter.SimpleLineChartValueFormatter;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by MXL on 2020/8/12
 * <br>类描述：使用开源工具包进行折线图绘制<br/>
 *  折线图 以时间24h为X坐标轴
 * @version 1.0
 * @since 1.0
 */
public class ChartFragment extends LazyBaseFragment {
    public  static String TAG="ChartFragment";
    private LineChartView chartTop;
    LineChartData data;
    String[] dateX ;
//    int[] money= {1000,420,900,330,1000,740,2300};//图表的数据点
    private  int[] potX;
    private  float[] potY;
    float[] dataY ;
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    private ChartFragment (){};
    private float minY ;//Y轴坐标最小值
    private float maxY ;//Y轴坐标最大值
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_chart,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initView();
    }

    public  static  ChartFragment newInstance(int[] pointX, float[] pointY, float[] dataY){
        ChartFragment fragment=new ChartFragment();
        Bundle bundle=new Bundle();
        bundle.putIntArray("pointX",pointX);
        bundle.putFloatArray("pointY",pointY);
        bundle.putFloatArray("dataY",dataY);
        fragment.setArguments(bundle);
        return  fragment;
    }
    @Override
    public View initView() {
        chartTop=getView().findViewById(R.id.chart_view);
        initAxisXLables();//获取x轴的标注
        initAxisPoints();//获取坐标点
        initLineChart();//初始化
        return null;
    }

    @Override
    public void initData() {
        dateX=new String[24];
       for(int i=0;i<dateX.length;i++){
           dateX[i]=i+"h";
       }
       dataY=getArguments().getFloatArray("dataY");
       potX=getArguments().getIntArray("pointX");
       potY=getArguments().getFloatArray("pointY");
       minY=min(dataY);
       maxY=max(dataY);
        Log.d(TAG, "initData: max="+maxY+"min="+minY);
    }

    @Override
    protected void lazyLoad() {

    }
    /**
     * 设置X 轴的显示
     */
    private void initAxisXLables(){
        for (int i = 0; i < dateX.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(dateX[i]));
        }
    }
    /**
     * 图表的每个点的显示
     */
    private void initAxisPoints() {
        for (int i = 0; i < potX.length; i++) {
            mPointValues.add(new PointValue(potX[i],potY[i]));
        }
    }
    private void initLineChart(){
      //获取坐标数据
        Line line = new Line(mPointValues).setColor(Color.parseColor("#5E8BDF"));  //折线的颜色
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
        line.setCubic(false);//曲线是否平
//	    line.setStrokeWidth(3);//线条的粗细，默认是3
        line.setFilled(true);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
        line.setFormatter(new SimpleLineChartValueFormatter(2));//设置显示小数点
//		line.setHasLabelsOnlyForSelected(false);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示
        line.setStrokeWidth(1);
        line.setPointRadius(3);
        lines.add(line);
        data = new LineChartData();
        data.setValueLabelBackgroundColor(Color.TRANSPARENT);
        data.setValueLabelBackgroundAuto(true);
        data.setValueLabelBackgroundEnabled(true);
        data.setValueLabelTextSize(6);
        data.setValueLabelsTextColor(Color.WHITE);  //此处设置坐标点旁边的文字颜色
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(false);  //X轴下面坐标轴字体是斜的显示还是直的，true是斜的显示
//	    axisX.setTextColor(Color.WHITE);  //设置字体颜色
        axisX.setTextColor(Color.BLACK);//黑色

        axisX.setTextSize(7);//设置字体大小
        axisX.setMaxLabelChars(0); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        Log.d(TAG, "initLineChart: xsize"+mAxisXValues.size());
        data.setAxisXBottom(axisX); //x 轴在底部
        axisX.setHasLines(true); //x 轴分割线

        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴
        axisY.setTextColor(Color.BLACK);
        axisY.setName("");//y轴标注
        axisY.setTextSize(8);//设置字体大小
        axisY.setHasLines(true);
        axisY.setMaxLabelChars(6);//max label length, for example 60
// 这样添加y轴坐标 就可以固定y轴的数据
        List<AxisValue> values = new ArrayList<>();
        for(int i = 0; i < dataY.length; i++){
            AxisValue value = new AxisValue(dataY[i]);
            values.add(value);
        }
        axisY.setValues(values);
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边

        //设置行为属性，支持缩放、滑动以及平移

        chartTop.setInteractive(true);
        chartTop.setZoomType(ZoomType.HORIZONTAL);
        chartTop.setMaxZoom((float) 4);//最大放大比例
        chartTop.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        chartTop.setLineChartData(data);
        chartTop.setVisibility(View.VISIBLE);
        /**注：下面的7，10只是代表一个数字去类比而已
         * 当时是为了解决X轴固定数据个数。见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         */
        Viewport v = new Viewport(chartTop.getMaximumViewport());
        v.bottom = minY;
        v.top = maxY;
        //固定Y轴的范围,如果没有这个,Y轴的范围会根据数据的最大值和最小值决定,这不是我想要的
        chartTop.setMaximumViewport(v);
        v.left = 0;
        v.right= 12;
        chartTop.setCurrentViewport(v);
    }
   public void  changeData(int[] potX,float[] potY){
//       Log.d(TAG, "changeData: "+potY[0]+" "+potY[1]);
       StringBuilder builder=new StringBuilder();
       for(int i=0;i<potX.length;i++){
           builder.append(potX[i]+" ");
       }
       StringBuilder builder2=new StringBuilder();
       for(int i=0;i<potX.length;i++){
           builder2.append(potY[i]+" ");
       }
       Log.d(TAG, "changeData: "+builder.toString());
       Log.d(TAG, "changeData: Y"+builder2.toString());
       mPointValues.clear();
       for (int i = 0; i < potX.length; i++)
       mPointValues.add(new PointValue(potX[i], potY[i]));
       chartTop.setLineChartData(data);
   }
   private float min(float[] dataY){
        float min =99999999;
        for(int i=0;i<dataY.length;i++){
            min=Math.min(min,dataY[i]);
        }
            return  min;
   }
    private float max(float[] dataY){
        float max =-1;
        for(int i=0;i<dataY.length;i++){
            max=Math.max(max,dataY[i]);
        }
        return  max;
    }
}
