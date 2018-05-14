package ahtewlg7.utimer.mvp;

import android.text.TextUtils;
import android.widget.TextView;

import com.google.common.base.Optional;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.RxActivity;

import org.joda.time.DateTime;
import org.reactivestreams.Subscription;

import java.io.File;
import java.util.concurrent.TimeUnit;

import ahtewlg7.utimer.common.FileSystemAction;
import ahtewlg7.utimer.common.NoteEntityAction;
import ahtewlg7.utimer.entity.INoteEntity;
import ahtewlg7.utimer.enumtype.errcode.NoteEditErrCode;
import ahtewlg7.utimer.exception.NoteEditException;
import ahtewlg7.utimer.util.DateTimeAction;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class NoteEditMvpP {
    public static final String TAG = NoteEditMvpP.class.getSimpleName();

    private INoteEntity noteEntity;
    private Disposable textChangeDisposable;

    private INoteEditMvpV noteEditMvpV;
    private INoteEditMvpM noteEditMvpM;

    public NoteEditMvpP(@NonNull INoteEditMvpV noteEditMvpV){
        this.noteEditMvpV = noteEditMvpV;
        noteEditMvpM      = new NoteEntityAction();
    }

    public void toLoadNote(final String noteId){
        noteEditMvpM.toLoadOrCreateNote(noteId)
            .doOnNext(new Consumer<Optional<INoteEntity>>() {
                @Override
                public void accept(Optional<INoteEntity> iNoteEntityOptional) throws Exception {
                    if(iNoteEntityOptional.isPresent() && iNoteEntityOptional.get().ifFileExist()) {
                        boolean result = noteEditMvpM.toReadContext(iNoteEntityOptional.get());
                        Logcat.d(TAG,"toLoadNote doOnNext : to read conetxt ,result = " + result);
                    }
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .compose(noteEditMvpV.getUiContext().<Optional<INoteEntity>>bindUntilEvent(ActivityEvent.DESTROY))
            .doOnSubscribe(new Consumer<Subscription>() {
                @Override
                public void accept(Subscription subscription) throws Exception {
                    noteEditMvpV.onLoading();
                }
            })
            .subscribe(new MySafeSubscriber<Optional<INoteEntity>>(){
                @Override
                public void onNext(Optional<INoteEntity> optional) {
                    super.onNext(optional);
                    if(!optional.isPresent()) {
                        Logcat.i(TAG,"toLoadNote , onNext ：noteEntity null");
                        noteEditMvpV.onLoadNull();
                        return;
                    }
                    noteEntity = optional.get();
                    if(!TextUtils.isEmpty(noteId) && !noteEntity.ifFileExist()){
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

    public void toDoneNote(){
        DateTime now    = DateTime.now();
        if(TextUtils.isEmpty(noteEntity.getNoteName())) {
            String tmpName = new DateTimeAction().toFormat(now) + "_note";
            noteEntity.setNoteName(tmpName);
        }

        if(TextUtils.isEmpty(noteEntity.getFileRPath())) {
            String fileRPath = new FileSystemAction().getNoteDocRPath() + noteEntity.getNoteName() + File.separator;
            noteEntity.setFileRPath(fileRPath);
        }
        noteEntity.setLastAccessTime(now);

        Logcat.i(TAG,"toDoneNote : " + noteEntity.toString());
        noteEditMvpM.toSaveNote(noteEntity)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new MySafeSubscriber<Boolean>(){
                @Override
                public void onNext(Boolean aBoolean) {
                    super.onNext(aBoolean);
                    if(aBoolean){
                        noteEditMvpV.toSaveContext(noteEntity);
                        noteEditMvpV.onNoteDone(noteEntity);
                    }else{
                        noteEditMvpV.onNoteSaveFail(noteEntity);
                    }
                }
            });

    }

    private void initTextChangeListener(TextView textView) throws RuntimeException{
        if(textView == null){
            Logcat.i(TAG,"initTextChangeListener err");
            throw new NoteEditException(NoteEditErrCode.ERR_EDIT_VIEW_NULL);
        }

        textChangeDisposable = RxTextView.textChangeEvents(textView)
                .debounce(600, TimeUnit.MILLISECONDS)
                .compose(noteEditMvpV.getUiContext().<TextViewTextChangeEvent>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<TextViewTextChangeEvent>() {
                    @Override
                    public void accept(TextViewTextChangeEvent textViewTextChangeEvent) {
                        Logcat.i(TAG, "initTextChangeListener onNext, before = " + textViewTextChangeEvent.before() +
                                ", start = " + textViewTextChangeEvent.start() + ", text = " + textViewTextChangeEvent.text() +
                                ", count = " + textViewTextChangeEvent.count());
                        noteEntity.setRawContext(textViewTextChangeEvent.text().toString());
                        noteEditMvpM.toMdContext(noteEntity);
                        noteEditMvpV.toShowContext(noteEntity);
                    }
                });
    }

    public interface INoteEditMvpM {
        public Flowable<Optional<INoteEntity>> toLoadOrCreateNote(String noteId);
        public boolean toReadContext(INoteEntity noteEntity);
        public void toMdContext(INoteEntity noteEntity);
        public Flowable<Boolean> toSaveNote(INoteEntity noteEntity);
    }

    public interface INoteEditMvpV {
        public @NonNull RxActivity getUiContext();
        public @NonNull TextView getEditView();

        public void onLoading();
        public void onLoadNull();
        public void onLoadUnexist();
        public void onLoadSucc(INoteEntity noteEntity);
        public void onLoadErr(Throwable e);
        public void onLoadComplete();

        public void toShowContext(INoteEntity noteEntity);
        public void toSaveContext(INoteEntity noteEntity);
        public void onNoteDone(INoteEntity noteEntity);
        public void onNoteSaveFail(INoteEntity noteEntity);
    }
}
