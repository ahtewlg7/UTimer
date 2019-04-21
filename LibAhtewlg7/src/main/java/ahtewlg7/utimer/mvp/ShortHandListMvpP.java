package ahtewlg7.utimer.mvp;

import android.support.annotation.NonNull;

import com.blankj.utilcode.util.FileUtils;
import com.google.common.collect.Lists;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;

import org.reactivestreams.Subscription;

import java.util.List;

import ahtewlg7.utimer.entity.busevent.UTimerBusEvent;
import ahtewlg7.utimer.entity.gtd.ShortHandEntity;
import ahtewlg7.utimer.enumtype.GtdBusEventType;
import ahtewlg7.utimer.factory.EventBusFatory;
import ahtewlg7.utimer.factory.ShortHandByUuidFactory;
import ahtewlg7.utimer.mvp.un.IAllItemListMvpM;
import ahtewlg7.utimer.mvp.un.IAllItemListMvpP;
import ahtewlg7.utimer.mvp.un.IAllItemListMvpV;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lw on 2018/12/9.
 */
public class ShortHandListMvpP implements IAllItemListMvpP<ShortHandEntity> {
    private List<ShortHandEntity> shortHandList;

    private IShorthandListMvpV shorthandListMvpV;
    private ShorthandListMvpM shorthandListMvpM;

    public ShortHandListMvpP(IShorthandListMvpV shorthandListMvpV) {
        this.shorthandListMvpV  = shorthandListMvpV;
        shorthandListMvpM       = new ShorthandListMvpM();
        shortHandList           = Lists.newArrayList();
    }

    @Override
    public void toLoadAllItem() {
        shorthandListMvpM.loadAllEntity()
                .compose(((RxFragment)shorthandListMvpV.getRxLifeCycleBindView()).<ShortHandEntity>bindUntilEvent(FragmentEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySafeSubscriber<ShortHandEntity>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        super.onSubscribe(s);
                        shortHandList.clear();
                        if(shorthandListMvpV != null)
                            shorthandListMvpV.onItemLoadStart();
                    }

                    @Override
                    public void onNext(ShortHandEntity entity) {
                        super.onNext(entity);
                        Logcat.i(TAG,"toLoadAllItem onNext : " + entity.toString());
                        shortHandList.add(entity);
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
                            shorthandListMvpV.onItemLoadEnd(shortHandList);
                    }
                });
    }

    @Override
    public void toDeleteItem(@NonNull Flowable<ShortHandEntity>  entityRx) {
        entityRx.subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<ShortHandEntity>() {
                    @Override
                    public void accept(ShortHandEntity entity) throws Exception {
                        boolean result = shorthandListMvpM.toDelEntity(entity);
                        if(result){
                            UTimerBusEvent delBusEvent = new UTimerBusEvent(GtdBusEventType.DELETE,entity);
                            EventBusFatory.getInstance().getDefaultEventBus().postSticky(delBusEvent);
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySafeSubscriber<ShortHandEntity>() {
                    @Override
                    public void onNext(ShortHandEntity entity) {
                        super.onNext(entity);

                        boolean ifExited = entity.getAttachFile().ifValid();
                        if (shorthandListMvpV != null && !ifExited) {
                            int index  = shortHandList.indexOf(entity);
//                            shortHandList.remove(entity);
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
    public void onItemCreated(ShortHandEntity entity) {
        shortHandList.add(entity);
        if(shorthandListMvpV != null)
            shorthandListMvpV.resetView(shortHandList);
    }

    @Override
    public void onItemEdited(int index, ShortHandEntity entity) {
        shortHandList.set(index, entity);
        if(shorthandListMvpV != null)
            shorthandListMvpV.resetView(index, entity);
    }

    class ShorthandListMvpM implements IAllItemListMvpM<ShortHandEntity> {
        ShorthandListMvpM(){
        }

        @Override
        public Flowable<ShortHandEntity> loadAllEntity() {
            return ShortHandByUuidFactory.getInstance().getAllLifeEntity();
        }

        @Override
        public boolean toDelEntity(ShortHandEntity entity) {
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

    public interface IShorthandListMvpV extends IAllItemListMvpV<ShortHandEntity> {
    }
}
