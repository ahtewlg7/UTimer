package ahtewlg7.utimer.gtd;

import ahtewlg7.utimer.entity.gtd.GtdActionEntity;
import io.reactivex.Observable;

public class DailyGtdState extends BaseGtdState {
    public static final String TAG = DailyGtdState.class.getSimpleName();

    public DailyGtdState(GtdMachine gtdMachine) {
        super(gtdMachine);
    }

    @Override
    public Observable<GtdActionEntity> getToDoList() {
        return super.getToDoList();
    }

    @Override
    public Observable<GtdActionEntity> getDelegateList() {
        return super.getDelegateList();
    }

    @Override
    public Observable<GtdActionEntity> getPersonDeferList() {
        return super.getPersonDeferList();
    }

    @Override
    public Observable<GtdActionEntity> getTimeDeferList() {
        return super.getTimeDeferList();
    }
}
