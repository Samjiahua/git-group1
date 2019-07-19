package com.lenovo.smarttraffic.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.lenovo.smarttraffic.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ACT1_CV extends Fragment {
    @BindView(R.id.act1_vc)
    CalendarView act1Vc;
    @BindView(R.id.act1_tv_time)
    TextView act1TvTime;
    Unbinder unbinder;
    boolean flag = true;
    private List<String> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.act1_cv, container, false);

        unbinder = ButterKnife.bind(this, view);

        initCalendar();
        return view;
    }

    private void initCalendar() {
        act1Vc.setOnCalendarMultiSelectListener(new CalendarView.OnCalendarMultiSelectListener() {
            @Override
            public void onCalendarMultiSelectOutOfRange(Calendar calendar) {

            }

            @Override
            public void onMultiSelectOutOfSize(Calendar calendar, int maxSize) {

            }

            @Override
            public void onCalendarMultiSelect(Calendar calendar, int curSize, int maxSize) {
                String time = calendar.getYear() + "-" +
                                calendar.getMonth() + "-" +
                                calendar.getDay();
                if (flag){
                    list.add(time);
                    act1TvTime.setText(list.toString());
                    flag = false;
                }else {
                    if (list.contains(time)){
                        list.remove(time);
                        act1TvTime.setText(list.toString());
                    }else {
                        list.add(time);
                        act1TvTime.setText(list.toString());
                    }
                    if (list.size() == 0){
                        flag = true;
                        act1TvTime.setText("");
                    }
                }
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
