package com.utimer.mvp;

import android.app.Service;

import androidx.annotation.NonNull;

import com.utimer.common.FastInboxNotifyAction;

import org.joda.time.DateTime;

import ahtewlg7.utimer.common.MediaKvAction;
import ahtewlg7.utimer.db.media.NewImageObserver;
import ahtewlg7.utimer.entity.material.MediaInfo;
import ahtewlg7.utimer.factory.EventBusFatory;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class BackgroundP {
    BackgroundM m;
    IBackgroundV v;

    public BackgroundP(IBackgroundV v){
        this.v = v;

        m = new BackgroundM(v.getAttchService());
        m.toUdpateCursorQuery();
    }

    public void toListenNewMedia(){
        m.toListenMediaChange()
            .subscribe(new Consumer<MediaInfo>() {
                @Override
                public void accept(MediaInfo mediaInfo) throws Exception {
                    m.toSaveLastTime(mediaInfo.getCreateDate());
                    EventBusFatory.getInstance().getDefaultEventBus().postSticky(mediaInfo);
                }
            });
    }

    public void toStartNotify(){
        m.toStartNotify();
    }

    public void toStopNotify(){
        m.toStopNotify();
    }

    class BackgroundM{
        private NewImageObserver imageObserver;
        private FastInboxNotifyAction notifyAction;

        BackgroundM(@NonNull Service service){
            imageObserver = new NewImageObserver();
            notifyAction = new FastInboxNotifyAction(service);
        }
        void toSaveLastTime(DateTime dateTime){
            if(dateTime != null)
                MediaKvAction.getInstance().setLastImageTime(dateTime);
        }

        void toUdpateCursorQuery(){
            imageObserver.setPreQueryTime(MediaKvAction.getInstance().getLastImageTime());
        }

        Observable<MediaInfo> toListenMediaChange() {
            return imageObserver.toListenerMediaChange();
        }

        void toStartNotify(){
            notifyAction.toStartListen();
            notifyAction.toStartFastInboxNotify();
        }
        void toStopNotify(){
            notifyAction.toStopListen();
            notifyAction.toStopFastInboNotify();
        }
    }

    public interface IBackgroundV{
        public @NonNull Service getAttchService();
    }
}
