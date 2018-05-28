package ahtewlg7.utimer.mvp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ahtewlg7.utimer.busevent.NoteDeleteEvent;
import ahtewlg7.utimer.common.EventBusFatory;
import ahtewlg7.utimer.common.NoteEntityAction;
import ahtewlg7.utimer.entity.NoteEntity;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

public class NoteContextSaveMvpP {
    public static final String TAG = NoteContextSaveMvpP.class.getSimpleName();

    private EventBus eventBus;
    private INoteSaveMvpV noteSaveMvpV;
    private INoteContextSaveMvpM noteSaveMvpM;

    public NoteContextSaveMvpP(@NonNull INoteSaveMvpV noteSaveMvpV){
        this.noteSaveMvpV = noteSaveMvpV;
        noteSaveMvpM      = new NoteEntityAction();
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
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNoteDeleteEvent(NoteDeleteEvent event) {
        Logcat.i(TAG,"onNoteDeleteEvent  : noteDeleteEvent = " + event.toString());
        if(event.getNoteFilePath().isPresent()) {
            Logcat.i(TAG,"onNoteDeleteEvent : noteDeleteEvent is present");
            noteSaveMvpM.toDeleteContext(event.getNoteFilePath().get())
                .subscribe(new MySafeSubscriber<Boolean>(){
                    @Override
                    public void onNext(Boolean aBoolean) {
                        super.onNext(aBoolean);
                        Logcat.i(TAG,"onNoteDeleteEvent : aBoolean = " + aBoolean);
                    }
                });
        }
    }

    //=======================================EventBus================================================
    public void toSaveNoteContext(final NoteEntity noteEntity){
        noteSaveMvpM.toSaveContext(noteEntity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new MySafeSubscriber<Boolean>(){
                @Override
                public void onNext(Boolean aBoolean) {
                    super.onNext(aBoolean);
                    if(aBoolean)
                        noteSaveMvpV.onNoteSaveSucc(noteEntity);
                    else
                        noteSaveMvpV.onNoteSaveFailed(noteEntity);
                }

                @Override
                public void onError(Throwable t) {
                    noteSaveMvpV.onNoteSaveErr(noteEntity, t);
                }

                @Override
                public void onComplete() {
                    noteSaveMvpV.onNoteSaveComplete(noteEntity);
                }
            });
    }

    public interface INoteSaveMvpV {
        public void onNoteSaveFailed(NoteEntity noteEntity);
        public void onNoteSaveSucc(NoteEntity noteEntity);
        public void onNoteSaveErr(NoteEntity noteEntity, Throwable e);
        public void onNoteSaveComplete(NoteEntity noteEntity);
    }

    public interface INoteContextSaveMvpM {
        public Flowable<Boolean> toSaveContext(NoteEntity noteEntity);
        public Flowable<Boolean> toDeleteContext(String filePath);
    }
}
