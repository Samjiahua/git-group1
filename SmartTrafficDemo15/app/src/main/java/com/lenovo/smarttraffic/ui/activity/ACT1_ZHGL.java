package com.lenovo.smarttraffic.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.ui.fragment.ACT1_CV;
import com.lenovo.smarttraffic.ui.fragment.ACT1_ZHGLS;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ACT1_ZHGL extends FragmentActivity {
    @BindView(R.id.act1_iv_back)
    ImageView act1IvBack;
    @BindView(R.id.act1_tv_plcz)
    TextView act1TvPlcz;
    @BindView(R.id.act1_tv_calendar)
    TextView act1TvCalendar;
    @BindView(R.id.act1_vp)
    ViewPager act1Vp;
    private List<Fragment> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act1_zhgl);
        ButterKnife.bind(this);

        list.add(new ACT1_ZHGLS(act1TvPlcz));
        list.add(new ACT1_CV());

        act1TvCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act1Vp.setCurrentItem(1);
            }
        });

        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }
        };
        act1Vp.setAdapter(fragmentPagerAdapter);

    }

    @OnClick(R.id.act1_iv_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}

