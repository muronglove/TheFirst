package com.example.administrator.thefirst;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;
import android.text.format.Time;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.qqtheme.framework.picker.DateTimePicker;

public class DateTimePickerHelper {
    MyDateTimePicker picker;
    int year;
    int month;
    int date;
    int hour;
    int minute;
    DateTimePickerHelper(MyDateTimePicker picker){
        this.picker = picker;
        Time t = new Time();
        t.setToNow();   //取得系统时间
        year = t.year;
        month = t.month + 1;
        date = t.monthDay;
        hour = t.hour;
        minute = t.minute;
    }

    public void helper(){

        picker.setDateRangeStart(year, month, date);//日期起点
        picker.setDateRangeEnd(2020, 1, 1);//日期终点
        picker.setTimeRangeStart(hour, minute);//时间范围起点
        picker.setTimeRangeEnd(23, 59);//时间范围终点
        picker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
            @Override
            public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
//                    makeText(getApplicationContext(), year + "-" + month + "-" + day + " "
//                            + hour + ":" + minute, Toast.LENGTH_LONG).show();
                picker.doSomething();


            }
        });
    }
}
