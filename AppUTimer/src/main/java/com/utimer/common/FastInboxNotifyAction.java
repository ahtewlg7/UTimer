package com.utimer.common;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.common.collect.Lists;
import com.utimer.R;

import java.util.List;

import ahtewlg7.utimer.state.GtdMachine;
import ahtewlg7.utimer.util.BroadcastAction;
import ahtewlg7.utimer.util.MyRInfo;
import ahtewlg7.utimer.util.NotifyAction;

/**
 * Created by lw on 2019/6/20.
 */
public class FastInboxNotifyAction {
    public static final int ID_NOTIFY_FAST_INBOX            = 1;
    public static final int VALUE_REQUEST_CODE_FAST_INBOX   = 1;
    public static final String NAME_NOTIFY_FAST_INBOX       = MyRInfo.getStringByID(R.string.notify_remote_input_inbox);

    public static final String RESULT_KEY_REMOTE_INPUT_INBOX = MyRInfo.getStringByID(R.string.notify_remote_input_inbox);
    public static final String LABEL_REMOTE_INPUT_INBOX     = MyRInfo.getStringByID(R.string.prompt_fast_inbox);
    public static final String LABEL_ACTION_INBOX           = MyRInfo.getStringByID(R.string.title_notify_action_fast_inbox);
    public static final String TITLE_NOTIFY_INBOX           = MyRInfo.getStringByID(R.string.title_notify_fast_inbox);
    public static final String DETAIL_NOTIFY_INBOX          = MyRInfo.getStringByID(R.string.prompt_inbox_msg);

    public static final String INTENT_ACTION_FAST_INBOX     =   MyRInfo.getStringByID(R.string.intent_action_fast_inbox);

    private boolean ifListening;
    private Service service;
    private NotifyAction notifyAction;
    private NotifyIntentReceiver notifyIntentReceiver;

    public FastInboxNotifyAction(Service service) {
        this.service     = service;
        notifyAction            = new NotifyAction();
        notifyIntentReceiver    = new NotifyIntentReceiver(service);
    }

    public boolean isIfListening() {
        return ifListening;
    }

    public void toEnableFastInboxNotify(){
        Notification notification = getNotification();
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        notifyAction.toStartNotify(ID_NOTIFY_FAST_INBOX, NAME_NOTIFY_FAST_INBOX, notification);
    }
    public void toDisableFastInboxNotify(){
        notifyAction.toStopNotify(ID_NOTIFY_FAST_INBOX);
    }

    public void toStartListen() {
        ifListening = notifyIntentReceiver.toStartListen();
    }

    public void toStopListen() {
        ifListening = !notifyIntentReceiver.toStopListen();
    }
    private Notification getNotification(){
        RemoteInput remoteInput     = notifyAction.getRemoteInput(RESULT_KEY_REMOTE_INPUT_INBOX, LABEL_REMOTE_INPUT_INBOX);
        PendingIntent intent        = notifyAction.getServiceIntent(VALUE_REQUEST_CODE_FAST_INBOX, new Intent(INTENT_ACTION_FAST_INBOX));
        Notification.Action action  = notifyAction.getNotifyAction(R.mipmap.ic_launcher_round, LABEL_ACTION_INBOX, intent, remoteInput);
        return notifyAction.getNotification(R.mipmap.ic_launcher_round, TITLE_NOTIFY_INBOX, DETAIL_NOTIFY_INBOX, true, action);
    }

    class NotifyIntentReceiver {
        private List<String> actionList;
        private BroadcastAction broadcastAction;
        private NotifyBroadcastReceiver broadcastReceiver;

        NotifyIntentReceiver(@NonNull ContextWrapper contextWrapper) {
            actionList          = Lists.newArrayList();
            broadcastAction     = new BroadcastAction();
            broadcastReceiver   = new NotifyBroadcastReceiver();

            actionList.add(INTENT_ACTION_FAST_INBOX);
        }

        boolean toStartListen() {
            return broadcastAction.registerReceiver(service, broadcastReceiver, actionList);
        }

        boolean toStopListen() {
            return broadcastAction.unregisterReceiver(service, broadcastReceiver);
        }
    }

    class NotifyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle remoteInput  = RemoteInput.getResultsFromIntent(intent);
            if(INTENT_ACTION_FAST_INBOX.equals(intent.getAction())) {
                String inputStuff = remoteInput.getString(RESULT_KEY_REMOTE_INPUT_INBOX);
                if(!TextUtils.isEmpty(inputStuff))
                    GtdMachine.getInstance().getCurrState(null).toInbox(inputStuff, inputStuff);
            }
            toDisableFastInboxNotify();
            toEnableFastInboxNotify();
        }
    }
}
