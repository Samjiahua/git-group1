package com.lenovo.smarttraffic.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lenovo.smarttraffic.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ACT3_XXXX extends Activity {
    @BindView(R.id.act3x_iv_back)
    ImageView act3xIvBack;
    @BindView(R.id.act3_tv_phone)
    TextView act3TvPhone;
    private String trl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act3_xxxx);
        ButterKnife.bind(this);
        trl = "010-88888888";

    }

    @OnClick({R.id.act3x_iv_back, R.id.act3_tv_phone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.act3x_iv_back:
                startActivity(new Intent(this, ACT3_LXZS.class));
                break;
            case R.id.act3_tv_phone:
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+trl)));
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
