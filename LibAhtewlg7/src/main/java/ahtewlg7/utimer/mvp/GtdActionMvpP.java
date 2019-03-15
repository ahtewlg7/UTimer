package ahtewlg7.utimer.mvp;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;

import org.reactivestreams.Subscription;

import ahtewlg7.utimer.db.DbActionFacade;
import ahtewlg7.utimer.entity.gtd.GtdActionEntity;
import ahtewlg7.utimer.factory.GtdActionLruCacheFactory;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by lw on 2019/3/7.
 */
public class GtdActionMvpP {
    private boolean isLoaded = false;
    private GtdActionMvpM mvpM;
    private IGtdActionMvpV mvpV;

    public GtdActionMvpP(IGtdActionMvpV mvpV){
        this.mvpV = mvpV;
        mvpM = new GtdActionMvpM();
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void toSaveAction(@NonNull Flowable<GtdActionEntity> actionEntityRx){
        mvpM.toSaveAction(actionEntityRx.doOnNext(new Consumer<GtdActionEntity>() {
                    @Override
                    public void accept(GtdActionEntity actionEntity) throws Exception {
                        GtdActionLruCacheFactory.getInstance().add(actionEntity.getUuid(), actionEntity);
                    }
                }))
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(new MySafeSubscriber<Boolean>(){
                @Override
                public void onNext(Boolean aBoolean) {
                    super.onNext(aBoolean);
                }
            });
    }
    public void toLoadAllItem() {
        mvpM.loadAllEntity()/*.observeOn(AndroidSchedulers.mainThread())*/
            .subscribe(new MySafeSubscriber<GtdActionEntity>() {
                @Override
                public void onSubscribe(Subscription s) {
                    super.onSubscribe(s);
                    GtdActionLruCacheFactory.getInstance().clearAll();
                }

                @Override
                public void onNext(GtdActionEntity entity) {
                    super.onNext(entity);
                    Logcat.i(TAG,"toLoadAllItem onNext : " + entity.toString());
                    GtdActionLruCacheFactory.getInstance().add(entity.getUuid(), entity);
                }

                @Override
                public void onError(Throwable t) {
                    super.onError(t);
                }

                @Override
                public void onComplete() {
                    super.onComplete();
                    isLoaded = true;
                    if(mvpV != null)
                        mvpV.onActionAllLoaded();
                }
            });
    }

    class GtdActionMvpM{
        DbActionFacade dbActionFacade;

        GtdActionMvpM(){
            dbActionFacade = new DbActionFacade();
        }

        Flowable<Boolean> toSaveAction(@NonNull Flowable<GtdActionEntity> actionEntityRx){
            return dbActionFacade.saveActionEntity(actionEntityRx);
        }

        Flowable<GtdActionEntity> loadAllEntity() {
            return dbActionFacade.loadAllUndoActionEntity()
                    .filter(new Predicate<Optional<GtdActionEntity>>() {
                        @Override
                        public boolean test(Optional<GtdActionEntity> gtdActionEntityOptional) throws Exception {
                            return gtdActionEntityOptional.isPresent();
                        }
                    })
                    .map(new Function<Optional<GtdActionEntity>, GtdActionEntity>() {
                        @Override
                        public GtdActionEntity apply(Optional<GtdActionEntity> gtdActionEntityOptional) throws Exception {
                            return gtdActionEntityOptional.get();
                        }
                    });
        }
    }

    public interface IGtdActionMvpV{
        public void onActionAllLoaded();
    }
}
