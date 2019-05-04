package ahtewlg7.utimer.ui;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ahtewlg7.utimer.entity.BaseEventBusBean;
import ahtewlg7.utimer.entity.busevent.ActionBusEvent;
import ahtewlg7.utimer.entity.busevent.ActivityBusEvent;
import ahtewlg7.utimer.entity.busevent.UTimerBusEvent;
import ahtewlg7.utimer.factory.EventBusFatory;
import ahtewlg7.utimer.mvp.rw.AllEntityRwMvpP;
import ahtewlg7.utimer.nlp.NlpAction;
import ahtewlg7.utimer.util.MySimpleObserver;
import io.reactivex.subjects.PublishSubject;


/**
 * Created by lw on 2016/11/15.
 */

public class BinderService extends Service{
    private AllEntityRwMvpP entityRwMvpP;
    private PublishSubject<BaseEventBusBean> eventBusRx;

    @Override
    public void onCreate() {
        super.onCreate();

        eventBusRx      = PublishSubject.create();
        entityRwMvpP    = new AllEntityRwMvpP(null, null,null);

        EventBusFatory.getInstance().getDefaultEventBus().register(this);
        toListenEventBus();

        entityRwMvpP.toLoadAll();
        NlpAction.getInstance().initNlp();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusFatory.getInstance().getDefaultEventBus().unregister(this);
    }

    //+++++++++++++++++++++++++++++++++++++++++++Binder+++++++++++++++++++++++++++++++++++++++++++++++++++
    @Override
    public IBinder onBind(Intent arg0) {
        return new BaseServiceBinder();
    }

    public class BaseServiceBinder extends Binder {
        public BinderService getService() {
            return BinderService.this;
        }
    }
    //+++++++++++++++++++++++++++++++++++++++++++EventBus+++++++++++++++++++++++++++++++++++++++++++++++++++
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onActionBusEvent(ActionBusEvent actionBusEvent) {
        eventBusRx.onNext(actionBusEvent);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onActivityBusEvent(ActivityBusEvent actionBusEvent) {
        eventBusRx.onNext(actionBusEvent);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUTimerBusEvent(UTimerBusEvent utimerBusEvent) {
        eventBusRx.onNext(utimerBusEvent);
    }

    private void toListenEventBus(){
        eventBusRx/*.observeOn(AndroidSchedulers.mainThread())*/
            .subscribe(new MySimpleObserver<BaseEventBusBean>(){
                @Override
                public void onNext(BaseEventBusBean eventBusBean) {
                    entityRwMvpP.toHandleBusEvent(eventBusBean);
                }
            });
    }
}
