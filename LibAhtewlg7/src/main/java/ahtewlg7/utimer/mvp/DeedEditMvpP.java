package ahtewlg7.utimer.mvp;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;

import org.joda.time.DateTime;
import org.reactivestreams.Subscription;

import java.util.List;
import java.util.concurrent.TimeUnit;

import ahtewlg7.utimer.entity.BaseEventBusBean;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.entity.w5h2.BaseW5h2Entity;
import ahtewlg7.utimer.gtd.GtdDeedParser;
import ahtewlg7.utimer.state.GtdMachine;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lw on 2019/5/3.
 */
public class DeedEditMvpP {
    private boolean isNlping;

    private ActionMvpM mvpM;
    private IActEditMvpV mvpV;
    private GtdDeedEntity actionEntity;

    public DeedEditMvpP(IActEditMvpV mvpV, GtdDeedEntity actionEntity){
        this.mvpV           = mvpV;
        this.actionEntity   = actionEntity;
        mvpM                = new ActionMvpM();
    }

    public boolean isNlping() {
        return isNlping;
    }

    public void toParseW5h2(){
        mvpM.toParseW5h2(Flowable.just(actionEntity).throttleFirst(3, TimeUnit.SECONDS))
            .compose(((RxFragment) mvpV.getRxLifeCycleBindView()).<Optional<BaseW5h2Entity>>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new MySafeSubscriber<Optional<BaseW5h2Entity>>(){
                @Override
                public void onNext(Optional<BaseW5h2Entity> w5h2EntityOptional) {
                    super.onNext(w5h2EntityOptional);
                    isNlping = false;
                    /*if(w5h2EntityOptional.isPresent())
                        actionEntity.getW5h2Entity(w5h2EntityOptional.get())*/
                    if(mvpV != null)
                        mvpV.onParseEnd();
                }

                @Override
                public void onError(Throwable t) {
                    super.onError(t);
                    isNlping = false;
                    if(mvpV != null)
                        mvpV.onParseErr(t);
                }

                @Override
                public void onSubscribe(Subscription s) {
                    super.onSubscribe(s);
                    isNlping = true;
                    if(mvpV != null)
                        mvpV.onParseStart();
                }
            });
    }
    public void toParseTimeElement(){
        mvpM.toParseTimeElement(Flowable.just(actionEntity).throttleFirst(3, TimeUnit.SECONDS))
                .compose(((RxFragment) mvpV.getRxLifeCycleBindView()).<Optional<List<DateTime>>>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySafeSubscriber<Optional<List<DateTime>>>(){
                    @Override
                    public void onNext(Optional<List<DateTime>> warningTimeList) {
                        super.onNext(warningTimeList);
                        isNlping = false;
                        if(warningTimeList.isPresent())
                            actionEntity.setWarningTimeList(warningTimeList.get());
                        if(mvpV != null)
                            mvpV.onParseEnd();
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        isNlping = false;
                        if(mvpV != null)
                            mvpV.onParseErr(t);
                    }

                    @Override
                    public void onSubscribe(Subscription s) {
                        super.onSubscribe(s);
                        isNlping = true;
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
        private GtdDeedParser gtdActParser;

        ActionMvpM(){
            gtdActParser        = new GtdDeedParser();
        }

        Flowable<Optional<BaseW5h2Entity>> toParseW5h2(@NonNull Flowable<GtdDeedEntity> actionEntityRx){
            return actionEntityRx.map(new Function<GtdDeedEntity, Optional<BaseW5h2Entity>>() {
                @Override
                public Optional<BaseW5h2Entity> apply(GtdDeedEntity actionEntity) throws Exception {
                    if(actionEntity == null || !actionEntity.ifValid())
                        return Optional.absent();
                    return gtdActParser.toParseW5h2(actionEntity.getDetail().get());
                }
            }).subscribeOn(Schedulers.computation());
        }

        Flowable<Optional<List<DateTime>>> toParseTimeElement(@NonNull Flowable<GtdDeedEntity> actionEntityRx){
            return actionEntityRx.map(new Function<GtdDeedEntity, Optional<List<DateTime>>>() {
                @Override
                public Optional<List<DateTime>> apply(GtdDeedEntity actionEntity) throws Exception {
                    if(actionEntity == null || !actionEntity.ifValid())
                        return Optional.absent();
                    return gtdActParser.toParseTimeElement(actionEntity.getDetail().get());
                }
            }).subscribeOn(Schedulers.computation());
        }

        Flowable<Optional<BaseEventBusBean>> toFinishEdit(@NonNull Flowable<GtdDeedEntity> actionEntityRx){
            return actionEntityRx.map(new Function<GtdDeedEntity, Optional<BaseEventBusBean>>() {
                @Override
                public Optional<BaseEventBusBean> apply(GtdDeedEntity actionEntity) throws Exception {
                    return GtdMachine.getInstance().getCurrState(actionEntity).toGtd(actionEntity);
                }
            });
        }
    }
    public interface IActEditMvpV extends IRxLifeCycleBindView{
        public void onParseStart();
        public void onParseEnd();
        public void onParseErr(Throwable t);
    }
}
