package ahtewlg7.utimer.mvp;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.reactivestreams.Subscription;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import ahtewlg7.utimer.busevent.NoteEditEndEvent;
import ahtewlg7.utimer.busevent.NoteEditEvent;
import ahtewlg7.utimer.common.EventBusFatory;
import ahtewlg7.utimer.common.NoteEntityAction;
import ahtewlg7.utimer.entity.NoteEntity;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MySafeSubscriber;
import ahtewlg7.utimer.util.MySimpleObserver;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class NoteRecyclerViewMvpP implements IRecyclerMvpP {
    public static final String TAG = NoteRecyclerViewMvpP.class.getSimpleName();

    private EventBus eventBus;
    private List<NoteEntity> noteEntityList;
    private INoteRecyclerViewMvpV noteRecylerViewMvpV;
    private INoteRecyclerViewMvpM noteRecyclerViewMvpM;

    public NoteRecyclerViewMvpP(INoteRecyclerViewMvpV noteRecylerViewMvpV){
        this.noteRecylerViewMvpV    = noteRecylerViewMvpV;
        noteEntityList              = Lists.newArrayList();
        noteRecyclerViewMvpM        = new NoteEntityAction();
        eventBus                    = EventBusFatory.getInstance().getDefaultEventBus();
    }

    //=======================================EventBus================================================
    public void toRegisterEventBus(){
        Logcat.i(TAG,"toRegisterEventBus");
        if(eventBus != null && !eventBus.isRegistered(this))
            eventBus.register(this);
    }
    public void toUnregisterEventBus(){
        Logcat.i(TAG,"toUnregisterEventBus");
        if(eventBus != null && eventBus.isRegistered(this))
            eventBus.unregister(this);
    }

    //EventBus callback
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNoteEditEndEvent(NoteEditEndEvent event) {
        Logcat.i(TAG,"onNoteEditEndEvent  : noteEditEndEvent = " + event.toString());

        onNoteEditEnd(Flowable.just(event).map(new Function<NoteEditEndEvent, Optional<String>>() {
            @Override
            public Optional<String> apply(NoteEditEndEvent noteEditEndEvent) throws Exception {
                return noteEditEndEvent.getNoteEntityId();
            }
        }));
    }

    //=============================================================================================
    @Override
    public void loadAllData() {
        noteRecyclerViewMvpM.loadAllEntity()
                .compose(noteRecylerViewMvpV.getUiContext().<Optional<NoteEntity>>bindUntilEvent(FragmentEvent.DESTROY))
                .filter(new Predicate<Optional<NoteEntity>>() {
                    @Override
                    public boolean test(Optional<NoteEntity> noteEntityOptional) throws Exception {
                        return noteEntityOptional.isPresent();
                    }
                })
                .map(new Function<Optional<NoteEntity>, NoteEntity>() {
                    @Override
                    public NoteEntity apply(Optional<NoteEntity> noteEntityOptional) throws Exception {
                        return noteEntityOptional.get();
                    }
                })
                .sorted(Ordering.natural().onResultOf(new com.google.common.base.Function<NoteEntity, Comparable>() {
                    @Override
                    @ParametersAreNonnullByDefault
                    public Comparable apply(NoteEntity input) {
                        return input.getLastAccessTime();
                    }
                }).reverse())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySafeSubscriber<NoteEntity>(){
                    @Override
                    public void onSubscribe(Subscription s) {
                        super.onSubscribe(s);
                        noteEntityList.clear();
                        noteRecylerViewMvpV.onRecyclerViewInitStart();
                    }

                    @Override
                    public void onNext(NoteEntity noteEntity) {
                        super.onNext(noteEntity);
                        noteEntityList.add(noteEntity);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        noteRecylerViewMvpV.onRecyclerViewInitErr();
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        noteRecylerViewMvpV.initRecyclerView(noteEntityList);
                    }
                });

    }

    public void toCreateNote(){
        toStartEditNote(Observable.just(Optional.<NoteEntity>absent()));
    }

    public void toEditNote(@NonNull NoteEntity noteEntity){
        toStartEditNote(Observable.just(Optional.of(noteEntity)));
    }

    public void toDeleteNoteItem(int index){
        noteRecyclerViewMvpM.deleteEntity(Flowable.just(Optional.of(noteEntityList.get(index))))
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySafeSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        super.onNext(aBoolean);
                        if(aBoolean)
                            noteRecylerViewMvpV.onNoteDeleteSucc();
                        else
                            noteRecylerViewMvpV.onNoteDeleteFail();
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        noteRecylerViewMvpV.onNoteDeleteErr();
                    }
                });
    }

    private void toStartEditNote(@NonNull Observable<Optional<NoteEntity>> noteObservable){
        noteObservable.filter(new Predicate<Optional<NoteEntity>>() {
                @Override
                public boolean test(Optional<NoteEntity> stringOptional) throws Exception {
                    return eventBus != null;
                }
            })
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(new MySimpleObserver<Optional<NoteEntity>>() {
                @Override
                public void onNext(Optional<NoteEntity> noteEntityOptional) {
                    if(noteEntityOptional.isPresent()) {
                        NoteEntity noteEntity = noteEntityOptional.get();
                        Logcat.i(TAG,"toStartEditNote , onNext : noteEntity = " + noteEntity.toString());
                        eventBus.postSticky(new NoteEditEvent(noteEntity.getId()));
                        noteEntityList.remove(noteEntity);
                    }else {
                        Logcat.i(TAG,"toStartEditNote , onNext : noteEntity is not present");
                        eventBus.postSticky(new NoteEditEvent());
                    }
                }
                @Override
                public void onComplete() {
                    Logcat.i(TAG,"toStartEditNote , onComplete");
                    noteRecylerViewMvpV.toStartNoteEditActivity();
                }
            });
    }

    private void onNoteEditEnd(@NonNull Flowable<Optional<String>> idObservable){
        noteRecyclerViewMvpM.loadEntity(idObservable)
                .compose(noteRecylerViewMvpV.getUiContext().<Optional<NoteEntity>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySafeSubscriber<Optional<NoteEntity>>(){
                    @Override
                    public void onNext(Optional<NoteEntity> noteEntity) {
                        super.onNext(noteEntity);
                        if(noteEntity.isPresent())
                            noteEntityList.add(0, noteEntity.get());
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        if(!noteEntityList.isEmpty())
                            noteRecylerViewMvpV.resetRecyclerView(noteEntityList);
                    }
                });
    }

    public void toGtdProject(NoteEntity noteEntity){
    }

    public interface INoteRecyclerViewMvpM extends IBaseRecyclerViewMvpM<NoteEntity>{
    }

    public interface INoteRecyclerViewMvpV extends IRecyclerViewMvpV<NoteEntity>{
        public @NonNull RxFragment getUiContext();
        public void toStartNoteEditActivity();

        public void onNoteDeleteSucc();
        public void onNoteDeleteFail();
        public void onNoteDeleteErr();
    }
}
