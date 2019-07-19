package com.lenovo.smarttraffic.ui.activity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MultiMonthView;
//import com.lenovo.smarttraffic.ui.activity.CalendarAct;

public class CalenView extends MultiMonthView {

    private int mPadding;

    public CalenView(Context context) {
        super(context);
    }
    /**
     * 绘制选中的日子
     *
     * @param canvas    canvas
     * @param calendar  日历日历calendar
     * @param x         日历Card x起点坐标
     * @param y         日历Card y起点坐标
     * @param hasScheme hasScheme 非标记的日期
     * @return 返回true 则绘制onDrawScheme，因为这里背景色不是是互斥的，所以返回true
     */
    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelectedPre, boolean isSelectedNext) {
        mSelectedPaint.setColor(Color.YELLOW);//改变选中颜色
        canvas.drawRect(x + mPadding, y + mPadding, x + mItemWidth - mPadding, y + mItemHeight - mPadding, mSelectedPaint);
        return true;
    }

    /**
     * 绘制标记的事件日子
     *
     * @param canvas   canvas
     * @param calendar 日历calendar
     * @param x        日历Card x起点坐标
     * @param y        日历Card y起点坐标
     */
    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y, boolean isSelected) {
    }

    /**
     * 绘制文本
     *
     * @param canvas     canvas
     * @param calendar   日历calendar
     * @param x          日历Card x起点坐标
     * @param y          日历Card y起点坐标
     * @param hasScheme  是否是标记的日期
     * @param isSelected 是否选中
     */
    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        float baselineY = mTextBaseLine + y;
        int cx = x + mItemWidth / 2;

        boolean isInRange = isInRange(calendar);
        boolean isEnable = !onCalendarIntercept(calendar);

        if (isSelected) {//优先绘制选择的
            canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY, mSelectTextPaint);
        } else if (hasScheme) {//否则绘制具有标记的
            canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY, calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() && isInRange && isEnable? mSchemeTextPaint : mOtherMonthTextPaint);

        } else {//最好绘制普通文本
            canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY, calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() && isInRange && isEnable? mCurMonthTextPaint : mOtherMonthTextPaint);
        }

    }
}
