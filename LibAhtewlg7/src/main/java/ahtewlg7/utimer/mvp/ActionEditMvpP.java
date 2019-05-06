package ahtewlg7.utimer.mvp;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;

import org.reactivestreams.Subscription;

import ahtewlg7.utimer.entity.BaseEventBusBean;
import ahtewlg7.utimer.entity.gtd.GtdActionEntity;
import ahtewlg7.utimer.entity.w5h2.BaseW5h2Entity;
import ahtewlg7.utimer.gtd.GtdActParser;
import ahtewlg7.utimer.state.GtdMachine;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lw on 2019/5/3.
 */
public class ActionEditMvpP {
    private ActionMvpM mvpM;
    private IActEditMvpV mvpV;
    private GtdActionEntity actionEntity;

    public ActionEditMvpP(IActEditMvpV mvpV, GtdActionEntity actionEntity){
        this.mvpV           = mvpV;
        this.actionEntity   = actionEntity;
        mvpM                = new ActionMvpM();
    }

    public void toParseW5h2(){
        mvpM.toParseW5h2(Flowable.just(actionEntity))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new MySafeSubscriber<Optional<BaseW5h2Entity>>(){
                @Override
                public void onNext(Optional<BaseW5h2Entity> w5h2EntityOptional) {
                    super.onNext(w5h2EntityOptional);
                    if(mvpV != null)
                        mvpV.onParseEnd(w5h2EntityOptional);
                }

                @Override
                public void onError(Throwable t) {
                    super.onError(t);
                    if(mvpV != null)
                        mvpV.onParseErr(t);
                }

                @Override
                public void onSubscribe(Subscription s) {
                    super.onSubscribe(s);
                    if(mvpV != null)
                        mvpV.onParseStart();
                }
            });
    }

    public void toFinishEdit(){
        mvpM.toFinishEdit(Flowable.just(actionEntity))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySafeSubscriber<Optional<BaseEventBusBean>>());
    }

    class ActionMvpM{
        private GtdActParser gtdActParser;

        ActionMvpM(){
            gtdActParser        = new GtdActParser();
        }

        Flowable<Optional<BaseW5h2Entity>> toParseW5h2(@NonNull Flowable<GtdActionEntity> actionEntityRx){
            return actionEntityRx.map(new Function<GtdActionEntity, Optional<BaseW5h2Entity>>() {
                @Override
                public Optional<BaseW5h2Entity> apply(GtdActionEntity actionEntity) throws Exception {
                    if(actionEntity == null || !actionEntity.ifValid())
                        return Optional.absent();
                    return gtdActParser.toParseW5h2(actionEntity.getDetail().get());
                }
            }).subscribeOn(Schedulers.computation());
        }

        Flowable<Optional<BaseEventBusBean>> toFinishEdit(@NonNull Flowable<GtdActionEntity> actionEntityRx){
            return actionEntityRx.map(new Function<GtdActionEntity, Optional<BaseEventBusBean>>() {
                @Override
                public Optional<BaseEventBusBean> apply(GtdActionEntity actionEntity) throws Exception {
                    return GtdMachine.getInstance().getCurrState(actionEntity).toGtd(actionEntity);
                }
            });
        }
    }
    public interface IActEditMvpV extends IRxLifeCycleBindView{
        public void onParseStart();
        public void onParseEnd(Optional<BaseW5h2Entity> baseW5h2EntityOptional);
        public void onParseErr(Throwable t);
    }
}
