package com.lenovo.smarttraffic.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.util.VollryUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Item7_SHOUS_Activity extends AppCompatActivity {
    @BindView(R.id.back1)
    ImageView back1;
    @BindView(R.id.i7_et)
    EditText i7Et;
    @BindView(R.id.i7_ss)
    Button i7Ss;
    @BindView(R.id.i7_lv)
    ListView i7Lv;
    @BindView(R.id.i7_txt)
    TextView i7Txt;
    private ArrayList<Ssbean> list = new ArrayList();
    private Ssada ssada;
    private boolean flag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item7_shousuo);
        ButterKnife.bind(this);
        initc();

    }

    private void initc() {
        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        i7Txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.clear();
                lisclea();
                i7Txt.setVisibility(View.GONE);
                i7Lv.setVisibility(View.GONE);
            }
        });
        i7Et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable)) {
                    int s = Integer.parseInt(editable.toString());
                    if (s > 10 || s < 1) {
                        editable.clear();
                    }
                }
            }
        });
        i7Ss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
                i7Txt.setVisibility(View.VISIBLE);
                i7Lv.setVisibility(View.VISIBLE);
            }
        });
        i7Lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int id = list.get(i).getId();
                Intent intent = new Intent(Item7_SHOUS_Activity.this, BusXQ.class);
                intent.putExtra("id", id);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            lisclea();

        }
    }

    private void submit() {
        if (TextUtils.isEmpty(i7Et.getText().toString())) {
            Toast.makeText(this, "不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(i7Et.getText().toString())) {
            Toast.makeText(this, "不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        int id = Integer.parseInt(i7Et.getText().toString());
        Ssbean ssbean = new Ssbean();
        ssbean.setId(id);
        if (list.contains(id)) {
            list.remove(id);
            ssada.notifyDataSetChanged();
        } else {
            list.add(ssbean);
        }

        Log.d("bean", ssbean.toString());

        Intent intent = new Intent(Item7_SHOUS_Activity.this, BusXQ.class);
        intent.putExtra("id", id);
        startActivityForResult(intent, 1);
    }

    private void lisclea() {
        if (ssada == null) {
            ssada = new Ssada(getApplicationContext(), list);
            i7Lv.setAdapter(ssada);
        } else {
            ssada.notifyDataSetChanged();
        }
    }

    class Ssbean {
        private int id;

        @Override
        public String toString() {
            return "Ssbean{" +
                    "id=" + id +
                    '}';
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    class Ssada extends BaseAdapter {
        private final Context context;
        private final ArrayList<Ssbean> list;
        private ViewHolder holder;

        public Ssada(Context context, ArrayList<Ssbean> list) {
            this.context = context;
            this.list = list;
        }

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
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.i7_ada, null);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.i7Id.setText(list.get(i).getId() + "");
            return view;
        }

        class ViewHolder {
            @BindView(R.id.i7_id)
            TextView i7Id;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

    public static class BusXQ extends AppCompatActivity {
        @BindView(R.id.back2)
        ImageView back2;
        @BindView(R.id.st_place)
        TextView stPlace;
        @BindView(R.id.en_place)
        TextView enPlace;
        @BindView(R.id.st_iv)
        ImageView stIv;
        @BindView(R.id.st_time)
        TextView stTime;
        @BindView(R.id.en_iv)
        ImageView enIv;
        @BindView(R.id.en_time)
        TextView enTime;
        @BindView(R.id.jiantou)
        AppCompatImageView jiantou;
        @BindView(R.id.bus_lv)
        ListView busLv;
        @BindView(R.id.roadid)
        TextView roadid;
        private ArrayList<Busbean> list = new ArrayList();
        private Busada busada;
        private boolean flag = true;
        private String start_place;
        private String end_place;
        private String start_place_start_time;
        private String end_place_end_time;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.bus_xq);
            ButterKnife.bind(this);
            int id = getIntent().getIntExtra("id", -1);
            if (id == -1) {
                Toast.makeText(this, "请重试", Toast.LENGTH_SHORT).show();
                return;
            }
            roadid.setText(id + "路");
            initcn();
            query();
        }

        private void query() {
            HashMap map = new HashMap<>();
            map.put("UserName", "user1");
            VollryUtils.post("get_busline_info", map, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        if ("F".equals(jsonObject.getString("RESULT"))) {
                            Toast.makeText(BusXQ.this, "失败", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        JSONArray detail = jsonObject.getJSONArray("ROWS_DETAIL");
                        start_place = detail.getJSONObject(0).getString("start_place");
                        end_place = detail.getJSONObject(0).getString("end_place");
                        start_place_start_time = detail.getJSONObject(0).getString("start_place_start_time");
                        end_place_end_time = detail.getJSONObject(0).getString("end_place_end_time");
                        stPlace.setText(start_place + "");
                        enPlace.setText(end_place + "");
                        stTime.setText(start_place_start_time + "");
                        enTime.setText(end_place_end_time + "");
                        JSONArray route = detail.getJSONObject(0).getJSONArray("route");
                        for (int i = 0; i < route.length(); i++) {
                            Busbean busbean = new Busbean();
                            String s = route.getString(i);
                            busbean.setId(i + 1);
                            busbean.setRoute(s);
                            list.add(busbean);
                        }
                        if (list.size() == route.length()) {
                            busada = new Busada(getApplicationContext(), list);
                            busLv.setAdapter(busada);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
        }

        private void initcn() {
            back2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            jiantou.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (flag == true) {
                        stPlace.setText(start_place);
                        stPlace.setText(end_place);
                        enTime.setText(start_place_start_time);
                        stTime.setText(end_place_end_time);
                        stIv.setBackgroundResource(R.mipmap.end);
                        enIv.setBackgroundResource(R.mipmap.start);
                        flag = false;
                    } else if (flag == false) {
                        stPlace.setText(start_place);
                        enPlace.setText(end_place);
                        stTime.setText(start_place_start_time);
                        enTime.setText(end_place_end_time);
                        stIv.setBackgroundResource(R.mipmap.start);
                        enIv.setBackgroundResource(R.mipmap.end);
                        flag = true;
                    }
                    Collections.reverse(list);
                    busada.notifyDataSetChanged();
                }
            });
            busLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    busada.yanse(i);
                    busada.notifyDataSetChanged();
                }
            });
        }

        class Busbean {
            private int id;
            private String route;

            @Override
            public String toString() {
                return "Busbean{" +
                        "id=" + id +
                        ", route=" + route +
                        '}';
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getRoute() {
                return route;
            }

            public void setRoute(String route) {
                this.route = route;
            }
        }

        class Busada extends BaseAdapter {
            private final Context context;
            private final ArrayList<Busbean> list;
            private ViewHolder holder;
            private int curr;

            public Busada(Context context, ArrayList<Busbean> list) {
                this.context = context;
                this.list = list;
            }

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

            public void yanse(int i) {
                curr = i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                if (view == null) {
                    view = LayoutInflater.from(context).inflate(R.layout.bus_ada, null);
                    holder = new ViewHolder(view);
                    view.setTag(holder);
                } else {
                    holder = (ViewHolder) view.getTag();
                }
                if (curr == i) {
                    Log.d("i+=", i + "");
                    holder.busId.setTextColor(Color.WHITE);
                    holder.busId.setBackgroundResource(R.drawable.circle2);
                    holder.busRoute.setTextColor(Color.RED);

                } else {
                    holder.busId.setTextColor(Color.BLACK);
                    holder.busRoute.setTextColor(Color.BLACK);
                    holder.busId.setBackgroundResource(R.drawable.circle);
                }
                holder.busId.setText(list.get(i).getId() + "");
                holder.busRoute.setText(list.get(i).getRoute() + "");
                return view;
            }

            class ViewHolder {
                @BindView(R.id.bus_id)
                TextView busId;
                @BindView(R.id.bus_route)
                TextView busRoute;

                ViewHolder(View view) {
                    ButterKnife.bind(this, view);
                }
            }
        }
    }
}
