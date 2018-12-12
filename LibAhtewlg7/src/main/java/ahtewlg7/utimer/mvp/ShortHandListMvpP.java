package ahtewlg7.utimer.mvp;

import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;

import org.reactivestreams.Subscription;

import ahtewlg7.utimer.entity.gtd.ShortHandEntity;
import ahtewlg7.utimer.factory.ShortHandFactory;
import ahtewlg7.utimer.gtd.GtdShortHandListAction;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by lw on 2018/12/9.
 */
public class ShortHandListMvpP implements IAllItemListMvpP<ShortHandEntity>{
    public static final String TAG = ShortHandListMvpP.class.getSimpleName();

    private IShorthandListMvpV shorthandListMvpV;
    private ShorthandListMvpM shorthandListMvpM;

    public ShortHandListMvpP(IShorthandListMvpV shorthandListMvpV) {
        this.shorthandListMvpV  = shorthandListMvpV;
        shorthandListMvpM       = new ShorthandListMvpM();
    }

    @Override
    public void toLoadAllItem() {
        Logcat.i(TAG,"toLoadAllItem");
        shorthandListMvpM.loadAllEntity()
                .compose(((RxFragment)shorthandListMvpV.getRxLifeCycleBindView()).<ShortHandEntity>bindUntilEvent(FragmentEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySafeSubscriber<ShortHandEntity>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        super.onSubscribe(s);
                        if(shorthandListMvpV != null)
                            shorthandListMvpV.onItemLoadStart();
                    }

                    @Override
                    public void onNext(ShortHandEntity entity) {
                        super.onNext(entity);
                        Logcat.i(TAG,"toLoadAllItem onNext : " + entity.toString());
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
                            shorthandListMvpV.onItemLoadEnd();
                    }
                });
    }

    @Override
    public void toDeleteItem(ShortHandEntity entity) {

    }

    @Override
    public void toCreateItem() {

    }

    class ShorthandListMvpM implements IAllItemListMvpM<ShortHandEntity>{
        private GtdShortHandListAction shortHandAction;

        ShorthandListMvpM(){
            shortHandAction = new GtdShortHandListAction();
        }

        @Override
        public Flowable<ShortHandEntity> loadAllEntity() {
            return shortHandAction.loadAllEntity()
                    .doOnSubscribe(new Consumer<Subscription>() {
                        @Override
                        public void accept(Subscription subscription) throws Exception {
                            ShortHandFactory.getInstance().clearAll();
                        }
                    })
                    .doOnNext(new Consumer<ShortHandEntity>() {
                        @Override
                        public void accept(ShortHandEntity entity) throws Exception {
                            ShortHandFactory.getInstance().addValue(entity);
                        }
                    });
        }
    }

    public interface IShorthandListMvpV extends IAllItemListMvpV<ShortHandEntity> {
    }
}
