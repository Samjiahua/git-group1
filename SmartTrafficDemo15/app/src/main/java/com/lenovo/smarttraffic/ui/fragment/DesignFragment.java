package com.lenovo.smarttraffic.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lenovo.smarttraffic.Constant;
import com.lenovo.smarttraffic.R;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.MediaType;


/**
 * @author Amoly
 * @date 2019/4/11.
 * description：设计页面
 */
public class DesignFragment extends BaseFragment {
    private static DesignFragment instance = null;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.tv_show)
    TextView tvShow;
    Unbinder unbinder;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int p = getnum();
            if (p > 200) {
                 tvShow.setText("pm当前值为"+p+",空气污染重，不建议出行");
                 tvShow.setTextColor(Color.RED);
                 progress.setProgressDrawable(getResources().getDrawable(R.drawable.dw_red));
            }else {
                tvShow.setText("pm当前值为"+p+",空气污染轻，适合出行");
                tvShow.setTextColor(Color.GREEN);
                progress.setProgressDrawable(getResources().getDrawable(R.drawable.dw_green));
            }
            Message message = new Message();
            message.what = p;
            if (p > 0) {
                message.arg1=1;
                progress.setProgress(p);
            }else {
                message.arg1=-1;
                progress.setProgress(p);
            }
            handler1.sendMessage(message);
        }
    };
    private Handler handler1=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int n = msg.what;
            Message message = new Message();
            message.arg1 =n;
            if (n==0){
                return;
            }
            handler1.sendMessageDelayed(message,1);
        }
    };
    private Timer timer;

    private int getnum() {
        Random random = new Random();
        return random.nextInt(399) + 1;
    }

    public static DesignFragment getInstance() {
        if (instance == null) {
            instance = new DesignFragment();
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.cy, null);
        unbinder = ButterKnife.bind(this, view);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                gettt();
            }
        },1000);
        return view;
    }

    private void gettt() {
        timer = new Timer();
       timer.schedule(new TimerTask() {
           @Override
           public void run() {
               handler.sendEmptyMessage(1);
           }
       },100,5000);
    }

    @Override
    public void onStop() {
        super.onStop();
      if (timer!=null){
          timer.cancel();
      }
    }

    @Override
    protected View getSuccessView() {
        TextView view = new TextView(getActivity());
        view.setText("创意设计");
        view.setTextColor(Color.RED);
        view.setTextSize(50);
        view.setVisibility(View.GONE);
        return view;
    }

    @Override
    protected Object requestData() {
        return Constant.STATE_SUCCEED;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onDestroy() {
        if (instance != null) {
            instance = null;
        }
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
