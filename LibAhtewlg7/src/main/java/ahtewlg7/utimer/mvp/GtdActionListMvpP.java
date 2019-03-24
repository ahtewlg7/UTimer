package ahtewlg7.utimer.mvp;

import android.support.annotation.NonNull;

import com.blankj.utilcode.util.FileUtils;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;

import org.reactivestreams.Subscription;

import java.util.List;

import ahtewlg7.utimer.entity.gtd.GtdActionEntity;
import ahtewlg7.utimer.factory.GtdActionByUuidFactory;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lw on 2018/12/9.
 */
public class GtdActionListMvpP{
    private IGtdActionListMvpV mvpV;
    private EntityListMvpM entityFlowable;

    public GtdActionListMvpP(IGtdActionListMvpV mvpV) {
        this.mvpV  = mvpV;
        entityFlowable = new EntityListMvpM();
    }

    public void toLoadAllItem() {
        entityFlowable.loadAllEntity()
                .compose(((RxFragment)mvpV.getRxLifeCycleBindView()).<List<GtdActionEntity>>bindUntilEvent(FragmentEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySafeSubscriber<List<GtdActionEntity>>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        super.onSubscribe(s);
                        if(mvpV != null)
                            mvpV.onItemLoadStart();
                    }

                    @Override
                    public void onNext(List<GtdActionEntity> entityList) {
                        super.onNext(entityList);
                        if(mvpV != null)
                            mvpV.onItemLoadEnd(entityList);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        if(mvpV != null)
                            mvpV.onItemLoadErr(t);
                    }
                });
    }

    public void toDeleteItem(@NonNull Flowable<GtdActionEntity>  entityRx) {
        entityRx.subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<GtdActionEntity>() {
                    @Override
                    public void accept(GtdActionEntity entity) throws Exception {
                        boolean result = entityFlowable.toDelEntity(entity);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySafeSubscriber<GtdActionEntity>() {
                    @Override
                    public void onNext(GtdActionEntity entity) {
                        super.onNext(entity);

                        boolean ifExited = entity.getAttachFile().ifValid();
                        if (mvpV != null && !ifExited) {
//                            int index  = entityList.indexOf(entity);
//                            entityList.remove(entity);
                            int index = 0;
                            mvpV.onDeleteSucc(index, entity);
                        }else if (mvpV != null)
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

    public void onItemCreated(GtdActionEntity entity) {
        /*entityList.add(entity);
        if(mvpV != null)
            mvpV.resetView(entityList);*/
    }

    public void onItemEdited(int index, GtdActionEntity entity) {
        /*entityList.set(index, entity);
        if(mvpV != null)
            mvpV.resetView(index, entity);*/
    }

    class EntityListMvpM{

        public Flowable<List<GtdActionEntity>> loadAllEntity() {
            return Flowable.just(GtdActionByUuidFactory.getInstance().getAll());
        }

        public boolean toDelEntity(GtdActionEntity entity) {
            /*if(entity != null){
                DelBusEvent delBusEvent = new DelBusEvent(entity.getId(),entity.getAttachFile().getAbsPath().get());
                EventBusFatory.getInstance().getDefaultEventBus().postSticky(delBusEvent);
            }*/

            boolean result = false;
            try{
                if(entity != null && entity.getAttachFile().ifValid())
                    result = FileUtils.deleteFile(entity.getAttachFile().getFile());
            }catch (Exception e){
                e.printStackTrace();
            }
            return result;
        }
    }

    public interface IGtdActionListMvpV extends IAllItemListMvpV<GtdActionEntity> {
    }
}
