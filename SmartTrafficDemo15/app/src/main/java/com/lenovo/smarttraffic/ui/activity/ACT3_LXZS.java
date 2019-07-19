package com.lenovo.smarttraffic.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lenovo.smarttraffic.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ACT3_LXZS extends Activity {
    @BindView(R.id.act3_iv_back)
    ImageView act3IvBack;
    @BindView(R.id.act3_buy1)
    TextView act3Buy1;
    @BindView(R.id.act3_buy2)
    TextView act3Buy2;
    @BindView(R.id.act3_buy3)
    TextView act3Buy3;
    @BindView(R.id.act3_buy4)
    TextView act3Buy4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act3_lxzs);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.act3_iv_back, R.id.act3_buy1, R.id.act3_buy2, R.id.act3_buy3, R.id.act3_buy4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.act3_iv_back:
                finish();
                break;
            case R.id.act3_buy1:
                startActivity(new Intent(this, ACT3_XXXX.class));
                break;
            case R.id.act3_buy2:
                startActivity(new Intent(this, ACT3_XXXX.class));
                break;
            case R.id.act3_buy3:
                startActivity(new Intent(this, ACT3_XXXX.class));
                break;
            case R.id.act3_buy4:
                startActivity(new Intent(this, ACT3_XXXX.class));
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
