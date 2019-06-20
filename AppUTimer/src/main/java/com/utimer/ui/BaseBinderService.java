package com.utimer.ui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;

import com.utimer.R;

import ahtewlg7.utimer.ui.BinderService;

/**
 * Created by lw on 2019/6/20.
 */
public class BaseBinderService extends BinderService {

    private NotificationManager notificationManager;
    private String notificationId = "channelId";
    private String notificationName = "channelName";

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(notificationId, notificationName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        this.startForeground(1,getNotification());
    }

    private Notification getNotification() {
        RemoteInput remoteInput = new RemoteInput.Builder("REPLY")
                .setLabel("REPLY")
                .build();
        PendingIntent replyIntent = PendingIntent.getActivity(this,
                0,
                getDirectReplyIntent(this, "REPLY"),
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action replyAction =
                new NotificationCompat.Action.Builder(R.drawable.bg_color,
                        "REPLY", replyIntent)
                        .addRemoteInput(remoteInput)
                        .build();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,notificationId)
                .setSmallIcon(R.drawable.txt_color)
                .setContentTitle("测试服务")
                .setContentText("我正在运行")
                .addAction(replyAction)
                .setAutoCancel(true);
        Notification notification = builder.build();
        return notification;
    }
    private Intent getDirectReplyIntent(Context context, String label) {
        /*return MessageActivity.getStartIntent(context)
                .addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                .setAction(REPLY_ACTION)
                .putExtra(CONVERSATION_LABEL, label);*/
        return new Intent();
    }
}
