package com.lenovo.smarttraffic.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.lenovo.smarttraffic.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DTActivity extends AppCompatActivity {

    @BindView(R.id.marker)
    Button marker;
    @BindView(R.id.layer)
    Button layer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dt);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.marker, R.id.layer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.marker:
                startActivity(new Intent(getApplicationContext(),MarkerActivity.class));
                break;
            case R.id.layer:
                startActivity(new Intent(getApplicationContext(),LayerActivity.class));
                break;
        }
    }
}
