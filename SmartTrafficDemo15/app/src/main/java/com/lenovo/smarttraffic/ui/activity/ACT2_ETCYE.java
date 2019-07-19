package com.lenovo.smarttraffic.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.util.VollryUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ACT2_ETCYE extends Activity {
    @BindView(R.id.act2_iv_back2)
    ImageView act2IvBack2;
    @BindView(R.id.act2_tv_balance1)
    TextView act2TvBalance1;
    @BindView(R.id.act2_tv_balance2)
    TextView act2TvBalance2;
    @BindView(R.id.act2_tv_balance3)
    TextView act2TvBalance3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act2_etcye);
        ButterKnife.bind(this);

        huoqu();
    }

    private void huoqu() {
        for (int i = 1; i < 4; i++) {
            Map map = new Hashtable();
            map.put("CarId", i);
            map.put("UserName", "user1");
            int finalI = i;
            VollryUtils.post("get_car_account_balance", map, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        if ("F".equals(jsonObject.getString("RESULT"))){
                            Toast.makeText(ACT2_ETCYE.this, "网络信息错误，请重试", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int balance = jsonObject.getInt("Balance");

                        if (finalI == 1){
                            act2TvBalance1.setText(balance+"元");
                        }else if (finalI == 2){
                            act2TvBalance2.setText(balance+"元");
                        }else if (finalI == 3){
                            act2TvBalance3.setText(balance+"元");
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(ACT2_ETCYE.this, "网络不通", Toast.LENGTH_SHORT).show();
                    return;
                }
            });
        }
    }

    @OnClick(R.id.act2_iv_back2)
    public void onViewClicked() {
        startActivity(new Intent(this, ACT2_ETC.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
