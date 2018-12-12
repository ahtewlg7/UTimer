package ahtewlg7.utimer.mvp.un;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;

import io.reactivex.Flowable;

/**
 * Created by lw on 2018/3/8.
 */

public interface IAllInfoMvpM<T> {
    public Flowable<T> loadAllEntity();
    public Flowable<Optional<T>> loadEntity(@NonNull Flowable<String> keyObservable);
    public T newEntity();
    public Flowable<Boolean> saveEntity(@NonNull Flowable<Optional<T>> flowable);
    public Flowable<Boolean> deleteEntity(@NonNull Flowable<Optional<T>> flowable);

    class GtdInboxMvpP /*implements IAllUtimerShowMvpP<AInboxEntity>*/ {
        public static final String TAG = GtdInboxMvpP.class.getSimpleName();

        /*private InboxListInfoMvpM inboxListInfoMvpM;
        private IInboxListInfoMvpV inboxListInfoMvpV;

        public GtdInboxMvpP(IInboxListInfoMvpV inboxListInfoMvpV){
            inboxListInfoMvpM = new InboxListInfoMvpM();
            this.inboxListInfoMvpV = inboxListInfoMvpV;
        }

        @Override
        public void toLoadAllData() {
            inboxListInfoMvpM.loadAllEntity()
                .compose(inboxListInfoMvpV.getFragment().getContext().<Optional<? extends AInboxEntity>>bindUntilEvent(FragmentEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySafeSubscriber<? extends AInboxEntity>(){
                    @Override
                    public void onSubscribe(Subscription s) {
                        super.onSubscribe(s);
                        inboxListInfoMvpV.onItemLoadStart();
                    }

                    @Override
                    public void onNext(AInboxEntity inboxEntity) {
                        super.onNext(inboxEntity);
    //                    noteEntityList.add(noteEntity);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        inboxListInfoMvpV.onItemLoadErr();
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
    //                    inboxListInfoMvpV.onItemLoadSucc(noteEntityList);
                    }
                });

        }

        @Override
        public void toAddItem(AInboxEntity entity) {

        }
        @Override
        public void toDeleteItem(AInboxEntity entity) {

        }


        public class InboxListInfoMvpM implements IAllInfoMvpM<T extends AInboxEntity>{
            private GtdShortHandListAction gtdInboxAction;

            public InboxListInfoMvpM(){
                gtdInboxAction = new GtdShortHandListAction();
            }

            @Override
            public Flowable<Optional<T>> loadAllEntity() {
                return gtdInboxAction.getAllShortHandList();
            }

            @Override
            public Flowable<Optional<T>> loadEntity(@NonNull Flowable<Optional<String>> keyObservable) {
                return null;
            }

            @Override
            public Flowable<Boolean> deleteEntity(@NonNull Flowable<Optional<AInboxEntity>> flowable) {
                return null;
            }
        }

        public interface IInboxListInfoMvpV extends IBaseGtdListMvpV<AInboxEntity> {
        }*/
    }
}
