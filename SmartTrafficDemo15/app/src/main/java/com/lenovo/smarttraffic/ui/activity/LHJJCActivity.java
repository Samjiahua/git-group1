package com.lenovo.smarttraffic.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.bean.AdaBean;
import com.lenovo.smarttraffic.bean.EXHJJCBean;
import com.lenovo.smarttraffic.db.BeanBJ;
import com.lenovo.smarttraffic.db.BeanCQ;
import com.lenovo.smarttraffic.db.BeanSH;
import com.lenovo.smarttraffic.db.BeanSZ;
import com.lenovo.smarttraffic.db.BeanXA;
import com.lenovo.smarttraffic.util.VollryUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LHJJCActivity extends AppCompatActivity {

    @BindView(R.id.timer)
    TextView timer;
    @BindView(R.id.mouth)
    TextView mouth;
    @BindView(R.id.pieChart)
    PieChart pieChart;
    @BindView(R.id.title)
    TextView city;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.vis)
    LinearLayout vis;
    private Timer time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lhjjc2);
        ButterKnife.bind(this);
        timer.setText(getWeekTimer());
        if (InitApp.flag) {
            mouth.setText("最新数据");
            InitApp.flag = false;
            InitApp.firstTimer = System.currentTimeMillis();
        } else {
            if (InitApp.firstTimer != 0) {
                long timer = (((System.currentTimeMillis() - InitApp.firstTimer) / 1000) % 60) / 60;
                mouth.setText("最近更新：" + (timer % 60) + 1 + "分钟之前");
            }
        }

        time = new Timer();
        time.schedule(new TimerTask() {
            @Override
            public void run() {
                getSense();
            }
        }, 100, 5000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        time.cancel();
        time.purge();
        time = null;
    }

    private String getWeekTimer() {
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
        String str = "";
        Calendar instance = Calendar.getInstance();
        int i = instance.get(Calendar.DAY_OF_WEEK);
        switch (i) {
            case 0:
                str = "星期日";
                break;
            case 1:
                str = "星期一";
                break;
            case 2:
                str = "星期二";
                break;
            case 3:
                str = "星期三";
                break;
            case 4:
                str = "星期四";
                break;
            case 5:
                str = "星期五";
                break;
            case 6:
                str = "星期六";
                break;
        }
        return format.format(new Date(System.currentTimeMillis())) + " " + str;
    }

    private void getSense() {
        List<EXHJJCBean> list = new ArrayList<>();//存储次数和pm2.5占比例的集合
        for (int i = 1; i < 6; i++) {//分别代表各个小车的数据
            Map map = new HashMap();
            map.put("UserName", "user1");
            int finalI = i;
            VollryUtils.post("get_all_sense", map, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    Log.e("jso = ", jsonObject.toString());
                    try {
                        if ("F".equals(jsonObject.getString("RESULT"))) {
                            Toast.makeText(InitApp.getContext(), "网络信息！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int mPm = jsonObject.getInt("pm2.5");
                        int mCo2 = jsonObject.getInt("co2");
                        int mTemp = jsonObject.getInt("temperature");
                        int mHum = jsonObject.getInt("humidity");
                        int mLight = jsonObject.getInt("LightIntensity");

                        if (finalI == 1) {//北京
                            new BeanBJ(mPm, mCo2, mTemp, mHum, mLight).save();
                        } else if (finalI == 2) {//深圳
                            new BeanSZ(mPm, mCo2, mTemp, mHum, mLight).save();
                        } else if (finalI == 3) {//上海
                            new BeanSH(mPm, mCo2, mTemp, mHum, mLight).save();
                        } else if (finalI == 4) {//重庆
                            new BeanCQ(mPm, mCo2, mTemp, mHum, mLight).save();
                        } else if (finalI == 5) {//雄安
                            new BeanXA(mPm, mCo2, mTemp, mHum, mLight).save();
                        }

                        list.add(new EXHJJCBean(finalI, mPm));
                        if (list.size() == 5) {
                            Collections.sort(list, new Comparator<EXHJJCBean>() {
                                @Override
                                public int compare(EXHJJCBean exhjjcBean, EXHJJCBean t1) {
                                    return exhjjcBean.getCount() - t1.getCount();
                                }
                            });
                            showPieChart(list);
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

    int count = 0;
    private String[] mTitle = {"北京", "深圳", "上海", "重庆", "雄安"};

    private void showPieChart(List<EXHJJCBean> list) {
        count++;
        pieChart.setDescription(null);
        pieChart.setUsePercentValues(true);
        pieChart.setDrawHoleEnabled(false);

        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setTextSize(20);
        legend.setFormSize(20);
        legend.setTextColor(Color.BLACK);

        List<PieEntry> yVals = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            yVals.add(new PieEntry(list.get(i).getPm(), mTitle[i]));
        }

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null) {
                    return;
                }
                String[] title = {"pm2.5(μg/m3)", "二氧化碳(ppm)", "光照强度(SL)", "温度(RH)", "湿度(℃)"};
                List<Integer> listMax = new ArrayList<>();
                List<Integer> listMin = new ArrayList<>();
                List<Integer> listAvg = new ArrayList<>();
                if (yVals.get(0).getY() == e.getY()) {
                    Toast.makeText(InitApp.getContext(), mTitle[0], Toast.LENGTH_SHORT).show();
                    city.setText("北京");
                    vis.setVisibility(View.VISIBLE);
                    listMax.add(LitePal.max(BeanBJ.class,"pm",int.class));
                    listMax.add(LitePal.max(BeanBJ.class,"co2",int.class));
                    listMax.add(LitePal.max(BeanBJ.class,"light",int.class));
                    listMax.add(LitePal.max(BeanBJ.class,"temper",int.class));
                    listMax.add(LitePal.max(BeanBJ.class,"hum",int.class));

                    listMin.add(LitePal.min(BeanBJ.class,"pm",int.class));
                    listMin.add(LitePal.min(BeanBJ.class,"co2",int.class));
                    listMin.add(LitePal.min(BeanBJ.class,"light",int.class));
                    listMin.add(LitePal.min(BeanBJ.class,"temper",int.class));
                    listMin.add(LitePal.min(BeanBJ.class,"hum",int.class));

                    listAvg.add((int) LitePal.average(BeanBJ.class,"pm"));
                    listAvg.add((int) LitePal.average(BeanBJ.class,"co2"));
                    listAvg.add((int) LitePal.average(BeanBJ.class,"light"));
                    listAvg.add((int) LitePal.average(BeanBJ.class,"temper"));
                    listAvg.add((int) LitePal.average(BeanBJ.class,"hum"));
                }else if (yVals.get(1).getY() == e.getY()){
                    Toast.makeText(InitApp.getContext(), mTitle[1], Toast.LENGTH_SHORT).show();
                    city.setText("深圳");
                    vis.setVisibility(View.VISIBLE);
                    listMax.add(LitePal.max(BeanSZ.class,"pm",int.class));
                    listMax.add(LitePal.max(BeanSZ.class,"co2",int.class));
                    listMax.add(LitePal.max(BeanSZ.class,"light",int.class));
                    listMax.add(LitePal.max(BeanSZ.class,"temper",int.class));
                    listMax.add(LitePal.max(BeanSZ.class,"hum",int.class));

                    listMin.add(LitePal.min(BeanSZ.class,"pm",int.class));
                    listMin.add(LitePal.min(BeanSZ.class,"co2",int.class));
                    listMin.add(LitePal.min(BeanSZ.class,"light",int.class));
                    listMin.add(LitePal.min(BeanSZ.class,"temper",int.class));
                    listMin.add(LitePal.min(BeanSZ.class,"hum",int.class));

                    listAvg.add((int) LitePal.average(BeanSZ.class,"pm"));
                    listAvg.add((int) LitePal.average(BeanSZ.class,"co2"));
                    listAvg.add((int) LitePal.average(BeanSZ.class,"light"));
                    listAvg.add((int) LitePal.average(BeanSZ.class,"temper"));
                    listAvg.add((int) LitePal.average(BeanSZ.class,"hum"));
                }else if (yVals.get(2).getY() == e.getY()){
                    Toast.makeText(InitApp.getContext(), mTitle[2], Toast.LENGTH_SHORT).show();
                    city.setText("上海");
                    vis.setVisibility(View.VISIBLE);
                    listMax.add(LitePal.max(BeanSH.class,"pm",int.class));
                    listMax.add(LitePal.max(BeanSH.class,"co2",int.class));
                    listMax.add(LitePal.max(BeanSH.class,"light",int.class));
                    listMax.add(LitePal.max(BeanSH.class,"temper",int.class));
                    listMax.add(LitePal.max(BeanSH.class,"hum",int.class));

                    listMin.add(LitePal.min(BeanSH.class,"pm",int.class));
                    listMin.add(LitePal.min(BeanSH.class,"co2",int.class));
                    listMin.add(LitePal.min(BeanSH.class,"light",int.class));
                    listMin.add(LitePal.min(BeanSH.class,"temper",int.class));
                    listMin.add(LitePal.min(BeanSH.class,"hum",int.class));

                    listAvg.add((int) LitePal.average(BeanSH.class,"pm"));
                    listAvg.add((int) LitePal.average(BeanSH.class,"co2"));
                    listAvg.add((int) LitePal.average(BeanSH.class,"light"));
                    listAvg.add((int) LitePal.average(BeanSH.class,"temper"));
                    listAvg.add((int) LitePal.average(BeanSH.class,"hum"));
                }else if (yVals.get(3).getY() == e.getY()){
                    Toast.makeText(InitApp.getContext(), mTitle[3], Toast.LENGTH_SHORT).show();
                    city.setText("重庆");
                    vis.setVisibility(View.VISIBLE);
                    listMax.add(LitePal.max(BeanCQ.class,"pm",int.class));
                    listMax.add(LitePal.max(BeanCQ.class,"co2",int.class));
                    listMax.add(LitePal.max(BeanCQ.class,"light",int.class));
                    listMax.add(LitePal.max(BeanCQ.class,"temper",int.class));
                    listMax.add(LitePal.max(BeanCQ.class,"hum",int.class));

                    listMin.add(LitePal.min(BeanCQ.class,"pm",int.class));
                    listMin.add(LitePal.min(BeanCQ.class,"co2",int.class));
                    listMin.add(LitePal.min(BeanCQ.class,"light",int.class));
                    listMin.add(LitePal.min(BeanCQ.class,"temper",int.class));
                    listMin.add(LitePal.min(BeanCQ.class,"hum",int.class));

                    listAvg.add((int) LitePal.average(BeanCQ.class,"pm"));
                    listAvg.add((int) LitePal.average(BeanCQ.class,"co2"));
                    listAvg.add((int) LitePal.average(BeanCQ.class,"light"));
                    listAvg.add((int) LitePal.average(BeanCQ.class,"temper"));
                    listAvg.add((int) LitePal.average(BeanCQ.class,"hum"));
                }else if (yVals.get(4).getY() == e.getY()){
                    Toast.makeText(InitApp.getContext(), mTitle[4], Toast.LENGTH_SHORT).show();
                    city.setText("雄安");
                    vis.setVisibility(View.VISIBLE);
                    listMax.add(LitePal.max(BeanXA.class,"pm",int.class));
                    listMax.add(LitePal.max(BeanXA.class,"co2",int.class));
                    listMax.add(LitePal.max(BeanXA.class,"light",int.class));
                    listMax.add(LitePal.max(BeanXA.class,"temper",int.class));
                    listMax.add(LitePal.max(BeanXA.class,"hum",int.class));

                    listMin.add(LitePal.min(BeanXA.class,"pm",int.class));
                    listMin.add(LitePal.min(BeanXA.class,"co2",int.class));
                    listMin.add(LitePal.min(BeanXA.class,"light",int.class));
                    listMin.add(LitePal.min(BeanXA.class,"temper",int.class));
                    listMin.add(LitePal.min(BeanXA.class,"hum",int.class));

                    listAvg.add((int) LitePal.average(BeanXA.class,"pm"));
                    listAvg.add((int) LitePal.average(BeanXA.class,"co2"));
                    listAvg.add((int) LitePal.average(BeanXA.class,"light"));
                    listAvg.add((int) LitePal.average(BeanXA.class,"temper"));
                    listAvg.add((int) LitePal.average(BeanXA.class,"hum"));
                }
                List<AdaBean> list = new ArrayList<>();
                for (int i = 0; i < title.length; i++) {
                    list.add(new AdaBean(title[i], listMax.get(i), listMin.get(i), listAvg.get(i)));
                }
                if (list.size() == 5) {
                    Log.e("list=", list.toString());
                    listView.setAdapter(new BJAdapter(list));
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

        PieDataSet dataSet = new PieDataSet(yVals, "");
        dataSet.setColors(new int[]{Color.parseColor("#154d5e"), Color.parseColor("#3d9fa4"), Color.parseColor("#a96530"),
                Color.parseColor("#43b988"), Color.parseColor("#e61c0e")});
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return InitApp.format.format(value) + "%\t" + count + "次";
            }
        });
        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate();
    }

    public class BJAdapter extends BaseAdapter {
        private List<AdaBean> list;

        public BJAdapter(List<AdaBean> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.ada, null);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.shzs.setText(list.get(i).getMsg() + "");
            holder.max.setText(list.get(i).getMax() + "");
            holder.min.setText(list.get(i).getMin() + "");
            holder.avg.setText(list.get(i).getAvg() + "");
            return view;
        }

        class ViewHolder {
            @BindView(R.id.shzs)
            TextView shzs;
            @BindView(R.id.max)
            TextView max;
            @BindView(R.id.min)
            TextView min;
            @BindView(R.id.avg)
            TextView avg;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
