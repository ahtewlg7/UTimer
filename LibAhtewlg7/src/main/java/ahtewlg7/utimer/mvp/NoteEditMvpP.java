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
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class NoteEditMvpP {
    public static final String TAG = NoteEditMvpP.class.getSimpleName();

    private INoteEntity noteEntity;

    private Disposable textChagneDisposable;

    private INoteEditMvpV noteEditMvpV;
    private INoteEditMvpM noteEditMvpM;

    public NoteEditMvpP(@NonNull INoteEditMvpV noteEditMvpV){
        this.noteEditMvpV = noteEditMvpV;
        noteEditMvpM      = new NoteEntityAction();
    }

    public void toLoadNote(String noteId){
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
                        Logcat.i(TAG,"toLoadNote , doOnNext ：noteEntity null");
                        noteEditMvpV.onLoadNull();
                        return;
                    }
                    noteEntity = optional.get();
                    if(!noteEntity.ifFileExist()){
                        Logcat.i(TAG,"toLoadNote , doOnNext ：noteEntity no exist");
                        noteEditMvpV.onLoadUnexist();
                    }else{
                        noteEditMvpV.onContextShow(noteEntity);
                        initTextChangeListener(noteEditMvpV.getEditView());
                    }
                }

                @Override
                public void onError(Throwable t) {
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

        noteEditMvpM.toSaveNote(noteEntity)
            .observeOn(AndroidSchedulers.mainThread())
            .filter(new Predicate<Boolean>() {
                @Override
                public boolean test(Boolean aBoolean) throws Exception {
                    return aBoolean;
                }
            })
            .subscribe(new MySafeSubscriber<Boolean>(){
                @Override
                public void onNext(Boolean aBoolean) {
                    super.onNext(aBoolean);
                    noteEditMvpV.toSaveContext(noteEntity);
                    noteEditMvpV.onNoteDone(noteEntity);
                }
            });

    }

    private void initTextChangeListener(TextView textView) throws RuntimeException{
        if(textView == null){
            Logcat.i(TAG,"initTextChangeListener err");
            throw new NoteEditException(NoteEditErrCode.ERR_EDIT_VIEW_NULL);
        }

        textChagneDisposable = RxTextView.textChangeEvents(textView)
                .compose(noteEditMvpV.getUiContext().<TextViewTextChangeEvent>bindUntilEvent(ActivityEvent.DESTROY))
                .map(new Function<TextViewTextChangeEvent, INoteEntity>() {
                    @Override
                    public INoteEntity apply(TextViewTextChangeEvent textViewTextChangeEvent) throws Exception {
                        Logcat.i(TAG,"textChangeEvents before:" + textViewTextChangeEvent.before() +
                                ",start:" + textViewTextChangeEvent.start() + ",text:" + textViewTextChangeEvent.text() +
                                ",count:" + textViewTextChangeEvent.count());
                        noteEntity.setMdContext(textViewTextChangeEvent.text().toString());
                        noteEditMvpM.toMdContext(noteEntity);
                        return noteEntity;
                    }
                })
                .subscribe(new Consumer<INoteEntity>() {
                    @Override
                    public void accept(INoteEntity noteEntity) throws Exception {
                        noteEditMvpV.onContextShow(noteEntity);
                    }
                });
    }

    public void toStopTextChangeListener(){
        if(textChagneDisposable != null && !textChagneDisposable.isDisposed())
            textChagneDisposable.dispose();
    }

    public interface INoteEditMvpM {
        public Flowable<Optional<INoteEntity>> toLoadOrCreateNote(String noteId);
        public boolean toReadContext(INoteEntity noteEntity);
        public void toMdContext(INoteEntity noteEntity);
        public Flowable<Boolean> toSaveNote(INoteEntity noteEntity);
    }

    public interface INoteEditMvpV {
        public @NonNull
        RxActivity getUiContext();
        public @NonNull TextView getEditView();

        public void onLoading();
        public void onLoadNull();
        public void onLoadUnexist();
        public void onLoadErr(Throwable e);
        public void onLoadComplete();

        public void onContextShow(INoteEntity noteEntity);
        public void toSaveContext(INoteEntity noteEntity);
        public void onNoteDone(INoteEntity noteEntity);
    }
}
