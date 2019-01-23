package ahtewlg7.utimer.state.un;

import com.google.common.base.Optional;

import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.un.IUtimerEntity;
import ahtewlg7.utimer.entity.gtd.AGtdEntity;
import ahtewlg7.utimer.entity.gtd.GtdActionEntity;
import ahtewlg7.utimer.entity.gtd.GtdProjectEntity;
import ahtewlg7.utimer.entity.gtd.ShortHandEntity;
import ahtewlg7.utimer.entity.material.AMaterialEntity;
import ahtewlg7.utimer.util.Logcat;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class BaseGtdState implements IGtdState {
    public static final String TAG = BaseGtdState.class.getSimpleName();

    protected GtdMachine gtdMachine;

    public BaseGtdState(GtdMachine gtdMachine){
        this.gtdMachine = gtdMachine;
    }


    @Override
    public Observable<ShortHandEntity> toInbox(Observable<AMaterialEntity> materialObservable) {
        return materialObservable.map(new Function<AMaterialEntity, ShortHandEntity>() {
            @Override
            public ShortHandEntity apply(AMaterialEntity entity) throws Exception {
                Logcat.d(TAG,"toInbox cancel");
                return null;
            }
        });
    }

    @Override
    public Observable<Boolean> toMaybe(Observable<IUtimerEntity> inboxObservable) {
        return inboxObservable.map(new Function<IUtimerEntity, Boolean>() {
            @Override
            public Boolean apply(IUtimerEntity entity) throws Exception {
                Logcat.d(TAG,"toMaybe cancel");
                return false;
            }
        });
    }

    @Override
    public Observable<Optional<GtdProjectEntity>> toProject(Observable<AUtimerEntity> inboxObservable) {
        return inboxObservable.map(new Function<AUtimerEntity, Optional<GtdProjectEntity>>() {
            @Override
            public Optional<GtdProjectEntity> apply(AUtimerEntity inboxEntity) throws Exception {
                Logcat.d(TAG,"toProject InboxEntity cancel");
                return Optional.absent();
            }
        });
    }

    @Override
    public void toAddAction(IUtimerEntity utimerEntity) {
        Logcat.d(TAG,"toAddAction cancel");
    }

    @Override
    public void toEddGtd(IUtimerEntity utimerEntity) {
        Logcat.d(TAG,"toEddGtd cancel");
    }

    @Override
    public void toHoldGtd(IUtimerEntity utimerEntity) {
        Logcat.d(TAG,"toHoldGtd cancel");
    }

    @Override
    public Optional<GtdActionEntity> getNextAction(AGtdEntity gtdEntity) {
        Logcat.d(TAG,"getNextAction cancel");
        return Optional.absent();
    }

    @Override
    public Observable<ShortHandEntity> getDailyCheckList() {
        Logcat.d(TAG,"getDailyCheckList cancel");
        return null;
    }

    @Override
    public Observable<GtdActionEntity> getToDoList() {
        Logcat.d(TAG,"getToDoList cancel");
        return null;
    }

    @Override
    public Observable<GtdActionEntity> getDelegateList() {
        Logcat.d(TAG,"getDelegateList cancel");
        return null;
    }

    @Override
    public Observable<GtdActionEntity> getPersonDeferList() {
        Logcat.d(TAG,"getPersonDeferList cancel");
        return null;
    }

    @Override
    public Observable<GtdActionEntity> getTimeDeferList() {
        Logcat.d(TAG,"getTimeDeferList cancel");
        return null;
    }
}
