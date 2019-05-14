package ahtewlg7.utimer.mvp;

import android.support.annotation.NonNull;

import com.google.common.collect.Lists;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;

import org.reactivestreams.Subscription;

import java.util.List;

import ahtewlg7.utimer.entity.busevent.DeedBusEvent;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.enumtype.GtdBusEventType;
import ahtewlg7.utimer.factory.EventBusFatory;
import ahtewlg7.utimer.factory.GtdDeedByUuidFactory;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static ahtewlg7.utimer.mvp.IAllItemListMvpV.INVALID_INDEX;

/**
 * Created by lw on 2018/12/9.
 */
public class DeedTodoListMvpP implements IAllItemListMvpP<GtdDeedEntity> {
    private IGtdTodoActionListMvpV mvpV;
    private EntityListMvpM mvpM;

    private List<GtdDeedEntity> entityList;

    public DeedTodoListMvpP(IGtdTodoActionListMvpV mvpV) {
        this.mvpV   = mvpV;
        mvpM        = new EntityListMvpM();
        entityList  = Lists.newArrayList();
    }

    @Override
    public void toLoadAllItem() {
        mvpM.loadAllEntity()
            .compose(((RxFragment)mvpV.getRxLifeCycleBindView()).<GtdDeedEntity>bindUntilEvent(FragmentEvent.DESTROY))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new MySafeSubscriber<GtdDeedEntity>() {
                @Override
                public void onSubscribe(Subscription s) {
                    super.onSubscribe(s);
                    entityList.clear();
                    if(mvpV != null)
                        mvpV.onItemLoadStart();
                }

                @Override
                public void onNext(GtdDeedEntity entity) {
                    super.onNext(entity);
                    entityList.add(entity);
                }

                @Override
                public void onError(Throwable t) {
                    super.onError(t);
                    if(mvpV != null)
                        mvpV.onItemLoadErr(t);
                }

                @Override
                public void onComplete() {
                    super.onComplete();

                    if(mvpV != null)
                        mvpV.onItemLoadEnd(entityList);
                }
            });
    }
    @Override
    public void toDeleteItem(@NonNull Flowable<GtdDeedEntity>  entityRx) {
        entityRx.subscribe(new MySafeSubscriber<GtdDeedEntity>() {
                    @Override
                    public void onNext(GtdDeedEntity entity) {
                        super.onNext(entity);
                        EventBusFatory.getInstance().getDefaultEventBus().postSticky(new DeedBusEvent(GtdBusEventType.DELETE, entity));

                        if(mvpV != null && entity.ifValid())
                            mvpV.onDeleteSucc(INVALID_INDEX, entity);
                        else if (mvpV != null)
                            mvpV.onDeleteFail(entity);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (mvpV != null)
                            mvpV.onDeleteErr(e);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        if (mvpV != null)
                            mvpV.onDeleteEnd();
                    }
                });
    }
    @Override
    public void onItemCreated(GtdDeedEntity entity) {
        /*entityList.add(entity);
        if(mvpV != null)
            mvpV.resetView(entityList);*/
    }
    @Override
    public void onItemEdited(int index, GtdDeedEntity entity) {
        /*entityList.set(index, entity);
        if(mvpV != null)
            mvpV.resetView(index, entity);*/
    }

    class EntityListMvpM{
        public Flowable<GtdDeedEntity> loadAllEntity() {
            return GtdDeedByUuidFactory.getInstance().getEntityByState();
        }
    }

    public interface IGtdTodoActionListMvpV extends IAllItemListMvpV<GtdDeedEntity> {
    }
}
