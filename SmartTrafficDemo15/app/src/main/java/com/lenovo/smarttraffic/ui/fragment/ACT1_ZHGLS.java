package com.lenovo.smarttraffic.ui.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.bean.ACT1_Bean;
import com.lenovo.smarttraffic.util.ACT1Dialog;
import com.lenovo.smarttraffic.util.VollryUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


@SuppressLint("ValidFragment")
public class ACT1_ZHGLS extends Fragment {
    @BindView(R.id.act1_list)
    ListView act1List;
    Unbinder unbinder;
    private TextView act1TvPlcz;
    private List<ACT1_Bean> beanList = new ArrayList<>();
    private ACT1Adapter adapter;
    private ProgressDialog dialog;

    public ACT1_ZHGLS(TextView act1TvPlcz) {
        this.act1TvPlcz = act1TvPlcz;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.act1_zhgls, container, false);
        unbinder = ButterKnife.bind(this, view);

        huoqu();

        act1List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckBox act1_cb = view.findViewById(R.id.act1_cb);
                beanList.get(i).setSet(act1_cb.isChecked());
            }
        });
        act1TvPlcz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!beanList.get(0).getSet() && !beanList.get(1).getSet() && !beanList.get(2).getSet()
                        && !beanList.get(3).getSet()){
                    Toast.makeText(getContext(), "请选择小车状态", Toast.LENGTH_SHORT).show();
                    return;
                }
                plcz();

            }

            private void plcz() {
                ACT1Dialog.showDialog(getContext());
                TextView act1_dia_carnumber = ACT1Dialog.dialog.getWindow().findViewById(R.id.act1_dia_carnumber);
                EditText act1_dia_money = ACT1Dialog.dialog.getWindow().findViewById(R.id.act1_dia_money);
                Button act1_dia_cz = ACT1Dialog.dialog.getWindow().findViewById(R.id.act1_dia_cz);
                Button act1_dia_qx = ACT1Dialog.dialog.getWindow().findViewById(R.id.act1_dia_qx);

                String str = "";
                for (int i = 0; i < 4; i++) {
                    if (beanList.get(i).getSet()){
                        str += beanList.get(i).getCarnumber() + " ";
                    }
                }
                act1_dia_carnumber.setText(str);

                act1_dia_cz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(act1_dia_money.getText().toString().trim())){
                            Toast.makeText(getContext(), "请输入充值金额", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int money = Integer.parseInt(act1_dia_money.getText().toString().trim());
                        if (money > 999 || money < 1){
                            Toast.makeText(getContext(), "充值金额范围在1-999之间", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for (int i = 0; i < 4; i++) {
                            if (beanList.get(i).getSet()){
                                chonghzi(beanList.get(i).getCarid(), money);
                            }
                        }
                        ACT1Dialog.disDialog();
                    }
                });
                act1_dia_qx.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ACT1Dialog.disDialog();
                    }
                });

            }
        });

        return view;
    }

    private void huoqu() {
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("正在加载中，请稍后。。。");
        dialog.setTitle("提示");
        dialog.setCancelable(false);
        dialog.show();

        for (int i = 1; i < 5; i++) {
            Map map = new Hashtable();
            map.put("CarId", i);
            map.put("UserName", "user1");
            int finalI = i;
            VollryUtils.post("get_car_account_balance", map, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        if ("F".equals(jsonObject.getString("RESULT"))) {
                            Toast.makeText(getContext(), "网络信息错误，请重试", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int balance = jsonObject.getInt("Balance");

                        huoqu1(finalI, balance);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(getContext(), "网络不通", Toast.LENGTH_SHORT).show();
                    return;
                }
            });
        }
    }

    private void huoqu1(int finalI, int balance) {
        Map map = new Hashtable();
        map.put("UserName", "user1");
        VollryUtils.post("get_car_info", map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if ("F".equals(jsonObject.getString("RESULT"))) {
                        Toast.makeText(getContext(), "网络信息错误，请重试", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    JSONArray array = jsonObject.getJSONArray("ROWS_DETAIL");
                    for (int i = 0; i < array.length(); i++) {
                        String carnumber = array.getJSONObject(i).getString("carnumber");
                        String number = array.getJSONObject(i).getString("number");
                        String carbrand = array.getJSONObject(i).getString("carbrand");
                        String pcardid = array.getJSONObject(i).getString("pcardid");
                        String carid = String.valueOf(finalI);
                        if (carid.equals(number)) {
                            huoqu2(finalI, carbrand, carnumber, balance, pcardid);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getContext(), "网络不通", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

    private void huoqu2(int finalI, String carbrand, String carnumber, int balance, String pcardid) {
        Map map = new Hashtable();
        map.put("UserName", "user1");
        VollryUtils.post("get_all_user_info", map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if ("F".equals(jsonObject.getString("RESULT"))) {
                        Toast.makeText(getContext(), "网络信息错误，请重试", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JSONArray array = jsonObject.getJSONArray("ROWS_DETAIL");
                    for (int i = 0; i < array.length(); i++) {
                        String pname = array.getJSONObject(i).getString("pname");
                        String pcardid1 = array.getJSONObject(i).getString("pcardid");
                        if (pcardid.equals(pcardid1)) {
                            ACT1_Bean act1_bean = new ACT1_Bean(finalI, carbrand, carnumber, pname, balance, false);
                            act1_bean.setSet(false);
                            beanList.add(act1_bean);
                        }
                    }
                    if (beanList.size() == 4) {
                        Collections.sort(beanList, new Comparator<ACT1_Bean>() {
                            @Override
                            public int compare(ACT1_Bean act1_bean, ACT1_Bean t1) {
                                return act1_bean.getCarid() - t1.getCarid();
                            }
                        });

                        adapter = new ACT1Adapter();
                        act1List.setAdapter(adapter);
                        dialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getContext(), "网络不通", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

    public class ACT1Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return beanList.size();
        }

        @Override
        public Object getItem(int i) {
            return beanList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.act1_adapter, null);
                holder = new ViewHolder(view);
                view.setTag(holder);
            }else {
                holder = (ViewHolder) view.getTag();
            }

            Class drawable = R.mipmap.class;
            try {
                Field field = drawable.getField(beanList.get(i).getCarbrand());
                int anInt = field.getInt(field.getName());

                holder.act1TvCarbarnd.setImageResource(anInt);
            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.act1TvCarid.setText(beanList.get(i).getCarid()+"");
            holder.act1TvPname.setText("车主:"+beanList.get(i).getPname());
            holder.act1TvCarnumber.setText("车牌:"+beanList.get(i).getCarnumber());
            holder.act1TvBalance.setText(beanList.get(i).getBalance()+"元");
            holder.act1Cb.setChecked(beanList.get(i).getSet());
            holder.act1Cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    holder.act1Cb.setChecked(b);
                    beanList.get(i).setSet(b);
                }
            });

            int carid = beanList.get(i).getCarid();
            holder.act1BtnCz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (carid){
                        case 1:
                            String carnumber1 = beanList.get(0).getCarnumber();
                            chonhzhiTest(1, carnumber1);
                            break;
                        case 2:
                            String carnumber2 = beanList.get(1).getCarnumber();
                            chonhzhiTest(2, carnumber2);
                            break;
                        case 3:
                            String carnumber3 = beanList.get(2).getCarnumber();
                            chonhzhiTest(3, carnumber3);
                            break;
                        case 4:
                            String carnumber4 = beanList.get(3).getCarnumber();
                            chonhzhiTest(4, carnumber4);
                            break;
                    }
                }
            });

            return view;
        }

        class ViewHolder {
            @BindView(R.id.act1_tv_carid)
            TextView act1TvCarid;
            @BindView(R.id.act1_tv_carbarnd)
            ImageView act1TvCarbarnd;
            @BindView(R.id.act1_tv_pname)
            TextView act1TvPname;
            @BindView(R.id.act1_tv_carnumber)
            TextView act1TvCarnumber;
            @BindView(R.id.act1_tv_balance)
            TextView act1TvBalance;
            @BindView(R.id.act1_cb)
            CheckBox act1Cb;
            @BindView(R.id.act1_btn_cz)
            Button act1BtnCz;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

    private void chonhzhiTest(int carid, String carnumber) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        View view = LayoutInflater.from(getContext()).inflate(R.layout.act1_dialog, null);

        TextView act1_dia_carnumber = view.findViewById(R.id.act1_dia_carnumber);
        EditText act1_dia_money = view.findViewById(R.id.act1_dia_money);
        Button act1_dia_cz = view.findViewById(R.id.act1_dia_cz);
        Button act1_dia_qx = view.findViewById(R.id.act1_dia_qx);

        builder.setView(view);
        act1_dia_carnumber.setText(carnumber);

        AlertDialog alertDialog = builder.create();

        act1_dia_cz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(act1_dia_money.getText().toString().trim())){
                    Toast.makeText(getContext(), "充值金额不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                int money = Integer.parseInt(act1_dia_money.getText().toString().trim());
                if (money > 999 || money < 1){
                    Toast.makeText(getContext(), "充值金额范围在1-999之间", Toast.LENGTH_SHORT).show();
                    return;
                }
                chonghzi(carid, money);


                alertDialog.dismiss();
            }
        });
        act1_dia_qx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void chonghzi(int carid, int money) {
        Map map = new Hashtable();
        map.put("CarId", carid);
        map.put("Money", money);
        map.put("UserName", "user1");
        VollryUtils.post("set_car_account_recharge", map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if ("F".equals(jsonObject.getString("RESULT"))){
                        Toast.makeText(getContext(), "充值失败", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(getContext(), "充值成功", Toast.LENGTH_SHORT).show();

                    int balance = beanList.get(carid - 1).getBalance();
                    beanList.get(carid - 1).setBalance(money + balance);
                    beanList.get(carid - 1).setSet(false);
                    adapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getContext(), "网络不通", Toast.LENGTH_SHORT).show();
                return;
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
