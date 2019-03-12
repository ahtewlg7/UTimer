package ahtewlg7.utimer.mvp;

import android.support.annotation.NonNull;

import com.blankj.utilcode.util.FileUtils;
import com.google.common.collect.Lists;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;

import org.reactivestreams.Subscription;

import java.util.List;

import ahtewlg7.utimer.entity.gtd.GtdActionEntity;
import ahtewlg7.utimer.gtd.GtdActionListAction;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lw on 2018/12/9.
 */
public class GtdActionListMvpP implements IAllItemListMvpP<GtdActionEntity>{
    public static final String TAG = GtdActionListMvpP.class.getSimpleName();

    private List<GtdActionEntity> entityList;

    private IGtdActionListMvpV shorthandListMvpV;
    private EntityListMvpM shorthandListMvpM;

    public GtdActionListMvpP(IGtdActionListMvpV shorthandListMvpV) {
        this.shorthandListMvpV  = shorthandListMvpV;
        shorthandListMvpM       = new EntityListMvpM();
        entityList              = Lists.newArrayList();
    }

    @Override
    public void toLoadAllItem() {
        Logcat.i(TAG,"toLoadAllItem");
        shorthandListMvpM.loadAllEntity()
                .compose(((RxFragment)shorthandListMvpV.getRxLifeCycleBindView()).<GtdActionEntity>bindUntilEvent(FragmentEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySafeSubscriber<GtdActionEntity>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        super.onSubscribe(s);
                        entityList.clear();
                        if(shorthandListMvpV != null)
                            shorthandListMvpV.onItemLoadStart();
                    }

                    @Override
                    public void onNext(GtdActionEntity entity) {
                        super.onNext(entity);
                        Logcat.i(TAG,"toLoadAllItem onNext : " + entity.toString());
                        entityList.add(entity);
                        if(shorthandListMvpV != null)
                            shorthandListMvpV.onItemLoad(entity);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        if(shorthandListMvpV != null)
                            shorthandListMvpV.onItemLoadErr(t);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        if(shorthandListMvpV != null)
                            shorthandListMvpV.onItemLoadEnd(entityList);
                    }
                });
    }

    @Override
    public void toDeleteItem(@NonNull Flowable<GtdActionEntity>  entityRx) {
        entityRx.subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<GtdActionEntity>() {
                    @Override
                    public void accept(GtdActionEntity entity) throws Exception {
                        boolean result = shorthandListMvpM.toDelEntity(entity);
                        Logcat.i(TAG,"toDeleteItem " + entity.getTitle() + " : " + result);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySafeSubscriber<GtdActionEntity>() {
                    @Override
                    public void onNext(GtdActionEntity entity) {
                        super.onNext(entity);

                        boolean ifExited = entity.getAttachFile().ifValid();
                        if (shorthandListMvpV != null && !ifExited) {
                            int index  = entityList.indexOf(entity);
//                            entityList.remove(entity);
                            shorthandListMvpV.onDeleteSucc(index, entity);
                        }else if (shorthandListMvpV != null)
                            shorthandListMvpV.onDeleteFail(entity);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (shorthandListMvpV != null)
                            shorthandListMvpV.onDeleteErr(e);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        if (shorthandListMvpV != null)
                            shorthandListMvpV.onDeleteEnd();
                    }
                });
    }

    @Override
    public void onItemCreated(GtdActionEntity entity) {
        entityList.add(entity);
        if(shorthandListMvpV != null)
            shorthandListMvpV.resetView(entityList);
    }

    @Override
    public void onItemEdited(int index, GtdActionEntity entity) {
        entityList.set(index, entity);
        if(shorthandListMvpV != null)
            shorthandListMvpV.resetView(index, entity);
    }

    class EntityListMvpM implements IAllItemListMvpM<GtdActionEntity>{
        private GtdActionListAction entityListAction;

        EntityListMvpM(){
            entityListAction = new GtdActionListAction();
        }

        @Override
        public Flowable<GtdActionEntity> loadAllEntity() {
            return entityListAction.loadAllEntity();
        }

        @Override
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
