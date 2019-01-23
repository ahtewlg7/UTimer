package ahtewlg7.utimer.mvp.un;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.reactivestreams.Subscription;

import java.util.List;

import ahtewlg7.utimer.entity.busevent.DelBusEvent;
import ahtewlg7.utimer.entity.busevent.EditBusEvent;
import ahtewlg7.utimer.entity.busevent.EditEndBusEvent;
import ahtewlg7.utimer.entity.gtd.ShortHandBuilder;
import ahtewlg7.utimer.entity.gtd.ShortHandEntity;
import ahtewlg7.utimer.gtd.GtdShortHandListAction;
import ahtewlg7.utimer.util.DateTimeAction;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class ShorthandListInfoMvpP extends AUtimerListMvpP<ShortHandEntity> {
    public static final String TAG = ShorthandListInfoMvpP.class.getSimpleName();

    private List<ShortHandEntity> shorthandList;
    private IShorthandListMvpV shorthandListMvpV;
    private ShorthandListMvpM shorthandListMvpM;

    public ShorthandListInfoMvpP(IShorthandListMvpV shorthandListMvpV) {
        super();
        this.shorthandListMvpV  = shorthandListMvpV;
        shorthandList           = Lists.newArrayList();
        shorthandListMvpM       = new ShorthandListMvpM();
    }

    //=======================================EventBus================================================
    //EventBus callback
    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEditEndBusEvent(EditEndBusEvent event) {
        Logcat.i(TAG, "onEditEndBusEvent  : " + event.toString());

        Flowable.just(event).filter(new Predicate<EditEndBusEvent>() {
                @Override
                public boolean test(EditEndBusEvent editEndBusEvent) throws Exception {
                    return editEndBusEvent.ifValid();
                }
            })
            .compose(shorthandListMvpV.getFragment().<EditEndBusEvent>bindUntilEvent(FragmentEvent.DESTROY))
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(new MySafeSubscriber<EditEndBusEvent>() {
                @Override
                public void onNext(EditEndBusEvent event) {
                    super.onNext(event);
                    shorthandList.add(0, (ShortHandEntity) event.getUtimerEntity());
                }

                @Override
                public void onComplete() {
                    super.onComplete();
                    if (!shorthandList.isEmpty())
                        shorthandListMvpV.resetView(shorthandList);
                }
            });
    }

    //=============================================================================================
    @Override
    public void toLoadAllItem() {
        shorthandListMvpM.loadAllEntity()
                .compose(shorthandListMvpV.getFragment().<ShortHandEntity>bindUntilEvent(FragmentEvent.DESTROY))
                /*.filter(new Predicate<Optional<ShortHandEntity>>() {
                    @Override
                    public boolean test(Optional<ShortHandEntity> optional) throws Exception {
                        return optional.isPresent();
                    }
                })
                .map(new Function<Optional<ShortHandEntity>, ShortHandEntity>() {
                    @Override
                    public ShortHandEntity apply(Optional<ShortHandEntity> noteEntityOptional) throws Exception {
                        return noteEntityOptional.get();
                    }
                })
                .sorted(Ordering.natural().onResultOf(new com.google.common.base.Function<ShortHandEntity, Comparable>() {
                    @Override
                    @ParametersAreNonnullByDefault
                    public Comparable apply(ShortHandEntity input) {
                        return input.getLastAccessTime();
                    }
                }).reverse())*/
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySafeSubscriber<ShortHandEntity>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        super.onSubscribe(s);
                        shorthandList.clear();
                        shorthandListMvpV.onViewInitStart();
                    }

                    @Override
                    public void onNext(ShortHandEntity entity) {
                        super.onNext(entity);
                        Logcat.i(TAG,"toLoadAllItem onNext : " + entity.toString());
                        shorthandList.add(entity);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        shorthandListMvpV.onViewInitErr();
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        shorthandListMvpV.initView(shorthandList);
                    }
                });

    }

    @Override
    public void toDeleteItem(final ShortHandEntity shortHandEntity) {
        shorthandListMvpM.deleteEntity(Flowable.just(Optional.of(shortHandEntity)))
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySafeSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        super.onNext(aBoolean);
                        if (aBoolean) {
                            shorthandListMvpV.onDeleteSucc(shortHandEntity);
                            if(shortHandEntity.getAttachFile().getAbsPath().isPresent()){
                                DelBusEvent delBusEvent = new DelBusEvent(shortHandEntity.getId(), shortHandEntity.getAttachFile().getAbsPath().get());
                                Logcat.i(TAG, "toDeleteItem : entity = " + shortHandEntity.toString() + ",delBusEvent = " + delBusEvent.toString());
                                eventBus.post(delBusEvent);
                            }
                        } else
                            shorthandListMvpV.onDeleteFail(shortHandEntity);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        shorthandListMvpV.onDeleteErr(t, shortHandEntity);
                    }
                });
    }

    public void toDeleteItem(int index) {
        try {
            ShortHandEntity entity = shorthandList.get(index);
            toDeleteItem(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void toEditItem(ShortHandEntity shortHandEntity) {
        eventBus.postSticky(new EditBusEvent(shortHandEntity));
        shorthandList.remove(shortHandEntity);
    }

    public void toCreateItem() {
        Logcat.i(TAG, "toCreate ");
        String now = new DateTimeAction().toFormatNow().toString();
        ShortHandEntity e = (ShortHandEntity)new ShortHandBuilder().setTitle(now).build();
        eventBus.postSticky(new EditBusEvent(e));
    }

    public void toGtdProject(ShortHandEntity entity) {
    }

    public class ShorthandListMvpM implements IAllInfoMvpM<ShortHandEntity> {
        private GtdShortHandListAction shortHandAction;

        public ShorthandListMvpM() {
            shortHandAction = new GtdShortHandListAction();
        }

        @Override
        public Flowable<ShortHandEntity> loadAllEntity() {
            return shortHandAction.loadAllEntity().subscribeOn(Schedulers.io());
        }

        @Override
        public Flowable<Optional<ShortHandEntity>> loadEntity(@NonNull Flowable<String> keyObservable) {
//            return shortHandAction.loadEntity(keyObservable);
            return Flowable.just(Optional.<ShortHandEntity>absent());//just for test
        }

        @Override
        public Flowable<Boolean> saveEntity(@NonNull Flowable<Optional<ShortHandEntity>> flowable) {
            return shortHandAction.saveEntity(flowable);
        }

        @Override
        public Flowable<Boolean> deleteEntity(@NonNull Flowable<Optional<ShortHandEntity>> flowable) {
            return shortHandAction.deleteEntity(flowable);
        }
    }

    public interface IShorthandListMvpV extends IBaseGtdListMvpV<ShortHandEntity> {
    }
}
