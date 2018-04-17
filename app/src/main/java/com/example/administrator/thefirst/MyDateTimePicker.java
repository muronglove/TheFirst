package com.example.administrator.thefirst;

import android.app.Activity;

import cn.qqtheme.framework.picker.DateTimePicker;

public abstract class MyDateTimePicker extends DateTimePicker implements DateTimePickerInterface{


    public MyDateTimePicker(Activity activity, @TimeMode int timeMode){
        super(activity,YEAR_MONTH_DAY,timeMode);
    }
}
