package com.lenovo.smarttraffic.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.bean.ACT2_Bean;

import org.litepal.LitePal;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ACT2_ETCJL extends Activity {
    @BindView(R.id.act2_iv_back3)
    ImageView act2IvBack3;
    @BindView(R.id.act2_tv_zong)
    TextView act2TvZong;
    @BindView(R.id.act2_list)
    ListView act2List;
    @BindView(R.id.act2_tv_null)
    TextView act2TvNull;
    private List<ACT2_Bean> list;
    private MyAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act2_etcjl);
        ButterKnife.bind(this);

        LitePal.initialize(this);
        list = LitePal.findAll(ACT2_Bean.class);

        int zong = 0;
        for (int i = 0; i < list.size(); i++) {
            zong += list.get(i).getMoney();
        }
        act2TvZong.setText("充值合计:"+zong + "元");

        adapter = new MyAdapter();
        act2List.setAdapter(adapter);
        act2List.setEmptyView(act2TvNull);
    }

    public class MyAdapter extends BaseAdapter {

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
                view = LayoutInflater.from(ACT2_ETCJL.this).inflate(R.layout.act2_adapter, null);
                holder = new ViewHolder(view);
                view.setTag(holder);
            }else {
                holder = (ViewHolder) view.getTag();
            }

            holder.act2TvTime.setText(list.get(i).getTime());
            holder.act2TvUsername.setText("充值人："+list.get(i).getUsername());
            holder.act2TvCarnumber3.setText("车牌号:"+list.get(i).getCarnumber());
            holder.act2TvMoney.setText(list.get(i).getMoney()+"元");
            holder.act2TvTimes.setText(list.get(i).getTimes());



            return view;
        }

        class ViewHolder {
            @BindView(R.id.act2_tv_time)
            TextView act2TvTime;
            @BindView(R.id.act2_tv_username)
            TextView act2TvUsername;
            @BindView(R.id.act2_tv_carnumber3)
            TextView act2TvCarnumber3;
            @BindView(R.id.act2_tv_money)
            TextView act2TvMoney;
            @BindView(R.id.act2_tv_times)
            TextView act2TvTimes;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

    @OnClick(R.id.act2_iv_back3)
    public void onViewClicked() {
        startActivity(new Intent(this, ACT2_ETC.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
