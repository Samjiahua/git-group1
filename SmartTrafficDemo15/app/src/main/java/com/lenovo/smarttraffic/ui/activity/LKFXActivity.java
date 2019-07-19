package com.lenovo.smarttraffic.ui.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.util.VollryUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LKFXActivity extends AppCompatActivity {

    @BindView(R.id.barChart)
    BarChart barChart;

    private String[] title = {"学院路","联想路","医院路","幸福路","环城快速路","环城高速路","停车场"};
    private String[] yleftMsg = {"","畅通","缓行","一般拥堵","中度拥堵","严重拥堵",""};
    private String[] xMsg = {"周一","周二","周三","周四","周五","周六","周日"};
    private int[] icon = {R.color.xueyuan,R.color.lianxiang,R.color.yiyuan,R.color.xingfu ,R.color.kuaisu,
            R.color.gaosu,R.color.tinfchechang};

    private List<IBarDataSet> mBarSet = new ArrayList<>();

    private BarDataSet dataSet;
    private Handler handler;
    private Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lkfx);
        ButterKnife.bind(this);
        initView();
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                getStstion();
                handler.postDelayed(this,5000);
            }
        };
        handler.post(runnable);
    }
    private void initView() {
        for (int i = 0; i < title.length; i++) {//先初始每条线的颜色以及每条线的道路名称
            dataSet = new BarDataSet(new ArrayList<>(), title[i]);
            dataSet.setColor(getResources().getColor(icon[i]));
            mBarSet.add(dataSet);
        }
    }

    private void getStstion() {
        dataSet.clear();
        //7天
        for (int i = 0; i < 7; i++) {
            //7条路
            for (int j = 0; j < mBarSet.size(); j++) {
                mBarSet.get(j).addEntry(new BarEntry(i, (float) (new Random().nextInt(5) + 0.5)));
            }
            initPian();
        }
        
        if (LKFXActivity.this == null) {
            return;
        }
        
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initBarChart();
            }
        });

    }

    private void initBarChart() {
        barChart.setDescription(null);
        barChart.setScaleEnabled(false);
        //获取x轴
        XAxis xAxis = barChart.getXAxis();
        //x不显示参考线
        xAxis.setDrawGridLines(false);
        //x轴底部显示
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //标签在小组中间
        xAxis.setCenterAxisLabels(true);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(16);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1.15f);
        xAxis.setSpaceMax(2f);

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                if (v < 0 | v >= 7) {
                    return "";
                }
                return xMsg[(int) v % xMsg.length];
            }
        });

        //右侧y轴
        YAxis axisRight = barChart.getAxisRight();
        axisRight.setTextSize(15);
        axisRight.setTextColor(Color.BLACK);
        axisRight.setAxisMinimum(0);
        axisRight.setStartAtZero(true);
        axisRight.setLabelCount(5);
        axisRight.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                return (int) v + "";
            }
        });

        //左侧轴
        YAxis axisLeft = barChart.getAxisLeft();
        axisLeft.setTextSize(15);
        axisLeft.setTextColor(Color.BLACK);
        //标签在小组中间
        axisLeft.setCenterAxisLabels(true);
        axisLeft.setAxisMinimum(0f);
        //y轴每段距离
        axisLeft.setGranularity(1f);
        axisLeft.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {

                return yleftMsg[(int) (v + 1)];
            }
        });

        Legend legend = barChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setTextSize(15);
        legend.setTextColor(Color.BLACK);
        legend.setFormSize(16);

        BarData mBarData = new BarData(mBarSet);
        //组与组之间的距离
        float groupSpace = 0.3f;
        //组内柱子距离
        float barSpace = 0.02f;
        //柱子宽度
        float barWidth = 0.1f;
        //设置柱子宽度
        mBarData.setBarWidth(barWidth);

        //不显示顶部数值
        mBarData.setDrawValues(false);

        barChart.setData(mBarData);


        barChart.groupBars(0f, groupSpace, barSpace);
        barChart.setExtraBottomOffset(20);
        barChart.invalidate();//刷新

    }

    private void initPian() {
        for (int i = 1; i < 8; i++) {
            Map map = new HashMap();
            map.put("UserName", "user1");
            map.put("RoadId", i);
            VollryUtils.post("get_road_status", map, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    Log.e("jso = ", jsonObject.toString());
                    try {
                        if ("F".equals(jsonObject.getString("RESULT"))) {
                            Toast.makeText(InitApp.getContext(), "网络信息！", Toast.LENGTH_SHORT).show();
                            return;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(InitApp.getContext(), "网络不通！", Toast.LENGTH_SHORT).show();
                    return;
                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }
}
