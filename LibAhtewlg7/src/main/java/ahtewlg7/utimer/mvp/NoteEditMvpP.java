package ahtewlg7.utimer.mvp;

import com.google.common.base.Optional;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.RxActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import ahtewlg7.utimer.busevent.NoteEditEndEvent;
import ahtewlg7.utimer.busevent.NoteEditEvent;
import ahtewlg7.utimer.common.EventBusFatory;
import ahtewlg7.utimer.common.NoteEntityAction;
import ahtewlg7.utimer.entity.NoteEntity;
import ahtewlg7.utimer.enumtype.LoadType;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MySafeSubscriber;
import ahtewlg7.utimer.view.IRxLifeCycleBindView;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public class NoteEditMvpP {
    public static final String TAG = NoteEditMvpP.class.getSimpleName();

    private EventBus eventBus;
    private NoteEntity noteEntity;

    private INoteEditMvpV noteEditMvpV;
    private NoteEntityAction noteEditMvpM;

    public NoteEditMvpP(@NonNull INoteEditMvpV noteEditMvpV){
        this.noteEditMvpV = noteEditMvpV;
        noteEditMvpM      = new NoteEntityAction();
        eventBus          = EventBusFatory.getInstance().getDefaultEventBus();
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
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onNoteEditEvent(NoteEditEvent event) {
        Logcat.i(TAG,"onNoteEditEvent : " + event.toString());

        toLoadNote(Flowable.just(event.getEoteEntityId()));
    }

    //=============================================================================================
    public void toDoneNote(){
        Logcat.i(TAG,"toDoneNote");
        Flowable.just(noteEntity)
            .flatMap(new Function<NoteEntity, Publisher<Boolean>>() {
                @Override
                public Publisher<Boolean> apply(NoteEntity noteEntity) throws Exception {
                    return noteEditMvpM.toSaveNote(noteEntity);
                }
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new MySafeSubscriber<Boolean>(){
                @Override
                public void onNext(Boolean aBoolean) {
                    super.onNext(aBoolean);
                    if(aBoolean){
                        postDoneEvent(noteEntity);
                        noteEditMvpV.onNoteSaveSucc(noteEntity);
                    }else{
                        noteEditMvpV.onNoteSaveFail(noteEntity);
                    }
                }
            });

    }

    private void toLoadNote(Flowable<Optional<String>> noteIdFlowable){
        noteEditMvpM.toLoadOrCreateNote(noteIdFlowable)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(((RxActivity)noteEditMvpV.getRxLifeCycleBindView()).<Optional<NoteEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new MySafeSubscriber<Optional<NoteEntity>>(){
                    @Override
                    public void onSubscribe(Subscription s) {
                        super.onSubscribe(s);
                        noteEditMvpV.onLoadStart();
                    }

                    @Override
                    public void onNext(Optional<NoteEntity> optional) {
                        super.onNext(optional);
                        if(!optional.isPresent()) {
                            Logcat.i(TAG,"toLoadNote , onNext ：noteEntity null");
                            noteEditMvpV.onLoadUnexist();
                            return;
                        }
                        noteEntity = optional.get();
                        if(noteEntity.getLoadType() != LoadType.NEW
                                && !noteEditMvpM.isMdFileExist(noteEntity)){
                            noteEntity.setNoteFileExist(false);
                            Logcat.i(TAG,"toLoadNote , onNext ：noteEntity no exist");
                            noteEditMvpV.onLoadUnexist();
                        }else {
                            Logcat.i(TAG,"toLoadNote , onNext ：load suss , " + noteEntity.toString());
                            if(noteEntity.getLoadType() == LoadType.NEW)
                                noteEntity.setNoteFileExist(false);
                            else
                                noteEntity.setNoteFileExist(true);
                            noteEditMvpV.onLoadSucc(noteEntity);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        Logcat.i(TAG,"toLoadNote , onError ：" + t.getMessage());
                        noteEditMvpV.onLoadErr(t);
                    }

                    @Override
                    public void onComplete() {
                        noteEditMvpV.onLoadEnd();
                    }
                });
    }

    private void postDoneEvent(NoteEntity noteEntity) {
        NoteEditEndEvent noteEditEndEvent = new NoteEditEndEvent(noteEntity.getId(), noteEntity.getLoadType());
        Logcat.i(TAG,"onNoteDone : noteEntity = " + noteEntity.toString() + ",noteEditEndEvent = " + noteEditEndEvent.toString());
        eventBus.post(noteEditEndEvent);
    }

    public interface INoteEditMvpM {
        public Flowable<Optional<NoteEntity>> toLoadOrCreateNote(@NonNull Flowable<Optional<String>> noteIdFlowable);
        public Flowable<Boolean> toSaveNote(NoteEntity noteEntity);
    }

    public interface INoteEditMvpV extends IRxLifeCycleBindView {
        public void onLoadStart();
        public void onLoadUnexist();
        public void onLoadSucc(@NonNull NoteEntity noteEntity);
        public void onLoadErr(Throwable e);
        public void onLoadEnd();

        public void onNoteSaveSucc(NoteEntity noteEntity);
        public void onNoteSaveFail(NoteEntity noteEntity);
    }
}
