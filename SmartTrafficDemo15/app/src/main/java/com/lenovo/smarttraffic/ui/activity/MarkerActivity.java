package com.lenovo.smarttraffic.ui.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.lenovo.smarttraffic.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MarkerActivity extends AppCompatActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.marker)
    ImageView marker;
    @BindView(R.id.msg)
    TextView msg;
    @BindView(R.id.mapView)
    MapView mapView;
    private AMap aMap;
    boolean flag = true;
    private double[] jingdu = {116.309884, 116.347593, 116.209749, 116.472871};
    private double[] weidu = {40.042737, 39.860512, 39.802303, 39.847603};
    private String[] title = {"1号小车\n联想大厦", "2号小车\n西铁营桥", "3号小车\nG107", "4号小车\nG2路段"};
    private int[] icon = {R.mipmap.marker_one,R.mipmap.marker_second,R.mipmap.marker_thrid,R.mipmap.marker_forth};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marker);
        ButterKnife.bind(this);
        mapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mapView.getMap();
        }
    }

    @OnClick({R.id.back, R.id.marker})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.marker:
                if (flag) {
                    for (int i = 0; i < title.length; i++) {
                        aMap.addMarker(new MarkerOptions().position(new LatLng(weidu[i], jingdu[i]))
                                .title(title[i]).draggable(true)
                                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), icon[i]))));
                    }


                    msg.setText("1、2、3、4号小车已标记完成");
                    flag = false;
                } else {
                    aMap.clear();
                    msg.setText("小车地图信息");
                    flag = true;
                }
                break;
        }
    }
}
