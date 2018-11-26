package ahtewlg7.utimer.mvp;

public class GtdInboxMvpP /*implements IBaseListInfoMvpP<AInboxEntity>*/ {
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
                    inboxListInfoMvpV.onViewInitStart();
                }

                @Override
                public void onNext(AInboxEntity inboxEntity) {
                    super.onNext(inboxEntity);
//                    noteEntityList.add(noteEntity);
                }

                @Override
                public void onError(Throwable t) {
                    super.onError(t);
                    inboxListInfoMvpV.onViewInitErr();
                }

                @Override
                public void onComplete() {
                    super.onComplete();
//                    inboxListInfoMvpV.initView(noteEntityList);
                }
            });

    }

    @Override
    public void toAddItem(AInboxEntity entity) {

    }
    @Override
    public void toDeleteItem(AInboxEntity entity) {

    }


    public class InboxListInfoMvpM implements IBaseListInfoMvpM<T extends AInboxEntity>{
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
