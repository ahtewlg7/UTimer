package ahtewlg7.utimer.mvp;

import android.text.TextUtils;
import android.widget.TextView;

import com.google.common.base.Optional;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.RxActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import ahtewlg7.utimer.busevent.NoteEditEndEvent;
import ahtewlg7.utimer.busevent.NoteEditEvent;
import ahtewlg7.utimer.common.EventBusFatory;
import ahtewlg7.utimer.common.FileSystemAction;
import ahtewlg7.utimer.common.NoteEntityAction;
import ahtewlg7.utimer.entity.NoteEntity;
import ahtewlg7.utimer.enumtype.LoadType;
import ahtewlg7.utimer.enumtype.errcode.NoteEditErrCode;
import ahtewlg7.utimer.exception.NoteEditException;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MySafeSubscriber;
import ahtewlg7.utimer.util.MySimpleObserver;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class NoteEditMvpP {
    public static final String TAG = NoteEditMvpP.class.getSimpleName();

    private EventBus eventBus;
    private NoteEntity noteEntity;

    private INoteEditMvpV noteEditMvpV;
    private INoteEditMvpM noteEditMvpM;

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
        Flowable.just(noteEntity)
            .doOnNext(new Consumer<NoteEntity>() {
                @Override
                public void accept(NoteEntity noteEntity) throws Exception {
                    DateTime now    = DateTime.now();
                    if(TextUtils.isEmpty(noteEntity.getTitle()))
                        noteEntity.setTitle(noteEntity.getNoteName());
                    if(TextUtils.isEmpty(noteEntity.getDetail())){
                        String tmp = noteEntity.getRawContext();
                        int endIndex = tmp.length() > 20 ? 20 : tmp.length();
                        noteEntity.setDetail(tmp.substring(0, endIndex));
                    }

                    if(TextUtils.isEmpty(noteEntity.getFileRPath()))
                        noteEntity.setFileRPath(new FileSystemAction().getNoteDocRPath());
                    noteEntity.setLastAccessTime(now);

                    Logcat.i(TAG,"toDoneNote, doOnNext: " + noteEntity.toString());
                }
            })
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
                        noteEditMvpV.toSaveContext(noteEntity);
                        onNoteDone(noteEntity);
                    }else{
                        noteEditMvpV.onNoteSaveFail(noteEntity);
                    }
                }
            });

    }

    private void toLoadNote(Flowable<Optional<String>> noteIdFlowable){
        noteEditMvpM.toLoadOrCreateNote(noteIdFlowable)
                .doOnNext(new Consumer<Optional<NoteEntity>>() {
                    @Override
                    public void accept(Optional<NoteEntity> iNoteEntityOptional) throws Exception {
                        Logcat.i(TAG,"toLoadNote doOnNext");
                        if(iNoteEntityOptional.isPresent()) {
                            boolean result = noteEditMvpM.toReadContext(iNoteEntityOptional.get());
                            Logcat.i(TAG,"toLoadNote doOnNext : to read context ,result = " + result);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(noteEditMvpV.getUiContext().<Optional<NoteEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new MySafeSubscriber<Optional<NoteEntity>>(){
                    @Override
                    public void onSubscribe(Subscription s) {
                        super.onSubscribe(s);
                        noteEditMvpV.onLoading();
                    }

                    @Override
                    public void onNext(Optional<NoteEntity> optional) {
                        super.onNext(optional);
                        if(!optional.isPresent()) {
                            Logcat.i(TAG,"toLoadNote , onNext ：noteEntity null");
                            noteEditMvpV.onLoadNull();
                            return;
                        }
                        noteEntity = optional.get();
                        if(noteEntity.getLoadType() != LoadType.NEW && !noteEntity.isNoteFielExist()){
                            Logcat.i(TAG,"toLoadNote , onNext ：noteEntity no exist");
                            noteEditMvpV.onLoadUnexist();
                        }else{
                            Logcat.i(TAG,"toLoadNote , onNext ：noteEntity load suss");
                            noteEditMvpV.onLoadSucc(noteEntity);
                            initTextChangeListener(noteEditMvpV.getEditView());
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        Logcat.i(TAG,"toLoadNote , onError ：" + t.getMessage());
                        noteEditMvpV.onLoadErr(t);
                    }

                    @Override
                    public void onComplete() {
                        noteEditMvpV.onLoadComplete();
                    }
                });
    }

    private void onNoteDone(NoteEntity noteEntity) {
        NoteEditEndEvent noteEditEndEvent =
                new NoteEditEndEvent(noteEntity.getId(), noteEntity.getLoadType());
        Logcat.i(TAG,"onNoteDone : noteEntity = " + noteEntity.toString() + ",noteEditEndEvent = " + noteEditEndEvent.toString());
        eventBus.post(noteEditEndEvent);
    }

    private void initTextChangeListener(TextView textView) throws RuntimeException{
        if(textView == null){
            Logcat.i(TAG,"initTextChangeListener err");
            throw new NoteEditException(NoteEditErrCode.ERR_EDIT_VIEW_NULL);
        }

        RxTextView.textChangeEvents(textView)
                .debounce(600, TimeUnit.MILLISECONDS)
                .compose(noteEditMvpV.getUiContext().<TextViewTextChangeEvent>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySimpleObserver<TextViewTextChangeEvent>(){
                    @Override
                    public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                        noteEditMvpM.handleTextChange(noteEntity, textViewTextChangeEvent);
                        noteEditMvpV.toShowContext(noteEntity);
                    }
                });
    }

    public interface INoteEditMvpM {
        public Flowable<Optional<NoteEntity>> toLoadOrCreateNote(@NonNull Flowable<Optional<String>> noteIdFlowable);
        public boolean toReadContext(NoteEntity noteEntity);
        public void handleTextChange(NoteEntity noteEntity, TextViewTextChangeEvent textViewTextChangeEvent);
        public Flowable<Boolean> toSaveNote(NoteEntity noteEntity);
    }

    public interface INoteEditMvpV {
        public @NonNull RxActivity getUiContext();
        public @NonNull TextView getEditView();

        public void onLoading();
        public void onLoadNull();
        public void onLoadUnexist();
        public void onLoadSucc(NoteEntity noteEntity);
        public void onLoadErr(Throwable e);
        public void onLoadComplete();

        public void toShowContext(NoteEntity noteEntity);
        public void toSaveContext(NoteEntity noteEntity);
        public void onNoteSaveFail(NoteEntity noteEntity);
    }
}
