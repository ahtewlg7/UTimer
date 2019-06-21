package ahtewlg7.utimer.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import com.blankj.utilcode.util.Utils;

/**
 * Created by lw on 2019/6/20.
 */
public class NotifyAction {
    NotificationManager notificationManager;

    public NotifyAction(){
        notificationManager = new AndrManagerFactory().getNotificationManager();
    }
    //+++++++++++++++++++++++++++++++++++++++++Notification++++++++++++++++++++++++++++++++++
    public Notification.Builder getNotificationBuilder(){
       return new Notification.Builder(Utils.getApp().getApplicationContext());
    }
    public Notification getNotification(@DrawableRes int iconRid ,@NonNull String title, @NonNull String content,
                                        boolean onGoing, @NonNull Notification.Action... actions){
        Notification.Builder builder = new Notification.Builder(Utils.getApp().getApplicationContext())
                .setSmallIcon(iconRid)
                .setContentTitle(title)
                .setContentText(content)
                .setOngoing(onGoing);
            for(Notification.Action action : actions)
                builder.addAction(action);
        return builder.build();
    }
    //+++++++++++++++++++++++++++++++++++++++++RemoteInput++++++++++++++++++++++++++++++++++++
    public RemoteInput.Builder getRemoteInput(@NonNull String resultKey){
        return new RemoteInput.Builder(resultKey);
    }
    public RemoteInput getRemoteInput(String resultKey, String label){
        return new RemoteInput.Builder(resultKey).setLabel(label).build();
    }
    //+++++++++++++++++++++++++++++++++++++++++PendingIntent++++++++++++++++++++++++++++++++++
    public PendingIntent getServiceIntent(int requestCode , @NonNull Intent startIntent){
        return PendingIntent.getBroadcast(Utils.getApp().getApplicationContext(), requestCode, startIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    //+++++++++++++++++++++++++++++++++++++++++Action+++++++++++++++++++++++++++++++++++++++++
    public Notification.Action.Builder getNotifyActionBuilder(@DrawableRes int iconRid,@NonNull String title,
                                                               @NonNull PendingIntent sendIntent){
        Icon icon = Icon.createWithResource(Utils.getApp().getApplicationContext(), iconRid);
        return new Notification.Action.Builder(icon,title, sendIntent);
    }
    public Notification.Action getNotifyAction(@DrawableRes int iconRid, @NonNull String title,
                          @NonNull PendingIntent sendIntent,@NonNull RemoteInput remoteInput){
        Icon icon = Icon.createWithResource(Utils.getApp().getApplicationContext(), iconRid);
        return new Notification.Action.Builder(icon, title, sendIntent)
                .addRemoteInput(remoteInput)
                .build();
    }


    public void toStartNotify(int notifyId, @NonNull String notificationName, @NonNull Notification notification){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(String.valueOf(notifyId), notificationName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(notifyId,notification);
    }
    public void toStopNotify(int notifyId){
        notificationManager.cancel(notifyId);
    }
}
