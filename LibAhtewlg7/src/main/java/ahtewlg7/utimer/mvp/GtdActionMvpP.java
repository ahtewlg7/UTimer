package ahtewlg7.utimer.mvp;

import android.support.annotation.NonNull;

import ahtewlg7.utimer.db.DbActionFacade;
import ahtewlg7.utimer.entity.gtd.GtdActionEntity;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by lw on 2019/3/7.
 */
public class GtdActionMvpP {
    private GtdActionMvpM mvpM;
    private IGtdActionMvpV mvpV;

    public GtdActionMvpP(IGtdActionMvpV mvpV){
        this.mvpV = mvpV;
        mvpM = new GtdActionMvpM();
    }

    public void toSaveAction(@NonNull Flowable<GtdActionEntity> actionEntityRx){
        mvpM.toSaveAction(actionEntityRx)
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(new MySafeSubscriber<Boolean>(){
                @Override
                public void onNext(Boolean aBoolean) {
                    super.onNext(aBoolean);
                }
            });
    }

    public

    class GtdActionMvpM{
        DbActionFacade dbActionFacade;

        GtdActionMvpM(){
            dbActionFacade = new DbActionFacade();
        }

        Flowable<Boolean> toSaveAction(@NonNull Flowable<GtdActionEntity> actionEntityRx){
            return dbActionFacade.saveActionEntity(actionEntityRx);
        }
    }

    public interface IGtdActionMvpV{
    }
}
