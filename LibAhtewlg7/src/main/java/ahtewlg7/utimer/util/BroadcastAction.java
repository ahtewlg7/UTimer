package ahtewlg7.utimer.util;

import android.content.BroadcastReceiver;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.blankj.utilcode.util.Utils;

import java.util.List;

/**
 * Created by lw on 2019/6/21.
 */
public class BroadcastAction {
    public boolean sendEmptyBroadcast(String action){
        if(TextUtils.isEmpty(action))
            return false;
        Intent intent = new Intent(action);
        return sendBroadcast(intent);
    }
    public boolean sendBroadcast(Intent intent){
        boolean ifSucc = true;
        try{
            Utils.getApp().sendBroadcast(intent);
        }catch (Exception e){
            e.printStackTrace();
            ifSucc = false;
        }
        return ifSucc;
    }

    public boolean registerReceiver(ContextWrapper contextWrapper, BroadcastReceiver broadcastReceiver, List<String> actionList){
        if(contextWrapper == null || broadcastReceiver == null || actionList.isEmpty())
            return false;
        boolean ifSucc = true;
        try{
            IntentFilter intentFilter = new IntentFilter();
            for(String action : actionList)
                if(!TextUtils.isEmpty(action))
                    intentFilter.addAction(action);
            contextWrapper.registerReceiver(broadcastReceiver,intentFilter);
        }catch (Exception e){
            e.printStackTrace();
            ifSucc = false;
        }
        return ifSucc;
    }
    public boolean unregisterReceiver(ContextWrapper contextWrapper, BroadcastReceiver broadcastReceiver){
        if(contextWrapper == null || broadcastReceiver == null)
            return false;
        boolean ifSucc = true;
        try{
            contextWrapper.unregisterReceiver(broadcastReceiver);
        }catch (Exception e){
            e.printStackTrace();
            ifSucc = false;
        }
        return ifSucc;
    }
}
