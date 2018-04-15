package com.example.administrator.thefirst;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction() == "VIDEO_TIMER"){
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,new Intent(context,NotificationActivity.class),0);
            Notification notification = new Notification.Builder(context).
                    setAutoCancel(true).
                    setSmallIcon(R.drawable.search).
                    setContentIntent(pendingIntent).
                    setContentTitle("This is title").
                    setContentText("This is content").
                    setTicker("This is ticker").
                    setVibrate(new long[]{0,1000,1000,1000}).
                    setAutoCancel(true).
                    //setColor(Color.RED).
                            build();
            NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(1,notification);
        }
    }
}
