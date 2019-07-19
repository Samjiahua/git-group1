package com.lenovo.smarttraffic.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.util.VollryUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ACT2_ETC extends Activity {
    @BindView(R.id.act2_iv_back)
    ImageView act2IvBack;
    @BindView(R.id.act2_iv_laba)
    ImageView act2IvLaba;
    @BindView(R.id.act2_tv_content)
    TextView act2TvContent;
    @BindView(R.id.act2_iv_etccz)
    ImageView act2IvEtccz;
    @BindView(R.id.act2_iv_etcye)
    ImageView act2IvEtcye;
    @BindView(R.id.act2_iv_etcjl)
    ImageView act2IvEtcjl;
    boolean flag = true;
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act2_etc);
        ButterKnife.bind(this);


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0 && flag == true) {
                    handler.sendEmptyMessageDelayed(1, 500);
                    act2IvLaba.setImageResource(R.mipmap.sound_1);
                }else if (msg.what == 1 && flag == true){
                    handler.sendEmptyMessageDelayed(0, 500);
                    act2IvLaba.setImageResource(R.mipmap.sound_2);
                }
            }
        };
        handler.sendEmptyMessage(0);



        huoqu();

    }

    private void huoqu() {
        Map map = new Hashtable();
        map.put("UserName", "user1");
        VollryUtils.post("get_notice", map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if ("F".equals(jsonObject.getString("ROWS_DETAIL"))){
                        Toast.makeText(ACT2_ETC.this, "网络信息错误，请重试", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JSONArray array = jsonObject.getJSONArray("ROWS_DETAIL");
                    for (int i = 0; i < array.length(); i++) {
                        String content = array.getJSONObject(i).getString("content");

                        act2TvContent.setText(content);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ACT2_ETC.this, "网络不通", Toast.LENGTH_SHORT).show();
                return;
            }
        });


    }

    @OnClick({R.id.act2_iv_back, R.id.act2_iv_etccz, R.id.act2_iv_etcye, R.id.act2_iv_etcjl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.act2_iv_back:
                finish();
                break;
            case R.id.act2_iv_etccz:
                startActivity(new Intent(this, ACT2_ETCCZ.class));
                break;
            case R.id.act2_iv_etcye:
                startActivity(new Intent(this, ACT2_ETCYE.class));
                break;
            case R.id.act2_iv_etcjl:
                startActivity(new Intent(this, ACT2_ETCJL.class));
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
