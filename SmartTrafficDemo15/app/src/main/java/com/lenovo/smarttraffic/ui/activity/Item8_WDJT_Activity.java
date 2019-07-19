package com.lenovo.smarttraffic.ui.activity;

import android.app.NotificationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.util.VollryUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Item8_WDJT_Activity extends AppCompatActivity {
    @BindView(R.id.wdjt)
    TextView wdjt;
    @BindView(R.id.wdlk)
    TextView wdlk;
    @BindView(R.id.a8_vp)
    ViewPager a8Vp;
    @BindView(R.id.back3)
    ImageView back3;
    @BindView(R.id.a8_title)
    TextView a8Title;
    private ArrayList<Fragment> list = new ArrayList<>();
    private FragmentPagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a8_wdjt);
        ButterKnife.bind(this);
        initc();
    }

    private void initc() {
        back3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        list.add(new Fragment01());
        list.add(new Fragment02());

        adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }
        };
        a8Vp.setAdapter(adapter);
        a8Vp.setCurrentItem(0);
        wdjt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a8Title.setText("我的交通");
                a8Vp.setCurrentItem(0);
            }
        });
        wdlk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a8Title.setText("我的路况");
                a8Vp.setCurrentItem(1);
            }
        });
    }

    public static class Fragment02 extends Fragment {
        @BindView(R.id.f8_lk_pm)
        TextView f8LkPm;
        @BindView(R.id.f8_lk_ts)
        TextView f8LkTs;
        @BindView(R.id.f8_lk_lt)
        TextView f8LkLt;
        @BindView(R.id.f8_lk_ltmax)
        TextView f8LkLtmax;
        @BindView(R.id.f8_lk_min)
        TextView f8LkMin;
        @BindView(R.id.f8_lk_ts01)
        TextView f8LkTs01;
        @BindView(R.id.f8_lk_por)
        ProgressBar f8LkPor;
        @BindView(R.id.f8_lk_show)
        TextView f8LkShow;
        Unbinder unbinder;
        @BindView(R.id.f8vv)
        VideoView f8vv;
        private Handler handler;
        private Runnable runnable;
        private int lightIntensity;
        private Handler handler1 = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                f8LkPor.setProgressDrawable(getResources().getDrawable(R.drawable.dw_blue));
                f8LkShow.setText(lightIntensity + "");
                Message message = new Message();
                message.what = lightIntensity;
                if (lightIntensity > 0) {
                    message.arg1 = 1;
                    f8LkPor.setProgress(lightIntensity);
                } else {
                    message.arg1 = -1;
                    f8LkPor.setProgress(lightIntensity);
                }
                handler2.sendMessage(message);
            }
        };
        private Handler handler2 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                int nn = msg.what;
                Message message = new Message();
                message.what = nn;
                if (nn == 0) {
                    return;
                }
                handler2.sendMessageDelayed(message, 100);
            }
        };

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.f8_wdlk, null);
            unbinder = ButterKnife.bind(this, view);
            lights();
            handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    query();
                    handler.postDelayed(this, 3000);
                }
            };
            handler.post(runnable);
            return view;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            handler.removeCallbacks(runnable);
        }

        private void query() {
            HashMap map = new HashMap<>();
            map.put("UserName", "user1");
//            调用路径 http://localhost:8080/api/v2/get_all_sense
            VollryUtils.post("get_all_sense", map, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        if ("F".equals(jsonObject.getString("RESULT"))) {
                            Toast.makeText(getContext(), "失败", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int anInt = jsonObject.getInt("pm2.5");
                        lightIntensity = jsonObject.getInt("LightIntensity");
                        handler1.sendEmptyMessage(1);
                        f8LkLt.setText("光照当前值:" + lightIntensity + "");
                        f8LkPm.setText("PM2.5当前值:" + anInt + "");
                        if (anInt > 200) {
                            f8LkTs.setText("友情提示：不适合出行");
                            f8vv.setVisibility(View.VISIBLE);
                            f8vv.setVideoURI(Uri.parse("android.resource://"+getActivity().getPackageName()+"/raw/"+R.raw.car1));
                            f8vv.start();
                            notifys();
                        } else {
                            f8vv.setVisibility(View.GONE);
                            f8LkTs.setText("友情提示：适合出行");

                        }
                        if (lightIntensity > 10 && lightIntensity < 1000) {
                            f8LkTs01.setText("友情提示：正常");
                        } else if (lightIntensity < 10) {
                            f8LkTs01.setText("友情提示：光照较弱，出行请开灯");

                        } else if (lightIntensity > 1000) {
                            f8LkTs01.setText("友情提示：光照较强，出行请戴墨镜");

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(getContext(), "!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void notifys() {
            NotificationManager no = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
//            Notification.
        }


        private void lights() {
            HashMap map = new HashMap<>();
            map.put("UserName", "user1");
//            用路径 http://localhost:8080/api/v2/set_light_sense_value
            VollryUtils.post("get_light_sense_value", map, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        if ("F".equals(jsonObject.getString("RESULT"))) {
                            Toast.makeText(getContext(), "1", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String down = jsonObject.getString("Down");
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

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            unbinder.unbind();
        }
    }

    public static class Fragment01 extends Fragment {
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.f8_wdjt, null);
            return view;
        }
    }
}
