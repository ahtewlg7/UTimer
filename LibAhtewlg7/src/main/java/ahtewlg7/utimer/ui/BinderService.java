package ahtewlg7.utimer.ui;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ahtewlg7.utimer.entity.IEventBusBean;
import ahtewlg7.utimer.entity.busevent.ActionBusEvent;
import ahtewlg7.utimer.enumtype.GtdBusEventType;
import ahtewlg7.utimer.factory.EventBusFatory;
import ahtewlg7.utimer.mvp.db.TableActionMvpP;
import ahtewlg7.utimer.mvp.db.TableNextIdMvpP;
import ahtewlg7.utimer.util.MySimpleObserver;
import io.reactivex.subjects.PublishSubject;


/**
 * Created by lw on 2016/11/15.
 */

public class BinderService extends Service{
    private MyTableActioMvpV myTableGtdMvpV;
    private MyTableNextIdMvpV myTableNextIdMvpV;

    private TableNextIdMvpP tableNextIdMvpP;
    private TableActionMvpP tableActionMvpP;
    private PublishSubject<IEventBusBean> eventBusRx;

    @Override
    public void onCreate() {
        super.onCreate();

        myTableGtdMvpV      = new MyTableActioMvpV();
        myTableNextIdMvpV   = new MyTableNextIdMvpV();
        tableNextIdMvpP     = new TableNextIdMvpP(myTableNextIdMvpV);
        tableActionMvpP     = new TableActionMvpP(myTableGtdMvpV);

        eventBusRx          = PublishSubject.create();

        EventBusFatory.getInstance().getDefaultEventBus().register(this);
        toListenEventBus();

//        tableNextIdMvpP.toLoadAll();
        tableActionMvpP.toLoadAll();
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
    @Subscribe(threadMode = ThreadMode.MAIN, sticky=true)
    public void onGtdActionEntity(ActionBusEvent actionBusEvent) {
        eventBusRx.onNext(actionBusEvent);
    }

    private void toListenEventBus(){
        eventBusRx/*.observeOn(AndroidSchedulers.mainThread())*/
            .subscribe(new MySimpleObserver<IEventBusBean>(){
                @Override
                public void onNext(IEventBusBean iEventBusBean) {
                    super.onNext(iEventBusBean);
                    if(iEventBusBean instanceof ActionBusEvent)
                        tableActionMvpP.toHandleActionEvent((ActionBusEvent)iEventBusBean);
                }
            });
    }

    //+++++++++++++++++++++++++++++++++++++++++++GtdMpvV+++++++++++++++++++++++++++++++++++++++++++++++++++
    class MyTableActioMvpV implements TableActionMvpP.ITableActionMvpV {
        @Override
        public void onAllLoadStarted() {

        }

        @Override
        public void onAllLoadEnd() {
            ActionBusEvent actionBusEvent = new ActionBusEvent(GtdBusEventType.LOAD);
            EventBusFatory.getInstance().getDefaultEventBus().post(actionBusEvent);
        }
    }
    //+++++++++++++++++++++++++++++++++++++++++++IDbActionMvpV+++++++++++++++++++++++++++++++++++++++++++++++
    class MyTableNextIdMvpV implements TableNextIdMvpP.IGtdIdKeyMvpV {
        @Override
        public void onAllLoadStarted() {
        }

        @Override
        public void onAllLoadEnd() {
        }
    }
}
