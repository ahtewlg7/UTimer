package ahtewlg7.utimer.mvp;

import android.support.annotation.NonNull;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;

import org.reactivestreams.Subscription;

import java.util.Collections;
import java.util.List;

import ahtewlg7.utimer.comparator.DeedWarningTimeComparator;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.enumtype.DeedState;
import ahtewlg7.utimer.factory.GtdDeedByUuidFactory;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by lw on 2019/6/25.
 */
public class BaseDeedListMvpP {
    private IBaseDeedMvpV mvpV;
    private BaseDeedMvpM mvpM;

    public BaseDeedListMvpP(IBaseDeedMvpV mvpV){
        this.mvpV = mvpV;
        mvpM      = new BaseDeedMvpM();
    }
    public void toLoadDeedByState(final DeedState deedState){
        mvpM.toLoad(deedState)
            .compose(((RxFragment)mvpV.getRxLifeCycleBindView()).<List<GtdDeedEntity>>bindUntilEvent(FragmentEvent.DESTROY))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new MySafeSubscriber<List<GtdDeedEntity>>() {
                @Override
                public void onSubscribe(Subscription s) {
                    super.onSubscribe(s);
                    if(mvpV != null)
                        mvpV.onLoadStart(deedState);
                }

                @Override
                public void onNext(List<GtdDeedEntity> entityList) {
                    super.onNext(entityList);
                    if(mvpV != null)
                        mvpV.onLoadSucc(deedState, entityList);
                }

                @Override
                public void onError(Throwable t) {
                    super.onError(t);
                    if(mvpV != null)
                        mvpV.onLoadErr(deedState, t);
                }

                @Override
                public void onComplete() {
                    super.onComplete();
                    if(mvpV != null)
                        mvpV.onLoadEnd(deedState);
                }
            });
    }



    class BaseDeedMvpM{
        Flowable<List<GtdDeedEntity>> toLoad(DeedState... state) {
            return GtdDeedByUuidFactory.getInstance().getEntityByState(state)
                    .doOnNext(new Consumer<List<GtdDeedEntity>>() {
                        @Override
                        public void accept(List<GtdDeedEntity> entityList) throws Exception {
                            Collections.sort(entityList, new DeedWarningTimeComparator().getAscOrder());
                        }
                    });
        }
    }
    public interface IBaseDeedMvpV extends IRxLifeCycleBindView{
        public @NonNull LifecycleProvider getRxLifeCycleBindView();
        public void onLoadStart(DeedState state);
        public void onLoadSucc(DeedState state, List<GtdDeedEntity> entityList);
        public void onLoadErr(DeedState state, Throwable err);
        public void onLoadEnd(DeedState state);
    }

}
