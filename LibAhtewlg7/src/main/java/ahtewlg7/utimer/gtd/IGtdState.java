package ahtewlg7.utimer.gtd;

import com.google.common.base.Optional;

import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.un.IUtimerEntity;
import ahtewlg7.utimer.entity.gtd.AGtdEntity;
import ahtewlg7.utimer.entity.gtd.GtdActionEntity;
import ahtewlg7.utimer.entity.gtd.GtdProjectEntity;
import ahtewlg7.utimer.entity.gtd.ShortHandEntity;
import ahtewlg7.utimer.entity.material.AMaterialEntity;
import io.reactivex.Observable;

public interface IGtdState {
    public Observable<ShortHandEntity> toInbox(Observable<AMaterialEntity> materialObservable);
    public Observable<Boolean> toMaybe(Observable<IUtimerEntity> inboxObservable) ;
    public Observable<Optional<GtdProjectEntity>> toProject(Observable<AUtimerEntity> inboxObservable) ;
    public void toAddAction(IUtimerEntity utimerEntity);
    public void toEddGtd(IUtimerEntity utimerEntity);
    public void toHoldGtd(IUtimerEntity utimerEntity);
    public Optional<GtdActionEntity> getNextAction(AGtdEntity gtdEntity);

    public Observable<ShortHandEntity> getDailyCheckList();
    public Observable<GtdActionEntity> getToDoList();
    public Observable<GtdActionEntity> getDelegateList();
    public Observable<GtdActionEntity> getPersonDeferList();
    public Observable<GtdActionEntity> getTimeDeferList();
}
