package com.lenovo.smarttraffic.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.navi.AMapNavi;
import com.lenovo.smarttraffic.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LayerActivity extends AppCompatActivity {

    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.layer)
    ImageView layer;
    @BindView(R.id.tv_navi)
    TextView tvNavi;
    @BindView(R.id.tv_night)
    TextView tvNight;
    @BindView(R.id.tv_nor)
    TextView tvNor;
    @BindView(R.id.tv_WX)
    TextView tvWX;
    @BindView(R.id.tv_bus)
    TextView tvBus;
    @BindView(R.id.vis)
    GridLayout vis;
    @BindView(R.id.msg)
    TextView msg;
    private AMap aMap;
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layer);
        ButterKnife.bind(this);
        mapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mapView.getMap();
        }
    }

    @OnClick({R.id.back, R.id.layer, R.id.tv_navi, R.id.tv_night, R.id.tv_nor, R.id.tv_WX, R.id.tv_bus})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.layer:
                if (flag){
                    vis.setVisibility(View.VISIBLE);
                    flag = false;
                }else{
                    vis.setVisibility(View.GONE);
                    flag = true;
                }
                break;
            case R.id.tv_navi:
                aMap.setMapType(AMap.MAP_TYPE_NAVI);
                msg.setText("已切换到导航地图");
                break;
            case R.id.tv_night:
                aMap.setMapType(AMap.MAP_TYPE_NIGHT);
                msg.setText("已切换到夜景地图");
                break;
            case R.id.tv_nor:
                aMap.setMapType(AMap.MAP_TYPE_NORMAL);
                msg.setText("已切换到标准地图");
                break;
            case R.id.tv_WX:
                aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
                msg.setText("已切换到卫星地图");
                break;
            case R.id.tv_bus:
                msg.setText("已切换到公交地图");
                aMap.setMapType(AMap.MAP_TYPE_BUS);
                break;
        }
    }
}
