package com.lenovo.smarttraffic.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.bean.ACT2_Bean;
import com.lenovo.smarttraffic.util.Utils;
import com.lenovo.smarttraffic.util.VollryUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ACT2_ETCCZ extends Activity {
    @BindView(R.id.act2_iv_back1)
    ImageView act2IvBack1;
    @BindView(R.id.act2_et_carid1)
    EditText act2EtCarid1;
    @BindView(R.id.act2_tv_carnumber1)
    TextView act2TvCarnumber1;
    @BindView(R.id.act2_btn_10)
    Button act2Btn10;
    @BindView(R.id.act2_btn_50)
    Button act2Btn50;
    @BindView(R.id.act2_btn_100)
    Button act2Btn100;
    @BindView(R.id.act2_et_money1)
    EditText act2EtMoney1;
    @BindView(R.id.act2_btn_cz1)
    Button act2BtnCz1;
    private Handler handler;
    private Runnable runnable;
    private int carid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act2_etccz);
        ButterKnife.bind(this);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(act2EtCarid1.getText().toString().trim())){
                    carid = Integer.parseInt(act2EtCarid1.getText().toString().trim());
                    if (carid == 1){
                        act2TvCarnumber1.setText("鲁B10001");
                    }else if (carid == 2){
                        act2TvCarnumber1.setText("鲁B10002");
                    }else if (carid == 3){
                        act2TvCarnumber1.setText("鲁B10003");
                    }
                }

                handler.postDelayed(this, 500);
            }
        };
        handler.post(runnable);
    }

    @OnClick({R.id.act2_iv_back1, R.id.act2_btn_10, R.id.act2_btn_50, R.id.act2_btn_100, R.id.act2_btn_cz1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.act2_iv_back1:
                startActivity(new Intent(this, ACT2_ETC.class));
                break;
            case R.id.act2_btn_10:
                act2EtMoney1.setText(10+"");
                break;
            case R.id.act2_btn_50:
                act2EtMoney1.setText(50+"");
                break;
            case R.id.act2_btn_100:
                act2EtMoney1.setText(100+"");
                break;
            case R.id.act2_btn_cz1:
                if (TextUtils.isEmpty(act2EtCarid1.getText().toString().trim())){
                    Toast.makeText(this, "请输入小车编号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (carid > 3 || carid < 1){
                    Toast.makeText(this, "小车编号只允许在1-3号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(act2EtMoney1.getText().toString().trim())){
                    Toast.makeText(this, "请输入充值金额", Toast.LENGTH_SHORT).show();
                    return;
                }
                int money = Integer.parseInt(act2EtMoney1.getText().toString().trim());
                if (money > 999 || money < 1){
                    Toast.makeText(this, "充值金额范围在1-999之间", Toast.LENGTH_SHORT).show();
                    return;
                }
                chongzhi(carid, money);
                break;
        }
    }

    private void chongzhi(int carid, int money) {
        Map map = new Hashtable();
        map.put("CarId", carid);
        map.put("Money", money);
        map.put("UserName", "user1");
        VollryUtils.post("set_car_account_recharge", map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if ("F".equals(jsonObject.getString("RESULT"))){
                        Toast.makeText(ACT2_ETCCZ.this, "充值失败", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Toast.makeText(ACT2_ETCCZ.this, "充值成功", Toast.LENGTH_SHORT).show();

                    ACT2_Bean bean = new ACT2_Bean(Utils.getWeekTime(), "user1",
                            act2TvCarnumber1.getText().toString(), money, Utils.getTime());
                    bean.save();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ACT2_ETCCZ.this, "网络不通", Toast.LENGTH_SHORT).show();
                return;
            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
